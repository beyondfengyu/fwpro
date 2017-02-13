package com.cus.cms.common.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cus.cms.common.frame.mvc.ContextUtil;
import com.cus.cms.common.util.BlankUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.ZParams.Aggregate;
import redis.clients.util.Pool;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;


/**
 * @author Andy  2016/3/14.
 * @description
 */
@Component("redisEnv")
public class RedisEnv {
    private Logger m_logger = LoggerFactory.getLogger(RedisEnv.class);
    private Pool<Jedis> masterPool;//当前主连接池
    private Map<String, RedisEnv> otherRedisEnv;//同步连接池
    private String configPath;
    private boolean isUse_slave = false;


    //3小时过期
    public int EXPIRE_TIME = 60 * 60 * 3;

    /**
     * 获取redis连接
     *
     * @return
     * @throws Exception
     */
    public Jedis borrowConn()
            throws Exception {
        if (masterPool instanceof JedisSentinelPool) {
            int numWaiters = masterPool.getNumWaiters();
            int numActive =  masterPool.getNumActive();
            int numIdel = masterPool.getNumIdle();
            m_logger.debug("numWaiters:" + numWaiters + ",numActive:" + numActive + ",numIdel:" + numIdel);
        }
        return (Jedis) masterPool.getResource();
    }

    /**
     * 返回redis连接
     *
     * @param jedis 数据库连接
     * @throws Exception
     */
    public void returnConn(Jedis jedis) throws Exception {
        masterPool.returnResource(jedis);
    }

    /**
     * 初始化redis连接池
     *
     * @throws Exception
     */
    public void init()
            throws Exception {
        FileInputStream fin = FileUtils.openInputStream(new File(ContextUtil.getContextPath()+getConfigPath()));
        Properties dbCfg = new Properties();
        dbCfg.loadFromXML(fin);
        int maxActive = Integer.parseInt(dbCfg.getProperty("rds_maxActive"));
        int minIdle = Integer.parseInt(dbCfg.getProperty("rds_minIdle"));
        int maxIdle = Integer.parseInt(dbCfg.getProperty("rds_maxIdle"));
        int maxWait = Integer.parseInt(dbCfg.getProperty("rds_maxWait"));
        int timeout = Integer.parseInt(dbCfg.getProperty("rds_timeout"));

        // 初始化连接池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnReturn(true);
        m_logger.info("init JedisPoolConfig：maxActive=" + maxActive + ",minIdle=" + minIdle
                + ",maxIdle=" + maxIdle + ",maxWait=" + maxWait + ",testOnBorrow="
                + config.getTestOnBorrow());

        // 初始化主服务器连接池
        String rds_master_server = dbCfg.getProperty("rds_master_server");
        String rds_sentinel_servers = dbCfg.getProperty("rds_sentinel_servers");

        m_logger.info("init master JedisPool...");
        if (BlankUtil.isBlank(rds_sentinel_servers)) {
            masterPool = getJedisPool(rds_master_server, config, timeout);
        } else {
            String masterName = dbCfg.getProperty("master_name");
            Set<String> sentinels = getSentinels(rds_sentinel_servers);
            m_logger.info("init JedisSentinelPool masterName:" + masterName + ",sentinels" + sentinels);
            masterPool = getJedisSentinelPool(rds_sentinel_servers, config, masterName, sentinels, timeout);
            m_logger.info("init JedisSentinelPool masterPool:" + masterPool);
        }


        //是否使用从库
        if (isUse_slave) {
            // 初始化从服务器连接池
            String rds_slave_server = dbCfg.getProperty("rds_slave_server");
            m_logger.info("init slave JedisPool...");
            otherRedisEnv = initOtherRedisEvn(rds_slave_server, config, timeout);
        }
    }

    private Set<String> getSentinels(String str) throws Exception {
        Set<String> set = new HashSet<String>();
        String[] sentinels = str.split("\n");
        for (String s : sentinels) {
            s = s.trim();
            if (s.equals("")) {
                continue;
            }
            m_logger.info("sentinels:" + s);
            String[] tmp = s.split(":", 2);
            if (tmp.length != 2) {
                throw new Exception("if \"load_balance_strategy\" is false then the value of \"YYPClientPoolProxy server\" must be ip:port");
            }
            set.add(s.trim());
        }
        return set;
    }

    /**
     * 初始化其它redisEvn
     *
     * @param servers
     * @param config
     * @param timeout
     * @return
     * @throws Exception
     */
    private Map<String, RedisEnv> initOtherRedisEvn(String servers,
                                                    JedisPoolConfig config, int timeout)
            throws Exception {
        if (servers == null || servers.isEmpty()) {
            return new HashMap<String, RedisEnv>();
        }
        String[] svArry = servers.trim().split("\n");
        Map<String, RedisEnv> pools = new HashMap<String, RedisEnv>();
        for (String sv : svArry) {
            if (!BlankUtil.isBlank(sv)) {
                RedisEnv slave = new RedisEnv();
                slave.masterPool = getJedisPool(sv, config, timeout);
                if (slave.masterPool != null) {
                    pools.put(sv, slave);
                }
            }
        }
        return pools;
    }

    /**
     * 获取单个jedis连接池
     *
     * @param sv
     * @param config
     * @param timeout
     * @return
     * @throws Exception
     */
    private JedisPool getJedisPool(String sv, JedisPoolConfig config,
                                   int timeout)
            throws Exception {
        String[] svSp = sv.split(":");
        if (svSp.length < 2) {
            return null;
        }
        String host = svSp[0];
        int port = Integer.parseInt(svSp[1]);
        JedisPool p = new JedisPool(config, host, port, timeout);// 线程数量限制，IP地址，端口，超时时间
        m_logger.info("connected redis cache server：" + sv);
        return p;
    }

    /**
     * 获取单个jedis连接池
     *
     * @param sv
     * @param config
     * @param timeout
     * @return
     * @throws Exception
     */
    private JedisSentinelPool getJedisSentinelPool(String sv, JedisPoolConfig config, String masterName, Set<String> sentinels,
                                                   int timeout)
            throws Exception {
        String[] svSp = sv.split(":");
        if (svSp.length < 2) {
            return null;
        }
        JedisSentinelPool p = new JedisSentinelPool(masterName, sentinels, config, timeout);// 线程数量限制，IP地址，端口，超时时间
        m_logger.info("connected redis cache server：" + sv);
        return p;
    }


    /**
     * 设置过期
     *
     * @param key
     * @param seconds
     * @throws Exception
     */
    public Long expire(String key, int seconds)
            throws Exception {
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = borrowConn();
            result = jedis.expire(key, seconds);
            addSyncTask(key, seconds);

        } catch (Exception e) {
            m_logger.error("jedis expire  access error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 增加同步任务
     *
     * @param param 参数,最后一个为方法名
     */
    private void addSyncTask(Object... param) {
        //如果没有从的缓存服务器,那么就不需要同步
        if (!isUse_slave) {
            return;
        }
    }

    /**
     * 保存到redis并设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     * @throws Exception
     */
    public void set(String key, String value, int seconds)
            throws Exception {
        if (value == null) return;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            try {
                jedis.set(key, value);
            } catch (Exception e) {
                m_logger.error("jedis set access error", e);
            }
            if (seconds >= 0) {
                jedis.expire(key, seconds);
            }
            addSyncTask(key, value, seconds);

        } catch (Exception e) {
            m_logger.error("jedis set expire error", e);
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 将字符串值 value 关联到 key
     *
     * @param key
     * @param value
     * @throws Exception
     */
    public void set(String key, String value)
            throws Exception {
        if (value == null) return;
        set(key, value, -1);
    }

    /**
     * 保存到redis并设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     * @throws Exception
     */
    public void setEx(String key, String value, int seconds) throws Exception {
        if (value == null) return;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            //此方法相当于set和expire两个方法;不同之处是， SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
            jedis.setex(key, seconds, value);
            addSyncTask(key, value, seconds);
        } catch (Exception e) {
            m_logger.error("jedis setEx access error", e);
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 删除给定的一个 key,不存在的 key 会被忽略
     *
     * @param key
     * @throws Exception
     */
    public void delete(String... key)
            throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            jedis.del(key);
            Object param = (Object) key;
            addSyncTask(param);

        } catch (Exception e) {
            m_logger.error("jedis delete access error", e);
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 返回 key 所关联的字符串值
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String get(String key)
            throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            m_logger.debug("getString redisCache ,key=" + key);
            return jedis.get(key);
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 匹配key
     *
     * @param pattern 格式支持?*等通配
     * @return
     * @throws Exception
     */
    public Set<String> getKeys(String pattern) throws Exception {
        Set<String> sets = new HashSet<String>();
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            sets = jedis.keys(pattern);
        } finally {
            returnConn(jedis);
        }
        return sets;
    }

    public Long sadd(String key, String... members) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            return jedis.sadd(key, members);
        } catch (Exception e) {
            m_logger.error("sadd_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Long scard(String key) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            return jedis.scard(key);
        } catch (Exception e) {
            m_logger.error("scard_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Set<String> smembers(String key) throws Exception {
        Set<String> set = new HashSet<String>();
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            set = jedis.smembers(key);
        } catch (Exception e) {
            m_logger.error("smembers_error", e);
        } finally {
            returnConn(jedis);
        }
        return set;
    }

    public String srandmember(String key) throws Exception {
        //String
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            return jedis.srandmember(key);
        } catch (Exception e) {
            m_logger.error("smembers_error", e);
        } finally {
            returnConn(jedis);
        }
        return null;
    }

    public Long srem(String key, String... members) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            return jedis.srem(key, members);
        } catch (Exception e) {
            m_logger.error("smembers_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    /**
     * 添加有序集合
     *
     * @param key
     * @param score
     * @param member
     * @return
     * @throws Exception
     */
    public Long zadd(String key, double score, String member) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zadd(key, score, member);
            addSyncTask(key, score, member);
        } catch (Exception e) {
            m_logger.error("zadd_error", e);
            return -1L;
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public void zaddMemberScore(String key, Map<String, Double> memberScores) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String member : memberScores.keySet()) {
                p.zadd(key, memberScores.get(member), member);
            }
            p.sync();
        } catch (Exception e) {
            m_logger.error("zadd_error", e);
        } finally {
            returnConn(jedis);
        }
    }

    public void zaddMemberScore(String key, Map<String, Double> memberScores, int ttl) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String member : memberScores.keySet()) {
                p.zadd(key, memberScores.get(member), member);
            }
            p.sync();
            if (ttl > 0) {
                jedis.expire(key, ttl);
            }
        } catch (Exception e) {
            m_logger.error("zadd_error", e);
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 添加有序集合
     *
     * @param key
     * @param scoreMembers
     * @return
     * @throws Exception
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zadd(key, scoreMembers);
            addSyncTask(key, scoreMembers);
        } catch (Exception e) {
            m_logger.error("zadd_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Long zadd(String key, Map<String, Double> scoreMembers, int ttl) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zadd(key, scoreMembers);
            if (ttl > 0) {
                jedis.expire(key, ttl);
            }
            addSyncTask(key, scoreMembers, ttl);
        } catch (Exception e) {
            m_logger.error("zadd_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Long zcard(String key) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zcard(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("zcard_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Long ttl(String key) throws Exception {
        Long expired = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            expired = jedis.ttl(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("ttl_error", e);
        } finally {
            returnConn(jedis);
        }
        return expired;
    }

    /**
     * zincrby
     *
     * @param key
     * @param score
     * @param member
     * @return
     * @throws Exception
     */
    public Double zincrby(String key, double score, String member) throws Exception {
        Double count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zincrby(key, score, member);
            addSyncTask(key, score, member);
        } catch (Exception e) {
            m_logger.error("zincrby_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public void zincrby(String key, Map<String, Double> memberScores) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String member : memberScores.keySet()) {
                p.zincrby(key, memberScores.get(member), member);
            }
            p.sync();
        } catch (Exception e) {
            m_logger.error("zincrby_error", e);
        } finally {
            returnConn(jedis);
        }
    }

    public boolean sismember(String key, String member) throws Exception {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            flag = jedis.sismember(key, member);
        } catch (Exception e) {
            m_logger.error("sismember_error", e);
        } finally {
            returnConn(jedis);
        }
        return flag;
    }

    /**
     * 统计有序集合
     *
     * @param key
     * @param min
     * @param max
     * @return
     * @throws Exception
     */
    public Long zCount(String key, double min, double max) throws Exception {
        Long count = 0l;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zcount(key, min, max);
        } catch (Exception e) {
            m_logger.error("zaddCount_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    /**
     * 统计有序集合
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Long zCountAll(String key) throws Exception {
        return zCount(key, 0, Double.MAX_VALUE);
    }

    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * <p/>
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     * <p/>
     * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
     * <p/>
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度:
     * O(log(N))
     * 返回值:
     * 如果 member 是有序集 key 的成员，返回 member 的排名。
     * 如果 member 不是有序集 key 的成员，返回 nil 。
     *
     * @param key
     * @param member
     * @return
     * @throws Exception
     */
    public Long zrank(String key, String member) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zrank(key, member);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public Long zrevrank(String key, String member) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.zrevrank(key, member);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * 其中成员的位置按 score 值递增(从小到大)来排序。
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
     * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 超出范围的下标并不会引起错误。
     * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。
     * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。
     * 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * 可用版本：
     * >= 1.2.0
     * 时间复杂度:
     * O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     * 返回值:
     * 指定区间内，带有 score 值(可选)的有序集成员的列表。
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Set<String> zrange(String key, int start, int end) throws Exception {
        Set<String> results = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.zrange(key, start, end);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    public double zscore(String key, String member) throws Exception {
        double results = 0;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Double score = jedis.zscore(key, member);
            if (score != null) {
                results = score.doubleValue();
            }
        } catch (Exception e) {
            m_logger.error("", e);
            throw e;
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    public Map<String, Object> zscore(String key, List<String> members) throws Exception {
        Jedis jedis = null;
        Map<String, Object> prop = new HashMap<String, Object>();
        try {
            jedis = borrowConn();

            Pipeline p = jedis.pipelined();
            for (String member : members) {
                p.zscore(key, member);
            }
            List<Object> list = p.syncAndReturnAll();
            for (int i = 0; i < list.size(); i++) {
                String member = members.get(i);
                String val = String.valueOf(list.get(i));
                if (BlankUtil.isBlank(val)) {
                    val = "0";
                }
                prop.put(member, val);
            }
        } finally {
            returnConn(jedis);
        }
        return prop;
    }

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * 有序集成员按 score 值递增(从小到大)次序排列。具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，
     * 注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
     * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。
     * 该选项自 Redis 2.0 版本起可用。
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Set<String> zrangeByScore(String key, int start, int end) throws Exception {
        Set<String> results = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.zrangeByScore(key, 0, 1, start, end);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    /**
     * 返回有序集 key 中，指定区间内的成员。
     * 其中成员的位置按 score 值递增(从大到小)来排序。
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
     * 如果你需要成员按 score 值递减(从小到大)来排列，请使用 ZRANGE 命令。
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * 超出范围的下标并不会引起错误。
     * 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZREVRANGE 命令只是简单地返回一个空列表。
     * 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。
     * 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * 可用版本：
     * >= 1.2.0
     * 时间复杂度:
     * O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     * 返回值:
     * 指定区间内，带有 score 值(可选)的有序集成员的列表。
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public Set<String> zrevrange(String key, int start, int end) throws Exception {
        Set<String> results = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.zrevrange(key, start, end);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end) throws Exception {
        Set<Tuple> results = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.zrangeWithScores(key, start, end);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) throws Exception {
        Set<Tuple> results = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.zrevrangeWithScores(key, start, end);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    /**
     * ZREM key member [member ...]
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     * 当 key 存在但不是有序集类型时，返回一个错误。
     * 在 Redis 2.4 版本以前， ZREM 每次只能删除一个元素。
     *
     * @param key
     * @param members
     * @return
     * @throws Exception
     */
    public long zrem(String key, String... members) throws Exception {
        long results = 0;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            for (String member : members) {
                results = jedis.zrem(key, member);
                if (results == 0) {
                    m_logger.info("zrem result is 0! key:" + key);
                }
            }
            addSyncTask(key, members);
        } catch (Exception e) {
            m_logger.error("zrem_error", e);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    /**
     * 检查给定 key 是否存在。
     * 可用版本：
     * >= 1.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 若 key 存在，返回 1 ，否则返回 0 。
     *
     * @param key
     * @return
     * @throws Exception
     */
    public boolean exists(String key) throws Exception {
        boolean results = false;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            results = jedis.exists(key);
        } finally {
            returnConn(jedis);
        }
        return results;
    }

    public void setProperty(String key, Map prop) throws Exception {
        setProperty(key, prop, -1);
    }

    public void setProperty(String key, Map prop, int seconds) throws Exception {
        Jedis jedis = null;
        try {
            if (prop == null || prop.size() == 0) {
                return;
            }
            jedis = borrowConn();
            try {
                jedis.hmset(key, prop);
            } catch (Exception e) {
                m_logger.error("SetProperty Redis Error,key=" + key, e);
            }

            if (seconds != -1) {
                jedis.expire(key, seconds);
            }
            addSyncTask(key, prop, seconds);
        } catch (Exception e) {
            m_logger.error("expire Property Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }

    }

    public List<String> listPropertyValue(String key, String... fields) throws Exception {
        Jedis jedis = null;
        List<String> values = new ArrayList<String>();
        try {
            jedis = borrowConn();
            values = jedis.hmget(key, fields);
        } catch (Exception e) {
            m_logger.error("GetProperty Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return values;
    }

    public String getPropertyValue(String key, String field) throws Exception {
        List<String> list = listPropertyValue(key, field);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Long incr(String key) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.incr(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("incr Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long incr(String key, int ttl) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.incr(key);
            if (ttl > 0) {
                jedis.expire(key, ttl);
            }
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("incr Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long incrBy(String key, long integer) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.incrBy(key, integer);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("incr Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long decr(String key) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.decr(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("decr Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long decrBy(String key, long integer) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.decrBy(key, integer);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("decr Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long hlen(String key) throws Exception {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = borrowConn();
            result = jedis.hlen(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("hlen Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Long hset(String key, String field, String value) throws Exception {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = borrowConn();
            result = jedis.hset(key, field, value);
            addSyncTask(key, field);
        } catch (Exception e) {
            m_logger.error("hmset Redis Error,key=" + key, e);
            return -1L;
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public String hget(String key, String field) throws Exception {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = borrowConn();
            result = jedis.hget(key, field);
            addSyncTask(key, field);
        } catch (Exception e) {
            m_logger.error("hmset Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public String hmset(String key, Map<String, String> map) throws Exception {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Jedis jedis = null;
        String result = "";
        try {
            jedis = borrowConn();
            result = jedis.hmset(key, map);
            if (!"ok".equalsIgnoreCase(result)) {
                m_logger.error("hmset Redis result is " + result + ",key=" + key + ",map=" + map);
            }
            addSyncTask(key, map);
        } catch (Exception e) {
            m_logger.error("hmset Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public String hmset(String key, Map<String, String> map, int ttl) throws Exception {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Jedis jedis = null;
        String result = "";
        try {
            jedis = borrowConn();
            result = jedis.hmset(key, map);
            if (ttl > 0) {
                jedis.expire(key, ttl);
            }
            if (!"ok".equalsIgnoreCase(result)) {
                m_logger.error("hmset Redis result is " + result + ",key=" + key + ",map=" + map);
            }
            addSyncTask(key, map, ttl);
        } catch (Exception e) {
            m_logger.error("hmset Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public List<String> hmget(String key, String... fields) throws Exception {
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = borrowConn();
            result = jedis.hmget(key, fields);
            addSyncTask(key, fields);
        } catch (Exception e) {
            m_logger.error("hmget Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public long hdel(String key, String field) throws Exception {
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = borrowConn();
            result = jedis.hdel(key, field);
            if (result == 0) {
                m_logger.error("hdel Redis result is 0 ! key=" + key + " field:" + field);
            }
            addSyncTask(key, field);
        } catch (Exception e) {
            m_logger.error("hdel Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public boolean hexists(String key, String field) throws Exception {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = borrowConn();
            result = jedis.hexists(key, field);
            addSyncTask(key, field);
        } catch (Exception e) {
            m_logger.error("hexists Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Map<String, String> hgetAll(String key) throws Exception {
        Jedis jedis = null;
        Map<String, String> result = null;
        try {
            jedis = borrowConn();
            result = jedis.hgetAll(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("hgetAll Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public Set<String> hkeys(String key) throws Exception {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = borrowConn();
            result = jedis.hkeys(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("hkeys Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public void removeOrderMapPropertyAll(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            jedis.del(key, key + "|fileds");
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("removeOrderMapPropertyAll Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
    }

    public List<String> getOrderMapProperty(String key, int star, int end) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Set<String> cache = jedis.zrevrange(key, star, end);
            if (cache != null && cache.size() > 0) {
                //从cache中取分页的数据返回给前端
                key = key + "|fileds";
                List<String> list = jedis.hmget(key, cache.toArray(new String[0]));
                return list;
            }
        } catch (Exception e) {
            m_logger.error("GetOrderMapProperty Redis Error,key=" + key, e);
        } finally {
            returnConn(jedis);
        }
        return null;
    }

    public void setJSONObject(String key, JSONObject json) {
        try {
            if (json == null) {
                return;
            }
            set(key, json.toString());
        } catch (Exception e) {
            m_logger.error("setJSONObject Redis Error,key=" + key, e);
        }
    }


    /**
     * 设置JSONObject数据到缓存
     *
     * @param key    缓存的键
     * @param json   缓存的值
     * @param expire 失效时间
     */
    public void setJSONObject(String key, JSONObject json, int expire) {
        try {
            if (json == null) {
                return;
            }
            set(key, json.toString(), expire);
        } catch (Exception e) {
            m_logger.error("setJSONObject Redis Error,key=" + key, e);
        }

    }

    public JSONObject getJSONObject(String key) {
        try {
            String jsonString = get(key);
            if (jsonString != null) {
                return JSONObject.parseObject(jsonString);
            }

        } catch (Exception e) {
            m_logger.error("getJSONObject Redis Error,key=" + key, e);
        }
        return null;
    }

    public void setJSONArray(String key, JSONArray jsonArray, int seconds) {
        try {
            if (jsonArray == null) {
                return;
            }
            set(key, jsonArray.toString(), seconds);
            m_logger.debug("setJSONArray Redis ,key=" + key);
        } catch (Exception e) {
            m_logger.error("setJSONArray Redis Error,key=" + key, e);
        }
    }

    public void setJSONArray(String key, JSONArray jsonArray) {
        try {
            if (jsonArray == null) {
                return;
            }
            set(key, jsonArray.toString());
            m_logger.debug("setJSONArray Redis ,key=" + key);
        } catch (Exception e) {
            m_logger.error("setJSONArray Redis Error,key=" + key, e);
        }
    }

    public JSONArray getJSONArray(String key) {
        try {
            Object jsonString = get(key);
            if (jsonString != null) {
                JSONArray jsonArray = JSONArray.parseArray(jsonString.toString());
                return jsonArray;
            }

        } catch (Exception e) {
            m_logger.error("getJSONArray Redis Error,key=" + key, e);
        }
        return null;
    }

    /**
     * 取总数
     *
     * @param key
     * @return
     */
    public Integer getInt(String key) {
        try {
            return Integer.parseInt(this.get(key));
        } catch (NumberFormatException e) {
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return 当 key 不存在或没有设置生存时间时，返回 -1 。否则，返回 key 的剩余生存时间(以秒为单位)
     * @throws Exception
     */
    public Long getExpiredTime(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            m_logger.debug("getExpiredTime redisCache ,key=" + key);
            return jedis.ttl(key);
        } catch (Exception e) {
            return 0l;
        } finally {
            returnConn(jedis);
        }
    }

    /**
     * 批量查询  add by qinbo
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public List<Object> hmgetMulti(List<String> keys) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.hgetAll(key);
            }
            result = p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("hmgetMulti error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public long lPush(String key, String value) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.lpushx(key, value);
            addSyncTask(key, value);
        } catch (Exception e) {
            m_logger.error("lPush_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public long rPush(String key, String value) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.rpush(key, value);
            addSyncTask(key, value);
        } catch (Exception e) {
            m_logger.error("rPush_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    public String lPop(String key) throws Exception {
        String item = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            item = jedis.lpop(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("lPop_error", e);
        } finally {
            returnConn(jedis);
        }
        return item;
    }

    public String rPop(String key) throws Exception {
        String item = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            item = jedis.rpop(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("rPop_error", e);
        } finally {
            returnConn(jedis);
        }
        return item;
    }

    public long lLen(String key) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.llen(key);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("lLen_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    /**
     * list操作lrange：从左边返回指定长度的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public List<String> lrange(String key, int start, int end) throws Exception {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            result = jedis.lrange(key, start, end);
        } catch (Exception e) {
            m_logger.error("lrange_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * list操作lpush：向左边推入成员
     *
     * @param key
     * @param strings
     * @return
     * @throws Exception
     */
    public long lpush(String key, String... strings) throws Exception {
        Jedis jedis = null;
        long size = 0;
        try {
            jedis = borrowConn();
            size = jedis.lpush(key, strings);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("lpush_error", e);
        } finally {
            returnConn(jedis);
        }
        return size;
    }

    /**
     * 根据score降序获取
     *
     * @param key
     * @param max
     * @param min
     * @return
     * @throws Exception
     */
    public Set<String> zrevrangeByScore(String key, double max, double min) throws Exception {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            result = jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            m_logger.error("zrevrangeByScore_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 根据score升序获取
     *
     * @param key
     * @param max
     * @param min
     * @return
     * @throws Exception
     */
    public Set<String> zrangeByScore(String key, double min, double max) throws Exception {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            result = jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            m_logger.error("zrangeByScore_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * hash 原子增
     *
     * @param key
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    public long hincrBy(String key, String field, long value) throws Exception {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            result = jedis.hincrBy(key, field, value);
            addSyncTask(key);
        } catch (Exception e) {
            m_logger.error("hincrBy_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }


    /**
     * hash hvals
     *
     * @param key
     * @return
     * @throws Exception
     */
    public List<String> hvals(String key) throws Exception {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            result = jedis.hvals(key);
        } catch (Exception e) {
            m_logger.error("hvals_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }


    /**
     * rpush
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public long rPush(String key, String... value) throws Exception {
        Long count = null;
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            count = jedis.rpush(key, value);
            addSyncTask(key, value);
        } catch (Exception e) {
            m_logger.error("rPush_error", e);
        } finally {
            returnConn(jedis);
        }
        return count;
    }

    /**
     * 管道zrangebyscore
     *
     * @param keys
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     * @throws Exception
     */
    public List<Object> zrangeByScoreInPiple(List<String> keys, double min, double max, int offset, int count) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.zrangeByScore(key, min, max, offset, count);
            }
            result = p.syncAndReturnAll();

        } catch (Exception e) {
            m_logger.error("zrangeByScoreInPiple_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 管道zrangebyscore
     *
     * @param keys
     * @param min
     * @param max
     * @return
     * @throws Exception
     */
    public List<Object> zrangeByScoreInPiple(List<String> keys, double min, double max) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.zrangeByScore(key, min, max);
            }
            result = p.syncAndReturnAll();

        } catch (Exception e) {
            m_logger.error("zrangeByScoreInPiple_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public List<Object> zrevrangeWithScoresInPiple(List<String> keys,
                                                   int start, int end) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.zrevrangeWithScores(key, start, end);
            }
            result = p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("zrevrangeWithScoresInPiple_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }


    public List<Object> zrangeWithScoresInPiple(List<String> keys,
                                                int start, int end) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.zrangeWithScores(key, start, end);
            }
            result = p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("zrevrangeWithScoresInPiple_error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 计算给定的numkeys个有序集合的《并集》，并且把结果放到destination中。
     * 在给定要计算的key和其它参数之前，必须先给定key个数(numberkeys)。 默认情况下，结果集中某个成员的score值是所有给定集下该成员score值之和。
     *
     * @param dstkey    如果key destination存在，就被覆盖。
     * @param srckeys
     * @param aggregate 使用AGGREGATE选项，你可以指定并集的结果集的聚合方式。默认使用的参数SUM，可以将所有集合中某个成员的score值之和作为结果集中该成员的score值。如果使用参数MIN或者MAX，结果集就是所有集合中元素最小或最大的元素。
     * @param weights
     * @return
     * @throws Exception
     */
    public Long zunionstore(String dstkey, String[] srckeys, Aggregate aggregate, int... weights) throws Exception {
        if (BlankUtil.isBlank(srckeys) || srckeys.length < 2) {
            m_logger.info("redisEnv zunionstore srckers not fit");
            return null;
        }
        /*
         * 参数
    	 */
        ZParams params = new ZParams();
        //如果需要权重
        if (!BlankUtil.isBlank(weights)) {
            params.weights(weights);
        }
        if (aggregate != null) {
            params.aggregate(aggregate);
        }
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = borrowConn();
            result = jedis.zunionstore(dstkey, params, srckeys);
        } catch (Exception e) {
            m_logger.error("zunionstore error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 计算给定的numkeys个有序集合的《交集》，并且把结果放到destination中。
     * 在给定要计算的key和其它参数之前，必须先给定key个数(numberkeys)。 默认情况下，结果集中某个成员的score值是所有给定集下该成员score值之和。
     *
     * @param dstkey    如果key destination存在，就被覆盖。
     * @param srckeys
     * @param aggregate 使用AGGREGATE选项，你可以指定并集的结果集的聚合方式。默认使用的参数SUM，可以将所有集合中某个成员的score值之和作为结果集中该成员的score值。如果使用参数MIN或者MAX，结果集就是所有集合中元素最小或最大的元素。
     * @param weights   你可以为每个给定的有序集指定一个乘法因子，意思就是，每个给定有序集的所有成员的score值在传递给聚合函数之前都要先乘以该因子。如果WEIGHTS没有给定，默认就是1。
     * @return
     * @throws Exception
     */
    public Long zinterstore(String dstkey, String[] srckeys, Aggregate aggregate, int... weights) throws Exception {
        if (BlankUtil.isBlank(srckeys) || srckeys.length < 2) {
            m_logger.info("redisEnv zinterstore srckers not fit");
            return null;
        }
    	/*
    	 * 参数
    	 */
        ZParams params = new ZParams();
        //如果需要权重
        if (!BlankUtil.isBlank(weights)) {
            params.weights(weights);
        }
        if (aggregate != null) {
            params.aggregate(aggregate);
        }
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = borrowConn();
            result = jedis.zinterstore(dstkey, params, srckeys);
        } catch (Exception e) {
            m_logger.error("zunionstore error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 批量get
     *
     * @param keys
     * @return
     * @throws Exception
     */
    public List<Object> getMulti(List<String> keys) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.get(key);
            }
            result = p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("hmgetMulti error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public List<Object> hmgetMulti(String hashKey, List<String> keys) throws Exception {
        Jedis jedis = null;
        List<Object> result = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.hget(hashKey, key);
            }
            result = p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("hmgetMulti error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    public void subscribe(JedisPubSub jedisPubSub, String... patterns) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            jedis.subscribe(jedisPubSub, patterns);
        } catch (Exception e) {
            m_logger.error("subscribe error", e);
        } finally {
            returnConn(jedis);
        }
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            jedis.psubscribe(jedisPubSub, patterns);
        } catch (Exception e) {
            m_logger.error("psubscribe error", e);
        } finally {
            returnConn(jedis);
        }
    }

    public long publish(String channel, String message) throws Exception {
        Jedis jedis = null;
        long result = 0;
        try {
            jedis = borrowConn();
            result = jedis.publish(channel, message);
        } catch (Exception e) {
            m_logger.error("psubscribe error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }


    /**
     * 通过设置过期时间来使key过期达到删除缓存的作用
     *
     * @param key
     * @return
     * @throws Exception
     */
    public long deleteByPexpire(String key) throws Exception {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = borrowConn();
            result = jedis.pexpire(key, 1);
        } catch (Exception e) {
            m_logger.error("jedis deleteByPexpire access error", e);
        } finally {
            returnConn(jedis);
        }
        return result;
    }

    /**
     * 管道操作
     *
     * @param op
     * @return
     * @throws Exception
     */
    public List<Object> pipeOperate(PipeOperation op) throws Exception {
        Jedis jedis = null;
        try {
            jedis = borrowConn();
            Pipeline p = jedis.pipelined();
            p = op.operate(p);
            return p.syncAndReturnAll();
        } catch (Exception e) {
            m_logger.error("pipleOperation error", e);
        } finally {
            returnConn(jedis);
        }
        return null;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public boolean isUse_slave() {
        return isUse_slave;
    }

    public void setIsUse_slave(boolean isUse_slave) {
        this.isUse_slave = isUse_slave;
    }
}

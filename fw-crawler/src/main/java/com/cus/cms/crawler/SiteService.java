package com.cus.cms.crawler;

import com.cus.cms.crawler.model.LinkMapper;
import com.cus.cms.crawler.task.MongodbHelper;
import com.cus.cms.crawler.util.DateTimeUtil;
import com.cus.cms.crawler.util.DbUtil;
import com.cus.cms.crawler.model.FwSeed;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 */
public class SiteService {

    private static final Logger logger = LoggerFactory.getLogger(SiteService.class);
    private MongoCollection<Document> docCollection;

    public MongoCollection<Document> getDocCollection() {
        return MongodbHelper.getDbCollection("fwpro", "fw_url");
    }

    public List<FwSeed> getSiteUrl() throws SQLException {
        List<FwSeed> list = new ArrayList<>();
        String sql = "select * from fw_seed order by id asc";
        Connection conn = DbUtil.createConn();
        if (conn == null) {
            return list;
        }
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            FwSeed fwUrl = new FwSeed();
            fwUrl.setSiteLink(resultSet.getString("site_url"));
            fwUrl.setSiteName(resultSet.getString("site_name"));
            fwUrl.setSiteType(resultSet.getInt("id"));
            fwUrl.setStartId(resultSet.getInt("start_id"));
            fwUrl.setThread(resultSet.getInt("thread"));
            list.add(fwUrl);
        }
        DbUtil.closeConn(conn);
        return list;
    }

    public int savePwUrl(String source, String linkUrl, int siteType) {
        int linkType = 1;
        if (linkUrl.endsWith("html") || linkUrl.endsWith("htm") || linkUrl.endsWith("shtml")) {
            linkType = 2;
        }
        String sql = "insert into fw_url(site_type,link_url,link_type,source,create_time)  SELECT ?,?,?,?,? FROM DUAL " +
                "WHERE NOT EXISTS(SELECT id FROM fw_url WHERE link_url = ?);";
        Connection conn = null;
        try {
            conn = DbUtil.createConn();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, siteType);
            statement.setString(2, linkUrl);
            statement.setInt(3, linkType);
            statement.setString(4, source);
            statement.setString(5, DateTimeUtil.getCurrentTime());
            statement.setString(6, linkUrl);
            return statement.execute() ? 1 : 0;
        } catch (Exception e) {
            logger.error("savePwUrl error,", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private final String BATCH_SQL = "insert into fw_url(site_type,link_url,link_type,source,create_time)  SELECT ?,?,?,?,? " +
            "FROM DUAL WHERE NOT EXISTS(SELECT id FROM fw_url WHERE link_url = ?)";

    public int batchInsert(List<LinkMapper> mappers) {
        if (mappers == null || mappers.size() < 1) {
            return 0;
        }

        Connection conn = null;
        boolean isAuto = false;
        try {
            conn = DbUtil.createConn();
            conn.setAutoCommit(false);

            PreparedStatement statement = conn.prepareStatement(BATCH_SQL);

            for (LinkMapper mapper : mappers) {
                String linkUrl = mapper.getLinkUrl();
                String source = mapper.getSource();
                int siteType = mapper.getSiteType();

                int linkType = 1;
                if (linkUrl.endsWith("html") || linkUrl.endsWith("htm") || linkUrl.endsWith("shtml")) {
                    linkType = 2;
                }

                statement.setInt(1, siteType);
                statement.setString(2, linkUrl);
                statement.setInt(3, linkType);
                statement.setString(4, source);
                statement.setString(5, DateTimeUtil.getCurrentTime());
                statement.setString(6, linkUrl);
                statement.addBatch();
            }
            int[] results = statement.executeBatch();
            conn.commit();
            logger.info("batchInsert finish, size is : {}, results is: {}", mappers.size(),results);
            return sumArray(results);
        } catch (Exception e) {
            try {
                conn.rollback();
                StartMain.queue.addAll(mappers);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            logger.error("savePwUrl error,", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void updateSeed(int id, int startId) {
        Connection conn = null;
        try {
            conn = DbUtil.createConn();
            PreparedStatement statement = conn.prepareStatement("update fw_seed set start_id=? where id=? ");
            statement.setInt(1, startId);
            statement.setInt(2, id);
            statement.execute();
            logger.info("updateSeed finish, id is: {}, startId is: {} ", id, startId);
        } catch (Exception e) {
            logger.error("updateSeed error, id is: {}, startId is: {} ", id, startId, e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int sumArray(int[] res) {
        int sum = 0;
        for(int i = 0; i < res.length; i++) {
            sum += res[i];
        }
        return sum;
    }

    public List<String> getAllUrl(int siteType){
        List<String> list = new ArrayList<>();
        String sql = "select * from fw_url2 order by id asc";
        Connection conn = null;
        try {
            conn = DbUtil.createConn();
            if (conn == null) {
                return list;
            }
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(resultSet.getString("link_url"));
            }
        } catch (Exception e) {

        }finally {
            DbUtil.closeConn(conn);
        }
        return list;
    }
}

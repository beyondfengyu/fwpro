package com.cus.cms.common.mongo;

import javax.net.SocketFactory;

import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DefaultDBDecoder;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

/**
 * @author Andy
 */
public class MongoClientOption {
    private String description;
    private ReadPreference readPreference = ReadPreference.primary();//读写分离
    private WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;//write concern 来让用户自己衡量性能和写安全 Acknowledged 中等级别，能够拿到mongodb返回的信息。默认是这个设置
    private CodecRegistry codecRegistry = MongoClient.getDefaultCodecRegistry();
    private int minConnectionsPerHost;
    private int maxConnectionsPerHost = 100; //最大的连接数
    private int threadsAllowedToBlockForConnectionMultiplier = 5;//线程队列数，它以上面connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
    private int serverSelectionTimeout = 1000 * 30;
    private int maxWaitTime = 1000 * 60 * 2;//最大等待连接的线程阻塞时间2分钟
    private int maxConnectionIdleTime;
    private int maxConnectionLifeTime;//最大连接存活数
    private int connectTimeout = 1000 * 10;//连接超时的毫秒。0是默认和无限
    private int socketTimeout = 0;//socket超时。0是默认和无限
    private boolean socketKeepAlive = false;
    private boolean sslEnabled = false;
    private boolean sslInvalidHostNameAllowed = false;
    private boolean alwaysUseMBeans = false;
    private int heartbeatFrequency = 10000;
    private int minHeartbeatFrequency = 500;
    private int heartbeatConnectTimeout = 20000;
    private int heartbeatSocketTimeout = 20000;
    private int localThreshold = 15;
    private boolean autoConnectRetry = true;//是否自动连接
    private String requiredReplicaSetName;
    private DBDecoderFactory dbDecoderFactory = DefaultDBDecoder.FACTORY;//Mongo Java驱动程序配置
    private DBEncoderFactory dbEncoderFactory = DefaultDBEncoder.FACTORY;//mongo java 编码工厂
    private SocketFactory socketFactory = SocketFactory.getDefault();//socket 工厂
    private boolean cursorFinalizerEnabled = true;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    public void setCodecRegistry(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    public void setMinConnectionsPerHost(int minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setServerSelectionTimeout(int serverSelectionTimeout) {
        this.serverSelectionTimeout = serverSelectionTimeout;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public void setSslInvalidHostNameAllowed(boolean sslInvalidHostNameAllowed) {
        this.sslInvalidHostNameAllowed = sslInvalidHostNameAllowed;
    }

    public void setAlwaysUseMBeans(boolean alwaysUseMBeans) {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }

    public void setHeartbeatFrequency(int heartbeatFrequency) {
        this.heartbeatFrequency = heartbeatFrequency;
    }

    public void setMinHeartbeatFrequency(int minHeartbeatFrequency) {
        this.minHeartbeatFrequency = minHeartbeatFrequency;
    }

    public void setHeartbeatConnectTimeout(int heartbeatConnectTimeout) {
        this.heartbeatConnectTimeout = heartbeatConnectTimeout;
    }

    public void setHeartbeatSocketTimeout(int heartbeatSocketTimeout) {
        this.heartbeatSocketTimeout = heartbeatSocketTimeout;
    }

    public void setLocalThreshold(int localThreshold) {
        this.localThreshold = localThreshold;
    }

    public void setRequiredReplicaSetName(String requiredReplicaSetName) {
        this.requiredReplicaSetName = requiredReplicaSetName;
    }

    public void setDbDecoderFactory(DBDecoderFactory dbDecoderFactory) {
        this.dbDecoderFactory = dbDecoderFactory;
    }

    public void setDbEncoderFactory(DBEncoderFactory dbEncoderFactory) {
        this.dbEncoderFactory = dbEncoderFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }

    public MongoClientOptions getMongoClientOptions() {
        Builder builder = MongoClientOptions.builder();
        builder.connectionsPerHost(maxConnectionsPerHost);
        builder.connectTimeout(connectTimeout);
        builder.description(description);
        builder.heartbeatConnectTimeout(heartbeatConnectTimeout);
        builder.heartbeatSocketTimeout(heartbeatSocketTimeout);
        builder.localThreshold(localThreshold);
        builder.maxConnectionIdleTime(maxConnectionIdleTime);
        builder.maxConnectionLifeTime(maxConnectionLifeTime);
        builder.minConnectionsPerHost(minConnectionsPerHost);
        builder.minHeartbeatFrequency(minHeartbeatFrequency);
        builder.readPreference(readPreference);
        builder.requiredReplicaSetName(requiredReplicaSetName);
        builder.serverSelectionTimeout(serverSelectionTimeout);
        builder.alwaysUseMBeans(alwaysUseMBeans);
        builder.socketFactory(socketFactory);
        builder.socketKeepAlive(socketKeepAlive);
        builder.sslEnabled(sslEnabled);
        builder.sslInvalidHostNameAllowed(sslInvalidHostNameAllowed);
        builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
        builder.writeConcern(writeConcern);
        builder.codecRegistry(codecRegistry);
        builder.maxWaitTime(maxWaitTime);
        builder.socketTimeout(socketTimeout);
        builder.heartbeatFrequency(heartbeatFrequency);
        builder.dbDecoderFactory(dbDecoderFactory);
        builder.dbEncoderFactory(dbEncoderFactory);
        builder.cursorFinalizerEnabled(cursorFinalizerEnabled);
        return builder.build();
    }

    public boolean isAutoConnectRetry() {
        return autoConnectRetry;
    }

    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }
}

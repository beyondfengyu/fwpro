package com.cus.cms.crawler.task;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author Andy
 */
public class MongodbHelper {

    private static MongoClient mongoClient;

    static{
        MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
        buide.connectionsPerHost(100);// 与目标数据库可以建立的最大链接数
        buide.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
        buide.maxWaitTime(100 * 60 * 5);// 一个线程成功获取到一个可用数据库之前的最大等待时间
        buide.threadsAllowedToBlockForConnectionMultiplier(100);
        buide.maxConnectionIdleTime(0);
        buide.maxConnectionLifeTime(0);
        buide.socketTimeout(0);
        buide.socketKeepAlive(true);
        MongoClientOptions myOptions = buide.build();
        mongoClient = new MongoClient(new ServerAddress("127.0.0.1", 27017), myOptions);
    }

    public static MongoCollection<Document>  getDbCollection(String db, String docset) {
        // 获取数据库
        MongoDatabase database = mongoClient.getDatabase(db);
        // 进入某个文档集
        MongoCollection<Document> collection = database.getCollection(docset);

        return collection;
    }

    public static void closeClient() {
        mongoClient.close();
    }
}

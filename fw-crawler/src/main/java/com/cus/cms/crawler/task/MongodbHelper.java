package com.cus.cms.crawler.task;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author Andy
 */
public class MongodbHelper {

    private static MongoClient mongoClient;

    static{
        mongoClient = new MongoClient("localhost", 27017);
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

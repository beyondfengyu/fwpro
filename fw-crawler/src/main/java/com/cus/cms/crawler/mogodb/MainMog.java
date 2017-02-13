package com.cus.cms.crawler.mogodb;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author Andy
 */
public class MainMog {

    public static void main(String[] args) {
        // 获取链接
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        // 获取数据库
        MongoDatabase database = mongoClient.getDatabase("fwpro");
        // 进入某个文档集
        MongoCollection<Document> collection = database.getCollection("fw_url");


        collection.createIndex(new Document("",""));
        System.out.println(collection.count());
        Document docc = new Document("site_link", "http://www.diyifanwen.com")
                .append("site_name", "第一范文网")
                .append("site_type", 1)
                .append("start_id", 1)
                .append("thread", 16);

        // 向文档中插入列表
//        collection.insertOne(docc);
        long end = System.currentTimeMillis();
//        // // 显示集合中的文档的数量
//        System.out.println("cast " + (end - start) + " ms , size: " + collection.count());
//
//
        BasicDBObject query = new BasicDBObject();
//        collection.deleteMany(query);
        FindIterable<Document> iter = collection.find();
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                System.out.println(doc.get("site_name") + " : " + doc.get("site_link"));
                System.out.println(doc.toJson());
            }
        });

//        collection.deleteMany(query);
//        end = System.currentTimeMillis();
//        // // 显示集合中的文档的数量
//        System.out.println("cast " + (end - start) + " ms , size: " + collection.count());
        // 关闭数据库连接
        mongoClient.close();
    }

}

package com.cus.cms.crawler.mogodb;

import com.cus.cms.crawler.util.SnowFlake;
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
        final MongoCollection<Document> collection = database.getCollection("fw_dir");
        final MongoCollection<Document> collection2 = database.getCollection("fw_dir2");

        final SnowFlake snowFlake = new SnowFlake(1, 1);

        FindIterable<Document> iter = collection.find();
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                Document newDoc = new Document("_id", snowFlake.nextId())
                        .append("dirName", doc.get("dir_name"))
                        .append("dirCode", doc.get("dir_code"))
                        .append("dirType", doc.get("dir_type"))
                        .append("lastCode", doc.get("last_code"))
                        .append("status", doc.getInteger("status"))
                        .append("showOrder", doc.getInteger("show_order"));
                collection2.insertOne(newDoc);

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

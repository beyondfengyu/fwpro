package com.cus.wob.crawler.mogodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println(collection.count());
        long start = System.currentTimeMillis();
        /********************** 数据插入 ****************************/
        // 创建一个包含多个文档的列表
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 1000000; i++) {
            Document docc = new Document("link_url", "http://www.diyifanwen.com/fanwen/jiaoshigongzuojihua/1611312195771202.htm" + i)
                    .append("site_type", 1).append("link_type", 1);
            documents.add(docc);
        }
        // 向文档中插入列表
        collection.insertMany(documents);
        long end = System.currentTimeMillis();
        // // 显示集合中的文档的数量
        System.out.println("cast " + (end - start) + " ms , size: " + collection.count());


        BasicDBObject query = new BasicDBObject();
        collection.deleteMany(query);
        end = System.currentTimeMillis();
        // // 显示集合中的文档的数量
        System.out.println("cast " + (end - start) + " ms , size: " + collection.count());
        // 关闭数据库连接
        mongoClient.close();
    }

}

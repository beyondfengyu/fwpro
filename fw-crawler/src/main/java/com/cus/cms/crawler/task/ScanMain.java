package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.cus.cms.crawler.util.DateTimeUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Andy
 */
public class ScanMain {


    public static void main(String[] args) throws InterruptedException {
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_url");


        while (true) {
            System.out.println("time: "+ DateTimeUtil.getCurrentTime() + "， size: " + collection.count());
            TimeUnit.SECONDS.sleep(60);

        }
//        System.out.println("time: "+ DateTimeUtil.getCurrentTime() + "， size: " + collection.count());
//        Document docc = new Document();
//        collection.insertOne(docc);
//        collection.insertOne(docc);
//        BasicDBObject dbObject = new BasicDBObject();
//        Pattern pattern = Pattern.compile("^.*\\.ZIP$", Pattern.CASE_INSENSITIVE);
//        dbObject.put("link_url", pattern);
//        System.out.println(collection.count(dbObject));
//
//        FindIterable<Document> iter = collection.find(docc).limit(2);
//        iter.forEach(new Block<Document>() {
//            public void apply(Document doc) {
//                System.out.println(doc.toJson());
//            }
//        });

//        collection.updateMany(docc, new Document("$set",new Document("start_id",2)));
//        iter = collection.find(new Document()).limit(2);
//        iter.forEach(new Block<Document>() {
//            public void apply(Document doc) {
//                System.out.println(doc.toJson());
//            }
//        });
//
//        pattern = Pattern.compile("^.*\\.txt.+$", Pattern.CASE_INSENSITIVE);
//        dbObject.put("link_url", pattern);
//        System.out.println(collection.count(dbObject));
//        iter = collection.find(dbObject).limit(2);
//        iter.forEach(new Block<Document>() {
//            public void apply(Document doc) {
//                String href = doc.getString("link_url");
//                if (href.endsWith("htm") || href.endsWith("htm") || href.endsWith("shtml")) {
//                    System.out.println("============="+doc.toJson());
//                }else {
//                    System.out.println(doc.toJson());
//                }
//            }
//        });
//

//        collection.deleteMany(dbObject);

//        MongodbHelper.closeClient();
    }
}

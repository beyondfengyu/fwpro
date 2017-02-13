package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.cus.cms.crawler.util.DateTimeUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author laochunyu
 */
public class ScanMain {


    public static void main(String[] args) throws InterruptedException {
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_url");


//        while (true) {
//            System.out.println("time: "+ DateTimeUtil.getCurrentTime() + "， size: " + collection.count());
//            TimeUnit.SECONDS.sleep(20);
//
//        }
        Document docc = new Document("link_url", "http://www.diyifanwen.com/Files/BeyondPic/2007-10/21/07102121060898388.jpg").append("site_type", 1).append("link_type",1);
//        collection.insertOne(docc);
//        collection.insertOne(docc);
        BasicDBObject dbObject = new BasicDBObject();
        Pattern pattern = Pattern.compile("^.*\\.jpg.*$", Pattern.CASE_INSENSITIVE);
        dbObject.put("link_url", pattern);
        System.out.println(collection.count(dbObject));
        FindIterable<Document> iter = collection.find(dbObject).limit(2);
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                System.out.println(doc.toJson());
            }
        });
//
//        pattern = Pattern.compile("^.*\\.txt.+$", Pattern.CASE_INSENSITIVE);
//        dbObject.put("link_url", pattern);
//        System.out.println(collection.count(dbObject));
//        iter = collection.find(dbObject).limit(2);
//        iter.forEach(new Block<Document>() {
//            public void apply(Document doc) {
//                System.out.println(doc.toJson());
//            }
//        });
//
//        pattern = Pattern.compile("^.*\\.txt$", Pattern.CASE_INSENSITIVE);
//        dbObject.put("link_url", pattern);
        collection.deleteMany(dbObject);
//        System.out.println(collection.count(dbObject));
//        iter = collection.find(dbObject).limit(2);
//        iter.forEach(new Block<Document>() {
//            public void apply(Document doc) {
//                System.out.println(doc.toJson());
//            }
//        });

//        MongodbHelper.closeClient();
    }
}

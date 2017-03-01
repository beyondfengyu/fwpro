package com.cus.cms.crawler.mogodb;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * @author Andy
 */
public class MainTest {

    public static void main(String[] args) {
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_page");

        FindIterable<Document> iter = collection.find().limit(10);
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                System.out.println(doc.toString());

            }
        });
    }
}

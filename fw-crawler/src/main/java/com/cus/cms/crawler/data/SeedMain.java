package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * @author Andy
 */
public class SeedMain {

    public static void main(String[] args) {
        String[] navs = {};

        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_dir");
        for (String nav : navs) {

        }
    }
}

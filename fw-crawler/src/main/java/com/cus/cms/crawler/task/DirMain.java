package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;

/**
 * @author Andy
 */
public class DirMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_dir");
        CrawService.getAllMap("http://www.diyifanwen.com/qt/wzdt.htm");
    }
}

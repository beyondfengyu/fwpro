package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

/**
 * @author Andy
 */
public class DirMain {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_dir");
        List<String> list = CrawService.getAllMap("http://www.diyifanwen.com/qt/wzdt.htm", "http://www.diyifanwen.com");

        for (String url : list) {
            String[] arr = url.replace("http://www.diyifanwen.com", "").split("/");

        }
    }
}

package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Andy
 */
public class CrawMain {


    public static void main(String[] args) throws InterruptedException, IOException {
        List<String> list = CrawService.getAllATagList("http://www.diyifanwen.com/sucai/shoujifengjingtupian", "http://www.diyifanwen.com", 1);

        System.out.println(list.size());
    }
}

package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Andy
 */
public class CrawMain {


    public static void main(String[] args) throws InterruptedException {
        List<String> list = CrawService.getAllATagList("http://www.diyifanwen.com/fanwen/jiaojiliyi/index_2.html", "http://www.diyifanwen.com", 1);

        System.out.println(list.size());
    }
}

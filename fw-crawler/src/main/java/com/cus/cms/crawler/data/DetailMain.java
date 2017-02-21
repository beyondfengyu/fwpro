package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.print.Doc;
import java.lang.Exception;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Andy
 */
public class DetailMain {

    private static final int count = 8;
    private static Lock lock;
    private static MongoCollection<Document> collLink;
    private static MongoCollection<Document> collPage;
    private static MongoCollection<Document> collError;

    public static void main(String[] args) throws Exception {
        lock = new ReentrantLock();
        collLink = MongodbHelper.getDbCollection("fwpro", "fw_link");
        collPage = MongodbHelper.getDbCollection("fwpro", "fw_page");
        collError = MongodbHelper.getDbCollection("fwpro", "fw_error");

        ExecutorService service = Executors.newFixedThreadPool(count);
//        String[] detail = DataService.getDetailPage("http://www.diyifanwen.com/fanwen/lunwenzhidao/151262245273184.htm");
//        if (detail != null) {
//            System.out.println("title is: " + detail[0]);
//            System.out.println("content is: " + detail[1]);
//        }

        for (int i = 0; i < count; i++) {
            service.execute(new Task());
        }
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            FindIterable<Document> iter = null;
            List<String> list = null;
//            while (true) {
                try {
                    final List<String> finalList = list = new ArrayList<>();
                    lock.lock();
                    iter = collLink.find(new Document("status", 1)).sort(new Document("id",-1)).limit(10);
                    iter.forEach(new Block<Document>() {
                        public void apply(Document doc) {
                            String link = doc.getString("link_url");
                            finalList.add(link);
                            Document newDoc = new Document("$set", new Document("status", 2));
                            collLink.updateOne(new Document("link_url", link), newDoc);
                        }
                    });
                } finally {
                    lock.unlock();
                }

                for (String link : list) {
                    try {
                        String[] detail = DataService.getDetailPage(link);
                        if (detail != null) {
                            System.out.println("title is: " + detail[0]);
                            Document newdoc = new Document();
                            collPage.insertOne(newdoc);
//                            System.out.println("content is: " + detail[1]);
                        }else {
                            Document doc = new Document("link_url", link).append("status", 1);
                            collError.insertOne(doc);
                        }
                    } catch (Exception e) {
                        System.out.println("page parse error, link is " + link);
                        Document doc = new Document("link_url", link).append("status", 1);
                        collError.insertOne(doc);
                    }
                }

//            }
        }
    }
}

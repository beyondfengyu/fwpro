package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.cus.cms.crawler.util.SnowFlake;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(DetailMain.class);
    private static final int count = 32;

    private static Lock lock;
    private static SnowFlake snowFlake;
    private static MongoCollection<Document> collLink;
    private static MongoCollection<Document> collPage;
    private static MongoCollection<Document> collError;
    private static MongoCollection<Document> collTmp;

    public static void main(String[] args) throws Exception {
        lock = new ReentrantLock();
        snowFlake = new SnowFlake(1, 1);
        collLink = MongodbHelper.getDbCollection("fwpro", "fw_link");
        collPage = MongodbHelper.getDbCollection("fwpro", "fw_page");
        collError = MongodbHelper.getDbCollection("fwpro", "fw_error");
        collTmp = MongodbHelper.getDbCollection("fwpro", "fw_tmp");

        final ExecutorService service = Executors.newFixedThreadPool(count);

        for (int i = 0; i < count; i++) {
            service.execute(new Task());
        }


        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                service.shutdown();
            }
        });
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            FindIterable<Document> iter = null;
            List<String> list = null;
            while (collTmp.count() < 1) {
                try {
                    final List<String> finalList = list =  new ArrayList<>();
                    lock.lock();
                    iter = collLink.find(new Document("status", 1)).limit(50);
                    iter.forEach(new Block<Document>() {
                        public void apply(Document doc) {
                            String link = doc.getString("link_url");
                            finalList.add(link);
                            Document newDoc = new Document("$set", new Document("status", 2));
                            collLink.updateOne(new Document("link_url", link).append("status",1), newDoc);
                        }
                    });
                } finally {
                    lock.unlock();
                }

                 if (list.size() == 0) {
                    break;
                }

                for (String link : list) {
                    try {
                        String[] detail = DataService.getDetailPage(link);
                        if (detail != null) {
                            StoreService.saveDetailPage(snowFlake.nextId(), detail[0], detail[1], link);
//                            logger.info("title is: {}", detail[0]);
                        } else {
                            logger.error("page parse error, link is " + link);
                            Document doc = new Document("link_url", link).append("status", 1);
                            collError.insertOne(doc);
                        }
                    } catch (Exception e) {
                        logger.error("page parse error, link is " + link, e);
                        Document doc = new Document("link_url", link).append("status", 1);
                        collError.insertOne(doc);
                    }
                }

            }
        }
    }
}

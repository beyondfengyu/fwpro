package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

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

    private static Lock lock;
    private static MongoCollection<Document> collLink;
    private static MongoCollection<Document> collPage;
    private static MongoCollection<Document> collError;

    public static void main(String[] args) throws Exception {
        lock = new ReentrantLock();
        collLink = MongodbHelper.getDbCollection("fwpro", "fw_link");
        collPage = MongodbHelper.getDbCollection("fwpro", "fw_page");
        collError = MongodbHelper.getDbCollection("fwpro", "fw_error");

        ExecutorService service = Executors.newFixedThreadPool(8);
        String[] detail = DataService.getDetailPage("http://www.diyifanwen.com/fanwen/lunwenzhidao/151262245273184.htm");
        if (detail != null) {
            System.out.println("title is: " + detail[0]);
            System.out.println("content is: " + detail[1]);
        }
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            FindIterable<Document> iter = null;
            List<String> list = null;
            while (true) {
                try {
                    final List<String> finalList = list = new ArrayList<>();
                    lock.lock();
                    iter = collLink.find(new Document("status", 1)).limit(1);
                    iter.forEach(new Block<Document>() {
                        public void apply(Document doc) {
                            String link = doc.getString("link_url");
                            finalList.add(link);
                            doc.put("status", 2);
                            collLink.updateOne(new Document("link_url", link), doc);
                        }
                    });
                } finally {
                    lock.unlock();
                }

                for (String link : list) {

                }

            }
        }
    }
}

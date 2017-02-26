package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 */
public class StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private MongoCollection<Document> docCollection;

    public StoreService(String dbName, String table){
        docCollection = MongodbHelper.getDbCollection(dbName, table);
    }

    public void saveUrl(List<String> linkUrls, int siteType){
        if(linkUrls == null || linkUrls.size() < 1){
            return;
        }
        try {
            List<Document> docs = new ArrayList<>();
            Document doc = null;
            for (String linkUrl : linkUrls) {
                int linkType = 1;
                if (linkUrl.endsWith("htm") || linkUrl.endsWith("html") || linkUrl.endsWith("shtml")) {
                    linkType = 2;
                }
                doc = new Document("link_url", linkUrl)
                        .append("link_type", linkType)
                        .append("site_type", siteType)
                        .append("status", 1);
                docs.add(doc);
            }
            docCollection.insertMany(docs);
        } catch (Exception e) {
            logger.error("saveUrl error", e);
        }
    }

    public void saveErrorUrl(String url, int siteType){
        try {
            MongoCollection<Document> dirCollection = MongodbHelper.getDbCollection("fwpro", "fw_error");
            Document doc = new Document("url", url)
                    .append("site_type", siteType)
                    .append("status", 1);
            dirCollection.insertOne(doc);
        } catch (Exception e) {
            logger.error("saveErrorUrl error , url is " + url, e);
        }
    }

    public void saveNav(String url, int dirType) {
        if (url == null || "".equals(url)) {
            return;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 2);
        }
        try {
            MongoCollection<Document> dirCollection = MongodbHelper.getDbCollection("fwpro", "fw_dir");
            String[] arr = url.split("/");
            Document doc = new Document("dir_name", arr[arr.length - 1])
                    .append("dir_code", arr[arr.length - 1])
                    .append("dir_type", dirType)
                    .append("last_code", null)
                    .append("status", 1)
                    .append("show_order", 0);
            dirCollection.insertOne(doc);
        } catch (Exception e) {
            logger.error("saveNav error , url is " + url, e);
        }
    }

    public void batchSaveNav(List<String> urls, int dirType) {
        if(urls == null || urls.size() < 1){
            return;
        }

        try {
            MongoCollection<Document> dirCollection = MongodbHelper.getDbCollection("fwpro", "fw_dir");
            List<Document> docs = new ArrayList<>();
            Document doc = null;
            for (String url : urls) {
                String[] arr = url.split("/");
                doc = new Document("dir_name", arr[arr.length - 1])
                        .append("dir_code", arr[arr.length - 1])
                        .append("dir_type", dirType)
                        .append("status", 1)
                        .append("show_order", 0);
                if (dirType != 1) {
                    doc.append("last_code", arr[arr.length - 2]);
                }
                docs.add(doc);
            }
            dirCollection.insertMany(docs);
        } catch (Exception e) {
            logger.error("batchSaveNav error", e);
        }
    }

    public static void saveDetailPage(long id, String title, String content, String source){
        MongoCollection<Document> collPage = MongodbHelper.getDbCollection("fwpro", "fw_page");
        String[] arr = source.split("/");
        Document doc = new Document("_id", id)
                .append("title", title)
                .append("source", source)
                .append("status", 1)
                .append("oneDir", arr[3])
                .append("twoDir", arr[4]);
        collPage.insertOne(doc);
        MongoCollection<Document> collCont = MongodbHelper.getDbCollection("fwpro", "fw_content");
        doc = new Document("_id", id)
                .append("content", content);
        collCont.insertOne(doc);

    }
}

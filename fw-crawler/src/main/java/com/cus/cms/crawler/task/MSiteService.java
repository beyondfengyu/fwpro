package com.cus.cms.crawler.task;

import com.cus.cms.crawler.StartMain;
import com.cus.cms.crawler.model.FwSeed;
import com.cus.cms.crawler.model.LinkMapper;
import com.cus.cms.crawler.util.DateTimeUtil;
import com.cus.cms.crawler.util.DbUtil;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 */
public class MSiteService {

    private static final Logger logger = LoggerFactory.getLogger(MSiteService.class);
    private MongoCollection<Document> docCollection;

    public MSiteService() {
        docCollection = MongodbHelper.getDbCollection("fwpro", "fw_url");
    }

    public MongoCollection<Document> getDocCollection() {
        return docCollection;
    }

    public void insertFwUrl(String linkUrl, Integer linkType, Integer siteType, Integer status) {
        try {
            Document document = new Document("link_url", linkUrl)
                    .append("site_type", siteType).append("link_type", linkType).append("status",status);
            docCollection.insertOne(document);
        } catch (Exception e) {
            logger.error("insertFwUrl error, linkUrl is: {}", linkUrl, e);
        }
    }

    public List<FwSeed> getSiteUrl() throws SQLException {
        final List<FwSeed> list = new ArrayList<>();
        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_seed");
        FindIterable<Document> iter = collection.find();
        iter.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                FwSeed seed = new FwSeed();
                seed.setSiteLink(document.getString("site_link"));
                seed.setSiteName(document.getString("site_name"));
                seed.setSiteType(document.getInteger("site_type"));
                seed.setStartId(document.getInteger("start_id"));
                seed.setThread(document.getInteger("thread"));
                list.add(seed);
            }
        });
        return list;
    }

    public List<String> getAllUrl(int siteType){
        final List<String> list = new ArrayList<>();
        FindIterable<Document> iter = docCollection.find(new Document("site_type",siteType));
        iter.forEach(new Block<Document>() {
            public void apply(Document doc) {
                list.add(doc.getString("link_url"));
            }
        });
        return list;
    }
}

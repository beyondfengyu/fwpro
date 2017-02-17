package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.cus.cms.crawler.util.DateTimeUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class SeedMain {

    public static void main(String[] args) throws Exception {

//        MongoCollection<Document> collection = MongodbHelper.getDbCollection("fwpro", "fw_link");
//        collection.drop();
//        while (true) {
//            System.out.println(DateTimeUtil.getCurrentTime() + " =====>>>> " + collection.count());
//            TimeUnit.SECONDS.sleep(3);
//        }
        String url = "http://www.diyifanwen.com/zuowen/xiaoxuedanyuanzuowen";
        DataService.getAllPageList(url, "http://www.diyifanwen.com");
    }
}

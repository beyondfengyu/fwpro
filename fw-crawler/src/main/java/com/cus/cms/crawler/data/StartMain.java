package com.cus.cms.crawler.data;

import com.cus.cms.crawler.mogodb.MongodbHelper;
import com.mongodb.client.MongoCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andy
 */
public class StartMain {

    private static ExecutorService executorService;

    public static void main(String[] args) throws Exception {
        executorService = Executors.newFixedThreadPool(16);

        String[] urls1 = {
                "http://www.diyifanwen.com/tool/mingrenjianjie", "名人简介",
                "http://www.diyifanwen.com/tool/mingrenmingyan", "名人名言",
                "http://www.diyifanwen.com/tool/duilian", "对联",
                "http://www.diyifanwen.com/tool/yuyan","寓言",
                "http://www.diyifanwen.com/tool/jingdianyuju", "经典语句",
                "http://www.diyifanwen.com/tool/geyan", "geyan",
                "http://www.diyifanwen.com/lizhi", "励志",
                "http://www.diyifanwen.com/sanwen", "散文",

                "http://www.diyifanwen.com/sms", "祝福语",
                "http://www.diyifanwen.com/sicijianshang", "诗词",
                "http://www.diyifanwen.com/zuowen", "作文",
                "http://www.diyifanwen.com/fanwen", "范文",
                "http://www.diyifanwen.com/guoxue", "国学",

                "http://www.diyifanwen.com/jiaoan", "教案"
        };

        Map<String, String> navMap = new HashMap<>();
         for(int i=0; i < urls1.length; ) {
            navMap.put(urls1[i], urls1[i + 1]);
            i = i + 2;
        }

        ConcurrentHashMap<String, String> dirMap = new ConcurrentHashMap<>();
        MongoCollection<org.bson.Document> dirCollection = MongodbHelper.getDbCollection("fwpro", "fw_dir");

        Set<String> urls = navMap.keySet();
        int i = 1;
        for (String url : urls) {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            Element nvEl = doc.getElementById("ClassNaviTxt");
            Elements als = nvEl.getElementsByTag("a");

            String lastcode = url.substring(url.lastIndexOf("/") + 1);
            org.bson.Document docs = new org.bson.Document("dir_name", navMap.get(url))
                    .append("dir_code", lastcode)
                    .append("dir_type", 1)
                    .append("last_code", null)
                    .append("status", 1)
                    .append("show_order", 0);
            dirCollection.insertOne(docs);

            for (Element el : als) {
                String txt = el.text();
                String link = el.attr("abs:href");
                if (link.endsWith("/") || link.endsWith("#")) {
                    link = link.substring(0, link.length() - 2);
                }
                String code = link.substring(link.lastIndexOf("/") + 1);
                if (dirMap.containsKey(txt)) {
                    System.out.println(String.format("已经存在=== %s ===>>> %s : %s", navMap.get(url), txt, link));
                    continue;
                }

                org.bson.Document dirdoc = new org.bson.Document("dir_name", txt)
                        .append("dir_code", code)
                        .append("dir_type", 2)
                        .append("last_code", lastcode)
                        .append("status", 1)
                        .append("show_order", 0);
                dirCollection.insertOne(dirdoc);

                System.out.println(String.format("%d %s ===>>> %s : %s  : %s", i++,navMap.get(url), txt, link, code));
            }
        }

    }
}

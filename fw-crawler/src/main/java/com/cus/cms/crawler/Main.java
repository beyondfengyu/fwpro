package com.cus.cms.crawler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class Main {

    private static String url = "http://www.diyifanwen.com/";

    public static void main(String[] args) throws IOException, InterruptedException {
//        Document doc = Jsoup.connect(url).timeout(5000).get();
//        Elements aels = doc.getElementsByTag("a");
//        List<String> list = new ArrayList<>();
//
//        for (Element el : aels) {
//            String href = el.attr("abs:href");
//            if(href.startsWith(url) && !list.contains(href)) {
//                list.add(el.attr("abs:href"));
//            }
//            System.out.println(el.html() + " : " + el.text() + ",  " + el.attr("abs:href"));
//        }
//
//        for (String url : list) {
//            doc = Jsoup.connect(url).timeout(5000).get();
//            aels = doc.getElementsByTag("a");
//            List<String> ulist = new ArrayList<>();
//            for (Element el : aels) {
//                String href = el.attr("abs:href");
//                if(href.startsWith(url) && !ulist.contains(href)) {
//                    ulist.add(el.attr("abs:href"));
//                }
//                System.out.println(el.html() + " : " + el.text() + ",  " + el.attr("abs:href"));
//            }
//
//        }
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        for(int i = 0 ;i < 5000000; i++) {
            map.put("http://http://www.diyifanwen.com/fanwen/jiaoshigongzuojihua/1611312195771202.htm" + i, 1);
        }
        System.out.println("finsh, size is: " + map.size());
        TimeUnit.SECONDS.sleep(30);
    }
}

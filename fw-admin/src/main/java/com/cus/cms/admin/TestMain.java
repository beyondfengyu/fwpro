package com.cus.cms.admin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 * @version 1.0
 * @date 2016/11/17
 */
public class TestMain {

    private static String url = "http://www.diyifanwen.com/";

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect(url).timeout(5000).get();
        Elements aels = doc.getElementsByTag("a");
        List<String> list = new ArrayList<>();

        for (Element el : aels) {
            String href = el.attr("abs:href");
            if(href.startsWith(url) && !list.contains(href)) {
                list.add(el.attr("abs:href"));
            }
            System.out.println(el.html() + " : " + el.text() + ",  " + el.attr("abs:href"));
        }

        for (String url : list) {
            doc = Jsoup.connect(url).timeout(5000).get();
            aels = doc.getElementsByTag("a");
            List<String> ulist = new ArrayList<>();
            for (Element el : aels) {
                String href = el.attr("abs:href");
                if(href.startsWith(url) && !ulist.contains(href)) {
                    ulist.add(el.attr("abs:href"));
                }
                System.out.println(el.html() + " : " + el.text() + ",  " + el.attr("abs:href"));
            }

        }
    }
}

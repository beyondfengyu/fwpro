package com.cus.cms.crawler;

import com.cus.cms.crawler.model.LinkMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy
 */
public class CrawService {
    private static final Logger logger = LoggerFactory.getLogger(CrawService.class);
    /**
     *  获取网页的所有符合要求的<a>标签的href属性值
     * @param url
     * @param prxUrl
     * @param siteType
     * @return
     */
    public static List<LinkMapper> getAllATag(String url, String prxUrl, int siteType) {
        List<LinkMapper> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            if (doc != null) {
                Elements aels = doc.getElementsByTag("a");
                for (Element el : aels) {
                    String href = el.attr("abs:href");
                    if (href.startsWith(prxUrl) && !list.contains(href)) {
                        LinkMapper mapper = new LinkMapper();
                        String linkUrl = el.attr("abs:href");
                        if (linkUrl.endsWith("#") || linkUrl.endsWith("/")) {
                            linkUrl = linkUrl.substring(0, linkUrl.length() - 1);
                        }
                        mapper.setLinkUrl(el.attr("abs:href"));
                        mapper.setSiteType(siteType);
                        mapper.setSource(url);
                        list.add(mapper);
                    }
                }
            }
        } catch (IOException e) {

        }
        return list;
    }

    public static List<String> getAllATagList(String url, String prxUrl, int siteType) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).timeout(35000).get();
            if (doc != null) {
                Elements aels = doc.getElementsByTag("a");
                for (Element el : aels) {
                    String href = el.attr("abs:href");
                    if (href.startsWith(prxUrl) && !list.contains(href)) {
                        String linkUrl = el.attr("abs:href");
                        if (linkUrl.endsWith("#") || linkUrl.endsWith("/")) {
                            linkUrl = linkUrl.substring(0, linkUrl.length() - 1);
                        }
                        if (linkUrl.endsWith(".gif") || linkUrl.endsWith(".jpg") || linkUrl.endsWith(".png")
                                || linkUrl.endsWith(".bmp")
                                || linkUrl.endsWith(".rar")
                                || linkUrl.endsWith(".swf")
                                || linkUrl.endsWith(".zip")
                                || linkUrl.endsWith(".ZIP")
                                || linkUrl.endsWith(".RAR")
                                || linkUrl.endsWith(".SWF")
                                || linkUrl.endsWith(".GIF")
                                || linkUrl.endsWith(".JPG")
                                || linkUrl.endsWith(".PNG")
                                || linkUrl.endsWith(".BMP")) {
                            continue;
                        }
                        list.add(linkUrl);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("getAllATagList url is :" + url );
            throw e;
        }
        return list;
    }

    /**
     *
     * @param url http://www.diyifanwen.com/qt/wzdt.htm
     * @return
     */
    public static List<String> getAllMap(String url, String prxUrl) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).timeout(5000).get();
            if (doc != null) {
                Element boxEl = doc.getElementsByClass("box1").first();
                Elements aels = boxEl.getElementsByTag("a");
                for (Element el : aels) {
                    String href = el.attr("abs:href");
                    if (href.startsWith(prxUrl) && !list.contains(href)) {
                        String linkUrl = el.attr("abs:href");
                        if (linkUrl.endsWith("#") || linkUrl.endsWith("/")) {
                            linkUrl = linkUrl.substring(0, linkUrl.length() - 1);
                        }
                        logger.info("map url ====>>> {},{}", el.text(), linkUrl);
                        list.add(linkUrl);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("getAllMap url is :" + url );
            throw e;
        }
        return list;
    }
}

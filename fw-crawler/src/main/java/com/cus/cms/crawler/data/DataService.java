package com.cus.cms.crawler.data;

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
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    public static List<String> getOneNavList(String url, String prxUrl) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            if (doc != null) {
                Element IndexDl = doc.getElementsByClass("IndexDl").first();
                Elements dts = IndexDl.getElementsByTag("dt");
                for (Element dt : dts) {
                    try {
                        if (dt.getElementsByTag("a") == null || dt.getElementsByTag("a").size() < 1) {
                            continue;
                        }
                        Element el = dt.getElementsByTag("a").first();
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
                    } catch (Exception e) {
                        logger.error("getOneNavList error, element html is: " + dt.html(), e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("getOneNavList url is :" + url);
            throw e;
        }
        return list;
    }

    public static List<String> getTwoNavList(String url, String prxUrl) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            if (doc != null) {
                Elements dts = doc.getElementsByClass("SLmore");
                for (Element el : dts) {
                    try {
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
                    } catch (Exception e) {
                        logger.error("getTwoNavList error, element html is: " + el.html(), e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("getTwoNavList url is :" + url);
            throw e;
        }
        return list;
    }

    public static List<String> getAllPageList(String url, String prxUrl) throws Exception {
        List<String> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            if (doc != null) {
                Element CutPage = doc.getElementById("CutPage");
                if (CutPage == null) {
                    // 解析出列表页的所有链接
                    getPageList(url, prxUrl, list);
                    return list;
                }
                Elements pels = CutPage.getElementsContainingOwnText("尾页");
                if (pels == null || pels.size() < 1) {
                    logger.error("CutPage get last is null , url is: " + url);
                    // 解析出列表页的所有链接
                    getPageList(url, prxUrl, list);
                    return list;
                }
                String lastIndex = pels.first().attr("abs:href");
                if (lastIndex == null) {
                    logger.error("CutPage get last no href Attributter , url is: " + url);
                    // 解析出列表页的所有链接
                    getPageList(url, prxUrl, list);
                    return list;
                }

                //分析尾页链接，获取总页数
                String[] arr = lastIndex.split("index_");
                String num = arr[1].split("\\.")[0];
                String form = arr[0] + "index_%d.html";

                for (int i = 1; i <= Integer.valueOf(num); i++) {
                    String page = url;
                    if (i != 1) {
                        page = String.format(form, i);
                    }
                    // 解析出列表页的所有链接
                    getPageList(page, prxUrl, list);
                }


            }
        } catch (Exception e) {
            logger.error("getAllPageList error, url is :" + url, e);
            throw e;
        }
        return list;
    }

    /**
     *  解析列表页，获取其中所有有效的链接
     * @param page
     * @param prxUrl
     * @param list
     * @throws IOException
     */
    public static void getPageList(String page, String prxUrl, List<String> list) throws IOException {
        try {
            Document doc = Jsoup.connect(page).timeout(10000).get();
            Element AListBox = doc.getElementById("AListBox");
            Element ulEl = AListBox.getElementsByTag("ul").first();
            Elements aels = ulEl.getElementsByTag("a");
            for (Element el : aels) {
                String href = el.attr("abs:href");
                if (href.startsWith(prxUrl) && !list.contains(href)) {
                    String linkUrl = el.attr("abs:href");
                    if (linkUrl.endsWith("#") || linkUrl.endsWith("/")) {
                        linkUrl = linkUrl.substring(0, linkUrl.length() - 1);
                    }
                    logger.info("list url ====>>> {},{}", el.text(), linkUrl);
                    list.add(linkUrl);
                }
            }
        } catch (Exception e) {
            logger.error("getAllPageList parse page error, url is :" + page, e);
        }
    }
}

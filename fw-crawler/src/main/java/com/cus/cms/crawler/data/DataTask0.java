package com.cus.cms.crawler.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class DataTask0 extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(DataTask0.class);

    public DataTask0(String url, String prxUrl) {
        super(url, prxUrl);
    }


    @Override
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> twoUrls = new ArrayList<String>();
                    twoUrls.add(url);
                    moreUrls.addAll(twoUrls);
                } catch (Exception e) {
                    logger.error("DataTask1 error, ", e);
                }
            }
        });

        for(int i = 0; i < 4; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String moreUrl = moreUrls.poll(30, TimeUnit.SECONDS);
                            if (moreUrl == null) {
                                logger.info("execute more url wait 60 second , but no url");
                                break;
                            }
                            List<String> list = DataService.getAllPageList(moreUrl, prxUrl);
                            storeService.saveUrl(list, 1);
                        } catch (Exception e) {
                            logger.error("execute more url error", e);
                        }
                    }
                }
            });
        }
    }

    private static ExecutorService executorService;

    public static void main(String[] args) throws Exception {
        executorService = Executors.newFixedThreadPool(8);
//        String url = "http://www.diyifanwen.com/zuowen";
//        Document doc = Jsoup.connect(url).get();
//        Element ZwClassDl = doc.getElementsByClass("ZwClassDl").first();
//        Elements als = ZwClassDl.getElementsByTag("a");
//
//        List<String> urls0 = new ArrayList<>();
//        for (Element el : als) {
//            urls0.add(el.attr("abs:href"));
//            System.out.println("url : " + el.attr("abs:href"));
//        }

        String[] urls0 = {
                "http://www.diyifanwen.com/tool/xingyeduilian",
                "http://www.diyifanwen.com/jiaoan/gaozhongxinxijishujiaoan",
        };

//
        for (String nav : urls0) {
            executorService.execute(new DataTask0(nav, "http://www.diyifanwen.com"));
//            TimeUnit.MINUTES.sleep(10);
        }

    }
}

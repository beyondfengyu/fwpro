package com.cus.cms.crawler.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class DataTask2 extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(DataTask2.class);

    public DataTask2(String url, String prxUrl) {
        super(url, prxUrl);
    }

    @Override
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> oneUrls = DataService.getOneNavList(url, prxUrl);
                    for (String str : oneUrls) {
                        List<String> twoUrls = null;
                        logger.info("oneUrls =====>>> {}", str);
                        if ("http://www.diyifanwen.com/zuowen/danyuanzuowen".equals(str)) {
                            List<String> twoUrls2 = DataService.getTwoNavList(str, prxUrl);
                            twoUrls = new ArrayList<String>();
                            for (String str2 : twoUrls2) {
                                logger.info("twoUrls =====>>> {}", str2);
                                List<String> threeUrls = DataService.getTwoNavList(str2, prxUrl);
                                twoUrls.addAll(threeUrls);
                            }
                        }else {
                            twoUrls = DataService.getTwoNavList(str, prxUrl);
                        }
                        moreUrls.addAll(twoUrls);
                    }

                } catch (Exception e) {
                    logger.error("DataTask2 error, ", e);
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

        String[] urls2 = {
                "http://www.diyifanwen.com/sms",
                "http://www.diyifanwen.com/sicijianshang",
                "http://www.diyifanwen.com/zuowen",
                "http://www.diyifanwen.com/fanwen",
                "http://www.diyifanwen.com/guoxue"
        };


        for (String nav : urls2) {
            executorService.execute(new DataTask2(nav,"http://www.diyifanwen.com"));
//            TimeUnit.MINUTES.sleep(10);
        }


    }
}

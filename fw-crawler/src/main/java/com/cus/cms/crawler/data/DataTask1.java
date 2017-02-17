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
public class DataTask1 extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(DataTask1.class);

    public DataTask1(String url, String prxUrl) {
        super(url, prxUrl);
    }


    @Override
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> twoUrls = DataService.getTwoNavList(url, prxUrl);
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
        executorService = Executors.newFixedThreadPool(16);

        String[] urls1 = {
                "http://www.diyifanwen.com/sms/zhufuduanxin",
                "http://www.diyifanwen.com/tool/chunlianjijin",
                "http://www.diyifanwen.com/tool/mingrenjianjie",
                "http://www.diyifanwen.com/tool/naojinjizhuanwan",
                "http://www.diyifanwen.com/tool/mingrenmingyan",
                "http://www.diyifanwen.com/tool/duilian",
                "http://www.diyifanwen.com/tool/yuyan",
                "http://www.diyifanwen.com/tool/jingdianyuju",
                "http://www.diyifanwen.com/tool/geyan",
                "http://www.diyifanwen.com/lizhi",
                "http://www.diyifanwen.com/sanwen"
        };


        for (String nav : urls1) {
            executorService.execute(new DataTask1(nav, "http://www.diyifanwen.com"));
//            TimeUnit.MINUTES.sleep(10);
        }


    }
}

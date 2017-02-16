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
                    storeService.saveNav(url, 1);
                    List<String> twoUrls = DataService.getTwoNavList(url, prxUrl);
                    moreUrls.addAll(twoUrls);
                    storeService.batchSaveNav(twoUrls, 2);
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
}

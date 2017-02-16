package com.cus.cms.crawler.data;

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
public class DataTask3 extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(DataTask3.class);

    public DataTask3(String url, String prxUrl) {
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
                    List<String> oneUrls = DataService.getOneNavList(url, prxUrl);
                    List<String> twoUrls = new ArrayList<String>();
                    for (String str : oneUrls) {
                        logger.info("oneUrls =====>>> {}", str);
                        twoUrls.addAll(DataService.getTwoNavList(str, prxUrl));

                    }
                    for (String str : twoUrls) {
                        logger.info("twoUrls =====>>> {}", str);
                        List<String> threeUrls = DataService.getTwoNavList(str, prxUrl);
                        storeService.batchSaveNav(threeUrls, 2);
                        moreUrls.addAll(threeUrls);
                    }
                } catch (Exception e) {
                    logger.error("DataTask3 error, ", e);
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

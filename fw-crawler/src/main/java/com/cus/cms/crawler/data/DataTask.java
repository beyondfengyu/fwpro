package com.cus.cms.crawler.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Andy
 */
public class DataTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataTask.class);

    private LinkedBlockingQueue<String> moreUrls;
    private String prxUrl;
    private String url;

    public DataTask(String url, String prxUrl) {
        this.url = url;
        this.prxUrl = prxUrl;
        this.moreUrls = new LinkedBlockingQueue<>();
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
                        logger.info("oneUrls =====>>> {}", str);
                        List<String> twoUrls = DataService.getTwoNavList(str, prxUrl);
                        moreUrls.addAll(twoUrls);
                    }

                } catch (Exception e) {
                    logger.error("DataTask error, ", e);
                }
            }
        });

        for(int i = 0; i < 4; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String moreUrl = moreUrls.poll(60, TimeUnit.SECONDS);
                            if (moreUrl == null) {
                                logger.info("execute more url wait 60 second , but no url");
                                break;
                            }
                            List<String> list = DataService.getAllPageList(moreUrl, prxUrl);
                        } catch (Exception e) {
                            logger.error("execute more url error", e);
                        }
                    }
                }
            });
        }
    }
}

package com.cus.cms.crawler.task;

import com.cus.cms.crawler.CrawService;
import com.cus.cms.crawler.SiteService;
import com.cus.cms.crawler.model.FwSeed;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Andy
 */
public class SiteTask2 extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(SiteTask2.class);

    private ConcurrentHashMap<String, Integer> urlMap;
    private LinkedBlockingQueue<String> urlQueue;
    private LinkedBlockingQueue<String> newQueue;
    private FwSeed fwSeed;
    private MSiteService siteService;


    public SiteTask2(FwSeed fwSeed) {
        this.fwSeed = fwSeed;
        this.siteService = new MSiteService();
        this.urlMap = new ConcurrentHashMap<>();
        this.urlQueue = new LinkedBlockingQueue<>();
        this.newQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(fwSeed.getThread() + 1);

        List<String> list = siteService.getAllUrl(fwSeed.getId());
        if (list.size() < 1) {
            list.add(fwSeed.getSiteLink());
        }

        for (String str : list) {
            urlMap.put(str, 1);
        }
        urlQueue.addAll(list.subList(740000, list.size()));

        // 启动多个线程同时工作
        for (int i = 0; i < fwSeed.getThread(); i++) {
            // 爬取网页数据
            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (true) {
                            String linkUrl = urlQueue.take();
                            // 通过爬虫爬取当前网页的所有链接元素
                            List<String> hrefs = null;
                            try {
                                hrefs = CrawService.getAllATagList(linkUrl, fwSeed.getSiteLink(), fwSeed.getId());
                                newQueue.addAll(hrefs);
                            } catch (IOException e) {
                                urlQueue.put(linkUrl);
                            }

                        }
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // 执行插入DB工作
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                logger.info("invoke batchInsert method run start ======");
                while (true) {
                    try {
                        String href = newQueue.take();
                        if (!urlMap.containsKey(href)) {
                            int type = 1;
                            if (href.endsWith("html") || href.endsWith("html") || href.endsWith("shtml")) {
                                type = 2;
                            }
                            urlMap.put(href, type);
                            urlQueue.put(href);
                            siteService.insertFwUrl(href,type, fwSeed.getId());
                            logger.info("======>>>> new url : {}", href);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

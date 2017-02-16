package com.cus.cms.crawler.data;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Andy
 */
public abstract  class BaseTask implements Runnable {

    protected LinkedBlockingQueue<String> moreUrls;
    protected String prxUrl;
    protected String url;

    protected StoreService storeService;

    public BaseTask(String url, String prxUrl) {
        this.url = url;
        this.prxUrl = prxUrl;
        this.moreUrls = new LinkedBlockingQueue<>();
        this.storeService = new StoreService("fwpro", "fw_link");
    }
}

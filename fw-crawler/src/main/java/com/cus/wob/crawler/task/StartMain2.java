package com.cus.wob.crawler.task;

import com.cus.wob.crawler.SiteService;
import com.cus.wob.crawler.SiteTask;
import com.cus.wob.crawler.model.FwSeed;
import com.cus.wob.crawler.model.LinkMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Andy
 */
public class StartMain2 {
    public static LinkedBlockingQueue<LinkMapper> queue = new LinkedBlockingQueue();
    private static ExecutorService executorService;

    public static void main(String[] args) throws SQLException {
        SiteService service = new SiteService();
        executorService = Executors.newFixedThreadPool(8);

        List<FwSeed> list = service.getSiteUrl();
        for (FwSeed fwUrl : list) {
            System.out.println("fwurl is: " + fwUrl.toString());
            executorService.execute(new SiteTask2(fwUrl));
        }
        new SaveThread(service).start();
    }

    static class SaveThread extends Thread{

        private SiteService siteService;

        public SaveThread(SiteService siteService) {
            this.siteService = siteService;
        }

        @Override
        public void run(){
            try {
                LinkMapper mapper = null;
                while (true) {
                    mapper = queue.take();
                    siteService.savePwUrl(mapper.getSource(), mapper.getLinkUrl(), mapper.getSiteType());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

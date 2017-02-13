package com.cus.cms.crawler.task;

import com.cus.cms.crawler.task.MSiteService;
import com.cus.cms.crawler.model.FwSeed;
import com.cus.cms.crawler.model.LinkMapper;

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
        MSiteService service = new MSiteService();
        executorService = Executors.newFixedThreadPool(8);

        List<FwSeed> list = service.getSiteUrl();
        for (FwSeed fwUrl : list) {
            System.out.println("fwurl is: " + fwUrl.toString());
            executorService.execute(new SiteTask2(fwUrl));
        }
        new SaveThread(service).start();
    }

    static class SaveThread extends Thread{

        private MSiteService siteService;

        public SaveThread(MSiteService siteService) {
            this.siteService = siteService;
        }

        @Override
        public void run(){
            try {
                LinkMapper mapper = null;
                while (true) {
                    mapper = queue.take();
//                    siteService.savePwUrl(mapper.getSource(), mapper.getLinkUrl(), mapper.getSiteType());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

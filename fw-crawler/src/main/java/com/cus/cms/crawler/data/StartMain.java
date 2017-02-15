package com.cus.cms.crawler.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andy
 */
public class StartMain {

    private static ExecutorService executorService;

    public static void main(String[] args) throws Exception {
        executorService = Executors.newFixedThreadPool(8);

       String[] navurls = {"http://www.diyifanwen.com/fanwen", "http://www.diyifanwen.com/guoxue/"};
        for (String nav : navurls) {
            executorService.execute(new DataTask(nav,"http://www.diyifanwen.com"));
        }

    }
}

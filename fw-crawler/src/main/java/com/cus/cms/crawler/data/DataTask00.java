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
public class DataTask00 extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(DataTask00.class);

    public DataTask00(String url, String prxUrl) {
        super(url, prxUrl);
    }


    @Override
    public void run() {
        try {
            List<String> list = new ArrayList<String>();
            DataService.getPageList(url, prxUrl, list);
            storeService.saveUrl(list, 1);
        } catch (Exception e) {
            logger.error("execute more url error", e);
        }
    }

    private static ExecutorService executorService;

    public static void main(String[] args) throws Exception {
        executorService = Executors.newFixedThreadPool(16);

        String[] urls00 = {
                "http://www.diyifanwen.com/zuowen/yingyuzuowen/zkyyzw/index_11.htm"
        };

        for (String nav : urls00) {
            executorService.execute(new DataTask00(nav, "http://www.diyifanwen.com"));
//            TimeUnit.MINUTES.sleep(10);
        }


    }
}

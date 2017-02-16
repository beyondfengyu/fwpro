package com.cus.cms.crawler.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andy
 */
public class StartMain {

    private static ExecutorService executorService;

    public static void main(String[] args) throws Exception {
        executorService = Executors.newFixedThreadPool(16);

        String[] urls1 = {
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
        String[] urls2 = {
                "http://www.diyifanwen.com/sms",
                "http://www.diyifanwen.com/sicijianshang",
                "http://www.diyifanwen.com/zuowen",
                "http://www.diyifanwen.com/fanwen",
                "http://www.diyifanwen.com/guoxue"
        };
        String[] urls3 = {
                "http://www.diyifanwen.com/jiaoan"
        };

        for (String nav : urls1) {
            executorService.execute(new DataTask1(nav, "http://www.diyifanwen.com"));
        }

        for (String nav : urls2) {
            executorService.execute(new DataTask2(nav,"http://www.diyifanwen.com"));
        }

        for (String nav : urls3) {
            executorService.execute(new DataTask3(nav,"http://www.diyifanwen.com"));
        }

    }
}

package com.cus.cms.admin.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Andy  2015/12/7.
 * @description
 */
public class HtmlTask {
    private static final Logger logger = LoggerFactory.getLogger(HtmlTask.class);
    private static Lock lock = new ReentrantLock();

    private static ExecutorService servicePool = Executors.newCachedThreadPool();

    /**
     * @throws IOException
     */
    public static void executePiaoHua() throws IOException {
        logger.info("execute timer task ======");
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
//    public static ResUrlDao getResUrlDao(){
//        if(resUrlDao==null){
//            resUrlDao = (ResUrlDao) SpringContextUtil.getBean("resUrlDao");
//        }
//        return resUrlDao;
//    }
//
//    public static ResVideoDao getResVideoDao(){
//        if(resVideoDao==null){
//            resVideoDao = (ResVideoDao) SpringContextUtil.getBean("resVideoDao");
//        }
//        return resVideoDao;
//    }
    public static void main(String[] arges) throws IOException {
        executePiaoHua();
    }

}

package com.cus.cms.admin.task;


import com.cus.cms.common.frame.ioc.SpringContextUtil;
import com.cus.cms.common.freemarker.Engine;
import com.cus.cms.common.util.PropertyUtil;

import java.util.Map;

/**
 * @author Andy  2016/2/25.
 * @description
 */
public class BaseTask {
    private static Engine engine;
//    private static ResNavDao resNavDao;
//    private static ResVideoDao resVideoDao;
//    private static VideoTypeDao videoTypeDao;
//    private static ResRecommendDao recommendVideoDao;
//    private static ResHotWordDao resHotWordDao;
//
//    protected static ResRecommendDao getRecommendVideoDao(){
//        if(recommendVideoDao==null){
//            recommendVideoDao = (ResRecommendDao)SpringContextUtil.getBean("resRecommendDao");
//        }
//        return recommendVideoDao;
//    }
//
//    protected static ResVideoDao getResVideoDao(){
//        if(resVideoDao==null){
//            resVideoDao = (ResVideoDao) SpringContextUtil.getBean("resVideoDao");
//        }
//        return resVideoDao;
//    }
//
//    protected static VideoTypeDao getVideoTypeDao(){
//        if(videoTypeDao==null){
//            videoTypeDao = (VideoTypeDao)SpringContextUtil.getBean("videoTypeDao");
//        }
//        return videoTypeDao;
//    }
//
//    protected static ResHotWordDao getResHotWordDao(){
//        if(resHotWordDao==null){
//            resHotWordDao = (ResHotWordDao)SpringContextUtil.getBean("resHotWordDao");
//        }
//        return resHotWordDao;
//    }
//
//    protected static ResNavDao getResNavDao(){
//        if(resNavDao==null){
//            resNavDao = (ResNavDao)SpringContextUtil.getBean("resNavDao");
//        }
//        return resNavDao;
//    }

    /**
     * 生成文件在默认的路径下
     * @param datas
     * @param ftlPath
     * @param outPath 输出的文件路径，该路径在默认的目录下
     */
    protected static void generateWithBasePath(Map<String,Object> datas,String ftlPath,String outPath){
        outPath = PropertyUtil.getValByKey("static_base_path") + outPath;
        generate(datas,ftlPath,outPath);
    }

    protected static void generateMobileWithBasePath(Map<String,Object> datas,String ftlPath,String outPath){
        outPath = PropertyUtil.getValByKey("static_mobile_base_path") + outPath;
        generate(datas,ftlPath,outPath);
    }

    /**
     * 生成文件在指定的详细路径下
     * @param datas
     * @param ftlPath
     * @param outPath 详细输出路径
     */
    protected static void generate(Map<String,Object> datas,String ftlPath,String outPath){
        getEngine().writeHtml(datas, ftlPath, outPath);
    }

    private static Engine getEngine(){
        if(engine==null){
            engine = (Engine) SpringContextUtil.getBean("engine");
        }
        return engine;
    }
}

package com.cus.cms.admin.task;

import com.cus.cms.common.frame.ioc.SpringContextUtil;
import com.cus.cms.service.wen.GenerHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andy
 */
public class StaticTask extends BaseTask{

    private static final Logger logger = LoggerFactory.getLogger(StaticTask.class);

    private static GenerHtmlService generHtmlService;

    public static void generIndexPage() {


    }

    public static void generDirsPage() {

    }

    public static void generDetailPage() {
        Map<String, Object> data = new HashMap<>();
        String current = getGenerHtmlService().test();
        data.put("time", current);
        generateWithBasePath(data, "detail.ftl", "/data/static.html");
        logger.info("=====>>>execute generDetailPage");
    }

    private static GenerHtmlService getGenerHtmlService() {
        if (generHtmlService == null) {
            generHtmlService = (GenerHtmlService) SpringContextUtil.getBean("generHtmlService");
        }
        return generHtmlService;
    }
}

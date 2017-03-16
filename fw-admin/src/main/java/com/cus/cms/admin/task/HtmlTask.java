package com.cus.cms.admin.task;

import com.cus.cms.common.frame.ioc.SpringContextUtil;
import com.cus.cms.common.model.wen.FwDir;
import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.dao.wen.FwContentDao;
import com.cus.cms.dao.wen.FwDirDao;
import com.cus.cms.dao.wen.FwPageDao;
import com.cus.cms.service.wen.GenerHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Andy  2015/12/7.
 * @description
 */
@Component("htmlTask")
public class HtmlTask extends BaseTask{
    private static final Logger logger = LoggerFactory.getLogger(HtmlTask.class);
    private static Lock lock = new ReentrantLock();

    @Autowired
    private FwDirDao fwDirDao;
    @Autowired
    private FwPageDao fwPageDao;
    @Autowired
    private FwContentDao fwContentDao;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 1)
    public void generIndexPage() {
        List<FwDir> dirs = fwDirDao.queryParentFwDirs();


    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 1)
    public void generDirsPage() {

    }

    @Scheduled(fixedDelay = 1000 * 3)
    public void generDetailPage() {
        Map<String, Object> data = new HashMap<>();
        String current = DateTimeUtil.getCurrentTime();
        data.put("time", current);
        generateWithBasePath(data, "detail.ftl", "/data/static.html");
        logger.info("=====>>>execute generDetailPage");
    }

}

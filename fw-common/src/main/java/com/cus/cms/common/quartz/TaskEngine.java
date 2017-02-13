package com.cus.cms.common.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
//import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * 读取任务配置文件的任务信息，并且调用定时器安排任务执行
 * @author Andy
 * @version 1.0
 * @date 2016/8/4
 */
public class TaskEngine {
    private static final Logger m_logger = LoggerFactory.getLogger(TaskEngine.class);

    private WorkScheduler workScheduler;
    private TaskContext taskContext;

    public TaskEngine(String config) {
        taskContext = new TaskContext(config);
        workScheduler = new WorkScheduler();
    }

    /**
     * 获取任务，并安排执行
     */
    public void scheduleTask() {
        JSONArray array = taskContext.loadTask();
        for (int i = 0; i < array.size(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (!TaskContext.SCOPE_MASTER.equals(object.getString(TaskContext.TARGET_SCOPE))) {
                    continue;
                }
                String clazz = object.getString(TaskContext.TARGET_CLASS);
                String method = object.getString(TaskContext.TARGET_METHOD);
                String cronExpression = object.getString(TaskContext.CRON_EXPRESSION);
                workScheduler.executeCronSchedule(Class.forName(clazz), method, cronExpression);
                m_logger.info("scheduleTask task{ class:" + clazz + ", method:" + method + ", cronExpression:" + cronExpression + "}");
            } catch (Exception e) {
                m_logger.error("scheduleTask init task error", e);
            }
        }
    }
}

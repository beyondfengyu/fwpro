package com.cus.wob.quartz;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

public class ReflectionJob implements Job {

    private static Logger m_logger = LoggerFactory.getLogger(WorkScheduler.class);

    @Override
    public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
        try {
            JobDataMap map = paramJobExecutionContext.getTrigger().getJobDataMap();
            Class<?> jobClass = (Class<?>) map.get("jobClass");
            String methodName = map.getString("methodName");
            m_logger.info("ReflectionJob jobClass is: " + jobClass + ",   methodName is: " + methodName);
            Method method = null;
            try {
                method = jobClass.getMethod(methodName);
                method.invoke(this);
            } catch (NoSuchMethodException e) {
                method = jobClass.getMethod(methodName, new Class[]{JobExecutionContext.class});
                method.invoke(this, paramJobExecutionContext);
            }
        } catch (Exception e) {
            m_logger.error("ReflectionJob error.", e);
        }
    }

}

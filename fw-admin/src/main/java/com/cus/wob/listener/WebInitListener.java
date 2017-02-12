package com.cus.wob.listener;

import com.cus.wob.frame.mvc.ContextUtil;
import com.cus.wob.quartz.WorkScheduler;
import com.cus.wob.task.HtmlTask;
import com.cus.wob.util.PropertyUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Andy  2016/2/25.
 * @description 容器启动的监听器
 */
public class WebInitListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 在web项目中存在两个容器，一个是root application context,另一个是servlet context(作为root context的子容器)，
     * 这样就会造成onApplicationEvent方法被调用两次，为了避免上面的问题，我们只在root context完成初始化时执行代码
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) { //容器Bean初始化完成后
            PropertyUtil.addConfigFile(ContextUtil.getContextPath() + "/config/server.properties");

            //定时获取资源网站最新的资源0 0 */6 * * ?
            WorkScheduler.executeCronSchedule(HtmlTask.class, "executePiaoHua", PropertyUtil.getValByKey("executePiaoHua"));
        }
    }
}

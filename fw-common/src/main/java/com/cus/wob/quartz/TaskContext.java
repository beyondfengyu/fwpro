package com.cus.wob.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cus.wob.util.Dom4jXmlParser;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import java.net.URL;
import java.util.List;

/**
 * 定时任务上下文，加载任务的配置信息
 * 如：任务类、方法名、CRON表达式、执行范围（是否可执行）
 *
 * @author laochunyu
 * @version 1.0
 * @date 2016/8/4
 */
public class TaskContext {
    private static final Logger m_logger = LoggerFactory.getLogger(TaskContext.class);
    public static final String TARGET_CLASS = "targetClass";
    public static final String TARGET_METHOD = "targetMethod";
    public static final String CRON_EXPRESSION = "cronExpression";
    public static final String TARGET_SCOPE = "scope";
    public static final String SCOPE_MASTER = "master";
    private String configPath;

    /**
     * 加载类路径下默认的配置文件(task_config.xml)
     */
    public TaskContext() {
        URL uri = Thread.currentThread().getContextClassLoader().getResource("task_config.xml");
        this.configPath = uri.getPath();
    }

    public TaskContext(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 加载配置的任务信息，如类名、方法名、cron表达式
     *
     * @return
     */
    public JSONArray loadTask() {
        JSONArray jsonArray = new JSONArray();
        Dom4jXmlParser xmlParser = new Dom4jXmlParser(this.configPath);
        Element root = xmlParser.getRoot();
        List<Element> jobs = root.elements("job");
        for (Element job : jobs) {
            JSONObject jsonObject = new JSONObject();
            // node target-class
            Element clazzEl = job.element(TARGET_CLASS);
            jsonObject.put(TARGET_CLASS, clazzEl.getText());
            // node method
            Element methodEl = job.element(TARGET_METHOD);
            jsonObject.put(TARGET_METHOD, methodEl.getText());
            // node cron expression
            Element cronEl = job.element(CRON_EXPRESSION);
            jsonObject.put(CRON_EXPRESSION, cronEl.getText());
            // node scope
            Element scopeEl = job.element(TARGET_SCOPE);
            jsonObject.put(TARGET_SCOPE, scopeEl.getText());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


}

package com.cus.wob.freemarker;

import com.cus.wob.frame.thread.BaseThreadFactory;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author laochunyu  2015/12/7.
 * @description 使用FreeMarker来静态化HTML页面
 */
public class HtmlStatic {
    private static final Logger m_logger = LoggerFactory.getLogger(HtmlStatic.class);
    private ScheduledExecutorService executorService;   //线程池
    private Engine engine;  //freemarker模板引擎

    /**
     *  初始化
     * @param poolSize 线程池大小
     * @param ftlDir 模板所在的目录
     */
    public HtmlStatic(int poolSize, String ftlDir){
        engine = new Engine(ftlDir);
        BaseThreadFactory btf = new BaseThreadFactory("htmlstatic");
        executorService = Executors.newScheduledThreadPool(poolSize,btf);
    }

    /**
     * 生成HTML文件
     * @param destPath 目标路径
     * @param ftlPath 模板路径
     * @param data 模板数据
     */
    public void generateHtml(String destPath,String ftlPath,Object data){
//        executorService.scheduleWithFixedDelay()
    }

    class HtmlTask implements Runnable{
        private String ftlPath;
        private String destPath;
        private Object data;

        public HtmlTask(String ftlPath,String destPath,Object data){
            this.ftlPath = ftlPath;
            this.destPath = destPath;
            this.data = data;
        }

        @Override
        public void run(){
            Template template = engine.getTemplate(ftlPath);
            engine.writeFromTemplate(template,data,destPath);
        }

    }
}

package com.cus.wob.freemarker;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.cus.wob.frame.mvc.ContextUtil;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;

/**
 * @author laochunyu 2015-7-21
 * @description 
 */
@Component("engine")
public class Engine {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static Configuration configuration;//配置对象

	public Engine(String ftlDir){
		if(configuration == null) {
			try{
				configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
				configuration.setDirectoryForTemplateLoading(new File(ContextUtil.getContextPath()+ftlDir));
				configuration.setDefaultEncoding("utf-8");
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
//					Properties p = new Properties();
//					p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/config/freemarker.properties"));
//					configuration.setSettings(p);
			}catch (Exception e) {
				logger.warn("getConfiguration fail, ftlDir is "+ftlDir, e);
			}
		}
	}

	/**
	 * 获取指定路径的模板对象
	 * @param ftlPath  模板文件名
	 * @return
	 * @throws IOException
	 */
	public Template getTemplate(String ftlPath){
		Template template = null;
		try {
			if(configuration == null){
				logger.warn("Configuration object has not init");
			}else{
				template = configuration.getTemplate(ftlPath);
			}
		} catch (Exception e) {
			logger.warn("get templat object fail, cause by " + e.getMessage(), e);
		}
		return template;
	}

	/**
	 * 生成HTML文件
	 * @param data 数据
	 * @param ftlPath 模板文件路径
	 * @param outPath 输出文件路径
	 */
	public void writeHtml(Object data,String ftlPath,String outPath){
		Template template = getTemplate(ftlPath);
		File outFile = new File(outPath);
		//如果输出文件目录不存在，则创建目录
		if(!outFile.getParentFile().exists()){
			outFile.getParentFile().mkdirs();
		}
		writeFromTemplate(template,data,outPath);
	}
	/**
	 * 合并模板和数据，输出内容
	 * @param template 模板对象
	 * @param obj 数据对象
	 * @param outPath 输出的文件路径
	 */
	public void writeFromTemplate(Template template, Object obj,String outPath) {
		Writer out = null;
		try{
			File file = new File(outPath);
			out = new OutputStreamWriter(new FileOutputStream(outPath),"utf-8");
			writeFromTemplate(template,obj, out);
		} catch (Exception e) {
			logger.error("writeFromTemplate error, cause by "+e.getMessage(), e);
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					logger.warn("close Writer object fail",e);
				}
			}
		}
	}
	/**
	 * 合并数据和模板，输出内容
	 * @param obj 数据对象
	 * @param os 输出流
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void writeFromTemplate(Template template, Object obj, Writer os) throws TemplateException, IOException {
		try{
			template.process(obj, os);
			os.flush();
		}finally{
			try {
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				logger.warn("OutputStreamWriter close stream fail ",e);
			}
		}
	}

	public static void main(String[] args){
		Engine engine = new Engine("f:/");
//		engine.init("f:/");
		Template template = engine.getTemplate("tmp.ftl");
		Map<String,String> map = new HashMap<String,String>();
		map.put("name","key中文的dsfd顶替要");
		map.put("var","3");
		engine.writeFromTemplate(template, map, "F:/中文的2.txt");
	}
}

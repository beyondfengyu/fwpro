package com.cus.cms.common.frame.ioc;


import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


public class SpringUtils {

	public static Object getBean(ServletContext servletContext, String beanName){
        Object bean = null;
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        if(wac != null)
            bean = wac.getBean(beanName);
        return bean;
    }
	
	public static Object getBean(HttpSession session, String beanName){
        Object bean = null;
        ServletContext servletContext = session.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        if(wac != null)
            bean = wac.getBean(beanName);
        return bean;
    }

}


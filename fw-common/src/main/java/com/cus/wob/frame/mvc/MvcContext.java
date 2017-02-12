package com.cus.wob.frame.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Andy 2015-11-11
 * @description 
 *
 */
public class MvcContext {
	
	public static ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();
	
	/**
	 * 初始化请求、响应的参数，存储在本地线程变量
	 * 在mvc的分发器中调用
	 * @param request
	 * @param response
	 */
	public static void initContext(HttpServletRequest request,HttpServletResponse response){
		threadLocal.set(new Context(request, response));
	}

	public static HttpServletRequest getRequest() {
		return getContext().getRequest();
	}

	public static HttpServletResponse getResponse() {
		return getContext().getResponse();
	}
	
	/**
	 * 从Request或Session或ApplicationContext中获得一个Attribute
	 * 
	 * @param key
	 * @return Attribut的值
	 */
	public static Object getAttribute(String key, Scope scope) {
		if (scope == null)
			return getAttribute(key);
		switch (scope) {
		case SESSION:
			return getRequest().getSession().getAttribute(key);
		case APPLICATION:
			return getRequest().getSession().getServletContext().getAttribute(
					key);
		default:
			return getRequest().getAttribute(key);
		}
	}
	
	/**
	 * 从Request,Session,ApplicationContext中获得一个Attribute 
	 * 该方法将按照Request, Session, ApplicationContext的顺序搜索指定的Attribute
	 * 
	 * @param key
	 * @return Attribut的值
	 */
	protected static Object getAttribute(String key) {
		Object value = getAttribute(key, Scope.REQUEST);
		if (value == null) {
			value = getAttribute(key, Scope.SESSION);
			if (value == null) {
				value = getAttribute(key, Scope.APPLICATION);
			}
		}
		return value;
	}
	
	public static Context getContext(){
		Context context = threadLocal.get();
		if(context==null){
			throw new RuntimeException("Cann't get the context from threadLocal!");
		}
		return context;
	}
	
	static class Context {
		private HttpServletRequest request;
		private HttpServletResponse response;
		
		public Context(HttpServletRequest request,HttpServletResponse response){
			this.request = request;
			this.response = response;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public HttpServletResponse getResponse() {
			return response;
		}
		
		
	}
	
}

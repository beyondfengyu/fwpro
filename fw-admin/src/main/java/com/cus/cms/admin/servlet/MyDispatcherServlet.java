package com.cus.cms.admin.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cus.cms.common.frame.mvc.MvcContext;
import org.springframework.web.servlet.DispatcherServlet;


/**
 *
 * @author Andy
 * @description 自定义的mvc分发器
 *
 */
public class MyDispatcherServlet extends DispatcherServlet{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doService(HttpServletRequest request,
							 HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		MvcContext.initContext(request, response);
		super.doService(request, response);
	}

	@Override
	protected void doDispatch(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		super.doDispatch(request, response);
	}

}

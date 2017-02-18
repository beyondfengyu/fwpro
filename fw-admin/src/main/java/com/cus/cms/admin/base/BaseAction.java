package com.cus.cms.admin.base;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cus.cms.common.frame.mvc.MvcContext;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.constants.AdminConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cus.cms.common.frame.mvc.Scope;



/**
 *
 * @author Andy   2015-11-10
 * @description 控制器的基类
 *
 */
public class BaseAction {
	protected final Logger m_logger = LoggerFactory.getLogger(getClass());
	
    protected int pageNumber;
    protected int pageSize;
    protected int recordCount;
	
    protected HttpServletRequest getRequest() {
		return MvcContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return MvcContext.getResponse();
	}

	protected long getAdminId(){
		try {
			Object tmp = getAttribute(AdminConstants.HAS_LOGIN, Scope.SESSION);
			if(tmp!=null) {
				String isLogin = tmp.toString();
				if (!BlankUtil.isBlank(isLogin) && "true".equalsIgnoreCase(isLogin)) {
					String adminId = getAttribute(AdminConstants.ADMIN_ID, Scope.SESSION).toString();
					if (!BlankUtil.isBlank(adminId)) {
						return Long.valueOf(adminId);
					}
				}
			}
		}catch (Exception e){
			m_logger.warn("getAdminId fail,admin has not login",e);
		}
		return -1;
	}


	/**
	 * 设置请求的属性
	 * @param key
	 * @param obj
	 */
	public void setAttribute(String key, Object obj){
		setAttribute(key, obj, Scope.REQUEST);
	}
	
	public void setAttribute(String key, Object obj, Scope scope) {
		switch (scope) {
			case REQUEST:
				getRequest().setAttribute(key, obj);
				break;
			case SESSION:
				 getRequest().getSession().setAttribute(key, obj);
				 break;
			case APPLICATION:
				getRequest().getSession().getServletContext().setAttribute(key, obj);
				break;
			default:
				getRequest().setAttribute(key, obj);
				break;
		}
	}
	
	public Object getAttribute(String key, Scope scope){
		Object attr = null;
		switch (scope) {
			case REQUEST:
				attr = getRequest().getAttribute(key);
				break;
			case SESSION:
				attr = getRequest().getSession().getAttribute(key);
				break;
			case APPLICATION:
				attr = getRequest().getSession().getServletContext().getAttribute(key);
				break;
			default:
				break;
		}
		return attr;
	}
	
	public void writeJson(String json){
		try{
            getResponse().setContentType("text/json;charset=utf-8");
            Writer writer = getResponse().getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
	}

	public void writeJsonResult(int result){
		StringBuilder builder = new StringBuilder();
		builder.append("{\"result\":").append(result).append("}");
		writeJson(builder.toString());
	}

	public void writeJson(boolean success,String msg){
		StringBuilder builder = new StringBuilder();
		builder.append("{\"success\":\"").append(success).append("\",");
		builder.append("\"msg\":\"").append(msg).append("\"}");
        writeJson(builder.toString());
	}


	public int getPageNumber(){
		String tmp = getRequest().getParameter("pageNumber");
		if(BlankUtil.isBlank(tmp)){
			return 1;
		}
		return Integer.valueOf(tmp);

	}

	public int getPageSize(){
		String tmp = getRequest().getParameter("pageSize");
		if(BlankUtil.isBlank(tmp)){
			return 10;
		}
		return Integer.valueOf(tmp);
	}
}


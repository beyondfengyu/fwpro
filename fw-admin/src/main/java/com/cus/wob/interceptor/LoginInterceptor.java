package com.cus.wob.interceptor;

import com.cus.wob.constants.AdminConstants;
import com.cus.wob.frame.mvc.Scope;
import com.cus.wob.util.BlankUtil;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * @author Andy  2016/3/10.
 * @description
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger m_logger = LoggerFactory.getLogger(LoginInterceptor.class);
    /**
     * 判断是否已经登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Object tmp = request.getSession().getAttribute(AdminConstants.HAS_LOGIN);
            if(tmp!=null) {
                String isLogin = tmp.toString();
                if (!BlankUtil.isBlank(isLogin) && "true".equalsIgnoreCase(isLogin)) {
                    String adminId = request.getSession().getAttribute(AdminConstants.ADMIN_ID).toString();
                    if (!BlankUtil.isBlank(adminId)) {
                        return true;
                    }
                }
            }
        }catch (Exception e){
            m_logger.warn("getAdminId fail,admin has not login",e);
        }
        response.setContentType("text/html;charset=utf-8");
        Writer writer = response.getWriter();
        writer.write("<script>window.location='/login/door.action';</script>");
        writer.flush();
        writer.close();
        return false;
//        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

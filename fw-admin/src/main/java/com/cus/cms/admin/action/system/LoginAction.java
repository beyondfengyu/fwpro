package com.cus.cms.admin.action.system;

import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.AdminConstants;
import com.cus.cms.common.model.AdminUser;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.util.StringUtil;
import com.cus.cms.service.system.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * @author Andy  2015/12/29
 * @description
 */
@Controller
public class LoginAction extends BaseAction {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("/login/door")
    public String toLogin() {
        return "/login";
    }

    /**
     * 后台登录验证
     *
     * @param account  账号，可以是用户名、邮箱
     * @param password MD5加密后的密码
     * @param authCode MD5加密后的验证码
     */
    @RequestMapping("/login/login")
    @ResponseBody
    public void login(String account, String password, String authCode) throws UnsupportedEncodingException {
        int result = validateData(account, password, authCode);
        if (result == 0) { //参数验证成功
            AdminUser adminUser = adminUserService.getAdminUser(account, password);
            if (adminUser != null) {
                setAttribute(AdminConstants.HAS_LOGIN, "true", com.cus.cms.common.frame.mvc.Scope.SESSION);
                setAttribute(AdminConstants.ADMIN_ID, adminUser.getId(), com.cus.cms.common.frame.mvc.Scope.SESSION);
                setAttribute(AdminConstants.ADMIN_NAME, adminUser.getUsername(), com.cus.cms.common.frame.mvc.Scope.SESSION);
                Cookie cookie1 = new Cookie(AdminConstants.HAS_LOGIN, "true");
                Cookie cookie2 = new Cookie(AdminConstants.ADMIN_ID, adminUser.getId() + "");
                Cookie cookie3 = new Cookie(AdminConstants.ADMIN_NAME, URLEncoder.encode(adminUser.getUsername(), "utf-8"));
                getResponse().addCookie(cookie1);
                getResponse().addCookie(cookie2);
                getResponse().addCookie(cookie3);
                adminUserService.updateLastLogin(adminUser.getId());
                m_logger.info("amdin [" + adminUser.getUsername() + "] login system.");
                writeJson(true, adminUser.getId() + "@" + adminUser.getUsername());
                return;
            } else {
                result = 404;  //不存在
            }
        }
        writeJson(false, result + "");
    }

    /**
     * 退出登录
     *
     * @param model
     * @return
     */
    @RequestMapping("/login/logout")
    public String logout(Model model) {
//        Subject currentUser = SecurityUtils.getSubject();  //获取当前用户认证信息
//        currentUser.logout();
        if (getAdminId() > 0) {
            m_logger.info("admin logout, name is:" + getAttribute(AdminConstants.ADMIN_NAME, com.cus.cms.common.frame.mvc.Scope.SESSION));
            getRequest().getSession().removeAttribute(AdminConstants.HAS_LOGIN);
            getRequest().getSession().removeAttribute(AdminConstants.ADMIN_ID);
            getRequest().getSession().removeAttribute(AdminConstants.ADMIN_NAME);
            Cookie[] cookies = getRequest().getCookies();
            for (Cookie cookie : cookies) {
                if (AdminConstants.HAS_LOGIN.equalsIgnoreCase(cookie.getName())
                        || AdminConstants.ADMIN_ID.equalsIgnoreCase(cookie.getName())
                        || AdminConstants.ADMIN_NAME.equalsIgnoreCase(cookie.getName())) {
                    Cookie cookieDel = new Cookie(cookie.getName(), null);
                    cookieDel.setPath("/");
                    cookieDel.setMaxAge(0);
                    getResponse().addCookie(cookieDel);
                }
            }
            return "redirect:/login/door.action";
        }
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //////// 自定义函数
    //////////////////////////////////////////////////////////////////////////////////

    /**
     * 检验表单数据是否正确
     *
     * @param account
     * @param password
     * @param authCode
     */
    private int validateData(String account, String password, String authCode) {
        account = StringUtil.filterSubstring(StringUtil.filterSpecial(account), 50);
        password = StringUtil.filterSpecial(password);
        authCode = StringUtil.filterSpecial(authCode);
        if (!BlankUtil.isBlank(account)) {
            if (!BlankUtil.isBlank(password)) {
                if (!StringUtil.checkCode(authCode, getRequest().getSession())) {
                    return 3;  //验证码不正确
                }
            } else {
                return 2;  //密码不能为空
            }
        } else {
            return 1;  //邮箱不能为空
        }
        return 0;
    }
}

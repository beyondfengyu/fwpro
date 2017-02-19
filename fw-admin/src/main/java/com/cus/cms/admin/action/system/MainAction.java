package com.cus.cms.admin.action.system;


import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.model.system.AdminRole;
import com.cus.cms.common.model.system.AdminUser;
import com.cus.cms.service.system.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainAction extends BaseAction {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 跳转到管理首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/admin/main")
    public String toMain(Model model) {
        try {
            long adminId = getAdminId();
            AdminUser adminUser = adminUserService.getAdminUserById(adminId);
            if (adminUser != null) {
                adminUser.setPassword("");
                AdminRole adminRole = adminUserService.getRoleByAdminId(adminId);
                if(adminRole == null){
                    throw new Exception("AdminRole is null, the adminId is "+adminId);
                }
                adminUser.setRoleStr(adminRole.getName());
                getRequest().setAttribute("adminUser", adminUser);
            }
        } catch (Exception e) {
            m_logger.error("toMain error, cause by " + e.getMessage(), e);
        }
        m_logger.info("main-----------------------");
        return "/main";
    }

}

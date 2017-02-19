package com.cus.cms.admin.action.system;


import com.alibaba.fastjson.JSONObject;
import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.AdminMenu;
import com.cus.cms.common.model.AdminRefRoleMenu;
import com.cus.cms.common.model.AdminRole;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.service.system.AdminMenuService;
import com.cus.cms.service.system.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Andy
 * @version 1.0
 * @date 2016/10/17
 */
@Controller
public class RoleAction extends BaseAction {

    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminMenuService adminMenuService;

    /**
     * 查询用户列表
     *
     * @param searchText 用户名称
     */
    @RequestMapping("/admin/getRoles")
    @ResponseBody
    public void getRoles(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<AdminRole> list = adminRoleService.getRoleWithPage(searchText, getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            jsonObject.put("total", adminRoleService.getCountWithPage(searchText));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/getRole")
    @ResponseBody
    public void getAdminRole(Long id) {
        JSONObject jsonObject = new JSONObject();
        if (!BlankUtil.isBlank(id)) {
            try {
                AdminRole adminRole = adminRoleService.getRoleById(id);
                if (adminRole != null) {
                    jsonObject.put("entity", adminRole);
                }
            } catch (Exception e) {
                m_logger.warn("getAdminRole fail,cause by " + e.getMessage(), e);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveRole")
    @ResponseBody
    public void saveRole(AdminRole adminRole, boolean isEdit) {
        int result = -1;
        if (adminRole != null) {
            try {
                result = adminRoleService.saveRole(adminRole, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/admin/delRoles")
    @ResponseBody
    public void delRoles(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminRoleService.delRoleById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delRole fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/admin/getMenuByRole")
    @ResponseBody
    public void getMenuByRole(Long roleId) {
        JSONObject jsonObject = new JSONObject();
        if (roleId != null && roleId > 0) {
            List<AdminMenu> list = adminMenuService.getAllMenu();
            List<AdminRefRoleMenu> refRoleMenus = adminRoleService.getMenuByRole(roleId);
            for (AdminMenu adminMenu : list) {
                adminMenu.setCheck(false);
                for (AdminRefRoleMenu adminRefRoleMenu : refRoleMenus) {
                    if (adminRefRoleMenu.getMenuId() == adminMenu.getId()) {
                        adminMenu.setCheck(true);
                        break;
                    }
                }
            }
            jsonObject.put("result", 0);
            jsonObject.put("list", list);
        } else {
            jsonObject.put("result", 1);
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveRoleMenu")
    @ResponseBody
    public void saveRoleMenu(Long roleId, Long[] menus) {
        int result = -1;
        if (roleId != null && menus != null) {
            try {
                result = adminRoleService.saveRoleMenu(roleId, menus);
            } catch (Exception e) {
                m_logger.warn("saveRoleMenu fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }
}

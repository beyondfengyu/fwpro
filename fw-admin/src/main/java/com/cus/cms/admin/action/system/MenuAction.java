package com.cus.cms.admin.action.system;

import com.alibaba.fastjson.JSONObject;

import com.cus.cms.admin.base.BaseAction;
import com.cus.cms.common.constants.ErrorCode;
import com.cus.cms.common.model.AdminMenu;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.wrapper.Combobox;
import com.cus.cms.service.system.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andy  2016/1/7.
 * @description
 */
@Controller
public class MenuAction extends BaseAction {
    @Autowired
    private AdminMenuService adminMenuService;

    /**
     *
     */
    @RequestMapping("/admin/getAllMenu")
    @ResponseBody
    public void getAllMenuByAdminId() {
        JSONObject jsonObject = new JSONObject();
        long adminId = getAdminId();
        if (adminId > -1) {
            List<AdminMenu> list = adminMenuService.getMenuByAdminId(adminId);
            if (BlankUtil.isBlank(list)) {
                jsonObject.put("result", 1);
            } else {
                jsonObject.put("result", 0);
                List<AdminMenu> parents = new ArrayList<AdminMenu>();
                List<AdminMenu> childs = new ArrayList<AdminMenu>();
                for (AdminMenu adminMenu : list) {
                    if (adminMenu.getParentId() == 0) {
                        parents.add(adminMenu);
                    } else {
                        childs.add(adminMenu);
                    }
                }
                jsonObject.put("parents", parents);
                jsonObject.put("childs", childs);
            }
        } else {
            jsonObject.put("result", ErrorCode.NOT_LOGIN);
        }
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 查询菜单列表
     *
     * @param searchText 菜单名称
     */
    @RequestMapping("/admin/queryMenus")
    @ResponseBody
    public void queryMenus(String searchText) {
        JSONObject jsonObject = new JSONObject();
        List<AdminMenu> list = adminMenuService.getMenuWithPage(searchText, getPageNumber(), getPageSize());
        if (!BlankUtil.isBlank(list)) {
            Map<Long,String> parentMap = getParentNames();
            for(AdminMenu adminMenu: list){
                if(adminMenu.getParentId()==0){
                    adminMenu.setParentName("#");
                }else{
                    adminMenu.setParentName(parentMap.get(adminMenu.getParentId()));
                }
            }
            jsonObject.put("total", adminMenuService.getCountWithPage(searchText));
        }
        jsonObject.put("rows", list);
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/getMenuById")
    @ResponseBody
    public void getMenuById(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {
            AdminMenu adminMenu = adminMenuService.getAdminMenuById(id);
            if (adminMenu != null) {
                jsonObject.put("entity", adminMenu);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/admin/saveMenu")
    @ResponseBody
    public void saveMenu(AdminMenu adminMenu, boolean isEdit) {
        int result = -1;
        if (adminMenu != null) {
            try {
                result = adminMenuService.saveMenu(adminMenu, isEdit);
            } catch (Exception e) {
                m_logger.warn("saveMenu fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/admin/delMenus")
    @ResponseBody
    public void delMenus(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                adminMenuService.delMenuById(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                m_logger.warn("delMenus fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    /**
     * 获取所有父级菜单，转换成组合框结构
     */
    @RequestMapping("/admin/getParentMenus")
    @ResponseBody
    public void getParentMenus() {
        String result = "[{txt:'--  无  --',val:'0'}]";
        try {
            List<AdminMenu> list = adminMenuService.getParentMenus();
            result = JSONObject.toJSONString(tranToCombobox(list));
        } catch (Exception e) {
            m_logger.warn("getParentMenus fail,cause by " + e.getMessage(), e);
        }
        writeJson(result);
    }

    private List<Combobox> tranToCombobox(List<AdminMenu> list) {
        List<Combobox> boxs = new ArrayList<Combobox>();
        Combobox box = null;
        box = new Combobox();
        box.setOtxt("--  无  --");
        box.setOval("0");
        boxs.add(box);
        if (!BlankUtil.isBlank(list))
            for (AdminMenu menu : list) {
                box = new Combobox();
                box.setOtxt(menu.getName());
                box.setOval(menu.getId() + "");
                boxs.add(box);
            }
        return boxs;
    }

    private Map<Long,String> getParentNames(){
        Map<Long,String> map = new HashMap<Long,String>();
        List<AdminMenu> list = adminMenuService.getParentMenus();
        for(AdminMenu adminMenu: list){
            if(adminMenu.getParentId()==0){
                map.put(adminMenu.getId(),adminMenu.getName());
            }
        }
        return map;
    }
}

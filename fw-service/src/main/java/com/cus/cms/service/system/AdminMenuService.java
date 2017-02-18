package com.cus.cms.service.system;



import com.cus.cms.common.model.AdminMenu;
import com.cus.cms.common.model.AdminRefRoleMenu;
import com.cus.cms.common.model.AdminRefUserRole;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.dao.system.AdminMenuDao;
import com.cus.cms.dao.system.AdminRefRoleMenuDao;
import com.cus.cms.dao.system.AdminRefUserRoleDao;
import com.cus.cms.dao.system.AdminRoleDao;
import com.cus.cms.service.BaseService;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andy  2015/12/26.
 * @description
 */
@Service("adminMenuService")
public class AdminMenuService extends BaseService<AdminMenu> {

    @Autowired
    private AdminMenuDao adminMenuDao;
    @Autowired
    private AdminRefUserRoleDao adminRefUserRoleDao;
    @Autowired
    private AdminRefRoleMenuDao adminRefRoleMenuDao;

    /**
     * 分页查询菜单
     * @param name 菜单名称
     * @param page
     * @param size
     * @return
     */
    public List<AdminMenu> getMenuWithPage(String name, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return adminMenuDao.queryMenuWithPage(name, offset, size);
    }

    public long getCountWithPage(String name) {
        if (!BlankUtil.isBlank(name)) {
            return adminMenuDao.count("name", name);
        }
        return adminMenuDao.count();
    }

    public List<AdminMenu> getParentMenus(){
        return adminMenuDao.queryParentMenus();
    }

    public int saveMenu(AdminMenu adminMenu,boolean isEdit){
        if(isEdit) {
            adminMenuDao.updateByKey(adminMenu);
        }else{
            adminMenu.setCreateTime(new Date());
            adminMenu.setId(snowFlake.nextId());
            adminMenu.setStatus(true);
            adminMenuDao.save(adminMenu, WriteConcern.ACKNOWLEDGED);
        }
        return 1;
    }

    public AdminMenu getAdminMenuById(long id){
        return adminMenuDao.get(id);
    }

    public WriteResult delMenuById(long id){
        return adminMenuDao.deleteById(id);
    }

    public List<AdminMenu> getMenuByAdminId(long adminId){
        List<AdminRefUserRole> refRoles = adminRefUserRoleDao.queryRefRoleByAdminId(adminId);
        if (refRoles == null || refRoles.size() < 1) {
            return null;
        }

        List<Long> roleIds = new ArrayList<>();
        for (AdminRefUserRole refUserRole : refRoles) {
            roleIds.add(refUserRole.getRoleId());
        }
        List<AdminRefRoleMenu> refMenus = adminRefRoleMenuDao.queryRefMenuByRoles(roleIds);
        if (refMenus == null || refMenus.size() < 1) {
            return null;
        }

        List<Long> menuIds = new ArrayList<>();
        for (AdminRefRoleMenu refRoleMenu : refMenus) {
            roleIds.add(refRoleMenu.getMenuId());
        }
        return adminMenuDao.queryMenuByRoles(menuIds);
    }

    public List<AdminMenu> getAllMenu() {
        return adminMenuDao.find().asList();
    }
}

package com.cus.cms.service.system;


import com.cus.cms.common.model.AdminRefRoleMenu;
import com.cus.cms.common.model.AdminRefUserRole;
import com.cus.cms.common.model.AdminRole;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.dao.system.AdminRefRoleMenuDao;
import com.cus.cms.dao.system.AdminRefUserRoleDao;
import com.cus.cms.dao.system.AdminRoleDao;
import com.cus.cms.service.BaseService;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy  2015/12/26.
 * @description
 */
@Service("adminRoleService")
public class AdminRoleService extends BaseService<AdminRole> {
    @Autowired
    private AdminRoleDao adminRoleDao;
    @Autowired
    private AdminRefUserRoleDao adminRefUserRoleDao;
    @Autowired
    private AdminRefRoleMenuDao adminRefRoleMenuDao;

    /**
     * 插入管理员信息
     * @param adminRole
     * @return
     */
    public int saveRole(AdminRole adminRole, boolean isEdit){
        if(isEdit){
            adminRoleDao.updateByKey(adminRole);
        }else{
            adminRole.setId(snowFlake.nextId());
            adminRoleDao.save(adminRole);
        }
        return 1;
    }


    public AdminRole getRoleById(Long roleId){
        return adminRoleDao.get(roleId);
    }


    public List<AdminRole> getRoleWithPage(String name, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return adminRoleDao.queryRoleWithPage(name, offset, size);
    }

    public long getCountWithPage(String name) {
        if (!BlankUtil.isBlank(name)) {
            return adminRoleDao.count("name", name);
        }
        return adminRoleDao.count();
    }

    public WriteResult delRoleById(long id){
        return adminRoleDao.deleteById(id);
    }

    public List<AdminRole> getAllRole(){
        return adminRoleDao.find().asList();
    }

    public List<AdminRefUserRole> getRoleByAdminId(Long adminId){
        return adminRefUserRoleDao.queryRefRoleByAdminId(adminId);
    }

    public int saveUserRole(Long adminId , Long[] roles){
        WriteResult result = adminRefUserRoleDao.delRefByUid(adminId);
        for(Long roleId: roles){
            AdminRefUserRole adminRefUserRole = new AdminRefUserRole();
            adminRefUserRole.setAdminId(adminId);
            adminRefUserRole.setRoleId(roleId);
            adminRefUserRoleDao.save(adminRefUserRole);
        }
        return 1;
    }


    public List<AdminRefRoleMenu> getMenuByRole(Long roleId){
        return adminRefRoleMenuDao.queryMenuByRole(roleId);
    }

    public int saveRoleMenu(Long roleId , Long[] menus){
        WriteResult result = adminRefRoleMenuDao.delRefByRoleId(roleId);
        for(Long menuId: menus){
            AdminRefRoleMenu adminRefRoleMenu = new AdminRefRoleMenu();
            adminRefRoleMenu.setMenuId(menuId);
            adminRefRoleMenu.setRoleId(roleId);
            adminRefRoleMenuDao.save(adminRefRoleMenu);
        }
        return 1;
    }
}

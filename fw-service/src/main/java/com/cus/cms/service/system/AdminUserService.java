package com.cus.cms.service.system;


import com.cus.cms.common.model.system.AdminRefUserRole;
import com.cus.cms.common.model.system.AdminRole;
import com.cus.cms.common.model.system.AdminUser;
import com.cus.cms.common.util.BlankUtil;
import com.cus.cms.common.util.DateTimeUtil;
import com.cus.cms.common.util.EncrytcUtil;
import com.cus.cms.dao.system.AdminRefUserRoleDao;
import com.cus.cms.dao.system.AdminRoleDao;
import com.cus.cms.dao.system.AdminUserDao;
import com.cus.cms.service.BaseService;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy  2015/12/26.
 * @description
 */
@Service("adminUserService")
public class AdminUserService extends BaseService {
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private AdminRoleDao adminRoleDao;
    @Autowired
    private AdminRefUserRoleDao adminRefUserRoleDao;

    /**
     * 插入管理员信息
     * @param adminUser
     * @return
     */
    public int saveUser(AdminUser adminUser, boolean isEdit){
        if(isEdit){
            adminUserDao.updateByKey(adminUser);
        }else{
            adminUser.setPassword(EncrytcUtil.encodeMD5String("123"));
            adminUser.setStatus(1);
            adminUser.setCreateTime(DateTimeUtil.getCurrentTime());
            if(BlankUtil.isBlank(adminUser.getHeadImg())) {
                adminUser.setHeadImg("/static/AdminLTE/img/user2-160x160.jpg");
            }
            adminUser.setId(snowFlake.nextId());
            adminUserDao.save(adminUser);
        }
        return 1;
    }

    /**
     * 判断管理员是否存在
     * @param account
     * @param password
     * @return
     */
    public AdminUser getAdminUser(String account,String password){
        return adminUserDao.queryByAccount(account, password);
    }

    public AdminUser getAdminUserById(long adminId){
        return adminUserDao.get(adminId);
    }

    public AdminRole getRoleByAdminId(long adminId){
        List<AdminRefUserRole> refRoles = adminRefUserRoleDao.queryRefRoleByAdminId(adminId);
        if (refRoles == null || refRoles.size() < 1) {
            return null;
        }

        List<Long> roleIds = new ArrayList<>();
        for (AdminRefUserRole refUserRole : refRoles) {
            roleIds.add(refUserRole.getRoleId());
        }
        return adminRoleDao.queryRoleByRoleIds(roleIds);
    }

    public List<AdminUser> getUserWithPage(String text, int page, int size){
        if (page < 1 || size < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        int offset = (page - 1) * size;
        return adminUserDao.queryUserWithPage(text, offset, size);
    }

    public long getCountWithPage(String text) {
        if (!BlankUtil.isBlank(text)) {
            return adminUserDao.queryCountWithPage(text);
        }
        return adminUserDao.count();
    }
    public AdminUser getUserByName(String name){
        if (BlankUtil.isBlank(name)) {
            return null;
        }
        return adminUserDao.queryUserByName(name);
    }

    public int updateLastLogin(long adminId){
        AdminUser param = new AdminUser();
        param.setId(adminId);
        param.setLastLogin(DateTimeUtil.getCurrentTime());
        UpdateResults updateResults= adminUserDao.updateLastLogin(param);
        return 1;
    }

    public void delUserById(long id){
        adminUserDao.deleteById(id);
    }
}

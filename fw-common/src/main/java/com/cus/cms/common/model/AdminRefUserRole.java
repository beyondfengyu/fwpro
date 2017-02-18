package com.cus.cms.common.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;


@Entity(value="admin_ref_user_role",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("adminId"), @Field("roleId")})
)
public class AdminRefUserRole {

    private long adminId;

    private long roleId;

    /**
     * @return admin_id
     */
    public long getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     */
    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    /**
     * @return role_id
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
package com.cus.cms.common.model.system;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;


@Entity(value="admin_ref_user_role",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("adminId"), @Field("roleId")})
)
public class AdminRefUserRole {

    @Id
    private ObjectId id;

    private long adminId;

    private long roleId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

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
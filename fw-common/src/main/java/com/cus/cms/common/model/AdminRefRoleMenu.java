package com.cus.cms.common.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;


@Entity(value="admin_ref_role_menu",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("roleId"), @Field("menuId")})
)
public class AdminRefRoleMenu {

    private long roleId;

    private long menuId;

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

    /**
     * @return menu_id
     */
    public long getMenuId() {
        return menuId;
    }

    /**
     * @param menuId
     */
    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }
}
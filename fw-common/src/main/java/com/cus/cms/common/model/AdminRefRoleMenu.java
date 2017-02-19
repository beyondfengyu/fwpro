package com.cus.cms.common.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;


@Entity(value="admin_ref_role_menu",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("roleId"), @Field("menuId")})
)
public class AdminRefRoleMenu {

    @Id
    private ObjectId id;

    private long roleId;

    private long menuId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
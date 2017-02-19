package com.cus.cms.common.model.system;

import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

@Entity(value = "admin_menu", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("name"), @Field("parentId")})
)
public class AdminMenu implements Serializable {
    /**
     * 主键，自增长
     */
    @Id
    private long id;

    /**
     * 上级菜单的ID,如果没有上级菜单，填充为0
     */
    private long parentId;
    private String name;
    private String path;
    private String icon;
    private Integer status;
    private Integer showorder;
    private String createTime;
    private String description;

    @Transient
    private String parentName;
    @Transient
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * 获取主键，自增长
     *
     * @return ID - 主键，自增长
     */
    public long getId() {
        return id;
    }

    /**
     * 设置主键，自增长
     *
     * @param id 主键，自增长
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取上级菜单的ID,如果没有上级菜单，填充为0
     *
     * @return PARENT_ID - 上级菜单的ID,如果没有上级菜单，填充为0
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * 设置上级菜单的ID,如果没有上级菜单，填充为0
     *
     * @param parentId 上级菜单的ID,如果没有上级菜单，填充为0
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return PATH
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return ICON
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return STATUS
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return showorder
     */
    public Integer getShoworder() {
        return showorder;
    }

    /**
     * @param showorder
     */
    public void setShoworder(Integer showorder) {
        this.showorder = showorder;
    }

    /**
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
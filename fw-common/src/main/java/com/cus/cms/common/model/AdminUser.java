package com.cus.cms.common.model;


import org.mongodb.morphia.annotations.*;

import java.util.Date;

@Entity(value="admin_user",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("username")})
)
public class AdminUser {
    @Id
    private long id;

    private long optUid;

    private String username;

    private String password;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 状态，0有效,1无效
     */
    private Boolean status;

    /**
     * 上次登录时间
     */
    private Date lastLogin;

    private String email;

    private String phone;

    private String qq;

    private Date createTime;

    private Date updateTime;

    @Transient
    private String roleStr;

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return opt_uid
     */
    public long getOptUid() {
        return optUid;
    }

    /**
     * @param optUid
     */
    public void setOptUid(long optUid) {
        this.optUid = optUid;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取头像
     *
     * @return head_img - 头像
     */
    public String getHeadImg() {
        return headImg;
    }

    /**
     * 设置头像
     *
     * @param headImg 头像
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * 获取状态，0有效,1无效
     *
     * @return status - 状态，0有效,1无效
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态，0有效,1无效
     *
     * @param status 状态，0有效,1无效
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取上次登录时间
     *
     * @return last_login - 上次登录时间
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLogin 上次登录时间
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * @param qq
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
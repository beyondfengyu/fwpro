package com.cus.cms.common.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;

/**
 * @author Andy
 */
@Embedded
public class Address implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private String address;

    private String iphone;

    private Integer defAdd;//是否是默认地址 0 表示不是，1 表示是

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the iphone
     */
    public String getIphone() {
        return iphone;
    }

    /**
     * @param iphone the iphone to set
     */
    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    /**
     * @return the defAdd
     */
    public Integer getDefAdd() {
        return defAdd;
    }

    /**
     * @param defAdd the defAdd to set
     */
    public void setDefAdd(Integer defAdd) {
        this.defAdd = defAdd;
    }
}

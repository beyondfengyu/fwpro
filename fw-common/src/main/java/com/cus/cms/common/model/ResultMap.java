package com.cus.cms.common.model;

/**
 * @author Andy
 */
public enum  ResultMap {

    SUCCESS("成功"),
    FAILURE("失败");

    private String desc;

    ResultMap(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}

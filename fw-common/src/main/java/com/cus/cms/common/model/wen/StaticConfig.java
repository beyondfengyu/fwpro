package com.cus.cms.common.model.wen;

import org.mongodb.morphia.annotations.*;

/**
 * @author Andy
 */
@Entity(value = "static_config", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("code"), @Field("ckey")})
)
public class StaticConfig {

    @Transient
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String CKEY = "ckey";
    public static final String CVAL = "cval";
    public static final String DESC = "desc";
    public static final String SHOWORDER = "showOrder";

    @Id
    private long id;
    private String code;
    private String ckey;
    private String cval;
    private String desc;
    private int showOrder;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showorder) {
        this.showOrder = showorder;
    }
}

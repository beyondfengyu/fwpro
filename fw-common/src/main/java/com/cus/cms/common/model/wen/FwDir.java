package com.cus.cms.common.model.wen;

import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

@Entity(value = "fw_dir", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("dirCode"),@Field("dirName")})
)
public class FwDir implements Serializable {
    @Transient
    public static final String ID = "_id";
    @Transient
    public static final String FW_CODE = "dirCode";
    @Transient
    public static final String FW_NAME = "dirName";
    @Transient
    public static final String FW_TYPE = "dirType";
    @Transient
    public static final String LAST_CODE = "lastCode";
    @Transient
    public static final String STATUS = "status";
    @Transient
    public static final String SHOW_ORDER = "showOrder";
    @Transient
    private String lastName;


    @Id
    private long id;

    @Property(value = FW_CODE)
    private String dirCode;

    @Property(value = FW_NAME)
    private String dirName;


    @Property(value = FW_TYPE)
    private Integer dirType;

    @Property(value = LAST_CODE)
    private String lastCode;

    private Integer status;

    @Property(value = SHOW_ORDER)
    private Integer showOrder;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDirCode() {
        return dirCode;
    }

    public void setDirCode(String dirCode) {
        this.dirCode = dirCode;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Integer getDirType() {
        return dirType;
    }

    public void setDirType(Integer dirType) {
        this.dirType = dirType;
    }

    public String getLastCode() {
        return lastCode;
    }

    public void setLastCode(String lastCode) {
        this.lastCode = lastCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }
}
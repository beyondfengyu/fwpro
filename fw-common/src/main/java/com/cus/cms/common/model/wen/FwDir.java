package com.cus.cms.common.model.wen;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

@Entity(value = "fw_dir", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("dir_name")})
)
public class FwDir implements Serializable {
    @Transient
    public static final String ID = "_id";
    @Transient
    public static final String FW_CODE = "fw_code";
    @Transient
    public static final String FW_NAME = "fw_name";
    @Transient
    public static final String FW_TYPE = "fw_type";
    @Transient
    public static final String LAST_CODE = "last_code";
    @Transient
    public static final String STATUS = "status";
    @Transient
    public static final String SHOW_ORDER = "show_order";

    @Id
    private ObjectId id;

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
    private Integer showorder;

    @Transient
    private String lastName;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getDirCode() {
        return dirCode;
    }

    public void setDirCode(String dirCode) {
        this.dirCode = dirCode;
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

    public Integer getShoworder() {
        return showorder;
    }

    public void setShoworder(Integer showorder) {
        this.showorder = showorder;
    }
}
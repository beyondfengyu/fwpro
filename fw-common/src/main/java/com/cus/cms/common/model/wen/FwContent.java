package com.cus.cms.common.model.wen;

import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * @author Andy
 */
@Entity(value = "fw_content", noClassnameStored = true)
public class FwContent implements Serializable {

    @Transient
    public static final String ID = "_id";
    @Transient
    public static final String CONTENT = "content";

    @Id
    private long id;  //与fw_page的id一致

    private String content;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "FwPage{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}


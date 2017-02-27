package com.cus.cms.common.model.wen;

import org.mongodb.morphia.annotations.*;

import java.io.Serializable;

/**
 * @author Andy
 */
@Entity(value = "fw_page", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("oneDir"),@Field("title")})
)
public class FwPage implements Serializable {

    @Transient
    public static final String ID = "_id";
    @Transient
    public static final String TITLE = "title";
    @Transient
    public static final String CONTENT = "content";
    @Transient
    public static final String ONE_DIR = "oneDir";
    @Transient
    public static final String TWO_DIR = "twoDir";
    @Transient
    public static final String STATUS = "status";
    @Transient
    public static final String SOURCE = "source";
    @Transient
    public static final String CREATE_TIME = "createTime";

    @Id
    private long id;

    private String title;

    @Transient
    private String content;

    private String oneDir;

    private String twoDir;

    private int status;

    private String source;

    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTwoDir() {
        return twoDir;
    }

    public void setTwoDir(String twoDir) {
        this.twoDir = twoDir;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getOneDir() {
        return oneDir;
    }

    public void setOneDir(String oneDir) {
        this.oneDir = oneDir;
    }

    @Override
    public String toString() {
        return "FwPage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", oneDir='" + oneDir + '\'' +
                ", twoDir='" + twoDir + '\'' +
                ", status=" + status +
                ", source='" + source + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}


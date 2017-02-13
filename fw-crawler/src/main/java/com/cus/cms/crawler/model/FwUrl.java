package com.cus.cms.crawler.model;

/**
 * @author Andy
 */
public class FwUrl {
    private int id;
    private int siteType;
    private String linkTxt;
    private String linkUrl;
    private int linkType;
    private String source;
    private int status;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }

    public String getLinkTxt() {
        return linkTxt;
    }

    public void setLinkTxt(String linkTxt) {
        this.linkTxt = linkTxt;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FwUrl{" +
                "id=" + id +
                ", linkTxt='" + linkTxt + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", linkType='" + linkType + '\'' +
                ", source='" + source + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

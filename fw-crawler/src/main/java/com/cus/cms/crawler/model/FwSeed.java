package com.cus.cms.crawler.model;

/**
 * @author Andy
 */
public class FwSeed {
    private int siteType;
    private String siteName;
    private String siteLink;
    private int startId;
    private int thread;

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return "FwSeed{" +
                "siteType=" + siteType +
                ", siteName='" + siteName + '\'' +
                ", siteLink='" + siteLink + '\'' +
                '}';
    }
}

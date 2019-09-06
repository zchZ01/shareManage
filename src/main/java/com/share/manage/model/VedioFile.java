package com.share.manage.model;

import java.io.File;
import java.util.Date;

public class VedioFile {
    /**上传文件路径*/
    private String url;

    /**上传者*/
    private String personName;

    /**上传时间*/
    private Date upTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }
}

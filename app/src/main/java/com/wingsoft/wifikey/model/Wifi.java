package com.wingsoft.wifikey.model;

/**
 * Created by wing on 15/7/6.
 */
public class Wifi {
    private String ssid="";
    private String key="";
    private String comment="";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}

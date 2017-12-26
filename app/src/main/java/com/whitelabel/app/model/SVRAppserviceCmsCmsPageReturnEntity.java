package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/7.
 */
public class SVRAppserviceCmsCmsPageReturnEntity extends SVRReturnEntity {

    private String data;
    private int status;

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}
    public String getContent() {
        return data;
    }

    public void setContent(String content) {
        this.data = content;
    }
}

package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/7.
 */
public class SVRAppserviceCmsStaticBlocksReturnEntity extends SVRReturnEntity {
    private int status;
    private String content;
    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

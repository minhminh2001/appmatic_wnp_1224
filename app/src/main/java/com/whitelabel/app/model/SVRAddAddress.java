package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAddAddress extends SVRReturnEntity{
    private int status;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}
}

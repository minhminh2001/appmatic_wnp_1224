package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/10/23.
 */
public class CustomAnimEntity extends SVRReturnEntity {
    private int status;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}
}

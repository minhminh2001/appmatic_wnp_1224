package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class SVRAppserviceRecoverOrderReturnEntity extends SVRReturnEntity {

    private int status;

    private String lastrealorderid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastrealorderid() {
        return lastrealorderid;
    }

    public void setLastrealorderid(String lastrealorderid) {
        this.lastrealorderid = lastrealorderid;
    }

}

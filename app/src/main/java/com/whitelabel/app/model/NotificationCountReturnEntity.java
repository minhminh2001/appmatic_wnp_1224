package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/14.
 */
public class NotificationCountReturnEntity extends SVRReturnEntity {

    private int status;
    private int count;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

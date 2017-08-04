package com.whitelabel.app.model;

/**
 * Created by Administrator on 2017/8/4.
 */

public class NotificationListEntity {
    private NotificationCell [] data;
    private int unreads;

    public NotificationCell[] getData() {
        return data;
    }

    public void setData(NotificationCell[] data) {
        this.data = data;
    }

    public int getUnreads() {
        return unreads;
    }

    public void setUnreads(int unreads) {
        this.unreads = unreads;
    }
}

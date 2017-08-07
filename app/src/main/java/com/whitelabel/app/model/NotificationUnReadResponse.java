package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class NotificationUnReadResponse implements Serializable {
    private int unreads;

    public int getUnreads() {
        return unreads;
    }

    public void setUnreads(int unreads) {
        this.unreads = unreads;
    }
}

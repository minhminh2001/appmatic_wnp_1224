package com.whitelabel.app.model;

/**
 * Created by img on 2017/11/16.
 */

public class SkipToAppStoreMarket {
    private long time;
    private boolean isAfterFirstOrder;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isAfterFirstOrder() {
        return isAfterFirstOrder;
    }

    public void setAfterFirstOrder(boolean afterFirstOrder) {
        isAfterFirstOrder = afterFirstOrder;
    }
}

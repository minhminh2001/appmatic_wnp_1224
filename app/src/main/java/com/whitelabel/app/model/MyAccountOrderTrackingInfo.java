package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by jay on 2016/8/17.
 */
public class MyAccountOrderTrackingInfo implements Serializable {
    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

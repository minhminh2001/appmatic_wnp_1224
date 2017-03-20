package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ShoppingCarStoreCreditBean implements Serializable {
    private String message;
    private String value;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

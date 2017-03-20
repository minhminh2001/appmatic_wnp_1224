package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/27.
 */
public class SVRAppserviceProductDetailResultProductDimensionReturnEntity implements Serializable {
    private String title;
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

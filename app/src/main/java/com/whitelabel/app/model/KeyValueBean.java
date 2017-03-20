package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2015/10/19.
 */
public class KeyValueBean implements Serializable {
    private  String key;
    private String value;
    private String label;
    public KeyValueBean(){

    }
    public KeyValueBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

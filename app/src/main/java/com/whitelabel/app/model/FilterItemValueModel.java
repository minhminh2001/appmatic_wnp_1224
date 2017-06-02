package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2.
 */

public class FilterItemValueModel implements Serializable {
    private String label;
    private String value;
    public FilterItemValueModel(String label, String value) {
        this.label = label;
        this.value = value;
    }
    public FilterItemValueModel() {

    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

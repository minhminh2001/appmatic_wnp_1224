package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductDetailResultDetailReturnEntity implements Serializable {
    private String label;
    private String value;
    private String code;
    private ArrayList<?> valueArray;
    //valueArray .get()拿到的是gson linkTreeMap

    public ArrayList<?> getValueArray() {
        return valueArray;
    }

    public void setValueArray(ArrayList<? > valueArray) {
        this.valueArray = valueArray;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}

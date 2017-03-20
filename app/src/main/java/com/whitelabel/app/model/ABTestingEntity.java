package com.whitelabel.app.model;

/**
 * Created by kevin on 2016/7/29.
 */
public class ABTestingEntity {
    private int itemId;
    private Object value;
    private Object defaultValue;

    public ABTestingEntity(int itemId, Object value, Object defaultValue) {
        this.itemId = itemId;
        this.value = value;
        this.defaultValue = defaultValue;
    }
    public ABTestingEntity() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductSearchFacetsCategoryItemReturnEntity implements Serializable {
    private String label;
    private String value;
    private boolean selected;
    private long count;
    private String url;
    private ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> items;

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> getItems() {
        return items;
    }

    public void setItems(ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> items) {
        this.items = items;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SVRAppserviceProductSearchFacetsFieldFilterItemReturnEntity implements Serializable {
    private String label;
    private String type;
    private String key;
    private ArrayList<FilterItem> value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<FilterItem> getValue() {
        return value;
    }

    public void setValue(ArrayList<FilterItem> value) {
        this.value = value;
    }

    public class FilterItem implements Serializable{
        private boolean selected;
        private String label;
        private String value;
        private int count;
        private String img;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
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

        public long getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}

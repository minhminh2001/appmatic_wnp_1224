package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/27.
 */
public class TMPLocalCartRepositoryProductOptionEntity implements Serializable {
    private String id;
    private String superAttribute;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperAttribute() {
        return superAttribute;
    }

    public void setSuperAttribute(String superAttribute) {
        this.superAttribute = superAttribute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/9.
 */
public class ProductDetailChild {
    private String id;
    private String label;
    private String price;
    private String superAttribute;
    private String superLabel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSuperAttribute() {
        return superAttribute;
    }

    public void setSuperAttribute(String superAttribute) {
        this.superAttribute = superAttribute;
    }

    public String getSuperLabel() {
        return superLabel;
    }

    public void setSuperLabel(String superLabel) {
        this.superLabel = superLabel;
    }
}

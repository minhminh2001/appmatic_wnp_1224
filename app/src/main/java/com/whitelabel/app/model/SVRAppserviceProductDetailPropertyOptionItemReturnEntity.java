package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/21.
 */
public class SVRAppserviceProductDetailPropertyOptionItemReturnEntity implements Serializable {
    private String id;
    private String label;
    private ArrayList<String> products;
    private String price;

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

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/5/3.
 */
public class LayoutStyleModel implements Serializable {
    private int home;
    private int ProductDetailType;
    public int getHome() {
        return 3;
    }
    public void setHome(int home) {
        this.home = home;
    }
    public int getProductDetailType() {
        return ProductDetailType;
    }
    public void setProductDetailType(int productDetailType) {
        ProductDetailType = productDetailType;
    }
}

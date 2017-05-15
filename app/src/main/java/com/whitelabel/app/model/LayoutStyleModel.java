package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/5/3.
 */

public class LayoutStyleModel implements Serializable {
    private int homeType;
    private int ProductDetailType;

    public int getHomeType() {
        return 3;
    }

    public void setHomeType(int homeType) {
        this.homeType = homeType;
    }

    public int getProductDetailType() {
        return ProductDetailType;
    }

    public void setProductDetailType(int productDetailType) {
        ProductDetailType = productDetailType;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BindProductResponseModel implements Serializable {
    private  double totalPrice;
    private List<ProductPropertyModel> relatedProducts;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductPropertyModel> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<ProductPropertyModel> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}

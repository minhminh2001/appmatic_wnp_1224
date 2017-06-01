package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BindProductResponseModel implements Serializable {
    private  int totalPrice;
    private List<SVRAppserviceProductDetailResultPropertyReturnEntity> relatedProducts;

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<SVRAppserviceProductDetailResultPropertyReturnEntity> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<SVRAppserviceProductDetailResultPropertyReturnEntity> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}

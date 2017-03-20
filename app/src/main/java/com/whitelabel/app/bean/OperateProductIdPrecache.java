package com.whitelabel.app.bean;

/**
 * Created by kevin on 2016/8/10.
 */
public class OperateProductIdPrecache {
    //将productId暂缓存，等成功执行某些操作后，再对productId进行处理
    //功能示例：  点击wishIcon进入登陆页面，登陆成功后将product加入wishlist
    private String productId;
    private boolean available=false;

    public OperateProductIdPrecache(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

package com.whitelabel.app.bean;

/**
 * Created by kevin on 2016/8/10.
 */
public class OperateProductIdPrecache {
    //将productId暂缓存，等成功执行某些操作后，再对productId进行处理
    //功能示例：  点击wishIcon进入登陆页面，登陆成功后将product加入wishlist
    private String productId;
    private boolean available=false;
    private int isLike;
    private boolean isUnLogin;
    public OperateProductIdPrecache(String productId) {
        this.productId = productId;
    }


    public OperateProductIdPrecache(String productId,int isLike,boolean isUnLogin) {
        this.productId = productId;
        this.isLike=isLike;
        this.isUnLogin=isUnLogin;
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

    public int getIsLike() {
        return isLike;
    }

    public void setLike(int like) {
        isLike = like;
    }

    public boolean isUnLogin() {
        return isUnLogin;
    }

    public void setUnLogin(boolean unLogin) {
        isUnLogin = unLogin;
    }
}

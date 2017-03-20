package com.whitelabel.app.callback;

/**
 * Created by Administrator on 2016/2/18.
 */
public interface ProductDetailCallback {
    boolean addProductToWishWhenLoginSuccess(String productId);
    void saveProductIdWhenJumpLoginPage(String productId);
    void changeOperateProductIdPrecacheStatus(boolean available);
}

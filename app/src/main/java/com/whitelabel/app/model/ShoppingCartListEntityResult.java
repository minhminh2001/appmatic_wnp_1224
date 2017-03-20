package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ShoppingCartListEntityResult extends SVRReturnEntity {

    private int success;

    private ShoppingCartListEntityCart cart;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ShoppingCartListEntityCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCartListEntityCart cart) {
        this.cart = cart;
    }
}

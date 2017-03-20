package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class ShoppingCartVoucherApplyEntity extends SVRReturnEntity {

    private int status;
    private String errorMessage;
    private ShoppingDiscountBean discount;
    private String grandTotal;
    private ShoppingDiscountBean shipping;

    public ShoppingDiscountBean getShipping() {
        return shipping;
    }

    public void setShipping(ShoppingDiscountBean shipping) {
        this.shipping = shipping;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ShoppingDiscountBean getDiscount() {
        return discount;
    }

    public void setDiscount(ShoppingDiscountBean discount) {
        this.discount = discount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

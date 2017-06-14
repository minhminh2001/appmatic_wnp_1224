package com.whitelabel.app.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/14.
 */
public class CheckoutPaymentSaveReturnEntity extends SVRReturnEntity {

    private int status;
    private CheckoutPaymentReturnShippingAddress shippingAddress;
    private CheckoutPaymentReturnShippingAddress billingAddress;
    private String paymentinfo;
    private ArrayList<ShoppingCartListEntityCell> reviewOrder;
    private String subtotal;
    private String grandtotal;
    private HashMap<String,String> discount;
    private HashMap<String,String> shipping;
    private ShoppingCarStoreCreditBean storeCredit;
    private String orders_notice;
    private String gst;


    public CheckoutPaymentReturnShippingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(CheckoutPaymentReturnShippingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getOrders_notice() {
        return orders_notice;
    }

    public void setOrders_notice(String orders_notice) {
        this.orders_notice = orders_notice;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }



    public ShoppingCarStoreCreditBean getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(ShoppingCarStoreCreditBean storeCredit) {
        this.storeCredit = storeCredit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CheckoutPaymentReturnShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(CheckoutPaymentReturnShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentinfo() {
        return paymentinfo;
    }

    public void setPaymentinfo(String paymentinfo) {
        this.paymentinfo = paymentinfo;
    }

    public ArrayList<ShoppingCartListEntityCell> getReviewOrder() {
        return reviewOrder;
    }

    public void setReviewOrder(ArrayList<ShoppingCartListEntityCell> reviewOrder) {
        this.reviewOrder = reviewOrder;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public HashMap<String,String> getDiscount() {
        return discount;
    }

    public void setDiscount(HashMap<String,String> discount) {
        this.discount = discount;
    }

    public HashMap<String,String> getShipping() {
        return shipping;
    }

    public void setShipping(HashMap<String,String> shipping) {
        this.shipping = shipping;
    }
}

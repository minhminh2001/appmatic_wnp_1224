package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class MyAccountOrderDetailEntityResult extends SVRReturnEntity{
    private String orderId;
    private String date;
    private String state;
    private String status;
    private String customerName;
    private String subtotal;
    private String shippingFee;
    private String grandTotal;
    private CheckoutPaymentReturnShippingAddress shippingAddress;
    private CheckoutPaymentReturnShippingAddress billingAddress;
    private PickUpAddress pickupAddress;
    private String paymentMethod;
    private String orderComment;
    private int isBanktransfer;
    private BanktransferBean banktransfer;
    private String storeCredit;
    private String gst;

    private int isRPayment;
    private int isPickupInStore;
    private String paymentCode;
    private List<MyAccountOrderMiddle> suborders;
    private HashMap<String,String> discount;
    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public int getIsRPayment() {
        return isRPayment;
    }

    public void setIsRPayment(int isRPayment) {
        this.isRPayment = isRPayment;
    }

    public int getIsPickupInStore() {
        return isPickupInStore;
    }

    public void setIsPickupInStore(int isPickupInStore) {
        this.isPickupInStore = isPickupInStore;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }




    public String getStoreCredit() {
        return storeCredit;
    }
    public void setStoreCredit(String storeCredit) {
        this.storeCredit = storeCredit;
    }
    public BanktransferBean getBanktransfer() {
        return banktransfer;
    }

    public void setBanktransfer(BanktransferBean banktransfer) {
        this.banktransfer = banktransfer;
    }

    public int getIsBanktransfer() {
        return isBanktransfer;
    }

    public void setIsBanktransfer(int isBanktransfer) {
        this.isBanktransfer = isBanktransfer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public CheckoutPaymentReturnShippingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(CheckoutPaymentReturnShippingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public CheckoutPaymentReturnShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(CheckoutPaymentReturnShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PickUpAddress getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(PickUpAddress pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public HashMap<String, String> getDiscount() {
        return discount;
    }

    public void setDiscount(HashMap<String, String> discount) {
        this.discount = discount;
    }

    public List<MyAccountOrderMiddle> getSuborders() {
        return suborders;
    }

    public void setSuborders(List<MyAccountOrderMiddle> suborders) {
        this.suborders = suborders;
    }

    public static class PickUpAddress implements Serializable {
        private String title;
        private String address;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

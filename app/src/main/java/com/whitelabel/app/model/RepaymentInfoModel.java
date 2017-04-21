package com.whitelabel.app.model;

/**
 * Created by ray on 2017/4/21.
 */

public class RepaymentInfoModel {


    /**
     * id : 27638
     * paymentMethod : paypal_standard
     * grandTotal : 235.0000
     * unit : HKD
     * orderSn : 100027675
     * status : 1
     */

    private String id;
    private String paymentMethod;
    private String grandTotal;
    private String unit;
    private String orderSn;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * MyAccountOrder:outer datas
 */
public class MyAccountOrderOuter extends SVRReturnEntity implements Serializable{
    private String orderId;
    private String orderSn;
    private String date;
    private String total;
    private String totalFormatted;
    private String status;
    private MyAccountOrderMiddle[] suborders;//MyAccountOrderMiddle

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MyAccountOrderMiddle[] getSuborders() {
        return suborders;
    }

    public void setSuborders(MyAccountOrderMiddle[] suborders) {
        this.suborders = suborders;
    }
}

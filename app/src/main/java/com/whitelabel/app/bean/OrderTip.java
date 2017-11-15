package com.whitelabel.app.bean;

/**
 * Created by Administrator on 2016/4/8.
 */
public class OrderTip {
    private int type;
    private String OrderNumber;
    private String OrderDate;
    private String OrderSttus;
    private String OrderId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getOrderSttus() {
        return OrderSttus;
    }

    public void setOrderSttus(String orderSttus) {
        OrderSttus = orderSttus;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}

package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/16.
 */
public class CheckoutOrderStatusEntity extends SVRReturnEntity {

    private int status;

    private String order_status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

}

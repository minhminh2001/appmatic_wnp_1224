package com.whitelabel.app.ui.checkout.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/19.
 */

public class RequestOrderNumberResponse implements Serializable {
    private int status;
    private String orderSn;

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

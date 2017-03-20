package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/8.
 */
public class CheckoutDefaultShippingAddressEntity extends SVRReturnEntity{

    private int status;
    private CheckoutDefaultShippingAddress address;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CheckoutDefaultShippingAddress getAddress() {
        return address;
    }

    public void setAddress(CheckoutDefaultShippingAddress address) {
        this.address = address;
    }
}

package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class CheckoutSelectShippingAddressEntity extends SVRReturnEntity{

    private int status;
    private List<CheckoutDefaultShippingAddress> address;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CheckoutDefaultShippingAddress> getAddress() {
        return address;
    }

    public void setAddress(List<CheckoutDefaultShippingAddress> address) {
        this.address = address;
    }
}

package com.whitelabel.app.ui.checkout.model;

import com.whitelabel.app.model.CheckoutDefaultShippingAddress;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutDefaultAddressResponse implements Serializable {
    private CheckoutDefaultShippingAddress  primaryBilling;
    private CheckoutDefaultShippingAddress  primaryShipping;

    public CheckoutDefaultShippingAddress getPrimaryBilling() {
        return primaryBilling;
    }

    public void setPrimaryBilling(CheckoutDefaultShippingAddress primaryBilling) {
        this.primaryBilling = primaryBilling;
    }

    public CheckoutDefaultShippingAddress getPrimaryShipping() {
        return primaryShipping;
    }

    public void setPrimaryShipping(CheckoutDefaultShippingAddress primaryShipping) {
        this.primaryShipping = primaryShipping;
    }
}

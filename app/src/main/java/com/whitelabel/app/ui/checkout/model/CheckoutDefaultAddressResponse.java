package com.whitelabel.app.ui.checkout.model;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutDefaultAddressResponse implements Serializable {
    private AddressBook primaryBilling;
    private AddressBook  primaryShipping;
    public AddressBook getPrimaryBilling() {
        return primaryBilling;
    }
    public void setPrimaryBilling(AddressBook primaryBilling) {
        this.primaryBilling = primaryBilling;
    }
    public AddressBook getPrimaryShipping() {
        return primaryShipping;
    }
    public void setPrimaryShipping(AddressBook primaryShipping) {
        this.primaryShipping = primaryShipping;
    }
}

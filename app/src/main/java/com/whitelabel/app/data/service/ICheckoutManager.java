package com.whitelabel.app.data.service;

import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;

import java.util.Observable;

/**
 * Created by Administrator on 2017/7/21.
 */

public interface ICheckoutManager {

    public rx.Observable<CheckoutDefaultAddressResponse> getCheckoutDefaultAddress(String sessionKey);
}

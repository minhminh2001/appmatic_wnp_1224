package com.whitelabel.app.data.service;


import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

import rx.Observable;
/**
 * Created by Administrator on 2017/7/18.
 */
public interface ICheckoutManager {
    public Observable<PaypalPlaceOrderReponse> paypalPlaceOrder(String sessionKey);

    public Observable<RequestOrderNumberResponse> requestOrderNumber(String sessionKey);

    public rx.Observable<CheckoutDefaultAddressResponse> getCheckoutDefaultAddress(String sessionKey);

}
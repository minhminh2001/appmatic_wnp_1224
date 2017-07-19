package com.whitelabel.app.data.service;

import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;

import rx.Observable;
/**
 * Created by Administrator on 2017/7/18.
 */
public interface ICheckoutManager {
    public Observable<PaypalPlaceOrderReponse> paypalPlaceOrder(String sessionKey);
}

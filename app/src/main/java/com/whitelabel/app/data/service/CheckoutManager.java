package com.whitelabel.app.data.service;

import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;

import rx.Observable;
import rx.functions.Action1;
/**
 * Created by Administrator on 2017/7/18.
 */
public class CheckoutManager implements ICheckoutManager {
    private CheckoutApi checkoutApi;
    private ICacheApi  iCacheApi;
    public CheckoutManager(CheckoutApi  checkoutApi,ICacheApi iCacheApi){
        this.checkoutApi=checkoutApi;
        this.iCacheApi=iCacheApi;
    }
    @Override
    public Observable<PaypalPlaceOrderReponse> paypalPlaceOrder(String sessionKey) {
        return  checkoutApi.payPalPlaceOrder(sessionKey)
                .doOnNext(new Action1<PaypalPlaceOrderReponse>() {
                    @Override
                    public void call(PaypalPlaceOrderReponse paypalPlaceOrderReponse) {
                        if(paypalPlaceOrderReponse.getStatus()==-1){
                            Observable.error(new ApiException(paypalPlaceOrderReponse.getErrorMessage()));
                        }
                    }
                });
    }
}

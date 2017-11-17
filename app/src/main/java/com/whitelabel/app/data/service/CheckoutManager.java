package com.whitelabel.app.data.service;

import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/21.
 */

public class CheckoutManager implements ICheckoutManager {
    private CheckoutApi checkoutApi;
    public CheckoutManager (CheckoutApi checkoutApi){
            this.checkoutApi=checkoutApi;
    }
    @Override
    public Observable<RequestOrderNumberResponse> requestOrderNumber(String sessionKey) {
        return checkoutApi.requestOrderNumber(sessionKey);
    }
    @Override
    public Observable<PaypalPlaceOrderReponse> paypalPlaceOrder(String sessionKey,String orderComment,String appVersion) {
        return checkoutApi.payPalPlaceOrder(sessionKey,orderComment,appVersion)
                .flatMap(new Func1<PaypalPlaceOrderReponse, Observable<PaypalPlaceOrderReponse>>() {
                    @Override
                    public Observable<PaypalPlaceOrderReponse> call(PaypalPlaceOrderReponse paypalPlaceOrderReponse) {
                        if (paypalPlaceOrderReponse.getStatus() == -1) {
                            return Observable.error(new ApiException(paypalPlaceOrderReponse.getErrorMessage()));
                        }else{
                            return Observable.just(paypalPlaceOrderReponse);
                        }
                    }
                });
    }
    @Override
    public Observable<CheckoutDefaultAddressResponse> getCheckoutDefaultAddress(String sessionKey) {
        return checkoutApi.getDefaultAddress(sessionKey)
           .flatMap(new Func1<ResponseModel<CheckoutDefaultAddressResponse>, Observable<CheckoutDefaultAddressResponse>>() {
            @Override
            public Observable<CheckoutDefaultAddressResponse> call(ResponseModel<CheckoutDefaultAddressResponse> checkoutDefaultAddressResponseResponseModel) {
                if(checkoutDefaultAddressResponseResponseModel.getStatus()==-1){
                    return Observable.error(new ApiException(checkoutDefaultAddressResponseResponseModel.getErrorMessage()));
                }
                return Observable.just(checkoutDefaultAddressResponseResponseModel.getData());
            }
        });

    }
}

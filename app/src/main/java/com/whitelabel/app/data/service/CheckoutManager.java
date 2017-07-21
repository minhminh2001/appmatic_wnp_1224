package com.whitelabel.app.data.service;

import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;

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
    public Observable<CheckoutDefaultAddressResponse> getCheckoutDefaultAddress(String sessionKey) {
        return checkoutApi.getDefaultAddress(sessionKey).doOnNext(new Action1<ResponseModel<CheckoutDefaultAddressResponse>>() {
            @Override
            public void call(ResponseModel<CheckoutDefaultAddressResponse> checkoutDefaultAddressResponseResponseModel) {
                  if(checkoutDefaultAddressResponseResponseModel.getStatus()==-1){
                      Observable.error(new ApiException(checkoutDefaultAddressResponseResponseModel.getErrorMessage()));
                  }
            }
        }).map(new Func1<ResponseModel<CheckoutDefaultAddressResponse>, CheckoutDefaultAddressResponse>() {
            @Override
            public CheckoutDefaultAddressResponse call(ResponseModel<CheckoutDefaultAddressResponse> checkoutDefaultAddressResponseResponseModel) {
                return checkoutDefaultAddressResponseResponseModel.getData();
            }
        });
    }
}

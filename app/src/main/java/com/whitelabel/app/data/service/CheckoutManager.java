package com.whitelabel.app.data.service;

import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/21.
 */
public class CheckoutManager implements ICheckoutManager {
    private CheckoutApi checkoutApi;
    private ICacheApi cacheHelper;
    public CheckoutManager (CheckoutApi checkoutApi, ICacheApi preferHelper){
        this.checkoutApi=checkoutApi;
        this.cacheHelper=preferHelper;
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

    @Override
    public Observable<SVRAppServiceCustomerCountry> getCountryAndRegions(String sessionKey) {
        return checkoutApi.getCountryAndRegions(sessionKey).map(new Func1<SVRAppServiceCustomerCountry, SVRAppServiceCustomerCountry>() {
            @Override
            public SVRAppServiceCustomerCountry call(SVRAppServiceCustomerCountry svrAppServiceCustomerCountry) {
                if(svrAppServiceCustomerCountry.getStatus()==1){
                    cacheHelper.saveCountryAndRegions(svrAppServiceCustomerCountry);
                }
                return svrAppServiceCustomerCountry;
            }
        });
    }


    @Override
    public void saveFinishOrderAndMarkTime(long currentTime) {
        cacheHelper.saveFinishOrderAndMarkTime(currentTime);
    }

    @Override
    public SkipToAppStoreMarket getFirstOrderAndMarkTime() {
        return cacheHelper.getFirstOrderAndMarkTime();
    }


    @Override
    public SVRAppServiceCustomerCountry getCountryAndRegions() {
        return cacheHelper.getCountryAndRegions();
    }

    @Override
    public Observable<SVRAddAddress> createCustomerAddress(String sessionKey, String firstName, String lastName, String countryId, String telePhone, String street0, String street1, String fax, String postCode, String city, String region, String defaultBilling, String defaultShipping, String regionId) {
        return checkoutApi.createCustomerAddress(sessionKey,firstName,lastName,countryId,telePhone,street0,street1,fax,postCode,city,region,defaultBilling,defaultShipping,regionId);
    }


}

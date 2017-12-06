package com.whitelabel.app.data.service;


import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

import rx.Observable;
/**
 * Created by Administrator on 2017/7/18.
 */
public interface ICheckoutManager {
    public Observable<PaypalPlaceOrderReponse> paypalPlaceOrder(String sessionKey,String orderComment,String appVersion);

    public Observable<RequestOrderNumberResponse> requestOrderNumber(String sessionKey);

    public rx.Observable<CheckoutDefaultAddressResponse> getCheckoutDefaultAddress(String sessionKey);

    public void saveFinishOrderAndMarkTime(long currentTime);

    public Observable<SVRAppServiceCustomerCountry> getCountryAndRegions(String sessionKey);

    public SkipToAppStoreMarket getFirstOrderAndMarkTime();

    public SVRAppServiceCustomerCountry getCountryAndRegions();

    public Observable<SVRAddAddress>  createCustomerAddress(
            String sessionKey,
            String firstName,
            String lastName,
            String countryId,
            String telePhone,
            String street0,
            String street1,
            String fax,
            String postCode,
            String city,
            String region,
            String defaultBilling,
            String defaultShipping,
            String regionId);

}
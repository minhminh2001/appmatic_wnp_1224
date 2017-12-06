package com.whitelabel.app.data.retrofit;

import android.text.TextUtils;

import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

import java.util.TreeMap;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface CheckoutApi {
    @GET("appservice/customer/getCheckoutAddress")
    public Observable<ResponseModel<CheckoutDefaultAddressResponse>> getDefaultAddress(@Query("session_key") String sessionKey);
    @FormUrlEncoded
    @POST("appservice/checkout/paypalPlaceOrder")
    public Observable<PaypalPlaceOrderReponse>  payPalPlaceOrder(@Field("session_key") String sessionKey,@Field("order_comments") String orderComments,@Field("app_version") String appVersion);

    @GET("appservice/checkout/success")
    public Observable<RequestOrderNumberResponse>  requestOrderNumber(@Query("session_key") String sessionKey);

    @GET("appservice/directory/getCountryAndRegionList")
    public Observable<SVRAppServiceCustomerCountry>  getCountryAndRegions(@Query("session_key") String sessionKey);

    @POST("appservice/customer/createCustomerAddress")
    public Observable<SVRAddAddress>  createCustomerAddress(
            @Query("session_key") String sessionKey,
            @Query("firstname") String firstName,
            @Query("lastname") String lastName,
            @Query("country_id") String countryId,
            @Query("telephone") String telePhone,
            @Query("street[0]") String street0,
            @Query("street[1]") String street1,
            @Query("fax") String fax,
            @Query("postcode") String postCode,
            @Query("city") String city,
            @Query("region") String region,
            @Query("default_billing") String defaultBilling,
            @Query("default_shipping") String defaultShipping,
            @Query("region_id") String regionId);


}

package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;

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
}

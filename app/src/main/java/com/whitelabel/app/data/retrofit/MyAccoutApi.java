package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.model.SubscriberResponse;
import com.whitelabel.app.model.WishDelEntityResult;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/12.
 */
public interface MyAccoutApi {

    @GET("appservice/customer/customerAddressInfo")
    public rx.Observable<AddresslistReslut> getAddressList(@Query("session_key") String sessionKey);

    @GET("appservice/customer/removeAddress")
    public rx.Observable<ResponseModel> deleteAddressById(@Query("session_key") String sessionKey,
        @Query("address_id") String addressId);

    @FormUrlEncoded
    @POST("appservice/wishlist/remove")
    public rx.Observable<WishDelEntityResult> deleteWishById(
        @Field("session_key") String sessionKey, @Field("item_id") String itemId);

    @FormUrlEncoded
    @POST("appservice/wishlist/addToWishList")
    public rx.Observable<AddToWishlistEntity> addWish(@Field("session_key") String sessionKey,
        @Field("product_id") String productId);

    @GET("appservice/notification/unreads/{user_id}")
    public rx.Observable<ResponseModel<NotificationUnReadResponse>> getNotificationUnReadResponse(
        @Path("user_id") String userId);

    @FormUrlEncoded
    @POST("appservice/customer/oneallLogin")
    public rx.Observable<SVRAppserviceCustomerFbLoginReturnEntity> threePartLogin(
        @Field("givenName") String givenName
        , @Field("displayName") String displayName
        , @Field("formatted") String formatted
        , @Field("familyName") String familyName
        , @Field("email") String email
        , @Field("identity_token") String identityToken
        , @Field("user_token") String userToken
        , @Field("provider") String provider
        , @Field("boundEmail") String boundEmail);

    @FormUrlEncoded
    @POST("appservice/customer/setsubscriberstatus")
    public rx.Observable<ResponseModel> setUserAgreement(@Field("session_key") String sessionKey,
        @Field("newsletterSubscribed") String isSubscried);

    @FormUrlEncoded
    @POST("appservice/customer/getsubscriberstatus")
    rx.Observable<SubscriberResponse> getUserAgreement(@Field("session_key") String sessionKey);

    @FormUrlEncoded
    @POST("appservice/customer/create")
    rx.Observable<ResponseModel> registerEmail(@FieldMap Map<String,String> params );

    @FormUrlEncoded
    @POST("appservice/customer/login")
    rx.Observable<SVRAppServiceCustomerLoginReturnEntity> loginEmail(
        @FieldMap Map<String, String> map);
}

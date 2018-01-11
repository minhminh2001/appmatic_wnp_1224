package com.whitelabel.app.data.retrofit;

import com.google.gson.JsonObject;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface ShoppingCartApi {
    @FormUrlEncoded
    @POST("appservice/cart/list")
    public Observable<JsonObject> getShoppingCartInfo(@Field("session_key") String sessionKey);
    @FormUrlEncoded
    @POST("appservice/cart/couponPost")
    public Observable<ShoppingCartVoucherApplyEntity>
    applyOrCancelVoucherCode(@Field("session_key")String sessionKey,@Field("coupon_code") String couponCode,@Field("remove") String  state);
    @FormUrlEncoded
    @POST("appservice/cart/checkCartStock")
    public Observable<ResponseModel> checkoutOutOfStock(@Field("session_key") String sessionKey);
    @FormUrlEncoded
    @POST("appservice/cart/delete")
    public  Observable<ShoppingCartDeleteCellEntity>  deleteProductFromShoppingCart(@Field("session_key")String sessionKey,@Field("id") String id);
    @FormUrlEncoded
    @POST("appservice/cart/updateItemAmountCart")
    public Observable<ShoppingCartDeleteCellEntity> setProductCountFromShoppingCart(@Field("session_key")String sessionKey, @FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("appservice/cart/add")
    public Observable<ResponseModel>  addProductToShoppingCart(@Field("session_key") String sessionKey,@Field("product_id") String productId,@FieldMap Map<String,String> param);
}

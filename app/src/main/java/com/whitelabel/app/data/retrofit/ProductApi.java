package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by Administrator on 2017/2/28.
 */
public interface ProductApi {
    @GET ("appservice/brands/view")
    public Observable<BrandStoreModel> getProductListByBrandId(@Query("brand") String brandId
            , @Query("p") String offset
            , @Query("limit") String limit
            , @Query("session_key") String sessionKey
            , @Query("price") String price
            , @Query("order") String order
            , @Query("dir") String dir
            , @Query("model_type") String model_type);
    @FormUrlEncoded
    @POST("appservice/wishlist/remove")
    public  Observable<WishDelEntityResult>   deleteWistListById(@Field("session_key") String sessionKey, @Field("item_id") String itemId);
    @FormUrlEncoded
    @POST("appservice/wishlist/addToWishList")
    public  Observable<AddToWishlistEntity> addWishList(@Field("session_key") String sessionKey, @Field("product_id") String productId);
    @GET("appservice/catalogSearch")
    public Observable<SVRAppserviceCatalogSearchReturnEntity>  getCategoryList();
}

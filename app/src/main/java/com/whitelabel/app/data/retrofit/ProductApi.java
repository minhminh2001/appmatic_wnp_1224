package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.SearchFilterResponse;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.WishDelEntityResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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
    @GET("appservice/category/menuDetail")
    public Observable<ResponseModel<CategoryDetailNewModel>>  getCategoryDetail(@Query("menu_id") String categoryId , @Query("session_key") String sessionKey);
    @GET("appservice/category/menuDetail")
    public Observable<ResponseModel<ShopBrandResponse>>  getShopBrandDetail(@Query("menu_id") String categoryId , @Query("session_key") String sessionKey);
    @GET("appservice/product/getRelatedProducts")
    public  Observable<ResponseModel<BindProductResponseModel>>  getRelateProducts(@Query("productId") String productId);
    @FormUrlEncoded
    @POST("appservice/cart/addBoughtTogether")
    public Observable<ResponseModel> addBoughtTogether(@Field("relatedProductIds") String productIds,@Field("session_key") String sessionKey);
    @POST("appservice/catalogSearch")
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getBaseCategory();
    @POST("appservice/catalogSearch")
    public Observable<CategoryBaseBean> getBaseCategoryV2();
    @GET("appservice/product/detail")
    public Observable<SVRAppserviceProductDetailReturnEntity> getProductDetail(@Query("session_key") String sessionKey,@Query("product_id") String productId);
    @GET("appservice/product/recommendedlist")
    public Observable<SVRAppserviceProductRecommendedReturnEntity>  getRecommendedList(@Query("store_id") String storeId,
                                                                                       @Query("limit") String limit,
                                                                                       @Query("product_id") String productId,
                                                                                       @Query("session_key") String sessionKey);
    @FormUrlEncoded
    @POST("appservice/catalogSearch/getSuggest")
    public Observable<SearchFilterResponse> autoHintSearch(@Field("session_key") String sessionKey,@Field("q") String keyword);

    @FormUrlEncoded
    @POST("appservice/order/reOrderProduct")
    public Observable<ResponseModel> setToCheckout(@FieldMap HashMap<String,String> maps);


}
package com.whitelabel.app.data.service;

import com.twitter.sdk.android.core.models.Search;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.NotifyMeResponse;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.RecentSearchKeywordResponse;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.SearchFilterResponse;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.ShoppingCartListEntityCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface ICommodityManager {
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getAllCategoryManager();
    public Observable<CategoryBaseBean> getAllCategoryManagerV2();
    public Observable<Integer> getLocalShoppingProductCount();
    public Observable<List<AddressBook>> getAddressListCache(String userId);
    public Observable<CategoryDetailNewModel> getCategoryDetail(boolean isCache, String category, String sessionKey);
    public Observable<ShopBrandResponse> getShopBrandDetail(boolean isCache, String category, String sessionKey);
    public Observable<ProductDetailModel> getProductDetail(String sessionKey,String productId);
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getLocalCategoryManager();
    public  Observable<SVRAppserviceProductRecommendedReturnEntity> getProductRecommendList(String storeId,String limit,String productId,String sessionKey);
    public  Observable<BindProductResponseModel> getRelateProducts(String productId);
    public  Observable<ResponseModel> addBoughtTogether(String productId,String sessionKey);
    public  Observable<SearchFilterResponse> autoHintSearch(String sessionKey,String keyword);
    public  Observable<ResponseModel> setToCheckout(HashMap<String,String> maps);
    public  Observable<NotifyMeResponse> registerNotifyForProduct(String productId,
                                                                  String email,
                                                                  String name,
                                                                  String stroreId,
                                                                  String sessionKey);
    public  Observable<RecentSearchKeywordResponse> getRecentSearchKeywords(String storeId, String sessionKey);
    public  Observable<RecentSearchKeywordResponse> saveRecentSearchKeyword(String keyword, String sessionKey);
}

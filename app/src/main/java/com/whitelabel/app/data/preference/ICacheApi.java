package com.whitelabel.app.data.preference;

import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/7/7.
 */

public interface ICacheApi {

    void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel);

    void saveCurrency(String currency);

    String getCurrency();

    String getVersionNumber();

    RemoteConfigResonseModel.RetomeConfig getLocalConfigModel();

    void saveAddressList(String userId, List<AddressBook> beans);

    rx.Observable<List<TMPLocalCartRepositoryProductEntity>> getShoppingCartProduct();

    rx.Observable<List<AddressBook>> getAddressListCache(final String userId);

    void saveCategoryDetail(String menuId, CategoryDetailNewModel categoryDetailModel);

    rx.Observable<CategoryDetailNewModel> getCategoryDetail(final String categoryId);

    GOUserEntity getUser();

    void saveUser(GOUserEntity goUserEntity);

    void saveBaseCategory(SVRAppserviceCatalogSearchReturnEntity allCategorys);

    void saveBaseCategoryV2(CategoryBaseBean categoryBaseBean);

    rx.Observable<SVRAppserviceCatalogSearchReturnEntity> getBaseCategory();

    rx.Observable<CategoryBaseBean> getBaseCategoryV2();

    void saveFinishOrderAndMarkTime(long currentTime);

    SkipToAppStoreMarket getFirstOrderAndMarkTime();

    rx.Observable<ShopBrandResponse> getShopBrandDetail();

    void saveShopBrandDetail(ShopBrandResponse shopBrandResponse);

    void saveGuideFlag(Boolean isFirst);

    boolean isGuide();

    void saveCountryAndRegions(SVRAppServiceCustomerCountry countryEntityResult);

    SVRAppServiceCustomerCountry getCountryAndRegions();

    rx.Observable<Boolean> addProductDetailToLocal(
        List<ShoppingItemLocalModel> shoppingItemLocalModels);

    rx.Observable<List<ShoppingItemLocalModel>> getShoppingListFromLocal();

    rx.Observable<Boolean> deleteItemById(String simpleId);

    rx.Observable<Boolean> updateNumberByiD(String simpleId,String number);

    Observable<Boolean> deleteShoppingItem(String simpleId);

    Observable<Boolean> updateLocalShoppingItemNumber(String simpleId,String s);

    Observable<Boolean> clearShoppingItem();
}

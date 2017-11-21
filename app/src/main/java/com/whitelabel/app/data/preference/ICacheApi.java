package com.whitelabel.app.data.preference;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import java.util.List;
import java.util.Observable;

/**
 * Created by Administrator on 2017/7/7.
 */

public interface ICacheApi {
     void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel);
     void saveCurrency(String currency);
    String getCurrency();
    String getVersionNumber();
    RemoteConfigResonseModel.RetomeConfig  getLocalConfigModel();
    void  saveAddressList(String userId,List<AddressBook> beans);
    rx.Observable<List<TMPLocalCartRepositoryProductEntity>> getShoppingCartProduct();
    rx.Observable<List<AddressBook>> getAddressListCache(final String userId);
     void saveCategoryDetail(String menuId,CategoryDetailNewModel categoryDetailModel);
    rx.Observable<CategoryDetailNewModel> getCategoryDetail(final String categoryId);
    GOUserEntity getUser();
    void saveUser(GOUserEntity goUserEntity);
    void saveBaseCategory(SVRAppserviceCatalogSearchReturnEntity allCategorys);
    rx.Observable<SVRAppserviceCatalogSearchReturnEntity> getBaseCategory();
    void saveFinishOrderAndMarkTime(long currentTime);
    SkipToAppStoreMarket getFirstOrderAndMarkTime();
}

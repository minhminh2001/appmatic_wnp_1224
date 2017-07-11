package com.whitelabel.app.data.preference;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import java.util.List;

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
     void saveCategoryDetail(CategoryDetailModel categoryDetailModel);
    rx.Observable<CategoryDetailModel> getCategoryDetail(final String categoryId);
    GOUserEntity getUser();
    void saveUser(GOUserEntity goUserEntity);
}

package com.whitelabel.app.data.service;

import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ResponseModel;

import rx.Observable;

/**
 * Created by Administrator on 2017/7/5.
 */
public interface IAccountManager {
    public Observable<AddresslistReslut> getAddressList(final String sessionKey);
    public Observable<ResponseModel> deleteAddressById(final String sessionKey, String addressId);
}

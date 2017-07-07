package com.whitelabel.app.data.service;

import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.utils.RxUtil;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/5.
 */
public class AccountManager implements IAccountManager{
    private MyAccoutApi  myAccoutApi;
    public AccountManager(MyAccoutApi myAccoutApi){
        this.myAccoutApi=myAccoutApi;
    }
    @Override
    public Observable<ResponseModel>  deleteAddressById(final String sessionKey, String addressId) {
       return myAccoutApi.deleteAddressById(sessionKey,addressId)
               .compose(RxUtil.<ResponseModel>rxSchedulerHelper());
    }
    @Override
    public Observable<AddresslistReslut> getAddressList(final String sessionKey) {
        return myAccoutApi.getAddressList(sessionKey)
                .compose(RxUtil.<AddresslistReslut>rxSchedulerHelper()).onErrorResumeNext(new Func1<Throwable, Observable<AddresslistReslut>>() {
                    @Override
                    public Observable< AddresslistReslut> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                });
    }

    @Override
    public Observable<ResponseModel> addWishlist(String sessionKey, String productId) {
        return myAccoutApi.addWish(sessionKey,productId);
    }

    @Override
    public Observable<ResponseModel> deleteWishlist(String sessionKey, String itemId) {
        return  myAccoutApi.deleteWishById(sessionKey,itemId);
    }
}

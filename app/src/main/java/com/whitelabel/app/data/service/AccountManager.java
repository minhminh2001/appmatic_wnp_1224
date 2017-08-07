package com.whitelabel.app.data.service;

import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.utils.RxUtil;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/5.
 */
public class AccountManager implements IAccountManager{
    private MyAccoutApi  myAccoutApi;
    private ICacheApi iCacheApi;
    @Inject
    public AccountManager(MyAccoutApi myAccoutApi,ICacheApi iCacheApi){
        this.myAccoutApi=myAccoutApi;
        this.iCacheApi=iCacheApi;
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
    public Observable<AddToWishlistEntity> addWishlist(String sessionKey, String productId) {
        return myAccoutApi.addWish(sessionKey,productId)
                .flatMap(new Func1<AddToWishlistEntity,Observable<AddToWishlistEntity> >() {
                    @Override
                    public Observable<AddToWishlistEntity>  call(AddToWishlistEntity addToWishlistEntity) {
                        if(addToWishlistEntity.getStatus()==-1){
                          return  Observable.error(new ApiException(addToWishlistEntity.getErrorMessage()));
                        }else{
                            return Observable.just(addToWishlistEntity);
                        }
                    }
                });
    }
    @Override
    public Observable<WishDelEntityResult> deleteWishlist(String sessionKey, String itemId) {
        return  myAccoutApi.deleteWishById(sessionKey,itemId)
                .flatMap(new Func1<WishDelEntityResult, Observable<WishDelEntityResult> >() {
                    @Override
                    public Observable<WishDelEntityResult>  call(WishDelEntityResult wishDelEntityResult) {
                        if(wishDelEntityResult.getStatus()==-1){
                           return  Observable.error(new ApiException(wishDelEntityResult.getErrorMessage()));
                        }else{
                            return Observable.just(wishDelEntityResult);
                        }
                    }
                });
    }

    @Override
    public Observable<NotificationUnReadResponse> getNotificationUnReadCount(String userId) {
        return myAccoutApi.getNotificationUnReadResponse(userId)
                .flatMap(new Func1<ResponseModel<NotificationUnReadResponse>, Observable<NotificationUnReadResponse>>() {
                    @Override
                    public Observable<NotificationUnReadResponse> call
                            (ResponseModel<NotificationUnReadResponse> bean) {
                        if(bean.getCode()==1){
                            return Observable.just(bean.getData());
                        }else{
                            return Observable.error(new ApiException(bean.getErrorMessage()));
                        }
                    }
                });
    }
}

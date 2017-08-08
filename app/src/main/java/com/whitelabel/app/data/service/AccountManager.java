package com.whitelabel.app.data.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.data.retrofit.OneAllApi;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.model.ResponseConnection;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.utils.JLogUtils;
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
    private OneAllApi oneAllApi;
    @Inject
    public AccountManager(MyAccoutApi myAccoutApi,ICacheApi iCacheApi,OneAllApi oneAllApi){
        this.myAccoutApi=myAccoutApi;
        this.iCacheApi=iCacheApi;
        this.oneAllApi=oneAllApi;
    }
    @Override
    public Observable<ResponseConnection> getOneAllUser(String platform, String accessToken, String secret) {
        com.whitelabel.app.model.NativeLoginRequest request = new com.whitelabel.app.model.NativeLoginRequest(platform, accessToken, secret);
        return  oneAllApi.info(request)
                .map(new Func1<JsonObject, ResponseConnection>() {
                    @Override
                    public ResponseConnection call(JsonObject jsonObject) {
                        JsonObject  result=jsonObject.getAsJsonObject("response");
                        JsonObject jsonObject1= result.getAsJsonObject("result");
                        String resultStr=jsonObject1.toString();
                        Gson gson=new Gson();
                        ResponseConnection responseConnection=gson.fromJson(resultStr,ResponseConnection.class);
                        return responseConnection;
                    }
                });
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

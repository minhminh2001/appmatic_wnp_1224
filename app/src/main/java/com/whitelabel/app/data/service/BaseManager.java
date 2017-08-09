package com.whitelabel.app.data.service;

import com.google.gson.JsonObject;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.BaseApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BaseManager implements IBaseManager {
    private MockApi mockApi;
    private BaseApi appApi;
    private ICacheApi cacheApi;
    @Inject
    public BaseManager(MockApi mockApi , BaseApi appApi, ICacheApi preferHelper){
        this.mockApi=mockApi;
        this.appApi=appApi;
        this.cacheApi =preferHelper;
    }
    @Override
    public boolean isSign() {
        return cacheApi.getUser()==null?false:true;
    }
    @Override
    public GOUserEntity getUser() {
        return cacheApi.getUser();
    }
    @Override
    public void saveUser(GOUserEntity goUserEntity) {
        cacheApi.saveUser(goUserEntity);
    }
    @Override
    public Observable<RemoteConfigResonseModel> getConfigInfo() {
        String userId= cacheApi.getVersionNumber();
       return  mockApi.getConfigInfo(userId)
                   .doOnNext(new Action1<RemoteConfigResonseModel>() {
                   @Override
                   public void call(RemoteConfigResonseModel remoteConfigResonseModel) {
                       cacheApi.saveConfigInfo(remoteConfigResonseModel);
                   }
               });
    }
    @Override
    public Observable<GOCurrencyEntity> getCurrencyUnit(String sessionKey, String deviceToken) {
        return  appApi.openApp(sessionKey,deviceToken).
                flatMap(new Func1<JsonObject, Observable<GOCurrencyEntity>>() {
            @Override
            public Observable<GOCurrencyEntity> call(JsonObject jsonObject) {
                return Observable.just(parseCurrentUnitJson(jsonObject));
            }
        }).doOnNext(new Action1<GOCurrencyEntity>() {
            @Override
            public void call(GOCurrencyEntity goCurrencyEntity) {
                cacheApi.saveCurrency(goCurrencyEntity.getName());
            }
        });
    }
    public GOCurrencyEntity  parseCurrentUnitJson(JsonObject jsonObject){
        GOCurrencyEntity  goCurrencyEntity=null;
        try {
            JsonObject jsonObj = jsonObject.getAsJsonObject("data");
            String unit=jsonObj.get("unit").getAsString();
            goCurrencyEntity=new GOCurrencyEntity();
            goCurrencyEntity.setName(unit);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return goCurrencyEntity;
    }

    @Override
    public Observable<SVRAppServiceCustomerLoginReturnEntity> emailLogin(String email, String password, String deviceToken,String appVersion,String platId,String serviceVersion) {
        return appApi.emailLogin(email,password,deviceToken,appVersion,platId,serviceVersion);
    }
}

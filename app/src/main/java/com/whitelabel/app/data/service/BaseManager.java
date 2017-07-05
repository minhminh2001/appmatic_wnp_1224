package com.whitelabel.app.data.service;

import com.google.gson.JsonObject;
import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.AppApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BaseManager implements IBaseManager {
    private MockApi mockApi;
    private AppApi appApi;
    private PreferHelper  preferHelper;
    public BaseManager(MockApi mockApi , AppApi  appApi, PreferHelper preferHelper){
        this.mockApi=mockApi;
        this.appApi=appApi;
        this.preferHelper=preferHelper;
    }
    @Override
    public Observable<RemoteConfigResonseModel> getConfigInfo() {
        String userId=preferHelper.getVersionNumber();
       return  mockApi.getConfigInfo(userId)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends RemoteConfigResonseModel>>() {
                        @Override
                        public Observable< ? extends  RemoteConfigResonseModel> call(Throwable throwable) {
                            return Observable.error(throwable);
                        }
                    }).doOnNext(new Action1<RemoteConfigResonseModel>() {
                   @Override
                   public void call(RemoteConfigResonseModel remoteConfigResonseModel) {
                       preferHelper.saveConfigInfo(remoteConfigResonseModel);
                   }
               });
    }
    @Override
    public Observable<GOCurrencyEntity> getCurrencyUnit(String sessionKey, String deviceToken) {
        return  appApi.openApp(sessionKey,deviceToken).flatMap(new Func1<JsonObject, Observable<GOCurrencyEntity>>() {
            @Override
            public Observable<GOCurrencyEntity> call(JsonObject jsonObject) {
                return Observable.just(parseCurrentUnitJson(jsonObject));
            }
        }).doOnNext(new Action1<GOCurrencyEntity>() {
            @Override
            public void call(GOCurrencyEntity goCurrencyEntity) {
                preferHelper.saveCurrency(goCurrencyEntity.getName());
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
}

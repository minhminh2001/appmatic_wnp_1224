package com.whitelabel.app.data.service;

import com.google.gson.JsonObject;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface IBaseManager {
    public Observable<RemoteConfigResonseModel> getConfigInfo();

    public Observable<GOCurrencyEntity> getCurrencyUnit(String sessionKey, String deviceToken);
}

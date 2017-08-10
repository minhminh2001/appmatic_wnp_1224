package com.whitelabel.app.model;

/**
 * Created by Administrator on 2017/8/10.
 */

public class StartZipModel {
    private RemoteConfigResonseModel remoteConfigResonseModel;
    private GOCurrencyEntity goCurrencyEntity;

    public RemoteConfigResonseModel getRemoteConfigResonseModel() {
        return remoteConfigResonseModel;

    }

    public void setRemoteConfigResonseModel(RemoteConfigResonseModel remoteConfigResonseModel) {
        this.remoteConfigResonseModel = remoteConfigResonseModel;
    }

    public GOCurrencyEntity getGoCurrencyEntity() {
        return goCurrencyEntity;
    }

    public void setGoCurrencyEntity(GOCurrencyEntity goCurrencyEntity) {
        this.goCurrencyEntity = goCurrencyEntity;
    }
}

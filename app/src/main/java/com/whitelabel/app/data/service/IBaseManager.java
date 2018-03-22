package com.whitelabel.app.data.service;

import com.google.gson.JsonObject;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.model.UserModel;

import retrofit2.http.Field;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/4.
 */

public interface IBaseManager {
    public Observable<RemoteConfigResonseModel> getConfigInfo();

    public Observable<GOCurrencyEntity> getCurrencyUnit(String sessionKey, String deviceToken);
    public boolean  isSign();
    public GOUserEntity getUser();
    public  Observable<SVRAppServiceCustomerLoginReturnEntity>  emailLogin(String email,String password,String deviceToken,String versionNumber,String plat,String serviceVersion);
    public void saveUser(GOUserEntity goUserEntity);
    public Observable<ResponseModel>  versionCheck();

}

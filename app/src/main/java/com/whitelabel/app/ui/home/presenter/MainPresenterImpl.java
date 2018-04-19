package com.whitelabel.app.ui.home.presenter;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.StoreInfo;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.MainContract;
import com.whitelabel.app.utils.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/7.
 */
public class MainPresenterImpl extends RxPresenter<MainContract.View>implements MainContract.Presenter{
    private IBaseManager iBaseManager;
    private IAccountManager iAccountManager;
    public MainPresenterImpl(IBaseManager iBaseManager,IAccountManager iAccountManager){
        this.iBaseManager=iBaseManager;
        this.iAccountManager=iAccountManager;
    }

    @Override
    public void getNotificationUnReadCount() {
        if(!iBaseManager.isSign())return ;
        String userId=iBaseManager.getUser().getId();
        Subscription subscription= iAccountManager.getNotificationUnReadCount(userId)
        .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<NotificationUnReadResponse>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(NotificationUnReadResponse notificationUnReadResponse) {
                mView.setNotificationUnReadCount(notificationUnReadResponse.getUnreads());
            }
        });
       addSubscrebe(subscription);
    }

    @Override
    public List<String> getServiceSupportedLanguageFromLocal(){
        RemoteConfigResonseModel.RetomeConfig remoteConfig = iBaseManager.getConfigInfoFromLocal();
        if(remoteConfig == null)
            return null;

        List<StoreInfo> storeInfos = remoteConfig.getStore();
        if(storeInfos == null)
            return null;

        List<String> languageCodes = new ArrayList<>();
        for(StoreInfo storeInfo : storeInfos){
            languageCodes.add(storeInfo.getCode());
        }

        return languageCodes;
    }

    @Override
    public Map<String, String>  getServiceSupportedStoreMapFromLocal() {

        RemoteConfigResonseModel.RetomeConfig remoteConfig = iBaseManager.getConfigInfoFromLocal();
        if(remoteConfig == null)
            return null;

        List<StoreInfo> storeInfos = remoteConfig.getStore();
        if(storeInfos == null)
            return null;

        Map<String, String> stringMap = new HashMap<>();
        for(StoreInfo storeInfo : storeInfos){
            stringMap.put(storeInfo.getCode(), storeInfo.getStoreId());
        }

        return stringMap;
    }
}

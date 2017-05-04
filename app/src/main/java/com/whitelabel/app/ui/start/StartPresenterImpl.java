package com.whitelabel.app.ui.start;

import com.google.gson.JsonObject;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ray on 2017/4/5.
 */

public class StartPresenterImpl extends RxPresenter<StartContract.View> implements StartContract.Presenter{
    @Override
    public void openApp(String sessionKey, String deviceToken) {
        Subscription  subscription= DataManager.getInstance().getAppApi().openApp(sessionKey,deviceToken)
                .compose(RxUtil.<JsonObject>rxSchedulerHelper())
                .map(new Func1<JsonObject, String>() {
                    @Override
                    public String call(JsonObject jsonObject) {
                        JLogUtils.i("ray","jsonObject:"+jsonObject.toString());
                        String unit="";
                        JsonObject  jsonObj= jsonObject.getAsJsonObject("data");
                        unit=jsonObj.get("unit").getAsString();
                        return unit;
                    }
                }).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        JLogUtils.i("ray","unit:"+s);
                        WhiteLabelApplication.getAppConfiguration().getCurrency().setName(s);
                        DataManager.getInstance().getPreferHelper().saveCurrency(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        JLogUtils.i("ray","response:"+throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }


    @Override
    public void getConfigInfo(String sessionKey,String deviceToken) {
        Observable openConfig= DataManager.getInstance().getAppApi().openApp(sessionKey,deviceToken);
        String currentVersionNumber= DataManager.getInstance().getPreferHelper().getVersionNumber();
        Observable<RemoteConfigResonseModel> configObservable=DataManager.getInstance().getAppApi().getConfigInfo((currentVersionNumber));
        Observable.merge(openConfig,configObservable).compose(RxUtil.rxSchedulerHelper()).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                if(o instanceof RemoteConfigResonseModel){
                    RemoteConfigResonseModel  remoteConfigResonseModel= (RemoteConfigResonseModel) o;
                    WhiteLabelApplication.getAppConfiguration().initAppConfig(
                            remoteConfigResonseModel.getData());
                    DataManager.getInstance().getPreferHelper().saveConfigInfo(remoteConfigResonseModel);
                    mView.delayStart();
                }else if(o instanceof JsonObject){
                    JsonObject jsonObject= (JsonObject) o;
                    String unit="";
                    JsonObject  jsonObj= jsonObject.getAsJsonObject("data");
                    unit=jsonObj.get("unit").getAsString();
                    WhiteLabelApplication.getAppConfiguration().getCurrency().setName(unit);
                    DataManager.getInstance().getPreferHelper().saveCurrency(unit);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
            }
        });
    }

    @Override
    public void getConfigInfo() {
       String currentVersionNumber= DataManager.getInstance().getPreferHelper().getVersionNumber();
       Subscription subscription= DataManager.getInstance().
                getAppApi().getConfigInfo(currentVersionNumber).
                compose(RxUtil.<RemoteConfigResonseModel>rxSchedulerHelper())
                .subscribe(new Action1<RemoteConfigResonseModel>() {
                    @Override
                    public void call(RemoteConfigResonseModel remoteConfigModel) {
                        if (remoteConfigModel.getCode() == 1) {
                            WhiteLabelApplication.getAppConfiguration().initAppConfig(
                                    remoteConfigModel.getData());
                            DataManager.getInstance().getPreferHelper().saveConfigInfo(remoteConfigModel);
                        }
                        mView.delayStart();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
        addSubscrebe(subscription);
    }










}

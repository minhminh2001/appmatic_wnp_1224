package com.whitelabel.app.ui.start;

import com.google.gson.JsonObject;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import java.util.Currency;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ray on 2017/4/5.
 */

public class StartPresenterImpl extends RxPresenter<StartContract.View> implements StartContract.Presenter{
    private IBaseManager configService;
    private long mStartTimeLong;
    public  void setStartTime(){
        mStartTimeLong=System.currentTimeMillis();
    }
    public void timeOutJudgment(){
         long  offset=System.currentTimeMillis()-mStartTimeLong;
         if(offset<2000){
                mView.postDelayed(offset);
         }else{
                mView.startNextActivity();
         }
    }
    public StartPresenterImpl(StartContract.View view, IBaseManager configService){
        this.configService=configService;
        this.mView=view;
    }
    @Override
    public void openApp(String sessionKey, String deviceToken) {
        Subscription  subscription= DataManager.getInstance().getAppApi().openApp(sessionKey,deviceToken)
                .compose(RxUtil.<JsonObject>rxSchedulerHelper())
                .map(new Func1<JsonObject, String>() {
                    @Override
                    public String call(JsonObject jsonObject) {
                        String unit="";
                        JsonObject  jsonObj= jsonObject.getAsJsonObject("data");
                        unit=jsonObj.get("unit").getAsString();
                        return unit;
                    }
                }).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
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
    public void getConfigInfo1() {
        Observable <RemoteConfigResonseModel> observable= configService.getConfigInfo();
        observable.subscribe(new Action1<RemoteConfigResonseModel>() {
            @Override
            public void call(RemoteConfigResonseModel remoteConfigResonseModel) {
                  WhiteLabelApplication.getAppConfiguration().initAppConfig(remoteConfigResonseModel.getData());
                timeOutJudgment();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
            }
        });
    }
    @Override
    public void getConfigInfo(String sessionKey,String deviceToken) {
        Observable openConfig= configService.getCurrencyUnit(sessionKey,deviceToken);
        Observable<RemoteConfigResonseModel> configObservable=configService.getConfigInfo();
        Observable.merge(openConfig,configObservable).compose(RxUtil.rxSchedulerHelper()).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                if(o instanceof RemoteConfigResonseModel){
                    RemoteConfigResonseModel  remoteConfigResonseModel= (RemoteConfigResonseModel) o;
                    WhiteLabelApplication.getAppConfiguration().initAppConfig(
                            remoteConfigResonseModel.getData());
                    timeOutJudgment();
                }else if(o instanceof GOCurrencyEntity){
                    GOCurrencyEntity currencyEntity= (GOCurrencyEntity) o;
                    WhiteLabelApplication.getAppConfiguration().getCurrency().setName(currencyEntity.getName());
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
                getMockApi().getConfigInfo(currentVersionNumber).
                compose(RxUtil.<RemoteConfigResonseModel>rxSchedulerHelper())
                .subscribe(new Action1<RemoteConfigResonseModel>() {
                    @Override
                    public void call(RemoteConfigResonseModel remoteConfigModel) {
                        if (remoteConfigModel.getCode() == 1) {
                            WhiteLabelApplication.getAppConfiguration().initAppConfig(
                                    remoteConfigModel.getData());
                            DataManager.getInstance().getPreferHelper().saveConfigInfo(remoteConfigModel);
                        }
                        timeOutJudgment();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }
}

package com.whitelabel.app.ui.start;

import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by ray on 2017/4/5.
 */

public class StartPresenterImpl extends RxPresenter<StartContract.View> implements StartContract.Presenter{
    private IBaseManager configService;
    private long mStartTimeLong;
    private IAccountManager iAccountManager;
    public  void setStartTime(){
        mStartTimeLong=System.currentTimeMillis();
    }
    public void timeOutJudgment(){
         long  offset=System.currentTimeMillis()-mStartTimeLong;
          mView.startIntentService();
         if(offset<2000){
                mView.postDelayed(offset);
         }else{
                mView.startGuidePage();
         }
    }



    @Inject
    public StartPresenterImpl( IBaseManager configService,IAccountManager iAccountManager){
        this.configService=configService;
        this.iAccountManager=iAccountManager;
    }
//    @Override
//    public void openApp(String sessionKey, String deviceToken) {
//        Subscription  subscription= DataManager.getInstance().getAppApi().openApp(sessionKey,deviceToken)
//                .compose(RxUtil.<JsonObject>rxSchedulerHelper())
//                .map(new Func1<JsonObject, String>() {
//                    @Override
//                    public String call(JsonObject jsonObject) {
//                        String unit="";
//                        JsonObject  jsonObj= jsonObject.getAsJsonObject("data");
//                        unit=jsonObj.get("unit").getAsString();
//                        return unit;
//                    }
//                }).subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        WhiteLabelApplication.getAppConfiguration().getCurrency().setName(s);
//                        DataManager.getInstance().getPreferHelper().saveCurrency(s);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        JLogUtils.i("ray","response:"+throwable.getMessage());
//                    }
//                });
//        addSubscrebe(subscription);
//    }
//    @Override
//    public void getConfigInfo1() {
//        Observable <RemoteConfigResonseModel> observable= configService.getConfigInfo();
//        observable.subscribe(new Action1<RemoteConfigResonseModel>() {
//            @Override
//            public void call(RemoteConfigResonseModel remoteConfigResonseModel) {
//                  WhiteLabelApplication.getAppConfiguration().initAppConfig(remoteConfigResonseModel.getData());
//                timeOutJudgment();
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//            }
//        });
//    }
//    @Override
//    public void getConfigInfo(final String sessionKey, final String deviceToken) {
//        Observable<RemoteConfigResonseModel> observable=  configService.getConfigInfo()
//                .compose(RxUtil.<RemoteConfigResonseModel>rxSchedulerHelper());
//        Observable<GOCurrencyEntity> observable1=  configService.getCurrencyUnit(sessionKey,deviceToken)
//                .compose(RxUtil.<GOCurrencyEntity>rxSchedulerHelper());
//        Subscription subscription=   Observable.zip(observable1, observable, new Func2< GOCurrencyEntity, RemoteConfigResonseModel,StartZipModel>() {
//            @Override
//            public StartZipModel call( GOCurrencyEntity goCurrencyEntity,RemoteConfigResonseModel remoteConfigResonseModel) {
//                StartZipModel startZipModel=new StartZipModel();
//                startZipModel.setRemoteConfigResonseModel(remoteConfigResonseModel);
//                startZipModel.setGoCurrencyEntity(goCurrencyEntity);
//                return startZipModel;
//            }
//        }).subscribe(new Subscriber<StartZipModel>() {
//            @Override
//            public void onCompleted() {
//            }
//            @Override
//            public void onError(Throwable e) {
//                JLogUtils.i("ray","error11:"+e.getMessage());
//            }
//            @Override
//            public void onNext(StartZipModel startZipModel) {
//                WhiteLabelApplication.getAppConfiguration().initAppConfig(
//                        startZipModel.getRemoteConfigResonseModel().getData());
//                WhiteLabelApplication.getAppConfiguration().getCurrency().setName(startZipModel.getGoCurrencyEntity().getName());
//                timeOutJudgment();
//            }
//        });
//
//      addSubscrebe(subscription);
//    }




    @Override
    public void getConfigInfo(final String sessionKey, final String deviceToken) {
        Subscription subscription= configService.getConfigInfo()
                .compose(RxUtil.<RemoteConfigResonseModel>rxSchedulerHelper())
                .subscribe(new Subscriber<RemoteConfigResonseModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
                        };
                        JLogUtils.i("ray","errorMessage1:"+e.getMessage());
                    }
                    @Override
                    public void onNext(RemoteConfigResonseModel remoteConfigResonseModel) {
                        WhiteLabelApplication.getAppConfiguration().initAppConfig(
                                remoteConfigResonseModel.getData());
                        requestCurrency(sessionKey,deviceToken);
                    }
                });
        addSubscrebe(subscription);
    }


    public void requestCurrency(String sessionkey,String deviceToken){
        Subscription subscription=  configService.getCurrencyUnit(sessionkey,deviceToken)
                .compose(RxUtil.<GOCurrencyEntity>rxSchedulerHelper())
                .subscribe(new Subscriber<GOCurrencyEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR) {
                            mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
                        }
                        JLogUtils.i("ray","errorMessage1:"+e.getMessage());
                    }

                    @Override
                    public void onNext(GOCurrencyEntity goCurrencyEntity) {
                        WhiteLabelApplication.getAppConfiguration().getCurrency().setName(goCurrencyEntity.getName());
                        timeOutJudgment();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void saveGuideFlag(Boolean isFirst) {
        iAccountManager.saveGuideFlag(isFirst);
    }

    @Override
    public boolean isGuide() {
        return iAccountManager.isGuide();
    }
}

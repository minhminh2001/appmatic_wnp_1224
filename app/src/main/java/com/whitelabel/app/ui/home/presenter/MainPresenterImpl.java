package com.whitelabel.app.ui.home.presenter;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.NotificationUnReadResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.MainContract;
import com.whitelabel.app.utils.RxUtil;

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
}

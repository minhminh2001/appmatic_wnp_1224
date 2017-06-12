package com.whitelabel.app.ui;


import com.whitelabel.app.data.retrofit.ApiFaildException;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;
import com.whitelabel.app.utils.JLogUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/23.
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {
    protected   T mView;
    protected CompositeSubscription mCompositeSubscription;
    @Override
    public void attachView(T mvpView) {
        mView=mvpView;
    }


    public String getErrorMsg(Throwable throwable){
        String errorMsg="";
        if (throwable instanceof ApiFaildException) {
            ApiFaildException apiException = (ApiFaildException) throwable;
            errorMsg=apiException.getMessage();
        } else {
            errorMsg=throwable.getMessage();
        }
        JLogUtils.i("BasePresenter","errorMsg："+errorMsg);
        return errorMsg;
    }

    protected   void  addSubscrebe(Subscription subscription){
        if(mCompositeSubscription==null){
            mCompositeSubscription=new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void unSubscrebe(){
        if(mCompositeSubscription!=null){
            mCompositeSubscription.unsubscribe();
        }
    }
    @Override
    public void detachView() {
        unSubscrebe();
        this.mView=null;
    }
}

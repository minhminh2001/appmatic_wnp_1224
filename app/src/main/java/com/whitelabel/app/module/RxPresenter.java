package com.whitelabel.app.module;
import android.view.View;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/23.
 */

public class RxPresenter<T extends  BaseView> implements BasePresenter<T> {

    protected   T mView;
    protected CompositeSubscription  mCompositeSubscription;
    @Override
    public void attachView(T mvpView) {
        mView=mvpView;
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
        this.mView=null;
        unSubscrebe();
    }
}

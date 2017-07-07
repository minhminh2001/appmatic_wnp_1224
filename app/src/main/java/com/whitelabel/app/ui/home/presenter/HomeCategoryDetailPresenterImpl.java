package com.whitelabel.app.ui.home.presenter;

import android.util.Log;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.HomeCategoryDetailContract;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ray on 2017/4/13.
 */

public class HomeCategoryDetailPresenterImpl extends RxPresenter<HomeCategoryDetailContract.View> implements HomeCategoryDetailContract.Presenter{

    private ICommodityManager  iCommodityManager;
    private IBaseManager  iBaseManager;
    private HomeCategoryDetailContract.View  mView;

    public HomeCategoryDetailPresenterImpl(ICommodityManager iCommodityManager,IBaseManager iBaseManager,HomeCategoryDetailContract.View  mView){
        this.iCommodityManager=iCommodityManager;
        this.mView=mView;
        this.iBaseManager=iBaseManager;
    }
    public void getCategoryDetail(final String categoryId){
       final String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
       Subscription subscription= iCommodityManager.getCategoryDetail(true,categoryId,sessionKey)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .doOnNext(new Action1<CategoryDetailModel>() {
                     @Override
                     public void call(CategoryDetailModel categoryDetailModel) {
//                         JLogUtils.i("ray","doOnNext_ThreadName:"+Thread.currentThread().getName());
                            if(categoryDetailModel!=null){
                             mView.loadData(categoryDetailModel);
                         }
                         mView.showSwipeLayout();
                     }
                 })
                .observeOn(Schedulers.io())
                 .flatMap(new Func1<CategoryDetailModel, Observable<CategoryDetailModel>>() {
                     @Override
                     public Observable<CategoryDetailModel> call(CategoryDetailModel categoryDetailModel) {
                         return  iCommodityManager.getCategoryDetail(false,categoryId,sessionKey);
                     }
                 }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CategoryDetailModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.closeRefreshLaout();
                        mView.closeSwipeLayout();
                        mView.showErrorMsg(e.getMessage());
                    }
                    @Override
                    public void onNext(CategoryDetailModel categoryDetailModel) {
                        mView.closeSwipeLayout();
                        mView.loadData(categoryDetailModel);
                    }
                });
        addSubscrebe(subscription);
    }

}

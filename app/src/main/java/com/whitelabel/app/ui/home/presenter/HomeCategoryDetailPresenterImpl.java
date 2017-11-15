package com.whitelabel.app.ui.home.presenter;

import android.util.Log;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.CategoryDetailNewModel;
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

    public HomeCategoryDetailPresenterImpl(ICommodityManager iCommodityManager,IBaseManager iBaseManager){
        this.iCommodityManager=iCommodityManager;
        this.iBaseManager=iBaseManager;
    }
    public void getCategoryDetail(final String categoryId){
       final String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
       Subscription subscription= iCommodityManager.getCategoryDetail(true,categoryId,sessionKey)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<CategoryDetailNewModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {

                     }
                    @Override
                    public void onNext(CategoryDetailNewModel categoryDetailModel) {
                        if(categoryDetailModel!=null){
                            mView.loadData(categoryDetailModel);
                        }

                        getOnlineCategoryDetail(categoryId);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getOnlineCategoryDetail(String categoryId) {
        mView.showSwipeLayout();
        final String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
       Subscription subscription= iCommodityManager.getCategoryDetail(false,categoryId,sessionKey)
                .compose(RxUtil.<CategoryDetailNewModel>rxSchedulerHelper())
                .subscribe(new Subscriber<CategoryDetailNewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.closeRefreshLaout();
                        mView.closeSwipeLayout();
                        mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                    }

                    @Override
                    public void onNext(CategoryDetailNewModel categoryDetailModel) {
                        mView.closeSwipeLayout();
                        mView.loadData(categoryDetailModel);
                    }
                });
        addSubscrebe(subscription);
    }
}

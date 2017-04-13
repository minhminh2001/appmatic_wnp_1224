package com.whitelabel.app.ui.home;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ray on 2017/4/13.
 */

public class HomeCategoryDetailPresenterImpl extends RxPresenter<HomeCategoryDetailContract.View> implements HomeCategoryDetailContract.Presenter{

    public void getCategoryDetail(String categoryId){
       Subscription subscription= DataManager.getInstance().getProductApi().getCategoryDetail(categoryId)
                .compose(RxUtil.<ResponseModel<CategoryDetailModel>>rxSchedulerHelper())
                .compose(RxUtil.<CategoryDetailModel>handleResult())
                .subscribe(new Action1<CategoryDetailModel>() {
                    @Override
                    public void call(CategoryDetailModel categoryDetailModel) {
                        mView.closeProgressDialog();
                        mView.closeRefreshLaout();
                         mView.loadData(categoryDetailModel);
                    }
                }, new ErrorHandlerAction() {
                    @Override
                    protected void requestError(ApiFaildException ex) {
                        mView.closeProgressDialog();
                        mView.closeRefreshLaout();
                        mView.showErrorMsg(ex.getErrorMsg());
                    }
                });
        addSubscrebe(subscription);
    }

}

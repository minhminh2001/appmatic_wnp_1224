package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BindProductPresenterImpl  extends RxPresenter<BindProductContract.View> implements BindProductContract.Presenter{

    @Override
    public void loadData(String productId) {
        Subscription  subscription=   DataManager.getInstance().getProductApi().getRelateProducts(productId).compose(RxUtil.<ResponseModel<BindProductResponseModel>>rxSchedulerHelper())
                .compose(RxUtil.<BindProductResponseModel>handleResult()).subscribe(new Action1<BindProductResponseModel>() {
            @Override
            public void call(BindProductResponseModel bindProductResponseModel) {
                mView.showData(bindProductResponseModel);
            }
        }, new ErrorHandlerAction() {
            @Override
            protected void requestError(ApiFaildException ex) {
                if(ex.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                    mView.showNetworkErrorView(ex.getErrorMsg());
                }
            }
        });
        addSubscrebe(subscription);
    }


    @Override
    public void addToCart(String relatedProductIds,String sessionKey) {
        Subscription  subscription= DataManager.getInstance().getProductApi().addBoughtTogether(relatedProductIds,sessionKey)
                .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
                    .subscribe(new Action1<ResponseModel>() {
                        @Override
                        public void call(ResponseModel responseModel) {

                        }
                    }, new ErrorHandlerAction() {
                        @Override
                        protected void requestError(ApiFaildException ex) {

                        }
                    });
        addSubscrebe(subscription);
    }
}

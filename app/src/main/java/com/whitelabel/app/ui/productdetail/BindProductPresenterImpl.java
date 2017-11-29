package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/5/31.
 */
public class BindProductPresenterImpl  extends RxPresenter<BindProductContract.View> implements BindProductContract.Presenter{
    private ICommodityManager iCommodityManager;
    @Inject
    public BindProductPresenterImpl(ICommodityManager iCommodityManager){
        this.iCommodityManager=iCommodityManager;
    }
    @Override
    public void loadData(String productId) {
        Subscription subscribe = iCommodityManager.getRelateProducts(productId).compose(RxUtil.<BindProductResponseModel>rxSchedulerHelper())
                .subscribe(new Subscriber<BindProductResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showNetworkErrorView(throwable.getMessage());
                        mView.showFaildErrorMsg(throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(BindProductResponseModel bindProductResponseModel) {
                        mView.showData(bindProductResponseModel);
                    }
                });
        addSubscrebe(subscribe);
    }
    public double  computeSumPrice(List<ProductPropertyModel> svrAppserviceProductDetailResultPropertyReturnEntities){
            double  prices=0;
            for(int i=0;i<svrAppserviceProductDetailResultPropertyReturnEntities.size();i++){
                ProductPropertyModel bean=
                        svrAppserviceProductDetailResultPropertyReturnEntities.get(i);
                 if(bean.isSelected()){
                     try {
                         prices += Double.parseDouble(bean.getFinalPrice());
                     }catch (Exception ex){
                         ex.getStackTrace();
                     }
                 }
            }
            return prices;
    }


    @Override
    public void addToCart(String relatedProductIds,String sessionKey) {
        Subscription subscribe = iCommodityManager.addBoughtTogether(relatedProductIds,sessionKey).compose(RxUtil.<ResponseModel>rxSchedulerHelper())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showNetworkErrorView(throwable.getMessage());
                        mView.showFaildErrorMsg(throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if(responseModel.getStatus()==1){
                            mView.addCartSuccess();
                        }else{
                            mView.showFaildErrorMsg(responseModel.getErrorMessage());
                        }
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public boolean checkProductIsSelected(List<ProductPropertyModel> svrAppserviceProductDetailResultPropertyReturnEntities) {
        if(svrAppserviceProductDetailResultPropertyReturnEntities!=null) {
            for (int i = 0; i < svrAppserviceProductDetailResultPropertyReturnEntities.size(); i++) {
                if (svrAppserviceProductDetailResultPropertyReturnEntities.get(i).isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }
}

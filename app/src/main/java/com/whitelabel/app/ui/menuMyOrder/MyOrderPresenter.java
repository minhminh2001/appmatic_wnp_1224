package com.whitelabel.app.ui.menuMyOrder;

import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by img on 2018/1/26.
 */

public class MyOrderPresenter extends RxPresenter<MyOrderContract.View> implements
    MyOrderContract.Presenter {

    private ICommodityManager iCommodityManager;

    private IBaseManager iBaseManager;

    @Inject
    public MyOrderPresenter(ICommodityManager iCommodityManager, IBaseManager iBaseManager) {
        this.iCommodityManager = iCommodityManager;
        this.iBaseManager = iBaseManager;
    }

    @Override
    public void getShoppingCount() {
        Subscription subscription = iCommodityManager.getLocalShoppingProductCount()
            .compose(RxUtil.<Integer>rxSchedulerHelper())
            .subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {
                    getShoppingCartCount(integer);
                }
            });
        addSubscrebe(subscription);
    }

    private void getShoppingCartCount(int count) {
        if (iBaseManager.isSign()) {
            count = (int) (iBaseManager.getUser().getCartItemCount() + count);
        }
        mView.loadShoppingCount(count);
    }

    @Override
    public void saveShoppingCartCount(int num) {
        if (iBaseManager.isSign()) {
            GOUserEntity userEntity = iBaseManager.getUser();
            userEntity.setCartItemCount(num);
            iBaseManager.saveUser(userEntity);
        }
    }

    @Override
    public void setToCheckout(ArrayList<OrderBody> orderBodies) {
        HashMap<String, String> params = new HashMap<>();
        int count = 0;
        String sessionKey = iBaseManager.getUser().getSessionKey();
        String orderId = "";
        params.put("session_key", sessionKey);

        if (orderBodies != null && !orderBodies.isEmpty()) {
            for (int i = 0; i < orderBodies.size(); i++) {
                OrderBody orderBody = orderBodies.get(i);
                orderId = orderBody.getOrderId();
                params.put("products[" + i + "][item_id]", orderBody.getItemId());
                params.put("products[" + i + "][qty]", orderBody.getOrderQuantity());
                count += Integer.valueOf(orderBody.getOrderQuantity());
            }
        }
        params.put("order_id", orderId);

        mView.showProgressDialog();
        final int finalCount = count;
        Subscription subscription = iCommodityManager.setToCheckout(params)
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(

                new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.closeProgressDialog();
                        ApiFaildException exception = ExceptionParse.parseException(throwable);
                        if (exception.getErrorType() == ExceptionParse.ERROR.HTTP_ERROR) {
                            mView.showNetErrorMessage();
                        } else if (exception.getErrorType() == ExceptionParse.ERROR.API_ERROR) {
                            mView.showFaildMessage(exception.getErrorMsg());
                        }
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        mView.closeProgressDialog();
                        if (responseModel.getStatus() == 1) {
                            mView.showReorderSuccessMessage(finalCount);
                        } else {
                            mView.refreshShoppingCartCount(finalCount);
                            mView.showReorderErrorMessage(responseModel.getErrorMessage());
                        }
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void setToCheckoutDetail(String orderId, List<MyAccountOrderInner> orderBodies) {
        HashMap<String, String> params = new HashMap<>();
        int count = 0;
        String sessionKey = iBaseManager.getUser().getSessionKey();
        params.put("session_key", sessionKey);

        if (orderBodies != null && !orderBodies.isEmpty()) {
            for (int i = 0; i < orderBodies.size(); i++) {
                MyAccountOrderInner myAccountOrderInner = orderBodies.get(i);
                params.put("products[" + i + "][item_id]", myAccountOrderInner.getItemId());
                params.put("products[" + i + "][qty]", myAccountOrderInner.getQty());
                count += Integer.valueOf(myAccountOrderInner.getQty());
            }
        }
        params.put("order_id", orderId);

        mView.showProgressDialog();
        final int finalCount = count;
        Subscription subscription = iCommodityManager.setToCheckout(params)
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(

                new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.closeProgressDialog();
                        ApiFaildException exception = ExceptionParse.parseException(throwable);
                        if (exception.getErrorType() == ExceptionParse.ERROR.HTTP_ERROR) {
                            mView.showNetErrorMessage();
                        } else if (exception.getErrorType() == ExceptionParse.ERROR.API_ERROR) {
                            mView.showFaildMessage(exception.getErrorMsg());
                        }
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        mView.closeProgressDialog();
                        if (responseModel.getStatus() == 1) {
                            mView.showReorderSuccessMessage(finalCount);
                        } else {
                            mView.showReorderErrorMessage(responseModel.getErrorMessage());
                        }
                    }
                });
        addSubscrebe(subscription);
    }


}

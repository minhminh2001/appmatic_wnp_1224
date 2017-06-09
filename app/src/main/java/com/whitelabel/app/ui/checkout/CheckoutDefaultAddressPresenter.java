package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.GlobalData;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutDefaultAddressPresenter extends RxPresenter<CheckoutDefaultAddressContract.View> implements CheckoutDefaultAddressContract.Presenter  {
    @Override
    public void getDefaultAddress() {
        String session="";
        if(WhiteLabelApplication.getAppConfiguration().isSignIn(WhiteLabelApplication.getInstance())){
            session=WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
        }
        Subscription subscription= DataManager.getInstance().getCheckoutApi().getDefaultAddress(session).
                compose(RxUtil.<ResponseModel<CheckoutDefaultAddressResponse>>rxSchedulerHelper())
                .compose(RxUtil.<CheckoutDefaultAddressResponse>handleResult())
                .subscribe(new Action1<CheckoutDefaultAddressResponse>() {
                    @Override
                    public void call(CheckoutDefaultAddressResponse checkoutDefaultAddressResponse) {
                        mView.dissmissProgressDialog();
                        mView.showData(checkoutDefaultAddressResponse);
                    }
                }, new ErrorHandlerAction() {
                    @Override
                    protected void requestError(ApiFaildException ex) {
                        mView.showErrorMsg(ex.getErrorMsg());
                    }
                });
        addSubscrebe(subscription);
    }




}

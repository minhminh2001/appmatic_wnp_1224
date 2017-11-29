package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICheckoutManager;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutDefaultAddressPresenter extends RxPresenter<CheckoutDefaultAddressContract.View> implements CheckoutDefaultAddressContract.Presenter  {
    private ICheckoutManager iCheckoutManager;
    private IBaseManager iBaseManager;
    public CheckoutDefaultAddressPresenter(ICheckoutManager iCheckoutManager, IBaseManager iBaseManager){
            this.iCheckoutManager=iCheckoutManager;
            this.iBaseManager=iBaseManager;
    }
    @Override
    public void getDefaultAddress() {
        String session=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
        Subscription subscription= iCheckoutManager.getCheckoutDefaultAddress(session)
                .compose(RxUtil.<CheckoutDefaultAddressResponse>rxSchedulerHelper())
                .subscribe(new Subscriber<CheckoutDefaultAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        JToolUtils.printObject(e);
                        mView.showErrorMsg(ExceptionParse.parseException(e).getErrorMsg());
                    }
                    @Override
                    public void onNext(CheckoutDefaultAddressResponse checkoutDefaultAddressResponse) {
                        mView.dissmissProgressDialog();
                        if (checkoutDefaultAddressResponse.getPrimaryShipping()!=null && checkoutDefaultAddressResponse.getPrimaryBilling()!=null){
                            if(!checkoutDefaultAddressResponse.getPrimaryShipping().getAddressId().equals(checkoutDefaultAddressResponse.getPrimaryBilling().getAddressId())){
                                mView.hideBillToDifferentLayout();
                            }
                        }
                        mView.showData(checkoutDefaultAddressResponse.getPrimaryShipping()
                                ,checkoutDefaultAddressResponse.getPrimaryBilling()
                                ,checkoutDefaultAddressResponse.getShippingMethod()
                                ,checkoutDefaultAddressResponse.getPickupAddress());
                    }
                });
        addSubscrebe(subscription);
    }




}

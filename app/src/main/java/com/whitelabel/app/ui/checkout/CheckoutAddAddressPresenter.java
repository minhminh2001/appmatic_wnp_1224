package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICheckoutManager;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/6/9.
 */

public class CheckoutAddAddressPresenter extends RxPresenter<CheckoutAddAddressContract.View> implements CheckoutAddAddressContract.Presenter  {
    private ICheckoutManager iCheckoutManager;
    private IBaseManager iBaseManager;
    public CheckoutAddAddressPresenter(ICheckoutManager iCheckoutManager, IBaseManager iBaseManager){
            this.iCheckoutManager=iCheckoutManager;
            this.iBaseManager=iBaseManager;
    }

    @Override
    public void getCountryAndRegions(){
        if (iCheckoutManager.getCountryAndRegions()==null){
            String session=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
            Subscription subscription= iCheckoutManager.getCountryAndRegions(session)
                    .compose(RxUtil.<SVRAppServiceCustomerCountry>rxSchedulerHelper())
                    .subscribe(new Subscriber<SVRAppServiceCustomerCountry>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            JToolUtils.printObject(throwable);
                            mView.showErrorMsg(ExceptionParse.parseException(throwable).getErrorMsg());
                        }

                        @Override
                        public void onNext(SVRAppServiceCustomerCountry svrAppServiceCustomerCountry) {
//                            mView.dissmissProgressDialog();
                            mView.showData(svrAppServiceCustomerCountry);
                        }
                    });
            addSubscrebe(subscription);
        }else {
            mView.showData(iCheckoutManager.getCountryAndRegions());
        }

    }




}

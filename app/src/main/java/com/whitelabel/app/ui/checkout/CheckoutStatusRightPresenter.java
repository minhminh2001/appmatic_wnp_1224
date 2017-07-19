package com.whitelabel.app.ui.checkout;

   import com.whitelabel.app.data.service.IBaseManager;
   import com.whitelabel.app.data.service.ICheckoutManager;
   import com.whitelabel.app.ui.RxPresenter;
   import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;
   import com.whitelabel.app.utils.RxUtil;

   import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/19.
 */
public class CheckoutStatusRightPresenter extends RxPresenter<CheckoutStatusRightContract.View> implements CheckoutStatusRightContract.Presenter{
    private ICheckoutManager iCheckoutManager;
    private IBaseManager iBaseManager;
    public CheckoutStatusRightPresenter(IBaseManager iBaseManager,ICheckoutManager iCheckoutManager){
        this.iCheckoutManager=iCheckoutManager;
        this.iBaseManager=iBaseManager;
    }
    public void requestOrderNumber(){
        String session=iBaseManager.getUser().getSessionKey();
        iCheckoutManager.requestOrderNumber(session)
                .compose(RxUtil.<RequestOrderNumberResponse>rxSchedulerHelper())
                .subscribe(new Subscriber<RequestOrderNumberResponse>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(RequestOrderNumberResponse requestOrderNumberResponse) {
                        mView.showOrderNumber(requestOrderNumberResponse.getOrderSn());
                    }
                });
    }
}

package com.whitelabel.app.ui.checkout;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICheckoutManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/18.
 */
public class CheckoutPresenterImpl extends RxPresenter<CheckoutContract.View> implements CheckoutContract.Presenter {
    private IBaseManager iBaseManager;
    private ICheckoutManager iCheckoutManager;
    public CheckoutPresenterImpl(IBaseManager iBaseManager,ICheckoutManager iCheckoutManager){
        this.iBaseManager=iBaseManager;
        this.iCheckoutManager=iCheckoutManager;
    }
    @Override
    public void payPalPlaceOrder(String orderComment) {
        mView.showCheckProgressDialog();
        mView.setButtonEnable(false);
        String sessionKey=iBaseManager.getUser().getSessionKey();
        Subscription subscription= iCheckoutManager.paypalPlaceOrder(sessionKey,orderComment,getPaypalOrderVersion())
                .compose(RxUtil.<PaypalPlaceOrderReponse>rxSchedulerHelper())
                .subscribe(new Subscriber<PaypalPlaceOrderReponse>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        ApiFaildException exception=ExceptionParse.parseException(e);
                        if(exception.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                                mView.showNetErrorMessage();
                        }else if(exception.getErrorType()==ExceptionParse.ERROR.API_ERROR){
                                mView.showFaildMessage(exception.getErrorMsg());
                        }
                        mView.setButtonEnable(true);
                        mView.closeCheckoutPaymentDialog();
                    }
                    @Override
                    public void onNext(PaypalPlaceOrderReponse responseModel) {
                        mView.startPayPalPaymentActivity(responseModel.getUrl(),responseModel.getOrderNumber());
                        mView.setButtonEnable(true);
                        mView.closeCheckoutPaymentDialog();
                    }
                });
        addSubscrebe(subscription);
    }



    @Override
    public void createCustomerAddress(String firstName, String lastName, String countryId, String telePhone, String street0, String street1, String fax, String postCode, String city, String region, String regionId) {
        mView.setButtonEnable(false);
        mView.showProgressDialog();
        final String sessionKey=iBaseManager.getUser().getSessionKey();
        Subscription subscription= iCheckoutManager.createCustomerAddress(sessionKey,firstName,lastName,countryId,telePhone,street0,street1,fax,postCode,city,region,"1","1",regionId).compose(RxUtil.<SVRAddAddress>rxSchedulerHelper()).subscribe(new Subscriber<SVRAddAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                ApiFaildException exception=ExceptionParse.parseException(throwable);
                if(exception.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                    mView.showNetErrorMessage();
                }else if(exception.getErrorType()==ExceptionParse.ERROR.API_ERROR){
                    mView.showFaildMessage(exception.getErrorMsg());
                }
                mView.setButtonEnable(true);
                mView.closeProgressDialog();
            }

            @Override
            public void onNext(SVRAddAddress svrAddAddress) {
                if (svrAddAddress.getStatus()==1){
                    mView.showAddressSuccess(true);
                }else {
                    mView.showAddressSuccess(false);
                }
                mView.setButtonEnable(true);
                mView.closeProgressDialog();
            }
        });
        addSubscrebe(subscription);
    }


    /**
     * format like iPhone11.1_1.0.0(13)
     * brand +system verson+appVersionName +appVersionCode
     * @return
     */
    public String getPaypalOrderVersion(){
        StringBuilder builder=new StringBuilder();
        builder.append(JToolUtils.getDeviceBrand());
        builder.append(JToolUtils.getSystemVersion());
        builder.append("_");
        builder.append(JToolUtils.getAppVersionName());
        builder.append("(");
        builder.append(JToolUtils.getAppVersionCode());
        builder.append(")");
        return builder.toString();
    }


}

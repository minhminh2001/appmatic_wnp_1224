package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ray on 2018/3/13.
 */

public class CheckoutRegisterPresenter extends RxPresenter<CheckoutRegisterContract.View>
    implements CheckoutRegisterContract.Presenter {

    private IAccountManager iAccountManager;

    private IShoppingCartManager iShoppingCartManager;

    @Inject
    public CheckoutRegisterPresenter(IAccountManager iAccountManager, IShoppingCartManager  iShoppingCartManager){
        this.iAccountManager=iAccountManager;
        this.iShoppingCartManager=iShoppingCartManager;
    }

    @Override
    public void registerEmail(final RegisterRequest registerRequest) {
        iAccountManager.register(registerRequest)
                .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
                .subscribe(new Subscriber<ResponseModel>(){
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        loginEmail(registerRequest.getEmail(),registerRequest.getPassword(),registerRequest.getDeviceToken());
                    }
                });
    }

    private void loginEmail(String email,String password,String deviceToken){

        iAccountManager.loginEmail(email,password,deviceToken)
                .compose(RxUtil.<SVRAppServiceCustomerLoginReturnEntity>rxSchedulerHelper())
                .subscribe(new Subscriber<SVRAppServiceCustomerLoginReturnEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(SVRAppServiceCustomerLoginReturnEntity svrAppServiceCustomerLoginReturnEntity) {

                    }
                });

    }
}

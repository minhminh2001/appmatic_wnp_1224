package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.data.model.MergeBatchResponse;
import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ray on 2018/3/13.
 */

public class CheckoutRegisterPresenter extends RxPresenter<CheckoutRegisterContract.View>
    implements CheckoutRegisterContract.Presenter {

    private IAccountManager iAccountManager;

    private IShoppingCartManager iShoppingCartManager;

    private IBaseManager iBaseManager;

    @Inject
    public CheckoutRegisterPresenter(IAccountManager iAccountManager,
        IShoppingCartManager iShoppingCartManager, IBaseManager iBaseManager) {
        this.iAccountManager = iAccountManager;
        this.iShoppingCartManager = iShoppingCartManager;
        this.iBaseManager = iBaseManager;
    }

    @Override
    public void registerEmail(final RegisterRequest registerRequest) {
        iAccountManager.register(registerRequest)
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
            .subscribe(new Subscriber<ResponseModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;
                        mView.showErrorMessage(apiException.getErrorMsg());
                    }
                }

                @Override
                public void onNext(ResponseModel responseModel) {
                    loginEmail(registerRequest.getEmail(), registerRequest.getPassword(),
                        registerRequest.getDeviceToken());
                }
            });
    }

    private void loginEmail(final String email, String password, String deviceToken) {
        iAccountManager.loginEmail(email, password, deviceToken)
            .compose(RxUtil.<SVRAppServiceCustomerLoginReturnEntity>rxSchedulerHelper())
            .subscribe(new Subscriber<SVRAppServiceCustomerLoginReturnEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(
                    SVRAppServiceCustomerLoginReturnEntity svrAppServiceCustomerLoginReturnEntity) {
                    svrAppServiceCustomerLoginReturnEntity.setEmail(email);
                    svrAppServiceCustomerLoginReturnEntity.setEmailLogin(true);
                    mView.loginSuccess(svrAppServiceCustomerLoginReturnEntity);
                    getShoppingListFromLocal();
                }
            });
    }

    public void getShoppingListFromLocal() {
        iShoppingCartManager.getProductListFromLocal().
            compose(RxUtil.<List<ShoppingItemLocalModel>>rxSchedulerHelper()).subscribe(
            new Subscriber<List<ShoppingItemLocalModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(List<ShoppingItemLocalModel> shoppingItemLocalModels) {
                    if (shoppingItemLocalModels != null && shoppingItemLocalModels.size() > 0) {
                        addBatchShopping(shoppingItemLocalModels);
                    }
                }
            });
    }

    private void addBatchShopping(List<ShoppingItemLocalModel> shoppingItemLocalModels) {
        String session = iBaseManager.getUser() == null ? "" : iBaseManager.getUser()
            .getSessionKey();
        iShoppingCartManager.addBatchShopping(shoppingItemLocalModels, session)
            .compose(RxUtil.<MergeBatchResponse>rxSchedulerHelper()).subscribe(
            new Subscriber<MergeBatchResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    JLogUtils.i("ray", "errorMessage:" + throwable.getMessage());

                }

                @Override
                public void onNext(MergeBatchResponse shoppingItemLocalModels) {
                    mView.addBatchShoppingSuccess();
                    clearLocalShoppingItem();
                }
            });
    }

    private void clearLocalShoppingItem() {
        iShoppingCartManager.clearShoppingItem()
            .compose(RxUtil.<Boolean>rxSchedulerHelper()).subscribe(
            new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onNext(Boolean aBoolean) {

                }
            }
        );
    }
}

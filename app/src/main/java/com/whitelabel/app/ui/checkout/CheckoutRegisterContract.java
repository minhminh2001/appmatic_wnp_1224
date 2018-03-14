package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Ray on 2018/3/13.
 */

public interface CheckoutRegisterContract {

    interface View extends BaseView {

        void loginSuccess(
            SVRAppServiceCustomerLoginReturnEntity svrAppServiceCustomerLoginReturnEntity);

        void addBatchShoppingSuccess();

        void showErrorMessage(String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {

        void registerEmail(RegisterRequest registerRequest);

    }

}

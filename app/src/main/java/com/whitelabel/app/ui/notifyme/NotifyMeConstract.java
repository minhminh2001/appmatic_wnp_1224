package com.whitelabel.app.ui.notifyme;

import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;
import com.whitelabel.app.ui.checkout.CheckoutContract;
import com.whitelabel.app.ui.login.LoginFragmentContract;

/**
 * Created by Aaron on 2018/3/27.
 */

public interface NotifyMeConstract {

    public interface View extends BaseView{

        public void showErrorMsg(String errorMsg);
        public void onSuccess();
    }

    public interface Presenter extends BasePresenter<NotifyMeConstract.View>{
        public void registerNotifyForProduct(String productId, String storeId, String name, String emal, String sessionKey);
    }
}

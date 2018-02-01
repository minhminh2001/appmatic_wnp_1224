package com.whitelabel.app.ui.login;

import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.Map;

/**
 * Created by img on 2018/1/29.
 */

public class SettingContract {
    public interface View extends BaseView {
        public void showErrorMsg(String errorMsg);
        public void setSubscriberSuccess(boolean isSuccess);
        public void getIsSubscriber(boolean isSuccess);

    }

    public interface Presenter extends BasePresenter<View> {
        public void setUserAgreement(String isAgreement);
        public void getUserAgreement();
    }
}

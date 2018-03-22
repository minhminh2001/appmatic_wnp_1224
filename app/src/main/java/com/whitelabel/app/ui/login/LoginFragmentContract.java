package com.whitelabel.app.ui.login;

import com.whitelabel.app.model.ResponseConnection;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface LoginFragmentContract {
    public interface View extends BaseView{
        void showNetErrorMessage();
        void jumpBoundEmailFragment(String givenName,String formatted,String familyName,String displayName,String identityToken,String userToken,String email,String provider);
        void showErrorMessage(String errorMessage);
        void showConfirmEmail();
        void loginSuccess(SVRAppserviceCustomerFbLoginReturnEntity fbLoginReturnEntity);
        void showUpdateDialog();
        //LoginRegisterEmailRegisterFragment：Resiter,LoginRegisterEmailLoginFragment：Login
        void emailLoginOrRegister();
    }
    public interface  Presenter extends BasePresenter<View>{
        public void requestOnallUser(String platform,String accessToken,String secret );
        void  loginFromServer(final String givenName,final String formatted,
                              final String familyName,final String displayName,
                              final String identityToken,final String userToken,
                              final String email,final String provider,boolean showProgress, String boundEmail );

        void versionCheck();
    }
}

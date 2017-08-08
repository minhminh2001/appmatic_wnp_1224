package com.whitelabel.app.ui.login;

import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface LoginFragmentContract {
    public interface View extends BaseView{

    }
    public interface  Presenter extends BasePresenter<View>{
        public void requestOnallUser(String platform,String accessToken,String secret );
    }
}

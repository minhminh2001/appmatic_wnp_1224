package com.whitelabel.app.ui.home;

import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/8/7.
 */

public interface MainContract {
    interface  View extends BaseView{
        public void setNotificationUnReadCount(int unReadCount);

    }
    interface  Presenter extends BasePresenter<View>{
        public void getNotificationUnReadCount();

    }
}

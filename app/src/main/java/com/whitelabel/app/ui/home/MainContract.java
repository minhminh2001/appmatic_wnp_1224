package com.whitelabel.app.ui.home;

import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/7.
 */

public interface MainContract {
    interface  View extends BaseView{
        public void setNotificationUnReadCount(int unReadCount);

    }
    interface  Presenter extends BasePresenter<View>{
        public void getNotificationUnReadCount();
        public List<String> getServiceSupportedLanguageFromLocal();
        public Map<String, String>  getServiceSupportedStoreMapFromLocal();

    }
}

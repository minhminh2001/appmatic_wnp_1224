package com.whitelabel.app.ui.start;

import android.view.View;

import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;

/**
 * Created by ray on 2017/4/5.
 */

public class StartContract {

      interface  View extends BaseView{
            void delayStart();
            void showErrorMessage(String errorMsg);
      }
     interface  Presenter extends BasePresenter<View>{
         void getConfigInfo();
         void openApp(String sessionKey,String deviceToken);

         void  getConfigInfo(String sessionKey,String deviceToken);
     }
}

package com.whitelabel.app.ui.home;

import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/7/5.
 */
public class HomeContract {
    public interface  View extends BaseView {
         public void loadData(SVRAppserviceCatalogSearchReturnEntity data);
         public void showProgressDialog();
         public void  dissmissProgressDialog();
         public void  showOnlineErrorLayout();
         public void hideOnlineErrorLayout();
         public void setShoppingCartCount(int count);
         public void showRootView();
    }
    public  interface  Presenter extends BasePresenter<View>{
        void  getBaseCategory();
        void   getShoppingCount();
        String formatShoppingCount(int count);
        void getLocalBaseCategory();
    }
}

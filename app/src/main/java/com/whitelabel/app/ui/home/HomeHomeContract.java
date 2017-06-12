package com.whitelabel.app.ui.home;

import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by ray on 2017/4/7.
 */

public class HomeHomeContract {
     public interface  View extends BaseView{
         public void showErrorMsg(String errormsg);
         public void loadRecyclerViewData(SVRAppserviceCatalogSearchReturnEntity  svrAppserviceCatalogSearchReturnEntity);
     }

    public interface Presenter extends BasePresenter<View>{
        public void getSearchCategory();
    }
}

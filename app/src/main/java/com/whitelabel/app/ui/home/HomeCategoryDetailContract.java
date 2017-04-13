package com.whitelabel.app.ui.home;

import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;

/**
 * Created by ray on 2017/4/13.
 */

public class HomeCategoryDetailContract {
     public   interface View extends BaseView{
        public void showErrorMsg(String errorMsg);
         public void loadData(CategoryDetailModel categoryDetailModel);
         void closeRefreshLaout();
     }
    public  static interface Presenter extends BasePresenter<View>{
       public void  getCategoryDetail(String categoryId);

    }
}

package com.whitelabel.app.ui.home;

import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.List;

/**
 * Created by img on 2017/11/20.
 */

public class ShopBrandContract {
    public interface View extends BaseView{
        public void loadData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean);
        public void loadTitleData(List<ShopBrandResponse.BrandsBean.ItemsBean> itemsBean);
        public void showErrorMsg(String errorMsg);
        public void showSwipeLayout();
        public void closeSwipeLayout();

    }

    public  static interface Presenter extends BasePresenter<View> {
        public void  getOnlineCategoryDetail(boolean isCache, String menuId);

    }
}

package com.whitelabel.app.ui.brandstore;

import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;

/**
 * Created by Administrator on 2017/2/28.
 */

public interface BrandStoreContract {
    interface View extends BaseView{

        public  void showContentView(BrandStoreModel brandStoreModel, int currPage);

        public void showNoDataView();
        public  void showNetworkErrorView(String errorMsg);

    }

    interface  Presenter extends BasePresenter<View>{
       void  getBrandProductList(String brandId, final int offset, int limit, String price, String order, String dir, String modelType);
        void addWistList(final SVRAppserviceProductSearchResultsItemReturnEntity bean);
        void  deleteWishListByItemId(final SVRAppserviceProductSearchResultsItemReturnEntity bean);
         ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductSearchResultsItemReturnEntity e);
    }

}

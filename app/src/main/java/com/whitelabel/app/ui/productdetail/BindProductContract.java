package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public interface BindProductContract {
    interface View extends BaseView{
        public void showData(BindProductResponseModel products);
        void showNetworkErrorView(String errorMsg);
        void showFaildErrorMsg(String errorMsg);
        void  addCartSuccess();
    }
    interface  Presenter extends BasePresenter<View>{
        public void loadData(String productId);
        public void addToCart(String relatedProductIds,String sessionKey);
        public double computeSumPrice(List<ProductPropertyModel> svrAppserviceProductDetailResultPropertyReturnEntities);
        public boolean  checkProductIsSelected(List<ProductPropertyModel> svrAppserviceProductDetailResultPropertyReturnEntities);
    }
}

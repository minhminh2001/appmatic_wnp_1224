package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface ProductDetailContract {
    public interface View extends BaseView{
        public void showNornalProgressDialog();
        public void showBottomProgressDialog();
        public void dissmissProgressDialog();
        public void showErrorMessage(String errorMsg);
        public void showContentLayout();
        public void  loadStaticData(ProductDetailModel productDetailModel);
        public void loadSimpleProductView(ProductDetailModel productDetailModel, ArrayList<String> productImageArrayList);
        public void loadConfigurableProductView(ProductDetailModel productDetailModel, ArrayList<String> productImageArrayList);
        public void loadGroupProductView(ProductDetailModel productDetailModel, ArrayList<String> productImageArrayList);
        public void  showBindProductView(List<SVRAppserviceProductDetailResultPropertyReturnEntity> bindProducts);
        public void hideBindProductView();
        public void clearUserSelectedProduct();
        public void hideVisibleProduct();
        public void setLikeView(boolean isLike);
        public void showActionView();
        public void hideAvailabilityView();
        public void showAvailabilityView();


    }
    public interface Presenter extends BasePresenter<View>{
        public  void loadProductDetailData(String productId);
        public void setDialogType(String type);
        public ProductDetailModel getProductData();
    }

}

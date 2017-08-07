package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedResultsItemReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        public void  showBindProductView(List<ProductPropertyModel> bindProducts);
        public void hideBindProductView();
        public void clearUserSelectedProduct();
        public void hideVisibleProduct();
        public void setLikeView(boolean isLike);
        public void hideAvailabilityView();
        public void showAvailabilityView();
        void setShoppingCartCount(int count);
        public void setWishIconColorToBlank();
        public void setWishIconColorToThemeColor();
        public void startLoginActivity(boolean expire);
        public void startShoppingCartActivity();
        public void showNoInventoryToast();
        public void setProductCountView(long count);
        public Map<String,String> getGroupProductParams();
        public String getConfiguationProductSimpleId();
        public void showProductRecommendLine();
        public void updateRecommendData(ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> results);
    }
    public interface Presenter extends BasePresenter<View>{
        void loadProductDetailData(String productId);
        void setDialogType(String type);
        ProductDetailModel getProductData();
        void getShoppingCount();
        void wishListBtnClick();
        void setOutOfStock(boolean isOutOfStock);
        long getUserSelectedProductQty();
        void setUserSelectedProductQty(int userSelectedProductQty);
        long getCurrUserSelectedProductMaxStockQty();
        void setCurrUserSelectedProductMaxStockQty(long currUserSelectedProductMaxStockQty);
        void  productCountMinusClick();
        void productCountPlusClick();
        void addToCartClick();
        void delayAddToCart();
    }

}

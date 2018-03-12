package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedResultsItemReturnEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/10.
 */

public interface ProductDetailContract {

    interface View extends BaseView {

        void showNornalProgressDialog();

        void showBottomProgressDialog();

        void dissmissProgressDialog();

        void showErrorMessage(String errorMsg);

        void showContentLayout();

        void loadStaticData(ProductDetailModel productDetailModel);

        void loadSimpleProductView(ProductDetailModel productDetailModel,
            ArrayList<String> productImageArrayList);

        void loadConfigurableProductView(ProductDetailModel productDetailModel,
            ArrayList<String> productImageArrayList);

        void loadGroupProductView(ProductDetailModel productDetailModel,
            ArrayList<String> productImageArrayList);

        void showBindProductView(List<ProductPropertyModel> bindProducts);

        void hideBindProductView();

        void clearUserSelectedProduct();

        void hideVisibleProduct();

        void setLikeView(boolean isLike);

        void hideAvailabilityView();

        void showAvailabilityView();

        void setShoppingCartCount(int count);

        void setWishIconColorToBlank();

        void setWishIconColorToThemeColor();

        void startLoginActivity(boolean expire);

        void startAddToCart();

        void showNoInventoryToast();

        void setProductCountView(long count);

        Map<String, String> getGroupProductParams();

        String getConfiguationProductSimpleId();

        void showProductRecommendLine();

        void updateRecommendData(
            ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> results);
    }

    public interface Presenter extends BasePresenter<View> {

        void loadProductDetailData(String productId);

        void setDialogType(String type);

        ProductDetailModel getProductData();

        void getShoppingCount();

        void saveShoppingCartCount(int num);

        void wishListBtnClick();

        void setOutOfStock(boolean isOutOfStock);

        long getUserSelectedProductQty();

        void setUserSelectedProductQty(int userSelectedProductQty);

        long getCurrUserSelectedProductMaxStockQty();

        void setCurrUserSelectedProductMaxStockQty(long currUserSelectedProductMaxStockQty);

        void productCountMinusClick();

        void productCountPlusClick();

        void addToCartClick();

        void delayAddToCart();

        void addCartToTopAnim(final RelativeLayout parentView, final ImageView sourceIv,
            ImageView targetIv);
    }

}

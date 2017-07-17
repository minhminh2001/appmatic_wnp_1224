package com.whitelabel.app.ui.productdetail;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/10.
 */
public class ProductDetailPresenter  extends RxPresenter<ProductDetailContract.View>implements ProductDetailContract.Presenter{
    private ICommodityManager iCommodityManager;
    private IShoppingCartManager iShoppingCartManager;
    private IBaseManager  iBaseManager;
    private IAccountManager iAccountManager;
    private String mDialogType;
    public static final String TYPE_CONFIGURABLE = "configurable";
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_GROUP="grouped";
    private ProductDetailModel  mProduct;
    private boolean isOutOfStock = false;
    private long userSelectedProductQty;
    public static final String DIALOG_TYPE_BOTTOM="from_product_list";
    private long currUserSelectedProductMaxStockQty;
    private boolean delayAddToCart;
    public ProductDetailPresenter(ProductDetailContract.View  view,IAccountManager iAccountManager, ICommodityManager iCommodityManager, IBaseManager iBaseManager,IShoppingCartManager iShoppingCartManager){
        this.mView=view;
        this.iCommodityManager=iCommodityManager;
        this.iBaseManager=iBaseManager;
        this.iAccountManager=iAccountManager;
        this.iShoppingCartManager=iShoppingCartManager;
    }


    public long getCurrUserSelectedProductMaxStockQty() {
        return currUserSelectedProductMaxStockQty;
    }

    public void setCurrUserSelectedProductMaxStockQty(long currUserSelectedProductMaxStockQty) {
        this.currUserSelectedProductMaxStockQty = currUserSelectedProductMaxStockQty;
    }

    public long getUserSelectedProductQty(){
        return userSelectedProductQty;
    }
    public void setUserSelectedProductQty(int userSelectedProductQty){
        this.userSelectedProductQty=userSelectedProductQty;
    }

    public void setOutOfStock(boolean isOutOfStock){
        this.isOutOfStock=isOutOfStock;
    }

    public ProductDetailModel getProductData(){
        return mProduct;
    }
    @Override
    public void setDialogType(String type) {
        this.mDialogType=type;
    }

    @Override
    public void loadProductDetailData(String productId) {
       String session=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
        if(DIALOG_TYPE_BOTTOM.equals(mDialogType)){
            mView.showBottomProgressDialog();
        }else{
            mView.showNornalProgressDialog();
        }
        Subscription subscription= iCommodityManager.getProductDetail(session,productId)
       .compose(RxUtil.<ProductDetailModel>rxSchedulerHelper())
       .subscribe(new Subscriber<ProductDetailModel>() {
           @Override
           public void onCompleted() {
           }
           @Override
           public void onError(Throwable e) {
               mView.dissmissProgressDialog();
               mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
           }
           @Override
           public void onNext(ProductDetailModel productDetailModel) {
               mProduct=productDetailModel;
               mView.dissmissProgressDialog();
               mView.showContentLayout();
               mView.loadStaticData(productDetailModel);
               mView.clearUserSelectedProduct();
               loadPropertyView(productDetailModel);
               loadBindProductView(productDetailModel);
               showVisibleProduct(productDetailModel);
           }
       });
        addSubscrebe(subscription);
    }

    private void loadBindProductView(ProductDetailModel productDetailModel) {
        if (productDetailModel.getRelatedProducts() != null && productDetailModel.getRelatedProducts().size() > 0) {
            productDetailModel.getRelatedProducts().add(0, productDetailModel.getProperty().get(0));
            mView.showBindProductView(productDetailModel.getRelatedProducts());
        } else {
            mView.hideBindProductView();
        }
    }

    public void showVisibleProduct(ProductDetailModel  productDetailModel){
        try {
            if ("0".equals(productDetailModel.getVisibility())) {
                mView.hideVisibleProduct();
                return;
            }
            if (productDetailModel.getIsLike() == 1) {
                mView.setLikeView(true);
            } else {
                mView.setLikeView(false);
            }
            if (JDataUtils.isEmpty(productDetailModel.getAvailability()) || "1".equals(productDetailModel.getAvailability())) {
                mView.hideAvailabilityView();
            } else {
                mView.showAvailabilityView();
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }

    }
    public void  loadPropertyView(ProductDetailModel productDetailModel){
        try {
            ArrayList<String> productImages = new ArrayList<>();
            if (productDetailModel.getImages() != null && productDetailModel.getImages().size() > 0) {
                productImages.addAll(productDetailModel.getImages());
            }
            switch (productDetailModel.getType()) {
                case TYPE_SIMPLE:
                    mView.loadSimpleProductView(productDetailModel, productImages);
                    break;
                case TYPE_CONFIGURABLE:
                    mView.loadConfigurableProductView(productDetailModel, productImages);
                    break;
                case TYPE_GROUP:
                    mView.loadGroupProductView(productDetailModel, productImages);
                    break;
            }
        }catch (Exception ex){
            JLogUtils.i("ray","exception:"+ex.getStackTrace());
        }
    }
    public void getShoppingCount(){
        Subscription  subscription=  iCommodityManager.getLocalShoppingProductCount()
                .compose(RxUtil.<Integer>rxSchedulerHelper())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        setShoppingCartCount(integer);
                    }
                });
        addSubscrebe(subscription);
    }
    public void setShoppingCartCount(int count){
        if(iBaseManager.isSign()) {
            count= (int) (iBaseManager.getUser().getCartItemCount()+count);
        }
        if(count!=0) {
            mView.setShoppingCartCount(count);
        }
    }
    @Override
    public void wishListBtnClick() {
         if(iBaseManager.isSign()){
            if (mProduct.getIsLike() == 1&&!JDataUtils.isEmpty(mProduct.getItemId())) {
                mView.setWishIconColorToBlank();
                delteFromWishlistRequest(mProduct.getItemId());
            } else if(mProduct.getIsLike()==0){
                mView.setWishIconColorToThemeColor();
                addToWishlistRequest(mProduct.getId());
            }
         }else{
             mView.startLoginActivity();
         }
    }

    public void setmProduct(ProductDetailModel mProduct) {
        this.mProduct = mProduct;
    }

    public void   addToWishlistRequest(String productId){
        String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
       Subscription subscription= iAccountManager.addWishlist(sessionKey,productId)
        .compose(RxUtil.<AddToWishlistEntity>rxSchedulerHelper())
        .subscribe(new Subscriber<AddToWishlistEntity>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(AddToWishlistEntity responseModel) {
                if(responseModel!=null) {
                    mProduct.setItemId(responseModel.getItemId());
                    GOUserEntity goUserEntity= iBaseManager.getUser();
                    goUserEntity.setWishListItemCount(responseModel.getWishListItemCount());
                    iBaseManager.saveUser(goUserEntity);
                }
            }
        });
       addSubscrebe(subscription);
    }
    public void delteFromWishlistRequest(String itemId){
        String sessionKey=iBaseManager.isSign()?iBaseManager.getUser().getSessionKey():"";
         iAccountManager.deleteWishlist(sessionKey,itemId)
                 .compose(RxUtil.<WishDelEntityResult>rxSchedulerHelper())
                 .subscribe(new Subscriber<WishDelEntityResult>() {
                     @Override
                     public void onCompleted() {
                     }
                     @Override
                     public void onError(Throwable e) {
                     }
                     @Override
                     public void onNext(WishDelEntityResult responseModel) {
                         if(responseModel!=null) {
                             GOUserEntity goUserEntity= iBaseManager.getUser();
                             goUserEntity.setWishListItemCount(responseModel.getWishListItemCount());
                             iBaseManager.saveUser(goUserEntity);
                         }
                     }
                 });
    }
    public void  productCountMinusClick(){
        if(currUserSelectedProductMaxStockQty<=0){
            mView.showNoInventoryToast();
            return ;
        }
        --userSelectedProductQty;
        if (userSelectedProductQty <= 1) {
            userSelectedProductQty = 1;
        } else if (userSelectedProductQty > currUserSelectedProductMaxStockQty) {
            userSelectedProductQty = currUserSelectedProductMaxStockQty;
            mView.showNoInventoryToast();
        }
        mView.setProductCountView(userSelectedProductQty);
    }
    public void productCountPlusClick(){
        if (currUserSelectedProductMaxStockQty <= 0) {
            mView.showNoInventoryToast();
            return;
        }
        ++userSelectedProductQty;
        if (userSelectedProductQty <= 1) {
            userSelectedProductQty = 1;
        } else if (userSelectedProductQty > currUserSelectedProductMaxStockQty) {
            userSelectedProductQty = currUserSelectedProductMaxStockQty;
            mView.showNoInventoryToast();
        }
        mView.setProductCountView(userSelectedProductQty);
    }
    @Override
    public void addToCartClick() {
        if (!isOutOfStock&&userSelectedProductQty > 0) {
            if(iBaseManager.isSign()){
                Map<String, String> params = getAddToCartParams();
                addToShoppingCart(params);
            }else{
                delayAddToCart=true;
                mView.startLoginActivity();
            }
        } else if(isOutOfStock) {
            wishListBtnClick();
        }
    }
    public void delayAddToCart(){
        if(delayAddToCart){
            Map<String, String> params = getAddToCartParams();
            addToShoppingCart(params);
        }else{
            loadProductDetailData(mProduct.getId());
        }
    }
    @Nullable
    public Map<String, String> getAddToCartParams() {
        Map<String,String> params=null;
        switch (mProduct.getType()){
            case TYPE_GROUP:
                params= mView.getGroupProductParams();
                break;
            case TYPE_CONFIGURABLE:
                params=new HashMap<>();
                params.put(mView.getConfiguationProductSimpleId(),userSelectedProductQty+"");
                break;
            case TYPE_SIMPLE:
                params=new HashMap<>();
                params.put(mProduct.getId(),userSelectedProductQty+"");
                break;
        }
        return params;
    }
    public void addToShoppingCart(Map<String,String> params){
        mView.showNornalProgressDialog();
        String sessionKey=iBaseManager.getUser().getSessionKey();
        iShoppingCartManager.addProductToShoppingCart(sessionKey,mProduct.getId(),params)
                .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.dissmissProgressDialog();
                        if(JToolUtils.expireHandler(ExceptionParse.parseException(e).getErrorMsg())){
                            mView.startLoginActivity();
                        }
                        mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
                    }
                    @Override
                    public void onNext(ResponseModel responseModel) {
                        mView.dissmissProgressDialog();
                        mView.startShoppingCartActivity();
                    }
                });
    }
}

package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.data.service.IGoogleAnalyticsManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/7/10.
 */
public class ProductDetailPresenter extends RxPresenter<ProductDetailContract.View> implements
    ProductDetailContract.Presenter {

    public static final String TYPE_CONFIGURABLE = "configurable";

    public static final String TYPE_SIMPLE = "simple";

    public static final String TYPE_GROUP = "grouped";

    public static final String DIALOG_TYPE_BOTTOM = "from_product_list";

    private ICommodityManager iCommodityManager;

    private IShoppingCartManager iShoppingCartManager;

    private IBaseManager iBaseManager;

    private IAccountManager iAccountManager;

    private IGoogleAnalyticsManager iGoogleAnalyticsManager;

    private String mDialogType;

    private ProductDetailModel mProduct;

    private boolean isOutOfStock = false;

    private long userSelectedProductQty;

    private long currUserSelectedProductMaxStockQty;

    private boolean delayAddToCart;

    public ProductDetailPresenter(IAccountManager iAccountManager,
        ICommodityManager iCommodityManager, IBaseManager iBaseManager,
        IShoppingCartManager iShoppingCartManager,
        IGoogleAnalyticsManager iGoogleAnalyticsManager) {
        this.iCommodityManager = iCommodityManager;
        this.iBaseManager = iBaseManager;
        this.iAccountManager = iAccountManager;
        this.iShoppingCartManager = iShoppingCartManager;
        this.iGoogleAnalyticsManager = iGoogleAnalyticsManager;
    }

    public long getCurrUserSelectedProductMaxStockQty() {
        return currUserSelectedProductMaxStockQty;
    }

    public void setCurrUserSelectedProductMaxStockQty(long currUserSelectedProductMaxStockQty) {
        this.currUserSelectedProductMaxStockQty = currUserSelectedProductMaxStockQty;
    }

    public long getUserSelectedProductQty() {
        return userSelectedProductQty;
    }

    public void setUserSelectedProductQty(int userSelectedProductQty) {
        this.userSelectedProductQty = userSelectedProductQty;
    }

    public void setOutOfStock(boolean isOutOfStock) {
        this.isOutOfStock = isOutOfStock;
    }

    public ProductDetailModel getProductData() {
        return mProduct;
    }

    @Override
    public void setDialogType(String type) {
        this.mDialogType = type;
    }

    @Override
    public void loadProductDetailData(final String productId) {
        String session = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey() : "";
        if (DIALOG_TYPE_BOTTOM.equals(mDialogType)) {
            mView.showBottomProgressDialog();
        } else {
            mView.showNornalProgressDialog();
        }
        Subscription subscription = iCommodityManager.getProductDetail(session, productId)
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
                    mProduct = productDetailModel;
                    mView.dissmissProgressDialog();
                    mView.showContentLayout();
                    mView.loadStaticData(productDetailModel);
                    mView.clearUserSelectedProduct();
                    iGoogleAnalyticsManager
                        .googleAnalyticsProductDetail(productDetailModel.getId());
                    loadPropertyView(productDetailModel);
                    loadBindProductView(productDetailModel);
                    showVisibleProduct(productDetailModel);
                    getRecommendProduct(productId);
                }
            });
        addSubscrebe(subscription);
    }

    private void loadBindProductView(ProductDetailModel productDetailModel) {
        if (productDetailModel.getRelatedProducts() != null && productDetailModel
            .getRelatedProducts().size() > 0) {
            productDetailModel.getRelatedProducts().add(0, productDetailModel.getProperty().get(0));
            mView.showBindProductView(productDetailModel.getRelatedProducts());
        } else {
            mView.hideBindProductView();
        }
    }

    public void showVisibleProduct(ProductDetailModel productDetailModel) {
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
            if (JDataUtils.isEmpty(productDetailModel.getAvailability()) || "1"
                .equals(productDetailModel.getAvailability())) {
                mView.hideAvailabilityView();
            } else {
                mView.showAvailabilityView();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public void loadPropertyView(ProductDetailModel productDetailModel) {
        try {
            ArrayList<String> productImages = new ArrayList<>();
            if (productDetailModel.getImages() != null && productDetailModel.getImages()
                .size() > 0) {
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
        } catch (Exception ex) {
            JLogUtils.i("ray", "exception:" + ex.getStackTrace());
        }
    }

    public void getShoppingCount() {
        Subscription subscription = iCommodityManager.getLocalShoppingProductCount()
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
                    getShoppingCartCount(integer);
                }
            });
        addSubscrebe(subscription);
    }

    private void getShoppingCartCount(int count) {
        if (iBaseManager.isSign()) {
            count = (int) (iBaseManager.getUser().getCartItemCount() + count);
        } else {
            count = iShoppingCartManager.getProductCountFromLocal();
        }
        mView.setShoppingCartCount(count);
    }

    @Override
    public void saveShoppingCartCount(int num) {
        if (iBaseManager.isSign()) {
            GOUserEntity userEntity = iBaseManager.getUser();
            userEntity.setCartItemCount(num);
            iBaseManager.saveUser(userEntity);
        }
    }

    @Override
    public void wishListBtnClick() {
        if (iBaseManager.isSign()) {
            if (mProduct.getIsLike() == 1 && !JDataUtils.isEmpty(mProduct.getItemId())) {
                mProduct.setIsLike(0);
                mView.setWishIconColorToBlank();
                delteFromWishlistRequest(mProduct.getItemId());
            } else if (mProduct.getIsLike() == 0) {
                mProduct.setIsLike(1);
                mView.setWishIconColorToThemeColor();
                addToWishlistRequest(mProduct.getId());
                iGoogleAnalyticsManager
                    .googleAnalyticsEvent(IGoogleAnalyticsManager.CATEGORY_PROCDUCT,
                        IGoogleAnalyticsManager.ACTION_ADDWISH, mProduct.getName(),
                        mProduct.getId());
            }
        } else {
            mView.startLoginActivity(false);
        }
    }

    public void setmProduct(ProductDetailModel mProduct) {
        this.mProduct = mProduct;
    }

    public void addToWishlistRequest(String productId) {
        String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey() : "";
        Subscription subscription = iAccountManager.addWishlist(sessionKey, productId)
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
                    if (responseModel != null) {
                        mProduct.setItemId(responseModel.getItemId());
                        GOUserEntity goUserEntity = iBaseManager.getUser();
                        goUserEntity.setWishListItemCount(responseModel.getWishListItemCount());
                        iBaseManager.saveUser(goUserEntity);
                    }
                }
            });
        addSubscrebe(subscription);
    }

    public void delteFromWishlistRequest(String itemId) {
        String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey() : "";
        iAccountManager.deleteWishlist(sessionKey, itemId)
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
                    if (responseModel != null) {
                        GOUserEntity goUserEntity = iBaseManager.getUser();
                        goUserEntity.setWishListItemCount(responseModel.getWishListItemCount());
                        iBaseManager.saveUser(goUserEntity);

                    }
                }
            });
    }

    public void productCountMinusClick() {
        if (currUserSelectedProductMaxStockQty <= 0) {
            mView.showNoInventoryToast();
            return;
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

    public void productCountPlusClick() {
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
        if (!isOutOfStock && userSelectedProductQty > 0) {
            Map<String, String> params = getAddToCartParams();
            if (iBaseManager.isSign()) {
                addToShoppingCart(params);
            } else {
                addLocalShopping(params);
            }
        } else if (isOutOfStock) {
            wishListBtnClick();
        }
    }

    private void addLocalShopping(Map<String, String> map) {
        List<ShoppingItemLocalModel> shoppingItemLocalModels = new ArrayList<>();
        for (String simpleId : map.keySet()) {
            shoppingItemLocalModels
                .add(new ShoppingItemLocalModel(mProduct.getId(), simpleId, map.get(simpleId)));
        }
        iShoppingCartManager.saveProductToLocal(shoppingItemLocalModels)
            .compose(RxUtil.<Boolean>rxSchedulerHelper())
            .subscribe(new Subscriber<Boolean>() {
                           @Override
                           public void onCompleted() {

                           }

                           @Override
                           public void onError(Throwable throwable) {

                           }

                           @Override
                           public void onNext(Boolean aBoolean) {
                               mView.startAddToCart();
                           }
                       }
            );
    }

    public void delayAddToCart() {
        if (delayAddToCart) {
            Map<String, String> params = getAddToCartParams();
            addToShoppingCart(params);
        } else {
            loadProductDetailData(mProduct.getId());
        }
    }

    @Nullable
    public Map<String, String> getAddToCartParams() {
        Map<String, String> params = null;
        switch (mProduct.getType()) {
            case TYPE_GROUP:
                params = mView.getGroupProductParams();
                break;
            case TYPE_CONFIGURABLE:
                params = new HashMap<>();
                params.put(mView.getConfiguationProductSimpleId(), userSelectedProductQty + "");
                break;
            case TYPE_SIMPLE:
                params = new HashMap<>();
                params.put(mProduct.getId(), userSelectedProductQty + "");
                break;
        }
        return params;
    }

    public void addToShoppingCart(Map<String, String> params) {
        mView.showNornalProgressDialog();
        iGoogleAnalyticsManager.googleAnalyticsEvent(IGoogleAnalyticsManager.CATEGORY_PROCDUCT,
            IGoogleAnalyticsManager.ACTION_ADDTOCART, mProduct.getName(), mProduct.getId());
        //TODO joyson may be use
//        iGoogleAnalyticsManager.googleAnalyticsAddCart(mProduct.getId(),mProduct.getName());
        String sessionKey = iBaseManager.getUser().getSessionKey();
        iShoppingCartManager.addProductToShoppingCart(sessionKey, mProduct.getId(), params)
            .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
            .subscribe(new Subscriber<ResponseModel>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    mView.dissmissProgressDialog();
                    if (JToolUtils.expireHandler(ExceptionParse.parseException(e).getErrorMsg())) {
                        mView.startLoginActivity(true);
                    }
                    JLogUtils
                        .i("ray", "errorMsg:" + ExceptionParse.parseException(e).getErrorMsg());
                    mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
                }

                @Override
                public void onNext(ResponseModel responseModel) {
                    mView.dissmissProgressDialog();
                    mView.startAddToCart();
                }
            });
    }

    public void getRecommendProduct(String productId) {
        String sessionKey = iBaseManager.isSign() ? iBaseManager.getUser().getSessionKey() : "";
        Subscription subscription = iCommodityManager
            .getProductRecommendList("1", "4", productId, sessionKey)
            .compose(RxUtil.<SVRAppserviceProductRecommendedReturnEntity>rxSchedulerHelper())
            .subscribe(new Subscriber<SVRAppserviceProductRecommendedReturnEntity>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(SVRAppserviceProductRecommendedReturnEntity result) {
                    if (result.getResults().size() > 0) {
                        mView.showProductRecommendLine();
                        mView.updateRecommendData(result.getResults());
                    }
                }
            });
        addSubscrebe(subscription);
    }

    /**
     * productDetail
     */
    public void addCartToTopAnim(final RelativeLayout parentView, final ImageView sourceIv,
        ImageView targetIv) {
        final ImageView goods = new ImageView(parentView.getContext());
        goods.setImageDrawable(sourceIv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sourceIv.getWidth(),
            sourceIv.getHeight());
        parentView.addView(goods, params);

        int[] parentLocation = new int[2];
        parentView.getLocationInWindow(parentLocation);

        int startLoc[] = new int[2];
        sourceIv.getLocationInWindow(startLoc);

        int endLoc[] = new int[2];
        targetIv.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLocation[0] + sourceIv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + sourceIv.getHeight() / 2;

        float toX = endLoc[0] - parentLocation[0] - targetIv.getWidth();
        float toY = endLoc[1] - parentLocation[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        //quadTo twice bessel  cubicTo thrice  bessel
//        path.quadTo((startX + toX) / 2, startY, toX, toY);
        path.cubicTo(startX / 2, startY / 4,
            WhiteLabelApplication.getPhoneConfiguration().getScreenWidth() / 2, -400, toX, toY);
        final PathMeasure mPathMeasure = new PathMeasure(path, false);

//TODO  joyson  may add scale anim
//        PropertyValuesHolder mPropertyValuesHolderScale = PropertyValuesHolder.ofFloat("scale",
// 1.0f,0.3f);
        PropertyValuesHolder mRotation = PropertyValuesHolder.ofFloat("rotation", 0.0f, 1080.0f);
        ValueAnimator styleAnimator = ValueAnimator.ofPropertyValuesHolder(mRotation);
        styleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                float animatorValueScale =  (float) animation.getAnimatedValue("scale");
                float animatorRotation = (float) animation.getAnimatedValue("rotation");
//                goods.setScaleX(animatorValueScale);
//                goods.setScaleY(animatorValueScale);
                goods.setRotation(animatorRotation);
            }
        });
        styleAnimator.setInterpolator(new LinearInterpolator());
        ValueAnimator transAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());

        transAnimator.setInterpolator(new LinearInterpolator());
        final float[] mCurrentPosition = new float[2];
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(transAnimator).with(styleAnimator);
        animSet.setDuration(500);
        animSet.start();
        transAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                parentView.removeView(goods);
                sourceIv.setVisibility(View.INVISIBLE);
                getShoppingCount();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

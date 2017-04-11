package com.whitelabel.app.ui.brandstore;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/2/28.
 */
public class BrandStorePresenter
        extends RxPresenter<BrandStoreContract.View>
        implements BrandStoreContract.Presenter{
    private final String TAG=this.getClass().getSimpleName();

    public void getBrandProductList(String brandId,final int offset, int  limit, String price, String order,String dir,String  modelType){
        String sessionKey="";
        if(WhiteLabelApplication.getAppConfiguration().getUserInfo()!=null){
            sessionKey= WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey();
        }
       Subscription  subscription= DataManager.getInstance().getProductApi().
                getProductListByBrandId(brandId,String.valueOf(offset),
                        String.valueOf(limit),
                        sessionKey,price,order,dir,modelType).compose(RxUtil.<BrandStoreModel>rxSchedulerHelper()).
               subscribe(new Action1<BrandStoreModel>() {
                   @Override
                   public void call(BrandStoreModel brandStoreModel) {
                       if(offset==1) {
                           mView.closeProgressDialog();
                       }
                       if (brandStoreModel.getStatus() == 1) {
                           if (offset == 1 && (brandStoreModel.getAllProducts() == null || brandStoreModel.getAllProducts().getResults() == null || brandStoreModel.getAllProducts().getResults().size() == 0)) {
                               mView.showNoDataView();
                           } else {
                               mView.showContentView(brandStoreModel,offset);
                           }
                       }
                   }
               }, new ErrorHandlerAction() {
                   @Override
                   protected void requestError(ApiFaildException ex) {
                       if(ex.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                           mView.showNetworkErrorView(ex.getErrorMsg());
                       }

                   }
               });
        addSubscrebe(subscription);
    }
    @Override
    public void addWistList(final  SVRAppserviceProductSearchResultsItemReturnEntity bean) {
        String sessionKey="";
        if(WhiteLabelApplication.getAppConfiguration().getUserInfo()!=null){
            sessionKey= WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey();
        }
        Subscription subscription=DataManager.getInstance().getProductApi().
                addWishList(sessionKey,bean.getProductId()).
                compose(RxUtil.<AddToWishlistEntity>rxSchedulerHelper()).
                subscribe(new Action1<AddToWishlistEntity>() {
                    @Override
                    public void call(AddToWishlistEntity addToWishlistEntity) {
                        bean.setItem_id(addToWishlistEntity.getItemId());
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(WhiteLabelApplication.getInstance(), addToWishlistEntity.getWishListItemCount());
                        GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
                                "Add To Wishlist",
                                bean.getName(),
                                Long.valueOf(bean.getProductId()));
                        JLogUtils.i("googleGA", "add to wishlist ");
                    }
                });

        addSubscrebe(subscription);

    }


    public ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductSearchResultsItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setFinalPrice(e.getFinal_price());
        try {
            entity.setInStock(Integer.parseInt(e.getInStock()));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        return entity;
    }

    @Override
    public void deleteWishListByItemId(final SVRAppserviceProductSearchResultsItemReturnEntity bean) {
        String sessionKey=null;
        if(WhiteLabelApplication.getAppConfiguration().getUserInfo()!=null){
            sessionKey= WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey();
        }
        Subscription  subscription=DataManager.getInstance().getProductApi().deleteWistListById(sessionKey,bean.getItem_id())
                .compose(RxUtil.<WishDelEntityResult>rxSchedulerHelper()).subscribe(new Action1<WishDelEntityResult>() {
                    @Override
                    public void call(WishDelEntityResult wishDelEntityResult) {
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(WhiteLabelApplication.getInstance(), wishDelEntityResult.getWishListItemCount());
                    }
                });
        addSubscrebe(subscription);
    }
}

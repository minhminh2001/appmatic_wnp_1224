package com.whitelabel.app.ui.productdetail;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
/**
 * Created by Administrator on 2017/7/10.
 */
public class ProductDetailPresenter  extends RxPresenter<ProductDetailContract.View>implements ProductDetailContract.Presenter{
    private ICommodityManager iCommodityManager;
    private IBaseManager  iBaseManager;
    private String mDialogType;
    public static final String TYPE_CONFIGURABLE = "configurable";
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_GROUP="grouped";

    private ProductDetailModel  mProduct;
    public ProductDetailPresenter(ProductDetailContract.View  view, ICommodityManager iCommodityManager, IBaseManager iBaseManager){
        this.mView=view;
        this.iCommodityManager=iCommodityManager;
        this.iBaseManager=iBaseManager;
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
        if(mDialogType!=null&&"from_product_list".equals(mDialogType)){
            mView.showBottomProgressDialog();
        }else{
            mView.showNornalProgressDialog();
        }
       iCommodityManager.getProductDetail(session,productId)
       .compose(RxUtil.<ProductDetailModel>rxSchedulerHelper())
       .subscribe(new Subscriber<ProductDetailModel>() {
           @Override
           public void onCompleted() {
           }
           @Override
           public void onError(Throwable e) {
               JLogUtils.i("ray","errorMsg:"+e.getLocalizedMessage());

               mView.dissmissProgressDialog();
               mView.showErrorMessage(ExceptionParse.parseException(e).getErrorMsg());
           }
           @Override
           public void onNext(ProductDetailModel productDetailModel) {
               mView.dissmissProgressDialog();
               mProduct=productDetailModel;
               mView.showContentLayout();
               mView.loadStaticData(productDetailModel);
               mView.clearUserSelectedProduct();
               loadSyncData(productDetailModel);
               mView.showActionView();
               showVisibleProduct(productDetailModel);
           }
       });
    }
    public void showVisibleProduct(ProductDetailModel  productDetailModel){
        try {
            if ("0".equals(productDetailModel)) {
                mView.hideVisibleProduct();
                return;
            }
            if (productDetailModel.getIsLike() == 1) {
                mView.setLikeView(true);
            } else {
                mView.setLikeView(false);
            }
            if (JDataUtils.isEmail(productDetailModel.getAvailability()) || "1".equals(productDetailModel.getAvailability())) {
                mView.hideAvailabilityView();
            } else {
                mView.showAvailabilityView();
            }
        }catch (Exception ex){
            JLogUtils.i("ray","error1:"+ex.getStackTrace());
        }
    }
    public void  loadSyncData(ProductDetailModel productDetailModel){
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
            List<SVRAppserviceProductDetailResultPropertyReturnEntity> products = productDetailModel.getRelatedProducts();
            if (products != null && products.size() > 0) {
                products.add(0, productDetailModel.getProperty().get(0));
                mView.showBindProductView(products);
            } else {
                mView.hideBindProductView();
            }
        }catch (Exception ex){
            JLogUtils.i("ray","error:"+ex.getStackTrace().toString());
        }
    }
}

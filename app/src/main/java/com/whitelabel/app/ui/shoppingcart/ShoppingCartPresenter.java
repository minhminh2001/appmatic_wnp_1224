package com.whitelabel.app.ui.shoppingcart;

import android.view.View;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.model.ShoppingCart;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingDiscountBean;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ShoppingCartPresenter extends RxPresenter<ShoppingCartContract.View > implements ShoppingCartContract.Presenter {
    private IShoppingCartManager iShoppingCartManager;
    private IBaseManager iBaseManager;
    public ShoppingCartPresenter(ShoppingCartContract.View view, IShoppingCartManager iShoppingCartManager, IBaseManager iBaseManager){
        this.mView=view;
        this.iShoppingCartManager=iShoppingCartManager;
        this.iBaseManager=iBaseManager;
    }
    @Override
    public void loadData(boolean showDialog) {
        if(showDialog){
            mView.showProgressDialog();
        }
       String sessionkey=iBaseManager.getUser().getSessionKey();
        iShoppingCartManager.getShoppingCartInfo(sessionkey)
                .compose(RxUtil.<ShoppingCartListEntityCart>rxSchedulerHelper())
                .subscribe(new Subscriber<ShoppingCartListEntityCart>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.dissmissProgressDialog();
                        mView.closeSwipeRefrshLayout();
                        if(ExceptionParse.parseException(e).getErrorType()==ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showNetErrorMsg();
                        }
                    }
                    @Override
                    public void onNext(ShoppingCartListEntityCart shoppingCartListEntityCart) {
                        mView.dissmissProgressDialog();
                        mView.closeSwipeRefrshLayout();
                        if(shoppingCartListEntityCart.getItems()==null||shoppingCartListEntityCart.getItems().length==0){
                            mView.showEmptyDataLayout();
                            return;
                        }
                        mView.setLayoutHaveProduct();
                        mView.loadProductsRecyclerView(shoppingCartListEntityCart.getItems());
                        setDiscount(shoppingCartListEntityCart.getDiscount());
                        mView.setBottomPriceView(shoppingCartListEntityCart);
                    }
                });
    }
    public void  setDiscount(ShoppingDiscountBean discount){
        if(discount!=null&&!JDataUtils.isEmpty(discount.getCouponCode())) {
            mView.setLayoutHaveVercherCode(discount.getCouponCode());
        }else if(discount!=null&&"1".equals(discount.getStopRulesProcessing())){
            mView.hideVercherCodeLayout();
        }else{
            mView.setLayoutNotHaveVercherCode();
        }
    }
}

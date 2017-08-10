package com.whitelabel.app.ui.home.presenter;

import android.util.Log;

import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.HomeContract;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/5.
 */
public class HomePresenterImpl extends RxPresenter<HomeContract.View> implements HomeContract.Presenter{
        private ICommodityManager mCommodityManager;
        private IBaseManager mBaseManager;
        private String TAG="";

        @Inject
        public HomePresenterImpl(ICommodityManager commodityManager,IBaseManager  baseManager){
            TAG=this.getClass().getSimpleName();
            this.mBaseManager=baseManager;
            mCommodityManager=commodityManager;
        }
        public void getBaseCategory(){
               mView.showProgressDialog();
              Subscription subscription= mCommodityManager.getAllCategoryManager()
                      .subscribeOn(Schedulers.newThread())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(new Subscriber<SVRAppserviceCatalogSearchReturnEntity>() {
                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onError(Throwable e) {
                              if(ExceptionParse.parseException(e).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR) {
                                  mView.showOnlineErrorLayout();
                              }
                          }

                          @Override
                          public void onNext(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                              mView.dissmissProgressDialog();
                              mView.showRootView();
                              mView.hideOnlineErrorLayout();
                              mView.loadData(svrAppserviceCatalogSearchReturnEntity);
                          }
                      });
            addSubscrebe(subscription);
        }
       public void getShoppingCount(){
          Subscription  subscription=  mCommodityManager.getLocalShoppingProductCount()
                    .compose(RxUtil.<Integer>rxSchedulerHelper())
            .subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                }
                @Override
                public void onError(Throwable e) {
                    Log.i(TAG,"error:"+e.getMessage());
                }
                @Override
                public void onNext(Integer integer) {
                    setShoppingCartCount(integer);
                }
            });
          addSubscrebe(subscription);
        }
    @Override
    public void getLocalBaseCategory() {
          Subscription subscription= mCommodityManager.getLocalCategoryManager()
                   .compose(RxUtil.<SVRAppserviceCatalogSearchReturnEntity>rxSchedulerHelper())
                   .subscribe(new Subscriber<SVRAppserviceCatalogSearchReturnEntity>() {
                       @Override
                       public void onCompleted() {

                       }
                       @Override
                       public void onError(Throwable e) {

                       }

                       @Override
                       public void onNext(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {

                           mView.showRootView();
                           mView.hideOnlineErrorLayout();
                           mView.loadData(svrAppserviceCatalogSearchReturnEntity);
                       }
                   });
        addSubscrebe(subscription);
    }
    @Override
    public String formatShoppingCount(int count) {
           if(count>99){
               return "99+";
           }else{
               return count+"";
           }
     }
    public void setShoppingCartCount(int count){
            if(mBaseManager.isSign()) {
                count= (int) (mBaseManager.getUser().getCartItemCount()+count);
            }
             if(count!=0) {
                 mView.setShoppingCartCount(count);
             }
        }
}

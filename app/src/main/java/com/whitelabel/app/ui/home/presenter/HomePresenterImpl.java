package com.whitelabel.app.ui.home.presenter;

import android.util.Log;

import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.HomeContract;
import com.whitelabel.app.utils.RxUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/5.
 */
public class HomePresenterImpl extends RxPresenter<HomeContract.View> implements HomeContract.Presenter{
        private CommodityManager mCommodityManager;
        private BaseManager mBaseManager;
        private String TAG="";
        private  boolean firstLoading=true;
        public HomePresenterImpl(HomeContract.View  view,CommodityManager commodityManager,BaseManager  baseManager){
            this.mView=view;
            TAG=this.getClass().getSimpleName();
            this.mBaseManager=baseManager;
            mCommodityManager=commodityManager;
        }
        public void getBaseCategory(){
              if(firstLoading) {
                  mView.showProgressDialog();
              }
              Subscription subscription= mCommodityManager.getAllCategoryManager()
                      .compose(RxUtil.<SVRAppserviceCatalogSearchReturnEntity>rxSchedulerHelper())
                      .subscribe(new Action1<SVRAppserviceCatalogSearchReturnEntity>() {
                          @Override
                          public void call(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                            mView.dissmissProgressDialog();
                            mView.showRootView();
                            mView.hideOnlineErrorLayout();
                            mView.loadData(svrAppserviceCatalogSearchReturnEntity);
                            firstLoading=false;
                          }
                      }, new Action1<Throwable>() {
                          @Override
                          public void call(Throwable throwable) {
                               mView.showOnlineErrorLayout();
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

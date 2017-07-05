package com.whitelabel.app.ui.home.presenter;

import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.model.ApplicationConfigurationEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.HomeContract;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/5.
 */
public class HomePresenterImpl extends RxPresenter<HomeContract.View> implements HomeContract.Presenter{
        private CommodityManager mCommodityManager;
        private ApplicationConfigurationEntity  mApplicationConfig;
        public HomePresenterImpl(HomeContract.View  view,CommodityManager commodityManager,ApplicationConfigurationEntity mApplicationConfig){
            this.mView=view;
            this.mCommodityManager=commodityManager;
        }
        public void getBaseCategory(){
              mView.showProgressDialog();
              Subscription subscription= mCommodityManager.getAllCategoryManager()
                      .compose(RxUtil.<SVRAppserviceCatalogSearchReturnEntity>rxSchedulerHelper())
                      .subscribe(new Action1<SVRAppserviceCatalogSearchReturnEntity>() {
                          @Override
                          public void call(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                            mView.dissmissProgressDialog();
                            mView.hideOnlineErrorLayout();
                            mView.loadData(svrAppserviceCatalogSearchReturnEntity);
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

        }



}

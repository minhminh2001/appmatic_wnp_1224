package com.whitelabel.app.ui.home.presenter;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.ui.home.HomeHomeContract;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ray on 2017/4/7.
 */

public class HomeHomePresenterImpl extends RxPresenter<HomeHomeContract.View> implements HomeHomeContract.Presenter {
    private ICommodityManager iCommodityManager;
    public  HomeHomePresenterImpl(ICommodityManager commodityManager){
            this.iCommodityManager=commodityManager;
    }


    public void getSearchCategory(){
       Subscription  subscription= iCommodityManager.getAllCategoryManager().
                compose(RxUtil.<SVRAppserviceCatalogSearchReturnEntity>rxSchedulerHelper())
                .subscribe(new Action1<SVRAppserviceCatalogSearchReturnEntity>() {
                    @Override
                    public void call(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                        mView.closeProgressDialog();
                        if(svrAppserviceCatalogSearchReturnEntity.getStatus()==1){
                             mView.loadRecyclerViewData(svrAppserviceCatalogSearchReturnEntity);
                        }
                    }
                }, new ErrorHandlerAction() {
                    @Override
                    protected void requestError(ApiFaildException ex) {
                        mView.closeProgressDialog();
                        mView.showErrorMsg(ex.getErrorMsg());
                    }
                });
        addSubscrebe(subscription);
    }

}

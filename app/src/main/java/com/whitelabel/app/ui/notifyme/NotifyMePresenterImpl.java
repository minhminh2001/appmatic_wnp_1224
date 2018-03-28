package com.whitelabel.app.ui.notifyme;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.NotifyMeResponse;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Observer;

/**
 * Created by Aaron on 2018/3/27.
 */

public class NotifyMePresenterImpl extends RxPresenter<NotifyMeConstract.View > implements NotifyMeConstract.Presenter{

    private static final int REQUEST_SUCCESS = 1;

    private ICommodityManager iCommodityManager;
    private IBaseManager iBaseManager;

    public NotifyMePresenterImpl(IBaseManager iBaseManager, ICommodityManager iCommodityManager){
        this.iCommodityManager = iCommodityManager;
        this.iBaseManager = iBaseManager;
    }

    @Override
    public void registerNotifyForProduct(String productId, String storeId, String name, String emal, String sessionKey) {

        iCommodityManager.registerNotifyForProduct(productId, emal, name, storeId, sessionKey)
                .compose(RxUtil.<NotifyMeResponse>rxSchedulerHelper())
                .subscribe(new Observer<NotifyMeResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NotifyMeResponse notifyMeResponse) {

                        if(notifyMeResponse.getStatus() == REQUEST_SUCCESS){
                            mView.onSuccess();
                        } else {
                            mView.showErrorMsg(notifyMeResponse.getResult());
                        }
                    }
                });
    }


}

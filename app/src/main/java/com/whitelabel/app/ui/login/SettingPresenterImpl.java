package com.whitelabel.app.ui.login;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SubscriberResponse;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RxUtil;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by img on 2018/1/29.
 */

public class SettingPresenterImpl extends RxPresenter<SettingContract.View> implements SettingContract.Presenter{

    private IAccountManager iAccountManager;
    private IBaseManager iBaseManager;

    @Inject
    public SettingPresenterImpl(IBaseManager iBaseManager,IAccountManager iAccountManager) {
        this.iBaseManager=iBaseManager;
        this.iAccountManager = iAccountManager;
    }

    @Override
    public void setUserAgreement(String isAgreement) {
        mView.showProgressDialog();
        iAccountManager.setUserAgreement(iBaseManager.getUser().getSessionKey(),isAgreement).compose(
            RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(new Subscriber<ResponseModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                mView.closeProgressDialog();
                mView.setSubscriberSuccess(false);
            }

            @Override
            public void onNext(ResponseModel responseModel) {
                mView.closeProgressDialog();
                mView.setSubscriberSuccess(true);
            }
        });
    }

    @Override
    public void getUserAgreement() {
        mView.showProgressDialog();
        iAccountManager.getUserAgreement(iBaseManager.getUser().getSessionKey()).compose(RxUtil.<SubscriberResponse>rxSchedulerHelper()).subscribe(

            new Subscriber<SubscriberResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    mView.closeProgressDialog();
                }

                @Override
                public void onNext(SubscriberResponse subscriberResponse) {
                    mView.closeProgressDialog();
                    JToolUtils.printObject(subscriberResponse);

                    if (subscriberResponse.getSubscribed().equals("1")){
                        mView.getIsSubscriber(true);
                    }else {
                        mView.getIsSubscriber(false);
                    }
                }
            });
    }


}

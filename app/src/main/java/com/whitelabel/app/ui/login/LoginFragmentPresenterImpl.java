package com.whitelabel.app.ui.login;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.NativeLoginRequest;
import com.whitelabel.app.model.ResponseConnection;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/8/8.
 */
public class LoginFragmentPresenterImpl extends RxPresenter<LoginFragmentContract.View> implements LoginFragmentContract.Presenter {
    private IBaseManager iBaseManager;
    private  IAccountManager iAccountManager;
    public LoginFragmentPresenterImpl(IBaseManager iBaseManager, IAccountManager iAccountManager){
        this.iBaseManager=iBaseManager;
        this.iAccountManager=iAccountManager;
    }
    public void requestOnallUser(String  platform,String accessToken,String secret){
        mView.showProgressDialog();
       Subscription subscription= iAccountManager.getOneAllUser(platform,accessToken,secret)
                .compose(RxUtil.<ResponseConnection>rxSchedulerHelper())
                 .subscribe(new Subscriber<ResponseConnection>() {
                     @Override
                     public void onCompleted() {
                     }
                     @Override
                     public void onError(Throwable e) {
                         JLogUtils.i("ray","error:"+e.getMessage());
                     }
                     @Override
                     public void onNext(ResponseConnection responseConnection) {
                         JLogUtils.i("ray","token:"+responseConnection.data.user.publishToken.key);
                         mView.closeProgressDialog();
                     }
                 });
         addSubscrebe(subscription);

    }
}

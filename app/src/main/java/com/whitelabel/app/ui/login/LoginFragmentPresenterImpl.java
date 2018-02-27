package com.whitelabel.app.ui.login;

import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.ResponseConnection;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JDataUtils;
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
    private final String EMAIL_CONFIRMATION = "This account is not confirmed";
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
                         mView.closeProgressDialog();
                         JLogUtils.i("ray","error:"+e.getMessage());
                     }
                     @Override
                     public void onNext(ResponseConnection responseConnection) {
                         ResponseConnection.ResponseBean.ResultBean.DataBean.UserBean.IdentityBean identityBean=responseConnection.getResponse().getResult().getData().getUser().getIdentity();
                         final String givenName=identityBean.getName()==null||JDataUtils.isEmpty(identityBean.getName().getGivenName())?"":identityBean.getName().getGivenName();
                         final String formatted=identityBean.getName()==null||JDataUtils.isEmpty(identityBean.getName().getFormatted())?"":identityBean.getName().getFormatted();
                         final String familyName=identityBean.getName()==null||JDataUtils.isEmpty(identityBean.getName().getFamilyName())?"":identityBean.getName().getFamilyName();
                         final String displayName=JDataUtils.isEmpty(identityBean.getDisplayName())?"":identityBean.getDisplayName();
                         final String identityToken=identityBean.getIdentity_token();
                         final String userToken=responseConnection.getResponse().getResult().getData().getUser().getUser_token();
                         final String email= identityBean.getEmails()!=null&&identityBean.getEmails().size()>0?identityBean.getEmails().get(0).getValue():"";
                         final String provider =identityBean.getSource().getName();
                         loginFromServer(givenName,formatted,familyName,displayName,identityToken,userToken,email,provider,false,"0");
                     }
                 });
         addSubscrebe(subscription);
    }
    public void  loginFromServer(final String givenName,final String formatted,
                                 final String familyName,final String displayName,
                                 final String identityToken,final String userToken,
                                 final String email,final String provider,boolean showProgress, String boundEmail ){
        if(showProgress){
            mView.showProgressDialog();
        }
        Subscription subscription=iAccountManager.threePartLogin(givenName,displayName,formatted,familyName,email,identityToken,userToken,provider,boundEmail)
                .compose(RxUtil.<SVRAppserviceCustomerFbLoginReturnEntity>rxSchedulerHelper())
                .subscribe(new Subscriber<SVRAppserviceCustomerFbLoginReturnEntity>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        mView.closeProgressDialog();
                       ApiFaildException apiFaildException= ExceptionParse.parseException(throwable);
                        if(apiFaildException.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showNetErrorMessage();
                        }
                        if(apiFaildException.getErrorType()==ExceptionParse.ERROR.API_ERROR){
                             int code= (Integer) apiFaildException.getData();
                             if(code==-1){
                                mView.jumpBoundEmailFragment(givenName,formatted,familyName,displayName,identityToken,userToken,email,provider);
                             }else if(code==-2||apiFaildException.getErrorMsg().contains(EMAIL_CONFIRMATION)){
                                 mView.showConfirmEmail();
                             }else{
                                 mView.showErrorMessage(apiFaildException.getErrorMsg());
                             }
                        }
                    }
                    @Override
                    public void onNext(SVRAppserviceCustomerFbLoginReturnEntity svrAppserviceCustomerFbLoginReturnEntity) {
                        mView.closeProgressDialog();
                        mView.loginSuccess(svrAppserviceCustomerFbLoginReturnEntity);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void versionCheck() {
        Subscription  subscription= iBaseManager.versionCheck().compose(RxUtil.<ResponseModel>rxSchedulerHelper()).subscribe(
            new Subscriber<ResponseModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    if(ExceptionParse.parseException(throwable).getErrorType()== ExceptionParse.ERROR.HTTP_ERROR) {
                        mView.showErrorMessage(ExceptionParse.parseException(throwable).getErrorMsg());
                    }
                }

                @Override
                public void onNext(ResponseModel responseModel) {
                    if (responseModel.getStatus()==1){
                        mView.emailLoginOrRegister();
                    }else if (responseModel.getStatus()==-1){
                        //need update
                        mView.showUpdateDialog();
                    }
                }
            });
        addSubscrebe(subscription);
    }


}

package com.whitelabel.app.ui.common;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
/**
 * Created by Administrator on 2017/6/12.
 */
public class BaseAddressPresenter extends RxPresenter<BaseAddressContract.View> implements BaseAddressContract.Presenter {
    private boolean  useCache;
    public BaseAddressPresenter(boolean  useCache){
        this.useCache=useCache;
    }
    @Override
    public void getAddressListCache(final String sessionKey) {
      Subscription subscriber= DataManager.getInstance().getPreferHelper().
                getAddressListCache(WhiteLabelApplication.getAppConfiguration().getUserInfo().getId())
                .compose(RxUtil.<List<AddressBook>>rxSchedulerHelper())
                .subscribe(new Action1<List<AddressBook>>() {
                    @Override
                    public void call(List<AddressBook> addressBooks) {
                         mView.openSwipeLayout();
                         getAddressListOnLine(sessionKey);
                         mView.loadData(addressBooks);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
      addSubscrebe(subscriber);
    }
    @Override
    public void deleteAddressById(final String sessionKey, String addressId) {
         DataManager.getInstance().getMyAccountApi().deleteAddressById(sessionKey,addressId)
                 .compose(RxUtil.<ResponseModel>rxSchedulerHelper())
                 .subscribe(new Action1<ResponseModel>() {
                     @Override
                     public void call(ResponseModel responseModel) {
                         mView.closeProgressDialog();
                        if(responseModel.getStatus()==1){
                            mView.openSwipeLayout();
                            getAddressListOnLine(sessionKey);
                        }
                     }
                 }, new ErrorHandlerAction() {
                     @Override
                     protected void requestError(ApiFaildException ex) {
                        mView.showNetworkErrorView(ex.getErrorMsg());
                     }
                 });

    }

    @Override
    public void getAddressListOnLine(String sessionKey) {
       Subscription subscription= DataManager.getInstance().getMyAccountApi().getAddressList(sessionKey)
                .compose(RxUtil.<AddresslistReslut>rxSchedulerHelper())
                .subscribe(new Action1<AddresslistReslut>() {
                    @Override
                    public void call(AddresslistReslut addresslistReslut) {
                        mView.closeProgressDialog();
                        mView.closeSwipeLayout();
                        if(addresslistReslut.getStatus()==1){
                            if(useCache){
                                DataManager.getInstance().getPreferHelper().saveAddressList(WhiteLabelApplication.getAppConfiguration().getUser().getId(),addresslistReslut.getAddress());
                            }
                            mView.loadData(addresslistReslut.getAddress());
                        }
                    }
                }, new ErrorHandlerAction() {
                    @Override
                    protected void requestError(ApiFaildException ex) {
                        mView.closeProgressDialog();
                        JLogUtils.i("ray","apiFaildException："+ex);
                        if(ex.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showNetworkErrorView(ex.getErrorMsg());
                        }
                    }
                });
       addSubscrebe(subscription);
    }
}

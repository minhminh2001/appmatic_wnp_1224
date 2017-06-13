package com.whitelabel.app.ui.common;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.ui.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.ExceptionParse;
import com.whitelabel.app.utils.RxUtil;
import java.util.List;
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
    public void getAddressListCache(String sessionKey) {
        DataManager.getInstance().getPreferHelper().
                getAddressListCache(WhiteLabelApplication.getAppConfiguration().getUserInfo().getId())
                .compose(RxUtil.<List<AddressBook>>rxSchedulerHelper())
                .subscribe(new Action1<List<AddressBook>>() {
                    @Override
                    public void call(List<AddressBook> addressBooks) {
                        mView.loadData(addressBooks);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }
    @Override
    public void getAddressListOnLine(String sessionKey) {
        DataManager.getInstance().getMyAccountApi().getAddressList(sessionKey)
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
                        mView.closeSwipeLayout();
                        if(ex.getErrorType()== ExceptionParse.ERROR.HTTP_ERROR){
                            mView.showNetworkErrorView();
                        }
                    }
                });
    }
}

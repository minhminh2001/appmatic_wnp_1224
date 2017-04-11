package com.whitelabel.app.ui.start;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.ApiFaildException;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.ui.common.RxPresenter;
import com.whitelabel.app.utils.ErrorHandlerAction;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RxUtil;

import rx.functions.Action1;

/**
 * Created by ray on 2017/4/5.
 */

public class StartPresenterImpl extends RxPresenter<StartContract.View> implements StartContract.Presenter{
    @Override
    public void getConfigInfo() {
       String currentVersionNumber= DataManager.getInstance().getPreferHelper().getVersionNumber();
        JLogUtils.i("ray","currentVersionNumber:"+currentVersionNumber);
        DataManager.getInstance().
                getAppApi().getConfigInfo(currentVersionNumber).
                compose(RxUtil.<RemoteConfigResonseModel>rxSchedulerHelper())
                .subscribe(new Action1<RemoteConfigResonseModel>() {
                    @Override
                    public void call(RemoteConfigResonseModel remoteConfigModel) {
                        if("1".equals(remoteConfigModel.getStatus())){
                            WhiteLabelApplication.getAppConfiguration().setConfigColor(remoteConfigModel.
                                    getData().getUiStyle().getThemeColor(),
                                    remoteConfigModel.getData().getUiStyle().getNavBarBackgroudColor(),
                                    remoteConfigModel.getData().getUiStyle().getButtonPressColor());
                            DataManager.getInstance().getPreferHelper().saveConfigInfo(remoteConfigModel);
                        }
                        mView.delayStart();
                    }
                }, new ErrorHandlerAction() {
                    @Override
                    protected void requestError(ApiFaildException ex) {
                        mView.delayStart();
                        JLogUtils.i("ray","ex"+ex.getErrorMsg());
                    }
                });
    }
}

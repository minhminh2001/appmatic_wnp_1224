package com.whitelabel.app;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;

import rx.observers.TestSubscriber;

/**
 * Created by Administrator on 2017/7/7.
 */
public class SessionKeyProvider {

    public String  getSession(){
        IBaseManager iBaseManager=new BaseManager(DataManager.getInstance().getMockApi(),DataManager.getInstance().getAppApi(),DataManager.getInstance().getPreferHelper());
        TestSubscriber<SVRAppServiceCustomerLoginReturnEntity> testSubscriber=TestSubscriber.create();
        iBaseManager.emailLogin("11@qq.com","111111","","1.0.1","2","1.0.5")
                .subscribe(testSubscriber);
        SVRAppServiceCustomerLoginReturnEntity  user=  testSubscriber.getOnNextEvents().get(0);
        return user.getSessionKey();

    }
}

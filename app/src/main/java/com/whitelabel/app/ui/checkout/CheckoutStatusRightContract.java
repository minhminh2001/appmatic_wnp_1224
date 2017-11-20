package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/7/19.
 */

public interface CheckoutStatusRightContract {
    public interface View extends BaseView{
        public void showOrderNumber(String orderNumber);

    }

    public interface Presenter extends BasePresenter<View>{


        public void requestOrderNumber();
        public void saveFinishOrderAndMarkTime(long currentTime);
        public SkipToAppStoreMarket getFirstOrderAndMarkTime();
    }
}

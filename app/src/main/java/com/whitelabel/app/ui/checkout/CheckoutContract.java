package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface CheckoutContract {
    public interface View extends BaseView{
        public void startPayPalPaymentActivity(String url,String orderNumber);
        public void showNetErrorMessage();
        public void showFaildMessage(String faildMessage);
        public void showCheckProgressDialog();
        public void closeCheckoutPaymentDialog();
        public void setButtonEnable(boolean enable);
        public void showAddressSuccess(boolean isSuccess);
    }
    public interface  Presenter extends BasePresenter<View>{
        public void payPalPlaceOrder(String orderComment);
        public void createCustomerAddress(
                String firstName,
                String lastName,
                String countryId,
                String telePhone,
                String street0,
                String street1,
                String fax,
                String postCode,
                String city,
                String region,
                String regionId);
    }

}

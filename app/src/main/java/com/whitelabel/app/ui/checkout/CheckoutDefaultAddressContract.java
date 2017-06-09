package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;

/**
 * Created by Administrator on 2017/6/9.
 */

public interface CheckoutDefaultAddressContract  {

     public interface  View extends BaseView{

         public  void  openProgressDialog();

         public void  dissmissProgressDialog();

         public void showErrorMsg(String errorMsg);

         public void showData(CheckoutDefaultAddressResponse checkoutDefaultAddressResponse);
     }

     public interface  Presenter extends BasePresenter<View>{
         public void getDefaultAddress();
     }

}

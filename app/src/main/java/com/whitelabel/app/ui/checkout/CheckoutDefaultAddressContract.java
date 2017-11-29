package com.whitelabel.app.ui.checkout;

import android.location.Address;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public interface CheckoutDefaultAddressContract  {

     public interface  View extends BaseView{

         public  void  openProgressDialog();

         public void  dissmissProgressDialog();

         public void hideBillToDifferentLayout();
         public void showErrorMsg(String errorMsg);

         public void showData(AddressBook shippingAddress,
                              AddressBook billingAddress,
                              List<CheckoutDefaultAddressResponse.ShippingMethodBean> shippingMethod,
                              CheckoutDefaultAddressResponse.PickUpAddress pickUpAddress);
     }

     public interface  Presenter extends BasePresenter<View>{
         public void getDefaultAddress();
     }

}

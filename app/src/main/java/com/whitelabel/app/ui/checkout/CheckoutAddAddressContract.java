package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public interface CheckoutAddAddressContract {

     public interface  View extends BaseView{

//         public  void  openProgressDialog();

//         public void  dissmissProgressDialog();

         public void showErrorMsg(String errorMsg);

         public void showData(SVRAppServiceCustomerCountry country);
     }

     public interface  Presenter extends BasePresenter<View>{
         public void getCountryAndRegions();
     }

}

package com.whitelabel.app.ui.common;

import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;
import com.whitelabel.app.ui.checkout.CheckoutDefaultAddressContract;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */

public class BaseAddressContract {
    public interface  View extends BaseView {
       public void  loadData(List<AddressBook> addressBooks);
       public void showNetworkErrorView();
    }
    public interface  Presenter extends BasePresenter<BaseAddressContract.View> {
        public  void  getAddressListCache(String sessionKey);
        public void   getAddressListOnLine(String sessionKey);

    }
}

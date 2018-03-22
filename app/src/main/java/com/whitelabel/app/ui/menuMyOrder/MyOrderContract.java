package com.whitelabel.app.ui.menuMyOrder;

import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**12.
 * Created by Administrator on 2017/7/
 */
public interface MyOrderContract {

    public interface  View extends BaseView{
        public void loadShoppingCount(int count);
        public void showNetErrorMessage();
        public void showFaildMessage(String faildMessage);
        public void showReorderErrorMessage(String errorMsg);
        public void showReorderSuccessMessage(int count);
        void refreshShoppingCartCount(int count);
    }
    public interface  Presenter extends BasePresenter<View>{
        public void getShoppingCount();
        public void saveShoppingCartCount(int num);
        public void setToCheckout(ArrayList<OrderBody> orderBodies);
        public void setToCheckoutDetail(String orderId,List<MyAccountOrderInner> orderBodies);
    }
}

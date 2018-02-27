package com.whitelabel.app.ui.shoppingcart;

import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**12.
 * Created by Administrator on 2017/7/
 */
public interface ShoppingCartVersionContract {

    public interface  View extends BaseView{
        void showUpdateDialog();
        void checkCartStock();
        void showErrorMessage(String errorMessage);
    }
    public interface  Presenter extends BasePresenter<View>{
        void versionCheck();
    }
}

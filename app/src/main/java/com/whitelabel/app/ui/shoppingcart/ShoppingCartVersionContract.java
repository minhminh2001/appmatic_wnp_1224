package com.whitelabel.app.ui.shoppingcart;

import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**
 * 12.
 * Created by Administrator on 2017/7/
 */
public interface ShoppingCartVersionContract {

    interface View extends BaseView {

        void showUpdateDialog();

        void checkCartStock();

        void showErrorMessage(String errorMessage);

        void showDialog();

        void dimissDialog();

        void setLayoutNotHaveProduct();

        void loadData(ShoppingCartListEntityCart shoppingCartListEntityCart);
    }

    interface Presenter extends BasePresenter<View> {

        void versionCheck();

        void getShoppingListFromLocal();

        void deleteItem(String simpleId);

        void updateShoppingItemNumber(String simpleId, String s);
    }
}

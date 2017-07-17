package com.whitelabel.app.ui.shoppingcart;

import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.ui.BaseView;

/**12.
 * Created by Administrator on 2017/7/
 */
public interface ShoppingCartContract {

    public interface  View extends BaseView{
        public void showProgressDialog();
        public void dissmissProgressDialog();
        public void showNetErrorMsg();
        public void showSwipeRefrshLayout();
        public void closeSwipeRefrshLayout();
        public void showEmptyDataLayout();
        public void loadProductsRecyclerView(ShoppingCartListEntityCell[] items);
        public void setBottomPriceView(ShoppingCartListEntityCart entityCart);
        public void setLayoutHaveVercherCode(String vercherCode);
        public void setLayoutNotHaveVercherCode();
        public void hideVercherCodeLayout();
        public void setLayoutHaveProduct();

    }
    public interface  Presenter extends BasePresenter<View>{
        public void loadData(boolean showDialog);

    }
}

package com.whitelabel.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.*;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.NotificationActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;

import static com.whitelabel.app.fragment.HomeBaseFragment.REQUEST_NOTIFICATION;
import static com.whitelabel.app.fragment.HomeBaseFragment.REQUEST_SEARCH;
import static com.whitelabel.app.fragment.HomeBaseFragment.REQUEST_SHOPPINGCART;

/**
 * Created by Arman on 3/3/2017.
 */

public class BaseFragmentSearchCart<T extends BasePresenter> extends com.whitelabel.app.BaseFragment<T> {
    protected String TAG = "BaseFragmentSearchCart";
    protected boolean showSearch = false;//TODO(Aaron):Don't display
    protected boolean showCart = true;
    protected boolean showSearchOptionMenu = true;
    MenuItem cartItem;
    MenuItem searchItem;
    MenuItem notificationItem;

    public void setSearchOptionMenu(boolean enable) {
        showSearchOptionMenu = enable;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (showSearchOptionMenu) {
            inflater.inflate(R.menu.menu_search_shopping_cart, menu);
            initCartMenu(menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (cartItem != null) {
            cartItem.setVisible(showCart);
        }
        if (searchItem != null) {
            searchItem.setVisible(showSearch);
        }

        if(notificationItem != null){
            notificationItem.setVisible(false);
        }

        JLogUtils.e(TAG, showSearch + "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                launchSearch();
                break;
            case R.id.action_shopping_cart:
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                    launchShoppingCart();
                } else {
                    jumpLoginActivity();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void jumpLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
        startActivityForResult(intent, 1000);
        getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().supportInvalidateOptionsMenu();
    }

    private void initCartMenu(Menu menu) {
        searchItem = menu.findItem(R.id.action_search);
        cartItem = menu.findItem(R.id.action_shopping_cart);
        notificationItem = menu.findItem(R.id.action_notification);

        final View cart = cartItem.getActionView();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchShoppingCart();
            }
        });
        View search = searchItem.getActionView();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearch();

            }
        });

        View notification = notificationItem.getActionView();
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNotification();
            }
        });

        ImageView searchIcon = (ImageView) search.findViewById(R.id.iv_img1);
        JViewUtils.setNavBarIconColor(getActivity(), searchIcon, R.drawable.ic_action_search);
        TextView textView = (TextView) cart.findViewById(R.id.ctv_home_shoppingcart_num);
        ImageView imageView = (ImageView) cart.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(), imageView, R.drawable.ic_action_cart);
        textView.setBackground(JImageUtils.getThemeCircle(getActivity()));
        JViewUtils.updateCartCount(textView, getCartItemCount());
    }

    private long getCartItemCount() {
        long cartItemCount = 0;
        try {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        } catch (Exception ex) {
            JLogUtils.e(TAG, ex.getMessage());
        }
        return cartItemCount;
    }

    //TODO: proper anim fix
    protected void launchSearch() {
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
        intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
        startActivityForResult(intent, REQUEST_SEARCH);
    }

    protected void launchShoppingCart() {
        Intent intent = new Intent(getActivity(), ShoppingCartActivity1.class);
        startActivityForResult(intent, REQUEST_SHOPPINGCART);
    }

    protected void launchNotification(){
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        startActivityForResult(intent, REQUEST_NOTIFICATION);
    }
}

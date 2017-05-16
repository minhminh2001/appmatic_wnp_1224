package com.whitelabel.app.fragment;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.*;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;

import static com.whitelabel.app.fragment.HomeBaseFragment.REQUEST_SEARCH;
import static com.whitelabel.app.fragment.HomeBaseFragment.REQUEST_SHOPPINGCART;

/**
 * Created by Arman on 3/3/2017.
 */

public class BaseFragmentSearchCart<T extends BasePresenter> extends com.whitelabel.app.BaseFragment<T> {
    protected String TAG = "BaseFragmentSearchCart";
    protected boolean showSearch = true;
    protected boolean showCart = true;

    MenuItem cartItem;
    MenuItem searchItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shopping_cart, menu);
        initCartMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(cartItem != null) {
            cartItem.setVisible(showCart);
        }
        if(searchItem != null) {
            searchItem.setVisible(showSearch);
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
                launchShoppingCart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().supportInvalidateOptionsMenu();
    }

    private void initCartMenu(Menu menu) {
        searchItem = menu.findItem(R.id.action_search);
        cartItem = menu.findItem(R.id.action_shopping_cart);
        View search=searchItem.getActionView();
        final View cart = cartItem.getActionView();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchShoppingCart();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearch();

            }
        });
        ImageView searchIcon= (ImageView) search.findViewById(R.id.iv_img1);
        JViewUtils.setNavBarIconColor(getActivity(),searchIcon,R.drawable.ic_action_search);
        TextView textView = (TextView) cart.findViewById(R.id.ctv_home_shoppingcart_num);
        ImageView  imageView= (ImageView) cart.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),imageView,R.drawable.ic_action_cart);
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
                } else {
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
        //getActivity().overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        getActivity().overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    protected void launchShoppingCart() {
        Intent intent = new Intent(getActivity(), ShoppingCartActivity1.class);
        startActivityForResult(intent, REQUEST_SHOPPINGCART);
        //getActivity().overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        getActivity().overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }
}

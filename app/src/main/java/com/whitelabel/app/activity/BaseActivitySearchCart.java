package com.whitelabel.app.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;

/**
 * Created by Arman on 3/2/2017.
 */

public class BaseActivitySearchCart<T extends BasePresenter> extends com.whitelabel.app.BaseActivity<T> {
    protected boolean showSearch = true;
    protected boolean showCart = true;
    MenuItem cartItem;
    MenuItem searchItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_shopping_cart, menu);
        initCartMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(cartItem != null) {
            cartItem.setVisible(showCart);
        }
        if(searchItem != null) {
            searchItem.setVisible(showSearch);
        }
        return true;
    }

    private void initCartMenu(Menu menu) {
        searchItem = menu.findItem(R.id.action_search);
        cartItem = menu.findItem(R.id.action_shopping_cart);
        final View cart = cartItem.getActionView();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchShoppingCart();
            }
        });
        TextView textView = (TextView) cart.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(this));
        JViewUtils.updateCartCount(textView, getCartItemCount());
    }

    private long getCartItemCount() {
        long cartItemCount = 0;
        try {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(this);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(this);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        } catch (Exception ex) {
            JLogUtils.e("", ex.getMessage());
        }
        return cartItemCount;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
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
    protected void onStart() {
        super.onStart();
        supportInvalidateOptionsMenu();
    }

    protected void initToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            setToolbarTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    protected void setToolbarTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }

        if(getSupportActionBar() != null) {
            title = JDataUtils.getFirstLetterToUpperCase(title);
            getSupportActionBar().setTitle(title);
        }
    }

    //TODO: proper anim fix
    protected void launchSearch() {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
        intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
        startActivityForResult(intent, REQUEST_SEARCH);
        //overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    protected void launchShoppingCart() {
        Intent intent = new Intent(this, ShoppingCartActivity1.class);
        startActivityForResult(intent, REQUEST_SHOPPINGCART);
        //overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

}

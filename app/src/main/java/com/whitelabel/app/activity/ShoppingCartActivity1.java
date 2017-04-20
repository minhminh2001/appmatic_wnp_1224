package com.whitelabel.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.fragment.ShoppingCartBaseFragment;
import com.whitelabel.app.fragment.ShoppingCartVerticalFragment;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

/**
 * Created by Administrator on 2016/1/26.
 */

public class ShoppingCartActivity1 extends com.whitelabel.app.BaseActivity {
    private final static String TAG = "ShoppingCartActivity1";
    private ShoppingCartBaseFragment fragment;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart1);
        JLogUtils.d(TAG, "onCreate");
        initIntent();
        initToolBar();
        initFragment();
    }
    
    private void initToolBar() {
        setTitle(getResources().getString(R.string.SHOPPINGCART));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        if (fragment != null) {
            fragment.refresh();
        }

    }

    private void initIntent() {
        type = getIntent().getIntExtra("type", 0);
    }

    private void initFragment() {
        fragment = ShoppingCartVerticalFragment.newInstance(ShoppingCartBaseFragment.FROM_OTHER, 0l);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mParent, fragment).commit();
    }


    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }

}

package com.whitelabel.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.ui.common.BasePresenter;
import com.whitelabel.app.ui.common.BaseView;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomButton;

/**
 * Created by Administrator on 2016/10/6.
 */
public class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    //requests
    protected static final int REQUEST_SHOPPINGCART = 10001;
    protected static final int REQUEST_SEARCH = 10002;

    protected boolean isActivityFinished = true;
    protected boolean isActivityInvisible = true;
    protected String currTag;
    private Toolbar mToolbar;
    private RelativeLayout mHomeSearchBarRL;
    private RelativeLayout mTitleRL;
    protected T mPresenter;
    private TextView tvTitleNum, tvTitle;
    private ImageView ivTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavBarBackgroundColor());

        }
        currTag = this.getClass().getSimpleName();
        isActivityFinished = false;
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(color);
        }
    }

    public boolean checkIsFinished() {
        return isActivityFinished;
    }

    public boolean checkIsInvisible() {
        return isActivityInvisible;
    }

    private Dialog mDialog;

    @Override
    public void showProgressDialog() {
        mDialog = JViewUtils.showProgressDialog(this);
    }

    @Override
    public void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void setToolBarColor(int colorId) {
        getToolbar().setBackgroundColor(JToolUtils.getColor(colorId));
    }

    public T getPresenter() {
        return null;
    }


    public void setTitleNum(int num) {
        tvTitleNum.setBackground(JImageUtils.getThemeCircle(this));
        if (num > 0) {
            tvTitleNum.setVisibility(View.VISIBLE);
            if (num <= 99) {
                tvTitleNum.setText(num + "");
            } else {
                tvTitleNum.setText("99+");
            }
        } else {
            tvTitleNum.setVisibility(View.GONE);
        }
    }

    public void setTitle(String text) {
        tvTitleNum.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(text);
        mHomeSearchBarRL.setVisibility(View.GONE);
    }

    public void setTitleImage(int resouce) {
        tvTitleNum.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        ivTitle.setVisibility(View.VISIBLE);
        ivTitle.setImageResource(resouce);
        mHomeSearchBarRL.setVisibility(View.GONE);
    }

    public void setHomeSearchBarClickListener(View.OnClickListener onClickListener) {
        showHomeSearchBar(true);
        mHomeSearchBarRL.setOnClickListener(onClickListener);
    }

    public void showHomeSearchBar(boolean isShow) {
        if (isShow) {
            mTitleRL.setVisibility(View.GONE);
            mHomeSearchBarRL.setVisibility(View.VISIBLE);
        } else {
            mTitleRL.setVisibility(View.VISIBLE);
            mHomeSearchBarRL.setVisibility(View.GONE);
        }
    }
    public void setLeftMenuIcon(int icon) {
        getToolbar().setNavigationIcon(icon);
    }
    public void setLeftMenuIcon(Drawable drawable) {
        getToolbar().setNavigationIcon(drawable);
    }
    public void setLeftMenuClickListener(final View.OnClickListener onClickListener) {
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }
    public View setRightTextMenuClickListener(MenuInflater menuInflater, int menuRes, Menu menu, int menuItemId, int actionViewRes, View.OnClickListener onClickListener) {
        menuInflater.inflate(menuRes, menu);
        MenuItem menuItem = menu.findItem(menuItemId);
        MenuItemCompat.setActionView(menuItem, actionViewRes);
        View view = menuItem.getActionView();
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void updateRightIcon(int iconId, int itemId) {
        MenuItem itemMenu = getToolbar().getMenu().findItem(itemId);
        if (itemMenu != null) {
            View view = itemMenu.getActionView();
            ImageView img = (ImageView) view.findViewById(R.id.iv_img);
            img.setImageDrawable(JToolUtils.getDrawable(iconId));
        }
    }

    public void updateRightIconNum(int itemId, long number) {
        MenuItem itemMenu = getToolbar().getMenu().findItem(itemId);
        if (itemMenu != null) {
            View view = itemMenu.getActionView();
            TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
            if (number > 0) {
                if (number > 99) {
                    textView.setText(getResources().getString(R.string.nine));
                } else {
                    textView.setText(number + "");
                }
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mHomeSearchBarRL = (RelativeLayout) findViewById(R.id.rl_home_searchBar);
        if(mHomeSearchBarRL!=null){
            mHomeSearchBarRL.setBackgroundColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getSearchBoxBackgroundColor());
        }
        mTitleRL = (RelativeLayout) findViewById(R.id.rl_home_searchBar);
        tvTitleNum = (TextView) findViewById(R.id.tv_title_num);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        TextView tvSearch = (TextView) findViewById(R.id.tv_search);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if(tvSearch !=null){
            tvSearch.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getSearchBoxTextColor());
        }
        if(tvTitle!=null) {
            JViewUtils.setNavBarTextColor(tvTitle);
        }
        ImageView ivTryAgain= (ImageView) findViewById(R.id.iv_try_again);
        CustomButton  btnAgain= (CustomButton) findViewById(R.id.btn_try_again);
        if(ivTryAgain!=null&&btnAgain!=null){
            ivTryAgain.setImageDrawable(JImageUtils.getThemeIcon(this,R.mipmap.connection_break_loading));
            btnAgain.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        }
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }
        if(getToolbar()!=null){
            getToolbar().setBackgroundColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavBarBackgroundColor());
        }
    }
    public Toolbar getToolbar() {
        return mToolbar;
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }
    public void transitionOnBackPressed() {
        super.onBackPressed();
    }
    public void startNextActivity(Bundle bundle, Class<?> pClass, boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft,
                R.anim.exit_righttoleft);
        if (finishFlag) {
            super.finish();
        }
    }
    public void startNextActivityForResult(Bundle bundle, Class<?> pClass, int requestCode, boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter_righttoleft,
                R.anim.exit_righttoleft);
        if (finishFlag) {
            super.finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        isActivityInvisible = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityInvisible = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityFinished = true;
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public void BottomtoTopActivity(Bundle bundle, Class<?> pClass, boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_bottom_top,
                R.anim.exit_bottom_top);
        if (finishFlag) {
            super.finish();
        }
    }
}

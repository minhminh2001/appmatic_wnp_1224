package com.whitelabel.app.module;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.common.utils.DialogUtils;
import com.whitelabel.app.R;

/**
 * Created by ray on 2017/3/20.
 */

public  abstract class BaseActivity <T extends BasePresenter> extends AppCompatActivity implements BaseView{
    public  T  mPresenter ;
    private Toolbar mToolbar;
    private Dialog mDialog;
    public void showProgressDialog(){
        mDialog= DialogUtils.showProgressDialog(this);
    }

    public void hideProgressDialog(){
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        mPresenter=getPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }
    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    protected  void initToolbar(){
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    protected   abstract T  getPresenter();
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }
    @Override
    public void setTitle(CharSequence title) {
        if(TextUtils.isEmpty(title)){
            title="";
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(title);
        }
    }


    public void setLeftMenuIconAndListener(int icon , final View.OnClickListener onClickListener){
        getToolbar().setNavigationIcon(icon);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });
    }



    public Toolbar getToolbar(){
        return mToolbar;
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

    public void startNextActivityForResult(Bundle bundle, Class<?> pClass, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.enter_righttoleft,
                R.anim.exit_righttoleft);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
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

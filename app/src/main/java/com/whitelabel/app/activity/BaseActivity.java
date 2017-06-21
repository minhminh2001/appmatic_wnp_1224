package com.whitelabel.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.facebook.appevents.AppEventsLogger;
import com.whitelabel.app.R;


/**
 * Created by imaginato on 2015/6/10.
 */
public  class BaseActivity extends FragmentActivity {
    protected boolean isActivityFinished = true;
    protected boolean isActivityInvisible = true;
    protected boolean isActivityPaused = true;
    private DisplayMetrics dm = new DisplayMetrics();

    public boolean checkIsFinished() {
        return isActivityFinished;
    }

    public boolean checkIsInvisible() {
        return isActivityInvisible;
    }

    public boolean checkIsPaused() {
        return isActivityPaused;
    }

    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(layoutResID);
    }

    /**
     * get display width
     */
    public int getDisplayWidth() {
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }

    /**
     * get display height
     */
    public int getDisplayHeight() {
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * override super method in order to add animation effect
     */
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_transition_enter_lefttoright, R.anim.activity_transition_exit_lefttoright);
    }

    /**
     * startActivity
     * When the last param is true, system will finish current activity
     */
    public void startNextActivity(Bundle bundle, Class<?> pClass, boolean finishFlag) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.activity_transition_enter_righttoleft,
                R.anim.activity_transition_exit_righttoleft);
        if (finishFlag) {
            super.finish();
        }
    }

    /**
     * startActivityForResult
     */
    public void startNextActivityForResult(Bundle bundle, Class<?> pClass,
                                           int resquestCode) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, resquestCode);
        overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
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

    public void ToptoBottom() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isActivityFinished = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            AppEventsLogger.activateApp(this);
        }catch (Exception ex){
            ex.getMessage();
        }
        isActivityInvisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            AppEventsLogger.deactivateApp(this);
        }catch (Exception ex){
            ex.getMessage();
        }
        isActivityPaused = true;
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
    }


}

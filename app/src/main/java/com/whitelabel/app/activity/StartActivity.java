package com.whitelabel.app.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.whitelabel.app.R;
import com.whitelabel.app.callback.INITCallback;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.handler.INITApp;
import com.whitelabel.app.task.INITExecutor;
import com.whitelabel.app.ui.productlist.ProductListFilterFragmentV2;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/6/10.
 */
public class StartActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {

    public static final int DELAY_TIME = 1000;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String TAG = "StartActivity";

    private long mStartTimeLong;

    private INITApp mCallback;

    private String mSessionKey;

    private boolean mSplashScreen;

    private Dialog mProgressDialog;

    private String mNetworkErrorMsg;

    private boolean existVending;

    private StartHandler mStartHandler;

    private String currTag = "StartActivity";

    public void startNextActivity() {
        if (mSessionKey != null && mSessionKey.length() != 0) {
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(StartActivity.this, LoginRegisterActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("Activity", "start");//压入数据
            intent.putExtras(mBundle);
            startActivity(intent);
            finish();
        }
    }

    private void postDelayed(long deploy) {
        StartRunnable startRunnable = new StartRunnable(StartActivity.this);
        mStartHandler.postDelayed(startRunnable, (DELAY_TIME - deploy));
    }

    private void gaTrackNotificationSwitch() {
        boolean isNotificationEnabled = JToolUtils.isNotificationEnabled(this);
        String trackLabel = "";
        if (isNotificationEnabled) {
            trackLabel = "Enabled";
        } else {
            trackLabel = "Disabled";
        }
        //缓存和当前状态是否一样，不一样则track
        String cacheState = JStorageUtils.getNotificaitionState(this);
        if (!trackLabel.equals(cacheState)) {
            JStorageUtils.saveNotificaitionState(this, trackLabel);
            GaTrackHelper.getInstance().googleAnalyticsEvent("Notification",
                "Enable Push Notification for GEMFIVE ",
                trackLabel,
                null);
            JLogUtils.i("googleGA", "Receive Notification switch");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.relative_container, ProductListFilterFragmentV2.newInstance()).commit();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (checkInstallationPlayServices()) {
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean checkInstallationPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = apiAvailability
                        .getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                    mProgressDialog.show();
                }
            } else {
                JLogUtils.i(TAG, "This device is not supported.");
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (mCallback != null) {
            INITExecutor.getInstance().clearTask(mCallback);
        }
        super.onDestroy();
    }

    static class StartHandler extends Handler {

        private WeakReference<StartActivity> activity;

        public StartHandler(StartActivity startActivity) {
            activity = new WeakReference<StartActivity>(startActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activity.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_CHECKVERSION:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    static class StartRunnable implements Runnable {

        WeakReference<StartActivity> mActivity;

        public StartRunnable(StartActivity start) {
            mActivity = new WeakReference<>(start);
        }

        @Override
        public void run() {
            if (mActivity.get() == null) return;
            mActivity.get().startNextActivity();
        }
    }

    static class MeInitCallBack extends INITCallback {

        WeakReference<StartActivity> mStartActivity;

        public MeInitCallBack(StartActivity startActivity) {
            mStartActivity = new WeakReference<>(startActivity);
        }

        @Override
        public void onSuccess(int resultCode, Object object) {
            if (mStartActivity.get() == null) return;
//            mStartActivity.get().checkAppVersion();
            delayStart();
        }

        @Override
        public void onFailure(int resultCode, Object object) {
            if (mStartActivity.get() == null) return;
//            mStartActivity.get().checkAppVersion();
            delayStart();
        }

        private void delayStart() {
            long deploy = System.currentTimeMillis() - mStartActivity.get().mStartTimeLong;
            if (deploy < DELAY_TIME) {
                mStartActivity.get().postDelayed(deploy);
            } else {
                mStartActivity.get().startNextActivity();
            }
        }
    }
}


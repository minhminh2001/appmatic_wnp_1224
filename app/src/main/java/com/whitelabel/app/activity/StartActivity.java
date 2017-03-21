package com.whitelabel.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.INITCallback;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.handler.INITApp;
import com.whitelabel.app.task.INITExecutor;
import com.whitelabel.app.utils.JLogUtils;

import java.lang.ref.WeakReference;



/**
 * Created by imaginato on 2015/6/10.
 */
public class StartActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener{
    public static final int DELAY_TIME = 1000;
    private long mStartTimeLong;
    private String mSessionKey;
    private  boolean mSplashScreen;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "StartActivity";
    private Dialog mProgressDialog;
    private String mNetworkErrorMsg;
    private boolean existVending;
    private StartHandler mStartHandler;
    private String currTag="StartActivity";
    public void startNextActivity(){
        if (mSessionKey != null && mSessionKey.length() != 0) {
            Intent intent = new Intent(StartActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
//            if(mSplashScreen){
                Intent intent = new Intent(StartActivity.this, LoginRegisterActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("Activity", "start");//压入数据
                intent.putExtras(mBundle);
                startActivity(intent);
                finish();
//            }else{
//                startNextActivity(null, SplashScreenActivity.class, true);
//            }
        }
    }

    static class StartHandler extends  Handler{
        private  WeakReference<StartActivity>  activity;
        public StartHandler(StartActivity startActivity){
            activity=new WeakReference<StartActivity>(startActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            if(activity.get()==null){
                return;
            }
            switch (msg.what){
                case ProductDao.REQUEST_CHECKVERSION:
                    if(msg.arg1 == ProductDao.RESPONSE_SUCCESS){
//                        long deploy=System.currentTimeMillis()-activity.get().mStartTimeLong;
//                        if(deploy<3000){
//                            activity.get(). postDelayed(deploy);
//                        }else{
//                            activity.get().startNextActivity();
//                        }
//                    }else{
//                        String title=activity.get().getResources().getString(R.string.versionCheckTitle);
//                        String hintmsg=activity.get().getResources().getString(R.string.versionCheckMsg);
//                        String btnMsg=activity.get().getResources().getString(R.string.update);
//                        JViewUtils.showMaterialDialog(activity.get(), title, hintmsg, btnMsg,activity.get().updateListener,false);
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
//                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(activity.get());
//                    requestErrorHelper.showNetWorkErrorToast(msg);
//                    long deploy=System.currentTimeMillis()-activity.get().mStartTimeLong;
//                    if(deploy<3000) {
//                        activity.get(). postDelayed(deploy);
//                    }else{
//                        activity.get(). startNextActivity();
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }


    static  class StartRunnable implements   Runnable{
        WeakReference<StartActivity> mActivity;
        public StartRunnable(StartActivity start){
            mActivity=new WeakReference<>(start);
        }
        @Override
        public void run() {
            if(mActivity.get()==null)return;
            mActivity.get().startNextActivity();
        }
    }

    private void postDelayed(long deploy) {
        StartRunnable  startRunnable=  new StartRunnable(StartActivity.this);
        mStartHandler.postDelayed(startRunnable, (DELAY_TIME -deploy));
    }

//    private View.OnClickListener updateListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
//            for(int i=0;i<packages.size();i++) {
//                PackageInfo packageInfo = packages.get(i);
//                String packgeName="";
//                packgeName=packageInfo.packageName;
//                JLogUtils.i("Allen","packge="+packgeName);
//                if(packgeName.contains("vending")){
//                    //跳转进市场搜索的代码
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(GlobalData.jumpMarketUrl));
//                    startActivity(intent);
//                    existVending=true;
//                }
//            }
//            if(!existVending){
//                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.whitelabel.app");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
//                existVending=false;
//            }
//        }
//    };
    private INITApp mCallback;

    private void gaTrackNotificationSwitch() {
//        boolean isNotificationEnabled = JToolUtils.isNotificationEnabled(this);
//        String trackLabel="";
//        if (isNotificationEnabled) {
//            trackLabel = "Enabled";
//        } else {
//            trackLabel = "Disabled";
//        }
//       //缓存和当前状态是否一样，不一样则track
//        String cacheState=JStorageUtils.getNotificaitionState(this);
//        if(!trackLabel.equals(cacheState)){
//            JStorageUtils.saveNotificaitionState(this,trackLabel);
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Notification",
//                    "Enable Push Notification for GEMFIVE ",
//                    trackLabel,
//                    null);
//            JLogUtils.i("googleGA", "Receive Notification switch");
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
//        gaTrackNotificationSwitch();
        mStartTimeLong = System.currentTimeMillis();
        mNetworkErrorMsg =getResources().getString(R.string.productlist_list_prompt_error_nointernet);
//        mSplashScreen = JStorageUtils.getToSplashScreenMark(StartActivity.this);
        mStartHandler=new StartHandler(this);
        if(GemfiveApplication.getAppConfiguration().isSignIn(StartActivity.this)) {
            mSessionKey = GemfiveApplication.getAppConfiguration().getUser().getSessionKey();
        }
        mCallback=new INITApp(StartActivity.this, new MeInitCallBack(this));
        INITExecutor.getInstance().execute(mCallback);

    }



    static class MeInitCallBack extends   INITCallback{
        WeakReference<StartActivity> mStartActivity;
        public MeInitCallBack(StartActivity startActivity){
                mStartActivity=new WeakReference<StartActivity>(startActivity);
        }

        @Override
        public void onSuccess(int resultCode, Object object) {
            if(mStartActivity.get()==null)return;
//            mStartActivity.get().checkAppVersion();
            delayStart();
        }

        @Override
        public void onFailure(int resultCode, Object object) {
            if(mStartActivity.get()==null)return;
//            mStartActivity.get().checkAppVersion();
            delayStart();
        }
        private void delayStart() {
            long deploy=System.currentTimeMillis()-mStartActivity.get().mStartTimeLong;
            if(deploy<DELAY_TIME){
                mStartActivity.get(). postDelayed(deploy);
            }else{
                mStartActivity.get().startNextActivity();
            }
        }
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
                if(mProgressDialog ==null||!mProgressDialog.isShowing()) {
                    mProgressDialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
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
        if(mProgressDialog !=null){
            mProgressDialog.dismiss();
        }
        if(mCallback!=null){
            INITExecutor.getInstance().clearTask(mCallback);
        }
        super.onDestroy();
    }
}


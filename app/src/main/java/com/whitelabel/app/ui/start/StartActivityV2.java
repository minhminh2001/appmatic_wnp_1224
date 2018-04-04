package com.whitelabel.app.ui.start;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.utils.DialogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.adapter.GuidePagerAdapter;
import com.whitelabel.app.callback.INITCallback;
import com.whitelabel.app.handler.INITApp;
import com.whitelabel.app.notification.RegistrationIntentService;
import com.whitelabel.app.task.INITExecutor;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.MaterialDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by imaginato on 2015/6/10.
 */
public class StartActivityV2 extends BaseActivity<StartContract.Presenter> implements StartContract.View {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int DELAY_TIME = 2000;
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    @BindView(R.id.start_logo_imageview)
    ImageView startLogoImageview;
    @BindView(R.id.btn_guide_one_skip_to_next_page)
    Button btnGuideOneSkipToNextPage;
    @BindView(R.id.btn_guide_two_skip_to_next_page)
    Button btnGuideTwoSkipToNextPage;
    @BindView(R.id.layout_maintenance_view)
    ConstraintLayout layoutMaintenanceView;

    private INITApp mCallback;
    private Dialog mProgressDialog;

    public void startGuidePage() {
        if (mPresenter.isGuide()){
            startNextActvity();
        }else {
            startLogoImageview.setVisibility(View.GONE);
        }

    }

    private void startNextActvity() {
//        if (WhiteLabelApplication.getAppConfiguration().isSignIn(StartActivityV2.this)) {
        Intent intent = new Intent(StartActivityV2.this, HomeActivity.class);
        startActivity(intent);
        finish();
//        } else {
//            Intent intent = new Intent(StartActivityV2.this, LoginRegisterActivity.class);
//            Bundle mBundle = new Bundle();
//            mBundle.putString("Activity", "start");//压入数据
//            intent.putExtras(mBundle);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void startIntentService() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    @Override
    public void showUpdateDialog() {
        JViewUtils.showUpdateGooglePlayStoreDialog(this);
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        JViewUtils.showErrorToast(this, errorMsg);
    }

    @Override
    public void showProgressDialog(){
        mProgressDialog = DialogUtils.showProgressDialog(this);
    }

    @Override
    public void hideProgressDialog(){
        if(mProgressDialog == null){
            return;
        }

        mProgressDialog.dismiss();
    }

    @Override
    public void onServerAvailable() {
        hideProgressDialog();
        showMaintenanceView(false);

        initGuide();
        mPresenter.getSearchCategory();
    }

    @Override
    public void showMaintenancePage() {
        hideProgressDialog();
        showMaintenanceView(true);
    }

    @OnClick(R.id.btn_refresh)
    public void onClick(View view){
        showProgressDialog();
        mPresenter.getConfigInfo("", "");
    }

    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(this)).build().inject(this);
    }

    @OnClick({R.id.btn_guide_one_skip_to_next_page, R.id.btn_guide_two_skip_to_next_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_guide_one_skip_to_next_page:
            case R.id.btn_guide_two_skip_to_next_page:
                startNextActvity();
                mPresenter.saveGuideFlag(true);
                break;
        }
    }

    static class StartRunnable implements Runnable {
        WeakReference<StartActivityV2> mActivity;

        public StartRunnable(StartActivityV2 start) {
            mActivity = new WeakReference<>(start);
        }

        @Override
        public void run() {
            if (mActivity.get() == null) return;
            mActivity.get().startGuidePage();
        }
    }

    public void postDelayed(long deploy) {
        StartRunnable startRunnable = new StartRunnable(StartActivityV2.this);
        new Handler().postDelayed(startRunnable, (DELAY_TIME - deploy));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
//        Fabric.with(this, new Crashlytics());
        mCallback = new INITApp(StartActivityV2.this, new MeInitCallBack(this));
        INITExecutor.getInstance().execute(mCallback);
        mPresenter.setStartTime();
        mPresenter.getConfigInfo("", "");

    }

    private void initGuide(){
        List<Integer> list=new ArrayList<>();
        list.add(R.drawable.ic_guide_one);
        list.add(R.drawable.ic_guide_two);
        vpGuide.setAdapter(new GuidePagerAdapter(StartActivityV2.this,list));
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (0==position){
                    btnGuideOneSkipToNextPage.setVisibility(View.VISIBLE);
                    btnGuideTwoSkipToNextPage.setVisibility(View.GONE);
                }else if (1==position){
                    btnGuideOneSkipToNextPage.setVisibility(View.GONE);
                    btnGuideTwoSkipToNextPage.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class MeInitCallBack extends INITCallback {
        WeakReference<StartActivityV2> mStartActivity;

        public MeInitCallBack(StartActivityV2 startActivity) {
            mStartActivity = new WeakReference<>(startActivity);
        }

        @Override
        public void onSuccess(int resultCode, Object object) {
            if (mStartActivity.get() == null) return;
        }

        @Override
        public void onFailure(int resultCode, Object object) {
            if (mStartActivity.get() == null) return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (checkInstallationPlayServices()) {
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//       }

    }

    private boolean checkInstallationPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                    mProgressDialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                    mProgressDialog.show();
                }
            } else {
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
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (mCallback != null) {
            INITExecutor.getInstance().clearTask(mCallback);
        }
        super.onDestroy();
    }

    private void showMaintenanceView(boolean isShow){
        layoutMaintenanceView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}


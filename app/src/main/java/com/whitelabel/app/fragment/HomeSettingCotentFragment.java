package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.utils.AppUtils;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomMyDialog;
import com.whitelabel.app.widget.MultiSwitchButton;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/8/21.
 */
public class HomeSettingCotentFragment extends HomeBaseFragment implements View.OnClickListener{
    private Activity homeActivity;
    private View view;
    private LinearLayout llSearch;
    private RelativeLayout rlSettingRate,rlBack;
    private TextView textView_cancle;
    private TextView sign_out,mVersion;
    private Dialog mDialog;
    private  boolean signing=false;
    private OtherDao mOtherDao;
    private String TAG;
    private MultiSwitchButton sbClosedSound,switchButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivity= (Activity) activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_settingscontent,null);
        return view;
    }

    private void setAppVersionName(TextView version) {
        if(!TextUtils.isEmpty(AppUtils.getAppVersionName(homeActivity))){
            version.setText(" "+ AppUtils.getAppVersionName(homeActivity));
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mCommonCallback!=null) {
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_SETTING);
            mCommonCallback.getToolBar().getMenu().clear();
            mCommonCallback.setTitle(getResources().getString(R.string.settings));
        }
        llSearch = (LinearLayout)view.findViewById(R.id.home_search);
        textView_cancle= (TextView) view.findViewById(R.id.home_search_cancel);
        TAG=this.getClass().getSimpleName();
        rlSettingRate=(RelativeLayout)view.findViewById(R.id.rl_setting_rate);
        rlSettingRate.setOnClickListener(this);
        mVersion= (TextView) view.findViewById(R.id.tv_setting_version_name);
        setAppVersionName(mVersion);
        view.findViewById(R.id.rl_sound).setVisibility(View.GONE);
        textView_cancle.setOnClickListener(this);
        sign_out= (TextView) view.findViewById(R.id.sign_out);
        sign_out.setBackground(JImageUtils.getbuttonBakcgroundStrokeDrawable(getActivity()));
        sign_out.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        sign_out.setOnClickListener(this);
        rlBack= (RelativeLayout) view.findViewById(R.id.rl_back);
        rlBack.setOnClickListener(this);
        switchButton= (MultiSwitchButton) view.findViewById(R.id.swithch_button1);
        int kai=0;
        if(GemfiveApplication.getAppConfiguration().isSignIn(homeActivity)){
            kai=GemfiveApplication.getAppConfiguration().getUser().getNewsletterSubscribed();
        }
        if(kai==1){
            switchButton.setCheckedImmediately(true);
        }else{
            switchButton.setCheckedImmediately(false);
        }
        dataHandler=new DataHandler(homeActivity,this);
        mOtherDao=new OtherDao(TAG,dataHandler);
        sbClosedSound= (MultiSwitchButton) view.findViewById(R.id.sb_close_sound);
        if(GemfiveApplication.getInstance().getAppConfiguration().isSignIn(getActivity())) {
            sbClosedSound.setCheckedImmediately(GemfiveApplication.getInstance().getAppConfiguration().getUser().isClosedSound());
        }

        sbClosedSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (GemfiveApplication.getInstance().getAppConfiguration().isSignIn(getActivity())&&getActivity()!=null){
                    GemfiveApplication.getInstance().getAppConfiguration().getUser().setClosedSound(isChecked);
                    GemfiveApplication.getInstance().getAppConfiguration().updateDate(getActivity(),GemfiveApplication.getInstance().getAppConfiguration().getUser());
                }
            }
        });
//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                String newsletterSubscribed;
//                if(GemfiveApplication.getAppConfiguration().isSignIn(homeActivity)) {
//                    String session_key = GemfiveApplication.getAppConfiguration().getUser().getSessionKey();
//                    if (isChecked) {
//                        GemfiveApplication.getAppConfiguration().getUser().setNewsletterSubscribed(1);
//                        GemfiveApplication.getAppConfiguration().updateDate(homeActivity, GemfiveApplication.getAppConfiguration().getUser());
//                        newsletterSubscribed = "1";
//                    } else {
//                        GemfiveApplication.getAppConfiguration().getUser().setNewsletterSubscribed(0);
//                        newsletterSubscribed = "0";
//                        GemfiveApplication.getAppConfiguration().updateDate(homeActivity, GemfiveApplication.getAppConfiguration().getUser());
//                    }
//                    mOtherDao.changeSubscribed(session_key,newsletterSubscribed);
//                }
//            }
//        });
    //    SharedPreferences share=settingsActivity.getSharedPreferences("session_key",Activity.MODE_PRIVATE);
    }

    public static  final  int CODE=8000;

    private DataHandler dataHandler;
    public  static final class DataHandler extends Handler{
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeSettingCotentFragment> mFragment;

        public DataHandler(Activity activity,HomeSettingCotentFragment fragment){
                mActivity=new WeakReference<Activity>(activity);
                mFragment=new WeakReference<HomeSettingCotentFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            switch (msg.what){
                case OtherDao.REQUEST_SUBSCRIBER:
//                    if(msg.arg1==OtherDao.RESPONSE_SUCCESS){
//                    }else{
//                        String errorMsg= (String) msg.obj;
//                        if(!JToolUtils.expireHandler(mActivity.get(),errorMsg,1000)){
//                            Toast.makeText(mActivity.get(),errorMsg+"",Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    break;
                case OtherDao.REQUEST_LOGOUT:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    //用户退出后，将不再显示AppRate
                    JStorageUtils.notShowAppRate(mActivity.get());
                    if(msg.arg1==OtherDao.RESPONSE_SUCCESS){
                        try {
                            GaTrackHelper.getInstance().googleAnalyticsEvent("Account Action",
                                    "Sign Out",
                                    null,
                                    Long.valueOf(mFragment.get().CustomerId));
                        }catch (Exception ex){
                            ex.getStackTrace();
                        }
                        try{
                            FirebaseEventUtils.getInstance().customizedSignOut(mActivity.get(), GemfiveApplication.getAppConfiguration().getUserInfo(mActivity.get()).getLoginType());
                        }catch (Exception ex){
                            ex.getMessage();
                        }
                        GemfiveApplication.getAppConfiguration().signOut(mActivity.get());
//                        SVRAppServiceCustomerSignOut signOutEntity = (SVRAppServiceCustomerSignOut) result;
                        Intent intent = new Intent(mActivity.get(), LoginRegisterActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Activity", "start");
                        intent.putExtras(bundle);
                        mActivity.get().startActivityForResult(intent, CODE);
                        mActivity.get().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
                        mFragment.get().signing = false;
                        mActivity.get().finish();

                        JLogUtils.i("googleGA", "Sign Out");
                    }else{
                              String errorMsg= (String) msg.obj;
                              mFragment.get().signing = false;
                              if ((!JToolUtils.expireHandler(mActivity.get(),errorMsg,1000))) {
                                  Toast.makeText(mActivity.get(),errorMsg+"",Toast.LENGTH_LONG).show();
                              }
                    }
                    break;
                case OtherDao.REQUEST_ERROR:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    mFragment.get().signing = false;
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;

            }

            super.handleMessage(msg);
        }
    }

    private void showSignOutDialogPrompt(){
        CustomMyDialog.Builder builder = new CustomMyDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.are_you_sure_you_want_signout));
        builder.setPositiveButton(getString(R.string.yes_upp), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CustomerId = GemfiveApplication.getAppConfiguration().getUser().getId();
                signing = true;
                mDialog = JViewUtils.showProgressDialog(homeActivity);
                mOtherDao.signOut(GemfiveApplication.getPhoneConfiguration().getRegistrationToken(), GemfiveApplication.getAppConfiguration().getUser().getSessionKey());
            }
        });
        builder.setNegativeButton(getString(R.string.no_upp),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mDialog = builder.create();
        Window win = mDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        mDialog.setCancelable(false);
        win.setWindowAnimations(R.style.dialogAnim);
        mDialog.show();
    }
    private String CustomerId;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_setting_rate:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.play_store_url)));
                startActivity(i);
                break;
            case R.id.sign_out:
                if(!signing&&GemfiveApplication.getAppConfiguration().getUser()!=null) {
                    showSignOutDialogPrompt();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        int kai= 0;
//        if(GemfiveApplication.getAppConfiguration().isSignIn(homeActivity)) {
//            kai = GemfiveApplication.getAppConfiguration().getUser().getNewsletterSubscribed();
//        }
//        if(kai == 1) {
//            switchButton.setChecked(true);
//        } else {
//            switchButton.setChecked(false);
//        }
              //   GemfiveApplication.getAppConfiguration().getUser().setNewsletterSubscribed(0);

//        if(!GemfiveApplication.getAppConfiguration().isSignIn(homeActivity)){
//            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    homeActivity.switchHomeFragment(null);//切换到Home
//
//                                }
//                            },300);
//        }

    }
    @Override
    public void onStart() {
        super.onStart();

//        EasyTracker easyTracker = EasyTracker.getInstance(homeActivity);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Settings Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
        GaTrackHelper.getInstance().googleAnalytics("Settings Screen", homeActivity);
//        JLogUtils.i("googleGA_screen", "Settings Screen");
    }
}

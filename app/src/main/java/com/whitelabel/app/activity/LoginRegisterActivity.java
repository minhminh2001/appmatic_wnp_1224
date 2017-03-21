package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.fragment.LoginRegisterEmailBoundFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailRegisterFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailSendFragment;
import com.whitelabel.app.fragment.LoginRegisterEmailSendSuccessFragment;
import com.whitelabel.app.fragment.LoginRegisterRegisterSuccessFragment;
import com.whitelabel.app.fragment.LoginRegisterResetPassFragment;
import com.whitelabel.app.model.FBGraphAPIUserEntity;
import com.whitelabel.app.widget.CustomMyDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/6/10.
 */

public class LoginRegisterActivity extends com.whitelabel.app.BaseActivity implements ToolBarFragmentCallback {

    public static final int REQUESTCODE_LOGIN = 1000;
    public static final int BACKAPP = -1;//退出
    public static final int EMAILLOGIN_FLAG = 0;//登录
    public static final int EMAILREGISTER_FLAG = 1;//注册
    public static final int FORGOTPASSWORD_FLAG = 2;//忘记密码
    public static final int SENDEMAIL_FLAG = 3;//发送邮件
    public static final int SENDESUCCESS_FLAG = 4;//发送邮件成功
    public static final int REGISTERSUCCESS_FLAG = 5;//注册成功
    public static final int EMAIL_BOUND = 6;//facebook绑定邮箱
    public FBGraphAPIUserEntity fbGraphAPIUserEntity;
    private ArrayList<Fragment> attachedFragmentArray;//存放顺序固定
    private String subEmail = "";
    public boolean fromStart = false;
    //addToWish and productId  use to login success add product to wish now.
    public boolean addToWish = false;
    public String productId = "";

    public String getSubEmail() {
        return subEmail;
    }

    public void setSubEmail(String subEmail) {
        this.subEmail = subEmail;
    }

    private boolean emailConfirm = true;

    public boolean isEmailConfirm() {
        return emailConfirm;
    }

    public void setEmailConfirm(boolean emailConfirm) {
        this.emailConfirm = emailConfirm;
    }

    private String myEmail = "";

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityFinished = true;
    }

    public boolean updateService = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (updateService) {
                fromStart = false;//强制退出App
                LoginRegisterActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean isActivityFinished = false;

    public boolean checkIsFinished() {
        return isActivityFinished;
    }


    @Override
    public void setToolBarLeftIconAndListenter(Drawable drawable, View.OnClickListener onClickListenter) {
        setLeftMenuIcon(drawable);
        setLeftMenuClickListener(onClickListenter);

    }

    @Override
    public void showToolBar(Boolean show) {

    }

    @Override
    public void setToolBarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void updateRightIconNum(int itemId, long number) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragmentTag == EMAILLOGIN_FLAG && attachedFragmentArray.get(0) instanceof LoginRegisterEmailLoginFragment) {
            LoginRegisterEmailLoginFragment fragment = (LoginRegisterEmailLoginFragment) attachedFragmentArray.get(0);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<Activity> activity;

        public DataHandler(Activity fragment) {
            activity = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activity.get() == null) {
                return;
            }
            switch (msg.what) {
                case NotificationDao.REQUEST_NOTIFICATION_COUNT:
//                    if (msg.arg1 == NotificationDao.RESPONSE_SUCCESS) {
//                        Integer integer = (Integer) msg.obj;
//                        if (integer > 0) {
//                            BadgeUtils.setBadge(activity.get().getApplicationContext(), integer);
//                        } else {
//                            BadgeUtils.clearBadge(activity.get().getApplicationContext());
//
//                        }
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private NotificationDao mDao;
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);
        initFragmentData();//初始化所有的Fragment

        //登录
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFilterSortContainer, attachedFragmentArray.get(0));
        transaction.commit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fromActivit = bundle.getString("Activity");
            if ("start".equals(fromActivit)) {
                fromStart = true;
            }
            //if session expire, we go to home when the user cancel/back
            boolean expired = bundle.getBoolean("expire");
            if (expired) {
                fromStart = true;
            }

            // this activity from click wish icon
            addToWish = bundle.getBoolean("addToWish", false);
            if (addToWish == true) {
                productId = bundle.getString("productId");
                if (TextUtils.isEmpty(productId)) {
                    addToWish = false;
                }
            }
        }
        InitMyDialog();
    }


    private void initFragmentData() {
        if (attachedFragmentArray != null) {
            attachedFragmentArray.clear();
        }
        attachedFragmentArray = new ArrayList<Fragment>();
        // 0
        LoginRegisterEmailLoginFragment loginFragment = new LoginRegisterEmailLoginFragment();
        attachedFragmentArray.add(loginFragment);
        // 1
        LoginRegisterEmailRegisterFragment RegisterFragment = new LoginRegisterEmailRegisterFragment();
        attachedFragmentArray.add(RegisterFragment);
        //2
        LoginRegisterResetPassFragment ResetPassFragment = new LoginRegisterResetPassFragment();
        attachedFragmentArray.add(ResetPassFragment);
        //3
        LoginRegisterEmailSendFragment emailSendFragment = new LoginRegisterEmailSendFragment();
        attachedFragmentArray.add(emailSendFragment);
        //4
        LoginRegisterEmailSendSuccessFragment sendSuccessFragment = new LoginRegisterEmailSendSuccessFragment();
        attachedFragmentArray.add(sendSuccessFragment);
        //5
        LoginRegisterRegisterSuccessFragment successFragment = new LoginRegisterRegisterSuccessFragment();
        attachedFragmentArray.add(successFragment);
        //6
        LoginRegisterEmailBoundFragment emailBoundFragment = new LoginRegisterEmailBoundFragment();
        attachedFragmentArray.add(emailBoundFragment);
    }

    private int currentFragmentTag = EMAILLOGIN_FLAG;

    public void redirectToAttachedFragment(int to, int type) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment subFragment = attachedFragmentArray.get(to);

        if (type == 1) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
//            fragmentTransaction.setCustomAnimations(
//                    R.animator.fragment_slide_left_enter,
//                    R.animator.fragment_slide_right_exit
//
//            );

            //系统动画
            //  fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        } else if (type == -1) {
            fragmentTransaction.setCustomAnimations(R.anim.return_slide_in_left, R.anim.return_slide_out_right);
//                fragmentTransaction.setCustomAnimations(
//                    R.animator.fragment_slide_right_enter,
//                    R.animator.fragment_slide_left_exit
//                   );
        } else if (type == -2) {
//            fragmentTransaction.setCustomAnimations(
//                    R.animator.fragment_slide_zero_enter,
//                    R.animator.fragment_slide_bottom_exit
//            );
        }
        currentFragmentTag = to;
        fragmentTransaction.replace(R.id.flFilterSortContainer, subFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private Handler handler = new Handler();
    boolean isBacking;

    @Override
    public void onBackPressed() {
        try {
            if (!isBacking) {
                isBacking = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBacking = false;
                    }
                }, 500);
                int backToFragment = backToFragment();

                if ((backToFragment == BACKAPP || currentFragmentTag == EMAILLOGIN_FLAG) && fromStart) {
                    Intent i = new Intent(LoginRegisterActivity.this, HomeActivity.class);
                    startActivity(i);
                    LoginRegisterActivity.this.finish();
                    LoginRegisterActivity.this.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
                } else if (backToFragment == BACKAPP || currentFragmentTag == EMAILLOGIN_FLAG) {
                    finish();
//                    LoginRegisterActivity.super.onBackPressed();
                    overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
                } else {
                    redirectToAttachedFragment(backToFragment, -1);
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }

    }

    private int backToFragment() {
        int backToFragment = EMAILLOGIN_FLAG;
        switch (currentFragmentTag) {
            case EMAILLOGIN_FLAG:
                backToFragment = BACKAPP;
                break;
            case EMAIL_BOUND:
                backToFragment = EMAILLOGIN_FLAG;
                break;
            case EMAILREGISTER_FLAG:
                backToFragment = EMAILLOGIN_FLAG;
                break;
            case SENDEMAIL_FLAG:
                backToFragment = EMAILLOGIN_FLAG;
                break;
            case SENDESUCCESS_FLAG:
                backToFragment = FORGOTPASSWORD_FLAG;
                break;
            case FORGOTPASSWORD_FLAG:
                backToFragment = EMAILLOGIN_FLAG;
                break;
            case REGISTERSUCCESS_FLAG:
                backToFragment = EMAILLOGIN_FLAG;
                break;
        }
        return backToFragment;
    }

    private Dialog dialog;

    public void InitMyDialog() {
        CustomMyDialog.Builder builder = new CustomMyDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure_you_want));
        //  builder.setTitle("提示");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                LoginRegisterActivity.this.finish();
                overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
            }
        });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();
    }

    public void switchFragment(int from, int to) {
        if (LoginRegisterActivity.EMAIL_BOUND == from && LoginRegisterActivity.REGISTERSUCCESS_FLAG == to) {
            redirectToAttachedFragment(to, 1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);

//        EasyTracker easyTracker = EasyTracker.getInstance(this);
//        easyTracker.send(MapBuilder.createEvent("Sign In Screen", // Event category (required)
//                null, // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());

    }

    @Override
    protected void onStop() {
        super.onStop();
//        EasyTracker.getInstance(this).activityStop(this);
    }
}

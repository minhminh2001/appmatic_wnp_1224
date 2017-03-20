package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HideFunctionLoginActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.model.TMPHelpCenterListToDetailEntity;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;

/**
 * Created by imaginato on 2015/7/27.
 */
//HomeHelpCenterListFragment
public class HomeHelpCenterListFragment extends HomeBaseFragment implements View.OnClickListener {
    private HomeActivity homeActivity;
    private View contentView;

    private CustomTextView ctvHeaderBarTitle;
    private RelativeLayout rlAboutus, rlPrivacypolicy, rlTermsofuse, rlHowtobuy, rlPayments, rlShippingdelivery,
            rlOrdertracking, rlCancellationsreturn, rlGemcashvoucher, rlCustomerservice,statement,rlHeaderBarMenu;
    private View hideFunction;
    private Handler handler=null;
    private HideThread hideThread;
    private int hideCount=0;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home_helpcenterlist, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mCommonCallback!=null)mCommonCallback.switchMenu(HomeCommonCallback.MENU_HELPCENTER);
        hideFunction=contentView.findViewById(R.id.information_RelativeLayout);
        hideFunction.setOnClickListener(this);
        handler=new Handler();
        hideThread=new HideThread();
        if(mCommonCallback!=null){
            mCommonCallback.resetMenuAndListenter();
            mCommonCallback.setTitle(getResources().getString(R.string.home_helpcenter_title));
            mCommonCallback.getToolBar().getMenu().clear();
        }
        rlHeaderBarMenu=(RelativeLayout) contentView.findViewById(R.id.rlHeaderBarMenu);
        ctvHeaderBarTitle = (CustomTextView) contentView.findViewById(R.id.ctvHeaderBarTitle);
        rlAboutus = (RelativeLayout) contentView.findViewById(R.id.rlAboutus);
        rlPrivacypolicy = (RelativeLayout) contentView.findViewById(R.id.rlPrivacypolicy);
        rlTermsofuse = (RelativeLayout) contentView.findViewById(R.id.rlTermsofuse);
        rlHowtobuy = (RelativeLayout) contentView.findViewById(R.id.rlHowtobuy);
        rlPayments = (RelativeLayout) contentView.findViewById(R.id.rlPayments);
        rlShippingdelivery = (RelativeLayout) contentView.findViewById(R.id.rlShippingdelivery);
        rlOrdertracking = (RelativeLayout) contentView.findViewById(R.id.rlOrdertracking);
        rlCancellationsreturn = (RelativeLayout) contentView.findViewById(R.id.rlCancellationsreturn);
        rlGemcashvoucher = (RelativeLayout) contentView.findViewById(R.id.rlGemcashvoucher);
        rlCustomerservice = (RelativeLayout) contentView.findViewById(R.id.rlCustomerservice);
        statement= (RelativeLayout) contentView.findViewById(R.id.statement);

        rlHeaderBarMenu.setOnClickListener(this);
        rlAboutus.setOnClickListener(this);
        rlPrivacypolicy.setOnClickListener(this);
        rlTermsofuse.setOnClickListener(this);
        rlHowtobuy.setOnClickListener(this);
        rlPayments.setOnClickListener(this);
        rlShippingdelivery.setOnClickListener(this);
        rlOrdertracking.setOnClickListener(this);
        rlCancellationsreturn.setOnClickListener(this);
        rlGemcashvoucher.setOnClickListener(this);
        rlCustomerservice.setOnClickListener(this);
        statement.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rlAboutus: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(0);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);

                break;
            }
            case R.id.rlPrivacypolicy: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(1);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlTermsofuse: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(2);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.statement: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(10);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlHowtobuy: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(3);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlPayments: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(4);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlShippingdelivery: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(5);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlOrdertracking: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(6);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlCancellationsreturn: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(7);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlGemcashvoucher: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(8);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;
            }
            case R.id.rlCustomerservice: {
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(9);
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
                break;

            }
            case R.id.information_RelativeLayout:{
                hideCount++;
                if (hideCount==1) {
                    handler.postDelayed(hideThread, 800);
                }
                break;
            }
        }
    }

    private class  HideThread implements Runnable{
        @Override
        public void run() {
            if(hideCount>=5){
                Intent intent=new Intent();
                intent.setClass(homeActivity, HideFunctionLoginActivity.class);
                homeActivity.startActivity(intent);
            }
               hideCount=0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        EasyTracker easyTracker = EasyTracker.getInstance(homeActivity);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Help Centre Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity,true);
        GaTrackHelper.getInstance().googleAnalytics("Help Centre Screen", homeActivity);
        JLogUtils.i("googleGA_screen", "Help Centre Screen");
    }

    @Override
    public void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity, false);
    }
}

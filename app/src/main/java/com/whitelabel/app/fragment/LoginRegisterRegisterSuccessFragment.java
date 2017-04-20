package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

/**
 * Created by imaginato on 2015/6/24.
 */
public class LoginRegisterRegisterSuccessFragment extends Fragment implements View.OnClickListener {
    private LoginRegisterActivity loginRegisterActivity;
    private View contentView;
    private TextView goshopping;
    private TextView rigisterSuccess1;
    private boolean  isStart;
    private ToolBarFragmentCallback toolBarFragmentCallback;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginRegisterActivity= (LoginRegisterActivity) activity;
            toolBarFragmentCallback= (ToolBarFragmentCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_loginregister_register_success,null);
        return contentView;
    }
    public void onClickLeftMenu(View v) {
        loginRegisterActivity.onBackPressed();
    }

    public void onClickRightMenu(View v) {
        if(isStart) {
            Intent i = new Intent(loginRegisterActivity, HomeActivity.class);
            startActivity(i);
            loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
            loginRegisterActivity.finish();
        }else{
            loginRegisterActivity.redirectToAttachedFragment(LoginRegisterActivity.EMAILLOGIN_FLAG, -2);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cancel, menu);
        View view=menu.findItem(R.id.action_cancel).getActionView();
        ImageView ivCancel= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),ivCancel,R.drawable.ic_action_close);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRightMenu(v);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_cancel:
                onClickRightMenu(item.getActionView());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.register));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        if(loginRegisterActivity.getIntent()!=null&&loginRegisterActivity.getIntent().getExtras()!=null){
            String activityAata = loginRegisterActivity.getIntent().getExtras().getString("Activity");//读出数据
            if ("start".equals(activityAata)){
                isStart = true;
            }
        }
        goshopping= (TextView) contentView.findViewById(R.id.goshopping);
        goshopping.setOnClickListener(this);
//        goshopping.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
//        goshopping.setBackground(JImageUtils.getbuttonBakcgroundStrokeDrawable(getActivity()));
        JViewUtils.setStrokeButtonGlobalStyle(getActivity(),goshopping);
        rigisterSuccess1= (TextView) contentView.findViewById(R.id.rigisterSuccess1);
        String myEmail=loginRegisterActivity.getMyEmail();
        if(loginRegisterActivity.isEmailConfirm()){
            rigisterSuccess1.setText(getResources().getString(R.string.process_email)+myEmail+" "+getResources().getString(R.string.to_confirm));
        }else {
            rigisterSuccess1.setText(getResources().getString(R.string.register_whitelabel)+" "+getResources().getString(R.string.app_name)+".");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goshopping:
                loginRegisterActivity.startNextActivity(null,HomeActivity.class,true);
                break;
        }
    }


}

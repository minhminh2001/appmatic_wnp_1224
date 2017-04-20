package com.whitelabel.app.fragment;

import android.app.Activity;
import android.os.Bundle;
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
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

/**
 * Created by imaginato on 2015/6/23.
 */
public class LoginRegisterEmailSendSuccessFragment extends Fragment implements View.OnClickListener {
    private LoginRegisterActivity loginRegisterActivity;
    private View contentView;
    private TextView successText;
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
    public void onClickLeftMenu(View v) {
        loginRegisterActivity.onBackPressed();
        loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
    }

    public void onClickRightMenu(View v) {
        loginRegisterActivity.redirectToAttachedFragment(LoginRegisterActivity.EMAILLOGIN_FLAG, -2);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_loginregister_login_send_success,null);
        return contentView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.send_confirmation_email));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        successText= (TextView) contentView.findViewById(R.id.success_Text);
        String myEmail=loginRegisterActivity.getMyEmail();
        successText.setText(getResources().getString(R.string.account_associated)+myEmail+getResources().getString(R.string.reset_password));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
    }


}

package com.whitelabel.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.model.NotificationCell;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.CustomWebView;

/**
 * Created by Administrator on 2015/9/24.
 */
public class HomeNotificationDetailFragment extends Fragment implements View.OnClickListener{

    private HomeActivity homeActivity;
    private View view;
    private CustomWebView webView;
    private CustomTextView tvTitle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_notification_detail, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView = (CustomWebView) view.findViewById(R.id.webview_Detail);
        ImageView ivBack = (ImageView) view.findViewById(R.id.ivHeaderBarMenu);
        view.findViewById(R.id.rl_ivHeaderBarMenu).setOnClickListener(this);

        tvTitle = (CustomTextView) view.findViewById(R.id.ctvHeaderBarTitle);
        ivBack.setOnClickListener(this);
        initData();
    }

    private void initData() {
        NotificationCell notificationCell = (NotificationCell) getArguments().getSerializable("data");
        tvTitle.setText(notificationCell.getTitle());
        webView.setText(notificationCell.getBody());
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ivHeaderBarMenu:
            case R.id.ivHeaderBarMenu:
                homeActivity.switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONDETAIL, HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONLIST, null);
                break;
            default:
                break;
        }
    }
}

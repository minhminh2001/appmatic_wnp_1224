package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.NotificationActivity;
import com.whitelabel.app.activity.NotificationDetailActivity;
import com.whitelabel.app.adapter.NotificationListAdapter;
import com.whitelabel.app.model.NotificationInfo;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomXListView;

import java.util.ArrayList;

public class NotificationListFragment<T extends BasePresenter> extends com.whitelabel.app.BaseFragment<T> implements CustomXListView.IXListViewListener,SwipeRefreshLayout.OnRefreshListener {
    private String TAG ="NotificationListFragment";

    private NotificationActivity notificationActivity;
    private NotificationListAdapter adapter;
    private CustomXListView clistView;
    private Dialog progressDialog;
    private ArrayList<NotificationInfo> notifications;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        notificationActivity = (NotificationActivity) activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_notification_list, null);
        setRetryTheme(view);
        clistView = (CustomXListView) view.findViewById(R.id.lv_notification);
        swipeContainer= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        swipeContainer.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notifications = new ArrayList<NotificationInfo>();
        progressDialog = JViewUtils.showProgressDialog(notificationActivity);
        adapter = new NotificationListAdapter(notificationActivity, notifications);
        clistView.setAdapter(adapter);
        clistView.setXListViewListener(this);
        clistView.setPullRefreshEnable(false);
        clistView.setPullLoadEnable(true);
        clistView.setHeaderDividersEnabled(false);
        clistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position <= notifications.size()) {
                    Intent intent = new Intent(notificationActivity, NotificationDetailActivity.class);
                    startActivityForResult(intent, 100);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        GaTrackHelper.getInstance().googleAnalyticsReportActivity(notificationActivity, true);
    }

    @Override
    public void onStop() {
        super.onStop();

        GaTrackHelper.getInstance().googleAnalyticsReportActivity(notificationActivity, false);
    }

    // TODO(Aaron):Data refresh in the next version
    @Override
    public void onRefresh() {

    }

    // TODO(Aaron):Load data in the next version
    @Override
    public void onLoadMore() {

    }
}

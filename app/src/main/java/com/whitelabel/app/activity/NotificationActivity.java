package com.whitelabel.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.fragment.NotificationListFragment;
import com.whitelabel.app.utils.JViewUtils;

/**
 * Created by Aaron on 2018/3/22.
 */

public class NotificationActivity extends com.whitelabel.app.BaseActivity {
    private final static String TAG = "NotificationActivity";
    private NotificationListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initToolBar();
        initFragment();
    }
    
    private void initToolBar() {
        setTitle(getResources().getString(R.string.NOTIFICATION));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFragment() {
        fragment = new NotificationListFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mParent, fragment).commit();
    }
}

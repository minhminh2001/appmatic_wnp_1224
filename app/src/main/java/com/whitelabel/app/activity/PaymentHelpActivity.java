package com.whitelabel.app.activity;

import android.os.Bundle;

import com.whitelabel.app.R;
import com.whitelabel.app.fragment.HomeHelpCenterDetailFragment;
import com.whitelabel.app.model.TMPHelpCenterListToDetailEntity;

/**
 * Created by ray on 2015/12/1.
 */
public class PaymentHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_help);
        android.support.v4.app.FragmentManager manager= getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction= manager.beginTransaction();
        HomeHelpCenterDetailFragment fragment=new HomeHelpCenterDetailFragment();
        TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
        dataentity.setHelpCenterType(4);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data", dataentity);
        fragment.setArguments(bundle);
        transaction.replace(R.id.content1,fragment);
        transaction.commitAllowingStateLoss();
    }



}

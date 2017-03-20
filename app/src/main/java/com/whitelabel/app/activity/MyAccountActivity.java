package com.whitelabel.app.activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.fragment.BaseFragment;
import com.whitelabel.app.fragment.MyAccountEditInfoFragment;
import com.whitelabel.app.utils.JToolUtils;

import java.util.ArrayList;

public class MyAccountActivity extends com.whitelabel.app.BaseActivity {
    private static final String TAG="MyAccountActivity";
    public static final int BACKAPP = -1;//退出
    public static final int EDITINFO_FLAG= 0;//编辑界面
    private ArrayList<BaseFragment> attachedFragmentArray= new ArrayList<BaseFragment>();//存放顺序固定
    private ArrayList<Integer> fragmentList=new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        initFragmentData();//初始化所有的Fragment
        initToolBar("");
    }
    public void initToolBar(String title) {
        setTitle(title);
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setRightTextMenuClickListener(
                getMenuInflater(),
                R.menu.menu_save,
                menu,
                R.id.action_save,
                R.layout.menu_item_save,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MyAccountEditInfoFragment)attachedFragmentArray.get(EDITINFO_FLAG)).save();
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                ((MyAccountEditInfoFragment)attachedFragmentArray.get(EDITINFO_FLAG)).save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void initFragmentData(){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        MyAccountEditInfoFragment editInfoFragment=new MyAccountEditInfoFragment();
        transaction.replace(R.id.flFilterSortContainer,editInfoFragment);
        transaction.commit();
        attachedFragmentArray.add(EDITINFO_FLAG,editInfoFragment);
        fragmentList.add(BACKAPP);
        fragmentList.add(EDITINFO_FLAG);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void redirectToAttachedFragment(int to, int type) {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        BaseFragment subFragment=attachedFragmentArray.get(to);
        //系统动画
        //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (type == -1) {
            fragmentTransaction.setCustomAnimations(
                    R.animator.fragment_profile_slide_left_enter,
                    R.animator.fragment_profile_slide_right_exit
            );
        } else if (type == 1) {
            fragmentTransaction.setCustomAnimations(
                    R.animator.fragment_profile_slide_right_enter,
                    R.animator.fragment_profile_slide_left_exit
            );
        }
        fragmentTransaction.replace(R.id.flFilterSortContainer, subFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

package com.whitelabel.app.utils;

import android.support.v4.app.Fragment;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.RetrofitHelper;
import com.whitelabel.app.fragment.HomeHomeFragment;
import com.whitelabel.app.ui.home.HomeHomeFragmentV2;
import com.whitelabel.app.ui.home.HomeHomeFragmentV3;

/**
 * Created by ray on 2017/5/3.
 */

public class FragmentFactory {
    private static FragmentFactory fragmentFactory;
    private FragmentFactory(){


    }
    public  static FragmentFactory newInstance(){
        if(fragmentFactory==null){
            synchronized (DataManager.class){
                fragmentFactory= new FragmentFactory();
            }
        }
        return fragmentFactory;
    }


    public Fragment  getHomeFragment(){
        if(WhiteLabelApplication.getAppConfiguration().getLayoutStyle().getHomeType()==1){
            return HomeHomeFragmentV2.newInstance();
        }else if(WhiteLabelApplication.getAppConfiguration().getLayoutStyle().getHomeType()==2){
            return HomeHomeFragmentV3.newInstance();
        }else if(WhiteLabelApplication.getAppConfiguration().getLayoutStyle().getHomeType()==3){
            return new HomeHomeFragment();
        }else if(WhiteLabelApplication.getAppConfiguration().getLayoutStyle().getHomeType()==4){

        }
        return new HomeHomeFragment();
    }
}

package com.whitelabel.app.data;

import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.AppApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.data.retrofit.RetrofitHelper;

/**
 * Created by Administrator on 2017/1/3.
 */

public class DataManager {
    private static  DataManager dataManager;
    private PreferHelper  preferHelper;
    private CheckoutApi checkoutApi;
    private  AppApi mAppApi;
    private ProductApi  mProductApi;
    private DataManager(){

    }

    public static DataManager getInstance(){
        if(dataManager==null){
            synchronized (DataManager.class){
                dataManager=new DataManager();
            }
        }
        return dataManager;
    }

    public PreferHelper getPreferHelper(){
        if(preferHelper==null){
            synchronized (DataManager.class){
                preferHelper=new PreferHelper();
            }
        }
        return preferHelper;
    }


    public ProductApi getProductApi(){
        if(mProductApi==null){
            synchronized (DataManager.class){
                mProductApi= RetrofitHelper.getDefaultRetrofit().create(ProductApi.class);
            }
        }
        return mProductApi;

    }
    public CheckoutApi  getCheckoutApi(){
            if(checkoutApi==null){
                synchronized (DataManager.class){
                    checkoutApi= RetrofitHelper.getDefaultRetrofit().create(CheckoutApi.class);
                }
            }
        return checkoutApi;
    }

    public AppApi  getAppApi(){
        if(mAppApi==null){
            synchronized (DataManager.class){
                mAppApi= RetrofitHelper.getMockRetrofit().create(AppApi.class);
            }
        }
        return mAppApi;
    }




}

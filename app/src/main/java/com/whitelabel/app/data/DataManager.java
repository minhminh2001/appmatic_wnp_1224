package com.whitelabel.app.data;

import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.AppApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
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
    private MyAccoutApi mMyAccountApi;
    private static String mBaseUrl;
    private static  String mMockUrl;
    private DataManager(){
    }
    public static DataManager getInstance(){
        mBaseUrl="http://192.168.1.233:9090/";
        mMockUrl="http://192.168.1.233:8088/";
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
    public MyAccoutApi getMyAccountApi(){
        if(mMyAccountApi==null){
            synchronized (DataManager.class){
                mMyAccountApi=new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(MyAccoutApi.class);
            }
        }
        return mMyAccountApi;
    }

    public ProductApi getProductApi(){
        if(mProductApi==null){
            synchronized (DataManager.class){
                mProductApi= new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(ProductApi.class);
            }
        }
        return mProductApi;
    }
    public CheckoutApi  getCheckoutApi(){
            if(checkoutApi==null){
                synchronized (DataManager.class){
                    checkoutApi= new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(CheckoutApi.class);
                }
            }
        return checkoutApi;
    }
    public AppApi  getAppApi(){
        if(mAppApi==null){
            synchronized (DataManager.class){
                mAppApi= new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(AppApi.class);
            }
        }
        return mAppApi;
    }
    private MockApi mMockApi;
    public MockApi getMockApi(){
        if(mMockApi==null){
            synchronized (DataManager.class){
                mMockApi=  new RetrofitHelper(mBaseUrl,mMockUrl).getMockRetrofit().create(MockApi.class);
            }
        }
        return mMockApi;
    }
}

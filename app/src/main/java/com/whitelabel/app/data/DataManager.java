package com.whitelabel.app.data;

import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.BaseApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.data.retrofit.RetrofitHelper;
import com.whitelabel.app.data.retrofit.ShoppingCartApi;

/**
 * Created by Administrator on 2017/1/3.
 */
public class DataManager {
    private static  DataManager dataManager;
    private PreferHelper  preferHelper;
    private CheckoutApi checkoutApi;
    private BaseApi mAppApi;
    private ProductApi  mProductApi;
    private MyAccoutApi mMyAccountApi;

    private ShoppingCartApi mShoppingCartApi;
    private static String mBaseUrl;
    private static  String mMockUrl;
    private DataManager(){
    }
    public static DataManager getInstance(){
        mBaseUrl="https://dev2.wnp.com.hk/";
        mMockUrl="http://192.168.1.233:8088/";
        if(dataManager==null){
            synchronized (DataManager.class){
                dataManager=new DataManager();
            }
        }
        return dataManager;
    }
    public ShoppingCartApi getShoppingCartApi(){
        if(mShoppingCartApi==null){
            synchronized (DataManager.class){
                mShoppingCartApi=new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(ShoppingCartApi.class);
            }
        }
        return mShoppingCartApi;
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
    public BaseApi getAppApi(){
        if(mAppApi==null){
            synchronized (DataManager.class){
                mAppApi= new RetrofitHelper(mBaseUrl,mMockUrl).getDefaultRetrofit().create(BaseApi.class);
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

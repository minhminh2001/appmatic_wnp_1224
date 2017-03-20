package com.whitelabel.app.data;


import com.whitelabel.app.data.retrofit.RetrofitHelper;
import com.whitelabel.app.data.retrofit.TestApi;

public class DataManager {
    private static  volatile  DataManager dataManager;
    private static final Object lock = new Object();
    private TestApi mTestApi;
    private DataManager(){
    }
    public static DataManager getInstance(){
        if(dataManager==null){
            synchronized (lock){
                if(dataManager==null) {
                    dataManager = new DataManager();
                }
            }
        }
        return dataManager;
    }

    public  TestApi getTestApi(){
        if(mTestApi==null){
            synchronized (DataManager.class){
                mTestApi= RetrofitHelper.getDefaultRetrofit().create(TestApi.class);
            }
        }
        return mTestApi;
    }
}

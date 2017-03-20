package com.whitelabel.app;

import android.app.Application;



/**
 * Created by Administrator on 2017/1/10.
 */

public class App  extends Application {
    private  static  App  mInstance;
    private String gcmRegistrationToken="";
    private boolean gcmRegistrationTokenPost=false;

    static final private Object lock = new Object();
    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (lock) {
            mInstance = this;
        }
        GlobalData.init(this);
    }
    public static App getInstance() {
        return mInstance;
    }


}

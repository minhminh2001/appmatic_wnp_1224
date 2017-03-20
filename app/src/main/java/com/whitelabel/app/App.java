package com.whitelabel.app;

import android.app.Application;





public class App  extends Application {
    private  static  App  mInstance;

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

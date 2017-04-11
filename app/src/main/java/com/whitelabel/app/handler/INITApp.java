package com.whitelabel.app.handler;

import android.app.Activity;
import android.os.Handler;

import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.INITCallback;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/6/12.
 */
public class INITApp implements Runnable {
    private WeakReference<Activity> activity;
    private INITCallback callback;
    private Handler handler;

    public INITApp(Activity activity, INITCallback callback) {
        this.activity =new WeakReference<Activity>(activity);
        this.callback = callback;
        this.handler = new Handler();
    }

    @Override
    public void run() {
        if (activity == null) {
            callOnFailure(INITCallback.CODE_NG, "The activity is empty.");
            return;
        }
        if(activity.get()==null)return;
        WhiteLabelApplication.getPhoneConfiguration().init(activity.get());
        WhiteLabelApplication.getAppConfiguration().init(activity.get());
        callOnSuccess(INITCallback.CODE_OK, "Successful initialization");
    }

    private void callOnFailure(int resultCode, Object object) {
        if (handler != null) {
            handler.post(new Runnable() {
                private int resultCode;
                private Object object;
                public Runnable init(int r, Object o) {
                    resultCode = r;
                    object = o;
                    return this;
                }

                @Override
                public void run() {
                    if (callback != null) {
                        callback.onFailure(resultCode, object);
                    }
                }
            }.init(resultCode, resultCode));
        }
    }

    private void callOnSuccess(int resultCode, Object object) {
        if (handler != null) {
            handler.post(new Runnable() {
                private int resultCode;
                private Object object;
                public Runnable init(int r, Object o) {
                    resultCode = r;
                    object = o;
                    return this;
                }

                @Override
                public void run() {
                    if (callback != null) {
                        callback.onSuccess(resultCode, object);
                    }
                }
            }.init(resultCode, object));
        }
    }
}

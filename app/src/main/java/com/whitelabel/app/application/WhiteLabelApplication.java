package com.whitelabel.app.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.exception.CrashHandler;
import com.whitelabel.app.model.ApplicationConfigurationEntity;
import com.whitelabel.app.model.PhoneConfigurationEntity;
import com.whitelabel.app.network.HttpClientRequest;
import com.whitelabel.app.utils.JToolUtils;


/**
 * Created by imaginato on 2015/6/10.
 */
public class WhiteLabelApplication extends MultiDexApplication {

    private static WhiteLabelApplication mInstance;
    public static boolean delayShowAppRate = false;
    private static PhoneConfigurationEntity phoneConfiguration;
    private static ApplicationConfigurationEntity appConfiguration;

    private Tracker mTracker;
    private GoogleAnalytics analytics;

    public static PhoneConfigurationEntity getPhoneConfiguration() {
        if (phoneConfiguration == null) {
            phoneConfiguration = PhoneConfigurationEntity.getInstance();
        }
        return phoneConfiguration;
    }

    public static WhiteLabelApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void addToRequestQueue(Request request, String tag) {
        HttpClientRequest.getInstance(getApplicationContext()).addToRequestQueue(request, tag);
    }

    public void cancelPendingRequests(Object tag) {
        HttpClientRequest.getInstance(getApplicationContext()).cancelPendingRequests(tag);
    }

    public static ApplicationConfigurationEntity getAppConfiguration() {
        if (appConfiguration == null) {
            appConfiguration = ApplicationConfigurationEntity.getInstance();
        }
        return appConfiguration;
    }

    public static void InitappConfigurationEntity() {
        appConfiguration = null;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        try {
            mInstance = this;
            GlobalData.init(this);
            FacebookSdk.sdkInitialize(getApplicationContext());
            FacebookSdk.setApplicationId(GlobalData.facebookId);
            WhiteLabelApplication.getAppConfiguration().isSignIn(getApplicationContext());
            WhiteLabelApplication.getAppConfiguration().init(getApplicationContext());
//            getAnalyticTracherInstance(this);
            ViewTarget.setTagId(R.id.glide_tag);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    //    public  static RefWatcher getRefWatcher(Context context){
//        WhiteLabelApplication application = (WhiteLabelApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//    private RefWatcher refWatcher;
    public GoogleAnalytics getAnalyticInstance() {
        if (analytics == null) {
            analytics = GoogleAnalytics.getInstance(this);
        }
        return analytics;
    }

    public Tracker getAnalyticTracherInstance(Context context) {
        if (mTracker == null) {
            getAnalyticInstance();
            mTracker = analytics.newTracker(GlobalData.gaTrackId);
            //Tracker mTracker = analytics.newTracker(R.xml.global_tracker);
            analytics.getLogger()
                    .setLogLevel(Logger.LogLevel.VERBOSE);
            mTracker.setAppVersion(JToolUtils.getAppVersion());
            mTracker.enableExceptionReporting(true);
            mTracker.enableAdvertisingIdCollection(true);
            mTracker.enableAutoActivityTracking(false);
        }
        return mTracker;
    }

}

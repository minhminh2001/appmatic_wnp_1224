package com.whitelabel.app.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.Serializable;


public class PhoneConfigurationEntity implements Serializable {
    private int screenWidth;
    private int screenHeigth;
    private float screenDensity;
    private int screenDpi;
    private float screenScaledDensity;
    private String registrationToken;//GCM's device token.

    private Location location;

    private PhoneConfigurationEntity() {
    }

    public static PhoneConfigurationEntity getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

    public void init(Activity activity) {
        getScreenInfo(activity);
    }

    public String getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();

        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        return sb.toString();
    }

    public void getScreenInfo(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeigth = metric.heightPixels;
        screenDensity = metric.density;
        screenDpi = metric.densityDpi;
        screenScaledDensity = metric.scaledDensity;
    }

    public boolean isConnect(Context context) {
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != conMan) {
                NetworkInfo mobileNetWorkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileNetWorkInfo != null) {
                    State mobile = mobileNetWorkInfo.getState();
                    if (mobile != null && mobile.equals(State.CONNECTED)) {
                        return true;
                    }
                }

                NetworkInfo wifiNetworkInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetworkInfo != null) {
                    State wifi = wifiNetworkInfo.getState();
                    if (wifi != null && wifi.equals(State.CONNECTED)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }

    public int getScreenWidth(Activity activity){
        if(screenWidth==0){
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;
            screenHeigth = metric.heightPixels;
            screenDensity = metric.density;
            screenDpi = metric.densityDpi;
            screenScaledDensity = metric.scaledDensity;
        }
        return  screenWidth;

    }


    public int getScreenHeigth(Activity activity){

        if(screenWidth==0){
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;
            screenHeigth = metric.heightPixels;
            screenDensity = metric.density;
            screenDpi = metric.densityDpi;
            screenScaledDensity = metric.scaledDensity;
        }
        return  screenHeigth;
    }

    public int getScreenWidth() {


        return screenWidth;
    }

    public int getScreenHeigth() {
        return screenHeigth;
    }

    public float getScreenDensity() {
        return screenDensity;
    }

    public int getScreenDpi() {
        return screenDpi;
    }

    public float getScreenScaledDensity() {
        return screenScaledDensity;
    }

    public Location getLocation() {
        if (location == null) {
            location = new Location();
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private static class SingletonHolder {
        static final PhoneConfigurationEntity INSTANCE = new PhoneConfigurationEntity();
    }

    public class Location implements Serializable {
        private double latitude = 0;
        private double longitude = 0;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}

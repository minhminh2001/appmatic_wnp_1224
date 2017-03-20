package com.whitelabel.app;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/20.
 */

public class GlobalData {

    public static String apiVersion;
    public static String appVersion="";
    public  static String  appName;
    public static String  serviceRequestUrl; //api url
    public static String downloadImagePath; // image  url
    public static  Map<String,String>  globalParams;

    public static   void init(Context context){
        appVersion="";
        apiVersion="v1";
        appName=context.getResources().getString(R.string.app_name);
        serviceRequestUrl= BuildConfig.REQUEST_URL;
        globalParams=getRequestGlobalParams();
        downloadImagePath="";

    }

    private static Map<String,String> getRequestGlobalParams(){
        Map<String,String>  params=new HashMap<>();
//        params.put("versionNumber",appVersion);
        return params;
    }
}

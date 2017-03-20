package com.whitelabel.app.utils;

import android.util.Log;

import com.whitelabel.app.BuildConfig;
import com.whitelabel.app.utils.logger.Logger;


/**
 * Created by imaginato on 2015/6/10.
 */
public class JLogUtils {
    public static final boolean useLogTool = false;
    public static final String LOGGER_INIT_TAG = "GEM";
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).i(msg+"");
            }else{
                Log.i(tag, msg+"");
            }
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).i(msg+"");
            }else{
                Log.i(tag, msg+"", tr);
            }

        }
    }
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).e(msg+"");
            }else{
                Log.e(tag, msg+"");
            }
        }
    }
    public static void e(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).e(tr, msg);
            }else{
                Log.e(tag, msg, tr);
            }
        }

    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).d(msg);
            }else{
                Log.d(tag, msg);
            }
        }

    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).d(msg);
            }else{
                Log.d(tag, msg, tr);
            }
        }

    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).w(msg);
            }else{
                Log.w(tag, msg);
            }
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).w(msg);
            }else{
                Log.w(tag, msg, tr);
            }
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).v(msg);
            }else {
                Log.v(tag, msg, tr);
            }
        }

    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).v(msg);
            }else{
                Log.v(tag, msg);
            }

        }

    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).wtf(msg);
            }else{
                Log.wtf(tag, msg, tr);
            }

        }

    }

    public static void wtf(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).wtf(msg);
            }else{
                Log.wtf(tag, msg);
            }
        }

    }
    public static void json(String tag,int requestCode, String json) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag+"-"+requestCode+"-->").json(json);
            }else{
                Log.d(tag, requestCode+"-->"+json);
            }
        }

    }
    public static void json(String tag, String json) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).json(json);
            }else{
                Log.d(tag,json);
            }
        }

    }
    public static void xml(String tag,String xml) {
        if (BuildConfig.DEBUG) {
            if(useLogTool){
                Logger.init(LOGGER_INIT_TAG);
                Logger.t(tag).xml(xml);
            }else{
                Log.d(tag,xml);
            }
        }
    }
}

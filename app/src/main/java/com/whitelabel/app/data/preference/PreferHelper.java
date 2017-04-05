package com.whitelabel.app.data.preference;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.utils.RxUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PreferHelper {
    private static  final String FILE_NAME="whtelabel";
    private static final String TABLE_CONFIG="config";
    public String  getVersionNumber(){
        RemoteConfigResonseModel.RetomeConfig config=getLocalConfigModel();
        String currentVersion="";
        if(config!=null){
            currentVersion=config.getVersion();
        }
        return currentVersion ;
    }
    public void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel){
        SharedPreferences  sharedPreferences= GemfiveApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        RemoteConfigResonseModel.RetomeConfig config=remoteConfigModel.getData();
        String  configStr=new Gson().toJson(config);
        sharedPreferences.edit().putString(TABLE_CONFIG,configStr).apply();
    }
    public RemoteConfigResonseModel.RetomeConfig  getLocalConfigModel(){
        SharedPreferences  sharedPreferences  =GemfiveApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        String str=sharedPreferences.getString(TABLE_CONFIG,"");
        Gson gson=new Gson();
        RemoteConfigResonseModel.RetomeConfig  retomeConfig=null;
        if(retomeConfig!=null) {
            retomeConfig = gson.fromJson(str, RemoteConfigResonseModel.RetomeConfig.class);
        }
        return retomeConfig;
    }
}

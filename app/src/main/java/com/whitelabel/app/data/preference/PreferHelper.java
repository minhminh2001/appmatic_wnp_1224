package com.whitelabel.app.data.preference;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.utils.JLogUtils;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PreferHelper {
    private static  final String FILE_NAME="whtelabel";
    private static final String TABLE_CONFIG="config";
    private static final String TABLE_CURRENCY="currency";
    public String  getVersionNumber(){
        RemoteConfigResonseModel.RetomeConfig config=getLocalConfigModel();
        JLogUtils.i("ray","retomeconfig:"+config);
        String currentVersion="";
        if(config!=null){
            currentVersion=config.getVersion();
        }
        return currentVersion ;
    }

    public void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        RemoteConfigResonseModel.RetomeConfig config=remoteConfigModel.getData();
        String  configStr=new Gson().toJson(config);
        JLogUtils.i("ray","configStr:"+configStr);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CONFIG,configStr);
        editor.apply();
    }


    public void saveCurrency(String currency){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CURRENCY,currency);
        editor.apply();
    }



    public String getCurrency(){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        return  sharedPreferences.getString(TABLE_CURRENCY,"HK$");
    }


    public RemoteConfigResonseModel.RetomeConfig  getLocalConfigModel(){
        SharedPreferences  sharedPreferences  = WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,-1);
        String str=sharedPreferences.getString(TABLE_CONFIG,"");
        Gson gson=new Gson();
        RemoteConfigResonseModel.RetomeConfig  retomeConfig=null;
        if(!TextUtils.isEmpty(str)) {
            retomeConfig = gson.fromJson(str, RemoteConfigResonseModel.RetomeConfig.class);
        }
        return retomeConfig;
    }
}

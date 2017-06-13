package com.whitelabel.app.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;

import rx.observables.AsyncOnSubscribe;

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
        editor.commit();
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
    public  void  saveAddressList(String userId,List<AddressBook> beans){
        try {
            SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr = gson.toJson(beans);
            editor.putString("address_"+userId, beanStr);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }

    }

    public rx.Observable<List<AddressBook>> getAddressListCache(final String userId){
       return  rx.Observable.fromCallable(new Callable<List<AddressBook>>() {
            @Override
            public List<AddressBook> call() throws Exception {
                if(TextUtils.isEmpty(userId)){
                    return  null;
                }
                List<AddressBook> beans=null;
                try{
                    SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences("myAccount", Context.MODE_PRIVATE);
                    String beanStr=sharedPreferences.getString("address_" + userId, "");
                    if(!TextUtils.isEmpty(beanStr)) {
                        JLogUtils.i("JStorageUtils","localStr;"+beanStr);
                        beans = JJsonUtils.parseAddressList(beanStr);
                    }
                }catch (Exception ex){
                    ex.getStackTrace();
                }
                return beans;
            }
        });

    }
}
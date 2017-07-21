package com.whitelabel.app.data.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PreferHelper  implements ICacheApi{
    private static  final String FILE_NAME="whtelabel";
    private static final String TABLE_CONFIG="config";
    private static final String TABLE_CURRENCY="currency";
    public String  getVersionNumber(){
        RemoteConfigResonseModel.RetomeConfig config=getLocalConfigModel();
        String currentVersion="";
        if(config!=null){
            currentVersion=config.getVersion();
        }
        return currentVersion ;
    }

    public void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        RemoteConfigResonseModel.RetomeConfig config=remoteConfigModel.getData();
        String  configStr=new Gson().toJson(config);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CONFIG,configStr);
        editor.commit();
    }
    public void saveCurrency(String currency){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CURRENCY,currency);
        editor.apply();
    }
    public String getCurrency(){
        SharedPreferences  sharedPreferences= WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TABLE_CURRENCY,"HK$");
    }
    public RemoteConfigResonseModel.RetomeConfig  getLocalConfigModel(){
        SharedPreferences  sharedPreferences  = WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
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
    public rx.Observable<List<TMPLocalCartRepositoryProductEntity>> getShoppingCartProduct(){
        return rx.Observable.fromCallable(new Callable<List<TMPLocalCartRepositoryProductEntity>>() {
            @Override
            public List<TMPLocalCartRepositoryProductEntity> call() throws Exception {
                ArrayList<TMPLocalCartRepositoryProductEntity> result = new ArrayList<TMPLocalCartRepositoryProductEntity>();
                SharedPreferences cartProductSP = WhiteLabelApplication.getInstance().getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
                if (cartProductSP == null) {
                    return result;
                }
                String productListString = cartProductSP.getString("productList", null);
                JLogUtils.d("response", "getLocalCart>>" + productListString);
                if (!JDataUtils.isEmpty(productListString)) {
                    ArrayList<TMPLocalCartRepositoryProductEntity> oldProductEntityArrayList = null;
                    try {
                        Gson gson = new Gson();
                        TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>> typeToken = new TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>>() {
                        };
                        if (typeToken != null && gson != null) {
                            Type type = typeToken.getType();
                            oldProductEntityArrayList = gson.fromJson(productListString, type);
                        }
                    } catch (Exception ex) {
                        JLogUtils.e("PreferHelper", "getProductListFromLocalCartRepository", ex);
                    }
                    if (oldProductEntityArrayList != null && oldProductEntityArrayList.size() > 0) {
                        result.addAll(oldProductEntityArrayList);
                    }
                }
                return result;
            }
        });
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
    public GOUserEntity   getUser(){
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("user_info", "");
        if (!userInfo.equals("")) {
            GOUserEntity user = JJsonUtils.getUserEntityFromJson(userInfo);
            return user;
        } else {
            return null;
        }
    }
    @Override
    public void saveUser(GOUserEntity goUserEntity) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", new Gson().toJson(goUserEntity));
        editor.commit();
    }
    public  void saveCategoryDetail(CategoryDetailModel categoryDetailModel){
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson=new Gson();
        String categoryStr=gson.toJson(categoryDetailModel);
        sharedPreferences.edit().putString("category"+categoryDetailModel.getCategory_id(),categoryStr).commit();;
    }
    public rx.Observable<CategoryDetailModel> getCategoryDetail(final String categoryId){
        return rx.Observable.fromCallable(new Callable<CategoryDetailModel>() {
            @Override
            public CategoryDetailModel call() throws Exception {
                SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance().getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
                String  categoryStr=sharedPreferences.getString("category"+categoryId,"");
                Gson gson=new Gson();
                CategoryDetailModel categoryDetailModel=null;
                if(categoryStr!=null&&!"".equals(categoryStr)) {
                    categoryDetailModel = gson.fromJson(categoryStr, CategoryDetailModel.class);
                }
                return categoryDetailModel;
            }
        });
    }
}
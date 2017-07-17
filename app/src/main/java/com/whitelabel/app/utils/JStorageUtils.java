package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.bean.Wishlist;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListLandingPageItemReturnEntity;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/27.
 */
public class JStorageUtils {
    private static final String TAG = "JStorageUtils";
    public static void saveNotificaitionState(Context context,String str){

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("notificaitionState", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("state", str);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    public static String  getNotificaitionState(Context context){
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("notificaitionState", Context.MODE_PRIVATE);
            String str=sharedPreferences.getString("state", "");
            return str;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return "";
    }
    public static void saveOrderListData(Context context,String userId,List<MyAccountOrderOuter> beans){
        if(context==null||beans==null){
            return;
        }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr = gson.toJson(beans);
            editor.putString("order_"+userId, beanStr);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    public static List<MyAccountOrderOuter>  getOrderListData(Context context,String userId){
        if(context==null||TextUtils.isEmpty(userId)){
            return  null;
        }
        List<MyAccountOrderOuter> beans=null;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            String beanStr=sharedPreferences.getString("order_" + userId, "");
            if(!TextUtils.isEmpty(beanStr)) {
                beans = JJsonUtils.parseOrderList(beanStr);
            }
            return beans;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return beans;
    }


    public static void saveAddressData(Context context,String userId,List<AddressBook> beans){
        if(context==null||beans==null){
            return;
        }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr = gson.toJson(beans);
            editor.putString("address_"+userId, beanStr);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }

    }

    public static List<AddressBook>  getAddressData(Context context,String userId){
        if(context==null||TextUtils.isEmpty(userId)){
            return  null;
        }
        List<AddressBook> beans=null;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            String beanStr=sharedPreferences.getString("address_" + userId, "");
            if(!TextUtils.isEmpty(beanStr)) {
                JLogUtils.i("JStorageUtils","localStr;"+beanStr);
                beans = JJsonUtils.parseAddressList(beanStr);
            }
            return beans;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return beans;
    }



    /**
     * saveWistList
     * @param context
     * @param userId
     * @param beans
     */
    public static void saveWistListData(Context context,String userId,List<Wishlist> beans){
        if(context==null){
            return;
        }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr="";
            if(beans==null){
                beanStr="";
            }else {
                 beanStr = gson.toJson(beans);
            }
            editor.putString("wistlist_"+userId, beanStr);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }


    /**&
     * getWishlist
     * @param context
     * @param userId
     * @return
     */
    public static List<com.whitelabel.app.model.Wishlist>  getWistListData(Context context,String userId){
        if(context==null||TextUtils.isEmpty(userId)){
            return  null;
        }
        List<com.whitelabel.app.model.Wishlist> beans=null;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            String beanStr=sharedPreferences.getString("wistlist_" + userId, "");
            if(!TextUtils.isEmpty(beanStr)) {
                beans = JJsonUtils.parseWishlist(beanStr);
            }
            return beans;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return beans;
    }


    public  static void saveMainCategoryData(Context context,String categoryId, List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans){
            if(context==null||beans==null){
                return;
            }
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("localMainCategory", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr = gson.toJson(beans);
            editor.putString(categoryId, beanStr);
            editor.commit();
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }


    public static List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> getMainCategoryData(Context context, String categoryId){
        if(context==null){
            return null;
        }
        List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans=null;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("localMainCategory", Context.MODE_PRIVATE);
            String beanStr=sharedPreferences.getString(categoryId, "");
            JLogUtils.i("getMainCategoryData", "==" + beanStr);
            if(!TextUtils.isEmpty(beanStr)) {
                beans = JJsonUtils.parseJsonList1(beanStr);
            }
            return beans;
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return beans;

    }


    public  static void  savaProductListToLocalCartRepository(Context context,List<TMPLocalCartRepositoryProductEntity> beans){
        if (context == null || beans == null ) {
            return;
        }
        try {
            SharedPreferences cartProductSP = context.getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = cartProductSP.edit();
            String beanStrs = gson.toJson(beans);
            editor.putString("productList", beanStrs);
            editor.commit();
        }catch (Exception ex){
            Log.i(TAG,"savaProductListToLocalCartRepository");
            ex.getStackTrace();
        }
    }



    public static  long getProductCountByAttribute(Context context,String id, List<ProductPropertyModel>attributeIds ){
        long productCount=0;
        JLogUtils.i(TAG,"id:"+id);
        SharedPreferences cartProductSP = context.getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
        if(cartProductSP==null){
            productCount=0;
        }
        String productListString = cartProductSP.getString("productList", null);
        if(!JDataUtils.isEmpty(productListString)) {
            ArrayList<TMPLocalCartRepositoryProductEntity> productEntityArrayList;
            Gson gson = new Gson();
            TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>> typeToken = new TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>>() {
            };
            Type type = typeToken.getType();
            JLogUtils.i(TAG,"jsonStr:"+productListString);
            productEntityArrayList = gson.fromJson(productListString, type);
            if(productEntityArrayList!=null){
                JLogUtils.i(TAG,"productEntityArrayList size:"+productEntityArrayList.size());
                for(int i=0;i<productEntityArrayList.size();i++){
                    TMPLocalCartRepositoryProductEntity entity=productEntityArrayList.get(i);
                    if(productEntityArrayList.get(i).getProductId().equals(id)){
                        boolean isAllExit =true;
                        if(attributeIds!=null&&attributeIds.size()>0) {
                             isAllExit = isExist(attributeIds, entity);
                        }
                        if(isAllExit) {
                            productCount = entity.getSelectedQty();
                            break;
                        }
                    }
                }

            }
        }
        return productCount;
    }


    private static boolean isExist(List<ProductPropertyModel> attributeIds, TMPLocalCartRepositoryProductEntity entity) {
        List<Boolean>  booleanList=new ArrayList<>();
        for(int z=0;z<attributeIds.size();z++){
            boolean isExitAttribute=false;
            for(int i=0;i<entity.getOptions().size();i++) {
                if(attributeIds.get(z)!=null&&entity.getOptions()!=null&&attributeIds.get(z).getId().equals(entity.getOptions().get(i).getId())){
                    isExitAttribute=true;
                }
            }
            booleanList.add(isExitAttribute);
        }
        boolean  isAllExit=false;
        for(int i=0;i<booleanList.size();i++){
            if(i==0){
                isAllExit=booleanList.get(i);
            }else{
                isAllExit=isAllExit&&booleanList.get(i);
            }
        }
        return isAllExit;
    }





    public static void addProductToLocalCartRepository(Context context, TMPLocalCartRepositoryProductEntity entity) {
        if (context == null || entity == null || JDataUtils.isEmpty(entity.getProductId())) {
            return;
        }

        SharedPreferences cartProductSP = context.getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
        if (cartProductSP == null) {
            return;
        }

        ArrayList<TMPLocalCartRepositoryProductEntity> productEntityArrayList = new ArrayList<TMPLocalCartRepositoryProductEntity>();
        Gson gson = new Gson();
        String productListString = cartProductSP.getString("productList", null);
        if (!JDataUtils.isEmpty(productListString)) {
            ArrayList<TMPLocalCartRepositoryProductEntity> oldProductEntityArrayList = null;
            try {
                TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>> typeToken = new TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>>() {
                };
                if (typeToken != null && gson != null) {
                    Type type = typeToken.getType();
                    oldProductEntityArrayList = gson.fromJson(productListString, type);
                }
            } catch (Exception ex) {
                JLogUtils.e(TAG, "addProductToLocalCartRepository", ex);
            }
            if (oldProductEntityArrayList != null && oldProductEntityArrayList.size() > 0) {
                productEntityArrayList.addAll(oldProductEntityArrayList);
            }
        }
        boolean isExist=false;
        for(int i=0;i<productEntityArrayList.size();i++){
            TMPLocalCartRepositoryProductEntity  productEntity= productEntityArrayList.get(i);
            if(productEntity.getProductId().equals(entity.getProductId())){
                if(entity.getOptions()!=null&&entity.getOptions().size()>0) {
                    isExist = isExist(entity, productEntity);
                }else{
                    isExist=true;
                }
                if (isExist) {
                    long sumQty = entity.getSelectedQty() + productEntity.getSelectedQty();//
                    productEntity.setSelectedQty(sumQty);
                    productEntity.setQty(entity.getQty());

                    if (sumQty > entity.getQty()) {
                        productEntity.setInStock(0);
                    }
                    break;
                }
            }
        }
        if(!isExist) {
            if(productEntityArrayList.size()==0){
                productEntityArrayList.add(entity);
            }else{
                productEntityArrayList.add(0,entity);
            }
        }
        String savedJsonString = null;
        try {
            savedJsonString = gson.toJson(productEntityArrayList);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "addProductToLocalCartRepository", ex);
        }
        if (!JDataUtils.isEmpty(savedJsonString)) {
            try {
                SharedPreferences.Editor editor = cartProductSP.edit();
                if (editor != null) {
                    editor.putString("productList", savedJsonString);
                }
                editor.commit();
            } catch (Exception ex) {
                JLogUtils.e(TAG, "addProductToLocalCartRepository", ex);
            }
        }
    }


    public static ArrayList<TMPLocalCartRepositoryProductEntity> getProductListFromLocalCartRepository(Context context) {
        ArrayList<TMPLocalCartRepositoryProductEntity> result = new ArrayList<TMPLocalCartRepositoryProductEntity>();
        if (context == null) {
            return result;
        }
        SharedPreferences cartProductSP = context.getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
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
                JLogUtils.e(TAG, "getProductListFromLocalCartRepository", ex);
            }
            if (oldProductEntityArrayList != null && oldProductEntityArrayList.size() > 0) {
                result.addAll(oldProductEntityArrayList);
            }
        }
        return result;
    }




    private static boolean isExist(TMPLocalCartRepositoryProductEntity oldEntity, TMPLocalCartRepositoryProductEntity entity) {
        List<Boolean>  booleanList=new ArrayList<>();
        for(int z=0;z<oldEntity.getOptions().size();z++){
            boolean isExitAttribute=false;
            for(int i=0;i<entity.getOptions().size();i++) {
                if(oldEntity.getOptions().get(z).getId().equals(entity.getOptions().get(i).getId())){
                    isExitAttribute=true;
                }
            }
            booleanList.add(isExitAttribute);
        }
        boolean  isAllExit=false;
        JLogUtils.i("JStorageUtils","jstorageUtils=="+booleanList);
        for(int i=0;i<booleanList.size();i++){
            if(i==0){
                isAllExit=booleanList.get(i);
            }else{
                isAllExit=isAllExit&booleanList.get(i);
            }
        }
        return isAllExit;
    }

    public static void clearLocalCartRepository(Context context) {
        if (context == null) {
            return;
        }

        SharedPreferences cartProductSP = context.getSharedPreferences("localStorageShoppingCartProductList", Context.MODE_PRIVATE);
        if (cartProductSP == null) {
            return;
        }

        try {
            SharedPreferences.Editor editor = cartProductSP.edit();
            if (editor != null) {
                editor.putString("productList", null);
            }
            editor.commit();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "clearLocalCartRepository", ex);
        }
    }

    public static void saveCategoryArrayList(Context context,SVRAppserviceCatalogSearchReturnEntity  list){
       if(context==null ){
           return;
       }
        SharedPreferences cartProductSP = context.getSharedPreferences("CatalogSearchReturnEntity", Context.MODE_PRIVATE);
        Gson gson =new Gson();
        SharedPreferences.Editor editor = cartProductSP.edit();
        try {
            String returnEntityStr = gson.toJson(list);
            editor.putString("catalogList", returnEntityStr);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        editor.commit();
    }

    public static SVRAppserviceCatalogSearchReturnEntity   getCategoryArrayList(Context context){
        SVRAppserviceCatalogSearchReturnEntity entity=null;
        if(context==null ){
            return entity;
        }
        SharedPreferences cartProductSP = context.getSharedPreferences("CatalogSearchReturnEntity", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String categoryListStr= cartProductSP.getString("catalogList", "");

        try {
            entity = gson.fromJson(categoryListStr, SVRAppserviceCatalogSearchReturnEntity.class);
        }catch(Exception ex){
            ex.getStackTrace();
        }
        return entity;
    }


    public static boolean showAppGuide1(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide1",Context.MODE_PRIVATE);
        boolean showGuide= shared.getBoolean("appGuide1", false);
        return !showGuide;
    //     return false;
    }
    public static void notShowGuide1(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide1",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("appGuide1",true);
        editor.commit();
    }

    public static boolean showAppGuide2(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide2",Context.MODE_PRIVATE);
        boolean showGuide= shared.getBoolean("appGuide2", false);
        return !showGuide;
       // return false;
    }
    public static void notShowGuide2(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide2",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("appGuide2",true);
        editor.commit();
    }
    public static boolean showAppGuide3(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide3",Context.MODE_PRIVATE);
        boolean showGuide= shared.getBoolean("appGuide3", false);
        return !showGuide;
       // return false;
    }
    public static void notShowGuide3(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide3",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("appGuide3",true);
        editor.commit();
    }
    public static boolean showAppGuide4(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide4",Context.MODE_PRIVATE);
        boolean showGuide= shared.getBoolean("appGuide4", false);
        return !showGuide;
      //  return false;
    }
    public static void notShowGuide4(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide4",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("appGuide4",true);
        editor.commit();
    }
    public static boolean showAppGuide5(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide5",Context.MODE_PRIVATE);
        boolean showGuide= shared.getBoolean("appGuide5", false);
        return !showGuide;
     //    return false;
    }
    public static void notShowGuide5(Context context){
        SharedPreferences shared=context.getSharedPreferences("appGuide5",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("appGuide5",true);
        editor.commit();
    }

    public static void notShowAppRate(Context context){
        SharedPreferences shared=context.getSharedPreferences("appRate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= shared.edit();
        editor.putBoolean("appRate",false);
        editor.commit();
    }
    public static boolean isShowAppRate(Context context){
        SharedPreferences shared=context.getSharedPreferences("appRate",Context.MODE_PRIVATE);
        boolean showAppRate= shared.getBoolean("appRate", true);
        return showAppRate;
    }
    public static void clickDelayShow(Context context){
        SharedPreferences shared=context.getSharedPreferences("ClickDelayShow",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= shared.edit();
        editor.putBoolean("ClickDelayShow",true);
        editor.commit();
    }
    public static boolean isClickDelayShow(Context context){
        SharedPreferences shared=context.getSharedPreferences("ClickDelayShow",Context.MODE_PRIVATE);
        boolean isClick= shared.getBoolean("ClickDelayShow", false);
        return isClick;
    }
    /**
     * 获取mSplashScreen标记
     */
    public static boolean getToSplashScreenMark(Context context){
        SharedPreferences shared = context.getSharedPreferences("SplashScreen", Activity.MODE_PRIVATE);
        boolean  mSplashScreen = shared.getBoolean("SplashScreen", false);
        return mSplashScreen;
    }

    /**
     * 保存SplashScreen标记
     * @param context
     */
    public static void saveToSplashScreenMark(Context context){
        SharedPreferences shared = context.getSharedPreferences("SplashScreen", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("SplashScreen", true);
        editor.apply();

    }
    public static void saveAppVersion(){
        SharedPreferences shared = WhiteLabelApplication.getInstance().getSharedPreferences("AppVersion", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("appVersion", JToolUtils.getAppVersion());
        editor.apply();
    }
    public static String getAppVersion(){
        SharedPreferences shared = WhiteLabelApplication.getInstance().getSharedPreferences("AppVersion", Activity.MODE_PRIVATE);
        String appVersion="";
        if(shared!=null) {
            appVersion= shared.getString("appVersion", "");
        }
       return appVersion;
    }




    //category  tree
    public static final String isClick="click";

    public static final String isOtherClick="otherClick";

    public static final String isOtherRightClick="otherRightClick";
    public static boolean[] getClick(Context context,int num){
        SharedPreferences preference= context.getSharedPreferences(isClick,Context.MODE_PRIVATE);
        boolean[] click=new boolean[num];
        for (int i=0;i<num;i++){
            click[i]=preference.getBoolean("myClick"+i, false);
        }
        return click;
    }
    public static void saveClick(Context context,boolean[] click){
        SharedPreferences preference= context.getSharedPreferences(isClick,Context.MODE_PRIVATE);
        for (int i=0;i<click.length;i++){
            preference.edit().putBoolean("myClick"+i,click[i]).commit();
        }

    }
    public static void saveOtherClick(Context context,boolean[] click){
        SharedPreferences preference= context.getSharedPreferences(isOtherClick,Context.MODE_PRIVATE);
        for (int i=0;i<click.length;i++){
            preference.edit().putBoolean("myOtherClick"+i,click[i]).commit();
        }

    }
    public static boolean[] getOtherClick(Context context,int num){
        SharedPreferences preference= context.getSharedPreferences(isOtherClick,Context.MODE_PRIVATE);
        boolean[] click=new boolean[num];
        for (int i=0;i<num;i++){
            click[i]=preference.getBoolean("myOtherClick"+i, false);
        }
        return click;
    }

    public static void saveOtherRigthtClick(Context context,boolean[] click){
        SharedPreferences preference= context.getSharedPreferences(isOtherRightClick,Context.MODE_PRIVATE);
        for (int i=0;i<click.length;i++){
            preference.edit().putBoolean("myOtherRightClick"+i,click[i]).commit();
        }

    }
    public static boolean[] getOtherRightClick(Context context,int num){
        SharedPreferences preference= context.getSharedPreferences(isOtherRightClick,Context.MODE_PRIVATE);
        boolean[] click=new boolean[num];
        for (int i=0;i<num;i++){
            click[i]=preference.getBoolean("myOtherRightClick"+i, false);
        }
        return click;
    }
}



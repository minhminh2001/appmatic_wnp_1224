package com.whitelabel.app.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;

import java.util.ArrayList;

public class ApplicationConfigurationEntity {
    private String httpServerAddress;
    private String httpGlobalParameter;
    private String dlImageServerAddress;
    private String dlImageServerGlobalParameter;
    private String ulImageServerAddress;
    private String ulImageServerGlobalParameter;
    private GOUserEntity user;
    private ConfigModel configModel;
    private ArrayList<GOThirdPartyUserEntity> thirdPartyUsers;
    private GOStoreViewEntity storeView;
    private GOCurrencyEntity currency;
    private SVRAppserviceCatalogSearchReturnEntity categoryArrayList;

    private ApplicationConfigurationEntity() {
        user = null;
        thirdPartyUsers = new ArrayList<GOThirdPartyUserEntity>();
    }
    public static ApplicationConfigurationEntity getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        static final ApplicationConfigurationEntity INSTANCE = new ApplicationConfigurationEntity();
    }
    private Object readResolve() {
        return getInstance();
    }

    public ConfigModel getThemeConfig(){
        return configModel;
    }
    public void setConfigColor(String themeColor,String navBarColor,String buttonPressColor){
        if(configModel!=null){
            JLogUtils.i("ray","====================111111============");
            configModel.setPrimaryColor(themeColor);
            configModel.setSecondaryColor(navBarColor);
            configModel.setButtonPressColor(buttonPressColor);
        }
    }
    public void init(Context context) {
        SVRAppserviceCatalogSearchReturnEntity entity = JStorageUtils.getCategoryArrayList(context);
        GemfiveApplication.getAppConfiguration().setCategoryArrayList(entity);
        configModel=new ConfigModel();
        httpServerAddress = GlobalData.serviceRequestUrl;
        JLogUtils.i("aaa", "init=" + httpServerAddress);
        httpGlobalParameter = "";
        dlImageServerAddress = GlobalData.downloadImageUrl;
        dlImageServerGlobalParameter = "";
        ulImageServerAddress = GlobalData.upLoadFileUrl;
        ulImageServerGlobalParameter = "";
        storeView = new GOStoreViewEntity();
        storeView.setId("1");
        storeView.setName("English");
        currency = new GOCurrencyEntity();
        currency.setId(1);
        currency.setName("RM");
    }

    public String getHttpServerAddress() {
        JLogUtils.i("aaa", "ApplicationConfigurationEntity ->getServerAddress()=" + httpServerAddress);
        return httpServerAddress;
    }

    public void setHttpServerAddresst(String HttpService) {
        this.httpServerAddress = HttpService;
    }

    public String getHttpGlobalParameter() {
        return httpGlobalParameter;
    }

    public String getDlImageServerAddress() {
        return dlImageServerAddress;
    }

    public String getDlImageServerGlobalParameter() {
        return dlImageServerGlobalParameter;
    }

    public String getUlImageServerAddress() {
        return ulImageServerAddress;
    }

    public String getUlImageServerGlobalParameter() {
        return ulImageServerGlobalParameter;
    }

    public boolean isSignIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("user_info", "");
        if (!userInfo.equals("")) {
            user = JJsonUtils.getUserEntityFromJson(userInfo);
            return true;
        } else {
            return false;
        }
    }

    public void signIn(Context context, SVRAppServiceCustomerLoginReturnEntity loginReturnEntity) {
        if (loginReturnEntity == null || JDataUtils.isEmpty(loginReturnEntity.getId())) {
            return;
        }
        user = new GOUserEntity();
        user.setId(loginReturnEntity.getId());
        user.setSessionKey(loginReturnEntity.getSessionKey());
        user.setEmail(loginReturnEntity.getEmail());
        user.setFirstName(loginReturnEntity.getFirstName());
        user.setLastName(loginReturnEntity.getLastName());
        user.setWishListItemCount(loginReturnEntity.getWishListItemCount());
        user.setCartItemCount(loginReturnEntity.getCartItemCount());
        user.setLoginType(loginReturnEntity.getLoginType());
        user.setHeadImage(loginReturnEntity.getHeadImage());
        user.setNewsletterSubscribed(loginReturnEntity.getNewsletterSubscribed());
        user.setStatus(loginReturnEntity.getStatus());
        user.setIsEmailLogin(loginReturnEntity.isEmailLogin());
        user.setOrderCount(loginReturnEntity.getOrderCount());
        JLogUtils.i("Allen11", "loginReturnEntity.isEmailLogin()=" + loginReturnEntity.isEmailLogin());

        /**
         * Store user info into local file
         */
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", new Gson().toJson(user));
        editor.commit();

    }

    //编辑个人信息后更新数据
    public void updateDate(Context context, GOUserEntity user) {
        this.user = user;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", new Gson().toJson(user));
        editor.commit();
    }

    //update order number
    public void addToOrder(Context context) {
        this.user.setOrderCount(user.getOrderCount() + 1);
        this.updateDate(context, user);
    }

    //update wishlist number
    public void addToWishlist(Context context) {
        this.user.setWishListItemCount(user.getWishListItemCount() + 1);
        this.updateDate(context, user);
    }

    public void updateWishlist(Context context, int number) {
        this.user.setWishListItemCount(number);
        this.updateDate(context, user);
    }

    public void deleteWishlist(Context context) {
        this.user.setWishListItemCount(user.getWishListItemCount() - 1);
        this.updateDate(context, user);
    }

    public void signIn(Context context, SVRAppserviceCustomerFbLoginReturnEntity thirdPartyLoginReturnEntity) {
        if (thirdPartyLoginReturnEntity == null || JDataUtils.isEmpty(thirdPartyLoginReturnEntity.getId())) {
            return;
        }

        user = new GOUserEntity();
        user.setId(thirdPartyLoginReturnEntity.getId());
        user.setSessionKey(thirdPartyLoginReturnEntity.getSessionKey());
        user.setEmail(thirdPartyLoginReturnEntity.getEmail());
        user.setFirstName(thirdPartyLoginReturnEntity.getFirstName());
        user.setLoginType(thirdPartyLoginReturnEntity.getLoginType());
        user.setLastName(thirdPartyLoginReturnEntity.getLastName());
        user.setWishListItemCount(thirdPartyLoginReturnEntity.getWishListItemCount());
        user.setCartItemCount(thirdPartyLoginReturnEntity.getCartItemCount());
        user.setHeadImage(thirdPartyLoginReturnEntity.getHeadImage());
        user.setNewsletterSubscribed(thirdPartyLoginReturnEntity.getNewsletterSubscribed());
        user.setStatus(thirdPartyLoginReturnEntity.getStatus());
        user.setOrderCount(thirdPartyLoginReturnEntity.getOrderCount());
        /**
         * Store user info into local file
         */
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", new Gson().toJson(user));
        editor.commit();

    }
    public GOUserEntity getUserInfo() {
        user = null;
        SharedPreferences sharedPreferences = GemfiveApplication.getInstance().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String jsonUserInfo = sharedPreferences.getString("user_info", "");
        if (JDataUtils.isEmpty(jsonUserInfo)) {
            return user;
        } else {
            user = new Gson().fromJson(jsonUserInfo, GOUserEntity.class);
            return user;

        }
    }
    /**
     * Get user info from local
     *
     * @param context
     * @return
     */
    public GOUserEntity getUserInfo(Context context) {
        if (context == null) {
            return user;
        }
        user = new GOUserEntity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String jsonUserInfo = sharedPreferences.getString("user_info", "");
        if (JDataUtils.isEmpty(jsonUserInfo)) {
            return user;
        } else {
            user = new Gson().fromJson(jsonUserInfo, GOUserEntity.class);
            return user;
        }

    }

    public void updateUserBasicInfo(String firstName, String lastName, String email) {
        if (user == null || JDataUtils.isEmpty(user.getId())) {
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
    }

    public void signOut(Activity activity) {
        if (user != null) {
            user = new GOUserEntity();
        }

        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", "");
        editor.commit();
    }

    public void signOut(FragmentActivity activity) {
        if (user != null) {
            user = new GOUserEntity();
        }

        SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", "");
        editor.commit();
    }

    public final GOUserEntity getUser() {
        return user;
    }

    public GOStoreViewEntity getStoreView() {
        return storeView;
    }

    public void setStoreView(GOStoreViewEntity storeView) {
        this.storeView = storeView;
    }

    public GOCurrencyEntity getCurrency() {
        if (currency == null) {
            currency = new GOCurrencyEntity();
        }
        currency.setId(1);
        currency.setName("RM");
        return currency;
    }

    public void setCurrency(GOCurrencyEntity currency) {
        this.currency = currency;
    }

    public SVRAppserviceCatalogSearchReturnEntity getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(SVRAppserviceCatalogSearchReturnEntity categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}

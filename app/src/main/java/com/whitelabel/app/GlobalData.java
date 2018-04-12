package com.whitelabel.app;

import android.content.Context;

import com.whitelabel.app.utils.JToolUtils;
/**
 * Created by Administrator on 2016/12/20.
 */
public class GlobalData {
    public static String oneAll_Subdomain="";
    public static String appVersion = "";
    public static String serviceVersion = "";
    public static String appName;
    public static String jumpMarketUrl;
    public static String authName;
    public static String authPwd;
    public static String gcmSendId;
    public static String checkoutHashKey;
    public static String gaTrackId;
    public static String facebookId;
    public static String serviceRequestUrl;
    public static String downloadImageUrl;
    public static String upLoadFileUrl;
    public static String downloadImagePath;
    public static String downloadImageProductPath;
    public static String uploadFilePath;
    public static String mockUrl;
    public static  String  apiVersion;
    public static  String apiKey="";
    public static  String buildId="";
    public static String appKey="";
    public static String  creditCardPaymentUrl="";
    public static  String  imageBaseUrl;
    // if true, all https connections are valid regardless of certificate validity
    public static boolean allowInvalidSSLTLS = false;
    private final static String pathSeparator = "/";
    public static String useHlb = "1";  //1  userHlb
    public static void init(Context context) {
        serviceVersion = BuildConfig.SERVICE_VERSION;
        appName = context.getResources().getString(R.string.app_name);
        jumpMarketUrl = BuildConfig.JUMP_MARKET_URI;
//        authName = context.getResources().getString(R.string.auth_name);
//        authPwd = context.getResources().getString(R.string.auth_pwd);
        gcmSendId = BuildConfig.GCM_SENDER_ID;
        appVersion = JToolUtils.getAppVersionName();
        oneAll_Subdomain = BuildConfig.TWITTER_SUBDOMAIN;
        appKey = BuildConfig.APP_KEY;
        gaTrackId = BuildConfig.GA_ID;
        apiVersion  = BuildConfig.API_VERSION;
        apiKey = BuildConfig.API_KEY;
        buildId = BuildConfig.BUILD_ID;
        facebookId = BuildConfig.FACEBOOK_ID;
        mockUrl = BuildConfig.MOCK_URL;
        serviceRequestUrl = BuildConfig.BASE_URL;
        imageBaseUrl = BuildConfig.IMAGE_URL;
        creditCardPaymentUrl = serviceRequestUrl + BuildConfig.PAYMENT_CREDITCARD_URL;
        downloadImagePath = BuildConfig.DOWNLOAD_IMAGE_PATH;
        downloadImageProductPath = BuildConfig.DOWNLOAD_IMAGE_PRODUCT_PATH;
        uploadFilePath = BuildConfig.UPLOAD_FILE_PATH;
        downloadImageUrl = imageBaseUrl + pathSeparator + downloadImagePath;
        upLoadFileUrl = serviceRequestUrl + pathSeparator + uploadFilePath;

    }

    /**
     * add Authorization ,Does not contain a live environment
     */
    private static String getAuthorizationUrl(String requestUrl) {
        int firstEndIndex = requestUrl.indexOf("://") + 3;
        String firstPart = requestUrl.substring(0, firstEndIndex);
        String lastPart = requestUrl.substring(firstEndIndex, requestUrl.length());
        requestUrl = firstPart + authName + ":" + authPwd + "@" + lastPart;
        return requestUrl;
    }

    public static void updateGlobalData(String serviceAddress, String downloadImageUrl, String updateLoadImage, String hashKey) {
        serviceRequestUrl = serviceAddress;
        GlobalData.downloadImageUrl = downloadImageUrl;
        upLoadFileUrl = updateLoadImage + pathSeparator + uploadFilePath;
        checkoutHashKey = hashKey;
    }
}

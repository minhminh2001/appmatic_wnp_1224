package com.whitelabel.app.utils;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Administrator on 2016/7/28.
 */
public class FacebookEventUtils {
    private static FacebookEventUtils mFacebookEventUtils;
    public final static String FACEBOOK_EVENT_REGISTION_TYPE_EMAIL = "email";
    public final static String FACEBOOK_EVENT_REGISTION_TYPE_FACEBOOK = "facebook";
    public final static String FACEBOOK_EVENT_SEARCH_SUCCESS="1";
    public final static String FACEBOOK_EVENT_SEARCH_FAILD="0";
    public final static String FACEBOOK_EVENT_CUSTOM_SAVE_SHIPPING_EVENT="Save Shipping Event";
    public final static String FACEBOOK_EVENT_CUSTOM_PLACE_ORDER="Place Order";

    public final static String FACEBOOK_EVENT_CUSTOM_PURCHASE_EVENT_FAIL="Purchase Event Fail";

    public final static String FACEBOOK_EVENT_CUSTOM_DEEPLINK="facebook_deep_link";

    private FacebookEventUtils() {

    }
    public static FacebookEventUtils getInstance() {
        if (mFacebookEventUtils == null) {
            synchronized (FacebookEventUtils.class) {
                if (mFacebookEventUtils == null) {
                    mFacebookEventUtils = new FacebookEventUtils();
                }
            }
        }
        return mFacebookEventUtils;
    }


    public void customLog(Context context,String type){
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent(type);
    }

    public void facebookEventRegSuccess(Context context, String type) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, type);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, parameters);
    }


    public void facebookEventAddedToCart(Context context, String productId, double price) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "MYR");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productId);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price,
                parameters);
    }

     public void facebookEventProductDetail(Context context, String productId, double price) {
         AppEventsLogger logger = AppEventsLogger.newLogger(context);
         Bundle parameters = new Bundle();
         parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "MYR");
         parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
         parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productId);
         logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, price,
                 parameters);
     }


    public void facebookEventPaymentInfo(Context context){
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_SUCCESS, "1");
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO,
                parameters);
    }


    public void facebookEventAddedToWistList(Context context, String productId, double price) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "MYR");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productId);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, price, parameters);
    }

    public void facebookEventInitiedCheckout(Context context, int productNum, String id, double price) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "MYR");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, productNum);
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
        parameters.putString(AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE, "1");
        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, price, parameters);

    }


    public void facebookEventCheckoutSuccess(Context context, int productNum, String id, double price) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "MYR");
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, productNum);
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, id);
        logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, price, parameters);

    }



    public void facebookEventSearch(Context context, String searchStr,String isSuccess) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, searchStr);
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product");
        parameters.putString(AppEventsConstants.EVENT_PARAM_SUCCESS, isSuccess);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, parameters);
    }
}





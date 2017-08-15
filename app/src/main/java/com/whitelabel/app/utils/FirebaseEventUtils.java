package com.whitelabel.app.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;


/**
 * Created by Administrator on 2016/10/28.
 */
public class FirebaseEventUtils {
    public final static String lOGIN_EMAIL ="Email";
    public final static String LOGIN_FACEBOOK ="Facebook";
    public final static String LOGIN_GOOGLE ="Google";
    private static FirebaseEventUtils  firebaseEventUtils;
    public static FirebaseEventUtils getInstance() {
        if (firebaseEventUtils == null) {
            synchronized (FirebaseEventUtils.class) {
                if (firebaseEventUtils == null) {
                    firebaseEventUtils = new FirebaseEventUtils();
                }
            }
        }
        return firebaseEventUtils;
    }
    /**
     * search
     * @param keyword
     */
    public void ecommerceSearchResult(Context context,String keyword){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,handlerStr(keyword+""));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS,bundle);
    }

    public void ecommerceViewItemList(Context context,String  itemCategory){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,handlerStr(itemCategory));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST,bundle);
    }
    /**
     * pdp  page
     * @param itemId
     */
    public void ecommerceViewItem(Context context,String  itemId,String itemName,String itemCategory,String quantity,String price,String value){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,itemId);
        bundle.putString(FirebaseAnalytics.Param.QUANTITY,quantity);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,handlerStr(itemCategory));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,handlerStr(itemName));
        bundle.putString(FirebaseAnalytics.Param.PRICE,price);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY,"MYR");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM,bundle);
    }

    public String handlerStr(String str){

        String finalStr="";
        try {
            if(str==null){
                return finalStr;
            }
            if (str.length() > 35) {
                finalStr = str.substring(0, 33);
                finalStr += "...";
            } else {
                finalStr = str;
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return  finalStr;
    }
    /**
     * VALUE:  subtotal
     */
    public void ecommerceAddToCart(Context context,String  quantity,String itemCategory,String itemName,String itemId,String value,String price ){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.QUANTITY,quantity);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,handlerStr(itemCategory));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,handlerStr(itemName));
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,itemId);
        bundle.putString(FirebaseAnalytics.Param.PRICE,price);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY,"MYR");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART,bundle);
    }
    public void ecommerceBeginCheckout(Context context,String value){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY,"MYR");
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
    }
    public void ecommercePurchase(Context context,String value,String shipping,String transactionId){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY,"MYR");
        bundle.putString(FirebaseAnalytics.Param.SHIPPING,shipping);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID,transactionId);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);
    }
    public void ecommerceAddWishlist(Context context,String categroy,String name,String id,String price){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY,"MYR");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY,handlerStr(categroy));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,handlerStr(name));
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,id);
        bundle.putString(FirebaseAnalytics.Param.PRICE,price);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);
    }

    public void ecommerceAddPaymentInfo(Context context){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle);
    }


    public void allAppSignUp(Context context,String method){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD,method);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
    }
    public void allAppSearch(Context context,String term){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,term+"");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }
    public void allAppShare(Context context,String itemId,String contentType){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,itemId);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }
    public void allAppSpendVirtualCurrency(Context context,String itemName,String virtualCurrencyName,String value){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,handlerStr(itemName));
        bundle.putString(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME,virtualCurrencyName);
        bundle.putString(FirebaseAnalytics.Param.VALUE,value);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, bundle);
    }

    private final  String EVENT_SIGNUP="sign_up";
    private final String EVENT_NOTIFICATION_FOREDGROUND="notification_foreground";
    private final String EVENT_NOTIFICATION_BACKGROUND="notification_background";


    public void customizedSignIn(Context context,String method){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("method",method);
        String EVENT_SIGNIN = "sign_in";
        mFirebaseAnalytics.logEvent(EVENT_SIGNIN, bundle);
    }

    public void customizedSignOut(Context context,String method){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("method",method);
        String EVENT_SIGNOUT = "sign_out";
        mFirebaseAnalytics.logEvent(EVENT_SIGNOUT, bundle);
    }

    public void customizedViewCurationDetail(Context context,String title,String id){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("cuation_titile",title);
        bundle.putString("curation_id",id);
        String EVENT_VIEW_CURATION_DETAIL = "view_curation_detail";
        mFirebaseAnalytics.logEvent(EVENT_VIEW_CURATION_DETAIL, bundle);
    }

    public void customizedViewCurationGroup(Context context,String category){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("category_name",handlerStr(category));
        String EVENT_VIEW_CURATION_GROUP = "view_curation_list";
        mFirebaseAnalytics.logEvent(EVENT_VIEW_CURATION_GROUP, bundle);
    }
    public void customizedAddAddressInfo(Context context){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        String EVENT_ADD_ADDRESS_INFO = "add_address_info";
        mFirebaseAnalytics.logEvent(EVENT_ADD_ADDRESS_INFO, bundle);
    }
    public void customizedBeginCheck(Context context,String coupon,String value,String shipping){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("coupon",coupon);
        bundle.putString("currency","MYR");
        bundle.putString("value",value);
        bundle.putString("shipping",shipping);
        String EVENT_BEGIN_PLACE_ORDER = "begin_place_order";
        mFirebaseAnalytics.logEvent(EVENT_BEGIN_PLACE_ORDER, bundle);
    }

//    public void customizedNotificationForeground(Context context,String notificationTitle){
//        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
//        Bundle bundle=new Bundle();
//        bundle.putString("notifiication_title",notificationTitle);
//        mFirebaseAnalytics.logEvent(EVENT_NOTIFICATION_FOREDGROUND, bundle);
//    }
//
//    public void customizedNotificationBackground(Context context,String notificationTitle){
//        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
//        Bundle bundle=new Bundle();
//        bundle.putString("notifiication_title",notificationTitle);
//        mFirebaseAnalytics.logEvent(EVENT_NOTIFICATION_BACKGROUND, bundle);
//    }

    public void customizedNotificationReceive(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_PUSH_NOTIFICATION_RECEIVE = "push_notification_receive";
        mFirebaseAnalytics.logEvent(EVENT_PUSH_NOTIFICATION_RECEIVE, bundle);
    }

    public void customizedNotificationOpen(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_PUSH_NOTIFICATION_OPEN = "push_notification_open";
        mFirebaseAnalytics.logEvent(EVENT_PUSH_NOTIFICATION_OPEN, bundle);
    }



    public void customizedPushNotificationRedirect(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_PUSH_NOTIFICATION_REDIRECT = "push_notification_redirect";
        mFirebaseAnalytics.logEvent(EVENT_PUSH_NOTIFICATION_REDIRECT, bundle);
    }


    public void customizedPushNotificationDismiss(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_PUSH_NOTIFICATION_DISMISS = "push_notification_dismiss";
        mFirebaseAnalytics.logEvent(EVENT_PUSH_NOTIFICATION_DISMISS, bundle);
    }


    public void customizedInAppNotificationOpen(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_IN_APP_NOTIFICATION_OPEN = "in_app_notification_open";
        mFirebaseAnalytics.logEvent(EVENT_IN_APP_NOTIFICATION_OPEN, bundle);
    }




    public void customizedInAppNotificationRedirect(Context context,String notificationTitle){
        FirebaseAnalytics  mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        Bundle bundle=new Bundle();
        bundle.putString("notifiication_title",notificationTitle);
        String EVENT_IN_APP_NOTIFICATION_REDIRECT = "in_app_notification_redirect";
        mFirebaseAnalytics.logEvent(EVENT_IN_APP_NOTIFICATION_REDIRECT, bundle);
    }

}

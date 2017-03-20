package com.whitelabel.app.handler;

import com.google.gson.Gson;
import com.whitelabel.app.model.AddToCartEntity;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.SVRAppServiceCustomerLogin;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.SVRExceptionReturnEntity;
import com.whitelabel.app.model.ShoppingCartListEntityResult;
import com.whitelabel.app.model.WishlistEntityResult;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

/**
 * Created by imaginato on 2015/6/11.
 */
public class JJsonUtils {
    private static final String TAG = "JJsonUtils";

    public static SVRExceptionReturnEntity getSVRExceptionReturnEntityFromJson(String jsonStr) {
        if (JDataUtils.isEmpty(jsonStr)) {
            return null;
        }

        SVRExceptionReturnEntity entity = null;
        try {
            Gson gson = new Gson();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "SVRExceptionReturnEntity", ex);
        }
        return entity;
    }

    public static SVRAppServiceCustomerLogin getSVRAPPSvrAppServiceCustomerLoginFromJson(String jsonStr) {
        if (JDataUtils.isEmpty(jsonStr)) {
            return null;
        }

        SVRAppServiceCustomerLogin entity = null;
        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, SVRAppServiceCustomerLogin.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPSvrAppServiceCustomerLoginFromJson", ex);
        }
        return entity;
    }



    /**
     * Parse ShoppingCartListEntityResult
     * @param jsonStr
     * @return
     */
    public static ShoppingCartListEntityResult getSVRAPPServiceShoppingCartListEntityResultFromJson(String jsonStr) {
        ShoppingCartListEntityResult entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, ShoppingCartListEntityResult.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceShoppingCartListEntityResultFromJson", ex);
        }
        return entity;
    }
    public static WishlistEntityResult getSVRAPPServiceWishlistEntityResultFromJson(String jsonStr) {
        WishlistEntityResult entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, WishlistEntityResult.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceWishlistEntityResultFromJson", ex);
        }
        return entity;
    }
    public static AddresslistReslut getSVRAPPServiceAddresslistReslutFromJson(String jsonStr) {
        AddresslistReslut entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, AddresslistReslut.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceAddresslistReslutFromJson", ex);
        }
        return entity;
    }

    public static SVRAppserviceProductDetailReturnEntity getSVRAPPServiceProductDetailEntityFromJson(String jsonStr) {
        SVRAppserviceProductDetailReturnEntity entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, SVRAppserviceProductDetailReturnEntity.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceAddresslistReslutFromJson", ex);
        }
        return entity;
    }
    public static AddToCartEntity getSVRAPPServiceAddToCartEntityFromJson(String jsonStr) {
        AddToCartEntity entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr, AddToCartEntity.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceAddToCartEntityFromJson", ex);
        }
        return entity;
    }

    public static AddToWishlistEntity getSVRAPPServiceAddToWishlistEntityFromJson(String jsonStr) {
        AddToWishlistEntity entity = null;

        if (JDataUtils.isEmpty(jsonStr)) {
            return entity;
        }

        try {
            Gson gson = new Gson();
            entity = gson.fromJson(jsonStr,AddToWishlistEntity.class);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getSVRAPPServiceAddToWishlistEntityFromJson", ex);
        }
        return entity;
    }

}

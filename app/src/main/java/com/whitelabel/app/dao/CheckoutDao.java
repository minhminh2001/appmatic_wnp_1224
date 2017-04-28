package com.whitelabel.app.dao;

import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.AddressParameter;
import com.whitelabel.app.model.CheckoutPaymentSaveReturnEntity;
import com.whitelabel.app.model.CustomAnimEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.GetAnimCodeEntity;
import com.whitelabel.app.model.SVRAppserviceSaveBillingEntity;
import com.whitelabel.app.model.SVRAppserviceSaveOrderReturnEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.TreeMap;

/**
 * Created by ray on 2015/10/26.
 */
public class CheckoutDao extends BaseHttp {
    public static final int REQUEST_GETLUCKDRAW = 10001;
    public static final int REQUEST_ERROR = 10002;
    public static final int REQUEST_SAVEBILLING = 60;
    public static final int REQUEST_PLACEORDER = 61;
    public static final int REQUEST_CHANGEORDERSTATUS = 62;
    public static final int REQUEST_SAVEPAYMENT = 63;
    public static final int REQUEST_TRACK = 64;
    public static final int REQUEST_WINCODE = 65;

    public CheckoutDao(String TAG, Handler handler) {
        super(TAG, handler);
    }

    public void getLuckdraw(String sessionKey, String orderId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("order_id", orderId);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/getRandomprizeCount", params, REQUEST_GETLUCKDRAW);
    }



    public void changeOrderStatus(String sessionKey, String orderId,
                                  String method,String productName,String currencyCode,String amount,String id,String state,String createTime) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("orderid", orderId);
        params.put("method",method);
        params.put("paypal[product_name]",productName);
        params.put("paypal[currency_code]",currencyCode);
        params.put("paypal[amount]",amount);
        params.put("paypal[id]",id);
        params.put("paypal[state]",state);
        params.put("paypal[create_time]",createTime);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/changeOrderStatus", params, REQUEST_CHANGEORDERSTATUS);
    }





    public void saveBilling(String session, AddressParameter addressParameter) {
        params = new TreeMap<>();
        params.put("session_key", session);
        if (!TextUtils.isEmpty(addressParameter.getAddressId())) {
            params.put("shipping_address_id", addressParameter.getAddressId());
        }

        if (!TextUtils.isEmpty(addressParameter.getFirstname())) {
            params.put("shipping[firstname]", addressParameter.getFirstname());
        }

        if (!TextUtils.isEmpty(addressParameter.getLastname())) {
            params.put("shipping[lastname]", addressParameter.getLastname());
        }

        if (!TextUtils.isEmpty(addressParameter.getCountryId())) {
            params.put("shipping[country_id]", addressParameter.getCountryId());
        }
        if (!TextUtils.isEmpty(addressParameter.getStreet0())) {
            params.put("shipping[street][0]", addressParameter.getStreet0());
        }


        if (!TextUtils.isEmpty(addressParameter.getStreet1())) {
            params.put("shipping[street][1]", addressParameter.getStreet1());
        }

        if (!TextUtils.isEmpty(addressParameter.getPostcode())) {
            params.put("shipping[postcode]", addressParameter.getPostcode());
        }

        if (!TextUtils.isEmpty(addressParameter.getCity())) {
            params.put("shipping[city]", addressParameter.getCity());
        }

        if (!TextUtils.isEmpty(addressParameter.getRegionId())) {
            params.put("shipping[region_id]", addressParameter.getRegionId());
        }
        if (!TextUtils.isEmpty(addressParameter.getRegion())) {
            params.put("shipping[region]", addressParameter.getRegion());
        }
        if (!TextUtils.isEmpty(addressParameter.getTelephone())) {
            params.put("shipping[telephone]", addressParameter.getTelephone());
        }
        params.put("shipping[save_in_address_book]", "1");
        params.put("shipping[same_as_billing]", "1");

        if (!TextUtils.isEmpty(addressParameter.getAddressId())) {
            params.put("billing_address_id", addressParameter.getAddressId());
        }
        if (!TextUtils.isEmpty(addressParameter.getFirstname())) {
            params.put("billing[firstname]", addressParameter.getFirstname());
        }

        if (!TextUtils.isEmpty(addressParameter.getLastname())) {
            params.put("billing[lastname]", addressParameter.getLastname());
        }

        if (!TextUtils.isEmpty(addressParameter.getCountryId())) {
            params.put("billing[country_id]", addressParameter.getCountryId());
        }

        if (!TextUtils.isEmpty(addressParameter.getStreet0())) {
            params.put("billing[street][0]", addressParameter.getStreet0());
        }
        if (!TextUtils.isEmpty(addressParameter.getStreet1())) {
            params.put("billing[street][1]", addressParameter.getStreet1());
        }
        if (!TextUtils.isEmpty(addressParameter.getPostcode())) {
            params.put("billing[postcode]", addressParameter.getPostcode());
        }
        if (!TextUtils.isEmpty(addressParameter.getCity())) {
            params.put("billing[city]", addressParameter.getCity());
        }

        if (!TextUtils.isEmpty(addressParameter.getRegionId())) {
            params.put("billing[region_id]", addressParameter.getRegionId());
        }
        if (!TextUtils.isEmpty(addressParameter.getRegion())) {
            params.put("billing[region]", addressParameter.getRegion());
        }
        if (!TextUtils.isEmpty(addressParameter.getTelephone())) {
            params.put("billing[telephone]", addressParameter.getTelephone());
        }
//        params.put("store_id","3");
        params.put("billing[save_in_address_book]", "1");
        params.put("billing[use_for_shipping]", "1");
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/saveBilling", params, REQUEST_SAVEBILLING);
    }

    public void savePlaceOrder(String session) {
        params = new TreeMap<>();
        params.put("session_key", session);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/saveOrder", params, REQUEST_PLACEORDER);
    }


    public void sendTrack(String sessionkey, String name, String email) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        params.put("name", name);
        params.put("email", email);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/trackInformation", params, REQUEST_TRACK);
    }

    public void getWinCode(String sessionKey, String orderId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("order_id", orderId);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/getRandomprize", params, REQUEST_WINCODE);

    }


    public void savePayment(String sessionKey, String paymentMethod, String molpayType,
                            String paymentCountry, String ccNumber, String ccId, String ccExpMonth, String ccExpYear,
                            String ccOwner, String ccType, String ccBank, String ccSecureHash, String nonce) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("payment[method]", paymentMethod);
        if (!TextUtils.isEmpty(molpayType)) {
            params.put("payment[molpay_type]", molpayType);
        }
        if (!TextUtils.isEmpty(paymentCountry)) {
            params.put("payment[cc_country]", paymentCountry);
        }
        if (!TextUtils.isEmpty(ccNumber)) {
            params.put("payment[cc_number]", ccNumber);
        }
        if (!TextUtils.isEmpty(ccId)) {
            params.put("payment[cc_cid]", ccId);
        }
        if (!TextUtils.isEmpty(ccExpMonth)) {
            params.put("payment[cc_exp_month]", ccExpMonth);
        }
        if (!TextUtils.isEmpty(ccExpYear)) {
            params.put("payment[cc_exp_year]", ccExpYear);
        }
        if (!TextUtils.isEmpty(ccOwner)) {
            params.put("payment[cc_owner]", ccOwner);
        }
        if (!TextUtils.isEmpty(ccType)) {
            params.put("payment[cc_type]", ccType);
        }
        if (!TextUtils.isEmpty(ccBank)) {
            params.put("payment[cc_bank]", ccBank);
        }

        if (!TextUtils.isEmpty(ccSecureHash)) {
            params.put("payment[secure_hash]", ccSecureHash);
        }

        if (!TextUtils.isEmpty(nonce)) {
            params.put("payment[nonce]", nonce);
        }
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/savePayment", params, REQUEST_SAVEPAYMENT);
    }


    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", requestCode, response);
        switch (requestCode) {
            case REQUEST_GETLUCKDRAW:
                if (isOk(response)) {
                    CustomAnimEntity entity = JJsonUtils.parseJsonObj(response, CustomAnimEntity.class);
                    postHandler(REQUEST_GETLUCKDRAW, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(REQUEST_GETLUCKDRAW, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SAVEBILLING:
                if (isOk(response)) {
                    SVRAppserviceSaveBillingEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceSaveBillingEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_PLACEORDER:
                if (isOk(response)) {
                    SVRAppserviceSaveOrderReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceSaveOrderReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHANGEORDERSTATUS:
                if (isOk(response)) {
                    postHandler(requestCode, null, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SAVEPAYMENT:
                if (isOk(response)) {
                    CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity = JJsonUtils.parseJsonObj(response, CheckoutPaymentSaveReturnEntity.class);
                    postHandler(requestCode, paymentSaveReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }

                break;
            case REQUEST_TRACK:
                if (isOk(response)) {

                } else {

                }
                break;
            case REQUEST_WINCODE:
                if (isOk(response)) {
                    GetAnimCodeEntity entity = JJsonUtils.parseJsonObj(response, GetAnimCodeEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean, RESPONSE_FAILED);
                }
                break;
        }
    }

    @Override

    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(),errorType);
    }
}

package com.whitelabel.app.dao;

import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.CheckoutGetPaymentListEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.KeyValueBean;
import com.whitelabel.app.model.StoreCreditBean;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ray on 2015/10/15.
 */
public class OtherDao extends BaseHttp {
    public static final int RQEUST_PAYMENT_METHOD = 3;
    public static final int REQUEST_SAVEBANK = 4;
    public static final int REQUEST_ERROR = 10;
    public static final int REQUEST_CRASH = 101;
    public static final int REQUEST_TRACK = 102;
    public static final int REQUEST_SUBSCRIBER = 103;
    public static final int REQUEST_LOGOUT = 104;
    public static final int REQUEST_STORECREDIT = 105;

    public OtherDao(String TAG, Handler handler) {
        super(TAG, handler);
    }

    @Override
    public void requestHttp(BaseHttp.HTTP_METHOD method, String url, TreeMap map, int requestCode) {
        super.requestHttp(method, url, map, requestCode);
    }

    public void getPaymentList(String sessionkey) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/getPaymentList", params, RQEUST_PAYMENT_METHOD);
    }

    public void sendCrash(ConcurrentHashMap<String, String> svrParams) {
        params = new TreeMap<>();
        for (ConcurrentHashMap.Entry<String, String> entry : svrParams.entrySet()) {
            if (entry != null) {
                final String keystring = entry.getKey();
                final String valuestring = entry.getValue();
                if (!JDataUtils.isEmpty(keystring) && !JDataUtils.isEmpty(valuestring)) {
                    params.put(keystring, valuestring);
                }
            }
        }
        requestHttp(HTTP_METHOD.POST, "appservice/crash/log", params, REQUEST_CRASH);
    }

    public void getStoreCredit(String sessionkey) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        requestHttp(HTTP_METHOD.GET, "appservice/customer/getStoreCreditInfo", params, REQUEST_STORECREDIT);

    }

    public void changeSubscribed(String sessionKey, String newsletterSubscribed) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("newsletterSubscribed", newsletterSubscribed);
        requestHttp(HTTP_METHOD.POST, "appservice/customer/setNewsletterSubscriberStatus", params, REQUEST_SUBSCRIBER);

    }


    public void sendTrack(String sessionKey, String name, String email) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        if (TextUtils.isEmpty(name)) {
            params.put("name", "");
        } else {
            params.put("name", name);
        }
        if (TextUtils.isEmpty(email)) {
            params.put("email", "");
        } else {
            params.put("email", email);
        }
        requestHttp(HTTP_METHOD.POST, "appservice/checkout/trackInformation", params, REQUEST_TRACK);

    }


    public void signOut(String deviceToken, String sessionKey) {
        params = new TreeMap<>();
        if (TextUtils.isEmpty(deviceToken)) {
            params.put("device_token", "");
        } else {
            params.put("device_token", deviceToken);
        }
        params.put("session_key", sessionKey);
        requestHttp(HTTP_METHOD.POST, "appservice/customer/logout", params, REQUEST_LOGOUT);

    }


    public void saveBankTransferConfirm(String id, String sessionkey, String bank_from, String email, String order_no, String transferee, String transferred, String transfer_date, String proof_file) {
//        JLogUtils.i("ray","bank_from==="+bank_from);
//        JLogUtils.i("ray","email======"+email);
//        JLogUtils.i("ray","order_no===="+order_no);
//        JLogUtils.i("ray","transferee=="+transferee);
//        JLogUtils.i("ray","transferred=="+transferred);
//        JLogUtils.i("ray","transfer_date=="+transfer_date);
//        JLogUtils.i("ray","proof_file===="+proof_file);
        params = new TreeMap<>();
        if (!TextUtils.isEmpty(id)) {
            params.put("transfer_id", id);
        }
        params.put("session_key", sessionkey);
        params.put("bank_from", bank_from);
        params.put("email", email);
        params.put("order_no", order_no);
        params.put("transferee", transferee);
        params.put("transferred", transferred);
        params.put("transfer_date", transfer_date);
        params.put("proof_file", proof_file);
        requestHttp(HTTP_METHOD.POST, "appservice/order/saveBanktransferConfirm", params, REQUEST_SAVEBANK);
    }

    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", requestCode, response);
        switch (requestCode) {
            case RQEUST_PAYMENT_METHOD:
                if (isOk(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CheckoutGetPaymentListEntity paymentListEntity = com.whitelabel.app.utils.JJsonUtils.parseJsonObj(response, CheckoutGetPaymentListEntity.class);
                    postHandler(requestCode,paymentListEntity,RESPONSE_SUCCESS);
                }else{
                     ErrorMsgBean bean= com.whitelabel.app.utils.JJsonUtils.parseJsonObj(response,ErrorMsgBean.class);
                    String errorMsg="";
                    if(bean!=null) {
                        errorMsg=bean.getErrorMessage();
                    }
                    postHandler(requestCode, errorMsg , RESPONSE_FAILED);

                }
                break;
            case REQUEST_SAVEBANK:
                if (isOk(response)) {
                    String description = "";
                    String id = "";
                    String cansubmit = "";
                    try {
                        JSONObject obj = new JSONObject(response);
                        description = obj.getString("description");
                        id = obj.getString("transferId");
                        cansubmit = obj.getString("canSubmit");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    KeyValueBean bean = new KeyValueBean();
                    bean.setValue(description);
                    bean.setKey(id);
                    bean.setLabel(cansubmit);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
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
            case REQUEST_SUBSCRIBER:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_LOGOUT:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_STORECREDIT:
                if (isOk(response)) {

                    StoreCreditBean storeCreditBean = com.whitelabel.app.utils.JJsonUtils.parseJsonObj(response, StoreCreditBean.class);
                    postHandler(requestCode, storeCreditBean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }

                break;
        }
    }


    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(),errorType);
    }
}

package com.whitelabel.app.dao;

import android.os.Handler;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppServiceCustomerResetpass;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.TreeMap;

/**
 * Created by imaginato on 2016/3/28.
 */
public class LoginRegisterDao extends BaseHttp {
    public static final int REQUEST_ERROR = 10;
    public static final int REQUEST_GETDATA = 1;
    public static final int REQUEST_BOUNDUSERINFO = 2;
    public static final int REQUEST_FBUSERINFO = 3;

    public LoginRegisterDao(String TAG, Handler handler) {
        super(TAG, handler);
    }

    public void loadDataFromServer(String email) {
        params = new TreeMap<>();
        params.put("email", email);
        requestHttp(HTTP_METHOD.POST, "appservice/customer/forgotpassword", params, REQUEST_GETDATA);
    }

    public void emailBoundUseInfoToLoginRemoteServer(String email, String firstname, String lastname, String fb_id, String fb_has_email, String store_id, String device_token) {
        params = new TreeMap<>();
        params.put("email", email);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("fb_id", fb_id);
        params.put("fb_has_email", fb_has_email);
        params.put("store_id", store_id);
        params.put("device_token", device_token);

        requestHttp(HTTP_METHOD.GET, "appservice/customer/fbLogin", params, REQUEST_BOUNDUSERINFO);
    }

    public void fbUseInfoToLoginRemoteServer(String email, String fb_has_email, String firstname, String lastname, String fb_id, String store_id, String device_token) {
        params = new TreeMap<>();
        params.put("email", email);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("fb_id", fb_id);
        params.put("fb_has_email", fb_has_email);
        params.put("store_id", store_id);
        params.put("device_token", device_token);

        requestHttp(HTTP_METHOD.GET, "appservice/customer/fbLogin", params, REQUEST_FBUSERINFO);
    }

    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", response);
        switch (requestCode) {
            case REQUEST_FBUSERINFO:
                if (isOk(response)) {
                    SVRAppserviceCustomerFbLoginReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCustomerFbLoginReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_BOUNDUSERINFO:
                if (isOk(response)) {
                    SVRAppserviceCustomerFbLoginReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCustomerFbLoginReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETDATA:
                if (isOk(response)) {
                    SVRAppServiceCustomerResetpass entity = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerResetpass.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
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

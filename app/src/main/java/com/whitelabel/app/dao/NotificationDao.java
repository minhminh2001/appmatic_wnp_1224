package com.whitelabel.app.dao;

import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.NotificationAppOpenReturnEntity;
import com.whitelabel.app.model.NotificationCell;
import com.whitelabel.app.model.SVRAppserviceNotificationListReturnEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

/**
 * Created by ray on 2015/10/26.
 */
public class NotificationDao extends BaseHttp {
    public static final int REQUEST_NOTIFICATIONREAD = 101;
    public static final int REQUEST_NOTIFICATIONDETAIL = 102;
    public static final int REQUEST_NOTIFICATION_COUNT = 103;
    public static final int REQUEST_ERROR = 10004;
    public static final int REQUEST_SENDREGISTERATIONTOKEN = 201;
    public static final int REQUEST_GETLIST = 104;

    public NotificationDao(String TAG, Handler handler) {
        super(TAG, handler);
    }
    public void notificationRead(String key, String id) {
        params = new TreeMap<>();
        params.put("session_key", key);
        if(!JDataUtils.isEmpty(id)) {
            params.put("id", id);
        }
        requestHttp(HTTP_METHOD.GET, "appsnotification/notification/read", params, REQUEST_NOTIFICATIONREAD);
    }
    public void sendRegistrationTokenToServer(String sessionKey, String deviceToken) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        if(!JDataUtils.isEmpty(deviceToken)) {
            params.put("device_token", deviceToken);
        }
        requestHttp(HTTP_METHOD.POST, "appservice/app/open", params, REQUEST_SENDREGISTERATIONTOKEN);
    }
    /**
     * @param sessionKey
     * @param id
     * @param read       set read  =0   auto set unread.
     *                   read=1  auto  set read
     */
    public void getNotificationDetail(String sessionKey,String id,String code,String read,String device_token){
        params=new TreeMap<>();
        params.put("session_key",sessionKey);
        params.put("id",id);
        params.put("read",read);
        if(!TextUtils.isEmpty(device_token)) {
            params.put("device_token", device_token);
        }
        requestHttp(HTTP_METHOD.GET,"appservice/notification/"+id+"/"+code,params,REQUEST_NOTIFICATIONDETAIL);
    }

    public void getNotificationDetailCount(String session_key, String device_token) {
        params = new TreeMap<>();
        if (!TextUtils.isEmpty(session_key)) {
            params.put("session_key", session_key);
        }
        params.put("device_token", device_token);
        requestHttp(HTTP_METHOD.GET, "appsnotification/notification/count", params, REQUEST_NOTIFICATION_COUNT);
    }

    public void sendRequestToGetList(String session_key, String page, String pagesize, String device_token) {
        params = new TreeMap<>();
        if (!TextUtils.isEmpty(session_key)) {
            params.put("session_key", session_key);
        }
        String userId="";
        if(WhiteLabelApplication.getAppConfiguration().getUser()!=null) {
            userId = WhiteLabelApplication.getAppConfiguration().getUser().getId();
        }

        params.put("page", page);
        params.put("pagesize", pagesize);
        params.put("device_token", device_token);
        requestHttp(HTTP_METHOD.GET, "appservice/notification/list/"+userId, params, REQUEST_GETLIST, page);
    }

    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.d("response", response);
        switch (requestCode) {
            case REQUEST_GETLIST:
                if (isOkByCode(response)) {
                    SVRAppserviceNotificationListReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceNotificationListReturnEntity.class);
                    entity.setPage(Integer.parseInt((String) object));
                    postHandler(REQUEST_GETLIST, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsg = getErrorMsgBean(response);
                    postHandler(REQUEST_GETLIST, errorMsg.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_NOTIFICATIONREAD:
                if (isOk(response)) {
                    NotificationAppOpenReturnEntity entity = JJsonUtils.parseJsonObj(response, NotificationAppOpenReturnEntity.class);
                    postHandler(REQUEST_NOTIFICATIONREAD, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsg = getErrorMsgBean(response);
                    postHandler(REQUEST_NOTIFICATIONREAD, errorMsg.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_NOTIFICATIONDETAIL:
                if (isOkByCode(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray detailArray = obj.getJSONArray("data");
                       JSONObject jsonObject= detailArray.getJSONObject(0);
                        NotificationCell cell = JJsonUtils.parseJsonObj(jsonObject.toString(), NotificationCell.class);
                        postHandler(REQUEST_NOTIFICATIONDETAIL, cell, RESPONSE_SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ErrorMsgBean errorMsg = getErrorMsgBean(response);
                    postHandler(REQUEST_NOTIFICATIONREAD, errorMsg.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_NOTIFICATION_COUNT:
                if (isOk(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Integer count = obj.getInt("count");
                        postHandler(REQUEST_NOTIFICATION_COUNT, count, RESPONSE_SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ErrorMsgBean errorMsg = getErrorMsgBean(response);
                    postHandler(REQUEST_NOTIFICATIONREAD, errorMsg.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SENDREGISTERATIONTOKEN:
//                NotificationAppOpenReturnEntity notificationAppOpenReturnEntity =JJsonUtils.parseJsonObj(response,NotificationAppOpenReturnEntity.class);
                GOCurrencyEntity goCurrencyEntity=null;
                JsonObject jsonObject=new JsonParser().parse(response).getAsJsonObject();
                try {
                    JsonObject jsonObj = jsonObject.getAsJsonObject("data");
                    String unit=jsonObj.get("unit").getAsString();
                    goCurrencyEntity=new GOCurrencyEntity();
                    goCurrencyEntity.setName(unit);
                }catch (Exception ex){
                    ex.getStackTrace();
                }
                WhiteLabelApplication.getAppConfiguration().getCurrency().setName(goCurrencyEntity.getName());
                break;
        }
    }

    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(),errorType);

    }
}

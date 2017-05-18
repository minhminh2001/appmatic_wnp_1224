package com.whitelabel.app.network;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.utils.JLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by ray on 2015/10/15.
 */
public abstract class BaseHttp {

    public static final int ERROR_TYPE_SERVER = 2;
    public static final int ERROR_TYPE_NET = 1;
    public static final int RESPONSE_SUCCESS = 1;
    public static final int RESPONSE_FAILED = -1;
    private String mUrl;
    protected TreeMap<String, String> params;
    private String TAG;
    protected Handler mHandler;

    public BaseHttp(String TAG, Handler handler) {
        this.TAG = TAG;
        this.mHandler = handler;
    }

    public void requestHttp(HTTP_METHOD method, String url, TreeMap map, int requestCode) {
        if (url == null) {
            throw new RuntimeException("httpUrl is null");
        }
        if (url.substring(0, 1).equals("/")) {
            url = url.substring(1);
        }
        this.mUrl = GlobalData.serviceRequestUrl + url;
        map = addCommonParams(map);
        this.params = map;

        if (HTTP_METHOD.POST == method) {
            post(requestCode, null);
        } else {
            get(requestCode, null);
        }
    }

    public void requestHttp(HTTP_METHOD method, String url, TreeMap map, int requestCode, Object object) {
        if (url == null) {
            throw new RuntimeException("httpUrl is null");
        }
        if (url.substring(0, 1).equals("/")) {
            url = url.substring(1);
        }
        this.mUrl = GlobalData.serviceRequestUrl + url;
        map = addCommonParams(map);
        this.params = map;
        if (HTTP_METHOD.POST == method) {
            post(requestCode, object);
        } else {
            get(requestCode, object);
        }
    }

    public TreeMap addCommonParams(TreeMap map) {
        if (map == null) {
            map = new TreeMap();
        }
        map.put("platformId", "2");//android
        map.put("versionNumber", GlobalData.appVersion);
        map.put("serviceVersion", GlobalData.serviceVersion);
        return map;
    }

    private void post(final int requestCode, final Object object) {
        getPostUrl();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JLogUtils.d(TAG, response);
                onSuccess(requestCode, response, object);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse!=null) {
                    onFalied(requestCode, error, object,ERROR_TYPE_SERVER);
                }else{
                    onFalied(requestCode, error, object,ERROR_TYPE_NET);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("API-VERSION",GlobalData.apiVersion);
                params.put("API-KEY",GlobalData.apiKey);
                params.put("APP-KEY",GlobalData.appKey);
                return params;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        WhiteLabelApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
    }
    //处理get 请求中的空格
    private String formatBlack(String value) {
        String[] values = value.split(" ");
        StringBuffer finalValue = new StringBuffer("");
        for (int i = 0; i < values.length; i++) {
            finalValue.append(URLEncoder.encode(values[i]));
            if (i < values.length - 1) {
                finalValue.append("%20");
            }
        }
        return finalValue.toString();
    }

    //处理get 请求特殊字符转码
    public void formatParams() {
        if (params != null && params.size() != 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    if (value.contains(" ")) {
                        value = formatBlack(value);
                    } else {
                        value = URLEncoder.encode(params.get(key));
                    }
                }
                params.put(key, value);
            }
        }
    }

    private String getMethodGetUrl() {
        String paramUrl = "";
        formatParams();
        if (params != null && params.size() != 0) {
            for (String key : params.keySet()) {
                paramUrl += key + "=" + params.get(key) + "&";
            }
            paramUrl = paramUrl.substring(0, paramUrl.length() - 1);
        }
        if (!mUrl.substring(mUrl.length() - 1, mUrl.length() - 1).equals("?")) {
            mUrl = mUrl + "?" + paramUrl;
        } else {
            mUrl += paramUrl;
        }
        JLogUtils.i("TAG", "get===>params" + mUrl);
        return mUrl;
    }


    public void postHandler(int code, Object obj, int state) {
        if (mHandler != null) {
            Message msg = new Message();
            msg.obj = obj;
            msg.what = code;
            msg.arg1 = state;
            mHandler.sendMessage(msg);

        }
    }

    public void postHandler(int code, Object obj) {
        Message msg = new Message();
        if (obj != null) {
            msg.obj = obj;
        }
        msg.what = code;
        mHandler.sendMessage(msg);
    }

    public void postErrorHandler(int errorCode, int requestCode, Object obj,int errorType) {
        Message msg = new Message();
        if (obj != null) {
            msg.obj = obj;
        }
        msg.what = errorCode;
        msg.arg1 = requestCode;
        //arg2来区分 错误类型
        msg.arg2=errorType;
        mHandler.sendMessage(msg);
    }

    private void get(final int requestCode, final Object obj) {
        mUrl = getMethodGetUrl();
        JLogUtils.d("MyUrl", mUrl);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                mUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JLogUtils.d(TAG, "success" + response.toString());
                onSuccess(requestCode, response, obj);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse!=null) {
                    onFalied(requestCode,error, obj,ERROR_TYPE_SERVER);
                }else{
                    onFalied(requestCode, error, obj,ERROR_TYPE_NET);
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("API-VERSION",GlobalData.apiVersion);
                params.put("API-KEY",GlobalData.apiKey);
                params.put("APP-KEY",GlobalData.appKey);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 1.0f));
        WhiteLabelApplication.getInstance().addToRequestQueue(strReq, TAG);
    }

    protected boolean isOk(String json) {
        int state = 0;
        try {
            JSONObject obj = new JSONObject(json);
            state = obj.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (state == RESPONSE_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    protected  boolean isOkByCode(String json){
        int state=0;
        try {
            JSONObject obj = new JSONObject(json);
            state = obj.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(state==RESPONSE_SUCCESS){
            return true;
        }else{
            return false;
        }

    }

    public void cancelHttpByTag(String tag) {
        WhiteLabelApplication.getInstance().cancelPendingRequests(tag);
    }

    public ErrorMsgBean getErrorMsgBean(String response) {
        ErrorMsgBean bean = com.whitelabel.app.utils.JJsonUtils.parseJsonObj(response, ErrorMsgBean.class);
        if (bean == null) {
            bean = new ErrorMsgBean();
            bean.setErrorMessage("error");
        }
        return bean;
    }

    public abstract void onSuccess(int requestCode, String response, Object object);

    public abstract void onFalied(int requestCode, VolleyError volleyError, Object object,int errorType);

    public void getPostUrl() {
        if (params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            ArrayList<Map.Entry<String, String>> pArray = new ArrayList();
            for (Map.Entry<String, String> entry : entrySet) {
                pArray.add(entry);
            }
            String param = "";
            for (int i = 0; i < pArray.size(); i++) {
                if (i == 0) {
                    param += "?" + pArray.get(0).toString();
                } else {
                    param += "&" + pArray.get(i).toString();
                }
            }
            JLogUtils.d("MyUrl_post", "" + mUrl + param);
        }
    }


    //    public abstract  void
    public enum HTTP_METHOD {
        GET, POST;
    }

}

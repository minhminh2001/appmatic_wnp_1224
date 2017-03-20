package com.whitelabel.app.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpClientRequest {
    private static HttpClientRequest mInstance;
    private static Context mCtx;
    public RequestQueue mRequestQueue;
    public static final String TAG = HttpClientRequest.class.getSimpleName();

    private HttpClientRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized HttpClientRequest getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpClientRequest(context);
        }
        return mInstance;
    }

    /**
     * Returns a Volley request queue for creating network requests
     *
     * @return {@link RequestQueue}
     */
//    /
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(),
                    new OkHttpStack());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public <T> void addRequest(Request<T> request) {

        getRequestQueue().add(request);
    }
}

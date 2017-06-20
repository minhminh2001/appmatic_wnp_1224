package com.whitelabel.app.model;

import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.net.URLEncoder;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by imaginato on 2015/6/11.
 */
public class SVRParameters {

    private final static String TAG = "SVRParameters";

    protected TreeMap<String, String> urlParams;

    public SVRParameters() {
        init();
    }

    public void put(String key, String value) {
        if (!JDataUtils.isEmpty(key) && !JDataUtils.isEmpty(value)) {
            urlParams.put(key, value);
        }
    }

    public void remove(String key) {
        urlParams.remove(key);
    }

    public TreeMap<String, String> getUrlParams() {
        if (urlParams == null) {
            urlParams = new TreeMap<String, String>();
        }
        return urlParams;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            final String key = entry.getKey();
            String value = entry.getValue();
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (Exception ex) {
                JLogUtils.e(TAG, "toString", ex);
            }
            result.append(key);
            result.append("=").append(value);
        }
        return result.toString();
    }

    private void init() {
        urlParams = new TreeMap<String, String>();
    }
}

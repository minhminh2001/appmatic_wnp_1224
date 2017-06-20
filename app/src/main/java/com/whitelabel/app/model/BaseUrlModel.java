package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/20.
 */

public class BaseUrlModel implements Serializable {
    private String  ServiceBaseUrl;

    public String getServiceBaseUrl() {
        return ServiceBaseUrl;
    }

    public void setServiceBaseUrl(String serviceBaseUrl) {
        ServiceBaseUrl = serviceBaseUrl;
    }
}

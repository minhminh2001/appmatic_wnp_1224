package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/4/26.
 */

public class ThirdPartyConfig implements Serializable {
    private  String facebookId;
    private String gcmSendId;
    private String  paypalClientId;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getGcmSendId() {
        return gcmSendId;
    }

    public void setGcmSendId(String gcmSendId) {
        this.gcmSendId = gcmSendId;
    }

    public String getPaypalClientId() {
        return paypalClientId;
    }

    public void setPaypalClientId(String paypalClientId) {
        this.paypalClientId = paypalClientId;
    }
}

package com.whitelabel.app.ui.checkout.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class PaypalPlaceOrderReponse implements Serializable {
    private int status;
    private String url;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

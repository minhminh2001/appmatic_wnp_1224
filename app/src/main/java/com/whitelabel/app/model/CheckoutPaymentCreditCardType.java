package com.whitelabel.app.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/14.
 */
public class CheckoutPaymentCreditCardType extends SVRReturnEntity {

    private int status;
    private HashMap<String,String> cctype;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getCctype() {
        return cctype;
    }

    public void setCctype(HashMap<String, String> cctype) {
        this.cctype = cctype;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2017/5/15.
 */

public class PaypalModel implements Serializable {
    private String sandbox;
    private String production;

    public String getSandbox() {
        return sandbox;
    }

    public void setSandbox(String sandbox) {
        this.sandbox = sandbox;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }
}

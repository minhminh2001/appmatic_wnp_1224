package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class StoreCreditItemBean implements Serializable {
    private String Date;
    private String Type;
    private String Points;
    private String Balance;
    private String Reference;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }
}

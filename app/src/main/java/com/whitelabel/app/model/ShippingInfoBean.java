package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ShippingInfoBean implements Serializable {
    private String westDeliversDays;
    private String eastDeliversDays;
    private String locationNotDelivered;
    private String detailDelivery1;
    private String detailDelivery2;

    public String getWestDeliversDays() {
        return westDeliversDays;
    }

    public void setWestDeliversDays(String westDeliversDays) {
        this.westDeliversDays = westDeliversDays;
    }

    public String getEastDeliversDays() {
        return eastDeliversDays;
    }

    public void setEastDeliversDays(String eastDeliversDays) {
        this.eastDeliversDays = eastDeliversDays;
    }

    public String getLocationNotDelivered() {
        return locationNotDelivered;
    }

    public void setLocationNotDelivered(String locationNotDelivered) {
        this.locationNotDelivered = locationNotDelivered;
    }

    public String getDetailDelivery1() {
        return detailDelivery1;
    }

    public void setDetailDelivery1(String detailDelivery1) {
        this.detailDelivery1 = detailDelivery1;
    }

    public String getDetailDelivery2() {
        return detailDelivery2;
    }

    public void setDetailDelivery2(String detailDelivery2) {
        this.detailDelivery2 = detailDelivery2;
    }
}

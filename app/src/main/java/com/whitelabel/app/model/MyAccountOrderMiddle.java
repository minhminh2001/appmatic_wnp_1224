package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * MyAccountOrder:middle datas
 */
public class MyAccountOrderMiddle  implements Serializable {
    private String status;
    private String statusCode;
    private String id;
    private List<MyAccountOrderInner> items;//MyAccountOrderInner;
    private MyAccountOrderTrackingInfo trackingInfo;

    public MyAccountOrderTrackingInfo getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(MyAccountOrderTrackingInfo trackingInfo) {
        this.trackingInfo = trackingInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MyAccountOrderInner> getItems() {
        return items;
    }

    public void setItems(List<MyAccountOrderInner> items) {
        this.items = items;
    }
}

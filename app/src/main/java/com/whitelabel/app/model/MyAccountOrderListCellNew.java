package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/27.
 */
public class MyAccountOrderListCellNew extends SVRReturnEntity implements Serializable {

    private String orderId;
    private String name;
    private String qty;
    private String status;//lable
    private String statusCode;
    private String time;
    private String image;
    private String price;
    private ArrayList<HashMap<String,String>>  options;

    public ArrayList<HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<HashMap<String, String>> options) {
        this.options = options;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.whitelabel.app.model;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ResponseModel<T> {
    private int status;
    private int code;
    private String errorMessage;
    private T data;
    private T address;

    public T getAddress() {
        return address;
    }

    public void setAddress(T address) {
        this.address = address;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}

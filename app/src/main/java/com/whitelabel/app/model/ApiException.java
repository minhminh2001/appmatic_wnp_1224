package com.whitelabel.app.model;

/**
 * Created by Administrator on 2017/1/26.
 */

public class ApiException extends Exception {
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public ApiException(String error){
        super(error);
    }
}

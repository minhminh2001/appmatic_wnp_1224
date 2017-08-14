package com.whitelabel.app.model;

/**
 * Created by Administrator on 2017/1/26.
 */

public class ApiException extends RuntimeException {
    private String errorMsg;
    private int code;
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public ApiException(String error){
        super(error);
        this.errorMsg=error;
    }
    public ApiException(String error,int code){
        super(error);
        this.errorMsg=error;
        this.code=code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

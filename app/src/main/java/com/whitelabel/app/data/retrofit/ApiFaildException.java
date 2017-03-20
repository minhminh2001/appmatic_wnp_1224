package com.whitelabel.app.data.retrofit;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ApiFaildException extends Exception {
    private int errorType;
    private String errorMsg;
    private Throwable throwable;
    private int code;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public ApiFaildException(String msg){
        super(msg);
    }
    public ApiFaildException(){

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

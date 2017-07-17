package com.whitelabel.app.data.retrofit;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ApiErrorException extends Exception {
    public ApiErrorException(String msg){
        super(msg);
    }
}

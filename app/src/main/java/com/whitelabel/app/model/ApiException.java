package com.whitelabel.app.model;

/**
 * Created by Administrator on 2017/1/26.
 */

public class ApiException extends Exception {
    public ApiException(String error){
        super(error);
    }
}

package com.whitelabel.app.utils;

import com.whitelabel.app.R;
import com.google.gson.JsonParseException;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.ApiFaildException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Administrator on 2017/1/26.
 */

public class ExceptionParse {
    public  static ApiFaildException parseException(Throwable  e){
        ApiFaildException  exception =new ApiFaildException();
        exception.setThrowable(e);
        if(e instanceof HttpException ||e instanceof ConnectException||e instanceof SocketTimeoutException ||e instanceof UnknownHostException){
            exception.setErrorType(ERROR.HTTP_ERROR);
            exception.setErrorMsg(WhiteLabelApplication.getInstance().getResources().getString(R.string.Global_Error_Internet));
        }else if(e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            exception.setErrorType(ERROR.PARSE_ERROR);
            exception.setErrorMsg("json error");
        }else if(e instanceof ApiException){
            exception.setErrorType(ERROR.API_ERROR);
            ApiException apiException= (ApiException) e;
            exception.setErrorMsg(apiException.getMessage());
        }else{  //   error

            exception.setErrorType(ERROR.UNKNOWN);
            exception.setErrorMsg("error");
        }
        return exception;
    }



    public  class ERROR{
        public static  final int UNKNOWN=1000;
        public static  final int PARSE_ERROR=1001;
        public static  final int  HTTP_ERROR=1003;
        public static  final int API_ERROR=1004;
    }

}

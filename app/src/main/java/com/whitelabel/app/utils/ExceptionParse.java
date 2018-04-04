package com.whitelabel.app.utils;

import com.whitelabel.app.R;
import com.google.gson.JsonParseException;
import com.whitelabel.app.WhiteLabelApplication;
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
        ApiFaildException  exception = new ApiFaildException();
        exception.setThrowable(e);
        if(e instanceof HttpException ||e instanceof ConnectException||e instanceof SocketTimeoutException ||e instanceof UnknownHostException){
            exception.setCode(e instanceof HttpException ? ((retrofit2.HttpException)e).code() : 0);
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
            exception.setData(apiException.getCode());
            exception.setErrorMsg(apiException.getErrorMsg());
        }else{  //   error

            exception.setErrorType(ERROR.UNKNOWN);
            exception.setErrorMsg("error");
        }
        return exception;
    }



    public  class ERROR{
        public static  final int UNKNOWN = 1000;
        public static  final int PARSE_ERROR = 1001;
        public static  final int  HTTP_ERROR = 1003;
        public static  final int API_ERROR = 1004;
        public static  final int HTTP_ERROR_CODE_NOT_FOUND_PAGE = 404;
        public static  final int HTTP_ERROR_CODE_SERVER_ERR = 500;
    }

}

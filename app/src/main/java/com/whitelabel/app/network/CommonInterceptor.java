package com.whitelabel.app.network;

import com.whitelabel.app.utils.JLogUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonInterceptor implements Interceptor {

private String versionNumber;
private String serviceVersion;
private String apiKey;
private String appKey;
private String apiVersion;

public CommonInterceptor(String apiKey,String appKey,String apiVersion,String versionNumber,String serviceVersion) {
    this.versionNumber=versionNumber;
    this.serviceVersion=serviceVersion;
    this.apiKey=apiKey;
    this.appKey=appKey;
    this.apiVersion=apiVersion;
}

@Override public Response intercept(Interceptor.Chain chain) throws IOException {
    Request oldRequest = chain.request();
    HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
            .newBuilder()
            .scheme(oldRequest.url().scheme())
            .host(oldRequest.url().host())
            .addQueryParameter("platformId", "2")
            .addQueryParameter("versionNumber",versionNumber)
            .addQueryParameter("serviceVersion",serviceVersion );
    Request.Builder builder= oldRequest.newBuilder().header("API-VERSION",apiVersion)
            .header("API-KEY",apiKey)
            .header("APP-KEY",appKey);
    Request newRequest = builder
            .method(oldRequest.method(), oldRequest.body())
            .url(authorizedUrlBuilder.build())
            .build();
    Response response=chain.proceed(newRequest);
    JLogUtils.i("ray","response_code:"+response.code());
    return response;
  }
}


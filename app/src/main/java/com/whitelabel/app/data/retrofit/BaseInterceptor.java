package com.whitelabel.app.data.retrofit;

import android.text.TextUtils;


import com.whitelabel.app.GlobalData;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class BaseInterceptor implements Interceptor {
    public static final  String POST ="POST";
    public  static final String GET="GET";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(POST.equals(request.method())){
            StringBuffer params=new StringBuffer();
            FormBody formBody = (FormBody) request.body();
            FormBody.Builder builder1=new FormBody.Builder();
            Request.Builder requestBuilder = request.newBuilder();
            for (int i = 0; i < formBody.size(); i++) {
                params.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append("&");
                builder1.addEncoded(formBody.encodedName(i),formBody.encodedValue(i));
            }
            String paramsStr="";
            if(!TextUtils.isEmpty(params)){
                paramsStr=params.substring(0,params.length()-1);
            }
            for(String key: GlobalData.globalParams.keySet()){
                builder1.addEncoded(key,GlobalData.globalParams.get(key));
            }
            requestBuilder.method(request.method(),builder1.build());
            request=requestBuilder.build();;
        }else if(GET.equals(request.method())){
            HttpUrl.Builder urlBuilder = request.url()
                    .newBuilder()
                    .scheme(request.url().scheme())
                    .host(request.url().host());
            for(String key :GlobalData.globalParams.keySet()){
                urlBuilder.addQueryParameter(key,GlobalData.globalParams.get(key));
            }
            Request.Builder requestBuilder = request.newBuilder();
        }
        return chain.proceed(request);
    }

}

package com.whitelabel.app.data.retrofit;
import android.text.TextUtils;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.utils.JLogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by Administrator on 2017/1/3.
 */
public class RetrofitHelper {
    private String mBaseUrl;
    private String mMockUrl;

    public RetrofitHelper(String mBaseUrl, String mMockUrl) {
        this.mBaseUrl = mBaseUrl;
        this.mMockUrl = mMockUrl;
    }

    public  Retrofit  getDefaultRetrofit(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    public   Retrofit getMockRetrofit(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(mMockUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    private  OkHttpClient  getOkHttpClient(){
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl url=original.url().newBuilder()
                            .addEncodedQueryParameter("platformId","2")
                            .addEncodedQueryParameter("versionNumber","1.0.1")
                            .addEncodedQueryParameter("serviceVersion","1.0.5").build();
                    Request.Builder  builder1=original.newBuilder()
                            .header("API-VERSION", "v1")
                            .header("API-KEY", "L5M7aUpZRr2ChzDx")
                            .header("APP-KEY","APP-29710023052170613");
                    Request request1=builder1.method(original.method(), original.body()).url(url).build();
                    return chain.proceed(request1);
                }
            });
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient mOkHttpClient=builder.build();
        return mOkHttpClient;
    }

}

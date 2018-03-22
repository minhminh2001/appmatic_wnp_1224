package com.whitelabel.app.data.retrofit;

import com.google.gson.JsonObject;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;

import org.json.JSONObject;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface BaseApi {

    @GET("appservice/app/open")
    public Observable<JsonObject>  openApp(@Query("session_key") String sessionKey,
                                           @Query("device_token") String  deviceToken);

    @FormUrlEncoded
    @POST("appservice/customer/login")
     Observable<SVRAppServiceCustomerLoginReturnEntity>  emailLogin(@Field("email")String email,@Field("password")
            String password,@Field("device_token") String deviceToken,
            @Field("versionNumber") String versionNumber, @Field("platformId") String platformId,
                                                                    @Field("serviceVersion") String sessionVersion);

    @FormUrlEncoded
    @POST("appservice/version/check")
    Observable<ResponseModel>  versionCheck(@Field("versionNumber")String versionNumber,@Field("platformId") String platformId
       );
}

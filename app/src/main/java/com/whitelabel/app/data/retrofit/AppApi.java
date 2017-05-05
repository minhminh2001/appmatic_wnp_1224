package com.whitelabel.app.data.retrofit;

import com.google.gson.JsonObject;
import com.whitelabel.app.model.RemoteConfigResonseModel;

import org.json.JSONObject;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface AppApi {

    @GET("appservice/app/open")
    public Observable<JsonObject>  openApp(@Query("session_key") String sessionKey,
                                           @Query("device_token") String  deviceToken);

}

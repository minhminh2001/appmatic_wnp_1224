package com.whitelabel.app.data.retrofit;


import com.google.gson.JsonObject;
import com.whitelabel.app.model.NativeLoginRequest;
import com.whitelabel.app.model.ResponseConnection;


import retrofit2.http.Body;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface OneAllApi {
    @PUT("/users.json")
    Observable<ResponseConnection> info(@Body NativeLoginRequest requestBody);
}

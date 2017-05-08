package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.RemoteConfigResonseModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ray on 2017/5/5.
 */

public interface MockApi {
    @GET("appservice/config/getConfig")
    public Observable<RemoteConfigResonseModel> getConfigInfo(@Query("version") String version);
}

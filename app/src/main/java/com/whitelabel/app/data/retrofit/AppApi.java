package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.RemoteConfigResonseModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface AppApi {
    @GET("appservice/config/getConfig")
    public Observable<RemoteConfigResonseModel> getConfigInfo(@Query("version") String version);


}

package com.whitelabel.app.data.retrofit;

import com.whitelabel.app.model.AddresslistReslut;

import java.util.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/12.
 */

public interface MyAccoutApi {
    @GET("appservice/customer/customerAddressInfo")
   public rx.Observable<AddresslistReslut>  getAddressList(@Query("session_key") String sessionKey);
}

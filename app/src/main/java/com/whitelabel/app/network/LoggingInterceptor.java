package com.whitelabel.app.network;

import com.whitelabel.app.utils.JLogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Arman on 12/22/2016.
 */

public class LoggingInterceptor implements Interceptor {
    public static String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        JLogUtils.i(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        JLogUtils.i(TAG, String.format("Received response for %s in %.1fms",
                response.request().url(), (t2 - t1) / 1e6d));

        return response;
    }
}

package com.whitelabel.app.network;

import android.text.TextUtils;

import com.whitelabel.app.utils.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by mindaset on 2017/7/24.
 */

public class LoggingInterceptor {
    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;




    public static Interceptor addLogInterceptor() {
        Interceptor logInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();

                long t1 = System.nanoTime();
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                MediaType contentType = null;
                String bodyString = null;
                if (response.body() != null) {
                    contentType = response.body().contentType();
                    bodyString = response.body().string();
                }
                // 请求响应时间
                double time = (t2 - t1) / 1e6d;
                switch (request.method()) {
                    case "GET":
                        Logger.d(
                                String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY,
                                        request.url(),
                                        time,
                                        request.headers(),
                                        response.code(),
                                        response.headers(),
                                        stringifyResponseBody(bodyString)));
                        break;
                    case "POST":
                        Logger.d(
                                String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY,
                                        request.url(),
                                        time,
                                        request.headers(),
                                        stringifyRequestBody(request),
                                        response.code(),
                                        response.headers(),
                                        stringifyResponseBody(bodyString)));
                        break;
                    case "PUT":
                        Logger.d(
                                String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY,
                                        request.url(),
                                        time,
                                        request.headers(),
                                        request.body().toString(),
                                        response.code(),
                                        response.headers(),
                                        stringifyResponseBody(bodyString)));
                        break;
                    case "DELETE":
                        Logger.d(
                                String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY,
                                        request.url(),
                                        time,
                                        request.headers(),
                                        response.code(),
                                        response.headers()));
                        break;
                    default:
                        break;
                }
                if (response.body() != null) {
                    // 深坑！
                    // 打印body后原ResponseBody会被清空，需要重新设置body
                    ResponseBody body = ResponseBody.create(contentType, bodyString);
                    return response.newBuilder().body(body).build();
                } else {
                    return response;
                }
            }
        };
        return logInterceptor;
    }

    private static String stringifyResponseBody(String bodyString) {
        if (!TextUtils.isEmpty(bodyString)) {
            return bodyString;
        }
        return "it empty from response body!";
    }

    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}

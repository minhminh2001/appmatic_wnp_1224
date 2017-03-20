package com.whitelabel.app.callback;

import com.whitelabel.app.network.OkHttpClientManager;

import okhttp3.Request;

public abstract class MyResultCallback<T> extends OkHttpClientManager.ResultCallback<T> {

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
    }

    @Override
    public void onAfter() {
        super.onAfter();

    }
}
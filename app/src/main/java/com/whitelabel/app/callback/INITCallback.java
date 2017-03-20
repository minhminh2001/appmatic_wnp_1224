package com.whitelabel.app.callback;

/**
 * Created by imaginato on 2015/6/12.
 */
public abstract class INITCallback {
    public static final int CODE_NG = 400;
    public static final int CODE_OK = 200;

    public abstract void onSuccess(int resultCode, Object object);
    public abstract void onFailure(int resultCode, Object object);
}

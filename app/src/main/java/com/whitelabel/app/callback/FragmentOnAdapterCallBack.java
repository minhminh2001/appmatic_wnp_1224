package com.whitelabel.app.callback;

import android.content.Intent;

/**
 * Created by kevin on 2016/8/8.
 */
public interface FragmentOnAdapterCallBack {
    void startActivityForResultCallBack(Intent intent, int code);
}

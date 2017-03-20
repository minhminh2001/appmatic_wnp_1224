package com.common.widget.wheel;

/**
 * Created by Administrator on 2017/1/20.
 */

public interface IWheelCallback {
    void done(int position);
    void cancel();
    void scroll(int position);
}

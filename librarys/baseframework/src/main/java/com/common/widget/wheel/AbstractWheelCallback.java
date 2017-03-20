package com.common.widget.wheel;

/**
 * Created by Administrator on 2017/2/10.
 */

public  abstract class AbstractWheelCallback implements IWheelCallback {
    @Override
     public abstract  void done(int position) ;

    @Override
    public void cancel() {

    }
    @Override
    public void scroll(int position) {

    }
}

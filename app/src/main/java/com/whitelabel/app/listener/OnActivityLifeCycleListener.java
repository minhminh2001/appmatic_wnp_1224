package com.whitelabel.app.listener;

/**
 * Created by imaginato on 2015/6/12.
 */
public interface OnActivityLifeCycleListener {
    public boolean checkIsFinished();
    public boolean checkIsInvisible();
    public boolean checkIsPaused();
}

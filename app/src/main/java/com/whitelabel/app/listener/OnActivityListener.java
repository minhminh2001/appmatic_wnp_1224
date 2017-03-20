package com.whitelabel.app.listener;

/**
 * Created by imaginato on 2015/6/11.
 */
public interface OnActivityListener {
    public void switchFragment(int from, int to);
    public void redirectToAttachedFragment(int to, int type);
    public void onBackPressed();
}

package com.whitelabel.app.listener;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by Arman on 12/23/2016.
 */

public abstract class OnSingleClickListener implements View.OnClickListener {

    private long mLastClickTime = 0;
    private static final int CLICK_INTERVAL = 400;

    public abstract void onSingleClick(View view);

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < CLICK_INTERVAL){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        onSingleClick(view);
    }
}

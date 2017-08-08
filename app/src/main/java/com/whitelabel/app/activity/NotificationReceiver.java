package com.whitelabel.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whitelabel.app.callback.NotificationCallback;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.utils.SendBoardUtil;

public class NotificationReceiver extends BroadcastReceiver {
    private NotificationDao mDao;
    private NotificationCallback mCallback;
    public static final String ACTION="com.whitelabel.app.notification";

    public NotificationReceiver(){

    }
    public NotificationReceiver(NotificationCallback callback){
        this.mCallback=callback;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
            int type=intent.getIntExtra("type", SendBoardUtil.READCODE);
            String id=intent.getStringExtra("id");
            if(mCallback!=null){
                mCallback.refreshNotification(type,id);
            }
    }
}




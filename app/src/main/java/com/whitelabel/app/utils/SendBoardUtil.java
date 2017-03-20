package com.whitelabel.app.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.whitelabel.app.activity.NotificationReceiver;

/**
 * Created by ray on 2015/11/5.
 */
public class SendBoardUtil {

    public final static int LOGINCODE=1;

    public final static int READCODE=2;

    public final static int READFLAG=4;
    public final static int LOGINOUTCODE=5;
    public final static  int NOTIFICATION=3;


    public static void sendNotificationBoard(Context context,int type,String id){
        Intent intent=new Intent();
        intent.setAction(NotificationReceiver.ACTION);
        intent.putExtra("type", type);
        if(!TextUtils.isEmpty(id)) {
            intent.putExtra("id",id);
        }
        context.sendBroadcast(intent);
    }
}

package com.whitelabel.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.whitelabel.app.activity.StartActivity;

import java.util.List;

public class BadgeUtils {


    public static void setBadge(Context context, int count) {
        setBadgeSamsung(context, count);
        setBadgeSony(context, count);
        sendToXiaoMi(context,count);
    }

    public static void clearBadge(Context context) {
        setBadgeSamsung(context, 0);
        clearBadgeSony(context);
    }


    private static void setBadgeSamsung(Context context, int count) {
        try{
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
                return;
            }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
        }catch (Exception ex){
            ex.getMessage();
        }
    }

    private static void setBadgeSony(Context context, int count) {
        try {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent();
            intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());

            context.sendBroadcast(intent);
        }catch (Exception ex){
            ex.getMessage();
        }
    }

    private final static String lancherActivityClassName = StartActivity.class.getName();
    private static void sendToXiaoMi(Context context, int count) {
        Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
        localIntent.putExtra("android.intent.extra.update_application_component_name",context.getPackageName() + "/"+ lancherActivityClassName );
        localIntent.putExtra("android.intent.extra.update_application_message_text",count);
        context.sendBroadcast(localIntent);
    }


    private static void clearBadgeSony(Context context) {

        try {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }

            Intent intent = new Intent();
            intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", false);
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(0));
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());

            context.sendBroadcast(intent);
        }catch (Exception ex){
            ex.getMessage();
        }
    }

    private static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
}
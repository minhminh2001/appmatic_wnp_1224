package com.whitelabel.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by ray on 2015/12/7.
 */
public class JScreenUtils {
    private static int screenWith=0;
    private static int screenHeight=0;

    public static boolean isInScreen(Activity activity,View view){
        int width,height;
        Point p=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        width=p.x;
        height=p.y;

        Rect rect=new Rect(0,0,width,height );
        if(view.getLocalVisibleRect(rect)){
            return true;
        }else{
            return false;
        }
    }
    public static int getScreenWidth(Activity context){
       if(screenWith==0){
           DisplayMetrics metric = new DisplayMetrics();
           context.getWindowManager().getDefaultDisplay().getMetrics(metric);
           screenWith = metric.widthPixels;     // 屏幕宽度（像素）
       }

        return screenWith;
    }




    public static int getScreenHeight(Activity context){
        if(screenHeight==0){
            DisplayMetrics metric = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenHeight = metric.heightPixels;
        }
        return screenHeight;
    }


    /**
     *
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     *
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

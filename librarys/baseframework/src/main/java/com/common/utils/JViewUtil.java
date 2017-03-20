package com.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ListView;

import com.common.R;
import com.common.helper.SystemBarTintManager;

/**
 * Created by kevin on 2017/1/12.
 */

public class JViewUtil {//Full screen transparent;getToolBarHeight


    public static void resetListViewHeight(Adapter adapter,ListView listView){
        int totalHeight = 0;                                    // 定义、初始化listview总高度值
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);          // 获取单个item
            listItem.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));// 设置item高度为适应内容
            listItem.measure(0, 0);                                        // 测量现在item的高度
            totalHeight += listItem.getMeasuredHeight();                   // 总高度增加一个listitem的高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1)); // 将分割线高度加上总高度作为最后listview的高度
        listView.setLayoutParams(params);
    }
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            final int bit = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            winParams.flags |= bit;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);

        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);

        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintResource(0);
    }

    public static int getToolBarHeight(Context context) {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }
    public static int getPhoneBottomBar(Context context){
        // get bottomBar height
        boolean hasBottomBars;
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasBottomBars = resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasBottomBars = !hasMenuKey && !hasBackKey;
        }
        int height = 0;
        if (hasBottomBars) {
            Resources resourcess = context.getResources();
            int resourceId = resourcess.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = resourcess.getDimensionPixelSize(resourceId);
            }
        }
        return height;
    }
    public static int getPhoneTopBar(Context context){
        // get top bar height
        int topHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            topHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return topHeight;
    }
    public static void setStatuBarColorId(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, colorId));
        }
    }
    public static void setStatuBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        }
    }
}

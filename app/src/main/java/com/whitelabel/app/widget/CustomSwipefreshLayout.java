package com.whitelabel.app.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.whitelabel.app.utils.SoftInputShownUtil;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CustomSwipefreshLayout extends SwipeRefreshLayout {
    private int scaleTouchSlop;
    private float preX;

    private boolean switchByKeyboard;
    private Activity activity;

    public void setSwitchByKeyboard(boolean switchByKeyboard,Activity activity){
        this.switchByKeyboard=switchByKeyboard;
        this.activity=activity;
    }
    public CustomSwipefreshLayout(Context context){
        super(context);
    }

    public CustomSwipefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return !isRefreshing()&&super.onStartNestedScroll(child, target, nestedScrollAxes);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    preX = ev.getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float moveX = ev.getX();
                    float instanceX = Math.abs(moveX - preX);
                    // 下拉刷新对滑动的容差值判断太小，大概是24，再加上60,以解决横向滑动和下拉刷新的冲突
                    if (instanceX > scaleTouchSlop + 60) {
                        return false;
                    }
                    //如果当前activity需要使用switchByKeyboard  功能，则如果键盘弹起，禁用下拉刷新。
                    if (switchByKeyboard && activity != null) {
                        if (SoftInputShownUtil.isSoftInputShown(activity)) {
                            return false;
                        }
                    }
                    break;
            }
            return super.onInterceptTouchEvent(ev);
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return false;
    }
}

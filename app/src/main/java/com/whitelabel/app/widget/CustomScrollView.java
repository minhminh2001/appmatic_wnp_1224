package com.whitelabel.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by imaginato on 2015/7/7.
 */
public class CustomScrollView extends ScrollView {


    public CustomScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private float xDistance, yDistance, lastX, lastY;
    private int downX;
    private int downY;
    private int mTouchSlop;
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent. ACTION_DOWN:
                    xDistance = yDistance = 0f;
                    lastX = ev.getX();
                    lastY = ev.getY();
                    downX = (int) ev.getRawX();
                    downY = (int) ev.getRawY();
                    break;
                case MotionEvent. ACTION_MOVE:
                    final float curX = ev.getX();
                    final float curY = ev.getY();
                    xDistance += Math. abs(curX - lastX);
                    yDistance += Math. abs(curY - lastY);
                    lastX = curX;
                    lastY = curY;
                    int moveY = (int) ev.getRawY();
                    int moveX = (int) ev.getRawX();
//                    if (Math.abs(moveY - downY) > mTouchSlop) {
//                        return true;
//                    }

                    //  ShoppingCartVertical 特殊处理，使滑动有惯性且不影响到横滑
                    if (Math.abs(moveY - downY) > mTouchSlop&&!(Math.abs(moveX - downX)+5>Math.abs(moveY - downY)) ){
                        return true;
                    }

                    if( xDistance > yDistance) {
                        return false;
                    }
            }

            return super.onInterceptTouchEvent(ev);
        }
}
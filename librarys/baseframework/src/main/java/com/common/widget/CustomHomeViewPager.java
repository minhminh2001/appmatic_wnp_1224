package com.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.common.R;


/**
 * Created by imaginato on 2015/7/15.
 */
public class CustomHomeViewPager extends ViewPager {
    private boolean isStopHorizontalScroll = false;
    private boolean isDisallowParentTouch = false;

    public CustomHomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface(context, attrs);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPagerStyle);
        if (types == null) {
            return;
        }

        isStopHorizontalScroll = types.getBoolean(R.styleable.CustomViewPagerStyle_isStopHorizontalScroll, false);
        isDisallowParentTouch = types.getBoolean(R.styleable.CustomViewPagerStyle_isDisallowParentTouch, false);

        types.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isStopHorizontalScroll && super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isStopHorizontalScroll) {
            return false;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (isDisallowParentTouch) {
                        setParentScrollAble(false);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    if (isDisallowParentTouch) {
                        setParentScrollAble(true);
                    }
                    break;
                }
            }
            try {
                return super.onInterceptTouchEvent(event);
            }catch (Exception ex){
                ex.getStackTrace();
                return false;
            }
        }
    }

    public void setIsStopHorizontalScroll(boolean isStopHorizontalScroll) {
        this.isStopHorizontalScroll = isStopHorizontalScroll;
    }

    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}
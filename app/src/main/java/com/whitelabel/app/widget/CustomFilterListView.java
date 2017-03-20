package com.whitelabel.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by imaginato on 2015/8/5.
 */
public class CustomFilterListView extends ListView {
    public CustomFilterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFilterListView(Context context) {
        super(context);
    }

    public CustomFilterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setParentScrollAble(false);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                setParentScrollAble(true);
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}

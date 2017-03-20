package com.whitelabel.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.whitelabel.app.listener.CustomScrollListener;

/**
 * A ScrollView which can scroll to (0,0) when pull down or up.
 *
 * @author markmjw
 * @date 2014-04-30
 */
public class StretchScrollView extends ScrollView {
    private static final int MSG_REST_POSITION = 0x01;

    /** The max scroll height. */
    private static final int MAX_SCROLL_HEIGHT = 400;
    /** Damping, the smaller the greater the resistance */
    private static final float SCROLL_RATIO = 0.4f;

    private View mChildRootView;

    private float mTouchY;
    private boolean mTouchStop = false;

    private int mScrollY = 0;
    private int mScrollDy = 0;
    private CustomScrollListener customScrollListener;
    public void setScrollListener(CustomScrollListener customScrollListener){
        this.customScrollListener=customScrollListener;
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_REST_POSITION == msg.what) {
                if (mScrollY != 0 && mTouchStop) {
                    mScrollY -= mScrollDy;

                    if ((mScrollDy < 0 && mScrollY > 0) || (mScrollDy > 0 && mScrollY < 0)) {
                        mScrollY = 0;
                    }

                    mChildRootView.scrollTo(0, mScrollY);
                    // continue scroll after 20ms
                    sendEmptyMessageDelayed(MSG_REST_POSITION, 20);
                }
            }
        }
    };
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (customScrollListener != null) {
            customScrollListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    public StretchScrollView(Context context) {
        super(context);

        init();
    }

    public StretchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public StretchScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            // when finished inflating from layout xml, get the first child view
            mChildRootView = getChildAt(0);
        }
    }

    private float xDistance, yDistance, lastX, lastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent. ACTION_DOWN:
                mTouchY = ev.getY();
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent. ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math. abs(curX - lastX);
                yDistance += Math. abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if( xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (null != mChildRootView) {
            doTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void doTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                mScrollY = mChildRootView.getScrollY();
                if (mScrollY != 0) {
                    mTouchStop = true;
                    mScrollDy = (int) (mScrollY / 10.0f);
                    mHandler.sendEmptyMessage(MSG_REST_POSITION);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                int deltaY = (int) (mTouchY - nowY);
                mTouchY = nowY;
                if (isNeedMove()) {
                    int offset = mChildRootView.getScrollY();
                    if (offset < MAX_SCROLL_HEIGHT && offset > -MAX_SCROLL_HEIGHT) {
                        mChildRootView.scrollBy(0, (int) (deltaY * SCROLL_RATIO));
                        mTouchStop = false;
                    }
                }
                break;

            default:
                break;
        }
    }

    private boolean isNeedMove() {
        int viewHeight = mChildRootView.getMeasuredHeight();
        int scrollHeight = getHeight();
        int offset = viewHeight - scrollHeight;
        int scrollY = getScrollY();

        return scrollY == 0 || scrollY == offset;
    }
}
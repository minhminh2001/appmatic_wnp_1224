package com.whitelabel.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.widget.photoview.gestures.GestureDetector;
import com.whitelabel.app.widget.photoview.gestures.OnGestureListener;

/**
 * Created by ray on 2015/9/1.
 */
public class CustomRecyclerView extends RecyclerView {
    private boolean isStopHorizontalScroll = false;
    private boolean isDisallowParentTouch = false;
    /**
     * 记录当前第一个View
     */
    private View mCurrentView;

    private OnItemScrollChangeListener mItemScrollChangeListener;

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener)
    {
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    public interface OnItemScrollChangeListener {
        void onChange(View view, int position);
    }

    private static  final  int  FLING_MIN_DISTANCE = 50;
    private static  final  int  FLING_MIN_VELOCITY = 0;
    private android.view.GestureDetector   gesture=new android.view.GestureDetector(getContext(), new android.view.GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX()-e2.getX()>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY&&e2.getX()-e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY){
                return true;
            }
            return false;
        }
    });

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);



        // TODO Auto-generated constructor stub

        initTypeface(context,attrs);
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View newView = getChildAt(0);

                if (mItemScrollChangeListener != null)
                {
                    if (newView != null && newView != mCurrentView)
                    {
                        mCurrentView = newView ;
                        mItemScrollChangeListener.onChange(mCurrentView,
                                getChildPosition(mCurrentView));

                    }
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gesture.onTouchEvent(event)) {
            return super.onTouchEvent(event);

        } else {
            return false;
        }
    }
    public void setIsStopHorizontalScroll(boolean isStopHorizontalScroll) {
        this.isStopHorizontalScroll = isStopHorizontalScroll;
    }

    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
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
            return super.onInterceptTouchEvent(event);
        }
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
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        mCurrentView = getChildAt(0);

        if (mItemScrollChangeListener != null)
        {
            mItemScrollChangeListener.onChange(mCurrentView,
                    getChildPosition(mCurrentView));
        }
    }

    @Override
    public void onScrollStateChanged(int arg0)
    {
    }
    /**
     *
     * 滚动时，判断当前第一个View是否发生变化，发生才回调
     */
    @Override
    public void onScrolled(int arg0, int arg1)
    {
        View newView = getChildAt(0);

        if (mItemScrollChangeListener != null)
        {
            if (newView != null && newView != mCurrentView)
            {
                mCurrentView = newView ;
                mItemScrollChangeListener.onChange(mCurrentView,
                        getChildPosition(mCurrentView));

            }
        }
    }
}

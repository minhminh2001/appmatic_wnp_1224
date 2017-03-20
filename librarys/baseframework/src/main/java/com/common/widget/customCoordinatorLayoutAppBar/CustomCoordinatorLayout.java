package com.common.widget.customCoordinatorLayoutAppBar;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin on 2016/10/14.
 */
public class CustomCoordinatorLayout extends CoordinatorLayout {

    private boolean switchScroll = true;

    public void setSwitchScroll(boolean switchScroll) {
        this.switchScroll = switchScroll;
    }

    public boolean getSwitchScroll() {
        return this.switchScroll;
    }

    public CustomCoordinatorLayout(Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return switchScroll && super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        if (switchScroll) {
            super.onNestedScrollAccepted(child, target, nestedScrollAxes);
        }
    }

    @Override
    public void onStopNestedScroll(View target) {
        if (switchScroll) {
            super.onStopNestedScroll(target);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (switchScroll) {
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (switchScroll) {
            super.onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return switchScroll && super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return switchScroll && super.onNestedPreFling(target, velocityX, velocityY);
    }


}

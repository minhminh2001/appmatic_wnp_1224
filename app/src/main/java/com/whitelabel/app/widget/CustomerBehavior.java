package com.whitelabel.app.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ray on 2017/5/11.
 */

public class CustomerBehavior extends AppBarLayout.Behavior {

    public CustomerBehavior() {
        super();
    }

    public CustomerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        // Trigger the following events if it is a vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        // If I slowly reach the top, without fling, show the RecyclerView
        int[] firstVisiblePositions = ((StaggeredGridLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPositions(null);
        for (int position : firstVisiblePositions) {
            if (position == 0) {
//                        showRelatedTerms();
                break;
            }
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY > 500) {
            // Hide the RecyclerView
        } else if (velocityY < -500) {
            // Show the recyclerView
        }
        return true;
    }

}

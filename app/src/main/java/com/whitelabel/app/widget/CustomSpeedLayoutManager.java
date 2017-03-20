package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by kevin on 2016/11/9.
 */

public class CustomSpeedLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 90f;
        private Context mContext;

        public CustomSpeedLayoutManager(Context context) {
            super(context);
            mContext = context;
        }

    //控制smoothScroll速度，MILLISECONDS_PER_INCH越小，速度越
        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {

            LinearSmoothScroller smoothScroller =
                    new LinearSmoothScroller(mContext) {

                        //This controls the direction in which smoothScroll looks
                        //for your view
                        @Override
                        public PointF computeScrollVectorForPosition
                        (int targetPosition) {
                            return CustomSpeedLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                        }
                    };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }
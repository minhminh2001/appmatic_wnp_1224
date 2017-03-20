package com.common.widget.customCoordinatorLayoutAppBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.common.R;

import java.util.ArrayList;


/**
 * 如果
 */
public class CustomParallaxedNestedScrollView extends NestedScrollView {
    private static final int DEFAULT_PARALLAX_VIEWS = 1;
    private static final float DEFAULT_INNER_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_ALPHA_FACTOR = -1F;
    private int numOfParallaxViews = DEFAULT_PARALLAX_VIEWS;
    private float innerParallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float parallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float alphaFactor = DEFAULT_ALPHA_FACTOR;
    private ArrayList<ParallaxedView> parallaxedViews = new ArrayList<>();
    public  ScrollInterface scrollInterface;

    public CustomParallaxedNestedScrollView(Context context) {
        this(context, null);
    }

    public CustomParallaxedNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomParallaxedNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void init(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScroll);
        this.parallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_parallax_factor, DEFAULT_PARALLAX_FACTOR);
        this.alphaFactor = typeArray.getFloat(R.styleable.ParallaxScroll_alpha_factor, DEFAULT_ALPHA_FACTOR);
        this.innerParallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_inner_parallax_factor, DEFAULT_INNER_PARALLAX_FACTOR);
        this.numOfParallaxViews = typeArray.getInt(R.styleable.ParallaxScroll_parallax_views_num, DEFAULT_PARALLAX_VIEWS);
        typeArray.recycle();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        makeViewsParallax();
    }

    private void makeViewsParallax() {
        if (getChildCount() > 0 && getChildAt(0) instanceof ViewGroup) {
            ViewGroup viewsHolder = (ViewGroup) getChildAt(0);
            int numOfParallaxViews = Math.min(this.numOfParallaxViews, viewsHolder.getChildCount());
            for (int i = 0; i < numOfParallaxViews; i++) {
                ParallaxedView parallaxedView = new CustomParallaxedNestedScrollView.ScrollViewParallaxedItem(viewsHolder.getChildAt(i));
                parallaxedViews.add(parallaxedView);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollInterface!=null){
            scrollInterface.onSChanged(l, t, oldl, oldt);
        }
        float parallax = parallaxFactor;
        float alpha = alphaFactor;
        for (ParallaxedView parallaxedView : parallaxedViews) {
            parallaxedView.setOffset((float)t / parallax);
            parallax *= innerParallaxFactor;
            if (alpha != DEFAULT_ALPHA_FACTOR) {
                float fixedAlpha = (t <= 0) ? 1 : (100 / ((float)t * alpha));
                parallaxedView.setAlpha(fixedAlpha);
                alpha /= alphaFactor;
            }
            parallaxedView.animateNow();
        }
    }

    protected class ScrollViewParallaxedItem extends ParallaxedView {

        public ScrollViewParallaxedItem(View view) {
            super(view);
        }

        @Override
        protected void translatePreICS(View view, float offset) {
            view.offsetTopAndBottom((int)offset - lastOffset);
            lastOffset = (int)offset;
        }
    }


    public void setOnCustomScroolChangeListener(ScrollInterface t) {
        this.scrollInterface = t;
    }

    /**
     * 定义滑动接口
     * @param
     */
    public interface ScrollInterface {
        void onSChanged(int l, int t, int oldl, int oldt);
    }
}

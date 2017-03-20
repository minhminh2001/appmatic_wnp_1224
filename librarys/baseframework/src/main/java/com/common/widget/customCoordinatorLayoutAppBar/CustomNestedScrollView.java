package com.common.widget.customCoordinatorLayoutAppBar;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;


/**
 * 如果
 */
public class CustomNestedScrollView extends NestedScrollView {
    public  ScrollInterface scrollInterface;

    public CustomNestedScrollView(Context context) {
        this(context, null);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollInterface!=null){
            scrollInterface.onSChanged(l, t, oldl, oldt);
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

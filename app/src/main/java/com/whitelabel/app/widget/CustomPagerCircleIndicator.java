package com.whitelabel.app.widget;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.youth.banner.Banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerCircleIndicator extends LinearLayout implements ViewPager
    .OnPageChangeListener {

    private List<View> points;

    private List<String> imageUrls;


    public CustomPagerCircleIndicator(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        points = new ArrayList<>();
        imageUrls=new ArrayList<>();
    }

    public CustomPagerCircleIndicator(Context context,
        @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPagerCircleIndicator(Context context,
        @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setIndictor(Banner banner,List<String> imgs) {
        Banner bannerView = banner;
        bannerView.setOnPageChangeListener(this);
        this.imageUrls=imgs;
        initIndicator();
    }

    public void initIndicator() {
        this.removeAllViews();
        this.points.clear();
        if (imageUrls.size() == 1) return;
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView vPoint = (ImageView) LayoutInflater.from(getContext())
                .inflate(R.layout.item_circle_page_indicator_point, this, false);
            Drawable src = vPoint.getDrawable();
            vPoint.setImageDrawable(JImageUtils.getCircleSelector(src));
            vPoint.setSelected(i == 0);
            points.add(vPoint);
            addView(vPoint);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setPointSelected(i);
    }

    private void setPointSelected(int index) {
        for (int i = 0; i < points.size(); i++) {
            View view = points.get(i);
            view.setSelected(index == i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

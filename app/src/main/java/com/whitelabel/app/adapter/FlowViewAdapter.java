package com.whitelabel.app.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class FlowViewAdapter extends PagerAdapter {
    private List<ImageView> mProductImageView;

    public FlowViewAdapter(List<ImageView> productImageView){
        this.mProductImageView=productImageView;
    }
    @Override
    public int getCount() {
        int count = 0;
        if (mProductImageView != null) {

            count = mProductImageView.size();
        }
        return count;
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        try {
            ((ViewPager) container).removeView(mProductImageView.get(position % mProductImageView.size()));
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
    @Override
    public Object instantiateItem(View container, int position) {
        try {
            ((ViewPager) container).addView(mProductImageView.get(position % mProductImageView.size()), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView object = null;
        if (mProductImageView != null && mProductImageView.size() > 0 && mProductImageView.size() > position) {
            object = mProductImageView.get(position % mProductImageView.size());
        }
        return object;
    }
}


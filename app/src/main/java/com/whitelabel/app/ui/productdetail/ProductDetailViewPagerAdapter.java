package com.whitelabel.app.ui.productdetail;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ProductDetailViewPagerAdapter extends PagerAdapter {
    private ArrayList<ImageView>  mProductImageView;
    public ProductDetailViewPagerAdapter(ArrayList<ImageView> productImageView){
        mProductImageView=productImageView;
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
    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View container, int position) {
        try {
            //((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            ((ViewPager) container).addView(mProductImageView.get(position % mProductImageView.size()), 0);
        } catch (Exception e) {
            //handler something
            e.printStackTrace();
        }
        //return mImageViews[position % mImageViews.length];
        ImageView object = null;
        if (mProductImageView != null && mProductImageView.size() > 0 && mProductImageView.size() > position) {
            object = mProductImageView.get(position % mProductImageView.size());
        }
        return object;
    }
    }
package com.whitelabel.app.adapter;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by img on 2017/11/17.
 */

public class GuidePagerAdapter extends PagerAdapter {
    Context context;
    List<Integer> lists;
    public GuidePagerAdapter(Context context, List<Integer> lists) {
        this.context=context;
        this.lists=lists;
    }

    @Override
    public int getCount() {
        return this.lists==null||this.lists.isEmpty()?0:this.lists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView .setLayoutParams(layoutParams);
        ((ViewPager) container).addView(imageView);
        Glide.with(context)
                .load(lists.get(position))
                .centerCrop()
                .crossFade()
                .into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ImageView imageView = (ImageView) object;
        if (imageView == null)
            return;
        Glide.clear(imageView);
        ((ViewPager) container).removeView(imageView);
    }
}

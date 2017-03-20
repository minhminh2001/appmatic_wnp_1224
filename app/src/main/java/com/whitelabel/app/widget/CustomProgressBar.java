package com.whitelabel.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JDataUtils;


public class CustomProgressBar extends LinearLayout {

    private static CustomProgressBar INSTANCE = null;

    private Animation animation;
    private ImageView imageView;

    private CustomProgressBar(Context context) {
        super(context);
        init();
    }

    private CustomProgressBar(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    private static CustomProgressBar initProgressBar(Activity activity) {
        if (activity == null) {
            return null;
        }
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(JDataUtils.dp2Px(80), JDataUtils.dp2Px(80));
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        CustomProgressBar progressBar = new CustomProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        rootContainer.addView(progressBar);
        return progressBar;
    }








    private static void ClearProgressBar(Activity activity) {
        try {
            FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
            int childviewcount = rootContainer.getChildCount();
            if (childviewcount > 1) {
                for (int index = 0; index < (childviewcount - 1); ++index) {
                    rootContainer.removeViewAt(rootContainer.getChildCount() - 1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static CustomProgressBar createProgressBar(Activity activity) {
        ClearProgressBar(activity);
        INSTANCE = initProgressBar(activity);
        return INSTANCE;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_customprogressbar, null);
        imageView = (ImageView) view.findViewById(R.id.img);
        this.setGravity(Gravity.CENTER);
        this.addView(view);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_progress);
        animation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            imageView.startAnimation(animation);
        } else {
            imageView.clearAnimation();
        }
        super.setVisibility(visibility);
    }

    public void show() {
        if (INSTANCE != null) {
            INSTANCE.setVisibility(View.VISIBLE);
        }
    }

    public void dismiss() {
        if (INSTANCE != null) {
            INSTANCE.setVisibility(View.GONE);
        }
    }
}

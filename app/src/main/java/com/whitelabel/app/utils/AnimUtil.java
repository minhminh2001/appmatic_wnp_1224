package com.whitelabel.app.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.whitelabel.app.R;

/**
 * Created by ray on 2015/11/6.
 */

public class AnimUtil {
    public static ObjectAnimator alpha_0_1_500(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(500).start();
        return animator;
    }


    public static void setWishIconColorToBlank(final ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        final ScaleAnimation animation2 = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(250);//设置动画持续时间
        animation2.setFillAfter(false);//动画执行完后是否停留在执行完的状态
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivWishIcon.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivWishIcon.startAnimation(animation2);
    }
    public static void setWishIconColorToPurple(final ImageView ivWishIcon, final ImageView ivWishIcon2) {
        ivWishIcon2.setVisibility(View.VISIBLE);
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
//        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        final ScaleAnimation animation2 = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(250);//设置动画持续时间
        animation2.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivWishIcon2.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivWishIcon.startAnimation(animation2);
    }


    public static   void  animateFadeIn(Context context,View view, Animation.AnimationListener  listener){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in);
        animation.setAnimationListener(listener);
        view.startAnimation(animation);
    }

    public static void animateLayoutChange(final View view) {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);

        final Animator collapseExpandAnim = ObjectAnimator.ofPropertyValuesHolder(view, pvhLeft, pvhTop,
                pvhRight, pvhBottom);
        collapseExpandAnim.setupStartValues();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                collapseExpandAnim.setupEndValues();
                collapseExpandAnim.start();
                return false;
            }
        });
    }
    public static void rotateArrow(View arrow, boolean rotate){
        rotateArrow( arrow.getContext(),arrow,  rotate);
    }
    public static void rotateArrow(Context context, View arrow, boolean rotate){
        // rotate=true旋转180度, 否则从180旋转复位
        if(rotate) {
            Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_to_180);
            operatingAnim.setFillAfter(true);
            arrow.startAnimation(operatingAnim);
        }else {
            Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_from180);
            arrow.startAnimation(operatingAnim);
        }
    }
    public static void animatePlusSign(final View plus, boolean open, Context context) {
        if (open) {
            if (plus.getVisibility() == View.GONE) {
                return;
            }
            Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_category_rotate);
            plus.startAnimation(operatingAnim);
            operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    plus.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            if (plus.getVisibility() == View.VISIBLE) {
                return;
            }
            plus.setVisibility(View.VISIBLE);
            Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_category_rotate_tozero);
            plus.startAnimation(operatingAnim);
            operatingAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
}

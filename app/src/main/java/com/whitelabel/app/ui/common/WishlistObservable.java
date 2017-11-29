package com.whitelabel.app.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.utils.JImageUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public  class WishlistObservable implements Observable.OnSubscribe<SVRAppserviceProductSearchResultsItemReturnEntity>,View.OnClickListener{
        public interface IWishIconUnLogin{
            void clickWishToLogin();
        }
        IWishIconUnLogin iWishIconUnLogin;
         private View view;
         private SVRAppserviceProductSearchResultsItemReturnEntity entity;
         private ImageView ivWishIcon;
         private ImageView  ivWishIcon2;
         private List<Subscriber<? super SVRAppserviceProductSearchResultsItemReturnEntity>> mSubscribers = new ArrayList<>();
         public WishlistObservable(View view , SVRAppserviceProductSearchResultsItemReturnEntity entity, ImageView ivWishIcon, ImageView ivWishIcon2,IWishIconUnLogin iWishIconUnLogin){
             this.view =view;
             this.entity=entity;
             this.ivWishIcon=ivWishIcon;
             this.ivWishIcon2=ivWishIcon2;
             this.iWishIconUnLogin=iWishIconUnLogin;
         }
         @Override
         public void call(Subscriber<? super SVRAppserviceProductSearchResultsItemReturnEntity> subscriber) {
             mSubscribers.add(subscriber);
             view.setOnClickListener(this);
         }
         @Override
         public void onClick(View view) {
             if(!WhiteLabelApplication.getAppConfiguration().isSignIn(view.getContext())){
                 iWishIconUnLogin.clickWishToLogin();
                 Intent intent = new Intent();
                 intent.setClass(view.getContext(), LoginRegisterActivity.class);
                 ((Activity)view.getContext()).startActivityForResult(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
                 ((Activity)view.getContext()).overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
                 return;
             }
             for(Subscriber subscriber1:mSubscribers){
                 subscriber1.onNext(entity);
             }
             if (entity.getIsLike() == 1) {
                 ivWishIcon.setVisibility(View.VISIBLE);
                 boolean repeatAnim = true;
                 ivWishIcon.setTag(repeatAnim);
                 ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(), R.mipmap.wishlist_purple_pressed_v2));
                 final ScaleAnimation animation2 = new ScaleAnimation(1f, 0f, 1f, 0f,
                         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                 animation2.setDuration(250);
                 animation2.setFillAfter(false);
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
                 entity.setIsLike(0);
             } else {
                     entity.setIsLike(1);
                     ivWishIcon2.setVisibility(View.VISIBLE);
                     ivWishIcon.setVisibility(View.VISIBLE);
                     boolean repeatAnim = false;
                     ivWishIcon.setTag(repeatAnim);
                     ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
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
                         }
                         @Override
                         public void onAnimationRepeat(Animation animation) {

                         }
                     });
                     ivWishIcon.startAnimation(animation2);
                 }
             }
         }
package com.whitelabel.app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.network.ImageLoader;

/**
 * Created by Administrator on 2016/10/6.
 */
public class UserGuideHelper {
    private Context context;
    private final ImageLoader mImageLoader;

    public UserGuideHelper(Context context, ImageLoader imageLoader) {
        this.context = context;
        mImageLoader = imageLoader;
    }

    public PopupWindow showLeftMenuUserGuide(View view) {
        if (context == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupWindowView = inflater.inflate(R.layout.menu_guide, null);
        final PopupWindow mUserGuidePopWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mUserGuidePopWindow.setOutsideTouchable(false);
        View album = popupWindowView.findViewById(R.id.show);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JStorageUtils.notShowGuide3(context);
                mUserGuidePopWindow.dismiss();
            }
        });
        mUserGuidePopWindow.showAsDropDown(view, 0, 0);
        return mUserGuidePopWindow;
    }

    public PopupWindow showHomeLeftIconUserGuide(View view) {
        if (context == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupWindowView = inflater.inflate(R.layout.popup_home_left_icon_userguide, null);
        PopupWindow mUserGuidePopWindow = null;
        try {
            mUserGuidePopWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            mUserGuidePopWindow.setOutsideTouchable(false);
            View album = popupWindowView.findViewById(R.id.rl_user_guide);
            ImageView ivUserGuide = (ImageView) popupWindowView.findViewById(R.id.display);
//        JImageUtils.displayFromDrawable(R.drawable.dialog_user_guide, ivUserGuide);
            final PopupWindow finalMUserGuidePopWindow = mUserGuidePopWindow;
            album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalMUserGuidePopWindow.dismiss();
                }
            });
            mUserGuidePopWindow.showAsDropDown(view, 0, 0);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return mUserGuidePopWindow;
    }


    public PopupWindow showHomeSecondUserGuide(View view) {
        if (context == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout popupWindowView = (RelativeLayout) inflater.inflate(R.layout.popup_home_second_userguide, null);
        final PopupWindow mUserGuidePopWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mUserGuidePopWindow.setOutsideTouchable(false);
        popupWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserGuidePopWindow.dismiss();
            }
        });
        mUserGuidePopWindow.showAsDropDown(view, 0, 0);
        return mUserGuidePopWindow;
    }

//    public PopupWindow showMyAccountEditUserGuide(View view) {
//        if (context == null) return null;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View popupWindowView = inflater.inflate(R.layout.popup_myaccount_edit_userguide, null);
//        final PopupWindow mUserGuidePopWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
//        mUserGuidePopWindow.setOutsideTouchable(false);
//        ImageView imageView = (ImageView) popupWindowView.findViewById(R.id.display2);
//        JImageUtils.displayFromDrawable(context, R.drawable.dispplay4, mImageLoader, imageView);
//        popupWindowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mUserGuidePopWindow.dismiss();
//            }
//        });
//        mUserGuidePopWindow.showAsDropDown(view, 0, 0);
//        return mUserGuidePopWindow;
//    }

    public PopupWindow showMyAccountAddressUserGuide(View view) {
        if (context == null) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView popupWindowView = (ImageView) inflater.inflate(R.layout.popup_myaccount_addressbook, null);
        final PopupWindow mUserGuidePopWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mUserGuidePopWindow.setOutsideTouchable(false);
        JImageUtils.displayFromDrawable(context, R.drawable.display5, mImageLoader, popupWindowView);
        popupWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserGuidePopWindow.dismiss();
            }
        });
        mUserGuidePopWindow.showAsDropDown(view, 0, 0);
        return mUserGuidePopWindow;
    }
}

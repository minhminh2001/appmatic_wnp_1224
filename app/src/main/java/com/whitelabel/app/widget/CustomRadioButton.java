package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JScreenUtils;

/**
 * Created by ray on 2017/3/27.
 */

public class CustomRadioButton  extends RelativeLayout{
    private ImageView ivBigImage;
    private ImageView ivSmallImage;
//    private  ImageView ivMediumImage;
    public  CustomRadioButton(Context context, AttributeSet  attributeSet){
        super(context,attributeSet);
        View view= LayoutInflater.from(context).inflate(R.layout.view_custom_radiobutton,null);
        ivBigImage= (ImageView) view.findViewById(R.id.iv_big);
        ivSmallImage= (ImageView) view.findViewById(R.id.iv_small);
//        ivMediumImage= (ImageView) view.findViewById(R.id.iv_medium);
        //        ivMediumImage.setBackground(JImageUtils.getColorCircle(context,ContextCompat.getColor(context,R.color.grayf0f0f0)));
        ivBigImage.setBackground(JImageUtils.getThemeCircle(context));
        ivSmallImage.setBackground(JImageUtils.getThemeCircle(context));
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.radio_oval);
        drawable.setStroke(JScreenUtils.dip2px(context,2), WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        ivBigImage.setBackground(drawable);
        addView(view);
    }
}

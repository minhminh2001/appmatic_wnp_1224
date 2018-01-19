package com.whitelabel.app.widget;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomRadioButtonImage extends RelativeLayout{
    private ImageView ivBigImage;
    private Context context;
    public CustomRadioButtonImage(Context context, AttributeSet  attributeSet){
        super(context,attributeSet);
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.view_custom_radiobutton2,null);
        ivBigImage= (ImageView) view.findViewById(R.id.iv_big);
        Drawable pressIcon = JImageUtils.getThemeIcon(context, R.drawable.icon_order_checked);
        ivBigImage.setBackground(pressIcon);
        addView(view);
    }

    public void setSelect(boolean  selected){
        if(selected){
            Drawable pressIcon = JImageUtils.getThemeIcon(context, R.drawable.icon_order_checked);
            ivBigImage.setBackground(pressIcon);
        }else{
            Drawable normalIcon = JImageUtils.getThemeIcon(context, R.drawable.icon_order_nocheck);
            ivBigImage.setBackground(normalIcon);
        }
        invalidate();
    }


}
package com.whitelabel.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.FontCache;
import com.whitelabel.app.utils.JFontUtils;


public class CustomTextView extends TextView {

    private int typefaceIndex;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//		initTypeface(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.CustomTextStyle);
        if (types == null) {
            return;
        }

        typefaceIndex = types.getInt(R.styleable.CustomTextStyle_textFont, -1);

        types.recycle();

        if (typefaceIndex != -1) {
            Typeface typeFace = JFontUtils.getTypeface(context, typefaceIndex);
            if (typeFace != null) {
                this.setTypeface(typeFace);
            }
        }
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setBackground(Drawable background) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                super.setBackground(background);
            } else {
                super.setBackgroundDrawable(background);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setFont(String font, Context context) {
        if (TextUtils.isEmpty(font)) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if (tf != null) {
            setTypeface(tf);
        }
    }
}

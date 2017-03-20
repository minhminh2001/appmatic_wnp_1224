package com.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.common.R;
import com.common.utils.JFontUtils;

//默认单行
public class CustomEditText extends EditText {
    private int typefaceIndex;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface(context, attrs);
        this.setSingleLine(true);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int typefaceIndex) {
        super(context, attrs, defStyleAttr);
        this.typefaceIndex = typefaceIndex;
        initTypeface(context, attrs);
        this.setSingleLine(true);
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


}

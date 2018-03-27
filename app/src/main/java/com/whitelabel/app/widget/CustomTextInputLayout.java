package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

/**
 * Created by Aaron on 2018/3/27.
 */

public class CustomTextInputLayout extends TextInputLayout {
    public CustomTextInputLayout(Context context) {
        super(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        recoverEditTextBackGround();
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        super.setError(error);
        recoverEditTextBackGround();
    }

    /**
     * 将背景颜色重置
     */
    private void recoverEditTextBackGround(){
        if (getEditText() == null) {
            return;
        }
        Drawable editTextBackground = getEditText().getBackground();
        if (editTextBackground == null) {
            return;
        }
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(editTextBackground)) {
            editTextBackground = editTextBackground.mutate();
        }
        DrawableCompat.clearColorFilter(editTextBackground);
    }
}

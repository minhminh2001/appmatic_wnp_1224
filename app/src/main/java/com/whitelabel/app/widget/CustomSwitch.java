package com.whitelabel.app.widget;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Display;

/**
 * Created by img on 2018/1/29.
 */

public class CustomSwitch extends SwitchCompat {

    public CustomSwitch(Context context) {
        super(context);
        initialize(context);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public void initialize(Context context) {
        // Setting up my colors
        int mediumGreen = WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color();
        int mediumGrey = ContextCompat.getColor(context, R.color.gray999999);
        int alphaMediumGreen = Color
            .argb(127, Color.red(mediumGreen), Color.green(mediumGreen), Color.blue(mediumGreen));
        int alphaMediumGrey = Color.argb(127, Color.red(mediumGrey), Color.green(mediumGrey), Color.blue(mediumGrey));
        // Sets the tints for the thumb in different states
        DrawableCompat.setTintList(this.getThumbDrawable(), new ColorStateList(
            new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
            },
            new int[]{
                mediumGreen,
                ContextCompat.getColor(getContext(), R.color.grayf0f0f0)
            }));
        // Sets the tints for the track in different states
        DrawableCompat.setTintList(this.getTrackDrawable(), new ColorStateList(
            new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{}
            },
            new int[]{
                alphaMediumGreen,
                alphaMediumGrey
            }));
    }
}

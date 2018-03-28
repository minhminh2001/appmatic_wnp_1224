package com.whitelabel.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;

/**
 * Created by Aaron on 2018/3/28.
 */

public class ToastUtils extends Toast {

    public ToastUtils(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        return ToastUtils.makeText(context, text, Gravity.BOTTOM, duration);
    }

    public static Toast makeText(Context context, CharSequence text, int gravity, int duration) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_default_toast, null);
        TextView tvText = (TextView) layout.findViewById(R.id.tv_text);
        tvText.setText(text);

        Toast toast = new Toast(context);
        toast.setView(layout);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);

        return toast;
    }
}

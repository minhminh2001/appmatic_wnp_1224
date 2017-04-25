package com.whitelabel.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.widget.circularProgressView.CircularProgressView;
import com.whitelabel.app.widget.circularProgressView.IndeterminateDrawable;

/**
 * Created by ray on 2015/7/29.
 */
public class CustomDialog  extends Dialog{
    private ImageView imageView;
    private IndeterminateDrawable drawable;
    public static final String TOP="top";
    public static final String BOOTOM="bottom";

    public CustomDialog(Context context) {
         this(context, R.style.dialog2,"");
    }
    public CustomDialog(Context context,String location) {
        this(context, R.style.dialog2, location);
    }
    public CustomDialog(Context context, int theme,String location) {
        super(context, theme);
        String mLocation = location;
        if("bottom".equals(mLocation)){
            this.getWindow().getAttributes().gravity = Gravity.BOTTOM;
            setContentView(R.layout.dialog_progress);
            CircularProgressView mProgressView = (CircularProgressView) findViewById(R.id.progress_view);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(JDataUtils.dp2Px(50),JDataUtils.dp2Px(50));
            params.setMargins(0,JDataUtils.dp2Px(20),0,JDataUtils.dp2Px(40));
            mProgressView.setLayoutParams(params);
        }else{
            this.getWindow().getAttributes().gravity = Gravity.CENTER;
            setContentView(R.layout.dialog_progress);

        }
        setCanceledOnTouchOutside(false);
    }
    @Override
    public void dismiss() {
        if(isShowing()) {
            if (getContext() instanceof Activity) {
                Activity activity = (Activity) getContext();
                if (activity != null && !activity.isFinishing()) {
                    super.dismiss();
                }
            } else {
                super.dismiss();
            }
        }
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    public void cancel() {
//        if(drawable!=null) {
//            drawable.stop();
//        }
        super.cancel();
    }


}

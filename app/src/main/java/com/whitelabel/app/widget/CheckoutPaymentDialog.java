package com.whitelabel.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;

/**
 * Created by Administrator on 2016/11/22.
 */

public class CheckoutPaymentDialog extends Dialog {
    private TextView tvContent;
    private String message;
    public CheckoutPaymentDialog(Context context,String message){
        super(context);
        this.message=message;
    }

    public CheckoutPaymentDialog(Context context,int styleId,String message){
        super(context,styleId);
        this.message=message;

    }



    public CheckoutPaymentDialog showDialog(){
        this.show();
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_checkout,null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);// load layout
//        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_checkout_dialog);// ImageView
        tvContent = (TextView) view.findViewById(R.id.tv_checkout_dialog);// hint text
        tvContent.setText(message);// set hint text
         setCanceledOnTouchOutside(false);
        setCancelable(false);// permit "back" to cancel dialog
         setContentView(layout);// set layout params
    }
    public CheckoutPaymentDialog setMessage(String message){
        if(tvContent!=null)
        tvContent.setText(message);
        return this;
    }
}

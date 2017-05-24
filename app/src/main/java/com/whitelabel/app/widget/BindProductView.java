package com.whitelabel.app.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.utils.JToolUtils;
import java.util.List;
/**
 * Created by ray on 2017/5/23.
 */
public class BindProductView extends LinearLayout {
    public BindProductView(Context context) {
        super(context);
    }
    public BindProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
    }
    public BindProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void initData(List<SVRAppserviceProductDetailResultPropertyReturnEntity>  products){
        if(products==null||products.size()==0){
            return;
        }
        removeAllViews();
        int count=products.size()<3?products.size():3;
        for(int i=0;i<count;i++){
            View bindProductItem=createProductView(products.get(i));
            addView(bindProductItem);
            if(i!=count-1){
                addView(createAddView());
            }
        }
    }
    public View createProductView(SVRAppserviceProductDetailResultPropertyReturnEntity bean){
        LinearLayout view= (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_product_bind,null);
        ImageView ivImg= (ImageView) view.findViewById(R.id.iv_img);
        TextView tvBindPrice= (TextView) view.findViewById(R.id.tv_bind_price);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        return view;
    }
    public View createAddView(){
        CustomTextView  customTextView=new CustomTextView(getContext());
//        customTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f));
        customTextView.setPadding(0, JToolUtils.dip2px(getContext(),35),0,0);
        customTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        customTextView.setTextSize(20);
        customTextView.setText("+");
        return customTextView;
    }
}

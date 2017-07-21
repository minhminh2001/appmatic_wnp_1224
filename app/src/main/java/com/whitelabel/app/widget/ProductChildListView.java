package com.whitelabel.app.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ray on 2017/4/12.
 */

public class ProductChildListView extends LinearLayout{
    private List<TextView>  tvNumbers;
     public ProductChildListView(Context context){
        super(context);
     }
     public ProductChildListView(Context context, AttributeSet attributeSet){
         super(context,attributeSet);
     }
    private  ArrayList<ProductPropertyModel>  mBeans;
    private TextView tvTotal;
    public void initProductChildListView(ArrayList<ProductPropertyModel>  beans){
        tvNumbers=new ArrayList<>();
        setPadding(JScreenUtils.dip2px(getContext(),10),0,JScreenUtils.dip2px(getContext(),10),0);
        mBeans=beans;
        if(mBeans==null||mBeans.size()==0){
            return;
        }
        removeAllViews();
        for(ProductPropertyModel bean : beans){
            addView(getProductView(bean));
        }
        addView(getLine());
        tvTotal=new TextView(getContext());
        setTotalPrice();
        tvTotal.setTextSize(16);
        tvTotal.setPadding(0,JScreenUtils.dip2px(getContext(),15),0,0);
        tvTotal.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        addView(tvTotal);
        addView(getLine());
    }
     public  interface OnProductCountChangeListener{
          void  change(int count);
    }
    private OnProductCountChangeListener  mOnProductCountChangeListener;
    public  void  setOnProductCountChangeListener(OnProductCountChangeListener  listener){
          this.mOnProductCountChangeListener=listener;
    }
   // id : qty
    public Map<String,String>  getChildIdAndQty(){
        Map<String,String> hashMap=new HashMap<>();
        for(int i=0;i<mBeans.size();i++){
//            JLogUtils.i("ray","mBeans.get(i).getInStock():"+mBeans.get(i).getInStock() +"==============tvNumbers.get(i).getText().toString():"+tvNumbers.get(i).getText().toString());
             if(mBeans.get(i).getInStock()==1&&!"0".equals(tvNumbers.get(i).getText().toString())){
                 hashMap.put(mBeans.get(i).getId(),tvNumbers.get(i).getText().toString());
             }
        }
        return hashMap;
    }
    private void setTotalPrice(){
         double  totalPrice=0;
         for(int i=0;i<mBeans.size();i++){
             if(mBeans.get(i).getInStock()==1){
                 totalPrice+=Integer.parseInt(tvNumbers.get(i).getText().toString())*Double.parseDouble(mBeans.get(i).getFinalPrice());
             }
         }
         String  totalStr=getContext().getResources().getString(R.string.product_detail_total);
         tvTotal.setText(totalStr+WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+JDataUtils.formatDouble(totalPrice+""));
    }
    public View getLine(){
        View view=new View(getContext());
        float  height= (float) 0.8;
       LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.
                LayoutParams.MATCH_PARENT, JScreenUtils.dip2px(getContext(),height));
         params.setMargins(0,JScreenUtils.dip2px(getContext(),15),0,0);
        view.setLayoutParams(params);
        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.v2_line));
        return view;
    }
    private  View getProductView(final ProductPropertyModel bean){
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_product_group, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_child_name);
        TextView tvChildPrice = (TextView) view.findViewById(R.id.tv_child_price);
        TextView tvChildFinalPrice = (TextView) view.findViewById(R.id.tv_child_final_price);
        View rlChildProductQuantity = view.findViewById(R.id.rlChildProductQuantity);
        View rlChildSold=view.findViewById(R.id.rl_child_sold);
        final ImageView ivChildPriceMinus = (ImageView) view.findViewById(R.id.ivChildPriceMinus);
        final  TextView tvChildNumber = (TextView) view.findViewById(R.id.tv_child_number);
        ImageView ivChildPricePlus = (ImageView) view.findViewById(R.id.ivChildPricePlus);
        textView.setText(bean.getName());
        tvChildPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvChildPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+JDataUtils.formatDouble(bean.getPrice()));
        tvChildFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+JDataUtils.formatDouble(bean.getFinalPrice()));
        if(Double.parseDouble(bean.getFinalPrice())>=Double.parseDouble(bean.getPrice())){
                tvChildPrice.setVisibility(View.GONE);
        }
        tvNumbers.add(tvChildNumber);
        tvChildNumber.setText("0");
        if(bean.getInStock()==0){
            rlChildProductQuantity.setVisibility(View.INVISIBLE);
            rlChildSold.setVisibility(View.VISIBLE);
        }
        ivChildPriceMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=Integer.parseInt(tvChildNumber.getText().toString());
                count--;
                if(count>=0){
                    tvChildNumber.setText(count+"");
                    setTotalPrice();
                }
                if(mOnProductCountChangeListener!=null) {
                    mOnProductCountChangeListener.change(getProductSelectQty());
                }
            }
        });
        ivChildPricePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    int count=Integer.parseInt(tvChildNumber.getText().toString());
                     count++;
                    if(count>bean.getStockQty()){
                        JViewUtils.showNoInventoryToast(getContext(),getContext().getResources().getString(R.string.insufficient_stock));
                    }else{
                        tvChildNumber.setText(count+"");
                        setTotalPrice();
                        if(mOnProductCountChangeListener!=null) {
                            mOnProductCountChangeListener.change(getProductSelectQty());
                        }
                    }
            }
        });
        return view;
    }
    private  int getProductSelectQty(){
        int qty=0;
        for(int i=0;i<tvNumbers.size();i++){
            qty+=Integer.parseInt(tvNumbers.get(i).getText().toString());
        }
        return qty;
    }
}

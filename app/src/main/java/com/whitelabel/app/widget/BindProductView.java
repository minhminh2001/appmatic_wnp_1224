package com.whitelabel.app.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import java.util.List;
/**
 * Created by ray on 2017/5/23.
 */
public class BindProductView extends RelativeLayout {
    public BindProductView(Context context) {
        super(context);
    }

    public BindProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int width=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) getContext());
        imageWidth=(width-JDataUtils.dp2Px(90))/3;
    }
    public BindProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    int imageWidth;
    public void initData(List<ProductPropertyModel>  products, ImageLoader imageLoader){
        int padding = JToolUtils.dip2px(getContext(), 10);
        setPadding(padding, padding, padding,0);
        if(products==null||products.size()==0){
            return;
        }
        removeAllViews();
        TextView tvTitle= getTitleText();
        addView(tvTitle);
        ImageView imageView=getRightImage();
        LayoutParams  layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0, padding,0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP,R.id.bindName);
        addView(imageView,layoutParams);
        int count=products.size()<3?products.size():3;
        LinearLayout  linearLayout=new LinearLayout(getContext());
        LinearLayout.LayoutParams  params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<3;i++){
            View bindProductItem=null;
            if(i<=count-1){
               bindProductItem=createProductView(products.get(i),imageLoader);
            }else{
                bindProductItem=new View(getContext());
                bindProductItem.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
                bindProductItem.setVisibility(View.INVISIBLE);
            }
            linearLayout.addView(bindProductItem);
            if(i<count-1){
                linearLayout.addView(createAddView());
            }
        }
        LayoutParams  bindProductParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        bindProductParams.setMargins(0, padding,0,0);
        bindProductParams.addRule(RelativeLayout.BELOW,R.id.bindName);
        addView(linearLayout,bindProductParams);
    }
    public ImageView  getRightImage(){
      ImageView imageView=new ImageView(getContext());
      imageView.setImageResource(R.mipmap.icon_right);
      return  imageView;
    }
    public  TextView getTitleText(){
        TextView tvTitle=new TextView(getContext());
        tvTitle.setId(R.id.bindName);
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        tvTitle.setText(getContext().getResources().getString(R.string.productdetail_bind_title));
        return tvTitle;
    }
    public View createProductView(ProductPropertyModel bean, ImageLoader imageLoader){
        LinearLayout view= (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_product_bind,null);
        ImageView ivImg= (ImageView) view.findViewById(R.id.iv_img);
        TextView tvBindPrice= (TextView) view.findViewById(R.id.tv_bind_price);
        if(!TextUtils.isEmpty(bean.getImage())){
            ivImg.getLayoutParams().height=imageWidth;
            ivImg.getLayoutParams().width=imageWidth;
            JImageUtils.downloadImageFromServerByUrl(getContext(), imageLoader, ivImg,
                    bean.getImage(),imageWidth,imageWidth);
        }
        tvBindPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+ JDataUtils.formatDouble(bean.getFinalPrice()));
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        return view;
    }
    public View createAddView(){
       final CustomTextView  customTextView=new CustomTextView(getContext());
        customTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        customTextView.setTextSize(20);
        customTextView.setText("+");
        final ViewTreeObserver  viewTreeObserver= customTextView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
               @Override
                public boolean onPreDraw() {
                  customTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                  customTextView.setPadding(0, (imageWidth-customTextView.getMeasuredHeight())/2,0,0);
                  return false;
                }
             }
        );
        return customTextView;
    }
}
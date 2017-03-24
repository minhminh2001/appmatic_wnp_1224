package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.model.Wishlist;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by imaginato on 2015/7/28.
 */
public class MyAccountWishlistAdapter extends ArrayAdapter<Wishlist> {
    public List<Wishlist> list;

    private Context context;
    private final ImageLoader mImageLoader;
    public MyAccountWishlistAdapter(Context context, List<Wishlist> list, ImageLoader imageLoader) {
        super(context, R.layout.fragment_wishlist_item,list);
        this.context = context;
        this.list = list;
        mImageLoader = imageLoader;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View view=convertView;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.fragment_wishlist_item, null);
            viewHolder.imageView=(ImageView) view.findViewById(R.id.whistlist_imageView);
            viewHolder.tvshort = (TextView) view.findViewById(R.id.wishlist_textView1);
            viewHolder.tvpicture = (TextView) view.findViewById(R.id.wishlist_textView2);
            viewHolder.tvoldprice = (TextView) view.findViewById(R.id.wishlist_textView3);
            viewHolder.tvoldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
            viewHolder.tvNewPrice = (TextView) view.findViewById(R.id.wishlist_textView4);
            viewHolder.waitlistUnavailable=(TextView)view.findViewById(R.id.waitlist_unavailable);
            viewHolder.waitlistTrans=(TextView)view.findViewById(R.id.waitlist_trans);
            viewHolder.line=(TextView)view.findViewById(R.id.line);

            viewHolder.ctvWishMerchant=(TextView) view.findViewById(R.id.ctv_wish_merchant);

            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
//        if (position!=list.size()-1){
//            viewHolder.line.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.line.setVisibility(View.GONE);
//        }
        final Wishlist aw = list.get(position);
        viewHolder.tvshort.setText(aw.getName());
        viewHolder.tvpicture.setText(aw.getBrand().toUpperCase());
        viewHolder.tvpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBrandStoreActivity((Activity) view.getContext(),aw.getBrand(),aw.getBrandId());
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String newPrice = JDataUtils.formatDouble(aw.getFinalPrice());//format 返回的是字符串
        String oldPrice = JDataUtils.formatDouble(aw.getPrice());//format 返回的是字符串
        viewHolder.tvNewPrice.setText(context.getResources().getString(R.string.rm) + " "+newPrice);
        if (!newPrice.equals(oldPrice)) {
            viewHolder.tvoldprice.setText(context.getResources().getString(R.string.rm)+" "+ oldPrice);
        } else {
            viewHolder.tvoldprice.setText("");
            viewHolder.tvoldprice.setHeight(0);
        }
        if(aw.getImage()==null||!aw.getImage().equals(String.valueOf(viewHolder.imageView.getTag()))){
            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.imageView, aw.getImage(), JToolUtils.dip2px(getContext(), 120), JToolUtils.dip2px(getContext(), 120));
        }
        viewHolder.imageView.setTag(aw.getImage());
        if (!TextUtils.isEmpty(aw.getAvailability())){
            if (("1").equals(aw.getAvailability())){
                viewHolder.waitlistUnavailable.setVisibility(View.GONE);
                viewHolder.waitlistTrans.setVisibility(View.GONE);
            }else{
                viewHolder.waitlistUnavailable.setVisibility(View.VISIBLE);
                viewHolder.waitlistTrans.setVisibility(View.VISIBLE);
            }
        }else {
            viewHolder.waitlistUnavailable.setVisibility(View.GONE);
            viewHolder.waitlistTrans.setVisibility(View.GONE);

        }

        if(!TextUtils.isEmpty( aw.getVendorDisplayName())) {
            String soldBy=viewHolder.ctvWishMerchant.getContext().getResources().getString(R.string.soldby);
            JLogUtils.d("jay","------"+aw.getVendor_id());
            if(!TextUtils.isEmpty(aw.getVendor_id())){
                viewHolder.ctvWishMerchant.setTextColor(context.getResources().getColor(R.color.purple92018d));
                SpannableStringBuilder ss=new SpannableStringBuilder(soldBy+" "+aw.getVendorDisplayName());
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.ctvWishMerchant.setText(ss);
                if(!"0".equals(aw.getVendor_id())){
                    viewHolder.ctvWishMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MerchantStoreFrontActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, aw.getVendor_id());
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, aw.getVendorDisplayName());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    });
                }else{
                    viewHolder.ctvWishMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, HomeActivity.class);
                            context.startActivity(i);
                            ((Activity)context).overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    });
                }

            }
            else{
                viewHolder.ctvWishMerchant.setText(soldBy+" "+aw.getVendorDisplayName());
                viewHolder.ctvWishMerchant.setTextColor(context.getResources().getColor(R.color.greyB8B8B8));
            }
        }else {
            viewHolder.ctvWishMerchant.setText("");
        }
        return view;
    }

    private void startBrandStoreActivity(Activity activity, String brandName, String brandId){
        if(!"0".equals(brandId)) {
            Intent brandStoreIntent = new Intent(activity, BrandStoreFontActivity.class);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_ID, brandId);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_NAME, brandName);
            activity.startActivity(brandStoreIntent);
            activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        }else{
            Intent intent=new Intent(activity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        }
    }
    private static class ViewHolder{
        ImageView imageView;
        TextView tvshort;
        TextView tvpicture;
        TextView tvoldprice;
        TextView tvNewPrice;
        TextView line;

        TextView waitlistUnavailable;
        TextView waitlistTrans;

        TextView ctvWishMerchant;

    }
}

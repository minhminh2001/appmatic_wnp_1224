package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.MyAccountOrderDetailActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.model.MyAccountOrderInner;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderTrackingInfo;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyAccountOrderDetailAdapter extends BaseAdapter {

    public LinkedList<MyAccountOrderMiddle> list;
    private Context context;
    private String mStatusCode;
    private String mStatus;
    private static final String TAG = "MyAccountOrderDetailAda";
    private final ImageLoader mImageLoader;
    public MyAccountOrderDetailAdapter(Context context, ImageLoader imageLoader,String statusCode,String status) {
        this.context = context;
        this.list = new LinkedList<MyAccountOrderMiddle>();
        MyAccountOrderDetailActivity activity = (MyAccountOrderDetailActivity) context;
        mImageLoader = imageLoader;
        mStatusCode=statusCode;
        mStatus=status;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_myaccount_orders_cell_middle, null);
        }
        MyAccountOrderMiddle orderMiddle = list.get(position);
        TextView tvShippedId = (TextView) convertView.findViewById(R.id.tv_myaccount_order_cell_middle_shipped_id);
        tvShippedId.setText(orderMiddle.getId());
        tvShippedId.setVisibility(View.GONE);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_myaccount_order_cell_middle_shipped_text);
        tvStatus.setText(orderMiddle.getStatus() + ": ");
        tvStatus.setVisibility(View.GONE);
        LinearLayout llMiddleBody = (LinearLayout) convertView.findViewById(R.id.ll_myaccount_order_cellmiddle_body);
        convertView.findViewById(R.id.order_line).setVisibility(View.GONE);
        convertView.findViewById(R.id.order_relative).setVisibility(View.GONE);
        MyAccountOrderInner[] orderInners = orderMiddle.getItems();
        for (int i = 0; i < orderInners.length; i++) {
            final MyAccountOrderInner orderInner = orderInners[i];
            View view_inner = LayoutInflater.from(context).inflate(R.layout.fragment_myorder_list_new_item1, null);
            view_inner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String visibility = orderInner.getVisibility();
                    if (!TextUtils.isEmpty(visibility)) {
                        if (("1").equals(visibility)) {
                            Intent it = new Intent(context, ProductActivity.class);
                            it.putExtra("productId", orderInner.getProductId());
                            ((BaseActivity) context).startActivity(it);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft,
//                                    R.anim.activity_transition_exit_righttoleft);
                        }
                    } else {
                        Intent it = new Intent(context, ProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", orderInner.getProductId());
                        it.putExtras(bundle);
                        ((BaseActivity) context).startActivity(it);
//                        ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft,
//                                R.anim.activity_transition_exit_righttoleft);

                  }
                }
            });
            View line = view_inner.findViewById(R.id.line);
             TextView tvCurreny= (TextView) view_inner.findViewById(R.id.rm_top);
            TextView productId = (TextView) view_inner.findViewById(R.id.order_product_id);
            TextView productBrand = (TextView) view_inner.findViewById(R.id.order_product_brand);
            TextView tvProductName = (TextView) view_inner.findViewById(R.id.tv_orderlist_new_name);
            TextView tvQuantity = (TextView) view_inner.findViewById(R.id.tv_orderlist_new_ext);
            TextView tvPrice = (TextView) view_inner.findViewById(R.id.tv_orderlist_new_pri);
            ImageView image = (ImageView) view_inner.findViewById(R.id.iv_orderlist_new);
            TextView tvColorsAndSize = (TextView) view_inner.findViewById(R.id.tv_orderlist_new_det);
            TextView orderNewStatus=(TextView)view_inner.findViewById(R.id.tv_orderlist_new_status);
            TextView orderStatus=(TextView)view_inner.findViewById(R.id.order_status);
//            TextView tvTrickingInfo=(TextView)view_inner.findViewById(R.id.tv_orderlist_tracking);
            TextView unavailable=(TextView)view_inner.findViewById(R.id.order_detail_unavailable);
            TextView orderDetailTrans=(TextView)view_inner.findViewById(R.id.order_detail_trans);
            TextView orderMerchantName=(TextView)view_inner.findViewById(R.id.tv_orderlist_new_mername);
            final MyAccountOrderTrackingInfo  trackingInfo = orderMiddle.getTrackingInfo();
            tvCurreny.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+"");
//            if (trackingInfo!=null) {
//                tvTrickingInfo.setVisibility(View.VISIBLE);
//                tvTrickingInfo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(context, TrackingInfoActivity.class);
//                        Bundle bundle=new Bundle();
//                        bundle.putString(TrackingInfoActivity.BUNDLE_TITLE, trackingInfo.getTitle());
//                        bundle.putString(TrackingInfoActivity.BUNDLE_URL, trackingInfo.getUrl());
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//                        ((Activity)context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                    }
//                });
//            } else{
//                tvTrickingInfo.setVisibility(View.GONE);
//            }
//            if(list.size()-1==position&&orderInners.length-1==i){
//                line.setVisibility(View.INVISIBLE);
//            }else{
//                line.setVisibility(View.VISIBLE);
//            }
            productBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startBrandStoreActivity(activity,orderInner.getBrand(),orderInner.getBrandId());
                }
            });
            if(!TextUtils.isEmpty(orderInner.getVendorDisplayName())){
                String soldBy=orderMerchantName.getContext().getResources().getString(R.string.soldby);
//                if(!TextUtils.isEmpty(orderInner.getVendor_id())){
//                    orderMerchantName.setTextColor(context.getResources().getColor(R.color.purple92018d));
//                    SpannableStringBuilder ss=new SpannableStringBuilder(soldBy + " " + orderInner.getVendorDisplayName());
//                    ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    orderMerchantName.setText(ss);
//                    if(!"0".equals(orderInner.getVendor_id())){
//                        orderMerchantName.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(context, MerchantStoreFrontActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, orderInner.getVendor_id());
//                                bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, orderInner.getVendorDisplayName());
//                                intent.putExtras(bundle);
//                                context.startActivity(intent);
//                                ((Activity)context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                            }
//                        });
//                    }else{
//                        orderMerchantName.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent i = new Intent(context, HomeActivity.class);
//                                context.startActivity(i);
//                                ((Activity)context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                            }
//                        });
//                    }
//                }
//                else{
                    orderMerchantName.setText(soldBy+ " " + orderInner.getVendorDisplayName());
                    orderMerchantName.setTextColor(context.getResources().getColor(R.color.black));
//                }
            }else{
                orderMerchantName.setText("");
            }

//            TextView tvBrand = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_brand);
//            TextView tvProductName = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_productname);
//            TextView tvQuantity = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_quantity);
//            TextView tvPrice = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_price);
//            ImageView image = (ImageView) view_inner.findViewById(R.id.image_checkout_review_shoppingcart_cell);
//            TextView tvColorsAndSize = (TextView) view_inner.findViewById(R.id.tv_checkout_review_shoppingcart_cell_colorandsize);
//            tvBrand.setText(orderInner.getBrand() == null?"BRAND":orderInner.getBrand().toUpperCase());

            if (!TextUtils.isEmpty(orderInner.getAvailability())) {
                if ("1".equals(orderInner.getAvailability())) {
                    unavailable.setVisibility(View.GONE);
                    orderDetailTrans.setVisibility(View.GONE);
                } else {
                    unavailable.setVisibility(View.VISIBLE);
                    orderDetailTrans.setVisibility(View.VISIBLE);
                }
            } else {
                unavailable.setVisibility(View.GONE);
                orderDetailTrans.setVisibility(View.GONE);
            }
            productId.setVisibility(View.VISIBLE);
            productBrand.setVisibility(View.VISIBLE);
            productId.setText(orderMiddle.getId());
            productBrand.setText(orderInner.getBrand());
            tvProductName.setText(orderInner.getName());
            tvQuantity.setText("Quantity: " + orderInner.getQty());
            tvPrice.setText(JDataUtils.formatDouble(orderInner.getPrice()));
            // Image download
            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, image, orderInner.getImage(), JToolUtils.dip2px(context, 160),
                    JToolUtils.dip2px(context, 160));
            orderNewStatus.setText(mStatus);
            JViewUtils.setStatus(orderStatus,mStatusCode);
            //GET color and size: {"label":xxx,"value":xxx}
            ArrayList<HashMap<String, String>> colorAndSizes = orderInner.getOptions();
            if (colorAndSizes != null && colorAndSizes.size() > 0) {

                StringBuilder attributeStr = new StringBuilder();
                if (colorAndSizes != null && colorAndSizes.size() > 0) {
                    for (int z = 0; z < colorAndSizes.size(); z++) {
                        attributeStr.append(colorAndSizes.get(z).get("value")).append(" | ");
                    }
                    attributeStr = new StringBuilder(attributeStr.substring(0, attributeStr.length() - 2));
                }
                tvColorsAndSize.setText(attributeStr.toString());
            } else {
                tvColorsAndSize.setVisibility(View.GONE);
            }
            llMiddleBody.addView(view_inner);
        }

        return convertView;
    }
//    private void startBrandStoreActivity(Activity activity,String brandName,String brandId){
//        if(!"0".equals(brandId)) {
//            Intent brandStoreIntent = new Intent(activity, BrandStoreFontActivity.class);
//            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_ID, brandId);
//            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_NAME, brandName);
//            activity.startActivity(brandStoreIntent);
//            activity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//        }else{
//            Intent intent=new Intent(activity, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intent);
//            activity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//            activity.finish();
//        }
//    }
}

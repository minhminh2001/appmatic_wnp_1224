package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.ProductDetailCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/22.
 */
public class ProductRecommendedListAdapter extends RecyclerView.Adapter<ProductRecommendedListAdapter.ViewHolderImp> {
    private final String TAG = "ProductRecommendedListAdapter";
    private Context context;
    private MyAccountDao myAccountDao;
    private ProductDao mProductDao;
    private ProductDetailCallback productDetailCallback;
    private ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> list;
    private final ImageLoader mImageLoader;

    public ProductRecommendedListAdapter(Context context,
                                         ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> arrayList, ImageLoader imageLoader, ProductDetailCallback productDetailCallback) {
        this.context = context;
        this.list = arrayList;
        this.productDetailCallback = productDetailCallback;
        mImageLoader = imageLoader;
    }
    public void updateData(ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity>  entities){
        list.clear();
        if(entities!=null&&entities.size()>0){
            list.addAll(entities);
        }
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        int size = list.size();
        if (size % 2 == 0) {
            return size / 2;
        } else {
            return size / 2 + 1;
        }
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public ViewHolderImp onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_productlist_gridlist_rowitem, null);
        return new ViewHolderImp(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderImp holder, int position) {
        final ViewHolderImp viewHolder = holder;
        position = position * 2;
        final int productListArrayListSize = list.size();
        viewHolder.itemView.setVisibility(View.VISIBLE);
        if (position < 0 || productListArrayListSize <= position) {
            viewHolder.itemView.setVisibility(View.GONE);
            return;
        }

        final SVRAppserviceProductRecommendedResultsItemReturnEntity leftProductEntity = list.get(position);
        if (leftProductEntity == null || JDataUtils.isEmpty(leftProductEntity.getProductId())) {
            viewHolder.itemView.setVisibility(View.GONE);
            return;
        }
        // set merchant name
        if (!TextUtils.isEmpty(leftProductEntity.getVendorDisplayName())) {
            String soldBy = viewHolder.ctvLeftProductMerchant.getContext().getResources().getString(R.string.soldby);
//            if (!TextUtils.isEmpty(leftProductEntity.getVendor_id())) {
//                viewHolder.ctvLeftProductMerchant.setTextColor(context.getResources().getColor(R.color.purple92018d));
//                SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + leftProductEntity.getVendorDisplayName());
//                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.ctvLeftProductMerchant.setText(ss);
//                if (!"0".equals(leftProductEntity.getVendor_id())) {
//                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MerchantStoreFrontActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, leftProductEntity.getVendor_id());
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, leftProductEntity.getVendorDisplayName());
//                            intent.putExtras(bundle);
//                            context.startActivity(intent);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                        }
//                    });
//                } else {
//                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(context, HomeActivity.class);
//                            context.startActivity(i);
//                        }
//                    });
//                }
//
//            } else {
                viewHolder.ctvLeftProductMerchant.setText(soldBy + " " + leftProductEntity.getVendorDisplayName());
                viewHolder.ctvLeftProductMerchant.setTextColor(context.getResources().getColor(R.color.black));
//            }
        } else {
            viewHolder.ctvLeftProductMerchant.setText("");
        }

        final int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(((Activity) context));
        final int marginLeft = phoneWidth * 15 / 640;
        final int marginRight = marginLeft;
        final int dividerWidth = phoneWidth * 16 / 640;
        final int destWidth = (phoneWidth - (marginLeft + marginRight) - dividerWidth) / 2;
        final int destHeight = destWidth;

        LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) viewHolder.vProductListDivider.getLayoutParams();
        if (dividerlp != null) {
            dividerlp.width = dividerWidth;
            dividerlp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            viewHolder.vProductListDivider.setLayoutParams(dividerlp);
        }

        LinearLayout.LayoutParams leftProductListlp = (LinearLayout.LayoutParams) viewHolder.llLeftProduct.getLayoutParams();
        if (leftProductListlp != null) {
            viewHolder.llLeftProduct.setPadding(0, 0, 0, dividerWidth);
            leftProductListlp.setMargins(marginLeft, 0, 0, 0);
            viewHolder.llLeftProduct.setLayoutParams(leftProductListlp);
        }

        LinearLayout.LayoutParams rightProductListlp = (LinearLayout.LayoutParams) viewHolder.llRightProduct.getLayoutParams();
        if (rightProductListlp != null) {
            viewHolder.llRightProduct.setPadding(0, 0, 0, dividerWidth);
            rightProductListlp.setMargins(0, 0, marginRight, 0);
            viewHolder.llRightProduct.setLayoutParams(rightProductListlp);
        }

        RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) viewHolder.ivLeftProductImage.getLayoutParams();
        if (leftImagelp != null) {
            leftImagelp.width = destWidth;
            leftImagelp.height = destHeight;
            viewHolder.ivLeftProductImage.setLayoutParams(leftImagelp);
        }

        ///////////////////////russell////////////////////////
        String leftInstock = leftProductEntity.getInStock();
        JLogUtils.i("russell->leftInstock", leftInstock + "");
        if ("0".equalsIgnoreCase(leftInstock)) {
            viewHolder.rlLeftOutOfStock.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlLeftOutOfStock.setVisibility(View.GONE);
        }
        ///////////////////////russell////////////////////////

        final String leftProductImageUrl = leftProductEntity.getSmallImage();
        if (!leftProductImageUrl.equals(String.valueOf(viewHolder.ivLeftProductImage.getTag()))) {
            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
            viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
        }

        String leftProductName = leftProductEntity.getName();
        viewHolder.ctvLeftProductName.setText(leftProductName);

        String leftProductBrand = leftProductEntity.getBrand();
        viewHolder.ctvLeftProductBrand.setText(leftProductBrand.toUpperCase());

        float leftProductPriceFloat = 0.0f;

        String leftProductPrice = leftProductEntity.getPrice();
        try {
            leftProductPriceFloat = Float.parseFloat(leftProductPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }

        float leftProductFinalPriceFloat = 0.0f;
        String leftProductFinalPrice = leftProductEntity.getFinalPrice();
        try {
            leftProductFinalPriceFloat = Float.parseFloat(leftProductFinalPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }

        if (JDataUtils.compare(leftProductFinalPriceFloat, leftProductPriceFloat) < 0) {

            viewHolder.ctvLeftProductPrice.setVisibility(View.VISIBLE);
            viewHolder.ctvLeftProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), JDataUtils.dp2Px(5));
            viewHolder.ctvLeftProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
            viewHolder.ctvLeftProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvLeftProductPrice.setVisibility(View.GONE);
        }
        viewHolder.ctvLeftProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));

        viewHolder.llLeftProduct.setOnClickListener(new View.OnClickListener() {

            private Context context;
            private String productId;

            public View.OnClickListener init(Context context, String i) {
                this.context = context;
                this.productId = i;
                return this;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", this.productId);
                if (viewHolder.ivLeftProductImage.getDrawable() == null) {
                    setBundleAndToPDP(intent, bundle);
                } else {
                    bundle.putString("from", "from_product_list");
                    bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(leftProductEntity));
                    bundle.putString("imageurl", leftProductImageUrl);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && viewHolder.ivLeftProductImage.getTag().toString().equals(leftProductImageUrl)) {
                        intent.putExtras(bundle);
                        ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity) context),
                                viewHolder.ivLeftProductImage,
                                context.getResources().getString(R.string.activity_image_trans));
                        ActivityCompat.startActivityForResult((Activity) context, intent, ProductDetailActivity.RESULT_WISH, aop.toBundle());
                    } else {
                        setBundleAndToPDP(intent, bundle);
                    }
                }
            }
        }.init(context, leftProductEntity.getProductId()));


        ///// Right //////
        position = position + 1;
        if (position < 0 || productListArrayListSize <= position) {
            viewHolder.llRightProduct.setVisibility(View.INVISIBLE);
            return;
        }

        final SVRAppserviceProductRecommendedResultsItemReturnEntity rightProductEntity = list.get(position);
        if (rightProductEntity == null || JDataUtils.isEmpty(rightProductEntity.getProductId())) {
            viewHolder.llRightProduct.setVisibility(View.INVISIBLE);
            return;
        }
        // set merchant name
        if (!TextUtils.isEmpty(rightProductEntity.getVendorDisplayName())) {
            viewHolder.ctvRightProductMerchant.setTextColor(context.getResources().getColor(R.color.purple92018d));
            String soldBy = viewHolder.ctvRightProductMerchant.getContext().getResources().getString(R.string.soldby);
//            if (!TextUtils.isEmpty(rightProductEntity.getVendor_id())) {
//                viewHolder.ctvRightProductMerchant.setTextColor(context.getResources().getColor(R.color.purple92018d));
//                SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + rightProductEntity.getVendorDisplayName());
//                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.ctvRightProductMerchant.setText(ss);
//                if(!"0".equals(rightProductEntity.getVendor_id())){
//                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MerchantStoreFrontActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, rightProductEntity.getVendor_id());
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, rightProductEntity.getVendorDisplayName());
//                            intent.putExtras(bundle);
//                            context.startActivity(intent);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                        }
//                    });
//                }else{
//                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(context, HomeActivity.class);
//                            context.startActivity(i);
//                            ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
//                        }
//                    });
//                }
//
//            } else {
                viewHolder.ctvRightProductMerchant.setText(soldBy + " " + rightProductEntity.getVendorDisplayName());
                viewHolder.ctvRightProductMerchant.setTextColor(context.getResources().getColor(R.color.black));
//            }

        } else {
            viewHolder.ctvRightProductMerchant.setText("");
        }
        viewHolder.llRightProduct.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams rightImagelp = (RelativeLayout.LayoutParams) viewHolder.ivRightProductImage.getLayoutParams();
        if (rightImagelp != null) {
            rightImagelp.width = destWidth;
            rightImagelp.height = destHeight;
            viewHolder.ivRightProductImage.setLayoutParams(rightImagelp);
        }

        final String rightProductImageUrl = rightProductEntity.getSmallImage();
        if (!rightProductImageUrl.equals(String.valueOf(viewHolder.ivRightProductImage.getTag()))) {
            JImageUtils.downloadImageFromServerByUrl(context, mImageLoader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);
            viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
        }

        String rightProductName = rightProductEntity.getName();
        viewHolder.ctvRightProductName.setText(rightProductName);

        String rightProductBrand = rightProductEntity.getBrand();
        viewHolder.ctvRightProductBrand.setText(rightProductBrand.toUpperCase());

        Float rightProductPriceFloat = 0.0f;
        String rightProductPrice = rightProductEntity.getPrice();
        try {
            rightProductPriceFloat = Float.parseFloat(rightProductPrice);
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        float rightProductFinalPriceFloat = 0.0f;
        String rightProductFinalPrice = rightProductEntity.getFinalPrice();
        try {
            rightProductFinalPriceFloat = Float.parseFloat(rightProductFinalPrice);
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        ///////////////////////russell////////////////////////
        String rightInstock = rightProductEntity.getInStock();
        if ("0".equalsIgnoreCase(rightInstock)) {
            viewHolder.rlRightOutOfStock.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlRightOutOfStock.setVisibility(View.GONE);
        }
        ///////////////////////russell////////////////////////

        if (JDataUtils.compare(rightProductFinalPriceFloat, rightProductPriceFloat) < 0) {
            viewHolder.ctvRightProductPrice.setVisibility(View.VISIBLE);
            viewHolder.ctvRightProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), JDataUtils.dp2Px(5));
            viewHolder.ctvRightProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductPriceFloat + ""));
            viewHolder.ctvRightProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvRightProductPrice.setVisibility(View.GONE);

        }
        viewHolder.ctvRightProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductFinalPriceFloat + ""));

        viewHolder.llRightProduct.setOnClickListener(new View.OnClickListener() {
            private Context context;
            private String productId;

            public View.OnClickListener init(Context context, String i) {
                this.context = context;
                this.productId = i;
                return this;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", this.productId);
                if (viewHolder.ivRightProductImage.getDrawable() == null) {
                    setBundleAndToPDP(intent, bundle);
                } else {
                    bundle.putString("from", "from_product_list");
                    bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(rightProductEntity));
                    bundle.putString("imageurl", rightProductImageUrl);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && viewHolder.ivRightProductImage.getTag().toString().equals(rightProductImageUrl)) {
                        intent.putExtras(bundle);
                        ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, viewHolder.ivRightProductImage, context.getResources().getString(R.string.activity_image_trans));
                        ActivityCompat.startActivityForResult((Activity) context, intent, ProductDetailActivity.RESULT_WISH, aop.toBundle());
                    } else {
                        setBundleAndToPDP(intent, bundle);
                    }
                }
            }
        }.init(context, rightProductEntity.getProductId()));


    }

    private ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductRecommendedResultsItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setFinalPrice(e.getFinalPrice());
        entity.setInStock(Integer.parseInt(e.getInStock()));
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        entity.setVendorDisplayName(e.getVendorDisplayName());
        return entity;
    }

    private void setBundleAndToPDP(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
        ((BaseActivity) context).startActivityForResult(intent, ProductDetailActivity.RESULT_WISH);
//        ((Activity) context).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
    }

    public class ViewHolderImp extends RecyclerView.ViewHolder {
        public LinearLayout llLeftProduct, llRightProduct;
        public ImageView ivLeftProductImage, ivRightProductImage;
        public CustomTextView ctvLeftProductName, ctvRightProductName, ctvLeftProductBrand, ctvRightProductBrand,
                ctvLeftProductPrice, ctvRightProductPrice, ctvLeftProductFinalPrice, ctvRightProductFinalPrice, ctvLeftProductMerchant, ctvRightProductMerchant;
        public View vProductListDivider;
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock;
        private View itemView;

        public ViewHolderImp(View view) {
            super(view);
            itemView = view;
            vProductListDivider = itemView.findViewById(R.id.vProductListDivider);
            llLeftProduct = (LinearLayout) itemView.findViewById(R.id.llLeftProduct);
            llRightProduct = (LinearLayout) itemView.findViewById(R.id.llRightProduct);
            ivLeftProductImage = (ImageView) llLeftProduct.findViewById(R.id.ivProductImage);
            ctvLeftProductName = (CustomTextView) llLeftProduct.findViewById(R.id.ctvProductName);
            ctvLeftProductBrand = (CustomTextView) llLeftProduct.findViewById(R.id.ctvProductBrand);
            ctvLeftProductPrice = (CustomTextView) llLeftProduct.findViewById(R.id.ctvProductPrice);
            ctvLeftProductFinalPrice = (CustomTextView) llLeftProduct.findViewById(R.id.ctvProductFinalPrice);
            ctvLeftProductMerchant = (CustomTextView) llLeftProduct.findViewById(R.id.ctv_product_merchant);
            rlLeftOutOfStock = (RelativeLayout) llLeftProduct.findViewById(R.id.rl_product_list_out_of_stock);

            ivRightProductImage = (ImageView) llRightProduct.findViewById(R.id.ivProductImage);
            ctvRightProductName = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductName);
            ctvRightProductBrand = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductBrand);
            ctvRightProductPrice = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductPrice);
            ctvRightProductFinalPrice = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductFinalPrice);
            rlRightOutOfStock = (RelativeLayout) llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
            ctvRightProductMerchant = (CustomTextView) llRightProduct.findViewById(R.id.ctv_product_merchant);

        }
    }




}

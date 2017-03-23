package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.FragmentOnAdapterCallBack;
import com.whitelabel.app.fragment.ProductListBaseFragment;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/13.
 */
public class ProductListAdapter extends BaseAdapter {
    private final String TAG = "ProductListAdapter";
    private ProductListActivity productListActivity;
    private ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList;
    private FragmentOnAdapterCallBack fragmentOnAdapterCallBack;
    private ProductListBaseFragment mProductListBaseFragment;
    private final ImageLoader mImageLoader;

    public ProductListAdapter(ProductListActivity productListActivity, ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList, ImageLoader imageLoader, ProductListBaseFragment productListBaseFragment,FragmentOnAdapterCallBack fragmentOnAdapterCallBack) {
        this.productListActivity = productListActivity;
        this.productItemEntityArrayList = productItemEntityArrayList;
        this.fragmentOnAdapterCallBack = fragmentOnAdapterCallBack;
        mImageLoader = imageLoader;
        mProductListBaseFragment=productListBaseFragment;

    }

    @Override
    public int getCount() {
        int count = 0;
        if (productItemEntityArrayList != null) {
            int productListSize = productItemEntityArrayList.size();
            if (productListSize % 2 == 0) {
                count = productListSize / 2;
            } else {
                count = productListSize / 2 + 1;
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        SVRAppserviceProductSearchResultsItemReturnEntity object = null;
        position = position * 2;
        if (productItemEntityArrayList != null && position >= 0 && productItemEntityArrayList.size() > position) {
            object = productItemEntityArrayList.get(position);
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null&& (boolean)convertView.getTag(R.id.llProductList)==mProductListBaseFragment.isDoubleCol) {
            viewHolder = (ViewHolder) convertView.getTag();
        }else {
            if (mProductListBaseFragment.isDoubleCol) {
                convertView = LayoutInflater.from(productListActivity).inflate(R.layout.adapter_productlist_gridlist_rowitem, null);
            } else {
                convertView = LayoutInflater.from(productListActivity).inflate(R.layout.adapter_productlist_gridlist_ver_rowitem, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.llLeftProduct = (LinearLayout) convertView.findViewById(R.id.llLeftProduct);
            viewHolder.llRightProduct = (LinearLayout) convertView.findViewById(R.id.llRightProduct);
            viewHolder.vProductListDivider = convertView.findViewById(R.id.vProductListDivider);
            viewHolder.vHorDivider= convertView.findViewById(R.id.vHorDivider);

            viewHolder.ivLeftProductImage = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.ivProductImage);
            viewHolder.ctvLeftProductName = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductName);
            viewHolder.ctvLeftProductBrand = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductBrand);
            viewHolder.ctvLeftProductPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductPrice);
            viewHolder.ctvLeftProductFinalPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductFinalPrice);
            viewHolder.ctvLeftProductMerchant = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctv_product_merchant);
            viewHolder.rlLeftOutOfStock = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_product_list_out_of_stock);

            viewHolder.ivRightProductImage = (ImageView) viewHolder.llRightProduct.findViewById(R.id.ivProductImage);
            viewHolder.ctvRightProductName = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductName);
            viewHolder.ctvRightProductBrand = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductBrand);
            viewHolder.ctvRightProductPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductPrice);
            viewHolder.ctvRightProductFinalPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductFinalPrice);
            viewHolder.rlRightOutOfStock = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
            viewHolder.ctvRightProductMerchant = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctv_product_merchant);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.llProductList,mProductListBaseFragment.isDoubleCol);
        }

        if (productItemEntityArrayList == null) {
            convertView.setVisibility(View.GONE);
            return convertView;
        }
        if(!mProductListBaseFragment.isDoubleCol){
            if(position==productItemEntityArrayList.size()-1){
                viewHolder.vHorDivider.setVisibility(View.GONE);
            }else{
                viewHolder.vHorDivider.setVisibility(View.VISIBLE);
            }
        }

        position = position * 2;
        final int productListArrayListSize = productItemEntityArrayList.size();
        if (position < 0 || productListArrayListSize <= position) {
            convertView.setVisibility(View.GONE);
            return convertView;
        }

        final SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity = productItemEntityArrayList.get(position);
        if (leftProductEntity == null || JDataUtils.isEmpty(leftProductEntity.getProductId())) {
            convertView.setVisibility(View.GONE);
            return convertView;
        }
        // set merchant name
        if (!TextUtils.isEmpty(leftProductEntity.getVendorDisplayName())) {
            /**
             *      //用颜色标记文本
             ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
             //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
             Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
             */
            String soldBy=viewHolder.ctvLeftProductMerchant.getContext().getResources().getString(R.string.soldby);
//            if(!TextUtils.isEmpty(leftProductEntity.getVendor_id())){
//                viewHolder.ctvLeftProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.purple92018d));
//                SpannableStringBuilder ss=new SpannableStringBuilder( soldBy+ " " + leftProductEntity.getVendorDisplayName());
//                ss.setSpan(new ForegroundColorSpan(productListActivity.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.ctvLeftProductMerchant.setText(ss);
//                if(!"0".equals(leftProductEntity.getVendor_id())){
//                    final SVRAppserviceProductSearchResultsItemReturnEntity finalLeftProductEntity = leftProductEntity;
//                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(productListActivity, MerchantStoreFrontActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalLeftProductEntity.getVendor_id());
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalLeftProductEntity.getVendorDisplayName());
//                            intent.putExtras(bundle);
//                            productListActivity.startActivity(intent);
//                            productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//                        }
//                    });
//                }else{
//                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(productListActivity, HomeActivity.class);
//                            productListActivity.startActivity(i);
//                            productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//                        }
//                    });
//                }
//
//            }
//            else{
                viewHolder.ctvLeftProductMerchant.setText(soldBy+" " + leftProductEntity.getVendorDisplayName());
                viewHolder.ctvLeftProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.black));
//            }

        } else {
            viewHolder.ctvLeftProductMerchant.setText("");
        }
        convertView.setVisibility(View.VISIBLE);

        final int phoneWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth(productListActivity);
        final int marginLeft = phoneWidth * 15 / 640;
        final int marginRight = marginLeft;
        final int dividerWidth = phoneWidth * 16 / 640;
        final int destWidth = (phoneWidth - (marginLeft + marginRight) - dividerWidth) / 2;
        final int destHeight = destWidth;

        LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) viewHolder.vProductListDivider.getLayoutParams();
        if (dividerlp != null&&mProductListBaseFragment.isDoubleCol) {
            dividerlp.width = dividerWidth;
            dividerlp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            viewHolder.vProductListDivider.setLayoutParams(dividerlp);
        }

        LinearLayout.LayoutParams leftProductListlp = (LinearLayout.LayoutParams) viewHolder.llLeftProduct.getLayoutParams();
        if (leftProductListlp != null) {
            viewHolder.llLeftProduct.setPadding(0, 0, 0, dividerWidth);
            viewHolder.llLeftProduct.setPadding(0, 0, 0, dividerWidth);
            if(mProductListBaseFragment.isDoubleCol){
                leftProductListlp.setMargins(marginLeft, 0, 0, 0);
            }else {
                leftProductListlp.setMargins(marginLeft, 0, marginRight, 0);
            }
            viewHolder.llLeftProduct.setLayoutParams(leftProductListlp);
        }

        LinearLayout.LayoutParams rightProductListlp = (LinearLayout.LayoutParams) viewHolder.llRightProduct.getLayoutParams();
        if (rightProductListlp != null) {
            viewHolder.llRightProduct.setPadding(0, 0, 0, dividerWidth);
            viewHolder.llRightProduct.setPadding(0, 0, 0, dividerWidth);
            if(mProductListBaseFragment.isDoubleCol){
                rightProductListlp.setMargins(0, 0, marginRight, 0);
            }else {
                rightProductListlp.setMargins(marginLeft, 0, marginRight, 0);
            }
            viewHolder.llRightProduct.setLayoutParams(rightProductListlp);
        }

        RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) viewHolder.ivLeftProductImage.getLayoutParams();
        if (leftImagelp != null) {
            if(mProductListBaseFragment.isDoubleCol) {
                leftImagelp.width = destWidth;
                leftImagelp.height = destHeight;
            }
            viewHolder.ivLeftProductImage.setLayoutParams(leftImagelp);
        }

        ///////////////////////russell////////////////////////
        String leftInstock = leftProductEntity.getInStock();
        if ("0".equalsIgnoreCase(leftInstock)) {
            viewHolder.rlLeftOutOfStock.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlLeftOutOfStock.setVisibility(View.GONE);
        }
        ///////////////////////russell////////////////////////

        final String leftProductImageUrl = leftProductEntity.getSmallImage();
        if (!leftProductImageUrl.equals(String.valueOf(viewHolder.ivLeftProductImage.getTag()))) {
            JImageUtils.downloadImageFromServerByUrl(productListActivity, mImageLoader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
            viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
        }

        String leftProductName = leftProductEntity.getName();
        viewHolder.ctvLeftProductName.setText(leftProductName);

        String leftProductBrand = leftProductEntity.getBrand();
        viewHolder.ctvLeftProductBrand.setText(leftProductBrand.toUpperCase());
        viewHolder.ctvLeftProductBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startBrandStoreActivity((Activity) view.getContext(),leftProductEntity.getBrand(),leftProductEntity.getBrandId());
            }
        });

        float leftProductPriceFloat = 0.0f;
        String leftProductPrice = leftProductEntity.getPrice();
        try {
            leftProductPriceFloat = Float.parseFloat(leftProductPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }
        float leftProductFinalPriceFloat = 0.0f;
        String leftProductFinalPrice = leftProductEntity.getFinal_price();
        try {
            leftProductFinalPriceFloat = Float.parseFloat(leftProductFinalPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }

        if (JDataUtils.compare(leftProductFinalPriceFloat, leftProductPriceFloat) < 0) {

            viewHolder.ctvLeftProductPrice.setVisibility(View.VISIBLE);
            viewHolder.ctvLeftProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);

            viewHolder.ctvLeftProductPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
            viewHolder.ctvLeftProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvLeftProductPrice.setVisibility(View.GONE);
        }
        viewHolder.ctvLeftProductFinalPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.llLeftProduct.setOnClickListener(new View.OnClickListener() {

            private ProductListActivity productListActivity;
            private String productId;

            public View.OnClickListener init(ProductListActivity a, String i) {
                this.productListActivity = a;
                this.productId = i;
                return this;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(productListActivity, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", this.productId);
                if (finalViewHolder.ivLeftProductImage.getDrawable() == null) {
                    setBundleAndToPDP(intent, bundle);
                } else {
                    bundle.putString("from", "from_product_list");
                    bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(leftProductEntity));
                    bundle.putString("imageurl", leftProductImageUrl);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        intent.putExtras(bundle);
                        ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(productListActivity, finalViewHolder.ivLeftProductImage, productListActivity.getString(R.string.activity_image_trans));
                        ActivityCompat.startActivityForResult(productListActivity, intent, productListActivity.RESULT_WISH, aop.toBundle());
                    } else {
                        setBundleAndToPDP(intent, bundle);
                    }
                }

            }
        }.init(productListActivity, leftProductEntity.getProductId()));


        ///// Right //////
        position = position + 1;
        if (position < 0 || productListArrayListSize <= position) {
            viewHolder.llRightProduct.setVisibility(View.GONE);
            viewHolder.vHorDivider.setVisibility(View.GONE);
            return convertView;
        }else{
            viewHolder.vHorDivider.setVisibility(View.VISIBLE);
        }

        final SVRAppserviceProductSearchResultsItemReturnEntity rightProductEntity = productItemEntityArrayList.get(position);
        if (rightProductEntity == null || JDataUtils.isEmpty(rightProductEntity.getProductId())) {
            viewHolder.llRightProduct.setVisibility(View.INVISIBLE);
            return convertView;
        }
        // set merchant name
        if (!TextUtils.isEmpty(rightProductEntity.getVendorDisplayName())) {
            String soldBy=viewHolder.ctvRightProductMerchant.getContext().getResources().getString(R.string.soldby);
//            if(!TextUtils.isEmpty(rightProductEntity.getVendor_id())){
//                viewHolder.ctvRightProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.purple92018d));
//                SpannableStringBuilder ss=new SpannableStringBuilder( soldBy+ " " + rightProductEntity.getVendorDisplayName());
//                ss.setSpan(new ForegroundColorSpan(productListActivity.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.ctvRightProductMerchant.setText(ss);
//                if(!"0".equals(rightProductEntity.getVendor_id())){
//                    final SVRAppserviceProductSearchResultsItemReturnEntity finalRightProductEntity = rightProductEntity;
//                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(productListActivity, MerchantStoreFrontActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalRightProductEntity.getVendor_id());
//                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalRightProductEntity.getVendorDisplayName());
//                            intent.putExtras(bundle);
//                            productListActivity.startActivity(intent);
//                            productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//                        }
//                    });
//                }else{
//                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(productListActivity, HomeActivity.class);
//                            productListActivity.startActivity(i);
//                            productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
//                        }
//                    });
//                }
//            }
//            else{
                viewHolder.ctvRightProductMerchant.setText(soldBy+" " + rightProductEntity.getVendorDisplayName());
                viewHolder.ctvRightProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.black));
//            }

        } else {
            viewHolder.ctvRightProductMerchant.setText("");
        }
        viewHolder.llRightProduct.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams rightImagelp = (RelativeLayout.LayoutParams) viewHolder.ivRightProductImage.getLayoutParams();
        if (rightImagelp != null) {
            if(mProductListBaseFragment.isDoubleCol){
                rightImagelp.width = destWidth;
                rightImagelp.height = destHeight;
            }
            viewHolder.ivRightProductImage.setLayoutParams(rightImagelp);
        }

        final String rightProductImageUrl = rightProductEntity.getSmallImage();
        if (!rightProductImageUrl.equals(String.valueOf(viewHolder.ivRightProductImage.getTag()))) {
            JImageUtils.downloadImageFromServerByUrl(productListActivity, mImageLoader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);
            viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
        }

        String rightProductName = rightProductEntity.getName();
        viewHolder.ctvRightProductName.setText(rightProductName);

        String rightProductBrand = rightProductEntity.getBrand();
        viewHolder.ctvRightProductBrand.setText(rightProductBrand.toUpperCase());
        viewHolder.ctvRightProductBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startBrandStoreActivity((Activity) view.getContext(),rightProductEntity.getBrand(),rightProductEntity.getBrandId());
            }
        });
        Float rightProductPriceFloat = 0.0f;
        String rightProductPrice = rightProductEntity.getPrice();
        try {
            rightProductPriceFloat = Float.parseFloat(rightProductPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }

        float rightProductFinalPriceFloat = 0.0f;
        String rightProductFinalPrice = rightProductEntity.getFinal_price();
        try {
            rightProductFinalPriceFloat = Float.parseFloat(rightProductFinalPrice);
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getView", ex);
        }

        ///////////////////////russell////////////////////////
        String rightInstock = rightProductEntity.getInStock();
        JLogUtils.i("russell->rightInstock", rightInstock + "");
        if ("0".equalsIgnoreCase(rightInstock)) {
            viewHolder.rlRightOutOfStock.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlRightOutOfStock.setVisibility(View.GONE);
        }
        ///////////////////////russell////////////////////////

        if (JDataUtils.compare(rightProductFinalPriceFloat, rightProductPriceFloat) < 0) {
            viewHolder.ctvRightProductPrice.setVisibility(View.VISIBLE);
            viewHolder.ctvRightProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);
            viewHolder.ctvRightProductPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductPriceFloat + ""));
            viewHolder.ctvRightProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvRightProductPrice.setVisibility(View.GONE);

        }
        viewHolder.ctvRightProductFinalPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductFinalPriceFloat + ""));

        viewHolder.llRightProduct.setOnClickListener(new View.OnClickListener() {
            private ProductListActivity productListActivity;
            private String productId;

            public View.OnClickListener init(ProductListActivity a, String i) {
                this.productListActivity = a;
                this.productId = i;
                return this;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(this.productListActivity, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", this.productId);
                if (finalViewHolder.ivRightProductImage.getDrawable() == null) {
                    setBundleAndToPDP(intent, bundle);
                } else {
                    bundle.putString("from", "from_product_list");
                    bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(rightProductEntity));
                    bundle.putString("imageurl", rightProductImageUrl);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        intent.putExtras(bundle);
                        ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(productListActivity, finalViewHolder.ivRightProductImage, productListActivity.getString(R.string.activity_image_trans));
                        ActivityCompat.startActivityForResult(productListActivity, intent, ProductListActivity.RESULT_WISH, aop.toBundle());
                    } else {
                        setBundleAndToPDP(intent, bundle);
                    }
                }
            }
        }.init(productListActivity, rightProductEntity.getProductId()));

        return convertView;
    }



    private void setBundleAndToPDP(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
        fragmentOnAdapterCallBack.startActivityForResultCallBack(intent, productListActivity.RESULT_WISH);
        this.productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    public class ViewHolder {
        public LinearLayout llLeftProduct, llRightProduct;
        public ImageView ivLeftProductImage, ivRightProductImage;
        public CustomTextView ctvLeftProductName, ctvRightProductName, ctvLeftProductBrand, ctvRightProductBrand,
                ctvLeftProductPrice, ctvRightProductPrice, ctvLeftProductFinalPrice, ctvRightProductFinalPrice, ctvLeftProductMerchant, ctvRightProductMerchant;
        public View vProductListDivider,vHorDivider;
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock;
    }


    private ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductSearchResultsItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setCategory(e.getCategory());
        entity.setFinalPrice(e.getFinal_price());
        entity.setInStock(Integer.parseInt(e.getInStock()));
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        entity.setVendorDisplayName(e.getVendorDisplayName());
        return entity;
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
            activity.finish();
        }
    }
}

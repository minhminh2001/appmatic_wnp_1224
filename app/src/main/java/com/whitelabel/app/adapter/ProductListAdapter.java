package com.whitelabel.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.FragmentOnAdapterCallBack;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.fragment.ProductListBaseFragment;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/13.
 */
public class ProductListAdapter extends BaseAdapter {
    public static final int VIEW_SWITCH_STATUS_SINGLE = 1;
    public static final int VIEW_SWITCH_STATUS_DOUBLE = 2;
    private final String TAG = "ProductListAdapter";
    private ProductListActivity productListActivity;
    private MyAccountDao myAccountDao;
    private ProductDao mProductDao;
    private ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList;
    private FragmentOnAdapterCallBack fragmentOnAdapterCallBack;
    private ProductListBaseFragment mProductListBaseFragment;
    private final ImageLoader mImageLoader;

    public ProductListAdapter(ProductListActivity productListActivity, ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList, ImageLoader imageLoader, ProductListBaseFragment productListBaseFragment, FragmentOnAdapterCallBack fragmentOnAdapterCallBack) {
        this.productListActivity = productListActivity;
        this.productItemEntityArrayList = productItemEntityArrayList;
        this.fragmentOnAdapterCallBack = fragmentOnAdapterCallBack;
        DataHandler dataHandler = new DataHandler(productListActivity, this);
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
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
            viewHolder.rlLeftProductlistWish = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_productlist_wish);
            viewHolder.ivLeftProductlistWishIcon = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.iv_productlist_wish_icon);
            viewHolder.ivLeftProductlistWishIcon2 = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.iv_productlist_wish_icon2);
            viewHolder.rlLeftOutOfStock = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_product_list_out_of_stock);

            viewHolder.ivRightProductImage = (ImageView) viewHolder.llRightProduct.findViewById(R.id.ivProductImage);
            viewHolder.ctvRightProductName = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductName);
            viewHolder.ctvRightProductBrand = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductBrand);
            viewHolder.ctvRightProductPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductPrice);
            viewHolder.ctvRightProductFinalPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductFinalPrice);
            viewHolder.rlRightOutOfStock = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
            viewHolder.ctvRightProductMerchant = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctv_product_merchant);
            viewHolder.rlRightProductlistWish = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_productlist_wish);
            viewHolder.ivRightProductlistWishIcon = (ImageView) viewHolder.llRightProduct.findViewById(R.id.iv_productlist_wish_icon);
            viewHolder.ivRightProductlistWishIcon2 = (ImageView) viewHolder.llRightProduct.findViewById(R.id.iv_productlist_wish_icon2);

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
            if(!TextUtils.isEmpty(leftProductEntity.getVendor_id())){
                viewHolder.ctvLeftProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.black000000));
                SpannableStringBuilder ss=new SpannableStringBuilder( soldBy+ " " + leftProductEntity.getVendorDisplayName());
                ss.setSpan(new ForegroundColorSpan(productListActivity.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.ctvLeftProductMerchant.setText(ss);
                if(!"0".equals(leftProductEntity.getVendor_id())){
                    final SVRAppserviceProductSearchResultsItemReturnEntity finalLeftProductEntity = leftProductEntity;
                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(productListActivity, MerchantStoreFrontActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalLeftProductEntity.getVendor_id());
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalLeftProductEntity.getVendorDisplayName());
                            intent.putExtras(bundle);
                            productListActivity.startActivity(intent);
//                            productListActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                }else{
                    viewHolder.ctvLeftProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(productListActivity, HomeActivity.class);
                            productListActivity.startActivity(i);
//                            productListActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                }

            }
            else{
                viewHolder.ctvLeftProductMerchant.setText(soldBy+" " + leftProductEntity.getVendorDisplayName());
                viewHolder.ctvLeftProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.greyB8B8B8));
            }

        } else {
            viewHolder.ctvLeftProductMerchant.setText("");
        }
        convertView.setVisibility(View.VISIBLE);

        final int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(productListActivity);
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
        if(!TextUtils.isEmpty(leftProductBrand)) {
            viewHolder.ctvLeftProductBrand.setText(leftProductBrand.toUpperCase());
            viewHolder.ctvLeftProductBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startBrandStoreActivity((Activity) view.getContext(), leftProductEntity.getBrand(), leftProductEntity.getBrandId());
                }
            });
        }

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

            viewHolder.ctvLeftProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
            viewHolder.ctvLeftProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvLeftProductPrice.setVisibility(View.GONE);
        }
        viewHolder.ctvLeftProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));

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
                        ActivityCompat.startActivity(productListActivity, intent, aop.toBundle());
                    } else {
                        setBundleAndToPDP(intent, bundle);
                    }
                }

            }
        }.init(productListActivity, leftProductEntity.getProductId()));

        //wish icon
        //初始化 wish icon的状态
        if (leftProductEntity.getIsLike() == 1) {
            setWishIconColorToPurpleNoAnim(viewHolder.ivLeftProductlistWishIcon);
        } else {
            setWishIconColorToBlankNoAnim(viewHolder.ivLeftProductlistWishIcon);
        }

        final int tempPosition = position;
        viewHolder.rlLeftProductlistWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftProductEntity.getIsLike() == 1) {
                    sendRequestToDeteleteCell(viewHolder.ivLeftProductlistWishIcon, viewHolder.rlLeftProductlistWish, leftProductEntity.getItem_id(), tempPosition);
                } else {
                    addtoWishlistsendRequest(leftProductEntity, viewHolder.rlLeftProductlistWish, viewHolder.ivLeftProductlistWishIcon, viewHolder.ivLeftProductlistWishIcon2, tempPosition);
                }
            }
        });
        if (addProductToWishWhenLoginSuccess(leftProductEntity.getProductId())) {
            addtoWishlistsendRequest(leftProductEntity, viewHolder.rlLeftProductlistWish, viewHolder.ivLeftProductlistWishIcon, viewHolder.ivLeftProductlistWishIcon2, tempPosition);
        }
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
            if(!TextUtils.isEmpty(rightProductEntity.getVendor_id())){
                viewHolder.ctvRightProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.black000000));
                SpannableStringBuilder ss=new SpannableStringBuilder( soldBy+ " " + rightProductEntity.getVendorDisplayName());
                ss.setSpan(new ForegroundColorSpan(productListActivity.getResources().getColor(R.color.greyB8B8B8)),0,soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.ctvRightProductMerchant.setText(ss);
                if(!"0".equals(rightProductEntity.getVendor_id())){
                    final SVRAppserviceProductSearchResultsItemReturnEntity finalRightProductEntity = rightProductEntity;
                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(productListActivity, MerchantStoreFrontActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalRightProductEntity.getVendor_id());
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalRightProductEntity.getVendorDisplayName());
                            intent.putExtras(bundle);
                            productListActivity.startActivity(intent);
//                            productListActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                }else{
                    viewHolder.ctvRightProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(productListActivity, HomeActivity.class);
                            productListActivity.startActivity(i);
//                            productListActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                }
            }
            else{
                viewHolder.ctvRightProductMerchant.setText(soldBy+" " + rightProductEntity.getVendorDisplayName());
                viewHolder.ctvRightProductMerchant.setTextColor(productListActivity.getResources().getColor(R.color.greyB8B8B8));
            }

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
        if(!TextUtils.isEmpty(rightProductBrand)) {
            viewHolder.ctvRightProductBrand.setText(rightProductBrand.toUpperCase());
            viewHolder.ctvRightProductBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startBrandStoreActivity((Activity) view.getContext(), rightProductEntity.getBrand(), rightProductEntity.getBrandId());
                }
            });
        }
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
            viewHolder.ctvRightProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductPriceFloat + ""));
            viewHolder.ctvRightProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            viewHolder.ctvRightProductPrice.setVisibility(View.GONE);

        }
        viewHolder.ctvRightProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductFinalPriceFloat + ""));

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
        //wish icon
        if (rightProductEntity.getIsLike() == 1) {
            setWishIconColorToPurpleNoAnim(viewHolder.ivRightProductlistWishIcon);
        } else {
            setWishIconColorToBlankNoAnim(viewHolder.ivRightProductlistWishIcon);
        }
        final int rightTempPosition = position;
        viewHolder.rlRightProductlistWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果 islike，且有itemId，就执行删除 wish item
                if (rightProductEntity.getIsLike() == 1) {
                    sendRequestToDeteleteCell(viewHolder.ivRightProductlistWishIcon, viewHolder.rlRightProductlistWish, rightProductEntity.getItem_id(), rightTempPosition);
                } else {
                    addtoWishlistsendRequest(rightProductEntity, viewHolder.rlRightProductlistWish, viewHolder.ivRightProductlistWishIcon, viewHolder.ivRightProductlistWishIcon2, rightTempPosition);
                }
            }
        });
        if (addProductToWishWhenLoginSuccess(rightProductEntity.getProductId())) {
            if (!rightProductEntity.getSyncnServering()) {
                addtoWishlistsendRequest(rightProductEntity, viewHolder.rlRightProductlistWish, viewHolder.ivRightProductlistWishIcon, viewHolder.ivRightProductlistWishIcon2, rightTempPosition);
            }
        }
        return convertView;
    }

    public boolean addProductToWishWhenLoginSuccess(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        if (productListActivity.operateProductIdPrecache != null && !TextUtils.isEmpty(productId)) {
            if (productId.equals(productListActivity.operateProductIdPrecache.getProductId())) {
                if (productListActivity.operateProductIdPrecache.isAvailable()) {
                    productListActivity.operateProductIdPrecache = null;
                    return true;
                } else {
                    productListActivity.operateProductIdPrecache = null;
                }
            }
        }
        return false;
    }

    private void setBundleAndToPDP(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
        fragmentOnAdapterCallBack.startActivityForResultCallBack(intent, productListActivity.RESULT_WISH);
        this.productListActivity.startActivityTransitionAnim();
    }

    public class ViewHolder {
        public LinearLayout llLeftProduct, llRightProduct;
        public ImageView ivLeftProductImage, ivRightProductImage, ivLeftProductlistWishIcon, ivLeftProductlistWishIcon2, ivRightProductlistWishIcon, ivRightProductlistWishIcon2;
        public CustomTextView ctvLeftProductName, ctvRightProductName, ctvLeftProductBrand, ctvRightProductBrand,
                ctvLeftProductPrice, ctvRightProductPrice, ctvLeftProductFinalPrice, ctvRightProductFinalPrice, ctvLeftProductMerchant, ctvRightProductMerchant;
        public View vProductListDivider,vHorDivider;
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock, rlLeftProductlistWish, rlRightProductlistWish;
    }

    private void setWishIconColorToBlankNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.GONE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_normal_v2);
    }

    private void setWishIconColorToBlank(final ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
//        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        final ScaleAnimation animation2 = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(250);//设置动画持续时间
        animation2.setFillAfter(false);//动画执行完后是否停留在执行完的状态
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivWishIcon.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivWishIcon.startAnimation(animation2);
    }

    private void setWishIconColorToPurpleNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
//        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }
    private void setWishIconColorToPurple(ImageView ivWishIcon, final ImageView ivWishIcon2) {
        ivWishIcon2.setVisibility(View.VISIBLE);
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
//        ivWishIcon.setImageResource(R.mipmap.r);

        final ScaleAnimation animation2 = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(250);//设置动画持续时间
        animation2.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivWishIcon.startAnimation(animation2);
    }

    //调用删除接口
    private void sendRequestToDeteleteCell(ImageView ivWwishIcon, RelativeLayout rlCurationWish, String itemId, int tempPosition) {
        setWishIconColorToBlank(ivWwishIcon);
        productItemEntityArrayList.get(tempPosition).setIsLike(0);
        if (!TextUtils.isEmpty(productItemEntityArrayList.get(tempPosition).getItem_id())) {
            myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUserInfo(productListActivity).getSessionKey(), itemId, tempPosition);
        }
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

    private void addtoWishlistsendRequest(SVRAppserviceProductSearchResultsItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(productListActivity)) {
            entity.setIsLike(1);
            mProductDao.addProductListToWish(entity.getProductId(), WhiteLabelApplication.getAppConfiguration().getUserInfo(productListActivity).getSessionKey(), tempPosition);
            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
        } else {
            productListActivity.saveProductIdWhenJumpLoginPage(entity.getProductId());
            Intent intent = new Intent();
            intent.setClass(productListActivity, LoginRegisterActivity.class);
            fragmentOnAdapterCallBack.startActivityForResultCallBack(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
            productListActivity.overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<ProductListAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, ProductListAdapter productListAdapter) {
            mAdapter = new WeakReference<ProductListAdapter>(productListAdapter);
            mContext = new WeakReference<Context>(context);
        }


        @Override
        public void handleMessage(Message msg) {
            if (mAdapter.get() == null || mContext.get() == null) {
                return;
            }

            switch (msg.what) {
                case MyAccountDao.REQUEST_DELETEWISHLIST:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        WishDelEntityResult wishDelEntityResult = (WishDelEntityResult) msg.obj;
                        int position = Integer.parseInt(String.valueOf(wishDelEntityResult.getParams()));
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(mAdapter.get().productListActivity, wishDelEntityResult.getWishListItemCount());
                    } else {
                        ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                        int position = Integer.parseInt(String.valueOf(bean.getParams()));
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        int position = Integer.parseInt(String.valueOf(addToWishlistEntity.getParams()));
                        if (mAdapter.get().productItemEntityArrayList == null || mAdapter.get().productItemEntityArrayList.size() == 0)
                            return;
                        SVRAppserviceProductSearchResultsItemReturnEntity productEntity =
                                (SVRAppserviceProductSearchResultsItemReturnEntity) mAdapter.get().productItemEntityArrayList.get(position);
                        productEntity.setItem_id(addToWishlistEntity.getItemId());
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(mAdapter.get().productListActivity, addToWishlistEntity.getWishListItemCount());
                        try {
                            GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
                                    "Add To Wishlist",
                                    productEntity.getName(),
                                    Long.valueOf(productEntity.getProductId()));
                            JLogUtils.i("googleGA", "add to wishlist ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                        if (!TextUtils.isEmpty(bean.getErrorMessage())) {
                            Toast.makeText(mAdapter.get().productListActivity, bean.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
                case MyAccountDao.ERROR:
                case ProductDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mAdapter.get().productListActivity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }
//    private void startBrandStoreActivity(Activity activity, String brandName, String brandId){
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

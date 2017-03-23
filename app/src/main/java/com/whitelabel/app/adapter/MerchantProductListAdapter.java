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
import android.text.TextUtils;
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
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductMerchantReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MerchantProductListAdapter extends BaseAdapter {
    public static final int TYPE_BAR = 0;
    public static final int TYPE_CONTENT = 1;
    private final String TAG = "MerchantProductListAdapter";
    private MerchantStoreFrontActivity mMerchantStoreFrontActivity;
    private SVRAppserviceProductMerchantReturnEntity mMerchantEntity;
    private ArrayList<SVRAppserviceProductItemReturnEntity> mProductItemEntityArrayList;
    private MyAccountDao myAccountDao;
    private ProductDao mProductDao;
    private ImageLoader mImageloader;

    public MerchantProductListAdapter(MerchantStoreFrontActivity merchantActivity, SVRAppserviceProductMerchantReturnEntity merchantEntity, ArrayList<SVRAppserviceProductItemReturnEntity> productItemEntityArrayList, ImageLoader imageLoader) {
        this.mMerchantStoreFrontActivity = merchantActivity;
        this.mMerchantEntity = merchantEntity;
        this.mProductItemEntityArrayList = productItemEntityArrayList;
        DataHandler dataHandler = new DataHandler(merchantActivity, this);
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
        mImageloader = imageLoader;

    }

    @Override
    public int getCount() {
        int count = 0;
        if (mProductItemEntityArrayList != null) {
            count = mProductItemEntityArrayList.size() + 1;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BAR;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        if (position <= 0) {
            return mMerchantEntity;
        } else {
            position = position * 2;
            SVRAppserviceProductItemReturnEntity object = null;
            if (mProductItemEntityArrayList != null && position >= 0 && mProductItemEntityArrayList.size() > position) {
                object = mProductItemEntityArrayList.get(position);
            }
            return object;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_BAR:{
                final ViewHolderBar viewHolderBar;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mMerchantStoreFrontActivity).inflate(R.layout.layout_top_switch_and_filter_bar, null);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,JDataUtils.dp2Px(40));
                    convertView.setLayoutParams(params);
                    viewHolderBar = new ViewHolderBar(convertView);
                    convertView.setTag(viewHolderBar);
                } else {
                    viewHolderBar = (ViewHolderBar) convertView.getTag();
                }
                if (mMerchantStoreFrontActivity.isDoubleCol) {
                    viewHolderBar.mHeaderViewToggle.setImageResource(R.mipmap.ic_view_single);
                } else {
                    viewHolderBar.mHeaderViewToggle.setImageResource(R.mipmap.ic_view_double);
                }
                viewHolderBar.mHeaderViewToggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMerchantStoreFrontActivity.toggleViewOption(viewHolderBar.mHeaderViewToggle);
                    }
                }); viewHolderBar.mHeaderFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMerchantStoreFrontActivity.filterSortOption(mMerchantStoreFrontActivity.TYPE_FILTER);
                    }
                }); viewHolderBar.mHeaderSort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMerchantStoreFrontActivity.filterSortOption(mMerchantStoreFrontActivity.TYPE_SORT);
                    }
                });
            }

                break;
            case TYPE_CONTENT:{
                ViewHolder viewHolder = null;
                if (convertView != null && (boolean) convertView.getTag(R.id.llProductList) == mMerchantStoreFrontActivity.isDoubleCol) {
                    viewHolder = (ViewHolder) convertView.getTag();
                } else {
                    if (mMerchantStoreFrontActivity.isDoubleCol) {
                        convertView = LayoutInflater.from(mMerchantStoreFrontActivity).inflate(R.layout.adapter_curation_productlist_rowitem, null);
                    } else {
                        convertView = LayoutInflater.from(mMerchantStoreFrontActivity).inflate(R.layout.adapter_curation_productlist_ver_rowitem, null);
                    }
                    viewHolder = new ViewHolder();
                    viewHolder.llProductList = (LinearLayout) convertView.findViewById(R.id.llProductList);
                    viewHolder.llLeftProduct = (LinearLayout) convertView.findViewById(R.id.llLeftProduct);
                    viewHolder.vProductListDivider = convertView.findViewById(R.id.vProductListDivider);
                    viewHolder.vHorDivider = convertView.findViewById(R.id.vHorDivider);

                    viewHolder.ivLeftProductImage = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.ivProductImage);
                    viewHolder.ctvLeftProductName = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductName);
                    viewHolder.ctvLeftProductBrand = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductBrand);
                    viewHolder.ctvLeftProductPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductPrice);
                    viewHolder.ctvLeftProductFinalPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductFinalPrice);
                    viewHolder.rlLeftOutOfStock = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_product_list_out_of_stock);
//           right
                    viewHolder.llRightProduct = (LinearLayout) convertView.findViewById(R.id.llRightProduct);
                    viewHolder.ivRightProductImage = (ImageView) viewHolder.llRightProduct.findViewById(R.id.ivProductImage);
                    viewHolder.ctvRightProductName = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductName);
                    viewHolder.ctvRightProductBrand = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductBrand);
                    viewHolder.ctvRightProductPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductPrice);
                    viewHolder.ctvRightProductFinalPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductFinalPrice);
                    viewHolder.rlRightOutOfStock = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
                    convertView.setTag(viewHolder);
                    convertView.setTag(R.id.llProductList, mMerchantStoreFrontActivity.isDoubleCol);
                }
                viewHolder.llProductList.setVisibility(View.VISIBLE);
                if (mProductItemEntityArrayList == null) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                //Left
                position = (position - 1) * 2;
                final int tempPosition = position;
                final int productListArrayListSize = mProductItemEntityArrayList.size();
                if (position < 0 || productListArrayListSize <= position) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                final SVRAppserviceProductItemReturnEntity
                        leftProductEntity = mProductItemEntityArrayList.get(position);
                if (leftProductEntity == null || JDataUtils.isEmpty(leftProductEntity.getProductId())) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                convertView.setVisibility(View.VISIBLE);

                final int phoneWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth();
                final int marginLeft = phoneWidth * 15 / 640;
                final int marginRight = marginLeft;
                final int dividerWidth = phoneWidth * 16 / 640;
                final int destWidth = (phoneWidth - (marginLeft + marginRight) - dividerWidth) / 2;
                final int destHeight = destWidth;

                LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) viewHolder.vProductListDivider.getLayoutParams();
                if (dividerlp != null && mMerchantStoreFrontActivity.isDoubleCol) {
                    dividerlp.width = dividerWidth;
                    dividerlp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    viewHolder.vProductListDivider.setLayoutParams(dividerlp);
                }
                if (!mMerchantStoreFrontActivity.isDoubleCol) {
                    if (position == mProductItemEntityArrayList.size() - 1) {
                        viewHolder.vHorDivider.setVisibility(View.GONE);
                    } else {
                        viewHolder.vHorDivider.setVisibility(View.VISIBLE);
                    }
                }

                LinearLayout.LayoutParams leftProductListlp = (LinearLayout.LayoutParams) viewHolder.llLeftProduct.getLayoutParams();
                if (leftProductListlp != null) {
                    viewHolder.llLeftProduct.setPadding(0, 0, 0, dividerWidth);
                    if (mMerchantStoreFrontActivity.isDoubleCol) {
                        leftProductListlp.setMargins(marginLeft, 0, 0, 0);
                    } else {
                        leftProductListlp.setMargins(marginLeft, 0, marginRight, 0);
                    }
                    viewHolder.llLeftProduct.setLayoutParams(leftProductListlp);
                }

                LinearLayout.LayoutParams rightProductListlp = (LinearLayout.LayoutParams) viewHolder.llRightProduct.getLayoutParams();
                if (rightProductListlp != null) {
                    viewHolder.llRightProduct.setPadding(0, 0, 0, dividerWidth);
                    if (mMerchantStoreFrontActivity.isDoubleCol) {
                        rightProductListlp.setMargins(0, 0, marginRight, 0);
                    } else {
                        rightProductListlp.setMargins(marginLeft, 0, marginRight, 0);
                    }
                    viewHolder.llRightProduct.setLayoutParams(rightProductListlp);
                }

                RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) viewHolder.ivLeftProductImage.getLayoutParams();
                if (leftImagelp != null) {
                    if (mMerchantStoreFrontActivity.isDoubleCol) {
                        leftImagelp.width = destWidth;
                        leftImagelp.height = destHeight;
                    }
                    viewHolder.ivLeftProductImage.setLayoutParams(leftImagelp);
                }

                final String leftProductImageUrl = leftProductEntity.getSmallImage();
                // load left image
                if (viewHolder.ivLeftProductImage.getTag() != null) {
                    if (!viewHolder.ivLeftProductImage.getTag().toString().equals(leftProductImageUrl)) {
                        JImageUtils.downloadImageFromServerByUrl(mMerchantStoreFrontActivity, mImageloader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
                        viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
                    }
                } else {
                    JImageUtils.downloadImageFromServerByUrl(mMerchantStoreFrontActivity, mImageloader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
                    viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
                }

                String leftProductName = leftProductEntity.getName();
                viewHolder.ctvLeftProductName.setText(leftProductName);

                ///////////////////////russell////////////////////////
                int leftInstock = leftProductEntity.getInStock();
                JLogUtils.i("russell->leftInstock", leftInstock + "");
                if (1 == leftInstock) {
                    viewHolder.rlLeftOutOfStock.setVisibility(View.GONE);
                } else {
                    viewHolder.rlLeftOutOfStock.setVisibility(View.VISIBLE);
                }
                ///////////////////////russell////////////////////////

                String leftProductBrand = leftProductEntity.getBrand();
                if (!JDataUtils.isEmpty(leftProductBrand)) {
                    leftProductBrand = leftProductBrand.toUpperCase();
                }
                viewHolder.ctvLeftProductBrand.setText(leftProductBrand);
                viewHolder.ctvLeftProductBrand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        startBrandStoreActivity((Activity) view.getContext(), leftProductEntity.getBrand(), leftProductEntity.getBrandId());
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
                String leftProductFinalPrice = leftProductEntity.getFinalPrice();
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
//
                final ViewHolder finalViewHolder = viewHolder;
                final SVRAppserviceProductItemReturnEntity finalLeftProductEntity = leftProductEntity;
                viewHolder.llLeftProduct.setOnClickListener(new View.OnClickListener() {
                    private MerchantStoreFrontActivity merchantActivity;
                    private String productId;

                    public View.OnClickListener init(MerchantStoreFrontActivity a, String i) {
                        this.merchantActivity = a;
                        this.productId = i;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(merchantActivity, ProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", this.productId);
                        if (finalViewHolder.ivLeftProductImage.getDrawable() == null) {
                            setBundleAndToPDP(intent, bundle);
                        } else {
                            bundle.putString("from", "from_product_list");
                            bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(finalLeftProductEntity));
                            bundle.putString("imageurl", leftProductImageUrl);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                intent.putExtras(bundle);
                                ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(merchantActivity, finalViewHolder.ivLeftProductImage, merchantActivity.getString(R.string.activity_image_trans));
                                ActivityCompat.startActivityForResult(merchantActivity, intent, merchantActivity.RESULT_ADD_WISH, aop.toBundle());
//                        ActivityCompat.startActivity(merchantActivity, intent, aop.toBundle());
                            } else {
                                setBundleAndToPDP(intent, bundle);
                            }
                        }
                    }
                }.init(mMerchantStoreFrontActivity, leftProductEntity.getProductId()));


                //Right
                position = position + 1;
                final int tempRightPosition = position;
                if (position < 0 || productListArrayListSize <= position) {
                    viewHolder.llRightProduct.setVisibility(View.GONE);
                    return convertView;
                }
                SVRAppserviceProductItemReturnEntity rightProductEntity = null;
                rightProductEntity = mProductItemEntityArrayList.get(position);
                if (rightProductEntity == null || JDataUtils.isEmpty(rightProductEntity.getProductId())) {
                    viewHolder.llRightProduct.setVisibility(View.INVISIBLE);
                    return convertView;
                }
                viewHolder.llRightProduct.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams rightImagelp = (RelativeLayout.LayoutParams) viewHolder.ivRightProductImage.getLayoutParams();
                if (rightImagelp != null) {
                    if (mMerchantStoreFrontActivity.isDoubleCol) {
                        rightImagelp.width = destWidth;
                        rightImagelp.height = destHeight;
                    }
                    viewHolder.ivRightProductImage.setLayoutParams(rightImagelp);
                }
                // load right image
                final String rightProductImageUrl = rightProductEntity.getSmallImage();


                if (viewHolder.ivRightProductImage.getTag() != null) {
                    if (!viewHolder.ivRightProductImage.getTag().toString().equals(rightProductImageUrl)) {

                        JImageUtils.downloadImageFromServerByUrl(mMerchantStoreFrontActivity, mImageloader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);
                        viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
                    }
                } else {
                    JImageUtils.downloadImageFromServerByUrl(mMerchantStoreFrontActivity, mImageloader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);

                    viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
                }
                String rightProductName = rightProductEntity.getName();
                viewHolder.ctvRightProductName.setText(rightProductName);

                ///////////////////////russell////////////////////////
                int rightInstock = rightProductEntity.getInStock();
                JLogUtils.i("russell->rightInstock", rightInstock + "");
                if (1 == rightInstock) {
                    viewHolder.rlRightOutOfStock.setVisibility(View.GONE);
                } else {
                    viewHolder.rlRightOutOfStock.setVisibility(View.VISIBLE);
                }
                ///////////////////////russell////////////////////////

                String rightProductBrand = rightProductEntity.getBrand();
                if (!JDataUtils.isEmpty(rightProductBrand)) {
                    rightProductBrand = rightProductBrand.toUpperCase();
                }
                viewHolder.ctvRightProductBrand.setText(rightProductBrand);
                final SVRAppserviceProductItemReturnEntity finalRightProductEntity1 = rightProductEntity;
                viewHolder.ctvRightProductBrand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        startBrandStoreActivity((Activity) view.getContext(), finalRightProductEntity1.getBrand(), finalRightProductEntity1.getBrandId());
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
                String rightProductFinalPrice = rightProductEntity.getFinalPrice();
                try {
                    rightProductFinalPriceFloat = Float.parseFloat(rightProductFinalPrice);
                } catch (Exception ex) {
                    JLogUtils.e(TAG, "getView", ex);
                }

                if (JDataUtils.compare(rightProductFinalPriceFloat, rightProductPriceFloat) < 0) {
                    viewHolder.ctvRightProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);
                    viewHolder.ctvRightProductPrice.setVisibility(View.VISIBLE);
                    viewHolder.ctvRightProductPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductPriceFloat + ""));
                    viewHolder.ctvRightProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                } else {
                    viewHolder.ctvRightProductPrice.setVisibility(View.GONE);
                }
                viewHolder.ctvRightProductFinalPrice.setText(GemfiveApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(rightProductFinalPriceFloat + ""));

                final SVRAppserviceProductItemReturnEntity finalRightProductEntity = rightProductEntity;
                viewHolder.llRightProduct.setOnClickListener(new View.OnClickListener() {
                    private MerchantStoreFrontActivity merchantActivity;
                    private String productId;

                    public View.OnClickListener init(MerchantStoreFrontActivity a, String i) {
                        this.merchantActivity = a;
                        this.productId = i;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(merchantActivity, ProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", this.productId);
                        if (finalViewHolder.ivRightProductImage.getDrawable() == null) {
                            setBundleAndToPDP(intent, bundle);
                        } else {
                            bundle.putString("from", "from_product_list");
                            bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(finalRightProductEntity));
                            bundle.putString("imageurl", rightProductImageUrl);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                intent.putExtras(bundle);
                                ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(merchantActivity, finalViewHolder.ivRightProductImage, merchantActivity.getString(R.string.activity_image_trans));
                                ActivityCompat.startActivityForResult(merchantActivity, intent, MerchantStoreFrontActivity.RESULT_ADD_WISH, aop.toBundle());
//                        ActivityCompat.startActivity(merchantActivity, intent, aop.toBundle());
                            } else {
                                setBundleAndToPDP(intent, bundle);
                            }
                        }
                    }
                }.init(mMerchantStoreFrontActivity, rightProductEntity.getProductId()));
            }
                break;
        }

        return convertView;
    }

    private ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceProductItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setFinalPrice(e.getFinalPrice());
        entity.setInStock(e.getInStock());
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        return entity;
    }

    private void setBundleAndToPDP(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
        this.mMerchantStoreFrontActivity.startActivityForResult(intent, MerchantStoreFrontActivity.RESULT_ADD_WISH);
        this.mMerchantStoreFrontActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    public class ViewHolder {
        public LinearLayout llProductListTitle, llProductList, llLeftProduct, llRightProduct;
        public ImageView ivCategory, ivLeftProductImage, ivRightProductImage;
        public CustomTextView ctvLeftProductName, ctvRightProductName, ctvLeftProductBrand, ctvRightProductBrand,
                ctvLeftProductPrice, ctvRightProductPrice, ctvLeftProductFinalPrice, ctvRightProductFinalPrice, ctvLeftCurationProductMerchant, ctvRightCurationProductMerchant;
        public View vProductListDivider, vHorDivider;
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock;
    }
    public class ViewHolderBar {
        ImageView mHeaderViewToggle;
        LinearLayout mHeaderFilter;
        LinearLayout mHeaderSort;

        public ViewHolderBar(View view) {
            mHeaderViewToggle = (ImageView) view.findViewById(R.id.iv_view_toggle_top);
            mHeaderFilter = (LinearLayout) view.findViewById(R.id.ll_filter_top);
            mHeaderSort = (LinearLayout) view.findViewById(R.id.ll_sort_top);
        }

    }

    public boolean addProductToWishWhenLoginSuccess(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        if (mMerchantStoreFrontActivity.mOperateProductIdPrecache != null && !TextUtils.isEmpty(productId)) {
            if (productId.equals(mMerchantStoreFrontActivity.mOperateProductIdPrecache.getProductId())) {
                if (mMerchantStoreFrontActivity.mOperateProductIdPrecache.isAvailable()) {
                    mMerchantStoreFrontActivity.mOperateProductIdPrecache = null;
                    return true;
                } else {
                    mMerchantStoreFrontActivity.mOperateProductIdPrecache = null;
                }
            }
        }
        return false;
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
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
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
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }

    private void setWishIconColorToPurple(ImageView ivWishIcon, final ImageView ivWishIcon2) {
        ivWishIcon2.setVisibility(View.VISIBLE);
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);

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
        mProductItemEntityArrayList.get(tempPosition).setIsLike(0);
        if (!TextUtils.isEmpty(mProductItemEntityArrayList.get(tempPosition).getItemId())) {
            myAccountDao.deleteWishListById(GemfiveApplication.getAppConfiguration().getUserInfo(mMerchantStoreFrontActivity).getSessionKey(), itemId, tempPosition);
        }
    }

    private void addtoWishlistsendRequest(SVRAppserviceProductItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
        if (GemfiveApplication.getAppConfiguration().isSignIn(mMerchantStoreFrontActivity)) {
            entity.setIsLike(1);
            mProductDao.addProductListToWish(entity.getProductId(), GemfiveApplication.getAppConfiguration().getUserInfo(mMerchantStoreFrontActivity).getSessionKey(), tempPosition);
            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
        } else {
            mMerchantStoreFrontActivity.saveProductIdWhenJumpLoginPage(entity.getProductId());
            Intent intent = new Intent();
            intent.setClass(mMerchantStoreFrontActivity, LoginRegisterActivity.class);
            mMerchantStoreFrontActivity.startActivityForResult(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
            mMerchantStoreFrontActivity.overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<MerchantProductListAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, MerchantProductListAdapter merchantProductListAdapter) {
            mAdapter = new WeakReference<MerchantProductListAdapter>(merchantProductListAdapter);
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
                        GemfiveApplication.getAppConfiguration().updateWishlist(mAdapter.get().mMerchantStoreFrontActivity, wishDelEntityResult.getWishListItemCount());
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        int position = Integer.parseInt(String.valueOf(addToWishlistEntity.getParams()));
                        SVRAppserviceProductItemReturnEntity productEntity = (SVRAppserviceProductItemReturnEntity) mAdapter.get().mProductItemEntityArrayList.get(position);
                        productEntity.setItemId(addToWishlistEntity.getItemId());
                        //update wishlist number
                        GemfiveApplication.getAppConfiguration().updateWishlist(mAdapter.get().mMerchantStoreFrontActivity, addToWishlistEntity.getWishListItemCount());
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                            int position = Integer.parseInt(String.valueOf(bean.getParams()));
                            if (!TextUtils.isEmpty(bean.getErrorMessage())) {
                                Toast.makeText(mAdapter.get().mMerchantStoreFrontActivity, bean.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case MyAccountDao.ERROR:
                case ProductDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mAdapter.get().mMerchantStoreFrontActivity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }

    private void startBrandStoreActivity(Activity activity, String brandName, String brandId) {
        if (!"0".equals(brandId)) {
            Intent brandStoreIntent = new Intent(activity, BrandStoreFontActivity.class);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_ID, brandId);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_NAME, brandName);
            activity.startActivity(brandStoreIntent);
            activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        } else {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
            activity.finish();
        }
    }

}

package com.whitelabel.app.adapter;

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
import com.whitelabel.app.activity.CurationActivity;
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
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailProductListItemReturnEntity;
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
 * Created by imaginato on 2015/7/22.
 */
public class CurationProductListAdapter extends BaseAdapter {
    public static final int TYPE_BAR = 0;
    public static final int TYPE_CONTENT = 1;
    public final int REQUEST_CURATION = 2000;
    private final String TAG = "CurationProductListAdapter";
    private CurationActivity curationActivity;
    private ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productItemEntityArrayList;
    private MyAccountDao myAccountDao;
    private ProductDao mProductDao;
    private final ImageLoader mImageLoader;
    private boolean mHideSwitchAndFilterBar=true;

    public CurationProductListAdapter(CurationActivity curationActivity, ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productItemEntityArrayList, ImageLoader imageLoader) {
        this.curationActivity = curationActivity;
        this.productItemEntityArrayList = productItemEntityArrayList;
        mImageLoader = imageLoader;
        DataHandler dataHandler = new DataHandler(curationActivity, this);
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (productItemEntityArrayList != null) {
            count = productItemEntityArrayList.size() + 1;
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

        SVRAppserviceLandingPagesDetailProductListItemReturnEntity object = null;
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

    private void setCurationImageView(ImageView view, String categoryImageUrl, int destWidth, int destHeight) {
        JImageUtils.downloadImageFromServerByUrl(curationActivity, mImageLoader, view, categoryImageUrl, destWidth, destHeight);
        if (view != null && view.getLayoutParams() != null) {
            view.getLayoutParams().width = destWidth;
            view.getLayoutParams().height = destHeight;
        }
        view.setTag(categoryImageUrl);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_BAR:
                final ViewHolderBar viewHolderBar;
                if (convertView == null) {
                    convertView = LayoutInflater.from(curationActivity).inflate(R.layout.layout_top_switch_and_filter_bar, null);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, JDataUtils.dp2Px(40));
                    convertView.setLayoutParams(params);
                    viewHolderBar = new ViewHolderBar(convertView);
                    convertView.setTag(viewHolderBar);
                } else {
                    viewHolderBar = (ViewHolderBar) convertView.getTag();
                }
                if (mHideSwitchAndFilterBar) {
                    convertView.setVisibility(View.GONE);
                } else {
                    convertView.setVisibility(View.VISIBLE);
                }

                if (curationActivity.isDoubleCol) {
                    viewHolderBar.mHeaderViewToggle.setImageResource(R.mipmap.ic_view_single);
                } else {
                    viewHolderBar.mHeaderViewToggle.setImageResource(R.mipmap.ic_view_double);
                }
                viewHolderBar.mHeaderViewToggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curationActivity.toggleViewOption(viewHolderBar.mHeaderViewToggle);
                    }
                });
                viewHolderBar.mHeaderFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curationActivity.filterSortOption(curationActivity.TYPE_FILTER);
                    }
                });
                viewHolderBar.mHeaderSort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curationActivity.filterSortOption(curationActivity.TYPE_SORT);
                    }
                });

                break;
            case TYPE_CONTENT:
                final ViewHolder viewHolder;
                if (convertView != null && (boolean) convertView.getTag(R.id.llProductList) == curationActivity.isDoubleCol) {
                    viewHolder = (ViewHolder) convertView.getTag();
                } else {
                    if (curationActivity.isDoubleCol) {
                        convertView = LayoutInflater.from(curationActivity).inflate(R.layout.adapter_curation_productlist_rowitem, null);
                    } else {
                        convertView = LayoutInflater.from(curationActivity).inflate(R.layout.adapter_curation_productlist_ver_rowitem, null);
                    }
                    viewHolder = new ViewHolder();
                    viewHolder.llProductList = (LinearLayout) convertView.findViewById(R.id.llProductList);
                    viewHolder.llLeftProduct = (LinearLayout) convertView.findViewById(R.id.llLeftProduct);
                    viewHolder.llRightProduct = (LinearLayout) convertView.findViewById(R.id.llRightProduct);
                    viewHolder.vProductListDivider = convertView.findViewById(R.id.vProductListDivider);
                    viewHolder.vHorDivider = convertView.findViewById(R.id.vHorDivider);

                    viewHolder.ivLeftProductImage = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.ivProductImage);
                    viewHolder.ctvLeftProductName = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductName);
                    viewHolder.ctvLeftProductBrand = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductBrand);
                    viewHolder.ctvLeftProductPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductPrice);
                    viewHolder.ctvLeftCurationProductMerchant = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctv_curation_product_merchant);
                    viewHolder.ctvLeftProductFinalPrice = (CustomTextView) viewHolder.llLeftProduct.findViewById(R.id.ctvProductFinalPrice);
                    viewHolder.rlLeftOutOfStock = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_product_list_out_of_stock);
                    viewHolder.rlLeftCurationWish = (RelativeLayout) viewHolder.llLeftProduct.findViewById(R.id.rl_product_wish);
                    viewHolder.ivLeftCurationWishIcon = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.iv_product_wish_icon);
                    viewHolder.ivLeftCurationWishIcon2 = (ImageView) viewHolder.llLeftProduct.findViewById(R.id.iv_product_wish_icon2);

                    //Right
                    viewHolder.ivRightProductImage = (ImageView) viewHolder.llRightProduct.findViewById(R.id.ivProductImage);
                    viewHolder.ctvRightProductName = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductName);
                    viewHolder.ctvRightProductBrand = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductBrand);
                    viewHolder.ctvRightProductPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductPrice);
                    viewHolder.ctvRightProductFinalPrice = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctvProductFinalPrice);
                    viewHolder.rlRightOutOfStock = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
                    viewHolder.rlRightCurationWish = (RelativeLayout) viewHolder.llRightProduct.findViewById(R.id.rl_product_wish);
                    viewHolder.ctvRightCurationProductMerchant = (CustomTextView) viewHolder.llRightProduct.findViewById(R.id.ctv_curation_product_merchant);
                    viewHolder.ivRightCurationWishIcon = (ImageView) viewHolder.llRightProduct.findViewById(R.id.iv_product_wish_icon);
                    viewHolder.ivRightCurationWishIcon2 = (ImageView) viewHolder.llRightProduct.findViewById(R.id.iv_product_wish_icon2);
                    convertView.setTag(viewHolder);
                    convertView.setTag(R.id.llProductList, curationActivity.isDoubleCol);
                }

                //viewHolder.llProductListTitle.setVisibility(View.GONE);
                viewHolder.llProductList.setVisibility(View.VISIBLE);

                if (productItemEntityArrayList == null) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                position = (position - 1) * 2;
                final int tempPosition = position;
                final int productListArrayListSize = productItemEntityArrayList.size();
                if (position < 0 || productListArrayListSize <= position) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                final SVRAppserviceLandingPagesDetailProductListItemReturnEntity leftProductEntity;
                leftProductEntity = productItemEntityArrayList.get(position);
                if (leftProductEntity == null || JDataUtils.isEmpty(leftProductEntity.getProductId())) {
                    viewHolder.llProductList.setVisibility(View.GONE);
                    return convertView;
                }
                // set merchant name
                if (!TextUtils.isEmpty(leftProductEntity.getVendorDisplayName())) {
                    String soldBy = viewHolder.ctvLeftCurationProductMerchant.getContext().getResources().getString(R.string.soldby);
                    if (!TextUtils.isEmpty(leftProductEntity.getVendor_id())) {
                        viewHolder.ctvLeftCurationProductMerchant.setTextColor(curationActivity.getResources().getColor(R.color.purple92018d));
                        SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + leftProductEntity.getVendorDisplayName());
                        ss.setSpan(new ForegroundColorSpan(curationActivity.getResources().getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.ctvLeftCurationProductMerchant.setText(ss);
                        if (!"0".equals(leftProductEntity.getVendor_id())) {
                            final SVRAppserviceLandingPagesDetailProductListItemReturnEntity finalLeftProductEntity = leftProductEntity;
                            viewHolder.ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(curationActivity, MerchantStoreFrontActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalLeftProductEntity.getVendor_id());
                                    bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalLeftProductEntity.getVendorDisplayName());
                                    intent.putExtras(bundle);
                                    curationActivity.startActivity(intent);
                                    curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                                }
                            });
                        } else {
                            viewHolder.ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(curationActivity, HomeActivity.class);
                                    curationActivity.startActivity(i);
                                    curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                                }
                            });
                        }

                    } else {
                        viewHolder.ctvLeftCurationProductMerchant.setText(soldBy + " " + leftProductEntity.getVendorDisplayName());
                        viewHolder.ctvLeftCurationProductMerchant.setTextColor(curationActivity.getResources().getColor(R.color.greyB8B8B8));
                    }
                } else {
                    viewHolder.ctvLeftCurationProductMerchant.setText("");
                }
                convertView.setVisibility(View.VISIBLE);

                final int phoneWidth = GemfiveApplication.getPhoneConfiguration().getScreenWidth();
                final int marginLeft = phoneWidth * 15 / 640;
                final int marginRight = marginLeft;
                final int dividerWidth = phoneWidth * 16 / 640;
                final int destWidth = (phoneWidth - (marginLeft + marginRight) - dividerWidth) / 2;
                final int destHeight = destWidth;

                LinearLayout.LayoutParams dividerlp = (LinearLayout.LayoutParams) viewHolder.vProductListDivider.getLayoutParams();
                if (dividerlp != null && curationActivity.isDoubleCol) {
                    dividerlp.width = dividerWidth;
                    dividerlp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    viewHolder.vProductListDivider.setLayoutParams(dividerlp);
                }
                if (!curationActivity.isDoubleCol) {
                    if (position == productItemEntityArrayList.size() - 1) {
                        viewHolder.vHorDivider.setVisibility(View.GONE);
                    } else {
                        viewHolder.vHorDivider.setVisibility(View.VISIBLE);
                    }
                }

                LinearLayout.LayoutParams leftProductListlp = (LinearLayout.LayoutParams) viewHolder.llLeftProduct.getLayoutParams();
                if (leftProductListlp != null) {
                    viewHolder.llLeftProduct.setPadding(0, 0, 0, dividerWidth);
                    if (curationActivity.isDoubleCol) {
                        leftProductListlp.setMargins(marginLeft, 0, 0, 0);
                    } else {
                        leftProductListlp.setMargins(marginLeft, 0, marginRight, 0);
                    }
                    viewHolder.llLeftProduct.setLayoutParams(leftProductListlp);
                }

                LinearLayout.LayoutParams rightProductListlp = (LinearLayout.LayoutParams) viewHolder.llRightProduct.getLayoutParams();
                if (rightProductListlp != null) {
                    viewHolder.llRightProduct.setPadding(0, 0, 0, dividerWidth);
                    if (curationActivity.isDoubleCol) {
                        rightProductListlp.setMargins(0, 0, marginRight, 0);
                    } else {
                        rightProductListlp.setMargins(marginLeft, 0, marginRight, 0);
                    }
                    viewHolder.llRightProduct.setLayoutParams(rightProductListlp);
                }

                RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) viewHolder.ivLeftProductImage.getLayoutParams();
                if (leftImagelp != null) {
                    if (curationActivity.isDoubleCol) {
                        leftImagelp.width = destWidth;
                        leftImagelp.height = destHeight;
                    }
                    viewHolder.ivLeftProductImage.setLayoutParams(leftImagelp);
                }

                final String leftProductImageUrl = leftProductEntity.getSmallImage();
                // load left image
                if (viewHolder.ivLeftProductImage.getTag() != null) {
                    if (!viewHolder.ivLeftProductImage.getTag().toString().equals(leftProductImageUrl)) {
                        JImageUtils.downloadImageFromServerByUrl(curationActivity, mImageLoader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
                        viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
                    }
                } else {
                    JImageUtils.downloadImageFromServerByUrl(curationActivity, mImageLoader, viewHolder.ivLeftProductImage, leftProductImageUrl, destWidth, destHeight);
                    viewHolder.ivLeftProductImage.setTag(leftProductImageUrl);
                }

                final String leftProductName = leftProductEntity.getName();
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
                        startBrandStoreActivity(leftProductEntity.getBrand(), leftProductEntity.getBrandId());
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
                viewHolder.llLeftProduct.setOnClickListener(new View.OnClickListener() {
                    private CurationActivity curationActivity;
                    private String productId;

                    public View.OnClickListener init(CurationActivity a, String i) {
                        this.curationActivity = a;
                        this.productId = i;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(this.curationActivity, ProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", this.productId);
                        if (finalViewHolder.ivLeftProductImage.getDrawable() == null) {
                            setBundleAndToPDP(intent, bundle);
                        } else {
                            bundle.putString("from", "from_product_list");
                            bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(leftProductEntity));
                            bundle.putString("imageurl", leftProductImageUrl);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && finalViewHolder.ivLeftProductImage.getTag().toString().equals(leftProductImageUrl)) {
                                intent.putExtras(bundle);
                                ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(curationActivity, finalViewHolder.ivLeftProductImage, curationActivity.getResources().getString(R.string.activity_image_trans));
                                ActivityCompat.startActivityForResult(curationActivity, intent, curationActivity.RESULT_WISH, aop.toBundle());
                            } else {
                                setBundleAndToPDP(intent, bundle);
                            }
                        }
                    }
                }.init(curationActivity, leftProductEntity.getProductId()));

                //wish icon
                //初始化 wish icon的状态
                if (leftProductEntity.getIs_like() == 1) {
                    setWishIconColorToPurpleNoAnim(viewHolder.ivLeftCurationWishIcon);
                } else {
                    setWishIconColorToBlankNoAnim(viewHolder.ivLeftCurationWishIcon);
                }
//        wishIcon 正在加载中，是否可以点击？
                viewHolder.rlLeftCurationWish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果 islike，且有itemId，就执行删除 wish item
                        if (leftProductEntity.getIs_like() == 1) {
                            sendRequestToDeteleteCell(viewHolder.ivLeftCurationWishIcon, viewHolder.rlLeftCurationWish, leftProductEntity.getItem_id(), tempPosition);
                        } else {
                            addtoWishlistsendRequest(leftProductEntity, viewHolder.rlLeftCurationWish, viewHolder.ivLeftCurationWishIcon, viewHolder.ivLeftCurationWishIcon2, tempPosition);
                        }
                    }
                });
                if (addProductToWishWhenLoginSuccess(leftProductEntity.getProductId())) {
                    addtoWishlistsendRequest(leftProductEntity, viewHolder.rlLeftCurationWish, viewHolder.ivLeftCurationWishIcon, viewHolder.ivLeftCurationWishIcon2, tempPosition);
                }
                /////Right ///////////
                position = position + 1;
                final int tempRightPosition = position;
                if (position < 0 || productListArrayListSize <= position) {
                    viewHolder.llRightProduct.setVisibility(View.GONE);
                    return convertView;
                }

                final SVRAppserviceLandingPagesDetailProductListItemReturnEntity rightProductEntity = productItemEntityArrayList.get(position);
                if (rightProductEntity == null || JDataUtils.isEmpty(rightProductEntity.getProductId())) {
                    viewHolder.llRightProduct.setVisibility(View.INVISIBLE);
                    return convertView;
                }
                viewHolder.llRightProduct.setVisibility(View.VISIBLE);
                // set merchant name
                if (!TextUtils.isEmpty(rightProductEntity.getVendorDisplayName())) {
                    String soldBy = viewHolder.ctvRightCurationProductMerchant.getContext().getResources().getString(R.string.soldby);
                    if (!TextUtils.isEmpty(rightProductEntity.getVendor_id())) {
                        viewHolder.ctvRightCurationProductMerchant.setTextColor(curationActivity.getResources().getColor(R.color.purple92018d));
                        SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + rightProductEntity.getVendorDisplayName());
                        ss.setSpan(new ForegroundColorSpan(curationActivity.getResources().getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.ctvRightCurationProductMerchant.setText(ss);
                        if (!"0".equals(rightProductEntity.getVendor_id())) {
                            final SVRAppserviceLandingPagesDetailProductListItemReturnEntity finalRightProductEntity = rightProductEntity;
                            viewHolder.ctvRightCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(curationActivity, MerchantStoreFrontActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, finalRightProductEntity.getVendor_id());
                                    bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, finalRightProductEntity.getVendorDisplayName());
                                    intent.putExtras(bundle);
                                    curationActivity.startActivity(intent);
                                    curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                                }
                            });
                        } else {
                            viewHolder.ctvRightCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(curationActivity, HomeActivity.class);
                                    curationActivity.startActivity(i);
                                    curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                                }
                            });
                        }

                    } else {
                        viewHolder.ctvRightCurationProductMerchant.setText(soldBy + " " + rightProductEntity.getVendorDisplayName());
                        viewHolder.ctvRightCurationProductMerchant.setTextColor(curationActivity.getResources().getColor(R.color.greyB8B8B8));
                    }

                } else {
                    viewHolder.ctvRightCurationProductMerchant.setText("");
                }
                viewHolder.llRightProduct.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams rightImagelp = (RelativeLayout.LayoutParams) viewHolder.ivRightProductImage.getLayoutParams();
                if (rightImagelp != null) {
                    if (curationActivity.isDoubleCol) {
                        rightImagelp.width = destWidth;
                        rightImagelp.height = destHeight;
                    }
                    viewHolder.ivRightProductImage.setLayoutParams(rightImagelp);
                }
                // load right image
                final String rightProductImageUrl = rightProductEntity.getSmallImage();
                if (viewHolder.ivRightProductImage.getTag() != null) {
                    if (!viewHolder.ivRightProductImage.getTag().toString().equals(rightProductImageUrl)) {
                        JImageUtils.downloadImageFromServerByUrl(curationActivity, mImageLoader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);
                        viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
                    }
                } else {
                    JImageUtils.downloadImageFromServerByUrl(curationActivity, mImageLoader, viewHolder.ivRightProductImage, rightProductImageUrl, destWidth, destHeight);

                    viewHolder.ivRightProductImage.setTag(rightProductImageUrl);
                }
                final String rightProductName = rightProductEntity.getName();
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
                viewHolder.ctvRightProductBrand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startBrandStoreActivity(rightProductEntity.getBrand(), rightProductEntity.getBrandId());
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

                viewHolder.llRightProduct.setOnClickListener(new View.OnClickListener() {
                    private CurationActivity curationActivity;
                    private String productId;

                    public View.OnClickListener init(CurationActivity a, String i) {
                        this.curationActivity = a;
                        this.productId = i;
                        return this;
                    }

                    @Override
                    public void onClick(View view) {
                        //PDP 页面  如果是商品的wish状态发生改变，需要返回时刷新此商品的wish状态
                        Intent intent = new Intent();
                        intent.setClass(this.curationActivity, ProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", this.productId);
                        if (finalViewHolder.ivRightProductImage.getDrawable() == null) {
                            setBundleAndToPDP(intent, bundle);
                        } else {
                            bundle.putString("from", "from_product_list");
                            bundle.putSerializable("product_info", getProductListItemToProductDetailsEntity(rightProductEntity));
                            bundle.putString("imageurl", rightProductImageUrl);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && finalViewHolder.ivRightProductImage.getTag().toString().equals(rightProductImageUrl)) {
                                intent.putExtras(bundle);
                                ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(curationActivity, finalViewHolder.ivRightProductImage, curationActivity.getResources()
                                        .getString(R.string.activity_image_trans));
                                ActivityCompat.startActivityForResult(curationActivity, intent, curationActivity.RESULT_WISH, aop.toBundle());
                            } else {
                                setBundleAndToPDP(intent, bundle);
                            }
                        }
                    }
                }.init(curationActivity, rightProductEntity.getProductId()));

                //wish icon
                if (rightProductEntity.getIs_like() == 1) {
                    setWishIconColorToPurpleNoAnim(viewHolder.ivRightCurationWishIcon);
                } else {
                    setWishIconColorToBlankNoAnim(viewHolder.ivRightCurationWishIcon);
                }
                viewHolder.rlRightCurationWish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rightProductEntity.getIs_like() == 1) {
                            sendRequestToDeteleteCell(viewHolder.ivRightCurationWishIcon, viewHolder.rlRightCurationWish, rightProductEntity.getItem_id(), tempRightPosition);
                        } else {
                            addtoWishlistsendRequest(rightProductEntity, viewHolder.rlRightCurationWish, viewHolder.ivRightCurationWishIcon, viewHolder.ivRightCurationWishIcon2, tempRightPosition);
                        }
                    }
                });
                // login 成功后会刷新，判断 是否包含此id，如果operateProductIdPrecache包含此id,执行add to wish请求，
                if (addProductToWishWhenLoginSuccess(rightProductEntity.getProductId())) {
                    addtoWishlistsendRequest(rightProductEntity, viewHolder.rlRightCurationWish, viewHolder.ivRightCurationWishIcon, viewHolder.ivRightCurationWishIcon2, tempRightPosition);
                }
                break;
        }


        return convertView;

    }

    private ProductListItemToProductDetailsEntity getProductListItemToProductDetailsEntity(SVRAppserviceLandingPagesDetailProductListItemReturnEntity e) {
        ProductListItemToProductDetailsEntity entity = new ProductListItemToProductDetailsEntity();
        entity.setBrand(e.getBrand());
        entity.setCategory(e.getCategory());
        entity.setFinalPrice(e.getFinalPrice());
        entity.setInStock(e.getInStock());
        entity.setName(e.getName());
        entity.setPrice(e.getPrice());
        entity.setVendorDisplayName(e.getVendorDisplayName());
        return entity;
    }

    private void setBundleAndToPDP(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
        curationActivity.startActivityForResult(intent, curationActivity.RESULT_WISH);
        curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    public boolean addProductToWishWhenLoginSuccess(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        if (curationActivity.operateProductIdPrecache != null && !TextUtils.isEmpty(productId)) {
            if (productId.equals(curationActivity.operateProductIdPrecache.getProductId())) {
                if (curationActivity.operateProductIdPrecache.isAvailable()) {
                    curationActivity.operateProductIdPrecache = null;
                    return true;
                } else {
                    curationActivity.operateProductIdPrecache = null;
                }
            }
        }
        return false;
    }

    public void hideSwitchAndFilterBar(boolean b) {
        mHideSwitchAndFilterBar = b;
    }

    public class ViewHolder {
        public LinearLayout llProductListTitle, llProductList, llLeftProduct, llRightProduct;

        public ImageView ivCategory, ivLeftProductImage, ivRightProductImage, ivLeftCurationWishIcon, ivRightCurationWishIcon, ivLeftCurationWishIcon2, ivRightCurationWishIcon2;

        public CustomTextView ctvLeftProductName, ctvRightProductName, ctvLeftProductBrand, ctvRightProductBrand,
                ctvLeftProductPrice, ctvRightProductPrice, ctvLeftProductFinalPrice, ctvRightProductFinalPrice, ctvLeftCurationProductMerchant, ctvRightCurationProductMerchant;
        public View vProductListDivider, vHorDivider;
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock, rlLeftCurationWish, rlRightCurationWish;
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
        productItemEntityArrayList.get(tempPosition).setIs_like(0);
        if (!TextUtils.isEmpty(productItemEntityArrayList.get(tempPosition).getItem_id())) {
            myAccountDao.deleteWishListById(GemfiveApplication.getAppConfiguration().getUserInfo(curationActivity).getSessionKey(), itemId, tempPosition);
        }
    }

    private void addtoWishlistsendRequest(SVRAppserviceLandingPagesDetailProductListItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
        if (GemfiveApplication.getAppConfiguration().isSignIn(curationActivity)) {
            entity.setIs_like(1);
            mProductDao.addProductListToWish(entity.getProductId(), GemfiveApplication.getAppConfiguration().getUserInfo(curationActivity).getSessionKey(), tempPosition);
            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
        } else {
            curationActivity.saveProductIdWhenJumpLoginPage(entity.getProductId());
            Intent intent = new Intent();
            intent.setClass(curationActivity, LoginRegisterActivity.class);
            curationActivity.startActivityForResult(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
            curationActivity.overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<CurationProductListAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, CurationProductListAdapter curationProductListAdapter) {
            mAdapter = new WeakReference<CurationProductListAdapter>(curationProductListAdapter);
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
                        GemfiveApplication.getAppConfiguration().updateWishlist(mAdapter.get().curationActivity, wishDelEntityResult.getWishListItemCount());
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        int position = Integer.parseInt(String.valueOf(addToWishlistEntity.getParams()));
                        SVRAppserviceLandingPagesDetailProductListItemReturnEntity productEntity = (SVRAppserviceLandingPagesDetailProductListItemReturnEntity) mAdapter.get().productItemEntityArrayList.get(position);
                        productEntity.setItem_id(addToWishlistEntity.getItemId());
                        //update wishlist number
                        GemfiveApplication.getAppConfiguration().updateWishlist(mAdapter.get().curationActivity, addToWishlistEntity.getWishListItemCount());
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
                        try {
                            ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                            int position = Integer.parseInt(String.valueOf(bean.getParams()));
                            if (!TextUtils.isEmpty(bean.getErrorMessage())) {
                                Toast.makeText(mAdapter.get().curationActivity, bean.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case MyAccountDao.ERROR:
                case ProductDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mAdapter.get().curationActivity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }


    private void startBrandStoreActivity(String brandName, String brandId) {
        if (!"0".equals(brandId)) {
            Intent brandStoreIntent = new Intent(curationActivity, BrandStoreFontActivity.class);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_ID, brandId);
            brandStoreIntent.putExtra(BrandStoreFontActivity.EXTRA_BRAND_NAME, brandName);
            curationActivity.startActivity(brandStoreIntent);
            curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        } else {
            Intent intent = new Intent(curationActivity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            curationActivity.startActivity(intent);
            curationActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
            curationActivity.finish();
        }
    }
}

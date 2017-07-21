package com.whitelabel.app.ui.brandstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.adapter.FlowViewAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.BannerModel;
import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomCountdown;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerViewV2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BrandStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<SVRAppserviceProductSearchResultsItemReturnEntity> mProducts;
    private static final int TYPE_TWOROW_ITEM = 256478;
    private static final int TYPE_SINGLEROW_ITEM = 256123;
    private static final int TYPE_HEADER = 10000;
    private static final int TYPE_FOOTER = 9621147;
    @BindView(R.id.ll_filter)
    LinearLayout mLlFilter;
    @BindView(R.id.ll_sort)
    LinearLayout mLlSort;
    private BrandStoreModel mHeaderBean;
    private final ImageLoader mImageLoader;
    private View.OnClickListener mIconListener;
    private OnFilterSortBarListener mOnFilterSortBarListener;
    private boolean isTwoRow = true;
    public interface OnFilterSortBarListener{
        void onSwitchViewClick(View view);
        void onFilterClick();
        void onSortClick();
    }

    public BrandStoreAdapter(List<SVRAppserviceProductSearchResultsItemReturnEntity> products, ImageLoader loader) {
        mProducts = products;
        mImageLoader = loader;
    }

    public void setTwoRow(boolean isTworRow) {
        this.isTwoRow = isTworRow;
    }

    public void setOnFilterSortBarListener(OnFilterSortBarListener listener) {
        this.mOnFilterSortBarListener = listener;
    }

    public void setHeaderContent(BrandStoreModel headerBean) {
        mHeaderBean = headerBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_product_list_hearder, null);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            RefreshLoadMoreRecyclerViewV2.CustomDragRecyclerFooterView footerView = new RefreshLoadMoreRecyclerViewV2.CustomDragRecyclerFooterView(parent.getContext());
            return new FooterHolder(footerView);
        } else if (viewType == TYPE_SINGLEROW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curation_productlist_ver_item, null);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curation_productlist_item, null);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == mProducts.size() + 1) {
            return TYPE_FOOTER;
        } else if (isTwoRow) {
            return TYPE_TWOROW_ITEM;
        } else {
            return TYPE_SINGLEROW_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (mHeaderBean == null) return;
            if (TextUtils.isEmpty(mHeaderBean.getName())) {
                headerViewHolder.tvTitle.setVisibility(View.GONE);
            } else {
                headerViewHolder.tvTitle.setText(mHeaderBean.getName());
            }
            if (TextUtils.isEmpty(mHeaderBean.getDescription())) {
                headerViewHolder.tvDesc.setVisibility(View.GONE);
            } else {
                headerViewHolder.tvDesc.setText(JToolUtils.fromHtml(mHeaderBean.getDescription()));
            }
            if (mHeaderBean.getBannerImages() == null || mHeaderBean.getBannerImages().size() == 0) {
                headerViewHolder.viewPager.setVisibility(View.GONE);
            } else {
                if (headerViewHolder.viewPager.getTag() == null) {
                    int width = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) headerViewHolder.tvTitle.getContext());
                    int imageHeight = width * 240 / 490;
                    headerViewHolder.viewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
                    headerViewHolder.viewPager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(), mImageLoader, mHeaderBean.getBannerImages())));
                    headerViewHolder.viewPager.addOnPageChangeListener(mPageChangeListener);
                    addTisView(headerViewHolder.llTips, mHeaderBean.getBannerImages().size());
                    if (mImageViewTips.size() == 1) {
                        headerViewHolder.llTips.setVisibility(View.GONE);
                    }
                    headerViewHolder.viewPager.setTag("use");
                }
            }
            if (isTwoRow) {
                headerViewHolder.ivViewToggle.setImageResource(R.mipmap.ic_view_single);
            } else {
                headerViewHolder.ivViewToggle.setImageResource(R.mipmap.ic_view_double);
            }
            if (mOnFilterSortBarListener != null) {
                headerViewHolder.ivViewToggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnFilterSortBarListener.onSwitchViewClick(v);
                    }
                });
                headerViewHolder.mHeaderFilterLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnFilterSortBarListener.onFilterClick();
                    }
                });
                headerViewHolder.mHeaderSortLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnFilterSortBarListener.onSortClick();
                    }
                });
            }
            if (TextUtils.isEmpty(mHeaderBean.getName()) && TextUtils.isEmpty(mHeaderBean.getDescription()) && (mHeaderBean.getBannerImages() == null || mHeaderBean.getBannerImages().size() == 0)) {
                headerViewHolder.rlProductLine.setVisibility(View.GONE);
            }
        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity = mProducts.get(position - 1);
            int destWidth = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 100);
            int destHeight = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 120);
            if (isTwoRow) {
                int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) itemViewHolder.itemView.getContext());
                int marginLeft = phoneWidth * 15 / 640;
                int dividerWidth = phoneWidth * 16 / 640;
                destWidth = (phoneWidth - (marginLeft * 2) - dividerWidth) / 2;
                destHeight = destWidth;
                RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) itemViewHolder.ivProductImage.getLayoutParams();
                if (leftImagelp != null) {
                    leftImagelp.width = destWidth;
                    leftImagelp.height = destHeight;
                    itemViewHolder.ivProductImage.setLayoutParams(leftImagelp);
                }
            }
            final String leftProductImageUrl = leftProductEntity.getSmallImage();
            // load left image
            if (itemViewHolder.ivProductImage.getTag() != null) {
                if (!itemViewHolder.ivProductImage.getTag().toString().equals(leftProductImageUrl)) {
                    JImageUtils.downloadImageFromServerByUrl(itemViewHolder.itemView.getContext(), mImageLoader, itemViewHolder.ivProductImage, leftProductImageUrl, destWidth, destHeight);
                    itemViewHolder.ivProductImage.setTag(leftProductImageUrl);
                }
            } else {
                JImageUtils.downloadImageFromServerByUrl(itemViewHolder.itemView.getContext(), mImageLoader, itemViewHolder.ivProductImage, leftProductImageUrl, destWidth, destHeight);
                itemViewHolder.ivProductImage.setTag(leftProductImageUrl);
            }
            String leftProductName = leftProductEntity.getName();
            itemViewHolder.ctvProductName.setText(leftProductName);
            ///////////////////////russell////////////////////////
            if ("1".equals(leftProductEntity.getInStock())) {
                itemViewHolder.rlProductListOutOfStock.setVisibility(View.GONE);
            } else {
                itemViewHolder.rlProductListOutOfStock.setVisibility(View.VISIBLE);
            }
            String leftProductBrand = leftProductEntity.getBrand();
            if (!JDataUtils.isEmpty(leftProductBrand)) {
                leftProductBrand = leftProductBrand.toUpperCase();
            }
            if (!TextUtils.isEmpty(leftProductBrand)) {
                itemViewHolder.ctvProductBrand.setText(leftProductBrand);
            } else {
                itemViewHolder.ctvProductBrand.setVisibility(View.GONE);
            }

            float leftProductPriceFloat = 0.0f;
            String leftProductPrice = leftProductEntity.getPrice();
            try {
                leftProductPriceFloat = Float.parseFloat(leftProductPrice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            float leftProductFinalPriceFloat = 0.0f;
            String leftProductFinalPrice = leftProductEntity.getFinal_price();
            try {
                leftProductFinalPriceFloat = Float.parseFloat(leftProductFinalPrice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (JDataUtils.compare(leftProductFinalPriceFloat, leftProductPriceFloat) < 0) {
                itemViewHolder.ctvProductPrice.setVisibility(View.VISIBLE);
                itemViewHolder.ctvProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);
                itemViewHolder.ctvProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
                itemViewHolder.ctvProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else {
                itemViewHolder.ctvProductPrice.setVisibility(View.GONE);
            }
            itemViewHolder.ctvProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));
            setMerchantName(leftProductEntity.getVendorDisplayName(), leftProductEntity.getVendor_id(), itemViewHolder.ctvCurationProductMerchant);
        }
    }

    private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int index = 0; index < mImageViewTips.size(); index++) {
                if (index == position) {
                    mImageViewTips.get(index).setBackgroundResource(R.mipmap.doc_checked);
                } else {
                    mImageViewTips.get(index).setBackgroundResource(R.mipmap.dot_unchecked);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setMerchantName(final String merchantName, final String merchantId, final TextView ctvLeftCurationProductMerchant) {
        if (!TextUtils.isEmpty(merchantName)) {
            String soldBy = ctvLeftCurationProductMerchant.getContext().getResources().getString(R.string.soldby);
            if (!TextUtils.isEmpty(merchantId)) {
                ctvLeftCurationProductMerchant.setTextColor(JToolUtils.getColor(R.color.purple92018d));
                SpannableStringBuilder ss = new SpannableStringBuilder(soldBy + " " + merchantName);
                ss.setSpan(new ForegroundColorSpan(JToolUtils.getColor(R.color.greyB8B8B8)), 0, soldBy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ctvLeftCurationProductMerchant.setText(ss);
                if (!"0".equals(merchantId)) {
                    ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ctvLeftCurationProductMerchant.getContext(), MerchantStoreFrontActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, merchantId);
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, merchantName);
                            intent.putExtras(bundle);
                            ((com.whitelabel.app.BaseActivity) ctvLeftCurationProductMerchant.getContext()).startActivity(intent);
//                            ((BaseActivity) ctvLeftCurationProductMerchant.getContext()).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                } else {
                    ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ctvLeftCurationProductMerchant.getContext(), HomeActivity.class);
                            ((com.whitelabel.app.BaseActivity) ctvLeftCurationProductMerchant.getContext()).startActivity(i);
                        }
                    });
                }

            } else {
                ctvLeftCurationProductMerchant.setText(soldBy + " " + merchantName);
                ctvLeftCurationProductMerchant.setTextColor(JToolUtils.getColor(R.color.greyB8B8B8));
            }
        } else {
            ctvLeftCurationProductMerchant.setText("");
        }
    }

    public void setOnItemClickListener(OnItemClickLitener listener) {
        OnItemClickLitener mItemClickListener = listener;
    }

    public void setOnItemWishListCliekListener(OnItemClickLitener listener) {
        OnItemClickLitener mWishListLisener = listener;
    }

    public interface OnItemClickLitener {
        void onItemClick(ItemViewHolder itemViewHolder, int position);
    }



    @Override
    public int getItemCount() {
        return mProducts.size() + 2;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_viewpager)
        ViewPager viewPager;
        @BindView(R.id.ll_tips)
        LinearLayout llTips;
        @BindView(R.id.tv_title)
        CustomTextView tvTitle;
        @BindView(R.id.tv_desc)
        CustomTextView tvDesc;
        @BindView(R.id.count_down_curation)
        CustomCountdown countDownCuration;
        @BindView(R.id.rl_product_line)
        RelativeLayout rlProductLine;
        @BindView(R.id.fl_cms)
        FrameLayout flCms;

        @BindView(R.id.iv_view_toggle)
        ImageView ivViewToggle;
        @BindView(R.id.rl_viewbar)
        RelativeLayout rlViewbar;
        @BindView(R.id.ll_filter)
        LinearLayout mHeaderFilterLL;
        @BindView(R.id.ll_sort)
        LinearLayout mHeaderSortLL;


        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    protected class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProductImage)
        ImageView ivProductImage;
        @BindView(R.id.rl_product_list_out_of_stock)
        RelativeLayout rlProductListOutOfStock;
        @BindView(R.id.ctvProductBrand)
        CustomTextView ctvProductBrand;
        @BindView(R.id.ctvProductName)
        CustomTextView ctvProductName;
        @BindView(R.id.ctvProductPrice)
        CustomTextView ctvProductPrice;
        @BindView(R.id.ctvProductFinalPrice)
        CustomTextView ctvProductFinalPrice;
        @BindView(R.id.ctv_curation_product_merchant)
        CustomTextView ctvCurationProductMerchant;
        @BindView(R.id.product_item)
        LinearLayout productItem;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private final List<ImageView> mImageViewTips = new ArrayList<>();

    private void addTisView(LinearLayout linearLayout, int count) {
        for (int i = 0; i < count; i++) {
            ImageView imageViewTips = new ImageView(linearLayout.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
            lp.setMargins(5, 0, 5, 0);
            imageViewTips.setLayoutParams(lp);
            if (i == 0) {
                imageViewTips.setBackgroundResource(R.mipmap.doc_checked);
            } else {
                imageViewTips.setBackgroundResource(R.mipmap.dot_unchecked);
            }
            linearLayout.addView(imageViewTips);
            mImageViewTips.add(imageViewTips);
        }
    }

    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<BannerModel> images) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            JImageUtils.downloadImageFromServerByUrl(context, imageLoader, imageView, images.get(i).getUrl(), 640, 640 * 240 / 490);
            imgs.add(imageView);
        }
        return imgs;
    }

}

package com.whitelabel.app.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.adapter.FlowViewAdapter;
import com.whitelabel.app.adapter.ProductListAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.whitelabel.app.utils.AnimUtil.setWishIconColorToPurple;


public class CategoryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TWOROW_ITEM = 256478;
    private static final int TYPE_SINGLEROW_ITEM = 256123;
    private static final int TYPE_HEADER = 10000;
    private static final int TYPE_MIDDLE = 20000;
    private static final int TYPE_FOOTER = 9621147;
    private CategoryDetailModel categoryDetailModel;
    private final ImageLoader mImageLoader;

    public interface OnFilterSortBarListener {
        void onSwitchViewClick(View view);

        void onFilterClick();

        void onSortClick();
    }


    private static final class DataHandler extends Handler {
        private final WeakReference<CategoryDetailAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, CategoryDetailAdapter productListAdapter) {
            mAdapter = new WeakReference<CategoryDetailAdapter>(productListAdapter);
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
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(mContext.get(), wishDelEntityResult.getWishListItemCount());
                    } else {
                        ErrorMsgBean bean = (ErrorMsgBean) msg.obj;
                        int position = Integer.parseInt(String.valueOf(bean.getParams()));
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        int position = Integer.parseInt(String.valueOf(addToWishlistEntity.getParams()));
                        SVRAppserviceProductSearchResultsItemReturnEntity productEntity =null;
                        int bestcount=mAdapter.get().categoryDetailModel.getNewArrivalProducts().size();
                                if(position>mAdapter.get().categoryDetailModel.getNewArrivalProducts().size()){
                                    productEntity=mAdapter.get().categoryDetailModel.getBestSellerProducts().get((position-bestcount-2));
                                }else{
                                    productEntity=mAdapter.get().categoryDetailModel.getNewArrivalProducts().get((position-1));
                                }

                        productEntity.setItem_id(addToWishlistEntity.getItemId());
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(mContext.get(), addToWishlistEntity.getWishListItemCount());
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
                            Toast.makeText(mContext.get(), bean.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
                case MyAccountDao.ERROR:
                case ProductDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mContext.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }
    private final String TAG = this.getClass().getSimpleName();
    MyAccountDao  myAccountDao;
    ProductDao mProductDao;
    public CategoryDetailAdapter(Context context,CategoryDetailModel categoryDetailModel, ImageLoader loader) {
        this.categoryDetailModel = categoryDetailModel;
        mImageLoader = loader;
        DataHandler dataHandler = new DataHandler(context, this);
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
    }

   public CategoryDetailModel getData(){
       return categoryDetailModel;
   }


    public int getNewArrivalProductSize() {
        return categoryDetailModel.getNewArrivalProducts().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_detail_header, null);
            return new HeaderViewHolder(view);
        }
//        else if (viewType == TYPE_FOOTER) {
//            RefreshLoadMoreRecyclerViewV2.CustomDragRecyclerFooterView footerView = new RefreshLoadMoreRecyclerViewV2.CustomDragRecyclerFooterView(parent.getContext());
//            return new FooterHolder(footerView);
//        }
//
        else if (viewType == TYPE_MIDDLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_sellers, null);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curation_productlist_item, null);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == categoryDetailModel.getNewArrivalProducts().size() + 1) {
            return TYPE_MIDDLE;
        } else {
            return TYPE_TWOROW_ITEM;
        }
//        else {
//            return TYPE_SINGLEROW_ITEM;
//        }


        //        else if (position == mProducts.size() + 1) {
//            return TYPE_FOOTER;
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (categoryDetailModel == null) return;
            if (TextUtils.isEmpty(categoryDetailModel.getCategory_img())) {
                headerViewHolder.detailViewpager.setVisibility(View.GONE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    int width = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity)
                            headerViewHolder.detailViewpager.getContext());
                    int imageHeight = width * 240 / 490;
                    headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
                    List<String> imgs=new ArrayList<>();
                    imgs.add(categoryDetailModel.getCategory_img());
                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(), mImageLoader, imgs)));
//                    headerViewHolder.detailViewpager.addOnPageChangeListener(mPageChangeListener);
//                    addTisView(headerViewHolder.llTips, categoryDetailModel.getCategory_img().size());
//                    if (mImageViewTips.size() == 1) {
//                        headerViewHolder.llTips.setVisibility(View.GONE);
//                    }
                    headerViewHolder.detailViewpager.setTag("use");
                }
            }

        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity = null;
            if (position > (categoryDetailModel.getNewArrivalProducts().size())) {
                leftProductEntity = categoryDetailModel.getBestSellerProducts().get(position - 2 - categoryDetailModel.getNewArrivalProducts().size());
            } else {
                leftProductEntity = categoryDetailModel.getNewArrivalProducts().get(position - 1);
            }
            int destWidth = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 100);
            int destHeight = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 120);
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
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickLitener!=null){
                        onItemClickLitener.onItemClick(itemViewHolder,position);
                    }
                }
            });
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

            float leftProductFinalPriceFloat = 0.0f;
            String leftProductFinalPrice = leftProductEntity.getFinal_price();
            try {
                leftProductFinalPriceFloat = Float.parseFloat(leftProductFinalPrice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                leftProductPriceFloat = Float.parseFloat(leftProductPrice);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (leftProductEntity.getIsLike() == 1) {
                setWishIconColorToPurpleNoAnim(itemViewHolder.ivLeftProductlistWishIcon);
            } else {
                setWishIconColorToBlankNoAnim(itemViewHolder.ivLeftProductlistWishIcon);
            }
            final SVRAppserviceProductSearchResultsItemReturnEntity finalLeftProductEntity = leftProductEntity;
            itemViewHolder.rlLeftProductlistWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalLeftProductEntity.getIsLike() == 1) {
                        sendRequestToDeteleteCell(finalLeftProductEntity,itemViewHolder.ivLeftProductlistWishIcon, finalLeftProductEntity.getItem_id(),itemViewHolder.getAdapterPosition());
                    } else {
                        addtoWishlistsendRequest(finalLeftProductEntity,
                                itemViewHolder.rlLeftProductlistWish, itemViewHolder.ivLeftProductlistWishIcon, itemViewHolder.ivLeftProductlistWishIcon2,
                                itemViewHolder.getAdapterPosition());
                    }
                }
            });
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
        }else if(holder instanceof ViewHolder){
            ViewHolder viewHolder= (ViewHolder) holder;
            if(position==0){
                viewHolder.tvTxt.setText(viewHolder.itemView.getContext().getResources().getString(R.string.home_new_arrivals));
            }else{
                viewHolder.tvTxt.setText(viewHolder.itemView.getContext().getResources().getString(R.string.home_best_sellers));
            }
        }
    }
    private void setWishIconColorToBlankNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.GONE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_normal_v2);
    }

    private void setWishIconColorToPurpleNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
//        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }

//    private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            for (int index = 0; index < mImageViewTips.size(); index++) {
//                if (index == position) {
//                    mImageViewTips.get(index).setBackground(JImageUtils.getThemeCircle( mImageViewTips.get(index).getContext()));
//                } else {
//                    mImageViewTips.get(index).setBackgroundResource(R.mipmap.dot_unchecked);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };



    private void addtoWishlistsendRequest(SVRAppserviceProductSearchResultsItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ivWwishIcon.getContext())) {
            entity.setIsLike(1);
            mProductDao.addProductListToWish(entity.getProductId(), WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey(), tempPosition);
            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
        } else {
            Intent intent = new Intent();
            intent.setClass(ivWwishIcon.getContext(), LoginRegisterActivity.class);
            ((Activity)ivWwishIcon.getContext()). startActivityForResult(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
            ((Activity)ivWwishIcon.getContext()).overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
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
    private void sendRequestToDeteleteCell(SVRAppserviceProductSearchResultsItemReturnEntity bean,ImageView ivWwishIcon, String itemId, int tempPosition) {
        setWishIconColorToBlank(ivWwishIcon);
        bean.setIsLike(0);
        if(!TextUtils.isEmpty(itemId)) {
            myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey(), itemId, tempPosition);
        }
    }
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
                            ctvLeftCurationProductMerchant.getContext().startActivity(intent);
                            ((Activity) ctvLeftCurationProductMerchant.getContext()).overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    });
                } else {
                    ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ctvLeftCurationProductMerchant.getContext(), HomeActivity.class);
                            ctvLeftCurationProductMerchant.getContext().startActivity(i);
                            ((Activity) ctvLeftCurationProductMerchant.getContext()).overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
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

    public interface OnItemClickLitener {
        void onItemClick(ItemViewHolder itemViewHolder, int position);
    }


    private OnItemClickLitener onItemClickLitener;


    public  void  setOnItemClickLitener(OnItemClickLitener  onItemClickLitener){
        this.onItemClickLitener=onItemClickLitener;

    }
    @Override
    public int getItemCount() {
        return categoryDetailModel.getBestSellerProducts().size() + categoryDetailModel.getNewArrivalProducts().size() + 2;
    }
//
//    static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.detail_viewpager)
//        ViewPager viewPager;
//        @BindView(R.id.ll_tips)
//        LinearLayout llTips;
//        @BindView(R.id.tv_title)
//        CustomTextView tvTitle;
//        @BindView(R.id.tv_desc)
//        CustomTextView tvDesc;
//        @BindView(R.id.count_down_curation)
//        CustomCountdown countDownCuration;
//        @BindView(R.id.rl_product_line)
//        RelativeLayout rlProductLine;
//        @BindView(R.id.fl_cms)
//        FrameLayout flCms;
//
//        @BindView(R.id.iv_view_toggle)
//        ImageView ivViewToggle;
//        @BindView(R.id.rl_viewbar)
//        RelativeLayout rlViewbar;
//        @BindView(R.id.ll_filter)
//        LinearLayout mHeaderFilterLL;
//        @BindView(R.id.ll_sort)
//        LinearLayout mHeaderSortLL;
//
//
//        HeaderViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }

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
        @BindView(R.id.rl_product_wish)
        RelativeLayout rlLeftProductlistWish;
        @BindView(R.id.iv_product_wish_icon)
        ImageView ivLeftProductlistWishIcon;
       @BindView(R.id.iv_product_wish_icon2)
        ImageView ivLeftProductlistWishIcon2;
        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

//    private final List<ImageView> mImageViewTips = new ArrayList<>();
//
//    private final void addTisView(LinearLayout linearLayout, int count) {
//        for (int i = 0; i < count; i++) {
//            ImageView imageViewTips = new ImageView(linearLayout.getContext());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
//            lp.setMargins(5, 0, 5, 0);
//            imageViewTips.setLayoutParams(lp);
//            if (i == 0) {
//                imageViewTips.setBackground(JImageUtils.getThemeCircle(linearLayout.getContext()));
//            } else {
//                imageViewTips.setBackgroundResource(R.mipmap.dot_unchecked);
//            }
//            linearLayout.addView(imageViewTips);
//            mImageViewTips.add(imageViewTips);
//        }
//    }

    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<String> images) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            JImageUtils.downloadImageFromServerByUrl(context, imageLoader, imageView, images.get(i), 640, 640 * 240 / 490);
            imgs.add(imageView);
        }
        return imgs;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HeaderViewHolder extends  RecyclerView.ViewHolder {
        @BindView(R.id.detail_viewpager)
        ViewPager detailViewpager;
        @BindView(R.id.ll_tips)
        LinearLayout llTips;
        @BindView(R.id.rl_switch_img)
        RelativeLayout rlSwitchImg;
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

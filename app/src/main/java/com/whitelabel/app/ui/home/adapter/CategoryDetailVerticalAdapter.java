package com.whitelabel.app.ui.home.adapter;

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

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.adapter.FlowViewAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.common.WishlistObservable;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;




public class CategoryDetailVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TWOROW_ITEM = 256478;
    private static final int TYPE_SINGLEROW_ITEM = 256123;
    private static final int TYPE_HEADER = 10000;
    private static final int TYPE_MIDDLE = 20000;
    private static final int TYPE_FOOTER = 9621147;
    private CategoryDetailModel categoryDetailModel;
    private final ImageLoader mImageLoader;
    MyAccountDao  myAccountDao;
    ProductDao mProductDao;
    private double screenWidth;
    public interface OnFilterSortBarListener {
        void onSwitchViewClick(View view);
        void onFilterClick();
        void onSortClick();
    }
    private static final class DataHandler extends Handler {
        private final WeakReference<CategoryDetailVerticalAdapter> mAdapter;
        private final WeakReference<Context> mContext;
        public DataHandler(Context context, CategoryDetailVerticalAdapter productListAdapter) {
            mAdapter = new WeakReference<CategoryDetailVerticalAdapter>(productListAdapter);
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
//                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mContext.get());
//                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }


    public CategoryDetailVerticalAdapter(Context context, CategoryDetailModel categoryDetailModel, ImageLoader loader) {
        this.categoryDetailModel = categoryDetailModel;
        mImageLoader = loader;
        DataHandler dataHandler = new DataHandler(context, this);
        String TAG = this.getClass().getSimpleName();
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
        screenWidth=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) context);
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
        }else if (viewType == TYPE_MIDDLE) {
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
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (categoryDetailModel == null) return;
//            int imageHeight = (int) (categoryDetailModel.getImage_height() * (screenWidth/categoryDetailModel.getImage_width()));
//            if(imageHeight==0){
                int  imageHeight= (int) (screenWidth*(348.0/750));
//            }
            headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));
            if(categoryDetailModel.getNewArrivalProducts()==null||categoryDetailModel.getNewArrivalProducts().size()==0){
                headerViewHolder.llNewArrivals.setVisibility(View.GONE);
            }else{
                headerViewHolder.llNewArrivals.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(categoryDetailModel.getCategory_img())) {
                headerViewHolder.detailViewpager.setVisibility(View.VISIBLE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    List<String> imgs=new ArrayList<>();
                    imgs.add(categoryDetailModel.getCategory_img());
                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(), mImageLoader, imgs,imageHeight)));
                    headerViewHolder.detailViewpager.setTag("use");
                }
            }
        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity = null;
            int  finalPostion=0;
            if (position > (categoryDetailModel.getNewArrivalProducts().size())) {
                leftProductEntity = categoryDetailModel.getBestSellerProducts().get(position - 2 - categoryDetailModel.getNewArrivalProducts().size());
                finalPostion=position-2;
            } else {
                leftProductEntity = categoryDetailModel.getNewArrivalProducts().get(position - 1);
                finalPostion=position-1;
            }
            int destWidth = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 100);
            int destHeight = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 120);
            int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) itemViewHolder.itemView.getContext());
            int marginLeft = JDataUtils.dp2Px(10);
            destWidth = (phoneWidth - (marginLeft * 2)) / 2;
            destHeight = destWidth;
            RelativeLayout.LayoutParams leftImagelp = (RelativeLayout.LayoutParams) itemViewHolder.ivProductImage.getLayoutParams();
            if (leftImagelp != null) {
                leftImagelp.width = LinearLayout.LayoutParams.MATCH_PARENT;
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

            if(finalPostion%2==1){
                itemViewHolder.itemView.setPadding(JDataUtils.dp2Px(10),0,JDataUtils.dp2Px(10),JDataUtils.dp2Px(10));
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
            finalLeftProductEntity.setPosition(position);
            Observable<SVRAppserviceProductSearchResultsItemReturnEntity> observable=Observable.
                    create(new WishlistObservable(itemViewHolder.rlLeftProductlistWish,finalLeftProductEntity,
                            itemViewHolder.ivLeftProductlistWishIcon, itemViewHolder.ivLeftProductlistWishIcon2));
            observable.buffer(observable.debounce(1000, TimeUnit.MILLISECONDS))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<SVRAppserviceProductSearchResultsItemReturnEntity>>() {
                        @Override
                        public void call(List<SVRAppserviceProductSearchResultsItemReturnEntity> beans) {
                                    SVRAppserviceProductSearchResultsItemReturnEntity bean=beans.get(beans.size()-1);
                                    if(bean.getIsLike()==1){
                                        mProductDao.addProductListToWish(bean.getProductId(),WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),
                                                bean.getPosition());
                                    }else if(bean.getItem_id()!=null){
                                        myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),
                                                bean.getItem_id(),bean.getPosition());
                                    }
                        }
                    });
            if (JDataUtils.compare(leftProductFinalPriceFloat, leftProductPriceFloat) < 0) {
                itemViewHolder.ctvProductPrice.setVisibility(View.VISIBLE);
//                itemViewHolder.ctvProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);
                itemViewHolder.ctvProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
                itemViewHolder.ctvProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else {
                itemViewHolder.ctvProductPrice.setVisibility(View.GONE);
            }
            itemViewHolder.ctvProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " +
                    JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));
            setMerchantName(leftProductEntity.getVendorDisplayName(), leftProductEntity.getVendor_id(), itemViewHolder.ctvCurationProductMerchant);
        }else if(holder instanceof ViewHolder){
            ViewHolder viewHolder= (ViewHolder) holder;
                viewHolder.tvTxt.setText(viewHolder.itemView.getContext().getResources().getString(R.string.home_best_sellers));
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
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }

    private void setWishIconColorToBlank(final ImageView ivWishIcon) {

    }
    private void sendRequestToDeteleteCell(SVRAppserviceProductSearchResultsItemReturnEntity bean,ImageView ivWwishIcon, String itemId) {
        setWishIconColorToBlank(ivWwishIcon);
        bean.setIsLike(0);
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
                            ((BaseActivity)ctvLeftCurationProductMerchant.getContext()).startActivity(intent);
//                            ((BaseActivity)getActivity()).startActivityTransitionAnim();
//                            ((Activity) ctvLeftCurationProductMerchant.getContext()).overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                        }
                    });
                } else {
                    ctvLeftCurationProductMerchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ctvLeftCurationProductMerchant.getContext(), HomeActivity.class);
                            ((BaseActivity)ctvLeftCurationProductMerchant.getContext()).startActivity(i);
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
        int offset=1;
        if(categoryDetailModel.getBestSellerProducts().size()!=0){
            offset+=1;
        }
        return categoryDetailModel.getBestSellerProducts().size() + categoryDetailModel.getNewArrivalProducts().size()+offset;
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

   public static class ItemViewHolder extends RecyclerView.ViewHolder {
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

    private List<ImageView> createImageViewList(Context context, ImageLoader imageLoader, List<String> images,int  imageHeight) {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            JImageUtils.downloadImageFromServerByUrl(context, imageLoader, imageView, images.get(i), (int) screenWidth,imageHeight);
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

    public static class HeaderViewHolder extends  RecyclerView.ViewHolder {
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
        @BindView(R.id.ll_new_arrivals)
        LinearLayout  llNewArrivals;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

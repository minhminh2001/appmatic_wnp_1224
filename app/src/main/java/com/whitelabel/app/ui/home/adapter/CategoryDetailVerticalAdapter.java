package com.whitelabel.app.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.IHomeItemClickListener;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.common.WishlistObservable;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.GlideImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;




public class CategoryDetailVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CategoryDetailNewModel categoryDetailModel;
    private final ImageLoader mImageLoader;
    MyAccountDao  myAccountDao;
    ProductDao mProductDao;
    private double screenWidth;
    int rcyListSize;
    Map<Integer,Integer> childCountMaps=new HashMap<>();
    List<SVRAppserviceProductSearchResultsItemReturnEntity> allItemLists=new ArrayList<>();
    List<Integer> titleLists=new ArrayList<>();
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
                        SVRAppserviceProductSearchResultsItemReturnEntity productEntity=mAdapter.get().allItemLists.get(position);
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
    public CategoryDetailVerticalAdapter(Context context, CategoryDetailNewModel categoryDetailModel, ImageLoader loader) {
        this.categoryDetailModel = categoryDetailModel;
        mImageLoader = loader;
        DataHandler dataHandler = new DataHandler(context, this);
        String TAG = this.getClass().getSimpleName();
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mProductDao = new ProductDao(TAG, dataHandler);
        screenWidth=WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) context);
//        getRcyListSize();
        createAllItemAndCreateTitleIndex();
    }

    private void createAllItemAndCreateTitleIndex(){
        if (categoryDetailModel!=null && categoryDetailModel.getCarousels()!=null && !categoryDetailModel.getCarousels().isEmpty()) {
            int firstTitle=1;
            int titleIndex=firstTitle;
            allItemLists.clear();
            titleLists.clear();
            titleLists.add(firstTitle);
            SVRAppserviceProductSearchResultsItemReturnEntity entiy =new SVRAppserviceProductSearchResultsItemReturnEntity();
            entiy.setName("header");
            //add header
            allItemLists.add(entiy);
            for (int i=0;i<categoryDetailModel.getCarousels().size();i++){
                SVRAppserviceProductSearchResultsItemReturnEntity bean =new SVRAppserviceProductSearchResultsItemReturnEntity();
                List<CategoryDetailNewModel.CarouselsBean> carousels = categoryDetailModel.getCarousels();
                bean.setName(carousels.get(i).getTitle());
                //add Title
                allItemLists.add(bean);
                List<SVRAppserviceProductSearchResultsItemReturnEntity> items = carousels.get(i).getItems();
                allItemLists.addAll(items);
                //if lastPosition don't add titleIndex
                if (i==carousels.size()-1){
                    break;
                }
                //create Title's Index and save
                titleIndex+=items.size()+1;
                titleLists.add(titleIndex);
            }
        }
    }

    public boolean isTitleIndex(int position){
        if (titleLists!=null && !titleLists.isEmpty()){
            return titleLists.contains(position);
        }else {
            return false;
        }
    }

   public CategoryDetailNewModel getData(){
       return categoryDetailModel;
   }

    public List<SVRAppserviceProductSearchResultsItemReturnEntity> getAllItemLists() {
        return allItemLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Const.HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_detail_header, null);
            return new HeaderViewHolder(view);
        }else if (viewType == Const.TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_base_sellers, null);
            return new TitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curation_productlist_item, null);
            return new ItemViewHolder(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Const.HEADER;
        } else if (isTitleIndex(position)) {
            return Const.TITLE;
        } else {
            return Const.ITEM;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (categoryDetailModel == null) return;
//            int imageHeight = (int) (categoryDetailModel.getImage_height() * (screenWidth/categoryDetailModel.getImage_width()));
//            if(imageHeight==0){
                int  imageHeight= (int) (screenWidth*(348.0/750));
//            }
            headerViewHolder.detailViewpager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight));

            if (categoryDetailModel.getBanners().isEmpty()) {
                headerViewHolder.detailViewpager.setVisibility(View.VISIBLE);
            } else {
                if (headerViewHolder.detailViewpager.getTag() == null) {
                    List<String> imgs=new ArrayList<>();
//                    imgs.add(categoryDetailModel.getCategory_img());
                    //                    headerViewHolder.detailViewpager.setAdapter(new FlowViewAdapter(createImageViewList(headerViewHolder.itemView.getContext(), mImageLoader, imgs,imageHeight)));
                    imgs.addAll(createImageUrlList(headerViewHolder.itemView.getContext(),categoryDetailModel.getBanners(),imageHeight));
                    headerViewHolder.detailViewpager.setImages(imgs)
                            .setImageLoader(new GlideImageLoader())
                            .setBannerStyle(BannerConfig.NOT_INDICATOR)
                            .setDelayTime(CategoryDetailHorizontalAdapter.NORMAL_BANNER_DELAY_TIME)
                            .setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int i) {
                                    onHeaderClick.onItemClick(headerViewHolder,i);
                                }
                            })
                            .start();

                    headerViewHolder.detailViewpager.setTag("use");
                }
            }
        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity = null;
            int  finalPostion=0;
            if (!titleLists.isEmpty() && !allItemLists.isEmpty() && position<allItemLists.size()){
                leftProductEntity =allItemLists.get(position);
                finalPostion=position-titleLists.size();
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
                String leftProductName = leftProductEntity.getName();
                itemViewHolder.ctvProductName.setText(leftProductName);
                ///////////////////////russell////////////////////////
                if ("1".equals(leftProductEntity.getInStock())) {
                    itemViewHolder.rlProductListOutOfStock.setVisibility(View.GONE);
                } else {
                    itemViewHolder.rlProductListOutOfStock.setVisibility(View.VISIBLE);
                }
                String leftProductBrand = leftProductEntity.getBrand();
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
                if (!JDataUtils.isEmpty(leftProductBrand)) {
                    leftProductBrand = leftProductBrand.toUpperCase();
                }
                if (!TextUtils.isEmpty(leftProductBrand)) {
                    itemViewHolder.ctvProductBrand.setText(leftProductBrand);
                } else {
                    itemViewHolder.ctvProductBrand.setVisibility(View.GONE);
                }

                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onVerticalItemClickLitener !=null){
                            onVerticalItemClickLitener.onItemClick(itemViewHolder,position);
                        }
                    }
                });

                if(finalPostion%2==1){
                    itemViewHolder.itemView.setPadding(JDataUtils.dp2Px(10),0,JDataUtils.dp2Px(10),JDataUtils.dp2Px(10));
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
            }



        }else if(holder instanceof TitleViewHolder){
            TitleViewHolder viewHolder= (TitleViewHolder) holder;
            if (position<allItemLists.size()){
                viewHolder.tvTxt.setText(allItemLists.get(position).getName());
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




    private IHomeItemClickListener.IVerticalItemClickLitener onVerticalItemClickLitener;
    private IHomeItemClickListener.IHeaderItemClickListener onHeaderClick;

    public  void setOnVerticalItemClickLitener(IHomeItemClickListener.IVerticalItemClickLitener onVerticalItemClickLitener){
        this.onVerticalItemClickLitener = onVerticalItemClickLitener;
    }

    public void setOnHeaderClick(IHomeItemClickListener.IHeaderItemClickListener onHeaderClick) {
        this.onHeaderClick = onHeaderClick;
    }

    @Override
    public int getItemCount() {
        return allItemLists.isEmpty()?1:allItemLists.size();
    }
//
//    static class HeaderViewHolder extends RecyclerView.TitleViewHolder {
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
    private List<String> createImageUrlList(Context context, List<CategoryDetailNewModel.BannersBean> images,int  imageHeight) {
        List<String> imgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            CategoryDetailNewModel.BannersBean bannersBeanResponse = images.get(i);
            String norUrl=bannersBeanResponse.getImage();
            String imageServerUrlByWidthHeight = JImageUtils.getImageServerUrlByWidthHeight(context, norUrl, (int) screenWidth, imageHeight);
            imgs.add(imageServerUrlByWidthHeight);
        }
        return imgs;
    }
    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.tv_txt)
        TextView tvTxt;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class HeaderViewHolder extends  RecyclerView.ViewHolder {
        @BindView(R.id.detail_viewpager)
//        ViewPager detailViewpager;
        Banner detailViewpager;
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

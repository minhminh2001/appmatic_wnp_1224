package com.whitelabel.app.ui.home.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.callback.IHomeItemClickListener;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.AddToWishlistEntity;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;



/**
 * Created by ray on 2017/5/9.
 */

public class CategoryDetailItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ProductDao mProductDao;
    private List<SVRAppserviceProductSearchResultsItemReturnEntity> mBeans;
    private ImageLoader mImageLoader;
    private MyAccountDao myAccountDao;
    //click which CarouselsBean'list
    private int parentPosition;
    private HomeActivity homeActivity;

    public CategoryDetailItemAdapter(HomeActivity homeActivity, int position, List<SVRAppserviceProductSearchResultsItemReturnEntity> beans, ImageLoader imageLoader) {
        mBeans = beans;
        this.homeActivity=homeActivity;
        mImageLoader=imageLoader;
        this.parentPosition=position;
        String TAG = "CategoryDetailItemAdapter";
        mProductDao = new ProductDao(TAG, new DataHandler(homeActivity,this));
        myAccountDao = new MyAccountDao(TAG, new DataHandler(homeActivity,this));
    }
    private static final class DataHandler extends Handler {
        private final WeakReference<CategoryDetailItemAdapter> mAdapter;
        private final WeakReference<Context> mContext;
        public DataHandler(Context context, CategoryDetailItemAdapter productListAdapter) {
            mAdapter = new WeakReference<CategoryDetailItemAdapter>(productListAdapter);
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
                        productEntity=mAdapter.get().mBeans.get(position);
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
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_curation_productlist_item, null);
        return new ItemViewHolder(view);
    }
    private IHomeItemClickListener.IHorizontalItemClickListener onItemClickLitener;

    public  void setOnItemClickLitener(IHomeItemClickListener.IHorizontalItemClickListener onItemClickLitener){
        this.onItemClickLitener=onItemClickLitener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SVRAppserviceProductSearchResultsItemReturnEntity leftProductEntity=mBeans.get(position);
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        int destWidth = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 100);
        int destHeight = JScreenUtils.dip2px(itemViewHolder.itemView.getContext(), 120);
        int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth((Activity) itemViewHolder.itemView.getContext());
        int marginLeft = phoneWidth * 15 / 640;
        int dividerWidth = phoneWidth * 16 / 640;
        destWidth = (phoneWidth - (marginLeft * 2) - dividerWidth) / 2;
        destHeight = destWidth;
        itemViewHolder.productItem.setLayoutParams(new LinearLayout.LayoutParams(destWidth, ActionBar.LayoutParams.WRAP_CONTENT));
        final String leftProductImageUrl = leftProductEntity.getSmallImage();
        // load left image
        if (itemViewHolder.ivProductImage.getTag() != null) {
            if (!itemViewHolder.ivProductImage.getTag().toString().equals(leftProductImageUrl)) {
                JImageUtils.downloadImageFromServerByProductUrl(itemViewHolder.itemView.getContext(), mImageLoader, itemViewHolder.ivProductImage, leftProductImageUrl, destWidth, destHeight);
                itemViewHolder.ivProductImage.setTag(leftProductImageUrl);
            }
        } else {
            JImageUtils.downloadImageFromServerByProductUrl(itemViewHolder.itemView.getContext(), mImageLoader, itemViewHolder.ivProductImage, leftProductImageUrl, destWidth, destHeight);
            itemViewHolder.ivProductImage.setTag(leftProductImageUrl);
        }
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickLitener!=null){
                    onItemClickLitener.onItemClick(itemViewHolder,parentPosition,position);
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
        setUnLoginClickWishBackThisPageToRefresh(itemViewHolder.itemView.getContext(),leftProductEntity,itemViewHolder.ivLeftProductlistWishIcon,position);
        final SVRAppserviceProductSearchResultsItemReturnEntity finalLeftProductEntity = leftProductEntity;
        finalLeftProductEntity.setPosition(position);
        Observable<SVRAppserviceProductSearchResultsItemReturnEntity> observable=Observable.
                create(new WishlistObservable(itemViewHolder.rlLeftProductlistWish, finalLeftProductEntity,
                        itemViewHolder.ivLeftProductlistWishIcon, itemViewHolder.ivLeftProductlistWishIcon2, new WishlistObservable.IWishIconUnLogin() {
                    @Override
                    public void clickWishToLogin() {
                        homeActivity.saveProductIdWhenCheckPage(finalLeftProductEntity.getProductId(),finalLeftProductEntity.getIsLike(),true);
                    }
                }));
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
                        }else if(!JDataUtils.isEmpty(bean.getItemId())){
                            myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),
                                    bean.getItemId(),bean.getPosition());
                        }
                    }
                });
//        itemViewHolder.rlLeftProductlistWish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (finalLeftProductEntity.getIsLike() == 1) {
//                    sendRequestToDeteleteCell(finalLeftProductEntity,itemViewHolder.ivLeftProductlistWishIcon, finalLeftProductEntity.getItem_id(),itemViewHolder.getAdapterPosition());
//                } else {
//                    addtoWishlistsendRequest(itemViewHolder.itemView.getContext(),finalLeftProductEntity,
//                            itemViewHolder.rlLeftProductlistWish, itemViewHolder.ivLeftProductlistWishIcon, itemViewHolder.ivLeftProductlistWishIcon2,
//                            itemViewHolder.getAdapterPosition());
//                }
//            }
//        });
        if (JDataUtils.compare(leftProductFinalPriceFloat, leftProductPriceFloat) < 0) {
            itemViewHolder.ctvProductPrice.setVisibility(View.VISIBLE);
            itemViewHolder.ctvProductFinalPrice.setPadding(JDataUtils.dp2Px(9), 0, JDataUtils.dp2Px(9), 0);
            itemViewHolder.ctvProductPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductPriceFloat + ""));
            itemViewHolder.ctvProductPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            itemViewHolder.ctvProductPrice.setVisibility(View.GONE);
        }
        itemViewHolder.ctvProductFinalPrice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble(leftProductFinalPriceFloat + ""));
//        setMerchantName(leftProductEntity.getVendorDisplayName(), leftProductEntity.getVendor_id(), itemViewHolder.ctvCurationProductMerchant);
    }

    private void sendRequestToDeteleteCell(SVRAppserviceProductSearchResultsItemReturnEntity bean,ImageView ivWwishIcon, String itemId, int tempPosition) {
        setWishIconColorToBlank(ivWwishIcon);
        bean.setIsLike(0);
        if(!TextUtils.isEmpty(itemId)) {
            myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey(), itemId, tempPosition);
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

    public void setUnLoginClickWishBackThisPageToRefresh(Context context, SVRAppserviceProductSearchResultsItemReturnEntity entity, ImageView ivWwishIcon,  int tempPosition){
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(context) && homeActivity.isUnLoginCanWishIconRefresh(entity.getProductId())){
            entity.setIsLike(1);
            mProductDao.addProductListToWish(entity.getProductId(), WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), tempPosition);
            setWishIconColorToPurpleNoAnim(ivWwishIcon);
        }
    }

//    private void addtoWishlistsendRequest(Context context,SVRAppserviceProductSearchResultsItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
//        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ivWwishIcon.getContext())) {
//            entity.setIsLike(1);
//            mProductDao.addProductListToWish(entity.getProductId(), WhiteLabelApplication.getAppConfiguration().getUserInfo().getSessionKey(), tempPosition);
//            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
//        } else {
//            Intent intent = new Intent();
//            intent.setClass(ivWwishIcon.getContext(), LoginRegisterActivity.class);
//            ((Activity)context).startActivityForResult(intent, LoginRegisterActivity.REQUESTCODE_LOGIN);
//            ((Activity)context).overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
//        }
//    }

    private void setWishIconColorToPurpleNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(),R.mipmap.wishlist_purple_pressed_v2));
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }

    private void setWishIconColorToBlankNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.GONE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_normal_v2);
    }
    @Override
    public int getItemCount() {
        if (mBeans!=null){
            return mBeans.size();
        }else {
            return 0;
        }

    }

    static class ItemViewHolder extends  RecyclerView.ViewHolder {
        @BindView(R.id.ivProductImage)
        ImageView ivProductImage;
        @BindView(R.id.rl_product_list_out_of_stock)
        RelativeLayout rlProductListOutOfStock;
        @BindView(R.id.iv_product_wish_icon2)
        ImageView ivLeftProductlistWishIcon2;
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

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

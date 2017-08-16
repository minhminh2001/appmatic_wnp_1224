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

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.callback.ProductDetailCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedResultsItemReturnEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
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
        DataHandler dataHandler = new DataHandler(context, this);
        mProductDao = new ProductDao(TAG, dataHandler);
        myAccountDao = new MyAccountDao(TAG, dataHandler);
        mImageLoader = imageLoader;
    }


    private static final class DataHandler extends Handler {
        private final WeakReference<ProductRecommendedListAdapter> mAdapter;
        private final WeakReference<Context> mContext;

        public DataHandler(Context context, ProductRecommendedListAdapter productRecommendedListAdapter) {
            mAdapter = new WeakReference<ProductRecommendedListAdapter>(productRecommendedListAdapter);
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
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        int position = Integer.parseInt(String.valueOf(addToWishlistEntity.getParams()));
                        SVRAppserviceProductRecommendedResultsItemReturnEntity productEntity = (SVRAppserviceProductRecommendedResultsItemReturnEntity) mAdapter.get().list.get(position);
                        productEntity.setItem_id(addToWishlistEntity.getItemId());
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(mContext.get(), addToWishlistEntity.getWishListItemCount());
                        Bundle bundle = new Bundle();
                        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_WISHLIST);
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
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mContext.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
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
    private void setWishIconColorToBlankNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.GONE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_normal_v2);
    }
    @Override
    public void onBindViewHolder(ViewHolderImp holder,  int position) {
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
                viewHolder.ctvLeftProductMerchant.setText(soldBy + " " + leftProductEntity.getVendorDisplayName());
                viewHolder.ctvLeftProductMerchant.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            viewHolder.ctvLeftProductMerchant.setText("");
        }
        if (leftProductEntity.getIs_like() == 1) {
            setWishIconColorToPurpleNoAnim(viewHolder.ivLeftProductlistWishIcon);
        } else {
            setWishIconColorToBlankNoAnim(viewHolder.ivLeftProductlistWishIcon);
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
          final int tempPosition = position;
            viewHolder.rlLeftProductlistWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leftProductEntity.getIs_like() == 1) {
                        sendRequestToDeteleteCell(viewHolder.ivLeftProductlistWishIcon, viewHolder.rlLeftProductlistWish, leftProductEntity.getItem_id(), tempPosition);
                    } else {
                        addtoWishlistsendRequest(leftProductEntity, viewHolder.rlLeftProductlistWish, viewHolder.ivLeftProductlistWishIcon, viewHolder.ivLeftProductlistWishIcon2, tempPosition);
                    }
                }
            });
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
                viewHolder.ctvRightProductMerchant.setText(soldBy + " " + rightProductEntity.getVendorDisplayName());
                viewHolder.ctvRightProductMerchant.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            viewHolder.ctvRightProductMerchant.setText("");
        }
        final int tempRightPosition = position;
        viewHolder.rlRightProductlistWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightProductEntity.getIs_like() == 1) {
                    sendRequestToDeteleteCell(viewHolder.ivRightProductlistWishIcon, viewHolder.rlRightProductlistWish, rightProductEntity.getItem_id(), tempRightPosition);
                } else {
                    addtoWishlistsendRequest(rightProductEntity, viewHolder.rlRightProductlistWish, viewHolder.ivRightProductlistWishIcon, viewHolder.ivRightProductlistWishIcon2, tempRightPosition);
                }
            }
        });
        viewHolder.llRightProduct.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams rightImagelp = (RelativeLayout.LayoutParams) viewHolder.ivRightProductImage.getLayoutParams();
        if (rightImagelp != null) {
            rightImagelp.width = destWidth;
            rightImagelp.height = destHeight;
            viewHolder.ivRightProductImage.setLayoutParams(rightImagelp);
        }
        if (rightProductEntity.getIs_like() == 1) {
            setWishIconColorToPurpleNoAnim(viewHolder.ivRightProductlistWishIcon);
        } else {
            setWishIconColorToBlankNoAnim(viewHolder.ivRightProductlistWishIcon);
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

    private void sendRequestToDeteleteCell(ImageView ivWwishIcon, RelativeLayout rlCurationWish, String itemId, int tempPosition) {
        list.get(tempPosition).setIs_like(0);
        setWishIconColorToBlank(ivWwishIcon);
        if (!TextUtils.isEmpty(list.get(tempPosition).getItem_id())) {
            myAccountDao.deleteWishListById(WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), itemId, tempPosition);
        }
    }

    private void addtoWishlistsendRequest(SVRAppserviceProductRecommendedResultsItemReturnEntity entity, RelativeLayout rlCurationWish, ImageView ivWwishIcon, ImageView ivWwishIcon2, int tempPosition) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(context)) {
            entity.setIs_like(1);
            mProductDao.addProductListToWish(entity.getProductId(), WhiteLabelApplication.getAppConfiguration().getUserInfo(context).getSessionKey(), tempPosition);
            setWishIconColorToPurple(ivWwishIcon, ivWwishIcon2);
        } else {
            productDetailCallback.saveProductIdWhenJumpLoginPage(entity.getProductId());
            Intent intent = new Intent();
            intent.setClass(context, LoginRegisterActivity.class);
            ((Activity) context).startActivityForResult(intent, LoginRegisterEmailLoginFragment.RESULTCODE);
            ((Activity) context).overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }
    private void setWishIconColorToPurple(ImageView ivWishIcon, final ImageView ivWishIcon2) {
        ivWishIcon2.setVisibility(View.VISIBLE);
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(), R.mipmap.wishlist_purple_pressed_v2));
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
    private void setWishIconColorToPurpleNoAnim(ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
        ivWishIcon.setImageResource(R.mipmap.wishlist_purple_pressed_v2);
        boolean repeatAnim = false;
        ivWishIcon.setTag(repeatAnim);
    }

    private void setWishIconColorToBlank(final ImageView ivWishIcon) {
        ivWishIcon.setVisibility(View.VISIBLE);
        boolean repeatAnim = true;
        ivWishIcon.setTag(repeatAnim);
        ivWishIcon.setImageDrawable(JImageUtils.getThemeIcon(ivWishIcon.getContext(), R.mipmap.wishlist_purple_pressed_v2));
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
        private RelativeLayout rlLeftOutOfStock, rlRightOutOfStock,rlLeftProductlistWish,rlRightProductlistWish;
        private View itemView;
        private ImageView ivLeftProductlistWishIcon,ivLeftProductlistWishIcon2,ivRightProductlistWishIcon,ivRightProductlistWishIcon2;
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

            rlLeftProductlistWish = (RelativeLayout) llLeftProduct.findViewById(R.id.rl_productlist_wish);
            ivLeftProductlistWishIcon = (ImageView) llLeftProduct.findViewById(R.id.iv_productlist_wish_icon);
            ivLeftProductlistWishIcon2 = (ImageView) llLeftProduct.findViewById(R.id.iv_productlist_wish_icon2);

            ivRightProductImage = (ImageView) llRightProduct.findViewById(R.id.ivProductImage);
            ctvRightProductName = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductName);
            ctvRightProductBrand = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductBrand);
            ctvRightProductPrice = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductPrice);
            ctvRightProductFinalPrice = (CustomTextView) llRightProduct.findViewById(R.id.ctvProductFinalPrice);
            rlRightOutOfStock = (RelativeLayout) llRightProduct.findViewById(R.id.rl_product_list_out_of_stock);
            ctvRightProductMerchant = (CustomTextView) llRightProduct.findViewById(R.id.ctv_product_merchant);

            rlRightProductlistWish = (RelativeLayout) llRightProduct.findViewById(R.id.rl_productlist_wish);
            ivRightProductlistWishIcon = (ImageView) llRightProduct.findViewById(R.id.iv_productlist_wish_icon);
            ivRightProductlistWishIcon2 = (ImageView) llRightProduct.findViewById(R.id.iv_productlist_wish_icon2);

        }
    }




}

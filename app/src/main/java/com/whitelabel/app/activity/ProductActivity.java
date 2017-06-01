package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.BindProductAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.ProductDetailCallback;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.model.WishDelEntityResult;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.ui.productdetail.BindProductActivity;
import com.whitelabel.app.utils.FacebookEventUtils;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JTimeUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.ShareUtil;
import com.whitelabel.app.widget.BindProductView;
import com.whitelabel.app.widget.CustomCoordinatorLayout;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomNestedScrollView;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.ProductChildListView;
import com.whitelabel.app.widget.ToolBarAlphaBehavior;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by imaginato on 2015/6/10.
 */
public class ProductActivity extends com.whitelabel.app.BaseActivity implements ProductDetailCallback, OnPageChangeListener, View.OnClickListener {
    //mGATrackTimeStart 加载pdp的时间，mGATrackAddCartTimeStart点击add to cart的时间
    public Long mGATrackTimeStart = 0L;
    public Long mGATrackAddCartTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    public static final int RESULT_WISH = 101;
    public static final int REQUEST_TOOLBAREXPAN = 1112;
    public static final int PRODUCT_PICTURE_REQUEST_CODE = 0x200;
    private String TAG = "ProductActivity";
    private final int REQUESTCODE_LOGIN = 1000;
    private final int REQUEST_SHOPPINGCART = 2000;
    private ViewGroup group;
    private BindProductView  bpvBindProduct;
    private TextView productDetailBindTitle;
    private TextView textView_num, oldprice, ctvAddToCart, price_textview,  ctvProductInStock, ctvProductOutOfStock, productUnavailable, productTrans, product_merchant;
    private Dialog mDialog;
    private TextView ctvProductName, ctvProductBrand;
    private AppBarLayout appbar_layout;
    private RelativeLayout mRLAddToWishlistSmall, mRLAddToWishlistBig;
    private LinearLayout  llBottomBar, mLLAddToCart;
    private ImageView ivHeaderBarWishlist, ivHeaderBarWishlist2, mIVHeaderBarWishlist, mIVHeaderBarWishlist2, ivHeaderBarShare;
    private ViewPager viewPager;
    private CustomTextView tvProductSaverm;
    private RelativeLayout rlProductPrice;
    private RelativeLayout rlProductQuantity, descriptionsRelative;
    private String productId;
    private LinearLayout llLayout;
    private LinearLayout llAttribute;
    private ArrayList<ImageView> mProductImageView;
    private ArrayList<ImageView> mProductImageViewTips;
    public ProductDetailModel mProductDetailBean;
    private float userSelectedProductPriceFloat;
    private float userSelectedProductFinalPriceFloat;
    private int userSelectedProductInStock;
//    private FrameLayout flSimpleConfig;
    private ProductChildListView pcGroupConfig;
    private long currUserSelectedProductMaxStockQty;
    private long userSelectedProductMaxStockQty;
    private long userSelectedProductQty;
    private LinearLayout llWebView;
    private int destWidthColorSize;
    private int destHeightColorSize;
    private Toast mToast;
    private WebView mWebView;
    private boolean isClickShopping = false;
    private DataHandler dataHandler;
    private List<TextView> mAttributeViews = new ArrayList<>();
    private ShoppingCarDao mShoppingDao;
    private String shareTitle, shareContent, shareImgurl, shareLink;
    private CustomNestedScrollView myScrollView;
    private ShareUtil share;
    private View llCash, showView;
    private ProductDao mProductDao;
    private MyAccountDao mAccountDao;
    private boolean isOutOfStock = false;
    private ImageView ivProductImage;
    private ArrayList<String> mProductImagesArrayList = new ArrayList<>();
    public OperateProductIdPrecache operateProductIdPrecache;//未登录时点击了wishicon,登陆成功后主动将其添加到wishlist
//    private RecyclerView recycleView;
//    private ProductRecommendedListAdapter recommendedAdapter;
//    private ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> recommendedList = new ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity>();
    //只要是未登录状态下进入其他产品页,登陆后返回至此页面时needRefreshWhenBackPressed会等于true
    private boolean needRefreshWhenBackPressed = false;
    private String mProductFirstImageurl = "";
    private long mStockQty;
    private long mMaxSaleQty;
    private String mFromProductList;

    private WheelPickerConfigEntity mAttributeEntity;

    private ToolBarAlphaBehavior toolBarAlphaBehavior;

    private ImageLoader mImageLoader;
    @Override
    protected void onDestroy() {
        JLogUtils.d(TAG, "onDestroy() ");
        if (dataHandler != null) {
            dataHandler.removeCallbacksAndMessages(null);
        }
        if (mProductDao != null) {
            mProductDao.cancelHttpByTag(TAG);
        }
        if (mShoppingDao != null) {
            mShoppingDao.cancelHttpByTag(TAG);
        }
        if (mAccountDao != null) {
            mAccountDao.cancelHttpByTag(TAG);
        }
        super.onDestroy();
        onDestoryWebView(mWebView);
    }
    public void onDestoryWebView(WebView webView) {
        try {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            try {
                webView.destroy();
            } catch (Throwable ex) {
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    private static class DataHandler extends Handler {
        private final WeakReference<ProductActivity> mActivity;

        public DataHandler(ProductActivity activity) {
            mActivity = new WeakReference<ProductActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            final ProductActivity activity = mActivity.get();
            switch (msg.what) {
                case REQUEST_TOOLBAREXPAN:
                    mActivity.get().appbar_layout.setExpanded(true);
                    break;
                case ProductDao.REQUEST_PRODUCTDETAIL:
                    if (activity.mDialog != null) {
                        activity.mDialog.dismiss();
                    }
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        activity.showView.setVisibility(View.VISIBLE);
                        activity.rlProductQuantity.setVisibility(View.VISIBLE);
                        activity.descriptionsRelative.setVisibility(View.VISIBLE);
                        SVRAppserviceProductDetailReturnEntity productentity = (SVRAppserviceProductDetailReturnEntity) msg.obj;
                        activity.mProductDetailBean = productentity.getResult();
                        activity.initProductDetailUI();
                        if (activity.addProductToWishWhenLoginSuccess(activity.productId)) {
                            activity.addtoWishlistsendRequest();
                        }
                        if (activity.mProductDetailBean.getImages() != null && activity.mProductDetailBean.getImages().size() > 0) {
                            activity.shareImgurl = activity.mProductDetailBean.getImages().get(0);
                        }
                        activity.shareLink = activity.mProductDetailBean.getUrl();
                        activity.share = new ShareUtil(activity, activity.shareTitle, activity.shareContent, activity.shareImgurl, activity.shareLink, "test");
                        activity.trackProductDetail();
                    } else {
                        activity.showView.setVisibility(View.INVISIBLE);
                        activity.rlProductQuantity.setVisibility(View.VISIBLE);
                        activity.descriptionsRelative.setVisibility(View.VISIBLE);
                        String errorMsg = (String) msg.obj;
                        Toast.makeText(activity, errorMsg + "", Toast.LENGTH_LONG).show();
                    }
                    break;
                case ShoppingCarDao.REQUEST_ADDPRODUCT:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        activity.isClickShopping = true;
                        if (activity.mDialog != null) {
                            activity.mDialog.dismiss();
                        }
                        activity.addToCartTrack();
                        Intent intent = new Intent();
                        intent.setClass(activity, ShoppingCartActivity1.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        Bundle bundle = new Bundle();
                        bundle.putLong("mGATrackTimeStart", mActivity.get().mGATrackAddCartTimeStart);
                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent, activity.REQUEST_SHOPPINGCART);
                        activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                    } else {
                        if (activity.mDialog != null) {
                            activity.mDialog.dismiss();
                        }
                        String errorStr = (String) msg.obj;
                        if (!JToolUtils.expireHandler(activity, errorStr, activity.REQUESTCODE_LOGIN)) {
                            Toast.makeText(activity, errorStr + "", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_DELETEWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        WishDelEntityResult wishDelEntityResult = (WishDelEntityResult) msg.obj;
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(activity.getApplicationContext(), wishDelEntityResult.getWishListItemCount());
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTLISTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        try {
                            AddToWishlistEntity entity = (AddToWishlistEntity) msg.obj;
//                            String productId = (String) entity.getParams();
//                            Iterator<SVRAppserviceProductRecommendedResultsItemReturnEntity> iterator = mActivity.get().recommendedList.iterator();
//                            while (iterator.hasNext()) {
//                                SVRAppserviceProductRecommendedResultsItemReturnEntity itemEntity = iterator.next();
//                                if (itemEntity.getProductId().equals(productId)) {
//                                    itemEntity.setIs_like(1);
//                                    int indx = mActivity.get().recommendedList.indexOf(itemEntity);
//                                    mActivity.get().recommendedAdapter.notifyItemChanged(indx);
//                                }
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ProductDao.REQUEST_ADDPRODUCTTOWISH:
                    if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                        AddToWishlistEntity addToWishlistEntity = (AddToWishlistEntity) msg.obj;
                        activity.mProductDetailBean.setItemId(addToWishlistEntity.getItemId());
                        //update wishlist number
                        WhiteLabelApplication.getAppConfiguration().updateWishlist(activity, addToWishlistEntity.getWishListItemCount());
//                        activity.showToast(activity, 2);
                        activity.facebookWishTrack();
                        activity.trackAddWistList();
                    } else {
                        String errorMsg = (String) msg.obj;
                        if (!JToolUtils.expireHandler(activity, errorMsg, activity.REQUESTCODE_LOGIN)) {
                            Toast.makeText(activity, errorMsg + "", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                case ProductDao.REQUEST_ERROR:
                    if (msg.arg1 == ProductDao.REQUEST_PRODUCTRECOMMEND) {
                        return;
                    }
                    if (activity.mDialog != null) {
                        activity.mDialog.dismiss();
                    }
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(activity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void facebookWishTrack(){
        try {
            FacebookEventUtils.getInstance().facebookEventAddedToWistList(this, mProductDetailBean.getId(), userSelectedProductFinalPriceFloat);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    private void trackAddWistList() {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
                    "Add To Wishlist",
                    mProductDetailBean.getName(), Long.valueOf(mProductDetailBean.getId()));
            FirebaseEventUtils.getInstance().ecommerceAddWishlist(this, this.mProductDetailBean.getCategory(), this.mProductDetailBean.getName(),
                    this.mProductDetailBean.getId(), JDataUtils.formatDouble(this.userSelectedProductFinalPriceFloat + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void trackProductDetail() {
        try {
            FacebookEventUtils.getInstance().facebookEventProductDetail(this, this.productId, this.userSelectedProductFinalPriceFloat);
            try {
                FirebaseEventUtils.getInstance().ecommerceViewItem(this, productId, mProductDetailBean.getName()
                        , mProductDetailBean.getCategory(), userSelectedProductQty + "",
                        JDataUtils.formatDouble(userSelectedProductFinalPriceFloat + ""), JDataUtils.formatDouble((userSelectedProductFinalPriceFloat * userSelectedProductQty) + ""));
            } catch (Exception ex) {
                ex.getMessage();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    public void addToCartTrack() {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
                    "Add To Cart",
                    mProductDetailBean.getName(),
                    Long.valueOf(mProductDetailBean.getId()));
            GaTrackHelper.getInstance().googleAnalyticsAddCart(this,
                    productId, mProductDetailBean.getName());
            FacebookEventUtils.getInstance().facebookEventAddedToCart(this, productId, userSelectedProductFinalPriceFloat * userSelectedProductQty);
            FirebaseEventUtils.getInstance().ecommerceAddToCart(this, userSelectedProductQty + "", mProductDetailBean.getCategory(),
                    mProductDetailBean.getName(), mProductDetailBean.getId(),
                    JDataUtils.formatDouble((userSelectedProductFinalPriceFloat * userSelectedProductQty) + ""), JDataUtils.formatDouble(userSelectedProductFinalPriceFloat + ""));
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    @Override
    public boolean addProductToWishWhenLoginSuccess(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        if (operateProductIdPrecache != null && !TextUtils.isEmpty(productId)) {
            if (productId.equals(operateProductIdPrecache.getProductId())) {
                if (operateProductIdPrecache.isAvailable()) {
                    operateProductIdPrecache = null;
                    return true;
                } else {
                    operateProductIdPrecache = null;
                }
            }
        }
        return false;
    }
    @Override
    public void saveProductIdWhenJumpLoginPage(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        operateProductIdPrecache = new OperateProductIdPrecache(productId);
    }
    public void addRecommendedToWishByOperate() {
        //点击wish icon 时跳到登陆页面成功登陆后，推荐商品可直接加入wishList
        if (operateProductIdPrecache != null) {
            String productId = operateProductIdPrecache.getProductId();
            operateProductIdPrecache = null;
            addtoWishlistsendRequestFromProductList(productId);
        }
    }
    @Override
    public void changeOperateProductIdPrecacheStatus(boolean available) {
        if (operateProductIdPrecache != null) {
            operateProductIdPrecache.setAvailable(available);
        }
    }
    private void setWishIconColorToBlank() {
        ivHeaderBarWishlist2.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist2.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
        boolean repeatAnim = true;
        ivHeaderBarWishlist.setTag(repeatAnim);
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
                ivHeaderBarWishlist.setVisibility(View.GONE);
                mIVHeaderBarWishlist.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivHeaderBarWishlist.startAnimation(animation2);
        mIVHeaderBarWishlist.startAnimation(animation2);
    }
    private void setWishIconColorToPurple() {
        ivHeaderBarWishlist2.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist2.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductActivity.this,
                                R.mipmap.wishlist_purple_pressed));
//        ivHeaderBarWishlist.setImageResource(R.mipmap.wishlist_purple_pressed);
        mIVHeaderBarWishlist.setImageResource(R.mipmap.wishlist_white_pressed);

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
        ivHeaderBarWishlist.startAnimation(animation2);
        mIVHeaderBarWishlist.startAnimation(animation2);
    }

    class MyWheelPickerCallback extends WheelPickerCallback {
        private List<SVRAppserviceProductDetailResultPropertyReturnEntity> mPropertyList;
        private int mLevel;
        public MyWheelPickerCallback(int level, List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyList) {
            mLevel = level;
            mPropertyList = propertyList;
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
        }
        @Override
        public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            if (newValue.getDisplay() == null) {
                return;
            }
            ArrayList<String> imgs = new ArrayList<>();
            clearUserSelectedProduct();
            String childProductsaveRM = "";
            String childProductItemsLeft = "";
            long tmpProductMaxSaleQty = 0;
            if (newValue != null && newValue.getIndex() != -1) {
                SVRAppserviceProductDetailResultPropertyReturnEntity entity = null;
                String attributeId = newValue.getValue();
                JLogUtils.i(TAG, "attributeId：" + attributeId);
                JLogUtils.i(TAG, "mLevel：" + mLevel);
                //根据id查找选中的
                for (int i = 0; i < mPropertyList.size(); i++) {
                    if (mPropertyList.get(i).getId().equals(attributeId)) {
                        entity = mPropertyList.get(i);
                        entity.setLevel(mLevel);
                        mAttributeViews.get(mLevel).setText(entity.getLabel());
                        mAttributeViews.get(mLevel).setTag(entity);
                        if (mLevel == mAttributeViews.size() - 1) {
                            userSelectedProductPriceFloat = Float.parseFloat(entity.getPrice());
                            userSelectedProductFinalPriceFloat = Float.parseFloat(entity.getFinalPrice());
                            userSelectedProductInStock = entity.getInStock();
                            userSelectedProductMaxStockQty = entity.getStockQty();
                            childProductsaveRM = entity.getSaveRm();
                            childProductItemsLeft = entity.getItemsLeft();
                            tmpProductMaxSaleQty = entity.getMaxSaleQty();
                            imgs.addAll(entity.getImages());
                        }
                        break;
                    }
                }
                if (mLevel < mAttributeViews.size() - 1) {
                    for (int i = mLevel + 1; i < mAttributeViews.size(); i++) {
                        if (entity != null && entity.getChild() != null && entity.getChild().size() > 0) {
                            entity = entity.getChild().get(0);
                            entity.setLevel(i);
                            mAttributeViews.get(i).setText(entity.getLabel());
                            mAttributeViews.get(i).setTag(entity);
                            if (i == mAttributeViews.size() - 1) {
                                userSelectedProductPriceFloat = Float.parseFloat(entity.getPrice());
                                userSelectedProductFinalPriceFloat = Float.parseFloat(entity.getFinalPrice());
                                userSelectedProductInStock = entity.getInStock();
                                userSelectedProductMaxStockQty = entity.getStockQty();
                                childProductsaveRM = entity.getSaveRm();
                                childProductItemsLeft = entity.getItemsLeft();
                                tmpProductMaxSaleQty = entity.getMaxSaleQty();
                                imgs.addAll(entity.getImages());
                            }
                        }
                    }
                }
                updateProductDetailUIProductImage(imgs);
                updateProductDetailUIProductPriceStock(userSelectedProductPriceFloat + "", userSelectedProductFinalPriceFloat + "", userSelectedProductInStock, userSelectedProductMaxStockQty, tmpProductMaxSaleQty, childProductsaveRM, childProductItemsLeft);
            }
        }
    }
    private void clearUserSelectedProduct() {
        userSelectedProductPriceFloat = 0.0f;
        userSelectedProductFinalPriceFloat = 0.0f;
        userSelectedProductInStock = 0;
        currUserSelectedProductMaxStockQty = 0;
        userSelectedProductMaxStockQty = 0;
        userSelectedProductQty = 1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        mGATrackTimeEnable = true;
        setContentView(R.layout.activity_product);
        initView();
        initToolBar();
        initData();
        getProductInfo();
        initNestedScrollView();
    }
    private void initData() {
        setStatusBarColor(JToolUtils.getColor(R.color.transparent5000));
        TAG = TAG + JTimeUtils.getCurrentTimeLong();
        mImageLoader = new ImageLoader(this);
        dataHandler = new DataHandler(this);
        mProductDao = new ProductDao(TAG, dataHandler);
        mShoppingDao = new ShoppingCarDao(TAG, dataHandler);
        mAccountDao = new MyAccountDao(TAG, dataHandler);
        clearUserSelectedProduct();
        Bundle bundle = getIntent().getExtras();
        productId = bundle.getString("productId");
        mAttributeEntity = new WheelPickerConfigEntity();
        mAttributeEntity.setArrayList(new ArrayList<WheelPickerEntity>());
        mAttributeEntity.setOldValue(new WheelPickerEntity());
        setActivityImageTransition(bundle);
    }
    public void initView(){
//        flSimpleConfig= (FrameLayout) findViewById(R.id.fl_simple_config);
        pcGroupConfig = (ProductChildListView) findViewById(R.id.pc_group_config);
        bpvBindProduct= (BindProductView) findViewById(R.id.bpv_bind_product);
        bpvBindProduct.setOnClickListener(this);
        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) findViewById(R.id.cl_product);
//        productDetailBindTitle= (TextView) findViewById(R.id.tv_bind_title);
//        productDetailBindTitle.setOnClickListener(this);
        coordinatorLayout.setSwitchScroll(false);
        appbar_layout = ((AppBarLayout) findViewById(R.id.appbar_layout));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //去除5.1以上的阴影效果
            appbar_layout.setOutlineProvider(null);
        }
        llWebView = (LinearLayout) findViewById(R.id.ll_webView);
        mWebView = getWebView();
        myScrollView = (CustomNestedScrollView) findViewById(R.id.myScroll);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        llAttribute = (LinearLayout) findViewById(R.id.ll_attribute);
        ivHeaderBarWishlist = (ImageView) findViewById(R.id.ivHeaderBarWishlist);
        mIVHeaderBarWishlist = (ImageView) findViewById(R.id.ivHeaderBarWishlist11);
        ivHeaderBarWishlist2 = (ImageView) findViewById(R.id.ivHeaderBarWishlist2);
        mIVHeaderBarWishlist2 = (ImageView) findViewById(R.id.ivHeaderBarWishlist22);
        ivHeaderBarShare = (ImageView) findViewById(R.id.ivHeaderBarShare);
        ivHeaderBarShare.setImageDrawable(JImageUtils.getThemeIcon(ProductActivity.this,R.mipmap.share_icon_normal));
        ivHeaderBarWishlist.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductActivity.this,
                                R.mipmap.wishlist_purple_normal));
        ivHeaderBarWishlist2.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductActivity.this,
                                R.mipmap.wishlist_purple_normal));
        llCash = findViewById(R.id.ll_cash);
        llCash.setOnClickListener(this);
        ivHeaderBarWishlist.setOnClickListener(this);
        mIVHeaderBarWishlist.setOnClickListener(this);
        ivHeaderBarWishlist2.setOnClickListener(this);
        mIVHeaderBarWishlist2.setOnClickListener(this);
        ivHeaderBarShare.setOnClickListener(this);
        rlProductPrice = (RelativeLayout) findViewById(R.id.rlProductPrice);
        rlProductPrice.requestFocus();
        rlProductPrice.setFocusableInTouchMode(true);
        rlProductPrice.setFocusable(true);
        oldprice = (TextView) findViewById(R.id.old_price);
        price_textview = (TextView) findViewById(R.id.price_textview);
        oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        product_merchant = (TextView) findViewById(R.id.product_merchant);
        ctvProductInStock = (TextView) findViewById(R.id.ctvProductInStock);
        ctvProductOutOfStock = (TextView) findViewById(R.id.ctvProductOutOfStock);
        productUnavailable = (TextView) findViewById(R.id.product_unavailable);
        productTrans = (TextView) findViewById(R.id.product_trans);
//        anim_mask_layout = createAnimLayout();
        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        group = (ViewGroup) findViewById(R.id.viewGroup);
        llBottomBar = (LinearLayout) findViewById(R.id.llBottomBar);
        ctvAddToCart = (CustomTextView) findViewById(R.id.ctvAddToCart);
        mRLAddToWishlistSmall = (RelativeLayout) findViewById(R.id.rl_addtowishlist_small);
        mRLAddToWishlistBig = (RelativeLayout) findViewById(R.id.rl_addtowishlist_big);
        mLLAddToCart = (LinearLayout) findViewById(R.id.ll_addtocart);
//        ctvAddToCart.setOnClickListener(this);
        llBottomBar.setOnClickListener(this);
        textView_num = (TextView) findViewById(R.id.detail_quantity_textview2);
        rlProductQuantity = (RelativeLayout) findViewById(R.id.rlProductQuantity);
        destWidthColorSize = (WhiteLabelApplication.getPhoneConfiguration().getScreenWidth() - (JDataUtils.dp2Px(27))) / 2;
        destHeightColorSize = JDataUtils.dp2Px(37);
        if (rlProductQuantity.getLayoutParams() != null) {
            rlProductQuantity.getLayoutParams().width = destWidthColorSize;
            rlProductQuantity.getLayoutParams().height = destHeightColorSize;
        }
        descriptionsRelative = (RelativeLayout) findViewById(R.id.detail_descriptions_RelativeLayout);
        descriptionsRelative.setVisibility(View.INVISIBLE);
        ctvProductBrand = (CustomTextView) findViewById(R.id.ctvProductBrand);
        ctvProductName = (TextView) findViewById(R.id.ctvProductName);
        tvProductSaverm = (CustomTextView) findViewById(R.id.tv_product_saverm);
        showView = findViewById(R.id.view);
        textView_num.setText("1");
        ctvProductBrand.setOnClickListener(this);
        pcGroupConfig.setOnProductCountChangeListener(new ProductChildListView.OnProductCountChangeListener() {
            @Override
            public void change(int count) {
                if(count==0){
                    setAddCartButtonEnable(false);
                }else{
                    setAddCartButtonEnable(true);
                }
            }
        });
    }

    private void initNestedScrollView() {
        myScrollView.setOnCustomScroolChangeListener(new CustomNestedScrollView.ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                //渐变色.
                if (toolBarAlphaBehavior != null)
                    toolBarAlphaBehavior.onNestedScroll(t);

            }
        });
    }
    private void initToolBar() {
        setTitle("");
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //toolBar变色回调
        toolBarAlphaBehavior = new
                ToolBarAlphaBehavior(getBaseContext(), getToolbar(), WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavBarBackgroundColor(), new ToolBarAlphaBehavior.CallBack() {
            @Override
            public void callBack(int color) {
                //状态bar颜色
                setStatusBarColor(color);
            }
        });
        //初始化toolBar 颜色
        toolBarAlphaBehavior.setAlPha(ToolBarAlphaBehavior.minAlpha);//toolbar透明度初始化
    }

    private void gotoShoppingCartActivity() {
        isClickShopping = true;
        Intent intent = new Intent(ProductActivity.this, ShoppingCartActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, REQUEST_SHOPPINGCART);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        View view = setRightTextMenuClickListener(
                getMenuInflater(),
                R.menu.menu_shopping_cart,
                menu,
                R.id.action_shopping_cart,
                R.layout.item_count, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoShoppingCartActivity();
                    }
                });
        TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(this));
        ImageView ivShopping= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(this,ivShopping,R.drawable.ic_action_cart);
        long cartCount = getCartItemCount();
        if (cartCount > 0 && cartCount <= 99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(cartCount + "");
        } else if (cartCount > 99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("99+");
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shopping_cart:
                gotoShoppingCartActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActivityImageTransition(Bundle bundle) {
        int phoneWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductActivity.this);
        if (!TextUtils.isEmpty(bundle.getString("imageurl"))) {
            ivProductImage.setVisibility(View.VISIBLE);
            mProductFirstImageurl = bundle.getString("imageurl");
            JLogUtils.d(TAG, "mProductFirstImageurl--->" + mProductFirstImageurl);
            int marginLeft = phoneWidth * 15 / 640;
            int dividerWidth = phoneWidth * 16 / 640;
            int destWidth = (phoneWidth - (2 * marginLeft) - dividerWidth) / 2;
            JImageUtils.downloadImageFromServerByUrl(ProductActivity.this, mImageLoader, ivProductImage, mProductFirstImageurl, destWidth, destWidth);
        } else {
            ivProductImage.setAlpha(0.0f);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ivProductImage.setTransitionName(getResources().getString(R.string.activity_image_trans));
            appbar_layout.setExpanded(false);
            dataHandler.sendEmptyMessageDelayed(REQUEST_TOOLBAREXPAN, 450);
        }
        getProductInfoFromIntent(bundle);
    }

    private void getProductInfoFromIntent(Bundle bundle) {
        mFromProductList = getIntent().getExtras().getString("from");
        if (bundle.getSerializable("product_info") != null) {
            ProductListItemToProductDetailsEntity productEntity = (ProductListItemToProductDetailsEntity) bundle.getSerializable("product_info");
            if(!TextUtils.isEmpty(productEntity.getBrand())) {
                ctvProductBrand.setText(productEntity.getBrand().toUpperCase());
            }
            ctvProductName.setText(productEntity.getName());
            if (JDataUtils.compare(Float.parseFloat(productEntity.getFinalPrice()), Float.parseFloat(productEntity.getPrice())) < 0) {
                oldprice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(productEntity.getPrice()));
                rlProductPrice.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                oldprice.setText("");
                rlProductPrice.getLayoutParams().height = 0;
            }
            price_textview.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+ JDataUtils.formatDouble(productEntity.getFinalPrice()));
        }
    }

//    public void refreshProductRecommended() {
//        //推荐商品，随机加载4个
//        // get data
//        String storeId = WhiteLabelApplication.getAppConfiguration().getStoreView().getId();
//        String sessionKey = "";
//        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
//            sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(ProductActivity.this).getSessionKey();
//        }
//        String limit = "4";
//        mProductDao.getProductRecommendList(storeId, limit, productId, sessionKey);
//    }

    public long getCartItemCount() {
        long cartItemCount = 0;
        try {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(this);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(this);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                } else {
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return cartItemCount;
    }
    private boolean isLoad = false;
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * Refress Shoppingcart Number:
         * Condition 1:
         * User has login , get data from UserInfo entity.
         * Condition 2:
         * User do not login , get data from local storage.
         */
        if (isLoad) {
            long cartItemcount = getCartItemCount();
            updateRightIconNum(R.id.action_shopping_cart, cartItemcount);
        }
        if (isClickShopping) {
            isClickShopping = false;
            getProductInfo();
        }
    }
    public void getProductInfo() {
        JLogUtils.d(TAG, "from=" + mFromProductList);
        if (!TextUtils.isEmpty(mFromProductList) && "from_product_list".equals(mFromProductList)) {
            mDialog = JViewUtils.showProgressDialog(ProductActivity.this, CustomDialog.BOOTOM);
        } else {
            mDialog = JViewUtils.showProgressDialog(ProductActivity.this);
        }
        String sessionKey = "";
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
            sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(ProductActivity.this).getSessionKey();
        }
        mProductDao.getProductDetail(productId, sessionKey);
    }
    @Override
    public void onBackPressed() {
        getToolbar().setVisibility(View.GONE);
        if (mProductDetailBean != null) {
            //如果是商品的wish状态发生改变，将isLike作为返回值返回
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("productId", this.productId);
            bundle.putInt("isLike", mProductDetailBean.getIsLike());
            bundle.putString("itemId", mProductDetailBean.getItemId());
            //curation productlist,pdp  接收到needRefreshWhenBackPressed 信息后会刷新页面
            bundle.putBoolean("needRefreshWhenBackPressed", needRefreshWhenBackPressed);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ivProductImage.getDrawable() == null) {
                finishActivity();
            } else {
                ivProductImage.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                ivProductImage.setAlpha(1f);
                transitionOnBackPressed();
            }
        } else {
            finishActivity();
        }
    }

    void finishActivity() {
        finish();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHeaderBarWishlist22:
            case R.id.ivHeaderBarWishlist2:
            case R.id.ivHeaderBarWishlist11:
            case R.id.ivHeaderBarWishlist: {
                if (mProductDetailBean.getIsLike() == 1) {
                    if (!TextUtils.isEmpty(mProductDetailBean.getItemId())) {
                        sendRequestToDeteleteCell(mProductDetailBean.getItemId());
                    }
                } else {
                    addtoWishlistsendRequest();
                }
                break;
            }
            case R.id.ivHeaderBarShare: {
                share.show();
                break;
            }
            case R.id.ivPriceMinus: {
                if (currUserSelectedProductMaxStockQty <= 0) {
                    showNoInventory(ProductActivity.this);
                    return;
                }
                --userSelectedProductQty;
                if (userSelectedProductQty <= 1) {
                    userSelectedProductQty = 1;
                } else if (userSelectedProductQty > currUserSelectedProductMaxStockQty) {
                    userSelectedProductQty = currUserSelectedProductMaxStockQty;
                    showNoInventory(ProductActivity.this);
                }
                textView_num.setText(userSelectedProductQty + "");
                break;
            }
            case R.id.bpv_bind_product:
                Intent bindIntent=new Intent(ProductActivity.this, BindProductActivity.class);
                bindIntent.putExtra(BindProductActivity.EXTRA_PRODUCTID,mProductDetailBean.getId());
                if(mProductDetailBean.getProperty()!=null&&mProductDetailBean.getProperty().size()>0){
                     Bundle  bundle=new Bundle();
                     bundle.putSerializable(BindProductActivity.EXTRA_PRODUCT_DATA,mProductDetailBean.getProperty().get(0));
                     bindIntent.putExtras(bundle);
                }
                startActivity(bindIntent);
                break;
            case R.id.ivPricePlus: {

                if (currUserSelectedProductMaxStockQty <= 0) {
                    showNoInventory(ProductActivity.this);
                    return;
                }
                ++userSelectedProductQty;
                if (userSelectedProductQty <= 1) {
                    userSelectedProductQty = 1;
                } else if (userSelectedProductQty > currUserSelectedProductMaxStockQty) {
                    userSelectedProductQty = currUserSelectedProductMaxStockQty;
                    showNoInventory(ProductActivity.this);
                }
                textView_num.setText(userSelectedProductQty + "");
                break;
            }
            case R.id.ll_addtocart: {
                if (!isOutOfStock) {
                    if (userSelectedProductQty > 0 && mProductDetailBean != null && !JDataUtils.isEmpty(mProductDetailBean.getId())) {
                        //                    JViewUtils.showProgressBar(ProductActivity.this);
                        addToCartSendRequest();
                    }
                } else {
                    if (mProductDetailBean.getIsLike() == 1) {
                        if (!TextUtils.isEmpty(mProductDetailBean.getItemId())) {
                            sendRequestToDeteleteCell(mProductDetailBean.getItemId());
                        }
                    } else {
                        addtoWishlistsendRequest();
                    }
                }
                break;
            }
            case R.id.ll_cash:
                Intent intent = new Intent(ProductActivity.this, PaymentHelpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            case R.id.ctv_detailDelivery1:
            case R.id.ctv_detailDetailDelivery2:
                Bundle bundle = new Bundle();
                bundle.putString("title", "SHIPPING DELIVERY");
                bundle.putString("content", "shipping-delivery");
                startNextActivity(bundle, HelpCenterDetialActivity.class, false);
                break;
            case R.id.ctv_product_attribute:
                //根据Tag中的属性ID去查找当前属性集合
                try {
                    SVRAppserviceProductDetailResultPropertyReturnEntity propertyReturnEntitys = (SVRAppserviceProductDetailResultPropertyReturnEntity) v.getTag();
                    List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyList = mProductDetailBean.getProperty();
                    WheelPickerEntity oldEntity = mAttributeEntity.getOldValue();
                    propertyList = getSvrAppserviceProductDetailResultPropertyReturnEntities(propertyReturnEntitys, propertyList);
                    for (int i = 0; i < propertyList.size(); i++) {
                        if (propertyList.get(i).getId().equals(propertyReturnEntitys.getId())) {
                            oldEntity.setIndex(i);
                            mAttributeEntity.setIndex(i);
                        }
                    }
                    mAttributeEntity.setOldValue(oldEntity);
                    mAttributeEntity.setCallBack(new MyWheelPickerCallback(propertyReturnEntitys.getLevel(), propertyList));
                    showWheelDialog(propertyList, mAttributeViews.get(propertyReturnEntitys.getLevel()).getText().toString());
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
                break;
            case R.id.ctvProductBrand:
                if(mProductDetailBean!=null) {
                    if(!"0".equals(mProductDetailBean.getBrandId())) {
                        Bundle brandStoreIntent = new Bundle();
                        brandStoreIntent.putString(BrandStoreFontActivity.EXTRA_BRAND_ID, mProductDetailBean.getBrandId());
                        brandStoreIntent.putString(BrandStoreFontActivity.EXTRA_BRAND_NAME, mProductDetailBean.getBrand().toUpperCase());
                        startNextActivity(brandStoreIntent, BrandStoreFontActivity.class, false);
                    }else{
                        Intent intent1=new Intent(this, HomeActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        finish();
                    }
                }
                break;
        }
    }
    private void showWheelDialog(List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyList, String currAttributeValue) {
        mAttributeEntity.getArrayList().clear();
        WheelPickerEntity oldEntity = mAttributeEntity.getOldValue();
        ArrayList<WheelPickerEntity> list = mAttributeEntity.getArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDisplay().equalsIgnoreCase(currAttributeValue)) {
                oldEntity.setIndex(i);
                mAttributeEntity.setIndex(i);
            }
        }
        for (int i = 0; i < propertyList.size(); i++) {
            SVRAppserviceProductDetailResultPropertyReturnEntity firstProperty = propertyList.get(i);
            WheelPickerEntity mAttributePickerEntity = new WheelPickerEntity();
            mAttributePickerEntity.setIndex(i);
            mAttributePickerEntity.setValue(firstProperty.getId());
            mAttributePickerEntity.setDisplay(firstProperty.getLabel());
            mAttributeEntity.getArrayList().add(mAttributePickerEntity);
        }
        mAttributeEntity.setOldValue(oldEntity);
        JViewUtils.showWheelPickerOneDialog(this, mAttributeEntity);
    }

    private List<SVRAppserviceProductDetailResultPropertyReturnEntity> getSvrAppserviceProductDetailResultPropertyReturnEntities(SVRAppserviceProductDetailResultPropertyReturnEntity propertyReturnEntitys, List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyList) {
        for (int i = 0; i < propertyReturnEntitys.getLevel(); i++) {
            SVRAppserviceProductDetailResultPropertyReturnEntity currProperty = (SVRAppserviceProductDetailResultPropertyReturnEntity) mAttributeViews.get(i).getTag();
            for (int z = 0; z < propertyList.size(); z++) {
                if (currProperty.getId().equals(propertyList.get(z).getId())) {
                    propertyList = propertyList.get(z).getChild();
                    break;
                }
            }
        }
        return propertyList;
    }
//    public boolean checkIsHaveStock() {
//        if (mMaxSaleQty > mStockQty) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public void changeViewPagerIndex(int flag) {
//        int currentIndex = viewPager.getCurrentItem();
//        int currentTotalImage = 0;
//        if (mProductImageView != null && mProductImageView.size() > 0) {
//            currentTotalImage = mProductImageView.size();
//        }
//        if (flag == 1) {
//            currentIndex++;
//            if (currentIndex >= currentTotalImage) {
//                currentIndex = currentTotalImage;
//            }
//        } else {
//            currentIndex--;
//            if (currentIndex <= 0) {
//                currentIndex = 0;
//            }
//        }
//        viewPager.setCurrentItem(currentIndex);
//    }
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
    @Override
    public void onPageSelected(int arg0) {
//        当滑动ViewPager是让顶层的ImagView消失
        ivProductImage.setAlpha(0.0f);
        if (mProductImageView != null && mProductImageView.size() > 0 && mProductImageView.size() > arg0) {
            setImageBackground(arg0 % mProductImageView.size());
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && mProductImagesArrayList.size() >= 1) {
            int destWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductActivity.this);
            JLogUtils.d(TAG, "size=" + mProductImagesArrayList.size() + "-----------rul=" + mProductImagesArrayList.get(arg0 % mProductImagesArrayList.size()));
            JImageUtils.downloadImageFromServerByUrl(ProductActivity.this, mImageLoader, ivProductImage, mProductImagesArrayList.get(arg0 % mProductImagesArrayList.size()), destWidth, destWidth);
        }
    }
    private void setImageBackground(int selectItems) {
        if (mProductImageViewTips == null || selectItems < 0 || mProductImageViewTips.size() <= selectItems) {
            return;
        }
        for (int index = 0; index < mProductImageViewTips.size(); index++) {
            if (index == selectItems) {
//                mProductImageViewTips.get(index).setBackgroundResource(R.mipmap.doc_checked);
                mProductImageViewTips.get(index).setBackground(JImageUtils.getThemeCircle(this));
            } else {
                mProductImageViewTips.get(index).setBackgroundResource(R.mipmap.dot_unchecked);
            }
        }
    }
    private void initProductDetailUI() {
        initProductDetailUIStaticContent();
        initProductDetailUIDynamicContent();
        if (mGATrackTimeEnable) {
            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, mGATrackTimeStart, "PDP Loading"
            );
            mGATrackTimeEnable = false;
        }
    }
    private void initProductDetailUIStaticContent() {
        if (mProductDetailBean == null) {
            return;
        }
        if(!TextUtils.isEmpty(mProductDetailBean.getBrand())) {
            ctvProductBrand.setText(mProductDetailBean.getBrand().toUpperCase());
        }
        ctvProductName.setText(mProductDetailBean.getName());//分享标题赋值
        if (!TextUtils.isEmpty(mProductDetailBean.getVendorDisplayName())) {
            String Sold_fulfilled_by = product_merchant.getContext().getResources().getString(R.string.soldfulby);
            if (!TextUtils.isEmpty(mProductDetailBean.getVendor_id())) {
                product_merchant.setTextColor(ProductActivity.this.getResources().getColor(R.color.black000000));
                SpannableStringBuilder ss = new SpannableStringBuilder(Sold_fulfilled_by + " " + mProductDetailBean.getVendorDisplayName());
                ss.setSpan(new ForegroundColorSpan(ProductActivity.this.getResources().getColor(R.color.greyB8B8B8)), 0, Sold_fulfilled_by.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                product_merchant.setText(ss);
                if (!"0".equals(mProductDetailBean.getVendor_id())) {
                    product_merchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ProductActivity.this, MerchantStoreFrontActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_ID, mProductDetailBean.getVendor_id());
                            bundle.putString(MerchantStoreFrontActivity.BUNDLE_VENDOR_DISPLAY_NAME, mProductDetailBean.getVendorDisplayName());
                            intent.putExtras(bundle);
                            ProductActivity.this.startActivity(intent);
                            ProductActivity.this.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    });
                } else {
//                    product_merchant.setText(Sold_fulfilled_by + " " + mProductDetailBean.getVendorDisplayName());
//                    product_merchant.setTextColor(ProductActivity.this.getResources().getColor(R.color.purple92018d));
                    product_merchant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ProductActivity.this, HomeActivity.class);
                            ProductActivity.this.startActivity(i);
                            ProductActivity.this.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    });
                }

            } else {
                product_merchant.setText(Sold_fulfilled_by + " " + mProductDetailBean.getVendorDisplayName());
                product_merchant.setTextColor(ProductActivity.this.getResources().getColor(R.color.greyB8B8B8));
            }

        } else {
            product_merchant.setText("");
        }
        shareTitle = mProductDetailBean.getName();
        //get ShoppingInfo text
        String htmlText=initShippingInfoHtmlText();
        ArrayList<SVRAppserviceProductDetailResultDetailReturnEntity> arrayList = mProductDetailBean.getDetail();
        if (arrayList != null && arrayList.size() > 0) {
            StringBuilder stringBuilder=new StringBuilder("<h3 class=\"text1\" ><B>PRODUCT DETAILS</B></h3>");
            for (int index = 0; index < arrayList.size(); ++index) {
                SVRAppserviceProductDetailResultDetailReturnEntity productdetailitem = arrayList.get(index);
                if (productdetailitem != null) {
                    if ("productDimension".equals(productdetailitem.getCode())) {
                        stringBuilder.append(getProductDimenSionV2Html(productdetailitem.getValueArray()));
                        continue;
                    }
                    String label=productdetailitem.getLabel();
                    if(!TextUtils.isEmpty(label)){
                        label="<B class=\"text1\" >"+label+"</B><br> ";
                    }
                    stringBuilder.append(label+productdetailitem.getValue()+"<br><br>");
                }
            }
            htmlText+=stringBuilder.toString();
        }
        //将存在的特殊字符替换成空格或空
        htmlText = htmlText.replaceAll("\u009D", "");
        htmlText = htmlText.replace("<br />\r\n<br />\r\n", "<br>\r\n");
        htmlText = htmlText.replace("<br />\n<br />\n", "<br>\r\n");
        htmlText = htmlText.replace("\n", "<br>");
        htmlText = htmlText.replace("\u2028", " ");
        if (!TextUtils.isEmpty(htmlText)) {
            //webwiew font default 13.5px
            JToolUtils.webViewFont(this, mWebView, htmlText, 13.5f);
        }
        int webviewCount=llWebView.getChildCount();
        if(webviewCount<1) {
            llWebView.addView(mWebView);
        }
    }
    //dimension需放到table表里显示
    private String getProductDimenSionV2Html(ArrayList<?> arrayList) {
        try {
            StringBuilder stringBuild = new StringBuilder("");
            if (arrayList != null && arrayList.size() > 0) {
                stringBuild.append(" <table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">   ");

                for (int i = 1; i <= arrayList.size(); i++) {
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) arrayList.get(i - 1);
                    String dimenTitle = (String) linkedTreeMap.get("title");
                    String dimenValue = (String) linkedTreeMap.get("value");
                    linkedTreeMap.get("title");
                    if (i % 2 == 1) {
                        stringBuild.append("   <tr>");
                    }
                    stringBuild.append("  <td> <strong>$title$</strong>: $value$&nbsp;&nbsp;&nbsp;</td>");

                    if (i % 2 == 0) {
                        stringBuild.append(" </tr>");
                    } else if (i == arrayList.size()) {
                        stringBuild.append(" </tr>");
                    }
                    String a = stringBuild.toString().replace("$title$", dimenTitle);
                    String b = a.replace("$value$", dimenValue);
                    stringBuild = new StringBuilder(b);
                }
                stringBuild.append(" </table> <br>  ");
            }
            return stringBuild.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public String initShippingInfoHtmlText() {
        try {
            StringBuilder sb = new StringBuilder("");
            if (mProductDetailBean.getShippingInfo() != null) {
                sb.append("<h3 class=\"text1\" ><B>SHIPPING INFO</B></h3>");
                if (!TextUtils.isEmpty(mProductDetailBean.getShippingInfo().getWestDeliversDays())) {
                    sb.append(mProductDetailBean.getShippingInfo().getWestDeliversDays() + "<br>");
                }
                if (!TextUtils.isEmpty(mProductDetailBean.getShippingInfo().getEastDeliversDays())) {
                    sb.append(mProductDetailBean.getShippingInfo().getEastDeliversDays() + "<br>");
                }
                if (!TextUtils.isEmpty(mProductDetailBean.getShippingInfo().getLocationNotDelivered())) {
                    sb.append(mProductDetailBean.getShippingInfo().getLocationNotDelivered() + "<br>");
                }
                String detailDelivery1 = mProductDetailBean.getShippingInfo().getDetailDelivery1();
                if (!TextUtils.isEmpty(detailDelivery1)) {
                    sb.append(detailDelivery1 + "<br>");
                }

                String detailDelivery2 = mProductDetailBean.getShippingInfo().getDetailDelivery2();
                if (!TextUtils.isEmpty(detailDelivery2)) {
                    detailDelivery2 = detailDelivery2.replace("<li>","");
                    detailDelivery2 = detailDelivery2.replace("</li>","");
                    sb.append(detailDelivery2 + "<br>");
                }
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
//    private WebView wbProductDetail;

    private WebView getWebView() {
        WebView webView = new WebView(getApplicationContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);//缩放
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDisplayZoomControls(false);
        return webView;
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        try {
            int start = strBuilder.getSpanStart(span);
            int end = strBuilder.getSpanEnd(span);
            int flags = strBuilder.getSpanFlags(span);
            ClickableSpan clickable = new ClickableSpan() {
                public void onClick(View view) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("helpCenter", 5);
                    startNextActivity(bundle2, RegisterToHelpCenter.class, false);
                }
            };
            strBuilder.removeSpan(span);
            strBuilder.setSpan(clickable, start, end, flags);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    private void initProductDetailUIDynamicContent() {
        if (mProductDetailBean == null) {
            return;
        }
        clearUserSelectedProduct();
        ArrayList<String> productImagesArrayList = new ArrayList<>();
        if (ProductDetailModel.TYPE_SIMPLE.equals(mProductDetailBean.getType())) {
            // Product ImageList
            //===============================================================================================================================
            setCashLayout(mProductDetailBean.getEligibleForCod());
            //===============================================================================================================================
            if (mProductDetailBean.getImages() != null && mProductDetailBean.getImages().size() > 0) {
                productImagesArrayList.addAll(mProductDetailBean.getImages());
            }
            updateProductDetailUIProductImage(productImagesArrayList);
            updateProductDetailUIProductPriceStock("0", "0", mProductDetailBean.getInStock(),
                    mProductDetailBean.getStockQty(), mProductDetailBean.getMaxSaleQty(),
                    mProductDetailBean.getSaveRm(), mProductDetailBean.getItemsLeft());
            if (!TextUtils.isEmpty(mProductDetailBean.getSaveRm())) {
                tvProductSaverm.setVisibility(View.VISIBLE);
                tvProductSaverm.setText("(" + mProductDetailBean.getSaveRm() + ")");
            } else {
                tvProductSaverm.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mProductDetailBean.getItemsLeft())) {
                if (getResources().getString(R.string.product_detail_instock).equals(ctvProductInStock.getText().toString())) {
                    ctvProductInStock.setText(ctvProductInStock.getText().toString() + " (" + mProductDetailBean.getItemsLeft() + ")");
                } else {
                    ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock) + " (" + mProductDetailBean.getItemsLeft() + ")");
                }
            } else {
                ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock));
            }
        } else if (ProductDetailModel.TYPE_CONFIGURABLE.equals(mProductDetailBean.getType())) {
            ArrayList<SVRAppserviceProductDetailResultPropertyReturnEntity> productPropertyList = mProductDetailBean.getProperty();
            if (productPropertyList == null || productPropertyList.size() <= 0) {
                // Product ImageList
                if (mProductDetailBean.getImages() != null && mProductDetailBean.getImages().size() > 0) {
                    productImagesArrayList.addAll(mProductDetailBean.getImages());
                }
                updateProductDetailUIProductImage(productImagesArrayList);
                updateProductDetailUIProductPriceStock("0", "0", mProductDetailBean.getInStock(), mProductDetailBean.getStockQty(), mProductDetailBean.getMaxSaleQty(), "",
                        mProductDetailBean.getItemsLeft());
            }else {
                llAttribute.removeAllViews();
                mAttributeViews.clear();
                createAttributeView(0, mProductDetailBean.getProperty().get(0));
            }
        }else if(ProductDetailModel.TYPE_GROUP.equals(mProductDetailBean.getType())){
            showView.setVisibility(View.GONE);
            pcGroupConfig.setVisibility(View.VISIBLE);
            pcGroupConfig.initProductChildListView(mProductDetailBean.getProperty());
            boolean instock=false;
            if(mProductDetailBean.getImages()!=null) {
                productImagesArrayList.addAll(mProductDetailBean.getImages());
            }
            for(SVRAppserviceProductDetailResultPropertyReturnEntity bean:mProductDetailBean.getProperty() ){
                if(bean.getInStock()==1){
                    instock=true;
                    break;
                }
            }
            if(instock) {
                mRLAddToWishlistSmall.setVisibility(View.GONE);
                mRLAddToWishlistBig.setVisibility(View.VISIBLE);
                ctvAddToCart.setText(getString(R.string.product_detail_addtocart));
                setAddCartButtonEnable(false);
            }else{
                outOfStockToWishlist();
            }
            updateProductDetailUIProductImage(productImagesArrayList);
        }
        List<SVRAppserviceProductDetailResultPropertyReturnEntity>  products=mProductDetailBean.getRelatedProducts();
//        findViewById(R.id.ll_bind_product).setVisibility(View.VISIBLE);
        if(products!=null&&products.size()>0) {
            bpvBindProduct.initData(products, mImageLoader);
        }else{
            bpvBindProduct.setVisibility(View.GONE);
        }
        initVisibleProduct();
    }
    public void createAttributeView(int level, SVRAppserviceProductDetailResultPropertyReturnEntity bean) {
        if (level % 2 == 0) {
            llLayout = new LinearLayout(this);
            llLayout.setOrientation(LinearLayout.HORIZONTAL);
            llLayout.setWeightSum(2);
            llAttribute.addView(llLayout);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.item_product_selected_attribute, null);
        llLayout.addView(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = JDataUtils.dp2px(this, 37);
        params.width = 0;
        params.weight = 1;
        params.setMargins(0, 0, JDataUtils.dp2px(this, 10), 0);
        view.setLayoutParams(params);
        TextView tvAttribute = (TextView) view.findViewById(R.id.ctv_product_attribute);
        mAttributeViews.add(tvAttribute);
        bean.setLevel(level);
        tvAttribute.setTag(bean);
        tvAttribute.setOnClickListener(this);
        tvAttribute.setText(bean.getLabel());
        if (bean.getChild() != null && bean.getChild().size() > 0) {
            createAttributeView((level + 1), bean.getChild().get(0));
        } else {
            userSelectedProductPriceFloat = Float.parseFloat(bean.getPrice());
            userSelectedProductFinalPriceFloat = Float.parseFloat(bean.getFinalPrice());
            userSelectedProductInStock = bean.getInStock();
            userSelectedProductMaxStockQty = bean.getStockQty();
            String childProductsaveRM = bean.getSaveRm();
            String childProductItemsLeft = bean.getItemsLeft();
            long tmpProductMaxSaleQty = bean.getMaxSaleQty();
            if (bean.getImages() != null && bean.getImages().size() > 0) {
//                userSelectedProductThumbnail = bean.getImages().get(0);
                updateProductDetailUIProductImage(bean.getImages());
            }
            updateProductDetailUIProductPriceStock(userSelectedProductPriceFloat + "", userSelectedProductFinalPriceFloat + "", userSelectedProductInStock, userSelectedProductMaxStockQty, tmpProductMaxSaleQty, childProductsaveRM, childProductItemsLeft);
        }
    }
    private final static  int BOTTONBAR_HEIGHT=80;

    //    currUserSelectedProductMaxStockQty - count > 0
    public void initVisibleProduct() {
        isLoad = true;
        if ("0".equals(mProductDetailBean.getVisibility())) {
            ivHeaderBarShare.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivHeaderBarShare.getLayoutParams();
            params.width = 20;
            ivHeaderBarShare.setLayoutParams(params);
            ivHeaderBarWishlist.setVisibility(View.VISIBLE);
            mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams) ivHeaderBarWishlist.getLayoutParams();
            param1.width = 0;
            ivHeaderBarWishlist.setLayoutParams(param1);
            RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams) mIVHeaderBarWishlist.getLayoutParams();
            param1.width = 0;
            mIVHeaderBarWishlist.setLayoutParams(param2);
            llBottomBar.setVisibility(View.GONE);
//            rlProductSizeColor.setVisibility(View.GONE);
            rlProductQuantity.setVisibility(View.GONE);
            ctvProductInStock.setVisibility(View.INVISIBLE);
            ctvProductOutOfStock.setVisibility(View.INVISIBLE);
        } else {
            ivHeaderBarShare.setVisibility(View.VISIBLE);
            ivHeaderBarWishlist.setVisibility(View.VISIBLE);
            mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
            if (mProductDetailBean.getIsLike() == 1) {
                ivHeaderBarWishlist.setImageDrawable
                        (JImageUtils.getThemeIcon
                                (ProductActivity.this,
                                        R.mipmap.wishlist_purple_pressed));
                mIVHeaderBarWishlist.setImageResource(R.mipmap.wishlist_white_pressed);
            } else {

                ivHeaderBarWishlist.setImageDrawable
                        (JImageUtils.getThemeIcon
                                (ProductActivity.this,
                                        R.mipmap.wishlist_purple_normal));
                mIVHeaderBarWishlist.setImageResource(R.mipmap.wishlist_white_normal);
            }
            //availability and visibility
            if (!TextUtils.isEmpty(mProductDetailBean.getAvailability())) {
                if (("1").equals(mProductDetailBean.getAvailability())) {
                    ivHeaderBarWishlist.setVisibility(View.VISIBLE);
                    ivHeaderBarShare.setVisibility(View.VISIBLE);
                    llBottomBar.setVisibility(View.VISIBLE);
                    setBottonBarHeight(BOTTONBAR_HEIGHT);
                    JLogUtils.i(TAG, "111currUserSelectedProductMaxStockQty:" + currUserSelectedProductMaxStockQty);
                    if (1 == userSelectedProductInStock && currUserSelectedProductMaxStockQty > 0) {
                        rlProductQuantity.setVisibility(View.VISIBLE);
                    } else {
                        rlProductQuantity.setVisibility(View.GONE);
                    }
                    productUnavailable.setVisibility(View.GONE);
                    llAttribute.setVisibility(View.VISIBLE);
                    productTrans.setVisibility(View.GONE);
                } else {
                    rlProductQuantity.setVisibility(View.GONE);
                    ivHeaderBarWishlist.setVisibility(View.GONE);
                    ivHeaderBarShare.setVisibility(View.GONE);
                    llBottomBar.setVisibility(View.GONE);
                    ctvProductInStock.setVisibility(View.GONE);
                    ctvProductOutOfStock.setVisibility(View.GONE);
                    productTrans.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    productUnavailable.setVisibility(View.VISIBLE);
                    llAttribute.setVisibility(View.GONE);
                    productTrans.setVisibility(View.VISIBLE);
                }

            } else {
                productUnavailable.setVisibility(View.GONE);
                llAttribute.setVisibility(View.VISIBLE);
                productTrans.setVisibility(View.GONE);
            }

        }
    }
    public void setCashLayout(int c) {
        if (c == 1) {
            llCash.setVisibility(View.VISIBLE);
        } else {
            llCash.setVisibility(View.GONE);
        }
    }
    private Map<String, ImageView> cacheImageMap = new HashMap<String, ImageView>();

    private void updateProductDetailUIProductImage(ArrayList<String> productImageUrlList) {
        if (mProductImagesArrayList != null || mProductImagesArrayList.size() > 0) {
            mProductImagesArrayList.clear();
        }
        mProductImagesArrayList.addAll(productImageUrlList);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        if (mProductFirstImageurl != null && productImageUrlList.size() > 0) {
            if (!mProductFirstImageurl.equals(productImageUrlList.get(0))) {
                int destWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductActivity.this);
                JImageUtils.downloadImageFromServerByUrl(ProductActivity.this, mImageLoader, ivProductImage, productImageUrlList.get(0), destWidth, destWidth);
                mProductFirstImageurl = "";
            }
        }
        if (mProductImageView != null) {
            mProductImageView.clear();
        } else {
            mProductImageView = new ArrayList<>();
        }
        if (mProductImageViewTips != null) {
            mProductImageViewTips.clear();
        } else {
            mProductImageViewTips = new ArrayList<>();
        }
        group.removeAllViews();
        final int destWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductActivity.this);
        int destHeight = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductActivity.this);
        //加載 圖片
        if (productImageUrlList != null) {
            for (int index = 0; index < productImageUrlList.size(); index++) {
                String md5Key = JDataUtils.getMD5Result(productImageUrlList.get(index));
                final ImageView imageView;
                if (cacheImageMap.containsKey(md5Key) && cacheImageMap.get(md5Key) != null) {
                    imageView = cacheImageMap.get(md5Key);
                } else {
                    imageView = new ImageView(this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    JImageUtils.downloadImageFromServerByUrl(ProductActivity.this, mImageLoader, imageView, productImageUrlList.get(index), destWidth, destHeight);

                    if (md5Key != null || !"".equals(md5Key)) {
                        cacheImageMap.put(md5Key, imageView);
                    }
                }
                JLogUtils.i("share", "img==" + productImageUrlList.size() + "---" + productImageUrlList.get(index));
                /**
                 * Give images clicking effect
                 */
                imageView.setOnClickListener(new View.OnClickListener() {

                    private int index;
                    private ArrayList<String> list;

                    public View.OnClickListener init(int index, ArrayList<String> list) {
                        this.index = index;
                        this.list = list;
                        return this;
                    }
                    @Override
                    public void onClick(View v) {
//                        startActivityToBigPictureDetail(index,list);
                        Intent intent = new Intent();
                        intent.setClass(ProductActivity.this, ProductDetailPictureActivity.class);
                        Bundle bundle = new Bundle();
                        JLogUtils.d(TAG, "currrrentItem--->" + viewPager.getCurrentItem() + "---" + mProductImageView.size());
                        if (ivProductImage.getDrawable() == null) {
                            bundle.putInt("currentIndex", index);
                            bundle.putStringArrayList("pictures", list);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                        } else {
                            bundle.putInt("currentIndex", index);
                            bundle.putStringArrayList("pictures", list);
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                intent.putExtras(bundle);
                                ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(ProductActivity.this, ivProductImage, ProductActivity.this.getString(R.string.activity_image_trans));
                                ActivityCompat.startActivityForResult(ProductActivity.this, intent, PRODUCT_PICTURE_REQUEST_CODE, aop.toBundle());
                            } else {
                                intent.putExtras(bundle);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                            }
                        }
                    }
                }.init(index, productImageUrlList));

                mProductImageView.add(imageView);
                ImageView imageViewTips = new ImageView(this);
                try {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                    lp.setMargins(5, 0, 5, 0);
                    imageViewTips.setLayoutParams(lp);
                } catch (Exception ex) {
                    JLogUtils.e(TAG, "initProductDetailUIDynamicContent", ex);
                }
                if (index == 0) {
                    imageViewTips.setBackground(JImageUtils.getThemeCircle(this));
                } else {
                    imageViewTips.setBackgroundResource(R.mipmap.dot_unchecked);
                }
                group.addView(imageViewTips);
                mProductImageViewTips.add(imageViewTips);
            }

        }
        viewPager.setAdapter(new MyAdapter());
        JLogUtils.i("ray","mProductImageViewTips:"+mProductImageViewTips.size());
        if (mProductImageViewTips.size() == 1) {
            group.setVisibility(View.INVISIBLE);
        } else {
            group.setVisibility(View.VISIBLE);
        }
        viewPager.addOnPageChangeListener(this);

        if (mProductImageView != null && mProductImageView.size() > 0) {
            viewPager.setCurrentItem(0);
//            viewPager.setOffscreenPageLimit(mProductImageView.size());
        }

        if (viewPager != null && viewPager.getLayoutParams() != null) {
            viewPager.getLayoutParams().height = destHeight;
            viewPager.getLayoutParams().width = destWidth;
        }
    }
    public void setAddCartButtonEnable(boolean enable){
        if(enable){
            ctvAddToCart.setText(getString(R.string.product_detail_addtocart));
            ctvAddToCart.setEnabled(true);
            mLLAddToCart.setEnabled(true);
//            JViewUtils.setSoildButtonGlobalStyle(this,mLLAddToCart);
            mLLAddToCart.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));

        }else{
            ctvAddToCart.setEnabled(false);
            mLLAddToCart.setEnabled(false);
            mLLAddToCart.setBackgroundResource(R.drawable.big_button_style_b8);
        }
    }



    private void updateProductDetailUIProductPriceStock(String price, String finalPrice, int instock, long stockqty, long maxSaleQty, String saveRM, String itemsLeft) {
        mStockQty = stockqty;
        mMaxSaleQty = maxSaleQty;
        // Price
        float childPrice = 0.0f;
        try {
            childPrice = Float.parseFloat(price);
        } catch (Exception ex) {
        }
        float childFinalPrice = 0.0f;
        try {
            childFinalPrice = Float.parseFloat(finalPrice);
        } catch (Exception ex) {
        }
//        userSelectedProductPriceOffsetFloat = 0;
//        userSelectedProductFinalPriceOffsetFloat = 0;
        float productPriceFloat = 0.0f;
        float productFinalPriceFloat = 0.0f;
        try {
            productPriceFloat = Float.parseFloat(mProductDetailBean.getPrice());
        } catch (Exception ex) {
        }
        try {
            productFinalPriceFloat = Float.parseFloat(mProductDetailBean.getFinalPrice());
        } catch (Exception ex) {
        }
        if (childPrice != 0) {
            userSelectedProductPriceFloat = childPrice;
        } else {
            userSelectedProductPriceFloat = productPriceFloat;
        }
        if (childFinalPrice != 0) {
            userSelectedProductFinalPriceFloat = childFinalPrice;
        } else {
            userSelectedProductFinalPriceFloat = productFinalPriceFloat;
        }

        if (JDataUtils.compare(userSelectedProductFinalPriceFloat, userSelectedProductPriceFloat) < 0) {
            oldprice.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble((userSelectedProductPriceFloat+"")));
            rlProductPrice.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            oldprice.setText("");
            rlProductPrice.getLayoutParams().height = 0;
        }
        price_textview.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName() + " " + JDataUtils.formatDouble((userSelectedProductFinalPriceFloat) + ""));
        if (TextUtils.isEmpty(saveRM)) {
            tvProductSaverm.setVisibility(View.GONE);
        } else {
            tvProductSaverm.setVisibility(View.VISIBLE);
            tvProductSaverm.setText("(" + saveRM + ")");
        }
        // Stock
        userSelectedProductInStock = instock;
        isOutOfStock = false;
        //if (0 == userSelectedProductInStock||"0"==mProductDetailBean.getAvailability()) { // out of stock
        JLogUtils.i(TAG, "userSelectedProductInStock:" + userSelectedProductInStock);
        if (0 == userSelectedProductInStock) {
            rlProductQuantity.setVisibility(View.GONE);
            setAddCartButtonEnable(false);
            RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
            if (bottomBarLp != null) {
                bottomBarLp.height = 0;
                llBottomBar.setLayoutParams(bottomBarLp);
            }
            currUserSelectedProductMaxStockQty = 0l;
            userSelectedProductMaxStockQty = 01;
            userSelectedProductQty = 0;
            ctvProductInStock.setVisibility(View.INVISIBLE);
            ctvProductOutOfStock.setVisibility(View.VISIBLE);
            textView_num.setText("" + userSelectedProductQty);
            outOfStockToWishlist();
        } else if (1 == userSelectedProductInStock) { // in stock
            mRLAddToWishlistSmall.setVisibility(View.GONE);
            mRLAddToWishlistBig.setVisibility(View.VISIBLE);
            setAddCartButtonEnable(true);
            RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
//            ctvAddToCart.setEnabled(true);
//            mLLAddToCart.setEnabled(true);
//            ctvAddToCart.setBackgroundResource(R.drawable.big_button_style_purple);
//            mLLAddToCart.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));

//            List<SVRAppserviceProductDetailResultPropertyReturnEntity> attributeIds=new ArrayList<>();
//            for(int i=0;i<mAttributeViews.size();i++){
//                attributeIds.add((SVRAppserviceProductDetailResultPropertyReturnEntity) mAttributeViews.get(i).getTag());
//            }
            long count = getProductCount();
            JLogUtils.d(TAG, "maxSaleQty=" + maxSaleQty + "-------------stockqty=" + stockqty);
//            TODO MAXQTY
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
                userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                currUserSelectedProductMaxStockQty = stockqty; //判断加减的时候使用到的
            } else {
                if (maxSaleQty > 0) {
                    userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                    currUserSelectedProductMaxStockQty = stockqty; //判断加减的时候使用到的
                } else {

                    userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                    currUserSelectedProductMaxStockQty = stockqty; //判断加减的时候使用到的
                }
            }
            JLogUtils.d(TAG, "currUserSelectedProductMaxStockQty=" + (currUserSelectedProductMaxStockQty - count));
            userSelectedProductQty = 1;
            if (currUserSelectedProductMaxStockQty - count > 0) {
                if (bottomBarLp != null) {
                    //此處  48dp 與 dimens 的button_touch_height關聯，必須同時修改
                    bottomBarLp.height = JDataUtils.dp2Px(48 + 32);
                    llBottomBar.setLayoutParams(bottomBarLp);
                }
                currUserSelectedProductMaxStockQty = currUserSelectedProductMaxStockQty - count;
                ctvProductInStock.setVisibility(View.VISIBLE);
                rlProductQuantity.setVisibility(View.VISIBLE);
                ctvProductOutOfStock.setVisibility(View.INVISIBLE);
                textView_num.setText("" + userSelectedProductQty);
                if (!TextUtils.isEmpty(itemsLeft)) {
                    if (getResources().getString(R.string.product_detail_instock).equals(ctvProductInStock.getText().toString())) {
                        ctvProductInStock.setText(ctvProductInStock.getText().toString() + " (" + itemsLeft + ")");
                    } else {
                        ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock) + " (" + itemsLeft + ")");
                    }
                } else {
                    ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock));
                }
            } else {
                if (bottomBarLp != null) {
                    bottomBarLp.height = 0;
                    llBottomBar.setLayoutParams(bottomBarLp);
                }
                currUserSelectedProductMaxStockQty = 0;
                textView_num.setText("0");
                ctvProductInStock.setVisibility(View.INVISIBLE);
                rlProductQuantity.setVisibility(View.GONE);
                ctvProductOutOfStock.setVisibility(View.VISIBLE);
                outOfStockToWishlist();
            }
        }
    }





    public void setBottonBarHeight(int px){
        RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
        bottomBarLp.height=JDataUtils.dp2Px(px);
        llBottomBar.setLayoutParams(bottomBarLp);
    }
    public long getProductCount() {
        List<SVRAppserviceProductDetailResultPropertyReturnEntity> attributeIds = new ArrayList<>();
        for (int i = 0; i < mAttributeViews.size(); i++) {
            attributeIds.add((SVRAppserviceProductDetailResultPropertyReturnEntity) mAttributeViews.get(i).getTag());
        }
        long count = JStorageUtils.getProductCountByAttribute(ProductActivity.this, mProductDetailBean.getId(), attributeIds);
        JLogUtils.i(TAG, "count:" + count);
        return count;
    }

    public void outOfStockToWishlist() {
        isOutOfStock = true;
        ctvAddToCart.setEnabled(true);
        mLLAddToCart.setEnabled(true);
        mRLAddToWishlistBig.setVisibility(View.GONE);
        mRLAddToWishlistSmall.setVisibility(View.VISIBLE);

        // if is liked , change button text.
        String addWishText = getString(R.string.product_detail_addtowishlist);
        if (1 == mProductDetailBean.getIsLike()) {
            addWishText = getString(R.string.product_detail_addedtowishlist);
        }
        ctvAddToCart.setText(addWishText);
//        ctvAddToCart.setBackgroundResource(R.drawable.big_button_style_purple);
        mLLAddToCart.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
        RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
        if (bottomBarLp != null) {
            //  bottom 的高度与xml文件要同时修改
            bottomBarLp.height = JDataUtils.dp2Px(80);
            llBottomBar.setLayoutParams(bottomBarLp);
        }
    }

    //调用删除接口
    private void sendRequestToDeteleteCell(String itemId) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
            setWishIconColorToBlank();
            if (ctvProductOutOfStock.getVisibility() == View.VISIBLE) {
                ctvAddToCart.setText(getResources().getString(R.string.product_detail_addtowishlist));
            }
            mProductDetailBean.setIsLike(0);
            mAccountDao.deleteWishById(WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getSessionKey(), itemId);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE_LOGIN == requestCode && resultCode == LoginRegisterEmailLoginFragment.RESULTCODE) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
                addRecommendedToWishByOperate();
                needRefreshWhenBackPressed = true;
                changeOperateProductIdPrecacheStatus(true);
                if(reAddCart){
                    addToCartSendRequest();
                    reAddCart=false;
                }else {
                    getProductInfo();
                }
            }
        }
        if (requestCode == ProductActivity.RESULT_WISH && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (!data.getBooleanExtra("needRefreshWhenBackPressed", false)) {
                    String productId = data.getStringExtra("productId");
                    String itemId = data.getStringExtra("itemId");
                    int isLike = data.getIntExtra("isLike", -1);
                    if (!TextUtils.isEmpty(productId) && isLike != -1) {
//                        refreWishIconByPDPResult(productId, isLike, itemId);
                    }
                } else {
                    //此出设了刷新后，按后退键， pdp返pdp也会刷新，一直刷到curation page或productlist page等
//                    refreshProductRecommended();
                    getProductInfo();
                }
            }
        }
        if (requestCode == ProductActivity.PRODUCT_PICTURE_REQUEST_CODE) {
            if (data != null) {
                if (data.getExtras() != null) {
                    int currentIndex = data.getExtras().getInt("currentIndex");
                    if (currentIndex >= 0) {
                        JLogUtils.d(TAG, "Product_currentIndex=" + currentIndex);
                        viewPager.setCurrentItem(currentIndex, true);
                    }
                }
            }

        }
    }

//    private void refreWishIconByPDPResult(String productId, int isLike, String itemId) {
        //pdp 页面，isLike或itemId有变动，就刷新
//        Iterator<SVRAppserviceProductRecommendedResultsItemReturnEntity> itemReturnEntityIterator = recommendedList.iterator();
//        while (itemReturnEntityIterator.hasNext()) {
//            SVRAppserviceProductRecommendedResultsItemReturnEntity entity = itemReturnEntityIterator.next();
//            if (entity.getProductId().equals(productId)) {
//                entity.setIs_like(isLike);
//                entity.setItem_id(itemId);
//                recommendedAdapter.notifyDataSetChanged();
//                continue;
//            }
//        }
//    }
    //点击加入购物车时发送数据
    private   boolean reAddCart;
    private void addToCartSendRequest() {
        mGATrackAddCartTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
            if (mProductDetailBean == null) {
                return;
            }
            Map<String, String>  idQtys=null;
            if(mProductDetailBean.getType().equals(ProductDetailModel.TYPE_GROUP)){
                idQtys=pcGroupConfig.getChildIdAndQty();
            }else if(mProductDetailBean.getType().equals(ProductDetailModel.TYPE_CONFIGURABLE)){
                idQtys=new HashMap<>();
                SVRAppserviceProductDetailResultPropertyReturnEntity bean=
                        (SVRAppserviceProductDetailResultPropertyReturnEntity) mAttributeViews.get(mAttributeViews.size()-1).getTag();
                idQtys.put(bean.getProductId(),userSelectedProductQty+"");
            }else if(mProductDetailBean.getType().equals(ProductDetailModel.getTypeSimple())){
                idQtys=new HashMap<>();
                idQtys.put(mProductDetailBean.getId(),userSelectedProductQty+"");
            }
            if(idQtys.size()==0){
                JViewUtils.showErrorToast(this,"");
                return ;
            }
            mDialog = JViewUtils.showProgressDialog(ProductActivity.this);
            mShoppingDao.addProductToShoppingCart(WhiteLabelApplication.getAppConfiguration().getUserInfo(ProductActivity.this).getSessionKey(), productId,idQtys);
        } else {
            reAddCart=true;
            Intent loginIntent = new Intent(this, LoginRegisterActivity.class);
            startActivityForResult(loginIntent,REQUESTCODE_LOGIN );
            overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }
    private void addtoWishlistsendRequestFromProductList(String productId) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getApplicationContext())) {
            //postion使用-1,因为推荐商品的刷新机制,无法判断它到底是谁
            mProductDao.addProductListToWish(productId, WhiteLabelApplication.getAppConfiguration().getUserInfo(getApplicationContext()).getSessionKey(), productId);
        }
    }
    private void addtoWishlistsendRequest() {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getApplicationContext())) {
            if (mProductDetailBean.getIsLike() == 0) {
                if (ctvProductOutOfStock.getVisibility() == View.VISIBLE) {
                    ctvAddToCart.setText(getResources().getString(R.string.product_detail_addedtowishlist));
                }
                setWishIconColorToPurple();
                mProductDetailBean.setIsLike(1);
                mProductDao.addProductToWish(productId, WhiteLabelApplication.getAppConfiguration().getUserInfo(getApplicationContext()).getSessionKey());
            }
        } else {
            saveProductIdWhenJumpLoginPage(productId);
            Intent intent = new Intent();
            intent.setClass(ProductActivity.this, LoginRegisterActivity.class);
            startActivityForResult(intent, REQUESTCODE_LOGIN);
            overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }
    private void showNoInventory(Context context) {
        if (context == null) {
            return;
        }
        LinearLayout toastView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_prompt_productdetail_notenoughinventory, null);
        TextView message = (TextView) toastView.findViewById(R.id.tv_text);
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductActivity.this)) {
            message.setText(getResources().getString(R.string.insufficient_stock));
        } else {
            if (mStockQty > 0 && mMaxSaleQty > 0) {
                message.setText(getResources().getString(R.string.insufficient_stock));
            } else {
                message.setText(getResources().getString(R.string.insufficient_stock));
            }
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
            if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
                mToast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
            }
            mToast.setView(toastView);
        } else {
            if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
                mToast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
            }
            mToast.setView(toastView);
        }
        mToast.show();
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (mProductImageView != null) {
                count = mProductImageView.size();
            }
            return count;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            try {
                ((ViewPager) container).removeView(mProductImageView.get(position % mProductImageView.size()));
            } catch (Exception ex) {
                ex.getMessage();
            }

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                //((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
                ((ViewPager) container).addView(mProductImageView.get(position % mProductImageView.size()), 0);
            } catch (Exception e) {
                //handler something
                e.printStackTrace();
            }
            //return mImageViews[position % mImageViews.length];
            ImageView object = null;
            if (mProductImageView != null && mProductImageView.size() > 0 && mProductImageView.size() > position) {
                object = mProductImageView.get(position % mProductImageView.size());
            }
            return object;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        JLogUtils.d(TAG, "onStart()");
        try {
            GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
            GaTrackHelper.getInstance().googleAnalyticsProductDetail(this, productId);
            GaTrackHelper.getInstance().googleAnalyticsProductDetail(getApplicationContext(), productId);

        } catch (Exception ex) {
            ex.getStackTrace();
        }
        JLogUtils.i("googleGA_screen", "Product Detail Screen");
    }

    @Override
    protected void onPause() {
        JLogUtils.d(TAG, "onPause()");
        super.onPause();
    }
    @Override
    protected void onStop() {
        mFromProductList = "";
        super.onStop();
        JLogUtils.d(TAG, "onStop()");
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }
}

package com.whitelabel.app.ui.productdetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
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

import com.common.utils.JViewUtil;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HelpCenterDetialActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductDetailPictureActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.adapter.ProductListAdapter;
import com.whitelabel.app.adapter.ProductRecommendedListAdapter;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.ProductDetailCallback;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.model.ProductListItemToProductDetailsEntity;
import com.whitelabel.app.model.ProductPropertyModel;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedResultsItemReturnEntity;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.brandstore.BrandStoreFontActivity;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV3;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JTimeUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.ShareUtil;
import com.whitelabel.app.widget.BindProductView;
import com.whitelabel.app.widget.CustomCoordinatorLayout;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomNestedScrollView;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.FullyLinearLayoutManager;
import com.whitelabel.app.widget.ProductChildListView;
import com.whitelabel.app.widget.ToolBarAlphaBehavior;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

public class ProductDetailActivity extends com.whitelabel.app.BaseActivity<ProductDetailContract.Presenter>
        implements ProductDetailCallback, OnPageChangeListener, View.OnClickListener ,ProductDetailContract.View{
    public Long mGATrackTimeStart = 0L;
    public Long mGATrackAddCartTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    public static final int RESULT_WISH = 101;
    public static final int PRODUCT_PICTURE_REQUEST_CODE = 0x200;
    private String TAG = "ProductDetailActivity";
    private final int REQUESTCODE_LOGIN = 1000;
    private ViewGroup llDots;
    private BindProductView  bpvBindProduct;
    private TextView textView_num, oldprice, ctvAddToCart, price_textview,  ctvProductInStock, ctvProductOutOfStock, productUnavailable, productTrans;
    private Dialog mDialog;
    private TextView ctvProductName, ctvProductBrand;
    private AppBarLayout appbar_layout;
    private RelativeLayout mRLAddToWishlistSmall, mRLAddToWishlistBig;
    private LinearLayout  llBottomBar, mLLAddToCart;
    private ImageView ivHeaderBarWishlist, ivHeaderBarWishlist2, mIVHeaderBarWishlist, mIVHeaderBarWishlist2, ivHeaderBarShare;
    private ViewPager viewPager;
    private CustomTextView tvProductSaverm;
    private RelativeLayout rlProductPrice;
    private RecyclerView  lvProductRecommendList;
    private RelativeLayout rlProductQuantity, descriptionsRelative;
    private ProductRecommendedListAdapter recommendedListAdapter;
    private String productId;
    private LinearLayout llLayout;
    private LinearLayout llAttribute;
    private ArrayList<ImageView> mProductImageView=new ArrayList<>();
    private ArrayList<ImageView> mProductImageViewTips=new ArrayList<>();
    public ProductDetailModel mProductDetailBean;
    private float userSelectedProductPriceFloat;
    private float userSelectedProductFinalPriceFloat;
    private int userSelectedProductInStock;
    private ProductChildListView pcGroupConfigView;
    private long userSelectedProductMaxStockQty;
    private LinearLayout llWebView;
    private Toast mToast;
    private WebView mWebView;
    private Handler dataHandler=new Handler();
    private List<TextView> mAttributeViews = new ArrayList<>();
    private CustomNestedScrollView myScrollView;
    private View llCash, showView;
    private ImageView ivProductImage;
    private ArrayList<String> mProductImagesArrayList = new ArrayList<>();
    public OperateProductIdPrecache operateProductIdPrecache;//未登录时点击了wishicon,登陆成功后主动将其添加到wishlist
    private boolean needRefreshWhenBackPressed = false;
    private String mProductFirstImageurl = "";
    private long mStockQty;
    private long mMaxSaleQty;
    private WheelPickerConfigEntity mAttributeEntity;
    private ToolBarAlphaBehavior toolBarAlphaBehavior;
    private ImageLoader mImageLoader;
    private final static  int BOTTONBAR_HEIGHT=80;
    private int destWidth;
    private RelativeLayout rlProductrecommendLine;
    int trueImageWidth;
    int trueImageHeight;
    int netImageHeight;
    int netImageWidth;
    private ProductListItemToProductDetailsEntity productEntity;
    private int isLike;
    private String  mFromProductList;
    private ImageView ivProductThumbnail;
    private final static int IV_PRODUCTTHUMBNAIL_WANDH=44;
    private ImageView ivShopping;
    private RelativeLayout rlRoot;
    private int currentShoppingCount;
    private View rootView;
    @Override
    public void showNornalProgressDialog() {
        mDialog = JViewUtils.showProgressDialog(ProductDetailActivity.this);
    }
    @Override
    public void showBottomProgressDialog() {
        mDialog = JViewUtils.showProgressDialog(ProductDetailActivity.this, CustomDialog.BOOTOM);
    }
    @Override
    public void dissmissProgressDialog() {
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }
    @Override
    public void showProductRecommendLine() {
        rlProductrecommendLine.setVisibility(View.VISIBLE);
    }
    @Override
    public void updateRecommendData(ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> results) {
        JLogUtils.i("ray","results:"+results.size());
        recommendedListAdapter.updateData(results);
    }



    @Override
    public void showErrorMessage(String errorMsg) {
//        JViewUtils.showErrorToast(this,errorMsg+"");
        JViewUtils.showPopUpWindw(this,rootView,errorMsg);
    }
    @Override
    public void showContentLayout() {
       showView.setVisibility(View.VISIBLE);
       rlProductQuantity.setVisibility(View.VISIBLE);
       descriptionsRelative.setVisibility(View.VISIBLE);
       ivHeaderBarShare.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        JLogUtils.d(TAG, "onDestroy() ");
        if (dataHandler != null) {
            dataHandler.removeCallbacksAndMessages(null);
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
                ex.getStackTrace();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    public void startAddToCart(){
//TODO joyson  requirement change ,temp not skip ShoppingCart
//        Intent intent = new Intent();
//        intent.setClass(this, ShoppingCartActivity1.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        Bundle bundle = new Bundle();
//        bundle.putLong("mGATrackTimeStart",mGATrackAddCartTimeStart);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, REQUEST_SHOPPINGCART);
        mPresenter.addCartToTopAnim(rlRoot,ivProductThumbnail,ivShopping);
        currentShoppingCount+=pcGroupConfigView.getProductSelectQty();
        mPresenter.saveShoppingCartCount(currentShoppingCount);
    }
//    private void facebookWishTrack(){
//        try {
//            FacebookEventUtils.getInstance().facebookEventAddedToWistList(this, productId, userSelectedProductFinalPriceFloat);
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//    }
//    private void trackAddWistList() {
//        try {
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
//                    "Add To Wishlist",
//                    mProductDetailBean.getName(), Long.valueOf(mProductDetailBean.getId()));
//            FirebaseEventUtils.getInstance().ecommerceAddWishlist(this, this.mProductDetailBean.getCategory(), this.mProductDetailBean.getName(),
//                    this.mProductDetailBean.getId(), JDataUtils.formatDouble(this.userSelectedProductFinalPriceFloat + ""));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private void trackProductDetail() {
//        try {
//            FacebookEventUtils.getInstance().facebookEventProductDetail(this, this.productId, this.userSelectedProductFinalPriceFloat);
//            try {
//                FirebaseEventUtils.getInstance().ecommerceViewItem(this, productId, mProductDetailBean.getName()
//                        , mProductDetailBean.getCategory(), userSelectedProductQty + "",
//                        JDataUtils.formatDouble(userSelectedProductFinalPriceFloat + ""), JDataUtils.formatDouble((userSelectedProductFinalPriceFloat * userSelectedProductQty) + ""));
//            } catch (Exception ex) {
//                ex.getMessage();
//            }
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//    }
//    public void addToCartTrack() {
//        try {
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
//                    "Add To Cart",
//                    mProductDetailBean.getName(),
//                    Long.valueOf(mProductDetailBean.getId()));
//            GaTrackHelper.getInstance().googleAnalyticsAddCart(this,
//                    productId, mProductDetailBean.getName());
//            FacebookEventUtils.getInstance().facebookEventAddedToCart(this, productId, userSelectedProductFinalPriceFloat * userSelectedProductQty);
//            FirebaseEventUtils.getInstance().ecommerceAddToCart(this, userSelectedProductQty + "", mProductDetailBean.getCategory(),
//                    mProductDetailBean.getName(), mProductDetailBean.getId(),
//                    JDataUtils.formatDouble((userSelectedProductFinalPriceFloat * userSelectedProductQty) + ""), JDataUtils.formatDouble(userSelectedProductFinalPriceFloat + ""));
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//    }
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
    @Override
    public void changeOperateProductIdPrecacheStatus(boolean available) {
        if (operateProductIdPrecache != null) {
            operateProductIdPrecache.setAvailable(available);
        }
    }
    public  void setWishIconColorToBlank() {
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

    public  void setWishIconColorToThemeColor() {
        ivHeaderBarWishlist2.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist2.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setVisibility(View.VISIBLE);
        mIVHeaderBarWishlist.setVisibility(View.VISIBLE);
        ivHeaderBarWishlist.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductDetailActivity.this,
                                R.mipmap.wishlist_purple_pressed));
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
        private List<ProductPropertyModel> mPropertyList;
        private int mLevel;
        public MyWheelPickerCallback(int level, List<ProductPropertyModel> propertyList) {
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
                ProductPropertyModel entity = null;
                String attributeId = newValue.getValue();
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
                updateProductDetailUIProductPriceStock(mPresenter.getProductData(),userSelectedProductPriceFloat + "", userSelectedProductFinalPriceFloat + "", userSelectedProductInStock, userSelectedProductMaxStockQty, tmpProductMaxSaleQty, childProductsaveRM, childProductItemsLeft);
            }
        }
    }
    public void clearUserSelectedProduct() {
        userSelectedProductPriceFloat = 0.0f;
        userSelectedProductFinalPriceFloat = 0.0f;
        userSelectedProductInStock = 0;
        userSelectedProductMaxStockQty = 0;
        mPresenter.setCurrUserSelectedProductMaxStockQty(0);
        mPresenter.setUserSelectedProductQty(1);
    }
    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(this)).build().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = new ImageLoader(this);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        mGATrackTimeEnable = true;
        setContentView(R.layout.activity_product);
        rootView=LayoutInflater.from(this).inflate(R.layout.activity_product,null);
        destWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(ProductDetailActivity.this);
        productId =  getIntent().getExtras().getString("productId");
        mFromProductList = getIntent().getExtras().getString("from");
        mProductFirstImageurl=getIntent().getExtras().getString("imageurl");
        isLike=getIntent().getExtras().getInt("isLike");
        initView();
        initData();
        mPresenter.setDialogType(mFromProductList);
        mPresenter.loadProductDetailData(productId);
    }
    private void initData() {
        setStatusBarColor(JToolUtils.getColor(R.color.transparent5000));
        TAG = TAG + JTimeUtils.getCurrentTimeLong();
        mAttributeEntity = new WheelPickerConfigEntity();
        mAttributeEntity.setArrayList(new ArrayList<WheelPickerEntity>());
        mAttributeEntity.setOldValue(new WheelPickerEntity());
        productEntity=
                (ProductListItemToProductDetailsEntity) getIntent().getExtras().getSerializable("product_info");
        clearUserSelectedProduct();
        if (!TextUtils.isEmpty(mProductFirstImageurl)) {
            ivProductImage.setVisibility(View.VISIBLE);
            int marginLeft = destWidth * 15 / 640;
            int dividerWidth = destWidth * 16 / 640;
            int destWidth = (this.destWidth - (2 * marginLeft) - dividerWidth) / 2;
            int destHeight = (int) (destWidth/1.332);
            JImageUtils.downloadImageFromServerByProductUrl(ProductDetailActivity.this, mImageLoader, ivProductImage, mProductFirstImageurl, destWidth,destHeight);
            JImageUtils.downloadImageFromServerByProductUrl(ProductDetailActivity.this, mImageLoader, ivProductThumbnail, mProductFirstImageurl, IV_PRODUCTTHUMBNAIL_WANDH,IV_PRODUCTTHUMBNAIL_WANDH);
        } else {
            ivProductImage.setAlpha(0.0f);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ivProductImage.setTransitionName(getResources().getString(R.string.activity_image_trans));
            appbar_layout.setExpanded(false);
            dataHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  appbar_layout.setExpanded(true);
                }
            },450);
        }
        if (productEntity == null)return;
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

    public void initView(){
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
                ToolBarAlphaBehavior(getBaseContext(), getToolbar(), WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavigation_bar_background_color(), new ToolBarAlphaBehavior.CallBack() {
            @Override
            public void callBack(int color) {
                setStatusBarColor(color);
            }
        });
        toolBarAlphaBehavior.setAlPha(ToolBarAlphaBehavior.minAlpha);//toolbar透明度初始化
        pcGroupConfigView = (ProductChildListView) findViewById(R.id.pc_group_config);
        bpvBindProduct= (BindProductView) findViewById(R.id.bpv_bind_product);
        bpvBindProduct.setOnClickListener(this);
        CustomCoordinatorLayout coordinatorLayout = (CustomCoordinatorLayout) findViewById(R.id.cl_product);
        coordinatorLayout.setSwitchScroll(false);
        appbar_layout = ((AppBarLayout) findViewById(R.id.appbar_layout));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //去除5.1以上的阴影效果
            appbar_layout.setOutlineProvider(null);
        }
        llWebView = (LinearLayout) findViewById(R.id.ll_webView);
        mWebView = getWebView();
        myScrollView = (CustomNestedScrollView) findViewById(R.id.myScroll);
        rlProductrecommendLine= (RelativeLayout) findViewById(R.id.rl_productrecommend_line);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        llAttribute = (LinearLayout) findViewById(R.id.ll_attribute);
        ivHeaderBarWishlist = (ImageView) findViewById(R.id.ivHeaderBarWishlist);
        mIVHeaderBarWishlist = (ImageView) findViewById(R.id.ivHeaderBarWishlist11);
        ivHeaderBarWishlist2 = (ImageView) findViewById(R.id.ivHeaderBarWishlist2);
        mIVHeaderBarWishlist2 = (ImageView) findViewById(R.id.ivHeaderBarWishlist22);
        ivHeaderBarShare = (ImageView) findViewById(R.id.ivHeaderBarShare);
        ivHeaderBarShare.setImageDrawable(JImageUtils.getThemeIcon(ProductDetailActivity.this,R.mipmap.share_icon_normal));
        ivHeaderBarWishlist.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductDetailActivity.this,
                                R.mipmap.wishlist_purple_normal));
        ivHeaderBarWishlist2.setImageDrawable
                (JImageUtils.getThemeIcon
                        (ProductDetailActivity.this,
                                R.mipmap.wishlist_purple_normal));
        llCash = findViewById(R.id.ll_cash);
        llCash.setOnClickListener(this);
        ivHeaderBarWishlist.setOnClickListener(this);
        mIVHeaderBarWishlist.setOnClickListener(this);
        ivHeaderBarWishlist2.setOnClickListener(this);
        mIVHeaderBarWishlist2.setOnClickListener(this);
        ivHeaderBarShare.setOnClickListener(this);
        rlProductPrice = (RelativeLayout) findViewById(R.id.rlProductPrice);

        oldprice = (TextView) findViewById(R.id.old_price);
        price_textview = (TextView) findViewById(R.id.price_textview);
        oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        ctvProductInStock = (TextView) findViewById(R.id.ctvProductInStock);
        ctvProductOutOfStock = (TextView) findViewById(R.id.ctvProductOutOfStock);
        productUnavailable = (TextView) findViewById(R.id.product_unavailable);
        productTrans = (TextView) findViewById(R.id.product_trans);
        viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        viewPager.requestFocus();
        viewPager.setFocusableInTouchMode(true);
        viewPager.setFocusable(true);
        llDots = (ViewGroup) findViewById(R.id.ll_dots);
        llBottomBar = (LinearLayout) findViewById(R.id.llBottomBar);
        ctvAddToCart = (CustomTextView) findViewById(R.id.ctvAddToCart);
        mRLAddToWishlistSmall = (RelativeLayout) findViewById(R.id.rl_addtowishlist_small);
        mRLAddToWishlistBig = (RelativeLayout) findViewById(R.id.rl_addtowishlist_big);
        mLLAddToCart = (LinearLayout) findViewById(R.id.ll_addtocart);
        llBottomBar.setOnClickListener(this);
        textView_num = (TextView) findViewById(R.id.detail_quantity_textview2);
        rlProductQuantity = (RelativeLayout) findViewById(R.id.rlProductQuantity);
        lvProductRecommendList= (RecyclerView) findViewById(R.id.lvProductRecommendList);
        ivProductThumbnail= (ImageView) findViewById(R.id.iv_add_to_cart_thumbnail);
        rlRoot=(RelativeLayout) findViewById(R.id.rl_product_detail_root);
        int destWidthColorSize = (WhiteLabelApplication.getPhoneConfiguration().getScreenWidth() - (JDataUtils.dp2Px(27))) / 2;
        int destHeightColorSize = JDataUtils.dp2Px(37);
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
        pcGroupConfigView.setOnProductCountChangeListener(new ProductChildListView.OnProductCountChangeListener() {
            @Override
            public void change(int count) {
                if(count==0){
                    setAddCartButtonEnable(false);
                }else{
                    setAddCartButtonEnable(true);
                }
            }
        });
        myScrollView.setOnCustomScroolChangeListener(new CustomNestedScrollView.ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                //渐变色.
                if (toolBarAlphaBehavior != null) {
                    toolBarAlphaBehavior.onNestedScroll(t);
                }
            }
        });
        lvProductRecommendList= (RecyclerView) findViewById(R.id.lvProductRecommendList);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this);
        manager.setItemPadding(18f);
        manager.setSmoothScrollbarEnabled(true);
        manager.setScrollEnabled(false);
        lvProductRecommendList.setLayoutManager(manager);
        lvProductRecommendList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        lvProductRecommendList.setHasFixedSize(true);
        lvProductRecommendList.setNestedScrollingEnabled(false);
        recommendedListAdapter = new ProductRecommendedListAdapter(this, new ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity>(),
                mImageLoader, this);
        lvProductRecommendList.setAdapter(recommendedListAdapter);
    }
    private void gotoShoppingCartActivity() {
        Intent intent = new Intent(ProductDetailActivity.this, ShoppingCartActivity1.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, REQUEST_SHOPPINGCART);
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
                        if(WhiteLabelApplication.getAppConfiguration().isSignIn(getApplicationContext())) {
                            gotoShoppingCartActivity();
                        }else{
                            startLoginActivity(false);
                        }
                    }
                });
        TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(this));
        ivShopping= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(this,ivShopping,R.drawable.ic_action_cart);
        mPresenter.getShoppingCount();
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
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getShoppingCount();
    }
    @Override
    public void setShoppingCartCount(int count) {
        currentShoppingCount =count;
        updateRightIconNum(R.id.action_shopping_cart, count);
    }
    @Override
    public void onBackPressed() {
           getToolbar().setVisibility(View.GONE);
           if(mPresenter.getProductData()!=null) {
               Intent intent = new Intent();
               Bundle bundle = new Bundle();
               bundle.putString("productId", this.productId);
               bundle.putInt("isLike", mPresenter.getProductData().getIsLike());
               bundle.putString("itemId", mPresenter.getProductData().getItemId());
               bundle.putBoolean("needRefreshWhenBackPressed", needRefreshWhenBackPressed);
               intent.putExtras(bundle);
               setResult(Activity.RESULT_OK, intent);
           }
        super.onBackPressed();
    }

    public void trackAddWishList(){
        GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
                "Add To Wishlist",
                mPresenter.getProductData().getName(), Long.valueOf(mProductDetailBean.getId()));
    }

    @Override
    public void setProductCountView(long count) {
        textView_num.setText(count+"");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHeaderBarWishlist22:
            case R.id.ivHeaderBarWishlist2:
            case R.id.ivHeaderBarWishlist11:
            case R.id.ivHeaderBarWishlist: {
                mPresenter.wishListBtnClick();
                break;
            }
            case R.id.ivHeaderBarShare: {
                String imageUrl="";
                if(mPresenter.getProductData().getImages().size()>0){
                    imageUrl= mPresenter.getProductData().getImages().get(0);
                }
                ShareUtil share = new ShareUtil(this, mPresenter.getProductData().getName(),
                        "", imageUrl, mPresenter.getProductData().getUrl(), "test");
                share.show();
                break;
            }
            case R.id.ivPriceMinus: {
                mPresenter.productCountMinusClick();
                break;
            }
            case R.id.bpv_bind_product:
                Intent bindIntent=new Intent(ProductDetailActivity.this, BindProductActivity.class);
                bindIntent.putExtra(BindProductActivity.EXTRA_PRODUCTID,mPresenter.getProductData().getId());
                if(mPresenter.getProductData().getProperty()!=null&&mPresenter.getProductData().getProperty().size()>0){
                     Bundle  bundle=new Bundle();
                     bundle.putSerializable(BindProductActivity.EXTRA_PRODUCT_DATA,mPresenter.getProductData().getProperty().get(0));
                     bindIntent.putExtras(bundle);
                }
                startActivity(bindIntent);
                break;
            case R.id.ivPricePlus: {
                mPresenter.productCountPlusClick();
                break;
            }
            case R.id.ll_addtocart: {
                mPresenter.addToCartClick();
                break;
            }
            case R.id.ctv_detailDelivery1:
            case R.id.ctv_detailDetailDelivery2:
                Bundle bundle = new Bundle();
                bundle.putString("title", "SHIPPING DELIVERY");
                bundle.putString("content", "shipping-delivery");
                startNextActivity(bundle, HelpCenterDetialActivity.class, false);
                break;
            case R.id.ctv_product_attribute:
                //根据Tag中的属性ID去查找当前属性集合
                    ProductPropertyModel propertyReturnEntitys = (ProductPropertyModel) v.getTag();
                    List<ProductPropertyModel> propertyList = mPresenter.getProductData().getProperty();
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
                break;
            case R.id.ctvProductBrand:
                if(mPresenter.getProductData()!=null) {
                    if(!"0".equals(mPresenter.getProductData().getBrandId())) {
                        Bundle brandStoreIntent = new Bundle();
                        brandStoreIntent.putString(BrandStoreFontActivity.EXTRA_BRAND_ID, mPresenter.getProductData().getBrandId());
                        brandStoreIntent.putString(BrandStoreFontActivity.EXTRA_BRAND_NAME, mPresenter.getProductData().getBrand().toUpperCase());
                        startNextActivity(brandStoreIntent, BrandStoreFontActivity.class, false);
                    }else{
                        Intent intent1=new Intent(this, HomeActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        finish();
                    }
                }
              break;
        }
    }
    private void showWheelDialog(List<ProductPropertyModel> propertyList, String currAttributeValue) {
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
            ProductPropertyModel firstProperty = propertyList.get(i);
            WheelPickerEntity mAttributePickerEntity = new WheelPickerEntity();
            mAttributePickerEntity.setIndex(i);
            mAttributePickerEntity.setValue(firstProperty.getId());
            mAttributePickerEntity.setDisplay(firstProperty.getLabel());
            mAttributeEntity.getArrayList().add(mAttributePickerEntity);
        }
        mAttributeEntity.setOldValue(oldEntity);
        JViewUtils.showWheelPickerOneDialog(this, mAttributeEntity);
    }
    private List<ProductPropertyModel> getSvrAppserviceProductDetailResultPropertyReturnEntities
            (ProductPropertyModel propertyReturnEntitys, List<ProductPropertyModel> propertyList) {
        for (int i = 0; i < propertyReturnEntitys.getLevel(); i++) {
            ProductPropertyModel currProperty = (ProductPropertyModel) mAttributeViews.get(i).getTag();
            for (int z = 0; z < propertyList.size(); z++) {
                if (currProperty.getId().equals(propertyList.get(z).getId())) {
                    propertyList = propertyList.get(z).getChild();
                    break;
                }
            }
        }
        return propertyList;
    }
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
    }
    private void setImageBackground(int selectItems) {
        if (mProductImageViewTips == null || selectItems < 0 || mProductImageViewTips.size() <= selectItems) {
            return;
        }
        for (int index = 0; index < mProductImageViewTips.size(); index++) {
            if (index == selectItems) {
                mProductImageViewTips.get(index).setBackground(JImageUtils.getThemeCircle(this));
            } else {
                mProductImageViewTips.get(index).setBackgroundResource(R.mipmap.dot_unchecked);
            }
        }
    }
    @Override
    public void loadStaticData(ProductDetailModel productDetailModel) {
        mProductDetailBean=productDetailModel;
        if (productDetailModel!=null){
            if(!TextUtils.isEmpty(productDetailModel.getBrand())) {
                ctvProductBrand.setText(productDetailModel.getBrand().toUpperCase());
            }
            ctvProductName.setText(productDetailModel.getName());//分享标题赋值
            if (!TextUtils.isEmpty(productDetailModel.getUiDetailHtmlText())) {
                JToolUtils.webViewFont(this, mWebView, productDetailModel.getUiDetailHtmlText(), 13.5f);
            }
            int webviewCount=llWebView.getChildCount();
            if(webviewCount<1) {
                llWebView.addView(mWebView);
            }
            netImageHeight = productDetailModel.getImageHeight();
            netImageWidth = productDetailModel.getImageWidth();
            trueImageWidth=destWidth;
            trueImageHeight=(destWidth*netImageHeight)/netImageWidth;
            JImageUtils.downloadImageFromServerByProductUrl(ProductDetailActivity.this, mImageLoader, ivProductImage, mProductFirstImageurl, trueImageWidth, trueImageHeight);
        }
    }
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
    public  void hideBindProductView() {
        bpvBindProduct.setVisibility(View.GONE);
    }
    public void showBindProductView(List<ProductPropertyModel> products) {
        bpvBindProduct.initData(products, mImageLoader);
    }
    public void loadGroupProductView(ProductDetailModel mProductDetailBean, ArrayList<String> productImagesArrayList) {
        showView.setVisibility(View.GONE);
        pcGroupConfigView.setVisibility(View.VISIBLE);
        if( mProductDetailBean.getProperty().size()>0&&mProductDetailBean.getImages().size()>0){
            mProductDetailBean.getProperty().get(0).setImage(mProductDetailBean.getImages().get(0));
        }
        pcGroupConfigView.initProductChildListView(mProductDetailBean.getProperty(),rootView);
        boolean instock=false;
        for(ProductPropertyModel bean:mProductDetailBean.getProperty() ){
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
    public void loadConfigurableProductView(ProductDetailModel productDetailBean, ArrayList<String> productImagesArrayList) {
        ArrayList<ProductPropertyModel> productPropertyList = productDetailBean.getProperty();
        if (productPropertyList == null || productPropertyList.size() <= 0) {
            updateProductDetailUIProductImage(productImagesArrayList);
            updateProductDetailUIProductPriceStock(productDetailBean,"0", "0", productDetailBean.getInStock(), productDetailBean.getStockQty(), productDetailBean.getMaxSaleQty(), "",
                    productDetailBean.getItemsLeft());
        }else {
            llAttribute.removeAllViews();
            mAttributeViews.clear();
            createAttributeView(0, productDetailBean.getProperty().get(0));
        }
    }
    public void loadSimpleProductView(ProductDetailModel productDetailBean, ArrayList<String> productImagesArrayList) {
        if (productDetailBean.getEligibleForCod() == 1) {
            llCash.setVisibility(View.VISIBLE);
        } else {
            llCash.setVisibility(View.GONE);
        }
        updateProductDetailUIProductImage(productImagesArrayList);
        updateProductDetailUIProductPriceStock(productDetailBean,"0", "0", productDetailBean.getInStock(),
                productDetailBean.getStockQty(), productDetailBean.getMaxSaleQty(),
                productDetailBean.getSaveRm(), productDetailBean.getItemsLeft());
        if (!TextUtils.isEmpty(productDetailBean.getSaveRm())) {
            tvProductSaverm.setVisibility(View.VISIBLE);
            tvProductSaverm.setText("(" + productDetailBean.getSaveRm() + ")");
        } else {
            tvProductSaverm.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(productDetailBean.getItemsLeft())) {
            if (getResources().getString(R.string.product_detail_instock).equals(ctvProductInStock.getText().toString())) {
                ctvProductInStock.setText(ctvProductInStock.getText().toString() + " (" + productDetailBean.getItemsLeft() + ")");
            } else {
                ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock) + " (" + productDetailBean.getItemsLeft() + ")");
            }
        } else {
            ctvProductInStock.setText(getResources().getString(R.string.product_detail_instock));
        }
    }
    public void createAttributeView(int level, ProductPropertyModel bean) {
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
                updateProductDetailUIProductImage(bean.getImages());
            }
            updateProductDetailUIProductPriceStock(mPresenter.getProductData(),userSelectedProductPriceFloat + "",
                    userSelectedProductFinalPriceFloat + "",
                    userSelectedProductInStock, userSelectedProductMaxStockQty,
                    tmpProductMaxSaleQty, childProductsaveRM, childProductItemsLeft);
        }
    }
    @Override
    public void setLikeView(boolean isLike) {
        isLike = setDiffPageIsLike(isLike);

        if(isLike){
            ivHeaderBarWishlist.setImageDrawable
                    (JImageUtils.getThemeIcon
                            (ProductDetailActivity.this,
                                    R.mipmap.wishlist_purple_pressed));
            mIVHeaderBarWishlist.setImageResource(R.mipmap.wishlist_white_pressed);
        }else{
            ivHeaderBarWishlist.setImageDrawable
                    (JImageUtils.getThemeIcon
                            (ProductDetailActivity.this,
                                    R.mipmap.wishlist_purple_normal));
            mIVHeaderBarWishlist.setImageResource(R.mipmap.wishlist_white_normal);
        }
    }

    private boolean setDiffPageIsLike(boolean isLike) {
        if (mFromProductList.equals(ProductListAdapter.FROM_PRODUCT_LIST)){
            if (productEntity != null){
                mPresenter.getProductData().setIsLike(productEntity.getIsLike());
                if (productEntity.getIsLike()==1){
                    isLike=true;
                }else {
                    isLike=false;
                }
            }
        }else if (mFromProductList.equals(HomeHomeFragmentV3.FROM_HOME_LIST)){
            mPresenter.getProductData().setIsLike(this.isLike);
            if (this.isLike==1){
                isLike=true;
            }else {
                isLike=false;
            }
        }
        return isLike;
    }

    public  void showAvailabilityView() {
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
    public void hideAvailabilityView() {
        ivHeaderBarWishlist.setVisibility(View.VISIBLE);
        ivHeaderBarShare.setVisibility(View.VISIBLE);
        llBottomBar.setVisibility(View.VISIBLE);
        setBottonBarHeight(BOTTONBAR_HEIGHT);
        if (1 == userSelectedProductInStock && mPresenter.getCurrUserSelectedProductMaxStockQty()> 0) {
            rlProductQuantity.setVisibility(View.VISIBLE);
        } else {
            rlProductQuantity.setVisibility(View.GONE);
        }
        productUnavailable.setVisibility(View.GONE);
        llAttribute.setVisibility(View.VISIBLE);
        productTrans.setVisibility(View.GONE);
    }
    @Override
    public void hideVisibleProduct() {
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
    }
    private void updateProductDetailUIProductImage(final ArrayList<String> productImageUrlList) {
        if(productImageUrlList==null)return;
            mProductImagesArrayList.clear();
            mProductImageView.clear();
            mProductImageViewTips.clear();
            mProductImagesArrayList.addAll(productImageUrlList);
            if (mProductFirstImageurl != null && productImageUrlList.size() > 0) {
                if (!mProductFirstImageurl.equals(productImageUrlList.get(0))) {
                    JImageUtils.downloadImageFromServerByProductUrl(ProductDetailActivity.this, mImageLoader, ivProductImage, productImageUrlList.get(0), netImageWidth, netImageHeight);
                    mProductFirstImageurl = "";
                }
            }
           llDots.removeAllViews();
            for (int index = 0; index < productImageUrlList.size(); index++) {
                final ImageView imageView = getImageView(productImageUrlList, index);
                mProductImageView.add(imageView);
                ImageView imageViewTips = createTipView(index);
                llDots.addView(imageViewTips);
                mProductImageViewTips.add(imageViewTips);
            }
        viewPager.setAdapter(new ProductDetailViewPagerAdapter(mProductImageView));
        if (productImageUrlList.size() == 1) {
            llDots.setVisibility(View.INVISIBLE);
        } else {
            llDots.setVisibility(View.VISIBLE);
        }
        viewPager.addOnPageChangeListener(this);
        if (mProductImageView != null && mProductImageView.size() > 0) {
            viewPager.setCurrentItem(0);
        }
        if (viewPager != null && viewPager.getLayoutParams() != null) {
            viewPager.getLayoutParams().height = trueImageHeight;
            viewPager.getLayoutParams().width = trueImageWidth;
        }
    }
    @NonNull
    private ImageView getImageView(final ArrayList<String> productImageUrlList, int index) {
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        JImageUtils.downloadImageFromServerByProductUrl(ProductDetailActivity.this, mImageLoader, imageView, productImageUrlList.get(index), netImageWidth, netImageHeight);
        imageView.setTag(index);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer index= (Integer) v.getTag();
                Intent intent = new Intent();
                intent.setClass(ProductDetailActivity.this, ProductDetailPictureActivity.class);
                Bundle bundle = new Bundle();
                JLogUtils.d(TAG, "currrrentItem--->" + viewPager.getCurrentItem() + "---" + mProductImageView.size());
                if (ivProductImage.getDrawable() == null) {
                    bundle.putInt("currentIndex", index);
                    bundle.putStringArrayList("pictures", productImageUrlList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                } else {
                    bundle.putInt("currentIndex", index);
                    bundle.putStringArrayList("pictures", productImageUrlList);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        intent.putExtras(bundle);
                        ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(ProductDetailActivity.this, ivProductImage, ProductDetailActivity.this.getString(R.string.activity_image_trans));
                        ActivityCompat.startActivityForResult(ProductDetailActivity.this, intent, PRODUCT_PICTURE_REQUEST_CODE, aop.toBundle());
                    } else {
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    }
                }
            }
        });
        return imageView;
    }
    @NonNull
    private ImageView createTipView(int index) {
        ImageView imageViewTips = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
            lp.setMargins(5, 0, 5, 0);
            imageViewTips.setLayoutParams(lp);
        if (index == 0) {
            imageViewTips.setBackground(JImageUtils.getThemeCircle(this));
        } else {
            imageViewTips.setBackgroundResource(R.mipmap.dot_unchecked);
        }
        return imageViewTips;
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
    private void updateProductDetailUIProductPriceStock(ProductDetailModel productDetailModel,String price,
                                                        String finalPrice, int instock, long stockqty, long maxSaleQty, String saveRM, String itemsLeft) {
        mStockQty = stockqty;
        mMaxSaleQty = maxSaleQty;
        // Price
        float childPrice = 0.0f;
        try {
            childPrice = Float.parseFloat(price);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        float childFinalPrice = 0.0f;
        try {
            childFinalPrice = Float.parseFloat(finalPrice);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
//        userSelectedProductPriceOffsetFloat = 0;
//        userSelectedProductFinalPriceOffsetFloat = 0;
        float productPriceFloat = 0.0f;
        float productFinalPriceFloat = 0.0f;
        try {
            productPriceFloat = Float.parseFloat(productDetailModel.getPrice());
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        try {
            productFinalPriceFloat = Float.parseFloat(productDetailModel.getFinalPrice());
        } catch (Exception ex) {
            ex.getStackTrace();
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
        mPresenter.setOutOfStock(false);
        if (0 == userSelectedProductInStock) {
            rlProductQuantity.setVisibility(View.GONE);
            setAddCartButtonEnable(false);
            RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
            if (bottomBarLp != null) {
                bottomBarLp.height = 0;
                llBottomBar.setLayoutParams(bottomBarLp);
            }
            mPresenter.setCurrUserSelectedProductMaxStockQty(0);
            userSelectedProductMaxStockQty = 01;
            mPresenter.setUserSelectedProductQty(0);
            ctvProductInStock.setVisibility(View.INVISIBLE);
            ctvProductOutOfStock.setVisibility(View.VISIBLE);
            textView_num.setText("" + mPresenter.getUserSelectedProductQty());
            outOfStockToWishlist();
        } else if (1 == userSelectedProductInStock) { // in stock
            mRLAddToWishlistSmall.setVisibility(View.GONE);
            mRLAddToWishlistBig.setVisibility(View.VISIBLE);
            setAddCartButtonEnable(true);
            RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
            long count = getProductCount();
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductDetailActivity.this)) {
                userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                mPresenter.setCurrUserSelectedProductMaxStockQty( stockqty); //判断加减的时候使用到的
            } else {
                if (maxSaleQty > 0) {
                    userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                    mPresenter.setCurrUserSelectedProductMaxStockQty(stockqty); //判断加减的时候使用到的
                } else {

                    userSelectedProductMaxStockQty = stockqty;//userSelectedProductMaxStockQty
                    mPresenter.setCurrUserSelectedProductMaxStockQty(stockqty); //判断加减的时候使用到的
                }
            }
            mPresenter.setUserSelectedProductQty(1);
            if (mPresenter.getCurrUserSelectedProductMaxStockQty()- count > 0) {
                if (bottomBarLp != null) {
                    //此處  48dp 與 dimens 的button_touch_height關聯，必須同時修改
                    bottomBarLp.height = JDataUtils.dp2Px(48 + 32);
                    llBottomBar.setLayoutParams(bottomBarLp);
                }
                mPresenter.setCurrUserSelectedProductMaxStockQty(mPresenter.getCurrUserSelectedProductMaxStockQty() - count);
                ctvProductInStock.setVisibility(View.VISIBLE);
                rlProductQuantity.setVisibility(View.VISIBLE);
                ctvProductOutOfStock.setVisibility(View.INVISIBLE);
                textView_num.setText("" + mPresenter.getUserSelectedProductQty());
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
                mPresenter.setCurrUserSelectedProductMaxStockQty(0);
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
        List<ProductPropertyModel> attributeIds = new ArrayList<>();
        for (int i = 0; i < mAttributeViews.size(); i++) {
            attributeIds.add((ProductPropertyModel) mAttributeViews.get(i).getTag());
        }
        long count = JStorageUtils.getProductCountByAttribute(ProductDetailActivity.this, mPresenter.getProductData().getId(), attributeIds);
        JLogUtils.i(TAG, "count:" + count);
        return count;
    }
    public void outOfStockToWishlist() {
        mPresenter.setOutOfStock(true);
        ctvAddToCart.setEnabled(true);
        mLLAddToCart.setEnabled(true);
        mRLAddToWishlistBig.setVisibility(View.GONE);
        mRLAddToWishlistSmall.setVisibility(View.VISIBLE);
        // if is liked , change button text.
        String addWishText = getString(R.string.product_detail_addtowishlist);
        if (1 == mPresenter.getProductData().getIsLike()) {
            addWishText = getString(R.string.product_detail_addedtowishlist);
        }
        ctvAddToCart.setText(addWishText);
        mLLAddToCart.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
        RelativeLayout.LayoutParams bottomBarLp = (RelativeLayout.LayoutParams) llBottomBar.getLayoutParams();
        if (bottomBarLp != null) {
            //  bottom 的高度与xml文件要同时修改
            bottomBarLp.height = JDataUtils.dp2Px(80);
            llBottomBar.setLayoutParams(bottomBarLp);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE_LOGIN == requestCode && resultCode == LoginRegisterEmailLoginFragment.RESULTCODE) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductDetailActivity.this)) {
                mPresenter.wishListBtnClick();
                needRefreshWhenBackPressed = true;
                changeOperateProductIdPrecacheStatus(true);
                mPresenter.delayAddToCart();
            }
        }else if (requestCode == ProductDetailActivity.RESULT_WISH && resultCode == Activity.RESULT_OK) {
                if (data != null&&data.getBooleanExtra("needRefreshWhenBackPressed", false)) {
                    mPresenter.loadProductDetailData(productId);
                }
        }else if (requestCode == ProductDetailActivity.PRODUCT_PICTURE_REQUEST_CODE) {
                if (data != null&&data.getExtras() != null) {
                    int currentIndex = data.getExtras().getInt("currentIndex");
                    if (currentIndex >= 0) {
                        JLogUtils.d(TAG, "Product_currentIndex=" + currentIndex);
                        viewPager.setCurrentItem(currentIndex, true);
                    }
            }
        }
    }
    @Override
    public Map<String, String> getGroupProductParams() {
        return pcGroupConfigView.getChildIdAndQty();
    }
    @Override
    public String getConfiguationProductSimpleId() {
        ProductPropertyModel bean=
                (ProductPropertyModel) mAttributeViews.get(mAttributeViews.size()-1).getTag();
        return bean.getProductId();
    }
    public void  startLoginActivity(boolean expire){
        Intent intent = new Intent();
        intent.setClass(ProductDetailActivity.this, LoginRegisterActivity.class);
        if(expire){
            intent.putExtra("expire", true);
        }
        startActivityForResult(intent, REQUESTCODE_LOGIN);
        overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }
    public  void showNoInventoryToast() {
//        LinearLayout toastView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_prompt_productdetail_notenoughinventory, null);
//        TextView message = (TextView) toastView.findViewById(R.id.tv_text);
//        if (WhiteLabelApplication.getAppConfiguration().isSignIn(ProductDetailActivity.this)) {
//            message.setText(getResources().getString(R.string.insufficient_stock));
//        } else {
//            if (mStockQty > 0 && mMaxSaleQty > 0) {
//                message.setText(getResources().getString(R.string.insufficient_stock));
//            } else {
//                message.setText(getResources().getString(R.string.insufficient_stock));
//            }
//        }
//        if (mToast == null) {
//            mToast = Toast.makeText(this.getApplicationContext(), "", Toast.LENGTH_SHORT);
//            if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
//                mToast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
//            }
//            mToast.setView(toastView);
//        } else {
//            if (WhiteLabelApplication.getPhoneConfiguration() != null && WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() != 0) {
//                mToast.setGravity(Gravity.BOTTOM, 0, (int) (WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth() * 0.25));
//            }
//            mToast.setView(toastView);
//        }
//        mToast.show();

        JViewUtils.showPopUpWindw(this,rootView,getResources().getString(R.string.insufficient_stock));
    }
    @Override
    protected void onStart() {
        super.onStart();
            GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);

    }
    @Override
    protected void onPause() {
        JLogUtils.d(TAG, "onPause()");
        super.onPause();
    }
    @Override
    protected void onStop() {
        mPresenter.setDialogType("");
        super.onStop();
        JLogUtils.d(TAG, "onStop()");
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }
}

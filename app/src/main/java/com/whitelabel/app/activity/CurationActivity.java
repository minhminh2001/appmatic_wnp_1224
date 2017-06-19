package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.CurationProductListAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.fragment.ProductListFilterFragment;
import com.whitelabel.app.fragment.ProductListSortFragment;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailProductListItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.ServerTime;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.FileUtils;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomCountdown;
import com.whitelabel.app.widget.CustomXListView;
import com.whitelabel.app.widget.FilterSortBottomView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import static com.whitelabel.app.R.id.cxlvProductList;

/**
 * Created by imaginato on 2015/6/26.
 */
public class CurationActivity extends BaseActivitySearchCart implements View.OnClickListener, CustomXListView.IXListViewListener,
        OnFilterSortFragmentListener, FilterSortBottomView.FilterSortBottomViewCallBack, IFilterSortActivity {
    public final int TYPE_SORT = 0;
    public final int TYPE_FILTER = 1;
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    public static final int RESULT_WISH = 101;
    private final int SEARCH_TYPE_INIT = 1;
    private final int SEARCH_TYPE_REFRESH = 2;
    private final int SEARCH_TYPE_LOADMORE = 6;
    private final int LIMIT = 36;
    private ImageView iv_bottom_slideto_top;
    private DataHandler dataHandler;
    private WebView wv_cms;
    //header view
    private View view;
    private CustomXListView mCustomXListView;
    private RelativeLayout rl_product_line;
    //    private SVRAppserviceLandingPagesListLandingPageItemReturnEntity curationEntity;
    private ServerTime serverTime;
    private CurationProductListAdapter curationProductListAdapter;
    private ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productItemEntityArrayList;
    private int offset = 0;
    private int searchType = SEARCH_TYPE_INIT;
    private static String TAG;
    private SearchResultHandler searchResultHandler;
    private String PROMPT_ERROR_NOINTERNET;
    private ImageView mIVBottomSlideToTop;
    private FilterSortHelper filterSortHelper;
    private FilterSortBottomView filterSortBottomView;
    public final static int TABBAR_INDEX_NONE = -1;
    public final static int TABBAR_INDEX_FILTER = 1;
    public final static int TABBAR_INDEX_SORT = 2;
    public final static int ACTIVITY_TYPE_PRODUCTLIST_CURATION = 4;
    private int mCurrentFilterSortTabIndex;
    private SVRAppserviceProductSearchParameter mSVRAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
    private final int LOADING = 2;
    private final int NONE = 1;
    private final int NOTDATA = 3;
    private final int ERROR = 4;
    private int TYPE = 1;
    private View connectionLayout, rlNodata;
    private RequestErrorHelper requestErrorHelper;
    private CustomCountdown customCountdown;
    private LinearLayout tryAgain;
    private boolean showConnectionLayout = true;
    private boolean isFirst = true;
    private ProductDao mProductDao;
    private SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity;
    public OperateProductIdPrecache operateProductIdPrecache;//未登录时点击了wishicon,登陆成功后主动将其添加到wishlist
    private boolean stopSearch = false;
    private boolean isFirstClose = true;
    public static final String EXTRA_CURATION_ID = "curation_id";
    public static final String EXTRA_CATEGORY_NAME = "curation_name";
    public static final String EXTRA_CURATION_TITLE = "curation_title";
    private String mCurationId;
    private String mCurationTitle;
    private ImageLoader mImageLoader;
    public boolean isDoubleCol = true;
    private ImageView mTopViewToggleIV;
    private RelativeLayout mTopFilterAndSortBarRL;
    private boolean mIsShowSwitchFilterBar;

    @Override
    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int type, int index, long minPrice, long maxPrice) {
        setSVRAppserviceCProductSearchParameterMinPriceMaxPrice(minPrice, maxPrice);
    }

    @Override
    public void onCancelClick(View view) {
        filterSortHelper.hideVisibleFragments();
        resetCurrentFilterSortTabIndex();
    }

    @Override
    public void onFilterSortListItemClick(int type, Object object) {

        filterSortHelper.hideVisibleFragments();
        //refresh data call api
        filterData();
        resetCurrentFilterSortTabIndex();
    }

    @Override
    public void onAnimationFinished(Fragment fragment) {
        filterSortHelper.hideContainer(fragment);
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return mCurrentFilterSortTabIndex;
    }



    public void filterData() {
        productItemEntityArrayList.clear();
        curationProductListAdapter.notifyDataSetChanged();
        mCustomXListView.stopLoadMore();
        mCustomXListView.setPullLoadEnable(false);
        searchType = SEARCH_TYPE_INIT;
        offset = 0;
        mDialog = JViewUtils.showProgressDialog(CurationActivity.this);
        search();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curation);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        mGATrackTimeEnable = true;
        initView();
        initListener();
        initData();
    }

    public void curationTrack(String heading, String category) {
//        try {
//            FirebaseEventUtils.getInstance().customizedViewCurationDetail(CurationActivity.this, heading, mCurationId);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//
//        try {
//            FirebaseEventUtils.getInstance().customizedViewCurationGroup(CurationActivity.this, mCategoryName);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }

    }

    private static class DataHandler extends Handler {
        private final WeakReference<CurationActivity> mActivity;

        public DataHandler(CurationActivity activity) {
            mActivity = new WeakReference<CurationActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_SERVERTIMER:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                            mActivity.get().mDialog.dismiss();
                        }
                        mActivity.get().serverTime = (ServerTime) msg.obj;
                        JLogUtils.d("response", "serviceTime:" + mActivity.get().serverTime.getTime());
                        if (mActivity.get().isFirst) {
                            mActivity.get().initListViewHeader(mActivity.get().curationReturnEntity);
                            mActivity.get().isFirst = false;
                        }
                        mActivity.get().setCurationDetailWhenResponse(mActivity.get().curationReturnEntity);
                    } else {
                        JLogUtils.d("responseERROR", "serviceTime:" + msg.obj);
                        Message msg1 = new Message();
                        msg1.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        msg1.obj = mActivity.get().PROMPT_ERROR_NOINTERNET;
                        mActivity.get().searchResultHandler.sendMessage(msg1);
                    }
                    break;
                case ProductDao.REQUEST_CURATIONDETAIL:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        mActivity.get().rlNodata.setVisibility(View.INVISIBLE);
                        mActivity.get().connectionLayout.setVisibility(View.GONE);
                        mActivity.get().TYPE = mActivity.get().NONE;
                        SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity = (SVRAppserviceLandingPagesDetailReturnEntity) msg.obj;
                        if (curationReturnEntity != null && curationReturnEntity.getProductList() != null && curationReturnEntity.getProductList().size() > 0) {

                            if (!TextUtils.isEmpty(curationReturnEntity.getHeading()) && TextUtils.isEmpty(mActivity.get().mCurationTitle)) {
                                mActivity.get().setToolbarTitle(curationReturnEntity.getHeading());
                            }
                            if (mActivity.get().offset == 1) {
                                mActivity.get().curationTrack(curationReturnEntity.getHeading(), curationReturnEntity.getProductList().get(0).getCategory());
                            }
//                            mActivity.get().offset = mActivity.get().offset + 1;
                            //判斷是否要顯示 時鐘
                            if (!TextUtils.isEmpty(curationReturnEntity.getWtfShowClock()) && curationReturnEntity.getWtfShowClock().equals("1")) {
                                //dao
                                if (mActivity.get().isFirst) {
                                    mActivity.get().curationReturnEntity = curationReturnEntity;
                                    mActivity.get().mProductDao.getServerTime();
                                    return;
                                }

                            } else {
                                if (mActivity.get().isFirst) {
                                    mActivity.get().initListViewHeader(curationReturnEntity);
                                    mActivity.get().isFirst = false;
                                }
                            }
                            // 如果不加載  serverTime, 則可以關閉  ProgressBar
                            if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                                mActivity.get().mDialog.dismiss();
                            }
                            mActivity.get().setCurationDetailWhenResponse(curationReturnEntity);
                        } else {
                            mActivity.get().mCustomXListView.setPullLoadEnable(false);
                            mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                            mActivity.get().TYPE = mActivity.get().NOTDATA;
                            Message notDataMsg = new Message();
                            notDataMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            mActivity.get().searchResultHandler.sendMessage(notDataMsg);
                        }
                    } else {
                        // Fail
                        if (mActivity.get().TYPE != mActivity.get().NOTDATA) {
                            mActivity.get().TYPE = mActivity.get().ERROR;
                        }
                        mActivity.get().mCustomXListView.stopLoadMore();
                        String errorMsg = (String) msg.obj;
                        Message faildMsg = new Message();
                        faildMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        faildMsg.obj = errorMsg + "";
                        mActivity.get().searchResultHandler.sendMessage(faildMsg);
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                        mActivity.get().mDialog.dismiss();
                    }
                    if (mActivity.get().TYPE != mActivity.get().NOTDATA) {
                        mActivity.get().TYPE = mActivity.get().ERROR;
                    }
                    if (mActivity.get().productItemEntityArrayList == null || mActivity.get().productItemEntityArrayList.size() == 0) {
                        mActivity.get().mCustomXListView.stopLoadMore();
                        mActivity.get().requestErrorHelper.showConnectionBreaks(msg);
                    } else {
                        Message msg1 = new Message();
                        msg1.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        msg1.obj = mActivity.get().PROMPT_ERROR_NOINTERNET;
                        mActivity.get().searchResultHandler.sendMessage(msg1);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public SVRAppserviceProductSearchFacetsReturnEntity facets;

    private void setCurationDetailWhenResponse(SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity) {
        mCustomXListView.setVisibility(View.VISIBLE);
        try {
            if (facets == null) {
                facets = new SVRAppserviceProductSearchFacetsReturnEntity();
            }
            if (curationReturnEntity.getFacets() != null) {
                facets.setCategory_filter(curationReturnEntity.getFacets().getCategory_filter());
                facets.setPrice_filter(curationReturnEntity.getFacets().getPrice_filter());
                facets.setBrand_filter(curationReturnEntity.getFacets().getBrand_filter());
                facets.setModel_type_filter(curationReturnEntity.getFacets().getModel_type_filter());
                facets.setSort_filter(curationReturnEntity.getFacets().getSort_filter());
            }
        } catch (Exception ex) {
            JLogUtils.e(TAG, "search -> onSuccess", ex);
        }
        ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productListResult = null;
        try {
            productListResult = curationReturnEntity.getProductList();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "search -> onSuccess", ex);
        }
        Message seccessMsg = new Message();
        seccessMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_SUCCESS_OK;
        seccessMsg.obj = productListResult;
        searchResultHandler.sendMessage(seccessMsg);
        mCustomXListView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (mProductDao != null) {
            mProductDao.cancelHttpByTag(TAG);
        }
        if (dataHandler != null) {
            dataHandler.removeCallbacksAndMessages(null);
        }
        if (filterSortBottomView.filterHandler != null) {
            filterSortBottomView.filterHandler = null;
        }
        super.onDestroy();
        try {
            if (wv_cms != null) {
                // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
                // destory()
                ViewParent parent = wv_cms.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(wv_cms);
                }
                wv_cms.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                wv_cms.getSettings().setJavaScriptEnabled(false);
                wv_cms.clearHistory();
                wv_cms.clearView();
                wv_cms.removeAllViews();
                try {
                    wv_cms.destroy();
                } catch (Throwable ex) {
                   ex.getStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
//        WhiteLabelApplication.getRefWatcher(this).watch(this);

    }

    private void initData() {
        mCurrentFilterSortTabIndex = TABBAR_INDEX_NONE;
        mCurationTitle = getIntent().getStringExtra(EXTRA_CURATION_TITLE);
        mCurationId = getIntent().getStringExtra(EXTRA_CURATION_ID);
        String mCategoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        JLogUtils.i(TAG, "mCategoryName:" + mCategoryName);
        dataHandler = new DataHandler(this);
        TAG = this.getClass().getSimpleName();
        mProductDao = new ProductDao(TAG, dataHandler);
        initToolBar(mCurationTitle);
        productItemEntityArrayList = new ArrayList<>();
        curationProductListAdapter = new CurationProductListAdapter(CurationActivity.this, productItemEntityArrayList, mImageLoader);
        mCustomXListView.setAdapter(curationProductListAdapter);
        PROMPT_ERROR_NOINTERNET = getString(R.string.productlist_list_prompt_error_nointernet);
        searchResultHandler = new SearchResultHandler(this);
        offset = 0;
        searchType = SEARCH_TYPE_INIT;
        mDialog = JViewUtils.showProgressDialog(CurationActivity.this);
        search();
    }


    private void initListener() {
        tryAgain.setOnClickListener(this);
        filterSortBottomView.hideBottomSlideToTop(true);
        filterSortBottomView.hideSwitchAndFilterBar(true);
        mIVBottomSlideToTop.setOnClickListener(this);
        mCustomXListView.setXListViewListener(this);
        mCustomXListView.setOnScrollListener(new CustomXListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (curationReturnEntity != null) {
                    if (curationReturnEntity.getProductList().size() <= 0) {
                        return;
                    }
                }
                if (!mIsShowSwitchFilterBar) {
                    return;
                }
//
                filterSortBottomView.setFilterShow(scrollState, new ProductListFilterHideCallBack() {
                    @Override
                    public void callBack() {
                    }
                });

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((offset * (LIMIT / 2) - firstVisibleItem) <= (LIMIT / 2 - 1) && TYPE == NONE) {
                    searchType = SEARCH_TYPE_LOADMORE;
                    if (!stopSearch) {
                        //现在是会连续加载两次，并且是同样数据
                        mCustomXListView.setLoadMoreProgress();
                    }
                    showConnectionLayout = false;
                }
                if (view.getFirstVisiblePosition() == 0) {
                    filterSortBottomView.hideBottomSlideToTop(true);
                } else {
                    filterSortBottomView.hideBottomSlideToTop(false);
                }

                if (firstVisibleItem > 1) {
                    mIsShowSwitchFilterBar = true;
                    mTopFilterAndSortBarRL.setVisibility(View.VISIBLE);
                } else {
                    mIsShowSwitchFilterBar = false;
                    mTopFilterAndSortBarRL.setVisibility(View.GONE);
                }
                filterSortBottomView.setIsShowSwitchFilterBar(mIsShowSwitchFilterBar);
            }
        });
    }
    private void initView() {
        mImageLoader = new ImageLoader(this);
        connectionLayout = findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(this, connectionLayout);
        connectionLayout = findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(this, connectionLayout);
        rlNodata = findViewById(R.id.rlNodata);
        tryAgain = (LinearLayout) findViewById(R.id.try_again);
        mCustomXListView = (CustomXListView) findViewById(cxlvProductList);
        mCustomXListView.setPullRefreshEnable(false);
        mCustomXListView.setPullLoadEnable(false);
        mCustomXListView.setHeaderDividersEnabled(false);
        mCustomXListView.setFooterDividersEnabled(false);
        //filter&sort
        mTopFilterAndSortBarRL = (RelativeLayout) findViewById(R.id.top_switch_and_filter_bar);
        mTopViewToggleIV = (ImageView) findViewById(R.id.iv_view_toggle_top);
        LinearLayout mTopFilterLL = (LinearLayout) findViewById(R.id.ll_filter_top);
        LinearLayout mTopSortLL = (LinearLayout) findViewById(R.id.ll_sort_top);
        mTopFilterLL.setOnClickListener(this);
        mTopSortLL.setOnClickListener(this);
        mTopViewToggleIV.setOnClickListener(this);

        FrameLayout mFlFilterSortContainer = (FrameLayout) findViewById(R.id.flFilterSortContainer);
        mIVBottomSlideToTop = (ImageView) findViewById(R.id.iv_bottom_slideto_top);


        int FRAGMENT_CONTAINER_ID = R.id.flFilterSortContainer;
        mFlFilterSortContainer.setOnClickListener(this);
        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(mTopFilterAndSortBarRL, mIVBottomSlideToTop, this);
        ProductListFilterFragment filterFragment = new ProductListFilterFragment();
        filterFragment.setFragmentListener(this);
        ProductListSortFragment sortFragment = new ProductListSortFragment();
        sortFragment.setFragmentListener(this);
        filterSortHelper = new FilterSortHelper(this, sortFragment, filterFragment, mFlFilterSortContainer, FRAGMENT_CONTAINER_ID);
    }

    private Dialog mDialog;

    private void search() {
        TYPE = LOADING;
        offset = offset + 1;
        String order = mSVRAppserviceProductSearchParameter.getOrder();
        String dir = mSVRAppserviceProductSearchParameter.getDir();
        String price = mSVRAppserviceProductSearchParameter.getPrice();
        String brand = null;
        if (!JDataUtils.isEmpty(mSVRAppserviceProductSearchParameter.getBrandId())) {
            brand = mSVRAppserviceProductSearchParameter.getBrandId();
        }
        if (!JDataUtils.isEmpty(mSVRAppserviceProductSearchParameter.getBrand())) {
            brand = mSVRAppserviceProductSearchParameter.getBrand();
        }
        String modelType = mSVRAppserviceProductSearchParameter.getModel_type();
        String sessionKey = "";
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(CurationActivity.this)) {
            sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(CurationActivity.this).getSessionKey();
            mProductDao.getCurationDetail(order, dir, brand, modelType, price, mCurationId, offset + "", LIMIT + "", sessionKey);
        } else {
            mProductDao.getCurationDetail(order, dir, brand, modelType, price, mCurationId, offset + "", LIMIT + "", "");
        }
    }

    private void refreWishIconByPDPResult(String productId, int isLike, String itemId) {
        //pdp 页面，isLike或itemId有变动，就刷新
        Iterator<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> itemReturnEntityIterator = productItemEntityArrayList.iterator();
        while (itemReturnEntityIterator.hasNext()) {
            SVRAppserviceLandingPagesDetailProductListItemReturnEntity entity = itemReturnEntityIterator.next();
            if (entity.getProductId().equals(productId)) {
                entity.setIs_like(isLike);
                entity.setItem_id(itemId);
                curationProductListAdapter.notifyDataSetChanged();
                continue;
            }
        }

    }

    public void saveProductIdWhenJumpLoginPage(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        operateProductIdPrecache = new OperateProductIdPrecache(productId);
    }

    public void changeOperateProductIdPrecacheStatus(boolean available) {
        if (operateProductIdPrecache != null) {
            operateProductIdPrecache.setAvailable(available);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CurationActivity.RESULT_WISH && resultCode == Activity.RESULT_OK) {
            //在curationProductListAdapter  onClick wishIcon 里 startActivityForResult 到 pdp;，按后退键后调用以下方法
            if (data != null) {
                //如果 pdp页面登陆了，则needRefreshWhenBackPressed=true,刷新当前页面所有product,否则只刷新指定product的isLike字段
                if (!data.getBooleanExtra("needRefreshWhenBackPressed", false)) {
                    String productId = data.getStringExtra("productId");
                    String itemId = data.getStringExtra("itemId");
                    int isLike = data.getIntExtra("isLike", -1);
                    if (!TextUtils.isEmpty(productId) && isLike != -1) {
                        refreWishIconByPDPResult(productId, isLike, itemId);
                    }
                } else {
                    onRefresh();
                }
            }
        }

        //登陆成功后要刷新所有product的信息  //在curationProductListAdapter  onClick wishIcon 里 startActivityForResult 到loginpage;，成功后调用以下方法
        if (LoginRegisterActivity.REQUESTCODE_LOGIN == requestCode && resultCode == LoginRegisterEmailLoginFragment.RESULTCODE) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(CurationActivity.this)) {
                //将预缓存的wishitem productId设为有效，以便稍后自动将其加如wishlist
                changeOperateProductIdPrecacheStatus(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initTimeCon(long allTime) {
        customCountdown.init(allTime, new CustomCountdown.OnCountDownListenter() {
            @Override
            public void onListenter(boolean isOver, long timeLeft) {
                if (isOver) {
                    //结束倒计时
                    closeClock();
                } else {
                    if (customCountdown.getVisibility() != View.VISIBLE) {
                        customCountdown.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        customCountdown.start();
    }

    public void closeClock() {
        // 时间结束，某些ui也要隐藏
        if (isFirstClose) {
            isFirstClose = false;
            customCountdown.setVisibility(View.GONE);
            curationReturnEntity.getProductList().clear();
            productItemEntityArrayList.clear();
            curationProductListAdapter.notifyDataSetChanged();
            stopSearch = true;
            rl_product_line.setVisibility(View.GONE);
            mCustomXListView.setPullRefreshEnable(false);
            mCustomXListView.setPullLoadEnable(false);
            flCms.setVisibility(View.VISIBLE);
            wv_cms.setVisibility(View.VISIBLE);
            webViewFont(curationReturnEntity.getWtfStaticApp());
        }
    }

    public void webViewFont(String str) {
        String html = FileUtils.readAssest(this, "html/content.html");
//        html = html.replace("@fontName0", "LatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        String baseurl = "file:///android_asset/html/";
        wv_cms.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }

    ImageView ivCuration;
    FrameLayout flCms;

    public void initListViewHeader(final SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity) {
        int imageWidth = 640;
        int imageHeight = 640 * 240 / 490;
        int mDestHeight = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(CurationActivity.this) * 240 / 490;
        view = LayoutInflater.from(CurationActivity.this).inflate(R.layout.header_list_curation, null);
        ivCuration = (ImageView) view.findViewById(R.id.iv_curation);

        //init clock  and cms
        if (serverTime != null && "1".equals(curationReturnEntity.getWtfShowClock())) {
            customCountdown = (CustomCountdown) view.findViewById(R.id.count_down_curation);
            flCms = (FrameLayout) view.findViewById(R.id.fl_cms);
            wv_cms = new WebView(getApplicationContext());
            wv_cms.getSettings().setJavaScriptEnabled(true);
            wv_cms.getSettings().setPluginState(WebSettings.PluginState.ON);
            wv_cms.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wv_cms.getSettings().setAllowFileAccess(true);
            wv_cms.getSettings().setDefaultTextEncodingName("UTF-8");
            wv_cms.getSettings().setLoadWithOverviewMode(true);
            wv_cms.getSettings().setUseWideViewPort(true);
            wv_cms.getSettings().setBuiltInZoomControls(false);//缩放
            wv_cms.getSettings().setSupportZoom(false);
            wv_cms.getSettings().setDisplayZoomControls(false);
            wv_cms.setHorizontalScrollBarEnabled(false);
            wv_cms.setVerticalScrollBarEnabled(false);
            flCms.addView(wv_cms, 0);
//            wv_cms=(WebView)view.findViewById(R.id.wv_cms);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //计算wtf还剩余多长时间
            try {
                long a = sdf.parse(serverTime.getTime()).getTime();
                boolean isClose = false;
                if (TextUtils.isEmpty(curationReturnEntity.getWtfEndtime())) {
                    isClose = true;
                }
                long b = 0;
                try {
                    b = sdf.parse(curationReturnEntity.getWtfEndtime()).getTime();
                } catch (Exception e) {
                    isClose = true;
                }
                //已经结束，直接调用closeClock方法
                if (a > b || isClose) {
                    closeClock();
                } else {
                    //为了 不让buttom刚出现就闪没，所以在此调用一下方法
//                    hideBottomSlideToTop(false);
                }
                initTimeCon((b - a) / 1000);
            } catch (Exception e) {
                JLogUtils.d("error", "loadClockhasError" + e.toString());
            }
        } else {
//            hideBottomSlideToTop(false);
        }

        ivCuration.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mDestHeight));
        TextView tvHeading = (TextView) view.findViewById(R.id.tv_title);
        TextView tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        if (TextUtils.isEmpty(curationReturnEntity.getHeading())) {
            tvHeading.setVisibility(View.GONE);
        } else {
            tvHeading.setText(curationReturnEntity.getHeading());
        }

        if (TextUtils.isEmpty(curationReturnEntity.getDescription())) {
            tvDesc.setVisibility(View.GONE);
        } else {
            tvDesc.setText(Html.fromHtml(curationReturnEntity.getDescription()));
        }
        if (TextUtils.isEmpty(curationReturnEntity.getImage())) {
            ivCuration.setVisibility(View.GONE);
            JLogUtils.d("TESTING =========EMPTY", curationReturnEntity.getImage());
        } else {
            JLogUtils.d("TESTING =========", curationReturnEntity.getImage());
            JImageUtils.downloadImageFromServerListener(this, mImageLoader, ivCuration, curationReturnEntity.getImage(), imageWidth, imageHeight, new CurationImageListener(this, curationReturnEntity));
        }
        mCustomXListView.addHeaderView(view);
    }

    private class CurationImageListener implements RequestListener<String, Bitmap> {
        WeakReference<CurationActivity> mActivity;
        SVRAppserviceLandingPagesDetailReturnEntity mCurationReturnEntity;

        public CurationImageListener(CurationActivity activity, SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity) {
            mActivity = new WeakReference<>(activity);
            mCurationReturnEntity = curationReturnEntity;
        }

        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            if (mActivity.get() == null) return false;
            mActivity.get().ivCuration.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (mActivity.get() == null) return false;
            mActivity.get().initBanner(mActivity.get().view, mCurationReturnEntity.getBadgeType(), mCurationReturnEntity.getBadgeValue());
            ivCuration.setImageBitmap(resource);
            return true;
        }
    }

    private void initBanner(View view, String badgeType, String badgeValue) {
        View vBanner1 = view.findViewById(R.id.banner1);
        View vBanner2 = view.findViewById(R.id.banner2);
        View vBanner3 = view.findViewById(R.id.banner3);
        View vBanner4 = view.findViewById(R.id.banner4);
        View vBanner5 = view.findViewById(R.id.banner5);
        View vBanner6 = view.findViewById(R.id.banner6);
        View vBanner7 = view.findViewById(R.id.banner7);
        View vBanner8 = view.findViewById(R.id.banner8);
        View vBanner9 = view.findViewById(R.id.banner9);
        TextView tvBanner2 = (TextView) view.findViewById(R.id.banner2_text);
        TextView tvBanner3 = (TextView) view.findViewById(R.id.banner3_text);
        TextView tvBanner4 = (TextView) view.findViewById(R.id.banner4_text);
        TextView tvBanner7 = (TextView) view.findViewById(R.id.banner7_text);
        TextView tvBanner8 = (TextView) view.findViewById(R.id.banner8_text);
        TextView tvBanner9 = (TextView) view.findViewById(R.id.banner9_text);
        //  4,7,8需要判断banner value 的长度，进而设置fontsize
        if ("1".equals(badgeType)) {
            vBanner1.setVisibility(View.VISIBLE);
        } else if ("2".equals(badgeType)) {
            vBanner2.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner2.setText(badgeValue);
            }
        } else if ("3".equals(badgeType)) {
            vBanner3.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner3.setText(badgeValue);
            }
        } else if ("4".equals(badgeType)) {
            vBanner4.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner4.setText(badgeValue);
                JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, tvBanner4);
                tvBanner4.setText(badgeValue);
                tvBanner4.setPadding(0, 4, 0, 0);
                tvBanner4.setTextSize(18);
            }
        } else if ("5".equals(badgeType)) {
            vBanner5.setVisibility(View.VISIBLE);
        } else if ("6".equals(badgeType)) {
            vBanner6.setVisibility(View.VISIBLE);
        } else if ("7".equals(badgeType)) {
            vBanner7.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner7.setText(badgeValue);
                tvBanner7.setText(badgeValue);
                tvBanner7.setPadding(JToolUtils.dip2px(CurationActivity.this, 2), 0, 0, 0);
                tvBanner7.setTextSize(16);
            }
        } else if ("8".equals(badgeType)) {
            vBanner8.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner8.setText(badgeValue);
                JViewUtils.setLandingBannerFontSize(badgeType, badgeValue, tvBanner8);
                tvBanner8.setText(badgeValue);
                tvBanner8.setPadding(JToolUtils.dip2px(CurationActivity.this, 2), JToolUtils.dip2px(CurationActivity.this, 22), 0, 0);
                tvBanner8.setTextSize(16);
            }
        } else if ("9".equals(badgeType)) {
            vBanner9.setVisibility(View.VISIBLE);
            if (badgeValue != null) {
                tvBanner9.setText(badgeValue);
                tvBanner9.setPadding(JToolUtils.dip2px(CurationActivity.this, 2), JToolUtils.dip2px(CurationActivity.this, 20), 0, 0);
                tvBanner9.setTextSize(16);
            }
        } else {
//            vBanner1.setVisibility(View.GONE);
//            vBanner2.setVisibility(View.GONE);
//            vBanner3.setVisibility(View.GONE);
//            vBanner4.setVisibility(View.GONE);
//            vBanner5.setVisibility(View.GONE);
//            vBanner6.setVisibility(View.GONE);
//            vBanner7.setVisibility(View.GONE);
//            vBanner8.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bottom_slideto_top:
                if (mCustomXListView != null) {
                    mCustomXListView.setSelection(0);
                }
                break;
            case R.id.try_again:
                connectionLayout.setVisibility(View.GONE);
                initData();
                break;
            case R.id.iv_view_toggle_top:
                toggleViewOption(mTopViewToggleIV);
                break;
            case R.id.ll_filter_top:
                filterSortOption(TYPE_FILTER);
                break;
            case R.id.ll_sort_top:
                filterSortOption(TYPE_SORT);
                break;
        }
    }

    public void filterSortOption(int type) {
        if (type == 0) {
            onSortWidgetClick(mCurrentFilterSortTabIndex != TABBAR_INDEX_SORT);
            mCurrentFilterSortTabIndex = mCurrentFilterSortTabIndex == TABBAR_INDEX_SORT ? TABBAR_INDEX_NONE : TABBAR_INDEX_SORT;
        } else {
            onFilterWidgetClick(mCurrentFilterSortTabIndex != TABBAR_INDEX_FILTER);
            mCurrentFilterSortTabIndex = mCurrentFilterSortTabIndex == TABBAR_INDEX_FILTER ? TABBAR_INDEX_NONE : TABBAR_INDEX_FILTER;
        }
    }

    public void toggleViewOption(ImageView toggleView) {
        if (isDoubleCol) {
            toggleView.setImageResource(R.mipmap.ic_view_double);
            mTopViewToggleIV.setImageResource(R.mipmap.ic_view_double);
        } else {
            toggleView.setImageResource(R.mipmap.ic_view_single);
            mTopViewToggleIV.setImageResource(R.mipmap.ic_view_single);
        }
        isDoubleCol = !isDoubleCol;
        mCustomXListView.setSelection(0);
        curationProductListAdapter.notifyDataSetChanged();
    }

    public void onFilterWidgetClick(boolean show) {
        filterSortHelper.onFilterClicked(show, createBundle());
    }

    public void onSortWidgetClick(boolean show) {
        filterSortHelper.onSortClicked(show, createBundle());
    }

    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(CurationActivity.ACTIVITY_TYPE_PRODUCTLIST_CURATION);
        filterSortPageEntity.setFacets(facets);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", filterSortPageEntity);
        return bundle;
    }


    public void resetCurrentFilterSortTabIndex() {
        mCurrentFilterSortTabIndex = TABBAR_INDEX_NONE;
    }

    public void setSVRAppserviceProductSearchParameterBrand(String brandValue) {
        mSVRAppserviceProductSearchParameter.setBrand(brandValue);
    }

    public void setSVRAppserviceProductSearchParameterType(String typeValue) {
        mSVRAppserviceProductSearchParameter.setModel_type(typeValue);
    }

    public void setSVRAppserviceProductSearchParameterSort(String sortValue) {
        String orderValue = null;
        String dirValue = null;
        if (!JDataUtils.isEmpty(sortValue)) {
            String[] sortValueArray = sortValue.split("__");
            if (sortValueArray != null && sortValueArray.length >= 2) {
                orderValue = sortValueArray[0];
                dirValue = sortValueArray[1];
            }
        }
        mSVRAppserviceProductSearchParameter.setOrder(orderValue);
        mSVRAppserviceProductSearchParameter.setDir(dirValue);
    }

    public void setSVRAppserviceCProductSearchParameterMinPriceMaxPrice(long minPrice, long maxPrice) {
        mSVRAppserviceProductSearchParameter.setPrice(minPrice + "-" + maxPrice);
    }

    @Override
    public void onRefresh() {
        offset -= 1;
        TYPE = LOADING;
        searchType = SEARCH_TYPE_REFRESH;
        mDialog = JViewUtils.showProgressDialog(CurationActivity.this);
        search();
    }

    public void onBackPressed() {
        if (filterSortHelper.isAnyActive()) {
            filterSortHelper.hideVisibleFragments();
            resetCurrentFilterSortTabIndex();
        } else {
            finish();
            overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);

        }
    }

    @Override
    public void onLoadMore() {
        searchType = SEARCH_TYPE_LOADMORE;
        search();
    }

    public static class SearchResultHandler extends Handler {
        public static final int TYPE_INTERFACE_FAILIRE = 1;
        public static final int TYPE_INTERFACE_SUCCESS_OK = 2;
        private final WeakReference<CurationActivity> mActivity;

        public SearchResultHandler(CurationActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case TYPE_INTERFACE_FAILIRE: {
                    handleFailure(msg);
                    break;
                }
                case TYPE_INTERFACE_SUCCESS_OK: {
                    handleSuccessOK(msg);
                    break;
                }
            }
        }

        private void handleFailure(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (mActivity.get().SEARCH_TYPE_LOADMORE == mActivity.get().searchType) {
                mActivity.get().offset = mActivity.get().offset - 1;
                mActivity.get().mCustomXListView.stopLoadMore();
//                if (!JDataUtils.errorMsgHandler(mActivity.get(), (String) msg.obj)) {
//                    JViewUtils.showToast(mActivity.get(), null, (String) msg.obj);
//                }
            } else if (mActivity.get().SEARCH_TYPE_INIT == mActivity.get().searchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                mActivity.get().rlNodata.setVisibility(View.VISIBLE);
                mActivity.get().mCustomXListView.setVisibility(View.GONE);
            } else if (mActivity.get().SEARCH_TYPE_REFRESH == mActivity.get().searchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                if (!JDataUtils.errorMsgHandler(mActivity.get(), (String) msg.obj)) {
                    JViewUtils.showToast(mActivity.get(), null, (String) msg.obj);
                }
            }
        }

        private void handleSuccessOK(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (mActivity.get().SEARCH_TYPE_LOADMORE == mActivity.get().searchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }
                if (searchResultArray == null || searchResultArray.size() < 0) {
                    mActivity.get().mCustomXListView.setPullLoadEnable(false);
                    mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                } else {
                    mActivity.get().mCustomXListView.setPullLoadEnable(true);
                    mActivity.get().productItemEntityArrayList.addAll(searchResultArray);
                    mActivity.get().curationProductListAdapter.notifyDataSetChanged();
                }
                mActivity.get().mCustomXListView.stopLoadMore();
            } else if (mActivity.get().SEARCH_TYPE_INIT == mActivity.get().searchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                mActivity.get().curationProductListAdapter.hideSwitchAndFilterBar(false);
                ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }
                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    mActivity.get().curationProductListAdapter.hideSwitchAndFilterBar(true);
                    mActivity.get().mCustomXListView.setPullLoadEnable(false);
                    mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                    mActivity.get().filterSortBottomView.hideSwitchAndFilterBar(true);
                    return;
                }
                if (searchResultArray == null || searchResultArray.size() < mActivity.get().LIMIT) {
                    mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                    mActivity.get().mCustomXListView.setPullLoadEnable(false);
                } else {
                    mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                    mActivity.get().mCustomXListView.setPullLoadEnable(true);

                }
                mActivity.get().productItemEntityArrayList.addAll(searchResultArray);
                mActivity.get().curationProductListAdapter.notifyDataSetChanged();
                if (mActivity.get().mGATrackTimeEnable) {
                    GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                            GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, mActivity.get().mGATrackTimeStart, "Curation Loading"
                    );
                    mActivity.get().mGATrackTimeEnable = false;
                }
                mActivity.get().filterSortBottomView.hideSwitchAndFilterBar(false);
            } else if (mActivity.get().SEARCH_TYPE_REFRESH == mActivity.get().searchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }
                mActivity.get().productItemEntityArrayList.clear();
                mActivity.get().productItemEntityArrayList.addAll(searchResultArray);
                mActivity.get().curationProductListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //EasyTracker.getInstance(this).activityStart(this);
//        EasyTracker easyTracker = EasyTracker.getInstance(this);
//        easyTracker.send(MapBuilder.createEvent("Curation Screen", // Event category (required)
//                null, // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (customCountdown != null) {
            customCountdown.cleanAllTimeText();
        }
//        EasyTracker.getInstance(this).activityStop(this);
    }
}

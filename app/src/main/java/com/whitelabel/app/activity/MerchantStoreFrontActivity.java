package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.MerchantProductListAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.fragment.ProductListFilterFragment;
import com.whitelabel.app.fragment.ProductListSortFragment;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceProductItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductMerchantReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.CustomXListView;
import com.whitelabel.app.widget.FilterSortBottomView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;


public class MerchantStoreFrontActivity extends BaseActivitySearchCart implements View.OnClickListener, CustomXListView.IXListViewListener,
        OnFilterSortFragmentListener, FilterSortBottomView.FilterSortBottomViewCallBack, IFilterSortActivity {
    public final int TYPE_SORT = 0;
    public final int TYPE_FILTER = 1;
    private final int LIMIT = 36;
    private final int SEARCH_TYPE_INIT = 1;
    private final int SEARCH_TYPE_LOADMORE = 6;
    private final int SEARCH_TYPE_REFRESH = 2;
    private final int LOADING = 2;
    private final int NONE = 1;
    private final int NO_DATA = 3;
    private final int ERROR = 4;
    public static final int RESULT_ADD_WISH = 101;
    public final static int TABBAR_INDEX_NONE = -1;
    public final static int TABBAR_INDEX_FILTER = 1;
    public final static int TABBAR_INDEX_SORT = 2;
    public final static int ACTIVITY_TYPE_PRODUCTLIST_MERCHANT = 3;
    public static String BUNDLE_VENDOR_ID = "vendor_id";
    public static String BUNDLE_VENDOR_DISPLAY_NAME = "vendor_display_name";
    private int mSearchType = SEARCH_TYPE_INIT;
    private int mOffset;
    private String TAG;
    private int TYPE = 1;
    private String PROMPT_ERROR_NOINTERNET;
    private int mCurrentFilterSortTabIndex;
    public boolean mShowSlideTopBar = true;
    private boolean mStopSearch = false;
    private boolean mIsFirst = true;//listview header 只在第一次进入时加载
    private DataHandler mDataHandler;
    private SearchResultHandler mSearchResultHandler;
    private CustomXListView mCustomXListView;
    private ImageView mIVBottomSlideToTop;
    private SVRAppserviceProductSearchParameter mSVRAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
    private View mConnectionLayout, mRlNodata;
    private LinearLayout mTryAgain;
    private Dialog mDialog;
    private MerchantProductListAdapter mMerchantProductListAdapter;
    private ArrayList<SVRAppserviceProductItemReturnEntity> mMerchantItemReturnEntityArrayList;
    private static SVRAppserviceProductMerchantReturnEntity mMerchantReturnEntity;
    private ProductDao mProductDao;
    private String mVendorId;
    private String mVendorDisplayName;
    private ImageLoader imageLoader;
    private FilterSortHelper filterSortHelper;
    private FilterSortBottomView filterSortBottomView;
    public OperateProductIdPrecache mOperateProductIdPrecache;//未登录时点击了wishicon,登陆成功后主动将其添加到wishlist
    public boolean isDoubleCol = true;
    private ImageView mTopViewToggleIV;
    private boolean mIsShowSwitchFilterBar;


    @Override
    public void onAnimationFinished(Fragment fragment) {
        filterSortHelper.hideContainer(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
        GaTrackHelper.getInstance().googleAnalytics("MerchantStoreFront Screen", this);
        JLogUtils.i("googleGA_screen", "MerchantStoreFront Screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);

    }

    private void refreWishIconByPDPResult(String productId, int isLike, String itemId) {
        //pdp 页面，isLike或itemId有变动，就刷新
        Iterator<SVRAppserviceProductItemReturnEntity> itemReturnEntityIterator = mMerchantItemReturnEntityArrayList.iterator();
        while (itemReturnEntityIterator.hasNext()) {
            SVRAppserviceProductItemReturnEntity entity = itemReturnEntityIterator.next();
            if (entity.getProductId().equals(productId)) {
                entity.setIsLike(isLike);
                entity.setItemId(itemId);
                mMerchantProductListAdapter.notifyDataSetChanged();
                continue;
            }
        }
    }

    public void saveProductIdWhenJumpLoginPage(String productId) {
        //点击wish icon 时跳到登陆页面前，需要保存
        mOperateProductIdPrecache = new OperateProductIdPrecache(productId);
    }

    public void changeOperateProductIdPrecacheStatus(boolean available) {
        if (mOperateProductIdPrecache != null) {
            mOperateProductIdPrecache.setAvailable(available);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MerchantStoreFrontActivity.RESULT_ADD_WISH && resultCode == Activity.RESULT_OK) {
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
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(MerchantStoreFrontActivity.this)) {
                //将预缓存的wishitem productId设为有效，以便稍后自动将其加如wishlist
                changeOperateProductIdPrecacheStatus(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_storefront);
        imageLoader = new ImageLoader(this);
        initIntent();
        initToolBar();
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent();
        initData();
    }

    private void initToolBar() {
        if (!TextUtils.isEmpty(mVendorDisplayName)) {
            setTitle(JDataUtils.getFirstLetterToUpperCase(mVendorDisplayName));
        } else {
            setTitle("");
        }
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        mCurrentFilterSortTabIndex = TABBAR_INDEX_NONE;
        mDataHandler = new DataHandler(this);
        TAG = this.getClass().getSimpleName();
        mProductDao = new ProductDao(TAG, mDataHandler);
        mMerchantItemReturnEntityArrayList = new ArrayList<SVRAppserviceProductItemReturnEntity>();
        mMerchantProductListAdapter = new MerchantProductListAdapter(MerchantStoreFrontActivity.this, mMerchantReturnEntity, mMerchantItemReturnEntityArrayList, imageLoader);
        mCustomXListView.setAdapter(mMerchantProductListAdapter);
        PROMPT_ERROR_NOINTERNET = getString(R.string.productlist_list_prompt_error_nointernet);
        mSearchResultHandler = new SearchResultHandler(this);
        mOffset = 0;
        mSearchType = SEARCH_TYPE_INIT;
        mDialog = JViewUtils.showProgressDialog(MerchantStoreFrontActivity.this);
        search();
    }

    public void initListViewHeader(final SVRAppserviceProductMerchantReturnEntity merchantReturnEntity) {
        int imageWidth = 640;
        int imageHeight = 640 * 240 / 490;
        int destHeight = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth(MerchantStoreFrontActivity.this) * 240 / 490;
        View mHeaderView = LayoutInflater.from(MerchantStoreFrontActivity.this).inflate(R.layout.header_list_merchant, null);


        final ImageView ivMerchant = (ImageView) mHeaderView.findViewById(R.id.iv_merchant_banner);
        ivMerchant.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, destHeight));
        TextView tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_merchant_title);
        TextView tvDesc = (TextView) mHeaderView.findViewById(R.id.tv_merchant_desc);
        RelativeLayout rlProductLine = (RelativeLayout) mHeaderView.findViewById(R.id.rl_merchant_product_line);

        if (TextUtils.isEmpty(merchantReturnEntity.getVendor_name())) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(merchantReturnEntity.getVendor_name());
        }
        if (TextUtils.isEmpty(merchantReturnEntity.getDescription())) {
            tvDesc.setVisibility(View.GONE);
        } else {
            tvDesc.setText(Html.fromHtml(merchantReturnEntity.getDescription()));
        }
        if (TextUtils.isEmpty(merchantReturnEntity.getBanner())) {
            ivMerchant.setVisibility(View.GONE);
        } else {
            JImageUtils.downloadImageFromServerByUrl(MerchantStoreFrontActivity.this, imageLoader, ivMerchant, merchantReturnEntity.getBanner(), imageWidth, imageHeight);
        }
        if (TextUtils.isEmpty(merchantReturnEntity.getVendor_name()) && TextUtils.isEmpty(merchantReturnEntity.getDescription()) && TextUtils.isEmpty(merchantReturnEntity.getBanner())) {
            rlProductLine.setVisibility(View.GONE);
        }
        mCustomXListView.addHeaderView(mHeaderView);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mVendorId = bundle.getString(BUNDLE_VENDOR_ID);
                mVendorDisplayName = bundle.getString(BUNDLE_VENDOR_DISPLAY_NAME);
            }
        }
    }

    private void initListener() {
        filterSortBottomView.hideBottomSlideToTop(true);
        filterSortBottomView.hideSwitchAndFilterBar(true);
        mIVBottomSlideToTop.setOnClickListener(this);
        mTryAgain.setOnClickListener(this);
        mCustomXListView.setXListViewListener(this);
        mCustomXListView.setPullRefreshEnable(false);
        mCustomXListView.setPullLoadEnable(false);
        mCustomXListView.setHeaderDividersEnabled(false);
        mCustomXListView.setFooterDividersEnabled(false);
        mCustomXListView.setOnScrollListener(new CustomXListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!mIsShowSwitchFilterBar) {
                    return;
                }
                filterSortBottomView.setFilterShow(scrollState, new ProductListFilterHideCallBack() {
                    @Override
                    public void callBack() {
                    }
                });
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((mOffset * (LIMIT / 2) - firstVisibleItem) <= (LIMIT / 2 - 1) && TYPE == NONE) {
                    mSearchType = SEARCH_TYPE_LOADMORE;
                    if (!mStopSearch) {
                        //现在是会连续加载两次，并且是同样数据
                        mCustomXListView.setLoadMoreProgress();
                    }
                }
                //如果是顶部，不显示 slide to top buttom
                if (view.getFirstVisiblePosition() == 0) {
                    filterSortBottomView.hideBottomSlideToTop(true);
                } else {
                    filterSortBottomView.hideBottomSlideToTop(false);
                }
                if (firstVisibleItem > 1) {
                    mIsShowSwitchFilterBar = true;
                    filterSortBottomView.getBottomBar().setVisibility(View.VISIBLE);
                } else {
                    mIsShowSwitchFilterBar = false;
                    filterSortBottomView.getBottomBar().setVisibility(View.GONE);
                }
                filterSortBottomView.setIsShowSwitchFilterBar(mIsShowSwitchFilterBar);

            }
        });

    }

    private void initView() {
        mConnectionLayout = findViewById(R.id.connectionBreaks);
        mTryAgain = (LinearLayout) findViewById(R.id.try_again);
        CustomTextView mCtvHeaderBarTitle = (CustomTextView) findViewById(R.id.ctvHeaderBarTitle);
        mCustomXListView = (CustomXListView) findViewById(R.id.cxlvProductList);

        //filter&sort

        RelativeLayout mTopFilterAndSortBarRL = (RelativeLayout) findViewById(R.id.top_switch_and_filter_bar);
        mTopViewToggleIV = (ImageView) findViewById(R.id.iv_view_toggle_top);
        LinearLayout mTopFilterLL = (LinearLayout) findViewById(R.id.ll_filter_top);
        LinearLayout mTopSortLL = (LinearLayout) findViewById(R.id.ll_sort_top);
        mTopFilterLL.setOnClickListener(this);
        mTopSortLL.setOnClickListener(this);
        mTopViewToggleIV.setOnClickListener(this);

        FrameLayout mFlFilterSortContainer = (FrameLayout) findViewById(R.id.flFilterSortContainer);
        mIVBottomSlideToTop = (ImageView) findViewById(R.id.iv_bottom_slideto_top);

        mRlNodata = findViewById(R.id.rlNodata);
        mTryAgain = (LinearLayout) findViewById(R.id.try_again);
        mCustomXListView = (CustomXListView) findViewById(R.id.cxlvProductList);
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

    public void onBackPressed() {
        if (filterSortHelper.isAnyActive()) {
            filterSortHelper.hideVisibleFragments();
            resetCurrentFilterSortTabIndex();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (!filterSortBottomView.showFilter) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_bottom_slideto_top:
                if (mCustomXListView != null) {
                    mCustomXListView.setSelection(0);
                }
                break;
            case R.id.try_again:
                mConnectionLayout.setVisibility(View.GONE);
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
        mMerchantProductListAdapter.notifyDataSetChanged();
    }

    public void onFilterWidgetClick(boolean show) {
        filterSortHelper.onFilterClicked(show, createBundle());
    }

    public void onSortWidgetClick(boolean show) {
        filterSortHelper.onSortClicked(show, createBundle());
    }

    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(MerchantStoreFrontActivity.ACTIVITY_TYPE_PRODUCTLIST_MERCHANT);
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
        mSearchType = SEARCH_TYPE_REFRESH;
        TYPE = LOADING;
        mDialog = JViewUtils.showProgressDialog(MerchantStoreFrontActivity.this);
        search();
    }

    @Override
    public void onLoadMore() {
        mSearchType = SEARCH_TYPE_LOADMORE;
        search();
    }

    @Override
    public void onCancelClick(View view) {
        filterSortHelper.hideVisibleFragments();
        resetCurrentFilterSortTabIndex();
    }

    public void filterData() {
        mMerchantItemReturnEntityArrayList.clear();
        mMerchantProductListAdapter.notifyDataSetChanged();
        mCustomXListView.stopLoadMore();
        mCustomXListView.setPullLoadEnable(false);
        mSearchType = SEARCH_TYPE_INIT;
        mOffset = 0;
        mDialog = JViewUtils.showProgressDialog(MerchantStoreFrontActivity.this);
        search();
    }

    @Override
    public void onFilterSortListItemClick(int type, Object object) {
        filterSortHelper.hideVisibleFragments();
        //refresh data call api
        filterData();
        resetCurrentFilterSortTabIndex();
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return mCurrentFilterSortTabIndex;
    }

    @Override
    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int type, int index, long minPrice, long maxPrice) {
        setSVRAppserviceCProductSearchParameterMinPriceMaxPrice(minPrice, maxPrice);
    }

    public class SearchResultHandler extends Handler {
        public static final int TYPE_INTERFACE_FAILIRE = 1;
        public static final int TYPE_INTERFACE_SUCCESS_OK = 2;
        private final WeakReference<MerchantStoreFrontActivity> mActivity;

        public SearchResultHandler(MerchantStoreFrontActivity activity) {
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
            if (mActivity.get().SEARCH_TYPE_LOADMORE == mActivity.get().mSearchType) {
                mActivity.get().mOffset = mActivity.get().mOffset - 1;
                mActivity.get().mCustomXListView.stopLoadMore();
//                if (!JDataUtils.errorMsgHandler(mActivity.get(), (String) msg.obj)) {
//                    JViewUtils.showToast(mActivity.get(), null, (String) msg.obj);
//                }
            } else if (mActivity.get().SEARCH_TYPE_INIT == mActivity.get().mSearchType || mActivity.get().SEARCH_TYPE_REFRESH == mActivity.get().mSearchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                mActivity.get().mRlNodata.setVisibility(View.VISIBLE);
                mActivity.get().mCustomXListView.setVisibility(View.GONE);
            } else if (mActivity.get().SEARCH_TYPE_REFRESH == mActivity.get().mSearchType) {
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
            if (mActivity.get().SEARCH_TYPE_LOADMORE == mActivity.get().mSearchType) {
                SVRAppserviceProductMerchantReturnEntity merchantEntity = null;
                try {
                    merchantEntity = (SVRAppserviceProductMerchantReturnEntity) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                if (merchantEntity == null || merchantEntity.getResults().size() < 0) {
                    mActivity.get().mCustomXListView.setPullLoadEnable(false);
                    if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                        mActivity.get().mDialog.dismiss();
                    }
                } else {
                    mActivity.get().mCustomXListView.setPullLoadEnable(true);

                }
                mActivity.get().mMerchantItemReturnEntityArrayList.addAll(merchantEntity.getResults());
                mActivity.get().mMerchantProductListAdapter.notifyDataSetChanged();
                mActivity.get().mCustomXListView.stopLoadMore();
            } else if (mActivity.get().SEARCH_TYPE_INIT == mActivity.get().mSearchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                SVRAppserviceProductMerchantReturnEntity merchantEntity = null;
                try {
                    merchantEntity = (SVRAppserviceProductMerchantReturnEntity) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }
                if (merchantEntity == null || merchantEntity.getResults().size() < mActivity.get().LIMIT) {
                    mActivity.get().mCustomXListView.setPullRefreshEnable(false);
                    mActivity.get().mCustomXListView.setPullLoadEnable(false);
                } else {
                    mActivity.get().mCustomXListView.setPullRefreshEnable(true);
                    mActivity.get().mCustomXListView.setPullLoadEnable(true);
                }
                mActivity.get().mMerchantItemReturnEntityArrayList.clear();
                mActivity.get().mMerchantItemReturnEntityArrayList.addAll(merchantEntity.getResults());
                mActivity.get().mMerchantProductListAdapter.notifyDataSetChanged();
                filterSortBottomView.hideSwitchAndFilterBar(false);
                //hideSwitchAndFilterBar(false);
            } else if (mActivity.get().SEARCH_TYPE_REFRESH == mActivity.get().mSearchType) {
                if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                    mActivity.get().mDialog.dismiss();
                }
                SVRAppserviceProductMerchantReturnEntity merchantEntity = null;
                try {
                    merchantEntity = (SVRAppserviceProductMerchantReturnEntity) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(mActivity.get().TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }
                mActivity.get().mMerchantItemReturnEntityArrayList.clear();
                mActivity.get().mMerchantItemReturnEntityArrayList.addAll(merchantEntity.getResults());
                mActivity.get().mMerchantProductListAdapter.notifyDataSetChanged();
            }
        }
    }

    private static class DataHandler extends Handler {
        private final WeakReference<MerchantStoreFrontActivity> mActivity;

        public DataHandler(MerchantStoreFrontActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_MERCHANTDETAILS:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        mActivity.get().mRlNodata.setVisibility(View.INVISIBLE);
                        mActivity.get().mConnectionLayout.setVisibility(View.GONE);
                        mActivity.get().TYPE = mActivity.get().NONE;
                        mMerchantReturnEntity = (SVRAppserviceProductMerchantReturnEntity) msg.obj;
                        if (mMerchantReturnEntity != null && mMerchantReturnEntity.getResults() != null && mMerchantReturnEntity.getResults().size() > 0) {
                            if (mActivity.get().mIsFirst) {
                                mActivity.get().initListViewHeader(mMerchantReturnEntity);
                                mActivity.get().mIsFirst = false;
                            }
                            mActivity.get().setMerchantDetailWhenResponse(mMerchantReturnEntity);
                        } else {
                            if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                                mActivity.get().mDialog.dismiss();
                            }
                            Message msg1 = new Message();
                            msg1.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            mActivity.get().mSearchResultHandler.sendMessage(msg1);

                            mActivity.get().mCustomXListView.stopLoadMore();
                            mActivity.get().mCustomXListView.setPullLoadEnable(false);
                            mActivity.get().mRlNodata.setVisibility(View.VISIBLE);
                            mActivity.get().TYPE = mActivity.get().NO_DATA;
                        }
                    } else {
                        // Fail
                        if (mActivity.get().TYPE != mActivity.get().NO_DATA) {
                            mActivity.get().TYPE = mActivity.get().ERROR;
                        }
                        if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                            mActivity.get().mDialog.dismiss();
                        }
                        mActivity.get().mCustomXListView.stopLoadMore();
                        String errorMsg = (String) msg.obj;
                        Message faildMsg = new Message();
                        faildMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        faildMsg.obj = errorMsg + "";
                        mActivity.get().mSearchResultHandler.sendMessage(faildMsg);
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    if (mActivity.get().mDialog != null && mActivity.get().mDialog.isShowing()) {
                        mActivity.get().mDialog.dismiss();
                    }
                    if (mActivity.get().TYPE != mActivity.get().NO_DATA) {
                        mActivity.get().TYPE = mActivity.get().ERROR;
                    }
                    if (mActivity.get().mMerchantItemReturnEntityArrayList == null || mActivity.get().mMerchantItemReturnEntityArrayList.size() == 0) {
                        mActivity.get().mCustomXListView.stopLoadMore();
                        mActivity.get().mConnectionLayout.setVisibility(View.VISIBLE);
                    } else {
                        Message msg1 = new Message();
                        msg1.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        msg1.obj = mActivity.get().PROMPT_ERROR_NOINTERNET;
                        mActivity.get().mSearchResultHandler.sendMessage(msg1);
                    }
                    break;
            }
        }
    }

    /**
     * 加载
     */
    private void search() {
        JLogUtils.d("jay", "search" + mOffset);
        TYPE = LOADING;
        mOffset = mOffset + 1;
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
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(MerchantStoreFrontActivity.this)) {
            sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(MerchantStoreFrontActivity.this).getSessionKey();
            mProductDao.getMerchantDetail(order, dir, brand, modelType, price, mVendorId, mOffset + "", LIMIT + "", sessionKey);
        } else {
            mProductDao.getMerchantDetail(order, dir, brand, modelType, price, mVendorId, mOffset + "", LIMIT + "", "");
        }
    }

    /**
     * 数据请求成功
     *
     * @param mMerchantReturnEntity
     */
    public SVRAppserviceProductSearchFacetsReturnEntity facets;

    private void setMerchantDetailWhenResponse(SVRAppserviceProductMerchantReturnEntity merchantReturnEntity) {
        try {
            if (facets == null) {
                facets = new SVRAppserviceProductSearchFacetsReturnEntity();
            }
            if (merchantReturnEntity.getFacets() != null) {
                facets.setCategory_filter(merchantReturnEntity.getFacets().getCategory_filter());
                facets.setPrice_filter(merchantReturnEntity.getFacets().getPrice_filter());
                facets.setBrand_filter(merchantReturnEntity.getFacets().getBrand_filter());
                facets.setModel_type_filter(merchantReturnEntity.getFacets().getModel_type_filter());
                facets.setSort_filter(merchantReturnEntity.getFacets().getSort_filter());
            }
        } catch (Exception ex) {
            JLogUtils.e(TAG, "search -> onSuccess", ex);
        }
        Message seccessMsg = new Message();
        seccessMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_SUCCESS_OK;
        seccessMsg.obj = merchantReturnEntity;
        mSearchResultHandler.sendMessage(seccessMsg);
        mCustomXListView.setVisibility(View.VISIBLE);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            onBackPressed();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        if (filterSortBottomView.filterHandler != null) {
            filterSortBottomView.filterHandler = null;
        }
        if (mProductDao != null) {
            mProductDao.cancelHttpByTag(TAG);
        }
        if (mDataHandler != null) {
            mDataHandler.removeCallbacksAndMessages(null);
        }
        mProductDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }
}

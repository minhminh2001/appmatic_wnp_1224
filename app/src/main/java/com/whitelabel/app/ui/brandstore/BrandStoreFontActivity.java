package com.whitelabel.app.ui.brandstore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.BaseActivitySearchCart;
import com.whitelabel.app.activity.IFilterSortActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.MerchantStoreFrontActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.fragment.ProductListFilterFragment;
import com.whitelabel.app.fragment.ProductListSortFragment;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.listener.OnSingleClickListener;
import com.whitelabel.app.model.BrandStoreModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.FilterSortBottomView;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerViewV2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BrandStoreFontActivity extends BaseActivitySearchCart<BrandStoreContract.Presenter> implements BrandStoreContract.View,
        View.OnClickListener,OnFilterSortFragmentListener,
        FilterSortBottomView.FilterSortBottomViewCallBack,
        IFilterSortActivity,RefreshLoadMoreRecyclerViewV2.OnLoadMoreListener {
    @BindView(R.id.iv_bottom_slideto_top)
    ImageView ivBottomSlidetoTop;
    @BindView(R.id.vBottomBarDivider)
    View vBottomBarDivider;
    @BindView(R.id.ctvBottomBarFilter)
    CustomTextView ctvBottomBarFilter;
    @BindView(R.id.tv_filter_plus_animate)
    TextView tvFilterPlusAnimate;
    @BindView(R.id.rl_filter_plus)
    RelativeLayout rlFilterPlus;
    @BindView(R.id.rlBottomBarFilter)
    RelativeLayout rlBottomBarFilter;
    @BindView(R.id.ctvBottomBarSort)
    CustomTextView ctvBottomBarSort;
    @BindView(R.id.tv_sort_plus_animate)
    TextView tvSortPlusAnimate;
    @BindView(R.id.rl_sort_plus)
    RelativeLayout rlSortPlus;
    @BindView(R.id.rlBottomBarSort)
    RelativeLayout rlBottomBarSort;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;
    @BindView(R.id.rl_bottom_bar1)
    RelativeLayout llBottomBar;
    @BindView(R.id.iv_error)
    ImageButton ivError;
    @BindView(R.id.ctv_error_header)
    CustomTextView ctvErrorHeader;
    @BindView(R.id.ctv_error_subheader)
    CustomTextView ctvErrorSubheader;
    @BindView(R.id.ll_error_message)
    LinearLayout llErrorMessage;
    @BindView(R.id.imageButtonServer)
    ImageButton imageButtonServer;
    @BindView(R.id.customTextViewServer)
    CustomTextView customTextViewServer;
    @BindView(R.id.iv_try_again)
    ImageView ivTryAgain;
    @BindView(R.id.btn_try_again)
    CustomButton btnTryAgain;
    @BindView(R.id.try_again)
    LinearLayout tryAgain;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.connectionBreaks)
    RelativeLayout connectionBreaks;
    @BindView(R.id.v_notdata_divider)
    View vNotdataDivider;
    @BindView(R.id.rlNodata)
    RelativeLayout rlNodata;
    @BindView(R.id.flFilterSortContainer)
    FrameLayout flFilterSortContainer;
    @BindView(R.id.iv_view_toggle_top)
    ImageView mIvViewToggleTop;
    @BindView(R.id.ll_filter_top)
    LinearLayout mLlFilterTop;
    @BindView(R.id.ll_sort_top)
    LinearLayout mLlSortTop;
    @BindView(R.id.top_switch_and_filter_bar)
    RelativeLayout mTopSwitchAndFilterBar;
    private String mBrandId;
    private String mBrandName;
    public static final String EXTRA_BRAND_ID = "brand_id";
    public static final String EXTRA_BRAND_NAME = "brand_name";
    private static final int REQUEST_PRE_ADD_WISH = 200;
    @BindView(R.id.rlm_recycler)
    RefreshLoadMoreRecyclerViewV2 rlmRecycler;
    private final List<SVRAppserviceProductSearchResultsItemReturnEntity> mProducts = new ArrayList<>();
    private final SVRAppserviceProductSearchParameter mSVRAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
    private int mOffset = 1;
    private final int PAGE_LIMIT = 10;
    private BrandStoreAdapter mAdapter;
    private boolean mFirstLoadHeader = true;
    private ImageLoader mImageLoader;
    private static final int REQUEST_ADD_WISH = 101;
    public final static int ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE = 5;
    private int mCurrentFilterSortTabIndex;
    private final static int TABBAR_INDEX_NONE = -1;
    private final static int TABBAR_INDEX_FILTER = 1;
    private final static int TABBAR_INDEX_SORT = 2;
    private SVRAppserviceProductSearchFacetsReturnEntity mFaces;
    private FilterSortBottomView filterSortBottomView;
    private FilterSortHelper filterSortHelper;
    private boolean isDoubleCol = true;
    private int mPreAddWishListPostion = -1;
    public final int TYPE_SORT = 0;
    public final int TYPE_FILTER = 1;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_bottom_slideto_top:
                rlmRecycler.getLayoutManager().scrollToPosition(0);
                break;
            case R.id.try_again:
                showProgressDialog();
                connectionBreaks.setVisibility(View.GONE);
                mOffset = 1;
                search();
                break;
            case R.id.iv_view_toggle_top:
                toggleViewOption(mIvViewToggleTop);
                break;
            case R.id.ll_filter_top:
                filterSortOption(TYPE_FILTER);
                break;
            case R.id.ll_sort_top:
                filterSortOption(TYPE_SORT);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        search();
    }

    @Override
    public void showNoDataView() {
        rlNodata.setVisibility(View.VISIBLE);
        rlmRecycler.setVisibility(View.GONE);
        filterSortBottomView.hideBottomSlideToTop(true);
        filterSortBottomView.hideSwitchAndFilterBar(true);
    }

    @Override
    public void showNetworkErrorView(String errorMsg) {
        closeProgressDialog();
        if (mOffset == 1) {
            connectionBreaks.setVisibility(View.VISIBLE);
            filterSortBottomView.hideBottomSlideToTop(true);
            filterSortBottomView.hideSwitchAndFilterBar(true);
        } else {
            Toast.makeText(BrandStoreFontActivity.this, errorMsg + "", Toast.LENGTH_LONG).show();
        }

        rlmRecycler.stopLoadMore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brank_store_font);
        ButterKnife.bind(this);
        mBrandId = getIntent().getStringExtra(EXTRA_BRAND_ID);
        mBrandName = getIntent().getStringExtra(EXTRA_BRAND_NAME);
        mImageLoader = new ImageLoader(this);
        tryAgain.setOnClickListener(this);
        mLlFilterTop.setOnClickListener(this);
        mLlSortTop.setOnClickListener(this);
        mIvViewToggleTop.setOnClickListener(this);
        initToolBar();
        initBottomBar();
        initRecyclerView();
        showProgressDialog();
        search();
    }

    @Override
    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int type, int index, long minPrice, long maxPrice) {
        setSVRAppserviceCProductSearchParameterMinPriceMaxPrice(minPrice, maxPrice);
    }

    @Override
    public void onCancelClick(View view) {
        filterSortHelper.hideVisibleFragments();
        resetCurrentFilterSortTabIndex();
    }

    private void resetCurrentFilterSortTabIndex() {
        mCurrentFilterSortTabIndex = TABBAR_INDEX_NONE;
        switchBottomBar();
    }

    public void setSVRAppserviceProductSearchParameterType(String typeValue) {
        mSVRAppserviceProductSearchParameter.setModel_type(typeValue);
    }

    public void setSVRAppserviceProductSearchParameterBrand(String brandValue) {
        mSVRAppserviceProductSearchParameter.setBrand(brandValue);
    }

    private void setSVRAppserviceCProductSearchParameterMinPriceMaxPrice(long minPrice, long maxPrice) {
        mSVRAppserviceProductSearchParameter.setPrice(minPrice + "-" + maxPrice);
    }

    private void switchBottomBar() {
//        if (TABBAR_INDEX_FILTER == mCurrentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(JToolUtils.getColor(R.color.purple66006E));
//            AnimUtil.animatePlusSign(tvFilterPlusAnimate, true, this);
//            //打开filter时隐藏 toTopBar
//            filterSortBottomView.hideBottomSlideToTop(true);
//            ctvBottomBarSort.setTextColor(JToolUtils.getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvSortPlusAnimate, false, this);
//        } else if (TABBAR_INDEX_SORT == mCurrentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(JToolUtils.getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvFilterPlusAnimate, false, this);
//            filterSortBottomView.hideBottomSlideToTop(true);
//            ctvBottomBarSort.setTextColor(JToolUtils.getColor(R.color.purple66006E));
//            AnimUtil.animatePlusSign(tvSortPlusAnimate, true, this);
//        } else if (TABBAR_INDEX_NONE == mCurrentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(JToolUtils.getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvFilterPlusAnimate, false, this);
//            filterSortBottomView.hideBottomSlideToTop(false);
//            ctvBottomBarSort.setTextColor(JToolUtils.getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvSortPlusAnimate, false, this);
//        }
    }

    @Override
    public void onFilterSortListItemClick(int type, Object object) {
        rlmRecycler.stopLoadMore();
        filterSortHelper.hideVisibleFragments();
        showProgressDialog();
        filterSortBottomView.hideBottomSlideToTop(true);
        filterData();
        resetCurrentFilterSortTabIndex();
    }

    private void filterData() {
        mProducts.clear();
        mAdapter.notifyDataSetChanged();
        mOffset = 1;
        mFirstLoadHeader = true;
        search();
    }

    private void search() {
        String order = mSVRAppserviceProductSearchParameter.getOrder();
        String dir = mSVRAppserviceProductSearchParameter.getDir();
        String price = mSVRAppserviceProductSearchParameter.getPrice();
        String modelType = mSVRAppserviceProductSearchParameter.getModel_type();
        mPresenter.getBrandProductList(mBrandId, mOffset, PAGE_LIMIT, price, order, dir, modelType);
    }

    @Override
    public void onAnimationFinished(Fragment fragment) {
        filterSortHelper.hideContainer(fragment);
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return mCurrentFilterSortTabIndex;
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

    private class SingleClickListener extends OnSingleClickListener {
        @Override
        public void onSingleClick(View view) {
            if (!filterSortBottomView.showFilter) {
                return;
            }
            switch (view.getId()) {
                case R.id.rlBottomBarFilter:
                    filterSortBottomView.setFilterStatus(0, null);
                    onFilterWidgetClick(mCurrentFilterSortTabIndex != TABBAR_INDEX_FILTER);
                    mCurrentFilterSortTabIndex = mCurrentFilterSortTabIndex == TABBAR_INDEX_FILTER ? TABBAR_INDEX_NONE : TABBAR_INDEX_FILTER;
                    switchBottomBar();
                    break;

                case R.id.rlBottomBarSort:
                    onSortWidgetClick(mCurrentFilterSortTabIndex != TABBAR_INDEX_SORT);
                    mCurrentFilterSortTabIndex = mCurrentFilterSortTabIndex == TABBAR_INDEX_SORT ? TABBAR_INDEX_NONE : TABBAR_INDEX_SORT;
                    switchBottomBar();
                    break;
                default:
                    break;
            }
        }
    }

    public void setSVRAppserviceProductSearchParameterSort(String sortValue) {
        String orderValue = null;
        String dirValue = null;
        if (!JDataUtils.isEmpty(sortValue)) {
            String[] sortValueArray = sortValue.split("__");
            if (sortValueArray.length >= 2) {
                orderValue = sortValueArray[0];
                dirValue = sortValueArray[1];
            }
        }
        mSVRAppserviceProductSearchParameter.setOrder(orderValue);
        mSVRAppserviceProductSearchParameter.setDir(dirValue);
    }

    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(ACTIVITY_TYPE_PRODUCTLIST_BRANDSTORE);
        filterSortPageEntity.setFacets(mFaces);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", filterSortPageEntity);
        return bundle;
    }

    private void onFilterWidgetClick(boolean show) {
        filterSortHelper.onFilterClicked(show, createBundle());
    }

    private void onSortWidgetClick(boolean show) {
        filterSortHelper.onSortClicked(show, createBundle());
    }

    private void initBottomBar() {
        mCurrentFilterSortTabIndex = TABBAR_INDEX_NONE;
        SingleClickListener singleClickListener = new SingleClickListener();
        ivBottomSlidetoTop.setOnClickListener(this);
        rlBottomBarFilter.setOnClickListener(singleClickListener);
        rlBottomBarSort.setOnClickListener(singleClickListener);
        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(mTopSwitchAndFilterBar, ivBottomSlidetoTop, this);
        filterSortBottomView.hideBottomSlideToTop(true);
        ProductListFilterFragment filterFragment = new ProductListFilterFragment();
        filterFragment.setCanUseBrand(false);
        filterFragment.setFragmentListener(this);
        ProductListSortFragment sortFragment = new ProductListSortFragment();
        sortFragment.setFragmentListener(this);
        filterSortHelper = new FilterSortHelper(this, sortFragment, filterFragment, flFilterSortContainer, R.id.flFilterSortContainer);
    }

    public void onBackPressed() {
        if (filterSortHelper.isAnyActive()) {
            filterSortHelper.hideVisibleFragments();
            resetCurrentFilterSortTabIndex();
        } else {
           super.onBackPressed();
        }
    }

    private final GridLayoutManager.SpanSizeLookup mTwoRowSpan = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            if (position == 0 || position == mProducts.size() + 1) {
                return 2;
            }
            return 1;
        }
    };
    private final GridLayoutManager.SpanSizeLookup mSingleRowSpan = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return 2;
        }
    };

    private void initRecyclerView() {
        mAdapter = new BrandStoreAdapter(mProducts, mImageLoader);
        rlmRecycler.setSpanSizeLookUp(mTwoRowSpan);
        rlmRecycler.setFillerSortBottomView(filterSortBottomView);
        ((SimpleItemAnimator) rlmRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        rlmRecycler.setLoadMoreEnable(true);
        rlmRecycler.setOnLoadMoreListener(this);
        mAdapter.setOnItemClickListener(new BrandStoreAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(BrandStoreAdapter.ItemViewHolder itemViewHolder, int position) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(BrandStoreFontActivity.this, ProductDetailActivity.class);
                    intent.putExtras(getProductBundle(mProducts.get(position)));
                    ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(BrandStoreFontActivity.this,
                            itemViewHolder.ivProductImage, WhiteLabelApplication.getInstance().getString(R.string.activity_image_trans));
                    ActivityCompat.startActivityForResult(BrandStoreFontActivity.this, intent, MerchantStoreFrontActivity.RESULT_ADD_WISH, aop.toBundle());
                } else {
                    startNextActivityForResult(getProductBundle(mProducts.get(position)), ProductDetailActivity.class, REQUEST_ADD_WISH, false);
                }
            }
        });
        mAdapter.setOnFilterSortBarListener(new BrandStoreAdapter.OnFilterSortBarListener() {
            @Override
            public void onSwitchViewClick(View view) {
                ImageView imageView = (ImageView) view;
                toggleViewOption(imageView);
            }

            @Override
            public void onFilterClick() {
                filterSortOption(TYPE_FILTER);
            }

            @Override
            public void onSortClick() {
                filterSortOption(TYPE_SORT);
            }
        });
        rlmRecycler.setAdapter(mAdapter);
    }

    private void toggleViewOption(ImageView imageView) {
        if (isDoubleCol) {
            imageView.setImageResource(R.mipmap.ic_view_double);
            mIvViewToggleTop.setImageResource(R.mipmap.ic_view_double);
            rlmRecycler.setSpanSizeLookUp(mSingleRowSpan);
        } else {
            rlmRecycler.setSpanSizeLookUp(mTwoRowSpan);
            imageView.setImageResource(R.mipmap.ic_view_single);
            mIvViewToggleTop.setImageResource(R.mipmap.ic_view_single);
        }
        isDoubleCol = !isDoubleCol;
        mAdapter.setTwoRow(isDoubleCol);
        mAdapter.notifyDataSetChanged();
    }

    private void addWishlist(ImageView icon1, ImageView icon2, int position) {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {
            mProducts.get(position).setIsLike(1);
            AnimUtil.setWishIconColorToPurple(icon1, icon2);
            mPresenter.addWistList(mProducts.get(position));
        } else {
            mPreAddWishListPostion = position;
            Intent intent = new Intent();
            intent.setClass(this, LoginRegisterActivity.class);
            startActivityForResult(intent, REQUEST_PRE_ADD_WISH);
            overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
        }
    }

    private void deleteWishlist(SVRAppserviceProductSearchResultsItemReturnEntity bean, ImageView icon1) {
        AnimUtil.setWishIconColorToBlank(icon1);
        bean.setIsLike(0);
        if (!TextUtils.isEmpty(bean.getItem_id())) {
            mPresenter.deleteWishListByItemId(bean);
        }
    }

    private Bundle getProductBundle(SVRAppserviceProductSearchResultsItemReturnEntity product) {
        product.setBrand(mBrandName);
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getProductId());
        bundle.putString("from", "from_product_list");
        bundle.putSerializable("product_info", mPresenter.getProductListItemToProductDetailsEntity(product));
        bundle.putString("imageurl", product.getSmallImage());
        return bundle;
    }

    private void refreshAll(BrandStoreModel brandStoreModel) {
        mAdapter.setHeaderContent(brandStoreModel);
        mFirstLoadHeader = false;
        refreshBody(brandStoreModel);
    }

    @Override
    public void showContentView(BrandStoreModel brandStoreModel, int currPage) {
        if (currPage != mOffset) return;
        rlmRecycler.setVisibility(View.VISIBLE);
        connectionBreaks.setVisibility(View.GONE);
        mFaces = brandStoreModel.getAllProducts().getFacets();
        if (mFaces == null) {
            mFaces = new SVRAppserviceProductSearchFacetsReturnEntity();
        }
        if (mFirstLoadHeader) {
            refreshAll(brandStoreModel);
        } else {
            refreshBody(brandStoreModel);
        }
    }

    private void refreshBody(BrandStoreModel brandStoreModel) {
        if (mOffset == 1) {
            mProducts.clear();
        }
        if (brandStoreModel.getAllProducts() != null && brandStoreModel.getAllProducts().getResults() != null && brandStoreModel.getAllProducts().getResults().size() > 0) {
            if (brandStoreModel.getAllProducts().getResults().size() < PAGE_LIMIT) {
                rlmRecycler.setLoadMoreEnable(false);
            } else {
                rlmRecycler.setLoadMoreEnable(true);
            }
            mProducts.addAll(brandStoreModel.getAllProducts().getResults());
            mOffset++;
        } else {
            rlmRecycler.setLoadMoreEnable(false);
        }
        if (mPreAddWishListPostion != -1 && mPreAddWishListPostion < mProducts.size()) {
            mProducts.get(mPreAddWishListPostion).setIsLike(1);
            mPreAddWishListPostion = -1;
        }
        mAdapter.notifyDataSetChanged();
        rlmRecycler.stopLoadMore();
    }

    private void initToolBar() {
        if (!TextUtils.isEmpty(mBrandName)) {
            setTitle(JDataUtils.getFirstLetterToUpperCase(mBrandName));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_PRE_ADD_WISH == requestCode && resultCode == LoginRegisterEmailLoginFragment.RESULTCODE) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {
                mPresenter.addWistList(mProducts.get(mPreAddWishListPostion));
                mOffset = 1;
                mProducts.get(mPreAddWishListPostion).setIsLike(1);
                mAdapter.notifyDataSetChanged();
                showProgressDialog();
                search();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public BrandStoreContract.Presenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new BrandStorePresenter();
        }
        return mPresenter;
    }

}

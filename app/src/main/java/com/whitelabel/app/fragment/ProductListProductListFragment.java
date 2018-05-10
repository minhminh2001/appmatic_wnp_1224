package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.ProductListAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.FragmentOnAdapterCallBack;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.LanguageUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.StoreUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomXListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by imaginato on 2015/8/10.
 */
public class ProductListProductListFragment extends ProductListBaseFragment implements FragmentOnAdapterCallBack, CustomXListView.IXListViewListener, View.OnClickListener {

    public static final int SEARCH_TYPE_INIT = 1;
    public static final int SEARCH_TYPE_KEYWORDS = 2;
    public static final int SEARCH_TYPE_FILTER = 3;
    public static final int SEARCH_TYPE_SORT = 4;
    public static final int SEARCH_TYPE_REFRESH = 5;
    public static final int SEARCH_TYPE_LOADMORE = 6;
    public static final int PRODUCT_LIST_FRAGMENT_REQUEST_CODE =100;
    private View contentView;
    private ProductListActivity productListActivity;
    private ProductListCategoryLandingFragment mLandingFragment;
    private int parentFragmentType = -1;
    private int categoryFragmentPosition = -1;
    private CustomXListView cxlvProductList;
    private RelativeLayout rlNodata;
    private ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList;
    private ProductListAdapter productListAdapter;
    private int currentP;
    private final int LOADING = 2;
    private final int NONE = 1;
    private final int NOTDATA = 3;
    private final int ERROR = 4;
    private int TYPE = 1;
    private String PROMPT_ERROR_NOINTERNET;
    private int searchType;
    private SearchResultHandler searchResultHandler;
    private RequestErrorHelper requestErrorHelper;
    private View connectionLayout;
    private LinearLayout tryAgain;
    private SVRAppserviceProductSearchFacetsReturnEntity searchReturnEntityFacets;
//    private boolean showConnectionLayout = true;
    private ProductDao mProductDao;
    private DataHandler dataHandler;
    private ImageLoader mImageLoader;
    private RelativeLayout mRlViewbar;
    private ImageView mIvViewToggle;
    private TextView mTVProductTotalCount;
    private boolean mIsFirst=true;
    private String TAG = this.getClass().getSimpleName();
    public TempCategoryBean tempCategoryBean;
    static SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity;
    private long gaTrackTimeStart = 0L;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        productListActivity = (ProductListActivity) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_productlist_productlist, null);
        tempCategoryBean=TempCategoryBean.getInstance();
        mImageLoader = new ImageLoader(productListActivity);
        isPrepared = true;
        init();
        TempCategoryBean tempCategoryBean=TempCategoryBean.getInstance();
        return contentView;
    }

    @Override
    public void startActivityForResultCallBack(Intent intent, int code) {
        this.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //如果 pdp页面登陆了，needRefreshWhenBackPressed=true,刷新当前页面所有product,否则只刷新指定product的isLike字段
                if (!data.getBooleanExtra("needRefreshWhenBackPressed", false)) {
                    String productId = data.getStringExtra("productId");
                    String itemId = data.getStringExtra("itemId");
                    int isLike = data.getIntExtra("isLike", -1);
                    Logger.e("peoductList productId :"+productId+" isLike:"+isLike);
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
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(productListActivity)) {
//                productListActivity.changeOperateProductIdPrecacheStatus(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreWishIconByPDPResult(String productId, int isLike, String itemId) {
        //pdp 页面，isLike或itemId有变动，就刷新
        Iterator<SVRAppserviceProductSearchResultsItemReturnEntity> itemReturnEntityIterator = productItemEntityArrayList.iterator();
        while (itemReturnEntityIterator.hasNext()) {
            SVRAppserviceProductSearchResultsItemReturnEntity entity = itemReturnEntityIterator.next();
            if (entity.getProductId().equals(productId)) {
                entity.setIsLike(isLike);
                entity.setItemId(itemId);
                if (productListAdapter!=null){
                    productListAdapter.notifyDataSetChanged();
                }
                continue;
            }
        }
    }
    public void showViewSwitch(boolean show) {
        if (mRlViewbar != null) {
            if(show){
                mRlViewbar.setVisibility(View.VISIBLE);
            }else{
                mRlViewbar.setVisibility(View.GONE);
            }
        }

    }
    private void initListViewHeader(){
        View view = LayoutInflater.from(productListActivity).inflate(R.layout.header_product_list_switch_and_filter_bar, null);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,JDataUtils.dp2Px(40));
        view.setLayoutParams(params);
        mRlViewbar = (RelativeLayout) view.findViewById(R.id.rl_viewbar);
        mIvViewToggle = (ImageView) view.findViewById(R.id.iv_view_toggle);
        LinearLayout mHeaderFilterLL = (LinearLayout) view.findViewById(R.id.ll_filter);
        LinearLayout mHeaderSortLL = (LinearLayout) view.findViewById(R.id.ll_sort);
        mTVProductTotalCount = (TextView) view.findViewById(R.id.tv_product_total);
        mIvViewToggle.setOnClickListener(this);
        mHeaderFilterLL.setOnClickListener(this);
        mHeaderSortLL.setOnClickListener(this);
        cxlvProductList.addHeaderView(view);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_view_toggle:
                toggleViewOption();
                productListActivity.onViewToggleChanged();
                break;
            case R.id.ll_filter:
                //productListActivity.filterSortOption(productListActivity.TYPE_FILTER);
                productListActivity.showFilerView();
                break;
            case R.id.ll_sort:
                productListActivity.filterSortOption(productListActivity.TYPE_SORT);
                break;
        }
    }

    public void toggleViewOption() {
        if(super.isDoubleCol){
            mIvViewToggle.setImageResource(R.mipmap.ic_view_double);
            mLandingFragment.mTopViewToggleIV.setImageResource(R.mipmap.ic_view_double);
        }else{
            mIvViewToggle.setImageResource(R.mipmap.ic_view_single);
            mLandingFragment.mTopViewToggleIV.setImageResource(R.mipmap.ic_view_single);
        }
        cxlvProductList.setSelection(0);
        super.isDoubleCol=!super.isDoubleCol;
        productListAdapter.notifyDataSetChanged();
    }

    private static class DataHandler extends Handler {

        private final WeakReference<ProductListActivity> mActivity;
        private final WeakReference<ProductListProductListFragment> mFragment;

        public DataHandler(ProductListActivity activity, ProductListProductListFragment fragment) {
            mActivity = new WeakReference<ProductListActivity>(activity);
            mFragment = new WeakReference<ProductListProductListFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_ERROR:
                    if (mFragment.get().TYPE != mFragment.get().NOTDATA) {
                        mFragment.get().TYPE = mFragment.get().ERROR;
                    }
                    if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                        mFragment.get().mDialog.dismiss();
                    }
                    mFragment.get().cxlvProductList.stopLoadMore();
                    if(mFragment.get().currentP==1){
                        mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                    }else {
                        if ((mActivity.get() != null && !mActivity.get().checkIsFinished()) && (mFragment.get().isAdded())){
                            Message errorMsg = new Message();
                            errorMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            errorMsg.obj = mFragment.get().PROMPT_ERROR_NOINTERNET;
                            mFragment.get().searchResultHandler.sendMessage(errorMsg);
                        }
                    }
                    break;
                case ProductDao.REQUEST_PRODUCTSEARCH:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        if ((mActivity.get() != null && !mActivity.get().checkIsFinished()) && (mFragment.get().isAdded())) {
                            if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                                mFragment.get().mDialog.dismiss();
                            }
                            mFragment.get().connectionLayout.setVisibility(View.GONE);
                            mFragment.get().TYPE = mFragment.get().NONE;
                            if ((mActivity.get() != null && !mActivity.get().checkIsFinished()) && (mFragment.get().isAdded())
                                    && (mFragment.get().tempCategoryBean.getSVRAppserviceProductSearchParameterById(mFragment.get().parentFragmentType, mFragment.get().categoryFragmentPosition) != null)
                                    && (mFragment.get().productItemEntityArrayList != null && mFragment.get().productListAdapter != null)) {

                                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productListResult = null;
                                try {
                                    SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity = (SVRAppserviceProductSearchReturnEntity) msg.obj;
                                    productListResult = svrAppserviceProductSearchReturnEntity.getResults();
                                    if (mFragment.get().mIsFirst) {
                                        mFragment.get().initListViewHeader();
                                        mFragment.get().mIsFirst = false;
                                    }
                                    mFragment.get().mTVProductTotalCount.setText(new StringBuffer().append(svrAppserviceProductSearchReturnEntity.getTotal()).append(mFragment.get().getResources().getString(R.string.switch_view_bar_results)));
                                    mFragment.get().clearSearchReturnEntityFacets();
                                    SVRAppserviceProductSearchFacetsReturnEntity facetsReturnEntity = svrAppserviceProductSearchReturnEntity.getFacets();
                                    JLogUtils.i(mFragment.get().TAG, "facetsReturnEntity===" + facetsReturnEntity);
                                    if (facetsReturnEntity != null) {
                                        mFragment.get().getSearchReturnEntityFacets().setCategory_filter(facetsReturnEntity.getCategory_filter());
                                        mFragment.get().getSearchReturnEntityFacets().setPrice_filter(facetsReturnEntity.getPrice_filter());
                                        mFragment.get().getSearchReturnEntityFacets().setBrand_filter(facetsReturnEntity.getBrand_filter());
                                        mFragment.get().getSearchReturnEntityFacets().setModel_type_filter(facetsReturnEntity.getModel_type_filter());
                                        mFragment.get().getSearchReturnEntityFacets().setSort_filter(facetsReturnEntity.getSort_filter());
                                        mFragment.get().getSearchReturnEntityFacets().setField_filter(facetsReturnEntity.getField_filter());
                                    }
                                } catch (Exception ex) {
                                    JLogUtils.e(mFragment.get().TAG, "getProductListFromServer -> onSuccess", ex);
                                }
                                Message successMsg = new Message();
                                successMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_SUCCESS_OK;
                                successMsg.obj = productListResult;
                                mFragment.get().searchResultHandler.sendMessage(successMsg);

                            }
                        }
                    } else {
                        if (mFragment.get().TYPE != mFragment.get().NOTDATA) {
                            mFragment.get().TYPE = mFragment.get().ERROR;
                        }
                        if ((mActivity.get() != null && !mActivity.get().checkIsFinished()) && (mFragment.get().isAdded())) {
                            if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                                mFragment.get().mDialog.dismiss();
                            }
                            mFragment.get().cxlvProductList.stopLoadMore();
                            String errormsg = (String) msg.obj;
                            Message faildMsg = new Message();
                            faildMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            faildMsg.obj = errormsg;
                            mFragment.get().searchResultHandler.sendMessage(faildMsg);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private boolean mHasLoadedOnce = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        JLogUtils.i("Allen", "productListProductListFragment");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            init();
        } else {
            isVisible = false;
        }

    }

    private boolean isPrepared, isVisible;

    private void init() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }

        // GA load speed start
        gaTrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();

        mHasLoadedOnce=true;
        mLandingFragment= (ProductListCategoryLandingFragment) getParentFragment();
        dataHandler=new DataHandler((ProductListActivity) getActivity(),this);
        mProductDao=new ProductDao(TAG,dataHandler);
        connectionLayout=contentView.findViewById(R.id.connectionBreaks);
        requestErrorHelper=new RequestErrorHelper(getContext(),connectionLayout);
        tryAgain= (LinearLayout) contentView.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionLayout.setVisibility(View.GONE);
                //  mDialog.show();
                search();
            }
        });
        searchResultHandler = new SearchResultHandler((ProductListActivity) getActivity(), this);
        PROMPT_ERROR_NOINTERNET = getString(R.string.productlist_list_prompt_error_nointernet);
        currentP = 1;
        parentFragmentType = -1;
        categoryFragmentPosition = -1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentFragmentType = bundle.getInt("fragmentType");
            if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == parentFragmentType) {
                categoryFragmentPosition = bundle.getInt("position");
            } else if (ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == parentFragmentType) {
                categoryFragmentPosition = -1;
            } else {
                categoryFragmentPosition = -1;
            }
        }
        cxlvProductList = (CustomXListView) contentView.findViewById(R.id.cxlvProductList);
        showViewSwitch(false);
        rlNodata = (RelativeLayout) contentView.findViewById(R.id.rlNodata);
        cxlvProductList.setHeaderDividersEnabled(false);
        cxlvProductList.setFooterDividersEnabled(false);
        cxlvProductList.setPullRefreshEnable(false);
        cxlvProductList.setPullLoadEnable(false);
        cxlvProductList.setXListViewListener(this);
        cxlvProductList.setOnScrollListener(new CustomXListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            public boolean isSlideToBottom(CustomXListView lv) {
                if (lv == null) return false;
                return lv.computeVerticalScrollExtent() + lv.computeVerticalScrollOffset() >= lv.computeVerticalScrollRange();
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(!mLandingFragment.mIsShowSwitchFilterBar){
                    return;
                }
                mLandingFragment.filterSortBottomView.setFilterShow(scrollState, new ProductListFilterHideCallBack() {
                    @Override
                    public void callBack() {
                    }
                });
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final SVRAppserviceProductSearchParameter param = tempCategoryBean.getSVRAppserviceProductSearchParameterById(parentFragmentType, categoryFragmentPosition);
                if ((currentP * (param.getLimit() / 2) - firstVisibleItem) <= (param.getLimit() / 2 - 1) && TYPE == NONE) {
                    searchType = SEARCH_TYPE_LOADMORE;
                    cxlvProductList.setLoadMoreProgress();
                    search();
                }
                if (view.getFirstVisiblePosition() == 0) {
                    mLandingFragment.filterSortBottomView.hideBottomSlideToTop(true);
                } else {
                    mLandingFragment.filterSortBottomView.hideBottomSlideToTop(false);
                }
                if(firstVisibleItem>=1){
                    mLandingFragment.mIsShowSwitchFilterBar=true;
//                    filterSortBottomView.hideSwitchAndFilterBar(false);
                    mLandingFragment.mTopFilterAndSortBarRL.setVisibility(View.VISIBLE);
                }else{
                    mLandingFragment.mIsShowSwitchFilterBar = false;
//                    filterSortBottomView.hideSwitchAndFilterBar(true);
                    mLandingFragment.mTopFilterAndSortBarRL.setVisibility(View.GONE);
                }
                mLandingFragment.filterSortBottomView.setIsShowSwitchFilterBar(mLandingFragment.mIsShowSwitchFilterBar);

            }
        });
        productItemEntityArrayList = new ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>();
        productListAdapter = new ProductListAdapter(productListActivity, productItemEntityArrayList, mImageLoader,this, this);
        cxlvProductList.setAdapter(productListAdapter);
        searchType = SEARCH_TYPE_INIT;
        search();
    }

    private void search() {
        getProductListFromServer();
    }

    @Override
    public void onDestroyView() {
        if (mProductDao != null) {
            mProductDao.cancelHttpByTag(TAG);
        }
        if (dataHandler != null) {
            dataHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();

    }

    private Dialog mDialog;

    private void getProductListFromServer() {
        if (productListActivity == null || productListActivity.checkIsFinished() || !isAdded()) {
            return;
        }
        final SVRAppserviceProductSearchParameter param = tempCategoryBean.getSVRAppserviceProductSearchParameterById(parentFragmentType, categoryFragmentPosition);
        if (param == null) {
            return;
        }
        if (SEARCH_TYPE_LOADMORE == searchType) {
            ++currentP;
        } else if (SEARCH_TYPE_FILTER == searchType) {
            currentP = 1;
        } else if (SEARCH_TYPE_SORT == searchType) {
            currentP = 1;
        } else if (SEARCH_TYPE_KEYWORDS == searchType) {
        } else if (SEARCH_TYPE_REFRESH == searchType) {
            mDialog = JViewUtils.showProgressDialog(productListActivity);
        } else if (SEARCH_TYPE_INIT == searchType) {
            showViewSwitch(false);
//            if (productListActivity.getCurrentProductListFragmentPosition() == categoryFragmentPosition) {
            productListActivity.filterSortBottomView.hideSwitchAndFilterBar(true);
            mDialog = JViewUtils.showProgressDialog(productListActivity);
            cxlvProductList.setPullLoadEnable(false);
            if (productItemEntityArrayList != null) {
                productItemEntityArrayList.clear();
            }
            if (productListAdapter != null) {
                productListAdapter.notifyDataSetChanged();
            }
//            }

            currentP = 1;
        }
        TYPE = LOADING;
        String languageCode = LanguageUtils.getCurrentLanguageCode();
        String storeId = StoreUtils.getStoreIdByLanguageCode(languageCode
                                                                .equalsIgnoreCase(LanguageUtils.LANGUAGE_CODE_AUTO)
                                                                ? LanguageUtils.LANGUAGE_CODE_ENGLISH
                                                                : languageCode);
        //String storeId = WhiteLabelApplication.getAppConfiguration().getStoreView().getId();
        String p = currentP + "";
        String limit = param.getLimit() + "";
        if (SEARCH_TYPE_REFRESH == searchType) {
            limit = param.getLimit() * currentP + "";
            p = "1";
        }

        String order = param.getOrder();
        String dir = param.getDir();
        String price = param.getPrice();
        String modelType = param.getModel_type();
        String q = "";
        String brand = "";
        String categoryId = "";
        String categoryOption = "";
        List<String> brandOptions = null;
        List<String> flavorOptions = null;
        List<String> lifeStageOptions = null;

        // filter search
        SVRAppserviceProductSearchParameter.FilterParam filterParam = param.getFilterParam();
        if(filterParam.isUse()){

            categoryOption = filterParam.getCat();
            brandOptions = filterParam.getBrandOptions();
            flavorOptions = filterParam.getFlavorOptions();
            lifeStageOptions = filterParam.getLiftStageOptions();

            categoryId = "";
        }
        // General search
        else {

            if (!JDataUtils.isEmpty(param.getBrandId())) {
                brand = param.getBrandId();
            } else {
                categoryId = param.getCategory_id();
                q = "";
            }
            if (!JDataUtils.isEmpty(param.getBrand())) {
                try {
                    brand = param.getBrand();
                } catch (Exception ex) {
                    ex.getStackTrace();
                    brand = param.getBrand();
                }
            }
        }

        /*Aaron
        if (!TextUtils.isEmpty(param.getVesBrand())) {
            q = param.getVesBrand();
        }*/

        //传入session是为判断产品是否被wish
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
            mProductDao.productSearch(storeId, p, limit, order, dir, brand, categoryId, modelType, q,"", price,
                    WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey(),
                    "category", categoryOption, brandOptions, flavorOptions, lifeStageOptions);
        } else {
            mProductDao.productSearch(storeId, p, limit, order, dir, brand, categoryId, modelType, q,"", price,
                    "","category", categoryOption, brandOptions, flavorOptions, lifeStageOptions);
        }
    }


    @Override
    public void onRefresh() {
        searchType = SEARCH_TYPE_REFRESH;
        search();
    }

    @Override
    public void onLoadMore() {
        searchType = SEARCH_TYPE_LOADMORE;
        search();
    }

    public ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> getProductItemEntityArrayList() {
        return productItemEntityArrayList;
    }

    public SVRAppserviceProductSearchFacetsReturnEntity getSearchReturnEntityFacets() {
        if (searchReturnEntityFacets == null) {
            searchReturnEntityFacets = new SVRAppserviceProductSearchFacetsReturnEntity();
        }
        return searchReturnEntityFacets;
    }

    public void clearSearchReturnEntityFacets() {
        if (searchReturnEntityFacets == null) {
            searchReturnEntityFacets = new SVRAppserviceProductSearchFacetsReturnEntity();
        } else {
            if (searchReturnEntityFacets.getCategory_filter() != null) {
                searchReturnEntityFacets.getCategory_filter().clear();
                searchReturnEntityFacets.setCategory_filter(null);
            }

            searchReturnEntityFacets.setPrice_filter(null);

            if (searchReturnEntityFacets.getBrand_filter() != null) {
                searchReturnEntityFacets.getBrand_filter().clear();
                searchReturnEntityFacets.setBrand_filter(null);
            }

            if (searchReturnEntityFacets.getModel_type_filter() != null) {
                searchReturnEntityFacets.getModel_type_filter().clear();
                searchReturnEntityFacets.setModel_type_filter(null);
            }

            if (searchReturnEntityFacets.getSort_filter() != null) {
                searchReturnEntityFacets.getSort_filter().clear();
                searchReturnEntityFacets.setModel_type_filter(null);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onFilterWidgetClick(boolean show) {
    }

    @Override
    public void onSortWidgetClick(boolean show) {
    }

    @Override
    public void onViewToggleChanged() {
    }

    @Override
    public void onSlideToTop() {
        if (cxlvProductList != null) {
            cxlvProductList.setSelection(0);
        }
    }

    @Override
    public void onSearchFilter() {
        searchType = SEARCH_TYPE_INIT;
        search();
    }

    @Override
    public SVRAppserviceProductSearchFacetsReturnEntity getFilterInfo() {
        return getSearchReturnEntityFacets();
    }

    public void searchByType(int type) {
        searchType = type;
        search();
    }

    private static class SearchResultHandler extends Handler {
        public static final int TYPE_INTERFACE_FAILIRE = 1;
        public static final int TYPE_INTERFACE_SUCCESS_OK = 2;
        private final WeakReference<ProductListActivity> mActivity;
        private final WeakReference<ProductListProductListFragment> mFragment;

        public SearchResultHandler(ProductListActivity activity, ProductListProductListFragment framgent) {
            mActivity = new WeakReference<ProductListActivity>(activity);
            mFragment = new WeakReference<ProductListProductListFragment>(framgent);

        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.arg1) {
                case TYPE_INTERFACE_FAILIRE: {
                    handleFailure(msg);
                    break;
                }
                case TYPE_INTERFACE_SUCCESS_OK: {
                    handleSuccessOK(msg);
                }
            }
        }

        private void handleFailure(Message msg) {
            ProductListActivity activity = mActivity.get();
            ProductListProductListFragment fragment = mFragment.get();
            if (SEARCH_TYPE_LOADMORE == fragment.searchType) {
                --fragment.currentP;
                fragment.cxlvProductList.stopLoadMore();
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_FILTER == fragment.searchType) {
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_SORT == fragment.searchType) {
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_KEYWORDS == fragment.searchType) {
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_REFRESH == fragment.searchType) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                fragment.cxlvProductList.stopRefresh();
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_INIT == fragment.searchType) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
                fragment.cxlvProductList.setVisibility(View.GONE);
                fragment.rlNodata.setVisibility(View.VISIBLE);
            }
        }

        private void handleSuccessOK(Message msg) {
            ProductListActivity activity = mActivity.get();
            final ProductListProductListFragment fragment = mFragment.get();
            if (SEARCH_TYPE_LOADMORE == fragment.searchType) {
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.TYPE = fragment.NOTDATA;
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
                //显示 filter
                fragment.productListActivity.filterSortBottomView.setFilterShow(0, new ProductListFilterHideCallBack() {
                    @Override
                    public void callBack() {
                    }
                });
                fragment.cxlvProductList.stopLoadMore();
            } else if (SEARCH_TYPE_FILTER == fragment.searchType) {
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.TYPE = fragment.NOTDATA;
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
            } else if (SEARCH_TYPE_SORT == fragment.searchType) {
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
            } else if (SEARCH_TYPE_KEYWORDS == fragment.searchType) {
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                    fragment.showViewSwitch(false);
                }
            } else if (SEARCH_TYPE_REFRESH == fragment.searchType) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
                fragment.cxlvProductList.stopRefresh();
            } else if (SEARCH_TYPE_INIT == fragment.searchType) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);

                    fragment.cxlvProductList.setVisibility(View.GONE);
                    fragment.rlNodata.setVisibility(View.VISIBLE);
                } else {
                    fragment.showViewSwitch(true);
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();

                    fragment.rlNodata.setVisibility(View.GONE);
                    fragment.cxlvProductList.setVisibility(View.VISIBLE);

                    fragment.cxlvProductList.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.cxlvProductList.requestFocusFromTouch();
                            fragment.cxlvProductList.setSelection(0);
                        }
                    });

                    if (fragment.tempCategoryBean.getCurrentProductListFragmentPosition() == fragment.categoryFragmentPosition) {
                        activity.filterSortBottomView.hideSwitchAndFilterBar(false);
                    }
                }
                if (fragment.productListActivity.mGATrackTimeEnable) {
                    GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                            GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, fragment.tempCategoryBean.mGATrackTimeStart, "2/3 Tier Category Loading"
                    );
                    fragment.productListActivity.mGATrackTimeEnable = false;
                }
            }
        }
    }
}

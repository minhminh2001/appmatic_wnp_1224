package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.adapter.ProductListAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.callback.FragmentOnAdapterCallBack;
import com.whitelabel.app.callback.ProductListFilterHideCallBack;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.SVRAppserviceProductSearchResultsItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.SuggestsEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.model.TMPProductListListPageEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomEditText;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.CustomXListView;
import com.whitelabel.app.widget.FilterSortBottomView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by imaginato on 2015/7/13.
 */
public class ProductListKeywordsSearchFragment extends ProductListBaseFragment implements FragmentOnAdapterCallBack, View.OnClickListener,
        CustomXListView.IXListViewListener, OnFilterSortFragmentListener, Filter.FilterListener,FilterSortBottomView.FilterSortBottomViewCallBack {
    public static final int SEARCH_TYPE_INIT = 1;
    public static final int SEARCH_TYPE_KEYWORDS = 2;
    public static final int SEARCH_TYPE_FILTER = 3;
    public static final int SEARCH_TYPE_SORT = 4;
    public static final int SEARCH_TYPE_REFRESH = 5;
    public static final int SEARCH_TYPE_LOADMORE = 6;
    public static final int SEARCH_TYPE_SUGGESTION = 7;
    public static final String SUGGESTION_ROW_TYPE_BRAND = "brand";
    public static final String SUGGESTION_ROW_TYPE_CATEGORY = "category";
    public static final String SUGGESTION_ROW_TYPE_PRODUCT = "product";
    public static final String SUGGESTION_ROW_TYPE_MODEL_TYPE = "model_type";
    public static final int SUGGESTION_KEYWORD_TIMEOUT = 500;
    public static final String FROM_OTHER_PAGE_KEYWORD = "FROM_OTHER_PAGE_KEYWORD";
    public static final String IS_FROM_SHOP_BRAND = "IS_FROM_SHOP_BRAND";

    private final String TAG = "ProductListKeywordsSearchFragment";
    protected ProductListActivity productListActivity;
    private RequestErrorHelper requestErrorHelper;
    protected View mContentView;
    private View connectionLayout;
    private ProductDao mProductDao;
    private RelativeLayout mClearRL;
    private CustomEditText cetKeywords;
    private CustomXListView cxlvProductList;
    private RelativeLayout rlNodata;
    private CustomTextView noDataBlank;
    private ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productItemEntityArrayList;
    private ProductListAdapter productListAdapter;
    private final int LOADING = 2;
    private final int NONE = 1;
    private final int NOTDATA = 3;
    private final int ERROR = 4;
    private int TYPE = 1;
    private String PROMPT_ERROR_NOINTERNET;
    private SearchResultHandler searchResultHandler;
    private SVRAppserviceProductSearchFacetsReturnEntity searchReturnEntityFacets;
    private TMPProductListListPageEntity productListListPageEntity;
    private FrameLayout flFilterSortContainer;
    private ImageLoader mImageLoader;
    private ProductListFilterFragment filterFragment;
    private ProductListSortFragment sortFragment;
    private FilterSortHelper filterSortHelper;
    public FilterSortBottomView filterSortBottomView;
//    private ArrayList<SuggestsEntity> mSuggestionsArrayList;
//    private String mKeyWord;
//    private SearchSuggestionAdapter mSearchSuggestionAdapter;
//    private String mSuggestionBrand;
//    private String mSuggestionCategoryID;
//    private String mSuggestionsModleType;
////    private PublishSubject<String> mSubject = PublishSubject.create();
//    private boolean mIsShowSuggestion;
//    private long mTime_start;
    private boolean mIsSuggestionSearch;
    long resultSumPageNum=1;

    private RelativeLayout mRlSwitchViewbar;
    private ImageView mHeaderIvViewToggle;
    private boolean mIsFirst=true;
    private ImageView mTopViewToggleIV;
    private RelativeLayout mTopFilterAndSortBarRL;
    private boolean mIsShowSwitchFilterBar;
    private String fromOtherPageKeyWord ="";
    private String fromOtherPageCategoryId ="";
    private String brandId;
    private boolean isFromShopBrand;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            productListActivity = (ProductListActivity) context;
            filterFragment = new ProductListFilterFragment();
            filterFragment.setFragmentListener(this);
            sortFragment = new ProductListSortFragment();
            sortFragment.setFragmentListener(this);
        }
    }
    public void showViewSwitch(boolean show) {
        if (mRlSwitchViewbar != null) {
            if(show){
                mRlSwitchViewbar.setVisibility(View.VISIBLE);
            }else{
                mRlSwitchViewbar.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void startActivityForResultCallBack(Intent intent, int code) {
        this.startActivity(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_productlist_list, null);
        mImageLoader = new ImageLoader(getContext());
        return mContentView;
    }
    @Override
    public void onFilterComplete(int count) {
    }
    @Override
    public int getCurrentFilterSortTabIndex() {
        return productListActivity.getCurrentFilterSortTabIndex();
    }
    private static class DataHandler extends Handler {
        private final WeakReference<ProductListActivity> mActivity;
        private final WeakReference<ProductListKeywordsSearchFragment> mFragment;
        private long mTime_end;
        public DataHandler(ProductListActivity activity, ProductListKeywordsSearchFragment fragment) {
            mActivity = new WeakReference<>(activity);
            mFragment = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment == null) {
                return;
            }
            ProductListActivity activity = mActivity.get();
            ProductListKeywordsSearchFragment fragment = mFragment.get();
            fragment.noDataBlank.setText(activity.getResources().getString(R.string.productlist_list_prompt_error_nodata));
            switch (msg.what) {
                case ProductDao.REQUEST_PRODUCTSEARCH:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                            mFragment.get().mDialog.dismiss();
                        }
                        fragment.connectionLayout.setVisibility(GONE);
                        fragment.TYPE = fragment.NONE;
                        if ((activity != null && !activity.checkIsFinished()) && (fragment.isAdded())
                                && (activity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1) != null)
                                && (fragment.productItemEntityArrayList != null && fragment.productListAdapter != null)) {
                            ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> productListResult = null;
                            try {
                                SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity = (SVRAppserviceProductSearchReturnEntity) msg.obj;
                                fragment.resultSumPageNum = svrAppserviceProductSearchReturnEntity.getPageNum();
                                SVRAppserviceProductSearchParameter param = activity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1);
                                if (param.getP() == 1 && svrAppserviceProductSearchReturnEntity.getTotal() == 0) {
                                    Message notDataMsg = new Message();
                                    notDataMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                                    fragment.searchResultHandler.sendMessage(notDataMsg);
                                    return;
                                }
                                productListResult = svrAppserviceProductSearchReturnEntity.getResults();
                                if (fragment.mIsFirst) {
                                    fragment.initListViewHeader();
                                    fragment.mIsFirst = false;
                                }
                                fragment.clearSearchReturnEntityFacets();
                                SVRAppserviceProductSearchFacetsReturnEntity facetsReturnEntity = svrAppserviceProductSearchReturnEntity.getFacets();
                                if (facetsReturnEntity != null) {
                                    fragment.getSearchReturnEntityFacets().setCategory_filter(facetsReturnEntity.getCategory_filter());
                                    fragment.getSearchReturnEntityFacets().setPrice_filter(facetsReturnEntity.getPrice_filter());
                                    fragment.getSearchReturnEntityFacets().setBrand_filter(facetsReturnEntity.getBrand_filter());
                                    fragment.getSearchReturnEntityFacets().setModel_type_filter(facetsReturnEntity.getModel_type_filter());
                                    fragment.getSearchReturnEntityFacets().setSort_filter(facetsReturnEntity.getSort_filter());
                                }
                            } catch (Exception ex) {
                                JLogUtils.e(fragment.TAG, "getProductListFromServer -> onSuccess", ex);
                            }
                            Message successMsg = new Message();
                            successMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_SUCCESS_OK;
                            successMsg.obj = productListResult;
                            fragment.searchResultHandler.sendMessage(successMsg);
                            return;
                        } else {
                            fragment.TYPE = fragment.NOTDATA;
                        }
                        Message faildMsg = new Message();
                        faildMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                        faildMsg.obj = fragment.PROMPT_ERROR_NOINTERNET;
                        fragment.searchResultHandler.sendMessage(faildMsg);
                    } else {
                        if (fragment.TYPE != fragment.NOTDATA) {
                            fragment.TYPE = fragment.ERROR;
                        }
                        if ((activity != null && !activity.checkIsFinished()) && (fragment.isAdded())) {
                            if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                                mFragment.get().mDialog.dismiss();
                            }
                            fragment.cxlvProductList.stopLoadMore();
                            String errormsg = (String) msg.obj;
                            Message faildMsg = new Message();
                            faildMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            faildMsg.obj = errormsg;
                            fragment.searchResultHandler.sendMessage(faildMsg);
                        }
                    }
                    break;
                case ProductDao.REQUEST_SUGGESTIONS:
//                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
//                        SearchSuggestionBean searchSuggestionBean = (SearchSuggestionBean) msg.obj;
//                        fragment.filterSortBottomView.hideSwitchAndFilterBar(true);
//                        fragment.cxlvProductList.setVisibility(View.INVISIBLE);
////                        fragment.rlNodata.setVisibility(GONE);
//                        fragment.connectionLayout.setVisibility(View.GONE);
//                        if (searchSuggestionBean != null) {
//                            if (fragment.mSuggestionsArrayList != null) {
//                                fragment.mSuggestionsArrayList.clear();
//                                fragment.mSuggestionsArrayList.addAll(searchSuggestionBean.getList());
//                                fragment.mSearchSuggestionAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    if (fragment.TYPE != fragment.NOTDATA) {
                        fragment.TYPE = fragment.ERROR;
                    }
                    if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                        mFragment.get().mDialog.dismiss();
                    }
                    fragment.cxlvProductList.stopLoadMore();
                    SVRAppserviceProductSearchParameter param = activity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1);
                    if (param.getP() == 1) {
                        fragment.requestErrorHelper.showConnectionBreaks(msg);
                    } else {
                        if ((activity != null && !activity.checkIsFinished()) && (fragment.isAdded())) {
                            Message errorMsg = new Message();
                            errorMsg.arg1 = SearchResultHandler.TYPE_INTERFACE_FAILIRE;
                            errorMsg.obj = fragment.PROMPT_ERROR_NOINTERNET;
                            fragment.searchResultHandler.sendMessage(errorMsg);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private DataHandler dataHandler;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataHandler = new DataHandler((ProductListActivity) getActivity(), this);
        mProductDao = new ProductDao(TAG, dataHandler);
        connectionLayout = mContentView.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionLayout);
        LinearLayout tryAgain = (LinearLayout) mContentView.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionLayout.setVisibility(GONE);
                rlNodata.setVisibility(View.INVISIBLE);
                getProductListFromServer();
            }
        });
        int FRAGMENT_CONTAINER_ID = R.id.flFilterSortContainer;
        searchResultHandler = new SearchResultHandler((ProductListActivity) getActivity(), this);
        PROMPT_ERROR_NOINTERNET = getString(R.string.productlist_list_prompt_error_nointernet);

        try {
            GaTrackHelper.getInstance().googleAnalytics("Search List Screen", getActivity());
            JLogUtils.i("googleAnalytics", "Search List Screen");

        } catch (Exception ex) {
            ex.getStackTrace();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            productListListPageEntity = (TMPProductListListPageEntity) bundle.getSerializable("data");
            fromOtherPageKeyWord = productListListPageEntity.getKeyWord();
            fromOtherPageCategoryId =productListListPageEntity.getCategoryId();
            brandId =productListListPageEntity.getBrandId();
            //default false show serach bar,if true show title bar
            isFromShopBrand = productListListPageEntity.isFromShopBrand();
        }

        RelativeLayout rlContainer = (RelativeLayout) mContentView.findViewById(R.id.rlContainer);
        RelativeLayout llHeaderBar = (RelativeLayout) mContentView.findViewById(R.id.llHeaderBar);
        LinearLayout llToolBar = (LinearLayout) mContentView.findViewById(R.id.ll_toolbar);
        RelativeLayout mBackRL = (RelativeLayout) mContentView.findViewById(R.id.rl_back);
        cetKeywords = (CustomEditText) mContentView.findViewById(R.id.cetKeywords);
        cxlvProductList = (CustomXListView) mContentView.findViewById(R.id.cxlvProductList);
        rlNodata = (RelativeLayout) mContentView.findViewById(R.id.rlNodata);
        noDataBlank = (CustomTextView) mContentView.findViewById(R.id.nodate_blank);
        mClearRL = (RelativeLayout) mContentView.findViewById(R.id.rl_clear);
        mClearRL.setVisibility(GONE);
        //filter&sort
        flFilterSortContainer = (FrameLayout) mContentView.findViewById(R.id.flFilterSortContainer);
        flFilterSortContainer.setOnClickListener(this);
        ImageView mIVBottomSlideToTop = (ImageView) mContentView.findViewById(R.id.iv_bottom_slideto_top);
        mTopFilterAndSortBarRL = (RelativeLayout) mContentView.findViewById(R.id.top_switch_and_filter_bar);
        mTopViewToggleIV = (ImageView) mContentView.findViewById(R.id.iv_view_toggle_top);
        LinearLayout mTopFilterLL = (LinearLayout) mContentView.findViewById(R.id.ll_filter_top);
        LinearLayout mTopSortLL = (LinearLayout) mContentView.findViewById(R.id.ll_sort_top);
        mTopFilterLL.setOnClickListener(this);
        mTopSortLL.setOnClickListener(this);
        mTopViewToggleIV.setOnClickListener(this);
        mIVBottomSlideToTop.setOnClickListener(this);
        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(mTopFilterAndSortBarRL, mIVBottomSlideToTop, this);
        filterSortHelper = new FilterSortHelper(getActivity(), sortFragment, filterFragment, flFilterSortContainer, FRAGMENT_CONTAINER_ID);

        filterSortBottomView.hideSwitchAndFilterBar(true);
        cxlvProductList.setVisibility(GONE);
        cxlvProductList.setHeaderDividersEnabled(false);
        cxlvProductList.setFooterDividersEnabled(false);
        cxlvProductList.setPullRefreshEnable(false);
        cxlvProductList.setPullLoadEnable(false);
        if (isFromShopBrand){
            setContentView(mContentView);
            llHeaderBar.setVisibility(GONE);
            llToolBar.setVisibility(VISIBLE);
            initTitleBar();
        }else {
            llHeaderBar.setVisibility(VISIBLE);
            llToolBar.setVisibility(GONE);
        }
        cxlvProductList.setOnScrollListener(new CustomXListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                filterSortBottomView.setFilterShow(scrollState, new ProductListFilterHideCallBack() {
                    @Override
                    public void callBack() {
                    }
                });
            }

            public boolean isSlideToBottom(CustomXListView lv) {
                if (lv == null) return false;
                return lv.computeVerticalScrollExtent() + lv.computeVerticalScrollOffset() >= lv.computeVerticalScrollRange();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final SVRAppserviceProductSearchParameter param = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1);
                if ((param.getP() * (param.getLimit() / 2) - firstVisibleItem) <= (param.getLimit() / 2 - 1) && TYPE == NONE) {
                    setSearchType(SEARCH_TYPE_LOADMORE);
                    cxlvProductList.setLoadMoreProgress();
                }
                //是否显示 到顶部的icon
                if (view.getFirstVisiblePosition() == 0) {
                    filterSortBottomView.hideBottomSlideToTop(true);
                } else {
                    filterSortBottomView.hideBottomSlideToTop(false);
                }
                mTopFilterAndSortBarRL.setVisibility(View.VISIBLE);
            }
        });

        productItemEntityArrayList = new ArrayList<>();
        productListAdapter = new ProductListAdapter(productListActivity, productItemEntityArrayList, mImageLoader,this, this);
        cxlvProductList.setAdapter(productListAdapter);

        rlContainer.setOnClickListener(this);
        mBackRL.setOnClickListener(this);
        cxlvProductList.setXListViewListener(this);
        mClearRL.setOnClickListener(this);
//        initSuggestionListView();
        cetKeywords.addTextChangedListener(new TextWatcher() {
            private String beforeText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mClearRL.setVisibility(GONE);
//                    clearSuggestionList();
                } else {
                    if (beforeText.equals(s.toString().trim())) return;
                    mClearRL.setVisibility(VISIBLE);
////                    clearSuggestionList();
//                    flFilterSortContainer.setVisibility(GONE);
////                    mSubject.onNext(s.toString().trim()); //rx-java
//                    queryWithHandler(s.toString().trim());// Handler
//                    showSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cetKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int eventKeyCode = 0;
                if (event != null) {
                    eventKeyCode = event.getKeyCode();
                }
                //if (EditorInfo.IME_ACTION_SEARCH == actionId||eventKeyCode==KeyEvent.KEYCODE_ENTER) {
                if (eventKeyCode == KeyEvent.KEYCODE_ENTER) {
                    onSubmitKeyWord();
                }
                return false;
            }
        });
//        cetKeywords.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                        clearSuggestionList();
//                        getSearchSuggestions(cetKeywords.getText().toString().trim());
//                        showSuggestions();
//                        cxlvProductList.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        if (getSearchType() != ProductListKeywordsSearchFragment.SEARCH_TYPE_KEYWORDS) {
            search();
        } else {
            cetKeywords.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private CustomEditText keywords;

                public ViewTreeObserver.OnGlobalLayoutListener init(CustomEditText e) {
                    this.keywords = e;
                    return this;
                }

                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        keywords.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        keywords.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            JViewUtils.showKeyboard(keywords);
                            if (!TextUtils.isEmpty(fromOtherPageKeyWord)){
                                cetKeywords.setText(fromOtherPageKeyWord);
                                search();
                            }
                            if (!TextUtils.isEmpty(fromOtherPageCategoryId)){
                                search();
                            }
                        }
                    }, 200);
                }
            }.init(cetKeywords));
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                cetKeywords.setText("");
            }
        });
       }

    private void initTitleBar() {
        setTitle(fromOtherPageKeyWord);
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(getActivity(), R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initListViewHeader(){
        View view = LayoutInflater.from(productListActivity).inflate(R.layout.header_product_list_switch_and_filter_bar, null);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,JDataUtils.dp2Px(40));
        view.setLayoutParams(params);
        mRlSwitchViewbar = (RelativeLayout) view.findViewById(R.id.rl_viewbar);
        LinearLayout mHeaderFilterLL = (LinearLayout) view.findViewById(R.id.ll_filter);
        LinearLayout mHeaderSortLL = (LinearLayout) view.findViewById(R.id.ll_sort);
        mHeaderIvViewToggle = (ImageView) view.findViewById(R.id.iv_view_toggle);
        mHeaderIvViewToggle.setOnClickListener(this);
        mHeaderFilterLL.setOnClickListener(this);
        mHeaderSortLL.setOnClickListener(this);
        cxlvProductList.addHeaderView(view);
    }

//    public void showSuggestions() {
//        if (mSearchSuggestionAdapter != null) {
//            mIsShowSuggestion = true;
//        }
//    }

    public void dismissSuggestions() {
        if (cetKeywords != null) {
            cetKeywords.clearFocus();
        }
    }

//    private void initSuggestionListView() {
//        //Rxjava实现延迟搜索
////        mSubject.debounce(SUGGESTION_KEYWORD_TIMEOUT, TimeUnit.MILLISECONDS)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .doOnNext(new Action1<String>() {
////                    @Override
////                    public void call(String s) {
////                        getSearchSuggestions(s);
////                    }
////                }).subscribe();
//        mSuggestionListView = (ListView) mContentView.findViewById(R.id.lv_suggestions_list);
//        mSuggestionsArrayList = new ArrayList<>();
//        mSearchSuggestionAdapter = new SearchSuggestionAdapter(getActivity(), mSuggestionsArrayList);
//        mSuggestionListView.setAdapter(mSearchSuggestionAdapter);
////        startFilter();
//        mSuggestionListView.setOnItemClickListener(new AdapterView.IHorizontalItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                onSuggestionItemClick(((SuggestsEntity) mSearchSuggestionAdapter.getItem(position)), true);
//            }
//        });
//        if (cetKeywords != null) {
//            if (TextUtils.isEmpty(cetKeywords.getText().toString().trim())) {
//                dismissSuggestions();
//            }
//        }
//    }

//    public void onSuggestionItemClick(SuggestsEntity suggestionItem, boolean submit) {
//        clearSuggestionSearch();
//        if (submit && suggestionItem != null) {
//            switch (suggestionItem.getRow_type()) {
//                case SUGGESTION_ROW_TYPE_BRAND:
//                    setKeyWord(suggestionItem);
//                    mIsSuggestionSearch = true;
//                    dismissSuggestions();
//                    cancelHandler();
//                    setSearchType(ProductListKeywordsSearchFragment.SEARCH_TYPE_SUGGESTION);
//                    mSuggestionBrand = suggestionItem.getTitle();
//                    search();
//                    break;
//                case SUGGESTION_ROW_TYPE_CATEGORY:
//                    mIsSuggestionSearch = true;
//                    dismissSuggestions();
//                    mSuggestionCategoryID = suggestionItem.getId();
//                    setSearchType(ProductListKeywordsSearchFragment.SEARCH_TYPE_SUGGESTION);
//                    search();
//                    break;
//                case SUGGESTION_ROW_TYPE_MODEL_TYPE:
//                    mIsSuggestionSearch = true;
//                    dismissSuggestions();
//                    cancelHandler();
//                    mSuggestionsModleType = suggestionItem.getTitle();
//                    setSearchType(ProductListKeywordsSearchFragment.SEARCH_TYPE_SUGGESTION);
//                    search();
//                    break;
//                case SUGGESTION_ROW_TYPE_PRODUCT:
//                    setKeyWord(suggestionItem);
//                    onSubmitKeyWord();
//                    break;
//                default:
//                    setKeyWord(suggestionItem);
//                    onSubmitKeyWord();
//                    break;
//
//            }
//        }
//
//    }

    private void setKeyWord(SuggestsEntity suggestionItem) {
        cetKeywords.setText(suggestionItem.getTitle());
        if (suggestionItem.getTitle() != null) {
            cetKeywords.setSelection(cetKeywords.length());
        }
    }

//    private void clearSuggestionSearch() {
//        mSuggestionBrand = "";
//        mSuggestionCategoryID = "";
//        mSuggestionsModleType = "";
//        productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setPrice("");
//        productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setBrand(mSuggestionBrand);
//        productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setCategory_id(mSuggestionCategoryID);
//        productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setModel_type(mSuggestionsModleType);
//
//    }

    private void onSubmitKeyWord() {
        if (!"".equals(cetKeywords.getText().toString().trim())) {
            JViewUtils.hideKeyboard(productListActivity);
            flFilterSortContainer.setVisibility(GONE);
            setSearchType(ProductListKeywordsSearchFragment.SEARCH_TYPE_KEYWORDS);
            //track
            productListActivity.GATrackSearchTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
//            productListActivity.GATrackSearchTimeEnable = true;
            search();
            productListActivity.resetCurrentFilterSortTabIndex();
        } else {
            showViewSwitch(false);
            filterSortBottomView.hideSwitchAndFilterBar(true);
            flFilterSortContainer.setVisibility(GONE);
            rlNodata.setVisibility(VISIBLE);
            cxlvProductList.setVisibility(View.INVISIBLE);
            noDataBlank.setText(getResources().getString(R.string.productlist_list_prompt_error_blank));
            JViewUtils.hideKeyboard(productListActivity);
        }
    }

//    private void getSearchSuggestions(String q) {
//        if (!JDataUtils.isChinese(q) && !TextUtils.isEmpty(cetKeywords.getText().toString().trim())) {
//            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
//                mProductDao.getSuggestions(q, WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey());
//            } else {
//                mProductDao.getSuggestions(q, "");
//            }
//        }
//    }
//    private Handler mDelayHandler = new Handler();
//    private DelayQueryRunnable mDelayQueryRunnable;
//    private void queryWithHandler(String newText) {
//        // 延迟
//        cancelHandler();
//        mDelayQueryRunnable = new DelayQueryRunnable(newText);
//        mDelayHandler.postDelayed(mDelayQueryRunnable, SUGGESTION_KEYWORD_TIMEOUT);
//    }
//
//    private void cancelHandler() {
//        if (mDelayQueryRunnable != null) {
//            mDelayQueryRunnable.cancel();
//            mDelayHandler.removeCallbacksAndMessages(null);
//        }
//    }

//    private class DelayQueryRunnable implements Runnable {
//        String mText;
//        private boolean mCanceled = false;
//
//        public DelayQueryRunnable(String text) {
//            this.mText = text;
//        }
//
//        @Override
//        public void run() {
//            if (mCanceled) {
//                return;
//            }
//            getSearchSuggestions(mText);
//        }
//
//        public void cancel() {
//            mCanceled = true;
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
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
        if (LoginRegisterActivity.REQUESTCODE_LOGIN == requestCode && resultCode == LoginRegisterEmailLoginFragment.RESULTCODE) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(productListActivity)) {
//                productListActivity.changeOperateProductIdPrecacheStatus(true);
                onRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreWishIconByPDPResult(String productId, int isLike, String itemId) {
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

    private void showKeyboard() {
        cetKeywords.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(cetKeywords, InputMethodManager.SHOW_IMPLICIT);
    }

    private void search() {
        getProductListFromServer();
        searchTrack();
    }


    public void searchTrack() {
        String keyWord = cetKeywords.getText().toString().trim();
        try {
            if (!TextUtils.isEmpty(keyWord)) {
                GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action", "Search", keyWord, null);
//                FirebaseEventUtils.getInstance().ecommerceSearchResult(getActivity(), keyWord);
//                FirebaseEventUtils.getInstance().allAppSearch(getActivity(), keyWord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Dialog mDialog;

    private void getProductListFromServer() {
        if (productListActivity == null || productListActivity.checkIsFinished() || !isAdded()) {
            return;
        }

        if (productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1) == null) {
            return;
        }

        if (SEARCH_TYPE_LOADMORE == getSearchType()) {
            final int currentP = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getP();
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(currentP + 1);
        } else if (SEARCH_TYPE_FILTER == getSearchType()) {
            //=======================================================================
            if (productItemEntityArrayList != null) {
                productItemEntityArrayList.clear();
            }
            if (productListAdapter != null) {
                cxlvProductList.setPullLoadEnable(false);
                productListAdapter.notifyDataSetChanged();
            }

            mDialog = JViewUtils.showProgressDialog(productListActivity);
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
            String keywords = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getQ();
            if (!JDataUtils.isEmpty(cetKeywords)) {
                keywords = cetKeywords.getText().toString().trim();
            }
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ(keywords);
        } else if (SEARCH_TYPE_SORT == getSearchType()) {
            mDialog = JViewUtils.showProgressDialog(productListActivity);
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
            String keywords = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getQ();
            //======================================================
            if (productItemEntityArrayList != null) {
                productItemEntityArrayList.clear();
            }
            if (productListAdapter != null) {
                cxlvProductList.setPullLoadEnable(false);
                productListAdapter.notifyDataSetChanged();
            }
            if (!JDataUtils.isEmpty(cetKeywords)) {
                keywords = cetKeywords.getText().toString().trim();
            }
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ(keywords);
        } else if (SEARCH_TYPE_KEYWORDS == getSearchType()) {
            //=====================================================
            showViewSwitch(false);
            if (productItemEntityArrayList != null) {
                productItemEntityArrayList.clear();
            }
            if (productListAdapter != null) {
                cxlvProductList.setPullLoadEnable(false);
                productListAdapter.notifyDataSetChanged();
            }
            filterSortBottomView.hideSwitchAndFilterBar(true);
            mDialog = JViewUtils.showProgressDialog(productListActivity);
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).clear();
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
            String keywords = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getQ();
            if (!JDataUtils.isEmpty(cetKeywords)) {
                keywords = cetKeywords.getText().toString().trim();
            }
            if (!TextUtils.isEmpty(fromOtherPageCategoryId)){
                productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setCategory_id(fromOtherPageCategoryId);
            }
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ(keywords);
        } else if (SEARCH_TYPE_REFRESH == getSearchType()) {
            mDialog = JViewUtils.showProgressDialog(productListActivity);
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
        } else if (SEARCH_TYPE_INIT == getSearchType()) {
            showViewSwitch(false);
            filterSortBottomView.hideSwitchAndFilterBar(true);
            mDialog = JViewUtils.showProgressDialog(productListActivity);
            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
            String keywords = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getQ();
            if (productItemEntityArrayList != null) {
                productItemEntityArrayList.clear();
            }
            if (productListAdapter != null) {
                cxlvProductList.setPullLoadEnable(false);
                productListAdapter.notifyDataSetChanged();
            }

            if (!JDataUtils.isEmpty(cetKeywords)) {
                keywords = cetKeywords.getText().toString().trim();
            }
            if (mIsSuggestionSearch) {
                productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ("");
            } else {
                productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ(keywords);
            }
        }
//        else if (SEARCH_TYPE_SUGGESTION == getSearchType()) {
//            showViewSwitch(false);
//            filterSortBottomView.hideSwitchAndFilterBar(true);
//            mDialog = JViewUtils.showProgressDialog(productListActivity);
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(1);
//            if (productItemEntityArrayList != null) {
//                productItemEntityArrayList.clear();
//            }
//            if (productListAdapter != null) {
//                cxlvProductList.setPullLoadEnable(false);
//                productListAdapter.notifyDataSetChanged();
//            }
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setQ("");
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setBrand(mSuggestionBrand);
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setCategory_id(mSuggestionCategoryID);
//            productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setModel_type(mSuggestionsModleType);
//        }
        TYPE = LOADING;
        String storeId = WhiteLabelApplication.getAppConfiguration().getStoreView().getId();
        final SVRAppserviceProductSearchParameter param = productListActivity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1);
        String p = param.getP() + "";
        String limit = param.getLimit() + "";
        String order = param.getOrder();
        String dir = param.getDir();
        String price = param.getPrice();
        String q = param.getQ();
        String brand = "";
        String categoryId = "";
        String modelType = param.getModel_type();
        if (!TextUtils.isEmpty(param.getBrand())) {
            brand = param.getBrand();
        }
        if (!TextUtils.isEmpty(param.getCategory_id())) {
            categoryId = param.getCategory_id();
        }
        String sessionKey="";
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
            sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
        }
        if (!TextUtils.isEmpty(brandId)){
            if (resultSumPageNum>=Integer.valueOf(p)){
                mProductDao.brandSearch(storeId,p,brandId,limit,order,dir,q, sessionKey);
            }else {
                cxlvProductList.stopLoadMore();
            }
        }else {
            mProductDao.productSearch(storeId, p, limit, order, dir, brand, categoryId, modelType, q,q ,price,
                    sessionKey,"","search"
            );
        }


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_clear:
                cetKeywords.setText("");
                break;
            case R.id.flFilterSortContainer: {
                break;
            }
            case R.id.iv_bottom_slideto_top: {
                if (cxlvProductList != null) {
                    cxlvProductList.setSelection(0);
                }
            }
            case R.id.rlContainer: {
                if (productListActivity != null) {
                    JViewUtils.hideKeyboard(productListActivity);
                }
                break;
            }
            case R.id.rl_back: {
                onBackPressed();
                break;
            }
            case R.id.iv_view_toggle:
            case R.id.iv_view_toggle_top: {
                toggleViewOption();
                break;
            }
            case R.id.ll_filter:
            case R.id.ll_filter_top:
                productListActivity.filterSortOption(productListActivity.TYPE_FILTER);
                break;
            case R.id.ll_sort:
            case R.id.ll_sort_top:
                productListActivity.filterSortOption(productListActivity.TYPE_SORT);
                break;
        }
    }


    private void toggleViewOption() {
        if(super.isDoubleCol){
            mHeaderIvViewToggle.setImageResource(R.mipmap.ic_view_double);
            mTopViewToggleIV.setImageResource(R.mipmap.ic_view_double);
        }else{
            mHeaderIvViewToggle.setImageResource(R.mipmap.ic_view_single);
            mTopViewToggleIV.setImageResource(R.mipmap.ic_view_single);
        }
        super.isDoubleCol=!super.isDoubleCol;
        cxlvProductList.setSelection(0);
        productListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        setSearchType(SEARCH_TYPE_REFRESH);
        search();
    }

    @Override
    public void onLoadMore() {
        setSearchType(SEARCH_TYPE_LOADMORE);
        search();
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

    private int getSearchType() {
        if (productListListPageEntity != null) {
            return productListListPageEntity.getSearchType();
        } else {
            return ProductListKeywordsSearchFragment.SEARCH_TYPE_KEYWORDS;
        }
    }

    private void setSearchType(int type) {
        if (productListListPageEntity == null) {
            productListListPageEntity = new TMPProductListListPageEntity();
        }
        productListListPageEntity.setSearchType(type);
    }

    @Override
    public void onBackPressed() {
        if (productListActivity != null) {
            JViewUtils.hideKeyboard(productListActivity);
            dismissSuggestions();
            if (filterSortHelper.isAnyActive()) {
                filterSortDefault();
            } else if (ProductListActivity.TABBAR_INDEX_NONE == productListActivity.getCurrentFilterSortTabIndex()) {
                if (ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME == productListActivity.getPrevType()) {
                    productListActivity.finish();
                    productListActivity.closeActivityTransitionAnim();
                } else {
                    productListActivity.switchFragment(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, null);
                }
            }
        }
    }
    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
        filterSortPageEntity.setCategoryFragmentPosition(-1);
        filterSortPageEntity.setFacets(searchReturnEntityFacets);

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", filterSortPageEntity);
        return bundle;
    }

    @Override
    public void onFilterWidgetClick(boolean show) {
        if (productListActivity != null) {
            filterSortHelper.onFilterClicked(show, createBundle());
        }
    }

    @Override
    public void onSortWidgetClick(boolean show) {
        if (productListActivity != null) {
            filterSortHelper.onSortClicked(show, createBundle());
        }
    }

    @Override
    public void onSlideToTop() {
        if (cxlvProductList != null) {
            cxlvProductList.setSelection(0);
        }
    }

    @Override
    public void onCancelClick(View view) {
        resetSelection();
    }

    private void resetSelection() {
        productListActivity.resetCurrentFilterSortTabIndex();
        filterSortHelper.hideVisibleFragments();
    }

    @Override
    public void onFilterSortListItemClick(int type, Object object) {
        filterSortDefault();
    }

    @Override
    public void onAnimationFinished(Fragment fragment) {
        filterSortHelper.hideContainer(fragment);
    }

    private void filterSortDefault() {
        resetSelection();
        setSearchType(SEARCH_TYPE_INIT);
        search();
    }

    private static class SearchResultHandler extends Handler {
        public static final int TYPE_INTERFACE_FAILIRE = 1;
        public static final int TYPE_INTERFACE_SUCCESS_OK = 2;
        private final WeakReference<ProductListActivity> mActivity;
        private final WeakReference<ProductListKeywordsSearchFragment> mFragment;

        public SearchResultHandler(ProductListActivity activity, ProductListKeywordsSearchFragment fragment) {
            mActivity = new WeakReference<>(activity);
            mFragment = new WeakReference<>(fragment);
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
                    break;
                }
            }
        }

        private void handleFailure(Message msg) {
            final ProductListKeywordsSearchFragment fragment = mFragment.get();
            ProductListActivity activity = mActivity.get();
            if (SEARCH_TYPE_LOADMORE == fragment.getSearchType()) {
                final int currentP = activity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).getP();
                activity.getSVRAppserviceProductSearchParameterById(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, -1).setP(currentP - 1);

                fragment.cxlvProductList.stopLoadMore();

                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    //   JViewUtils.showToast(productListActivity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_FILTER == fragment.getSearchType()) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }

                fragment.cxlvProductList.setVisibility(GONE);
                fragment.rlNodata.setVisibility(VISIBLE);
            } else if (SEARCH_TYPE_SORT == fragment.getSearchType()) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }

                fragment.cxlvProductList.setVisibility(GONE);
                fragment.rlNodata.setVisibility(VISIBLE);
            } else if (SEARCH_TYPE_KEYWORDS == fragment.getSearchType()) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    // JViewUtils.showToast(productListActiviy, null, (String) msg.obj);
                }
                fragment.dismissSuggestions();
                fragment.cxlvProductList.setVisibility(GONE);
                fragment.rlNodata.setVisibility(VISIBLE);

            } else if (SEARCH_TYPE_REFRESH == fragment.getSearchType()) {
                if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                    mFragment.get().mDialog.dismiss();
                }
                fragment.cxlvProductList.stopRefresh();
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
                    JViewUtils.showToast(activity, null, (String) msg.obj);
                }
            } else if (SEARCH_TYPE_INIT == fragment.getSearchType()) {
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
//                    JViewUtils.showToast(productListActivity, null, (String) msg.obj);
                }
                fragment.cxlvProductList.setVisibility(GONE);
                fragment.rlNodata.setVisibility(VISIBLE);
                fragment.dismissSuggestions();
                fragment.cxlvProductList.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.cxlvProductList.requestFocusFromTouch();
                        fragment.cxlvProductList.setSelection(0);
                    }
                });
            } else if (SEARCH_TYPE_SUGGESTION == fragment.getSearchType()) {
                if (!JDataUtils.errorMsgHandler(activity, (String) msg.obj)) {
//                    JViewUtils.showToast(productListActivity, null, (String) msg.obj);
                }
                fragment.cxlvProductList.setVisibility(GONE);
                fragment.rlNodata.setVisibility(VISIBLE);
                fragment.cxlvProductList.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.cxlvProductList.requestFocusFromTouch();
                        fragment.cxlvProductList.setSelection(0);
                    }
                });
            }
        }

        private void handleSuccessOK(Message msg) {
            final ProductListKeywordsSearchFragment fragment = mFragment.get();
            ProductListActivity activity = mActivity.get();
            if (SEARCH_TYPE_LOADMORE == fragment.getSearchType()) {
                ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> searchResultArray = null;
                try {
                    searchResultArray = (ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity>) msg.obj;
                } catch (Exception ex) {
                    JLogUtils.e(fragment.TAG, "handleSuccessOK", ex);
                    ex.printStackTrace();
                }

                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                        mFragment.get().mDialog.dismiss();
                    }
                    fragment.cxlvProductList.setPullLoadEnable(true);
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
//                fragment.cxlvProductList.setVisibility(VISIBLE);
                fragment.filterSortBottomView.hideSwitchAndFilterBar(false);
                fragment.cxlvProductList.stopLoadMore();
            } else if (SEARCH_TYPE_FILTER == fragment.getSearchType()) {
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

                    fragment.cxlvProductList.setVisibility(View.INVISIBLE);
                    fragment.rlNodata.setVisibility(VISIBLE);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
            } else if (SEARCH_TYPE_SORT == fragment.getSearchType()) {
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

                    fragment.cxlvProductList.setVisibility(View.INVISIBLE);
                    fragment.rlNodata.setVisibility(VISIBLE);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
            } else if (SEARCH_TYPE_KEYWORDS == fragment.getSearchType()) {
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

                    fragment.cxlvProductList.setVisibility(View.INVISIBLE);
                    fragment.rlNodata.setVisibility(VISIBLE);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();

                    fragment.rlNodata.setVisibility(GONE);
                    fragment.cxlvProductList.setVisibility(VISIBLE);

                    fragment.cxlvProductList.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.cxlvProductList.requestFocusFromTouch();
                            fragment.cxlvProductList.setSelection(0);
                        }
                    });

                    fragment.filterSortBottomView.hideSwitchAndFilterBar(false);
                }
                if (fragment.productListActivity.GATrackSearchTimeEnable) {
                    GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                            GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, fragment.productListActivity.GATrackSearchTimeStart, "Search Results Loading"
                    );
                    fragment.productListActivity.GATrackSearchTimeEnable = false;
                    fragment.showViewSwitch(true);
                }
            } else if (SEARCH_TYPE_REFRESH == fragment.getSearchType()) {
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
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                }
                fragment.cxlvProductList.stopRefresh();
            } else if (SEARCH_TYPE_INIT == fragment.getSearchType()) {
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
                fragment.cxlvProductList.setVisibility(View.VISIBLE);
                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();
                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                    fragment.cxlvProductList.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment.cxlvProductList.requestFocusFromTouch();
                            fragment.cxlvProductList.setSelection(0);
                        }
                    });
                    fragment.filterSortBottomView.hideSwitchAndFilterBar(false);
                    fragment.showViewSwitch(true);
                }
            } else if (SEARCH_TYPE_SUGGESTION == fragment.getSearchType()) {
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
                fragment.cxlvProductList.setVisibility(View.VISIBLE);
                fragment.productItemEntityArrayList.clear();
                fragment.productListAdapter.notifyDataSetChanged();
                if (searchResultArray == null || searchResultArray.size() <= 0) {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    fragment.cxlvProductList.setPullLoadEnable(false);
                    fragment.rlNodata.setVisibility(View.VISIBLE);
                } else {
                    fragment.cxlvProductList.setPullRefreshEnable(false);
                    if (searchResultArray.size() <= 9) {
                        fragment.cxlvProductList.setPullLoadEnable(false);
                    } else {
                        fragment.cxlvProductList.setPullLoadEnable(true);
                    }
                    fragment.productItemEntityArrayList.addAll(searchResultArray);
                    fragment.productListAdapter.notifyDataSetChanged();
                    fragment.rlNodata.setVisibility(GONE);
                    fragment.filterSortBottomView.hideSwitchAndFilterBar(false);
                }
                fragment.cxlvProductList.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.cxlvProductList.requestFocusFromTouch();
                        fragment.cxlvProductList.setSelection(0);
                    }
                });
                fragment.showViewSwitch(true);
            }
        }
    }
}

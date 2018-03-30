package com.whitelabel.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.whitelabel.app.R;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.fragment.ProductListBaseFragment;
import com.whitelabel.app.fragment.ProductListCategoryLandingFragment;
import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;
import com.whitelabel.app.listener.OnSingleClickListener;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.TMPProductListListPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.FilterSortBottomView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imagniato on 2015/7/13.
 */
public class ProductListActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener,
        FilterSortBottomView.FilterSortBottomViewCallBack, IFilterSortActivity {
    public  final int TYPE_SORT = 0;
    public  final int TYPE_FILTER = 1;

    public boolean mGATrackTimeEnable = false;
    public Long GATrackSearchTimeStart = 0L;
    public boolean GATrackSearchTimeEnable = false;
    public static final int RESULT_WISH = 101;

    public final static int TABBAR_INDEX_FILTER = 1;
    public final static int TABBAR_INDEX_SORT = 2;
    public final static String INTENT_DATA_PREVTYPE = "prevType";
    public final static String INTENT_DATA_FRAGMENTTYPE = "fragmentType";
    public final static String INTENT_DATA_CATEGORYID = "categoryEntity";
    public final static String INTENT_DATA_LEFT_TOP_TITLE = "leftMenuTitle";
    public final static String INTENT_CATEGORY_ID = "categoryId";
    private final static String CURRENT_INDEX = "position";
    public static final String SHOP_BRAND_ID = "SHOP_BRAND_ID";
    public static final String SHOP_BRAND_TITLE = "SHOP_BRAND_TITLE";
    public final static int INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY = 1;
    public final static int INTENT_DATA_PREVTYPE_VALUE_HOME = 2;
    public static final int FRAGMENT_TYPE_PRODUCTLIST_CATEGORY = 0;
    public static final int FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS = 1;
    private final int TYPE_FRAGMENT_SWITCH_NONE = 0;
    private final int TYPE_FRAGMENT_SWITCH_RIGHT2LEFT = 1;
    private final int TYPE_FRAGMENT_SWITCH_LEFT2RIGHT = -1;
    private final String TAG = "ProductListActivity";
    private final String LINK = "->";
    private final String C_L_LINK = FRAGMENT_TYPE_PRODUCTLIST_CATEGORY + LINK + FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS;
    private final String L_C_LINK = FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS + LINK + FRAGMENT_TYPE_PRODUCTLIST_CATEGORY;
    protected ArrayList<ProductListBaseFragment> mAttachedFragmentList;
    protected ArrayList<String> fragmentSequenceArray;
    protected ProductListBaseFragment mCurrentFragment;
    protected boolean isActivityFinished = true;
    protected boolean isActivityInvisible = true;
    protected boolean isActivityPaused = true;
    private int FRAGMENT_CONTAINER_ID;




    private int prevType;
    private int fragmentType;
    public String categoryId;
    public String keyWord;
    private TMPProductListListPageEntity tmpProductListListPageEntity;
    public LinearLayout llToolBar;
    public RelativeLayout rlBottomBar, rlBottomBarFilter, rlBottomBarSort;
    private TextView tvSortAnimate, tvFilterAnimate;
    private CustomTextView ctvBottomBarFilter, ctvBottomBarSort;
    private ImageView iv_bottom_slideto_top;
    //封装了bottom filter sort布局的隐藏显示功能
    public FilterSortBottomView filterSortBottomView;
    public OperateProductIdPrecache operateProductIdPrecache;//未登录时点击了wishicon,登陆成功后主动将其添加到wishlist
    private SingleClickListener singleClickListener;

    private int currentFilterSortTabIndex;
    private TempCategoryBean tempCategoryBean;

    public boolean checkIsFinished() {
        return isActivityFinished;
    }

    public boolean checkIsInvisible() {
        return isActivityInvisible;
    }

    public boolean checkIsPaused() {
        return isActivityPaused;
    }

    private void addFragment(int index, ProductListBaseFragment fragment) {
        if (index < 0) {
            return;
        }
        if (mAttachedFragmentList == null) {
            mAttachedFragmentList = new ArrayList<ProductListBaseFragment>();
        }

        int fragmentlistsize = mAttachedFragmentList.size();
        if (index >= fragmentlistsize) {
            for (int position = fragmentlistsize; position <= index; ++position) {
                mAttachedFragmentList.add(null);
            }
        }

        fragmentlistsize = mAttachedFragmentList.size();
        if (fragmentlistsize > index) {
            mAttachedFragmentList.set(index, fragment);
        }
    }
    private void initFragment() {
        addFragment(FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, new ProductListCategoryLandingFragment());
        addFragment(FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, new ProductListKeywordsSearchFragment());
    }
    public void saveProductIdWhenCheckPage(String productId, int isLike,boolean isUnLogin) {
        operateProductIdPrecache = new OperateProductIdPrecache(productId,isLike,isUnLogin);
    }

    public boolean isUnLoginCanWishIconRefresh(String productId){
        if (operateProductIdPrecache!=null){
            if (operateProductIdPrecache.getProductId().equals(productId) && operateProductIdPrecache.isUnLogin()){
                operateProductIdPrecache=null;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        tempCategoryBean=TempCategoryBean.getInstance();
        isActivityFinished = false;
        currentFilterSortTabIndex = tempCategoryBean.TABBAR_INDEX_NONE;
        FRAGMENT_CONTAINER_ID = R.id.flFilterSortContainer;
        llToolBar = (LinearLayout) findViewById(R.id.ll_toolbar);
        rlBottomBar = (RelativeLayout) findViewById(R.id.rlBottomBar);
        rlBottomBarFilter = (RelativeLayout) findViewById(R.id.rlBottomBarFilter);
        ctvBottomBarFilter = (CustomTextView) findViewById(R.id.ctvBottomBarFilter);
        rlBottomBarSort = (RelativeLayout) findViewById(R.id.rlBottomBarSort);
        ctvBottomBarSort = (CustomTextView) findViewById(R.id.ctvBottomBarSort);
        iv_bottom_slideto_top = (ImageView) findViewById(R.id.iv_bottom_slideto_top);
        tvSortAnimate = (TextView) findViewById(R.id.tv_sort_plus_animate);
        tvFilterAnimate = (TextView) findViewById(R.id.tv_filter_plus_animate);
        iv_bottom_slideto_top.setOnClickListener(this);
        singleClickListener = new SingleClickListener();
        rlBottomBarFilter.setOnClickListener(singleClickListener);
        rlBottomBarSort.setOnClickListener(singleClickListener);
        tempCategoryBean.svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        prevType = 0;
        fragmentType = FRAGMENT_TYPE_PRODUCTLIST_CATEGORY;
        Intent intent = getIntent();
        if (intent != null) {
            prevType = intent.getIntExtra(INTENT_DATA_PREVTYPE, 0);
            fragmentType = intent.getIntExtra(INTENT_DATA_FRAGMENTTYPE, FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            tempCategoryBean.currentProductListFragmentPosition = intent.getIntExtra(CURRENT_INDEX, 0);
            try {
                CategoryBaseBean.CategoryBean.ChildrenBeanX searchCategoryEntity = (CategoryBaseBean.CategoryBean.ChildrenBeanX) intent.getSerializableExtra(INTENT_DATA_CATEGORYID);
                tempCategoryBean.searchCategoryEntity=searchCategoryEntity;
                tempCategoryBean.leftMenuTitle= (String) intent.getSerializableExtra(INTENT_DATA_LEFT_TOP_TITLE);
                if (tempCategoryBean.searchCategoryEntity != null && !JDataUtils.isEmpty(tempCategoryBean.searchCategoryEntity.getId())) {
                    tempCategoryBean.svrAppserviceProductSearchParameter.setCategory_id(tempCategoryBean.searchCategoryEntity.getId());
                    tempCategoryBean.svrAppserviceProductSearchParameter.setName(tempCategoryBean.searchCategoryEntity.getName());
                    viewListTrack(tempCategoryBean.searchCategoryEntity.getName());
                }
                categoryId = intent.getStringExtra(INTENT_CATEGORY_ID);
                keyWord = intent.getStringExtra(ProductListKeywordsSearchFragment.FROM_OTHER_PAGE_KEYWORD);
                String brandId=intent.getStringExtra(ProductListActivity.SHOP_BRAND_ID);
                String shopBrandTitle=intent.getStringExtra(ProductListActivity.SHOP_BRAND_TITLE);
                boolean isFromShopBrand=intent.getBooleanExtra(ProductListKeywordsSearchFragment.IS_FROM_SHOP_BRAND,false);
                tmpProductListListPageEntity=new TMPProductListListPageEntity();
                tmpProductListListPageEntity.setCategoryId(categoryId);
                tmpProductListListPageEntity.setKeyWord(keyWord);
                tmpProductListListPageEntity.setBrandId(brandId);
                tmpProductListListPageEntity.setShopBrandTitle(shopBrandTitle);
                tmpProductListListPageEntity.setFromShopBrand(isFromShopBrand);
            } catch (Exception ex) {
                JLogUtils.e(TAG, "onCreate", ex);
            }
        }

        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(rlBottomBar, iv_bottom_slideto_top, this);
        mAttachedFragmentList = new ArrayList<>();
        fragmentSequenceArray = new ArrayList<>();
        initFragment();
        switchFragment(-1, fragmentType, tmpProductListListPageEntity);

        ;
    }


    public void viewListTrack(String name) {
        //TODO joyson old business code
//        FirebaseEventUtils.getInstance().ecommerceViewItemList(ProductListActivity.this, name);
    }

    //TODO joyson old business code
//    private void switchBottomBar() {
//        if (TABBAR_INDEX_FILTER == currentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(getResources().getColor(R.color.purple66006E));
//            AnimUtil.animatePlusSign(tvFilterAnimate, true, this);
//            //打开filter时隐藏 toTopBar
//            filterSortBottomView.hideBottomSlideToTop(true);
//            ctvBottomBarSort.setTextColor(getResources().getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvSortAnimate, false, this);
//        } else if (TABBAR_INDEX_SORT == currentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(getResources().getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvFilterAnimate, false, this);
//            filterSortBottomView.hideBottomSlideToTop(true);
//            ctvBottomBarSort.setTextColor(getResources().getColor(R.color.purple66006E));
//            AnimUtil.animatePlusSign(tvSortAnimate, true, this);
//        } else if (TABBAR_INDEX_NONE == currentFilterSortTabIndex) {
//            ctvBottomBarFilter.setTextColor(getResources().getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvFilterAnimate, false, this);
//            filterSortBottomView.hideBottomSlideToTop(false);
//            ctvBottomBarSort.setTextColor(getResources().getColor(R.color.black000000));
//            AnimUtil.animatePlusSign(tvSortAnimate, false, this);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityInvisible = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isActivityPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityInvisible = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityFinished = true;
        if (filterSortBottomView.filterHandler != null) {
            filterSortBottomView.filterHandler = null;
        }
    }

    public void switchFragment(int from, int to, Serializable serializable) {
        if (-1 == from) {
            tempCategoryBean.resetCurrentFilterSortTabIndex();
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_NONE, serializable);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == from && FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == to) {
            tempCategoryBean.resetCurrentFilterSortTabIndex();
            if (fragmentSequenceArray != null) {
                redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_RIGHT2LEFT, serializable);
                fragmentSequenceArray.add(C_L_LINK);
            }
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == from && FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == to) {
            tempCategoryBean.resetCurrentFilterSortTabIndex();
            if (fragmentSequenceArray != null) {
                if (fragmentSequenceArray.contains(C_L_LINK)) {
                    redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_LEFT2RIGHT, serializable);
                    try {
                        int fragmentSequenceArraySize = fragmentSequenceArray.size();
                        final int historyLinkIndex = fragmentSequenceArray.indexOf(C_L_LINK);
                        final int removeItemCount = fragmentSequenceArraySize - historyLinkIndex - 1;
                        for (int removeItemIndex = 0; removeItemIndex < removeItemCount; ++removeItemIndex) {
                            --fragmentSequenceArraySize;
                            fragmentSequenceArray.remove(fragmentSequenceArraySize);
                        }
                    } catch (Exception ex) {
                        JLogUtils.e(TAG, "switchFragment", ex);
                    }
                } else {
                    redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_RIGHT2LEFT, serializable);
                    fragmentSequenceArray.add(L_C_LINK);
                }
            }
        }
    }

    private void redirectToAttachedFragment(int to, int type, Serializable serializable) {
        if (mAttachedFragmentList != null && mAttachedFragmentList.size() > to) {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            JLogUtils.i("zz", "to==" + to);
            ProductListBaseFragment subFragment = mAttachedFragmentList.get(to);

            if ((subFragment != null) && (mCurrentFragment != null) && (subFragment.getClass().equals(mCurrentFragment.getClass()))) {
                if (serializable != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", serializable);
                    subFragment.getArguments().putAll(bundle);
                }
            } else {
                if (serializable != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", serializable);
                    subFragment.setArguments(bundle);
                }

                if (TYPE_FRAGMENT_SWITCH_RIGHT2LEFT == type) {
                    mFragmentTransaction.setCustomAnimations(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                } else if (TYPE_FRAGMENT_SWITCH_LEFT2RIGHT == type) {
                    mFragmentTransaction.setCustomAnimations(R.anim.activity_transition_enter_lefttoright, R.anim.activity_transition_exit_lefttoright);
                }

                mFragmentTransaction.replace(FRAGMENT_CONTAINER_ID, subFragment);
                mFragmentTransaction.commit();
            }

            mCurrentFragment = subFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment != null) {
            mCurrentFragment.onBackPressed();
        }
    }

    @Override
    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int type, int index, long minPrice, long maxPrice) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            tempCategoryBean.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(index, minPrice, maxPrice);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            tempCategoryBean.setSVRAppserviceProductSearchParameterMinPriceMaxPrice(minPrice, maxPrice);
        }
    }

    public int getPrevType() {
        return prevType;
    }

    @Override
    public void onClick(View view) {
        if (!filterSortBottomView.showFilter) {
            //如果现在filter正是隐藏的,不执行 setOnClickListener(null);
            return;
        }
        if (view.getId() == R.id.iv_bottom_slideto_top) {
            mCurrentFragment.onSlideToTop();
        }
    }
    public void filterSortOption(int type) {
        if(type == TYPE_SORT){
            if (mCurrentFragment != null) {
                mCurrentFragment.onSortWidgetClick(currentFilterSortTabIndex != TABBAR_INDEX_SORT);
                currentFilterSortTabIndex = currentFilterSortTabIndex == TABBAR_INDEX_SORT ? tempCategoryBean.TABBAR_INDEX_NONE : TABBAR_INDEX_SORT;
            }
        }else if (type == TYPE_FILTER){
            filterSortBottomView.setFilterStatus(0, null);
            if (mCurrentFragment != null) {
                mCurrentFragment.onFilterWidgetClick(currentFilterSortTabIndex != TABBAR_INDEX_FILTER);
                currentFilterSortTabIndex = currentFilterSortTabIndex == TABBAR_INDEX_FILTER ? tempCategoryBean.TABBAR_INDEX_NONE : TABBAR_INDEX_FILTER;
            }
        }
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return tempCategoryBean.getCurrentFilterSortTabIndex();
    }

    private class SingleClickListener extends OnSingleClickListener {

        @Override
        public void onSingleClick(View view) {
            if (!filterSortBottomView.showFilter) {
                return;
            }
            switch (view.getId()) {
                case R.id.rlBottomBarFilter: {

                    break;
                }
                case R.id.rlBottomBarSort: {

                    break;
                }
            }
        }
    }

    @Override
    public void finish(){
        super.finish();

        overridePendingTransition(0, R.anim.slide_right_out);
    }
}

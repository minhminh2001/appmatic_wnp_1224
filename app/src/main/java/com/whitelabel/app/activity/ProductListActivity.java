package com.whitelabel.app.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OperateProductIdPrecache;
import com.whitelabel.app.fragment.ProductListBaseFragment;
import com.whitelabel.app.fragment.ProductListCategoryLandingFragment;
import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;
import com.whitelabel.app.listener.OnSingleClickListener;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.SVRAppserviceProductFilterSelectedItem;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsFieldFilterItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsFieldFilterItemReturnEntity.FilterItem;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.TMPProductListListPageEntity;
import com.whitelabel.app.model.TempCategoryBean;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.FilterSortBottomView;
import com.whitelabel.app.widget.FlexBoxLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imagniato on 2015/7/13.
 */
public class ProductListActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,FilterSortBottomView.FilterSortBottomViewCallBack, IFilterSortActivity {
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
    private final String FILTER_TYPE_CATEGORY = "cat";
    private final String FILTER_TYPE_BRAND = "vesbrand";
    private final String FILTER_TYPE_FLAVOR = "flavor";
    private final String FILTER_TYPE_LIFT_STAGE = "stage";
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
    private DrawerLayout filterDrawerLayout;
    private FlexBoxLayout flexboxCategory;
    private FlexBoxLayout flexboxBrand;
    private FlexBoxLayout flexboxFlavor;
    private FlexBoxLayout flexboxLiftStage;
    private Button btnFilterDone;
    private CheckBox btnShowMoreCategory;
    private CheckBox btnShowMoreBrand;
    private CheckBox btnShowMoreFlavor;
    private CheckBox btnShowMoreLiftStage;
    private Button btnRestFilter;
    private LinearLayout llCategoryContainer;
    private LinearLayout llBrandContainer;
    private LinearLayout llFlavorContainer;
    private LinearLayout llLiftStageContainer;

    private int currentFilterSortTabIndex;
    private TempCategoryBean tempCategoryBean;
    private List<FilterItem> categoryFilterItemList = new ArrayList<>();
    private List<FilterItem> brandFilterItemList = new ArrayList<>();
    private List<FilterItem> flavorFilterItemList = new ArrayList<>();
    private List<FilterItem> liftStageFilterItemList = new ArrayList<>();

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
        filterDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout_filter);
        flexboxBrand = (FlexBoxLayout)findViewById(R.id.flex_brand);
        flexboxCategory = (FlexBoxLayout)findViewById(R.id.flex_category);
        flexboxFlavor = (FlexBoxLayout)findViewById(R.id.flex_flavor);
        flexboxLiftStage = (FlexBoxLayout)findViewById(R.id.flex_lift_stage);
        btnFilterDone = (Button)findViewById(R.id.btn_done);
        btnShowMoreCategory = (CheckBox)findViewById(R.id.cb_show_more_category);
        btnShowMoreBrand = (CheckBox)findViewById(R.id.cb_show_more_brand);
        btnShowMoreFlavor = (CheckBox)findViewById(R.id.cb_show_more_flavor);
        btnShowMoreLiftStage = (CheckBox)findViewById(R.id.cb_show_more_lift_stage);
        btnRestFilter = (Button)findViewById(R.id.btn_reset);
        llCategoryContainer = (LinearLayout)findViewById(R.id.category_container);
        llBrandContainer = (LinearLayout)findViewById(R.id.brand_container);
        llFlavorContainer = (LinearLayout)findViewById(R.id.flavor_container);
        llLiftStageContainer = (LinearLayout)findViewById(R.id.lift_stage_container);

        btnFilterDone.setOnClickListener(this);
        btnRestFilter.setOnClickListener(this);
        btnShowMoreCategory.setOnCheckedChangeListener(this);
        btnShowMoreBrand.setOnCheckedChangeListener(this);
        btnShowMoreFlavor.setOnCheckedChangeListener(this);
        btnShowMoreLiftStage.setOnCheckedChangeListener(this);
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

        initFilterDrawerLayout();

        filterSortBottomView = new FilterSortBottomView();
        filterSortBottomView.initView(rlBottomBar, iv_bottom_slideto_top, this);
        mAttachedFragmentList = new ArrayList<>();
        fragmentSequenceArray = new ArrayList<>();
        initFragment();
        switchFragment(-1, fragmentType, tmpProductListListPageEntity);
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

    private void initFilterDrawerLayout(){
        btnRestFilter.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());

        filterDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        filterDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {
                filterDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                filterDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        filterDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {

                // clear filter
                SVRAppserviceProductSearchParameter searchParam = getSearchParamsForFilter();
                SVRAppserviceProductSearchParameter.FilterParam filterParam = searchParam.getFilterParam();
                filterParam.clear();
            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    private void updateShowMoreButtonForFilter(){
        flexboxCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                flexboxCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if(flexboxCategory.getRowCount() <= 0){
                    llCategoryContainer.setVisibility(View.GONE);
                } else {
                    llCategoryContainer.setVisibility(View.VISIBLE);
                }

                if(flexboxCategory.getRowCount() <= 2) {
                    btnShowMoreCategory.setVisibility(View.GONE);
                }else{
                    btnShowMoreCategory.setVisibility(View.VISIBLE);
                }
            }
        });
        flexboxBrand.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                flexboxBrand.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if(flexboxBrand.getRowCount() <= 0){
                    llBrandContainer.setVisibility(View.GONE);
                } else {
                    llBrandContainer.setVisibility(View.VISIBLE);
                }

                if(flexboxBrand.getRowCount() <= 2){
                    btnShowMoreBrand.setVisibility(View.GONE);
                } else{
                    btnShowMoreBrand.setVisibility(View.VISIBLE);
                }
            }
        });

        flexboxFlavor.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                flexboxFlavor.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if(flexboxFlavor.getRowCount() <= 0){
                    llFlavorContainer.setVisibility(View.GONE);
                } else {
                    llFlavorContainer.setVisibility(View.VISIBLE);
                }

                if(flexboxFlavor.getRowCount() <= 2) {
                    btnShowMoreFlavor.setVisibility(View.GONE);
                } else {
                    btnShowMoreFlavor.setVisibility(View.VISIBLE);
                }
            }
        });

        flexboxLiftStage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                flexboxLiftStage.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if(flexboxLiftStage.getRowCount() <= 0){
                    llLiftStageContainer.setVisibility(View.GONE);
                } else{
                    llLiftStageContainer.setVisibility(View.VISIBLE);
                }

                if(flexboxLiftStage.getRowCount() <= 2) {
                    btnShowMoreLiftStage.setVisibility(View.GONE);
                } else{
                    btnShowMoreLiftStage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void fillFilterData(){

        SVRAppserviceProductSearchFacetsReturnEntity searchFacetsReturnEntity = mCurrentFragment.getFilterInfo();
        ArrayList<SVRAppserviceProductSearchFacetsFieldFilterItemReturnEntity> filterAllItems = searchFacetsReturnEntity.getField_filter();
        for(int i = 0; i < filterAllItems.size(); i++){
            SVRAppserviceProductSearchFacetsFieldFilterItemReturnEntity item = filterAllItems.get(i);
            if(item.getKey().equalsIgnoreCase(FILTER_TYPE_CATEGORY)){
                categoryFilterItemList.clear();
                for(FilterItem categoryItem : item.getValue()){
                    categoryFilterItemList.add(categoryItem);
                }
            } else if(item.getKey().equalsIgnoreCase(FILTER_TYPE_BRAND)){
                brandFilterItemList.clear();
                for(FilterItem categoryItem : item.getValue()){
                    brandFilterItemList.add(categoryItem);
                }
            } else if(item.getKey().equalsIgnoreCase(FILTER_TYPE_FLAVOR)){
                flavorFilterItemList.clear();
                for(FilterItem flavorItem : item.getValue()){
                    flavorFilterItemList.add(flavorItem);
                }
            } else if(item.getKey().equalsIgnoreCase(FILTER_TYPE_LIFT_STAGE)){
                liftStageFilterItemList.clear();
                for(FilterItem liftStageItem : item.getValue()){
                    liftStageFilterItemList.add(liftStageItem);
                }
            }
        }
    }

    private void updateFilterView(){

        SVRAppserviceProductFilterSelectedItem selectedItem = getSelectedItemForFilter();

        flexboxCategory.removeAllViews();
        for(FilterItem categoryItem : categoryFilterItemList){
            CheckBox cb = (CheckBox) getLayoutInflater().inflate(R.layout.layout_filter_sort_checkbox, null);
            cb.setTextColor(getTextColorForFilterItem());
            cb.setBackground(getBackgrondForFilterItem());
            cb.setText(categoryItem.getLabel());
            cb.setTag(categoryItem.getValue());
            flexboxCategory.addView(cb);

            if(categoryItem.getValue().equalsIgnoreCase(selectedItem.getCategoryOption())){
                cb.setChecked(true);
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int childCount = flexboxCategory.getChildCount();
                    for(int index = 0; index < childCount; index++){
                        CheckBox cb = (CheckBox) flexboxCategory.getChildAt(index);
                        cb.setChecked(false);
                    }
                    buttonView.setChecked(isChecked);

                    execFilter();
                }
            });
        }

        flexboxBrand.removeAllViews();
        for(FilterItem brandItem : brandFilterItemList){
            CheckBox cb = (CheckBox) getLayoutInflater().inflate(R.layout.layout_filter_sort_checkbox, null);
            cb.setTextColor(getTextColorForFilterItem());
            cb.setBackground(getBackgrondForFilterItem());
            cb.setText(brandItem.getLabel());
            cb.setTag(brandItem.getValue());
            flexboxBrand.addView(cb);

            List<String> selectedBrandOptions = selectedItem.getBrandOptions();
            for(int index = 0; index < selectedBrandOptions.size(); index++){
                if(brandItem.getValue().equalsIgnoreCase(selectedBrandOptions.get(index))){
                    cb.setChecked(true);
                }
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    execFilter();
                }
            });
        }

        flexboxFlavor.removeAllViews();
        for(FilterItem flavorItem : flavorFilterItemList){
            CheckBox cb = (CheckBox) getLayoutInflater().inflate(R.layout.layout_filter_sort_checkbox, null);
            cb.setTextColor(getTextColorForFilterItem());
            cb.setBackground(getBackgrondForFilterItem());
            cb.setText(flavorItem.getLabel());
            cb.setTag(flavorItem.getValue());
            flexboxFlavor.addView(cb);

            List<String> selectedFlavorOptions = selectedItem.getFlavorOptions();
            for(int index = 0; index < selectedFlavorOptions.size(); index++){
                if(flavorItem.getValue().equalsIgnoreCase(selectedFlavorOptions.get(index))){
                    cb.setChecked(true);
                }
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    execFilter();
                }
            });
        }

        flexboxLiftStage.removeAllViews();
        for(FilterItem liftStageItem : liftStageFilterItemList){
            CheckBox cb = (CheckBox) getLayoutInflater().inflate(R.layout.layout_filter_sort_checkbox, null);
            cb.setTextColor(getTextColorForFilterItem());
            cb.setBackground(getBackgrondForFilterItem());
            cb.setText(liftStageItem.getLabel());
            cb.setTag(liftStageItem.getValue());
            flexboxLiftStage.addView(cb);

            List<String> selectedStageOptions = selectedItem.getLifeStageOptions();
            for(int index = 0; index < selectedStageOptions.size(); index++){
                if(liftStageItem.getValue().equalsIgnoreCase(selectedStageOptions.get(index))){
                    cb.setChecked(true);
                }
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    execFilter();
                }
            });
        }
    }


    private void clearCategoryOptionForFilter(){

        for(int i = 0; i < flexboxCategory.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxCategory.getChildAt(i);
            if(cb.isChecked()){
                cb.setChecked(false);
                break;
            }
        }
    }

    private void clearBrandOptionForFilter(){

        for(int i = 0; i < flexboxBrand.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxBrand.getChildAt(i);
            if(cb.isChecked()){
                cb.setChecked(false);
            }
        }
    }

    private void clearFlavorOptionForFilter(){

        for(int i = 0; i < flexboxFlavor.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxFlavor.getChildAt(i);
            if(cb.isChecked()){
                cb.setChecked(false);
            }
        }
    }

    private void clearLiftOptionForFilter(){

        for(int i = 0; i < flexboxLiftStage.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxLiftStage.getChildAt(i);
            if(cb.isChecked()){
                cb.setChecked(false);
            }
        }
    }

    private SVRAppserviceProductSearchParameter getSearchParamsForFilter(){
        int parameterIndex = -1;
        int fragmentType = 0;

        if(mCurrentFragment instanceof ProductListKeywordsSearchFragment){
            fragmentType = FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS;
        } else if(mCurrentFragment instanceof ProductListCategoryLandingFragment){
            fragmentType = FRAGMENT_TYPE_PRODUCTLIST_CATEGORY;
            parameterIndex = ((ProductListCategoryLandingFragment)mCurrentFragment).getCurrentPagePosition();
        }

        return tempCategoryBean.getSVRAppserviceProductSearchParameterById(fragmentType, parameterIndex);
    }

    private SVRAppserviceProductFilterSelectedItem getSelectedItemForFilter(){
        int parameterIndex = -1;
        int fragmentType = 0;

        if(mCurrentFragment instanceof ProductListKeywordsSearchFragment){
            fragmentType = FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS;
        } else if(mCurrentFragment instanceof ProductListCategoryLandingFragment){
            fragmentType = FRAGMENT_TYPE_PRODUCTLIST_CATEGORY;
            parameterIndex = ((ProductListCategoryLandingFragment)mCurrentFragment).getCurrentPagePosition();
        }

        return tempCategoryBean.getSVRAppserviceProductFilterSelectedItemById(fragmentType, parameterIndex);
    }

    private String getCategoryOptionForFilter(){
        SVRAppserviceProductFilterSelectedItem selectedItem = getSelectedItemForFilter();
        selectedItem.clearCategoryOption();

        String categoryId = null;
        for(int i = 0; i < flexboxCategory.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxCategory.getChildAt(i);
            if(cb.isChecked()){
                categoryId = (String)cb.getTag();
                selectedItem.setCategoryOption(categoryId);
                break;
            }
        }

        return categoryId;
    }

    private List<String> getBrandOptionForFilter(){

        SVRAppserviceProductFilterSelectedItem selectedItem = getSelectedItemForFilter();
        selectedItem.clearBrandOptions();

        List<String> brandOptions = null;
        for(int i = 0; i < flexboxBrand.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxBrand.getChildAt(i);
            if(cb.isChecked()){
                if(brandOptions == null){
                    brandOptions = new ArrayList<>();
                }
                brandOptions.add((String)cb.getTag());
                selectedItem.getBrandOptions().add((String)cb.getTag());
            }
        }

        return brandOptions;
    }

    private List<String> getFlavorOptionForFilter(){

        SVRAppserviceProductFilterSelectedItem selectedItem = getSelectedItemForFilter();
        selectedItem.clearFlavorOptions();

        List<String> flavorOptions = null;
        for(int i = 0; i < flexboxFlavor.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxFlavor.getChildAt(i);
            if(cb.isChecked()){
                if(flavorOptions == null){
                    flavorOptions = new ArrayList<>();
                }
                flavorOptions.add((String)cb.getTag());
                selectedItem.getFlavorOptions().add((String)cb.getTag());
            }
        }

        return flavorOptions;
    }

    private List<String> getLiftStageOptionForFilter(){

        SVRAppserviceProductFilterSelectedItem selectedItem = getSelectedItemForFilter();
        selectedItem.clearLiftStageOptions();

        List<String> liftStageOptions = null;
        for(int i = 0; i < flexboxLiftStage.getChildCount(); i++){
            CheckBox cb = (CheckBox)flexboxLiftStage.getChildAt(i);
            if(cb.isChecked()){
                if(liftStageOptions == null){
                    liftStageOptions = new ArrayList<>();
                }
                liftStageOptions.add((String)cb.getTag());
                selectedItem.getLifeStageOptions().add((String)cb.getTag());
            }
        }

        return liftStageOptions;
    }

    private void execFilter(){

        SVRAppserviceProductSearchParameter searchParam = getSearchParamsForFilter();
        SVRAppserviceProductSearchParameter.FilterParam filterParam = searchParam.getFilterParam();

        // clear filter param
        filterParam.clear();

        // use for search by category
        String categoryId;
        if(!TextUtils.isEmpty(searchParam.getCategory_id())
                && (getBrandOptionForFilter() != null
                || getFlavorOptionForFilter() != null
                || getLiftStageOptionForFilter() != null)){
            categoryId = searchParam.getCategory_id();
        } else {
            categoryId = getCategoryOptionForFilter();
        }

        // use for search by brand
        List<String> brandOptions = null;
        if(!TextUtils.isEmpty(searchParam.getBrandId())
                && (getCategoryOptionForFilter() != null
                || getFlavorOptionForFilter() != null
                || getLiftStageOptionForFilter() != null)){
            brandOptions = new ArrayList<>();
            brandOptions.add(searchParam.getBrandName());
        } else {
            brandOptions = getBrandOptionForFilter();
        }

        filterParam.setCat(categoryId);
        filterParam.setBrandOptions(brandOptions);
        filterParam.setFlavorOptions(getFlavorOptionForFilter());
        filterParam.setLiftStageOptions(getLiftStageOptionForFilter());

        // exec search for filter
        mCurrentFragment.onSearchFilter();
    }

    private ColorStateList getTextColorForFilterItem(){
        int checkedColor = WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color();
        int unCheckedColor = ContextCompat.getColor(this, R.color.black);
        int[] colors = new int[]{unCheckedColor, checkedColor, unCheckedColor};

        int [][]statelist = new int[3][];
        statelist[0] = new int[]{-android.R.attr.state_checked};
        statelist[1] = new int[]{android.R.attr.state_checked};
        statelist[2] = new int[]{};

        return new ColorStateList(statelist, colors);
    }

    private Drawable getBackgrondForFilterItem(){

        int strokeWidth = 1;
        int roundRadius = 60;
        int strokeColor = ContextCompat.getColor(this, R.color.greyCCCCCC);
        int fillColor = ContextCompat.getColor(this, R.color.white);//内部填充颜色

        GradientDrawable unCheckedBg = new GradientDrawable();
        unCheckedBg.setColor(fillColor);
        unCheckedBg.setCornerRadius(roundRadius);
        unCheckedBg.setStroke(strokeWidth, strokeColor);

        strokeColor = WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color();
        GradientDrawable checkedBg = new GradientDrawable();
        checkedBg.setColor(fillColor);
        checkedBg.setCornerRadius(roundRadius);
        checkedBg.setStroke(strokeWidth, strokeColor);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, unCheckedBg);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedBg);

        return stateListDrawable;
    }

    public void showFilerView(){

        mCurrentFragment.onFilterWidgetClick(true);

        // fill filter data from ProductListkeywordFragment or ProductListCategoryLandingFragment
        fillFilterData();

        // create filter item
        updateFilterView();

        // update display state of show more button
        updateShowMoreButtonForFilter();

        // show filter view
        filterDrawerLayout.openDrawer(Gravity.END);
    }

    public void onViewToggleChanged(){
        mCurrentFragment.onViewToggleChanged();
    }

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

        // click filter done button
        if(view.getId() == R.id.btn_done){
            //execFilter();
            filterDrawerLayout.closeDrawer(Gravity.END);
        }

        else if(view.getId() == R.id.btn_reset){
            clearCategoryOptionForFilter();
            clearBrandOptionForFilter();
            clearFlavorOptionForFilter();
            clearLiftOptionForFilter();

            //execFilter();
            //filterDrawerLayout.closeDrawer(Gravity.END);
        }

        if (!filterSortBottomView.showFilter) {
            //如果现在filter正是隐藏的,不执行 setOnClickListener(null);
            return;
        }

        if (view.getId() == R.id.iv_bottom_slideto_top) {
            mCurrentFragment.onSlideToTop();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.cb_show_more_category:
                btnShowMoreCategory.setText(isChecked ?
                getString(R.string.filter_show_less) : getString(R.string.filter_show_more));
                flexboxCategory.showAll(isChecked);
                break;
            case R.id.cb_show_more_brand:
                btnShowMoreBrand.setText(isChecked ?
                        getString(R.string.filter_show_less) : getString(R.string.filter_show_more));
                flexboxBrand.showAll(isChecked);
                break;
            case R.id.cb_show_more_flavor:
                btnShowMoreFlavor.setText(isChecked ?
                        getString(R.string.filter_show_less) : getString(R.string.filter_show_more));
                flexboxFlavor.showAll(isChecked);
                break;
            case R.id.cb_show_more_lift_stage:
                btnShowMoreLiftStage.setText(isChecked ?
                        getString(R.string.filter_show_less) : getString(R.string.filter_show_more));
                flexboxLiftStage.showAll(isChecked);
                break;
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
        return currentFilterSortTabIndex;
    }

    public void resetCurrentFilterSortTabIndex() {
        currentFilterSortTabIndex = tempCategoryBean.TABBAR_INDEX_NONE;
        tempCategoryBean.resetCurrentFilterSortTabIndex();
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

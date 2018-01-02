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
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    public Long GATrackSearchTimeStart = 0L;
    public boolean GATrackSearchTimeEnable = false;
    public static final int RESULT_WISH = 101;
    public final static int TABBAR_INDEX_NONE = -1;
    public final static int TABBAR_INDEX_FILTER = 1;
    public final static int TABBAR_INDEX_SORT = 2;
    public final static String INTENT_DATA_PREVTYPE = "prevType";
    public final static String INTENT_DATA_FRAGMENTTYPE = "fragmentType";
    public final static String INTENT_DATA_CATEGORYID = "categoryEntity";
    public final static String INTENT_DATA_LEFT_TOP_TITLE = "leftMenuTitle";
    public final static String INTENT_CATEGORY_ID = "categoryId";
    private final static String CURRENT_INDEX = "position";
    public static final String SHOP_BRAND_ID = "SHOP_BRAND_ID";
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
    private SVRAppserviceProductSearchParameter svrAppserviceProductSearchParameter;
    public CategoryBaseBean.CategoryBean.ChildrenBeanX searchCategoryEntity;
    private ArrayList<SVRAppserviceProductSearchParameter> searchCategoryParameterArrayList;
    public String leftMenuTitle;
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
    private int currentProductListFragmentPosition = 0;

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
        isActivityFinished = false;
        currentFilterSortTabIndex = TABBAR_INDEX_NONE;
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
        svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        prevType = 0;
        fragmentType = FRAGMENT_TYPE_PRODUCTLIST_CATEGORY;
        Intent intent = getIntent();
        if (intent != null) {
            prevType = intent.getIntExtra(INTENT_DATA_PREVTYPE, 0);
            fragmentType = intent.getIntExtra(INTENT_DATA_FRAGMENTTYPE, FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            currentProductListFragmentPosition = intent.getIntExtra(CURRENT_INDEX, 0);
            try {
                searchCategoryEntity = (CategoryBaseBean.CategoryBean.ChildrenBeanX) intent.getSerializableExtra(INTENT_DATA_CATEGORYID);
                leftMenuTitle= (String) intent.getSerializableExtra(INTENT_DATA_LEFT_TOP_TITLE);
                if (searchCategoryEntity != null && !JDataUtils.isEmpty(searchCategoryEntity.getId())) {
                    svrAppserviceProductSearchParameter.setCategory_id(searchCategoryEntity.getId());
                    svrAppserviceProductSearchParameter.setName(searchCategoryEntity.getName());
                    //svrAppserviceProductSearchParameter.setBrandId(searchCategoryEntity.getBrandId());
                    //firebaseTrack
                    viewListTrack(searchCategoryEntity.getName());
                }
                categoryId = intent.getStringExtra(INTENT_CATEGORY_ID);
                keyWord = intent.getStringExtra(ProductListKeywordsSearchFragment.FROM_OTHER_PAGE_KEYWORD);
                String brandId=intent.getStringExtra(ProductListActivity.SHOP_BRAND_ID);
                boolean isFromShopBrand=intent.getBooleanExtra(ProductListKeywordsSearchFragment.IS_FROM_SHOP_BRAND,false);
                tmpProductListListPageEntity=new TMPProductListListPageEntity();
                tmpProductListListPageEntity.setCategoryId(categoryId);
                tmpProductListListPageEntity.setKeyWord(keyWord);
                tmpProductListListPageEntity.setBrandId(brandId);
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
    }


    public void viewListTrack(String name) {
//        FirebaseEventUtils.getInstance().ecommerceViewItemList(ProductListActivity.this, name);
    }

    private void switchBottomBar() {
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
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_NONE, serializable);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == from && FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == to) {
            resetCurrentFilterSortTabIndex();
            if (fragmentSequenceArray != null) {
                redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_RIGHT2LEFT, serializable);
                fragmentSequenceArray.add(C_L_LINK);
            }
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == from && FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == to) {
            resetCurrentFilterSortTabIndex();
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

    public CategoryBaseBean.CategoryBean.ChildrenBeanX getSearchCategoryEntity() {
        return searchCategoryEntity;
    }

    /**
     * @param type FragmentType
     */
    public SVRAppserviceProductSearchParameter getSVRAppserviceProductSearchParameterById(int type, int index) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            return getSVRAppserviceProductSearchParameterById(index);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            return getSvrAppserviceProductSearchParameter();
        } else {
            return null;
        }
    }

    private SVRAppserviceProductSearchParameter getSVRAppserviceProductSearchParameterById(int index) {
        if (index < 0 || searchCategoryParameterArrayList == null || searchCategoryParameterArrayList.size() <= index) {
            return null;
        } else {
            return searchCategoryParameterArrayList.get(index);
        }
    }

    private SVRAppserviceProductSearchParameter getSvrAppserviceProductSearchParameter() {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }
        return svrAppserviceProductSearchParameter;
    }

    public void setSVRAppserviceProductSearchParameterCategoryId(int type, int index, String categoryId) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterCategoryId(index, categoryId);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterCategoryId(categoryId);
        }
    }

    private void setSVRAppserviceProductSearchParameterCategoryId(int index, String categoryId) {
        if (index < 0) {
            return;
        }
        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }
        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setCategory_id(categoryId);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setCategory_id(categoryId);
        }
    }

    private void setSVRAppserviceProductSearchParameterCategoryId(String categoryId) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setCategory_id(categoryId);
    }

    ///////////////////////////////////////////////set brandId begin/////////////////////////////////////////////////////

    public void setSVRAppserviceProductSearchParameterBrandId(int type, int index, String brandId) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterBrandId(index, brandId);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterBrandId(brandId);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrandId(int index, String brandId) {
        if (index < 0) {
            return;
        }
        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrandId(brandId);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setBrandId(brandId);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrandId(String brandId) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setBrandId(brandId);
    }

    //////////////////////////////////////////set brandId end/////////////////////////////////////////////////////////

    @Override
    public void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int type, int index, long minPrice, long maxPrice) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterMinPriceMaxPrice(index, minPrice, maxPrice);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterMinPriceMaxPrice(minPrice, maxPrice);
        }
    }

    private void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(int index, long minPrice, long maxPrice) {
        if (index < 0) {
            return;
        }

        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setPrice(minPrice + "-" + maxPrice);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setPrice(minPrice + "-" + maxPrice);
        }
    }

    private void setSVRAppserviceProductSearchParameterMinPriceMaxPrice(long minPrice, long maxPrice) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setPrice(minPrice + "-" + maxPrice);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void setSVRAppserviceProductSearchParameterBrand(int type, int index, String brandValue) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterBrand(index, brandValue);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterBrand(brandValue);
        }
    }

    public void setSVRAppserviceProductSearchParameterBrandName(int index, String brandValue) {
        if (index < 0) {
            return;
        }
        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();

        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrandName(brandValue);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setBrandName(brandValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrand(int index, String brandValue) {
        if (index < 0) {
            return;
        }
        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();

        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setBrand(brandValue);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setBrand(brandValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterBrand(String brandValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setBrand(brandValue);
    }

    public void setSVRAppserviceProductSearchParameterType(int type, int index, String typeValue) {
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterType(index, typeValue);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterType(typeValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterType(int index, String typeValue) {
        if (index < 0) {
            return;
        }

        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    parameter.setModel_type(typeValue);
                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            searchCategoryParameterArrayList.get(index).setModel_type(typeValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterType(String typeValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        svrAppserviceProductSearchParameter.setModel_type(typeValue);
    }

    public void setSVRAppserviceProductSearchParameterSort(int type, int index, String sortValue) {
        JLogUtils.i("ray","type:"+type+"  index:"+index+"  sortvalue:"+sortValue);
        if (FRAGMENT_TYPE_PRODUCTLIST_CATEGORY == type) {
            setSVRAppserviceProductSearchParameterSort(index, sortValue);
        } else if (FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS == type) {
            setSVRAppserviceProductSearchParameterSort(sortValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterSort(int index, String sortValue) {
        if (index < 0) {
            return;
        }

        if (searchCategoryParameterArrayList == null) {
            searchCategoryParameterArrayList = new ArrayList<>();
        }

        int arraySize = searchCategoryParameterArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                SVRAppserviceProductSearchParameter parameter = new SVRAppserviceProductSearchParameter();
                if (newInstallIndex == index) {
                    String orderValue = null;
                    String dirValue = null;

                    if (!JDataUtils.isEmpty(sortValue)) {
                        String[] sortValueArray = sortValue.split("__");
                        if (sortValueArray != null && sortValueArray.length >= 2) {
                            orderValue = sortValueArray[0];
                            dirValue = sortValueArray[1];
                        }
                    }

                    parameter.setOrder(orderValue);
                    parameter.setDir(dirValue);

                    searchCategoryParameterArrayList.add(parameter);
                    break;
                } else {
                    searchCategoryParameterArrayList.add(parameter);
                    continue;
                }
            }
        } else {
            String orderValue = null;
            String dirValue = null;

            if (!JDataUtils.isEmpty(sortValue)) {
                String[] sortValueArray = sortValue.split("__");
                if (sortValueArray != null && sortValueArray.length >= 2) {
                    orderValue = sortValueArray[0];
                    dirValue = sortValueArray[1];
                }
            }
            JLogUtils.i("Martin", "sortValue=>" + sortValue + "  orderValue=>" + orderValue + "  dirValue=>" + dirValue);
            searchCategoryParameterArrayList.get(index).setOrder(orderValue);
            searchCategoryParameterArrayList.get(index).setDir(dirValue);
        }
    }

    private void setSVRAppserviceProductSearchParameterSort(String sortValue) {
        if (svrAppserviceProductSearchParameter == null) {
            svrAppserviceProductSearchParameter = new SVRAppserviceProductSearchParameter();
        }

        String orderValue = null;
        String dirValue = null;

        if (!JDataUtils.isEmpty(sortValue)) {
            String[] sortValueArray = sortValue.split("__");
            if (sortValueArray != null && sortValueArray.length >= 2) {
                orderValue = sortValueArray[0];
                dirValue = sortValueArray[1];
            }
        }
        JLogUtils.i("Martin", "sortValue=>" + sortValue + "  orderValue=>" + orderValue + "  dirValue=>" + dirValue);
        svrAppserviceProductSearchParameter.setOrder(orderValue);
        svrAppserviceProductSearchParameter.setDir(dirValue);
    }

    public int getPrevType() {
        return prevType;
    }

    public int getCurrentFilterSortTabIndex() {
        return currentFilterSortTabIndex;
    }

    public void resetCurrentFilterSortTabIndex() {
        currentFilterSortTabIndex = TABBAR_INDEX_NONE;
        switchBottomBar();
    }

    public int getCurrentProductListFragmentPosition() {
        return currentProductListFragmentPosition;
    }

    public void setCurrentProductListFragmentPosition(int currentProductListFragmentPosition) {
        this.currentProductListFragmentPosition = currentProductListFragmentPosition;
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
        if(type==0){
            if (mCurrentFragment != null) {
                mCurrentFragment.onSortWidgetClick(currentFilterSortTabIndex != TABBAR_INDEX_SORT);
                currentFilterSortTabIndex = currentFilterSortTabIndex == TABBAR_INDEX_SORT ? TABBAR_INDEX_NONE : TABBAR_INDEX_SORT;
            }
        }else{
            filterSortBottomView.setFilterStatus(0, null);
            if (mCurrentFragment != null) {
                mCurrentFragment.onFilterWidgetClick(currentFilterSortTabIndex != TABBAR_INDEX_FILTER);
                currentFilterSortTabIndex = currentFilterSortTabIndex == TABBAR_INDEX_FILTER ? TABBAR_INDEX_NONE : TABBAR_INDEX_FILTER;
            }
        }
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
}

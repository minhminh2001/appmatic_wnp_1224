package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchParameter;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.TMPProductListFilterSortPageEntity;
import com.whitelabel.app.utils.FilterSortHelper;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTabCustomPageIndicator;
import com.whitelabel.app.widget.FilterSortBottomView;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/14.
 */
public class ProductListCategoryLandingFragment extends ProductListBaseFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener, OnFilterSortFragmentListener,FilterSortBottomView.FilterSortBottomViewCallBack {
    private final String TAG = "ProductListCategoryLandingFragment";
    protected ProductListActivity productListActivity;
    protected View mContentView;
    private String categoryId;
    private CustomTabCustomPageIndicator ctpiCategoryList;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> categoryArrayList;
    private ProductListFilterFragment filterFragment;
    private ProductListSortFragment sortFragment;
    private FilterSortHelper filterSortHelper;
    public FilterSortBottomView filterSortBottomView;
    private ArrayList<ProductListProductListFragment> categoryProductListFragmentArrayList;
    public ImageView mTopViewToggleIV;
    public RelativeLayout mTopFilterAndSortBarRL;
    public boolean mIsShowSwitchFilterBar;
    public ImageView mIVBottomSlideToTop;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            productListActivity = (ProductListActivity) context;
            filterFragment = new ProductListFilterFragment();
            filterFragment.setFragmentListener(this);
            sortFragment = new ProductListSortFragment();
            sortFragment.setFragmentListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_productlist_categorylanding, null);
        productListActivity.mGATrackTimeStart= GaTrackHelper.getInstance().googleAnalyticsTimeStart();
//        productListActivity.mGATrackTimeEnable=true;
        setContentView(mContentView);
        return mContentView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_shopping_cart, menu);
        MenuItem menuItem= menu.findItem(R.id.action_shopping_cart);
        MenuItemCompat.setActionView(menuItem, R.layout.item_count);

        MenuItem   searchItem=menu.findItem(R.id.action_search);
        View searchView=searchItem.getActionView();
        ImageView ivSearch= (ImageView) searchView.findViewById(R.id.iv_img1);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productListActivity != null) {
                    filterSortBottomView.hideSwitchAndFilterBar(true);
                    productListActivity.switchFragment(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, null);
                }
            }
        });
        JViewUtils.setNavBarIconColor(getActivity(),ivSearch,R.drawable.ic_action_search);
        View view=menuItem.getActionView();
        view. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShoppingCartActivity();
            }
        });
        TextView textView= (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(getActivity()));
        ImageView  ivShopping= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),ivShopping,R.drawable.ic_action_cart);
        long cartCount=getCartItemCount();
        if(cartCount>0&&cartCount<=99){
            textView.setVisibility(View.VISIBLE);
            textView.setText(cartCount + "");
        }else if(cartCount>99) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("99+");
        }else{
            textView.setVisibility(View.INVISIBLE);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void gotoShoppingCartActivity(){
        if (productListActivity != null) {
            Intent intent = new Intent(productListActivity, ShoppingCartActivity1.class);
            startActivity(intent);
            productListActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_shopping_cart:
                gotoShoppingCartActivity();
                break;
            case R.id.action_search:

                if (productListActivity != null) {
                    filterSortBottomView.hideSwitchAndFilterBar(true);
                    productListActivity.switchFragment(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS, null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initToolBar(){
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_back));
        setLeftMenuClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolBar();
        int FRAGMENT_CONTAINER_ID = R.id.flFilterSortContainer;
        try {
            GaTrackHelper.getInstance().googleAnalytics("Sub Category Screen", getActivity());
            JLogUtils.i("googleAnalytics", "Sub Category Screen");

        }catch (Exception ex){
            ex.getStackTrace();
        }

        mIVBottomSlideToTop = (ImageView) mContentView.findViewById(R.id.iv_bottom_slideto_top);
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
        ctpiCategoryList = (CustomTabCustomPageIndicator) mContentView.findViewById(R.id.ctpiCategoryList);
        ctpiCategoryList.setIndicatorColorResource(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        ViewPager vpProductList = (ViewPager) mContentView.findViewById(R.id.vpProductList);
        FrameLayout flFilterSortContainer = (FrameLayout) mContentView.findViewById(R.id.flFilterSortContainer);
        flFilterSortContainer.setOnClickListener(this);
        if(getArguments()!=null) {
            categoryId = (String) getArguments().getSerializable("data");
        }
        String allCategoryName = null;
        int parentCategoryIndex = 0;
        int categoryViewCount = 0;
        categoryArrayList = new ArrayList<>();
        SVRAppserviceCatalogSearchCategoryItemReturnEntity entity = productListActivity.getSearchCategoryEntity();

        //all暂时取消掉  ray
         SVRAppserviceProductSearchParameter  parameter= productListActivity.getSVRAppserviceProductSearchParameterById(productListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS,-1);
        //需求隐藏 Gem Brands 的ALL  GEM Brands 的
        String gemBradns=productListActivity.getResources().getString(R.string.gembrand);
        boolean   isGemBrands=false;
            if(!TextUtils.isEmpty(parameter.getName())&&gemBradns.equals(parameter.getName().replace(" ","").toUpperCase())){
                isGemBrands=true;
            }
        if ((entity != null) && (!JDataUtils.isEmpty(entity.getId()))) {
            SVRAppserviceCatalogSearchCategoryItemReturnEntity allCategory = new SVRAppserviceCatalogSearchCategoryItemReturnEntity();
            allCategory.setId(entity.getId());
            allCategory.setName(getString(R.string.productlist_categorylanding_allcategory));
            allCategory.setLevel(entity.getLevel());
            allCategory.setChildren(null);
            allCategoryName = entity.getName();
            if (!JDataUtils.isEmpty(allCategoryName)) {
                allCategoryName = allCategoryName.toUpperCase();
            }

            if (entity.getChildren() != null && entity.getChildren().size() > 0) {
                final int categoryChildrenSize = entity.getChildren().size();
                categoryViewCount = categoryChildrenSize;
                parentCategoryIndex = 0;
                for (int index = 0; index < categoryChildrenSize; ++index) {
                    if(!isGemBrands) {//当为GEM Brands  隐藏ALL
                        //是第0个并且entity的第0个不是all,防止重复添加
                        if (index == parentCategoryIndex&&entity.getId()!=entity.getChildren().get(parentCategoryIndex).getId()) {
                            categoryArrayList.add(allCategory);
                        }
                    }
                    categoryArrayList.add(entity.getChildren().get(index));
                }
            } else {
                if(!isGemBrands) {
                    categoryArrayList.add(allCategory);
                }
            }
        }
        setTitle(allCategoryName);
        if(categoryArrayList!=null&& categoryId !=null&&!"0".equals(categoryId)) {
            for (int i = 0; i < categoryArrayList.size(); i++) {
                   if(categoryArrayList.get(i).getId().equals(categoryId)){
                    parentCategoryIndex=i;
                }
            }
        }
        if(getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            //默认选择的 page由 currentProductListFragmentPosition优先控制
            if(productListActivity.getCurrentProductListFragmentPosition()==0) {
                productListActivity.setCurrentProductListFragmentPosition(parentCategoryIndex);
            }
            JLogUtils.i("Martin", "currentProductListFragmentPosition=>" + parentCategoryIndex);
            CustomTabPageIndicatorAdapter fragmentPagerAdapter = new CustomTabPageIndicatorAdapter(getChildFragmentManager());
            vpProductList.setAdapter(fragmentPagerAdapter);

            ctpiCategoryList.setViewPager(vpProductList);

            vpProductList.setOffscreenPageLimit(categoryViewCount);
            vpProductList.addOnPageChangeListener(this);
            vpProductList.setCurrentItem(productListActivity.getCurrentProductListFragmentPosition());
            JLogUtils.i("ray","currlog:"+productListActivity.getCurrentProductListFragmentPosition());
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ctpiCategoryList.setSelectColor(productListActivity.getCurrentProductListFragmentPosition());

                }
            });
        }

        filterSortHelper = new FilterSortHelper(getActivity(), sortFragment, filterFragment, flFilterSortContainer, FRAGMENT_CONTAINER_ID);
    }
    public long getCartItemCount(){
        long cartItemCount = 0;
        try{
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(getActivity());
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return cartItemCount;
    }
    @Override
    public void onResume() {
        super.onResume();

        long cartItemcount=getCartItemCount();
        updateRightIconNum(R.id.action_shopping_cart, cartItemcount);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_bottom_slideto_top:
                onSlideToTop();
                break;
            case R.id.ll_sort_top:
                productListActivity.filterSortOption(productListActivity.TYPE_SORT);
                break;
            case R.id.ll_filter_top:
                productListActivity.filterSortOption(productListActivity.TYPE_FILTER);
                break;
            case R.id.iv_view_toggle_top:
                int position=productListActivity.getCurrentProductListFragmentPosition();
                if(position<categoryProductListFragmentArrayList.size()) {
                    categoryProductListFragmentArrayList.get(position).toggleViewOption();
                }
                break;
        }
    }

    /**
     * @param index From 0 to X
     */
    private ProductListProductListFragment getCategoryProductListFragmentById(int index) {
        if (categoryProductListFragmentArrayList == null || index < 0) {
            return null;
        }
        if (categoryProductListFragmentArrayList.size() > index) {
            return categoryProductListFragmentArrayList.get(index);
        } else {
            return null;
        }
    }

    /**
     * @param index From 0 to X
     */
    private void setCategoryProductListFragment(int index, ProductListProductListFragment fragment) {
        if (index < 0) {
            return;
        }

        if (categoryProductListFragmentArrayList == null) {
            categoryProductListFragmentArrayList = new ArrayList<>();
        }

        int arraySize = categoryProductListFragmentArrayList.size();
        if (index >= arraySize) {
            for (int newInstallIndex = arraySize; newInstallIndex <= index; ++newInstallIndex) {
                if (newInstallIndex == index) {
                    categoryProductListFragmentArrayList.add(fragment);
                    break;
                } else {
                    categoryProductListFragmentArrayList.add(new ProductListProductListFragment());
                    continue;
                }
            }
        } else {
            categoryProductListFragmentArrayList.set(index, fragment);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ProductListProductListFragment fragment = getCategoryProductListFragmentById(position);
        if (fragment != null && fragment.getProductItemEntityArrayList() != null && fragment.getProductItemEntityArrayList().size() > 0) {
            filterSortBottomView.hideSwitchAndFilterBar(false);
        } else {
            filterSortBottomView.hideSwitchAndFilterBar(true);
        }
        productListActivity.setCurrentProductListFragmentPosition(position);

        try {
            String categoryA=  productListActivity.searchCategoryEntity.getName();
            String categoryA_B=  categoryArrayList.get(position).getName();
            GaTrackHelper.getInstance().googleAnalytics(categoryA + "->" + categoryA_B, productListActivity);
            JLogUtils.i("googleGA_screen",categoryA+"->"+categoryA_B);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSlideToTop() {
        int position=productListActivity.getCurrentProductListFragmentPosition();
        if(position<categoryProductListFragmentArrayList.size()) {
            categoryProductListFragmentArrayList.get(position).onSlideToTop();
        }
    }

    @Override
    public void onBackPressed() {
        if (productListActivity != null) {
            if (filterSortHelper.isAnyActive()) {
                filterSortDefault();
            } else {
                productListActivity.finish();
                productListActivity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
            }
        }
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

    private Bundle createBundle() {
        TMPProductListFilterSortPageEntity filterSortPageEntity = new TMPProductListFilterSortPageEntity();
        filterSortPageEntity.setPreviousFragmentType(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
        filterSortPageEntity.setCategoryFragmentPosition(productListActivity.getCurrentProductListFragmentPosition());

        ProductListProductListFragment productListFragment = getCategoryProductListFragmentById(productListActivity.getCurrentProductListFragmentPosition());

        if (productListFragment != null) {
            filterSortPageEntity.setFacets(productListFragment.getSearchReturnEntityFacets());
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", filterSortPageEntity);
        return bundle;
    }

    @Override
    public void onCancelClick(View view) {
        resetSelection();
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
        ProductListProductListFragment currentProductListFragment = getCategoryProductListFragmentById(productListActivity.getCurrentProductListFragmentPosition());
        resetSelection();
        if (currentProductListFragment != null) {
            currentProductListFragment.searchByType(ProductListProductListFragment.SEARCH_TYPE_INIT);
        }
    }

    private void resetSelection() {
        productListActivity.resetCurrentFilterSortTabIndex();
        filterSortHelper.hideVisibleFragments();
    }

    @Override
    public int getCurrentFilterSortTabIndex() {
        return productListActivity.getCurrentFilterSortTabIndex();
    }

    class CustomTabPageIndicatorAdapter extends FragmentPagerAdapter {
        public CustomTabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            ProductListProductListFragment productListProductListFragment = new ProductListProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("fragmentType", ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
            bundle.putInt("position", position);
            productListProductListFragment.setArguments(bundle);

            setCategoryProductListFragment(position, productListProductListFragment);

            String categoryId = null;
            String brandId = null;
            String brandName=null;
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                SVRAppserviceCatalogSearchCategoryItemReturnEntity category = categoryArrayList.get(position);
                if (category != null) {
                    categoryId = category.getId();
                    brandId = category.getBrandId();
                    brandName=category.getBrandName();

                }
            }
            productListActivity.setSVRAppserviceProductSearchParameterCategoryId(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, position, categoryId);
            productListActivity.setSVRAppserviceProductSearchParameterBrandId(ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY, position, brandId);
            productListActivity.setSVRAppserviceProductSearchParameterBrandName(position, brandName);
            return productListProductListFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String categoryName = null;
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                final int categoryArrayListSize = categoryArrayList.size();
                SVRAppserviceCatalogSearchCategoryItemReturnEntity category = categoryArrayList.get(position % categoryArrayListSize);
                if (category != null) {
                    categoryName = category.getName();
                }
            }
            return categoryName;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (categoryArrayList != null) {
                count = categoryArrayList.size();
            }
            return count;
        }
    }
}

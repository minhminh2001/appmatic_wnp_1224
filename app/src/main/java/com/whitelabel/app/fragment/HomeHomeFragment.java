package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV3;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV4;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomHomeViewPager;
import com.whitelabel.app.widget.CustomTabCustomPageIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/17.
 */
public class HomeHomeFragment extends HomeBaseFragment implements HomeActivity.HomeFragmentCallback{
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    private HomeActivity homeActivity;
    private View mContainView;
    private CustomTabCustomPageIndicator ctpiCategoryList;
    private CustomHomeViewPager chvpContainer;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> categoryArrayList;
    private CustomTabPageIndicatorAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> mFragments;
    private int currentCategoryFragmentIndex = 0;
    private Dialog mDialog;
    int everythingIndex = 0;
    int categoryViewCount = 0;
    private String categoryId;
    private String TAG;
    private ProductDao productDao;
    private View rlHome;
    private View ll_error;
    public boolean firstloaded = false;
    public  static  final  int TYPE_FRAGMENT_HORIZONTAL=1;
    public  static  final int TYPE_FRAGMENT_VERTICAL=2;
    private int  fragmentType;
    private final static String PARAM1="param1";
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        homeActivity = (HomeActivity) activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            fragmentType=getArguments().getInt(PARAM1);
        }
        JLogUtils.i("ray","fragmentType:"+fragmentType);
    }
    public static   HomeHomeFragment  newInstance(int fragmentType){
        HomeHomeFragment homeHomeFragment=new HomeHomeFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(PARAM1,fragmentType);
        homeHomeFragment.setArguments(bundle);
        return homeHomeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setScrollToolBarEnable(true);
        mContainView = inflater.inflate(R.layout.fragment_home_home, null);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        setRetryTheme(mContainView);
        return mContainView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mCommonCallback.setHomeSearchBarAndOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(homeActivity, ProductListActivity.class);
                intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                homeActivity.startActivity(intent);
//                homeActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
            }
        });
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem cartItem = menu.findItem(R.id.action_shopping_cart);
        MenuItemCompat.setActionView(cartItem, R.layout.item_count);
        View view = cartItem.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeActivity, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ImageView ivImg= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),ivImg,R.drawable.action_cart);
        TextView textView = (TextView) view.findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setBackground(JImageUtils.getThemeCircle(getActivity()));
        JViewUtils.updateCartCount(textView, getCartItemCount());
    }
    public long getCartItemCount() {
        long cartItemCount = 0;
        try {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(homeActivity)) {
                cartItemCount = WhiteLabelApplication.getAppConfiguration().getUserInfo(homeActivity).getCartItemCount();
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(homeActivity);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            } else {
                ArrayList<TMPLocalCartRepositoryProductEntity> list = JStorageUtils.getProductListFromLocalCartRepository(homeActivity);
                if (list.size() > 0) {
                    for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : list) {
                        cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                    }
                }
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return cartItemCount;
    }
    @Override
    public void onStop() {
        super.onStop();
        hideLoaderDialog();
        hideErrorLayout();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommonCallback.switchMenu(HomeCommonCallback.MENU_HOME);

        ctpiCategoryList = (CustomTabCustomPageIndicator) mContainView.findViewById(R.id.ctpiCategoryList);
        ctpiCategoryList.setIndicatorColorResource(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());

        chvpContainer = (CustomHomeViewPager) mContainView.findViewById(R.id.chvpContainer);
        rlHome = mContainView.findViewById(R.id.rl_home);
        rlHome.setVisibility(View.GONE);

        TAG = this.getClass().getSimpleName();
        DataHandler mHandler = new DataHandler(getActivity(), this);
        productDao = new ProductDao(TAG, mHandler);
        if (getArguments() != null) {
            categoryId = (String) getArguments().getSerializable("data");
        }
        resetData();
        requestData();
        setHasOptionsMenu(true);
    }

    public void resetData(){
        everythingIndex = 0;
        categoryViewCount = 0;
        categoryArrayList = new ArrayList<>();
    }

    public void requestData(){
        if (!firstloaded) {
            showLoaderDialog();
            productDao.getBaseCategory();
        } else {
            SVRAppserviceCatalogSearchReturnEntity catalogSearchReturnEntity = WhiteLabelApplication.getAppConfiguration().getCategoryArrayList();
            for (int i = 0; i < catalogSearchReturnEntity.getCategory().size(); i++) {
                JLogUtils.d("Category", catalogSearchReturnEntity.getCategory().get(i).getName());
            }
            initData(catalogSearchReturnEntity);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mFragments.clear();
            fragmentPagerAdapter = null;
            chvpContainer.setAdapter(null);
            chvpContainer.removeAllViews();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    private void showOnlineErrorLayout(int errorMessage) {
        if (getActivity() != null) {
            inflateIfNeeded();
            ll_error.setVisibility(View.VISIBLE);
            RequestErrorHelper requestErrorHelper = new RequestErrorHelper(getContext(), ll_error);
            requestErrorHelper.showConnectionBreaks(errorMessage);
            requestErrorHelper.setResponseListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLoaderDialog();
                    productDao.getBaseCategory();
                }
            });
        }
    }

    private void inflateIfNeeded() {
        if (ll_error == null) {
            ll_error = ((ViewStub) mContainView.findViewById(R.id.vs_offline)).inflate();
            ImageView ivTryAgain= (ImageView) ll_error.findViewById(R.id.iv_try_again);
            CustomButton btnAgain= (CustomButton)  ll_error.findViewById(R.id.btn_try_again);
            if(ivTryAgain!=null&&btnAgain!=null){
                ivTryAgain.setImageDrawable(JImageUtils.getThemeIcon(getActivity(),R.mipmap.connection_break_loading));
                btnAgain.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            }
        }
    }

    private void hideErrorLayout() {
        if (ll_error != null && ll_error.getVisibility() == View.VISIBLE) {
            ll_error.setVisibility(View.GONE);
        }
    }
    private static class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeHomeFragment> mFragment;
        public DataHandler(Activity activity, HomeHomeFragment fragment) {
            mActivity = new WeakReference<>(activity);
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            HomeHomeFragment fragment = mFragment.get();
            switch (msg.what) {
                case ProductDao.REQUEST_CATALOGSEARCH:
                    fragment.hideLoaderDialog();
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        fragment.firstloaded = true;
                        SVRAppserviceCatalogSearchReturnEntity searchCatalog = (SVRAppserviceCatalogSearchReturnEntity) msg.obj;
                        if (searchCatalog != null) {
                            fragment.firstloaded = true;
                            WhiteLabelApplication.getAppConfiguration().setCategoryArrayList(searchCatalog);
                            JStorageUtils.saveCategoryArrayList(mActivity.get(), searchCatalog);
                            fragment.initData(searchCatalog);
                        }
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    fragment.hideLoaderDialog();
                    fragment.showOnlineErrorLayout(msg.arg2);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void showLoaderDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(getActivity());
        }
        mDialog.show();
    }
    private void hideLoaderDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    private void initData(SVRAppserviceCatalogSearchReturnEntity searchCatalog) {
        if (getActivity() != null) {
            if(mFragments !=null&& mFragments.size()>0){
                for(int i = 0; i< mFragments.size(); i++){
                    ((HomeHomeFragmentV3) mFragments.get(i)).onRefresh();
                }
            }else {
                rlHome.setVisibility(View.VISIBLE);
                hideErrorLayout();
                categoryArrayList = searchCatalog.getCategory();
                //initFragment();
                mFragments = new ArrayList<>();
                if (categoryArrayList != null) {
                    for (int i = 0; i < categoryArrayList.size(); i++) {
                        mFragments.add(createFragmentByIndex(i));
                    }
                }
                categoryViewCount = searchCatalog.getCategory().size() - 1;
                fragmentPagerAdapter = new CustomTabPageIndicatorAdapter(getChildFragmentManager());
                chvpContainer.setAdapter(fragmentPagerAdapter);
                ctpiCategoryList.setViewPager(chvpContainer);
                //page change listener -- russell
                ctpiCategoryList.setOnPageChangeListener(pageChangeListener);
                everythingIndex = getCurrIndex(categoryId) == -1 ? everythingIndex : getCurrIndex(categoryId);
                chvpContainer.setOffscreenPageLimit(categoryViewCount);
                JLogUtils.i(TAG, "everythingIndex:" + everythingIndex);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ctpiCategoryList.setSelectColor(everythingIndex);
                        chvpContainer.setCurrentItem(everythingIndex);
                    }
                });
                updateShoppingCartItemCount();
            }
        }
        if (mGATrackTimeEnable) {
            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, mGATrackTimeStart, "Home Screen Loading"
            );
            mGATrackTimeEnable = false;
        }
    }
    public Fragment  createFragmentByIndex(int  index){
        Fragment fragment=null;
        if(fragmentType==TYPE_FRAGMENT_HORIZONTAL){
            fragment=HomeHomeFragmentV4.newInstance(index, categoryArrayList.get(index).getId());
        }else if(fragmentType==TYPE_FRAGMENT_VERTICAL){
            fragment=HomeHomeFragmentV3.newInstance(index, categoryArrayList.get(index).getId());
        }
        return fragment;
    }
    public static boolean isCategory(SVRAppserviceCatalogSearchCategoryItemReturnEntity categoryEntity) {
        return categoryEntity != null && categoryEntity.getChildren() != null && categoryEntity.getChildren().size() > 0;
    }
    public int getCurrIndex(String categoryId) {
        if (categoryArrayList != null) {
            for (int i = 0; i < categoryArrayList.size(); i++) {
                if (categoryArrayList.get(i).getId().equals(categoryId)) {
                    return i;
                }
            }
        }
        return -1;
    }
    public int getCurrentFragmentIndex() {
        return currentCategoryFragmentIndex;
    }
    /**
     * need to time refresh cartItemCount, so must get data from webservice
     */
    public void updateShoppingCartItemCount() {
        long cartItemcount = getCartItemCount();
        mCommonCallback.updateRightIconNum(R.id.action_shopping_cart, cartItemcount);
    }
    class CustomTabPageIndicatorAdapter extends FragmentPagerAdapter {
        public CustomTabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            JLogUtils.i("zzz", "getItem=" + position);
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                fragment = mFragments.get(position);
                bundle.putSerializable("categoryEntity", categoryArrayList.get(position));
                bundle.putInt("index", position);
            }
//            fragment.setArguments(bundle);
            return fragment;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String categoryName = null;
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                final int categoryArrayListSize = categoryArrayList.size();
                position = position % categoryArrayListSize;
                SVRAppserviceCatalogSearchCategoryItemReturnEntity category = categoryArrayList.get(position);
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
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            if (categoryArrayList.size() <= position) return;
            mFragments.get(currentCategoryFragmentIndex).onPause();
            currentCategoryFragmentIndex = position;
            mFragments.get(position).onResume();
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };



}
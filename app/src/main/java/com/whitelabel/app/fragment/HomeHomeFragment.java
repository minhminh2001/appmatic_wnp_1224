package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.home.HomeCategoryDetailFragment;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomHomeViewPager;
import com.whitelabel.app.widget.CustomTabCustomPageIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/17.
 */
public class HomeHomeFragment extends HomeBaseFragment implements View.OnClickListener {
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    private HomeActivity homeActivity;
    private View mContainView;
    private CustomTabCustomPageIndicator ctpiCategoryList;
    private CustomHomeViewPager chvpContainer;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> categoryArrayList;
    private CustomTabPageIndicatorAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> mFragment;
    private int currentCategoryFragmentIndex = 0;
    private Dialog mDialog;
    int everythingIndex = 0;
    int categoryViewCount = 0;
    private String categoryId;
    private String TAG;
    private boolean isShowAppRatePopup = false;
    private PopupWindow popupWindow;
    private TextView rateNow, askMeLater, noThanks;
    private View layout;
    private DataHandler mHandler;
    private ProductDao productDao;
    private View rlHome;
    private View ll_error;
    public boolean firstloaded = false;
    private RequestErrorHelper requestErrorHelper;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        homeActivity = (HomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setScrollToolBarEnable(true);
        mContainView = inflater.inflate(R.layout.fragment_home_home, null);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
//        mGATrackTimeEnable = true;
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
                homeActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
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
                } else {
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
        TAG = this.getClass().getSimpleName();
        mHandler = new DataHandler(getActivity(), this);
        productDao = new ProductDao(TAG, mHandler);
        layout = mContainView.findViewById(R.id.rl_root);
        if (getArguments() != null) {
            categoryId = (String) getArguments().getSerializable("data");
        }
        ctpiCategoryList = (CustomTabCustomPageIndicator) mContainView.findViewById(R.id.ctpiCategoryList);
        ctpiCategoryList.setIndicatorColorResource(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        chvpContainer = (CustomHomeViewPager) mContainView.findViewById(R.id.chvpContainer);
        rlHome = mContainView.findViewById(R.id.rl_home);
        rlHome.setVisibility(View.GONE);
        everythingIndex = 0;
        categoryViewCount = 0;
        categoryArrayList = new ArrayList<>();
        requestData();
        setHasOptionsMenu(true);
    }


    public void requestData(){
        if (!firstloaded) {//first request  data;
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
        mCommonCallback.marketLayerClose();
        try {
            mFragment.clear();
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
            requestErrorHelper = new RequestErrorHelper(getContext(), ll_error);
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
            rlHome.setVisibility(View.VISIBLE);
            hideErrorLayout();
            boolean showGuide = JStorageUtils.showAppGuide1(homeActivity);
            if (showGuide) {
                mCommonCallback.showUserGuide(UserGuideType.HOMELEFTICON);
            }
            //categoryArrayList.addAll(searchCatalog.getCategory());
            categoryArrayList = searchCatalog.getCategory();
            //initFragment();
            mFragment = new ArrayList<>();
            if (categoryArrayList != null) {
                for (int i = 0; i < categoryArrayList.size(); i++) {
                    mFragment.add(HomeCategoryDetailFragment.newInstance(i,categoryArrayList.get(i).getId()));
                    JLogUtils.i("ray","mCategoryId:"+categoryArrayList.get(i).getId());
//                    mFragment.add(HomeHomeCategoryFragment.newInstance(categoryArrayList.get(i).getId()));
                }
            }
            //////////////////////////ray
//            if (mFragment != null && mFragment.size() > 0) {
//                HomeCategoryDetailFragment fragment = (HomeCategoryDetailFragment) mFragment.get(0);
//                fragment.setmCurrDisplayedAndData(0, categoryArrayList.get(0));
//            }
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
        if (mGATrackTimeEnable) {
            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_IMPRESSION, mGATrackTimeStart, "Home Screen Loading"
            );
            mGATrackTimeEnable = false;
        }
    }

    public void showAppRate() {
//        if (JStorageUtils.isShowAppRate(homeActivity) && !WhiteLabelApplication.delayShowAppRate && !JStorageUtils.showAppGuide1(homeActivity) && JStorageUtils.isClickDelayShow(homeActivity)) {
//            if (!!homeActivity.getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        JLogUtils.i("Allen", "APPRate");
//                        isShowAppRatePopup = true;
//                        appRate();
//                    }
//                }, 300);
//            }
//        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isShowAppRatePopup) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                isShowAppRatePopup = false;
                JStorageUtils.notShowAppRate(homeActivity);
                return true;
            }
        }
        return false;
    }

    public void appRate() {
        if (getActivity() != null) {
            LayoutInflater inflater = (LayoutInflater) homeActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupWindowView = inflater.inflate(R.layout.popupwindow_app_rate, null);
            popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, false);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            rateNow = (TextView) popupWindowView.findViewById(R.id.rate_now);
            askMeLater = (TextView) popupWindowView.findViewById(R.id.ask_me_later);
            noThanks = (TextView) popupWindowView.findViewById(R.id.no_thanks);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            rateNow = (TextView) popupWindowView.findViewById(R.id.rate_now);
            rateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    isShowAppRatePopup = false;
                    JStorageUtils.notShowAppRate(homeActivity);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setData(Uri.parse(getString(R.string.play_store_url)));
                    startActivity(intent);
                }
            });
            noThanks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    isShowAppRatePopup = false;
                    JStorageUtils.notShowAppRate(homeActivity);
                }
            });
            askMeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    isShowAppRatePopup = false;
                    WhiteLabelApplication.delayShowAppRate = true;
                }
            });
        }
    }

    public void switchTab(String categoryId) {

        try {
            int index = getCurrIndex(categoryId);
            everythingIndex = index == -1 ? 0 : index;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    chvpContainer.setCurrentItem(everythingIndex);
                }
            });
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    public void onResume() {
        //initSarchAnim();
        super.onResume();

    }

    private void initFragment() {
        mFragment = new ArrayList<Fragment>();
        if (categoryArrayList != null) {
            for (int i = 0; i < categoryArrayList.size(); i++) {
                mFragment.add(new HomeCategoryDetailFragment());
            }
        }
    }

    public static boolean isCategory(SVRAppserviceCatalogSearchCategoryItemReturnEntity categoryEntity) {
        if (categoryEntity != null && categoryEntity.getChildren() != null && categoryEntity.getChildren().size() > 0) {
            return true;
        }
        return false;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
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
                fragment = mFragment.get(position);
                bundle.putSerializable("categoryEntity", categoryArrayList.get(position));
                bundle.putInt("index", position);
            }
//            fragment.setArguments(bundle);
            return fragment;
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
//            if (isCategory(categoryArrayList.get(position))) {
//                boolean showGuide = JStorageUtils.showAppGuide2(homeActivity);
//                if (showGuide) {
//                    homeActivity.showMarketLayers = false;//禁止弹出广告层
//                    if (mCommonCallback.getRlMarketLayer().getVisibility() == View.VISIBLE) {
//                        mCommonCallback.marketLayerClose();//关闭广告弹出层
//                    }
//                    if (mCommonCallback != null) {
//                        mCommonCallback.showUserGuide(UserGuideType.HOMESECONDPAGE);
//                    }
//                }
//            }

            mFragment.get(currentCategoryFragmentIndex).onPause();
            currentCategoryFragmentIndex = position;
//            ((HomeCategoryDetailFragment) mFragment.get(position)).setmCurrDisplayedAndData(position, categoryArrayList.get(position));
            mFragment.get(position).onResume();
//            homeTrack(position);
        }


        public void homeTrack(int position) {
            try {
                //追踪点击分类
                String brandName = WhiteLabelApplication.getAppConfiguration().getCategoryArrayList().getCategory().get(position).getName();
                GaTrackHelper.getInstance().googleAnalytics(brandName, homeActivity);
                JLogUtils.i("googleGA_screen", brandName);
                //统计总数
                GaTrackHelper.getInstance().googleAnalytics("Main Category Landing Screen", homeActivity);
                JLogUtils.i("googleGA_screen", "Category Landing Screen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            //If it is sliding, close advertisement.
//            if (state == 1) {
//                ((HomeCategoryDetailFragment) mFragment.get(currentCategoryFragmentIndex)).setmMarketShow(false);
//                ((HomeCategoryDetailFragment) mFragment.get(currentCategoryFragmentIndex)).removeOpenMarketRun();
//            }
        }
    };

    public void notifyToCancelCloseMarketRun() {
//        if (isAdded()) {
//            if (mFragment != null && mFragment.get(currentCategoryFragmentIndex) != null) {
//                ((HomeCategoryDetailFragment) mFragment.get(currentCategoryFragmentIndex)).removeCloseMarketRun();
//            }
//        }
    }

}
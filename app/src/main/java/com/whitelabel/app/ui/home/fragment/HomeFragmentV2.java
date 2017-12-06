package com.whitelabel.app.ui.home.fragment;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.ui.home.HomeContract;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PageIntentUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomDialog;
import com.whitelabel.app.widget.CustomHomeViewPager;
import com.whitelabel.app.widget.CustomTabCustomPageIndicator;
import java.util.ArrayList;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by imaginato on 2015/7/17.
 */
public class HomeFragmentV2 extends HomeBaseFragment<HomeContract.Presenter> implements HomeActivity.HomeFragmentCallback,HomeContract.View{
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;
    private View mContainView;
    private CustomTabCustomPageIndicator piPageIndicatory;
    private CustomHomeViewPager vpCategoryViewPager;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> categoryArrayList;
    private CustomTabPageIndicatorAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> mFragments;
    private int currentCategoryFragmentIndex = 0;
    private Dialog mDialog;
    int categoryViewCount = 0;
    private View rlHome;
    private View ll_error;
    public  static  final  int TYPE_FRAGMENT_HORIZONTAL=1;
    public  static  final int TYPE_FRAGMENT_VERTICAL=2;
    private int  fragmentType;
    private final static String PARAM1="param1";
    private boolean isFirstLoading=true;
    String categoryName = "";
    @Override
    public void inject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            fragmentType=getArguments().getInt(PARAM1);
        }
        setSearchOptionMenu(false);
        setHasOptionsMenu(true);
    }
    public static HomeFragmentV2 newInstance(int fragmentType){
        HomeFragmentV2 homeHomeFragment=new HomeFragmentV2();
        Bundle bundle=new Bundle();
        bundle.putInt(PARAM1,fragmentType);
        homeHomeFragment.setArguments(bundle);
        return homeHomeFragment;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mCommonCallback.setHomeSearchBarAndOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProductListActivity.class);
                intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                getActivity().startActivity(intent);

            }
        });inflater.inflate(R.menu.menu_home, menu);

        MenuItem cartItem = menu.findItem(R.id.action_shopping_cart);
        MenuItemCompat.setActionView(cartItem, R.layout.item_count);
        View view = cartItem.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    jumpLoginActivity();
                }
            }
        });
        ImageView ivImg= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),ivImg,R.drawable.action_cart);
        JLogUtils.i("HomeFragmentV2","start");
        mPresenter.getShoppingCount();
    }
    private void jumpLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
        startActivityForResult(intent, 1000);
        getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }
    @Override
    public void showRootView() {
        rlHome.setVisibility(View.VISIBLE);
    }
    @Override
    public void loadData(SVRAppserviceCatalogSearchReturnEntity data) {
             isFirstLoading=false;
            if(mFragments !=null&& mFragments.size()>0){
                for(int i = 0; i< mFragments.size(); i++){
                    if(mFragments.get(i) instanceof HomeHomeFragmentV3){
                        ((HomeHomeFragmentV3) mFragments.get(i)).onRefresh();
                    }else if(mFragments.get(i) instanceof HomeHomeFragmentV4){
                        ((HomeHomeFragmentV4) mFragments.get(i)).onRefresh();
                    }
                }
            }else {
              categoryArrayList = data.getCategory();
              mFragments = new ArrayList<>();
               for (int i = 0; i < categoryArrayList.size(); i++) {
                    mFragments.add(createFragmentByIndex(i));
               }
            categoryViewCount = data.getCategory().size() - 1;
            fragmentPagerAdapter = new CustomTabPageIndicatorAdapter(getChildFragmentManager());
            vpCategoryViewPager.setAdapter(fragmentPagerAdapter);
            piPageIndicatory.setViewPager(vpCategoryViewPager);
            piPageIndicatory.setOnPageChangeListener(pageChangeListener);
            vpCategoryViewPager.setOffscreenPageLimit(categoryViewCount);
        }
    }
    @Override
    public void dissmissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    @Override
    public void requestData() {
        if(isFirstLoading) {
            mPresenter.getBaseCategory();
        }else{
            mPresenter.getLocalBaseCategory();
        }
    }
    @Override
    public void showOnlineErrorLayout() {
        if (getActivity() != null) {
            inflateIfNeeded();
            ll_error.setVisibility(View.VISIBLE);
            RequestErrorHelper requestErrorHelper = new RequestErrorHelper(getContext(), ll_error);
            requestErrorHelper.showConnectionBreaks(BaseHttp.ERROR_TYPE_NET);
            requestErrorHelper.setResponseListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       requestData();
                }
            });
        }
    }
    @Override
    public void hideOnlineErrorLayout() {
        if (ll_error != null && ll_error.getVisibility() == View.VISIBLE) {
            ll_error.setVisibility(View.GONE);
        }
    }
    @Override
    public void setShoppingCartCount(int count) {
        mCommonCallback.updateRightIconNum(R.id.action_shopping_cart, count);
        MenuItem menuItem= mCommonCallback.getToolBar().getMenu().findItem(R.id.action_shopping_cart);
        TextView textView= (TextView) menuItem.getActionView().findViewById(R.id.ctv_home_shoppingcart_num);
        textView.setVisibility(View.VISIBLE);
        textView.setBackground(JImageUtils.getThemeCircle(getActivity()));
        textView.setText(mPresenter.formatShoppingCount(count));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setScrollToolBarEnable(true);
        mContainView = inflater.inflate(R.layout.fragment_home_home, null);
        mGATrackTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        setRetryTheme(mContainView);
        return mContainView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommonCallback.switchMenu(HomeCommonCallback.MENU_HOME);
        piPageIndicatory = (CustomTabCustomPageIndicator) mContainView.findViewById(R.id.ctpiCategoryList);
        piPageIndicatory.setIndicatorColorResource(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        vpCategoryViewPager = (CustomHomeViewPager) mContainView.findViewById(R.id.chvpContainer);
        rlHome = mContainView.findViewById(R.id.rl_home);
        TAG = this.getClass().getSimpleName();
        resetData();
        requestData();

    }
    public void resetData(){
//        everythingIndex = 0;
        categoryViewCount = 0;
        categoryArrayList = new ArrayList<>();
    }
    @Override
    public void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(getActivity());
        }
        mDialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mFragments.clear();
            fragmentPagerAdapter = null;
            vpCategoryViewPager.setAdapter(null);
            vpCategoryViewPager.removeAllViews();
        } catch (Exception ex) {
            ex.getStackTrace();
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
    public Fragment  createFragmentByIndex(int  index){
        Fragment fragment=null;
        if(fragmentType==TYPE_FRAGMENT_HORIZONTAL){
            fragment=HomeHomeFragmentV4.newInstance(index, categoryArrayList.get(index).getMenuId());
        }else if(fragmentType==TYPE_FRAGMENT_VERTICAL){
            fragment=HomeHomeFragmentV3.newInstance(index, categoryArrayList.get(index).getMenuId());
        }
        return fragment;
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
            if (categoryArrayList != null && position >= 0 && categoryArrayList.size() > position) {
                final int categoryArrayListSize = categoryArrayList.size();
                position = position % categoryArrayListSize;
                SVRAppserviceCatalogSearchCategoryItemReturnEntity category = categoryArrayList.get(position);
                if (category != null) {
                    categoryName = category.getMenuTitle();
                }
                gaScreen(position,categoryName);
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
            skipBrandPage(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void skipBrandPage(int index){
        if (categoryArrayList.size()>0 && index==categoryArrayList.size()-1){
            vpCategoryViewPager.setCurrentItem(0);
            String menuId = categoryArrayList.get(index).getMenuId();
            PageIntentUtils.skipToBrandListPage(getActivity(),menuId,categoryName);
        }
    }

    private void gaScreen(int position,String titleName) {
        String tempScreenName="";
        if (categoryArrayList!=null && position==categoryArrayList.size()){
                tempScreenName=titleName;
        }else {
            StringBuilder builder=new StringBuilder();
            builder.append("[");
            builder.append(titleName);
            builder.append("]");
            builder.append(Const.GA.HOME_SCREEN);
            tempScreenName=builder.toString();
        }
        GaTrackHelper.getInstance().googleAnalytics(tempScreenName,getActivity());
    }

}
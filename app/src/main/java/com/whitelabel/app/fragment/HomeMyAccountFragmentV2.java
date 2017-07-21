package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.ui.home.fragment.HomeAccountAddressBookFragmentV3;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.widget.MyAccountTopMenuView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class HomeMyAccountFragmentV2 extends HomeBaseFragment {
    private HomeActivity homeActivity;
    public static final String SWITCH_WISHLISTFRAGMENT = "from_checkout_to_wishlist";
    public static final String SWITCH_ORDERFRAGMENT = "from_checkout_to_orders";
    public static final String SWITCH_ADDRESSFRAGMENT = "from_checkout_to_address";
    private final static String TAG = "HomeMyAccountFragmentV2";
    public static final String TAG_WISHLIST = "wishlist";
    public static final String TAG_ORDERLIST = "orderlist";
    public static final String TAG_ADDRESSLIST = "addresslist";
    private Fragment mFragmentWishlist;
    private Fragment mFragmentAddresslist;
    private Fragment mFragmentOrderlist;
    public String mCurrTag;
    public boolean isShowGuide4 = true;
    private MyAccountTopMenuView ctpiCategoryList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home_accountv2, null);
        ctpiCategoryList = (MyAccountTopMenuView) contentView.findViewById(R.id.ctpiCategoryList);
        return contentView;
    }
    public static HomeMyAccountFragmentV2 newInstance(Serializable param1) {
        HomeMyAccountFragmentV2 fragment = new HomeMyAccountFragmentV2();
        Bundle args = new Bundle();
        if (param1 != null) {
            args.putSerializable("data", param1);
        }
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            initData();
            Bundle bundle = getArguments();
            String data = bundle.getString("data");
            JLogUtils.i(TAG, "data:" + data);
            startFragmentByType(data, false);
        } else {
            removeExpiredFragment();
        }
    }
    private void removeExpiredFragment() {
        try {
            Fragment wishFragment = getFragmentManager().findFragmentByTag(TAG_WISHLIST);
            if (wishFragment != null) {
                getChildFragmentManager().beginTransaction().remove(wishFragment).commitAllowingStateLoss();
            }
            Fragment orderFragment = getFragmentManager().findFragmentByTag(TAG_ORDERLIST);
            if (orderFragment != null) {
                getChildFragmentManager().beginTransaction().remove(orderFragment).commitAllowingStateLoss();
            }

            Fragment addressFragment = getFragmentManager().findFragmentByTag(TAG_ADDRESSLIST);
            if (addressFragment != null) {
                getChildFragmentManager().beginTransaction().remove(addressFragment).commitAllowingStateLoss();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    public void startFragmentByType(String type, boolean refresh) {
        if(!isAdded())return;
        //setCurrentPosition 会触发changePageListener，会调用两遍onActivityCreated,需要注意
        if(SWITCH_ORDERFRAGMENT.equalsIgnoreCase(type)){
            ctpiCategoryList.setCurrentPosition(1);
            switchChildFragment(TAG_ORDERLIST, refresh);
        }else if(SWITCH_ADDRESSFRAGMENT.equalsIgnoreCase(type)){
            ctpiCategoryList.setCurrentPosition(2);
        }else{
            ctpiCategoryList.setCurrentPosition(0);
            //默认为 wish item,所以setCurrentPosition 不会触发changePageListener，这里要手动调用
            switchChildFragment(TAG_WISHLIST, refresh);
        }
    }
    public void switchChildFragment(String tag, boolean refresh) {
        mCurrTag = tag;
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        Fragment currFragment = fragmentManager.findFragmentByTag(tag);
        if (currFragment == null) {
            switch (tag){
                case TAG_WISHLIST:
                    mFragmentWishlist = new HomeMyAccountWishlistFragment();
                    currFragment = mFragmentWishlist;
                    transaction.add(R.id.frame, mFragmentWishlist, tag);
                    break;
                case TAG_ORDERLIST:
                    mFragmentOrderlist = new HomeMyAccountOrdersFragment();
                    currFragment = mFragmentOrderlist;
                    transaction.add(R.id.frame, mFragmentOrderlist, tag);
                    break;
                case TAG_ADDRESSLIST:
                    mFragmentAddresslist =  HomeAccountAddressBookFragmentV3.newInstance(true);
                    currFragment = mFragmentAddresslist;
                    transaction.add(R.id.frame, mFragmentAddresslist, tag);
                    break;
            }
        } else {
            ((MyAccountFragmentRefresh) currFragment).refresh(refresh);
        }
        //如果是addressFragment,则手动判断是否需要guide
//        if (mCurrTag.equals(TAG_ADDRESSLIST) && getActivity() != null) {
//        } else
            if (!mCurrTag.equals(TAG_ADDRESSLIST)) {
            isShowGuide4 = true;
        }
        transaction.show(currFragment);
        transaction.commitAllowingStateLoss();
    }
    public void hideFragment(FragmentTransaction transaction) {
        if (mFragmentWishlist != null) {
            transaction.hide(mFragmentWishlist);
        }
        if (mFragmentOrderlist != null) {
            transaction.hide(mFragmentOrderlist);
        }
        if (mFragmentAddresslist != null) {
            transaction.hide(mFragmentAddresslist);
        }
    }
    private void initData() {
        List<String> myAccountMenuTitle = new ArrayList<>();
        myAccountMenuTitle.add(getResources().getString(R.string.home_myaccount_header_wishlist));
        myAccountMenuTitle.add(getResources().getString(R.string.home_myaccount_header_orders));
        myAccountMenuTitle.add(getResources().getString(R.string.home_myaccount_header_addressbook));
        ctpiCategoryList.setTitles(myAccountMenuTitle);
        ctpiCategoryList.setIndicatorColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        ctpiCategoryList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_WISHLIST);
                    switchChildFragment(TAG_WISHLIST, false);
                } else if (position == 1) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_ORDER);
                    switchChildFragment(TAG_ORDERLIST, false);
                } else if (position == 2) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_ADDRESS);
                    switchChildFragment(TAG_ADDRESSLIST, false);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        updateProfileUI();
    }
    private void updateProfileUI() {
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(homeActivity)) {
            String username = "";
            String firstname = WhiteLabelApplication.getAppConfiguration().getUser().getFirstName();
            String lastname = WhiteLabelApplication.getAppConfiguration().getUser().getLastName();
            if (!JDataUtils.isEmpty(firstname)) {
                username = username + firstname + " ";
            }
            username += lastname;
            mCommonCallback.setTitle(setFirstLetterToUpperCase(username));
        } else {
            homeActivity.switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HOME, null);
        }
    }
    private static String setFirstLetterToUpperCase(String title) {
        if (!TextUtils.isEmpty(title)) {
            title = title.toLowerCase();
            String[] split = title.trim().split(" ");
            StringBuilder newTitle = new StringBuilder();
            if (split.length != 0) {
                for (String cell : split) {
                    if (cell.trim().length() > 1) {
                        newTitle.append(cell.substring(0, 1).toUpperCase()).append(cell.substring(1)).append(" ");
                    } else {
                        newTitle.append(cell.toUpperCase()).append(" ");
                    }
                }
            }
            return newTitle.toString().trim();
        }
        return "";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1000) {
            FragmentManager fragmentManager = getChildFragmentManager();
            Fragment currFragment = fragmentManager.findFragmentByTag(mCurrTag);
            if (currFragment instanceof HomeAccountAddressBookFragmentV3) {
                ((HomeAccountAddressBookFragmentV3) currFragment).requestData();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


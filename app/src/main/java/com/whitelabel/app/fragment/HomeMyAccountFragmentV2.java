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
import com.whitelabel.app.application.GemfiveApplication;
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
    //    private ImageView ivHeaderBarEdit;
//    private RelativeLayout rlHeaderBarMenu;
//    private CustomTextView ctvHeaderBarTitle;
//    private View vFragmentIndicator;
    private HomeActivity homeActivity;
    //    private int screenWidth;
//    private int fromX = 0;
//    private int blue,black;
    public static final String SWITCH_ORDERFRAGMENT = "from_checkout_to_orders";
    public static final String SWITCH_ADDRESSFRAGMENT = "from_checkout_to_address";
    public static final String SWITCH_STORECREDITFRAGMENT = "storeCredit";
    private final static String TAG = "HomeMyAccountFragmentV2";
    public static final String TAG_WISHLIST = "wishlist";
    public static final String TAG_ORDERLIST = "orderlist";
    public static final String TAG_ADDRESSLIST = "addresslist";
    public static final String TAG_STORECREDIT = "storecredit";
    private Fragment mFragmentWishlist;
    private Fragment mFragmentAddresslist;
    private Fragment mFragmentOrderlist;
    private Fragment mFragmentStorecredit;
    public String mCurrTag;
    public boolean isShowGuide4 = true;
    private boolean switchFramentEnabled = true;
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
            Fragment orderFragment = getFragmentManager().findFragmentByTag(TAG_ORDERLIST);
            if (orderFragment != null) {
                getChildFragmentManager().beginTransaction().remove(orderFragment).commitAllowingStateLoss();
            }

            Fragment addressFragment = getFragmentManager().findFragmentByTag(TAG_ADDRESSLIST);
            if (addressFragment != null) {
                getChildFragmentManager().beginTransaction().remove(addressFragment).commitAllowingStateLoss();
            }
            Fragment creditFragment = getFragmentManager().findFragmentByTag(TAG_STORECREDIT);
            if (creditFragment != null) {
                getChildFragmentManager().beginTransaction().remove(creditFragment).commitAllowingStateLoss();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public void startFragmentByType(String type, boolean refresh) {
        if (!isAdded()) return;
        //setCurrentPosition 会触发changePageListener，会调用两遍onActivityCreated,需要注意
         if (SWITCH_ADDRESSFRAGMENT.equalsIgnoreCase(type)) {
            ctpiCategoryList.setCurrentPosition(1);
        } else if (SWITCH_STORECREDITFRAGMENT.equals(type)) {
            ctpiCategoryList.setCurrentPosition(2);
        } else {
            ctpiCategoryList.setCurrentPosition(0);
            //默认为 wish item,所以setCurrentPosition 不会触发changePageListener，这里要手动调用
            switchChildFragment(TAG_ORDERLIST, refresh);
        }
    }

    public void switchChildFragment(String tag, boolean refresh) {
        mCurrTag = tag;
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        Fragment currFragment = fragmentManager.findFragmentByTag(tag);
        if (currFragment == null) {
             if (tag.equals(TAG_ORDERLIST)) {
                mFragmentOrderlist = new HomeMyAccountOrdersFragment();
                currFragment = mFragmentOrderlist;
                transaction.add(R.id.frame, mFragmentOrderlist, tag);
            } else if (tag.equals(TAG_ADDRESSLIST)) {
                mFragmentAddresslist = new HomeMyAccountAddressBookFragment();
                currFragment = mFragmentAddresslist;
                transaction.add(R.id.frame, mFragmentAddresslist, tag);
            } else if (tag.equals(TAG_STORECREDIT)) {
                mFragmentStorecredit = HomeStoreCreditFragment.newInstance();
                currFragment = mFragmentStorecredit;
                transaction.add(R.id.frame, mFragmentStorecredit, tag);
            }
        } else {
            ((MyAccountFragmentRefresh) currFragment).refresh(refresh);
        }
        transaction.show(currFragment);
        transaction.commitAllowingStateLoss();

    }

    public void hideFragment(FragmentTransaction transaction) {
        if (mFragmentOrderlist != null) {
            transaction.hide(mFragmentOrderlist);
        }
        if (mFragmentAddresslist != null) {
            transaction.hide(mFragmentAddresslist);
        }
        if (mFragmentStorecredit != null) {
            transaction.hide(mFragmentStorecredit);
        }
    }


    public void setMenuEnable(boolean enable) {
        if (ctpiCategoryList != null && isAdded()) {
            ctpiCategoryList.setEnabled(enable);
        }

    }

    private void initData() {
//        fromX = 0;
        List<String> myAccountMenuTitle = new ArrayList<>();
        myAccountMenuTitle.add(getResources().getString(R.string.home_myaccount_header_orders));
        myAccountMenuTitle.add(getResources().getString(R.string.home_myaccount_header_addressbook));
        myAccountMenuTitle.add(getResources().getString(R.string.home_account_store_credit));
        ctpiCategoryList.setTitles(myAccountMenuTitle);
        ctpiCategoryList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_ORDER);
                    switchChildFragment(TAG_ORDERLIST, false);
                } else if (position == 1) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_ADDRESS);
                    switchChildFragment(TAG_ADDRESSLIST, false);
                } else if (position == 2) {
                    homeActivity.switchMenu(HomeCommonCallback.MENU_STORECREDITS);
                    switchChildFragment(TAG_STORECREDIT, false);
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
        if (GemfiveApplication.getAppConfiguration().isSignIn(homeActivity)) {
            String username = "";
            String firstname = GemfiveApplication.getAppConfiguration().getUser().getFirstName();
            String lastname = GemfiveApplication.getAppConfiguration().getUser().getLastName();
            if (!JDataUtils.isEmpty(firstname)) {
                username = username + firstname + " ";
            }
            username += lastname;
//            username = username.toUpperCase();
            mCommonCallback.setTitle(setFirstLetterToUpperCase(username));
        } else {
            homeActivity.switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HOME, null);
        }
    }

    private static String setFirstLetterToUpperCase(String title) {
        if (!TextUtils.isEmpty(title)) {
            title = title.toLowerCase();

            String[] split = title.trim().split(" ");
            String newTitle = "";
            if (split.length != 0) {
                for (String cell : split) {
                    if (cell.trim().length() > 1) {
                        newTitle += cell.substring(0, 1).toUpperCase() + cell.substring(1) + " ";
                    } else {
                        newTitle += cell.toUpperCase() + " ";
                    }
                }
            }
            return newTitle.trim();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1000) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment currFragment = fragmentManager.findFragmentByTag(mCurrTag);
            if (currFragment instanceof HomeMyAccountAddressBookFragment) {
                ((HomeMyAccountAddressBookFragment) currFragment).refresh(true);
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}


package com.whitelabel.app.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.*;
import com.whitelabel.app.adapter.LeftMenuDogsAndCatsAdapter;
import com.whitelabel.app.callback.NotificationCallback;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchCategoryItemReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.ui.BasePresenter;
import com.whitelabel.app.utils.BadgeUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.LanguageUtils;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomCoordinatorLayout;
import com.whitelabel.app.widget.CustomTextView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/**
 * Created by Administrator on 2016/10/3.
 */
public abstract class DrawerLayoutActivity<T extends BasePresenter> extends com.whitelabel.app.BaseActivity<T> implements View.OnClickListener {
    private CustomCoordinatorLayout rootLayout;
    private AppBarLayout appbar_layout;
    private FrameLayout flContainer;
    private DrawerLayout drawerLayout;
    private ImageView ivHome;
    private ImageView ivCategoryTree;
    private ImageView ivShoppingCart;
    private ImageView ivNotification;
    private ImageView ivMyWishList;
    private ImageView ivMyOrder;
    private ImageView ivAddress;
    private ImageView ivStoreCredit;
    private ImageView ivCustomerCare;
    private ImageView ivSetting;
    private ImageView ivLanguage;
    private TextView tvUserName, tvHome, tvCategoryTree, tvShoppingCart, tvNotification, tvWistlist,
            tvMyOrder, tvSetting, tvCustomerService, tvHelpCenter, tvOrderNum, tvMyOrderNum,
            tvShipping, tvShoppingNum, tvNotificationNum, tvWistNum,tvLanguage,
            tvAddress, tvStoreCredit, tvSwitchEnglish, tvSwitchChinese;
    protected TextView tvDogs,tvCats;
    protected RelativeLayout rlDogs,rlCats;
    private Handler baseHandler = new Handler();
    private RelativeLayout rlDrawerOrder;
    private RelativeLayout rlDrawerAddress;
    private RelativeLayout rlDrawerSotreCredit;
    private RelativeLayout rlDrawerHome ;
    private RelativeLayout rlDrawerCategoryTree;
    private RelativeLayout rlDrawerShoppingCart;
    private RelativeLayout rlDrawerNotification;
    private RelativeLayout rlDrawerWish ;
    private RelativeLayout rlCustomerCare ;
    private RelativeLayout rlSetting ;
    private RelativeLayout rlLanguage;
    private NotificationReceiver receiver;
    private RecyclerView rvDogsAndCatsList;
    LeftMenuDogsAndCatsAdapter leftMenuDogsAndCatsAdapter;
    private String lv0Title="Dogs";
    protected static boolean isHomePage=true;
    protected abstract boolean refreshNotification(int type, String id);
    protected abstract void jumpHomePage();
    public abstract void jumpHomePage(Serializable serializable);
    protected abstract void jumpCategoryTreePage();
    protected abstract void jumpShoppingCartPage();
    protected abstract void jumpNotificationPage();
    protected abstract void jumpWistListPage();
    protected abstract void jumpOrderPage();
    protected abstract void jumpSettingPage();
    protected abstract void jumpEditProfilePage();
    protected abstract void jumpCustomerServicePage();
    protected abstract void jumpHelpCenterServicePage();
    protected abstract void jumpShippingServicePage();
    protected abstract void jumpAddressPage();
    protected abstract void jumpHomePageAndRecreate();
    protected abstract void jumpStoreCreditPage();

    private void setAppBarLayoutBehaviour() {
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior() {
            @Override
            public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
                // Trigger the following events if it is a vertical scrolling
                return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
            }
            @Override
            public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
                super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                // If I slowly reach the top, without fling, show the RecyclerView
                int[] firstVisiblePositions = ((StaggeredGridLayoutManager) ((RecyclerView) target).getLayoutManager()).findFirstCompletelyVisibleItemPositions(null);
                for (int position : firstVisiblePositions) {
                    if (position == 0) {
                        break;
                    }
                }
            }
            @Override
            public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
                if (velocityY > 500) {
                    // Hide the RecyclerView
                } else if (velocityY < -500) {
                    // Show the recyclerView
                }
                return true;
            }
        };
        ((CoordinatorLayout.LayoutParams) appbar_layout.getLayoutParams()).setBehavior(behavior);
    }
    @Override
    public void onClick(View v) {
        int DELAY = 300;
        switch (v.getId()) {
            case R.id.rl_drawer_home:
                drawerLayout.closeDrawer(Gravity.LEFT);
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HOME);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpHomePage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_categorytree:
                drawerLayout.closeDrawer(Gravity.LEFT);
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_CATEGORYTREE);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpCategoryTreePage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_shoppingcart:
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpShoppingCartPage();
                    }
                }, DELAY);

                break;
            case R.id.rl_drawer_notification:
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this)) {
                    switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_NOTIFICATION);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpNotificationPage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_wishlist:
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this)) {
                    switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_WISHLIST);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpWistListPage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_order:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_ORDER);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this))
                            jumpOrderPage();
                    }
                }, DELAY);
                break;
            case R.id.rl_setting:
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this)) {
                    switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_SETTING);
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpSettingPage();
                    }
                }, DELAY);
                break;
            case R.id.rl_customer_care:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_COSTOMSERVICE);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpCustomerServicePage();
                    }
                }, DELAY);
                break;
            case R.id.tv_switch_en:
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeLanguage(LanguageUtils.LanguageType.LANGUAGE_ENGLISH);
                        jumpHomePageAndRecreate();
                    }
                }, DELAY);
                break;
            case R.id.tv_switch_cn:
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeLanguage(LanguageUtils.LanguageType.LANGUAGE_TRADITIONAL_CHINESE);
                        jumpHomePageAndRecreate();
                    }
                }, DELAY);
                break;
            case R.id.tv_help_center:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HELPCENTER);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpHelpCenterServicePage();
                    }
                }, DELAY);
                break;
            case R.id.tv_shipping:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_SHIPPING);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpShippingServicePage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_address:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_ADDRESS);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this))
                            jumpAddressPage();
                    }
                }, DELAY);
                break;
            case R.id.rl_drawer_store_credit:
                break;
            case R.id.ll_profile:
                if (WhiteLabelApplication.getAppConfiguration().isSignIn(DrawerLayoutActivity.this))
                    switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HOME);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpEditProfilePage();
                    }
                }, DELAY);
                break;
            case R.id.rl_dogs:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_DOGS);
                leftMenuDogsAndCatsAdapter.setNewData(JStorageUtils.getLeftTreeData(0));
                break;
            case R.id.rl_cats:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_CATS);
                leftMenuDogsAndCatsAdapter.setNewData(JStorageUtils.getLeftTreeData(1));
                break;
        }
    }
    private void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivHome = (ImageView) findViewById(R.id.iv_home);
        ivCategoryTree = (ImageView) findViewById(R.id.iv_category_tree);
        ivShoppingCart = (ImageView) findViewById(R.id.iv_shopping_cart);
        ivNotification = (ImageView) findViewById(R.id.iv_notification);
        ivMyWishList = (ImageView) findViewById(R.id.iv_wishlist);
        ivMyOrder = (ImageView) findViewById(R.id.iv_orderlist);
        ivCustomerCare=(ImageView) findViewById(R.id.iv_customer_care);
        ivSetting=(ImageView) findViewById(R.id.iv_setting);
        ivLanguage = (ImageView) findViewById(R.id.iv_language);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        View llProfile = findViewById(R.id.ll_profile);
        llProfile.setBackgroundColor(WhiteLabelApplication.getAppConfiguration().
                getThemeConfig().getSide_menu_background_default_color());
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvCategoryTree = (TextView) findViewById(R.id.tv_category_tree);
        tvShoppingCart = (TextView) findViewById(R.id.tv_shopping_cart);
        tvNotification = (TextView) findViewById(R.id.tv_notification);
        tvWistlist = (TextView) findViewById(R.id.tv_wishlist);
        tvMyOrder = (TextView) findViewById(R.id.tv_order);
        tvSetting = (TextView) findViewById(R.id.tv_setting);
        tvLanguage = (TextView) findViewById(R.id.tv_language);
        tvCustomerService = (TextView) findViewById(R.id.tv_customer_service);
        tvHelpCenter = (TextView) findViewById(R.id.tv_help_center);
        tvShipping = (TextView) findViewById(R.id.tv_shipping);
        tvShoppingNum = (TextView) findViewById(R.id.tv_shoppingCart_num);
        tvSwitchEnglish = (TextView) findViewById(R.id.tv_switch_en);
        tvSwitchChinese = (TextView) findViewById(R.id.tv_switch_cn);

        tvNotificationNum = (TextView) findViewById(R.id.tv_notification_num);
        tvWistNum = (TextView) findViewById(R.id.tv_wishlist_num);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvStoreCredit = (TextView) findViewById(R.id.tv_store_credit);
        ivAddress = (ImageView) findViewById(R.id.iv_address);
        ivStoreCredit = (ImageView) findViewById(R.id.iv_store_credit);
        rlDrawerHome = (RelativeLayout) findViewById(R.id.rl_drawer_home);
        rlDrawerCategoryTree = (RelativeLayout) findViewById(R.id.rl_drawer_categorytree);
        rlDrawerShoppingCart = (RelativeLayout) findViewById(R.id.rl_drawer_shoppingcart);
        rlDrawerNotification = (RelativeLayout) findViewById(R.id.rl_drawer_notification);
        rlDrawerWish = (RelativeLayout) findViewById(R.id.rl_drawer_wishlist);
        rlDrawerOrder = (RelativeLayout) findViewById(R.id.rl_drawer_order);
        rlDrawerAddress = (RelativeLayout) findViewById(R.id.rl_drawer_address);
        rlDrawerSotreCredit = (RelativeLayout) findViewById(R.id.rl_drawer_store_credit);
        rlCustomerCare= (RelativeLayout) findViewById(R.id.rl_customer_care);
        rlSetting = (RelativeLayout) findViewById(R.id.rl_setting);
        rlLanguage = (RelativeLayout) findViewById(R.id.rl_language);

        rlDogs=(RelativeLayout)findViewById(R.id.rl_dogs);
        rlCats=(RelativeLayout)findViewById(R.id.rl_cats);
        tvDogs = (CustomTextView) findViewById(R.id.tv_dogs);
        tvCats = (CustomTextView) findViewById(R.id.tv_cats);
        rvDogsAndCatsList = (RecyclerView) findViewById(R.id.rv_dogs_and_cats_list);

        JViewUtils.setSlideMenuTextStyle(tvHome,false,true);
        JViewUtils.setSlideMenuTextStyle(tvShoppingCart,false,true);
        JViewUtils.setSlideMenuTextStyle(tvNotification,false,true);
        JViewUtils.setSlideMenuTextStyle(tvWistlist,false,true);
        JViewUtils.setSlideMenuTextStyle(tvMyOrder,false,true);
        JViewUtils.setSlideMenuTextStyle(tvAddress,false,true);
        JViewUtils.setSlideMenuTextStyle(tvCategoryTree,false,true);
        JViewUtils.setSlideMenuTextStyle(tvDogs,false,false);
        JViewUtils.setSlideMenuTextStyle(tvCats,false,false);
        JViewUtils.setSlideMenuTextStyle(tvSetting,false,true);
        JViewUtils.setSlideMenuTextStyle(tvCustomerService,false,true);
        JViewUtils.setSlideMenuTextStyle(tvHelpCenter,false,true);
        JViewUtils.setSlideMenuTextStyle(tvShipping,false,true);
        JViewUtils.setSlideMenuTextStyle(tvLanguage,false,true);
        JViewUtils.setSlideMenuTextStyle(tvSwitchEnglish,false,true);
        JViewUtils.setSlideMenuTextStyle(tvSwitchChinese,false,true);

        int iconDefaultColor=WhiteLabelApplication.getAppConfiguration().getThemeConfig().getNavigation_bar_background_color();
        ivHome.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_home_normal),iconDefaultColor));
        ivShoppingCart.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_shopping_cart_normal),iconDefaultColor));
        ivNotification.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_notification_default),iconDefaultColor));
        ivMyWishList.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_my_wishlist_normal),iconDefaultColor));
        ivMyOrder.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_my_orders_normal),iconDefaultColor));
        ivAddress.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_address_normal),iconDefaultColor));
        ivCategoryTree.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_categorytree_default),iconDefaultColor));
        ivCustomerCare.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_customer_care_normal),iconDefaultColor));
        ivSetting.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_settings_normal),iconDefaultColor));
        ivLanguage.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.ic_slide_settings_normal),iconDefaultColor));

        JViewUtils.setSlideMenuBgStyle(rlDrawerHome);
        JViewUtils.setSlideMenuBgStyle(rlDrawerCategoryTree);
        JViewUtils.setSlideMenuBgStyle(rlDrawerShoppingCart);
        JViewUtils.setSlideMenuBgStyle(rlDrawerNotification);
        JViewUtils.setSlideMenuBgStyle(rlDrawerWish);
        JViewUtils.setSlideMenuBgStyle(rlDrawerOrder);
        JViewUtils.setSlideMenuBgStyle(rlDrawerAddress);
        JViewUtils.setSlideMenuBgStyle(rlDrawerSotreCredit);
        JViewUtils.setSlideMenuBgStyle(rlCustomerCare);
        JViewUtils.setSlideMenuBgStyle(rlSetting);
        JViewUtils.setSlideMenuBgStyle(rlLanguage);
        JViewUtils.setSlideMenuBgStyle(tvHelpCenter);
        JViewUtils.setSlideMenuBgStyle(tvShipping);
        JViewUtils.setSlideMenuBgStyle(tvSwitchEnglish);
        JViewUtils.setSlideMenuBgStyle(tvSwitchChinese);

        llProfile.setOnClickListener(this);
        rlDrawerCategoryTree.setOnClickListener(this);
        rlDrawerHome.setOnClickListener(this);
        rlDrawerShoppingCart.setOnClickListener(this);
        rlDrawerNotification.setOnClickListener(this);
        rlDrawerWish.setOnClickListener(this);
        rlDrawerOrder.setOnClickListener(this);
        rlDrawerAddress.setOnClickListener(this);
        rlDrawerSotreCredit.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlCustomerCare.setOnClickListener(this);
        tvHelpCenter.setOnClickListener(this);
        tvShipping.setOnClickListener(this);
        tvSwitchChinese.setOnClickListener(this);
        tvSwitchEnglish.setOnClickListener(this);
        rlDogs.setOnClickListener(this);
        rlCats.setOnClickListener(this);
        tvShoppingNum.setBackground(JImageUtils.getThemeCircle(this));
        tvNotificationNum.setBackground(JImageUtils.getThemeCircle(this));
        tvWistNum.setBackground(JImageUtils.getThemeCircle(this));

        LanguageUtils.LanguageType currentLanguageType = LanguageUtils.getCurrentLanguage();
        if(currentLanguageType == LanguageUtils.LanguageType.LANGUAGE_ENGLISH){
            tvSwitchEnglish.getPaint().setFakeBoldText(true);
        } else {
            tvSwitchChinese.getPaint().setFakeBoldText(true);
        }
    }

    public void setNotificationCount(int  count){
        if (count > 0) {
            if (count > 99) {
             tvNotificationNum.setText("99+");
            } else {
              tvNotificationNum.setText(String.valueOf(count));
            }
            tvNotificationNum.setVisibility(View.VISIBLE);
        } else {
           tvNotificationNum.setVisibility(View.INVISIBLE);
        }
    }
    ActionBarDrawerToggle mActionDrawableToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_base);
        receiver = new NotificationReceiver(callback);
        IntentFilter intentFilter = new IntentFilter(NotificationReceiver.ACTION);
        registerReceiver(receiver, intentFilter);
        initLayout();
        initLeftAdapter();
//        setAppBarLayoutBehaviour();
        mActionDrawableToggle = new ActionBarDrawerToggle(this, getDrawerLayout(), getToolbar(), R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                requestNotificationUnReadCount();
            }
        };
        getDrawerLayout().addDrawerListener(mActionDrawableToggle);
        mActionDrawableToggle.syncState();
    }

    //init left menu dogs and cats adapter
    private void initLeftAdapter() {
        switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_DOGS);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DrawerLayoutActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDogsAndCatsList.setLayoutManager(mLayoutManager);

        // Disabled nested scrolling since Parent scrollview will scroll the content.
        rvDogsAndCatsList.setNestedScrollingEnabled(false);

        leftMenuDogsAndCatsAdapter = new LeftMenuDogsAndCatsAdapter(JStorageUtils.getLeftTreeData(0), new LeftMenuDogsAndCatsAdapter.ITreeClick() {
            @Override
            public void onChildClick(CategoryBaseBean.CategoryBean.ChildrenBeanX parentBean,CategoryBaseBean.CategoryBean.ChildrenBeanX.ChildrenBean childrenBean,String lv1Title) {
//                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intent = new Intent(DrawerLayoutActivity.this, ProductListActivity.class);
                intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY);
                intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
                // Get Parent data
                if (parentBean.getId() != null ) {
                    intent.putExtra(ProductListActivity.INTENT_DATA_CATEGORYID, parentBean);
                    intent.putExtra(ProductListActivity.INTENT_DATA_LEFT_TOP_TITLE, lv0Title);
                }
                intent.putExtra(ProductListActivity.INTENT_CATEGORY_ID, childrenBean.getId());
                startActivity(intent);
            }
        });
        rvDogsAndCatsList.setAdapter(leftMenuDogsAndCatsAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
    private NotificationCallback callback = new NotificationCallback() {
        @Override
        public void refreshNotification(int type, String id) {
            DrawerLayoutActivity.this.refreshNotification(type, id);
            requestNotificationUnReadCount();

        }
    };
    public abstract void requestNotificationUnReadCount();
    public void resetSate() {
        tvHome.setSelected(false);
        tvCategoryTree.setSelected(false);
        tvShoppingCart.setSelected(false);
        tvNotification.setSelected(false);
        tvWistlist.setSelected(false);
        tvMyOrder.setSelected(false);
        tvAddress.setSelected(false);
        tvStoreCredit.setSelected(false);

        ivHome.setSelected(false);
        ivCategoryTree.setSelected(false);
        ivShoppingCart.setSelected(false);
        ivNotification.setSelected(false);
        ivMyWishList.setSelected(false);
        ivMyOrder.setSelected(false);
        ivAddress.setSelected(false);
        ivStoreCredit.setSelected(false);
        tvHelpCenter.setSelected(false);
        tvShipping.setSelected(false);
        tvSetting.setSelected(false);
        tvCustomerService.setSelected(false);

        rlDrawerHome.setSelected(false);
        rlDrawerCategoryTree.setSelected(false);
        rlDrawerShoppingCart.setSelected(false);
        rlDrawerNotification.setSelected(false);
        rlDrawerWish.setSelected(false);
        rlDrawerOrder.setSelected(false);
        rlDrawerAddress.setSelected(false);
        rlDrawerSotreCredit.setSelected(false);
        rlCustomerCare.setSelected(false);
        rlSetting.setSelected(false);
    }

    public void switchMenu(int type) {
        if (type == HomeBaseFragment.HomeCommonCallback.MENU_DOGS || type == HomeBaseFragment.HomeCommonCallback.MENU_CATS){
            tvCats.setSelected(false);
            tvDogs.setSelected(false);
        }else {
            resetSate();
        }

        if (type == HomeBaseFragment.HomeCommonCallback.MENU_HOME) {
            ivHome.setSelected(true);
            tvHome.setSelected(true);
            rlDrawerHome.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_CATEGORYTREE) {
            ivCategoryTree.setSelected(true);
            tvCategoryTree.setSelected(true);
            rlDrawerCategoryTree.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SHOPPINGCART) {
            ivShoppingCart.setSelected(true);
            tvShoppingCart.setSelected(true);
            rlDrawerShoppingCart.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_NOTIFICATION) {
            ivNotification.setSelected(true);
            tvNotification.setSelected(true);
            rlDrawerNotification.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_WISHLIST) {
            ivMyWishList.setSelected(true);
            tvWistlist.setSelected(true);
            rlDrawerWish.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_ORDER) {
            ivMyOrder.setSelected(true);
            tvMyOrder.setSelected(true);
            rlDrawerOrder.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_ADDRESS) {
            ivAddress.setSelected(true);
            tvAddress.setSelected(true);
            rlDrawerAddress.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_STORECREDITS) {
            ivStoreCredit.setSelected(true);
            tvStoreCredit.setSelected(true);
            rlDrawerSotreCredit.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SETTING) {
            ivSetting.setSelected(true);
            tvSetting.setSelected(true);
            rlSetting.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_COSTOMSERVICE) {
            tvCustomerService.setSelected(true);
            ivCustomerCare.setSelected(true);
            rlCustomerCare.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_HELPCENTER) {
            tvHelpCenter.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SHIPPING) {
            tvShipping.setSelected(true);
        }else if (type == HomeBaseFragment.HomeCommonCallback.MENU_DOGS){
            tvDogs.setSelected(true);
            lv0Title=getResources().getString(R.string.navigation_dogs);
        }else if (type == HomeBaseFragment.HomeCommonCallback.MENU_CATS){
            lv0Title=getResources().getString(R.string.navigation_cats);
            tvCats.setSelected(true);
        }
    }
    public void updateLeftMenuNumber() {
        try {
            long cartItemCount = 0;
            //get local  product num
            ArrayList<TMPLocalCartRepositoryProductEntity> productList = JStorageUtils.getProductListFromLocalCartRepository(this);
            if (productList != null && productList.size() > 0) {
                for (TMPLocalCartRepositoryProductEntity localCartRepositoryProductEntity : productList) {
                    cartItemCount += localCartRepositoryProductEntity.getSelectedQty();
                }
            }
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {
                rlDrawerOrder.setVisibility(View.VISIBLE);
                rlDrawerAddress.setVisibility(View.VISIBLE);
                // TODO(Aaron):Temporary use,next version use rlDrawerNotification.setVisibility(View.VISIBLE)
                rlDrawerNotification.setVisibility(View.GONE);
//                rlDrawerSotreCredit.setVisibility(View.VISIBLE);
                String username = "";
                if (!TextUtils.isEmpty(WhiteLabelApplication.getAppConfiguration().getUser().getFirstName())) {
                    username += WhiteLabelApplication.getAppConfiguration().getUser().getFirstName() + " ";
                }
                username += WhiteLabelApplication.getAppConfiguration().getUser().getLastName();
                tvUserName.setText(username);
                 //ivUserImg.setVisibility(View.VISIBLE);
                //update  wishlist  orderlist  cart list  count
                long wishtlistNum = WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getWishListItemCount();
                long orderNum = WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getOrderCount();
                cartItemCount += WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getCartItemCount();
                if (wishtlistNum > 0 && wishtlistNum <= 99) {
                    tvWistNum.setVisibility(View.VISIBLE);
                    tvWistNum.setText(wishtlistNum + "");
                } else if (wishtlistNum > 99) {
                    tvWistNum.setVisibility(View.VISIBLE);
                    tvWistNum.setText("99+");
                } else {
                    tvWistNum.setVisibility(View.GONE);
                }
            } else {
//            ivUserImg.setVisibility(View.GONE);
                tvWistNum.setVisibility(View.GONE);
                tvUserName.setText(getString(R.string.sign_register));
                rlDrawerOrder.setVisibility(View.GONE);
                rlDrawerAddress.setVisibility(View.GONE);
                rlDrawerSotreCredit.setVisibility(View.GONE);
                rlDrawerNotification.setVisibility(View.GONE); //(Aaron)
            }

            if (cartItemCount > 0 && cartItemCount <= 99) {
                tvShoppingNum.setVisibility(View.VISIBLE);
                tvShoppingNum.setText(cartItemCount + "");
            } else if (cartItemCount > 99) {
                tvShoppingNum.setVisibility(View.VISIBLE);
                tvShoppingNum.setText("99+");
            } else {
                tvShoppingNum.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLeftMenuNumber();
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (CustomCoordinatorLayout) findViewById(R.id.root_layout);
        appbar_layout = (AppBarLayout) findViewById(R.id.appbar_layout);
        flContainer = (FrameLayout) findViewById(R.id.flContainer);
        flContainer.addView(view);
    }

    //因toolBar滑动原因,需要添加paddingBottom,主页除外
    public void setFragmentPaddingBottom(boolean hasPaddingBottom) {
        if (isFinishing()) return;
        if (hasPaddingBottom) {
            //TODO joyson  shield sroll bottom padding
//            flContainer.setPadding(flContainer.getPaddingLeft(), flContainer.getPaddingTop(), flContainer.getPaddingRight(), getToolbar().getHeight());
            flContainer.setPadding(flContainer.getPaddingLeft(), flContainer.getPaddingTop(), flContainer.getPaddingRight(), 0);
        } else {
            flContainer.setPadding(flContainer.getPaddingLeft(), flContainer.getPaddingTop(), flContainer.getPaddingRight(), 0);
        }
    }
    //
    public void setCoordinatorLayoutSwitch(boolean switchScroll) {
        if (switchScroll == false && appbar_layout != null) {
            appbar_layout.setExpanded(true, false);
        }
        if (rootLayout != null) {
            rootLayout.setSwitchScroll(switchScroll);
        }
    }

    private void changeLanguage(LanguageUtils.LanguageType languageType){
        LanguageUtils.changeLanguage(languageType);
    }
}

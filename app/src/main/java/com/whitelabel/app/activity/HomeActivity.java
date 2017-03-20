package com.whitelabel.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.facebook.appevents.AppEventsLogger;
import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.fragment.HomeCategoryTreeFragment;
import com.whitelabel.app.fragment.HomeHelpCenterDetailFragment;
import com.whitelabel.app.fragment.HomeHelpCenterListFragment;
import com.whitelabel.app.fragment.HomeHomeFragment;
import com.whitelabel.app.fragment.HomeMyAccountFragmentV2;
import com.whitelabel.app.fragment.HomeNotificationDetailFragment;
import com.whitelabel.app.fragment.HomeNotificationListFragment;
import com.whitelabel.app.fragment.HomeSelectCrditCardFragment;
import com.whitelabel.app.fragment.HomeSettingCotentFragment;
import com.whitelabel.app.fragment.ShoppingCartBaseFragment;
import com.whitelabel.app.fragment.ShoppingCartVerticalFragment;
import com.whitelabel.app.listener.OnNotificationCountListener;
import com.whitelabel.app.model.MarketingLayersEntity;
import com.whitelabel.app.model.TMPHelpCenterListToDetailEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.UserGuideHelper;
import com.whitelabel.app.widget.CustomTextView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HomeActivity extends DrawerLayoutActivity implements HomeBaseFragment.HomeCommonCallback {
    public static final int FRAGMENT_TYPE_HOME_HOME = 0;    //Home;
    public static final int FRAGMENT_TYPE_HOME_MYACCOUNT = 1;   //Sign in;
    public static final int FRAGMENT_TYPE_HOME_HELPCENTERLIST = 3;  //Help Centre;
    public static final int FRAGMENT_TYPE_HOME_CATEGORY = 9;
    public static final int FRAGMENT_TYPE_HOME_HELPCENTERDETAIL = 4;    //?
    public static final int FRAGMENT_TYPE_HOME_NOTIFICATIONLIST = 5;    //Notification
    public static final int FRAGMENT_TYPE_HOME_NOTIFICATIONDETAIL = 6;  //?
    public static final int FRAGMENT_TYPE_HOME_SETTING = 7;//Setting
    public static final int FRAGMENT_TYPE_HOME_SHOPPINGCART = 8;//Setting
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_CATEGORYTREE = "toCategoryTreeFragment";
    public static final int FRAGMENT_TYPE_HOME_GOBACK = 101;
    //    public static final int FRAGMENT_TYPE_HOME_STORECREDITS=9;
    public static final int FRAGMENT_TYPE_HOME_CREDITCARD = 10;
    private final int TYPE_FRAGMENT_SWITCH_NONE = 0;
    private final int TYPE_FRAGMENT_SWITCH_RIGHT2LEFT = 1;
    private final int TYPE_FRAGMENT_SWITCH_LEFT2RIGHT = -1;
    private final int TYPE_FRAGMENT_SWITCH = -2;
    //跳转到哪个Fragment上
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_EXITAPP = "exitApp";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_WISHLIST = "from_checkout_to_wishlist";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_ORDER = "from_checkout_to_orders";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_ADDRESS = "from_checkout_to_address";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTER = "toHelpCenterFragmet";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERCUSTOMSERVICE = "toHelpCenterFragmetCustomServiceFragment";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERSHIPPINGDELIVERY = "toHelpCenterFragmetShippingDeliveryFragment";
    public final static String EXTRA_REDIRECTTO_TYPE = "redirectto_type";
    public final static String EXTRA_REDIRECTTO_TYPE_VALUE_START = "toStartActivity";
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_STORECREDIT = "toStoreCreditFragment";
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_NOTIFICATION = "toNotificationFragmet";
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART = "toShoppingCartFragment";
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_SETTING = "toSettingFragment";
    public static final String EXTRA_REDIRECTTO_TYPE_VALUE_EDITPROFILE = "toEditProfileActivity";
    private View mUserGuideAbove;
    private ArrayList<Fragment> mAttachedFragmentList;
    private OnNotificationCountListener onNotificationCountListener;
    protected Fragment mCurrentFragment;
    private Dialog mDialog;
    public Handler mHandler = new Handler();
    public int fragmentType;
    private CustomTextView tvMarketingLayers, tvMarketingLayersDesc;
    private PopupWindow mUserGuidePopWindow;
    public boolean showMarketLayers = true;
    private boolean marketLayerClosed;
//    public static String BUNDLE_TO_START="toStart";
    private final String DEEPLINK_TYPE_PRODUCTDETAIL = "product";
    private final String DEEPLINK_TYPE_LANDINGPAGE = "landingpage";
    //跳转到哪个Fragment上
    public static final String BUNDLE_START_FRAGMENT = "startFragment";
    public static final String BUNDLE_FRAGMENT_STORECREDIT = "storeCredit";
    private ImageView ivMarketLayer, ivMarketLayerClose;
    private RelativeLayout rlMarketLayer;
    //    private View vUserGuide;
    private UserGuideHelper mUserGuideHelper;
    private ImageLoader mImageLoader;


    @Override
    protected void onStart() {
        super.onStart();
        try {
            AppEventsLogger.activateApp(this);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AppEventsLogger.deactivateApp(this);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public Toolbar getToolBar() {
        return getToolbar();
    }

    @Override
    public void showUserGuide(HomeBaseFragment.UserGuideType userGuideType) {
        if (mUserGuidePopWindow != null && mUserGuidePopWindow.isShowing()) {
            return;
        }
        if (userGuideType == HomeBaseFragment.UserGuideType.LEFTMENU) {
            getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mUserGuidePopWindow = mUserGuideHelper.showLeftMenuUserGuide(mUserGuideAbove);
            mUserGuidePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    JStorageUtils.notShowGuide3(HomeActivity.this);
                    showMarketLayers = true;
                    getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        } else if (userGuideType == HomeBaseFragment.UserGuideType.HOMELEFTICON) {
            if (!getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
                getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mUserGuidePopWindow = mUserGuideHelper.showHomeLeftIconUserGuide(mUserGuideAbove);
                mUserGuidePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        JStorageUtils.notShowGuide1(HomeActivity.this);
                        showMarketLayers = true;
                        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                });
            }
        } else if (userGuideType == HomeBaseFragment.UserGuideType.HOMESECONDPAGE) {
            if (!getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
                getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mUserGuidePopWindow = mUserGuideHelper.showHomeSecondUserGuide(mUserGuideAbove);
                mUserGuidePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        JStorageUtils.notShowGuide2(HomeActivity.this);
                        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                });
            }
        }
//        else if (userGuideType == HomeBaseFragment.UserGuideType.MYACCOUNTEDIT) {
//            if (!getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
//                getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                mUserGuidePopWindow = mUserGuideHelper.showMyAccountEditUserGuide(mUserGuideAbove);
//                mUserGuidePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        JStorageUtils.notShowGuide4(HomeActivity.this);
//                        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                    }
//                });
//            }
//        }
        else if (userGuideType == HomeBaseFragment.UserGuideType.ADDRESS) {
            if(!getDrawerLayout().isDrawerOpen(Gravity.LEFT)){
                mUserGuidePopWindow=mUserGuideHelper.showMyAccountAddressUserGuide(mUserGuideAbove);
                getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mUserGuidePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        JStorageUtils.notShowGuide5(HomeActivity.this);
                        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                });
            }
        }
    }

    @Override
    public void jumpHomePage(Serializable serializable) {
        if (!(mCurrentFragment instanceof HomeHomeFragment)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HOME, serializable);
        }
    }

    @Override
    protected void jumpHomePage() {
        jumpHomePage(null);
    }

    @Override
    protected void jumpCategoryTreePage() {
        if (!(mCurrentFragment instanceof HomeCategoryTreeFragment)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_CATEGORY, null);
        }
    }

    @Override
    protected void jumpShoppingCartPage() {
        if (!(mCurrentFragment instanceof ShoppingCartVerticalFragment)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_SHOPPINGCART, null);
        }
    }

    @Override
    protected void jumpAddressPage() {
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_MYACCOUNT, HomeMyAccountFragmentV2.SWITCH_ADDRESSFRAGMENT);
        } else {
            jumpLoginActivity();
        }
    }

    @Override
    protected void jumpStoreCreditPage() {
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_MYACCOUNT, HomeMyAccountFragmentV2.SWITCH_STORECREDITFRAGMENT);
        } else {
            jumpLoginActivity();
        }
    }

    @Override
    protected void jumpNotificationPage() {
        switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONLIST, null);
    }

    @Override
    protected void jumpWistListPage() {
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_MYACCOUNT, HomeMyAccountFragmentV2.SWITCH_WISHLISTFRAGMENT);
        } else {
            jumpLoginActivity();
        }

    }

    @Override
    protected void jumpOrderPage() {
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_MYACCOUNT, HomeMyAccountFragmentV2.SWITCH_ORDERFRAGMENT);
        } else {
            jumpLoginActivity();
        }

    }

    @Override
    protected void jumpSettingPage() {
        if (!(mCurrentFragment instanceof HomeSettingCotentFragment)) {
            if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
                switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_SETTING, "setting");
            } else {
                jumpLoginActivity();
            }
        }
    }

    @Override
    protected void jumpEditProfilePage() {
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {
            jumpEditProfileActivity();
        } else {
            jumpLoginActivity();
        }
    }

    @Override
    protected void jumpCustomerServicePage() {

        TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
        dataentity.setHelpCenterType(9);
        dataentity.setType(HomeHelpCenterDetailFragment.FIREST_MENU);
        switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
    }

    @Override
    protected void jumpHelpCenterServicePage() {
        switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, null);
    }

    @Override
    protected void jumpShippingServicePage() {
        TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
        dataentity.setHelpCenterType(5);
        dataentity.setType(HomeHelpCenterDetailFragment.FIREST_MENU);
        switchFragment(-1, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, dataentity);
    }

    private void jumpLoginActivity() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivityForResult(intent, 1000);
        overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }

    private void jumpEditProfileActivity() {
        Intent intent = new Intent(this, MyAccountActivity.class);
        startActivityForResult(intent, 1000);
        overridePendingTransition(R.anim.activity_anim1_enter1, R.anim.activity_anim1_exit1);
    }

    private void addFragment(int index, Fragment fragment) {
        if (index < 0) {
            return;
        }
        if (mAttachedFragmentList == null) {
            mAttachedFragmentList = new ArrayList<Fragment>();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


    private void initFragment(Bundle savedInstanceState) {
        addFragment(FRAGMENT_TYPE_HOME_HOME, new HomeHomeFragment());
        addFragment(FRAGMENT_TYPE_HOME_MYACCOUNT, new HomeMyAccountFragmentV2());
        addFragment(FRAGMENT_TYPE_HOME_HELPCENTERLIST, new HomeHelpCenterListFragment());
        addFragment(FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, new HomeHelpCenterDetailFragment());
        addFragment(FRAGMENT_TYPE_HOME_NOTIFICATIONLIST, new HomeNotificationListFragment());
        addFragment(FRAGMENT_TYPE_HOME_NOTIFICATIONDETAIL, new HomeNotificationDetailFragment());
        addFragment(FRAGMENT_TYPE_HOME_SETTING, new HomeSettingCotentFragment());
        addFragment(FRAGMENT_TYPE_HOME_SHOPPINGCART, ShoppingCartVerticalFragment.newInstance(ShoppingCartBaseFragment.FROM_HOME, 0L));
        addFragment(FRAGMENT_TYPE_HOME_CREDITCARD, HomeSelectCrditCardFragment.newInstance());
        addFragment(FRAGMENT_TYPE_HOME_CATEGORY, new HomeCategoryTreeFragment());
    }
    @Override
    public void resetMenuAndListenter() {
        setLeftMenuIcon(R.drawable.action_menu);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDrawerLayout().getDrawerLockMode(Gravity.LEFT) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                    getDrawerLayout().openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    private Animation getFadeOutAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlMarketLayer.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlMarketLayer.setVisibility(View.GONE);
                rlMarketLayer.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }
    @Override
    public void setHomeSearchBarAndOnClick(View.OnClickListener onClickListener) {
        setHomeSearchBarClickListener(onClickListener);
    }

    public ImageView getIvMarketLayer() {
        return ivMarketLayer;
    }

    public RelativeLayout getRlMarketLayer() {
        return this.rlMarketLayer;
    }

    public void showMarketLayers() {
        marketLayerClosed = false;
        rlMarketLayer.clearAnimation();
        AnimUtil.animateFadeIn(this, rlMarketLayer, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rlMarketLayer.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivMarketLayerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JLogUtils.i("advertisement-click", "only permit to click advertisement close button.");
                if (!marketLayerClosed) {
                    marketLayerClosed = true;
                    marketLayerClose();
                }
            }
        });
    }

    public void closeMarketLayers() {
        if (rlMarketLayer.getVisibility() == View.VISIBLE) {
            rlMarketLayer.startAnimation(getFadeOutAnimation());
        }
//        enableSlidingMenu();
    }


    @Override
    public void initMarketingLayers(MarketingLayersEntity entity) {
        if (entity != null) {
            tvMarketingLayers.setText(entity.getTitle());
            tvMarketingLayersDesc.setText(entity.getDescription());
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_home);
        mImageLoader = new ImageLoader(this);
//        setTitleImage(R.mipmap.homepage_logo_white);
        mUserGuideHelper = new UserGuideHelper(this, mImageLoader);
        mUserGuideAbove = findViewById(R.id.v_user_guide);
        //iaml
        ivMarketLayer = (ImageView) findViewById(R.id.iv_marketing_layers);
        ivMarketLayerClose = (ImageView) findViewById(R.id.iv_marketing_layers_close);
        rlMarketLayer = (RelativeLayout) findViewById(R.id.rl_marketing_layers);
        tvMarketingLayers = (CustomTextView) findViewById(R.id.tv_marketing_layers);
        tvMarketingLayersDesc = (CustomTextView) findViewById(R.id.tv_marketing_layers_desc);

        resetMenuAndListenter();
        getDrawerLayout().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                updateLeftMenuNumber();
                boolean showGuide = JStorageUtils.showAppGuide3(HomeActivity.this);
                boolean islogin = GemfiveApplication.getAppConfiguration().isSignIn(HomeActivity.this);
                if (showGuide && islogin) {
                    showUserGuide(HomeBaseFragment.UserGuideType.LEFTMENU);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        initFragment(savedInstanceState);
        redirectToFragmentByIntent(getIntent());
        redirectToInterfaceByDeepLink();
    }

    private void redirectToInterfaceByDeepLink() {
        if (getIntent() != null && getIntent().getData() != null) {
            String host = getIntent().getData().getHost();
            if (DEEPLINK_TYPE_LANDINGPAGE.equals(host)) {
                String curationId = getIntent().getData().getPath().replace("/", "");
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, CurationActivity.class);
                intent.putExtra(CurationActivity.EXTRA_CURATION_ID, curationId);
                startActivity(intent);
            } else if (DEEPLINK_TYPE_PRODUCTDETAIL.equals(host)) {
                String productId = getIntent().getData().getPath().replace("/", "");
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        redirectToInterfaceByDeepLink();
        redirectToFragmentByIntent(intent);

    }

    public void redirectToFragmentByIntent(Intent intent) {
        Serializable serializable = null;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            fragmentType = FRAGMENT_TYPE_HOME_HOME;
        } else {
            if (EXTRA_REDIRECTTO_TYPE_VALUE_EXITAPP.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                this.finish();
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_WISHLIST.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_WISHLIST);
                if (!(mCurrentFragment instanceof HomeMyAccountFragmentV2)) {
                    fragmentType = FRAGMENT_TYPE_HOME_MYACCOUNT;
                    serializable = HomeMyAccountFragmentV2.SWITCH_WISHLISTFRAGMENT;
                } else {
                    HomeMyAccountFragmentV2 fragment = (HomeMyAccountFragmentV2) mAttachedFragmentList.get(1);
                    fragment.startFragmentByType(HomeMyAccountFragmentV2.SWITCH_WISHLISTFRAGMENT, true);
                    return;
                }
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_ORDER.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                fragmentType = FRAGMENT_TYPE_HOME_MYACCOUNT;
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_ORDER);
                serializable = HomeMyAccountFragmentV2.SWITCH_ORDERFRAGMENT;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTER.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HELPCENTER);
                fragmentType = FRAGMENT_TYPE_HOME_HELPCENTERLIST;
                serializable = bundle.getString(EXTRA_REDIRECTTO_TYPE);
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_ADDRESS.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                fragmentType = FRAGMENT_TYPE_HOME_MYACCOUNT;
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_ADDRESS);
                serializable = HomeMyAccountFragmentV2.SWITCH_ADDRESSFRAGMENT;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERCUSTOMSERVICE.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                fragmentType = FRAGMENT_TYPE_HOME_HELPCENTERDETAIL;
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_COSTOMSERVICE);
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(9);
                dataentity.setType(HomeHelpCenterDetailFragment.FIREST_MENU);
                redirectToAttachedFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, TYPE_FRAGMENT_SWITCH_NONE, dataentity);
                return;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERSHIPPINGDELIVERY.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                fragmentType = FRAGMENT_TYPE_HOME_HELPCENTERDETAIL;
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_SHIPPING);
                TMPHelpCenterListToDetailEntity dataentity = new TMPHelpCenterListToDetailEntity();
                dataentity.setHelpCenterType(5);
                redirectToAttachedFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, TYPE_FRAGMENT_SWITCH_NONE, dataentity);
                return;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_NOTIFICATION.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_NOTIFICATION);
                fragmentType = FRAGMENT_TYPE_HOME_NOTIFICATIONLIST;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_SHOPPINGCART);
                fragmentType = FRAGMENT_TYPE_HOME_SHOPPINGCART;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_CATEGORYTREE.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_CATEGORYTREE);
                fragmentType = FRAGMENT_TYPE_HOME_CATEGORY;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_SETTING.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_SETTING);
                fragmentType = FRAGMENT_TYPE_HOME_SETTING;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_START.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                Intent startIntent = new Intent(HomeActivity.this, StartActivity.class);
                startActivity(startIntent);
                finish();
                return;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_STORECREDIT.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_STORECREDITS);
                serializable = HomeMyAccountFragmentV2.SWITCH_STORECREDITFRAGMENT;
                fragmentType = FRAGMENT_TYPE_HOME_MYACCOUNT;
            } else if (EXTRA_REDIRECTTO_TYPE_VALUE_EDITPROFILE.equals(bundle.getString(EXTRA_REDIRECTTO_TYPE))) {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HOME);
                jumpEditProfileActivity();
                fragmentType = FRAGMENT_TYPE_HOME_HOME;
            } else {
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HOME);
                serializable = intent.getStringExtra("categoryId");
                fragmentType = FRAGMENT_TYPE_HOME_HOME;
            }
        }
        switchFragment(-1, fragmentType, serializable);
    }


    public void marketLayerClose() {
        closeMarketLayers();
        //notify homehomeFragment to cancel Thread-closeMarketRun of homeCategoryFragment.
        if (mCurrentFragment instanceof HomeHomeFragment) {
            ((HomeHomeFragment) mCurrentFragment).notifyToCancelCloseMarketRun();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (rlMarketLayer.getVisibility() == View.VISIBLE) {//返回界面是如果显示动画就关闭
            // closeMarketLayers();
            marketLayerClose();
        }
        if (mCurrentFragment != null && mCurrentFragment instanceof HomeHomeFragment) {
            ((HomeHomeFragment) mCurrentFragment).updateShoppingCartItemCount();
        }
    }

    public void switchFragment(int from, int to, Serializable serializable) {
        if (-1 == from) {
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_NONE, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST == from && HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL == to) {
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_RIGHT2LEFT, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL == from && HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST == to) {
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONLIST == from && HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONDETAIL == to) {//from notification list fragment to notification detail fragment
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_RIGHT2LEFT, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONDETAIL == from && HomeActivity.FRAGMENT_TYPE_HOME_NOTIFICATIONLIST == to) {//from notification detail fragment back to notification list fragment
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_LEFT2RIGHT, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_GOBACK == from && HomeActivity.FRAGMENT_TYPE_HOME_HOME == to) {
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_LEFT2RIGHT, serializable);
        } else if (HomeActivity.FRAGMENT_TYPE_HOME_CATEGORY == from && HomeActivity.FRAGMENT_TYPE_HOME_HOME == to) {
            redirectToAttachedFragment(to, TYPE_FRAGMENT_SWITCH_LEFT2RIGHT, serializable);
        }
    }

    private void redirectToAttachedFragment(int to, int type, Serializable serializable) {
        if (mAttachedFragmentList != null && mAttachedFragmentList.size() > to) {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            Fragment subFragment = mAttachedFragmentList.get(to);
            JLogUtils.i(currTag, "subFragment:::" + subFragment);
            if (type == TYPE_FRAGMENT_SWITCH) {
                mFragmentTransaction.setCustomAnimations(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
            }
            if ((subFragment != null) && (mCurrentFragment != null) && (subFragment.getClass().equals(mCurrentFragment.getClass()))) {
                if (serializable != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", serializable);
                    if (subFragment.getArguments() != null) {
                        subFragment.getArguments().putAll(bundle);
                    }
                    if (subFragment instanceof HomeMyAccountFragmentV2) {
                        ((HomeMyAccountFragmentV2) subFragment).startFragmentByType((String) serializable, true);
                    } else if (subFragment instanceof HomeHomeFragment) {
                        ((HomeHomeFragment) subFragment).switchTab((String) serializable);
                    } else if (subFragment instanceof HomeHelpCenterDetailFragment) {
                        ((HomeHelpCenterDetailFragment) subFragment).refresh((TMPHelpCenterListToDetailEntity) serializable);
                    } else if (subFragment instanceof ShoppingCartBaseFragment) {
                        ((ShoppingCartBaseFragment) subFragment).refresh();
                    }
                }
            } else {
                if (serializable != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", serializable);
                    JLogUtils.i(currTag,"---------------subFragment.getArguments():--------------"+subFragment.getArguments());
                    if(subFragment.getArguments()==null){
                         subFragment.setArguments(bundle);
                    }else{
                        subFragment.getArguments().putAll(bundle);
                    }
                    if (type == 1) {
                        mFragmentTransaction.setCustomAnimations(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        mCanback = false;
                        mHandler.postDelayed(new BackRunnble(this), 500);
                    }
                }
                mFragmentTransaction.replace(R.id.flContainer, subFragment);
                mFragmentTransaction.commitAllowingStateLoss();//效果等同于commit在activity
            }
            mCurrentFragment = subFragment;
            fragmentType = to;
        }
    }

    class BackRunnble implements Runnable {
        public WeakReference<HomeActivity> mActivity;
        public BackRunnble(HomeActivity activity) {
            mActivity = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void run() {
            if (mActivity.get() == null) return;
            mActivity.get().mCanback = true;
        }
    }

    public boolean mCanback = true;
    @Override
    public void onBackPressed() {
        showExitDialog();
    }
    public void showExitDialog() {
        String dialogMsg=getResources().getString(R.string.are_you_sure_you_want);
        String leftButton=getResources().getString(R.string.yes_upp);
        String rightButton=getResources().getString(R.string.no_upp);
        mDialog= JViewUtils.showExitDialog(this, dialogMsg, leftButton, rightButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GemfiveApplication.delayShowAppRate = false;
                        HomeActivity.this.finish();//执行完dialog的动画
                    }
                }, 300);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }
    public boolean refreshNotification(int type, String id) {
        if (mCurrentFragment instanceof HomeNotificationListFragment) {
            ((HomeNotificationListFragment) mCurrentFragment).refresh(type, id);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!GemfiveApplication.getAppConfiguration().isSignIn(HomeActivity.this) && requestCode == HomeSettingCotentFragment.CODE) {
            if (!(mCurrentFragment instanceof HomeHomeFragment)) {
                switchFragment(-1, FRAGMENT_TYPE_HOME_HOME, "1");
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (rlMarketLayer.getVisibility() == View.VISIBLE) {
                closeMarketLayers();
                return true;
            }
            if (mCurrentFragment instanceof HomeHomeFragment) {
                boolean isShowRateApp = ((HomeHomeFragment) mCurrentFragment).onKeyDown(keyCode, event);
                if (isShowRateApp) {
                    return true;
                }
            }
            if (mCurrentFragment instanceof HomeHelpCenterDetailFragment && ((HomeHelpCenterDetailFragment) mCurrentFragment).onBackPressed()) {
                if (mCanback) {
                    switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERDETAIL, HomeActivity.FRAGMENT_TYPE_HOME_HELPCENTERLIST, null);
                }
                return true;
            } else if (!(mCurrentFragment instanceof HomeHomeFragment)) {
                switchFragment(HomeActivity.FRAGMENT_TYPE_HOME_GOBACK, FRAGMENT_TYPE_HOME_HOME, null);
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_HOME);
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && mDialog != null && mDialog.isShowing()) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
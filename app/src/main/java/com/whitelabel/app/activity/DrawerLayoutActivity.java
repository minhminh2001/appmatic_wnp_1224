package com.whitelabel.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.NotificationCallback;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.fragment.HomeBaseFragment;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.utils.BadgeUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomCoordinatorLayout;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/**
 * Created by Administrator on 2016/10/3.
 */
public abstract class DrawerLayoutActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {
    private CustomCoordinatorLayout rootLayout;
    private AppBarLayout appbar_layout;
    private View flContainer;
    private DrawerLayout drawerLayout;
    private ImageView ivHome;
    private ImageView ivCategoryTree;
    private ImageView ivShoppingCart;
    private ImageView ivNotification;
    private ImageView ivMyWishList;
    private ImageView ivMyOrder;
    private ImageView ivAddress;
    private ImageView ivStoreCredit;
    private TextView tvUserName, tvHome, tvCategoryTree, tvShoppingCart, tvNotification, tvWistlist,
            tvMyOrder, tvSetting, tvCustomerService, tvHelpCenter, tvOrderNum, tvMyOrderNum,
            tvShipping, tvShoppingNum, tvNotificationNum, tvWistNum,
            tvAddress, tvStoreCredit;
    private Handler baseHandler = new Handler();
    private RelativeLayout rlDrawerOrder;
    private RelativeLayout rlDrawerAddress;
    private RelativeLayout rlDrawerSotreCredit;
    private NotificationReceiver receiver;
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
//                        showRelatedTerms();
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
            case R.id.tv_setting:
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
            case R.id.tv_customer_service:
                switchMenu(HomeBaseFragment.HomeCommonCallback.MENU_COSTOMSERVICE);
                drawerLayout.closeDrawer(Gravity.LEFT);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jumpCustomerServicePage();
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
        tvCustomerService = (TextView) findViewById(R.id.tv_customer_service);
        tvHelpCenter = (TextView) findViewById(R.id.tv_help_center);
        tvShipping = (TextView) findViewById(R.id.tv_shipping);
        tvShoppingNum = (TextView) findViewById(R.id.tv_shoppingCart_num);

        tvNotificationNum = (TextView) findViewById(R.id.tv_notification_num);
        tvWistNum = (TextView) findViewById(R.id.tv_wishlist_num);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvStoreCredit = (TextView) findViewById(R.id.tv_store_credit);
        ivAddress = (ImageView) findViewById(R.id.iv_address);
        ivStoreCredit = (ImageView) findViewById(R.id.iv_store_credit);
        RelativeLayout rlDrawerHome = (RelativeLayout) findViewById(R.id.rl_drawer_home);
        RelativeLayout rlDrawerCategoryTree = (RelativeLayout) findViewById(R.id.rl_drawer_categorytree);
        RelativeLayout rlDrawerShoppingCart = (RelativeLayout) findViewById(R.id.rl_drawer_shoppingcart);
        RelativeLayout rlDrawerNotification = (RelativeLayout) findViewById(R.id.rl_drawer_notification);
//        int black=ContextCompat.getColor(this,R.color.black000000);
        JViewUtils.setSlideMenuTextStyle(tvHome,false);
        JViewUtils.setSlideMenuTextStyle(tvShoppingCart,false);
        JViewUtils.setSlideMenuTextStyle(tvNotification,false);
        JViewUtils.setSlideMenuTextStyle(tvWistlist,false);
        JViewUtils.setSlideMenuTextStyle(tvMyOrder,false);
        JViewUtils.setSlideMenuTextStyle(tvAddress,false);
        JViewUtils.setSlideMenuTextStyle(tvCategoryTree,false);
        JViewUtils.setSlideMenuTextStyle(tvSetting,true);
        JViewUtils.setSlideMenuTextStyle(tvCustomerService,true);
        JViewUtils.setSlideMenuTextStyle(tvHelpCenter,true);
        JViewUtils.setSlideMenuTextStyle(tvShipping,true);
//        tvHome.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvShoppingCart.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvNotification.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvWistlist.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvMyOrder.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvAddress.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        int gray=ContextCompat.getColor(this,R.color.blackD0);
//        tvSetting.setTextColor(JImageUtils.getThemeTextColorDrawable(gray));
//        tvCustomerService.setTextColor(JImageUtils.getThemeTextColorDrawable(gray));
//        tvHelpCenter.setTextColor(JImageUtils.getThemeTextColorDrawable(gray));
//        tvShipping.setTextColor(JImageUtils.getThemeTextColorDrawable(gray));
//        tvCategoryTree.setTextColor(JImageUtils.getThemeTextColorDrawable(black));
//        tvOrderNum= (TextView) findViewById(R.id.tv_order_num);
        int iconDefaultColor=ContextCompat.getColor(this,R.color.blackD0);
        ivHome.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_default),iconDefaultColor));
        ivShoppingCart.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_shoppingcart_default),iconDefaultColor));
        ivNotification.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_notification_default),iconDefaultColor));
        ivMyWishList.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_wishlist_default),iconDefaultColor));
        ivMyOrder.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_order_default),iconDefaultColor));
        ivAddress.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_address_default),iconDefaultColor));
        ivCategoryTree.setImageDrawable(JImageUtils.getThemeIconSelector(ContextCompat.getDrawable(this,R.drawable.icon_drawer_home_categorytree_default),iconDefaultColor));

        RelativeLayout rlDrawerWish = (RelativeLayout) findViewById(R.id.rl_drawer_wishlist);
        rlDrawerOrder = (RelativeLayout) findViewById(R.id.rl_drawer_order);
        rlDrawerAddress = (RelativeLayout) findViewById(R.id.rl_drawer_address);
        rlDrawerSotreCredit = (RelativeLayout) findViewById(R.id.rl_drawer_store_credit);
        llProfile.setOnClickListener(this);
        rlDrawerCategoryTree.setOnClickListener(this);
        rlDrawerHome.setOnClickListener(this);
        rlDrawerShoppingCart.setOnClickListener(this);
        rlDrawerNotification.setOnClickListener(this);
        rlDrawerWish.setOnClickListener(this);
        rlDrawerOrder.setOnClickListener(this);
        rlDrawerAddress.setOnClickListener(this);
        rlDrawerSotreCredit.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        tvCustomerService.setOnClickListener(this);
        tvHelpCenter.setOnClickListener(this);
        tvShipping.setOnClickListener(this);
        tvShoppingNum.setBackground(JImageUtils.getThemeCircle(this));
        tvNotificationNum.setBackground(JImageUtils.getThemeCircle(this));
        tvWistNum.setBackground(JImageUtils.getThemeCircle(this));
    }
    private static final class DataHandler extends Handler {
        private WeakReference<DrawerLayoutActivity> mActivity;

        public DataHandler(DrawerLayoutActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case NotificationDao.REQUEST_NOTIFICATION_COUNT:
                    if (msg.arg1 == NotificationDao.RESPONSE_SUCCESS) {
                        Integer integer = (Integer) msg.obj;
                        if (integer > 0) {
                            if (integer > 99) {
                                mActivity.get().tvNotificationNum.setText("99+");
                            } else {
                                mActivity.get().tvNotificationNum.setText(String.valueOf(integer));
                            }
                            mActivity.get().tvNotificationNum.setVisibility(View.VISIBLE);
                        } else {
                            mActivity.get().tvNotificationNum.setVisibility(View.INVISIBLE);
                        }
                        BadgeUtils.setBadge(mActivity.get().getApplicationContext(), integer);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
//        receiver = new NotificationReceiver(callback);
//        IntentFilter intentFilter = new IntentFilter(NotificationReceiver.ACTION);
//        registerReceiver(receiver, intentFilter);
//        DataHandler dataHandler = new DataHandler(this);
//        mDao = new NotificationDao("NotificationReceiver", dataHandler);
//        String sessionKey = "";
//        if (WhiteLabelApplication.getAppConfiguration().getUser() != null && WhiteLabelApplication.getAppConfiguration() != null) {
//            sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
//        }
//        mDao.getNotificationDetailCount(sessionKey, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//        SendBoardUtil.sendNotificationBoard(this, SendBoardUtil.READCODE, null);
        initLayout();
//        setAppBarLayoutBehaviour();
//        mActionDrawableToggle = new ActionBarDrawerToggle(this, getDrawerLayout(), getToolbar(), R.string.openDrawer, R.string.closeDrawer) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                String sessionKey = "";
//                if (WhiteLabelApplication.getAppConfiguration().getUser() != null && WhiteLabelApplication.getAppConfiguration() != null) {
//                    sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
//                }
//                mDao.getNotificationDetailCount(sessionKey, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
//            }
//        };
//        getDrawerLayout().addDrawerListener(mActionDrawableToggle);
//        mActionDrawableToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private NotificationDao mDao;
    private NotificationCallback callback = new NotificationCallback() {
        @Override
        public void refreshNotification(int type, String id) {
            String sessionKey = "";
            boolean bool = DrawerLayoutActivity.this.refreshNotification(type, id);
            if (!bool) {
                if (WhiteLabelApplication.getAppConfiguration().getUser() != null && WhiteLabelApplication.getAppConfiguration() != null) {
                    sessionKey = WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey();
                }
                mDao.getNotificationDetailCount(sessionKey, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
            }
        }
    };

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
    }

    public void switchMenu(int type) {
        resetSate();
        if (type == HomeBaseFragment.HomeCommonCallback.MENU_HOME) {
            ivHome.setSelected(true);
            tvHome.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_CATEGORYTREE) {
            ivCategoryTree.setSelected(true);
            tvCategoryTree.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SHOPPINGCART) {
            ivShoppingCart.setSelected(true);
            tvShoppingCart.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_NOTIFICATION) {
            ivNotification.setSelected(true);
            tvNotification.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_WISHLIST) {
            ivMyWishList.setSelected(true);
            tvWistlist.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_ORDER) {
            ivMyOrder.setSelected(true);
            tvMyOrder.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_ADDRESS) {
            ivAddress.setSelected(true);
            tvAddress.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_STORECREDITS) {
            ivStoreCredit.setSelected(true);
            tvStoreCredit.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SETTING) {
            tvSetting.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_COSTOMSERVICE) {
            tvCustomerService.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_HELPCENTER) {
            tvHelpCenter.setSelected(true);
        } else if (type == HomeBaseFragment.HomeCommonCallback.MENU_SHIPPING) {
            tvShipping.setSelected(true);
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
        flContainer = findViewById(R.id.flContainer);
    }

    //因toolBar滑动原因,需要添加paddingBottom,主页除外
    public void setFragmentPaddingBottom(boolean hasPaddingBottom) {
        if (isFinishing()) return;
        if (hasPaddingBottom) {
            flContainer.setPadding(flContainer.getPaddingLeft(), flContainer.getPaddingTop(), flContainer.getPaddingRight(), getToolbar().getHeight());
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
}

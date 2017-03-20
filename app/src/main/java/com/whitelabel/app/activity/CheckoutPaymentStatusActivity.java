package com.whitelabel.app.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.fragment.CheckoutPaymentStatusRightFragment;
import com.whitelabel.app.fragment.CheckoutPaymentStatusWrongFragment;
import com.whitelabel.app.utils.GaTrackHelper;

import java.io.Serializable;

public class CheckoutPaymentStatusActivity extends DrawerLayoutActivity {

    private FrameLayout mContiner;
    private Fragment checkoutPaymentStatusFragment;

    private static String SESSION_KEY;
    public String html;
    private final int PAYMENTSUCESS = 1;
    private final int PAYMENTFAILURE = 2;
    public boolean isLuckDraw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment_status);
//        setBehindContentView(R.layout.frame_menu);
        mContiner = (FrameLayout) findViewById(R.id.flContainer);
        //init session_key
        //SharedPreferences sharedPreferences = getSharedPreferences("session_key", Activity.MODE_PRIVATE);
        SESSION_KEY = GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey();
        setTitle(getResources().getString(R.string.PAYMENT_STATUS));
        setLeftMenuIcon(R.drawable.action_menu);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawerLayout().openDrawer(Gravity.LEFT);
            }
        });

        initData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setRightTextMenuClickListener(
                getMenuInflater(),
                R.menu.menu_shopping_cart,
                menu,
                R.id.action_shopping_cart,
                R.layout.item_count, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpShoppingCartPage();
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shopping_cart:
                jumpShoppingCartPage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean refreshNotification(int type, String id) {
        return false;
    }

    @Override
    protected void jumpHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    public void jumpHomePage(Serializable serializable) {
        jumpHomePage();
    }

    @Override
    protected void jumpCategoryTreePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_CATEGORYTREE);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpShoppingCartPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpNotificationPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_NOTIFICATION);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpWistListPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_WISHLIST);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpOrderPage() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ORDER);
        i.putExtras(bundle1);
        startActivity(i);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpSettingPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SETTING);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpEditProfilePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_EDITPROFILE);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpCustomerServicePage() {
        Intent intent1 = new Intent(this, HomeActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERCUSTOMSERVICE);
        intent1.putExtras(bundle1);
        startActivity(intent1);
//        this.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        this.finish();
    }

    @Override
    protected void jumpHelpCenterServicePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTER);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpShippingServicePage() {
        Intent intent2 = new Intent(this, HomeActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERSHIPPINGDELIVERY);
        intent2.putExtras(bundle2);
        startActivity(intent2);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpAddressPage() {
        Intent intent2 = new Intent(this, HomeActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ADDRESS);
        intent2.putExtras(bundle2);
        startActivity(intent2);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    @Override
    protected void jumpStoreCreditPage() {
        Intent storeCreditIntent = new Intent(this, HomeActivity.class);
        Bundle storeCreditBundle = new Bundle();
        storeCreditBundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_STORECREDIT);
        storeCreditIntent.putExtras(storeCreditBundle);
        startActivity(storeCreditIntent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        finish();
    }

    public boolean mGATrackTimeEnable = true;
    public long mGATrackTimeStart;
    private int fromType;

    private void initData() {
        Bundle bundle = getIntent().getExtras();

        mGATrackTimeStart = bundle.getLong("mGATrackTimeStart", 0);

        fromType = bundle.getInt("fromType");
        String paymentStatus = bundle.getString("payment_status");
        String errorMsg = bundle.getString("errorMsg");
        String orderNumber = bundle.getString("orderNumber");

        isLuckDraw = bundle.getBoolean("isLuckDraw", false);
        html = bundle.getString("html");

        if ("1".equalsIgnoreCase(paymentStatus)) {
            checkoutPaymentStatusFragment = new CheckoutPaymentStatusRightFragment();
//            bundle1.putInt("fromType", fromType);
            checkoutPaymentStatusFragment.setArguments(bundle);
            gaTrackerPayment(PAYMENTSUCESS);
        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("errorMsg", errorMsg);
            bundle1.putString("orderNumber", orderNumber);
            bundle1.putInt("fromType", fromType);
            checkoutPaymentStatusFragment = new CheckoutPaymentStatusWrongFragment();
            checkoutPaymentStatusFragment.setArguments(bundle1);
            gaTrackerPayment(PAYMENTFAILURE);

        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flContainer, checkoutPaymentStatusFragment).commit();
    }

    public void gaTrackerPayment(int type) {
        String payment = "";
        if (type == PAYMENTSUCESS) {
            payment = "Payment Sucess";
        } else if (type == PAYMENTFAILURE) {
            payment = "Payment Failure";
        }
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                    "Payment",
                    payment,
                    null);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setLolppoNavPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
            boolean hasBar = hasNavBar(CheckoutPaymentStatusActivity.this);
            if (hasBar) {
                int navBarHeight = getNavigationBarHeight();
                findViewById(R.id.flContainer_paymentstatus).setPadding(0, 0, 0, navBarHeight);
            }
        }
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, ShoppingCartActivity1.class);
            //clear top
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String getSessionKey() {
        return SESSION_KEY;
    }
}
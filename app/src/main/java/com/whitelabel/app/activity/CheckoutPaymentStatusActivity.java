package com.whitelabel.app.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.ui.checkout.CheckoutPaymentStatusRightFragment;
import com.whitelabel.app.fragment.CheckoutPaymentStatusWrongFragment;
import com.whitelabel.app.ui.home.MainContract;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JViewUtils;
import java.io.Serializable;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

public class CheckoutPaymentStatusActivity extends DrawerLayoutActivity<MainContract.Presenter> implements MainContract.View {
    public String html;
    private final int PAYMENTSUCESS = 1;
    private final int PAYMENTFAILURE = 2;
    public boolean isLuckDraw;
    public boolean mGATrackTimeEnable = true;
    public long mGATrackTimeStart;
    public final static String EXTRA_ORDER_NUMBER="order_number";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment_status);
        WhiteLabelApplication.getAppConfiguration().getUser().setCartItemCount(0);
        WhiteLabelApplication.getAppConfiguration().updateUserData(this,WhiteLabelApplication.getAppConfiguration().getUser());
        setTitle(getResources().getString(R.string.PAYMENT_STATUS));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.ic_action_menu));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawerLayout().openDrawer(Gravity.LEFT);
            }
        });
        initData();
    }
    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(this)).build().inject(this);
    }

    @Override
    public void setNotificationUnReadCount(int unReadCount) {
        setNotificationCount(unReadCount);
    }

    @Override
    public void requestNotificationUnReadCount() {
        mPresenter.getNotificationUnReadCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        View view =setRightTextMenuClickListener(
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
        ImageView ivShopping= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(this,ivShopping,R.drawable.ic_action_cart);
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
        finish();
    }
    @Override
    protected void jumpShoppingCartPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    protected void jumpNotificationPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_NOTIFICATION);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    protected void jumpWistListPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_WISHLIST);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void jumpOrderPage() {
        Intent i = new Intent(this, HomeActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ORDER);
        i.putExtras(bundle1);
        startActivity(i);
        finish();
    }

    @Override
    protected void jumpSettingPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SETTING);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void jumpEditProfilePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_EDITPROFILE);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void jumpCustomerServicePage() {
        Intent intent1 = new Intent(this, HomeActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERCUSTOMSERVICE);
        intent1.putExtras(bundle1);
        startActivity(intent1);
//        this.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
        this.finish();
    }
    @Override
    protected void jumpHelpCenterServicePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTER);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    protected void jumpShippingServicePage() {
        Intent intent2 = new Intent(this, HomeActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_HELPCENTERSHIPPINGDELIVERY);
        intent2.putExtras(bundle2);
        startActivity(intent2);
        finish();
    }
    @Override
    protected void jumpAddressPage() {
        Intent intent2 = new Intent(this, HomeActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ADDRESS);
        intent2.putExtras(bundle2);
        startActivity(intent2);
        finish();
    }
    @Override
    protected void jumpStoreCreditPage() {
        Intent storeCreditIntent = new Intent(this, HomeActivity.class);
        Bundle storeCreditBundle = new Bundle();
        storeCreditBundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_STORECREDIT);
        storeCreditIntent.putExtras(storeCreditBundle);
        startActivity(storeCreditIntent);
        finish();
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mGATrackTimeStart = bundle.getLong("mGATrackTimeStart", 0);
        int fromType = bundle.getInt("fromType");
        String paymentStatus = bundle.getString("payment_status");
        String errorMsg = bundle.getString("errorMsg");
        String orderNumber = bundle.getString("orderNumber");
        isLuckDraw = bundle.getBoolean("isLuckDraw", false);
        html = bundle.getString("html");
        android.support.v4.app.Fragment checkoutPaymentStatusFragment;
        if ("1".equalsIgnoreCase(paymentStatus)) {
            checkoutPaymentStatusFragment = new CheckoutPaymentStatusRightFragment();
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
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, ShoppingCartActivity1.class);
            //clear top
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.whitelabel.app.ui.checkout;

import com.molpay.molpayxdk.MOLPayActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.CheckoutPaymentRedirectActivity;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.adapter.DialogProductAdapter;
import com.whitelabel.app.dao.CheckoutDao;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.fragment.CheckoutPaymentFragment;
import com.whitelabel.app.fragment.CheckoutReviewFragment;
import com.whitelabel.app.fragment.CheckoutShippingAddaddressFragment;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddressParameter;
import com.whitelabel.app.model.CheckoutPaymentSaveReturnEntity;
import com.whitelabel.app.model.DialogProductBean;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppserviceSaveBillingEntity;
import com.whitelabel.app.model.SVRAppserviceSaveOrderReturnEntity;
import com.whitelabel.app.model.ShoppingDiscountBean;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PaypalHelper;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CheckoutPaymentDialog;
import com.whitelabel.app.widget.MaterialDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

public class CheckoutActivity extends com.whitelabel.app.BaseActivity<CheckoutContract.Presenter>
    implements View.OnClickListener, CheckoutContract.View, CheckoutRegisterFragment
    .CheckoutRegisterCallBack {

    private static final String TAG_ADD_ADDRESS = "addNewAddressFragment";

    private static final String TAG_DEFAULT_ADDRESS = "defaultAddressFragment";

    private static final String TAG_PAYMENT = "paymentFragment";

    private static final String TAG_REVIEW = "reviewFragment";

    private static final String TAG_REGISTER = "register";

    private static final int MENU_PROFILE = 0;

    private static final int MENU_ADDRESS = 1;

    private static final int MENU_PAYMENT = 2;

    private static final int MENU_REVIEW = 3;

    private final static int FRAGMENT_SELECT_ADDRESS = 1;

    private final static int FRAGMENT_ADD_ADDRESS = 0;

    private final static int FRAGMENT_PAYMENT = 2;

    private final static int FRAGMENT_REVIEW = 3;

    private final static int FRAGMENT_REGISTER = 5;

    private final int REQUESTCODE_LOGIN = 1000;

    public final static int REQUESTCODE_CHECKOUT = 1001;

    public final static String CHECKOUT_IS_JUST_LOGIN = "checkout_is_just_login";

    public Long mGATrackCheckoutTimeStart = 0L;

    public boolean mGATrackCheckoutTimeEnable = false;

    public boolean mGATrackAddressToPaymentTimeEnable = false;

    public Long mGATrackAddressToPaymentTimeStart = 0L;

    public Long mGATrackPlaceOrderToResultTimeStart = 0L;

//    public ArrayList<Fragment> list_fragment;

    public TextView btnContinue;

    public ScrollView scrollViewBody;

    public LinearLayout llBody, ll_btn;

    public ProgressBar progressBarLoading;

    public String TAG = CheckoutActivity.class.getSimpleName();

    public String paymentMethodCode;

    public String molpayType;

    /**
     * 1. to record fragment count of Shipping Module for Go Back Button.
     * 2. making this variable public in order to be seen in Fragment
     * 3. it's size need to less than 3 : defaultAddressFragment,selectAddressFragment,
     * addAddressFragment
     * and more than 1 : defaultAddressFragment or addAddressFragment
     */
    public ArrayList<Fragment> list_fragment_shipping;

    public int fromType;

    public AddressBook shippingAddress;

    public AddressBook billingAddress;

    public CheckoutDefaultAddressResponse.PickUpAddress pickUpAddress;

    public boolean isBillAddressChecked;

    public boolean isPickInStoreChecked;

    String amount = "";

    String shippingFee = "";

    private boolean isGuestModel;

    private List<View> vPoint;

    private List<View> vLine;

    private String errorProductTitle;

    private FragmentTransaction fragmentTransaction;

    private CheckoutDefaultAddressFragment checkoutDefaultAddressFragment;

    private Fragment checkoutPaymentFragment;

    private Fragment checkoutReviewFragment;

    private Fragment checkAddaddressFragment;

    private String order_id;

    private AddressParameter addressParams;

    private String html;

    private String paymethodType;//1 online 2credit 3.offline payment

    private String bank;

    private CheckoutRegisterFragment checkoutRegisterFragment;

    /**
     * record that if fragment has entered into payment module.
     * initialized value is true because it must be in shipping model in the beginning.
     */

    private int currentModule = 1;

    /**
     * is "go back" operation or not
     */
    private DataHandler mHandler = new DataHandler(this);

    private boolean isGoBack = false;

    private CheckoutPaymentDialog mCheckoutPaymentDialog = null;//place order dialog

    private Dialog mDialog;//common loading dialog

    private ShoppingDiscountBean mDiscountBean;

    private int skipPayment;

    private MyAccountDao mAccountDao;

    private CheckoutDao mCheckoutDao;

    private CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity;

    private ImageLoader mImageLoader;

    private PaypalHelper mPaypalHelper;

    private boolean isClick = true;

    private List<View> vText;

    private boolean isJustLoggedin = false;

    @Override
    protected void initInject() {
        DaggerPresenterComponent1.builder()
            .applicationComponent(WhiteLabelApplication.getApplicationComponent()).
            presenterModule(new PresenterModule(this)).build().inject(this);
    }

    @Override
    public void showCheckProgressDialog() {
        mCheckoutPaymentDialog = new CheckoutPaymentDialog(CheckoutActivity.this,
            R.style.loading_dialog, getResources().getString(R.string.dialog_checkout_text))
            .showDialog();
    }

    @Override
    public void showNetErrorMessage() {

        if(mDialog != null){
            mDialog.cancel();
        }

        RequestErrorHelper requestErrorHelper = new RequestErrorHelper(this);
        requestErrorHelper.showNetWorkErrorToast();
    }

    @Override
    public void switchNextFragment() {

        openSelectFragment();
    }

    @Override
    public void showFaildMessage(String faildMessage) {
        JViewUtils.showMaterialDialog(this, "", faildMessage, null);
    }

    public void closeCheckoutPaymentDialog() {
        if (mCheckoutPaymentDialog != null && mCheckoutPaymentDialog.isShowing()) {
            mCheckoutPaymentDialog.dismiss();
        }
    }

    @Override
    public void startPayPalPaymentActivity(String url, String orderNumber) {
        Intent intent = new Intent(this, PayPalPaymentActivity.class);
        intent.putExtra(PayPalPaymentActivity.PAYMENT_URL, url);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PayPalPaymentActivity.EXTRA_DATA, paymentSaveReturnEntity);
        intent.putExtra(PayPalPaymentActivity.PAYMENT_ORDER_NUMBER, orderNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * e
     * to record fragment(module) count
     * less than 3: shippingFragment,paymentFragment,reviewFragment.
     * more than 1: at least shippingFragment.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initIntent();
        reInit();
        initView();
        initData();
        initToolBar();
        mPaypalHelper = new PaypalHelper();
        mPaypalHelper.startPaypalService(this);
    }

    private void initToolBar() {
        setTitle(getResources().getString(R.string.CHECKOUT));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this, R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackOption();
            }
        });
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDiscountBean = (ShoppingDiscountBean) bundle.getSerializable("discountBean");
            fromType = bundle.getInt("fromType");
            mGATrackCheckoutTimeStart = bundle.getLong("mGATrackTimeStart", 0L);
            mGATrackCheckoutTimeEnable = true;
        }
    }

    public void expireStartLoginActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expire", true);
        BottomtoTopActivity(bundle, LoginRegisterActivity.class, false);
    }

    private void initData() {
        mAccountDao = new MyAccountDao(TAG, mHandler);
        mCheckoutDao = new CheckoutDao(TAG, mHandler);
        errorProductTitle = getResources().getString(R.string.checkout_product_error_title);
        if (!isGuestModel) {
            setButtonEnable(true);
            openSelectFragment();
        } else {
            openRegisterFragment();
        }
    }

    private void openPaymentFragment() {
        switchMenu(MENU_PAYMENT);
        currentModule = FRAGMENT_PAYMENT;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hiddenAll(fragmentTransaction);
        if (checkoutPaymentFragment == null) {
            checkoutPaymentFragment = new CheckoutPaymentFragment();
            fragmentTransaction
                    .add(R.id.ll_checkout_body, checkoutPaymentFragment, TAG_PAYMENT);
        }
        fragmentTransaction.show(checkoutPaymentFragment).commitAllowingStateLoss();
    }

    private void openReviewFragment(String code) {
        currentModule = FRAGMENT_REVIEW;
        switchMenu(MENU_REVIEW);
        Bundle bundle = new Bundle();
        bundle.putSerializable("paymentSaveReturnEntity", paymentSaveReturnEntity);
        if (!TextUtils.isEmpty(molpayType)) {
            bundle.putString("payment[molpay_type]", molpayType);
        }
        if (!TextUtils.isEmpty(code)) {
            bundle.putString("code", code);
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        btnContinue.setText(getResources().getString(R.string.PLACE_MY_ORDER));
        hiddenAll(fragmentTransaction);
        if (checkoutReviewFragment == null) {
            checkoutReviewFragment = new CheckoutReviewFragment();
            checkoutReviewFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.ll_checkout_body, checkoutReviewFragment, TAG_REVIEW);

        }

        fragmentTransaction.show(checkoutReviewFragment).commitAllowingStateLoss();
    }

    private void openRegisterFragment() {
        currentModule = FRAGMENT_REGISTER;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hiddenAll(fragmentTransaction);
        if (checkoutRegisterFragment == null) {
            checkoutRegisterFragment = new CheckoutRegisterFragment();
            fragmentTransaction
                    .add(R.id.ll_checkout_body, checkoutRegisterFragment, TAG_REGISTER);
        }
        fragmentTransaction.show(checkoutRegisterFragment).commitAllowingStateLoss();
    }

    public void reInit() {
        checkAddaddressFragment = getSupportFragmentManager().findFragmentByTag(TAG_ADD_ADDRESS);
        checkoutDefaultAddressFragment = (CheckoutDefaultAddressFragment) getSupportFragmentManager()
            .findFragmentByTag(TAG_DEFAULT_ADDRESS);
        checkoutPaymentFragment = getSupportFragmentManager().findFragmentByTag(TAG_PAYMENT);
        checkoutReviewFragment = getSupportFragmentManager().findFragmentByTag(TAG_REVIEW);
    }

    public void hiddenAll(FragmentTransaction fragmentTransaction) {
        if (checkAddaddressFragment != null) {
            fragmentTransaction.hide(checkAddaddressFragment);
        }
        if (checkoutDefaultAddressFragment != null) {
            fragmentTransaction.hide(checkoutDefaultAddressFragment);
        }
        if (checkoutPaymentFragment != null) {
            fragmentTransaction.hide(checkoutPaymentFragment);
        }
        if (checkoutReviewFragment != null) {
            fragmentTransaction.hide(checkoutReviewFragment);
        }
        if (checkoutRegisterFragment != null) {
            fragmentTransaction.hide(checkoutRegisterFragment);
        }
    }

    private void initView() {

        isGuestModel = !WhiteLabelApplication.getAppConfiguration().isSignIn(CheckoutActivity.this);
        initTopMenu();

        mImageLoader = new ImageLoader(this);
        btnContinue = (TextView) findViewById(R.id.btn_checkout_payment_continue);
        ll_btn = (LinearLayout) findViewById(R.id.ll_checkout_bottomBar);
        scrollViewBody = (ScrollView) findViewById(R.id.sv_checkout_body);
        llBody = (LinearLayout) findViewById(R.id.ll_checkout_body);
        progressBarLoading = (ProgressBar) findViewById(R.id.pb_checkout_body_loading);
        btnContinue.setOnClickListener(this);
        ll_btn.setOnClickListener(this);
        ll_btn.setVisibility(View.VISIBLE);
        JViewUtils.setSoildButtonGlobalStyle(this, btnContinue);
    }

    private void initTopMenu() {
        vPoint = new ArrayList<>();
        vLine = new ArrayList<>();
        vText = new ArrayList<>();
        View pointOne = findViewById(R.id.iv_point_one);
        View pointTwo = findViewById(R.id.iv_point_two);
        View pointThree = findViewById(R.id.iv_point_three);
        View pointFour = findViewById(R.id.iv_point_four);
        View vProgressOne = findViewById(R.id.v_progress_one);
        View vProgressTwo = findViewById(R.id.v_progress_two);
        View vProgressThree = findViewById(R.id.v_progress_three);

        TextView vProfile = (TextView) findViewById(R.id.tv_profile);
        TextView vAddress = (TextView) findViewById(R.id.tv_address);
        TextView vPayment = (TextView) findViewById(R.id.tv_payment);
        TextView vReview = (TextView) findViewById(R.id.tv_review);
        vProfile.setTextColor(
            JImageUtils.getThemeTextColorDrawable(ContextCompat.getColor(this, R.color.black)));
        vAddress.setTextColor(
            JImageUtils.getThemeTextColorDrawable(ContextCompat.getColor(this, R.color.black)));
        vPayment.setTextColor(
            JImageUtils.getThemeTextColorDrawable(ContextCompat.getColor(this, R.color.black)));
        vReview.setTextColor(
            JImageUtils.getThemeTextColorDrawable(ContextCompat.getColor(this, R.color.black)));

        vText.add(vAddress);
        vText.add(vPayment);
        vText.add(vReview);
        vLine.add(vProgressTwo);
        vLine.add(vProgressThree);
        vPoint.add(pointTwo);
        vPoint.add(pointThree);
        vPoint.add(pointFour);
        if (isGuestModel) {
            vPoint.add(0, pointOne);
            vLine.add(0, vProgressOne);
            vText.add(0, vProfile);
            setLineWidth((JScreenUtils.getScreenWidth(this) - JScreenUtils.dip2px(this, 40)) / 4);
            switchMenu(MENU_PROFILE);
        } else {
            pointOne.setVisibility(View.GONE);
            vProgressOne.setVisibility(View.GONE);
            vProfile.setVisibility(View.GONE);
            setLineWidth((JScreenUtils.getScreenWidth(this) - JScreenUtils.dip2px(this, 30)) / 3);
            switchMenu(MENU_ADDRESS);
        }
    }

    private void switchMenu(int menu) {
        menu = !isGuestModel ? menu - 1 : menu;
        for (int i = 0; i < vPoint.size(); i++) {
            vText.get(i).setSelected(i == menu);
            vPoint.get(i).setBackground(JImageUtils.getGrayThemeIcon(this,R.drawable.button_oval_grey));
            if (i == menu) {
                vPoint.get(i)
                    .setBackground(
                        JImageUtils.getThemeIcon(this, R.drawable.button_oval_grey_stroke));
            } else if (i < menu) {
                vPoint.get(i)
                    .setBackground(JImageUtils.getThemeIcon(this, R.drawable.button_oval_grey));
            }
        }

        for (int i = 0; i < vLine.size(); i++) {
            if (i < menu) {
                vLine.get(i).setBackgroundColor(
                    WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            } else {
                vLine.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.greyEEEEEE));
            }

        }
    }

    private void setLineWidth(int width) {
        for (int i = 0; i < vLine.size(); i++) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vLine.get(i)
                .getLayoutParams();
            params.height = JScreenUtils.dip2px(this, 1.5f);
            params.width = width;
            vLine.get(i).setLayoutParams(params);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkout_payment_continue:
                continueOption();
                break;
            default:
                break;
        }
    }

    /**
     * GO BACK BUTTON
     * go back to last fragment or activity
     */
    private void goBackOption() {
        switch (currentModule) {
            case FRAGMENT_REVIEW:
                openPaymentFragment();
                currentModule=FRAGMENT_PAYMENT;
                break;
            case FRAGMENT_PAYMENT:
                openSelectFragment();
                currentModule=FRAGMENT_SELECT_ADDRESS;
                break;
            case FRAGMENT_SELECT_ADDRESS:
            case FRAGMENT_ADD_ADDRESS:
            case FRAGMENT_REGISTER:
                Intent intent = new Intent();
                intent.putExtra(CHECKOUT_IS_JUST_LOGIN, isJustLoggedin());
                setResult(REQUESTCODE_CHECKOUT, intent);
                finish();
                break;
        }
    }


    @Override
    public void showUpdateDialog() {

        if(mDialog != null){
            mDialog.cancel();
        }

        JViewUtils.showUpdateGooglePlayStoreDialog(this);
    }

    @Override
    public void startPayPalPlaceOrder() {
        if(mDialog != null){
            mDialog.cancel();
        }

        GaTrackHelper.getInstance().googleAnalytics("Review Order Screen", this);
        mPresenter.payPalPlaceOrder(
                ((CheckoutReviewFragment) checkoutReviewFragment).getOrderComment());
    }

    /**
     * CONTINUE BUTTON
     */
    private void continueOption() {
        isGoBack = false;
        switch (currentModule) {
            case FRAGMENT_ADD_ADDRESS:
                if (checkAddaddressFragment != null) {
                    ((CheckoutAddaddressFragment) checkAddaddressFragment)
                        .checkAndSave(new CheckoutAddaddressFragment.ISaveAddressMsgData() {
                            @Override
                            public void createCustomerAddress(String firstName, String lastName,
                                String countryId, String telePhone, String street0, String street1,
                                String fax, String postCode, String city, String region,
                                String regionId) {
                                mPresenter.createCustomerAddress(firstName, lastName, countryId,
                                    telePhone, street0, street1, fax, postCode, city, region,
                                    regionId);
                            }
                        });
                }
                break;
            case FRAGMENT_SELECT_ADDRESS://In shipping Module
                sendRequestAndGoToNextPaymentModule();
                GaTrackHelper.getInstance().googleAnalytics("Select Address Screen", this);
                JLogUtils.i("googleGA_screen", "Select Address Screen");
                break;
            case FRAGMENT_PAYMENT://In payment Module
                sendRequestToSavePayment();
                GaTrackHelper.getInstance().googleAnalytics("Select Payment Screen", this);
                JLogUtils.i("googleGA_screen", "Select Payment Screen");
                break;
            case FRAGMENT_REVIEW://place my order
                mDialog = JViewUtils.showProgressDialog(this);
                mPresenter.versionCheck();
                break;
            case FRAGMENT_REGISTER:
                checkoutRegisterFragment.requestResister();
                break;
        }
    }

    /**
     * Go to next payment redirect activity ,then open a webview and send Request Of Payment
     * Redirect
     */
    private void goToPaymentRedirectPage(String lastrealorderid, String payment_type,
        String grandTotal, String shippingFee, String payUrl) {

        //Go to next payment redirect activity ,then open a webview and send Request Of Payment
        // Redirect
        Bundle bundle = new Bundle();
        bundle.putLong("mGATrackTimeStart", mGATrackPlaceOrderToResultTimeStart);
        bundle.putString("session_key",
            WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getSessionKey());
        bundle.putString("lastrealorderid", lastrealorderid);
        bundle.putString("payment_type", payment_type);
        bundle.putString("grand_total", grandTotal.replace("RM", "").trim());
        bundle.putString("shipping_fee", shippingFee.replace("RM", "").trim());
        bundle.putInt("paymentMethod", CheckoutPaymentRedirectActivity.PAYMENT_CARD);
        if (!TextUtils.isEmpty(payUrl)) {
            bundle.putString("payUrl", payUrl);
        }
        bundle.putSerializable("paymentSaveReturnEntity", paymentSaveReturnEntity);
        bundle.putString("payUrl", payUrl);
        bundle.putInt("fromType", fromType);
        if (mDiscountBean != null) {
            bundle.putSerializable("discountBean", mDiscountBean);
        }

        startNextActivity(bundle, CheckoutPaymentRedirectActivity.class, true);
    }

    public void goToPaymentSuccess(String lastrealorderid, String payment_type, String grandTotal,
        String shippingFee, String html) {
        Bundle bundle = new Bundle();
        bundle.putString("payment_status", "1");
        bundle.putString("orderNumber", lastrealorderid);
        bundle.putString("grand_total", grandTotal);
        bundle.putString("shipping_fee", shippingFee);
        bundle.putString("html", html);
        if (mDiscountBean != null) {
            bundle.putSerializable("discountBean", mDiscountBean);
        }
        bundle.putInt("fromType", fromType);
        bundle.putLong("mGATrackTimeStart", mGATrackPlaceOrderToResultTimeStart);
        bundle.putSerializable("paymentSaveReturnEntity", paymentSaveReturnEntity);
        startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
    }

    public void gaTrackerPlaceOrder() {
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                "Place Order",
                null,
                null);

        } catch (Exception ex) {
            ex.getStackTrace();
        }

    }

    /**
     * transfer ChannelCode according to channel text
     */
    private String transferChannelCode(String text) {
        String channelCode;
        if ("Maybank2u".equalsIgnoreCase(text)) {
            channelCode = "maybank2u";
        } else if ("Hong Leong Connect".equalsIgnoreCase(text)) {
            channelCode = "hlb";
        } else if ("CIMB Clicks".equalsIgnoreCase(text)) {
            channelCode = "cimb";
        } else if ("RHB Now".equalsIgnoreCase(text)) {
            channelCode = "rhb";
        } else if ("AmOnline".equalsIgnoreCase(text)) {
            channelCode = "amb";
        } else if ("FPX e-Payment Channel".equalsIgnoreCase(text)) {
            channelCode = "fpx";
        } else {
            channelCode = null;
        }

        return channelCode;
    }

    /**
     * send Request To Save Payment and parse return entity which is param in next review fragment
     */
    private void sendRequestToSavePayment() {
        final CheckoutPaymentFragment paymentFragment = (CheckoutPaymentFragment)
            getSupportFragmentManager()
                .findFragmentByTag(TAG_PAYMENT);
        paymentFragment.savePayment(null);
    }

    public void setButtonEnable(boolean enable) {
        btnContinue.setEnabled(enable);
        if (enable) {
            JViewUtils.setSoildButtonGlobalStyle(this, btnContinue);
        } else {
            btnContinue.setBackgroundResource(R.drawable.big_button_style_b8);
        }
    }

    @Override
    public void showAddressSuccess(boolean isSuccess) {
        if (isSuccess) {
            skipToDefaultAddressPage();
        }
    }

    //first register and to checkout page to skip :add address page
    public void openSelectFragment() {
        switchMenu(MENU_ADDRESS);
        currentModule = FRAGMENT_SELECT_ADDRESS;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hiddenAll(fragmentTransaction);
        if (checkoutDefaultAddressFragment == null) {
            checkoutDefaultAddressFragment = new CheckoutDefaultAddressFragment();
            fragmentTransaction
                    .add(R.id.ll_checkout_body, checkoutDefaultAddressFragment, TAG_DEFAULT_ADDRESS);
        }
        if (checkoutRegisterFragment != null) {
            fragmentTransaction.remove(checkoutRegisterFragment);
        }
        fragmentTransaction.show(checkoutDefaultAddressFragment).commitAllowingStateLoss();
        ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .setiChangeAddAddressPage(
                new CheckoutDefaultAddressFragment.IChangeAddAddressPage() {
                    @Override
                    public void selectAddressFragment() {

                        currentModule = FRAGMENT_ADD_ADDRESS;
                        checkAddaddressFragment = new CheckoutAddaddressFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        hiddenAll(fragmentTransaction);
                        fragmentTransaction.remove(checkoutDefaultAddressFragment);
                        checkoutDefaultAddressFragment=null;
                        fragmentTransaction.add(R.id.ll_checkout_body, checkAddaddressFragment,
                            TAG_ADD_ADDRESS);
                        fragmentTransaction.show(checkAddaddressFragment)
                            .commitAllowingStateLoss();
                    }
                });
    }

    //to select shipping or pick up store page :show address page
    private void skipToDefaultAddressPage() {

        currentModule = FRAGMENT_SELECT_ADDRESS;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hiddenAll(fragmentTransaction);
        fragmentTransaction.remove(checkAddaddressFragment);
        checkAddaddressFragment=null;
        if (checkoutDefaultAddressFragment == null) {
            checkoutDefaultAddressFragment = new CheckoutDefaultAddressFragment();
            fragmentTransaction
                    .add(R.id.ll_checkout_body, checkoutDefaultAddressFragment, TAG_DEFAULT_ADDRESS);
        }
        fragmentTransaction.show(checkoutDefaultAddressFragment).commitAllowingStateLoss();
    }

    public void switReviewFragment(String molpayType,
        CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity, String code, String html,
        String type, String bank) {

        this.html = html;
        this.paymentMethodCode = code;
        paymethodType = type;
        this.paymentSaveReturnEntity = paymentSaveReturnEntity;
        //switch fragment and set params
        this.molpayType = molpayType;
        this.bank = bank;
        openReviewFragment(code);
    }

    public void showProductDialog(String title, ArrayList<DialogProductBean> beans) {
        // The following item(s) in your cart cannot be delivered to your location
        final MaterialDialog mMaterialDialog = new MaterialDialog(CheckoutActivity.this);
        mMaterialDialog.setTitle(title);
        View view = LayoutInflater.from(CheckoutActivity.this).inflate(R.layout.dialog_list, null);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        DialogProductAdapter adapter = new DialogProductAdapter(CheckoutActivity.this, beans,
            mImageLoader);
        listView.setAdapter(adapter);
        mMaterialDialog.setContentView(view);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
    }

    /**
     * send Request And Go To Next Payment Module
     */
    private void sendRequestAndGoToNextPaymentModule() {
        mGATrackAddressToPaymentTimeEnable = true;
        mGATrackAddressToPaymentTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        shippingAddress = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .getTopAddress();
        billingAddress = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .getBootomAddress();
        pickUpAddress = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .getmPickUpAddress();
        isBillAddressChecked = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .isBillAddressChecked();
        isPickInStoreChecked = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .isPickUpInStoreChecked();
        int shippingCode = ((CheckoutDefaultAddressFragment) checkoutDefaultAddressFragment)
            .curentClickShipping;
        setButtonEnable(false);
        mDialog = JViewUtils.showProgressDialog(CheckoutActivity.this);
        mCheckoutDao.saveBilling(
            WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getSessionKey(),
            shippingAddress, billingAddress, shippingCode, isBillAddressChecked);
    }

    public boolean isJustLoggedin(){
        return isJustLoggedin;
    }

    public void justLoggedin(boolean isLogin){
        isJustLoggedin = isLogin;
    }

    public void payPalPayment(String orderNumber, String price, String unit, String productName,
        String shippingFee) {
        mPaypalHelper.startPaypalPayment(this, price, unit, productName, orderNumber);
    }

    public void selectedAddressTrack() {
//        try {
//            FirebaseEventUtils.getInstance().customizedAddAddressInfo(this);
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
    }

    public void gaTrackerSaveShipping(String state) {
        try {

            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                "Save Address",
                state,
                Long.valueOf(WhiteLabelApplication.getAppConfiguration().getUser().getId()));

        } catch (Exception ex) {
            ex.getStackTrace();
        }

    }

    /**
     * validate Datas Of NewAddressFragment or editAddressFragment
     *
     * @SVRParameters
     */
    private AddressParameter validateDatasOfNewAddressFragment(
        CheckoutShippingAddaddressFragment addNewAddressFragment,
        CheckoutShippingAddaddressFragment editAddressFragment) {

        AddressParameter params = new AddressParameter();
        if (addNewAddressFragment == null) {
            /**
             * AddNewAddress or EditAddress
             */
            addNewAddressFragment = editAddressFragment;
        }
        Editable etFirstname = addNewAddressFragment.etFirstname.getText();
        if (null == etFirstname || JDataUtils.isEmpty(etFirstname.toString().trim())) {
            addNewAddressFragment.tvFirstnameAnim
                .setText(getResources().getString(R.string.This_is_a_required_field));
            addNewAddressFragment.tvFirstnameAnim
                .setTextColor(getResources().getColor(R.color.red_common));
            return null;
        } else {
            params.setFirstname(etFirstname.toString());
        }

        Editable etLastname = addNewAddressFragment.etLastname.getText();
        if (null == etLastname || JDataUtils.isEmpty(etLastname.toString().trim())) {
            addNewAddressFragment.tvLastnameAnim
                .setText(getResources().getString(R.string.This_is_a_required_field));
            addNewAddressFragment.tvLastnameAnim
                .setTextColor(getResources().getColor(R.color.red_common));
            return null;
        } else {
            params.setLastname(etLastname.toString());
        }

        Editable etAddressLine1 = addNewAddressFragment.etAddressLine1.getText();
        if (null == etAddressLine1 || JDataUtils.isEmpty(etAddressLine1.toString().trim())) {

            addNewAddressFragment.tvAddressLine1Anim
                .setText(getResources().getString(R.string.This_is_a_required_field));
            addNewAddressFragment.tvAddressLine1Anim
                .setTextColor(getResources().getColor(R.color.red_common));
//            svrParameters.put("validation_notpass_reason", "address line 1 is required!");

            return null;
        } else {
            params.setStreet0(etAddressLine1.toString());
//            svrParameters.put("street[0]", etAddressLine1.toString());
        }

        Editable etAddressLine2 = addNewAddressFragment.etAddressLine2.getText();
//        svrParameters.put("street[1]", etAddressLine2.toString());
        params.setStreet1(etAddressLine2.toString());
        Editable etShippingCity = addNewAddressFragment.etShippingCity.getText();
        if (null == etShippingCity || JDataUtils.isEmpty(etShippingCity.toString().trim())) {
            addNewAddressFragment.tvCityAnim
                .setText(getResources().getString(R.string.This_is_a_required_field));
            addNewAddressFragment.tvCityAnim
                .setTextColor(getResources().getColor(R.color.red_common));
            return null;
        } else {
            params.setCity(etShippingCity.toString());
        }

        Editable etPhone = addNewAddressFragment.etPhone.getText();
        if (null == etPhone || JDataUtils.isEmpty(etPhone.toString().trim())) {

            addNewAddressFragment.tvPhone
                .setText(getResources().getString(R.string.This_is_a_required_field));
            addNewAddressFragment.tvPhone.setTextColor(getResources().getColor(R.color.red_common));
//            svrParameters.put("validation_notpass_reason", "telephone is required!");
            return null;
        } else {
            //svrParameters.put("telephone", etPhone.toString());
            params.setTelephone(etPhone.toString());
        }

        EditText etCountry = addNewAddressFragment.etShippingCountry;
        if (etCountry.getTag() == null || JDataUtils.isEmpty(etCountry.getTag().toString())) {
            return null;
        } else {
            params.setCountryId(etCountry.getTag().toString());
        }
        EditText etState = addNewAddressFragment.etShippingState;
        params.setRegion(etState.getText().toString());
        params.setRegionId(etState.getTag() == null ? "" : etState.getTag().toString());
        return params;

    }

    /**
     * override keyback event of device
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean updateService = false;
        if (keyCode == KeyEvent.KEYCODE_BACK && updateService) {
            Bundle mBundle = new Bundle();
            mBundle.putString("exitApp", "exitApp");//压入数据
            startNextActivity(mBundle, HomeActivity.class, true);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && isClick) {
            isClick = false;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isClick = true;
                }
            }, 800);
            goBackOption();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * use  molpay  xdk
     */
    private void goToMolpayPage(String orderId, String amount, String shippingFee, String phone,
        String
            channel, String productName) {
        order_id = orderId;
        GOUserEntity user = WhiteLabelApplication.getAppConfiguration()
            .getUserInfo(CheckoutActivity.this);
        HashMap<String, Object> paymentDetails = new HashMap<>();
        if (!TextUtils.isEmpty(amount)) {
            paymentDetails.put(MOLPayActivity.mp_amount, amount.replace(",", ""));
        }
        JLogUtils
            .i("Pconstants", "PConstants.MP_AMOUNT：" + Float.parseFloat(amount.replace(",", "")));
        paymentDetails
            .put(MOLPayActivity.mp_bill_name, user.getFirstName() + " " + user.getLastName());
        JLogUtils.i("Pconstants", "PConstants.MP_BILL_NAME：" + user.getFirstName() + " " +
            user.getLastName());
        paymentDetails.put(MOLPayActivity.mp_bill_email, user.getEmail());
        JLogUtils.i("Pconstants", "PConstants.MP_BILL_EMAIL：" + user.getEmail());
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, phone);
        JLogUtils.i("Pconstants", "PConstants.MP_BILL_MOBILE：" + phone);
        paymentDetails
            .put(MOLPayActivity.mp_bill_description, productName + ", Shipping Fee " + shippingFee);
        JLogUtils.i("Pconstants",
            "PConstants.MP_BILL_DESCRIPTION：" + productName + ", Shipping Fee " + shippingFee);
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
        JLogUtils.i("Pconstants", "PConstants.MP_CURRENCY：MYR");
        paymentDetails
            .put(MOLPayActivity.mp_channel, JDataUtils.isEmpty(channel) ? "multi" : channel);
        JLogUtils
            .i("Pconstants", "PConstants.MP_CHANNEL：" + (JDataUtils.isEmpty(channel) ? "multi" :
                channel));
        JLogUtils.i("Pconstants", "PConstants.MP_DEBUG_MODE：false");
        paymentDetails.put(MOLPayActivity.mp_channel_editing, true);
        JLogUtils.i("Pconstants", "PConstants.MP_EDITING_ENABLED：true");
        paymentDetails.put(MOLPayActivity.mp_editing_enabled, false);
        JLogUtils.i("Pconstants", "PConstants.MP_SECURE_MODE_ENABLED：false");
        Intent intent = new Intent(CheckoutActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE_LOGIN == requestCode) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(CheckoutActivity.this)) {
                JLogUtils.i("checkout-continue-option---->", "login back");
                startNextActivity(null, ShoppingCartActivity1.class, true);
            }
            return;
        }
    }

    public void startShoppingActivity() {
        Intent intent = new Intent(CheckoutActivity.this, ShoppingCartActivity1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void saveShoppingCartCount(int num) {
        GOUserEntity userEntity = WhiteLabelApplication.getAppConfiguration()
            .getUserInfo(CheckoutActivity.this);
        userEntity.setCartItemCount(num);
        WhiteLabelApplication.getAppConfiguration()
            .updateUserData(CheckoutActivity.this, userEntity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
        //TODO joyson may be use
    }

    @Override
    protected void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (mAccountDao != null) {
            mAccountDao.cancelHttpByTag(TAG);
        }
        if (mCheckoutDao != null) {
            mCheckoutDao.cancelHttpByTag(TAG);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        closeCheckoutPaymentDialog();
        super.onDestroy();
//        WhiteLabelApplication.getRefWatcher(this).watch(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }

    private static class DataHandler extends Handler {

        private final WeakReference<CheckoutActivity> mActivity;

        public DataHandler(CheckoutActivity activity) {
            mActivity = new WeakReference<CheckoutActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case MyAccountDao.REQUEST_ADDRESS_SAVE:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        SVRAddAddress address = (SVRAddAddress) msg.obj;
                        mActivity.get().addressParams.setAddressId(address.getId());
                        mActivity.get().mCheckoutDao.saveBilling(
                            WhiteLabelApplication.getAppConfiguration().getUserInfo(mActivity.get())
                                .getSessionKey(), mActivity.get().addressParams);
                    } else {
                        if (mActivity.get().mDialog != null) {
                            mActivity.get().mDialog.cancel();
                        }
                        String faildStr = (String) msg.obj;
                        mActivity.get().setButtonEnable(true);
                        if (faildStr != null && !JToolUtils.expireHandler(mActivity.get(), faildStr,
                            mActivity.get().REQUESTCODE_LOGIN)) {
                            CheckoutShippingAddaddressFragment addNewAddressFragment =
                                (CheckoutShippingAddaddressFragment) mActivity
                                    .get().getFragmentManager().findFragmentByTag(TAG_ADD_ADDRESS);
                            addNewAddressFragment.tvErrorMsg.setText(faildStr);
                            addNewAddressFragment.tvErrorMsg.setVisibility(View.VISIBLE);
                            addNewAddressFragment.tvErrorMsg.setFocusable(true);
                            addNewAddressFragment.tvErrorMsg.setFocusableInTouchMode(true);
                            addNewAddressFragment.tvErrorMsg.requestFocus();
                        }
                    }
                    break;
                case CheckoutDao.REQUEST_SAVEBILLING:
                    if (mActivity.get().mDialog != null) {
                        mActivity.get().mDialog.cancel();
                    }
                    if (msg.arg1 == CheckoutDao.RESPONSE_SUCCESS) {
                        mActivity.get().selectedAddressTrack();
                        mActivity.get().setButtonEnable(true);
                        SVRAppserviceSaveBillingEntity saveBillingEntity =
                            (SVRAppserviceSaveBillingEntity) msg.obj;
                        //handler sku
                        if (saveBillingEntity.getError_products() != null && saveBillingEntity
                            .getError_products().size() > 0) {
                            mActivity.get().showProductDialog(mActivity.get().errorProductTitle,
                                saveBillingEntity.getError_products());
                            return;
                        }
                        mActivity.get().gaTrackerSaveShipping(
                            ((CheckoutDefaultAddressFragment) mActivity
                                .get().checkoutDefaultAddressFragment).getPrimaryShipping()
                                .getRegion());
                        if (1 == saveBillingEntity.getSkip_payment()) {
                            /**
                             * grand total is 0 and redirect to review order fragment.
                             */
                            mActivity.get().skipPayment = 1;

                            //switch fragment and set params
                            mActivity.get().checkoutReviewFragment = new CheckoutReviewFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("paymentSaveReturnEntity", saveBillingEntity);
                            mActivity.get().checkoutReviewFragment.setArguments(bundle);
                            mActivity.get().fragmentTransaction = mActivity.get()
                                .getSupportFragmentManager().beginTransaction();
                            //change currentFragment(module)
                            mActivity.get().currentModule = FRAGMENT_REVIEW;
                            //change button text
                            mActivity.get().btnContinue.setText(
                                mActivity.get().getResources().getString(R.string.PLACE_MY_ORDER));
                            //record this fragment
                            mActivity.get().fragmentTransaction
                                .add(R.id.ll_checkout_body, mActivity.get().checkoutReviewFragment,
                                    TAG_REVIEW);
                            mActivity.get().hiddenAll(  mActivity.get().fragmentTransaction);
                            mActivity.get().fragmentTransaction
                                .show(mActivity.get().checkoutReviewFragment)
                                .commitAllowingStateLoss();
                        } else {
                            /**
                             * normal payment
                             */
                            mActivity.get().skipPayment = 0;
                            //switch fragment
                            mActivity.get().openPaymentFragment();
                        }
                        mActivity.get().switchMenu(MENU_PAYMENT);

                        mActivity.get().scrollViewBody.scrollTo(0, 0);
                        try {
                            String CustomerId = WhiteLabelApplication.getAppConfiguration()
                                .getUser().getId();
                            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                                "Save Address",
                                ((CheckoutDefaultAddressFragment) mActivity
                                    .get().checkoutDefaultAddressFragment).getPrimaryShipping()
                                    .getRegion(),
                                Long.valueOf(CustomerId));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String faildStr = (String) msg.obj;
                        mActivity.get().setButtonEnable(true);
                        if (faildStr != null && !JToolUtils.expireHandler(mActivity.get(), faildStr,
                            mActivity.get().REQUESTCODE_LOGIN)) {
                            JViewUtils.showMaterialDialog(mActivity.get(), "", faildStr, null);
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_EDIT_SAVE:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        mActivity.get().mCheckoutDao.saveBilling(
                            WhiteLabelApplication.getAppConfiguration().getUserInfo(mActivity.get())
                                .getSessionKey(), mActivity.get().addressParams);
                    } else {
                        if (mActivity.get().mDialog != null) {
                            mActivity.get().mDialog.cancel();
                        }
                        mActivity.get().setButtonEnable(true);
                        String faildStr = (String) msg.obj;
                        if (faildStr != null && !JToolUtils.expireHandler(mActivity.get(), faildStr,
                            mActivity.get().REQUESTCODE_LOGIN)) {
                            CheckoutShippingAddaddressFragment editAddressFragment =
                                (CheckoutShippingAddaddressFragment) mActivity
                                    .get().getFragmentManager()
                                    .findFragmentByTag("editAddressFragment");
                            editAddressFragment.tvErrorMsg.setText(faildStr);
                            editAddressFragment.tvErrorMsg.setVisibility(View.VISIBLE);
                            editAddressFragment.tvErrorMsg.setFocusable(true);
                            editAddressFragment.tvErrorMsg.setFocusableInTouchMode(true);
                            editAddressFragment.tvErrorMsg.requestFocus();
                        }
                    }
                    break;
                case CheckoutDao.REQUEST_PLACEORDER:
                    mActivity.get().setButtonEnable(true);
                    if (msg.arg1 == CheckoutDao.RESPONSE_SUCCESS) {
                        mActivity.get().closeCheckoutPaymentDialog();
                        mActivity.get().saveShoppingCartCount(0);
                        CheckoutReviewFragment reviewFragment = (CheckoutReviewFragment) mActivity
                            .get().getSupportFragmentManager().findFragmentByTag(TAG_REVIEW);
                        SVRAppserviceSaveOrderReturnEntity saveOrderReturnEntity =
                            (SVRAppserviceSaveOrderReturnEntity) msg.obj;
                        mActivity.get().order_id = saveOrderReturnEntity.getLastrealorderid();
                        String grandTotal = reviewFragment.tvGrandTotal.getText().toString();
                        String shippingFee = reviewFragment.tvShippingfee.getText().toString();
                        mActivity.get().paymentSaveReturnEntity = reviewFragment
                            .getPaymentSaveReturnEntity();
                        //getResources().getString(R.string.payment_method_ONLINE_BANKING)
                        shippingFee = shippingFee.substring(3, shippingFee.length());
                        if (mActivity.get().paymethodType
                            .equals(CheckoutPaymentFragment.ONLINEPAYMENT)) {
                            /**
                             * Online Banking
                             */
//                            grandTotal = grandTotal.substring(3, grandTotal.length());
                            String phone = reviewFragment.address.getTelephone();
                            String channelCode = mActivity.get()
                                .transferChannelCode(mActivity.get().bank);
                            if (channelCode == null) {
                                reviewFragment.tvErrorMsg.setText(mActivity.get().getResources()
                                    .getString(R.string.Payment_channel_is_incorrect));
                                reviewFragment.tvErrorMsg.setVisibility(View.VISIBLE);
                            } else {
                                mActivity.get()
                                    .goToMolpayPage(saveOrderReturnEntity.getLastrealorderid(),
                                        saveOrderReturnEntity.getAmount(), shippingFee, phone,
                                        channelCode, reviewFragment.productName);
                            }
                        } else if (mActivity.get().paymethodType
                            .equals(CheckoutPaymentFragment.PAYPAL)) {
                            mActivity.get()
                                .payPalPayment(saveOrderReturnEntity.getLastrealorderid(),
                                    saveOrderReturnEntity.getAmount(),
                                    saveOrderReturnEntity.getUnit(), reviewFragment.productName,
                                    shippingFee);
                        } else {
                            //offline payment
                            if (mActivity.get().paymethodType
                                .equals(CheckoutPaymentFragment.OFFLINEPAYMENT)) {
                                mActivity.get()
                                    .goToPaymentSuccess(saveOrderReturnEntity.getLastrealorderid(),
                                        reviewFragment.payment_type, grandTotal, shippingFee,
                                        mActivity.get().html);
                            } else if (mActivity.get().paymethodType
                                .equals(CheckoutPaymentFragment.CODPAYMENT)) {
                                mActivity.get()
                                    .goToPaymentSuccess(saveOrderReturnEntity.getLastrealorderid(),
                                        reviewFragment.payment_type, grandTotal, shippingFee,
                                        saveOrderReturnEntity.getCashondeliveryContent());
                            } else {
//                          * Credit Card
                                mActivity.get().goToPaymentRedirectPage(
                                    saveOrderReturnEntity.getLastrealorderid(),
                                    reviewFragment.payment_type, grandTotal, shippingFee,
                                    saveOrderReturnEntity.getPaymentUrl());
                            }
                        }
                        mActivity.get().gaTrackerPlaceOrder();
                    } else {
                        String errorStr = (String) msg.obj;
                        mActivity.get().closeCheckoutPaymentDialog();
                        if (errorStr != null && !JToolUtils.expireHandler(mActivity.get(), errorStr,
                            mActivity.get().REQUESTCODE_LOGIN)) {
                            JViewUtils.showMaterialDialog(mActivity.get(), "", errorStr, null);
                        }
                    }
                    break;
                case CheckoutDao.REQUEST_CHANGEORDERSTATUS:
                    mActivity.get().closeCheckoutPaymentDialog();
                    if (msg.arg1 == CheckoutDao.RESPONSE_SUCCESS) {
                        Bundle bundle_success = new Bundle();
                        bundle_success.putLong("mGATrackTimeStart",
                            mActivity.get().mGATrackPlaceOrderToResultTimeStart);
                        bundle_success.putString("payment_status", "1");
                        bundle_success.putString("lastrealorderid", mActivity.get().order_id);
                        bundle_success.putString("grand_total", mActivity.get().amount);
                        bundle_success.putString("shipping_fee", mActivity.get().shippingFee);
                        bundle_success.putSerializable("paymentSaveReturnEntity",
                            mActivity.get().paymentSaveReturnEntity);
                        bundle_success.putInt("fromType", mActivity.get().fromType);
                        bundle_success.putInt("paymentMethod",
                            CheckoutPaymentRedirectActivity.PAYMENT_PALPAY);
                        if (mActivity.get().mDiscountBean != null) {
                            bundle_success
                                .putSerializable("discountBean", mActivity.get().mDiscountBean);
                        }
                        mActivity.get().startNextActivity(bundle_success,
                            CheckoutPaymentRedirectActivity.class, true);
                    } else {
                        String errorMsg = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorMsg,
                            mActivity.get().REQUESTCODE_LOGIN)) {
                            Bundle bundle_failuer = new Bundle();
                            bundle_failuer.putString("payment_status", "0");
                            bundle_failuer.putString("errorMsg", errorMsg);
                            bundle_failuer.putString("orderNumber", mActivity.get().order_id);
                            if (mActivity.get().mDiscountBean != null) {
                                bundle_failuer
                                    .putSerializable("discountBean", mActivity.get().mDiscountBean);
                            }
                            bundle_failuer.putInt("fromType", mActivity.get().fromType);
                            mActivity.get().startNextActivity(bundle_failuer,
                                CheckoutPaymentStatusActivity.class, true);
                        }
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                case MyAccountDao.ERROR:
                case CheckoutDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mActivity.get());
//                    if(msg.arg1== ProductDao.REQUEST_CHECKVERSION) {
//                        mActivity.get().PlaceMyOrder();
//                    } else
                    if (msg.arg1 == MyAccountDao.REQUEST_ADDRESS_SAVE || msg.arg1 == CheckoutDao
                        .REQUEST_SAVEBILLING || msg.arg1 == MyAccountDao.REQUEST_EDIT_SAVE || msg
                        .arg1 == CheckoutDao.REQUEST_CHANGEORDERSTATUS) {
                        if (mActivity.get().mDialog != null) {
                            mActivity.get().mDialog.cancel();
                        }
                        mActivity.get().setButtonEnable(true);
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    } else if (msg.arg1 == CheckoutDao.REQUEST_PLACEORDER) {
                        mActivity.get().closeCheckoutPaymentDialog();
                        mActivity.get().setButtonEnable(true);
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    } else if (msg.arg1 == ShoppingCarDao.REQUEST_RECOVERORDER) {
                        mActivity.get().closeCheckoutPaymentDialog();
                        mActivity.get().startShoppingActivity();
                    }
                    break;
                case ShoppingCarDao.REQUEST_RECOVERORDER:
                    if (mActivity.get().mDialog != null) {
                        mActivity.get().mDialog.cancel();
                    }
                    mActivity.get().closeCheckoutPaymentDialog();
                    mActivity.get().startShoppingActivity();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void finish(){
        super.finish();

        exitAnimation();
    }

}

package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whitelabel.app.*;
import com.whitelabel.app.ui.checkout.CheckoutActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.adapter.WheelPickerAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.CheckoutDao;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.model.CheckoutGetPaymentListEntity;
import com.whitelabel.app.model.CheckoutPaymentSaveReturnEntity;
import com.whitelabel.app.model.PaymentMethodModel;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.MaterialDialog;
import com.whitelabel.app.widget.wheel.OnWheelChangedListener;
import com.whitelabel.app.widget.wheel.WheelView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutPaymentFragment extends com.whitelabel.app.BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private CheckoutActivity checkoutActivity;
    private PopupWindow popupWindowPaymentType;
    private WebView tvHtml;
    private View view;
    private TextView tvPaymentMethod, ctv_payment_method_lab;
    private CustomButtomLineRelativeLayout ll_payment_cardnumber, rl_card_name, rl_card_type;
//            , rl_issuer_bank;
    private View view_avc_line, view_expiration_date_line;
    /**
     * EditText and Animation TextView define
     */
    public TextView tvCreditCardNumberAnim, tvCVCAnim, tvNameoncardAnim;
    public EditText etCreditCardNumber, etCVC, etNameoncard;
    private String code;
    private String html;
    private String bank;
    /**
     * TextView and selector ImageView define;
     */
    public TextView tvOnlinebankHint, tvCardtypeHint, tvCardType, tvIssuerbankHint, tvIssuerBank, tvIssuerCountry, tvExpirationDateHint, tvSelectExpirationDate, tvOnlineBankingPayWith, tvOnlinebankingBlankbottom;
    private ImageView arrowCardType, arrowSelectExpirationDate;
//    arrowSelectOnlineBankingPayWith,arrowIssuerbank, arrowIssuerCountry;
    private ImageView tvPaymentMethodArrow, clear_card_number, clear_card_cvc, clear_card_name;
    private View view_paymentmethod_line;
    public LinearLayout llPaymentMethodOnlineBanking;
    public LinearLayout llPaymentMethodCreditCardBody, llPaymentMethod;
    //    public ScrollView svPaymentMethodCreditCard;
    public TextView tvErrorMsg;
    private WheelPickerConfigEntity configEntity;
    private WheelPickerConfigEntity configEntity2;
    private WebView wvHtml;
    private final int REQUESTCODE_LOGIN = 1000;
    private OtherDao offlineDao;
    private Dialog mDialog;
    public final static String ONLINEPAYMENT = "online";
    public final static String CARDPAYMENT = "ccsave";
    public final static String OFFLINEPAYMENT = "offline";
    public final static String CODPAYMENT = "cod";
    public final static  String PAYPAL="paypal_standard";
    private final static String CURRENT_YEAR = "2017";
    private String paymentType="";
    private String molpayType;
    /*
     *0 means online banking; 1 means credit card
     */
    private int currentYear;
    private String  codDialogTitle;
    private CheckoutDao mCheckoutDao;
    private ImageView mPaymentListImage;
    private ImageLoader  mImageLoader;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checkoutActivity = (CheckoutActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getPaymengStr(List<PaymentMethodModel> method, String paymentType) {
        String likeStr = "";
        switch (paymentType){
            case CARDPAYMENT:
                likeStr = "Credit";
                break;
            case ONLINEPAYMENT:
                likeStr = "Online";
            break;
            case OFFLINEPAYMENT:
                if (paymentListEntity.getBanktransfer() != null) {
                    return paymentListEntity.getBanktransfer().getTitle();
                }
                break;
            case CODPAYMENT:
                if (paymentListEntity.getCashondelivery() != null) {
                    return paymentListEntity.getCashondelivery().getTitle();
                }
                break;
        }
        for (int i = 0; i < method.size(); i++) {
            if (method.get(i).getLabel().contains(likeStr)) {
                return method.get(i).getLabel();
            }
        }
        return method.get(0).getLabel();
    }


    public void showProductDialog() {
        // Kindly update your cart
        final MaterialDialog mMaterialDialog = new MaterialDialog(checkoutActivity);
        mMaterialDialog.setTitle(codDialogTitle);
        View view = LayoutInflater.from(checkoutActivity).inflate(R.layout.dialog_html_text, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_error_msg);
        if (!TextUtils.isEmpty(paymentListEntity.getCashondelivery().getContent_error())) {
            textView.setText(Html.fromHtml(paymentListEntity.getCashondelivery().getContent_error()));
        }
        mMaterialDialog.setContentView(view);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }
//    private View vLine;
//    private String brainTreeClientToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkout_payment, container, false);
        String addressError = getResources().getString(R.string.cod_error_address);
        tvHtml = (WebView) view.findViewById(R.id.tv_html);
        wvHtml = (WebView) view.findViewById(R.id.wv_html);
        initWebView();
        mImageLoader=new ImageLoader(getActivity());
        ll_payment_cardnumber = (CustomButtomLineRelativeLayout) view.findViewById(R.id.ll_payment_cardnumber);
        rl_card_name = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_card_name);
        rl_card_type = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_card_type);
//        rl_issuer_bank = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_issuer_bank);
        tvOnlinebankHint = (TextView) view.findViewById(R.id.tv_onlinebank_hint);
        view_avc_line = view.findViewById(R.id.view_avc_line);
        view_expiration_date_line = view.findViewById(R.id.view_expiration_date_line);
        view_paymentmethod_line = view.findViewById(R.id.view_paymentmethod_line);
//        view_payment_online_line = view.findViewById(R.id.view_payment_online_line);
        ctv_payment_method_lab = (TextView) view.findViewById(R.id.ctv_payment_method_lab);
        ctv_payment_method_lab.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        tvPaymentMethod = (TextView) view.findViewById(R.id.tv_checkout_payment_method);
        tvPaymentMethodArrow = (ImageView) view.findViewById(R.id.tv_checkout_payment_method_arrow);
        llPaymentMethod = (LinearLayout) view.findViewById(R.id.ll_checkout_payment_method);
        tvOnlinebankingBlankbottom = (TextView) view.findViewById(R.id.tv_checkout_payment_onlinebanking_blankbottom);
        TextView tvForpopLoaction = (TextView) checkoutActivity.findViewById(R.id.tv_checkout_for_payment_pop_loaction);
        llPaymentMethodOnlineBanking = (LinearLayout) view.findViewById(R.id.ll_checkout_payment_onlinebanking);
        llPaymentMethodCreditCardBody = (LinearLayout) view.findViewById(R.id.ll_checkout_payment_creditcard_body);
        ImageView ivSelectI = (ImageView) view.findViewById(R.id.iv_checkout_payment_select_i);
        ivSelectI.setOnClickListener(this);
        tvCreditCardNumberAnim = (TextView) view.findViewById(R.id.tv_checkout_payment_creditcardnumber_anim);
        tvCreditCardNumberAnim.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        etCreditCardNumber = (EditText) view.findViewById(R.id.et_checkout_payment_creditcardnumber);
        etCreditCardNumber.setOnFocusChangeListener(this);
        etCreditCardNumber.addTextChangedListener(new CreditCartNumberTextWatcher());
        tvCVCAnim = (TextView) view.findViewById(R.id.tv_checkout_payment_CVC_anim);
        etCVC = (EditText) view.findViewById(R.id.et_checkout_payment_CVC);
        etCVC.setOnFocusChangeListener(this);
        tvNameoncardAnim = (TextView) view.findViewById(R.id.tv_checkout_payment_nameoncard_anim);
        tvNameoncardAnim.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        etNameoncard = (EditText) view.findViewById(R.id.et_checkout_payment_nameoncard);
        etNameoncard.setOnFocusChangeListener(this);
        tvCardtypeHint = (TextView) view.findViewById(R.id.tv_checkout_payment_cardtype_hint);
        tvCardType = (TextView) view.findViewById(R.id.tv_checkout_payment_cardtype);
        arrowCardType = (ImageView) view.findViewById(R.id.arrow_checkout_payment_select_cardtype);
        view.findViewById(R.id.ll_checkout_payment_cardtype).setOnClickListener(this);
        tvIssuerbankHint = (TextView) view.findViewById(R.id.tv_checkout_payment_issuerbank_hint);
        tvIssuerBank = (TextView) view.findViewById(R.id.tv_checkout_payment_issuerbank);
//        arrowIssuerbank = (ImageView) view.findViewById(R.id.arrow_checkout_payment_select_issuerbank);
        //arrowIssuerbank.setOnClickListener(this);
        view.findViewById(R.id.ll_checkout_payment_issuerbank).setOnClickListener(this);
        tvExpirationDateHint = (TextView) view.findViewById(R.id.tv_checkout_payment_expiration_date_hint);
        tvSelectExpirationDate = (TextView) view.findViewById(R.id.tv_checkout_payment_expiration_date);
        arrowSelectExpirationDate = (ImageView) view.findViewById(R.id.arrow_checkout_payment_select_expiration_date);
        view.findViewById(R.id.ll_checkout_payment_expire_date).setOnClickListener(this);
        tvOnlineBankingPayWith = (TextView) view.findViewById(R.id.tv_checkout_payment_onlinebanking_paywith);
//        arrowSelectOnlineBankingPayWith = (ImageView) view.findViewById(R.id.arrow_checkout_payment_onlinebanking_paywith);
        view.findViewById(R.id.ll_checkout_payment_onlinebanking).setOnClickListener(this);
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_checkout_errormsg_payment);
        llPaymentMethod.setFocusable(true);
        llPaymentMethod.setFocusableInTouchMode(true);
        llPaymentMethod.requestFocus();
        clear_card_number = (ImageView) view.findViewById(R.id.clear_card_number);
        mPaymentListImage = (ImageView) view.findViewById(R.id.paymentlist_image);
        clear_card_number.setOnClickListener(this);
        clear_card_name = (ImageView) view.findViewById(R.id.clear_card_name);
        clear_card_name.setOnClickListener(this);
        clear_card_cvc = (ImageView) view.findViewById(R.id.clear_card_cvc);
        clear_card_cvc.setOnClickListener(this);
        etCreditCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && etCreditCardNumber.isFocused()) {
                    clear_card_number.setVisibility(View.VISIBLE);
                } else {
                    clear_card_number.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etCVC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && etCVC.isFocused()) {
                    clear_card_cvc.setVisibility(View.VISIBLE);
                } else {
                    clear_card_cvc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etNameoncard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && etNameoncard.isFocused()) {
                    clear_card_name.setVisibility(View.VISIBLE);
                } else {
                    clear_card_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void initWebView() {
        wvHtml.getSettings().setJavaScriptEnabled(true);
        tvHtml.setWebViewClient(client);
        wvHtml.setWebViewClient(client);
    }

    private WebViewClient client = new WebViewClient() {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception ex) {
                ex.getStackTrace();
            }
            return true;
        }
    };

    private CheckoutGetPaymentListEntity paymentListEntity;
    private DataHandler mHandler;

    private static class DataHandler extends Handler {
        private final WeakReference<CheckoutActivity> mActivity;
        private final WeakReference<CheckoutPaymentFragment> mFragment;

        public DataHandler(CheckoutActivity checkoutActivity, CheckoutPaymentFragment fragment) {
            mActivity = new WeakReference<>(checkoutActivity);
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            final CheckoutPaymentFragment fragment = mFragment.get();
            switch (msg.what) {
                case OtherDao.RQEUST_PAYMENT_METHOD:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    if (msg.arg1 == OtherDao.RESPONSE_SUCCESS) {
                        fragment.paymentListEntity = (CheckoutGetPaymentListEntity) msg.obj;
//                        fragment.brainTreeClientToken = fragment.paymentListEntity.getBraintreetoken();
                        fragment.initDatasWithWebServiceReturn(fragment.paymentListEntity);
                        if (fragment.checkoutActivity.mGATrackAddressToPaymentTimeEnable) {
                            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                                    GaTrackHelper.GA_TIME_CATEGORY_CHECKOUT,
                                    fragment.checkoutActivity.mGATrackAddressToPaymentTimeStart,
                                    "Checkout - Payment"
                            );
                            fragment.checkoutActivity.mGATrackAddressToPaymentTimeEnable = false;
                        }
                    } else {
                        String errorMsg = String.valueOf(msg.obj);
                        if (!JToolUtils.expireHandler(mActivity.get(), errorMsg, fragment.REQUESTCODE_LOGIN)) {
                            Toast.makeText(mActivity.get(), errorMsg, Toast.LENGTH_SHORT).show();
                            JLogUtils.i("checkoutpayment-sendRequestToGetPaymentList", errorMsg);
                        }
                    }
                    break;
                case CheckoutDao.REQUEST_SAVEPAYMENT:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    mActivity.get().setButtonEnable(true);
                    if (msg.arg1 == CheckoutDao.RESPONSE_SUCCESS) {
                        fragment.gaTrackerSavePayment();
                        fragment.tvErrorMsg.setVisibility(View.GONE);
                        CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity = (CheckoutPaymentSaveReturnEntity) msg.obj;
                        String moplayType = fragment.molpayType;
//                        fragment.savePaymentTrack();
                        mActivity.get().switReviewFragment(moplayType, paymentSaveReturnEntity, fragment.code, fragment.html, fragment.paymentType, fragment.bank);
                    } else {
                        if (mActivity.get() != null && fragment.isAdded()) {
                            String errorMsg = (String) msg.obj;
                            if (!JToolUtils.expireHandler(mActivity.get(), errorMsg, fragment.REQUESTCODE_LOGIN)) {
//                                if (fragment.paymentType == CODPAYMENT) {
                                JViewUtils.showMaterialDialog(mActivity.get(), "", "" + errorMsg, null);
//
                            }
                        }
                    }
                    break;
                case CheckoutDao.REQUEST_ERROR:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    mActivity.get().setButtonEnable(true);
                    if (mActivity.get() != null && fragment.isAdded()) {

                        RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mActivity.get());
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    }
                    break;
                case OtherDao.REQUEST_ERROR:
                    if (mActivity.get() != null && fragment.isAdded()) {
                        RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mActivity.get());
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    }
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    mActivity.get().btnContinue.setEnabled(true);
                    mActivity.get().btnContinue.setBackgroundResource(R.drawable.big_button_style_config);

                    break;
            }
            super.handleMessage(msg);
        }


    }

//    private void savePaymentTrack() {
//        try {
//            FirebaseEventUtils.getInstance().ecommerceAddPaymentInfo(getActivity());
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new DataHandler(checkoutActivity, this);
        offlineDao = new OtherDao(checkoutActivity.TAG, mHandler);
        mCheckoutDao = new CheckoutDao(checkoutActivity.TAG, mHandler);
        initDatePickerCallBack();
        initExipeDate();
//        sendRequestToGetPaymentList(true);

//        maxError = checkoutActivity.getResources().getString(R.string.cod_error_max);
//        minError = checkoutActivity.getResources().getString(R.string.code_error_min);
//        stateError1 = checkoutActivity.getResources().getString(R.string.code_error_state1);
//        stateError2 = checkoutActivity.getResources().getString(R.string.code_error_state2);
//        stateProduct = checkoutActivity.getResources().getString(R.string.code_error_product);
//        productError = getResources().getString(R.string.code_error_product);
//        codDialogTitle = getResources().getString(R.string.dialog_cod_hint);
        view.setBackgroundColor(getResources().getColor(R.color.whiteFFFFFF));
        goneView();

    }


    public void goneView() {
//        view_paymentmethod_line.setVisibility(View.INVISIBLE);
//        llPaymentMethod.setVisibility(View.INVISIBLE);
//        llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
        llPaymentMethodCreditCardBody.setVisibility(View.INVISIBLE);
        tvOnlinebankingBlankbottom.setVisibility(View.GONE);
        tvHtml.setVisibility(View.GONE);
        wvHtml.setVisibility(View.GONE);
        tvErrorMsg.setText("");
    }


    public void initExipeDate() {

        WheelPickerEntity wwDateOld1 = new WheelPickerEntity();
        wwDateOld1.setDisplay(currentYear + "");
        wwDateOld1.setValue(currentYear + "");

        /**
         * If user has select a year, then we need to keep his last select.
         */
        if (!TextUtils.isEmpty(expirationDate)) {
            String year = expirationDate.split("-")[0];
            for (int i = 0; i < list_date_wheel1.size(); i++) {
                if (list_date_wheel1.get(i).getDisplay().equals(year)) {
                    wwDateOld1.setIndex(i);
                    configEntity.setIndex(i);
                }
            }
        }
        configEntity.setOldValue(wwDateOld1);
        WheelPickerEntity ww2 = new WheelPickerEntity();
        ww2.setDisplay("01");
        ww2.setValue("01");
        /**
         * If user has select a year, then we need to keep his last select.
         */
        if (!TextUtils.isEmpty(expirationDate)) {
            String month = expirationDate.split("-")[1];
            for (int i = 0; i < list_date_wheel2.size(); i++) {
                if (list_date_wheel2.get(i).getDisplay().equals(month)) {
                    ww2.setIndex(i);
                    configEntity2.setIndex(i);
                }
            }
        }
        configEntity2.setOldValue(ww2);

        ///////////////////////////////////////Initialize Date Picker end////////////////////////////////////////////
        initWheelPickerTwoDialog(checkoutActivity, configEntity, configEntity2);

    }

    ArrayList<WheelPickerEntity> list_date_wheel2;
    ArrayList<WheelPickerEntity> list_date_wheel1;

    private void initDatePickerCallBack() {
        list_date_wheel2 = new ArrayList<WheelPickerEntity>();
        for (int i = 1; i <= 12; i++) {
            WheelPickerEntity w = new WheelPickerEntity();
            w.setDisplay(i < 10 ? ("0" + i) : ("" + i));
            w.setValue(i < 10 ? ("0" + i) : ("" + i));
            list_date_wheel2.add(w);
        }

        list_date_wheel1 = new ArrayList<WheelPickerEntity>();

        for (int i = 0; i < 70; i++) {
            WheelPickerEntity w = new WheelPickerEntity();
            w.setDisplay(currentYear + i + "");
            w.setValue(currentYear + i + "");
            list_date_wheel1.add(w);
        }

        WheelPickerCallback wheelCallbackOfMonth = new WheelPickerCallback() {

            @Override
            public void onCancel() {
                CustomButtomLineRelativeLayout.setBottomLineActive(view_expiration_date_line, false);
                AnimUtil.rotateArrow(arrowSelectExpirationDate, false);
                tvExpirationDateHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                tvExpirationDateHint.setTextColor(JToolUtils.getColor(R.color.label_saved));

                CustomButtomLineRelativeLayout.setBottomLineActive(view_expiration_date_line, false);
                AnimUtil.rotateArrow(arrowSelectExpirationDate, false);
                String date = tvSelectExpirationDate.getText().toString();

                if (newValue.getDisplay() == null) {
                    if (TextUtils.isEmpty(expirationDate) || date.split("-").length < 2) {
                        if (oldValue.getDisplay().trim().length() < 4) {
                            expirationDate = expirationDate + "-" + CURRENT_YEAR;
                        } else {
                            expirationDate = oldValue.getDisplay() + "-" + expirationDate;
                        }
                        tvSelectExpirationDate.setText(expirationDate);

                    } else {
                        expirationDate += "-" + date.split("-")[1];
                        tvSelectExpirationDate.setText(expirationDate);
                    }

                } else {
                    expirationDate += "-" + newValue.getDisplay();
                    tvSelectExpirationDate.setText(expirationDate);

                }

                tvExpirationDateHint.setText(getResources().getString(R.string.Expiration_Date));
                tvExpirationDateHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
            }
        };

        WheelPickerCallback wheelCallbackOfYear = new WheelPickerCallback() {
            @Override
            public void onCancel() {
                JLogUtils.i("Russell", "onCancel()");
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onDone() -- oldValue => " + oldValue + "  newValue => " + newValue);

                String date = tvSelectExpirationDate.getText().toString();
                JLogUtils.i("Allen", "date==" + date);

                if (newValue.getDisplay() == null) {
                    if (TextUtils.isEmpty(expirationDate)) {
                        expirationDate = oldValue.getDisplay();
                        if (expirationDate.trim().length() > 2) {
                            expirationDate = "01";
                        }
                        JLogUtils.i("Allen", "expirationDate1==" + expirationDate);
                    } else {
                        expirationDate = date.split("-")[0];
                        JLogUtils.i("Allen", "expirationDate2==" + expirationDate);

                    }
                } else {
                    //birthday.setText(newValue.getDisplay());
                    expirationDate = newValue.getDisplay();
                    JLogUtils.i("Allen", "expirationDate3==" + expirationDate);

                }
            }
        };

        configEntity2 = new WheelPickerConfigEntity();
        configEntity = new WheelPickerConfigEntity();

        configEntity2.setArrayList(list_date_wheel1);
        configEntity.setArrayList(list_date_wheel2);

        configEntity2.setCallBack(wheelCallbackOfMonth);
        configEntity.setCallBack(wheelCallbackOfYear);

    }


    /**
     * TextView and ImageView click listener
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        ArrayList<WheelPickerEntity> list;
        WheelPickerEntity oldEntity;
        switch (v.getId()) {
            case R.id.clear_card_cvc:
                etCVC.setText("");
                break;
            case R.id.clear_card_name:
                etNameoncard.setText("");
                break;
            case R.id.clear_card_number:
                etCreditCardNumber.setText("");
                break;
            case R.id.iv_checkout_payment_select_i:
                JViewUtils.hideKeyboard(getActivity());
                String title = checkoutActivity.getString(R.string.checkout_payment_dialog_title);
                final MaterialDialog mMaterialDialog = new MaterialDialog(checkoutActivity);
                mMaterialDialog.setTitle(title);
                View view = LayoutInflater.from(checkoutActivity).inflate(R.layout.dialog_checkout_payment_select, null);
                mMaterialDialog.setContentView(view);
                mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
                mMaterialDialog.show();
                break;
            case R.id.ll_checkout_payment_expire_date:
                String expiration = tvSelectExpirationDate.getText().toString();
                if (!TextUtils.isEmpty(expiration)) {
                    String[] expirats = expiration.split("-");
                    if (expirats != null && expirats.length == 2) {
                        int year = Integer.parseInt(expirats[0]);
                        int month = Integer.parseInt(expirats[1]);
                        wvLeft.setCurrentItem(year - currentYear);
                        wvMiddle.setCurrentItem(month - 1);
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customDialog.show();
                    }
                },260);
                JViewUtils.cleanCurrentViewFocus(getActivity());
                tvExpirationDateHint.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                CustomButtomLineRelativeLayout.setBottomLineActive(view_expiration_date_line,true);
                AnimUtil.rotateArrow(arrowSelectExpirationDate,true);
                //createDialogPickerOfTwoColumn();
                break;
            default:
                break;
        }
    }

    private Dialog customDialog;
    private WheelView wvLeft;
    private WheelView wvMiddle;

    public void initWheelPickerTwoDialog(Context context, WheelPickerConfigEntity pickerConfigEntityLeft, WheelPickerConfigEntity pickerConfigEntityMiddle) {
        if (context == null || pickerConfigEntityLeft == null || pickerConfigEntityMiddle == null) {
            return;
        }
        if (pickerConfigEntityLeft.getCallBack() == null || pickerConfigEntityLeft.getArrayList() == null || pickerConfigEntityLeft.getOldValue() == null) {
            return;
        }
        pickerConfigEntityLeft.getCallBack().getOldValue().setIndex(pickerConfigEntityLeft.getOldValue().getIndex());
        pickerConfigEntityLeft.getCallBack().getOldValue().setValue(pickerConfigEntityLeft.getOldValue().getValue());
        pickerConfigEntityLeft.getCallBack().getOldValue().setDisplay(pickerConfigEntityLeft.getOldValue().getDisplay());

        pickerConfigEntityLeft.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntityLeft.getCallBack().getNewValue().setValue(null);
        pickerConfigEntityLeft.getCallBack().getNewValue().setDisplay(null);

        if (pickerConfigEntityMiddle.getCallBack() == null || pickerConfigEntityMiddle.getArrayList() == null || pickerConfigEntityMiddle.getOldValue() == null) {
            return;
        }
        pickerConfigEntityMiddle.getCallBack().getOldValue().setIndex(pickerConfigEntityMiddle.getOldValue().getIndex());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setValue(pickerConfigEntityMiddle.getOldValue().getValue());
        pickerConfigEntityMiddle.getCallBack().getOldValue().setDisplay(pickerConfigEntityMiddle.getOldValue().getDisplay());

        pickerConfigEntityMiddle.getCallBack().getNewValue().setIndex(-1);
        pickerConfigEntityMiddle.getCallBack().getNewValue().setValue(null);
        pickerConfigEntityMiddle.getCallBack().getNewValue().setDisplay(null);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wheelpicker_two, null);
        final RelativeLayout rlContainer = (RelativeLayout) dialogView.findViewById(R.id.rlContainer);
        final CustomTextView ctvCancel = (CustomTextView) dialogView.findViewById(R.id.ctvCancel);
        final CustomTextView ctvSet = (CustomTextView) dialogView.findViewById(R.id.ctvSet);
        ctvSet.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        wvLeft = (WheelView) dialogView.findViewById(R.id.wvLeft);
        wvMiddle = (WheelView) dialogView.findViewById(R.id.wvMiddle);

        WheelPickerAdapter wpaLeft = new WheelPickerAdapter(context, pickerConfigEntityLeft.getArrayList());
        wvLeft.setViewAdapter(wpaLeft);
        wvLeft.setVisibleItems(5);
        wvLeft.setCurrentItem(pickerConfigEntityLeft.getIndex());

        WheelPickerAdapter wpaMiddle = new WheelPickerAdapter(context, pickerConfigEntityMiddle.getArrayList());
        wvMiddle.setViewAdapter(wpaMiddle);
        wvMiddle.setVisibleItems(5);
        wvMiddle.setCurrentItem(pickerConfigEntityMiddle.getIndex());

        customDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        Window dialogWindow = customDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        rlContainer.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }

            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        ctvCancel.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onCancel();
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onCancel();
                }

            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        ctvSet.setOnClickListener(new View.OnClickListener() {
            private Dialog dialog;
            private WheelPickerCallback wheelPickerCallbackLeft;
            private WheelPickerCallback wheelPickerCallbackMiddle;
            private WheelPickerCallback wheelPickerCallbackRight;

            public View.OnClickListener init(Dialog d, WheelPickerCallback cl, WheelPickerCallback cm) {
                this.dialog = d;
                this.wheelPickerCallbackLeft = cl;
                this.wheelPickerCallbackMiddle = cm;
                return this;
            }

            @Override
            public void onClick(View view) {
                this.dialog.cancel();
                if (wheelPickerCallbackLeft != null) {
                    wheelPickerCallbackLeft.onDone(wheelPickerCallbackLeft.getOldValue(), wheelPickerCallbackLeft.getNewValue());
                }
                if (wheelPickerCallbackMiddle != null) {
                    wheelPickerCallbackMiddle.onDone(wheelPickerCallbackMiddle.getOldValue(), wheelPickerCallbackMiddle.getNewValue());
                }
                if (wheelPickerCallbackRight != null) {
                    wheelPickerCallbackRight.onDone(wheelPickerCallbackRight.getOldValue(), wheelPickerCallbackRight.getNewValue());
                }
            }
        }.init(customDialog, pickerConfigEntityLeft.getCallBack(), pickerConfigEntityMiddle.getCallBack()));
        wvLeft.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }

                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityLeft.getArrayList(), pickerConfigEntityLeft.getCallBack()));
        wvMiddle.addChangingListener(new OnWheelChangedListener() {
            private ArrayList<WheelPickerEntity> array;
            private WheelPickerCallback wheelPickerCallback;

            public OnWheelChangedListener init(ArrayList<WheelPickerEntity> a, WheelPickerCallback c) {
                this.array = a;
                this.wheelPickerCallback = c;
                return this;
            }

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (this.array != null && (newValue >= 0 && newValue < this.array.size()) && this.array.get(newValue) != null) {
                    if (wheelPickerCallback != null) {
                        final WheelPickerEntity newPickerEntity = array.get(newValue);
                        if (newPickerEntity != null) {
                            wheelPickerCallback.getNewValue().setIndex(newPickerEntity.getIndex());
                            wheelPickerCallback.getNewValue().setValue(newPickerEntity.getValue());
                            wheelPickerCallback.getNewValue().setDisplay(newPickerEntity.getDisplay());
                        }
                    }
                }
                if (wheelPickerCallback != null) {
                    wheelPickerCallback.onScrolling(wheelPickerCallback.getOldValue(), wheelPickerCallback.getNewValue());
                }
            }
        }.init(pickerConfigEntityMiddle.getArrayList(), pickerConfigEntityMiddle.getCallBack()));

        customDialog.setContentView(dialogView);

    }

    /**
     * init Credit Card Type Datas
     *
     * @param cctypes
     */
//    private void initCreditCardTypeDatas(HashMap<String, String> cctypes) {
//
//        ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
//        WheelPickerEntity entity;
//        List<String> list_tem = new ArrayList<String>();//used to record position
//
//        for (String str : cctypes.keySet()) {
//
//            list_tem.add(str);
//
//            entity = new WheelPickerEntity();
//            entity.setDisplay(cctypes.get(str));
//            entity.setValue(str);
//            list.add(entity);
//
//        }
//
//        WheelPickerEntity oldEntity = new WheelPickerEntity();
//        oldEntity.setDisplay(cctypes.get(list_tem.get(0)));
//        oldEntity.setValue(list_tem.get(0));
//
//        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//        configEntity.setArrayList(list);
//
//        final String cctype = tvCardType.getText().toString();
//        if (!JDataUtils.isEmpty(cctype)) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getDisplay().equalsIgnoreCase(cctype)) {
//                    oldEntity.setIndex(i);
//                    configEntity.setIndex(i);
//                }
//            }
//        }
//
//        configEntity.setOldValue(oldEntity);
//
//        configEntity.setCallBack(new WheelPickerCallback() {
//            @Override
//            public void onCancel() {
//                JLogUtils.i("Russell", "onCancel()");
//            }
//
//            @Override
//            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
//            }
//
//            @Override
//            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                if (newValue.getDisplay() == null) {
//                    if (JDataUtils.isEmpty(cctype)) {
//                        tvCardType.setText(oldValue.getDisplay());
//                        tvCardType.setHint(oldValue.getValue());
//                        tvCardtypeHint.setText("");
//                    }
//
//                } else {
//                    tvCardType.setText(newValue.getDisplay());
//                    tvCardType.setHint(newValue.getValue());
//                    tvCardtypeHint.setText("");
//                }
//
//            }
//        });
//        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
//    }

    /**
     * create datas for online banking pay with
     *
     * @return
     */
    private ArrayList<WheelPickerEntity> makeDatasForOnlineBankingPayWith() {

        ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();

        WheelPickerEntity entity1 = new WheelPickerEntity();
        entity1.setDisplay("Maybank2u");
        entity1.setValue("maybank2u.php");
        list.add(entity1);

        WheelPickerEntity entity2 = new WheelPickerEntity();
        entity2.setDisplay("Hong Leong Connect");
        entity2.setValue("hlb.php");
        list.add(entity2);

        WheelPickerEntity entity3 = new WheelPickerEntity();
        entity3.setDisplay("CIMB Clicks");
        entity3.setValue("cimb.php");
        list.add(entity3);

        WheelPickerEntity entity4 = new WheelPickerEntity();
        entity4.setDisplay("RHB Now");
        entity4.setValue("rhb.php");
        list.add(entity4);

        WheelPickerEntity entity5 = new WheelPickerEntity();
        entity5.setDisplay("AmOnline");
        entity5.setValue("amb.php");
        list.add(entity5);

        WheelPickerEntity entity6 = new WheelPickerEntity();
        entity6.setDisplay("FPX e-Payment Channel");
        entity6.setValue("fpx.php");
        list.add(entity6);

        return list;
    }

    /**
     * send Request To Get Issuer Bank List
     */
//    private void sendRequestToGetIssuerBankList() {
//        if(getActivity()==null){
//            return;
//        }
//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey());
//
//        SVRCheckoutPaymentIssuerBankList issuerBankListHandler = new SVRCheckoutPaymentIssuerBankList(checkoutActivity, parameters);
//
//        issuerBankListHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//
//                CheckoutPaymentIssuerBankListEntity issuerBankListEntity = (CheckoutPaymentIssuerBankListEntity) result;
//
//                ArrayList<WheelPickerEntity> list = null;
//                WheelPickerEntity oldEntity;
//
//                if (null != issuerBankListEntity.getBanks() && issuerBankListEntity.getBanks().length > 0) {
//
//                    /**
//                     * save issuerBankList to local file
//                     */
//                    SharedPreferences sharedPreferences = checkoutActivity.getSharedPreferences("issuerBankList", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("issuerBankList", new Gson().toJson(issuerBankListEntity.getBanks()));
//                    editor.commit();
//
//                    /**
//                     * set datas to dialogpicker
//                     */
//                    list = new ArrayList<WheelPickerEntity>();
//                    for (String str : issuerBankListEntity.getBanks()) {
//                        oldEntity = new WheelPickerEntity();
//                        oldEntity.setDisplay(str);
//                        oldEntity.setValue(str);
//                        list.add(oldEntity);
//                    }
//                }
//
//                oldEntity = new WheelPickerEntity();
//                oldEntity.setDisplay("AEON Credit Service (M) Sdn Bhd");
//                oldEntity.setValue("AEON Credit Service (M) Sdn Bhd");
//
//                createDialogPicker(list, oldEntity, tvIssuerBank);
//
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//
//            }
//        });
//
//    }
    private Dialog createDialog(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_checkout_payment_select, null);

        Dialog dialog = new Dialog(context, R.style.loading_dialog);

        dialog.setCancelable(true);

        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return dialog;
    }

//    private void closePopupWindow() {
//        if (popupWindowPaymentType != null && popupWindowPaymentType.isShowing()) {
//            popupWindowPaymentType.dismiss();
//        }
//    }

    /**
     * EditText focus listener
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_checkout_payment_creditcardnumber:
                if (hasFocus) {
                    ll_payment_cardnumber.setBottomLineActive(true);
                    tvCreditCardNumberAnim.startAnimation(getHintAnimation(tvCreditCardNumberAnim, checkoutActivity.getResources().getString(R.string.Credit_Card_Number)));
                    etCreditCardNumber.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//show card number
                    if (etCreditCardNumber.getText().length() != 0) {
                        clear_card_number.setVisibility(View.VISIBLE);
                    } else {
                        clear_card_number.setVisibility(View.GONE);
                    }
                    /*etCVC.setFocusable(true);
                    etCVC.setFocusableInTouchMode(true);*/
                } else {
                    ll_payment_cardnumber.setBottomLineActive(false);
                    if (JDataUtils.isStringBlank(etCreditCardNumber.getText())) {
                        tvCreditCardNumberAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvCreditCardNumberAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvCreditCardNumberAnim.setTextColor(getResources().getColor(R.color.label_saved));
                        //etCreditCardNumber.setInputType(InputType.TYPE_MASK_VARIATION);
                        etCreditCardNumber.setTransformationMethod(PasswordTransformationMethod.getInstance());//hide card number
                    }
                    clear_card_number.setVisibility(View.GONE);
                }
                break;
            case R.id.et_checkout_payment_CVC:
                if (hasFocus) {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_avc_line, true);
                    if (etCVC.getText().length() != 0) {
                        clear_card_cvc.setVisibility(View.VISIBLE);
                    } else {
                        clear_card_cvc.setVisibility(View.GONE);
                    }
                    tvCVCAnim.startAnimation(getHintAnimation(tvCVCAnim, checkoutActivity.getResources().getString(R.string.CVC_CVV2)));
                } else {
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_avc_line, false);
                    if (JDataUtils.isStringBlank(etCVC.getText())) {
                        tvCVCAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvCVCAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvCVCAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                    clear_card_cvc.setVisibility(View.GONE);
                }
                break;
            case R.id.et_checkout_payment_nameoncard:
                if (hasFocus) {
                    rl_card_name.setBottomLineActive(true);
                    if (etNameoncard.getText().length() != 0) {
                        clear_card_name.setVisibility(View.VISIBLE);
                    } else {
                        clear_card_name.setVisibility(View.GONE);
                    }
                    tvNameoncardAnim.startAnimation(getHintAnimation(tvNameoncardAnim, checkoutActivity.getResources().getString(R.string.Name_On_Card)));
                } else {
                    rl_card_name.setBottomLineActive(false);
                    if (JDataUtils.isStringBlank(etNameoncard.getText())) {
                        tvNameoncardAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvNameoncardAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvNameoncardAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                    clear_card_name.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private Animation getHintAnimation(final TextView tv, final String hintText) {

        Animation animation = AnimationUtils.loadAnimation(checkoutActivity, R.anim.anim_checkout_hint);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                    }
                }, 100);


                tv.setText(hintText);
                tv.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return animation;
    }

    /**
     * create a select dialog
     *
     * @param list      options
     * @param oldEntity
     * @param view      --container of select result
     */
    private void createDialogPicker(ArrayList<WheelPickerEntity> list, final WheelPickerEntity oldEntity, final TextView view) {
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                JLogUtils.i("Russell", "onCancel()");
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    view.setText(oldValue.getDisplay());
                    view.setHint(oldValue.getValue());

                    tvCardtypeHint.setText("");
                    tvIssuerbankHint.setText("");
                } else {
                    view.setText(newValue.getDisplay());
                    view.setHint(newValue.getValue());

                    tvCardtypeHint.setText("");
                    tvIssuerbankHint.setText("");
                }

            }
        });
        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
    }

    /**
     * create a time dialog picker of two columns
     */
    private String expirationDate = "";


    /**
     * send Request To Get Payment List
     */
//    public void sendRequestToGetPaymentList(boolean resetPaymentType) {
//        goneView();
////        if (resetPaymentType) {
//////            paymentType = CARDPAYMENT;
////        }
//        mDialog = JViewUtils.showProgressDialog(getActivity());
//        offlineDao.getPaymentList(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey());
//    }


    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
//        if(mCheckoutDao!=null){
//            mCheckoutDao.cancelHttpByTag();
//        }
        super.onDestroyView();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE_LOGIN == requestCode) {
            /**
             * If sesion-expiration occured during payment process,
             * turn to login page ,
             * then go back to shoppingcart page
             */
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(checkoutActivity)) {
                JLogUtils.i("checkout-payment-fragment---->", "login back");
                checkoutActivity.startNextActivity(null, ShoppingCartActivity1.class, true);
            }
        }
    }
    public void handlerPaymentType(List<PaymentMethodModel> method, String paymentType) {
         switch (paymentType){
             case CARDPAYMENT:
                 checkoutActivity.setButtonEnable(true);
                 llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
                 tvOnlinebankingBlankbottom.setVisibility(View.GONE);
                 llPaymentMethodCreditCardBody.setVisibility(View.VISIBLE);
                 tvHtml.setVisibility(View.GONE);
                 wvHtml.setVisibility(View.GONE);
                 tvPaymentMethod.setText(getPaymengStr(method, paymentType));
                 break;

             case ONLINEPAYMENT:
                 checkoutActivity.setButtonEnable(true);
                 llPaymentMethodOnlineBanking.setVisibility(View.VISIBLE);
                 llPaymentMethodCreditCardBody.setVisibility(View.INVISIBLE);
                 tvOnlinebankingBlankbottom.setVisibility(View.VISIBLE);
                 tvHtml.setVisibility(View.GONE);
                 wvHtml.setVisibility(View.GONE);
                 tvPaymentMethod.setText(getPaymengStr(method, paymentType));
                 break;

             case CODPAYMENT:
                 Boolean right = checkCODPayment();
                 if (!right) {
                     if (CheckoutPaymentFragment.this.paymentType == CODPAYMENT)
                         handlerPaymentType(method, CARDPAYMENT);
                     return;
                 }
                 tvPaymentMethod.setText("Cash On Delivery");
                 llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
                 tvOnlinebankingBlankbottom.setVisibility(View.GONE);
                 llPaymentMethodCreditCardBody.setVisibility(View.INVISIBLE);
                 if (paymentListEntity.getCashondelivery() != null && !TextUtils.isEmpty(paymentListEntity.getCashondelivery().getContent()) && !"null".equals(paymentListEntity.getCashondelivery().getContent())) {
                     wvHtml.setVisibility(View.VISIBLE);
                     String content = JToolUtils.replaceFont(paymentListEntity.getCashondelivery().getContent());
                     JToolUtils.webViewFont(WhiteLabelApplication.getInstance().getBaseContext(), wvHtml, content);
                     //wvHtml.loadDataWithBaseURL("file:///android_asset/html/", paymentListEntity.getCashondelivery().getContent(), "text/html", "utf-8", null);
                 }
                 tvHtml.setVisibility(View.GONE);
                 break;


             case OFFLINEPAYMENT:
                 if (paymentListEntity.getBanktransfer() != null) {
                     checkoutActivity.setButtonEnable(true);
                     llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
                     tvOnlinebankingBlankbottom.setVisibility(View.GONE);
                     llPaymentMethodCreditCardBody.setVisibility(View.INVISIBLE);
                     if (paymentListEntity.getBanktransfer() != null && !TextUtils.isEmpty(paymentListEntity.getBanktransfer().getContent()) && !"null".equals(paymentListEntity.getBanktransfer().getContent())) {
                         tvHtml.setVisibility(View.VISIBLE);
                         String content = JToolUtils.replaceFont(paymentListEntity.getBanktransfer().getContent());
                         JToolUtils.webViewFont(WhiteLabelApplication.getInstance().getBaseContext(), tvHtml, content);
                     }
                     wvHtml.setVisibility(View.GONE);
                     tvPaymentMethod.setText(getPaymengStr(method, paymentType));
                 } else {
                     handlerPaymentType(method, CARDPAYMENT);
                     return;
                 }
                 break;
             case PAYPAL:

                 llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
                 tvOnlinebankingBlankbottom.setVisibility(View.GONE);
                 llPaymentMethodCreditCardBody.setVisibility(View.INVISIBLE);
                 break;
         }

        CheckoutPaymentFragment.this.paymentType = paymentType;
    }

   private  WheelPickerCallback  paymentMethodCallback=  new WheelPickerCallback() {
        @Override
        public void onCancel() {
            AnimUtil.rotateArrow(tvPaymentMethodArrow,false);
            CustomButtomLineRelativeLayout.setRelativeBottomLineActive(view_paymentmethod_line, false);
            ctv_payment_method_lab.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        @Override
        public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
        }
        @Override
        public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            AnimUtil.rotateArrow(tvPaymentMethodArrow,false);
            CustomButtomLineRelativeLayout.setRelativeBottomLineActive(view_paymentmethod_line,false);
            ctv_payment_method_lab.setTextColor(JToolUtils.getColor(R.color.label_saved));
            if (newValue.getDisplay() == null) {
                return;
            }
            paymentType=newValue.getValue();
            if (newValue.getValue().equals(CARDPAYMENT)) {
                handlerPaymentType(methods, CARDPAYMENT);
            } else if(newValue.getValue().equals(PAYPAL)){
                handlerPaymentType(methods,PAYPAL);
            } else if (newValue.getDisplay().contains("Online")) {
                handlerPaymentType(methods, ONLINEPAYMENT);
            } else if ("banktransfer".equals(newValue.getValue())) {
                handlerPaymentType(methods, OFFLINEPAYMENT);
            } else if ("cashondelivery".equals(newValue.getValue())) {
                handlerPaymentType(methods, CODPAYMENT);
            }
            tvPaymentMethod.setText(newValue.getDisplay());
            tvPaymentMethod.setTag(newValue.getValue());
        }
    };
    private List<PaymentMethodModel> methods;
    /**
     * init  Datas With WebService Return
     *
     * @param paymentListEntity
     */
    private void initDatasWithWebServiceReturn(final CheckoutGetPaymentListEntity paymentListEntity) {
//        view.setBackgroundColor(getResources().getColor(R.color.greyE1E1E1));
//        paymentType=CARDPAYMENT;
        llPaymentMethod.setVisibility(View.VISIBLE);
        view_paymentmethod_line.setVisibility(View.VISIBLE);
//        llPaymentMethodCreditCardBody.setVisibility(View.VISIBLE);
        /////////////////////////method begin////////////////////////////
         methods = paymentListEntity.getMethods();
//        if (methods.size() == 1) {
//            String method = methods.get(0).getLabel();
//            /**
//             * If There is only one method, hide selector and don't permit to click
//             */
//            tvPaymentMethodArrow.setVisibility(View.GONE);
//            tvPaymentMethod.setText(method);
//
//            if (method.trim().startsWith("Online")) {
//                llPaymentMethodOnlineBanking.setVisibility(View.VISIBLE);
////                svPaymentMethodCreditCard.setVisibility(View.INVISIBLE);
//                tvOnlinebankingBlankbottom.setVisibility(View.VISIBLE);
//
//            } else if (method.trim().startsWith("Credit")) {
//                llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
////                svPaymentMethodCreditCard.setVisibility(View.VISIBLE);
//                tvOnlinebankingBlankbottom.setVisibility(View.GONE);
//                llPaymentMethodCreditCardBody.setVisibility(View.VISIBLE);
//                tvHtml.setVisibility(View.GONE);
//
//            } else if (paymentListEntity != null && paymentListEntity.getBanktransfer() != null && "banktransfer".equals(paymentListEntity.getBanktransfer().getCode())) {
//                llPaymentMethodOnlineBanking.setVisibility(View.INVISIBLE);
////                svPaymentMethodCreditCard.setVisibility(View.VISIBLE);
//                tvOnlinebankingBlankbottom.setVisibility(View.GONE);
//                llPaymentMethodCreditCardBody.setVisibility(View.VISIBLE);
//                tvHtml.setVisibility(View.VISIBLE);
//            } else {
//                JLogUtils.i("russell->payment-method", "neither online banking nor credit card");
//            }
//
//        } else {
            if (methods != null&&methods.size()>0) {
                 paymentType=methods.get(0).getCode();
                 tvPaymentMethod.setText(methods.get(0).getLabel());
                 tvPaymentMethod.setTag(methods.get(0).getCode());
                 handlerPaymentType(methods, paymentType);
            }

            llPaymentMethod.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
//                                                       //cleanCurrentViewFocus  为了使 EditText 失去焦点，以便自动执行onFocusChange()
//                                                       JViewUtils.cleanCurrentViewFocus(getActivity());
//                                                       final ArrayList<WheelPickerEntity> list;
//                                                       final WheelPickerEntity oldEntity;
//                                                       list = new ArrayList<WheelPickerEntity>();
//                                                       for (int i = 0; i < methods.size(); i++) {
//                                                           WheelPickerEntity entity = new WheelPickerEntity();
//                                                           entity.setDisplay(methods.get(i).getLabel());
//                                                           entity.setValue(methods.get(i).getCode());
//                                                           list.add(entity);
//                                                       }
//                                                       try {
//                                                           if (paymentListEntity.getBanktransfer() != null) {
//                                                               WheelPickerEntity entity = new WheelPickerEntity();
//                                                               entity.setDisplay(paymentListEntity.getBanktransfer().getTitle());
//                                                               entity.setValue(paymentListEntity.getBanktransfer().getCode());
//                                                               list.add(entity);
//                                                           }
//                                                           if (paymentListEntity.getCashondelivery() != null) {
//                                                               WheelPickerEntity entity = new WheelPickerEntity();
//                                                               entity.setDisplay(paymentListEntity.getCashondelivery().getTitle());
//                                                               entity.setValue(paymentListEntity.getCashondelivery().getCode());
//                                                               list.add(entity);
//                                                           }
//                                                       } catch (Exception ex) {
//                                                           ex.getStackTrace();
//                                                       }
//                                                       oldEntity = new WheelPickerEntity();
//                                                       oldEntity.setDisplay(methods.get(0).getLabel());
//                                                       oldEntity.setValue(methods.get(0).getCode());
//                                                       //改变底部line颜色
//                                                       CustomButtomLineRelativeLayout.setRelativeBottomLineActive(view_paymentmethod_line, true);
//                                                       AnimUtil.rotateArrow(tvPaymentMethodArrow, true);
//
//                                                       ctv_payment_method_lab.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
//
//                                                       final WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//
//                                                       final String payment = tvPaymentMethod.getText().toString();
//                                                       for (int i = 0; i < list.size(); i++) {
//                                                           if (list.get(i).getDisplay().equalsIgnoreCase(payment)) {
//                                                               oldEntity.setIndex(i);
//                                                               configEntity.setIndex(i);
//                                                           }
//                                                       }
//                                                       configEntity.setArrayList(list);
//                                                       configEntity.setOldValue(oldEntity);
//                                                       configEntity.setCallBack(paymentMethodCallback);
//                                                       JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
                                                   }
                                               });
        /////////////////////////Credit Card Bank begin////////////////////////////


//        if(false) {
//            checkoutActivity.findViewById(R.id.ll_checkout_payment_issuerbank).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    rl_issuer_bank.setBottomLineActive(true);
//                    AnimUtil.rotateArrow(arrowIssuerbank, true);
//                    tvIssuerbankHint.setVisibility(View.VISIBLE);
//                    tvIssuerbankHint.startAnimation(getHintAnimation(tvIssuerbankHint, checkoutActivity.getResources().getString(R.string.Issuer_Bank)));
//
//                    JViewUtils.cleanCurrentViewFocus(getActivity());
//                    String[] banks = paymentListEntity.getBanks();
//                    final ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
//                    for (String bank : banks) {
//                        WheelPickerEntity entity = new WheelPickerEntity();
//                        entity.setDisplay(bank);
//                        entity.setValue(bank);
//                        list.add(entity);
//                    }
//
//                    final WheelPickerEntity oldEntity = new WheelPickerEntity();
//                    oldEntity.setDisplay(banks[0]);
//                    oldEntity.setValue(banks[0]);
//
//                    WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//
//                    final String bank = tvIssuerBank.getText().toString();
//                    for (int i = 0; i < list.size(); i++) {
//                        if (list.get(i).getDisplay().equalsIgnoreCase(bank)) {
//                            oldEntity.setIndex(i);
//                            configEntity.setIndex(i);
//                        }
//                    }
//
//                    configEntity.setArrayList(list);
//                    configEntity.setOldValue(oldEntity);
//                    configEntity.setCallBack(new WheelPickerCallback() {
//                        @Override
//                        public void onCancel() {
//                            AnimUtil.rotateArrow(arrowIssuerbank, false);
//                            rl_issuer_bank.setBottomLineActive(false);
//                            if (tvIssuerBank.getText().length() > 0) {
//                                tvIssuerbankHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
//                            } else {
//                                tvIssuerbankHint.setVisibility(View.INVISIBLE);
//                            }
//                        }
//
//                        @Override
//                        public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                            JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
//                        }
//
//                        @Override
//                        public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                            if (newValue.getDisplay() == null) {
//                                if (JDataUtils.isEmpty(bank)) {
//
//                                    tvIssuerBank.setText(oldValue.getDisplay());
//                                    tvIssuerBank.setHint(oldValue.getValue());
//                                    tvIssuerBank.setTextColor(getResources().getColor(R.color.black));
//                                    tvIssuerbankHint.setText(getResources().getString(R.string.Issuer_Bank));
//                                    tvIssuerbankHint.setTextColor(ctv_payment_method_lab.getHintTextColors());
//                                }
//                            } else {
//                                tvIssuerBank.setText(newValue.getDisplay());
//                                tvIssuerBank.setHint(newValue.getValue());
//                                tvIssuerBank.setTextColor(getResources().getColor(R.color.black));
//                                tvIssuerbankHint.setText(getResources().getString(R.string.Issuer_Bank));
//                                tvIssuerbankHint.setTextColor(ctv_payment_method_lab.getHintTextColors());
//                            }
//                            AnimUtil.rotateArrow(arrowIssuerbank, false);
//                            rl_issuer_bank.setBottomLineActive(false);
//                            if (tvIssuerBank.getText().length() > 0) {
//                                tvIssuerbankHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
//                            } else {
//                                tvIssuerbankHint.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    });
//                    JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
//
//                    //createDialogPicker(list, oldEntity, tvIssuerBank);
//                }
//            });
//            /////////////////////////Credit Card Bank end////////////////////////////
//
//            /////////////////////////Online Banking Bank begin////////////////////////////
//                HashMap<String, String> onlinebanks = paymentListEntity.getOnlinebanks();
//                final ArrayList<WheelPickerEntity> list_onlinebanks = new ArrayList<WheelPickerEntity>();
//
//                for (String bank : onlinebanks.keySet()) {
//                    WheelPickerEntity entity = new WheelPickerEntity();
//                    entity.setDisplay(onlinebanks.get(bank));
//                    entity.setValue(bank);
//                    list_onlinebanks.add(entity);
//                }
//
//                tvOnlineBankingPayWith.setText(list_onlinebanks.get(0).getDisplay());
//                tvOnlineBankingPayWith.setHint(list_onlinebanks.get(0).getValue());
//
//                checkoutActivity.findViewById(R.id.ll_checkout_payment_onlinebanking).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        final WheelPickerEntity oldEntity_onlinebanks = new WheelPickerEntity();
//                        oldEntity_onlinebanks.setDisplay(list_onlinebanks.get(0).getDisplay());
//                        oldEntity_onlinebanks.setValue(list_onlinebanks.get(0).getValue());
//
//                        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//
//                        String bank = tvOnlineBankingPayWith.getText().toString();
//                        for (int i = 0; i < list_onlinebanks.size(); i++) {
//                            if (list_onlinebanks.get(i).getDisplay().equalsIgnoreCase(bank)) {
//                                oldEntity_onlinebanks.setIndex(i);
//                                configEntity.setIndex(i);
//                            }
//
//                        }
//                        CustomButtomLineRelativeLayout.setBottomLineActive(view_payment_online_line, true);
//                        AnimUtil.rotateArrow(arrowSelectOnlineBankingPayWith, true);
//                        tvOnlinebankHint.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
//                        configEntity.setArrayList(list_onlinebanks);
//                        configEntity.setOldValue(oldEntity_onlinebanks);
//                        configEntity.setCallBack(new WheelPickerCallback() {
//                            @Override
//                            public void onCancel() {
//                                CustomButtomLineRelativeLayout.setBottomLineActive(view_payment_online_line, false);
//                                AnimUtil.rotateArrow(arrowSelectOnlineBankingPayWith, false);
//                                tvOnlinebankHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
//                            }
//
//                            @Override
//                            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
//                            }
//
//                            @Override
//                            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                                CustomButtomLineRelativeLayout.setBottomLineActive(view_payment_online_line, false);
//                                AnimUtil.rotateArrow(arrowSelectOnlineBankingPayWith, false);
//                                tvOnlinebankHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
//
//                                if (newValue.getDisplay() == null) {
//                                    if (JDataUtils.isEmpty(tvOnlineBankingPayWith)) {
//
//                                        tvOnlineBankingPayWith.setText(oldValue.getDisplay());
//                                        tvOnlineBankingPayWith.setHint(oldValue.getValue());
//                                    }
//
//                                } else {
//                                    tvOnlineBankingPayWith.setText(newValue.getDisplay());
//                                    tvOnlineBankingPayWith.setHint(newValue.getValue());
//
//                                }
//
//                            }
//                        });
//                        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
//
//                        //createDialogPicker(list_onlinebanks, oldEntity_onlinebanks, tvOnlineBankingPayWith);
//                    }
//                });
//            }
            /////////////////////////Online Banking Bank end////////////////////////////

            /////////////////////////Credit Card Type Begin////////////////////////////

            HashMap<String, String> cctypes = paymentListEntity.getCctype();
           if(cctypes!=null) {
               final ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
               WheelPickerEntity entity;
               List<String> list_tem = new ArrayList<String>();//used to record position
               for (String str : cctypes.keySet()) {
                   list_tem.add(str);
                   entity = new WheelPickerEntity();
                   entity.setDisplay(cctypes.get(str));
                   entity.setValue(str);
                   list.add(entity);
               }
               final WheelPickerEntity oldEntity = new WheelPickerEntity();
               oldEntity.setDisplay(cctypes.get(list_tem.get(0)));
               oldEntity.setValue(list_tem.get(0));
               final WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
               configEntity.setArrayList(list);
               getActivity().findViewById(R.id.ll_checkout_payment_cardtype).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       rl_card_type.setBottomLineActive(true);
                       AnimUtil.rotateArrow(arrowCardType, true);
                       tvCardtypeHint.setVisibility(View.VISIBLE);
                       tvCardtypeHint.startAnimation(getHintAnimation(tvCardtypeHint, checkoutActivity.getResources().getString(R.string.Card_Type)));
                       JViewUtils.cleanCurrentViewFocus(getActivity());
                       final String cctype = tvCardType.getText().toString();
                       if (!JDataUtils.isEmpty(cctype)) {
                           for (int i = 0; i < list.size(); i++) {
                               if (list.get(i).getDisplay().equalsIgnoreCase(cctype)) {
                                   oldEntity.setIndex(i);
                                   configEntity.setIndex(i);
                               }
                           }
                       }
                       configEntity.setOldValue(oldEntity);
                       configEntity.setCallBack(new WheelPickerCallback() {
                           @Override
                           public void onCancel() {
                               rl_card_type.setBottomLineActive(false);
                               AnimUtil.rotateArrow(arrowCardType, false);
                               if (tvCardType.getText().length() > 0) {
                                   tvCardtypeHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
                               } else {
                                   tvCardtypeHint.setVisibility(View.INVISIBLE);
                               }
                           }
                           @Override
                           public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                               JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
                           }
                           @Override
                           public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {

                               if (newValue.getDisplay() == null) {
                                   if (JDataUtils.isEmpty(cctype)) {
                                       tvCardType.setText(oldValue.getDisplay());
                                       tvCardType.setHint(oldValue.getValue());
                                       tvCardType.setTextColor(getResources().getColor(R.color.black));
                                       tvCardtypeHint.setText(getResources().getString(R.string.Card_Type));
                                       tvCardtypeHint.setTextColor(ctv_payment_method_lab.getHintTextColors());
                                   }

                               } else {
                                   tvCardType.setText(newValue.getDisplay());
                                   tvCardType.setHint(newValue.getValue());
                                   tvCardType.setTextColor(getResources().getColor(R.color.black));
                                   tvCardtypeHint.setText(getResources().getString(R.string.Card_Type));
                                   tvCardtypeHint.setTextColor(ctv_payment_method_lab.getHintTextColors());
                               }
                               rl_card_type.setBottomLineActive(false);
                               AnimUtil.rotateArrow(arrowCardType, false);
                               if (tvCardType.getText().toString().length() > 0) {
                                   tvCardtypeHint.setTextColor(JToolUtils.getColor(R.color.label_saved));
                               } else {
                                   tvCardtypeHint.setVisibility(View.INVISIBLE);
                               }
                           }
                       });
                       JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
                   }
               });
           }
//        getPaymentlistImage
        String paymentImageUrl = paymentListEntity.getPaymentlist_image();
//        String paymentImageUrl = "paymentlist_app/default/img_secure_payment.png";
        if (!TextUtils.isEmpty(paymentImageUrl)) {
             int destWidth = WhiteLabelApplication.getPhoneConfiguration().getScreenWidth();
             int imageHeight = destWidth*65/600;
            JImageUtils.downloadImageFromServerListener(getActivity(), mImageLoader, mPaymentListImage, paymentImageUrl, new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }
                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    int h = resource.getHeight();
                    int w = resource.getWidth();
                    LinearLayout.LayoutParams image_parames = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getHeight(w, h));
                    mPaymentListImage.setLayoutParams(image_parames);
//                    mPaymentListImage.setImageBitmap(resource);
                    return false;
                }
            });
        }
    }
    public int getHeight(int with, int height) {
        return (int) (getActivity().getResources().getDisplayMetrics().widthPixels * height / with);
    }
    public boolean checkCODPayment() {
        if (paymentListEntity.getCashondelivery() != null && paymentListEntity.getCashondelivery().getError_flag() == 0) {
            showProductDialog();
            return false;
        }
        return true;
    }


    public void savePayment(String nonce) {
        //开始计时
//        checkoutActivity.mGATrackPaymentToReviewTimeEnable = true;
//        checkoutActivity.mGATrackPaymentToReviewTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
        bank = "";
        code = "";
        molpayType = "";
        String paymentMethod = "";
        String paymentCountry = "";
        String ccNumber = "";
        String ccId = "";
        String ccExpMonth = "";
        String ccExpYear = "";
        String ccOwner = "";
        String ccType = "";
        String ccBank = "";
        String ccSecureHash = "";
        paymentMethod="paypal_express";
        mDialog = JViewUtils.showProgressDialog(checkoutActivity);
        checkoutActivity.setButtonEnable(false);
        mCheckoutDao.savePayment(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),paymentMethod, molpayType, paymentCountry, ccNumber, ccId, ccExpMonth, ccExpYear, ccOwner, ccType, ccBank, ccSecureHash, nonce);
    }

    public void gaTrackerSavePayment() {
        String paymentStr = "";
        if (paymentType == ONLINEPAYMENT) {
            paymentStr = "Online Banking Method";
        } else if (paymentType == CARDPAYMENT) {
            paymentStr = "Credit Card Method";
        } else if (paymentType == OFFLINEPAYMENT) {
            paymentStr = "Bank Transfer Method";
        }
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                    "Save Payment",
                    getString(R.string.paypal_payment),
                    Long.valueOf(WhiteLabelApplication.getAppConfiguration().getUser().getId()));
        } catch (Exception ex) {
            ex.getStackTrace();
        }

    }

    private class CreditCartNumberTextWatcher implements TextWatcher {
        int beforeTextLength = 0;
        int onTextLength = 0;
        boolean isChanged = false;

        int location = 0;// 记录光标的位置
        private char[] tempChar;
        private StringBuffer buffer = new StringBuffer();
        int konggeNumberB = 0;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            konggeNumberB = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    konggeNumberB++;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (isChanged) {
                location = etCreditCardNumber.getSelectionEnd();
                int index = 0;
                while (index < buffer.length()) {
                    if (buffer.charAt(index) == ' ') {
                        buffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < buffer.length()) {
                    if (index % 5 == 4) {
                        buffer.insert(index, ' ');
                        konggeNumberC++;
                    }
                    index++;
                }

                if (konggeNumberC > konggeNumberB) {
                    location += (konggeNumberC - konggeNumberB);
                }

                tempChar = new char[buffer.length()];
                buffer.getChars(0, buffer.length(), tempChar, 0);
                String str = buffer.toString();
                if (location > str.length()) {
                    location = str.length();
                } else if (location < 0) {
                    location = 0;
                }

                etCreditCardNumber.setText(str);
                Editable etable = etCreditCardNumber.getText();
                Selection.setSelection(etable, location);
                isChanged = false;
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        GaTrackHelper.getInstance().googleAnalytics(Const.GA.GUEST_PAYMENT_SCREEN, getActivity());
    }
}

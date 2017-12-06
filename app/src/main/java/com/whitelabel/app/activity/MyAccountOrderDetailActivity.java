package com.whitelabel.app.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.MyAccountOrderDetailAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.dao.CheckoutDao;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.CheckoutPaymentReturnShippingAddress;
import com.whitelabel.app.model.MyAccountOrderDetailEntityResult;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.RepaymentInfoModel;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.FileUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.PaypalHelper;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;

public class MyAccountOrderDetailActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {
    private String mOrderNumber;
    private MyAccountOrderDetailAdapter adapter;
    private TextView tvOrderNumber;
    private TextView tvDate;
    private TextView tvShippingFee;
    private TextView tvSubTotal;
    private RelativeLayout rlVoucher;
    private TextView tvVoucher;
    private TextView tvVoucherTitle;
    private TextView tvGrandTotal;
//    private TextView tvUsername;
//    private TextView tvAddress1;
//    private TextView tvCityStatePostCode;
//    private TextView tvCountry;
//    private TextView tvPhone;
    private TextView mTvGst;
    private TextView tvStoreCreditTitle;
    private TextView tvStoreCreditVlaue;
    private TextView tvTopAddressTitle;

    private CustomWebView tvCreditCardTypeText;
    private ScrollView scrollView;
    private ListView listView;
    private Dialog mDialog;
    private View rlStoreCredit;
    private String orderId;
    private final String SESSION_EXPIRED = "session expired,login again please";
    private final int REQUESTCODE_LOGIN = 1000;
    private TextView tvConfirm;
    private MyAccountDao mDao;
    private String TAG;
    private ImageLoader mImageLoader;
    private static final class DataHandler extends Handler {
        private final WeakReference<MyAccountOrderDetailActivity> mActivity;
        public DataHandler(MyAccountOrderDetailActivity activity) {
            mActivity = new WeakReference<MyAccountOrderDetailActivity>(activity);
        }
        @Override
        public void handleMessage(final Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (mActivity.get().mDialog != null) {
                mActivity.get().mDialog.dismiss();
            }
            switch (msg.what) {
                case MyAccountDao.REQUEST_SENDREQUEST:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        mActivity.get().mBean = (MyAccountOrderDetailEntityResult) msg.obj;
                        mActivity.get().initWithWebServiceDatas(mActivity.get().mBean);
                    } else {
                        String errorMsg = msg.obj.toString();
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), errorMsg)) {
                            if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(mActivity.get().SESSION_EXPIRED))) {

                                Intent intent = new Intent();
                                intent.setClass(mActivity.get(), LoginRegisterActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("expire", true);
                                intent.putExtras(bundle);
                                mActivity.get().startActivityForResult(intent, mActivity.get().REQUESTCODE_LOGIN);

                            }
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_RE_PAYMENT:
                    if(mActivity.get().mDialog!=null){
                        mActivity.get().mDialog.dismiss();
                    }
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        RepaymentInfoModel  repaymentInfoModel= (RepaymentInfoModel) msg.obj;
                        String productName="";
                        try {
                            productName = mActivity.get().mBean.getSuborders()[0].getItems()[0].getName();
                        }catch (Exception ex){
                            ex.getMessage();
                        }
                        mActivity.get().mOrderNumber=repaymentInfoModel.getOrderSn();
                        mActivity.get().mPaypalHelper.startPaypalPayment(mActivity.get(),
                                repaymentInfoModel.
                                getGrandTotal(),
                                repaymentInfoModel.getUnit(),
                                productName,
                                repaymentInfoModel.getOrderSn());
                    }else{
                        String errorMsg = msg.obj.toString();
                        JDataUtils.errorMsgHandler(mActivity.get(), errorMsg);
                    }
                    break;
                case CheckoutDao.REQUEST_CHANGEORDERSTATUS:
                    if(mActivity.get().mDialog!=null) {
                        mActivity.get().mDialog.dismiss();
                    }
                    if (msg.arg1 == CheckoutDao.RESPONSE_SUCCESS) {
                        Bundle bundle_success = new Bundle();
                        bundle_success.putString("payment_status", "1");
                        bundle_success.putString("lastrealorderid", mActivity.get().mOrderNumber);
                        bundle_success.putInt("paymentMethod", CheckoutPaymentRedirectActivity.PAYMENT_PALPAY);
                        mActivity.get().startNextActivity(bundle_success, CheckoutPaymentRedirectActivity.class, true);
                    } else {
                        String errorMsg = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorMsg, mActivity.get().REQUESTCODE_LOGIN)) {
                            Bundle bundle_failuer = new Bundle();
                            bundle_failuer.putString("payment_status", "0");
                            bundle_failuer.putString("errorMsg", errorMsg);
                            bundle_failuer.putString("orderNumber", mActivity.get().mBean.getOrderId());
                            mActivity.get().startNextActivity(bundle_failuer, CheckoutPaymentStatusActivity.class, true);
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;

            }
            super.handleMessage(msg);
        }
    }



    @Override
    protected void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        super.onDestroy();
    }
    private PaypalHelper  mPaypalHelper;
    private CheckoutDao mCheckoutDao;
    private LinearLayout llShippingAddress;
    private LinearLayout llBillingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_order_detail1);
        initToolBar();
        initView();
        // ViewUtils.inject(this);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        llShippingAddress= (LinearLayout) findViewById(R.id.ll_shipping_address);
        llBillingAddress= (LinearLayout) findViewById(R.id.ll_billing_address);
        tvConfirm.setOnClickListener(this);
        JViewUtils.setSoildButtonGlobalStyle(this,tvConfirm);
//        tvConfirm.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
        findViewById(R.id.ll_sc_checkout).setOnClickListener(this);
        TAG =this.getClass().getSimpleName();
        DataHandler dataHandler = new DataHandler(this);
        mDao=new MyAccountDao(TAG, dataHandler);
        mCheckoutDao = new CheckoutDao(TAG, dataHandler);
        mPaypalHelper=new PaypalHelper();
        mPaypalHelper.startPaypalService(this);
        initSessionKey();
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.order_detail));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initView() {
        mImageLoader = new ImageLoader(this);
        mTvGst= (TextView) findViewById(R.id.tv_order_detail_gst);
        tvOrderNumber = (TextView) findViewById(R.id.tv_myaccount_orderdetail_ordernumber);
        tvDate = (TextView) findViewById(R.id.tv_myaccount_orderdetail_date);
        tvShippingFee = (TextView) findViewById(R.id.tv_order_detail_shippingfee);
        tvSubTotal = (TextView) findViewById(R.id.tv_order_detail_subtotal);
        rlVoucher = (RelativeLayout) findViewById(R.id.rl_order_detail_voucher);
        tvVoucher = (TextView) findViewById(R.id.tv_order_detail_voucher);
        tvVoucherTitle = (TextView) findViewById(R.id.tv_order_detail_voucher_title);
        tvGrandTotal = (TextView) findViewById(R.id.tv_order_detail_grandtotal);
        tvTopAddressTitle= (TextView) findViewById(R.id.tv_order_detail_top_title);
//        tvUsername = (TextView) findViewById(R.id.tv_order_detail_username);
//        tvAddress1 = (TextView) findViewById(R.id.tv_order_detail_address1);
//        TextView tvAddress2 = (TextView) findViewById(R.id.tv_order_detail_address2);
//        tvCityStatePostCode = (TextView) findViewById(R.id.tv_order_detail_citystatepostcode);
//        tvCountry = (TextView) findViewById(R.id.tv_order_detail_country);
//        tvPhone = (TextView) findViewById(R.id.tv_order_detail_telephone);
        tvCreditCardTypeText = (CustomWebView) findViewById(R.id.tv_order_detail_paymentmethod_text);
        RelativeLayout rlBody = (RelativeLayout) findViewById(R.id.rl_orderdetail_body);
        scrollView = (ScrollView) findViewById(R.id.scollview_myorder_detail);
        listView = (ListView) findViewById(R.id.lv_myaccount_orderdetail);
        LinearLayout llWebView = (LinearLayout) findViewById(R.id.ll_myaccount_orderdetail_paymentmethod_creditcard);
        ProgressBar progressBarLoading = (ProgressBar) findViewById(R.id.pb_orderdetail_loading);
        rlStoreCredit = findViewById(R.id.rl_order_detail_storecredit);
        tvStoreCreditTitle = (TextView) findViewById(R.id.tv_order_detail_storecredit_title);
        tvStoreCreditVlaue = (TextView) findViewById(R.id.tv_order_detail_storecredit);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initSessionKey() {
        //init session_key
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)) {

            initData();
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("expire", true);
            BottomtoTopActivity(bundle, LoginRegisterActivity.class, false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if(mBean.getIsRPayment()==1){
//                    if()
//                    mPaypalHelper.startPaypalPayment(this,mBean.get,
//                            WhiteLabelApplication.getAppConfiguration().getCurrency().getName(),
//
//                            );
                    mDialog=JViewUtils.showProgressDialog(this);
                    mDao.rePaymentInfo(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(),mBean.getOrderId());

                }else {
                    Intent intent = new Intent(MyAccountOrderDetailActivity.this, BankTransaferActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", mBean.getBanktransfer());
                    intent.putExtras(bundle);
                    int REQUESTCODE_TRANSAFER = 2000;
                    startActivityForResult(intent, REQUESTCODE_TRANSAFER);
                }
                break;

            default:
                break;
        }
    }

    private String creditStr = "";

    private void initData() {
        //Get Entity from order list
        creditStr = getResources().getString(R.string.store_credit);
        final MyAccountOrderOuter orderOuter = (MyAccountOrderOuter) getIntent().getExtras().getSerializable("orderOuter");
        orderId = orderOuter.getOrderId();
        tvOrderNumber.setText(getResources().getString(R.string.Order_Number) + " " + orderOuter.getOrderSn());
        JLogUtils.d("orderId1", orderOuter.getOrderSn());
        sendRequest(orderId);
        //Set value to detail page column
        MyAccountOrderMiddle[] orderMiddle = orderOuter.getSuborders();
        LinkedList<MyAccountOrderMiddle> list_orderMiddles = new LinkedList<MyAccountOrderMiddle>();
        list_orderMiddles.addAll(Arrays.asList(orderMiddle));
        adapter = new MyAccountOrderDetailAdapter(this, mImageLoader,orderOuter.getStatusCode(),orderOuter.getStatus());
        adapter.list = list_orderMiddles;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        int totalHeight = 0;//To fix measurement on page
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }

    private MyAccountOrderDetailEntityResult mBean;

    /**
     * send request to server
     *
     * @param orderId
     */
    private void sendRequest(String orderId) {
        mDialog = JViewUtils.showProgressDialog(MyAccountOrderDetailActivity.this);
        mDao.sendRequest("", WhiteLabelApplication.getAppConfiguration().getUserInfo(this).getSessionKey(), orderId);

//        SVRParameters parameters = new SVRParameters();
//        parameters.put("storeId", "");
//        parameters.put("session_key", SESSION_KEY);
//        parameters.put("orderId", orderId);
//        mDialog = JViewUtils.showProgressDialog(MyAccountOrderDetailActivity.this);
//
//        SVRMyAccountOrderDetail myAccountOrderDetailHandler = new SVRMyAccountOrderDetail(this, parameters);
//
//        myAccountOrderDetailHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                mBean = (MyAccountOrderDetailEntityResult) result;
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        initWithWebServiceDatas(mBean);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(int resultCode, final String errorMsg) {
//
//                if (!JDataUtils.errorMsgHandler(MyAccountOrderDetailActivity.this, errorMsg)) {
//                    if ((!JDataUtils.isEmpty(errorMsg)) && (errorMsg.contains(SESSION_EXPIRED))) {
//
//                        Intent intent = new Intent();
//                        intent.setClass(MyAccountOrderDetailActivity.this, LoginRegisterActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("expire", true);
//                        intent.putExtras(bundle);
//                        startActivityForResult(intent, REQUESTCODE_LOGIN);
//
//                    } else {
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MyAccountOrderDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }
    public void webViewFont(String str) {
        String html = FileUtils.readAssest(this, "html/order_web.html");
//        html = html.replace("@fontName0", "LatoRegular");
//        html = html.replace("@fontPath0", "../fonts/Lato-Regular.ttf");// assets相对路径
        html = html.replace("@mytext", str);
        String baseurl = "file:///android_asset/html/";
        tvCreditCardTypeText.setBackgroundColor(0);
        tvCreditCardTypeText.getBackground().setAlpha(0);
        tvCreditCardTypeText.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);

    }
    /**
     * init UI datas with WebService Datas
     *
     * @param orderDetail
     */
    private ObjectAnimator mAnimator;

    private void initWithWebServiceDatas(MyAccountOrderDetailEntityResult orderDetail) {
        if (orderDetail==null){
            return;
        }
        String method = orderDetail.getPaymentMethod();
        if (mDialog != null) {
            mDialog.cancel();
        }
        if (!TextUtils.isEmpty(orderDetail.getStoreCredit())) {
            rlStoreCredit.setVisibility(View.VISIBLE);
            try {
                tvStoreCreditVlaue.setText("- RM " + JDataUtils.formatDouble(orderDetail.getStoreCredit()));
            } catch (Exception ex) {
                ex.getMessage();
            }
            tvStoreCreditTitle.setText(creditStr);
        }
        scrollView.setVisibility(View.VISIBLE);
        scrollView.scrollTo(0, 0);
        AnimUtil.alpha_0_1_500(scrollView);
//        ViewPropertyAnimator.animate(scrollView).setDuration(600).alphaBy(0).alpha(1).start();
        if (!TextUtils.isEmpty(method)) {
            // tvCreditCardTypeText.setText(method);
            webViewFont(method);
        }

        if (orderDetail.getIsBanktransfer() == 1) {
            tvConfirm.setVisibility(View.VISIBLE);
            findViewById(R.id.ll_sc_checkout).setVisibility(View.VISIBLE);
            findViewById(R.id.bottom_black).setVisibility(View.VISIBLE);
        } else  if(orderDetail.getIsRPayment()==1){
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setText(getResources().getString(R.string.order_re_payment));
            findViewById(R.id.ll_sc_checkout).setVisibility(View.VISIBLE);
            findViewById(R.id.bottom_black).setVisibility(View.VISIBLE);

        }else {
            findViewById(R.id.ll_sc_checkout).setVisibility(View.GONE);
            findViewById(R.id.bottom_black).setVisibility(View.GONE);
            tvConfirm.setVisibility(View.GONE);
        }
        tvDate.setText(orderDetail.getDate());
        tvSubTotal.setText(orderDetail.getSubtotal());
        tvShippingFee.setText(orderDetail.getShippingFee());
        if (!TextUtils.isEmpty(orderDetail.getGst())) {
            mTvGst.setText(orderDetail.getGst());
        }else{
            mTvGst.setText("");
        }
        //Voucher->discount
        if (orderDetail.getDiscount() != null && orderDetail.getDiscount().size() > 0) {
            rlVoucher.setVisibility(View.VISIBLE);
            if (JDataUtils.isEmpty(orderDetail.getDiscount().get("title"))) {
                tvVoucherTitle.setText(getResources().getString(R.string.Discount));
            } else {
                tvVoucherTitle.setText(getResources().getString(R.string.Discount) + " (" + orderDetail.getDiscount().get("title") + ")");
            }
            tvVoucher.setText(orderDetail.getDiscount().get("value"));
        }
        tvGrandTotal.setText(orderDetail.getGrandTotal());
        tvGrandTotal.setTextColor(getResources().getColor(R.color.black000000));
        if (orderDetail.getIsPickupInStore()==1){
            tvTopAddressTitle.setText(orderDetail.getPickupAddress().getTitle());
            llShippingAddress.addView(getPickUpAddress(orderDetail.getPickupAddress()));
            llBillingAddress.addView(getAddressView(orderDetail.getBillingAddress()));
        }else {
            llShippingAddress.addView(getAddressView(orderDetail.getShippingAddress()));
            llBillingAddress.addView(getAddressView(orderDetail.getBillingAddress()));
        }

//        ShippingAddress shippingAddress = orderDetail.getShippingAddress();
//        tvUsername.setText(shippingAddress.getFirstname() + " " + shippingAddress.getLastname());
//        tvAddress1.setText(shippingAddress.getStreet());
//        //tvAddress2.setText(shippingAddress.getStreet());
//        String state = JDataUtils.isEmpty(shippingAddress.getRegion()) ? "" : shippingAddress.getRegion() + ", ";
//        tvCityStatePostCode.setText(shippingAddress.getCity() + ", " + state + shippingAddress.getPostcode());
//        tvCountry.setText(shippingAddress.getCountry());
//        tvPhone.setText(getResources().getString(R.string.t) + shippingAddress.getTelephone());
        MyAccountOrderMiddle[] orderMiddle = orderDetail.getSuborders();
        LinkedList<MyAccountOrderMiddle> list_orderMiddles = new LinkedList<MyAccountOrderMiddle>();
        list_orderMiddles.addAll(Arrays.asList(orderMiddle));
        adapter.list = list_orderMiddles;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        int totalHeight = 0;//To fix measurement on page
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
//
    }
    @NonNull
    private View getAddressView(CheckoutPaymentReturnShippingAddress address) {
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell_for_review, null);
        view.findViewById(R.id.image_address_select_top).setVisibility(View.GONE);
        view.findViewById(R.id.image_address_select_end).setVisibility(View.GONE);
        //view.findViewById(R.id.btn_address_select_cover).setVisibility(View.GONE);
        TextView tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
        //TextView tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
        TextView tvAddress1 = (TextView) view.findViewById(R.id.tv_address_select_address1);
        TextView tvAddress2 = (TextView) view.findViewById(R.id.tv_address_select_address2);
        TextView  tvDayTimeTelephone= (TextView) view.findViewById(R.id.tv_day_time_telephone);
        TextView tvCityStatePostcode = (TextView) view.findViewById(R.id.tv_address_select_citystatepostcode);
        TextView tvCountry = (TextView) view.findViewById(R.id.tv_address_select_country);
        TextView tvTelephone = (TextView) view.findViewById(R.id.tv_address_select_telephone);
        tvFirstname.setText(address.getFirstname() + " " + address.getLastname());
        //tvLastname.setText(address.getLastname());
        tvAddress1.setText(address.getStreet());
        tvAddress2.setVisibility(View.GONE);
        //initstoreCredit
        /**
         * Constructor city,state,postcode
         */
        if(!TextUtils.isEmpty(address.getFax())){
            tvDayTimeTelephone.setText(getResources().getString(R.string.day_time_contact)+" : "+address.getFax());
        }else{
            tvDayTimeTelephone.setVisibility(View.GONE);
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder=stringBuilder.append(address.getCity()).append(",");
        if(!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")){
            stringBuilder=stringBuilder.append(address.getRegion());
        }
        if(!TextUtils.isEmpty(address.getPostcode())){
            stringBuilder=stringBuilder.append(",").append(address.getPostcode());
        }
        tvCityStatePostcode.setText(stringBuilder.toString());
        tvCountry.setText(address.getCountry());
        tvTelephone.setText(getResources().getString(R.string.address_mobile_number)+" : " + address.getTelephone());
        view.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent00));
        return view;
    }

    private View getPickUpAddress(MyAccountOrderDetailEntityResult.PickUpAddress pickUpAddress){
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_order_detail_pick_up_store_address, null);
        TextView tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
        //TextView tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
        tvFirstname.setText(Html.fromHtml(pickUpAddress.getAddress()));
        //tvLastname.setText(address.getLastname());
        view.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent00));
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUESTCODE_LOGIN == requestCode) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(MyAccountOrderDetailActivity.this)) {
                sendRequest(orderId);
            }
        } else if (resultCode == BankTransaferActivity.RESULTCODE && data != null) {
            String id = data.getStringExtra("id");
            String rankFrom = data.getStringExtra("rankFrom");
            String email = data.getStringExtra("email");
            String orderNumber = data.getStringExtra("orderNumber");
            String transferee = data.getStringExtra("transferee");
            String transferDate = data.getStringExtra("transferDate");
            String transferred = data.getStringExtra("transferred");
            String url = data.getStringExtra("url");
            String canSubmit = data.getStringExtra("cansubmit");
            mBean.getBanktransfer().setTransferId(id);
            mBean.getBanktransfer().setBankFrom(rankFrom);
            mBean.getBanktransfer().setEmail(email);
            mBean.getBanktransfer().setOrderNo(orderNumber);
            mBean.getBanktransfer().setTransferee(transferee);
            mBean.getBanktransfer().setTransferDate(transferDate);
            mBean.getBanktransfer().setTransferred(transferred);
            mBean.getBanktransfer().setProofFile(url);
            try {
                mBean.getBanktransfer().setCanSubmit(Integer.parseInt(canSubmit));
            } catch (Exception ex) {
                ex.getStackTrace();
            }

        }
//        else if(requestCode==PaypalHelper.REQUEST_CODE_PAYMENT){
//
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm =
//                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        Log.i(TAG, confirm.toJSONObject().toString(4));
//                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
//                    } catch (JSONException e) {
//                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//                JSONObject jsonObject= confirm.getPayment().toJSONObject();
//                String productName="PayPal-Android-SDK";
//                String currencyCode="";
//                String amount="";
//                String id=confirm.getProofOfPayment().getPaymentId();
//                String state=confirm.getProofOfPayment().getState();
//                String createTime=confirm.getProofOfPayment().getCreateTime();
//                try {
//                    currencyCode=jsonObject.getString("currency_code");
//                    amount=jsonObject.getString("amount");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                mDialog=JViewUtils.showProgressDialog(this);
//                mCheckoutDao.changeOrderStatus(WhiteLabelApplication.getAppConfiguration().
//                                getUserInfo(this).getSessionKey(),
//                        mOrderNumber,mBean.getPaymentCode(),productName,currencyCode,amount,id,state,createTime);
//            }
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        EasyTracker.getInstance(this).activityStart(this);
//        EasyTracker easyTracker = EasyTracker.getInstance(this);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Order Detail Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
        GaTrackHelper.getInstance().googleAnalytics("Order Detail Screen", this);
        JLogUtils.i("googleGA_screen", "Order Detail Screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }


}

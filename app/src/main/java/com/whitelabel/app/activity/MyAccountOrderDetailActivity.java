package com.whitelabel.app.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.MyAccountOrderDetailAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.MyAccountOrderDetailEntityResult;
import com.whitelabel.app.model.MyAccountOrderMiddle;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.model.ShippingAddress;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.FileUtils;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;

public class MyAccountOrderDetailActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {

    private MyAccountOrderDetailAdapter adapter;
    private TextView tvOrderNumber;
    private TextView tvDate;
    private TextView tvShippingFee;
    private TextView tvSubTotal;
    private RelativeLayout rlVoucher;
    private TextView tvVoucher;
    private TextView tvVoucherTitle;
    private TextView tvGrandTotal;
    private TextView tvUsername;
    private TextView tvAddress1;
    private TextView tvAddress2;
    private TextView tvCityStatePostCode;
    private TextView tvCountry;
    private TextView tvPhone;
    private TextView mTvGst;
    private CustomWebView tvCreditCardTypeText;
    private RelativeLayout rlBody;
    private ScrollView scrollView;
    private ListView listView;
    private LinearLayout llWebView;
    private ProgressBar progressBarLoading;
    private Dialog mDialog;
    private Handler mHandler = new Handler();
    private View rlStoreCredit;
    private String orderId;
    private final String SESSION_EXPIRED = "session expired,login again please";
    private final int REQUESTCODE_LOGIN = 1000;
    private final int REQUESTCODE_TRANSAFER = 2000;
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
                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;

            }
            super.handleMessage(msg);
        }
    }

    private DataHandler dataHandler;


    @Override
    protected void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_order_detail1);
        initToolBar();
        initView();
        // ViewUtils.inject(this);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(this);
        findViewById(R.id.ll_sc_checkout).setOnClickListener(this);
        TAG =this.getClass().getSimpleName();
        dataHandler=new DataHandler(this);
        mDao=new MyAccountDao(TAG,dataHandler);
        initSessionKey();
    }





    private void initToolBar() {
        setTitle(getResources().getString(R.string.order_detail));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private TextView tvStoreCreditTitle;
    private TextView tvStoreCreditVlaue;

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
        tvUsername = (TextView) findViewById(R.id.tv_order_detail_username);
        tvAddress1 = (TextView) findViewById(R.id.tv_order_detail_address1);
        tvAddress2 = (TextView) findViewById(R.id.tv_order_detail_address2);
        tvCityStatePostCode = (TextView) findViewById(R.id.tv_order_detail_citystatepostcode);
        tvCountry = (TextView) findViewById(R.id.tv_order_detail_country);
        tvPhone = (TextView) findViewById(R.id.tv_order_detail_telephone);
        tvCreditCardTypeText = (CustomWebView) findViewById(R.id.tv_order_detail_paymentmethod_text);
        rlBody = (RelativeLayout) findViewById(R.id.rl_orderdetail_body);
        scrollView = (ScrollView) findViewById(R.id.scollview_myorder_detail);
        listView = (ListView) findViewById(R.id.lv_myaccount_orderdetail);
        llWebView = (LinearLayout) findViewById(R.id.ll_myaccount_orderdetail_paymentmethod_creditcard);
        progressBarLoading = (ProgressBar) findViewById(R.id.pb_orderdetail_loading);
        rlStoreCredit = findViewById(R.id.rl_order_detail_storecredit);
        tvStoreCreditTitle = (TextView) findViewById(R.id.tv_order_detail_storecredit_title);
        tvStoreCreditVlaue = (TextView) findViewById(R.id.tv_order_detail_storecredit);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initSessionKey() {
        //init session_key
        if (GemfiveApplication.getAppConfiguration().isSignIn(this)) {

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
                Intent intent = new Intent(MyAccountOrderDetailActivity.this, BankTransaferActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", mBean.getBanktransfer());
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUESTCODE_TRANSAFER);
                overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
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
        adapter = new MyAccountOrderDetailAdapter(this, mImageLoader);
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
        mDao.sendRequest("", GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey(), orderId);

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
        } else {
            findViewById(R.id.ll_sc_checkout).setVisibility(View.GONE);
            findViewById(R.id.bottom_black).setVisibility(View.GONE);
            tvConfirm.setVisibility(View.GONE);
        }

        tvDate.setText(orderDetail.getDate());
        tvSubTotal.setText(orderDetail.getSubtotal());
        tvShippingFee.setText(orderDetail.getShippingFee());
        if (!TextUtils.isEmpty(orderDetail.getGst().trim())) {
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
        ShippingAddress shippingAddress = orderDetail.getShippingAddress();
        tvUsername.setText(shippingAddress.getFirstname() + " " + shippingAddress.getLastname());
        tvAddress1.setText(shippingAddress.getStreet());
        //tvAddress2.setText(shippingAddress.getStreet());
        String state = JDataUtils.isEmpty(shippingAddress.getRegion()) ? "" : shippingAddress.getRegion() + ", ";
        tvCityStatePostCode.setText(shippingAddress.getCity() + ", " + state + shippingAddress.getPostcode());
        tvCountry.setText(shippingAddress.getCountry());
        tvPhone.setText(getResources().getString(R.string.t) + shippingAddress.getTelephone());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUESTCODE_LOGIN == requestCode) {
            if (GemfiveApplication.getAppConfiguration().isSignIn(MyAccountOrderDetailActivity.this)) {

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
    }


    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);
//        EasyTracker easyTracker = EasyTracker.getInstance(this);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Order Detail Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
//        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
//        GaTrackHelper.getInstance().googleAnalytics("Order Detail Screen", this);
//        JLogUtils.i("googleGA_screen", "Order Detail Screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }


}

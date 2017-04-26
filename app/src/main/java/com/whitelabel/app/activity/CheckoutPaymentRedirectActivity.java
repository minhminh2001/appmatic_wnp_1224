package com.whitelabel.app.activity;

import android.app.Dialog;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whitelabel.app.R;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.dao.CheckoutDao;
import com.whitelabel.app.model.ShoppingDiscountBean;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

import java.io.Serializable;

public class CheckoutPaymentRedirectActivity extends com.whitelabel.app.BaseActivity {
    private Dialog mDialog;
    private WebView webView;

    private ShoppingDiscountBean mDiscountBean;
    private   String REDIRECT_PAYMENT_URL= GlobalData.serviceRequestUrl+ "appservice/checkout/"; ;
    private   String lastrealorderid;
    private String grandTotal;
    private String shippingFee;
    private Serializable paymentSaveReturnEntity;
    private String TAG=this.getClass().getSimpleName();
    private boolean isSuccess;
    private String payment_type;
    private String session_key;
    public  static final int PAYMENT_ONLINE=1;
    public  static final int PAYMENT_CARD=2;
    public int paymentMethod;
    private int fromType;
    private boolean DISABLE_SSL_CHECK_FOR_TESTING=false;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            closeDialog();
            Bundle bundle = new Bundle();
            bundle.putString("payment_status", "1");
            bundle.putString("orderNumber", lastrealorderid);
            bundle.putString("grand_total", grandTotal);
            bundle.putString("shipping_fee", shippingFee);
            bundle.putSerializable("discountBean", mDiscountBean);
            bundle.putSerializable("paymentSaveReturnEntity", paymentSaveReturnEntity);
            bundle.putInt("fromType",fromType);
//            switch (msg.what){
//                case CheckoutDao.REQUEST_GETLUCKDRAW:
//                        if(msg.arg1==CheckoutDao.RESPONSE_SUCCESS){
//                            CustomAnimEntity entity= (CustomAnimEntity) msg.obj;
//                            if(!"0".equals(entity.getCount())){
//                                bundle.putBoolean("isLuckDraw",true);
//                            }
//                        }
//                    break;
//                case CheckoutDao.REQUEST_ERROR:
//                    break;
//            }
            startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
            super.handleMessage(msg);
        }
    };
    private  void  startPaymentStatusScreen(){
        Bundle bundle = new Bundle();
        bundle.putString("payment_status", "1");
        bundle.putString("orderNumber", lastrealorderid);
        bundle.putString("grand_total", grandTotal);
        bundle.putString("shipping_fee", shippingFee);
        bundle.putSerializable("discountBean", mDiscountBean);
        bundle.putSerializable("paymentSaveReturnEntity", paymentSaveReturnEntity);
        bundle.putInt("fromType",fromType);
        startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
    }

    public void closeDialog(){
        if(mDialog!=null) {
            mDialog.cancel();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //webView = new WebView(this);
        //setContentView(webView);
        setContentView(R.layout.activity_checkout_payment_redirect);
        initToolBar();
        initIntent();
        CheckoutDao mDao = new CheckoutDao(TAG, mHandler);
        if(paymentMethod==PAYMENT_CARD) {
            initWebVeiw();
        }else{
            startPaymentStatusScreen();
//            mDialog = JViewUtils.showProgressDialog(CheckoutPaymentRedirectActivity.this);
//            mDao.getLuckdraw(WhiteLabelApplication.getAppConfiguration().getUser().getSessionKey(), lastrealorderid);
        }
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.CHECKOUT));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
//        setLeftMenuClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }
    private void initWebVeiw() {
        String url_suffix="";
        if (JDataUtils.isEmpty(payment_type)) {
            url_suffix = "redirect";
        } else {
            url_suffix = "redirectmolpay";
        }
        webView = (WebView) findViewById(R.id.wv_checkout_payment_redirect);
//        mDialog= JViewUtils.showProgressDialog(CheckoutPaymentRedirectActivity.this);
        String url = REDIRECT_PAYMENT_URL + url_suffix + "?session_key=" + session_key + "&lastrealorderid=" + lastrealorderid + "&appid=1&usehlb=" + GlobalData.useHlb;
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        //webView.loadUrl(REDIRECT_PAYMENT_URL + url_suffix + "?session_key=" + session_key + "&lastrealorderid=" + lastrealorderid);

        //Permit javascript using AndroidInterfaceForJs.returnAndroid(param)
//        webView.addJavascriptInterface(new AndroidInterfaceForJs(this), "AndroidInterfaceForJs");

//        Map<String,String> extraHeaders = new HashMap<String, String>();
//        extraHeaders.put("Referer", "http-equiv=Content-Type content=application/x-www-form-urlencoded;charset=iso-8859-1");
        //webView.loadUrl(url, extraHeaders);

        webView.setWebViewClient(new WebViewClient() {
            private final String SUCCESS = "checkout/onepage/success/";
            private final String FAILD = "checkout/onepage/failure/";

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (DISABLE_SSL_CHECK_FOR_TESTING) {
                    handler.proceed();// Ignore SSL certificate errors
                }else{
                    super.onReceivedSslError(view,handler,error);
                    handler.cancel();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    try {
                        if(!isSuccess) {
                            if (url.indexOf(SUCCESS) != -1) {
                                isSuccess=true;
                                startPaymentStatusScreen();
                                return false;
                            } else if (url.indexOf(FAILD) != -1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("payment_status", "0");
                                bundle.putString("orderNumber", lastrealorderid);
                                bundle.putSerializable("discountBean", mDiscountBean);
                                bundle.putInt("fromType",fromType);
                                startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
                                return false;
                            }
                        }
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
                return false;
            }
        });
        webView.postUrl(url, null/*EncodingUtils.getBytes(params, "BASE64")*/);
    }

    private void initIntent() {
        Bundle bundle = getIntent().getExtras();
        fromType=getIntent().getIntExtra("fromType", 0);
        paymentMethod=bundle.getInt("paymentMethod", PAYMENT_CARD);
         session_key = bundle.getString("session_key");
         payment_type = bundle.getString("payment_type");
        grandTotal = bundle.getString("grand_total");
        shippingFee = bundle.getString("shipping_fee");
        lastrealorderid = bundle.getString("lastrealorderid");
        paymentSaveReturnEntity = bundle.getSerializable("paymentSaveReturnEntity");
        mDiscountBean= (ShoppingDiscountBean) bundle.getSerializable("discountBean");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) &&webView!=null&& webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

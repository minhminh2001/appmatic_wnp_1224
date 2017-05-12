package com.whitelabel.app.activity;

import android.app.Dialog;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
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
//    private Dialog mDialog;
    private WebView webView;
    private ShoppingDiscountBean mDiscountBean;
    private   String lastrealorderid;
    private String grandTotal;
    private String shippingFee;
    private Serializable paymentSaveReturnEntity;
    private boolean isSuccess;
    private String session_key;
    public  static final int PAYMENT_PALPAY =1;
    public  static final int PAYMENT_CARD=2;
    public int paymentMethod;
    private int fromType;
    private boolean DISABLE_SSL_CHECK_FOR_TESTING=false;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment_redirect);
        initToolBar();
//        initIntent();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            fromType = getIntent().getIntExtra("fromType", 0);
            paymentMethod = bundle.getInt("paymentMethod", PAYMENT_CARD);
            session_key = bundle.getString("session_key");
            grandTotal = bundle.getString("grand_total");
            shippingFee = bundle.getString("shipping_fee");
            lastrealorderid = bundle.getString("lastrealorderid");
            paymentSaveReturnEntity = bundle.getSerializable("paymentSaveReturnEntity");
            mDiscountBean = (ShoppingDiscountBean) bundle.getSerializable("discountBean");
        }
        if(paymentMethod==PAYMENT_PALPAY){
            startPaymentStatusScreen();
        }else {
            initWebVeiw();
        }
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.CHECKOUT));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
    }
    private void initWebVeiw() {
        webView = (WebView) findViewById(R.id.wv_checkout_payment_redirect);
        String url=String.format(GlobalData.creditCardPaymentUrl,session_key,lastrealorderid);
        WebSettings settings=webView.getSettings();
        JLogUtils.i("CheckoutPayment","url:"+url);
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        webView.setWebViewClient(new WebViewClient() {
            private final String SUCCESS = "checkout/onepage/success";
            private final String FAILD = "checkout/onepage/failure";

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (DISABLE_SSL_CHECK_FOR_TESTING) {
                    handler.proceed();
                }else{
                    super.onReceivedSslError(view,handler,error);
                    handler.cancel();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                JLogUtils.i("CheckoutPayment","url:"+url);
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

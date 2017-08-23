package com.whitelabel.app.ui.checkout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.BaseActivity;
import com.whitelabel.app.activity.CheckoutActivity;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.model.CheckoutPaymentSaveReturnEntity;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayPalPaymentActivity extends com.whitelabel.app.BaseActivity {
    @BindView(R.id.wv_checkout_payment_redirect)
    WebView wvCheckoutPaymentRedirect;
    private String mUrl;
    public final static String  PAYMENT_URL="payment_url";
    public final static String PAYMENT_ORDER_NUMBER="order_number";
    public final static String EXTRA_DATA="extra_bean";
    private String orderNumber;
    private CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);
        ButterKnife.bind(this);
        mUrl=getIntent().getStringExtra(PAYMENT_URL);
        paymentSaveReturnEntity= (CheckoutPaymentSaveReturnEntity) getIntent().getExtras().getSerializable(EXTRA_DATA);
        showProgressDialog();
        initToolbar();
        initWebView();
    }
    private void initWebView() {
        WebSettings settings=wvCheckoutPaymentRedirect.getSettings();
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(wvCheckoutPaymentRedirect, true);
        }
        orderNumber=getIntent().getStringExtra(PAYMENT_ORDER_NUMBER);
        wvCheckoutPaymentRedirect.setWebViewClient(webViewClient);
        wvCheckoutPaymentRedirect.loadUrl(mUrl,null);
        wvCheckoutPaymentRedirect.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>40){
                    closeProgressDialog();
                }
            }
        });
    }
    private WebViewClient  webViewClient=new WebViewClient(){
        private String paymentFaild="checkout/cart/";
        private String paymentSuccess="checkout/onepage/success/";
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            JLogUtils.i("ray","url:");
//            return false;
//        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JLogUtils.i("PayPalPaymentActivity","url:"+url);
            if(url.contains(paymentFaild)){
                startPaymentFaildActivity();
                finish();
            }


            if(url.contains(paymentSuccess)){
                startPaymentSuccessActivity();
                finish();
            }
            return false;
        }
    };
    private void startPaymentSuccessActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("payment_status", "1");
        bundle.putString(CheckoutPaymentStatusActivity.EXTRA_ORDER_NUMBER,orderNumber);
        bundle.putSerializable("paymentSaveReturnEntity",paymentSaveReturnEntity);
        startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
    }
    private void startPaymentFaildActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("payment_status", "0");
        startNextActivity(bundle, CheckoutPaymentStatusActivity.class, true);
    }



    private void initToolbar() {
        setTitle(getResources().getString(R.string.CHECKOUT));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}

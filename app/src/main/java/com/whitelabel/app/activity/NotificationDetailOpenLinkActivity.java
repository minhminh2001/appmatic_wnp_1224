package com.whitelabel.app.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JViewUtils;

public class NotificationDetailOpenLinkActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String attached_link = getIntent().getExtras().getString("attached_link");
        String title=getIntent().getExtras().getString("title");
        TextView tvTitle = (TextView) findViewById(R.id.ctvHeaderBarTitle);
        tvTitle.setText(title);
        webView= (WebView) findViewById(R.id.wv_view);
        View vBack = findViewById(R.id.ivHeaderBarMenu);
        vBack.setOnClickListener(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        mDialog=JViewUtils.showProgressDialog(NotificationDetailOpenLinkActivity.this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(mDialog!=null){
                    mDialog.cancel();
                }
                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl(attached_link);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.ivHeaderBarMenu:
                onBackPressed();
                break;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

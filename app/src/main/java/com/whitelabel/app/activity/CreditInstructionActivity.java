package com.whitelabel.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;

public class CreditInstructionActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener{
    private String data;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_credit_instruction);
        initToolbar();
        initIntent();
        initWebView();
    }


    private void initToolbar() {

        setTitle(getResources().getString(R.string.credit_instruction));
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }
    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch (id){ }
    }
    private void initIntent() {
        data=getIntent().getStringExtra("data");
    }
    private void initWebView() {
        mWebView= (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(false);//缩放
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent=new Intent(CreditInstructionActivity.this,RegisterToHelpCenter.class);
                intent.putExtra("helpCenter", 8);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
                return true;
            }
        });
        JLogUtils.d("jay","data="+data);
        JToolUtils.webViewFont(this, mWebView, data, 12.5f);

//        JToolUtils.webViewFont(CreditInstructionActivity.this, mWebView, data, "html/custom_web.html");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        try {
//            GaTrackHelper.getInstance().googleAnalyticsReportActivity(this,true);
//            GaTrackHelper.getInstance().googleAnalytics("Store Credit Instruction Screen", this);
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//        JLogUtils.i("googleGA_screen", "Store Credit Instruction Screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
        }catch (Exception ex) {
            ex.getStackTrace();
        }
    }
}

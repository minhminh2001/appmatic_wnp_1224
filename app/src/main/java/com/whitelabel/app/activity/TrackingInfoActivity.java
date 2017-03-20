package com.whitelabel.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.NetUtils;

public class TrackingInfoActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {
    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_URL = "url";
    private static final String TAG = "TrackingInfoActivity";
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mTrackingInfoUrl;
    private String mTitle;
    private Dialog mDialog;
    private View mConnectionLayout;
    private LinearLayout mTryAgain;

    @Override
    protected void onStart() {
        super.onStart();
//        GaTrackHelper.getInstance().googleAnalytics("Tracking Info Screen", this);
//        JLogUtils.i("googleGA_screen", "Tracking Info Screen Screen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_info);
        initIntent();
        initToolBar();
        initView();
        initWebView();
        loadData();
    }

    private void loadData() {
        if (NetUtils.isConnected(this)) {
            mDialog = JViewUtils.showProgressDialog(TrackingInfoActivity.this);
            mWebView.loadUrl(mTrackingInfoUrl);
        } else {
            mConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initIntent() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString(BUNDLE_TITLE) != null) {
            mTitle = bundle.getString(BUNDLE_TITLE);
        } else {
            mTitle = "";
        }
        mTrackingInfoUrl = bundle.getString(BUNDLE_URL);
    }

    private void initToolBar() {
        setTitle(mTitle);
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initWebView() {
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
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    view.reload();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                JLogUtils.d(TAG, "onPageStarted");
                mWebView.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                JLogUtils.d(TAG, "onPageFinished");
                super.onPageFinished(view, url);
                if (mDialog!=null&&mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mConnectionLayout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.INVISIBLE);
                if (mDialog!=null&&mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
            mDialog=null;
        }
    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.pb_address);
        mWebView = (WebView) findViewById(R.id.cwvDetail);
        mConnectionLayout = findViewById(R.id.connectionBreaks);
        mTryAgain = (LinearLayout) findViewById(R.id.try_again);
        mTryAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_again:
                mConnectionLayout.setVisibility(View.INVISIBLE);
                loadData();
                break;
        }
    }

    //重写Activity的onKeyDown事件，判断当用户按下“返回”按钮，webview返回上一页
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}

package com.whitelabel.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by imaginato on 2015/7/7.
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
        initWebViewSettings();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSettings();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebViewSettings();
    }

    private void initWebViewSettings() {
        WebSettings ws = getSettings();
        ws.setJavaScriptEnabled(true);
    }

    public final void setText(String text) {
        this.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);

    }


    public void setHtmlText(String text){
        this.loadData(text, "text/html", "UTF-8");

    }
}

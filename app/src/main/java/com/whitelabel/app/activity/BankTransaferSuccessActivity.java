package com.whitelabel.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomWebView;

public class BankTransaferSuccessActivity extends com.whitelabel.app.BaseActivity {
    private CustomWebView wvDesc;
    private TextView tvConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transafer_success);
        initView();
        initData();
        initToolBar();
        initListener();
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.bank_transafer_confirm));
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankTransaferSuccessActivity.this.finish();
            }
        });
    }
    private void initListener() {
        findViewById(R.id.ll_sc_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankTransaferSuccessActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
                finish();
            }
        });
    }

    private void initData() {
        String desc=getIntent().getStringExtra("desc");
        if(!TextUtils.isEmpty(desc)) {
            String content = JToolUtils.replaceFont(desc);
            //JToolUtils.webViewFont(GemfiveApplication.getInstance().getBaseContext(),wvDesc,content);
            wvDesc.setText(content);
        }
    }

    private void initView() {
        wvDesc= (CustomWebView) findViewById(R.id.tv_desc);
        tvConfirm= (TextView) findViewById(R.id.tv_confirm);
        tvConfirm.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(this));
        //initWebView();
    }
    private void initWebView() {
        wvDesc.getSettings().setJavaScriptEnabled(true);
        wvDesc.setWebViewClient(client);
    }
    private WebViewClient client=  new WebViewClient(){

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }catch (Exception ex){
                ex.getStackTrace();
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bank_transafer_success, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

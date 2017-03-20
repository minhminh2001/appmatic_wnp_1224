package com.whitelabel.app.qrscan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.RequestErrorHelper;

/**
 * Created by Arman on 2/8/2017.
 */

public class QrScanErrorActivity extends AppCompatActivity {

    private RequestErrorHelper requestErrorHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_error);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.qr_title));
        }

        //set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black000000));
        }

        View errorLayout = findViewById(R.id.ll_error_message);
        requestErrorHelper = new RequestErrorHelper(this, errorLayout);

        Intent intent = getIntent();
        if (intent != null) {
            IQrScanView.ErrorType type = (IQrScanView.ErrorType) intent.getSerializableExtra("errorType");
            int errorMessage = intent.getIntExtra("errorMessage", -1);
            showError(type, errorMessage);
        }
    }

    @Override
    public void onBackPressed() {
        backToScan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToScan();
                break;
            case R.id.action_cancel:
                setResult(RESULT_CANCELED);
                finish();
                overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backToScan() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.anim_enter_slide_left_to_right, R.anim.anim_exit_slide_left_to_right);
    }

    private void showError(IQrScanView.ErrorType type, int errorMessage) {
        switch (type) {
            case Connection:
                requestErrorHelper.showConnectionBreaks(errorMessage);
                requestErrorHelper.setResponseListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToScan();
                    }
                });
                requestErrorHelper.setResponseButtonText(getString(R.string.refresh));
                break;
            case Qr:
                requestErrorHelper.setErrorImage(R.drawable.qr_error);
                requestErrorHelper.setHeaderMessage(getString(R.string.qr_error));
                requestErrorHelper.setSubheaderMessage(getString(R.string.qr_error_long));
                requestErrorHelper.setResponseListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToScan();
                    }
                });
                requestErrorHelper.setResponseButtonText(getString(R.string.scan_again));
                break;
        }
    }
}

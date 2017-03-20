package com.whitelabel.app.qrscan;

import android.app.Activity;

import com.whitelabel.app.R;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.MaterialDialog;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by Arman on 2/17/2017.
 */

public class CustomCaptureManager extends CaptureManager {

    private boolean destroyed = false;
    private Activity activity;
    private MaterialDialog dialog;

    public CustomCaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        super(activity, barcodeView);
        this.activity = activity;
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void displayFrameworkBugMessageAndExit() {
        if (activity.isFinishing() || this.destroyed) {
            return;
        }

        dialog = JViewUtils.showPermissionDialog(activity, activity.getString(R.string.permission_error_title), activity.getString(R.string.permission_error_camera_content), QrScanActivity.REQUESTCODE_SETTINGS, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}

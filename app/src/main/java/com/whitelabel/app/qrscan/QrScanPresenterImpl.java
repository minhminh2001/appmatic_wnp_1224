package com.whitelabel.app.qrscan;

import android.text.TextUtils;

import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

/**
 * Created by Arman on 1/17/2017.
 */

public class QrScanPresenterImpl implements IQrScanPresenter {

    private static final String TYPE_PDP = "1";
    private static final String TYPE_CURATION = "2";
    private IQrScanView qrScanView;
    private static String TAG = "QrScanPresenterImpl";

    public QrScanPresenterImpl(IQrScanView view) {
        qrScanView = view;
    }

    @Override
    public void onQrScanDetected(String result) {
        JLogUtils.i(TAG, result);

        if (!TextUtils.isEmpty(result) && JDataUtils.isValidGemfiveUrl(result)) {
            String id = JDataUtils.extractId(result);
            String productType = JDataUtils.extractType(result);

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(productType)) {
                qrScanView.hideProgressBar();
                qrScanView.showErrorLayout(IQrScanView.ErrorType.Qr, null);
            } else if (productType.equals(TYPE_PDP)) {
                qrScanView.getProductDetails(id);
            } else {
                qrScanView.getCurationDetails(id);
            }
        } else {
            qrScanView.hideProgressBar();
            qrScanView.showErrorLayout(IQrScanView.ErrorType.Qr, null);
        }
    }
}

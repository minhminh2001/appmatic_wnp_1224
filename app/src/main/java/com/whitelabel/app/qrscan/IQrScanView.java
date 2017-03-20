package com.whitelabel.app.qrscan;

import android.os.Message;
import android.support.annotation.Nullable;

/**
 * Created by Arman on 1/17/2017.
 */

public interface IQrScanView {

    enum ErrorType {
        Connection,
        Qr
    }

    void showProgressBar();
    void hideProgressBar();
    void showErrorLayout(ErrorType errorType, @Nullable Message message);
    void getProductDetails(String id);
    void getCurationDetails(String id);
}

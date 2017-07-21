package com.whitelabel.app.utils;

import android.content.Context;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.network.BaseHttp;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/10/6.
 */
public class RequestErrorHelper {
    private WeakReference<Context> mContent;
    private WeakReference<View> mConnectionLayout;

    public RequestErrorHelper(Context context) {
        mContent = new WeakReference<>(context);
    }

    public RequestErrorHelper(Context context, View connectionLayout) {
        mContent = new WeakReference<>(context);
        mConnectionLayout = new WeakReference<>(connectionLayout);
    }

    public void setConnectionBreaksView(View connectionLayout) {
        mConnectionLayout = new WeakReference<>(connectionLayout);
    }

    public void showNetWorkErrorToast(Message msg) {
        if (isServerError(msg) && mContent.get() != null) {
            //show server error
            Toast.makeText(mContent.get(), mContent.get().getResources().getString(R.string.Global_Error_Server), Toast.LENGTH_LONG).show();
        } else {
            //show network error
            Toast.makeText(mContent.get(), mContent.get().getResources().getString(R.string.Global_Error_Internet), Toast.LENGTH_LONG).show();
        }
    }


    public void  showNetWorkErrorToast(){
        Toast.makeText(mContent.get(), mContent.get().getResources().getString(R.string.Global_Error_Internet), Toast.LENGTH_LONG).show();
    }

    public void showConnectionBreaks(Message msg) {
        if (mConnectionLayout.get() != null) {
            mConnectionLayout.get().setVisibility(View.VISIBLE);
            if (isServerError(msg)) {
                showServerError();
            } else {
                showNetError();
            }
        }
    }

    public void showConnectionBreaks(int errorMessage) {
        if (mConnectionLayout.get() != null) {
            mConnectionLayout.get().setVisibility(View.VISIBLE);
            if (isServerError(errorMessage)) {
                showServerError();
            } else {
                showNetError();
            }
        }
    }

    //判断是否是服务错误
    public static boolean isServerError(Message msg) {
        if (msg == null) {
            return false;
        }
        return msg.arg2 == BaseHttp.ERROR_TYPE_SERVER;
    }

    public static boolean isServerError(int errorMessage) {
        return errorMessage == BaseHttp.ERROR_TYPE_SERVER;
    }

    public void setErrorImage(int resource) {
        ((ImageView) mConnectionLayout.get().findViewById(R.id.iv_error)).setImageDrawable(ContextCompat.getDrawable(mContent.get(), resource));
    }

    public void setHeaderMessage(String message) {
        ((TextView) mConnectionLayout.get().findViewById(R.id.ctv_error_header)).setText(message);
    }

    public void setSubheaderMessage(String message) {
        ((TextView) mConnectionLayout.get().findViewById(R.id.ctv_error_subheader)).setText(message);
    }

    public void setResponseButtonText(String message) {
        ((Button) mConnectionLayout.get().findViewById(R.id.btn_try_again)).setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        ((Button) mConnectionLayout.get().findViewById(R.id.btn_try_again)).setText(message);
    }

    public void setResponseImage(int resource) {
        ((ImageView) mConnectionLayout.get().findViewById(R.id.iv_try_again)).setImageDrawable(ContextCompat.getDrawable(mContent.get(), resource));
    }

    public void setResponseListener(View.OnClickListener listener) {
        mConnectionLayout.get().findViewById(R.id.try_again).setOnClickListener(listener);
    }

    public void showCancelButton(boolean show) {
        mConnectionLayout.get().findViewById(R.id.iv_close).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setCancelListener(View.OnClickListener listener) {
        mConnectionLayout.get().findViewById(R.id.iv_close).setOnClickListener(listener);
    }

    private void showServerError() {
        setErrorImage(R.drawable.s_server);
        setHeaderMessage(mContent.get().getString(R.string.server_lost));
        setSubheaderMessage(mContent.get().getString(R.string.wait_service));
    }

    private void showNetError() {
        setErrorImage(R.drawable.s_wifi);
        setHeaderMessage(mContent.get().getString(R.string.connection_lost));
        setSubheaderMessage(mContent.get().getString(R.string.check_internet));
    }


}

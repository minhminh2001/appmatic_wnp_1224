package com.whitelabel.app.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.whitelabel.app.R;
import com.whitelabel.app.dao.HelpCenterDao;
import com.whitelabel.app.model.SVRAppserviceCmsCmsPageReturnEntity;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/11/24.
 */
public class RegisterToHelpCenter extends com.whitelabel.app.BaseActivity  implements View.OnClickListener {
    private Dialog mDialog;
    private final String TAG = "RegisterToHelpCenter";
    private CustomWebView cwvDetail;
    private HelpCenterDao mDao;
    private int contentType = -1;
    private DataHandler mHandler;
    private static final class DataHandler extends android.os.Handler {
        private final WeakReference<RegisterToHelpCenter> mActivity;

        public DataHandler(RegisterToHelpCenter activity) {
            mActivity = new WeakReference<RegisterToHelpCenter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null){
                return;
            }
            if(mActivity.get().mDialog!=null&&mActivity.get().mDialog.isShowing()){
                mActivity.get().mDialog.dismiss();
            }
            switch (msg.what){
                case HelpCenterDao.REQUEST_GETDATA:
                    if(msg.arg1==HelpCenterDao.RESPONSE_SUCCESS){
                            SVRAppserviceCmsCmsPageReturnEntity returnEntity = (SVRAppserviceCmsCmsPageReturnEntity) msg.obj;
                            String content= JToolUtils.replaceFont(returnEntity.getContent());
                             mActivity.get().cwvDetail.setText(content);
                    }
                    break;
                case HelpCenterDao.REQUEST_ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler=new DataHandler(this);
        mDao=new HelpCenterDao(TAG,mHandler);
        setContentView(R.layout.fragment_home_helpcenterdetail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            contentType=bundle.getInt("helpCenter");
        }
        cwvDetail = (CustomWebView)findViewById(R.id.cwvDetail);
        initViewTitle();
        initViewContent();

    }
    private void onClickLeftMenu(){
        setResult(101, this.getIntent().putExtras(new Bundle()));
       onBackPressed();
    }
    private void initToolBar(String title){
        setTitle(title);
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle bundle = new Bundle();
            setResult(101, this.getIntent().putExtras(bundle));
            super.onBackPressed();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initViewTitle() {
        String titlestring = null;
        if (0 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_aboutus2);
        } else if (1 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_privacypolicy2);
        } else if (2 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_termsofus2);
        } else if (3 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_howtobuy2);
        } else if (4 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_payments2);
        } else if (5 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_shippingdelivery2);
        } else if (6 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_ordertracking2);
        } else if (7 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_cancellationsreturns2);
        } else if (8 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_gemcashvoucher2);
        } else if (9 == contentType) {
            titlestring = getString(R.string.home_helpcenter_list_customerservice2);
        }
        initToolBar(titlestring);
    }

    private void initViewContent() {
       mDialog= JViewUtils.showProgressDialog(RegisterToHelpCenter.this);
        String content = null;
        if (0 == contentType) {
            content = "about-us-mobile";
        } else if (1 == contentType) {
            content = "privacy-policy-mobile";
        } else if (2 == contentType) {
            content = "terms-of-use-mobile";
        } else if (3 == contentType) {
            content = "how-to-buy-mobile";
        } else if (4 == contentType) {
            content = "payments-mobile";
        } else if (5 == contentType) {
            content = "shipping-delivery-mobile";
        } else if (6 == contentType) {
            content = "order-tracking-mobile";
        } else if (7 == contentType) {
            content = "cancellation-returns-mobile";
        } else if (8 == contentType) {
            content = "store-credit-gemcash-voucher-mobile";
        } else if (9 == contentType) {
            content = "customer-service-mobile";
        }

        mDao.loadDataFromServer(content);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}

package com.whitelabel.app.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.*;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.GetServiceEntity;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomEdit;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/11/2.
 */
public class HideFunctionLoginActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {


    private CustomEdit userName, password;
    private String TAG;
    private ProductDao mDao;
    private Dialog mDialog;

    private static final class DataHandler extends Handler {
        private final WeakReference<HideFunctionLoginActivity> mActivity;

        public DataHandler(HideFunctionLoginActivity activity) {
            mActivity = new WeakReference<HideFunctionLoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().mDialog.dismiss();
            switch (msg.what) {
                case ProductDao.REQUEST_GETSERVICE:
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        GetServiceEntity entity = (GetServiceEntity) msg.obj;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entity", entity);
                        mActivity.get().startNextActivity(bundle, HideFunctionActivity.class, false);
                    } else {
                        String msgStr = String.valueOf(msg.obj);
                        Toast.makeText(mActivity.get(), msgStr, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
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
        setContentView(R.layout.activity_hide_login);
        initToolBar();
        DataHandler mHandler = new DataHandler(this);
        TAG = HideFunctionLoginActivity.this.getClass().getSimpleName();
        TextView cancel = (TextView) findViewById(R.id.cancel);
        TextView okay = (TextView) findViewById(R.id.okay);
        cancel.setOnClickListener(this);
        okay.setOnClickListener(this);
        userName = (CustomEdit) findViewById(R.id.userName);
        password = (CustomEdit) findViewById(R.id.password);
        TextView currentService = (TextView) findViewById(R.id.currentService);
        currentService.setText(GlobalData.serviceRequestUrl);
        ImageView back = (ImageView) findViewById(R.id.vHeaderBarBack);
        findViewById(R.id.rl_vHeaderBarBack).setOnClickListener(this);
        back.setOnClickListener(this);
        mDao = new ProductDao(TAG, mHandler);
    }

    private void initToolBar() {
        setTitle(getResources().getString(R.string.select_server));
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                userName.setText("");
                password.setText("");
                this.finish();
//                overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);

                break;
            case R.id.okay:
                String userNameStr = userName.getText();
                String passWordStr = password.getText();
                mDao.getServiceList(userNameStr, passWordStr);
                mDialog = JViewUtils.showProgressDialog(this);
                break;
            case R.id.rl_vHeaderBarBack:
            case R.id.vHeaderBarBack:
                this.onBackPressed();
                break;
        }
    }
}

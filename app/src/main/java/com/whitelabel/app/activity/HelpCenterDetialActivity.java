package com.whitelabel.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.dao.HelpCenterDao;
import com.whitelabel.app.model.SVRAppserviceCmsCmsPageReturnEntity;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/7/7.
 */
public class HelpCenterDetialActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener{
    private CustomWebView cwvCMS;
    private ProgressBar wishlistPB;
    private HelpCenterDao dao;
    private String TAG;
    private DataHandler mHandler;

    private static class DataHandler extends Handler{
        private final WeakReference<HelpCenterDetialActivity> mActivity;
        public DataHandler(HelpCenterDetialActivity activity) {
            mActivity = new WeakReference<HelpCenterDetialActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HelpCenterDao.REQUEST_GETDATA:
                    if(msg.arg1==HelpCenterDao.RESPONSE_SUCCESS){
                        SVRAppserviceCmsCmsPageReturnEntity returnEntity = (SVRAppserviceCmsCmsPageReturnEntity) msg.obj;
                        String content= JToolUtils.replaceFont(returnEntity.getContent());
                        mActivity.get().cwvCMS.setText(content);
                        mActivity.get().wishlistPB.setVisibility(View.INVISIBLE);
                    }else{
                        String msgs=String.valueOf(msg.obj);
                        Toast.makeText(mActivity.get(), msgs, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HelpCenterDao.REQUEST_ERROR:
                    String message=String.valueOf(msg.obj);
                    JViewUtils.showErrorToast(mActivity.get(), message);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG =HelpCenterDetialActivity.this.getClass().getSimpleName();
        setContentView(R.layout.activity_helpcenter_detail);

        mHandler=new DataHandler(this);
        dao=new HelpCenterDao(TAG,mHandler);

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String title=bundle.getString("title");
        String content = bundle.getString("content");

        initToolBar(title);
        wishlistPB= (ProgressBar) findViewById(R.id.pb_address);
        cwvCMS= (CustomWebView) findViewById(R.id.cwvDetail);

        dao.loadDataFromServer(content);
    }

    private void initToolBar(String title) {
        setTitle(title);
        setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onDestroy() {
        dao.cancelHttpByTag(TAG);
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }

    }

}

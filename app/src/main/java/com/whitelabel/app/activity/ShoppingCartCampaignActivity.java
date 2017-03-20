package com.whitelabel.app.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.adapter.ShoppingCartCampaignAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.ShoppingCartCampaignListEntity;
import com.whitelabel.app.model.ShoppingCartCampaignListEntityReturn;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ShoppingCartCampaignActivity extends com.whitelabel.app.BaseActivity {

    //  @ViewInject(R.id.gv_campaign)
    private GridView gridView;
    private View my_text1;
    private String campaignProductId;
    private Dialog mDialog;
    private ArrayList<ShoppingCartCampaignListEntity> list;
    private ShoppingCartCampaignAdapter adapter;
    private ProductDao mProductDao;
    private String TAG;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_campaign);
        initToolbar();
        DataHandler dataHandler = new DataHandler(this);
        TAG = this.getClass().getSimpleName();
        JLogUtils.d(TAG, "onCreate");
        mImageLoader = new ImageLoader(this);
        mProductDao = new ProductDao(TAG, dataHandler);
        my_text1 = findViewById(R.id.my_text1);
        gridView = (GridView) findViewById(R.id.gv_campaign);

        initData();
    }

    private void initToolbar() {

        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        campaignProductId = bundle.getString("productId");
        String title = bundle.getString("popText");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        list = new ArrayList<>();
        adapter = new ShoppingCartCampaignAdapter(this, list, mImageLoader);
        gridView.setAdapter(adapter);
        mDialog = JViewUtils.showProgressDialog(this);
        sendRequestToGetList();
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<ShoppingCartCampaignActivity> mActivity;

        public DataHandler(ShoppingCartCampaignActivity activity) {
            mActivity = new WeakReference<>(activity);
        }



        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case ProductDao.REQUEST_CAMPAGINPRODUCT:
                    if (mActivity.get().mDialog != null) {
                        mActivity.get().mDialog.cancel();
                    }
                    if (msg.arg1 == ProductDao.RESPONSE_SUCCESS) {
                        mActivity.get().my_text1.setVisibility(View.VISIBLE);
                        ShoppingCartCampaignListEntityReturn cartCampaignListEntityReturn = (ShoppingCartCampaignListEntityReturn) msg.obj;
                        if (cartCampaignListEntityReturn.getStatus() == 1) {
                            mActivity.get().initCampaignList(cartCampaignListEntityReturn.getList());
                        }
                    } else {
                        mActivity.get().my_text1.setVisibility(View.VISIBLE);
                        String errorMsg = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorMsg, 2000)) {
                            Toast.makeText(mActivity.get(), errorMsg + "", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ProductDao.REQUEST_ERROR:
                    if (mActivity.get().mDialog != null) {
                        mActivity.get().mDialog.cancel();
                    }
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);

                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void sendRequestToGetList() {
        mProductDao.getCampaignProduct(GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey());
    }

    private void initCampaignList(ArrayList<ShoppingCartCampaignListEntity> list_webservice) {
        list.addAll(list_webservice);
        adapter.notifyDataSetChanged();
    }

    public String getCampaignProductId() {
        return campaignProductId;
    }


}

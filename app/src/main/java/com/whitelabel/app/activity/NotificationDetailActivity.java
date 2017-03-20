package com.whitelabel.app.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.NotificationDao;
import com.whitelabel.app.model.NotificationCell;
import com.whitelabel.app.model.NotificationReceivedEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JTimeUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.SendBoardUtil;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;

public class NotificationDetailActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener {

    private CustomTextView webView_content;
    private Button btnOpen;
    private String attached_link;
    private String TAG = this.getClass().getSimpleName();
    private NotificationDao mDao;
    private TextView tvNotiTitle, tvTime;
    private Dialog mDialog;
    private NotificationCell mBean;
    private ImageView ivNotificationImage;
    private boolean isUnRead;
    private View connectionLayout;
    private RequestErrorHelper requestErrorHelper;
    private LinearLayout tryAgain;
    private String mExpiryTime;
    private LinearLayout mLlBottomBar;
    public static final int RESPONSECODE = 1000;
    private ImageLoader mImageLoader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        initToolBar();
        mImageLoader = new ImageLoader(this);
        ivNotificationImage = (ImageView) findViewById(R.id.iv_notification_img);
        webView_content = (CustomTextView) findViewById(R.id.webview_Detail);
        btnOpen = (Button) findViewById(R.id.notification_detail_open);
        tvNotiTitle = (TextView) findViewById(R.id.tv_nitititle);
        tvTime = (TextView) findViewById(R.id.tv_notification_time);
        mLlBottomBar = (LinearLayout) findViewById(R.id.llBottomBar);
        mLlBottomBar.setOnClickListener(this);
//        ivBack.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        dataHandler = new DataHandler(this);
        mDao = new NotificationDao(TAG, dataHandler);
        initData();

        connectionLayout = findViewById(R.id.connectionBreaks);
        requestErrorHelper=new RequestErrorHelper(this,connectionLayout);
        tryAgain = (LinearLayout) findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionLayout.setVisibility(View.GONE);
                initData();
            }
        });

    }


    private void initToolBar() {
        setTitle("");
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public  void setUiData(NotificationCell notificationCell){
        mLlBottomBar.setVisibility(View.VISIBLE);
        tvTime.setText(notificationCell.getSent_at());
        tvNotiTitle.setText(notificationCell.getTitle());
        if (!TextUtils.isEmpty(notificationCell.getTitle())) {
            setTitle(notificationCell.getTitle());
        }

        //big image
        if (!TextUtils.isEmpty(notificationCell.getBanner())) {
            ivNotificationImage.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivNotificationImage.getLayoutParams();
            int height = (int) ((JScreenUtils.getScreenWidth(NotificationDetailActivity.this) - JDataUtils.dp2px(NotificationDetailActivity.this, 40)) * 0.55);
            int width = (JScreenUtils.getScreenWidth(NotificationDetailActivity.this) - JDataUtils.dp2px(NotificationDetailActivity.this, 40));
            params.height = height;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            ivNotificationImage.setLayoutParams(params);
            JImageUtils.downloadImageFromServerByUrl(NotificationDetailActivity.this, mImageLoader, ivNotificationImage, notificationCell.getBanner(), width, height);
        }
        webView_content.setText(notificationCell.getContent());
        attached_link = notificationCell.getAttached_link();
        mExpiryTime = notificationCell.getExpiryTime();
        JLogUtils.d(TAG,"expiry_date="+ mExpiryTime);
        if (!TextUtils.isEmpty(mExpiryTime)) {
            long expiryDateLongValue = JTimeUtils.getLongOfTime(mExpiryTime);
            if (expiryDateLongValue != -1) {
                long currentDateLongValue= System.currentTimeMillis();
                if(expiryDateLongValue<=currentDateLongValue){
                    btnOpen.setEnabled(false);
                    btnOpen.setBackgroundColor(getResources().getColor(R.color.greyCCCCCC));
                    btnOpen.setTextColor(getResources().getColor(R.color.white));
                    btnOpen.setText("EXPIRED");
                }
            }
        }
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<NotificationDetailActivity> mActivity;

        public DataHandler(NotificationDetailActivity activity) {
            mActivity = new WeakReference<NotificationDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            if (mActivity.get().mDialog != null) {
                mActivity.get().mDialog.cancel();
            }
            switch (msg.what) {
                case NotificationDao.REQUEST_NOTIFICATIONREAD:
                    mActivity.get().connectionLayout.setVisibility(View.GONE);
                    if (msg.arg1 == NotificationDao.RESPONSE_SUCCESS) {
                        SendBoardUtil.sendNotificationBoard(mActivity.get(), SendBoardUtil.READCODE, null);
                        JLogUtils.i("russell->notification->detail->read", "successfully");
                    } else {
                        JLogUtils.i("russell->notification->detail->read", "unsuccessfully");
                    }
                    break;
                case NotificationDao.REQUEST_NOTIFICATIONDETAIL:
                    mActivity.get().connectionLayout.setVisibility(View.GONE);
                    if (msg.arg1 == NotificationDao.RESPONSE_SUCCESS) {
                        mActivity.get().mBean = (NotificationCell) msg.obj;
                        if (mActivity.get().isUnRead) {
                            SendBoardUtil.sendNotificationBoard(mActivity.get(), SendBoardUtil.READFLAG, mActivity.get().mBean.getItems_id());
                        }
                        mActivity.get().setUiData(mActivity.get().mBean);
                    } else {
                        String errorMsg = String.valueOf(msg.obj);
                        JViewUtils.showErrorToast(mActivity.get(), errorMsg);
                    }
                    break;
                case NotificationDao.REQUEST_ERROR:
                    mActivity.get().requestErrorHelper.showConnectionBreaks(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    String itemId = "";
    private NotificationReceivedEntity entity;
    private String notificationTitle;
    private DataHandler dataHandler;

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mDialog = JViewUtils.showProgressDialog(NotificationDetailActivity.this);
        String device_token= GemfiveApplication.getPhoneConfiguration().getRegistrationToken();
        if (bundle != null && !JDataUtils.isEmpty(bundle.getString("where"))) {
            entity = (NotificationReceivedEntity) bundle.getSerializable("data");
//            try {
//                FirebaseEventUtils.getInstance().customizedNotificationOpen(NotificationDetailActivity.this, entity.getTitle());
//            } catch (Exception ex) {
//                ex.getStackTrace();
//            }
            if (entity != null) {
                notificationTitle = entity.getTitle();
            }
            itemId = entity.getItems_id();
            if (!TextUtils.isEmpty(entity.getTitle())) {
                setTitle(entity.getTitle());
            }
            isUnRead = true;
            mDao.getNotificationDetail(GemfiveApplication.getAppConfiguration().getUser() == null ? null : GemfiveApplication.getAppConfiguration().getUser().getSessionKey(), itemId, "1",device_token);
        } else if (bundle != null) {
            NotificationCell notificationCell = (NotificationCell) bundle.getSerializable("data");
//            try {
//                FirebaseEventUtils.getInstance().customizedInAppNotificationOpen(NotificationDetailActivity.this, notificationCell.getTitle());
//            } catch (Exception ex) {
//                ex.getStackTrace();
//            }
            isUnRead = notificationCell.getUnread() == 1 ? true : false;
            if (!TextUtils.isEmpty(notificationCell.getTitle())) {
                setTitle(notificationCell.getTitle());
                notificationTitle = notificationCell.getTitle();
            }
            itemId = notificationCell.getItems_id();
            mDao.getNotificationDetail(GemfiveApplication.getAppConfiguration().getUser() == null ? null : GemfiveApplication.getAppConfiguration().getUser().getSessionKey(), itemId, "1","");
        }

        try {
            NotificationManager manger = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manger.cancel(Integer.parseInt(itemId));
        } catch (Exception ex) {
            ex.getStackTrace();
        }

//        try {
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Notification",
//                    "Open Notification Detail",
//                    notificationTitle,
//                    Long.valueOf(itemId));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_detail_open:
//                GaTrackHelper.getInstance().googleAnalyticsEvent("Notification",
//                        "Notification CTA",
//                        notificationTitle,
//                        Long.valueOf(itemId));
                if (mBean != null && mBean.getAttached_link_type() == 1) {
                    if (mBean.getInternal_type() == 1 && mBean.getCategory() != null) {
                        if ("0".equals(mBean.getCategory().getSecondCategory()) && "0".equals(mBean.getCategory().getThirdCategory())) { //1级菜单
                            Intent intent = new Intent(NotificationDetailActivity.this, HomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("categoryId", mBean.getCategory().getFirstCategory());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_righttoleft,
                                    R.anim.exit_righttoleft);
                            finish();
                        } else {//2级菜单
                            Intent intent = new Intent();
                            intent.setClass(NotificationDetailActivity.this, ProductListActivity.class);
                            intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_MAINCATEGOTY);
                            intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_CATEGORY);
                            intent.putExtra(ProductListActivity.INTENT_DATA_CATEGORYID, mBean.getCategory().getAndroidTree());
                            intent.putExtra("categoryId", mBean.getCategory().getThirdCategory());
                            NotificationDetailActivity.this.startActivity(intent);
                            NotificationDetailActivity.this.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                        }
                    } else if (mBean.getInternal_type() == 2) {
                        if ("0".equals(mBean.getActive())) {
                            Intent intent = new Intent(NotificationDetailActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_righttoleft,
                                    R.anim.exit_righttoleft);
                            finish();
                        } else {
                            Intent intent = new Intent(NotificationDetailActivity.this, CurationActivity.class);
                            intent.putExtra(CurationActivity.EXTRA_CURATION_ID, mBean.getLandingPageId());
                            startActivity(intent);
                        }
                    } else if (mBean.getInternal_type() == 3) {
                        if ("0".equals(mBean.getActive())) {
                            Intent intent = new Intent(NotificationDetailActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_righttoleft,
                                    R.anim.exit_righttoleft);
                            finish();
                        } else {
                            Intent it = new Intent(NotificationDetailActivity.this, ProductActivity.class);
                            it.putExtra("productId", mBean.getProductId());
                            startActivity(it);
                            overridePendingTransition(R.anim.enter_righttoleft,
                                    R.anim.exit_righttoleft);
                        }
                    }
                } else if (mBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("attached_link", attached_link);
                    bundle.putString("title", mBean.getTitle());
                    startNextActivity(bundle, NotificationDetailOpenLinkActivity.class, false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDao.cancelHttpByTag(TAG);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, true);
//        GaTrackHelper.getInstance().googleAnalytics("Notification detail screen", this);
//        JLogUtils.i("googleGA_screen", "Notification detail screen");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        GaTrackHelper.getInstance().googleAnalyticsReportActivity(this, false);
    }
}

package com.whitelabel.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.adapter.ShareIntentListAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.model.FacebookStoryEntity;

import java.util.List;

/**
 * Created by ray on 2015/9/25.
 */
public class ShareUtil {
    private String title;
    private String content;
    private String imageUrl;
    private String webUrl;
    private Activity mContext;
    private String FB_ERROR_NOINTERNET;
    private String FB_ERROR_UNINSTALLED;
    private String facebookDesc;
    private String caption;
    private List<ResolveInfo> activityList;
    public ShareUtil(Activity context, String title, String content) {
        mContext = context;
        this.title = title;
        this.content = content;
        init();

    }

    private void init() {

        PackageManager pm = mContext.getPackageManager();
        Intent clipboardIntent = new Intent(Intent.ACTION_ALL_APPS);
        clipboardIntent.setType("text/plain");
        List<ResolveInfo> activityList1 = pm.queryIntentActivities(clipboardIntent, 0);


        FB_ERROR_NOINTERNET = mContext.getResources().getString(R.string.facebook_error_nointernet);
        FB_ERROR_UNINSTALLED = mContext.getResources().getString(R.string.facebook_error_notinstalled);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        activityList = pm.queryIntentActivities(sharingIntent, 0);
    }

    public ShareUtil(Activity context, String content) {
        mContext = context;
        this.content = content;
        init();
    }

    public ShareUtil(Activity activity, String title, String content, String imageUrl, String webUrl, String facebookDesc) {
        this.mContext = activity;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.webUrl = webUrl;
        this.facebookDesc = facebookDesc;
        caption = title;
        init();
        initFacebook();
    }


    public ShareUtil(Activity context, String title, String content, String imageUrl, String webUrl, String facebookDesc, String Caption) {
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.webUrl = webUrl;
        this.facebookDesc = facebookDesc;
        this.caption = Caption;
        init();
    }

    private String productId;
    public void show() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter((Activity) mContext, activityList.toArray());
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Share your link");
        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = (ResolveInfo) activityList.get(item);
//                try {
//                    //google追踪分享到某应用
//                    ProductActivity productActivity= (ProductActivity) mContext;
//                    if(productActivity.mProductDetailBean !=null){
//                       productId=productActivity.mProductDetailBean.getId();
//                    }
//                    JLogUtils.i("ShareUtil","info.loadLabel(mContext.getPackageManager()).toString():"+info.loadLabel(mContext.getPackageManager()).toString());
//                    GaTrackHelper.getInstance().googleAnalyticsEvent("Procduct Action",
//                            "Share Product",
//                            info.loadLabel(mContext.getPackageManager()).toString(),
//                            Long.valueOf(productId));
//                    JLogUtils.i("googleGA","shareGA"+ productId);
//                    FirebaseEventUtils.getInstance().allAppShare(mContext.getApplicationContext(),productId,info.loadLabel(mContext.getPackageManager()).toString());
////                    JLogUtils.i("googleGA","shareGA"+ info.loadLabel(mContext.getPackageManager()).toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                // if email shared by user
                if (info.activityInfo.packageName.contains("com.facebook.katana")) {
                    JLogUtils.i("share","info.activityInfo.packageName.contains(\"facebook\")");
                    //                shareFacebook();
                    FacebookStoryEntity entity = new FacebookStoryEntity();
                    entity.setApplicationName(GlobalData.appName);
                    entity.setName(title);
                    entity.setDescription(content);
                    entity.setLink(webUrl);//WebURL
                    entity.setCaption(facebookDesc);//facebookDesc
                    entity.setPicture(imageUrl);//ͼƬURL
                    JLogUtils.i("share","webUrl="+webUrl+" title="+title+" content="+content+" imageurl="+imageUrl+" facebookDesc"+facebookDesc);
                    JShareUtils.publishFacebookStoryByNativeApp(mContext, entity, shareDialog, shareHandler);
                    return;
                }
                // start respective activity
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, title+"\n"+webUrl);
                mContext.startActivity(intent);
            }// end onClick
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private  ShareDialog   shareDialog;
    private    CallbackManager    callbackManager;
    private void initFacebook() {
        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mContext);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                boolean fbed = checkFbInstalled();
                if ((result != null && result.getPostId() != null) || fbed) {
                    sendTrack();
//                    EasyTracker easyTracker = EasyTracker.getInstance(mContext);
//                    easyTracker.send(MapBuilder.createEvent("UI Action", // Event category (required)
//                                    "Facebook Share Button Pressed", // Event action (required)
//                                    "success", // Event label
//                                    null) // Event value
//                                    .build()
//                    );
                    if (!fbed) {
                        shareHandler.sendEmptyMessage(JShareUtils.HANDLER_WHAT_FACEBOOK_SUCCESS_OK);
                    }
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                shareHandler.sendEmptyMessage(JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_INIT);
            }
        });
    }
    private Handler mHandler=new Handler();
    private void sendTrack() {
        String email= GemfiveApplication.getAppConfiguration().getUserInfo(mContext).getEmail();
        String name=GemfiveApplication.getAppConfiguration().getUserInfo(mContext).getFirstName() + " " + GemfiveApplication.getAppConfiguration().getUserInfo(mContext).getLastName();
        new OtherDao("ShareUtil",mHandler).sendTrack(GemfiveApplication.getAppConfiguration().getUserInfo(mContext).getSessionKey(),name,email);
    }
    public Boolean checkFbInstalled() {
        PackageManager pm = mContext.getPackageManager();
        boolean flag = false;
        try {
            pm.getPackageInfo("com.facebook.katana",
                    PackageManager.GET_ACTIVITIES);
            flag = true;
        } catch (PackageManager.NameNotFoundException e) {
            flag = false;
        }
        return flag;
    }
    private Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_INIT) {

                JViewUtils.showToast(mContext, null, FB_ERROR_NOINTERNET);

            } else if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_UNINSTALLED) {

            } else if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_NOINTERNET) {
                JViewUtils.showToast(mContext, null, FB_ERROR_NOINTERNET);
            }
        }

    };
}

package com.whitelabel.app.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.CheckoutDao;
import com.whitelabel.app.model.FacebookStoryEntity;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.GetAnimCodeEntity;
import com.whitelabel.app.model.ShoppingDiscountBean;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JShareUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;

public class CheckoutPaymentStatusRightFragment extends BaseFragment  implements View.OnClickListener{

    private CheckoutPaymentStatusActivity checkoutPaymentStatusActivity;
    private CustomWebView wvHtml;
    private String FB_ERROR_NOINTERNET;
    private String FB_ERROR_UNINSTALLED;
    private String FB_SHARED_OK;
    private ShoppingDiscountBean mDiscountBean;
    private String orderNumber;
    private TextView tvShare;
    private TextView tvCheckOrder;
    private TextView tvContinueShopping;
    private TextView codeNumber,tvContinueShopping3,tvContinueShopping4;
//    private double grandTotal;
//    private double shippingFee;
//    private CheckoutPaymentSaveReturnEntity paymentSaveReturnEntity;
//    private View myBoxGroup;
//    private View showCode;
//        ,myText1,myText2,myText3;
//    private boolean animStop2=true;
//    private int windowWinth,windowHeight;
//    private  int OffsetY;
//    private  int OffsetX;
//    private  int magnifyX,magnifyY;
//    private int magnifyXTO;
//    private int magnifyYTO;
    private ImageView animImg;
//    private RelativeLayout rlHeaderBarMenu;
    private View animImgView;
    private View showCodeFailure;
    private View rlBackGroud;
    private int fromType=0;
    private View rlRoot;
    private View layout;
    private ShareHandler shareHandler;
    private String mImageUrl="";
    private String mTitle="";
    private String  mDescription="";
    private String  mLink="";
    private DataHandler dataHandler;
    private CheckoutDao mCheckoutDao;
    private String TAG;
//    private ImageView img1,img2,img3,img4,img5;
//    private ArrayList<ImageView> allImageVeiw;
//    private AnimHandler myHandler;
//    private boolean animStop=true;
//    private Animator alphAnim;
    private static class ShareHandler extends Handler{
        private final WeakReference<Activity> mActivity;
        private final WeakReference<CheckoutPaymentStatusRightFragment> mFragment;
        public ShareHandler (Activity activity,CheckoutPaymentStatusRightFragment fragment){
            mActivity=new WeakReference<Activity>(activity);
            mFragment=new WeakReference<CheckoutPaymentStatusRightFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_INIT) {
                if (mActivity.get() != null && mFragment.get().isAdded()) {
                    JViewUtils.showToast(mActivity.get(), null, mFragment.get().FB_ERROR_NOINTERNET);
                }
            } else if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_UNINSTALLED) {
                if (mActivity.get() != null && mFragment.get().isAdded()) {
                    JViewUtils.showToast(mActivity.get(), null, mFragment.get().FB_ERROR_UNINSTALLED);
                }
            } else if (msg.what == JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_NOINTERNET) {
                if (mActivity.get() != null && mFragment.get().isAdded()) {
                    JViewUtils.showToast(mActivity.get(), null, mFragment.get().FB_ERROR_NOINTERNET);
                }
            }else if(msg.what==JShareUtils.HANDLER_WHAT_FACEBOOK_SUCCESS_OK){
                if (mActivity.get() != null && mFragment.get().isAdded()) {
                    JViewUtils.showToast(mActivity.get(), null, mFragment.get().FB_SHARED_OK);
                }
            }
        }
    }
    private static class DataHandler extends Handler{
        private final WeakReference<CheckoutPaymentStatusActivity> mActivity;
        private  final WeakReference<CheckoutPaymentStatusRightFragment> mFragment;
        public DataHandler(CheckoutPaymentStatusActivity activity,CheckoutPaymentStatusRightFragment fragment){
            mActivity=new WeakReference<CheckoutPaymentStatusActivity>(activity);
            mFragment=new WeakReference<CheckoutPaymentStatusRightFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            switch (msg.what){
                case CheckoutDao.REQUEST_WINCODE:

                    if(msg.arg1==CheckoutDao.RESPONSE_SUCCESS){
                        mFragment.get().animImgView.setVisibility(View.GONE);
                        mFragment.get().animImg.setVisibility(View.GONE);
                        mFragment.get().rlBackGroud.setVisibility(View.VISIBLE);
                        //成功后将数据放到Entity中
                        GetAnimCodeEntity entity = (GetAnimCodeEntity) msg.obj;
                        JLogUtils.i("Allen", "  !!!!Success =" + entity.getStatus() + " message=" + entity.getMessage());
                        if(entity.getWon_type().equals("none")){
                            mFragment.get().rlBackGroud.setVisibility(View.INVISIBLE);
                            mFragment.get().showCodeFailure.setBackgroundResource(R.mipmap.sexangle);
                            mFragment.get().showCodeFailure.setVisibility(View.VISIBLE);
                        }else {
                            mFragment.get().codeNumber.setText(entity.getMessage());
                        }
                    }else {

                        mFragment.get().animImgView.setVisibility(View.GONE);
                        mFragment.get().animImg.setVisibility(View.GONE);
                        mFragment.get().rlBackGroud.setVisibility(View.INVISIBLE);
                        mFragment.get().showCodeFailure.setBackgroundResource(R.mipmap.sexangle);
                        mFragment.get().showCodeFailure.setVisibility(View.VISIBLE);
                    }
                    break;
                case CheckoutDao.REQUEST_ERROR:
                    mFragment.get().animImgView.setVisibility(View.GONE);
                    mFragment.get().animImg.setVisibility(View.GONE);
                    mFragment.get().rlBackGroud.setVisibility(View.INVISIBLE);
                    mFragment.get().showCodeFailure.setBackgroundResource(R.mipmap.sexangle);
                    mFragment.get().showCodeFailure.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TAG=getClass().getSimpleName();
        dataHandler=new DataHandler((CheckoutPaymentStatusActivity) getActivity(),this);
        mCheckoutDao=new CheckoutDao(TAG,dataHandler);
        shareHandler=new ShareHandler(getActivity(),CheckoutPaymentStatusRightFragment.this);
        FB_ERROR_NOINTERNET = getResources().getString(R.string.facebook_error_nointernet);
        FB_ERROR_UNINSTALLED = getResources().getString(R.string.facebook_error_notinstalled);
        FB_SHARED_OK = getResources().getString(R.string.facebook_success_ok);

        if (getArguments() != null){
            mDiscountBean = (ShoppingDiscountBean) getArguments().getSerializable("discountBean");
            fromType=getArguments().getInt("fromType");
        }
        AnimUtil.alpha_0_1_500(rlRoot);
        if(!TextUtils.isEmpty(checkoutPaymentStatusActivity.html)){
            String content=JToolUtils.replaceFont(checkoutPaymentStatusActivity.html);
            JToolUtils.webViewFont(WhiteLabelApplication.getInstance().getBaseContext(), wvHtml, content);
            //wvHtml.setText(checkoutPaymentStatusActivity.html);
        }

        if(mDiscountBean!=null&&mDiscountBean.getIsShare()==1){
            tvShare.setVisibility(View.VISIBLE);
            mImageUrl=mDiscountBean.getShareImage();
            mDescription=mDiscountBean.getShareDescription();
            mLink=mDiscountBean.getShareLink();
            mTitle=mDiscountBean.getShareTitle();
        }
//        CustomAnimtion();
//        initLuckDraw();
//
//        if(JStorageUtils.isShowAppRate(checkoutPaymentStatusActivity)&&!WhiteLabelApplication.delayShowAppRate) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    appRate();
//                }
//            }, 300);
//        }

        WhiteLabelApplication.getAppConfiguration().addToOrder(checkoutPaymentStatusActivity);

        if(checkoutPaymentStatusActivity.mGATrackTimeEnable) {
            GaTrackHelper .getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_PAYMENT,
                    checkoutPaymentStatusActivity.mGATrackTimeStart,
                    "Payment Success"
            );
            checkoutPaymentStatusActivity.mGATrackTimeEnable = false;
        }
    }

    private PopupWindow popupWindow;
    private TextView rateNow,askMeLater,noThanks;

//    public void appRate(){
//        LayoutInflater inflater= (LayoutInflater) checkoutPaymentStatusActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View popupWindowView=inflater.inflate(R.layout.popupwindow_app_rate, null);
//        popupWindow=new PopupWindow(popupWindowView,LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT,false);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        rateNow= (TextView) popupWindowView.findViewById(R.id.rate_now);
//        askMeLater= (TextView) popupWindowView.findViewById(R.id.ask_me_later);
//        noThanks= (TextView) popupWindowView.findViewById(R.id.no_thanks);
//        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//        rateNow= (TextView) popupWindowView.findViewById(R.id.rate_now);
//        rateNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JStorageUtils.notShowAppRate(checkoutPaymentStatusActivity);
//                popupWindow.dismiss();
//              //  mWebView.setVisibility(View.VISIBLE);
//              //  mWebView.loadUrl(getString(R.string.play_store_url));//需要翻墙
////                Intent intent = new Intent();
////                intent.setAction("android.intent.action.VIEW");
////                intent.addCategory("android.intent.category.DEFAULT");
////                intent.addCategory("android.intent.category.BROWSABLE");
////                intent.setData(Uri.parse(getString(R.string.play_store_url)));
////                startActivity(intent);
//
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(getString(R.string.play_store_url)));
//                startActivity(i);
//            }
//        });
//        noThanks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                JStorageUtils.notShowAppRate(checkoutPaymentStatusActivity);
//            }
//        });
//        askMeLater.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                WhiteLabelApplication.delayShowAppRate=true;
//                JStorageUtils.clickDelayShow(checkoutPaymentStatusActivity);
//            }
//        });
//    }
//    private void initLuckDraw() {
//        if (checkoutPaymentStatusActivity.isLuckDraw) {
//            myBoxGroup.setBackgroundResource(R.mipmap.choujiang);
//            myBoxGroup.setVisibility(View.VISIBLE);
//            MyAnimtion bb=new MyAnimtion();
//            Thread threadAnim=new Thread(bb);
//             threadAnim.start();
//        } else {

//
//        initFacebook();
//    }

    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    public Boolean checkFbInstalled() {
        PackageManager pm = checkoutPaymentStatusActivity.getPackageManager();
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


//    private void initFacebook() {
//        FacebookSdk.sdkInitialize(checkoutPaymentStatusActivity);
//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);
//        // this part is optional
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                boolean fbed = checkFbInstalled();
//                if ((result != null && result.getPostId() != null) || fbed) {
//                    sendTrack();
////                    GaTrackHelper.getInstance().googleAnalyticsEvent("Payment",
////                            "Facebook Share Button Pressed",
////                            "success",
////                            null);
//                    if (!fbed) {
//                        shareHandler.sendEmptyMessage(JShareUtils.HANDLER_WHAT_FACEBOOK_SUCCESS_OK);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                shareHandler.sendEmptyMessage(JShareUtils.HANDLER_WHAT_FACEBOOK_ERROR_INIT);
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
//    http://192.168.1.96/appservice/checkout/trackInformation

    private void sendTrack() {
        mCheckoutDao.sendTrack( WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getSessionKey(), WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getFirstName() + " " + WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getLastName(), WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getEmail());
//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getSessionKey());
//        parameters.put("name", WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getFirstName() + " " + WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getLastName());
//        parameters.put("email", WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getEmail());
//        TrackInformation shoppingCartListHandler = new TrackInformation(checkoutPaymentStatusActivity, parameters);
//        shoppingCartListHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//
//            }
//        });
    }


    private void shareFacebook(){
        String link = mLink;
        String picture =mImageUrl;
        String applicationname = getResources().getString(R.string.app_name);
        FacebookStoryEntity entity = new FacebookStoryEntity();
        entity.setLink(link);
        entity.setApplicationName(applicationname);
        entity.setDescription(mDescription);
        entity.setName(mTitle);
        entity.setCaption(mTitle);
        entity.setPicture(picture);
        JShareUtils.publishFacebookStoryByNativeApp(checkoutPaymentStatusActivity, entity, shareDialog, shareHandler);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checkoutPaymentStatusActivity = (CheckoutPaymentStatusActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    private boolean myClickBox=true;
    @Override
    public void onClick(View v) {
        rlBackGroud.setBackgroundResource(R.mipmap.sexangle);
//        animStop=false;
        initAnim init = new initAnim(v);
        new Handler().postDelayed(init, 50);
    }

    private void initWebView() {
        wvHtml.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
                return true;
            }
        });
    }



    public void startShoppingCart(){
        if(getActivity()!=null) {
            if (fromType == ShoppingCartBaseFragment.FROM_HOME) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_SHOPPINGCART);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                getActivity().finish();
            } else {
                Intent intent = new Intent(checkoutPaymentStatusActivity, ShoppingCartActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            getActivity().overridePendingTransition(R.anim.enter_lefttoright,
                    R.anim.exit_lefttoright);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_payment_status_right, container, false);
        layout=view.findViewById(R.id.rl_root);
        TextView tvOrderNumber = (TextView) view.findViewById(R.id.tv_checkout_payment_status_ordernumber);
        TextView tvEmail = (TextView) view.findViewById(R.id.tv_checkout_payment_status_email);
        tvEmail.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        tvShare= (TextView) view.findViewById(R.id.tv_share);
        rlRoot=view.findViewById(R.id.sv_content);
        rlBackGroud=view.findViewById(R.id.rlBackGroud);
        tvShare.setOnClickListener(this);
//        myBoxGroup=view.findViewById(R.id.myBoxGroup);
        ImageView tvGoToShoppingCart = (ImageView) view.findViewById(R.id.iv_checkout_paymentstatus_goto_shoppingcart);
        tvGoToShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShoppingCart();
//                Intent intent = new Intent(checkoutPaymentStatusActivity, ShoppingCartActivity1.class);
//                //clear top
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                checkoutPaymentStatusActivity.overridePendingTransition(R.anim.enter_righttoleft,
//                        R.anim.exit_righttoleft);
//                checkoutPaymentStatusActivity.finish();
            }
        });
//        rlHeaderBarMenu= (RelativeLayout) view.findViewById(R.id.rlHeaderBarMenu);
//    ;
        /////////////////////set orderNumber////////////////////
        Bundle bundle = getArguments();
        orderNumber = bundle.getString("orderNumber");
//        if (!JDataUtils.isEmpty(bundle.getString("grand_total"))){
//            grandTotal = Double.parseDouble(bundle.getString("grand_total").replace(",", "").replace("RM",""));
//        }
//        if (!JDataUtils.isEmpty(bundle.getString("shipping_fee"))){
//            shippingFee = Double.parseDouble(bundle.getString("shipping_fee").replace(",", "").replace("RM",""));
//        }
        wvHtml= (CustomWebView) view.findViewById(R.id.wv_html);
        if(!TextUtils.isEmpty(checkoutPaymentStatusActivity.html)){
            wvHtml.setVisibility(View.VISIBLE);
        }
        initWebView();

//        paymentSaveReturnEntity = (CheckoutPaymentSaveReturnEntity) bundle.getSerializable("paymentSaveReturnEntity");
        /////////////////////set email////////////////////
        GOUserEntity user = WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity);

        if (user != null) {
            tvEmail.setText(user.getEmail());
        }
        tvOrderNumber.setText(orderNumber);
        tvContinueShopping = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping);
        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkoutPaymentStatusActivity.startNextActivity(null, HomeActivity.class, true);
                Intent intent = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                startActivity(intent);
                checkoutPaymentStatusActivity.overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
                checkoutPaymentStatusActivity.finish();
            }
        });

        animImgView=view.findViewById(R.id.animImgView);
        showCodeFailure=view.findViewById(R.id.showCode_failure);
//        windowWinth= WhiteLabelApplication.getPhoneConfiguration().getScreenWidth();
//        windowHeight=WhiteLabelApplication.getPhoneConfiguration().getScreenHeigth()- AppUtils.getStatusBarHeight(checkoutPaymentStatusActivity)-AppUtils.getNavigationBarHeight(checkoutPaymentStatusActivity);
        animImg= (ImageView) view.findViewById(R.id.animImg);
//        myText1= (TextView) view.findViewById(R.id.my_text1);
//        myText2= (TextView) view.findViewById(R.id.my_text2);
//        myText3= (TextView) view.findViewById(R.id.my_text4);
//        showCode=view.findViewById(R.id.showCode);
        codeNumber= (TextView) view.findViewById(R.id.my_text3);
        tvContinueShopping3= (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping3);
        tvContinueShopping3.setOnClickListener(this);
        tvContinueShopping4= (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping4);
        tvContinueShopping4.setOnClickListener(this);
//        img1= (ImageView) view.findViewById(R.id.img1);
//        img2= (ImageView) view.findViewById(R.id.img2);
//        img3= (ImageView) view.findViewById(R.id.img3);
//        img4= (ImageView) view.findViewById(R.id.img4);
//        img5= (ImageView) view.findViewById(R.id.img5);
//        img1.setOnClickListener(this);
//        img2.setOnClickListener(this);
//        img3.setOnClickListener(this);
//        img4.setOnClickListener(this);
//        img5.setOnClickListener(this);
//        allImageVeiw=new ArrayList<ImageView>();
//        allImageVeiw.add(img1);
//        allImageVeiw.add(img2);
//        allImageVeiw.add(img3);
//        allImageVeiw.add(img4);
//        allImageVeiw.add(img5);
        //实例化Handler一定要在启动线程之前
//        myHandler=new AnimHandler(this);
//        myHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                ImageView img= (ImageView) msg.obj;
//                switch (msg.what){
//                    case 1:img.setImageResource(R.mipmap.box1);break;
//                    case 2:img.setImageResource(R.mipmap.box2);break;
//                    case 3:img.setImageResource(R.mipmap.box3);break;
//                    case 4:img.setImageResource(R.mipmap.box4);break;
//                    case 5:img.setImageResource(R.mipmap.box5);break;
//                    case 6:img.setImageResource(R.mipmap.box6);break;
//                    case 7:img.setImageResource(R.mipmap.box5);break;
//                    case 8:img.setImageResource(R.mipmap.box4);break;
//                    case 9:img.setImageResource(R.mipmap.box3);break;
//                    case 10:img.setImageResource(R.mipmap.box2);break;
//                    case 11:img.setImageResource(R.mipmap.box1);break;
//                    case 12:img.setImageResource(R.mipmap.box1_1);break;
//                    case 13:img.setImageResource(R.mipmap.box1_2);break;
//                    case 14:img.setImageResource(R.mipmap.box1_3);break;
//                    case 15:img.setImageResource(R.mipmap.box1_4);break;
//                    case 16:img.setImageResource(R.mipmap.box1_5);break;
//                    case 17:img.setImageResource(R.mipmap.box1_6);break;
//                    case 18:img.setImageResource(R.mipmap.box1_7);break;
//                    case 19:img.setImageResource(R.mipmap.box1_8);break;
//                    case 20:img.setImageResource(R.mipmap.box1_9);break;
//                    case 21:img.setImageResource(R.mipmap.box1_10);break;
//                    case 22:img.setImageResource(R.mipmap.box1_11);break;
//                    case 23:img.setImageResource(R.mipmap.box1_12);break;
//                    case 24:img.setImageResource(R.mipmap.box1_13);break;
//                    case 25:img.setImageResource(R.mipmap.box1_14);break;
//                    case 26:img.setImageResource(R.mipmap.box1_15);break;
//                    case 27:img.setImageResource(R.mipmap.box0);break;
//                    //第二个动画
//                    case 28:
//                        int x= (int) animImg.getX();
//                        int y=  (int) animImg.getY();
//                        animImg.setX(x + OffsetX-magnifyX/2);
//                        animImg.setY(y + OffsetY - magnifyY / 2);
//                        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) animImg.getLayoutParams(); //取控件当前的布局参数
//                        linearParams.height = linearParams.height+magnifyY;// 控件的高
//                        linearParams.width = linearParams.width+magnifyX;// 控件的宽
//                        animImg.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
//                        switch(currentFrameImgs2){
//                            case 1:img.setImageResource(R.mipmap.box1);break;
//                            case 2:img.setImageResource(R.mipmap.box2);break;
//                            case 3:img.setImageResource(R.mipmap.box3);break;
//                            case 4:img.setImageResource(R.mipmap.box4);break;
//                            case 5:img.setImageResource(R.mipmap.box5);break;
//                            case 6:img.setImageResource(R.mipmap.box6);break;
//                            case 7:img.setImageResource(R.mipmap.box5);break;
//                            case 8:img.setImageResource(R.mipmap.box4);break;
//                            case 9:img.setImageResource(R.mipmap.box3);break;
//                            case 10:img.setImageResource(R.mipmap.box2);break;
//                            case 11:img.setImageResource(R.mipmap.box1);break;
//                            case 12:img.setImageResource(R.mipmap.sexangle);
//                                showCode.setVisibility(View.VISIBLE);
//                                break;
//                        }
//                        break;
//                }
//            }
//        };

        tvCheckOrder = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_checkorder);
        tvCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JLogUtils.i("Allen","check ou");
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ORDER);
                //checkoutPaymentStatusActivity.startNextActivity(bundle, HomeActivity.class, true);

                Intent intent = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                checkoutPaymentStatusActivity.overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
                checkoutPaymentStatusActivity.finish();
            }
        });


            tvCheckOrder.setVisibility(View.VISIBLE);
            tvContinueShopping.setVisibility(View.VISIBLE);
            JViewUtils.setStrokeButtonGlobalStyle(getActivity(),tvCheckOrder);
//            tvCheckOrder.setBackground(JImageUtils.getbuttonBakcgroundStrokeDrawable(getActivity()));
//            tvCheckOrder.setTextColor();

        JViewUtils.setSoildButtonGlobalStyle(getActivity(),tvContinueShopping);
//            tvContinueShopping.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(getActivity()));
        return view;
    }

//    private static class AnimHandler extends Handler {
//
//        private final WeakReference<CheckoutPaymentStatusRightFragment> mFragment;
//
//        public AnimHandler(CheckoutPaymentStatusRightFragment fragment) {
//            mFragment = new WeakReference<CheckoutPaymentStatusRightFragment>(fragment);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (mFragment.get() == null ) {
//                return;
//            }
//
//            ImageView img = (ImageView) msg.obj;
//            switch (msg.what) {
//                case 1:
//                    img.setImageResource(R.mipmap.box1);
//                    break;
//                case 2:
//                    img.setImageResource(R.mipmap.box2);
//                    break;
//                case 3:
//                    img.setImageResource(R.mipmap.box3);
//                    break;
//                case 4:
//                    img.setImageResource(R.mipmap.box4);
//                    break;
//                case 5:
//                    img.setImageResource(R.mipmap.box5);
//                    break;
//                case 6:
//                    img.setImageResource(R.mipmap.box6);
//                    break;
//                case 7:
//                    img.setImageResource(R.mipmap.box5);
//                    break;
//                case 8:
//                    img.setImageResource(R.mipmap.box4);
//                    break;
//                case 9:
//                    img.setImageResource(R.mipmap.box3);
//                    break;
//                case 10:
//                    img.setImageResource(R.mipmap.box2);
//                    break;
//                case 11:
//                    img.setImageResource(R.mipmap.box1);
//                    break;
//                case 12:
//                    img.setImageResource(R.mipmap.box1_1);
//                    break;
//                case 13:
//                    img.setImageResource(R.mipmap.box1_2);
//                    break;
//                case 14:
//                    img.setImageResource(R.mipmap.box1_3);
//                    break;
//                case 15:
//                    img.setImageResource(R.mipmap.box1_4);
//                    break;
//                case 16:
//                    img.setImageResource(R.mipmap.box1_5);
//                    break;
//                case 17:
//                    img.setImageResource(R.mipmap.box1_6);
//                    break;
//                case 18:
//                    img.setImageResource(R.mipmap.box1_7);
//                    break;
//                case 19:
//                    img.setImageResource(R.mipmap.box1_8);
//                    break;
//                case 20:
//                    img.setImageResource(R.mipmap.box1_9);
//                    break;
//                case 21:
//                    img.setImageResource(R.mipmap.box1_10);
//                    break;
//                case 22:
//                    img.setImageResource(R.mipmap.box1_11);
//                    break;
//                case 23:
//                    img.setImageResource(R.mipmap.box1_12);
//                    break;
//                case 24:
//                    img.setImageResource(R.mipmap.box1_13);
//                    break;
//                case 25:
//                    img.setImageResource(R.mipmap.box1_14);
//                    break;
//                case 26:
//                    img.setImageResource(R.mipmap.box1_15);
//                    break;
//                case 27:
//                    img.setImageResource(R.mipmap.box0);
//                    break;
//                //第二个动画
//                case 28:
//                    int x = (int) mFragment.get().animImg.getX();
//                    int y = (int) mFragment.get().animImg.getY();
//                    mFragment.get().animImg.setX(x + mFragment.get().OffsetX - mFragment.get().magnifyX / 2);
//                    mFragment.get().animImg.setY(y + mFragment.get().OffsetY - mFragment.get().magnifyY / 2);
//                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mFragment.get().animImg.getLayoutParams(); //取控件当前的布局参数
//                    linearParams.height = linearParams.height + mFragment.get().magnifyY;// 控件的高
//                    linearParams.width = linearParams.width + mFragment.get().magnifyX;// 控件的宽
//                    mFragment.get().animImg.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
//                    switch ( mFragment.get().currentFrameImgs2) {
//
//                        case 1:
//                            img.setImageResource(R.mipmap.box1);
//                            break;
//                        case 2:
//                            img.setImageResource(R.mipmap.box2);
//                            break;
//                        case 3:
//                            img.setImageResource(R.mipmap.box3);
//                            break;
//                        case 4:
//                            img.setImageResource(R.mipmap.box4);
//                            break;
//                        case 5:
//                            img.setImageResource(R.mipmap.box5);
//                            break;
//                        case 6:
//                            img.setImageResource(R.mipmap.box6);
//                            break;
//                        case 7:
//                            img.setImageResource(R.mipmap.box5);
//                            break;
//                        case 8:
//                            img.setImageResource(R.mipmap.box4);
//                            break;
//                        case 9:
//                            img.setImageResource(R.mipmap.box3);
//                            break;
//                        case 10:
//                            img.setImageResource(R.mipmap.box2);
//                            break;
//                        case 11:
//                            img.setImageResource(R.mipmap.box1);
//                            break;
//                        case 12:
//                            img.setImageResource(R.mipmap.sexangle);
//                            mFragment.get().showCode.setVisibility(View.VISIBLE);
//                            break;
//                    }
//                    break;
//            }
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();

//        if (paymentSaveReturnEntity != null) {
//            ArrayList<ShoppingCartListEntityCell> items = paymentSaveReturnEntity.getReviewOrder();
//            GaTrackHelper.getInstance().googleAnalyticsCheckoutSuccess(getActivity(),items,orderNumber,grandTotal,shippingFee);
//            try {
//                int sumNum=0;
//                String ids="";
//                for(int i=0;i<items.size();i++){
//                    sumNum+=Integer.parseInt(items.get(i).getQty());
//                    ids+=items.get(i).getProductId()+",";
//                }
//                ids=ids.substring(0,ids.length()-1);
//                FirebaseEventUtils.getInstance().ecommercePurchase(getActivity(),JDataUtils.formatDouble((grandTotal+"")),JDataUtils.formatDouble(shippingFee+""),orderNumber);
//            }catch (Exception ex){
//                ex.getStackTrace();
//            }
//        }

    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }
//
//    public void getAnmitionCode(){
//        String session_key= WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity).getSessionKey();
//        mCheckoutDao.getWinCode(session_key,orderNumber);
//    }
//
//    private int currentFrameImgs=0,allFrameImgs=27;
//    private int currentBox=1,allBox=5;

//    private class MyAnimtion implements Runnable {
//        @Override
//        public void run() {
//            while (animStop){
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                currentFrameImgs++;
//                Message msg=new Message();
//                msg.what=currentFrameImgs;
//                msg.obj=allImageVeiw.get(currentBox-1);
//
//                if(currentFrameImgs>allFrameImgs){
//                    currentBox++;
//                    currentFrameImgs=1;
//                    if(currentBox>allBox){
//                        currentBox=1;
//                    }
//                    msg.what=currentFrameImgs;
//                    msg.obj=allImageVeiw.get(currentBox-1);
//                }
//                myHandler.sendMessage(msg);
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        animStop=false;
//        animStop2=false;
    }
//    private int currentFrameImgs2=0,allFrameImgs2=13;
//    private class MyAnimtion2 implements Runnable {
//        private ImageView img;
//        public MyAnimtion2(ImageView img){
//            this.img=img;
//        }
//        @Override
//        public void run() {
//            for(currentFrameImgs2=0;currentFrameImgs2<allFrameImgs2;currentFrameImgs2++){
//                if(animStop2){
//                    try {
//                        Thread.sleep(30);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Message msg=new Message();
//                    msg.what=28;
//                    msg.obj=img;
//                    myHandler.sendMessage(msg);
//
//                }
//            }
//        }
//    }
    private class initAnim implements Runnable {
        private View v;
        public initAnim(View v){
            this.v=v;
        }
        @Override
        public void run() {
//            img1.setImageResource(R.mipmap.box0);
//            img2.setImageResource(R.mipmap.box0);
//            img3.setImageResource(R.mipmap.box0);
//            img4.setImageResource(R.mipmap.box0);
//            img5.setImageResource(R.mipmap.box0);
//            img1.setOnClickListener(null);
//            img2.setOnClickListener(null);
//            img3.setOnClickListener(null);
//            img4.setOnClickListener(null);
//            img5.setOnClickListener(null);
            switch (v.getId()){
//                case  R.id.img1:
//                    if(myClickBox) {
//                        myClickBox = false;
//                        getOffsetXAndY(img1);//获得每次平移大小
//                        MyAnimtion2 anim1 = new MyAnimtion2(initView(img1));
//                        Thread thread1 = new Thread(anim1);
//                        thread1.start();//开启平移线程
//                        img1.setImageResource(R.mipmap.box1);
//                        getAnmitionCode();
//                    }
//                    break;
//                case  R.id.img2:
//                    if(myClickBox) {
//                        myClickBox = false;
//                        getOffsetXAndY(img2);
//                        MyAnimtion2 anim2 = new MyAnimtion2(initView(img2));
//                        Thread thread2 = new Thread(anim2);
//                        thread2.start();
//                        img2.setImageResource(R.mipmap.box1);
//                        getAnmitionCode();
//                    }
//                    break;
//                case  R.id.img3:
//                    if(myClickBox) {
//                        myClickBox = false;
//                        getOffsetXAndY(img3);
//                        MyAnimtion2 anim3 = new MyAnimtion2(initView(img3));
//                        Thread thread3 = new Thread(anim3);
//                        thread3.start();
//                        img3.setImageResource(R.mipmap.box1);
//                        getAnmitionCode();
//                    }
//                    break;
//                case  R.id.img4:
//                    if(myClickBox) {
//                        myClickBox = false;
//                        getOffsetXAndY(img4);
//                        MyAnimtion2 anim4 = new MyAnimtion2(initView(img4));
//                        Thread thread4 = new Thread(anim4);
//                        thread4.start();
//                        img4.setImageResource(R.mipmap.box1);
//                        getAnmitionCode();
//                    }
//                    break;
//                case  R.id.img5:
//                    if(myClickBox) {
//                        myClickBox = false;
//                        getOffsetXAndY(img5);
//                        MyAnimtion2 anim5 = new MyAnimtion2(initView(img5));
//                        Thread thread5 = new Thread(anim5);
//                        thread5.start();
//                        img5.setImageResource(R.mipmap.box1);
//                        getAnmitionCode();
//                    }
//                    break;
                case R.id.tv_share:
                    shareFacebook();
                    break;
                case R.id.tv_checkout_payment_status_right_continueshopping3:
                    Intent intent2 = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                    startActivity(intent2);
                    checkoutPaymentStatusActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                    checkoutPaymentStatusActivity.finish();
                    break;
                case R.id.tv_checkout_payment_status_right_continueshopping4:
                    Intent intent4 = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                    startActivity(intent4);
                    checkoutPaymentStatusActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                    checkoutPaymentStatusActivity.finish();
                    break;
            }
        }
    }
//    private ImageView initView(ImageView img){
////        animImg.setVisibility(View.VISIBLE);
//        img.setVisibility(View.INVISIBLE);
//        animImg.setX(ImgGetX(img));
//        animImg.setY(ImgGetY(img));
//        return animImg;
//    }
//
//    private void getOffsetXAndY(ImageView img){
//        int x= ImgGetX(img);
//        int y= ImgGetY(img);
//        int imgWinth=img.getWidth();
//        int imgHeight=img.getHeight();
//        int centerY=windowHeight/2;
//        int centerX=windowWinth/2;
//        OffsetY=(centerY-y-imgHeight/2)/allFrameImgs2;
//        OffsetX=(centerX-x-imgWinth/2)/allFrameImgs2;
//        magnifyXTO=JDataUtils.dp2Px(280);
//        magnifyYTO=JDataUtils.dp2Px(240);
//        magnifyX=magnifyXTO/allFrameImgs2;
//        magnifyY=magnifyYTO/allFrameImgs2;
//
//    }
    //获得组件绝对坐标
//    public int ImgGetX(ImageView img){
//         int[] location = new int[2];
//        img.getLocationOnScreen(location);
//        return  location[0];
//    }
//    //获得组件绝对坐标
//    public int ImgGetY(ImageView img){
//         int[] location = new int[2];
//        img.getLocationOnScreen(location);
//        return  location[1];
//    }

}

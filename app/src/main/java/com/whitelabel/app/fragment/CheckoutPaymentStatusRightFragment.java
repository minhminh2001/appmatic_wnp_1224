package com.whitelabel.app.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JShareUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;

public class CheckoutPaymentStatusRightFragment extends BaseFragment  implements View.OnClickListener{
    private CheckoutPaymentStatusActivity checkoutPaymentStatusActivity;
    private CustomWebView wvHtml;
    private TextView tvShare;
    private View rlBackGroud;
    private int fromType=0;
    private View rlRoot;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        shareHandler=new ShareHandler(getActivity(),CheckoutPaymentStatusRightFragment.this);
//        FB_ERROR_NOINTERNET = getResources().getString(R.string.facebook_error_nointernet);
//        FB_ERROR_UNINSTALLED = getResources().getString(R.string.facebook_error_notinstalled);
//        FB_SHARED_OK = getResources().getString(R.string.facebook_success_ok);

        if (getArguments() != null){
//            mDiscountBean = (ShoppingDiscountBean) getArguments().getSerializable("discountBean");
            fromType=getArguments().getInt("fromType");
        }
        AnimUtil.alpha_0_1_500(rlRoot);
        if(!TextUtils.isEmpty(checkoutPaymentStatusActivity.html)){
            String content=JToolUtils.replaceFont(checkoutPaymentStatusActivity.html);
            JToolUtils.webViewFont(WhiteLabelApplication.getInstance().getBaseContext(), wvHtml, content);
        }
//        if(mDiscountBean!=null&&mDiscountBean.getIsShare()==1){
//            tvShare.setVisibility(View.VISIBLE);
//            mImageUrl=mDiscountBean.getShareImage();
//            mDescription=mDiscountBean.getShareDescription();
//            mLink=mDiscountBean.getShareLink();
//            mTitle=mDiscountBean.getShareTitle();
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
//    private PopupWindow popupWindow;
//    private TextView rateNow,askMeLater,noThanks;
//    private ShareDialog shareDialog;
//    private CallbackManager callbackManager;
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//    private void shareFacebook(){
//        String link = mLink;
//        String picture =mImageUrl;
//        String applicationname = getResources().getString(R.string.app_name);
//        FacebookStoryEntity entity = new FacebookStoryEntity();
//        entity.setLink(link);
//        entity.setApplicationName(applicationname);
//        entity.setDescription(mDescription);
//        entity.setName(mTitle);
//        entity.setCaption(mTitle);
//        entity.setPicture(picture);
//        JShareUtils.publishFacebookStoryByNativeApp(checkoutPaymentStatusActivity, entity, shareDialog, shareHandler);
//    }

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
                checkoutPaymentStatusActivity.startActivityTransitionAnim();
                getActivity().finish();
            } else {
                Intent intent = new Intent(checkoutPaymentStatusActivity, ShoppingCartActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            getActivity().overridePendingTransition(R.anim.activity_transition_enter_lefttoright,
                    R.anim.activity_transition_exit_lefttoright);
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
        View layout = view.findViewById(R.id.rl_root);
        TextView tvOrderNumber = (TextView) view.findViewById(R.id.tv_checkout_payment_status_ordernumber);
        TextView tvEmail = (TextView) view.findViewById(R.id.tv_checkout_payment_status_email);
        tvEmail.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
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
            }
        });
//        rlHeaderBarMenu= (RelativeLayout) view.findViewById(R.id.rlHeaderBarMenu);
//    ;
        /////////////////////set orderNumber////////////////////
        Bundle bundle = getArguments();
        String orderNumber = bundle.getString("orderNumber");
        wvHtml= (CustomWebView) view.findViewById(R.id.wv_html);
        if(!TextUtils.isEmpty(checkoutPaymentStatusActivity.html)){
            wvHtml.setVisibility(View.VISIBLE);
        }
        initWebView();
        /////////////////////set email////////////////////
        GOUserEntity user = WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutPaymentStatusActivity);

        if (user != null) {
            tvEmail.setText(user.getEmail());
        }
        tvOrderNumber.setText(orderNumber);
        TextView tvContinueShopping = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping);
        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkoutPaymentStatusActivity.startNextActivity(null, HomeActivity.class, true);
                Intent intent = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                startActivity(intent);
                checkoutPaymentStatusActivity.startActivityTransitionAnim();
                checkoutPaymentStatusActivity.finish();
            }
        });
        TextView tvContinueShopping3 = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping3);
        tvContinueShopping3.setOnClickListener(this);
        TextView tvContinueShopping4 = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_continueshopping4);
        tvContinueShopping4.setOnClickListener(this);
        TextView tvCheckOrder = (TextView) view.findViewById(R.id.tv_checkout_payment_status_right_checkorder);
        tvCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(HomeActivity.EXTRA_REDIRECTTO_TYPE, HomeActivity.EXTRA_REDIRECTTO_TYPE_VALUE_ORDER);
                Intent intent = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                checkoutPaymentStatusActivity.startActivityTransitionAnim();
                checkoutPaymentStatusActivity.finish();
            }
        });
            tvCheckOrder.setVisibility(View.VISIBLE);
            tvContinueShopping.setVisibility(View.VISIBLE);
            JViewUtils.setStrokeButtonGlobalStyle(getActivity(), tvCheckOrder);
            JViewUtils.setSoildButtonGlobalStyle(getActivity(), tvContinueShopping);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private class initAnim implements Runnable {
        private View v;
        public initAnim(View v){
            this.v=v;
        }
        @Override
        public void run() {
            switch (v.getId()){
                case R.id.tv_share:
//                    shareFacebook();
                    break;
                case R.id.tv_checkout_payment_status_right_continueshopping3:
                    Intent intent2 = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                    startActivity(intent2);
                    checkoutPaymentStatusActivity.overridePendingTransition(R.anim.activity_transition_enter_righttoleft, R.anim.activity_transition_exit_righttoleft);
                    checkoutPaymentStatusActivity.finish();
                    break;
                case R.id.tv_checkout_payment_status_right_continueshopping4:
                    Intent intent4 = new Intent(checkoutPaymentStatusActivity, HomeActivity.class);
                    startActivity(intent4);
                     checkoutPaymentStatusActivity.startActivityTransitionAnim();
                    checkoutPaymentStatusActivity.finish();
                    break;
            }
        }
    }
}
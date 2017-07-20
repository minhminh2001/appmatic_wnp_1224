package com.whitelabel.app.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.*;
import com.whitelabel.app.activity.CheckoutPaymentStatusActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;

import java.lang.ref.WeakReference;

@SuppressWarnings("ALL")
public class CheckoutPaymentStatusWrongFragment extends com.whitelabel.app.BaseFragment {
    private CheckoutPaymentStatusActivity checkoutPaymentStatusActivity;
    private TextView tvErrorMsg;
    private boolean isSend;
    private String errorMsg;
    private String orderNumber;
    private boolean isLoading;
    private Dialog mDialog;
    private int fromType=0;
    private ShoppingCarDao mShoppingCarDao;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checkoutPaymentStatusActivity = (CheckoutPaymentStatusActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromType=bundle.getInt("fromType");
            errorMsg = bundle.getString("errorMsg");
            orderNumber = bundle.getString("orderNumber");
        }

    }
    public void gaTrackerProductToCart(){
        try{
            if(getActivity()!=null) {
                GaTrackHelper.getInstance().googleAnalyticsEvent("Checkout Action",
                        "Tap Try Agaign In Payment Failure Screen",
                        null,
                        null);
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }
    private static final class DataHandler extends Handler{
        private final WeakReference<CheckoutPaymentStatusActivity> mActivity;
        private final WeakReference<CheckoutPaymentStatusWrongFragment> mFragment;
        public DataHandler(CheckoutPaymentStatusActivity activity,CheckoutPaymentStatusWrongFragment fragment){
            mActivity=new WeakReference<CheckoutPaymentStatusActivity>(activity);
            mFragment=new WeakReference<CheckoutPaymentStatusWrongFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            switch (msg.what){
                case ShoppingCarDao.REQUEST_RECOVERORDER :
                    if ( mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    if(msg.arg1==ShoppingCarDao.RESPONSE_SUCCESS){
                        mFragment.get().isSend=true;
                        mFragment.get().isLoading=false;
                        mFragment.get().startShoppingCart();
                        mFragment.get().gaTrackerProductToCart();
                    }else{
                        mFragment.get().isLoading=false;
                        mFragment.get().startShoppingCart();
                    }
                break;
                case ShoppingCarDao.REQUEST_ERROR:
                    if ( mFragment.get().mDialog != null) {
                    mFragment.get().mDialog.cancel();
                    }
                    mFragment.get().isLoading=false;
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }
    /**
     * send Request To Recover Order
     */
    private void sendRequestToRecoverOrder() {
        isLoading=true;
        mShoppingCarDao.sendRecoverOrder( WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey(),orderNumber,"");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_payment_status_wrong, container, false);
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_payment_status_wrong_errorMsg);
        ImageView tvGoToShoppingCart = (ImageView) view.findViewById(R.id.iv_checkout_paymentstatus_goto_shoppingcart);
        tvGoToShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShoppingCart();
            }
        });
        RelativeLayout rlHeaderBarMenu = (RelativeLayout) view.findViewById(R.id.rlHeaderBarMenu);
        TextView tvRetry = (TextView) view.findViewById(R.id.tv_checkout_payment_status_wrong_retry);
//        tvRetry.setBackground(JImageUtils.getbuttonBakcgroundStrokeDrawable(getActivity()));
//        tvRetry.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        JViewUtils.setStrokeButtonGlobalStyle(getActivity(),tvRetry);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startShoppingCart();
                //checkoutPaymentStatusActivity.startNextActivity(null, ShoppingCartActivity.class, true);
            }
        });

        return view;
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
                getActivity().overridePendingTransition(R.anim.activity_transition_enter_lefttoright,
                        R.anim.activity_transition_exit_lefttoright);
                getActivity().finish();
            }

        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!JDataUtils.isEmpty(errorMsg) && !errorMsg.contains("json str is empty")  && !errorMsg.contains("Internal Server Error")) {
            String content=JToolUtils.replaceFont(errorMsg);
            tvErrorMsg.setText(content);
        }
        String TAG = this.getClass().getSimpleName();
        DataHandler dataHandler = new DataHandler((CheckoutPaymentStatusActivity) getActivity(), this);
        mShoppingCarDao=new ShoppingCarDao(TAG, dataHandler);
        if(checkoutPaymentStatusActivity.mGATrackTimeEnable) {
            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_PAYMENT,
                    checkoutPaymentStatusActivity.mGATrackTimeStart,
                    "Payment Failure"
            );
            checkoutPaymentStatusActivity.mGATrackTimeEnable = false;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
//        EasyTracker easyTracker = EasyTracker.getInstance(checkoutPaymentStatusActivity);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Checkout Failure Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(checkoutPaymentStatusActivity,true);
        GaTrackHelper.getInstance().googleAnalytics("Checkout Failure Screen", checkoutPaymentStatusActivity);
        JLogUtils.i("googleGA_screen", "Checkout Failure Screen");
    }

    @Override
    public void onStop() {
        super.onStop();
//        GaTrackHelper.getInstance().googleAnalyticsReportActivity(checkoutPaymentStatusActivity, false);
    }
}

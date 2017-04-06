package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.CheckoutActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.ProductActivity;
import com.whitelabel.app.adapter.ShoppingCartVerticalAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.MaterialDialogCallback;
import com.whitelabel.app.callback.ShoppingCartAdapterCallback;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartErrorMsgBean;
import com.whitelabel.app.model.ShoppingCartListBase;
import com.whitelabel.app.model.ShoppingCartListEntityBody;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JScreenUtils;
import com.whitelabel.app.utils.JStorageUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.SoftInputShownUtil;
import com.whitelabel.app.widget.CustomSwipefreshLayout;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link ShoppingCartVerticalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCartVerticalFragment extends ShoppingCartBaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnFocusChangeListener, View.OnClickListener, ShoppingCartAdapterCallback {
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "mGATrackTimeStart";

    public Long mGATrackCheckoutTimeStart = 0L;
    public Long mGATrackTimeStart = 0L;
    public boolean mGATrackTimeEnable = false;

    private CustomSwipefreshLayout swipeRefrshLayout;
    private String mReedemStr;
    private DataHandler mHandler;
    private TimeHandler timeHandler;
    public RecyclerView listView;
    //    private RelativeLayout rlShoppingcartTopGoback;
//    private View vVoucher;
    private View infoView;
//    private ImageView ivUpdate;
    public TextView tvSubtotal;
    public TextView tvVoucher;
    private TextView tvApply;
    private EditText etVoucherApply;
    public TextView tvGrandTotal;
    public TextView tvErrorMsg;
    public LinearLayout llNothing;
    public LinearLayout llCheckout;
    public TextView tvCheckout;
    private LinearLayout llApplyAnim;
    private TextView tvNullApplyHint;
    private ImageView tvApplyImageAnim, clearVoucher;
    private TextView tvApplyTextAnim;
    private RelativeLayout llVoucherPrice;
    public TextView tvShippingFree;
    private TextView tvVoucherWorld, tv_shoppingbottominfo_blank;
    private ImageView btnBack;
    public ShoppingCartVerticalAdapter adapter;
    public LinkedList<ShoppingCartListBase> mProducts;
    private ShoppingCartListEntityCart mCar;
    private final int REQUESTCODE = 2000;
    private ShoppingCarDao mCarDao;
    private String TAG = this.getClass().getSimpleName();
    private RequestErrorHelper requestErrorHelper;
//    private View vCampaign; vProgress
    private View  connectionBreak;
    public int fromType;
    private Dialog mDialog;
    private LinearLayout btnTry;
//    private String campaignProductId;
    private final int VIEW_INIT_SHOW = 3;
    private final int VIEW_INIT_HIDE = 4;
    private final int VIEW_NOTHING_SHOW = 1;
    private final int VIEW_NOTHING_HIDE = 2;
    private final int VIEW_NOTNETWORK_SHOW = 5;
    private final int VIEW_NOTNETWORK_HIDE = 6;
    private final int VIEW_VOUCHER_SHOW = 7;
    private final int VIEW_VOUCHER_HIDE = 8;
//    private String mCampaignBanner = "";
//    private String mCampaignPopText = "";
    private final int STATUS_VOUCHERCODE_APPLY = 1;
    private final int STATUS_VOUCHERCODE_CANCEL = 2;
    //    private ShoppingCartCallback shoppingCallback;
    private String mCancelStr, mApplyStr;
    private String voucherCode = "";
    private RelativeLayout llBody;
    private View btnGoShopping;
    private final int APPLIED = 1;
    private final int UNAPPLIED = 2;
    private final int REDEEM = 1;
    private final int UNREDEEM = 2;
    private int currStatus;
    private final static int LOADING = 100;
    private final static int LOADSUCCESS = 101;
//    private View llStoreCreditHint;
    private boolean apply = false;
    private HomeBaseFragment.HomeCommonCallback mHomeCallback;
    private BaseActivity baseActivity;
    private Boolean mIsFromLogin = false;
    private String mVoucherCode;
    private ImageLoader mImageLoader;

    private TextView mTvGst;

    @Override
    public boolean getSwipeRefreshStatus() {
        return swipeRefrshLayout.isRefreshing();
    }

    @Override
    public void onRefresh() {
        //暂时没有用到
//        mCarDao.getShoppingCartLocalInfo(getActivity());

        sendRequest();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
//            case R.id.iv_shoppingcart_campaign:
//                Bundle bundle = new Bundle();
//                bundle.putString("productId", campaignProductId);
//                bundle.putString("popText", mCampaignPopText);
//                Intent intent = new Intent(getActivity(), ShoppingCartCampaignActivity.class);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 2000);
//                getActivity().overridePendingTransition(R.anim.enter_righttoleft,
//                        R.anim.exit_righttoleft);
//                break;
//            case R.id.rl_shoppingcart_top_goback:
//                shoppingCallback.leftButtonClick();
//                break;
            case R.id.tv_shoppingcart_apply:
                checkAndApplyVoucherCode();
                break;
//            case R.id.ctv_storecredit:
//                if (getSwipeRefreshStatus()) {
//                    return;
//                }
//                if (!GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
//                    startLoginActivity();
//                } else {
//                    mDialog = JViewUtils.showProgressDialog(getActivity());
//                    String sessionKey = "";
//                    if (GemfiveApplication.getAppConfiguration() != null && GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()) != null) {
//                        sessionKey = GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
//                    }
//                    if (mReedemStr.equals(String.valueOf(tvStoreCredit.getText()))) {
//                        mCarDao.redeemStoreCredit(sessionKey, "0");
//                    } else {
//                        mCarDao.redeemStoreCredit(sessionKey, "1");
//                    }
//                }
//                break;
            case R.id.tv_sc_checkout:
                if (getSwipeRefreshStatus()) {
                    return;
                }
                if (!GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
                    startLoginActivity();
                } else {
                    showDialog();
                    mGATrackCheckoutTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
                    String sessionKey = "";
                    if (GemfiveApplication.getAppConfiguration() != null && GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()) != null) {
                        sessionKey = GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
                    }
                    mCarDao.checkOutofStock(sessionKey);
                }
                break;
            case R.id.try_again:
                initData();
                break;
            case R.id.btn_sc_nothing_goshopping:
                startHomeActivity();
                break;
            case R.id.clear_voucher:
                etVoucherApply.setText("");
                break;
        }
    }

    private void checkAndApplyVoucherCode() {
        if (getSwipeRefreshStatus()) {
            return;
        }
        JViewUtils.cleanCurrentViewFocus(getActivity());
        String voucherCode = etVoucherApply.getText().toString();
        if (!GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
            mVoucherCode = voucherCode;
            startLoginActivity();
        } else {
            if (voucherCode.toString().trim().length() < 1) {
                tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_error);
                tvApplyTextAnim.setTextColor(getResources().getColor(R.color.redC1033D));
                tvApplyTextAnim.setText(getResources().getString(R.string.apply_hint_red));
                llApplyAnim.setVisibility(View.VISIBLE);
            } else {
                showDialog();
                tvNullApplyHint.setVisibility(View.GONE);
                String sessionKey = "";
                if (GemfiveApplication.getAppConfiguration() != null && GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()) != null) {
                    sessionKey = GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
                }
                if (mApplyStr.equalsIgnoreCase(tvApply.getText().toString())) {
                    mCarDao.applyOrCancelVoucherCode(sessionKey, voucherCode, "0");
                } else {
                    mCarDao.applyOrCancelVoucherCode(sessionKey, voucherCode, "1");
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), true);
            GaTrackHelper.getInstance().googleAnalytics("Shopping Cart Screen", getActivity());
            JLogUtils.i("googleGA_screen", "Shopping Cart Screen");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null) {
            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), false);
        }
    }

    @Override
    public void onResume() {
        addHeightListenerOnInfoView();
        if (getActivity() instanceof HomeActivity && currStatus == LOADSUCCESS) {
            currStatus = LOADING;
            initData();
        }
        super.onResume();
    }

    public void refresh() {
        initData();
    }

    public void startHomeActivity() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_righttoleft,
                R.anim.exit_righttoleft);
    }

    public void startLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), LoginRegisterActivity.class);
        startActivityForResult(loginIntent, REQUESTCODE);
        getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }

//    private TextView tvStoreCreditTitle, tvStoreCredit, tvStoreCreditPrice, tvStoreCreditWorld, tvStoreCreditValue;
//    private View llStoreCredit, llBottomStoreCredit;

    public void initView(View view) {
        mImageLoader = new ImageLoader(getActivity());
        btnGoShopping = view.findViewById(R.id.btn_sc_nothing_goshopping);
        btnGoShopping.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(getActivity()));
        llBody = (RelativeLayout) view.findViewById(R.id.ll_body);
//        rlShoppingcartTopGoback= (RelativeLayout) view.findViewById(R.id.rl_shoppingcart_top_goback);
        btnBack = (ImageView) view.findViewById(R.id.tv_shoppingcart_top_goback);
        listView = (RecyclerView) view.findViewById(R.id.hlv_shoppingcart);
        llNothing = (LinearLayout) view.findViewById(R.id.ll_sc_nothing);
        llCheckout = (LinearLayout) view.findViewById(R.id.ll_sc_checkout);
        tvCheckout = (TextView) view.findViewById(R.id.tv_sc_checkout);
        connectionBreak = view.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionBreak);
        btnTry = (LinearLayout) view.findViewById(R.id.try_again);
        swipeRefrshLayout = (CustomSwipefreshLayout) view.findViewById(R.id.swipe_container);
//        swipeRefrshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefrshLayout.setColorSchemeColors(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        swipeRefrshLayout.setOnRefreshListener(this);
        llBody.setFocusable(true);
        llBody.setFocusableInTouchMode(true);
        llBody.requestFocus();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        listView.setLayoutManager(manager);
        //监听 listView 上下滑动,先将onTouch给gestureDetector，gestureDetector再给GestureListener处理
        listView.setOnTouchListener(gestureTouchListener);
        mGestureListener = new ShoppingOnGestureListener();
        gestureDetector = new GestureDetector(mGestureListener);
        tvCheckout.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(getActivity()));
    }

    //监听StretchScrollView 上下滑动
    public GestureDetector gestureDetector;
    private GestureDetector.OnGestureListener mGestureListener;
    private View.OnTouchListener gestureTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    class ShoppingOnGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (SoftInputShownUtil.isSoftInputShown(getActivity())) {
                //向下滑就先禁止滑动，隐藏完键盘后再允许滑动
                if (distanceY < 0) {
                    listView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    timeHandler.sendEmptyMessageDelayed(1000, 300);
                }
                JViewUtils.hideKeyboard(getActivity());
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }
    }

    public final static class TimeHandler extends Handler {
        private WeakReference<ShoppingCartVerticalFragment> mFragment;

        public TimeHandler(ShoppingCartVerticalFragment fragment) {
            mFragment = new WeakReference<ShoppingCartVerticalFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1000) {
                mFragment.get().listView.setOnTouchListener(mFragment.get().gestureTouchListener);
            }
            super.handleMessage(msg);
        }

    }

    @Override
    public View getInfoView() {
        return infoView;
    }


    public void initInfoView(View view) {
        tv_shoppingbottominfo_blank = (TextView) view.findViewById(R.id.tv_shoppingbottominfo_blank);
//        vVoucher = view.findViewById(R.id.ll_voucher);
//        ivUpdate = (ImageView) view.findViewById(R.id.iv_shoppingcart_campaign);
        tvSubtotal = (TextView) view.findViewById(R.id.tv_shoppingcart_subtotal);
        tvVoucher = (TextView) view.findViewById(R.id.tv_shoppingcart_voucher);
        tvApply = (TextView) view.findViewById(R.id.tv_shoppingcart_apply);
        etVoucherApply = (EditText) view.findViewById(R.id.et_shoppingcart_voucher_apply);
        tvGrandTotal = (TextView) view.findViewById(R.id.tv_shoppingcart_grandtotal);
        mTvGst = (TextView) view.findViewById(R.id.tv_shoppingcart_gst);
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_shoppingcart_errormsg);
        llApplyAnim = (LinearLayout) view.findViewById(R.id.ll_shoppingcart_apply_anim);
        tvNullApplyHint = (TextView) view.findViewById(R.id.tv_shoppingcart_null_apply_hint);
        tvApplyImageAnim = (ImageView) view.findViewById(R.id.tv_shoppingcart_apply_image_anim);
        tvApplyTextAnim = (TextView) view.findViewById(R.id.tv_shoppingcart_apply_text_anim);
        llVoucherPrice = (RelativeLayout) view.findViewById(R.id.ll_shoppingcart_voucher);
        tvShippingFree = (TextView) view.findViewById(R.id.tv_shoppingcart_shippingFree);
        tvVoucherWorld = (TextView) view.findViewById(R.id.tv_voucher_world);
//        tvStoreCreditTitle = (TextView) view.findViewById(R.id.tv_storecredit_title);
//        tvStoreCreditWorld = (TextView) view.findViewById(R.id.tv_storeCredit_world);
//        tvStoreCreditValue = (TextView) view.findViewById(R.id.tv_shoppingcart_storeCredit);
//        llStoreCreditHint = view.findViewById(R.id.ll_storeCredit_hint);
//        tvStoreCredit = (TextView) view.findViewById(R.id.ctv_storecredit);
//        llBottomStoreCredit = view.findViewById(R.id.ll_shoppingcart_storeCredit);
//        tvStoreCreditPrice = (TextView) view.findViewById(R.id.tv_storecredit_price);
//        llStoreCredit = view.findViewById(R.id.ll_storeCredit);
//        vCampaign = view.findViewById(R.id.rl_campaign);
//        vProgress = view.findViewById(R.id.progress_view);
        clearVoucher = (ImageView) view.findViewById(R.id.clear_voucher);
        etVoucherApply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0 && etVoucherApply.isFocused()) {
                    clearVoucher.setVisibility(View.VISIBLE);
                } else {
                    clearVoucher.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    voucherCode = editable.toString();
                }
            }
        });



    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.et_shoppingcart_voucher_apply:
                    if (!TextUtils.isEmpty(etVoucherApply.getText().toString())) {
                        clearVoucher.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        } else {
            clearVoucher.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        voucherCode = "";
        currStatus = LOADSUCCESS;
        if (!(getActivity() instanceof HomeActivity)) {
            initData();
        }
        initListener();
    }

    private AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //除了i 外其他都暂时为null
            if (mProducts.get(i) instanceof ShoppingCartListEntityBody) {
                return;
            }
            ShoppingCartListEntityCell entity = (ShoppingCartListEntityCell) mProducts.get(i);
            String visibility = entity.getVisibility();
            if (!TextUtils.isEmpty(visibility)) {
                if (("1").equals(visibility)) {
                    Intent it = new Intent(getActivity(), ProductActivity.class);
                    it.putExtra("productId", ((ShoppingCartListEntityCell) mProducts.get(i)).getProductId());
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.enter_righttoleft,
                            R.anim.exit_righttoleft);
                }
            } else {

            }
//
//            if (!"0".equals(mProducts.get(i).getCanViewPdp())) {
//                Intent it = new Intent(getActivity(), ProductActivity.class);
//                it.putExtra("productId", mProducts.get(i).getProductId());
//                startActivity(it);
//                getActivity().overridePendingTransition(R.anim.enter_righttoleft,
//                        R.anim.exit_righttoleft);
//            }
        }
    };

    private void initListener() {
        btnTry.setOnClickListener(this);
//        ivUpdate.setOnClickListener(this);
//        rlShoppingcartTopGoback.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvCheckout.setOnClickListener(this);
        llCheckout.setOnClickListener(this);
        llBody.setOnTouchListener(touchListener);
        btnGoShopping.setOnClickListener(this);
//        tvStoreCredit.setOnClickListener(this);
        clearVoucher.setOnClickListener(this);

        etVoucherApply.setOnFocusChangeListener(this);

        //键盘弹起时 禁用下拉刷新功能,否则会与滑动隐藏键盘发生冲突
        swipeRefrshLayout.setSwitchByKeyboard(true, getActivity());
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (getActivity() != null && SoftInputShownUtil.isSoftInputShown(getActivity())) {
                    JToolUtils.closeKeyBoard(getActivity(), etVoucherApply);
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case LoginRegisterEmailLoginFragment.RESULTCODE:
                if (!(getActivity() instanceof HomeActivity)) {
                    mIsFromLogin = true;
                    initData();
                }
                break;
        }
    }

    private void startCheckoutActivity() {
        Bundle bundle = new Bundle();
        if (mCar.getDiscount() != null) {
            bundle.putSerializable("discountBean", mCar.getDiscount());
        }
        try {
            StringBuffer productIds = new StringBuffer();
            if (mCar.getItems() != null) {
                for (int i = 0; i < mCar.getItems().length; i++) {
                    productIds.append(mCar.getItems()[i].getProductId()).append(",");
                }
            }
            String productsStr = productIds.substring(0, productIds.length() - 1);
            bundle.putString("productIds", productsStr);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        bundle.putInt("fromType", fromType);
        bundle.putString("grandTotal", tvGrandTotal.getText().toString());
        bundle.putLong("mGATrackTimeStart", mGATrackCheckoutTimeStart);

        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_righttoleft,
                R.anim.exit_righttoleft);
    }

    public final static class DataHandler extends Handler {
        private WeakReference<Activity> mActivity;
        private WeakReference<ShoppingCartVerticalFragment> mFragment;
        public DataHandler(Activity activity, ShoppingCartVerticalFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<ShoppingCartVerticalFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivity.get();
            final ShoppingCartVerticalFragment fragment = mFragment.get();
            if (activity != null & fragment != null) {
                switch (msg.what) {
//                    case ShoppingCarDao.REQUEST_LOCALSHOPPINGINFO:
//                        //initData()每次进来加载本地数据，调用此方法，
//                        //如果未登陆，就 offLineHandler
//                        //如果登陆，如果有本地缓存的shopping,先synShoppingCart，推到server上后，
//                        // 在REQUEST_MULTIPLECODE response里 再调initData(),此时已没有本地缓存，走onLineHandler 从服务器拉数据。
//                        JLogUtils.d("getShoppingCartInfo", "REQUEST_LOCALSHOPPINGINFO");
//                        fragment.mCar = (ShoppingCartListEntityCart) msg.obj;
//                        if (activity != null && !GemfiveApplication.getAppConfiguration().isSignIn(activity)) {
//                            JLogUtils.d("getShoppingCartInfo", "offlineHandler");
//                            mFragment.get().swipeRefrshLayout.setRefreshing(false);
//                            fragment.currStatus = LOADSUCCESS;
//                            fragment.offlineHandler(fragment.mCar);
//                        } else if (activity != null) {
//                            JLogUtils.d("getShoppingCartInfo", "onLineHandler");
//                            fragment.onLineHandler(fragment.mCar);
//                        }
//                        break;
                    case ShoppingCarDao.REQUEST_SHOPPINGINFO:
                        fragment.currStatus = LOADSUCCESS;
                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS && activity != null) {
                            fragment.mCar = (ShoppingCartListEntityCart) msg.obj;
                            fragment.initShoppingCartData(fragment.mCar, true);
                            fragment.mCarDao.saveShoppingCartCount(activity, fragment.mCar.getSummaryQty());
                            if (fragment.mApplyStr.equalsIgnoreCase(fragment.tvApply.getText().toString()) && fragment.mIsFromLogin && !TextUtils.isEmpty(fragment.mVoucherCode)) {
                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragment.etVoucherApply.setText(fragment.mVoucherCode);
                                        fragment.checkAndApplyVoucherCode();
                                    }
                                }, 10);
                            }
                        } else if (activity != null) {
                            String errorMsg = String.valueOf(msg.obj);
                            if (!JToolUtils.expireHandler(activity, errorMsg, 2000)) {
                                JViewUtils.showErrorToast(activity, errorMsg);
                            }
                        }
                        mFragment.get().swipeRefrshLayout.setRefreshing(false);
                        fragment.closeDialog();
                        break;
                    case ShoppingCarDao.REQUEST_VOUCHERCODE:
                        fragment.closeDialog();
                        fragment.mIsFromLogin = false;
                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS && activity != null) {
                            ShoppingCartVoucherApplyEntity shoppingCartVoucherApplyEntity = (ShoppingCartVoucherApplyEntity) msg.obj;
                            fragment.mCar.setDiscount(shoppingCartVoucherApplyEntity.getDiscount());
                            fragment.mCar.setGrandTotal(shoppingCartVoucherApplyEntity.getGrandTotal());
                            fragment.mCar.setShipping(shoppingCartVoucherApplyEntity.getShipping());
                            fragment.initShoppingCartData(fragment.mCar, false);
                            if (shoppingCartVoucherApplyEntity.getDiscount() != null && !TextUtils.isEmpty(shoppingCartVoucherApplyEntity.getDiscount().getCaption())) {
                                fragment.gaTrackerApplyCode(fragment.APPLIED);
                            } else {
                                fragment.gaTrackerApplyCode(fragment.UNAPPLIED);
                            }

                        } else if (activity != null) {
                            ErrorMsgBean errorBean = (ErrorMsgBean) msg.obj;
                            if (!JToolUtils.expireHandler(activity, errorBean.getErrorMessage(), 2000)) {
                                fragment.voucherCodeHintMsg(VOUCHER_APPLY_HINT_FIALD, errorBean.getErrorMessage());
                            }
                        }
                        break;
                    case ShoppingCarDao.REQUEST_ERROR:
                        fragment.closeDialog();
                        mFragment.get().swipeRefrshLayout.setRefreshing(false);
                        if (msg.arg1 == ShoppingCarDao.REQUEST_SHOPPINGINFO && activity != null) {
                            fragment.currStatus = LOADSUCCESS;
                            fragment.requestErrorHelper.showConnectionBreaks(msg);
                        } else {
                            fragment.requestErrorHelper.showNetWorkErrorToast(msg);

                        }
                        break;
                    case ShoppingCarDao.REQUEST_CHECKSTOCK:
                        fragment.closeDialog();
                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
                            //检查版本成功后获取ABTesting  的数据
                            fragment.gaTrackerCheckout();
                            fragment.startCheckoutActivity();
                        } else {
                            if (activity != null) {
                                ShoppingCartErrorMsgBean errorMsg = (ShoppingCartErrorMsgBean) msg.obj;
                                if (!JToolUtils.expireHandler(activity, errorMsg.getErrorMessage(), 2000)) {
                                    try {
                                        //oos 提示，可点击ok按钮刷新数据
                                        JViewUtils.showMaterialDialogV2(activity, null, errorMsg.getErrorMessage() + "", new MaterialDialogCallback() {
                                            @Override
                                            public void callBack() {
                                                fragment.sendRequest();
                                            }
                                        });
                                        //如果有错误，在点击ok后重新调接口刷新
//                                    if(errorMsg.getErrorItems()!=null) {
//                                        outofstockHandler(errorMsg.getErrorItems());
//                                    }
                                    } catch (Exception ex) {
                                        ex.getStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                    case ShoppingCarDao.REQUEST_MULTIPLECODE:
                        //addBatch 将本地shopping 添加到server
                        fragment.closeDialog();
                        fragment.mProducts.clear();
                        JStorageUtils.savaProductListToLocalCartRepository(activity, ShoppingCartVerticalAdapter.shoppingCarToTMPLocal(fragment.mProducts));
                        if (ShoppingCarDao.RESPONSE_SUCCESS != msg.arg1 && activity != null) {
                            ErrorMsgBean errorMsgBean = (ErrorMsgBean) msg.obj;
                            Toast.makeText(activity, errorMsgBean.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                        fragment.initData();
                        break;
//                    case ShoppingCarDao.REQUEST_STORECREDIT:
//                        fragment.closeDialog();
//                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS && activity != null) {
//                            ShoppingCartDeleteCellEntity shoppingCartDeleteCellEntity = (ShoppingCartDeleteCellEntity) msg.obj;
//                            fragment.mCar.setSummaryQty(shoppingCartDeleteCellEntity.getSummaryQty());
//                            fragment.mCar.setSubTotal(shoppingCartDeleteCellEntity.getSubTotal());
//                            fragment.mCar.setStoreCreditMessage(shoppingCartDeleteCellEntity.getStoreCreditMessage());
//                            fragment.mCar.setGrandTotal(shoppingCartDeleteCellEntity.getGrandTotal());
//                            fragment.mCar.setStoreCredit(shoppingCartDeleteCellEntity.getStoreCredit());
//                            if (fragment.mCar.getStoreCredit() != null) {
//                                fragment.gaTrackerRedeem(fragment.REDEEM);
//                            } else {
//                                fragment.gaTrackerRedeem(fragment.UNREDEEM);
//                            }
//                            fragment.initShoppingCartData(fragment.mCar, false);
//                        } else if (activity != null) {
//                            String errorMsg = String.valueOf(msg.obj);
//                            if (!JToolUtils.expireHandler(activity, errorMsg, 2000)) {
//                                JViewUtils.showErrorToast(activity, errorMsg);
//                            }
//                        }
//                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

//    public void outofstockHandler(ErrorItemsBean errorItemsBean) {
//        //目前ErrorItemsBean 里所有的list只是暂时 拼合，以判断是否oos
//        List<String> ids = new ArrayList<String>();
//        if (errorItemsBean.getOos_ids() != null) {
//            ids.addAll(errorItemsBean.getOos_ids());
//        }
//        if (errorItemsBean.getDisabled_ids() != null) {
//            ids.addAll(errorItemsBean.getDisabled_ids());
//        }
//        if (errorItemsBean.getInsufficient_ids() != null) {
//            ids.addAll(errorItemsBean.getInsufficient_ids());
//        }
//        if (errorItemsBean.getUnapproved_ids() != null) {
//            ids.addAll(errorItemsBean.getUnapproved_ids());
//        }
//
//        for (int i = 0; i < mProducts.size(); i++) {
//            if (mProducts.get(i) instanceof ShoppingCartListEntityBody) {
//                continue;
//            }
//            ShoppingCartListEntityCell cell = (ShoppingCartListEntityCell) mProducts.get(i);
//            for (int j = 0; j < ids.size(); j++) {
//                if (cell.getId().equals(ids.get(j))) {
//                    cell.setInStock("0");
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }

    public void closeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void gaTrackerRedeem(int type) {
        if (getActivity() != null) {
            String action = "";
            if (type == REDEEM) {
                action = "Redeem Credit";
            } else {
                action = "Cancel Redeeming Credit";
            }
            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                    action,
                    null,
                    null);
        }
    }

    public void gaTrackerApplyCode(int type) {
        if (getActivity() != null) {
            String typeStr = "";
            if (type == APPLIED) {
                typeStr = "Applied";
            } else {
                typeStr = "Unapplied";
            }
            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                    "Apply Voucher Code",
                    typeStr,
                    null);
            JLogUtils.i("googleGA", "Remove Item From Cart");
        }
    }

    private final static int VOUCHER_APPLY_HINT_SUCCESS = 101;
    private final static int VOUCHER_APPLY_HINT_FIALD = 102;
    private final static int VOUCHER_APPLY_HINT_HIDE = 103;

    public void voucherCodeHintMsg(int type, String errorMsg) {
        if (type == VOUCHER_APPLY_HINT_SUCCESS) {
            tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_right);
            tvApplyTextAnim.setTextColor(getResources().getColor(R.color.green_common));
            String apply_hintNav = getResources().getString(R.string.apply_hint_green);
            String apply_hint = apply_hintNav.replace("$voucherCode$", voucherCode);
            tvApplyTextAnim.setText(apply_hint);
            llApplyAnim.setVisibility(View.VISIBLE);
        } else if (type == VOUCHER_APPLY_HINT_FIALD) {
            tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_error);
            tvApplyTextAnim.setTextColor(getResources().getColor(R.color.redC1033D));
            tvApplyTextAnim.setText(errorMsg);
            llApplyAnim.setVisibility(View.VISIBLE);
        } else if (type == VOUCHER_APPLY_HINT_HIDE) {
            llApplyAnim.setVisibility(View.GONE);
        }
    }

    public void showOrHideView(int type) {
        llCheckout.setTag(false);
        if (type == VIEW_NOTHING_SHOW) {//1
            llNothing.setVisibility(View.VISIBLE);
            llCheckout.setVisibility(View.GONE);
            swipeRefrshLayout.setVisibility(View.GONE);
            showSearch = true;
            getActivity().supportInvalidateOptionsMenu();
        } else if (type == VIEW_NOTHING_HIDE) {//2
            llNothing.setVisibility(View.GONE);
            llCheckout.setVisibility(View.VISIBLE);
            swipeRefrshLayout.setVisibility(View.VISIBLE);
            showSearch = false;
            getActivity().supportInvalidateOptionsMenu();
        } else if (type == VIEW_INIT_HIDE) {//4
            llCheckout.setVisibility(View.GONE);
            swipeRefrshLayout.setVisibility(View.GONE);
        } else if (type == VIEW_INIT_SHOW) {//3
            llCheckout.setVisibility(View.VISIBLE);
            swipeRefrshLayout.setVisibility(View.VISIBLE);
        } else if (type == VIEW_NOTNETWORK_SHOW) {//5
            connectionBreak.setVisibility(View.VISIBLE);
        } else if (type == VIEW_NOTNETWORK_HIDE) {//6
            connectionBreak.setVisibility(View.GONE);
        } else if (type == VIEW_VOUCHER_SHOW) {//7
            etVoucherApply.setText("");
//            vVoucher.setVisibility(View.VISIBLE);
        } else if (type == VIEW_VOUCHER_HIDE) {//8
//            vVoucher.setVisibility(View.GONE);
            llApplyAnim.setVisibility(View.GONE);
        }
    }

    public void offlineHandler(ShoppingCartListEntityCart object) {
        initShoppingCartData(object, true);
    }

    public void setDiscountPrice(double disCount, String title) {
        llVoucherPrice.setVisibility(View.VISIBLE);
        tvVoucher.setText("-RM " + JDataUtils.formatDouble((Math.abs(disCount)) + ""));
        tvVoucherWorld.setText(title);
    }

    private void initShoppingCartData(ShoppingCartListEntityCart cart, boolean isInit) {
        if (getActivity() != null) {
            if (GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
                if (!TextUtils.isEmpty(cart.getGst())) {
                    mTvGst.setVisibility(View.VISIBLE);
                    mTvGst.setText("(" + cart.getGst().trim() + ")");
                } else {
                    mTvGst.setText("");
                }
            } else {
                mTvGst.setVisibility(View.GONE);
            }
            tvSubtotal.setText("RM " + JDataUtils.formatDouble(cart.getSubTotal()));
            tvGrandTotal.setText(" RM " + JDataUtils.formatDouble(cart.getGrandTotal()));
            if (cart.getShipping() != null) {
                tvShippingFree.setText("RM " + cart.getShipping().getValue());
            }
            setButtonQty(cart.getSummaryQty());
            saveShoppingCartCount(cart.getSummaryQty());
            //discount
            if (cart.getDiscount() != null && !TextUtils.isEmpty(cart.getDiscount().getCouponCode())) {
                switchVoucheStatus(cart.getDiscount().getCouponCode(), STATUS_VOUCHERCODE_CANCEL);
                voucherCodeHintMsg(VOUCHER_APPLY_HINT_SUCCESS, cart.getDiscount().getCouponCode());
                if (!TextUtils.isEmpty(cart.getDiscount().getValue()) && !TextUtils.isEmpty(cart.getDiscount().getTitle())) {
                    setDiscountPrice(Double.parseDouble(cart.getDiscount().getValue()), cart.getDiscount().getTitle());
                } else {
                    llVoucherPrice.setVisibility(View.GONE);
                }
            } else if (cart.getDiscount() != null) {
                if ("1".equals(cart.getDiscount().getStopRulesProcessing())) {
                    showOrHideView(VIEW_VOUCHER_HIDE);
                } else {
                    showOrHideView(VIEW_VOUCHER_SHOW);
                    switchVoucheStatus("", STATUS_VOUCHERCODE_APPLY);
                    voucherCodeHintMsg(VOUCHER_APPLY_HINT_HIDE, "");
                }
                if (!TextUtils.isEmpty(cart.getDiscount().getValue()) && !TextUtils.isEmpty(cart.getDiscount().getTitle())) {
                    setDiscountPrice(Double.parseDouble(cart.getDiscount().getValue()), cart.getDiscount().getTitle());
                } else {
                    llVoucherPrice.setVisibility(View.GONE);
                }
            } else {
                showOrHideView(VIEW_VOUCHER_SHOW);
                switchVoucheStatus("", STATUS_VOUCHERCODE_APPLY);
                voucherCodeHintMsg(VOUCHER_APPLY_HINT_HIDE, "");
                llVoucherPrice.setVisibility(View.GONE);
            }
//            mCampaignPopText = cart.getPopupText();
//            mCampaignBanner = cart.getAndroidCampBanner();
//            if (cart.getCanUseCampaign() == 1) {
//                showCampaignProduct(campaignProductId, cart.getAndroidCampBanner(), cart.getPopupText());
//            } else {
//                campaignProductId = "";
//                mCampaignBanner = "";
//                mCampaignPopText = "";
//                hideCampaignProduct();
//            }
            if (isInit) {
                if (mProducts != null) {
                    mProducts.clear();
                }
                if (cart.getItems() != null) {
                    ShoppingCartListEntityCell[] items = cart.getItems();
                    boolean isCampaignProduct = false;
                    for (ShoppingCartListEntityCell cell : items) {
                        if (cell.getQty() != null) {
                            cell.setCurrStockQty(Integer.parseInt(cell.getQty()) + cell.getStockQty() + "");
                        }
                        mProducts.add(cell);
//                        if (cell.getIsCampaignProduct() == 1) {
////                            isCampaignProduct = true;
//                            campaignProductId = cell.getProductId();
//                        }
                    }
//                    if (!isCampaignProduct) {
//                        campaignProductId = "";
//                    }
                    if (mProducts.size() > 0) {
                        mProducts.add(new ShoppingCartListEntityBody(ShoppingCartVerticalAdapter.TYPE_BODY));
                    }
                }
                tv_shoppingbottominfo_blank.setVisibility(View.GONE);
                adapter.setInitBlankView(true);//预计算布局高度,初始化空白布局的大小
                adapter.notifyDataSetChanged();
            }
            if (mProducts != null && mProducts.size() > 1) {
                showOrHideView(VIEW_NOTNETWORK_HIDE);
                showOrHideView(VIEW_NOTHING_HIDE);
            } else {
                showOrHideView(VIEW_NOTHING_SHOW);
                showOrHideView(VIEW_NOTNETWORK_HIDE);
            }
            //initStoreCredit

//            if (cart.getStoreCredit() != null) {
//                switchStoreCredit(STATUS_STORECREDIT_CANCEL);
//                llBottomStoreCredit.setVisibility(View.VISIBLE);
//                tvStoreCreditWorld.setText(cart.getStoreCredit().getTitle());
//                tvStoreCreditValue.setText("-RM " + JDataUtils.formatDouble(cart.getStoreCredit().getValue()));
//            } else {
//                switchStoreCredit(STATUS_STORECREDIT_REEDEM);
//                llBottomStoreCredit.setVisibility(View.GONE);
//            }
//
//
//            if (cart.getStoreCreditMessage() != null && GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
//                llStoreCredit.setVisibility(View.VISIBLE);
//                tvStoreCreditTitle.setText(cart.getStoreCreditMessage().getMessage());
//                tvStoreCreditPrice.setText(cart.getStoreCreditMessage().getValue());
//            } else {
//                llStoreCredit.setVisibility(View.GONE);
//            }
        }
        if (mGATrackTimeEnable) {
            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                    GaTrackHelper.GA_TIME_CATEGORY_CHECKOUT, mGATrackTimeStart, "Cart Loading"
            );
            mGATrackTimeEnable = false;
        }
    }

    public void saveShoppingCartCount(int num) {
        if (GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
            GOUserEntity userEntity = GemfiveApplication.getAppConfiguration().getUserInfo(getActivity());
            userEntity.setCartItemCount(num);
            GemfiveApplication.getAppConfiguration().updateDate(getActivity(), userEntity);
        }
    }

    public void updateShoppingData(int qty, String grandTotal, String total) {
        mCar.setSummaryQty(qty);
        mCar.setGrandTotal(grandTotal);
        mCar.setSubTotal(total);
        initShoppingCartData(mCar, false);
    }

    public void updateShoppingData(ShoppingCartDeleteCellEntity bean) {
        mCar.setDiscount(bean.getDiscount());
        mCar.setSubTotal(bean.getSubTotal());
        mCar.setGrandTotal(bean.getGrandTotal());
        mCar.setShipping(bean.getShipping());
        mCar.setCanUseCampaign(bean.getCanUseCampaign());
        mCar.setSummaryQty(bean.getSummaryQty());
        mCar.setPopupText(bean.getPopupText());
        mCar.setAndroidCampBanner(bean.getAndroidCampBanner());
        mCar.setStoreCreditMessage(bean.getStoreCreditMessage());
        mCar.setStoreCredit(bean.getStoreCredit());
//        if (bean.isCampaignProduct()) {
//            campaignProductId = "";
//        }
        initShoppingCartData(mCar, false);
    }

    @Override
    public void onPause() {
        //停止监听，并且将gapHeight归零
        delHeightListenerOnInfoView();
        //gapHeight = 0;
        mIsFromLogin = false;
        voucherCode = etVoucherApply.getText().toString();
        super.onPause();
    }

    public void switchVoucheStatus(String code, int type) {
        if (STATUS_VOUCHERCODE_APPLY == type) {
            tvApply.setText(mApplyStr);
            tvApply.setBackground(JImageUtils.getButtonBackgroudSolidDrawable(getActivity()));
//            tvApply.setBackground(getResources().getDrawable(R.drawable.big_button_style_config));
            llApplyAnim.setVisibility(View.GONE);
            etVoucherApply.setEnabled(true);
            etVoucherApply.setText(voucherCode);
        } else {
            etVoucherApply.setText(code);
            etVoucherApply.setEnabled(false);
            clearVoucher.setVisibility(View.GONE);
            tvApply.setText(mCancelStr);
            tvApply.setBackground(getResources().getDrawable(R.drawable.big_button_style_black));
        }
    }


//    private final int STATUS_STORECREDIT_REEDEM = 22;
//    private final int STATUS_STORECREDIT_CANCEL = 23;

//    public void switchStoreCredit(int fromType) {
//        if (fromType == STATUS_STORECREDIT_REEDEM) {
//            tvStoreCredit.setText(mReedemStr);
//            //tvStoreCredit.setBackgroundResource(R.color.purple);
//            tvStoreCredit.setBackground(getResources().getDrawable(R.drawable.big_button_style_config));
//            llStoreCreditHint.setVisibility(View.GONE);
//        } else {
//            tvStoreCredit.setText(mCancelStr);
//            //tvStoreCredit.setBackgroundResource(R.color.black000000);
//            tvStoreCredit.setBackground(getResources().getDrawable(R.drawable.big_button_style_black));
//            llStoreCreditHint.setVisibility(View.VISIBLE);
//        }
//    }

//    public void initCampaignSize() {
//        int width = GemfiveApplication.getPhoneConfiguration().getScreenWidth(getActivity());
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivUpdate.getLayoutParams();
//        params.height = (int) (width * (200 / 720.0));
//        params.width = ActionBar.LayoutParams.MATCH_PARENT;
//        ivUpdate.setLayoutParams(params);
//    }

//    private class ImageLoadingListener implements RequestListener<String, Bitmap> {
//        @Override
//        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//            vProgress.setVisibility(View.GONE);
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//            if (getActivity() != null) {
//                ivUpdate.setImageBitmap(resource);
//                vProgress.setVisibility(View.GONE);
//                return true;
//            }
//            return false;
//        }
//    }

//    public void showCampaignProduct(final String campaignProductId, String banner, final String popText) {
//        vCampaign.setVisibility(View.VISIBLE);
//        ivUpdate.setVisibility(View.VISIBLE);
//        initCampaignSize();
//        if (banner != null && !banner.equals(String.valueOf(ivUpdate.getTag()))) {
//            ivUpdate.setTag(banner);
//            JImageUtils.downloadImageFromServerListener(getActivity(), mImageLoader, ivUpdate, banner, new ImageLoadingListener());
//        } else {
//            vProgress.setVisibility(View.GONE);
//        }
//    }

//    public void hideCampaignProduct() {
//        vCampaign.setVisibility(View.GONE);
//        ivUpdate.setVisibility(View.GONE);
//        for (int i = 0; i < mProducts.size(); i++) {
//            if (mProducts.get(i) instanceof ShoppingCartListEntityBody) {
//                continue;
//            }
//            if (((ShoppingCartListEntityCell) mProducts.get(i)).getIsCampaignProduct() == 1) {
//                mProducts.remove(i);
//                adapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }


    public void setButtonQty(int sunQty) {
        if (getActivity() != null) {
            if (sunQty == 1) {
                String checkoutStr = getResources().getString(R.string.check_out_num_product).replace("{num}", sunQty + "");
                if (!checkoutStr.equals(tvCheckout.getText().toString())) {
                    tvCheckout.setText(checkoutStr);
                }
            } else {
                String checkoutStr = getResources().getString(R.string.check_out_num_products).replace("{num}", sunQty + "");
                if (!checkoutStr.equals(tvCheckout.getText().toString())) {
                    tvCheckout.setText(checkoutStr);
                }
            }
        }
    }

    public void synShoppingCart() {
        if (mCar != null && mCar.getItems() != null && mCar.getItems().length > 0) {
            mCarDao.addBatch(GemfiveApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey(), mCar.getItems());
        }
    }

    public void onLineHandler(ShoppingCartListEntityCart object) {
        if (object.getItems() != null && object.getItems().length > 0) {
            mProducts = toShoppingCartList(object.getItems());
            JLogUtils.d("getShoppingCartInfo", "size:" + mProducts.size());
            JLogUtils.d("getShoppingCartInfo", "addBatch:");
            synShoppingCart();
        } else {
            JLogUtils.d("getShoppingCartInfo", "get shoppinglist from server:");
            sendRequest();
        }
    }

    public LinkedList<ShoppingCartListBase> toShoppingCartList(ShoppingCartListBase[] cell) {
        LinkedList<ShoppingCartListBase> cells = new LinkedList<>();
        for (int i = 0; i < cell.length; i++) {
            cells.add(cell[i]);
        }
        return cells;
    }

    public void gaTrackerCheckout() {
        if (getActivity() != null) {
            int sumNum = 0;
            for (int i = 0; i < mCar.getItems().length; i++) {
                sumNum += Integer.parseInt(mCar.getItems()[i].getQty());
            }
            try {
                FirebaseEventUtils.getInstance().ecommerceBeginCheckout(getActivity(), JDataUtils.formatDouble(mCar.getGrandTotal()));
                GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
                        "Tap Checkout Items Button",
                        null,
                        0l);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long currTime;

    private void sendRequest() {
        if (!swipeRefrshLayout.isRefreshing()) {
            showDialog();
        }
        currTime = System.currentTimeMillis();
        String sessionKey="";
        if(GemfiveApplication.getAppConfiguration().isSignIn(getActivity())){
            sessionKey=GemfiveApplication.getAppConfiguration().getUser().getSessionKey();
        }
        mCarDao.getShoppingCarInfo(sessionKey);
    }

    private void showDialog() {
        if (getActivity() != null) {
            mDialog = JViewUtils.showProgressDialog(getActivity());
        }
    }


    private void initData() {
        showOrHideView(VIEW_INIT_HIDE);
//        swipeRefrshLayout.requestFocus();
//        swipeRefrshLayout.setFocusableInTouchMode(true);
//        swipeRefrshLayout.setFocusable(true);
//        btnBack.setImageResource(shoppingCallback.getLeftButtonImage());
        mHandler = new DataHandler(getActivity(), this);
        timeHandler = new TimeHandler(this);
        mCarDao = new ShoppingCarDao(TAG, mHandler);
        mCancelStr = getResources().getString(R.string.shoppingcart_btn_cancel);
        mApplyStr = getResources().getString(R.string.shoppingcart_btn_apply);
        mReedemStr = getResources().getString(R.string.redeem);
        initAdapter();
        sendRequest();
//        mCarDao.getShoppingCartLocalInfo(getActivity());
    }

    private void initAdapter() {
        mProducts = new LinkedList<>();
        adapter = new ShoppingCartVerticalAdapter(getActivity(), mProducts, mImageLoader, this);
        adapter.setItemOnClickListener(mItemListener);
        listView.setAdapter(adapter);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ShoppingCartFragment.d
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingCartVerticalFragment newInstance(int param1, Long param2) {
        ShoppingCartVerticalFragment fragment = new ShoppingCartVerticalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putLong(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  参数来源于newInstance的赋值
        if (getArguments() != null) {
            fromType = getArguments().getInt(ARG_PARAM1, FROM_OTHER);
            mGATrackTimeStart = getArguments().getLong("mGATrackTimeStart", 0);
            mGATrackTimeEnable = true;
        }

        showCart = false;
        showSearch = false;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mHomeCallback != null) {
            mHomeCallback.setTitle(getResources().getString(R.string.SHOPPINGCART));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    // item和底部布局高度，判断是否需要在中间添加空白view,以致使底部布局靠底
    int recyclerViewHeight = 0;
    int itemHieght = 0;
    int gapHeight = 0;
    int oldAllItemHeight = 0;
    ViewTreeObserver.OnPreDrawListener onPreDrawListener;

    private void addHeightListenerOnInfoView() {
        //底部是否需要贴底
        recyclerViewHeight = JScreenUtils.getScreenHeight(getActivity()) - JViewUtils.getToolBarHeight(getContext());
        ViewTreeObserver vto = infoView.getViewTreeObserver();
        onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (mProducts != null && mProducts.size() <= 2 && itemHieght != 0) {
                    // 标题栏高度
                    Rect rNavBar = new Rect();
                    listView.getWindowVisibleDisplayFrame(rNavBar);
                    int allItemHeight = infoView.getHeight() + rNavBar.top + itemHieght;
                    int tempGapHeight = recyclerViewHeight - allItemHeight;
                    //获取差距高
                    if (tempGapHeight != 0 && gapHeight != tempGapHeight) {
                        //tempGapHeight是负的代表要减多少，如果减得过多，直接=0并且gone。
                        if (tempGapHeight < 0 && gapHeight > 0) {
                            tempGapHeight = gapHeight + tempGapHeight;
                            if (tempGapHeight <= 0) {
                                gapHeight = 0;
                                tv_shoppingbottominfo_blank.setVisibility(View.GONE);

                                return true;
                            }
                        } else if (tempGapHeight < 0 && gapHeight == 0) {
                            gapHeight = 0;
                            tv_shoppingbottominfo_blank.setVisibility(View.GONE);
                            return true;
                        } else if (tempGapHeight < 0 && gapHeight < 0) {
                            return true;
                        }
                        //recycle总高度比之前小，说明缩小了，tempGapHeight是需要增加的高度
                        if (oldAllItemHeight > allItemHeight) {
                            gapHeight = gapHeight + tempGapHeight;
                        } else {
                            //recycle总高度比之前小，说明增长了，tempGapHeight直接就是想要的高度
                            gapHeight = tempGapHeight;
                        }
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) tv_shoppingbottominfo_blank.getLayoutParams();
                        rl.height = gapHeight;
                        tv_shoppingbottominfo_blank.requestLayout();
                        tv_shoppingbottominfo_blank.setVisibility(View.VISIBLE);
                    }
                    oldAllItemHeight = allItemHeight;
                } else {

                    if (tv_shoppingbottominfo_blank.getVisibility() != View.GONE) {
                        gapHeight = 0;
                        tv_shoppingbottominfo_blank.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        };
        vto.addOnPreDrawListener(onPreDrawListener);
        ////
    }


    @Override
    public void setItemHeightByView(int allItemHeight) {
        Rect rNavBar = new Rect();
        listView.getWindowVisibleDisplayFrame(rNavBar);
        int allItemHeight2 = allItemHeight + rNavBar.top;
        int tempGapHeight = recyclerViewHeight - allItemHeight2;
        if (tempGapHeight > 0) {
            gapHeight = tempGapHeight;
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) tv_shoppingbottominfo_blank.getLayoutParams();
            rl.height = tempGapHeight;
            tv_shoppingbottominfo_blank.requestLayout();
            tv_shoppingbottominfo_blank.setVisibility(View.VISIBLE);
        }
    }

    private void delHeightListenerOnInfoView() {
        ViewTreeObserver vto = infoView.getViewTreeObserver();
        if (onPreDrawListener != null) {
            vto.removeOnPreDrawListener(onPreDrawListener);
        }
    }

    private void hideRecycleViewAndBottom() {
        //每次加载数据都先把listView 和botton隐藏了
        listView.setVisibility(View.INVISIBLE);
        llCheckout.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_cart_vertical, container, false);
        initView(view);
        //加载  shoppingCart 底部布局文件, adapter里通过调用 getInfoView 来获取view,并绑定在viewHolder上。
        infoView = inflater.inflate(R.layout.fragment_shopping_cart_vertical_info, container, false);
        initInfoView(infoView);
        return view;
    }


    @Override
    public void setItemHeight(int itemHeight) {
        this.itemHieght = itemHeight;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HomeBaseFragment.HomeCommonCallback) {
            mHomeCallback = (HomeBaseFragment.HomeCommonCallback) activity;
        }
        baseActivity = (BaseActivity) activity;
//        shoppingCallback= (ShoppingCartCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mCarDao != null) {
            mCarDao.cancelHttpByTag(TAG);
        }
    }
}

package com.whitelabel.app.ui.shoppingcart;

import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ShoppingCartVerticalFragmentV2 {
//     extends ShoppingCartBaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnFocusChangeListener, View.OnClickListener,ShoppingCartContract.View, ShoppingCartAdapterCallback
//    private static final String ARG_PARAM1 = "type";
//    private static final String ARG_PARAM2 = "mGATrackTimeStart";
//    private TextView tvShoppingShippingFeeTitle;
//    public Long mGATrackCheckoutTimeStart = 0L;
//    public Long mGATrackTimeStart = 0L;
//    public boolean mGATrackTimeEnable = false;
//    private CustomSwipefreshLayout swipeRefrshLayout;
//    private DataHandler mHandler;
//    private Handler timeHandler;
//    public RecyclerView shoppingCartRecyclerView;
//    private View infoView;
//    public TextView tvSubtotal;
//    public TextView tvVoucher;
//    private TextView tvApply;
//    private EditText etVoucherApply;
//    public TextView tvGrandTotal;
//    public TextView tvErrorMsg;
//    public LinearLayout llNothing;
//    public LinearLayout llCheckout;
//    public TextView tvCheckout;
//    private LinearLayout llApplyAnim;
//    private TextView tvNullApplyHint;
//    private ImageView tvApplyImageAnim, clearVoucher;
//    private TextView tvApplyTextAnim;
//    private RelativeLayout llVoucherPrice;
//    public TextView tvShippingFree;
//    private TextView tvVoucherWorld, tv_shoppingbottominfo_blank;
//    public ShoppingCartVerticalAdapter adapter;
//    public ArrayList<ShoppingCartListBase> mProducts;
//    private ShoppingCartListEntityCart mCar;
//    private final int REQUESTCODE = 2000;
//    private ShoppingCarDao mCarDao;
//    private String TAG = this.getClass().getSimpleName();
//    private RequestErrorHelper requestErrorHelper;
////    private View vCampaign; vProgress
//    private View  connectionBreak;
//    public int fromType;
//    private Dialog mDialog;
//    private LinearLayout btnTry;
//    private String mCancelStr, mApplyStr;
//    private String voucherCode = "";
//    private RelativeLayout llBody;
//    private TextView btnGoShopping;
//    private final int APPLIED = 1;
//    private final int UNAPPLIED = 2;
//    private final int REDEEM = 1;
//    private int currStatus;
//    private final static int LOADING = 100;
//    private final static int LOADSUCCESS = 101;
//    private HomeCommonCallback mHomeCallback;
//    private ImageLoader mImageLoader;
//    private TextView mTvGst;
//    private ShoppingCartContract.Presenter mPresenter;
//    @Override
//    public boolean getSwipeRefreshStatus() {
//        return swipeRefrshLayout.isRefreshing();
//    }
//
//    @Override
//    public void onRefresh() {
//        //暂时没有用到
//        mPresenter.loadData(false);
//    }
//
//    @Override
//    public void dissmissProgressDialog() {
//        closeProgressDialog();
//    }
//
//    @Override
//    public void showNetErrorMsg() {
//
//    }
//
//    @Override
//    public void showSwipeRefrshLayout() {
//        swipeRefrshLayout.setRefreshing(true);
//    }
//
//    @Override
//    public void closeSwipeRefrshLayout() {
//        swipeRefrshLayout.setRefreshing(false);
//    }
//
//    @Override
//    public void showEmptyDataLayout() {
//        llNothing.setVisibility(View.VISIBLE);
//        llCheckout.setVisibility(View.GONE);
//        swipeRefrshLayout.setVisibility(View.GONE);
//        showSearch = true;
//        connectionBreak.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void loadProductsRecyclerView(ShoppingCartListEntityCell[] items) {
//        if (mProducts != null) {
//            mProducts.clear();
//        }
//        Collections.addAll(mProducts, items);
//        if (mProducts.size() > 0) {
//            mProducts.add(new ShoppingCartListEntityBody(ShoppingCartVerticalAdapter.TYPE_BODY));
//        }
//        tv_shoppingbottominfo_blank.setVisibility(View.GONE);
//        adapter.setInitBlankView(true);//预计算布局高度,初始化空白布局的大小
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void setBottomPriceView(ShoppingCartListEntityCart entityCart) {
//        tvSubtotal.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(entityCart.getSubTotal()));
//        tvGrandTotal.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(entityCart.getGrandTotal()));
//        //discount
//        if(entityCart.getShipping()!=null){
//            tvShoppingShippingFeeTitle.setText(entityCart.getShipping().getTitle());
//            tvShippingFree.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+JDataUtils.formatDouble(entityCart.getShipping().getValue()));
//            tvShippingFree.setVisibility(View.VISIBLE);
//        }
//        if(entityCart.getDiscount()!=null&&!TextUtils.isEmpty(entityCart.getDiscount().getValue()) && !TextUtils.isEmpty(entityCart.getDiscount().getTitle())){
//            setDiscountPrice(Double.parseDouble(entityCart.getDiscount().getValue()), entityCart.getDiscount().getTitle());
//        }else{
//            llVoucherPrice.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void hideVercherCodeLayout() {
//        llApplyAnim.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.tv_shoppingcart_apply:
//                checkAndApplyVoucherCode();
//                break;
//            case R.id.tv_sc_checkout:
//                if (getSwipeRefreshStatus()) {
//                    return;
//                }
//                if (!WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
//                    startLoginActivity();
//                } else {
//                    showProgressDialog();
//                    mGATrackCheckoutTimeStart = GaTrackHelper.getInstance().googleAnalyticsTimeStart();
//                    String sessionKey = "";
//                    if (WhiteLabelApplication.getAppConfiguration() != null && WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()) != null) {
//                        sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
//                    }
//                    mCarDao.checkOutofStock(sessionKey);
//                }
//                break;
//            case R.id.try_again:
//                initData();
//                break;
//            case R.id.btn_sc_nothing_goshopping:
//                startHomeActivity();
//                break;
//            case R.id.clear_voucher:
//                etVoucherApply.setText("");
//                break;
//        }
//    }
//    private void checkAndApplyVoucherCode() {
//        if (getSwipeRefreshStatus()) {
//            return;
//        }
//        JViewUtils.cleanCurrentViewFocus(getActivity());
//        String voucherCode = etVoucherApply.getText().toString();
//        if (!WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
//            mVoucherCode = voucherCode;
//            startLoginActivity();
//        } else {
//            if (voucherCode.trim().length() < 1) {
//                tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_error);
//                tvApplyTextAnim.setTextColor(getResources().getColor(R.color.redC1033D));
//                tvApplyTextAnim.setText(getResources().getString(R.string.apply_hint_red));
//                llApplyAnim.setVisibility(View.VISIBLE);
//            } else {
//                showProgressDialog();
//                tvNullApplyHint.setVisibility(View.GONE);
//                String sessionKey = "";
//                if (WhiteLabelApplication.getAppConfiguration() != null && WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()) != null) {
//                    sessionKey = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey();
//                }
//                if (mApplyStr.equalsIgnoreCase(tvApply.getText().toString())) {
//                    mCarDao.applyOrCancelVoucherCode(sessionKey, voucherCode, "0");
//                } else {
//                    mCarDao.applyOrCancelVoucherCode(sessionKey, voucherCode, "1");
//                }
//            }
//        }
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getActivity() != null) {
//            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), true);
//            GaTrackHelper.getInstance().googleAnalytics("Shopping Cart Screen", getActivity());
//            JLogUtils.i("googleGA_screen", "Shopping Cart Screen");
//        }
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (getActivity() != null) {
//            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), false);
//        }
//    }
//    @Override
//    public void onResume() {
//        addHeightListenerOnInfoView();
//        if (getActivity() instanceof HomeActivity && currStatus == LOADSUCCESS) {
//            currStatus = LOADING;
//            initData();
//        }
//        super.onResume();
//    }
//    public void refresh() {
//        initData();
//    }
//    public void startHomeActivity() {
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        startActivity(intent);
//        ((BaseActivity)getActivity()).startActivityTransitionAnim();
//    }
//    public void startLoginActivity() {
//        Intent loginIntent = new Intent(getActivity(), LoginRegisterActivity.class);
//        startActivityForResult(loginIntent, REQUESTCODE);
//        getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
//    }
//    public void initView(View view) {
//        mImageLoader = new ImageLoader(getActivity());
//        btnGoShopping = (TextView) view.findViewById(R.id.btn_sc_nothing_goshopping);
//        JViewUtils.setSoildButtonGlobalStyle(getActivity(),btnGoShopping);
//        llBody = (RelativeLayout) view.findViewById(R.id.ll_body);
//        shoppingCartRecyclerView = (RecyclerView) view.findViewById(R.id.hlv_shoppingcart);
//        llNothing = (LinearLayout) view.findViewById(R.id.ll_sc_nothing);
//        llCheckout = (LinearLayout) view.findViewById(R.id.ll_sc_checkout);
//        tvCheckout = (TextView) view.findViewById(R.id.tv_sc_checkout);
//        connectionBreak = view.findViewById(R.id.connectionBreaks);
//        requestErrorHelper = new RequestErrorHelper(getContext(), connectionBreak);
//        btnTry = (LinearLayout) view.findViewById(R.id.try_again);
//        swipeRefrshLayout = (CustomSwipefreshLayout) view.findViewById(R.id.swipe_container);
//        swipeRefrshLayout.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
//        swipeRefrshLayout.setOnRefreshListener(this);
//        llBody.setFocusable(true);
//        llBody.setFocusableInTouchMode(true);
//        llBody.requestFocus();
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(GridLayoutManager.VERTICAL);
//        manager.setSmoothScrollbarEnabled(true);
//        shoppingCartRecyclerView.setLayoutManager(manager);
//        shoppingCartRecyclerView.setOnTouchListener(gestureTouchListener);
//        mGestureListener = new ShoppingOnGestureListener();
//        gestureDetector = new GestureDetector(getActivity(),mGestureListener);
//        JViewUtils.setSoildButtonGlobalStyle(getActivity(),tvCheckout);
//    }
//    //监听StretchScrollView 上下滑动
//    public GestureDetector gestureDetector;
//    private GestureDetector.OnGestureListener mGestureListener;
//    private View.OnTouchListener gestureTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            return gestureDetector.onTouchEvent(event);
//        }
//    };
//    class ShoppingOnGestureListener extends  GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if (SoftInputShownUtil.isSoftInputShown(getActivity())) {
//                //向下滑就先禁止滑动，隐藏完键盘后再允许滑动
//                if (distanceY < 0) {
//                      shoppingCartRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            return true;
//                        }
//                    });
//                    timeHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                         shoppingCartRecyclerView.setOnTouchListener(gestureTouchListener);
//                        }
//                    }, 300);
//                }
//                JViewUtils.hideKeyboard(getActivity());
//            }
//            return false;
//        }
//    }
//    @Override
//    public View getInfoView() {
//        return infoView;
//    }
//    public void initInfoView(View view) {
//        tv_shoppingbottominfo_blank = (TextView) view.findViewById(R.id.tv_shoppingbottominfo_blank);
//        tvSubtotal = (TextView) view.findViewById(R.id.tv_shoppingcart_subtotal);
//        tvVoucher = (TextView) view.findViewById(R.id.tv_shoppingcart_voucher);
//        tvShoppingShippingFeeTitle= (TextView) view.findViewById(R.id.tv_shopping_shipping_title);
//        tvApply = (TextView) view.findViewById(R.id.tv_shoppingcart_apply);
//        etVoucherApply = (EditText) view.findViewById(R.id.et_shoppingcart_voucher_apply);
//        tvGrandTotal = (TextView) view.findViewById(R.id.tv_shoppingcart_grandtotal);
//        mTvGst = (TextView) view.findViewById(R.id.tv_shoppingcart_gst);
//        tvErrorMsg = (TextView) view.findViewById(R.id.tv_shoppingcart_errormsg);
//        llApplyAnim = (LinearLayout) view.findViewById(R.id.ll_shoppingcart_apply_anim);
//        tvNullApplyHint = (TextView) view.findViewById(R.id.tv_shoppingcart_null_apply_hint);
//        tvApplyImageAnim = (ImageView) view.findViewById(R.id.tv_shoppingcart_apply_image_anim);
//        tvApplyTextAnim = (TextView) view.findViewById(R.id.tv_shoppingcart_apply_text_anim);
//        llVoucherPrice = (RelativeLayout) view.findViewById(R.id.ll_shoppingcart_voucher);
//        tvShippingFree = (TextView) view.findViewById(R.id.tv_shoppingcart_shippingFree);
//        tvVoucherWorld = (TextView) view.findViewById(R.id.tv_voucher_world);
//        clearVoucher = (ImageView) view.findViewById(R.id.clear_voucher);
//        etVoucherApply.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() != 0 && etVoucherApply.isFocused()) {
//                    clearVoucher.setVisibility(View.VISIBLE);
//                } else {
//                    clearVoucher.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable != null) {
//                    voucherCode = editable.toString();
//                }
//            }
//        });
//    }
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
//            switch (v.getId()) {
//                case R.id.et_shoppingcart_voucher_apply:
//                    if (!TextUtils.isEmpty(etVoucherApply.getText().toString())) {
//                        clearVoucher.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
//        } else {
//            clearVoucher.setVisibility(View.GONE);
//        }
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mPresenter=new ShoppingCartPresenter(this,
//                new ShoppingCartManager(DataManager.getInstance().getShoppingCartApi(),DataManager.getInstance().getPreferHelper()),
//                new BaseManager(DataManager.getInstance().getMockApi(),DataManager.getInstance().getAppApi(),DataManager.getInstance().getPreferHelper())
//                );
//        voucherCode = "";
//        currStatus = LOADSUCCESS;
//        if (!(getActivity() instanceof HomeActivity)) {
//                initData();
//        }
//        initListener();
//    }
//    public void initData(){
//        llCheckout.setVisibility(View.GONE);
//        swipeRefrshLayout.setVisibility(View.GONE);
//        mHandler = new DataHandler(getActivity(), this);
//        timeHandler = new Handler();
//        mCarDao = new ShoppingCarDao(TAG, mHandler);
//        mCancelStr = getResources().getString(R.string.shoppingcart_btn_cancel);
//        mApplyStr = getResources().getString(R.string.shoppingcart_btn_apply);
//        initAdapter();
//        mPresenter.loadData(true);
//    }
//    private AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            //除了i 外其他都暂时为null
//            if (mProducts.get(i) instanceof ShoppingCartListEntityBody) {
//                return;
//            }
//            ShoppingCartListEntityCell entity = (ShoppingCartListEntityCell) mProducts.get(i);
//            String visibility = entity.getVisibility();
//            if (!TextUtils.isEmpty(visibility)) {
//                if (("1").equals(visibility)) {
//                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
//                    it.putExtra("productId", ((ShoppingCartListEntityCell) mProducts.get(i)).getProductId());
//                    startActivity(it);
//                    ((BaseActivity)getActivity()).startActivityTransitionAnim();
//                }
//            }
//        }
//    };
//    private void initListener() {
//        btnTry.setOnClickListener(this);
////        ivUpdate.setOnClickListener(this);
////        rlShoppingcartTopGoback.setOnClickListener(this);
//        tvApply.setOnClickListener(this);
//        tvCheckout.setOnClickListener(this);
//        llCheckout.setOnClickListener(this);
//        llBody.setOnTouchListener(touchListener);
//        btnGoShopping.setOnClickListener(this);
////        tvStoreCredit.setOnClickListener(this);
//        clearVoucher.setOnClickListener(this);
//        etVoucherApply.setOnFocusChangeListener(this);
//        //键盘弹起时 禁用下拉刷新功能,否则会与滑动隐藏键盘发生冲突
//        swipeRefrshLayout.setSwitchByKeyboard(true, getActivity());
//    }
//
//    private View.OnTouchListener touchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                if (getActivity() != null && SoftInputShownUtil.isSoftInputShown(getActivity())) {
//                    JToolUtils.closeKeyBoard(getActivity(), etVoucherApply);
//                    return true;
//                }
//            }
//            return false;
//        }
//    };
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode) {
//            case LoginRegisterEmailLoginFragment.RESULTCODE:
//                if (!(getActivity() instanceof HomeActivity)) {
//                    initData();
//                }
//                break;
//        }
//    }
//    private void startCheckoutActivity() {
//        Bundle bundle = new Bundle();
//        if (mCar.getDiscount() != null) {
//            bundle.putSerializable("discountBean", mCar.getDiscount());
//        }
//        try {
//            StringBuffer productIds = new StringBuffer();
//            if (mCar.getItems() != null) {
//                for (int i = 0; i < mCar.getItems().length; i++) {
//                    productIds.append(mCar.getItems()[i].getProductId()).append(",");
//                }
//            }
//            String productsStr = productIds.substring(0, productIds.length() - 1);
//            bundle.putString("productIds", productsStr);
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//        bundle.putInt("fromType", fromType);
//        bundle.putString("grandTotal", tvGrandTotal.getText().toString());
//        bundle.putLong("mGATrackTimeStart", mGATrackCheckoutTimeStart);
//        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        ((BaseActivity)getActivity()).startActivityTransitionAnim();
//    }
//    public final static class DataHandler extends Handler {
//        private WeakReference<Activity> mActivity;
//        private WeakReference<ShoppingCartVerticalFragmentV2> mFragment;
//        public DataHandler(Activity activity, ShoppingCartVerticalFragmentV2 fragment) {
//            mActivity = new WeakReference<Activity>(activity);
//            mFragment = new WeakReference<ShoppingCartVerticalFragmentV2>(fragment);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            final Activity activity = mActivity.get();
//            final ShoppingCartVerticalFragmentV2 fragment = mFragment.get();
//            if (activity != null & fragment != null) {
//                switch (msg.what) {
//                    case ShoppingCarDao.REQUEST_VOUCHERCODE:
//                        fragment.closeDialog();
//                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
//                            ShoppingCartVoucherApplyEntity shoppingCartVoucherApplyEntity = (ShoppingCartVoucherApplyEntity) msg.obj;
//                            fragment.mCar.setDiscount(shoppingCartVoucherApplyEntity.getDiscount());
//                            fragment.mCar.setGrandTotal(shoppingCartVoucherApplyEntity.getGrandTotal());
//                            fragment.mCar.setShipping(shoppingCartVoucherApplyEntity.getShipping());
//                            fragment.initShoppingCartData(fragment.mCar, false);
//                            if (shoppingCartVoucherApplyEntity.getDiscount() != null && !TextUtils.isEmpty(shoppingCartVoucherApplyEntity.getDiscount().getCaption())) {
//                                fragment.gaTrackerApplyCode(fragment.APPLIED);
//                            } else {
//                                fragment.gaTrackerApplyCode(fragment.UNAPPLIED);
//                            }
//                        } else {
//                            ErrorMsgBean errorBean = (ErrorMsgBean) msg.obj;
//                            if (!JToolUtils.expireHandler(activity, errorBean.getErrorMessage(), 2000)) {
//                                fragment.setVoucherFaildMessage(errorBean.getErrorMessage());
//                            }
//                        }
//                        break;
//                    case ShoppingCarDao.REQUEST_ERROR:
//                        fragment.closeDialog();
//                        mFragment.get().swipeRefrshLayout.setRefreshing(false);
//                        if (msg.arg1 == ShoppingCarDao.REQUEST_SHOPPINGINFO) {
//                            fragment.currStatus = LOADSUCCESS;
//                            fragment.requestErrorHelper.showConnectionBreaks(msg);
//                        } else {
//                            fragment.requestErrorHelper.showNetWorkErrorToast(msg);
//
//                        }
//                        break;
//                    case ShoppingCarDao.REQUEST_CHECKSTOCK:
//                        fragment.closeDialog();
//                        if (msg.arg1 == ShoppingCarDao.RESPONSE_SUCCESS) {
//                            //检查版本成功后获取ABTesting  的数据
//                            fragment.gaTrackerCheckout();
//                            fragment.startCheckoutActivity();
//                        } else {
//                            ShoppingCartErrorMsgBean errorMsg = (ShoppingCartErrorMsgBean) msg.obj;
//                            if (!JToolUtils.expireHandler(activity, errorMsg.getErrorMessage(), 2000)) {
//                                try {
//                                    JViewUtils.showMaterialDialogV2(activity, null, errorMsg.getErrorMessage() + "", new MaterialDialogCallback() {
//                                        @Override
//                                        public void callBack() {
//                                            fragment.sendRequest();
//                                        }
//                                    });
//                                } catch (Exception ex) {
//                                    ex.getStackTrace();
//                                }
//                            }
//                        }
//                        break;
//                    case ShoppingCarDao.REQUEST_MULTIPLECODE:
//                        //addBatch 将本地shopping 添加到server
//                        fragment.closeDialog();
//                        fragment.mProducts.clear();
//                        JStorageUtils.savaProductListToLocalCartRepository(activity, ShoppingCartVerticalAdapter.shoppingCarToTMPLocal(fragment.mProducts));
//                        if (ShoppingCarDao.RESPONSE_SUCCESS != msg.arg1) {
//                            ErrorMsgBean errorMsgBean = (ErrorMsgBean) msg.obj;
//                            Toast.makeText(activity, errorMsgBean.getErrorMessage(), Toast.LENGTH_LONG).show();
//                        }
//                        fragment.initData();
//                        break;
//                }
//            }
//            super.handleMessage(msg);
//        }
//    }
//    public void closeDialog() {
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
//    }
//    public void gaTrackerRedeem(int type) {
//        if (getActivity() != null) {
//            String action = "";
//            if (type == REDEEM) {
//                action = "Redeem Credit";
//            } else {
//                action = "Cancel Redeeming Credit";
//            }
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
//                    action,
//                    null,
//                    null);
//        }
//    }
//    public void gaTrackerApplyCode(int type) {
//        if (getActivity() != null) {
//            String typeStr = "";
//            if (type == APPLIED) {
//                typeStr = "Applied";
//            } else {
//                typeStr = "Unapplied";
//            }
//            GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
//                    "Apply Voucher Code",
//                    typeStr,
//                    null);
//            JLogUtils.i("googleGA", "Remove Item From Cart");
//        }
//    }
//    public void setVoucherFaildMessage(String errorMsg){
//        tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_error);
//        tvApplyTextAnim.setTextColor(getResources().getColor(R.color.redC1033D));
//        tvApplyTextAnim.setText(errorMsg);
//        llApplyAnim.setVisibility(View.VISIBLE);
//    }
//    public void setDiscountPrice(double disCount, String title) {
//        llVoucherPrice.setVisibility(View.VISIBLE);
//        tvVoucher.setText("-"+WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble((Math.abs(disCount)) + ""));
//        tvVoucherWorld.setText(title);
//    }
////    private void initShoppingCartData(ShoppingCartListEntityCart cart, boolean isInit) {
////        if (getActivity() == null)return ;
////             if (!TextUtils.isEmpty(cart.getGst())) {
////                    mTvGst.setVisibility(View.VISIBLE);
////                    mTvGst.setText("(" + cart.getGst().trim() + ")");
////              } else {
////                    mTvGst.setText("");
////             }
////             saveShoppingCartCount(cart.getSummaryQty());
////            tvSubtotal.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(cart.getSubTotal()));
////            tvGrandTotal.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" " + JDataUtils.formatDouble(cart.getGrandTotal()));
////            //discount
////            if(cart.getShipping()!=null){
////                tvShoppingShippingFeeTitle.setText(cart.getShipping().getTitle());
////                tvShippingFree.setText(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+JDataUtils.formatDouble(cart.getShipping().getValue()));
////                tvShippingFree.setVisibility(View.VISIBLE);
////            }
////            if(cart.getDiscount()!=null&&!TextUtils.isEmpty(cart.getDiscount().getValue()) && !TextUtils.isEmpty(cart.getDiscount().getTitle())){
////                setDiscountPrice(Double.parseDouble(cart.getDiscount().getValue()), cart.getDiscount().getTitle());
////            }else{
////                llVoucherPrice.setVisibility(View.GONE);
////            }
////           ShoppingDiscountBean discount=cart.getDiscount();
////            if(discount!=null&&!JDataUtils.isEmpty(cart.getDiscount().getCouponCode())) {
////                setLayoutHaveVercherCode(cart.getDiscount().getCouponCode());
////            }else if(discount!=null&&"1".equals(cart.getDiscount().getStopRulesProcessing())){
////                    llApplyAnim.setVisibility(View.GONE);
////            }else{
////                setLayoutNotHaveVercherCode();
////            }
////            if (isInit) {
////                if (mProducts != null) {
////                    mProducts.clear();
////                }
////                   Collections.addAll(mProducts, cart.getItems());
////                    if (mProducts.size() > 0) {
////                        mProducts.add(new ShoppingCartListEntityBody(ShoppingCartVerticalAdapter.TYPE_BODY));
////                    }
////                tv_shoppingbottominfo_blank.setVisibility(View.GONE);
////                adapter.setInitBlankView(true);//预计算布局高度,初始化空白布局的大小
////                adapter.notifyDataSetChanged();
////            }
////            if (mProducts != null && mProducts.size() > 1) {
////                    setLayoutHaveProduct();
////            } else {
////                  setLayoutNotHaveProduct();
////            }
////        if (mGATrackTimeEnable) {
////            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
////                    GaTrackHelper.GA_TIME_CATEGORY_CHECKOUT, mGATrackTimeStart, "Cart Loading"
////            );
////            mGATrackTimeEnable = false;
////        }
////    }
//    public void setLayoutHaveProduct(){
//        connectionBreak.setVisibility(View.GONE);
//        llNothing.setVisibility(View.GONE);
//        llCheckout.setVisibility(View.VISIBLE);
//        swipeRefrshLayout.setVisibility(View.VISIBLE);
//        showSearch = false;
//        getActivity().supportInvalidateOptionsMenu();
//    }
//
//    public void saveShoppingCartCount(int num) {
//        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
//            GOUserEntity userEntity = WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity());
//            userEntity.setCartItemCount(num);
//            WhiteLabelApplication.getAppConfiguration().updateDate(getActivity(), userEntity);
//        }
//    }
//
//    public void updateShoppingData(int qty, String grandTotal, String total) {
//        mCar.setSummaryQty(qty);
//        mCar.setGrandTotal(grandTotal);
//        mCar.setSubTotal(total);
//        initShoppingCartData(mCar, false);
//    }
//
//    public void updateShoppingData(ShoppingCartDeleteCellEntity bean) {
//        mCar.setDiscount(bean.getDiscount());
//        mCar.setSubTotal(bean.getSubTotal());
//        mCar.setGrandTotal(bean.getGrandTotal());
//        mCar.setShipping(bean.getShipping());
//        mCar.setCanUseCampaign(bean.getCanUseCampaign());
//        mCar.setSummaryQty(bean.getSummaryQty());
//        mCar.setPopupText(bean.getPopupText());
//        mCar.setAndroidCampBanner(bean.getAndroidCampBanner());
//        mCar.setStoreCreditMessage(bean.getStoreCreditMessage());
//        mCar.setStoreCredit(bean.getStoreCredit());
//        initShoppingCartData(mCar, false);
//    }
//
//    @Override
//    public void onPause() {
//        //停止监听，并且将gapHeight归零
//        delHeightListenerOnInfoView();
//        //gapHeight = 0;
//        voucherCode = etVoucherApply.getText().toString();
//        super.onPause();
//    }
//
//    public  void  setLayoutHaveVercherCode(String code){
//        etVoucherApply.setText(code);
//        etVoucherApply.setEnabled(false);
//        clearVoucher.setVisibility(View.GONE);
//        tvApply.setText(mCancelStr);
//        tvApply.setBackground(getResources().getDrawable(R.drawable.big_button_style_black));
//        tvApplyImageAnim.setImageResource(R.mipmap.icon_shopping_cart_right);
//        tvApplyTextAnim.setTextColor(getResources().getColor(R.color.green_common));
//        String apply_hintNav = getResources().getString(R.string.apply_hint_green);
//        String apply_hint = apply_hintNav.replace("$voucherCode$", voucherCode);
//        tvApplyTextAnim.setText(apply_hint);
//        llApplyAnim.setVisibility(View.VISIBLE);
//    }
//    public void setLayoutNotHaveVercherCode(){
//        etVoucherApply.setText("");
//        tvApply.setText(mApplyStr);
//        JViewUtils.setSoildButtonGlobalStyle(getContext(),tvApply);
//        llApplyAnim.setVisibility(View.GONE);
//        etVoucherApply.setEnabled(true);
//        etVoucherApply.setText(voucherCode);
//        llApplyAnim.setVisibility(View.GONE);
//    }
//    public void gaTrackerCheckout() {
//        if (getActivity() != null) {
//            try {
//                FirebaseEventUtils.getInstance().ecommerceBeginCheckout(getActivity(), JDataUtils.formatDouble(mCar.getGrandTotal()));
//                GaTrackHelper.getInstance().googleAnalyticsEvent("Cart Action",
//                        "Tap Checkout Items Button",
//                        null,
//                        0L);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
////    private long currTime;
//
//    private void initAdapter() {
//        mProducts = new ArrayList<>();
//        adapter = new ShoppingCartVerticalAdapter(getActivity(), mProducts, mImageLoader, this);
//        adapter.setItemOnClickListener(mItemListener);
//        shoppingCartRecyclerView.setAdapter(adapter);
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @return A new instance of fragment ShoppingCartFragment.d
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ShoppingCartVerticalFragmentV2 newInstance(int param1, Long param2) {
//        ShoppingCartVerticalFragmentV2 fragment = new ShoppingCartVerticalFragmentV2();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, param1);
//        args.putLong(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //  参数来源于newInstance的赋值
//        if (getArguments() != null) {
//            fromType = getArguments().getInt(ARG_PARAM1, FROM_OTHER);
//            mGATrackTimeStart = getArguments().getLong("mGATrackTimeStart", 0);
//            mGATrackTimeEnable = true;
//        }
//
//        showCart = false;
//        showSearch = false;
//        setHasOptionsMenu(true);
//    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (mHomeCallback != null) {
//            mHomeCallback.setTitle(getResources().getString(R.string.SHOPPINGCART));
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    // item和底部布局高度，判断是否需要在中间添加空白view,以致使底部布局靠底
//    int recyclerViewHeight = 0;
//    int itemHieght = 0;
//    int gapHeight = 0;
//    int oldAllItemHeight = 0;
//    ViewTreeObserver.OnPreDrawListener onPreDrawListener;
//
//    private void addHeightListenerOnInfoView() {
//        //底部是否需要贴底
//        recyclerViewHeight = JScreenUtils.getScreenHeight(getActivity()) - JViewUtils.getToolBarHeight(getContext());
//        ViewTreeObserver vto = infoView.getViewTreeObserver();
//        onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                if (mProducts != null && mProducts.size() <= 2 && itemHieght != 0) {
//                    // 标题栏高度
//                    Rect rNavBar = new Rect();
//                    shoppingCartRecyclerView.getWindowVisibleDisplayFrame(rNavBar);
//                    int allItemHeight = infoView.getHeight() + rNavBar.top + itemHieght;
//                    int tempGapHeight = recyclerViewHeight - allItemHeight;
//                    //获取差距高
//                    if (tempGapHeight != 0 && gapHeight != tempGapHeight) {
//                        //tempGapHeight是负的代表要减多少，如果减得过多，直接=0并且gone。
//                        if (tempGapHeight < 0 && gapHeight > 0) {
//                            tempGapHeight = gapHeight + tempGapHeight;
//                            if (tempGapHeight <= 0) {
//                                gapHeight = 0;
//                                tv_shoppingbottominfo_blank.setVisibility(View.GONE);
//
//                                return true;
//                            }
//                        } else if (tempGapHeight < 0 && gapHeight == 0) {
//                            gapHeight = 0;
//                            tv_shoppingbottominfo_blank.setVisibility(View.GONE);
//                            return true;
//                        } else if (tempGapHeight < 0 && gapHeight < 0) {
//                            return true;
//                        }
//                        //recycle总高度比之前小，说明缩小了，tempGapHeight是需要增加的高度
//                        if (oldAllItemHeight > allItemHeight) {
//                            gapHeight = gapHeight + tempGapHeight;
//                        } else {
//                            //recycle总高度比之前小，说明增长了，tempGapHeight直接就是想要的高度
//                            gapHeight = tempGapHeight;
//                        }
//                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) tv_shoppingbottominfo_blank.getLayoutParams();
//                        rl.height = gapHeight;
//                        tv_shoppingbottominfo_blank.requestLayout();
//                        tv_shoppingbottominfo_blank.setVisibility(View.VISIBLE);
//                    }
//                    oldAllItemHeight = allItemHeight;
//                } else {
//                    if (tv_shoppingbottominfo_blank.getVisibility() != View.GONE) {
//                        gapHeight = 0;
//                        tv_shoppingbottominfo_blank.setVisibility(View.GONE);
//                    }
//                }
//                return true;
//            }
//        };
//        vto.addOnPreDrawListener(onPreDrawListener);
//    }
//    @Override
//    public void setItemHeightByView(int allItemHeight) {
//        Rect rNavBar = new Rect();
//        shoppingCartRecyclerView.getWindowVisibleDisplayFrame(rNavBar);
//        int allItemHeight2 = allItemHeight + rNavBar.top;
//        int tempGapHeight = recyclerViewHeight - allItemHeight2;
//        if (tempGapHeight > 0) {
//            gapHeight = tempGapHeight;
//            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) tv_shoppingbottominfo_blank.getLayoutParams();
//            rl.height = tempGapHeight;
//            tv_shoppingbottominfo_blank.requestLayout();
//            tv_shoppingbottominfo_blank.setVisibility(View.VISIBLE);
//        }
//    }
//    private void delHeightListenerOnInfoView() {
//        ViewTreeObserver vto = infoView.getViewTreeObserver();
//        if (onPreDrawListener != null) {
//            vto.removeOnPreDrawListener(onPreDrawListener);
//        }
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_shopping_cart_vertical, container, false);
//        initView(view);
//        infoView = inflater.inflate(R.layout.fragment_shopping_cart_vertical_info, container, false);
//        initInfoView(infoView);
//        setRetryTheme(view);
//        return view;
//    }
//    @Override
//    public void setItemHeight(int itemHeight) {
//        this.itemHieght = itemHeight;
//    }
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        if (activity instanceof HomeBaseFragment.HomeCommonCallback) {
//            mHomeCallback = (HomeCommonCallback) activity;
//        }
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//        }
//        if (mCarDao != null) {
//            mCarDao.cancelHttpByTag(TAG);
//        }
//    }
}

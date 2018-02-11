package com.whitelabel.app.fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.MyAccountOrderDetailActivity;
import com.whitelabel.app.activity.ShoppingCartActivity1;
import com.whitelabel.app.adapter.OrderListRecyclerViewTextAdapter;
import com.whitelabel.app.adapter.OrderListRecyclerViewAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.bean.OrderTip;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ShoppingCarDao;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.MyAccountOrderListEntityResult;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.ui.menuMyOrder.MyOrderContract;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JImageUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by imaginato on 2015/7/28.
 */
public class HomeMyAccountOrdersFragment extends HomeBaseFragment<MyOrderContract.Presenter> implements View.OnClickListener, MyAccountFragmentRefresh, SwipeRefreshLayout.OnRefreshListener ,MyOrderContract.View{
    private BaseActivity homeActivity;
    private final Handler mHandler = new Handler();
    private static final int pageSize = 10;
    private int pageIndex = 1;
    private ArrayList<MyAccountOrderOuter> listOuterRecord;
    private Dialog mDialog;
    private RequestErrorHelper requestErrorHelper;
    private View connectionBreaks;
    private ViewStub vsEmpty;
    private RefreshLoadMoreRecyclerView recyclerView;
    private OrderListRecyclerViewAdapter mOrderListRecyclerViewAdapter;
    private OrderListRecyclerViewTextAdapter mOrderListRecyclerViewTextAdapter;
    private SwipeRefreshLayout swipeRefrshLayout;
    private MyAccountDao mMyAccountDao;
    private ShoppingCarDao mShoppingCarDao;
    private ImageLoader mImageLoader;
    private DataHandler dataHandler;
    private boolean isImageRcyList=true;
    private ImageView ivChangeRcyListToogle;
    public static final String ORDER_ERROR_MESSAGE="orderErrorMessage";
    private View rootView;
    private int orderClickItemsCount=0;
    private int currentShoppingCount=0;
    @Override
    public void onRefresh() {
        pageIndex = 1;
        sendRequest();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            homeActivity = (BaseActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inject() {
        super.inject();
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
            presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again:
                connectionBreaks.setVisibility(View.GONE);
                showSwipeRefreshDialog();
                sendRequest();
                break;
            case R.id.rl_recyclerview_change_layout_toggle:
                if (isImageRcyList){
                    ivChangeRcyListToogle.setImageResource(R.mipmap.ic_view_double);
                    recyclerView.setAdapter(mOrderListRecyclerViewTextAdapter);
                    mOrderListRecyclerViewTextAdapter.notifyDataSetChanged();
                    isImageRcyList=!isImageRcyList;
                }else {
                    ivChangeRcyListToogle.setImageResource(R.mipmap.ic_view_single);
                    recyclerView.setAdapter(mOrderListRecyclerViewAdapter);
                    mOrderListRecyclerViewAdapter.updateDataChange();
                    mOrderListRecyclerViewAdapter.notifyDataSetChanged();
                    isImageRcyList=!isImageRcyList;
                }
                break;

        }
    }
    private void showSwipeRefreshDialog() {
        dataHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefrshLayout.setRefreshing(true);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_myaccount_order_list_new, null);
        rootView=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_myaccount_order_list_new,null);
        setRetryTheme(view);
        mImageLoader = new ImageLoader(homeActivity);
        recyclerView = (RefreshLoadMoreRecyclerView) view.findViewById(R.id.rcy_order_list);
        vsEmpty = (ViewStub) view.findViewById(R.id.tv_myaccount_orderlist_empty_new);
        vsEmpty.setLayoutResource(R.layout.fragment_order_empty);
        connectionBreaks=view.findViewById(R.id.connectionBreaks);
        requestErrorHelper=new RequestErrorHelper(getContext(),connectionBreaks);
        swipeRefrshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        LinearLayout tryAgain = (LinearLayout) view.findViewById(R.id.try_again);
        view.findViewById(R.id.rl_recyclerview_change_layout_toggle).setOnClickListener(this);
        ivChangeRcyListToogle =(ImageView) view.findViewById(R.id.iv_change_rcy_list_toogle);
        tryAgain.setOnClickListener(this);
        swipeRefrshLayout.setOnRefreshListener(this);
//        swipeRefrshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefrshLayout.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCommonCallback != null) {
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_ORDER);
        }
        dataHandler = new DataHandler(homeActivity, this);
        String TAG = this.getClass().getSimpleName();
        mMyAccountDao = new MyAccountDao(TAG, dataHandler);
        mShoppingCarDao = new ShoppingCarDao(TAG, dataHandler);
        init();
        initRecyclerView();
        String userId = "";
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(getActivity())) {
            userId = WhiteLabelApplication.getAppConfiguration().getUser().getId();
        }
        mMyAccountDao.getLocalOrderData(getActivity(), userId);
        showSwipeRefreshDialog();
        setHasOptionsMenu(true);
    }

    private void initRecyclerView() {
        recyclerView.setPullLoadEnable(false);
        mOrderListRecyclerViewAdapter = new OrderListRecyclerViewAdapter(homeActivity,recyclerView, listOuterRecord, true, mImageLoader);
        JLogUtils.d("list_size", listOuterRecord.size() + "");
        recyclerView.setAdapter(mOrderListRecyclerViewAdapter);
        mOrderListRecyclerViewAdapter.notifyItemInserted(listOuterRecord.size() - 1);
        mOrderListRecyclerViewAdapter.updateDataChange();
        recyclerView.setOnRefreshListener(new RefreshLoadMoreRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.stopRefresh();
            }
        });
        recyclerView.setLoadMoreListener(new RefreshLoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pageIndex > 1) {
                    sendRequest();
                } else {
                    recyclerView.stopLoadMore();
                }
            }
        });
        mOrderListRecyclerViewAdapter.setOnOrderViewItemClickListener(new OrderListRecyclerViewAdapter.OnOrderViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position,ArrayList<OrderBody> orders) {

                switch(view.getId()){
                    //reorder
                    case R.id.btn_order_list_item_addtocart:
                    case R.id.iv_add_to_cart:
                    case R.id.rl_add_to_cart:
                        mPresenter.setToCheckout(orders);
                        orderClickItemsCount = getOrderItemsCount(orders);
                        break;
                        //skip to order Detail page
                     default:
                         MyAccountOrderOuter orderOuterParam = null;
                         final ArrayList arrayList = mOrderListRecyclerViewAdapter.getDataList(listOuterRecord);
                         for (MyAccountOrderOuter orderOuter : listOuterRecord) {
                             if (arrayList.get(position) instanceof OrderTip) {
                                 if (((OrderTip) arrayList.get(position)).getOrderNumber().equals(orderOuter.getOrderSn())) {
                                     orderOuterParam = orderOuter;
                                     break;
                                 }
                             } else {
                                 if (((OrderBody) arrayList.get(position)).getOrderNumber().equals(orderOuter.getOrderSn())) {
                                     orderOuterParam = orderOuter;
                                     break;
                                 }
                             }
                         }
                         skipToOrderDetailPage(orderOuterParam);
                         break;
                }
            }
        });
        mOrderListRecyclerViewTextAdapter=new OrderListRecyclerViewTextAdapter(homeActivity, listOuterRecord);
        mOrderListRecyclerViewTextAdapter.notifyItemInserted(listOuterRecord.size() - 1);
        mOrderListRecyclerViewAdapter.updateDataChange();
        mOrderListRecyclerViewTextAdapter.setOnOrderTextViewItemClickListener(new OrderListRecyclerViewTextAdapter.OnOrderTextViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                skipToOrderDetailPage(listOuterRecord.get(position));
            }
        });
    }

    private Integer getOrderItemsCount(ArrayList<OrderBody> orderBodies){
        int itemCounts = 0;
        if (orderBodies!=null && !orderBodies.isEmpty()){
            for (OrderBody orderBody:orderBodies){
                itemCounts += Integer.valueOf(orderBody.getOrderQuantity());
            }
        }
        return itemCounts;
    }

    private void skipToOrderDetailPage(MyAccountOrderOuter orderOuter_param) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderOuter", orderOuter_param);
        Intent intent0 = new Intent(homeActivity, MyAccountOrderDetailActivity.class);
        intent0.putExtras(bundle);
        homeActivity.startActivity(intent0);
    }

    private void initData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    showSwipeRefreshDialog();
                    pageIndex = 1;
                    sendRequest();
                }
            }
        });
    }
    private void init() {
        recyclerView.setVisibility(View.GONE);
        if (listOuterRecord == null) {
            listOuterRecord = new ArrayList<>();
        } else {
            listOuterRecord.clear();
        }
    }

    @Override
    public void loadShoppingCount(int count) {
        currentShoppingCount=count;
    }

    @Override
    public void showNetErrorMessage() {
        RequestErrorHelper requestErrorHelper=new RequestErrorHelper(homeActivity);
        requestErrorHelper.showNetWorkErrorToast();
    }
    @Override
    public void showFaildMessage(String faildMessage) {
        JViewUtils.showMaterialDialog(homeActivity, "", faildMessage,null);
    }

    @Override
    public void showReorderErrorMessage(String errorMsg) {
        JViewUtils.showPopUpWindw(homeActivity,rootView,errorMsg);
    }

    @Override
    public void showReorderSuccessMessage() {
        JViewUtils.showHintToast(homeActivity.getResources().getString(R.string.add_order_to_checkout));
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeMyAccountOrdersFragment> mFragment;

        public DataHandler(Activity activity, HomeMyAccountOrdersFragment fragment) {
            mActivity = new WeakReference<>(activity);
            mFragment = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case MyAccountDao.LOCAL_ORDER_GET:
                    @SuppressWarnings("unchecked")
                    List<MyAccountOrderOuter> results = (List<MyAccountOrderOuter>) msg.obj;
                    if (mFragment.get().listOuterRecord != null && mFragment.get().listOuterRecord.size() > 0) {
                        return;
                    }
                    if (results != null && results.size() > 0) {
                        mFragment.get().listOuterRecord.addAll(mFragment.get().initArray(results));
                        mFragment.get().mOrderListRecyclerViewAdapter.updateDataChange();
                        mFragment.get().mOrderListRecyclerViewAdapter.notifyDataSetChanged();
                        mFragment.get().recyclerView.setVisibility(View.VISIBLE);
                    }
                    mFragment.get().pageIndex = 1;
                    mFragment.get().sendRequest();
                    break;
                case MyAccountDao.ERROR:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    mFragment.get().swipeRefrshLayout.setRefreshing(false);
                    if(mFragment.get().listOuterRecord ==null||mFragment.get().listOuterRecord.size()==0) {
                        mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                    }else{
                        mFragment.get().requestErrorHelper.showNetWorkErrorToast(msg);
                    }
                    break;
                case MyAccountDao.REQUEST_ORDERLIST:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.cancel();
                    }
                    mFragment.get().swipeRefrshLayout.setRefreshing(false);
                    mFragment.get().recyclerView.stopLoadMore();
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        final MyAccountOrderListEntityResult orderlistEntityResult = (MyAccountOrderListEntityResult) msg.obj;
                        if (orderlistEntityResult != null && orderlistEntityResult.getResults() != null && orderlistEntityResult.getResults().size() > 0) {
                            mFragment.get().recyclerView.setVisibility(View.VISIBLE);
                            if (mFragment.get().pageIndex == 1) {
                                mFragment.get().listOuterRecord.clear();
                                String userId = "";
                                if (WhiteLabelApplication.getAppConfiguration().isSignIn(mActivity.get())) {
                                    userId = WhiteLabelApplication.getAppConfiguration().getUser().getId();
                                }
                                mFragment.get().mMyAccountDao.saveLocalOrderData(mActivity.get(), userId, orderlistEntityResult.getResults());
                            }
                            mFragment.get().vsEmpty.setVisibility(View.GONE);
//                            mFragment.get().initWithWebServiceDatas(orderlistEntityResult.getResults());
                            mFragment.get().listOuterRecord.addAll(mFragment.get().initArray(orderlistEntityResult.getResults()));
                            if (mFragment.get().listOuterRecord.size() < 10) {
                                mFragment.get().recyclerView.setPullLoadEnable(false);
                            } else {
                                mFragment.get().recyclerView.setPullLoadEnable(true);
                            }
                            mFragment.get().mOrderListRecyclerViewAdapter.updateDataChange();
                            mFragment.get().mOrderListRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            if (mFragment.get().pageIndex == 1) {
                                mFragment.get().vsEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                        mFragment.get().pageIndex++;
//                        try {
//                            boolean showGuide = JStorageUtils.showAppGuide4(mActivity.get());
//                            if (showGuide && mFragment.get().mCommonCallback != null) {
//                                mFragment.get().mCommonCallback.showUserGuide(UserGuideType.MYACCOUNTEDIT);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {


//                            mFragment.get().myAccountUserGuide.ShowMyAccountUserGuide(HomeMyAccountFragmentV2.TAG_ORDERLIST);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        String errorMsg = (String) msg.obj;
                        if (!JToolUtils.expireHandler(mActivity.get(), errorMsg, 2000)) {
                            Toast.makeText(mActivity.get(), errorMsg + "", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ShoppingCarDao.REQUEST_RECOVERORDER :
//                    ErrorMsgBean msgBean = (ErrorMsgBean) ;
                    mFragment.get().startShoppingCart((String) msg.obj);
                    break;
                case ShoppingCarDao.REQUEST_REORDER_PRODUCT:
                    mFragment.get().swipeRefrshLayout.setRefreshing(false);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void startShoppingCart(String errorMessage){
        if(getActivity()!=null) {
            swipeRefrshLayout.setRefreshing(false);
            Intent intent = new Intent(homeActivity, ShoppingCartActivity1.class);
            intent.putExtra(ORDER_ERROR_MESSAGE,errorMessage);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_transition_enter_lefttoright,
                    R.anim.activity_transition_exit_lefttoright);
        }
    }

    /**
     * send request to server
     */
    private void sendRequest() {
        mMyAccountDao.getOrderList(WhiteLabelApplication.getAppConfiguration().getUserInfo(homeActivity).getSessionKey(), pageIndex + "", pageSize + "");
    }
    private ArrayList<MyAccountOrderOuter> initArray(List<MyAccountOrderOuter> array) {
        ArrayList<MyAccountOrderOuter> arrayList = new ArrayList<>();
        arrayList.addAll(array);
        return arrayList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int REQUESTCODE_LOGIN = 1000;
        if (REQUESTCODE_LOGIN == requestCode) {
            if (WhiteLabelApplication.getAppConfiguration().isSignIn(homeActivity)) {
                showSwipeRefreshDialog();
                sendRequest();
            }
        }
    }
    @Override
    public void refresh(boolean bool) {
        if (getActivity() != null && bool) {
            init();
            initData();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity, true);
        GaTrackHelper.getInstance().googleAnalytics("My Orders Screen", homeActivity);
        JLogUtils.i("googleGA_screen", "My Orders Screen ");
    }
    @Override
    public void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(homeActivity, false);
    }
}
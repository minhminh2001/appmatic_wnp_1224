package com.whitelabel.app.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.MyAccountOrderDetailActivity;
import com.whitelabel.app.adapter.OrderListRecyclerViewAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.bean.OrderBody;
import com.whitelabel.app.bean.OrderTip;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.MyAccountOrderListEntityResult;
import com.whitelabel.app.model.MyAccountOrderOuter;
import com.whitelabel.app.network.ImageLoader;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.RefreshLoadMoreRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/28.
 */
public class HomeMyAccountOrdersFragment extends HomeBaseFragment implements View.OnClickListener, MyAccountFragmentRefresh, SwipeRefreshLayout.OnRefreshListener {
    private Activity homeActivity;
    private Handler mHandler = new Handler();
    private static final int pageSize = 10;
    private int pageIndex = 1;
    private ArrayList<MyAccountOrderOuter> list_outer_record;
    private final int REQUESTCODE_LOGIN = 1000;
    private Dialog mDialog;
    private RequestErrorHelper requestErrorHelper;
    private View connectionBreaks;
    private ViewStub vsEmpty;
    private LinearLayout tryAgain;
    private MyAccountOrderListEntityResult result;
    private RefreshLoadMoreRecyclerView recyclerView;
    private OrderListRecyclerViewAdapter mOrderListRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefrshLayout;
    private int first = 0;
    private MyAccountDao mMyAccountDao;
    private String TAG;
    private String networkerror = "";
    private ImageLoader mImageLoader;
    @Override
    public void onRefresh() {
        pageIndex = 1;
        sendRequest();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            homeActivity = activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again:
                connectionBreaks.setVisibility(View.GONE);
                showSwipeRefreshDialog();
                sendRequest();
                break;
        }
    }

    public void showSwipeRefreshDialog() {
        dataHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefrshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myaccount_order_list_new, null);
        mImageLoader = new ImageLoader(homeActivity);
        recyclerView = (RefreshLoadMoreRecyclerView) view.findViewById(R.id.list);
        vsEmpty = (ViewStub) view.findViewById(R.id.tv_myaccount_orderlist_empty_new);
        vsEmpty.setLayoutResource(R.layout.fragment_order_empty);
        connectionBreaks=view.findViewById(R.id.connectionBreaks);
        requestErrorHelper=new RequestErrorHelper(getContext(),connectionBreaks);
        swipeRefrshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        tryAgain= (LinearLayout) view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(this);
        swipeRefrshLayout.setOnRefreshListener(this);
//        swipeRefrshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefrshLayout.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCommonCallback != null) {
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_ORDER);
        }
        dataHandler = new DataHandler(homeActivity, this);
        TAG = this.getClass().getSimpleName();
        mMyAccountDao = new MyAccountDao(TAG, dataHandler);
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

    public void initRecyclerView() {
        recyclerView.setPullLoadEnable(false);
        mOrderListRecyclerViewAdapter = new OrderListRecyclerViewAdapter(homeActivity, list_outer_record, true, mImageLoader);
        JLogUtils.d("list_size", list_outer_record.size() + "");
        recyclerView.setAdapter(mOrderListRecyclerViewAdapter);
        mOrderListRecyclerViewAdapter.notifyItemInserted(list_outer_record.size() - 1);
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
            public void onItemClick(View view, int position) {
                MyAccountOrderOuter orderOuter_param = null;
                final ArrayList arrayList = mOrderListRecyclerViewAdapter.getDataList(list_outer_record);
                for (MyAccountOrderOuter orderOuter : list_outer_record) {
                    if (arrayList.get(position) instanceof OrderTip) {
                        if (((OrderTip) arrayList.get(position)).getOrderNumber().equals(orderOuter.getOrderSn())) {
                            orderOuter_param = orderOuter;
                            break;
                        }
                    } else {
                        if (((OrderBody) arrayList.get(position)).getOrderNumber().equals(orderOuter.getOrderSn())) {
                            orderOuter_param = orderOuter;
                            break;
                        }
                    }

                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderOuter", orderOuter_param);
                Intent intent0 = new Intent(homeActivity, MyAccountOrderDetailActivity.class);
                intent0.putExtras(bundle);
                homeActivity.startActivity(intent0);
                homeActivity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
            }
        });
    }

    public void initData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
//                    mDialog = JViewUtils.showProgressDialog(homeActivity);
                    showSwipeRefreshDialog();
                    pageIndex = 1;
                    sendRequest();
                }
            }
        });
    }


    private void init() {
        recyclerView.setVisibility(View.GONE);
        if (list_outer_record == null) {
            list_outer_record = new ArrayList<MyAccountOrderOuter>();
        } else {
            list_outer_record.clear();
        }
        networkerror = getResources().getString(R.string.Global_Error_Internet);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static final class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeMyAccountOrdersFragment> mFragment;

        public DataHandler(Activity activity, HomeMyAccountOrdersFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<HomeMyAccountOrdersFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case MyAccountDao.LOCAL_ORDER_GET:
                    List<MyAccountOrderOuter> results = (List<MyAccountOrderOuter>) msg.obj;
                    if (mFragment.get().list_outer_record != null && mFragment.get().list_outer_record.size() > 0) {
                        return;
                    }
                    if (results != null && results.size() > 0) {

                        mFragment.get().list_outer_record.addAll(mFragment.get().initArray(results));
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
                    if(mFragment.get().list_outer_record==null||mFragment.get().list_outer_record.size()==0) {
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
                                mFragment.get().list_outer_record.clear();
                                String userId = "";
                                if (WhiteLabelApplication.getAppConfiguration().isSignIn(mActivity.get())) {
                                    userId = WhiteLabelApplication.getAppConfiguration().getUser().getId();
                                }
                                mFragment.get().mMyAccountDao.saveLocalOrderData(mActivity.get(), userId, orderlistEntityResult.getResults());
                            }
                            mFragment.get().vsEmpty.setVisibility(View.GONE);
//                            mFragment.get().initWithWebServiceDatas(orderlistEntityResult.getResults());
                            mFragment.get().list_outer_record.addAll(mFragment.get().initArray(orderlistEntityResult.getResults()));
                            if (mFragment.get().list_outer_record.size() < 10) {
                                mFragment.get().recyclerView.setPullLoadEnable(false);
                            } else {
                                mFragment.get().recyclerView.setPullLoadEnable(true);
                            }
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
            }
            super.handleMessage(msg);
        }
    }

    private DataHandler dataHandler;

    /**
     * send request to server
     */
    private void sendRequest() {
        mMyAccountDao.getOrderList(WhiteLabelApplication.getAppConfiguration().getUserInfo(homeActivity).getSessionKey(), pageIndex + "", pageSize + "");

    }


    public ArrayList<MyAccountOrderOuter> initArray(List<MyAccountOrderOuter> array) {
        ArrayList<MyAccountOrderOuter> arrayList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            arrayList.add(array.get(i));
        }
        return arrayList;
    }

    //    private  OnMyAccountUserGuide myAccountUserGuide;
//    private void onAccountFragment(Fragment fragment){
//         myAccountUserGuide= (OnMyAccountUserGuide) fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        onAccountFragment(getParentFragment());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
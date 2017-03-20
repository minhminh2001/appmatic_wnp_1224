package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.CreditInstructionActivity;
import com.whitelabel.app.adapter.StoreCreditAdapter;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.OtherDao;
import com.whitelabel.app.model.StoreCreditBean;
import com.whitelabel.app.model.StoreCreditItemBean;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomSwipefreshLayout;
import com.whitelabel.app.widget.CustomXListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link HomeStoreCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeStoreCreditFragment extends HomeBaseFragment implements View.OnClickListener
        ,SwipeRefreshLayout.OnRefreshListener,MyAccountFragmentRefresh {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String TAG;
    private OtherDao mOtherDao;
    private DataHandler mHandler;
    private StoreCreditBean mBean;
    private CustomXListView xListView;
    //    private Dialog mDialog;
    private CustomSwipefreshLayout swipeLayout;
    //    private View ivMenu;
    private View headView;
    private List<StoreCreditItemBean> history;
    private View llNotData;
    private String currTag = "HomeStoreCreditFragment";

    private  StoreCreditAdapter mAdapter;
//    private OnMyAccountUserGuide MyAccountUserGuide;

    public HomeStoreCreditFragment() {
        // Required empty public constructor
    }

    @Override
    public void refresh(boolean isRefresh) {

    }


//    private void onAttachFragment(Fragment fragment){
//        //强转接口
//        MyAccountUserGuide= (OnMyAccountUserGuide) fragment;
//    }


    @Override
    public void onRefresh() {
        String sessionKey="";
        if(GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
            sessionKey=GemfiveApplication.getAppConfiguration().getUser().getSessionKey();
        }
        mOtherDao.getStoreCredit(sessionKey);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeStoreCreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rl_help:
                Intent intent = new Intent(getActivity(), CreditInstructionActivity.class);
                intent.putExtra("data", mBean.getCMScontent());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_righttoleft,
                        R.anim.exit_righttoleft);
                break;
            case R.id.try_again:
                mConnectionLayout.setVisibility(View.GONE);
                showRefreshLayout();
                sendRequest();
                break;
        }
    }

    public static HomeStoreCreditFragment newInstance() {
        HomeStoreCreditFragment fragment = new HomeStoreCreditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.onAttachFragment(getParentFragment());
        if (getArguments() != null) {

        }
//        this.onAttachFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_store_credit, container, false);
//        ivMenu=view.findViewById(R.id.vHeaderBarMenu);
        swipeLayout = (CustomSwipefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.purple660070);
        xListView = (CustomXListView) view.findViewById(R.id.cxlvProductList);
        swipeLayout.setOnRefreshListener(this);
        llNotData = view.findViewById(R.id.ll_not_data);
        mTryAgain = (LinearLayout) view.findViewById(R.id.try_again);
        mTryAgain.setOnClickListener(this);
        mConnectionLayout = view.findViewById(R.id.connectionBreaks);
        requestErrorHelper=new RequestErrorHelper(getContext(),mConnectionLayout);
        return view;
    }
    LinearLayout mTryAgain;
    View mConnectionLayout;
    private RequestErrorHelper requestErrorHelper;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headView=null;
        if(mCommonCallback!=null){
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_STORECREDITS);
        }
        initListView();
        initData();
        setHasOptionsMenu(true);
    }


    private void initListView() {
        history = new ArrayList<>();
        xListView.setPullRefreshEnable(false);
        xListView.setPullLoadEnable(false);
        xListView.setHeaderDividersEnabled(false);
        xListView.setFooterDividersEnabled(false);


    }

    final static class DataHandler extends Handler {
        private WeakReference<HomeStoreCreditFragment> mFragment;
        private WeakReference<Activity> mActivity;

        public DataHandler(Activity activity, HomeStoreCreditFragment fragment) {
            mFragment = new WeakReference<HomeStoreCreditFragment>(fragment);
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mFragment.get() == null || mActivity.get() == null) {
                return;
            }
            mFragment.get().swipeLayout.setRefreshing(false);
            switch (msg.what) {
                case OtherDao.REQUEST_STORECREDIT:
                    if(msg.arg1==OtherDao.RESPONSE_SUCCESS){
					 mFragment.get().mConnectionLayout.setVisibility(View.GONE);
                        StoreCreditBean bean= (StoreCreditBean) msg.obj;
                        mFragment.get().initStoreCredit(bean);
                    } else {
                        String errorMsg = (String) msg.obj;
                        Toast.makeText(mActivity.get(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                    break;
                case OtherDao.REQUEST_ERROR:
                    mFragment.get().swipeLayout.setRefreshing(false);
                    if (msg.arg1 == OtherDao.REQUEST_STORECREDIT) {
                        if (mFragment.get().mBean == null) {
                            mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                            return;
                        }
                    }
                    mFragment.get().requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
        }
    }

    private void initData() {
        TAG = this.getClass().getSimpleName();
        mHandler = new DataHandler(getActivity(), this);
        mOtherDao = new OtherDao(TAG, mHandler);
        showRefreshLayout();
        sendRequest();
    }

    private void sendRequest() {
        if (getActivity() != null) {
            String sessionKey="";
            if(GemfiveApplication.getAppConfiguration().isSignIn(getActivity())) {
                sessionKey=GemfiveApplication.getAppConfiguration().getUser().getSessionKey();
            }
            mOtherDao.getStoreCredit(sessionKey);
        }
    }

    private void showRefreshLayout() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
    }

    public void initStoreCredit(StoreCreditBean bean) {
        mBean = bean;

        if (getActivity() != null) {
            if (!"0".equals(bean.getIsEnabled())) {
                xListView.setVisibility(View.VISIBLE);
                llNotData.setVisibility(View.GONE);
                if (headView != null) {
                    xListView.removeHeaderView(headView);
                }
                headView = LayoutInflater.from(getActivity()).inflate(R.layout.header_storecredit, null);
                TextView tvTitle = (TextView) headView.findViewById(R.id.title);
                TextView tvValue = (TextView) headView.findViewById(R.id.tv_value);
                TextView tvNotCredit = (TextView) headView.findViewById(R.id.tv_not_credit_hint);
                View vRecentTransaction = headView.findViewById(R.id.recentTransaction);
                View vBottomLine = headView.findViewById(R.id.v_bottom_line);
                tvTitle.setText(JDataUtils.formatThousand(bean.getAmount()) + "");
                tvValue.setText("RM " + JDataUtils.formatDouble(bean.getAmountToRM()) + " equivalent");
                headView.findViewById(R.id.rl_help).setOnClickListener(this);
                TextView tvCms = (TextView) headView.findViewById(R.id.ctvcms);
                if (!TextUtils.isEmpty(bean.getCMScontentTitle())) {
                    tvCms.setVisibility(View.VISIBLE);
                    String str = "<div> Why pay more, when you can pay less with GEMCredits?</div>";
                    CharSequence trimmed = noTrailingwhiteLines(Html.fromHtml(str));
                    tvCms.setText(trimmed);
                }
                xListView.setAdapter(null);
                xListView.addHeaderView(headView);
                xListView.setVisibility(View.VISIBLE);
                if (bean.getHistory() != null && bean.getHistory().size() > 0) {
                    mAdapter = new StoreCreditAdapter(getActivity(), bean.getHistory());
                    xListView.setAdapter(mAdapter);
                    tvNotCredit.setVisibility(View.GONE);
                } else {
                    tvNotCredit.setVisibility(View.VISIBLE);
                }
            } else {
                xListView.setVisibility(View.GONE);
                llNotData.setVisibility(View.VISIBLE);
            }
        }
    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }
        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }
        return s.subSequence(start, end);
    }

    @Override
    public void onStart() {
        super.onStart();
//        try {
//            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), true);
//            GaTrackHelper.getInstance().googleAnalytics("My Store Credit Screen", getContext());
//        } catch (Exception ex) {
//            ex.getStackTrace();
//        }
//        JLogUtils.i("googleGA_screen", "Store Credit Screen");
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (getActivity() != null) {
//            GaTrackHelper.getInstance().googleAnalyticsReportActivity(getActivity(), false);
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}

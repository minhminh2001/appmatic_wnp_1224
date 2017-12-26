package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.adapter.CustomerCareAdapter;
import com.whitelabel.app.dao.HelpCenterDao;
import com.whitelabel.app.model.SVRAppserviceCmsCmsPageReturnEntity;
import com.whitelabel.app.model.TMPHelpCenterListToDetailEntity;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.logger.Logger;
import com.whitelabel.app.widget.CustomTextView;
import com.whitelabel.app.widget.CustomWebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by imaginato on 2015/7/27.
 */
public class HomeHelpCenterDetailFragment extends HomeBaseFragment implements View.OnClickListener {
    private final String TAG = "HomeHelpCenterDetailFragment";
    private Activity homeActivity;
    private View contentView;
    private CustomWebView cwvDetail;
    public static final int FIREST_MENU = 2;
    public static final int SECOND_MENU = 0;
    private int contentType = -1;
    private DataHandler mHandler;
    private TMPHelpCenterListToDetailEntity listToDetailEntity;
    private HelpCenterDao dao;
    private RecyclerView rcyCustumers;
    private String titleKeyWord;
    private Dialog mDialog;
    String mTitles = "";

    private List<String> customerCares= Arrays.asList("Privacy Policy","Terms & Conditions","Returns & Exchanges","Delivery","Payment Option","Customer Service");
    private List<String> customerParams= Arrays.asList("privacy_policy","terms","return_exchange","delivery","payment","contact_us");

    private static final class DataHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final WeakReference<HomeHelpCenterDetailFragment> mFragment;

        public DataHandler(Activity activity, HomeHelpCenterDetailFragment fragment) {
            mActivity = new WeakReference<Activity>(activity);
            mFragment = new WeakReference<HomeHelpCenterDetailFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            switch (msg.what) {
                case HelpCenterDao.REQUEST_GETDATA:
                    if (msg.arg1 == HelpCenterDao.RESPONSE_SUCCESS) {
                        SVRAppserviceCmsCmsPageReturnEntity returnEntity = (SVRAppserviceCmsCmsPageReturnEntity) msg.obj;
                        String content=JToolUtils.replaceFont(returnEntity.getContent());
                        JLogUtils.d("jay","content="+content);

                        mFragment.get().cwvDetail.setText(content);

                        if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                            mFragment.get().mDialog.dismiss();
                        }
                        mFragment.get().connectionLayout.setVisibility(View.GONE);
                    } else {
                        String msgs = String.valueOf(msg.obj);
                        Toast.makeText(mActivity.get(), msgs, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HelpCenterDao.REQUEST_ERROR:
                    if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                        mFragment.get().mDialog.dismiss();
                    }
                    mFragment.get().requestErrorHelper.showConnectionBreaks(msg);
                    break;
                case HelpCenterDao.REQUEST_GET_CUSTOMER_CARE:
                    if (msg.arg1 == HelpCenterDao.RESPONSE_SUCCESS) {
                        SVRAppserviceCmsCmsPageReturnEntity returnEntity = (SVRAppserviceCmsCmsPageReturnEntity) msg.obj;
                        String content=JToolUtils.replaceFont(returnEntity.getContent());
                        mFragment.get().rcyCustumers.setVisibility(View.GONE);
                        mFragment.get().connectionLayout.setVisibility(View.GONE);
//                        mFragment.get().tvHtmlMsg.setText(Html.fromHtml(content));
                        mFragment.get().cwvDetail.setVisibility(View.VISIBLE);
                        mFragment.get().cwvDetail.setText(content);
                        if (mFragment.get().mDialog != null && mFragment.get().mDialog.isShowing()) {
                            mFragment.get().mDialog.dismiss();
                        }
                        mFragment.get().mCommonCallback.setTitle(mFragment.get().titleKeyWord);
                        mFragment.get().listToDetailEntity.setType(SECOND_MENU);
                        mFragment.get().initToolBar();

                    } else {
                        String msgs = String.valueOf(msg.obj);
                        Toast.makeText(mActivity.get(), msgs, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    }

    @Override
    public void onDestroy() {
        dao.cancelHttpByTag(TAG);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        homeActivity = (Activity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home_helpcenterdetail, null);
        contentView.findViewById(R.id.ll_toolbar).setVisibility(View.GONE);
        return contentView;
    }

    private View connectionLayout;
    private RequestErrorHelper requestErrorHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new DataHandler(getActivity(), this);
        connectionLayout=contentView.findViewById(R.id.connectionBreaks);
        rcyCustumers= (RecyclerView) contentView.findViewById(R.id.rcy_customer_care);
        requestErrorHelper=new RequestErrorHelper(getContext(),connectionLayout);
        LinearLayout tryAgain = (LinearLayout) contentView.findViewById(R.id.try_again);
        mDialog = JViewUtils.showProgressDialog(homeActivity);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionLayout.setVisibility(View.GONE);
                initViewTitle();
                initCustomerCare();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                listToDetailEntity = (TMPHelpCenterListToDetailEntity) bundle.getSerializable("data");
                if (listToDetailEntity != null) {
                    contentType = listToDetailEntity.getHelpCenterType();
                }
            } catch (Exception ex) {
                JLogUtils.e(TAG, "onActivityCreated", ex);
            }
        }

        cwvDetail = (CustomWebView) contentView.findViewById(R.id.cwvDetail);
        dao = new HelpCenterDao(TAG, mHandler);
        initToolBar();
        initViewTitle();
        initCustomerCare();

    }


    public void refresh(TMPHelpCenterListToDetailEntity entity) {
        if (isAdded()) {
            listToDetailEntity = entity;
            contentType = listToDetailEntity.getHelpCenterType();
            cwvDetail.setText("");
            initToolBar();
            initViewTitle();
            initCustomerCare();
        }
    }

    private void initToolBar() {
        if (listToDetailEntity.getType() == FIREST_MENU) {
            mCommonCallback.setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_menu));
        } else {
            mCommonCallback.setLeftMenuIcon(JToolUtils.getDrawable(R.drawable.action_back));
        }
        mCommonCallback.setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listToDetailEntity.getType() == SECOND_MENU){
                    mCommonCallback.resetMenuAndListenter();
                    listToDetailEntity.setType(FIREST_MENU);
                    refresh(listToDetailEntity);
                }else {
                    if (homeActivity instanceof HomeActivity){
                        ((HomeActivity)homeActivity).getDrawerLayout().openDrawer(Gravity.LEFT);
                        GaTrackHelper.getInstance().googleAnalytics(Const.GA.SLIDE_MENU_SCREEN,getActivity());
                    }
                }
            }
        });
        if (!(homeActivity instanceof HomeActivity)) {
            homeActivity.finish();
        }
    }

    private void initViewTitle() {
        int switchMenu = -1;
        if (0 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_aboutus2);
        } else if (1 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_privacypolicy2);
        } else if (2 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_termsofus2);
        } else if (3 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_howtobuy2);
        } else if (4 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_payments2);
        } else if (5 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_shippingdelivery2);
            switchMenu = HomeCommonCallback.MENU_SHIPPING;
        } else if (6 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_ordertracking2);
        } else if (7 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_cancellationsreturns2);
        } else if (8 == contentType) {
            mTitles = getString(R.string.home_helpcenter_list_gemcashvoucher2);
        } else if (9 == contentType) {
            switchMenu = HomeCommonCallback.MENU_COSTOMSERVICE;
            mTitles = getString(R.string.home_helpcenter_list_customerservice2);
        } else if (10 == contentType) {
            mTitles = getString(R.string.statement_of_ipr2);
        }
        if (mCommonCallback != null) {
            mCommonCallback.setTitle(mTitles);
            mCommonCallback.switchMenu(switchMenu);
            mCommonCallback.getToolBar().getMenu().clear();
        }
    }

    private void initCustomerCare(){
        if (mDialog!=null){
            mDialog.dismiss();
        }
        cwvDetail.setVisibility(View.GONE);
        CustomerCareAdapter adapter=new CustomerCareAdapter(customerCares);
        rcyCustumers.setAdapter(adapter);
        rcyCustumers.setVisibility(View.VISIBLE);
        rcyCustumers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mDialog!=null){
                    mDialog.show();
                }
                dao.loadCustomerCare(customerParams.get(position));
                titleKeyWord=customerCares.get(position);
            }
        });
    }

    public boolean onBackPressed() {
        return listToDetailEntity.getType() == SECOND_MENU;
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initViewContent() {
        mDialog = JViewUtils.showProgressDialog(homeActivity);
        String content = null;
        if (0 == contentType) {
            content = "about-us-mobile";
        } else if (1 == contentType) {
            content = "privacy-policy-mobile";
        } else if (2 == contentType) {
            content = "terms-of-use-mobile";
        } else if (3 == contentType) {
            content = "how-to-buy-mobile";
        } else if (4 == contentType) {
            content = "payments-mobile";
        } else if (5 == contentType) {
            content = "shipping-delivery-mobile";
        } else if (6 == contentType) {
            content = "order-tracking-mobile";
        } else if (7 == contentType) {
            content = "cancellation-returns-mobile";
        } else if (8 == contentType) {
            content = "store-credit-gemcash-voucher-mobile";
        } else if (9 == contentType) {
            content = "customer-service-mobile";
        } else if (10 == contentType) {
            content = "statement-of-ipr-mobile";
        }
        dao.loadDataFromServer(content);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}

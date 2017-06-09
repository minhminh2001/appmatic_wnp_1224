package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.activity.EditAddressActivity;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.adapter.AddressBookAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddressDeleteCellEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenu;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuCreator;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuItem;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/28.
 */
public class HomeMyAccountAddressBookFragment extends HomeBaseFragment implements View.OnClickListener, com.whitelabel.app.fragment.MyAccountFragmentRefresh, SwipeRefreshLayout.OnRefreshListener {
    private View contentView;
    //    private ListView lv;
    private TextView textView_add;
    private AddressBookAdapter adapter;
    private HomeActivity addressBookActivity;
    private Handler mHandler = new Handler();
    private String number;
    private int mSelectShippingIndex = -1;
    private int mSelectBillingIndex=-1;
    private SwipeMenuListView mListView;
    public ArrayList<AddressBook> mBeans = new ArrayList<AddressBook>();
    private Dialog mDialog;
    private MyAccountDao dao;
    private String TAG;
    private SwipeRefreshLayout refreshLayout;
    private RequestErrorHelper requestErrorHelper;
    private View connectionLayout;
    private final static class DataHandler extends Handler {
        private final WeakReference<HomeActivity> mActivity;
        private final WeakReference<HomeMyAccountAddressBookFragment> mFragment;
        public DataHandler(HomeActivity activity, HomeMyAccountAddressBookFragment fragment) {
            mActivity = new WeakReference<HomeActivity>(activity);
            mFragment = new WeakReference<HomeMyAccountAddressBookFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null) {
                return;
            }
            HomeActivity activity = mActivity.get();
            final HomeMyAccountAddressBookFragment fragment = mFragment.get();

            switch (msg.what) {
                case MyAccountDao.REQUEST_DELETEADDRESS:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        final AddressDeleteCellEntity addressDeleteCell = (AddressDeleteCellEntity) msg.obj;
                        if (addressDeleteCell.getStatus() == 1) {
                            fragment.sendRequest();
                        }
                    } else {
                        final String SESSION_EXPIRED = "session expired,login again please";
                        if (!JDataUtils.errorMsgHandler(activity, msg.obj.toString())) {
                            if (!activity.isFinishing()) {
                                if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains(SESSION_EXPIRED))) {
                                    Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent();
                                    intent.setClass(activity, LoginRegisterActivity.class);
                                    fragment.startActivityForResult(intent, 1000);
                                    return;
                                }
                            }
                            if (activity != null) {
                                if (msg.obj.toString().indexOf("not belong to this customer") < 0) {
                                    Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show();
                                }
                                fragment.sendRequest();
                            }
                        }
                    }
                    break;
                case MyAccountDao.LOCAL_ADDRESS_GET:
                    fragment.connectionLayout.setVisibility(View.GONE);
                    fragment.textView_add.setVisibility(View.VISIBLE);
                    List<AddressBook> address = (List<AddressBook>) msg.obj;
                    if (address != null && address.size() > 0) {
                        fragment.initWithWebServiceDatas(address);
                    }
                    fragment.sendRequest();
                    break;
                case MyAccountDao.REQUEST_GETADDRESLIST:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    fragment.refreshLayout.setRefreshing(false);
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        fragment.connectionLayout.setVisibility(View.GONE);
                        fragment.textView_add.setVisibility(View.VISIBLE);
                        if (activity != null && !activity.isFinishing() && fragment.isAdded()) {
                            final AddresslistReslut addresslistReslut = (AddresslistReslut) msg.obj;
                            if (null != addresslistReslut.getAddress()) {
                                fragment.dao.saveLocalAddressData(activity, WhiteLabelApplication.getAppConfiguration().getUser().getId(), addresslistReslut.getAddress());
                                fragment.initWithWebServiceDatas(addresslistReslut.getAddress());
                            }
                        }
                    } else {
                        String msgStr = String.valueOf(msg.obj);
                        Toast.makeText(activity, msgStr, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MyAccountDao.ERROR:
                    if (fragment.mDialog != null) {
                        fragment.mDialog.cancel();
                    }
                    fragment.refreshLayout.setRefreshing(false);
                    if (msg.arg1 == MyAccountDao.REQUEST_GETADDRESLIST) {
                        if (fragment.mBeans == null || fragment.mBeans.size() == 0) {
                            fragment.textView_add.setVisibility(View.INVISIBLE);
                            fragment.requestErrorHelper.showConnectionBreaks(msg);
                            return;
                        }
                    }
                    fragment.requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addressBookActivity = (HomeActivity) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_addressbook, null);
        setRetryTheme(contentView);
        return contentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCommonCallback != null) {
            mCommonCallback.switchMenu(HomeCommonCallback.MENU_ADDRESS);
        }
        TAG = addressBookActivity.getClass().getSimpleName();
        DataHandler dataHandler = new DataHandler(addressBookActivity, this);
        dao = new MyAccountDao(TAG, dataHandler);
//        mBar=(ProgressBar)contentView.findViewById(R.id.pb_shoppingcart);
        connectionLayout = contentView.findViewById(R.id.connectionBreaks);
        requestErrorHelper = new RequestErrorHelper(getContext(), connectionLayout);
        LinearLayout tryAgain = (LinearLayout) contentView.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(this);
        refreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container);
        mListView = (SwipeMenuListView) contentView.findViewById(R.id.mListView);
        textView_add = (TextView) contentView.findViewById(R.id.addressbook_add_textview);
        textView_add.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setColorSchemeColors(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        dao.getLocalAddressData(getActivity(), WhiteLabelApplication.getAppConfiguration().getUser().getId());
        showRefreshLayout();
        setSwipeListView();
        setHasOptionsMenu(true);
    }
    @Override
    public void onRefresh() {
        sendRequest();
    }
    private SwipeMenu menu;
    private int mMenuWidth = 50;
    public void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu, int position) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(getResources().getDrawable(R.color.white));
                // set item width
                openItem.setWidth(JToolUtils.dip2px(getActivity(), mMenuWidth));
                openItem.setIcon(getResources().getDrawable(R.drawable.draw_edit));
                // add to menu
                menu.addMenuItem(openItem);
                if (position != mSelectBillingIndex&&position!=mSelectShippingIndex) {
                    menu.addMenuItem(createDeleteSwipeItem());
                }
//                AdderssBookFragment.this.menu=menu;
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AddressBook addressBook = mBeans.get(position);
                switch (index) {
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean", addressBook);
                        Intent intent = new Intent(getActivity(), EditAddressActivity.class);
                        intent.putExtras(bundle);
                        getParentFragment().startActivityForResult(intent, 2000);
                        getActivity().overridePendingTransition(R.anim.enter_righttoleft,
                                R.anim.exit_righttoleft);
                        break;
                    case 1:
                        sendRequestToDeteleteCell(addressBook.getAddressId());
                        break;
                }
                return false;
            }
        });
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                refreshLayout.setEnabled(false);
            }

            @Override
            public void onSwipeEnd(int position) {
                refreshLayout.setEnabled(true);
            }
        });
        adapter = new AddressBookAdapter(addressBookActivity, mBeans);
    }
    public final SwipeMenuItem createDeleteSwipeItem() {
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity());
        // set item background
        deleteItem.setBackground(getResources().getDrawable(R.color.white));
        // set item width
        deleteItem.setWidth(JToolUtils.dip2px(getActivity(), mMenuWidth));
        // set a icon
        deleteItem.setIcon(getResources().getDrawable(R.drawable.draw_dele));
        // add to menu
        return deleteItem;
    }
    private void onAccountFragment(Fragment fragment) {
        HomeMyAccountFragmentV2 myAccountUserGuide = (HomeMyAccountFragmentV2) fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAccountFragment(getParentFragment());
    }
    private void initWithWebServiceDatas(List<AddressBook> address) {
        if (mBeans != null) {
            mBeans.clear();
        }
        for (int i = 0; i < address.size(); i++) {
            AddressBook addressBook = address.get(i);
            if("1".equals(addressBook.getPrimaryShipping())&&"1".equals(addressBook.getPrimaryBilling())) {
                AddressBook cloneObject= (AddressBook) addressBook.clone();
                addressBook.setPrimaryBilling("0");
                mBeans.add(0,addressBook);
                cloneObject.setPrimaryShipping("0");
                mBeans.add(0,cloneObject);
                mSelectShippingIndex=1;
                mSelectBillingIndex=0;
            }else if("1".equals(addressBook.getPrimaryBilling())){
                mBeans.add(0,addressBook);
                mSelectBillingIndex=i;
            }else if("1".equals(addressBook.getPrimaryShipping())){
                mBeans.add(0,addressBook);
                mSelectShippingIndex=i;
            } else{
                mBeans.add(addressBook);
            }
        }
        mListView.setAdapter(adapter);
    }
    private void sendRequestToDeteleteCell(String itemId) {
        mDialog = JViewUtils.showProgressDialog(getActivity());
        dao.deleteAddress(WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey(), itemId);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void refresh(boolean refresh) {
        if (addressBookActivity != null && !addressBookActivity.isFinishing() && isAdded() && refresh) {
            showRefreshLayout();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sendRequest();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addressbook_add_textview:
                Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                intent.putExtra("listnum", number);
                getParentFragment().startActivityForResult(intent, 2000);
                getActivity().overridePendingTransition(
                        R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            case R.id.try_again:
                connectionLayout.setVisibility(View.GONE);
                showRefreshLayout();
                sendRequest();
                break;
        }
    }
    public void showRefreshLayout() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }
    /**
     * send request to server
     */
    private void sendRequest() {
        if (getActivity() != null) {
            dao.getAddresslist(WhiteLabelApplication.getAppConfiguration().getUserInfo(addressBookActivity).getSessionKey());
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(addressBookActivity, true);
        GaTrackHelper.getInstance().googleAnalytics("My Address Book Screen", addressBookActivity);
        JLogUtils.i("googleGA_screen", "My Address Book Screen ");
    }
    @Override
    public void onStop() {
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(addressBookActivity, false);
        adapter.notifyDataSetChanged();
    }
}

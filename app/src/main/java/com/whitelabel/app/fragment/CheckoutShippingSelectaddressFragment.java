package com.whitelabel.app.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.ui.checkout.CheckoutActivity;
import com.whitelabel.app.adapter.CheckoutShippingAddressAdapter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.model.CheckoutSelectShippingAddressEntity;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenu;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuCreator;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuItem;
import com.whitelabel.app.widget.swipemenulistview.SwipeMenuListView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckoutShippingSelectaddressFragment extends Fragment implements View.OnClickListener{
    private CheckoutActivity checkoutActivity;
    public CheckoutDefaultShippingAddress  mAddress;
    private SwipeMenuListView lvAddress;//used for more than one address and inflated by Adapter
    private final  int  mMenuWidth=60;
    public Button btnAddNewAddress;
    public TextView tvErrorMsg;
    public SwipeRefreshLayout swipeLayout;
    private  LinkedList<CheckoutDefaultShippingAddress> mBeans=new LinkedList<CheckoutDefaultShippingAddress>();
    private CheckoutShippingAddressAdapter checkoutShippingAddressAdapter;
    private Dialog mDialog;
    private View mContent;
    private String TAG=this.getClass().getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            checkoutActivity = (CheckoutActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    @Override
    public void onClick(View v) {
//        int id=v.getId();
//        switch (id){
//            case R.id.btn_checkout_shipping_add_new_address :
//                btnAddNewAddress.setVisibility(View.GONE);
//                CheckoutShippingAddaddressFragment checkoutShippingAddaddressFragment = new CheckoutShippingAddaddressFragment();
//                FragmentTransaction fragmentTransaction = checkoutActivity.getFragmentManager().beginTransaction();
//                checkoutActivity.list_fragment_shipping.add(checkoutShippingAddaddressFragment);
//                checkoutActivity.list_fragment.clear();
//                checkoutActivity.list_fragment.add(checkoutShippingAddaddressFragment);
//                fragmentTransaction.add(R.id.ll_checkout_body, checkoutShippingAddaddressFragment, "addNewAddressFragment");
//                fragmentTransaction.hide(checkoutActivity.list_fragment_shipping.get(0));//hide selectAddressFragment
//                fragmentTransaction.setCustomAnimations(
//                        R.animator.fragment_slide_right_enter,
//                        R.animator.fragment_slide_left_exit
//                ).show(checkoutShippingAddaddressFragment).commit();
//                btnAddNewAddress.setVisibility(View.GONE);
//                checkoutActivity.addressConditionInShipping = "0";
//                break;
//        }
    }

    public void initView(View view){
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_checkout_errormsg_defaultOrSelectAddress);
        btnAddNewAddress = (Button) view.findViewById(R.id.btn_checkout_shipping_add_new_address);
        btnAddNewAddress.setVisibility(View.VISIBLE);
        btnAddNewAddress.setOnClickListener(this);
        mContent=view.findViewById(R.id.mContent);
        mContent.setVisibility(View.GONE);
        lvAddress = (SwipeMenuListView) view.findViewById(R.id.lv_checkout_shipping_selectaddress);
//        swipeLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
//        swipeLayout.setColorSchemeResources(R.color.purple660070);
//        swipeLayout.setOnRefreshListener(this);
    }
//    @Override
//    public void onRefresh() {
//        new MyAccountDao(TAG,mHandler).getAddressBySession(WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutActivity).getSessionKey());
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_shipping_selectaddress, container, false);
        initView(view);
        //initFooter();
        // Show "Add new address Button"

        return view;
    }
    private void initFooter(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View footerV = inflater.inflate(R.layout.shipping_selectaddresslist_footer_layout, null);
        lvAddress.addFooterView(footerV);
        tvErrorMsg = (TextView) footerV.findViewById(R.id.tv_checkout_errormsg_defaultOrSelectAddress);
        btnAddNewAddress = (Button) footerV.findViewById(R.id.btn_checkout_shipping_add_new_address);
        btnAddNewAddress.setVisibility(View.VISIBLE);
        btnAddNewAddress.setOnClickListener(this);
    }
    private void initData() {
        checkoutShippingAddressAdapter = new CheckoutShippingAddressAdapter(getActivity(),mBeans,lvAddress);
        lvAddress.setAdapter(checkoutShippingAddressAdapter);
        sendRequestToGetSelectAddress();
//        new MyAccountDao(TAG,mHandler).getAddressBySession(WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutActivity).getSessionKey());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler=new DataHandler((CheckoutActivity) getActivity(),this);
        initListView();
        initData();
        checkoutActivity. scrollViewBody.scrollTo(0, (int) (getResources().getDimension(R.dimen.scroll_height)*-100));
    }

    public void initListView(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu,int position) {
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
                // create "delete" item
//                AdderssBookFragment.this.menu=menu;

            }
        };
        // set creator
        lvAddress.setMenuCreator(creator);
        lvAddress.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
//                        CheckoutDefaultShippingAddress address = mBeans.get(position);
//                        CheckoutShippingAddaddressFragment checkoutShippingAddaddressFragment = new CheckoutShippingAddaddressFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("address", address);
//                        checkoutShippingAddaddressFragment.setArguments(bundle);
//                        FragmentTransaction fragmentTransaction = checkoutActivity.getFragmentManager().beginTransaction();
//                        fragmentTransaction.hide(checkoutActivity.list_fragment_shipping.get(0));//hide select address fragment
//                        fragmentTransaction.setCustomAnimations(
//                                R.animator.fragment_slide_right_enter,
//                                R.animator.fragment_slide_left_exit
//                        ).add(R.id.ll_checkout_body, checkoutShippingAddaddressFragment, "editAddressFragment").commit();
//                        //record fragment count of Shipping Module for Go Back Button.
//                        checkoutActivity.list_fragment_shipping.add(checkoutShippingAddaddressFragment);
//                        checkoutActivity.list_fragment.clear();
//                        checkoutActivity.list_fragment.add(checkoutShippingAddaddressFragment);
//
//                        checkoutActivity.addressConditionInShipping = "0";
                        break;


                }
                return false;
            }
        });
    }

    public void openAddFragment(){
        if(getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            btnAddNewAddress.setVisibility(View.GONE);
            CheckoutShippingAddaddressFragment checkoutShippingAddaddressFragment = new CheckoutShippingAddaddressFragment();
            FragmentTransaction fragmentTransaction = checkoutActivity.getFragmentManager().beginTransaction();
//            fragmentTransaction.remove(checkoutActivity.list_fragment.get(0));//Add New Address Fragment is the first one
//            checkoutActivity.list_fragment.clear();//Add New Address Fragment is the first one
//            checkoutActivity.list_fragment_shipping.clear();//Add New Address Fragment is the first one
//            checkoutActivity.list_fragment.add(checkoutShippingAddaddressFragment);
//            checkoutActivity.list_fragment_shipping.add(checkoutShippingAddaddressFragment);
//            fragmentTransaction.add(R.id.ll_checkout_body, checkoutShippingAddaddressFragment, "addNewAddressFragment").commitAllowingStateLoss();
            btnAddNewAddress.setVisibility(View.GONE);
//            checkoutActivity.addressConditionInShipping = "0";
        }
    }

    public static final class DataHandler extends Handler{
        private final WeakReference<CheckoutActivity> mActivity;
        private final WeakReference<CheckoutShippingSelectaddressFragment> mFragment;
        public DataHandler(CheckoutActivity activity,CheckoutShippingSelectaddressFragment fragment){
            mActivity=new WeakReference<CheckoutActivity>(activity);
            mFragment=new WeakReference<CheckoutShippingSelectaddressFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            mFragment.get().closeDialog();
            //mFragment.get().setRefreshing(false);
            switch (msg.what) {
                case MyAccountDao.REQUEST_GETADDRESS:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        if ( mFragment.get().mDialog != null) {
                            mFragment.get().mDialog.cancel();
                        }
                        mFragment.get().tvErrorMsg.setVisibility(View.GONE);
                        final CheckoutSelectShippingAddressEntity selectShippingAddress = (CheckoutSelectShippingAddressEntity) msg.obj;
                        mFragment.get().mBeans.clear();
                        if (selectShippingAddress.getAddress() == null || selectShippingAddress.getAddress().size() == 0) {
                            mFragment.get(). openAddFragment();
                        } else {
                            try {
                                mFragment.get(). oldIndex=0;
                                mFragment.get().mAddress = selectShippingAddress.getAddress().get(0);
                                mFragment.get().mBeans.addAll(selectShippingAddress.getAddress());
                                mFragment.get().initListViewDatasWithWebServiceReturn( mFragment.get().mBeans);
                            } catch (Exception ex) {
                                ex.getStackTrace();
                            }
                        }
                        if(mFragment.get().checkoutActivity.mGATrackCheckoutTimeEnable) {
                            GaTrackHelper.getInstance().googleAnalyticsTimeStop(
                                    GaTrackHelper.GA_TIME_CATEGORY_CHECKOUT,
                                    mFragment.get().checkoutActivity.mGATrackCheckoutTimeStart,
                                    "Checkout - Address Loading"
                            );
                            mFragment.get().checkoutActivity.mGATrackCheckoutTimeEnable = false;
                        }
                    } else {
                        String errorMsg = String.valueOf(msg.obj);
                        if (!JToolUtils.expireHandler(mActivity.get(),  mFragment.get(), errorMsg, 10000)) {
                            JViewUtils.showErrorToast(mActivity.get(), errorMsg);
                        }
                    }
                    break;

                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    super.handleMessage(msg);
            }
        }
    }

private DataHandler mHandler;

    public void closeDialog(){
        if (mDialog != null) {
            mDialog.cancel();
        }
    }
    /**
     * send Request To Get Select Address
     */
    public void sendRequestToGetSelectAddress() {
        mDialog= JViewUtils.showProgressDialog(checkoutActivity);
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(true);
//            }
//        });
        new MyAccountDao(TAG,mHandler).getAddressBySession(WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutActivity).getSessionKey());
    }
    private int oldIndex=-1;
    private void initListViewDatasWithWebServiceReturn(final LinkedList<CheckoutDefaultShippingAddress> list) {
        checkoutShippingAddressAdapter.notifyDataSetChanged();
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // checkoutShippingAddressAdapter.updateView(position);
                CheckoutDefaultShippingAddress newAddress = list.get(position);
                if (oldIndex == position) {
                    return;
                }
                setSelectAddress(view);
                mAddress = checkoutShippingAddressAdapter.list.get(position);
                if (oldIndex != -1) {
                    CheckoutDefaultShippingAddress oldAddress = list.get(oldIndex);
                    oldAddress.setPrimaryShipping("2");
                }
                newAddress.setPrimaryShipping("1");
                oldIndex = position;
//                checkoutShippingAddressAdapter.notifyDataSetChanged();
            }
        });
        try {
            int totalHeight = 0;
            for (int i = 0; i < checkoutShippingAddressAdapter.getCount(); i++) {
                View listItem = checkoutShippingAddressAdapter.getView(i, null, lvAddress);
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(lvAddress.getWidth(), View.MeasureSpec.AT_MOST);
//            listItem.measure(0, 0);
                listItem.measure(desiredWidth, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = lvAddress.getLayoutParams();
            params.height = totalHeight + (lvAddress.getDividerHeight() * (checkoutShippingAddressAdapter.getCount() - 1));
            //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
            lvAddress.setLayoutParams(params);
        }catch(Exception ex){
            ex.getStackTrace();
        }
        mContent.setVisibility(View.VISIBLE);
    }

    public void setSelectAddress(View currView ){
        View vSelected=currView.findViewById(R.id.AddAddress_select_button);
        View mparent=currView.findViewById(R.id.mparent);
        vSelected.setVisibility(View.VISIBLE);
        mparent.setBackgroundColor(getResources().getColor(R.color.greyC2C2C2));
        View oldView= getViewByPosition(oldIndex,lvAddress);
        View vOldSelected=oldView.findViewById(R.id.AddAddress_select_button);
        View mOldparent=oldView.findViewById(R.id.mparent);
        vOldSelected.setVisibility(View.INVISIBLE);
        mOldparent.setBackgroundColor(Color.WHITE);
    }

}

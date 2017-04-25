package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.google.gson.Gson;
import com.whitelabel.app.activity.CheckoutActivity;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.utils.JDataUtils;

public class CheckoutShippingDefaultaddressFragment extends Fragment {

    private View view;//use this view to inflate the fragment

    private PopupWindow popupWindowSelect;

    private CheckoutActivity checkoutActivity;

    private Handler mHandler = new Handler();

    private TextView tvFirstname;
    private TextView tvLastname;
    private TextView tvAddress1;
    private TextView tvAddress2;
    private TextView tvCityStatePostcode;
    private TextView tvTelephone;

    public TextView tvErrorMsg;

    /**
     * when first comes to checkoutActivity, send request to get default address, and this variable record the default address.
     */
    public CheckoutDefaultShippingAddress address;

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

        //Needn't to send request to get default address by itself ,
        //which is done by checkoutActivity and it will transfer params to this fragment.

        /*mHandler.post(new Runnable() {
            @Override
            public void run() {
                sendRequestToGetDefaultAddress();
            }
        });*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate parent view
        view = inflater.inflate(R.layout.fragment_checkout_shipping_selectaddress, container, false);

        LinearLayout llAddress = (LinearLayout) view.findViewById(R.id.ll_checkout_shipping_selectaddress);
        llAddress.setVisibility(View.VISIBLE);
        ListView lvAddress = (ListView) view.findViewById(R.id.lv_checkout_shipping_selectaddress);
        lvAddress.setVisibility(View.GONE);

        tvErrorMsg = (TextView) view.findViewById(R.id.tv_checkout_errormsg_defaultOrSelectAddress);

        //use address cell to inflate a linearlayout of parent view
        View viewChild = LayoutInflater.from(checkoutActivity).inflate(R.layout.fragment_checkout_shipping_selectaddress_cell,null);
        llAddress.addView(viewChild, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        viewChild.findViewById(R.id.image_address_select_top).setVisibility(View.GONE);
        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.image_address_select_end);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //you can choose popupwindow to show a dialog
                popupWindowSelect.showAsDropDown(imageButton,0,-18);

            }
        });
        initView();
        return view;
    }

    private void initView() {
        View view = LayoutInflater.from(checkoutActivity).inflate(
                R.layout.pop_checkout_shipping_address_select, null);

        popupWindowSelect = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindowSelect.setBackgroundDrawable(new ColorDrawable());

        popupWindowSelect.setOutsideTouchable(true);

        popupWindowSelect.setFocusable(true);

        //popupWindowSelect.setAnimationStyle(R.style.pop_checkout_payment);

        view.findViewById(R.id.tv_checkout_shipping_address_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //switch to edit fragment and show relative address
                CheckoutShippingAddaddressFragment checkoutShippingAddaddressFragment = new CheckoutShippingAddaddressFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("address", address);
                checkoutShippingAddaddressFragment.setArguments(bundle);

                checkoutActivity.getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_left_exit
                ).replace(R.id.ll_checkout_body, checkoutShippingAddaddressFragment,"editAddressFragment").commit();

                //record fragment count of Shipping Module for Go Back Button.
                checkoutActivity.list_fragment_shipping.add(checkoutShippingAddaddressFragment);
                checkoutActivity.addressConditionInShipping = "0";

                closePopupWindow();
            }
        });

        view.findViewById(R.id.tv_checkout_shipping_address_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to address select fragment
                CheckoutShippingSelectaddressFragment checkoutShippingSelectaddressFragment = new CheckoutShippingSelectaddressFragment();
                checkoutActivity.getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.fragment_slide_right_enter,
                        R.animator.fragment_slide_left_exit
                ).replace(R.id.ll_checkout_body, checkoutShippingSelectaddressFragment,"selectAddressFragment").commit();

                //record fragment count of Shipping Module for Go Back Button.
                checkoutActivity.list_fragment_shipping.add(checkoutShippingSelectaddressFragment);
                checkoutActivity.addressConditionInShipping = "1";

                closePopupWindow();
            }
        });
    }

    private void closePopupWindow() {
        if (popupWindowSelect != null && popupWindowSelect.isShowing()) {
            popupWindowSelect.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        tvFirstname = (TextView) view.findViewById(R.id.tv_address_select_firstname);
        tvLastname = (TextView) view.findViewById(R.id.tv_address_select_lastname);
        tvAddress1 = (TextView) view.findViewById(R.id.tv_address_select_address1);
        tvAddress2 = (TextView) view.findViewById(R.id.tv_address_select_address2);
        tvCityStatePostcode = (TextView) view.findViewById(R.id.tv_address_select_citystatepostcode);
        TextView tvCountry = (TextView) view.findViewById(R.id.tv_address_select_country);
        tvTelephone = (TextView) view.findViewById(R.id.tv_address_select_telephone);

        Gson gson = new Gson();
        Bundle b = getArguments();

        address = gson.fromJson(b.getString("address"), CheckoutDefaultShippingAddress.class);

        tvFirstname.setText(address.getFirstName());
        tvLastname.setText(address.getLastName());
        tvAddress1.setText(address.getStreet().get(0));
        tvAddress2.setText(address.getStreet().get(1));


        String cityStatePostcode = address.getCity() + ", ";
        if (!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")) {

            cityStatePostcode += address.getRegion() + ", ";
        }
        cityStatePostcode += address.getPostcode();
        tvCityStatePostcode.setText(cityStatePostcode);

        tvCountry.setText(address.getCountry());
        tvTelephone.setText(address.getTelephone());
    }

    /**
     * Send request to webservice to get default address
     * INACTIVE method
     */
//    private void sendRequestToGetDefaultAddress() {
//        if(getActivity()==null){
//            return ;
//        }
//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey());
//
//        SVRCheckoutDefaultShippingAddress defaultShippingAddressHandler = new SVRCheckoutDefaultShippingAddress(checkoutActivity, parameters);
//
//        defaultShippingAddressHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//
//                final CheckoutDefaultShippingAddressEntity defaultShippingAddress = (CheckoutDefaultShippingAddressEntity) result;
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (defaultShippingAddress.getStatus() == 1) {
//
//                            initsUIWithWebserviceDatas(defaultShippingAddress.getAddress());
//                        } else {
//                            //Toast error Message
//                            JLogUtils.i("CheckoutDefaultShippingAddressEntity", "error");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(int resultCode, final String errorMsg) {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(checkoutActivity, errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//    }

    /**
     * INACTIVE method
     */
    private void initsUIWithWebserviceDatas(CheckoutDefaultShippingAddress address) {

        tvFirstname.setText(address.getFirstName());
        tvLastname.setText(address.getLastName());
        tvAddress1.setText(address.getStreet().get(0));
        tvAddress2.setText(address.getStreet().get(1));

        String cityStatePostcode = address.getCity() + ", ";
        if (!JDataUtils.isEmpty(address.getRegion()) && !address.getRegion().equalsIgnoreCase("null")) {

            cityStatePostcode += address.getRegion() + ", ";
        }
        cityStatePostcode += address.getPostcode();
        tvCityStatePostcode.setText(cityStatePostcode);

        tvTelephone.setText(address.getTelephone());

    }
}

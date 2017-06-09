package com.whitelabel.app.ui.checkout;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomCheckBox;
import com.whitelabel.app.widget.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class CheckoutDefaultAdressFragment extends BaseFragment<CheckoutDefaultAddressContract.Presenter>
        implements CheckoutDefaultAddressContract.View {
    @BindView(R.id.tv_shipping_address_change)
    CustomTextView tvShippingAddressChange;
    @BindView(R.id.tv_shipping_address_firstname)
    CustomTextView tvShippingAddressFirstname;
    @BindView(R.id.tv_shipping_address_lastname)
    CustomTextView tvShippingAddressLastname;
    @BindView(R.id.tv_shipping_address_address1)
    CustomTextView tvShippingAddressAddress1;
    @BindView(R.id.tv_shipping_address_address2)
    CustomTextView tvShippingAddressAddress2;
    @BindView(R.id.tv_shipping_address_citystatepostcode)
    CustomTextView tvShippingAddressCitystatepostcode;
    @BindView(R.id.tv_shipping_address_country)
    CustomTextView tvShippingAddressCountry;
    @BindView(R.id.tv_shipping_address_telephone)
    CustomTextView tvShippingAddressTelephone;
    @BindView(R.id.tv_shipping_address_day_telephone)
    CustomTextView tvShippingAddressDayTelephone;
    @BindView(R.id.ll_shipping_address)
    LinearLayout llShippingAddress;
    @BindView(R.id.cb_bill_address)
    CustomCheckBox cbBillAddress;
    @BindView(R.id.ll_checkbox)
    LinearLayout llCheckbox;
    @BindView(R.id.tv_billing_address_change)
    CustomTextView tvBillingAddressChange;
    @BindView(R.id.tv_billing_address_firstname)
    CustomTextView tvBillingAddressFirstname;
    @BindView(R.id.tv_billing_address_lastname)
    CustomTextView tvBillingAddressLastname;
    @BindView(R.id.tv_billing_address_address1)
    CustomTextView tvBillingAddressAddress1;
    @BindView(R.id.tv_billing_address_address2)
    CustomTextView tvBillingAddressAddress2;
    @BindView(R.id.tv_billing_address_citystatepostcode)
    CustomTextView tvBillingAddressCitystatepostcode;
    @BindView(R.id.tv_billing_address_country)
    CustomTextView tvBillingAddressCountry;
    @BindView(R.id.tv_billing_address_telephone)
    CustomTextView tvBillingAddressTelephone;
    @BindView(R.id.tv_billing_address_day_telephone)
    CustomTextView tvBillingAddressDayTelephone;
    @BindView(R.id.ll_billing_address)
    LinearLayout llBillingAddress;
    // TODO: Rename parameter arguments, choose names that match
    private Dialog mProgressDialog;

    public CheckoutDefaultAdressFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckoutDefaultAdressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutDefaultAdressFragment newInstance() {
        CheckoutDefaultAdressFragment fragment = new CheckoutDefaultAdressFragment();
        return fragment;
    }
    @Override
    public CheckoutDefaultAddressContract.Presenter getPresenter() {
        return new CheckoutDefaultAddressPresenter();
    }
    @Override
    public void showErrorMsg(String errorMsg) {
        JViewUtils.showErrorToast(getActivity(), errorMsg);
    }
    @Override
    public void showData(CheckoutDefaultAddressResponse checkoutDefaultAddressResponse) {

    }
    @Override
    public void openProgressDialog() {
        mProgressDialog = JViewUtils.showProgressDialog(getActivity());
    }
    @Override
    public void dissmissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout_default_adress, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openProgressDialog();
        mPresenter.getDefaultAddress();
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @OnClick({R.id.tv_shipping_address_change, R.id.ll_checkbox, R.id.tv_billing_address_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shipping_address_change:
                break;
            case R.id.ll_checkbox:
                break;
            case R.id.tv_billing_address_change:
                break;
        }
    }
}

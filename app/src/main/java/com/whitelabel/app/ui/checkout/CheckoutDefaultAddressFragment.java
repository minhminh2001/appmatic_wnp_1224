package com.whitelabel.app.ui.checkout;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.AddAddressActivity;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomCheckBox;
import com.whitelabel.app.widget.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

public class CheckoutDefaultAddressFragment extends BaseFragment<CheckoutDefaultAddressContract.Presenter>
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
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    public final static  int REQUEST_BILLING_ADDRESS=1000;
    public final static  int REQUEST_SHIPPING_ADDRESS=2000;
    // TODO: Rename parameter arguments, choose names that match
    private Dialog mProgressDialog;
    private AddressBook mPrimaryBilling;
    private AddressBook  mPrimaryShipping;
    public final static  int REQUEST_ADD_ADDRESS_CODE=10000;
    public AddressBook getPrimaryShipping() {
        return mPrimaryShipping;
    }
    public CheckoutDefaultAddressFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckoutDefaultAdressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutDefaultAddressFragment newInstance() {
        CheckoutDefaultAddressFragment fragment = new CheckoutDefaultAddressFragment();
        return fragment;
    }

    @Override
    public void inject() {
        DaggerPresenterComponent1.builder().applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);

    }

    @Override
    public void showErrorMsg(String errorMsg) {
        JViewUtils.showErrorToast(getActivity(), errorMsg);
    }
    public AddressBook getPrimaryBilling() {
        return mPrimaryBilling;
    }

    @Override
    public void hideBillToDefferentLayout() {
        llCheckbox.setVisibility(View.INVISIBLE);
        llBillingAddress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(AddressBook  shippingAddress,AddressBook billingAddress) {
        if(shippingAddress==null&&billingAddress==null){
            Intent intent=new Intent(getActivity(), AddAddressActivity.class);
            intent.putExtra(AddAddressActivity.EXTRA_USE_DEFAULT,false);
            startActivityForResult(intent,REQUEST_ADD_ADDRESS_CODE);
            getActivity(). overridePendingTransition(R.anim.activity_transition_enter_righttoleft,
                    R.anim.activity_transition_exit_righttoleft);
            return;
        }
        rlRoot.setVisibility(View.VISIBLE);
        if(shippingAddress!=null) {
            mPrimaryShipping=shippingAddress;
            tvShippingAddressFirstname.setText(mPrimaryShipping.getFirstName());
            tvShippingAddressLastname.setText(mPrimaryShipping.getLastName());
            if (mPrimaryShipping.getStreet().size() > 0) {
                tvShippingAddressAddress1.setText(mPrimaryShipping.getStreet().get(0));
            }
            if (mPrimaryShipping.getStreet().size() > 1
                    && !TextUtils.isEmpty(mPrimaryShipping.getStreet().get(1))) {
                tvShippingAddressAddress2.setVisibility(View.VISIBLE);
                tvShippingAddressAddress2.setText(mPrimaryShipping.getStreet().get(1));
            } else {
                tvShippingAddressAddress2.setVisibility(View.GONE);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder = stringBuilder.append(mPrimaryShipping.getCity());
            if (!TextUtils.isEmpty(mPrimaryShipping.getRegion())) {
                stringBuilder = stringBuilder.append(", ").append(mPrimaryShipping.getRegion());
            }
            if (!TextUtils.isEmpty(mPrimaryShipping.getPostcode())) {
                stringBuilder = stringBuilder.append(", ").append(mPrimaryShipping.getPostcode());
            }
            tvShippingAddressCitystatepostcode.setText(stringBuilder.toString());
            tvShippingAddressCountry.setText(mPrimaryShipping.getCountry());
            tvShippingAddressTelephone.setText(getActivity().getResources().getString(R.string.address_mobile_number) + " : " + mPrimaryShipping.getTelephone());
            tvShippingAddressDayTelephone.setText(getActivity().getResources().getString(R.string.day_time_contact) + " : " + mPrimaryShipping.getFax());
        }
        //  Default billing set
        if(billingAddress!=null) {
            mPrimaryBilling=billingAddress;
            tvBillingAddressFirstname.setText(mPrimaryBilling.getFirstName());
            tvBillingAddressLastname.setText(mPrimaryBilling.getLastName());
            if (mPrimaryBilling.getStreet().size() > 0) {
                tvBillingAddressAddress1.setText(mPrimaryBilling.getStreet().get(0));
            }
            if (mPrimaryBilling.getStreet().size() > 1 &&
                    !TextUtils.isEmpty(mPrimaryBilling.getStreet().get(1))) {
                tvBillingAddressAddress2.setVisibility(View.VISIBLE);
                tvBillingAddressAddress2.setText(mPrimaryBilling.getStreet().get(1));
            } else {
                tvBillingAddressAddress2.setVisibility(View.GONE);
            }
            StringBuilder billingBuilder = new StringBuilder();
            billingBuilder = billingBuilder.append(mPrimaryBilling.getCity());
            if (!TextUtils.isEmpty(mPrimaryBilling.getRegion())) {
                billingBuilder = billingBuilder.append(", ").append(mPrimaryBilling.getRegion());
            }
            if (!TextUtils.isEmpty(mPrimaryBilling.getPostcode())) {
                billingBuilder = billingBuilder.append(", ").append(mPrimaryBilling.getPostcode());
            }
            tvBillingAddressCitystatepostcode.setText(billingBuilder.toString());
            tvBillingAddressCountry.setText(mPrimaryBilling.getCountry());
            tvBillingAddressTelephone.setText(getActivity().getResources().getString(R.string.address_mobile_number) + " : " + mPrimaryBilling.getTelephone());
            tvBillingAddressDayTelephone.setText(getActivity().getResources().getString(R.string.day_time_contact) + " : " + mPrimaryBilling.getFax());
        }
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
        initView();
        openProgressDialog();
        mPresenter.getDefaultAddress();
    }
    private void initView() {
        cbBillAddress.setColorChecked(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        cbBillAddress.setOnCheckedChangeListener(new CustomCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
                 if(isChecked){
                     llBillingAddress.setVisibility(View.VISIBLE);
                 }else{
                     llBillingAddress.setVisibility(View.GONE);
                 }
            }
        });
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
                Intent intent=new Intent(getActivity(),CheckoutSelectAddressActivity.class);
                startActivityForResult(intent,REQUEST_SHIPPING_ADDRESS);
                ((BaseActivity)getActivity()).startActivityTransitionAnim();
                break;
            case R.id.ll_checkbox:
                cbBillAddress.setChecked(!cbBillAddress.isChecked(),true);
                break;
            case R.id.tv_billing_address_change:
                Intent billingIntent=new Intent(getActivity(),CheckoutSelectAddressActivity.class);
                startActivityForResult(billingIntent,REQUEST_BILLING_ADDRESS);
                ((BaseActivity)getActivity()).startActivityTransitionAnim();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_SHIPPING_ADDRESS&&resultCode==CheckoutSelectAddressActivity.RESULT_CODE){
                AddressBook addressBook= (AddressBook) data.getExtras().getSerializable("data");
                   showData(addressBook,null);
        }else if(requestCode==REQUEST_BILLING_ADDRESS&&resultCode==CheckoutSelectAddressActivity.RESULT_CODE){
                AddressBook  addressBook= (AddressBook) data.getExtras().getSerializable("data");
                showData(null,addressBook);
        }else if(requestCode==REQUEST_ADD_ADDRESS_CODE&&resultCode==AddAddressActivity.RESULT_CODE){
            openProgressDialog();
            mPresenter.getDefaultAddress();
        }else if(requestCode==REQUEST_ADD_ADDRESS_CODE){
             getActivity().finish();
             getActivity().overridePendingTransition(R.anim.activity_transition_enter_lefttoright, R.anim.activity_transition_exit_lefttoright);
        }
    }
}

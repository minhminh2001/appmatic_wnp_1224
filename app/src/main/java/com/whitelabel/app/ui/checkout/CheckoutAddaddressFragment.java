package com.whitelabel.app.ui.checkout;

import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.Const;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.model.CountryRegions;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomEditText;
import com.whitelabel.app.widget.CustomTextView;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckoutAddaddressFragment#} factory method to
 * create an instance of this fragment.
 */
public class CheckoutAddaddressFragment extends BaseFragment<CheckoutAddAddressContract
    .Presenter> implements CheckoutAddAddressContract.View, View.OnFocusChangeListener {

    @BindView(R.id.tv_checkout_shipping_firstname_anim)
    CustomTextView tvFirstnameAnim;

    @BindView(R.id.et_checkout_shipping_firstname)
    CustomEditText etFirstname;

    @BindView(R.id.clear_checkout_first)
    ImageView clearCheckFirst;

    @BindView(R.id.rl_checkaddadd_firstname)
    CustomButtomLineRelativeLayout rlCheckaddaddFirstname;

    @BindView(R.id.tv_checkout_shipping_lastname_anim)
    CustomTextView tvLastnameAnim;

    @BindView(R.id.et_checkout_shipping_lastname)
    CustomEditText etLastname;

    @BindView(R.id.clear_checkout_last)
    ImageView clearCheckLast;

    @BindView(R.id.rl_checkaddadd_lastname)
    CustomButtomLineRelativeLayout rlCheckaddaddLastname;

    @BindView(R.id.tv_country_anim)
    CustomTextView tvCountryAnim;

    @BindView(R.id.et_checkout_shipping_country)
    CustomEditText etShippingCountry;

    @BindView(R.id.arrow_checkout_shipping_select_country)
    ImageView arrowSelectCountry;

    @BindView(R.id.rl_checkaddadd_country)
    CustomButtomLineRelativeLayout rlCheckaddaddCountry;

    @BindView(R.id.tv_checkout_shipping_addressline1_anim)
    CustomTextView tvAddressLine1Anim;

    @BindView(R.id.et_checkout_shipping_addressline1)
    CustomEditText etAddressLine1;

    @BindView(R.id.clear_checkout_address1)
    ImageView clearCheckAddress1;

    @BindView(R.id.rl_checkaddadd_address1)
    CustomButtomLineRelativeLayout rlCheckaddaddAddress1;

    @BindView(R.id.tv_checkout_shipping_addressline2_anim)
    CustomTextView tvAddressLine2Anim;

    @BindView(R.id.et_checkout_shipping_addressline2)
    CustomEditText etAddressLine2;

    @BindView(R.id.clear_checkout_address2)
    ImageView clearCheckAddress2;

    @BindView(R.id.rl_checkaddadd_address2)
    CustomButtomLineRelativeLayout rlCheckaddaddAddress2;

    @BindView(R.id.tv_checkout_shipping_postcode_anim)
    CustomTextView tvPostCode;

    @BindView(R.id.et_checkout_shipping_postcode)
    CustomEditText etPostCode;

    @BindView(R.id.clear_checkout_code)
    ImageView clearCheckCode;

    @BindView(R.id.rl_checkaddadd_postcode)
    CustomButtomLineRelativeLayout rlCheckaddaddPostcode;

    @BindView(R.id.tv_checkout_shipping_city_anim)
    CustomTextView tvCityAnim;

    @BindView(R.id.et_checkout_shipping_city)
    CustomEditText etShippingCity;

    @BindView(R.id.clear_checkout_city)
    ImageView clearCheckCity;

    @BindView(R.id.rl_checkaddadd_city)
    CustomButtomLineRelativeLayout rlCheckaddaddCity;

    @BindView(R.id.tv_checkout_shipping_state_anim)
    CustomTextView tvStateAnim;

    @BindView(R.id.et_checkout_shipping_state)
    CustomEditText etShippingState;

    @BindView(R.id.arrow_checkout_shipping_select_state)
    ImageView arrowSelectState;

    @BindView(R.id.rl_checkout_shipping_state)
    CustomButtomLineRelativeLayout rlCheckoutShippingState;

    @BindView(R.id.tv_checkout_shipping_address_phone)
    CustomTextView tvPhoneOtheruse;

    @BindView(R.id.tv_checkout_shipping_phone_anim)
    CustomTextView tvPhone;

    @BindView(R.id.et_checkout_shipping_phone)
    CustomEditText etPhone;

    @BindView(R.id.clear_checkout_phone)
    ImageView clearCheckPhone;

    @BindView(R.id.rl_checkadd_phone)
    CustomButtomLineRelativeLayout rlCheckaddPhone;

    @BindView(R.id.tv_checkout_shipping_address_dayphone)
    CustomTextView tvDayPhoneOtheruse;

    @BindView(R.id.tv_checkout_shipping_dayphone_anim)
    CustomTextView tvDayPhone;

    @BindView(R.id.et_checkout_shipping_dayphone)
    CustomEditText etDayPhone;

    @BindView(R.id.clear_checkout_dayphone)
    ImageView clearCheckDayPhone;

    @BindView(R.id.rl_checkadd_dayphone)
    CustomButtomLineRelativeLayout rlCheckaddDayphone;

    @BindView(R.id.tv_checkout_errormsg_addnewaddress)
    CustomTextView tvErrorMsg;

    //    @BindView(R.id.pb_shoppingcart)
//    ProgressBar mProgressBar;
    @BindView(R.id.my_scroll_view)
    ScrollView scrollView;

    Unbinder unbinder;

    private Dialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    private CheckoutActivity checkoutActivity;

    private ArrayList<CountrySubclass> list_countries = new ArrayList<CountrySubclass>();

    private CheckoutDefaultShippingAddress mAddress;

    private WheelPickerEntity mCurrBean;

    private ArrayList<CountryRegions> mRegions = new ArrayList<>();

    public CheckoutAddaddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void inject() {
        DaggerPresenterComponent1.builder()
            .applicationComponent(WhiteLabelApplication.getApplicationComponent()).
            presenterModule(new PresenterModule(getActivity())).build().inject(this);

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater
            .inflate(R.layout.fragment_checkout_shipping_addaddress, container, false);
        unbinder = ButterKnife.bind(this, view);
        etFirstname.setOnFocusChangeListener(this);
        etLastname.setOnFocusChangeListener(this);
        etAddressLine1.setOnFocusChangeListener(this);
        etAddressLine2.setOnFocusChangeListener(this);
        etPostCode.setOnFocusChangeListener(this);
        etPhone.setOnFocusChangeListener(this);
        etDayPhone.setOnFocusChangeListener(this);
        etShippingCity.setOnFocusChangeListener(this);
        etShippingState.setOnFocusChangeListener(this);
        etShippingCountry.setOnFocusChangeListener(this);
        mPresenter.getCountryAndRegions();
        etPostCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0 && etPostCode.isFocused()) {
                    clearCheckCode.setVisibility(View.VISIBLE);
                } else {
                    clearCheckCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // EditText and Animation TextView --define end
        // arrow selector --define begin

        return view;
    }

    public final ArrayList<CountryRegions> getState(ArrayList<CountrySubclass> countrys) {
        ArrayList<CountryRegions> regions = new ArrayList<>();
        if (countrys != null && countrys.size() > 1) {
            CountrySubclass country = countrys.get(1);
            if (country != null) {
                etShippingCountry.setTag(country.getCountry_id() + "");
                regions = country.getRegions();
            }
        }
        return regions;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initIntent();
        initData();
    }

    private void initIntent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable address_seri = bundle.getSerializable("address");
            if (address_seri != null) {
                mAddress = (CheckoutDefaultShippingAddress) address_seri;
            }
        }
    }

    public void initData() {
        if (mAddress != null) {
            if (mAddress.getRegion() == "") {
                tvStateAnim.setVisibility(View.GONE);
            }
            initEditDatas(mAddress);
            initAllHint();
        }
        mPresenter.getCountryAndRegions();
    }

    /**
     * when editing address,use this method
     */
    private void initEditDatas(CheckoutDefaultShippingAddress address) {
        tvPhoneOtheruse.setHint(address.getAddressId());//set AddressId
        tvDayPhoneOtheruse.setHint(address.getTelephone());
        etFirstname.setText(address.getFirstName());
        etLastname.setText(address.getLastName());
        etAddressLine1.setText(address.getStreet().get(0));
        etAddressLine2.setText(address.getStreet().get(1));
        etPostCode.setText(address.getPostcode());
        etPhone.setText(address.getTelephone());
        etDayPhone.setText(address.getTelephone());
        etShippingCity.setText(address.getCity());
        etShippingState.setText(address.getRegion());
        etShippingState.setTag(address.getRegionId());
        etShippingState.setText(address.getRegion());
        etShippingState.setTag(address.getRegionId());
//        if(!TextUtils.isEmpty(etShippingState.getText().toString())){
//            etShippingState.setTextColor(JToolUtils.getColor(R.color.label_saved));
//            etShippingState.setEnabled(false);
//            arrowSelectState.setEnabled(false);
//        }
        etShippingCountry.setText(address.getCountry());
        etShippingCountry.setTag(address.getCountryId());
    }

    public void initAllHint() {
        if (!etFirstname.getText().toString().equals("")) {
            tvFirstnameAnim.setText(getResources().getString(R.string.first_name));
            tvFirstnameAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        if (!etLastname.getText().toString().equals("")) {
            tvLastnameAnim.setText(getResources().getString(R.string.last_name));
            tvLastnameAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        if (!etAddressLine1.getText().toString().equals("")) {
            tvAddressLine1Anim.setText(getResources().getString(R.string.address1));
            tvAddressLine1Anim.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        if (!etAddressLine2.getText().toString().equals("")) {
            tvAddressLine2Anim.setText(getResources().getString(R.string.address2));
            tvAddressLine2Anim.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
//        if(!etPostCode.getText().toString().equals("")){tvPostCode.setText(getResources()
// .getString(R.string.postal_code));
//            tvPostCode.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if (!etShippingCity.getText().toString().equals("")) {
            tvCityAnim.setText(getResources().getString(R.string.city));
            tvCityAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        if (!etPhone.getText().toString().equals("")) {
            tvPhone.setText(getResources().getString(R.string.eg123));
            tvPhone.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }
        if (!etDayPhone.getText().toString().equals("")) {
            tvDayPhone.setText(getResources().getString(R.string.day_time_contact));
            tvDayPhone.setTextColor(JToolUtils.getColor(R.color.label_saved));
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean onblurAll(int id) {
        switch (id) {
            case R.id.et_checkout_shipping_firstname:
                rlCheckaddaddFirstname.setBottomLineActive(false);
                tvFirstnameAnim.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvFirstnameAnim.setVisibility(View.VISIBLE);
                if (etFirstname.getText().toString().trim().equals("")) {
                    etFirstname.setHint(getResources().getString(R.string.first_name));
//                    tvFirstnameAnim.getLocationOnScreen(location);

                    etFirstname.clearAnimation();
                    //验证字段
                    tvFirstnameAnim.setText(getResources().getString(R.string.required_field));
                    tvFirstnameAnim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_lastname:
                rlCheckaddaddLastname.setBottomLineActive(false);
                tvLastnameAnim.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvLastnameAnim.setVisibility(View.VISIBLE);
                if (etLastname.getText().toString().trim().equals("")) {
                    etLastname.setHint("Last Name");
//                    tvLastnameAnim.getLocationOnScreen(location);

                    //验证字段
                    tvLastnameAnim.setText(getResources().getString(R.string.required_field));
                    tvLastnameAnim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_country:
                rlCheckaddaddCountry.setBottomLineActive(false);
                tvCountryAnim.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvCountryAnim.setVisibility(View.VISIBLE);
                if (etShippingCountry.getText().toString().trim().equals("")) {
                    etShippingCountry.setHint("Country");
//                    tvCountryAnim.getLocationOnScreen(location);

                    //验证字段
                    tvCountryAnim.setText(getResources().getString(R.string.required_field));
                    tvCountryAnim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_addressline1:
                rlCheckaddaddAddress1.setBottomLineActive(false);
                tvAddressLine1Anim
                    .setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvAddressLine1Anim.setVisibility(View.VISIBLE);
                if (etAddressLine1.getText().toString().trim().equals("")) {
                    etAddressLine1.setHint(getResources().getString(R.string.address1));
//                    tvAddressLine1Anim.getLocationOnScreen(location);

                    //验证字段
                    tvAddressLine1Anim.setText(getResources().getString(R.string.required_field));
                    tvAddressLine1Anim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_addressline2:
                rlCheckaddaddAddress2.setBottomLineActive(false);
                tvAddressLine2Anim
                    .setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvAddressLine2Anim.setVisibility(View.VISIBLE);
                if (etAddressLine2.getText().toString().trim().equals("")) {
                    etAddressLine2.setHint(getResources().getString(R.string.address2));
                    tvAddressLine2Anim.clearAnimation();
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_postcode:
                rlCheckaddaddPostcode.setBottomLineActive(false);
                tvPostCode.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvPostCode.setVisibility(View.VISIBLE);
                if (etPostCode != null && etPostCode.getText().toString().trim().equals("")) {
                    etPostCode.setHint("Postal Code");
//                    tvPostCode.getLocationOnScreen(location);

                    //验证字段
                    tvPostCode.setText(getResources().getString(R.string.This_is_a_required_field));
                    tvPostCode.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else if (etPostCode != null && etPostCode.getText().toString().trim()
                    .length() < 4) {
//                    tvPostCode.getLocationOnScreen(location);
                    //验证字段
                    tvPostCode.setText(getResources().getString(R.string.blur_postalcode));
                    tvPostCode.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_city:
                rlCheckaddaddCity.setBottomLineActive(false);
                tvCityAnim.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvCityAnim.setVisibility(View.VISIBLE);
                if (etShippingCity.getText().toString().trim().equals("")) {
                    etShippingCity.setHint("City");
//                    tvCityAnim.getLocationOnScreen(location);
                    //验证字段
                    tvCityAnim.setText(getResources().getString(R.string.required_field));
                    tvCityAnim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_state:
                rlCheckoutShippingState.setBottomLineActive(false);
                tvStateAnim.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvStateAnim.setVisibility(View.VISIBLE);
                if (etShippingState.getText().toString().trim().equals("")) {
//                    tvStateAnim.getLocationOnScreen(location);
                    tvStateAnim.setText(getResources().getString(R.string.required_field));
                    tvStateAnim.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_phone:

                rlCheckaddPhone.setBottomLineActive(false);
                tvPhone.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvPhone.setVisibility(View.VISIBLE);
                if (etPhone.getText().toString().trim().equals("")) {
                    etPhone.setHint(getResources().getString(R.string.eg123));
//                    tvPhoneOtheruse.getLocationOnScreen(location);
                    //验证字段
                    tvPhone.setText(getResources().getString(R.string.required_field));
                    tvPhone.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else if (etPhone.getText().toString().length() < 7) {
                    etPhone.setHint(getResources().getString(R.string.eg123));
//                    tvPhoneOtheruse.getLocationOnScreen(location);
                    //验证字段
                    tvPhone.setText(getResources().getString(R.string.address_phone_error_hint));
                    tvPhone.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }
                break;
            case R.id.et_checkout_shipping_dayphone:
                rlCheckaddDayphone.setBottomLineActive(false);
                tvDayPhone.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvDayPhone.setVisibility(View.VISIBLE);
                if (etDayPhone.getText().toString().trim().equals("")) {
                    etDayPhone.setHint(getResources().getString(R.string.address_day_phone));
//                    tvDayPhone.getLocationOnScreen(location);
                    tvDayPhone.clearAnimation();
                    //验证字段
                    tvDayPhone.setText(getResources().getString(R.string.required_field));
                    tvDayPhone.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else if (etDayPhone.getText().toString().length() < 7) {
                    etDayPhone.setHint(getResources().getString(R.string.address_day_phone));
//                    tvDayPhone.getLocationOnScreen(location);
                    tvDayPhone.clearAnimation();
                    //验证字段
                    tvDayPhone.setText(getResources().getString(R.string.address_phone_error_hint));
                    tvDayPhone.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    tvDayPhone.clearAnimation();
                }
                break;
        }
        return true;
    }

    public void checkAndSave(ISaveAddressMsgData iSaveAddressMsgData) {
//        JViewUtils.cleanCurrentViewFocus(getActivity());
        if (onblurAll(R.id.
            et_checkout_shipping_firstname)
            && onblurAll(R.id.et_checkout_shipping_lastname)
            && onblurAll(R.id.et_checkout_shipping_country)
            && onblurAll(R.id.et_checkout_shipping_addressline1)
            && onblurAll(R.id.et_checkout_shipping_city)
            && onblurAll(R.id.et_checkout_shipping_state)
            && onblurAll(R.id.et_checkout_shipping_phone)
            && onblurAll(R.id.et_checkout_shipping_dayphone)) {
            String regionText = null;
            String regionId = null;
            if (!TextUtils.isEmpty(String.valueOf(etShippingState.getText().toString().trim()))) {
                regionText = etShippingState.getText().toString().trim();
                regionId = String.valueOf(etShippingState.getTag());
            }
            iSaveAddressMsgData.createCustomerAddress(getEditTextMsg(etFirstname),
                getEditTextMsg(etLastname),
                String.valueOf(etShippingCountry.getTag()),
                getEditTextMsg(etPhone),
                getEditTextMsg(etAddressLine1),
                getEditTextMsg(etAddressLine2),
                getEditTextMsg(etDayPhone),
                getEditTextMsg(etPostCode),
                getEditTextMsg(etShippingCity),
                regionText, regionId
            );
        }
    }

    private String getEditTextMsg(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * EditText focus listener
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_checkout_shipping_firstname:
                if (hasFocus) {
                    rlCheckaddaddFirstname.setBottomLineActive(true);
                    if (etFirstname.getText().length() != 0) {
                        clearCheckFirst.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckFirst.setVisibility(View.GONE);
                    }
                    tvFirstnameAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvFirstnameAnim.startAnimation(getHintAnimation(tvFirstnameAnim, "First Name"));
                    etFirstname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && etFirstname.isFocused()) {
                                clearCheckFirst.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckFirst.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    rlCheckaddaddFirstname.setBottomLineActive(false);
                    clearCheckFirst.setVisibility(View.GONE);
                    //validate text format
                    if (JDataUtils.isStringBlank(etFirstname.getText())) {
                        tvFirstnameAnim
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvFirstnameAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvFirstnameAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_lastname:
                if (hasFocus) {
                    rlCheckaddaddLastname.setBottomLineActive(true);
                    if (etLastname.getText().length() != 0) {
                        clearCheckLast.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckLast.setVisibility(View.GONE);
                    }
                    tvLastnameAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvLastnameAnim.startAnimation(getHintAnimation(tvLastnameAnim, "Last Name"));
                    etLastname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && etLastname.isFocused()) {
                                clearCheckLast.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckLast.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    rlCheckaddaddLastname.setBottomLineActive(false);
                    clearCheckLast.setVisibility(View.GONE);
                    if (JDataUtils.isStringBlank(etLastname.getText())) {
                        tvLastnameAnim
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvLastnameAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvLastnameAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_addressline1:
                if (hasFocus) {
                    rlCheckaddaddAddress1.setBottomLineActive(true);
                    if (etAddressLine1.getText().length() != 0) {
                        clearCheckAddress1.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckAddress1.setVisibility(View.INVISIBLE);
                    }
                    tvAddressLine1Anim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvAddressLine1Anim
                        .startAnimation(getHintAnimation(tvAddressLine1Anim, "Address 1"));
                    etAddressLine1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && etAddressLine1.isFocused()) {
                                clearCheckAddress1.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckAddress1.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rlCheckaddaddAddress1.setBottomLineActive(false);
                    clearCheckAddress1.setVisibility(View.INVISIBLE);
                    if (JDataUtils.isStringBlank(etAddressLine1.getText())) {
                        tvAddressLine1Anim
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvAddressLine1Anim
                            .setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvAddressLine1Anim
                            .setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_addressline2:
                if (hasFocus) {
                    rlCheckaddaddAddress2.setBottomLineActive(true);
                    if (etAddressLine2.getText().length() != 0 && etAddressLine2.isFocused()) {
                        clearCheckAddress2.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckAddress2.setVisibility(View.INVISIBLE);
                    }
                    tvAddressLine2Anim.setVisibility(View.VISIBLE);
                    tvAddressLine2Anim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvAddressLine2Anim.startAnimation(
                        getHintAnimation(tvAddressLine2Anim, "Address 2 (Optional)"));
                    etAddressLine2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && etAddressLine2.isFocused()) {
                                clearCheckAddress2.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckAddress2.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rlCheckaddaddAddress2.setBottomLineActive(false);
                    clearCheckAddress2.setVisibility(View.INVISIBLE);
                    tvAddressLine2Anim.setTextColor(getResources().getColor(R.color.label_saved));
                    if ("".equals(etAddressLine2.getText().toString().trim())) {
                        tvAddressLine2Anim.setVisibility(View.INVISIBLE);
                    }
                }
                break;
//            case R.id.et_checkout_shipping_postcode:
//                if (hasFocus) {
//                    rl_checkaddadd_postcode.setBottomLineActive(true);
//                    if (etPostCode.getText().length()!=0) {
//                        clearCheckCode.setVisibility(View.VISIBLE);
//                    }else {
//                        clearCheckCode.setVisibility(View.INVISIBLE);
//                    }
//                    tvPostCode.setTextColor(getResources().getColor(R.color.colorAccent));
//                    tvPostCode.startAnimation(getHintAnimation(tvPostCode, "Postal Code"));
//                } else {
//                    rl_checkaddadd_postcode.setBottomLineActive(false);
//                    clearCheckCode.setVisibility(View.INVISIBLE);
//                    if (JDataUtils.isStringBlank(etPostCode.getText())) {
//                        tvPostCode.setText(getResources().getString(R.string
// .This_is_a_required_field));
//                        tvPostCode.setTextColor(getResources().getColor(R.color.red_common));
//                    }else if(etPostCode!=null&&etPostCode.getText().toString().trim().length()<4){
//                        tvPostCode.setText(getResources().getString(R.string.blur_postalcode));
//                        tvPostCode.setTextColor(getResources().getColor(R.color.red_common));
//                    }else{
//                        /**
//                         * We need to send request to get city and state by postcode.
//                         */
////                        sendRequestToGetCityAndStateByPostcode(etPostCode.getText().toString());
//                        tvPostCode.setTextColor(getResources().getColor(R.color.label_saved));
//                    }
//                }
//                break;
            case R.id.et_checkout_shipping_city:
                if (hasFocus) {
                    rlCheckaddaddCity.setBottomLineActive(true);
                    if (etShippingCity.getText().length() != 0) {
                        clearCheckCity.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckCity.setVisibility(View.GONE);
                    }
                    tvCityAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvCityAnim.startAnimation(getHintAnimation(tvCityAnim, "City"));
                    etShippingCity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && etShippingCity.isFocused()) {
                                clearCheckCity.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckCity.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rlCheckaddaddCity.setBottomLineActive(false);
                    clearCheckCity.setVisibility(View.GONE);
                    if (JDataUtils.isStringBlank(etShippingCity.getText())) {
                        tvCityAnim
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvCityAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvCityAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_state:
                if (!hasFocus) {
                    tvStateAnim.setTextColor(getResources().getColor(R.color.label_saved));
                }
                break;
            case R.id.et_checkout_shipping_phone:
                if (hasFocus) {
                    rlCheckaddPhone.setBottomLineActive(true);
                    if (etPhone.getText().length() != 0) {
                        clearCheckPhone.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckPhone.setVisibility(View.INVISIBLE);
                    }
                    tvPhone.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvPhone.startAnimation(
                        getHintAnimation(tvPhone, getResources().getString(R.string.eg123)));
                    etPhone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0) {
                                clearCheckPhone.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckPhone.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rlCheckaddPhone.setBottomLineActive(false);
                    clearCheckPhone.setVisibility(View.INVISIBLE);
                    if (JDataUtils.isStringBlank(etPhone.getText())) {
                        tvPhone
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvPhone.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvPhone.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_dayphone:
                if (hasFocus) {
                    rlCheckaddDayphone.setBottomLineActive(true);
                    if (etDayPhone.getText().length() != 0) {
                        clearCheckDayPhone.setVisibility(View.VISIBLE);
                    } else {
                        clearCheckDayPhone.setVisibility(View.INVISIBLE);
                    }
                    tvDayPhone.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvDayPhone.startAnimation(getHintAnimation(tvDayPhone,
                        getResources().getString(R.string.day_time_contact)));
                    etDayPhone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0) {
                                clearCheckDayPhone.setVisibility(View.VISIBLE);
                            } else {
                                clearCheckDayPhone.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rlCheckaddDayphone.setBottomLineActive(false);
                    clearCheckDayPhone.setVisibility(View.INVISIBLE);
                    if (JDataUtils.isStringBlank(etDayPhone.getText())) {
                        tvDayPhone
                            .setText(getResources().getString(R.string.This_is_a_required_field));
                        tvDayPhone.setTextColor(getResources().getColor(R.color.red_common));
                    } else {
                        tvDayPhone.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_country:
                if (!hasFocus) {
                    tvCountryAnim.setTextColor(getResources().getColor(R.color.label_saved));
                }
            default:
                break;
        }
    }

    public void openState() {
        if (mRegions == null && getActivity() != null) {
            Toast.makeText(getActivity(), "Please select a country", Toast.LENGTH_SHORT).show();
            return;
        }
        rlCheckoutShippingState.setBottomLineActive(true);
        ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity entity = new WheelPickerEntity();
        for (int i = 0; i < mRegions.size(); i++) {
            WheelPickerEntity bean = new WheelPickerEntity();
            bean.setDisplay(mRegions.get(i).getName());
            bean.setValue(mRegions.get(i).getRegion_id());
            if (String.valueOf(etShippingState.getTag()).equals(bean.getValue())) {
                entity.setIndex(i);
            }
            list.add(bean);
        }
        createStatueDialogPicker(list, entity, etShippingState);

    }

    @OnClick({R.id.arrow_checkout_shipping_select_state, R.id.et_checkout_shipping_state, R.id
        .arrow_checkout_shipping_select_country, R.id.et_checkout_shipping_country, R.id
        .clear_checkout_first,
        R.id.clear_checkout_last, R.id.clear_checkout_address1, R.id.clear_checkout_address2, R
        .id.clear_checkout_code, R.id.clear_checkout_city, R.id.clear_checkout_phone, R.id
        .clear_checkout_dayphone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_checkout_shipping_select_state:
            case R.id.et_checkout_shipping_state:
                openState();
                break;
            case R.id.arrow_checkout_shipping_select_country:
            case R.id.et_checkout_shipping_country:
                tvCountryAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                tvCountryAnim.startAnimation(getHintAnimation(tvCountryAnim, "Country"));
                etShippingCountry.setFocusable(true);
                etShippingCountry.requestFocus();
                ArrayList<WheelPickerEntity> wheelBeans = new ArrayList<WheelPickerEntity>();
                WheelPickerEntity oldBean = new WheelPickerEntity();
                for (int i = 0; i < list_countries.size(); i++) {
                    WheelPickerEntity ww = new WheelPickerEntity();
                    ww.setDisplay(list_countries.get(i).getName());
                    ww.setValue(list_countries.get(i).getCountry_id());
                    if (String.valueOf(etShippingCountry.getTag())
                        .equals(list_countries.get(i).getCountry_id())) {
                        oldBean.setIndex(i);
                    }
                    wheelBeans.add(ww);
                }
                createDialogPicker(wheelBeans, oldBean, etShippingCountry);
                break;
            case R.id.clear_checkout_first:
                etFirstname.setText("");
                break;
            case R.id.clear_checkout_last:
                etLastname.setText("");
                break;
            case R.id.clear_checkout_address1:
                etAddressLine1.setText("");
                break;
            case R.id.clear_checkout_address2:
                etAddressLine2.setText("");
                break;
            case R.id.clear_checkout_code:
                etPostCode.setText("");
                break;
            case R.id.clear_checkout_city:
                etShippingCity.setText("");
                break;
            case R.id.clear_checkout_phone:
                etPhone.setText("");
                break;
            case R.id.clear_checkout_dayphone:
                etDayPhone.setText("");
                break;
            default:
                break;
        }
    }

    public boolean verifyNotNull(EditText view, TextView text) {
        if ("".equals(view.getText().toString().trim())) {
            text.setText(getResources().getString(R.string.This_is_a_required_field));
            text.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        return true;
    }

    /**
     * create a select dialog
     *
     * @param list options
     * @param view --container of select result
     */
    private void createStatueDialogPicker(ArrayList<WheelPickerEntity> list,
        final WheelPickerEntity oldEntity, final TextView view) {
        AnimUtil.rotateArrow(arrowSelectState, true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                AnimUtil.rotateArrow(arrowSelectState, false);
                tvStateAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
                rlCheckoutShippingState.setBottomLineActive(false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue
                    .getDisplay() + "  newValue => " + newValue.getDisplay());
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                AnimUtil.rotateArrow(arrowSelectState, false);
                rlCheckoutShippingState.setBottomLineActive(false);
                tvStateAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
                if (newValue == null) {
                    return;
                } else {
                    if (TextUtils.isEmpty(newValue.getDisplay())) {
                        return;
                    }
                    if (getResources().getString(R.string.pleaseselect)
                        .equals(newValue.getDisplay())) {
                        return;
                    }
                    tvStateAnim.setVisibility(View.VISIBLE);
                    view.setText(newValue.getDisplay());
                    view.setTag(newValue.getValue());
                }

            }
        });
        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
    }

    private void createDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity,
        final TextView view) {
        rlCheckaddaddCountry.setBottomLineActive(true);
        AnimUtil.rotateArrow(arrowSelectCountry, true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                AnimUtil.rotateArrow(arrowSelectCountry, false);
                tvCountryAnim.setTextColor(getResources().getColor(R.color.label_saved));
                rlCheckaddaddCountry.setBottomLineActive(false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell",
                    "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                AnimUtil.rotateArrow(arrowSelectCountry, false);
                tvCountryAnim.setTextColor(getResources().getColor(R.color.label_saved));
                rlCheckaddaddCountry.setBottomLineActive(false);
                if (newValue == null) {
                    System.out.print("oldValue=====" + oldValue);
                    view.setText(oldValue.getDisplay());
                    view.setTag(oldValue.getValue());
                    mCurrBean = oldValue;
                } else {
                    if (getResources().getString(R.string.pleaseselect)
                        .equals(newValue.getDisplay())) {
                        return;
                    }
                    if (TextUtils.isEmpty(newValue.getDisplay())) {
                        return;
                    }
                    System.out.print("newValue=====" + newValue);
                    view.setText(newValue.getDisplay());
                    view.setTag(newValue.getValue());
                    mCurrBean = newValue;
                }
                if (mRegions != null) {
                    mRegions.clear();
                }
                for (int i = 0; i < list_countries.size(); i++) {
                    if (mCurrBean.getValue().equals(list_countries.get(i).getCountry_id())) {
                        mRegions.addAll(list_countries.get(i).getRegions());
                    }
                }
                System.out.println("mRegions=========================" + mRegions);
                if (mRegions == null || mRegions.size() == 0) {
                    etShippingState.setVisibility(View.VISIBLE);
                    rlCheckoutShippingState.setVisibility(View.GONE);
                } else {
                    etShippingState.setVisibility(View.GONE);
                    rlCheckoutShippingState.setVisibility(View.VISIBLE);
                }


            }
        });
        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
    }

    /**
     * init Text animation
     */
    private Animation getHintAnimation(final TextView tv, final String hintText) {

        Animation animation = AnimationUtils
            .loadAnimation(checkoutActivity, R.anim.anim_checkout_hint);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                    }
                }, 100);

                tv.setText(hintText);
                tv.setTextColor(
                    WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        return animation;
    }


    @Override
    public void showErrorMsg(String errorMsg) {
        JViewUtils.showErrorToast(getActivity(), errorMsg);
    }

    @Override
    public void showData(SVRAppServiceCustomerCountry countryEntityResult) {
        if (countryEntityResult == null) {
            return;
        }
        if (countryEntityResult.getCountry() != null) {
            list_countries = countryEntityResult.getCountry();
            list_countries
                .add(0, new CountrySubclass("", getResources().getString(R.string.pleaseselect)));
            ArrayList<CountryRegions> state = getState(list_countries);
            if (state != null) {
                mRegions.clear();
                mRegions.addAll(state);
                mRegions.add(0,
                    new CountryRegions("", getResources().getString(R.string.pleaseselect)));
            }
            if (list_countries.size() > 1) {
                etShippingCountry.setText(list_countries.get(1).getName());
                etShippingCountry.setTag(list_countries.get(1).getCountry_id());
            }
            checkoutActivity.scrollViewBody
                .scrollTo(0, (int) (getResources().getDimension(R.dimen.scroll_height) * 100));
            if ("".equals(etShippingState.getText().toString().trim())) {
                tvStateAnim.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        GaTrackHelper.getInstance().googleAnalytics(Const.GA.GUEST_ADDRESS_SCREEN, getActivity());
    }

    public interface ISaveAddressMsgData {

        public void createCustomerAddress(
            String firstName,
            String lastName,
            String countryId,
            String telePhone,
            String street0,
            String street1,
            String fax,
            String postCode,
            String city,
            String region,
            String regionId);
    }

    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}

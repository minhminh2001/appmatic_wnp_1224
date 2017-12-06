package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.google.gson.Gson;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.CheckoutDefaultShippingAddress;
import com.whitelabel.app.model.CountryRegions;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRGetCityANdStateByPostCodeEntity;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.ui.checkout.CheckoutActivity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckoutShippingAddaddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckoutShippingAddaddressFragment#} factory method to
 * create an instance of this fragment.
 */
public class CheckoutShippingAddaddressFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {
    private ProgressBar   mProgressBar;
    /**
     * EditText and Animation TextView define
     */
    public TextView tv_country_anim,tvFirstnameAnim, tvLastnameAnim, tvAddressLine1Anim, tvAddressLine2Anim,
            tvPostCode, tvPhone, tvCityAnim, tvStateAnim;
    public EditText etFirstname, etLastname, etAddressLine1, etAddressLine2, etPostCode, etPhone, etShippingCity,  etShippingState, etShippingCountry;
    /**
     * TextView and selector ImageView define;
     */
    private MyAccountDao mAccountDao;
    private ImageView arrowSelectCountry, arrowSelectState,clearCheckFirst,clearCheckLast,clearCheckAddress1,clearCheckAddress2,clearCheckCode,clearCheckCity,clearCheckPhone;
    private CustomButtomLineRelativeLayout rl_checkaddadd_firstname,rl_checkaddadd_lastname,rl_checkaddadd_country,rl_checkaddadd_city,
            rl_checkaddadd_address1,rl_checkaddadd_address2,rl_checkout_shipping_state,rl_checkadd_phone;
    public TextView tvErrorMsg,tvPhone_otheruse ;
    private OnFragmentInteractionListener mListener;
    private CheckoutActivity checkoutActivity;
    private ArrayList<CountrySubclass> list_countries = new ArrayList<CountrySubclass>();
    private final int REQUESTCODE_LOGIN = 1000;
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


    private static final  class DataHandler extends Handler{
        private final WeakReference<CheckoutActivity> mActivity;
        private final WeakReference<CheckoutShippingAddaddressFragment> mFragment;
        public DataHandler(CheckoutActivity activity,CheckoutShippingAddaddressFragment fragment){
            mActivity=new WeakReference<CheckoutActivity>(activity);
            mFragment=new WeakReference<CheckoutShippingAddaddressFragment>(fragment);
        }
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            CheckoutActivity activity=mActivity.get();
            CheckoutShippingAddaddressFragment fragment=mFragment.get();
            switch (msg.what){
                case MyAccountDao.REQUEST_GETCOUNTRY_REGIONS:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        final SVRAppServiceCustomerCountry countryEntityResult = (SVRAppServiceCustomerCountry) msg.obj;
                        if(activity!=null) {
                            fragment.mProgressBar.setVisibility(View.GONE);
                            fragment.etShippingCountry.setOnClickListener(fragment);
                            fragment.etShippingState.setOnClickListener(fragment);
                            fragment.etShippingState.setTextColor(activity.getResources().getColor(R.color.hint));
                            fragment.list_countries = countryEntityResult.getCountry();
                            fragment.list_countries.add(0, new CountrySubclass("", activity.getResources().getString(R.string.pleaseselect)));
                            if(fragment.list_countries.size()>1){
                                fragment.etShippingCountry.setText(fragment.list_countries.get(1).getName());
                                fragment.etShippingCountry.setTag(fragment.list_countries.get(1).getCountry_id());
                            }
                            fragment.mRegions.addAll(fragment.getState(fragment.list_countries));
                            fragment.mRegions.add(0, new CountryRegions("", activity.getResources().getString(R.string.pleaseselect)));
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("countries", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("countries", new Gson().toJson(countryEntityResult));
                            editor.commit();
                        }
                    }else{
                        ErrorMsgBean errorBean= (ErrorMsgBean) msg.obj;
                        if(activity!=null&&errorBean!=null&&!JToolUtils.expireHandler(activity,errorBean.getErrorMessage(),fragment.REQUESTCODE_LOGIN)){
                            Toast.makeText(activity, errorBean.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_CITY_STATE_BYPOSTCODE:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS&&activity!=null){
                        SVRGetCityANdStateByPostCodeEntity getCityANdStateByPostCodeEntity = (SVRGetCityANdStateByPostCodeEntity) msg.obj;
                        if (getCityANdStateByPostCodeEntity.getStatus() == 1) {
                            fragment.etShippingCity.setEnabled(true);
                            if (!JDataUtils.isEmpty(getCityANdStateByPostCodeEntity.getCity())) {
                                fragment.etShippingCity.setText(getCityANdStateByPostCodeEntity.getCity());
                                fragment.etShippingState.setText(getCityANdStateByPostCodeEntity.getRegionName());
                                fragment.etShippingState.setTag(getCityANdStateByPostCodeEntity.getRegionId());
                                fragment.tvCityAnim.setVisibility(View.VISIBLE);
                                fragment.tvCityAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
                                fragment.rl_checkaddadd_city.setBottomLineActive(false);
                                if(fragment.etShippingCity.hasFocus()){
                                    fragment.rl_checkaddadd_city.setFocusable(true);
                                    fragment.rl_checkaddadd_city.requestFocus();
                                    InputMethodManager inputMethodManager = (InputMethodManager)  fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(fragment.etShippingCity.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }

                                fragment.tvCityAnim.setText("City");
                                fragment.tvStateAnim.setVisibility(View.VISIBLE);
                                fragment.tvStateAnim.setText("State");
                                fragment.tvStateAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
//                                fragment.etShippingState.setEnabled(false);
//                                fragment.etShippingState.setTextColor(JToolUtils.getColor(R.color.label_saved));
                                //"state" 将item 标题显示出来
                            }
                        }
                    }else if(activity!=null){
                        ErrorMsgBean msgBean= (ErrorMsgBean) msg.obj;
                        if(msgBean!=null&&!JToolUtils.expireHandler(activity,msgBean.getErrorMessage(),fragment.REQUESTCODE_LOGIN)){
                            fragment.tvErrorMsg.setText(msgBean.getErrorMessage());
                            fragment.tvErrorMsg.setVisibility(View.VISIBLE);
                            fragment. checkoutActivity.scrollViewBody.scrollTo(0, 5000000);
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(activity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    fragment.mProgressBar.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_shipping_addaddress, container, false);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.my_scroll_view);
        mProgressBar=(ProgressBar)view.findViewById(R.id.pb_shoppingcart);
        tvPhone_otheruse = (TextView) view.findViewById(R.id.tv_checkout_shipping_address_phone);
        // edit line
        rl_checkadd_phone = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkadd_phone);
        rl_checkaddadd_city = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_city);
//        rl_checkaddadd_postcode = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_postcode);
        rl_checkaddadd_firstname = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_firstname);
        rl_checkaddadd_lastname = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_lastname);
        rl_checkaddadd_country = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_country);
        rl_checkaddadd_address1 = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_address1);
        rl_checkaddadd_address2 = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkaddadd_address2);
        // EditText and Animation TextView --define begin
        tv_country_anim = (TextView) view.findViewById(R.id.tv_country_anim);
        tvFirstnameAnim = (TextView) view.findViewById(R.id.tv_checkout_shipping_firstname_anim);
        etFirstname = (EditText) view.findViewById(R.id.et_checkout_shipping_firstname);
        etFirstname.setOnFocusChangeListener(this);
        tvLastnameAnim = (TextView) view.findViewById(R.id.tv_checkout_shipping_lastname_anim);
        etLastname = (EditText) view.findViewById(R.id.et_checkout_shipping_lastname);
        etLastname.setOnFocusChangeListener(this);
        tvAddressLine1Anim = (TextView) view.findViewById(R.id.tv_checkout_shipping_addressline1_anim);
        etAddressLine1 = (EditText) view.findViewById(R.id.et_checkout_shipping_addressline1);
        etAddressLine1.setOnFocusChangeListener(this);
        tvAddressLine2Anim = (TextView) view.findViewById(R.id.tv_checkout_shipping_addressline2_anim);
        etAddressLine2 = (EditText) view.findViewById(R.id.et_checkout_shipping_addressline2);
        etAddressLine2.setOnFocusChangeListener(this);
        tvPostCode = (TextView) view.findViewById(R.id.tv_checkout_shipping_postcode_anim);
        etPostCode = (EditText) view.findViewById(R.id.et_checkout_shipping_postcode);
        etPostCode.setOnFocusChangeListener(this);
        tvPhone = (TextView) view.findViewById(R.id.tv_checkout_shipping_phone_anim);
        etPhone = (EditText) view.findViewById(R.id.et_checkout_shipping_phone);
        etPhone.setOnFocusChangeListener(this);
        tvCityAnim = (TextView) view.findViewById(R.id.tv_checkout_shipping_city_anim);
        etShippingCity = (EditText) view.findViewById(R.id.et_checkout_shipping_city);
        etShippingCity.setOnFocusChangeListener(this);
        arrowSelectState = (ImageView) view.findViewById(R.id.arrow_checkout_shipping_select_state);
        arrowSelectState.setOnClickListener(this);
        tvStateAnim = (TextView) view.findViewById(R.id.tv_checkout_shipping_state_anim);
        etShippingState = (EditText) view.findViewById(R.id.et_checkout_shipping_state);
        etShippingState.setOnFocusChangeListener(this);
        arrowSelectCountry = (ImageView) view.findViewById(R.id.arrow_checkout_shipping_select_country);
        etShippingCountry = (EditText) view.findViewById(R.id.et_checkout_shipping_country);
        etShippingCountry.setOnClickListener(this);
        etShippingCountry.setOnFocusChangeListener(this);
        arrowSelectCountry.setOnClickListener(this);

//        clearCheckFirst,clearCheckLast,clearCheckAddress1,clearCheckAddress2,clearCheckCode,clearCheckCity,clearCheckPhone
        clearCheckFirst=(ImageView)view.findViewById(R.id.clear_checkout_first);
        clearCheckLast=(ImageView)view.findViewById(R.id.clear_checkout_last);
        clearCheckAddress1=(ImageView)view.findViewById(R.id.clear_checkout_address1);
        clearCheckAddress2=(ImageView)view.findViewById(R.id.clear_checkout_address2);
        clearCheckCode=(ImageView)view.findViewById(R.id.clear_checkout_code);
        clearCheckCity=(ImageView)view.findViewById(R.id.clear_checkout_city);
        clearCheckPhone=(ImageView)view.findViewById(R.id.clear_checkout_phone);


        clearCheckFirst.setOnClickListener(this);
        clearCheckLast.setOnClickListener(this);
        clearCheckAddress1.setOnClickListener(this);
        clearCheckAddress2.setOnClickListener(this);
        clearCheckCode.setOnClickListener(this);
        clearCheckCity.setOnClickListener(this);
        clearCheckPhone.setOnClickListener(this);


        // arrow selector --define end
        rl_checkout_shipping_state = (CustomButtomLineRelativeLayout) view.findViewById(R.id.rl_checkout_shipping_state);
        tvErrorMsg = (TextView) view.findViewById(R.id.tv_checkout_errormsg_addnewaddress);
//        etShippingCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId){
//                    case EditorInfo.IME_ACTION_NEXT:
//                        if(getActivity()!=null) {
//                            JToolUtils.closeKeyBoard(getActivity(),etShippingCity);
//                        }
//                        openState();
//                        break;
//                }
//            return true;
//            }
//        });
        etPostCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                etShippingState.setTextColor(checkoutActivity.getResources().getColor(R.color.black000000));
//                etShippingState.setEnabled(true);
                if (s.length() != 0&&etPostCode.isFocused()) {
                    clearCheckCode.setVisibility(View.VISIBLE);
                } else {
                    clearCheckCode.setVisibility(View.INVISIBLE);
//                              etShippingState.setTextColor(getResources().getColor(R.color.black));
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
    public final  ArrayList<CountryRegions> getState(ArrayList<CountrySubclass>  countrys){
        ArrayList<CountryRegions>  regions=null;
        if(countrys!=null&&countrys.size()>1){
            CountrySubclass country=  countrys.get(1);
            etShippingCountry.setTag(country.getCountry_id() + "");
            regions = country.getRegions();
        }
        return  regions;
    }

    private  CheckoutDefaultShippingAddress mAddress;
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
                mAddress = (CheckoutDefaultShippingAddress)address_seri;
            }
        }
    }
    public void initData(){
        String TAG = this.getClass().getSimpleName();
        if(mAddress!=null){
            if(mAddress.getRegion()==""){
                tvStateAnim.setVisibility(View.GONE);
            }
            initEditDatas(mAddress);
            initAllHint();
        }
        DataHandler dataHandler = new DataHandler(checkoutActivity, this);
        mAccountDao=new MyAccountDao(TAG, dataHandler);
        mRegions=new ArrayList<CountryRegions>();
        SharedPreferences sharedPreferences = checkoutActivity.getSharedPreferences("countries", Activity.MODE_PRIVATE);
        String  countryJson=sharedPreferences.getString("countries","");
        if("".equals(countryJson)) {
            sendRequestToGetCountryAndRegions("from_country");
        }else{
            etShippingCountry.setOnClickListener(this);
            etShippingState.setOnClickListener(this);
            SVRAppServiceCustomerCountry  countrys=new Gson().fromJson(countryJson,SVRAppServiceCustomerCountry.class);
            list_countries=countrys.getCountry();
            list_countries.set(0,new CountrySubclass("",getResources().getString(R.string.pleaseselect)));
            mRegions.addAll(getState(list_countries));
            mRegions.add(0, new CountryRegions("", getResources().getString(R.string.pleaseselect)));
            if(list_countries.size()>1){
                etShippingCountry.setText(list_countries.get(1).getName());
                etShippingCountry.setTag(list_countries.get(1).getCountry_id());
            }
        }
        checkoutActivity. scrollViewBody.scrollTo(0, (int) (getResources().getDimension(R.dimen.scroll_height)*100));
        if("".equals(etShippingState.getText().toString().trim())){
            tvStateAnim.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * when editing address,use this method
     * @param address
     */
    private void initEditDatas(CheckoutDefaultShippingAddress address) {
        tvPhone_otheruse.setHint(address.getAddressId());//set AddressId
        etFirstname.setText(address.getFirstName());
        etLastname.setText(address.getLastName());
        etAddressLine1.setText(address.getStreet().get(0));
        etAddressLine2.setText(address.getStreet().get(1));
        etPostCode.setText(address.getPostcode());
        etPhone.setText(address.getTelephone());
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
    public void initAllHint(){
        if(!etFirstname.getText().toString().equals("")){tvFirstnameAnim.setText(getResources().getString(R.string.first_name));
            tvFirstnameAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if(!etLastname.getText().toString().equals("")){tvLastnameAnim.setText(getResources().getString(R.string.last_name));
            tvLastnameAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if(!etAddressLine1.getText().toString().equals("")){tvAddressLine1Anim.setText(getResources().getString(R.string.address1));
            tvAddressLine1Anim.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if(!etAddressLine2.getText().toString().equals("")){tvAddressLine2Anim.setText(getResources().getString(R.string.address2));
            tvAddressLine2Anim.setTextColor(JToolUtils.getColor(R.color.label_saved));}
//        if(!etPostCode.getText().toString().equals("")){tvPostCode.setText(getResources().getString(R.string.postal_code));
//            tvPostCode.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if(!etShippingCity.getText().toString().equals("")){tvCityAnim.setText(getResources().getString(R.string.city));
            tvCityAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));}
        if(!etPhone.getText().toString().equals("")){tvPhone.setText(getResources().getString(R.string.eg123));
            tvPhone.setTextColor(JToolUtils.getColor(R.color.label_saved));}

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
    /**
     * EditText focus listener
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_checkout_shipping_firstname:
                if (hasFocus) {
                    rl_checkaddadd_firstname.setBottomLineActive(true);
                    if (etFirstname.getText().length()!=0){
                        clearCheckFirst.setVisibility(View.VISIBLE);
                    }else{
                        clearCheckFirst.setVisibility(View.GONE);
                    }
                    tvFirstnameAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvFirstnameAnim.startAnimation(getHintAnimation(tvFirstnameAnim, "First Name"));
                    etFirstname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&etFirstname.isFocused()) {
                                clearCheckFirst.setVisibility(View.VISIBLE);
                            }else {
                                clearCheckFirst.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    rl_checkaddadd_firstname.setBottomLineActive(false);
                    clearCheckFirst.setVisibility(View.GONE);
                    //validate text format
                    if (JDataUtils.isStringBlank(etFirstname.getText())) {
                        tvFirstnameAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvFirstnameAnim.setTextColor(getResources().getColor(R.color.red_common));
                    } else  {
                        tvFirstnameAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_lastname:
                if (hasFocus) {
                    rl_checkaddadd_lastname.setBottomLineActive(true);
                    if (etLastname.getText().length()!=0) {
                        clearCheckLast.setVisibility(View.VISIBLE);
                    }else {
                        clearCheckLast.setVisibility(View.GONE);
                    }
                    tvLastnameAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvLastnameAnim.startAnimation(getHintAnimation(tvLastnameAnim, "Last Name"));
                    etLastname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&etLastname.isFocused()) {
                                clearCheckLast.setVisibility(View.VISIBLE);
                            }else {
                                clearCheckLast.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    rl_checkaddadd_lastname.setBottomLineActive(false);
                    clearCheckLast.setVisibility(View.GONE);
                    if (JDataUtils.isStringBlank(etLastname.getText())) {
                        tvLastnameAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvLastnameAnim.setTextColor(getResources().getColor(R.color.red_common));
                    }else{
                        tvLastnameAnim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_addressline1:
                if (hasFocus) {
                    rl_checkaddadd_address1.setBottomLineActive(true);
                    if (etAddressLine1.getText().length()!=0) {
                        clearCheckAddress1.setVisibility(View.VISIBLE);
                    }else {
                        clearCheckAddress1.setVisibility(View.INVISIBLE);
                    }
                    tvAddressLine1Anim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvAddressLine1Anim.startAnimation(getHintAnimation(tvAddressLine1Anim, "Address 1"));
                    etAddressLine1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&etAddressLine1.isFocused()) {
                                clearCheckAddress1.setVisibility(View.VISIBLE);
                            }else {
                                clearCheckAddress1.setVisibility(View.INVISIBLE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rl_checkaddadd_address1.setBottomLineActive(false);
                    clearCheckAddress1.setVisibility(View.INVISIBLE);
                    if (JDataUtils.isStringBlank(etAddressLine1.getText())) {
                        tvAddressLine1Anim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvAddressLine1Anim.setTextColor(getResources().getColor(R.color.red_common));
                    }else{
                        tvAddressLine1Anim.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_addressline2:
                if (hasFocus) {
                    rl_checkaddadd_address2.setBottomLineActive(true);
                    if (etAddressLine2.getText().length()!=0&&etAddressLine2.isFocused()) {
                        clearCheckAddress2.setVisibility(View.VISIBLE);
                    }else {
                        clearCheckAddress2.setVisibility(View.INVISIBLE);
                    }
                    tvAddressLine2Anim.setVisibility(View.VISIBLE);
                    tvAddressLine2Anim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvAddressLine2Anim.startAnimation(getHintAnimation(tvAddressLine2Anim, "Address 2 (Optional)"));
                    etAddressLine2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    rl_checkaddadd_address2.setBottomLineActive(false);
                    clearCheckAddress2.setVisibility(View.INVISIBLE);
                    tvAddressLine2Anim.setTextColor(getResources().getColor(R.color.label_saved));
                    if("".equals(etAddressLine2.getText().toString().trim())){
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
//                        tvPostCode.setText(getResources().getString(R.string.This_is_a_required_field));
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
                    rl_checkaddadd_city.setBottomLineActive(true);
                    if (etShippingCity.getText().length()!=0) {
                        clearCheckCity.setVisibility(View.VISIBLE);
                    }else {
                        clearCheckCity.setVisibility(View.GONE);
                    }
                    tvCityAnim.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvCityAnim.startAnimation(getHintAnimation(tvCityAnim, "City"));
                    etShippingCity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    rl_checkaddadd_city.setBottomLineActive(false);
                    clearCheckCity.setVisibility(View.GONE);
                    if (JDataUtils.isStringBlank(etShippingCity.getText())) {
                        tvCityAnim.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvCityAnim.setTextColor(getResources().getColor(R.color.red_common));
                    }else{
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
                    rl_checkadd_phone.setBottomLineActive(true);
                    if (etPhone.getText().length()!=0) {
                        clearCheckPhone.setVisibility(View.VISIBLE);
                    }else {
                        clearCheckPhone.setVisibility(View.INVISIBLE);
                    }
                    tvPhone.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvPhone.startAnimation(getHintAnimation(tvPhone, getResources().getString(R.string.eg123)));
                    etPhone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0) {
                                clearCheckPhone.setVisibility(View.VISIBLE);
                            }else {
                                clearCheckPhone.setVisibility(View.INVISIBLE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    rl_checkadd_phone.setBottomLineActive(false);
                    clearCheckPhone.setVisibility(View.INVISIBLE);
                    if (JDataUtils.isStringBlank(etPhone.getText())) {
                        tvPhone.setText(getResources().getString(R.string.This_is_a_required_field));
                        tvPhone.setTextColor(getResources().getColor(R.color.red_common));
                    }else{
                        tvPhone.setTextColor(getResources().getColor(R.color.label_saved));
                    }
                }
                break;
            case R.id.et_checkout_shipping_country:
                if(!hasFocus){
                    tv_country_anim.setTextColor(getResources().getColor(R.color.label_saved));
                }
            default:
                break;
        }
    }

//    private void sendRequestToGetCityAndStateByPostcode(String postcode) {
//        etShippingCity.setEnabled(false);
//        String countryId=etShippingCountry.getTag()==null?"":etShippingCountry.getTag().toString();
//        mAccountDao.getCityAndStateByPostCodet(WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutActivity).getSessionKey(), postcode, countryId);
//
//    }


    public void openState(){
        if(mRegions==null&&getActivity()!=null){
            Toast.makeText(getActivity(),"Please select a country",Toast.LENGTH_SHORT).show();
            return;
        }
        rl_checkout_shipping_state.setBottomLineActive(true);
        ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity entity=   new WheelPickerEntity();
        for(int i=0;i<mRegions.size();i++){
            WheelPickerEntity  bean=new WheelPickerEntity();
            bean.setDisplay(mRegions.get(i).getName());
            bean.setValue(mRegions.get(i).getRegion_id());
            if(String.valueOf(etShippingState.getTag()).equals(bean.getValue())){
                entity.setIndex(i);
            }
            list.add(bean);
        }
        createStatueDialogPicker(list,entity,etShippingState);

    }

    /**
     * TextView and ImageView click listener
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        WheelPickerEntity oldEntity;
        switch (v.getId()) {
            case R.id.arrow_checkout_shipping_select_state:
            case R.id.et_checkout_shipping_state:
                openState();
                break;
            case R.id.arrow_checkout_shipping_select_country:
            case R.id.et_checkout_shipping_country:
                tv_country_anim.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_country_anim.startAnimation(getHintAnimation(tv_country_anim, "Country"));
                etShippingCountry.setFocusable(true);
                etShippingCountry.requestFocus();
                ArrayList<WheelPickerEntity> wheelBeans = new ArrayList<WheelPickerEntity>();
                WheelPickerEntity oldBean=new WheelPickerEntity();
                for (int i=0;i<list_countries.size();i++) {
                    WheelPickerEntity ww = new WheelPickerEntity();
                    ww.setDisplay(list_countries.get(i).getName());
                    ww.setValue(list_countries.get(i).getCountry_id());
                    if(String.valueOf(etShippingCountry.getTag()).equals(list_countries.get(i).getCountry_id())){
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
            default:
                break;
        }
    }

    /**
     * send request to webservice to get country and region datas
     */
    private void sendRequestToGetCountryAndRegions(final String type) {
        mProgressBar.setVisibility(View.VISIBLE);
        mAccountDao.getCountryAndRegions(WhiteLabelApplication.getAppConfiguration().getUserInfo(checkoutActivity).getSessionKey());
    }

    public boolean AllVerifyNotNull(){
        if(verifyNotNull(etFirstname,tvFirstnameAnim)&&verifyNotNull(etLastname,tvLastnameAnim)&&verifyNotNull(etAddressLine1,tvAddressLine1Anim)
                &&verifyNotNull(etPhone, tvPhone) &&verifyNotNull(etShippingCity,tvCityAnim)){
            return true;
        }
        return false;
    }
    public boolean verifyNotNull(EditText view,TextView text){
        if("".equals(view.getText().toString().trim())){
            text.setText(getResources().getString(R.string.This_is_a_required_field));
            text.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
        return true;
    }


    /**
     * create a select dialog
     *
     * @param list      options
     * @param oldEntity
     * @param view      --container of select result
     */
    private void createStatueDialogPicker(ArrayList<WheelPickerEntity> list, final WheelPickerEntity oldEntity, final TextView view) {
        AnimUtil.rotateArrow(arrowSelectState,true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                AnimUtil.rotateArrow(arrowSelectState,false);
                tvStateAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
                rl_checkout_shipping_state.setBottomLineActive(false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue.getDisplay() + "  newValue => " + newValue.getDisplay());
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                AnimUtil.rotateArrow(arrowSelectState,false);
                rl_checkout_shipping_state.setBottomLineActive(false);
                tvStateAnim.setTextColor(JToolUtils.getColor(R.color.label_saved));
                if (newValue == null) {
                    return;
                } else {
                    if (TextUtils.isEmpty(newValue.getDisplay())) {
                        return;
                    }
                    if (getResources().getString(R.string.pleaseselect).equals(newValue.getDisplay())) {
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
    private WheelPickerEntity  mCurrBean;

    private ArrayList<CountryRegions>  mRegions;
    private void createDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {
        rl_checkaddadd_country.setBottomLineActive(true);
        AnimUtil.rotateArrow(arrowSelectCountry,true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                AnimUtil.rotateArrow(arrowSelectCountry,false);
                tv_country_anim.setTextColor(getResources().getColor(R.color.label_saved));
                rl_checkaddadd_country.setBottomLineActive(false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                AnimUtil.rotateArrow(arrowSelectCountry,false);
                tv_country_anim.setTextColor(getResources().getColor(R.color.label_saved));
                rl_checkaddadd_country.setBottomLineActive(false);
                if(newValue==null){
                    System.out.print("oldValue====="+oldValue);
                    view.setText(oldValue.getDisplay());
                    view.setTag(oldValue.getValue());
                    mCurrBean=oldValue;
                }else{
                    if(getResources().getString(R.string.pleaseselect).equals(newValue.getDisplay())){
                        return;
                    }
                    if(TextUtils.isEmpty(newValue.getDisplay())){
                        return;
                    }
                    System.out.print("newValue====="+newValue);
                    view.setText(newValue.getDisplay());
                    view.setTag(newValue.getValue());
                    mCurrBean=newValue;
                }
                if(mRegions!=null){
                    mRegions.clear();
                }
                for(int i=0;i<list_countries.size();i++ ){
                    if(mCurrBean.getValue().equals(list_countries.get(i).getCountry_id())){
                        mRegions.addAll(list_countries.get(i).getRegions());
                    }
                }
                System.out.println("mRegions========================="+mRegions);
                if(mRegions==null||mRegions.size()==0){
                    etShippingState.setVisibility(View.VISIBLE);
                    rl_checkout_shipping_state.setVisibility(View.GONE);
                }else{
                    etShippingState.setVisibility(View.GONE);
                    rl_checkout_shipping_state.setVisibility(View.VISIBLE);
                }


            }
        });
        JViewUtils.showWheelPickerOneDialog(checkoutActivity, configEntity);
    }
    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * init Text animation
     * @param tv
     * @param hintText
     * @return
     */
    private Animation getHintAnimation(final TextView tv, final String hintText) {

        Animation animation = AnimationUtils.loadAnimation(checkoutActivity, R.anim.anim_checkout_hint);
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
                tv.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
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
}

package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.whitelabel.app.R;
import com.google.gson.Gson;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CountryRegions;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.SVRAddAddress;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRGetCityANdStateByPostCodeEntity;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomCheckBox;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Created by imaginato on 2015/6/29.
 */
public class AddAddressActivity extends com.whitelabel.app.BaseActivity implements View.OnFocusChangeListener,View.OnClickListener{
    private EditText firstName,lastName,country,address1,address2,postalcode,city,state,eg;
    private TextView firstNameText;
    private TextView firstNameText2;
    private TextView lastNameText;
    private TextView lastNameText2;
    private TextView countryText;
    private TextView countryText2;
    private TextView address1Text;
    private TextView address1Text2;
    private TextView address2Text;
    private TextView address2Text2;
    private TextView postalcodeText;
    private TextView postalcodeText2;
    private TextView cityText;
    private TextView cityText2;
    private EditText  etDayPhone;
    private TextView stateText;
    private TextView stateText2;
    private TextView egText;
    private TextView egText2;
    private TextView tvDayPhone;
    private TextView tvDayPhone2;
    private ImageView clearAddressFirst,clearAddressLast,clearAddress1,clearAddress2,clearAddressCode,clearAddressCity,clearAddressPhone,clearDayPhone;
    private ImageView iv_country_arrow,iv_state_arrow;
    private CustomButtomLineRelativeLayout rl_addadd_country,rl_addadd_address1,rl_addadd_address2,rl_addadd_postcode,rl_addadd_city,rl_addadd_state;
    private View v_add_phone_line,view_firstname_line,view_lastname_line,vAddDayPhoneLine;
    private ArrayList<CountrySubclass> list_countries = new ArrayList<CountrySubclass>();
    private Handler mHandler = new Handler();
    private InputMethodManager imm ;
    private TextView tvError;
    private CustomCheckBox cbDefaultShipping;
    private CustomCheckBox cbDefaultBilling;
    private ScrollView  mScrollView;
    private Dialog mDialog;
    public  final static   int  RESULT_CODE=1000;
    private final String SESSION_EXPIRED = "session expired,login again please";
    private final int REQUESTCODE_LOGIN = 1000;
    private MyAccountDao dao;
    private int height;
    public final static  String  EXTRA_USE_DEFAULT="use_default";
    private int[] location = new int[2];
    private static class DataHandler extends Handler{
        private final WeakReference<AddAddressActivity> mActivity;
        public DataHandler(AddAddressActivity activity){
            mActivity=new WeakReference<AddAddressActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null){
                return;
            }
            final AddAddressActivity activity=mActivity.get();
            // 点击save会调用 sendRequestToGetCityAndStateByPostCode，以至于 save（）的dialog被getPostCode（）关闭
            if (activity.mDialog != null&&msg.what!=MyAccountDao.REQUEST_CITY_STATE_BYPOSTCODE) {
                activity.mDialog.cancel();
            }
            switch (msg.what){
                case MyAccountDao.REQUEST_CITY_STATE_BYPOSTCODE:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVRGetCityANdStateByPostCodeEntity getCityANdStateByPostCodeEntity = (SVRGetCityANdStateByPostCodeEntity) msg.obj;
                        if (getCityANdStateByPostCodeEntity.getStatus() == 1) {
                            activity.city.setEnabled(true);
                            if (!JDataUtils.isEmpty(getCityANdStateByPostCodeEntity.getCity())) {
                                activity.city.setText(getCityANdStateByPostCodeEntity.getCity());
                                activity.stateText.setText(getCityANdStateByPostCodeEntity.getRegionName());
                                activity.stateText.setTag(getCityANdStateByPostCodeEntity.getRegionId());
                                if(activity.cityText.getVisibility()==View.GONE&&activity.cityText2.getVisibility()!=View.VISIBLE) {
                                    activity.cityText2.setText("City");
                                }else if(activity.cityText2.getVisibility()==View.VISIBLE&&
                                        activity.cityText2.getText().toString().equals(activity.getResources().getString(R.string.required_field))){
                                    //之前是红字的话，改为灰字
                                    activity.cityText2.setText("City");
                                }
                                activity.rl_addadd_city.setBottomLineActive(false);
                                activity.cityText2.setTextColor(JToolUtils.getColor(R.color.label_saved));//设置为灰色
                                if(activity.city.hasFocus()){
                                    activity.city.clearFocus();
                                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(activity.city.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                activity.cityText2.setVisibility(View.VISIBLE);
                                activity.onFocus(activity.postalcode, activity.stateText, activity.stateText2, "State", null);
                                activity.stateText2.setTextColor(activity.getResources().getColor(R.color.hint));
                                activity.stateText.setEnabled(false);
                                activity.stateText.setTextColor(activity.getResources().getColor(R.color.hint));
                                activity.state.setEnabled(false);
                            }
                        }
                    }else{
                        activity.city.setEnabled(true);
                        if (!JDataUtils.errorMsgHandler(activity, msg.obj.toString())) {
                            if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains(activity.SESSION_EXPIRED))) {
                                Intent intent = new Intent();
                                intent.setClass(activity, LoginRegisterActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("expire", true);
                                intent.putExtras(bundle);
                                activity.startActivityForResult(intent, activity.REQUESTCODE_LOGIN);
                            } else {
                                activity.tvError.setText(msg.obj.toString());
                                activity.tvError.setVisibility(View.VISIBLE);
                                activity.mScrollView.scrollTo(0, 5000);
                            }
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_ADDRESS_SAVE:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
//                        SVRAddAddress addAddress = (SVRAddAddress) msg.obj;
                        LinkedList<AddressBook> list = new LinkedList<AddressBook>();
                        String item1= activity.firstName.getText().toString();
                        String item2=activity.lastName.getText().toString();
                        String item3=activity.country.getText().toString();
                        String item4=activity.eg.getText().toString();
                        String item5=activity.address1.getText().toString();
                        String item6=activity.address2.getText().toString();
                        String item7=activity.postalcode.getText().toString();
                        String item8=activity.city.getText().toString();
                        String item9=activity.state.getText().toString();
                        AddressBook ab=new AddressBook();
                        ab.setFirstName(item1);
                        ab.setLastName(item2);
                        ab.setCountry(item3);
                        ab.setTelephone(item4);
                        ab.setPostcode(item7);
                        ab.setCity(item8);
                        list.add(ab);
                        activity.setResult(RESULT_CODE);
                        activity.finish();
                    }else{
                        activity.menuItemClicking=false;
                        if (!activity.checkIsFinished() && !activity.checkIsInvisible()) {
                            if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains(activity.SESSION_EXPIRED))) {
                                Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(activity, LoginRegisterActivity.class);
                                activity.startActivityForResult(intent, 1000);
                                return;
                            }
                        }
                        String errorMsg1 = msg.obj.toString().replace("error", "").replace("701", "").replace("  ", "");
                        activity.tvError.setText(errorMsg1);
                        activity.mScrollView.smoothScrollTo(0, 500);
                    }
                    break;
                case MyAccountDao.REQUEST_GETCOUNTRY_REGIONS:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS) {
                        final SVRAppServiceCustomerCountry countryEntityResult = (SVRAppServiceCustomerCountry) msg.obj;
                        if(countryEntityResult!=null) {
                            activity.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    activity. country.setOnClickListener(activity);
                                    activity.stateText.setOnClickListener(activity);
                                    activity.stateText.setTextColor(activity.getResources().getColor(R.color.hint));
                                    activity. list_countries = countryEntityResult.getCountry();
                                    activity. list_countries.add(0, new CountrySubclass("", activity.getResources().getString(R.string.pleaseselect)));
                                    activity. mRegions.addAll(activity.getState( activity.list_countries));
                                    activity. mRegions.add(0, new CountryRegions("", activity.getResources().getString(R.string.pleaseselect)));
                                    SharedPreferences sharedPreferences = activity.getSharedPreferences("countries", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("countries", new Gson().toJson(countryEntityResult));
                                    editor.commit();
                                }
                            });
                        }
                    }else{
                        if(!JDataUtils.errorMsgHandler(activity,msg.obj.toString())) {
                            Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(activity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    activity.menuItemClicking=false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        DataHandler handler = new DataHandler(this);
        String TAG = this.getClass().getSimpleName();
        dao=new MyAccountDao(TAG, handler);
        initToolBar();
        boolean firstAdd = getIntent().getBooleanExtra(EXTRA_USE_DEFAULT, true);
        if(!firstAdd){
            findViewById(R.id.relative14).setVisibility(View.GONE);
            findViewById(R.id.rl_default_billing).setVisibility(View.GONE);
        }
        v_add_phone_line=findViewById(R.id.v_add_phone_line);
        vAddDayPhoneLine=findViewById(R.id.v_add_day_phone_line);
        view_firstname_line=findViewById(R.id.view_firstname_line);
        view_lastname_line=findViewById(R.id.view_lastname_line);
        rl_addadd_country= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_country);
        rl_addadd_address1= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_address1);
        rl_addadd_address2= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_address2);
        rl_addadd_postcode= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_postcode);
        rl_addadd_city= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_city);
        rl_addadd_state= (CustomButtomLineRelativeLayout) findViewById(R.id.rl_addadd_state);
        TextView tvAddressWord = (TextView) findViewById(R.id.tv_user_address);
        tvAddressWord.setOnClickListener(this);
        tvError=(TextView)findViewById(R.id.tv_error_hint);
        firstName= (EditText) findViewById(R.id.edit_addaddress_firstName);
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    clearAddressFirst.setVisibility(View.VISIBLE);
                }else {
                    clearAddressFirst.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mScrollView=(ScrollView)findViewById(R.id.mScrollView);
        lastName= (EditText)findViewById(R.id.edit_addaddress_lastName);
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    clearAddressLast.setVisibility(View.VISIBLE);
                }else {
                    clearAddressLast.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        country= (EditText)findViewById(R.id.edit_addaddresss_country);
        address1= (EditText)findViewById(R.id.edit_addaddresss_address1);
        address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    clearAddress1.setVisibility(View.VISIBLE);
                }else {
                    clearAddress1.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        address2= (EditText) findViewById(R.id.edit_addaddresss_address2);
        address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    clearAddress2.setVisibility(View.VISIBLE);
                }else {
                    clearAddress2.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        postalcode= (EditText) findViewById(R.id.edit_addaddresss_postalcode);
        postalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    clearAddressCode.setVisibility(View.VISIBLE);
                } else {
                    clearAddressCode.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        postalcode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        city= (EditText)findViewById(R.id.edit_addaddresss_city);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0&&city.isFocused()) {
                    clearAddressCity.setVisibility(View.VISIBLE);
                } else {
                    clearAddressCity.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
         });
           etDayPhone= (EditText) findViewById(R.id.edit_day_phone_eg);
           etDayPhone.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (s.length() != 0) {
                     clearDayPhone.setVisibility(View.VISIBLE);
                 } else {
                     clearDayPhone.setVisibility(View.GONE);
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });

        state= (EditText)findViewById(R.id.edit_addaddresss_state);
        eg= (EditText) findViewById(R.id.edit_addaddresss_eg);
        eg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    clearAddressPhone.setVisibility(View.VISIBLE);
                } else {
                    clearAddressPhone.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        eg.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        etDayPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        address1.setOnFocusChangeListener(this);
        address2.setOnFocusChangeListener(this);
        postalcode.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        country.setOnFocusChangeListener(this);
        state.setOnFocusChangeListener(this);
        eg.setOnFocusChangeListener(this);
        etDayPhone.setOnFocusChangeListener(this);
        firstNameText= (TextView)findViewById(R.id.ctv_addaddress_firstName_text);
        firstNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        firstNameText2= (TextView)findViewById(R.id.ctv_addaddress_firstName_text2);
        lastNameText= (TextView)findViewById(R.id.ctv_addaddress_lastName_text);
        lastNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        lastNameText2= (TextView) findViewById(R.id.ctv_addaddress_lastName_text2);
        countryText= (TextView) findViewById(R.id.ctv_addaddresss_country_text);
        countryText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        countryText2= (TextView) findViewById(R.id.ctv_addaddresss_country_text2);
        address1Text= (TextView) findViewById(R.id.ctv_addaddresss_address1_text1);
        address1Text.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        address1Text2= (TextView) findViewById(R.id.ctv_addaddresss_address1_text2);
        address2Text= (TextView) findViewById(R.id.ctv_addaddresss_address2_text1);
        address2Text.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        address2Text2= (TextView) findViewById(R.id.ctv_addaddresss_address2_text2);
        postalcodeText= (TextView) findViewById(R.id.ctv_addaddresss_postalcode_text);
        postalcodeText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        postalcodeText2= (TextView)findViewById(R.id.ctv_addaddresss_postalcode_text2);
        cityText= (TextView) findViewById(R.id.ctv_addaddresss_city_text1);
        cityText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        cityText2= (TextView) findViewById(R.id.ctv_addaddresss_city_text2);
        stateText= (TextView) findViewById(R.id.ctv_addaddresss_state_text1);
        stateText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        stateText2= (TextView) findViewById(R.id.ctv_addaddresss_state_text2);
        tvDayPhone= (TextView) findViewById(R.id.ctv_day_phone_eg_text);
        tvDayPhone.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        tvDayPhone2= (TextView) findViewById(R.id.ctv_day_phone_eg_text2);
        egText= (TextView) findViewById(R.id.ctv_addaddresss_eg_text);
        egText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        egText2= (TextView) findViewById(R.id.ctv_addaddresss_eg_text2);
        TextView phoneNumber = (TextView) findViewById(R.id.ctv_addaddress_number_value);
        phoneNumber.setOnClickListener(this);
        clearAddressFirst=(ImageView)findViewById(R.id.iv_addaddress_clear_first);
        clearAddressLast=(ImageView)findViewById(R.id.iv_addaddress_clear_last);
        clearAddress1=(ImageView)findViewById(R.id.iv_addaddress_clear_address1);
        clearAddress2=(ImageView)findViewById(R.id.iv_addaddress_clear_address2);
        clearAddressCode=(ImageView)findViewById(R.id.iv_address_clear_code);
        clearAddressCity=(ImageView)findViewById(R.id.iv_address_clear_city);
        clearAddressPhone=(ImageView)findViewById(R.id.iv_address_clear_phone);
        clearDayPhone= (ImageView) findViewById(R.id.iv_day_phone_clear_phone);
        iv_country_arrow=(ImageView)findViewById(R.id.iv_country_arrow);
        iv_state_arrow=(ImageView)findViewById(R.id.iv_state_arrow);
        clearAddressFirst.setOnClickListener(this);
        clearAddressLast.setOnClickListener(this);
        clearAddress1.setOnClickListener(this);
        clearAddress2.setOnClickListener(this);
        clearAddressCode.setOnClickListener(this);
        clearAddressCity.setOnClickListener(this);
        clearAddressPhone.setOnClickListener(this);
        clearDayPhone.setOnClickListener(this);

//        country.setOnClickListener(this);
        imm = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE);
        RelativeLayout scrollView = (RelativeLayout) findViewById(R.id.addaddress_ScrollView);
        scrollView.setOnClickListener(this);
        cbDefaultShipping = (CustomCheckBox) findViewById(R.id.addaddress_checkbox);
        cbDefaultShipping.setColorChecked(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        cbDefaultShipping.setChecked(false);
        cbDefaultBilling= (CustomCheckBox) findViewById(R.id.cb_billing_check);
        cbDefaultBilling.setColorChecked(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        cbDefaultBilling.setChecked(false);
//        country.setTag("MY");
//        country.setText(getResources().getString(R.string.malaysia));
        state.setVisibility(View.GONE);
        stateText.setVisibility(View.VISIBLE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height=dm.heightPixels;
        initData();
        postalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!state.isEnabled()) {
                    state.setEnabled(true);
                    stateText.setEnabled(true);
                    stateText.setTextColor(getResources().getColor(R.color.black000000));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.add_address));
        setLeftMenuIcon(JViewUtils.getNavBarIconDrawable(this,R.drawable.ic_action_back));
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       View view= setRightTextMenuClickListener(
                getMenuInflater(),
                R.menu.menu_save,
                menu,
                R.id.action_save,
                R.layout.menu_item_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveAddressOption();
                    }
                });
        TextView tvMenuSave= (TextView) view.findViewById(R.id.tv_menu_item_save);
        JViewUtils.setNavBarTextColor(tvMenuSave);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                saveAddressOption();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void srollto(){
        JLogUtils.d("firstNameText2", location[1] + " " + height);
        if (height>location[1]){
            mScrollView.scrollTo(0, 0);
        }else{
            mScrollView.scrollTo(0, mScrollView.getHeight());
        }
    }

    private void sendRequestToGetCountryAndRegions() {
        mDialog=JViewUtils.showProgressDialog(AddAddressActivity.this);
        String sessionKey="";
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(this)){
            sessionKey= WhiteLabelApplication.getAppConfiguration().getUserInfo(AddAddressActivity.this).getSessionKey();
        }
        dao.getCountryAndRegions(sessionKey);

    }
    public final  ArrayList<CountryRegions> getState(ArrayList<CountrySubclass>  countrys){
        ArrayList<CountryRegions>  regions=null;
        for(int i=0;i<countrys.size();i++){
            CountrySubclass sub=countrys.get(i);
            if(i==1){
                country.setTag(sub.getCountry_id()+"");
                regions=sub.getRegions();
                country.setText(sub.getName());
            }
        }
        return  regions;
    }

    private ArrayList<CountryRegions>  mRegions=new ArrayList<CountryRegions>();
    private ArrayList<CountrySubclass> countryList;
    private SharedPreferences sharedCountry,sharedStateProvince;
    public void initData(){
            sendRequestToGetCountryAndRegions();
    }

    //stateProvince
    private WheelPickerEntity  mCurrBean;
    private void createDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {
        rl_addadd_country.setBottomLineActive(true);
        AnimUtil.rotateArrow(AddAddressActivity.this,iv_country_arrow,true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                rl_addadd_country.setBottomLineActive(false);
                AnimUtil.rotateArrow(AddAddressActivity.this,iv_country_arrow,false);
            }
            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }
            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                rl_addadd_country.setBottomLineActive(false);
                AnimUtil.rotateArrow(AddAddressActivity.this,iv_country_arrow,false);
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
                if(mRegions==null||mRegions.size()==0){
                    state.setVisibility(View.VISIBLE);
                    stateText.setVisibility(View.GONE);
                }else{
                    state.setVisibility(View.GONE);
                    stateText.setVisibility(View.VISIBLE);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(AddAddressActivity.this, configEntity);
    }
    /**
     * create a select dialog
     *
     * @param list      options
     * @param oldEntity
     * @param view      --container of select result
     */
    private void createStatueDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {
        AnimUtil.rotateArrow(AddAddressActivity.this,iv_state_arrow,true);
        rl_addadd_state.setBottomLineActive(true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                rl_addadd_state.setBottomLineActive(false);
                AnimUtil.rotateArrow(AddAddressActivity.this,iv_state_arrow,false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                rl_addadd_state.setBottomLineActive(false);
                AnimUtil.rotateArrow(AddAddressActivity.this,iv_state_arrow,false);
                if (newValue == null) {
                    return;
                } else {
                    if (TextUtils.isEmpty(newValue.getDisplay())) {
                        return;
                    }
                    if (getResources().getString(R.string.pleaseselect).equals(newValue.getDisplay())) {
                        return;
                    }
                    view.setText(newValue.getDisplay());
                    view.setTag(newValue.getValue());
                    view.setTextColor(getResources().getColor(R.color.black000000));
                    stateText2.setVisibility(View.VISIBLE);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(AddAddressActivity.this, configEntity);
    }
    // && onblurAll(R.id.edit_addaddresss_postalcode)
    private boolean menuItemClicking=false;
    private void saveAddressOption() {
        if(menuItemClicking){
            return;
        }
        JViewUtils.cleanCurrentViewFocus(AddAddressActivity.this);
        if (onblurAll(R.id.edit_addaddress_firstName) && onblurAll(R.id.edit_addaddress_lastName)  && onblurAll(R.id.edit_addaddresss_country)
                && onblurAll(R.id.edit_addaddresss_address1)
                && onblurAll(R.id.edit_addaddresss_city)  && onblurAll(R.id.edit_addaddresss_eg)&&onblurAll(R.id.edit_day_phone_eg)) {

            mDialog=JViewUtils.showProgressDialog(AddAddressActivity.this);
            String region="";
            String region_id="";
            if(state.getVisibility()== View.GONE){
                if( !TextUtils.isEmpty(String.valueOf(stateText.getText()))){
                    region=stateText.getText().toString().trim();
                }
                if( stateText.getTag()!=null){
                    region_id= String.valueOf(stateText.getTag());
                }
            }else{
                if( !TextUtils.isEmpty(String.valueOf(state.getText()))){
                    region=state.getText().toString().trim();
                }
            }
            String sessionKey="";
            if(WhiteLabelApplication.getAppConfiguration().isSignIn(AddAddressActivity.this)){
                 sessionKey= WhiteLabelApplication.getAppConfiguration().getUserInfo(AddAddressActivity.this).getSessionKey();
            }
            String firstname =firstName.getText().toString().trim();
            String lastname=lastName.getText().toString().trim();
            String country_id = String.valueOf(country.getTag());
            String telephone=eg.getText().toString().trim();
            String street0=address1.getText().toString().trim();
            String street1=address2.getText().toString().trim();
            String postcode=postalcode.getText().toString().trim();
            String city2= city.getText().toString().trim();
            String defaultShipping= cbDefaultShipping.isChecked()?"1":"0";
            String defaultBilling=cbDefaultBilling.isChecked()?"1":"0";
            String fax=etDayPhone.getText().toString();
            menuItemClicking=true;
            dao.addressSave(sessionKey,firstname,lastname,country_id,telephone,street0,street1,postcode,city2,region,region_id,defaultShipping,defaultBilling,fax);
        }
    }
    public void clickState(){
            if(mRegions==null){
                Toast.makeText(AddAddressActivity.this,"Please select a country",Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
            WheelPickerEntity entity=   new WheelPickerEntity();

            String stateId=String.valueOf(stateText.getTag());
            int selectIndex=0;
            for(int i=0;i<mRegions.size();i++){
                WheelPickerEntity  bean=new WheelPickerEntity();
                if(mRegions.get(i).getRegion_id().equals(stateId)){
                    selectIndex=i;
                }
                bean.setDisplay(mRegions.get(i).getName());
                bean.setValue(mRegions.get(i).getRegion_id());
                list.add(bean);
            }
             entity.setIndex(selectIndex);
            createStatueDialogPicker(list, entity, stateText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_user_address:
                cbDefaultShipping.setChecked(!cbDefaultShipping.isChecked(),true);
                break;
            case R.id.addaddress_ScrollView:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.edit_addaddresss_country:
//                wheelPickerCountry();
                ArrayList<WheelPickerEntity> wheelBeans = new ArrayList<WheelPickerEntity>();
                WheelPickerEntity oldBean=new WheelPickerEntity();
                for (int i=0;i<list_countries.size();i++) {
                    WheelPickerEntity ww = new WheelPickerEntity();
                    ww.setDisplay(list_countries.get(i).getName());
                    ww.setValue(list_countries.get(i).getCountry_id());
                    if(String.valueOf(country.getTag()).equals(list_countries.get(i).getCountry_id())){
                        oldBean.setIndex(i);
                    }
                    wheelBeans.add(ww);
                }
                createDialogPicker(wheelBeans, oldBean, country);
                onFocus(country, countryText, countryText2, "Country", rl_addadd_country);
                break;
            case R.id.ctv_addaddresss_state_text1:
                clickState();
                JViewUtils.cleanCurrentViewFocus(AddAddressActivity.this);
                state.setFocusable(true);
                state.requestFocus();
                break;
            case R.id.edit_addaddresss_state:
//               clickState();
//                imm.hideSoftInputFromWindow(state.getWindowToken(), 0);
//                state.setInputType(InputType.TYPE_NULL);
                break;
            case R.id.ctv_phone_number_value:
                // wheelPickerPhoneNumber();
                break;
            case R.id.iv_addaddress_clear_first:
                firstName.setText("");
                break;
            case R.id.iv_addaddress_clear_last:
                lastName.setText("");
                break;
            case R.id.iv_addaddress_clear_address1:
                address1.setText("");
                break;
            case R.id.iv_addaddress_clear_address2:
                address2.setText("");
                break;
            case R.id.iv_address_clear_city:
                city.setText("");
                break;
            case R.id.iv_address_clear_code:
                postalcode.setText("");
                break;
            case R.id.iv_address_clear_phone:
                eg.setText("");
                break;
            case R.id.iv_day_phone_clear_phone:
                etDayPhone.setText("");
                break;
        }
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.edit_addaddresss_city:
                    onFocus(city, cityText, cityText2, "City", rl_addadd_city);
                    if (city.getText().length()!=0) {
                        clearAddressCity.setVisibility(View.VISIBLE);
                    }else {
                        clearAddressCity.setVisibility(View.GONE);
                    }
                    break;
                case R.id.edit_addaddress_firstName:
                    onFocus(firstName, firstNameText, firstNameText2, "First Name",null);
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line,true);
                        if (firstName.getText().length()!=0) {
                            clearAddressFirst.setVisibility(View.VISIBLE);
                        }else {
                            clearAddressFirst.setVisibility(View.GONE);
                        }
                    break;
                case R.id.edit_addaddress_lastName:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line,true);
                    onFocus(lastName, lastNameText, lastNameText2, "Last Name",null);
                    if (lastName.getText().length()!=0)
                        clearAddressLast.setVisibility(View.VISIBLE);
                    else
                        clearAddressLast.setVisibility(View.GONE);
                    break;
                case R.id.edit_addaddresss_country:
                    onFocus(country, countryText, countryText2, "Country",rl_addadd_country);
                    ArrayList<WheelPickerEntity> wheelBeans = new ArrayList<WheelPickerEntity>();
                    WheelPickerEntity oldBean=new WheelPickerEntity();
                    for (int i=0;i<list_countries.size();i++) {
                        WheelPickerEntity ww = new WheelPickerEntity();
                        ww.setDisplay(list_countries.get(i).getName());
                        ww.setValue(list_countries.get(i).getCountry_id());
                        if(String.valueOf(country.getTag()).equals(list_countries.get(i).getCountry_id())){
                            oldBean.setIndex(i);
                        }
                        wheelBeans.add(ww);
                    }
                    createDialogPicker(wheelBeans, oldBean, country);
//                    wheelPickerCountry();
                    break;
                case R.id.edit_addaddresss_address1:
                    onFocus(address1,address1Text,address1Text2,getResources().getString(R.string.address1),rl_addadd_address1);
                    if (address1.getText().length()!=0) {
                        clearAddress1.setVisibility(View.VISIBLE);
                    }else {
                        clearAddress1.setVisibility(View.GONE);
                    }
                    break;
                case R.id.edit_addaddresss_address2:
                    onFocus(address2,address2Text,address2Text2,getResources().getString(R.string.address2),rl_addadd_address2);
                    if (address2.getText().length()!=0) {
                        clearAddress2.setVisibility(View.VISIBLE);
                    }else {
                        clearAddress2.setVisibility(View.GONE);
                    }
                    break;
//                case R.id.edit_addaddresss_postalcode:
//                    onFocus(postalcode, postalcodeText, postalcodeText2, "Postal Code",rl_addadd_postcode);
//                    if (postalcode.getText().length()!=0) {
//                        clearAddressCode.setVisibility(View.VISIBLE);
//                    }else {
//                        clearAddressCode.setVisibility(View.GONE);
//                    }
//                    break;
                case R.id.edit_addaddresss_state:
                    onFocus(state, stateText, stateText2, "State",rl_addadd_state);
                    clickState();
//                    CharSequence tvcountry = country.getText();
//                    if (tvcountry == null || JDataUtils.isEmpty(tvcountry.toString())) {
//                        tvcountry = "Malaysia";
//                    }
                    break;
                case R.id.edit_addaddresss_eg:
                    onFocus(eg,egText,egText2,getResources().getString(R.string.eg123),null);
                    CustomButtomLineRelativeLayout.setBottomLineActive(v_add_phone_line, true);
                    if (eg.getText().length()!=0)
                        clearAddressPhone.setVisibility(View.VISIBLE);
                    else
                        clearAddressPhone.setVisibility(View.GONE);
                    break;
                case R.id.edit_day_phone_eg:
                    onFocus(etDayPhone,tvDayPhone,tvDayPhone2,getResources().getString(R.string.address_day_phone),null);
                    CustomButtomLineRelativeLayout.setBottomLineActive(vAddDayPhoneLine, true);
                    if (etDayPhone.getText().length()!=0)
                        clearDayPhone.setVisibility(View.VISIBLE);
                    else
                        clearDayPhone.setVisibility(View.GONE);
                    break;
            }
        }else{
            onblurAll(v.getId());
            if (postalcode.getText()!=null&&postalcode.getText().length()!=0)
                cityText2.setVisibility(View.VISIBLE);
            clearAddressLast.setVisibility(View.GONE);
            clearAddressFirst.setVisibility(View.GONE);
            clearAddress1.setVisibility(View.GONE);
            clearAddress2.setVisibility(View.GONE);
            clearAddressCode.setVisibility(View.GONE);
            clearAddressCity.setVisibility(View.GONE);
            clearAddressPhone.setVisibility(View.GONE);
            clearDayPhone.setVisibility(View.GONE);
        }

    }
    public boolean onblurAll(int id){
        switch (id){
            case R.id.edit_addaddress_firstName:

                CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line,false);
                firstNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                firstNameText2.setVisibility(View.VISIBLE);
                if(firstName.getText().toString().trim().equals("")){
                    firstName.setHint(getResources().getString(R.string.first_name));

                    firstNameText2.getLocationOnScreen(location);
                    srollto();

                    firstNameText.clearAnimation();
                    //验证字段
                    firstNameText2.setText(getResources().getString(R.string.required_field));
                    firstNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    firstNameText.clearAnimation();
                }
                break;
            case R.id.edit_addaddress_lastName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line,false);
                lastNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                lastNameText2.setVisibility(View.VISIBLE);
                if(lastName.getText().toString().trim().equals("")){
                    lastName.setHint("Last Name");

                    lastNameText2.getLocationOnScreen(location);
                    srollto();

                    lastNameText.clearAnimation();
                    //验证字段
                    lastNameText2.setText(getResources().getString(R.string.required_field));
                    lastNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    lastNameText.clearAnimation();
                }
                break;
            case R.id.edit_addaddresss_country:
                rl_addadd_country.setBottomLineActive(false);
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                countryText2.setVisibility(View.VISIBLE);
                if(country.getText().toString().trim().equals("")){
                    country.setHint("Country");

                    countryText2.getLocationOnScreen(location);
                    srollto();

                    countryText.clearAnimation();
                    //验证字段
                    countryText2.setText(getResources().getString(R.string.required_field));
                    countryText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    countryText.clearAnimation();
                }
                break;
            case R.id.edit_addaddresss_address1:
                rl_addadd_address1.setBottomLineActive(false);
                address1Text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                address1Text2.setVisibility(View.VISIBLE);
                if(address1.getText().toString().trim().equals("")){
                    address1.setHint(getResources().getString(R.string.address1));

                    address1Text2.getLocationOnScreen(location);
                    srollto();

                    address1Text.clearAnimation();
                    //验证字段
                    address1Text2.setText(getResources().getString(R.string.required_field));
                    address1Text2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    address1Text.clearAnimation();

                }
                break;
            case R.id.edit_addaddresss_address2:
                rl_addadd_address2.setBottomLineActive(false);
                address2Text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                address2Text2.setVisibility(View.VISIBLE);
                if(address2.getText().toString().trim().equals("")){
                    address2.setHint(getResources().getString(R.string.address2));
                    address2Text.clearAnimation();
                    address2Text2.setVisibility(View.INVISIBLE);
                    return false;
                }else{
                    address2Text.clearAnimation();
                }
                break;
            case R.id.edit_addaddresss_postalcode:
                rl_addadd_postcode.setBottomLineActive(false);
                postalcodeText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                postalcodeText2.setVisibility(View.VISIBLE);
                if(postalcode!=null&&postalcode.getText().toString().trim().equals("")){
                    postalcode.setHint("Postal Code");

                    postalcodeText2.getLocationOnScreen(location);
                    srollto();

                    postalcodeText.clearAnimation();
                    //验证字段
                    postalcodeText2.setText(getResources().getString(R.string.This_is_a_required_field));
                    postalcodeText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else if(postalcode!=null&&postalcode.getText().toString().trim().length()<4){
                    postalcodeText2.getLocationOnScreen(location);
                    srollto();
                    postalcodeText.clearAnimation();
                    //验证字段
                    postalcodeText2.setText(getResources().getString(R.string.blur_postalcode));
                    postalcodeText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    postalcodeText.clearAnimation();
                    /**
                     * send request to get city and state by postcode.
                     * russell
                     */
//                    sendRequestToGetCityAndStateByPostCode(postalcode.getText().toString().trim());
                }
                break;
            case R.id.edit_addaddresss_city:
                rl_addadd_city.setBottomLineActive(false);
                cityText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                cityText2.setVisibility(View.VISIBLE);
                if(city.getText().toString().trim().equals("")){
                    city.setHint("City");
                    cityText2.getLocationOnScreen(location);
                    srollto();
                    cityText.clearAnimation();
                    //验证字段
                    cityText2.setText(getResources().getString(R.string.required_field));
                    cityText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    cityText.clearAnimation();
                }
                break;
            case R.id.edit_addaddresss_state:
                rl_addadd_state.setBottomLineActive(false);
                stateText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                stateText2.setVisibility(View.VISIBLE);
                if(state.getText().toString().trim().equals("")){

                    stateText2.getLocationOnScreen(location);
                    srollto();
                    stateText.clearAnimation();
                    return false;
                }else{
                    stateText.clearAnimation();
                }
                break;
            case R.id.edit_addaddresss_eg:
                CustomButtomLineRelativeLayout.setBottomLineActive(v_add_phone_line,false);
                egText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                egText2.setVisibility(View.VISIBLE);
                if(eg.getText().toString().trim().equals("")){
                    eg.setHint(getResources().getString(R.string.eg123));

                    egText2.getLocationOnScreen(location);
                    srollto();
                    egText.clearAnimation();
                    //验证字段
                    egText2.setText(getResources().getString(R.string.required_field));
                    egText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    egText.clearAnimation();
                }
                break;
            case R.id.edit_day_phone_eg:
                CustomButtomLineRelativeLayout.setBottomLineActive(vAddDayPhoneLine,false);
                tvDayPhone2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                tvDayPhone2.setVisibility(View.VISIBLE);
                if(etDayPhone.getText().toString().trim().equals("")){
                    etDayPhone.setHint(getResources().getString(R.string.address_day_phone));
                    tvDayPhone2.getLocationOnScreen(location);
                    srollto();
                    tvDayPhone.clearAnimation();
                    //验证字段
                    tvDayPhone2.setText(getResources().getString(R.string.required_field));
                    tvDayPhone2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    tvDayPhone.clearAnimation();
                }
                break;
        }
        return true;
    }

//    private void sendRequestToGetCityAndStateByPostCode(String postcode) {
//        city.setEnabled(false);
//        String session_key="";
//        if(WhiteLabelApplication.getAppConfiguration().isSignIn(AddAddressActivity.this)){
//            session_key= WhiteLabelApplication.getAppConfiguration().getUserInfo(AddAddressActivity.this).getSessionKey();
//        }
//        String country_id=country.getTag() == null ? "" : country.getTag().toString();
//        dao.getCityAndStateByPostCodet(session_key,postcode,country_id);
//    }

    public void onFocus(EditText edit,TextView text,TextView text2,String hint,CustomButtomLineRelativeLayout relativeLayout){
        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);
        //上移高度应该为自身的高度
        int textHeight=text.getHeight();
        Animation tran;
        if(textHeight>0){
            tran = new TranslateAnimation(0, 0, 0, 0-textHeight);
        }else {
            tran = new TranslateAnimation(0, 0, 0, -50);
        }
        //平移
        tran.setDuration(300);
        //渐变
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        set.addAnimation(tran);
        set.addAnimation(alpha);
        text2.setVisibility(View.VISIBLE);
        text2.setText(hint);
        if(edit.getText().toString().trim().equals("")) {
            text2.setVisibility(View.INVISIBLE);
            edit.setHint("");
            text.startAnimation(set);
        }else{
            text2.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        }
        if(relativeLayout!=null) {
            relativeLayout.setBottomLineActive(true);
        }
    }

}
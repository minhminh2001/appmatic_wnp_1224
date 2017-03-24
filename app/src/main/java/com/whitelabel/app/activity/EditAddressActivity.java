package com.whitelabel.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.google.gson.Gson;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CountryRegions;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVREditAddress;
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
public class EditAddressActivity extends com.whitelabel.app.BaseActivity implements OnFocusChangeListener,View.OnClickListener{
    private EditText firstName,lastName,country,address1,address2,postalcode,city,state,eg;
    private TextView firstNameText,firstNameText2,lastNameText,lastNameText2,countryText
            ,countryText2,address1Text,address1Text2,address2Text,address2Text2,postalcodeText,postalcodeText2,
            cityText,cityText2,stateText,stateText2,egText,egText2,phoneNumber;
    private CustomButtomLineRelativeLayout rl_edit_country,rl_edit_address1,rl_edit_address2,rl_edit_postcode,rl_edit_city,rl_edit_state;
    private ArrayList<CountrySubclass> list_countries = new ArrayList<CountrySubclass>();
    private View view_firstname_line,view_lastname_line,v_phone_line;
    private ImageView iv_country_arrow,iv_state_arrow;
    private ImageView ivClearEditFirst,ivClearEditLast,ivClearEditAddress1,ivClearEditAddress2,ivClearEditCode,ivClearEditCity,ivClearEditPhone;
//    public static String SESSION_KEY = null;
    private Handler mHandler = new Handler();
//    private String addeessId,firtname,lastname,county,address11,address22,postalcode1,city1,state1,phonenumber;
    private CustomCheckBox addaddress_checkbox;
    private AddressBook  mBean;
    private View checkBoxView;
    private TextView tvError;
//    private ProgressBar mProgressBar;
    private Dialog  mDialog;
    private ScrollView myScrollView;

    private final String SESSION_EXPIRED = "session expired,login again please";
    private final int REQUESTCODE_LOGIN = 1000;
    private MyAccountDao dao;
    private String TAG;
    private int index;


    private static final class DataHandler extends  Handler{
        private final WeakReference<EditAddressActivity> mActivity;
        public DataHandler(EditAddressActivity activity){
            mActivity=new WeakReference<EditAddressActivity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null){
                return;
            }
           final EditAddressActivity activity=mActivity.get();
            // 点击save会调用 sendRequestToGetCityAndStateByPostCode，以至于 save（）的dialog被getPostCode（）关闭
            if (activity.mDialog != null&&msg.what!=MyAccountDao.REQUEST_CITY_STATE_BYPOSTCODE) {
                activity. mDialog.cancel();
            }
            switch (msg.what){
                case MyAccountDao.REQUEST_GETCOUNTRY_REGIONS:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS) {
                        final SVRAppServiceCustomerCountry countryEntityResult = (SVRAppServiceCustomerCountry) msg.obj;
                        activity.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(activity.mDialog!=null){
                                    activity.mDialog.cancel();
                                }
                                activity.country.setOnClickListener(activity);
                                activity.stateText.setOnClickListener(activity);

                                activity.list_countries = countryEntityResult.getCountry();
                                activity. list_countries.add(0, new CountrySubclass("", activity.getResources().getString(R.string.pleaseselect)));
                                ArrayList<CountryRegions> regisons=activity.getState(activity.mBean.getCountryId(), activity.list_countries);
                                if(regisons==null||regisons.size()==0){
                                    activity.stateText.setVisibility(View.GONE);
                                    activity.state.setVisibility(View.VISIBLE);
                                }else{
                                    activity. stateText.setVisibility(View.VISIBLE);
                                    activity. state.setVisibility(View.GONE);
                                }
                                activity.mRegions.addAll(regisons);
                                activity. mRegions.add(0, new CountryRegions("", activity.getResources().getString(R.string.pleaseselect)));
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("countries", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("countries", new Gson().toJson(countryEntityResult));
                                editor.commit();
                            }
                        });
                    }else{
                        if (!JDataUtils.errorMsgHandler(activity, msg.obj.toString())) {
                            Toast.makeText(activity,msg.obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_CITY_STATE_BYPOSTCODE:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVRGetCityANdStateByPostCodeEntity getCityANdStateByPostCodeEntity = (SVRGetCityANdStateByPostCodeEntity) msg.obj;
                        if (getCityANdStateByPostCodeEntity.getStatus() == 1) {
                            activity. city.setEnabled(true);
                            if (!JDataUtils.isEmpty(getCityANdStateByPostCodeEntity.getCity())) {
                                activity.cityText2.setTextColor(JToolUtils.getColor(R.color.label_saved));//设置为灰色
                                activity.cityText2.setVisibility(View.VISIBLE);
                                activity.rl_edit_city.setBottomLineActive(false);
                                if(activity.city.hasFocus()){
                                    activity.city.clearFocus();
                                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(activity.city.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }

                                activity.city.setText(getCityANdStateByPostCodeEntity.getCity());
                                activity.stateText.setText(getCityANdStateByPostCodeEntity.getRegionName());
                                activity.stateText.setTag(getCityANdStateByPostCodeEntity.getRegionId());
                                activity.state.setEnabled(false);
//
                                activity.stateText.setTextColor(activity.getResources().getColor(R.color.hint));
                                activity.stateText.setEnabled(false);
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

                                activity. tvError.setText(msg.obj.toString());
                                activity.tvError.setVisibility(View.VISIBLE);
                                activity. myScrollView.scrollTo(0, 5000);
                            }
                        }

                    }
                    break;
                case MyAccountDao.REQUEST_EDIT_SAVE:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVREditAddress addAddress = (SVREditAddress) msg.obj;
                        JLogUtils.i("Zero", "  Success =" + addAddress.getStatus());
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
                        activity.setResult(1000);
                        activity.finish();
                    }else{
                        if (!JDataUtils.errorMsgHandler(activity, msg.obj.toString())) {
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
                            activity. myScrollView.scrollTo(0, 5000);
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(activity);
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private DataHandler dataHandler;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaddress);
        TAG =this.getClass().getSimpleName();
        dataHandler=new DataHandler(this);
        dao=new MyAccountDao(TAG,dataHandler);
//        SharedPreferences sharedPreferences = getSharedPreferences("session_key", Activity.MODE_PRIVATE);
        checkBoxView=findViewById(R.id.relative14);
        addaddress_checkbox= (CustomCheckBox) findViewById(R.id.addaddress_checkbox);
        addaddress_checkbox.setChecked(false);
        myScrollView= (ScrollView) findViewById(R.id.myScrollView);
//        SESSION_KEY = sharedPreferences.getString("session_key", "");
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        index=intent.getIntExtra("position",0);
        if(bundle!=null ){
            mBean=(AddressBook)bundle.getSerializable("bean");
            if (mDialog != null) {
                mDialog.cancel();
            }
        }
        if("1".equals(mBean.getPrimaryShipping())){
            checkBoxView.setVisibility(View.GONE);
            addaddress_checkbox.setChecked(true);
        }else{
            checkBoxView.setVisibility(View.VISIBLE);

            addaddress_checkbox.setChecked(false);
        }

        firstName= (EditText) findViewById(R.id.edit_firstName_EditText);
        firstName.setText(mBean.getFirstName());
        rl_edit_country=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_country);
        rl_edit_address1=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_address1);
        rl_edit_address2=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_address2);
        rl_edit_postcode=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_postcode);
        rl_edit_city=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_city);
        rl_edit_state=(CustomButtomLineRelativeLayout)findViewById(R.id.rl_edit_state);
        lastName= (EditText)findViewById(R.id.edit_lastName_EditText);
        lastName.setText(mBean.getLastName());
        country= (EditText)findViewById(R.id.edit_country_EditText);
        country.setText(mBean.getCountry());
        country.setTag(mBean.getCountryId());
        view_firstname_line=findViewById(R.id.view_firstname_line);
        view_lastname_line=findViewById(R.id.view_lastname_line);
        v_phone_line=findViewById(R.id.v_edit_phone_line);
        tvError=(TextView)findViewById(R.id.tv_error_hint);
        address1= (EditText)findViewById(R.id.edit_address1_EditText);
        address1.setText(mBean.getStreet().get(0));
        address2= (EditText) findViewById(R.id.edit_address2_EditText);
        address2.setText(mBean.getStreet().get(1));
        postalcode= (EditText) findViewById(R.id.edit_postalcode_EditText);
        postalcode.setText(mBean.getPostcode());
        stateText= (TextView) findViewById(R.id.edit_state_text1);
        city= (EditText)findViewById(R.id.edit_city_EditText);
        city.setText(mBean.getCity());
        state= (EditText)findViewById(R.id.edit_state_EditText);
        eg= (EditText) findViewById(R.id.edit_eg);
        eg.setText(mBean.getTelephone());
        eg.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        country.setOnFocusChangeListener(this);
        address1.setOnFocusChangeListener(this);
        address2.setOnFocusChangeListener(this);
        postalcode.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        state.setOnFocusChangeListener(this);
        eg.setOnFocusChangeListener(this);
        firstNameText= (TextView)findViewById(R.id.edit_firstName_text);
        firstNameText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        firstNameText2= (TextView)findViewById(R.id.edit_firstName_text2);
        lastNameText= (TextView)findViewById(R.id.edit_lastName_text);
        lastNameText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        lastNameText2= (TextView) findViewById(R.id.edit_lastName_text2);
        countryText= (TextView) findViewById(R.id.edit_country_text);
        countryText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        countryText2= (TextView) findViewById(R.id.edit_country_text2);
        address1Text= (TextView) findViewById(R.id.edit_address1_text1);
        address1Text.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        address1Text2= (TextView) findViewById(R.id.edit_address1_text2);
        address2Text= (TextView) findViewById(R.id.edit_address2_text1);
        address1Text.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        address2Text2= (TextView) findViewById(R.id.edit_address2_text2);
        postalcodeText= (TextView) findViewById(R.id.edit_postalcode_text);
        postalcodeText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        postalcodeText2= (TextView)findViewById(R.id.edit_postalcode_text2);
        cityText= (TextView) findViewById(R.id.edit_city_text1);
        cityText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        cityText2= (TextView) findViewById(R.id.edit_city_text2);

        stateText2= (TextView) findViewById(R.id.edit_state_text2);
        egText= (TextView) findViewById(R.id.ctv_eg_text);
        egText.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        egText2= (TextView) findViewById(R.id.ctv_eg_text2);
        phoneNumber= (TextView) findViewById(R.id.ctv_phone_number_value);
        phoneNumber.setOnClickListener(this);

        iv_country_arrow=(ImageView)findViewById(R.id.iv_country_arrow);
        iv_state_arrow=(ImageView)findViewById(R.id.iv_state_arrow);

        ivClearEditFirst=(ImageView)findViewById(R.id.iv_clear_first);
        ivClearEditLast=(ImageView)findViewById(R.id.iv_clear_last);
        ivClearEditAddress1=(ImageView)findViewById(R.id.iv_clear_address1);
        ivClearEditAddress2=(ImageView)findViewById(R.id.iv_clear_address2);
        ivClearEditCode=(ImageView)findViewById(R.id.iv_clear_code);
        ivClearEditCity=(ImageView)findViewById(R.id.ic_clear_city);
        ivClearEditPhone=(ImageView)findViewById(R.id.ic_clear_phone);

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
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    ivClearEditFirst.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditFirst.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    ivClearEditLast.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditLast.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    ivClearEditAddress1.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditAddress1.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    ivClearEditAddress2.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditAddress2.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        postalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    ivClearEditCode.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0&&city.isFocused()) {
                    ivClearEditCity.setVisibility(View.VISIBLE);
                }else {
                    ivClearEditCity.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        eg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0)
                    ivClearEditPhone.setVisibility(View.VISIBLE);
                else
                    ivClearEditPhone.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivClearEditFirst.setOnClickListener(this);
        ivClearEditLast.setOnClickListener(this);
        ivClearEditAddress1.setOnClickListener(this);
        ivClearEditAddress2.setOnClickListener(this);
        ivClearEditCode.setOnClickListener(this);
        ivClearEditCity.setOnClickListener(this);
        ivClearEditPhone.setOnClickListener(this);

        country.setOnClickListener(this);
        state.setOnClickListener(this);
        
        if(!TextUtils.isEmpty(mBean.getRegion())){
            stateText2.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(mBean.getRegionId())){
            stateText.setTag(mBean.getRegionId());
            stateText.setOnClickListener(this);
            stateText.setText(mBean.getRegion());

            DisplayMetrics dm = getResources().getDisplayMetrics();
            float value  = dm.scaledDensity;
            stateText.setTextSize(state.getTextSize()/value);
        }
        if(!TextUtils.isEmpty(mBean.getRegion())){
            state.setEnabled(false);
            stateText.setEnabled(false);
            stateText.setTextColor(getResources().getColor(R.color.hint));
            state.setText(mBean.getRegion());
        }
        initAllHint();
        initData();
        initToolBar();
    }
    private void initToolBar() {
        setTitle(getResources().getString(R.string.edit_address));
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
    public void initAllHint(){
        if(!firstName.getText().toString().equals("")){firstNameText2.setVisibility(View.VISIBLE);}
        if(!lastName.getText().toString().equals("")){lastNameText2.setVisibility(View.VISIBLE);}
        if(!country.getText().toString().equals("")){countryText2.setVisibility(View.VISIBLE);}
        if(!address1.getText().toString().equals("")){address1Text2.setVisibility(View.VISIBLE);}
        if(!address2.getText().toString().equals("")){address2Text2.setVisibility(View.VISIBLE);}
        if(!postalcode.getText().toString().equals("")){postalcodeText2.setVisibility(View.VISIBLE);}
        if(!city.getText().toString().equals("")){cityText2.setVisibility(View.VISIBLE);}
        if (!state.getText().toString().equals("")){stateText2.setVisibility(View.VISIBLE);}
        if (!eg.getText().toString().equals("")){egText2.setVisibility(View.VISIBLE);}
    }

    private ArrayList<CountrySubclass> countryList;
    private SharedPreferences sharedCountry,sharedStateProvince;
    private ArrayList<CountryRegions>  mRegions=new ArrayList<CountryRegions>();

    public void initData(){
        sendRequestToGetCountryAndRegions();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setRightTextMenuClickListener(
                getMenuInflater(),
                R.menu.menu_save,
                menu,
                R.id.action_save,
                R.layout.menu_item_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickSave();
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                clickSave();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void clickSave(){
        JViewUtils.cleanCurrentViewFocus(EditAddressActivity.this);
        if (onblurAll(R.id.edit_firstName_EditText) && onblurAll(R.id.edit_lastName_EditText)  && onblurAll(R.id.edit_address1_EditText)
                && onblurAll(R.id.edit_postalcode_EditText)
                && onblurAll(R.id.edit_city_EditText) && onblurAll(R.id.edit_country_EditText) && onblurAll(R.id.edit_state_EditText) && onblurAll(R.id.edit_eg)) {

            mDialog=JViewUtils.showProgressDialog(EditAddressActivity.this);
            String region="";
            String region_id="";
            if(state.getVisibility()==View.GONE){
                if( !TextUtils.isEmpty(String.valueOf(stateText.getText()))){
                    //   parameters.put("region",stateText.getText().toString().trim());
                    region=stateText.getText().toString().trim();
                }
                if( stateText.getTag()!=null){
                    //      parameters.put("region_id", String.valueOf(stateText.getTag()));
                    region_id= String.valueOf(stateText.getTag());
                }
            }else{
                if( !TextUtils.isEmpty(String.valueOf(state.getText()))){
                    //    parameters.put("region",state.getText().toString().trim());
                    region=state.getText().toString().trim();
                }
            }

            String address_id=mBean.getAddressId();
            String sessionKey=GemfiveApplication.getAppConfiguration().getUserInfo(EditAddressActivity.this).getSessionKey();
            String firstname =firstName.getText().toString().trim();
            String lastname=lastName.getText().toString().trim();
            String country_id = String.valueOf(country.getTag());
            String telephone=eg.getText().toString().trim();
            String street0=address1.getText().toString().trim();
            String street1=address2.getText().toString().trim();
            String postcode=postalcode.getText().toString().trim();
            String city2= city.getText().toString().trim();
            int addAddressCode=addaddress_checkbox.isChecked()?1:0;
            String default_shipping=""+addAddressCode;
            dao.EditSave(address_id,sessionKey, firstname, lastname, country_id, telephone, street0, street1, postcode, city2, region, region_id, default_shipping);

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_country_EditText:
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
                onFocus(country, countryText, countryText2, "Country",rl_edit_country);
                break;
            case R.id.ctv_phone_number_value:
              //  wheelPickerPhoneNumber();
                break;
            case R.id.edit_state_text1:
                clickState();
                JViewUtils.cleanCurrentViewFocus(EditAddressActivity.this);
                state.setFocusable(true);
                state.requestFocus();
                break;
            case R.id.edit_state_EditText:
//                onFocus(state,stateText,stateText2,"State");
//                clickState();
                break;
            case R.id.iv_clear_first:
                firstName.setText("");
                break;
            case R.id.iv_clear_last:
                lastName.setText("");
                break;
            case R.id.iv_clear_address1:
                address1.setText("");
                break;
            case R.id.iv_clear_address2:
                address2.setText("");
                break;
            case R.id.iv_clear_code:
                postalcode.setText("");
                break;
            case R.id.ic_clear_city:
                city.setText("");
                break;
            case R.id.ic_clear_phone:
                eg.setText("");
                break;
        }
    }
    public void clickState(){
            if(mRegions==null){
                Toast.makeText(EditAddressActivity.this,"Please select a country",Toast.LENGTH_SHORT).show();
                return;
            }
            int index=0;
            ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
            WheelPickerEntity entity=   new WheelPickerEntity();
            String stateId=String.valueOf(stateText.getTag());
            for(int i=0;i<mRegions.size();i++){
                if(mRegions.get(i).getRegion_id().equals(stateId)){
                    index=i;
                }
                WheelPickerEntity  bean=new WheelPickerEntity();
                bean.setDisplay(mRegions.get(i).getName());
                bean.setValue(mRegions.get(i).getRegion_id());
                list.add(bean);
            }
        entity.setIndex(index);
            createStatueDialogPicker(list, entity, stateText);
    }




    private void createStatueDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {
        rl_edit_state.setBottomLineActive(true);
        AnimUtil.rotateArrow(EditAddressActivity.this,iv_state_arrow,true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                rl_edit_state.setBottomLineActive(false);
                AnimUtil.rotateArrow(EditAddressActivity.this,iv_state_arrow,false);
            }
            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }
            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                rl_edit_state.setBottomLineActive(false);
                AnimUtil.rotateArrow(EditAddressActivity.this,iv_state_arrow,false);
                if (newValue == null) {
                    return;
                } else {
                    if(TextUtils.isEmpty(newValue.getDisplay())){
                        return;
                    }
                    if(getResources().getString(R.string.pleaseselect).equals(newValue.getDisplay())){
                        return;
                    }
                    view.setText(newValue.getDisplay());
                    view.setTag(newValue.getValue());
                    stateText2.setVisibility(View.VISIBLE);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(EditAddressActivity.this, configEntity);
    }

    private WheelPickerEntity  mCurrBean;
    private void createDialogPicker(ArrayList<WheelPickerEntity> list, WheelPickerEntity oldEntity, final TextView view) {
        rl_edit_country.setBottomLineActive(true);
        AnimUtil.rotateArrow(EditAddressActivity.this,iv_country_arrow,true);
        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(list);
        configEntity.setOldValue(oldEntity);
        System.out.println("=====================" + oldEntity.getIndex());
        configEntity.setIndex(oldEntity.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                rl_edit_country.setBottomLineActive(false);
                AnimUtil.rotateArrow(EditAddressActivity.this,iv_country_arrow,false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Russell", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                rl_edit_country.setBottomLineActive(false);
                if (newValue == null) {
                    System.out.print("oldValue=====" + oldValue);
                    view.setText(oldValue.getDisplay());
                    view.setTag(oldValue.getValue());
                    mCurrBean = oldValue;
                } else {
                    if (getResources().getString(R.string.pleaseselect).equals(newValue.getDisplay())) {
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
                if (mRegions == null || mRegions.size() == 0) {
                    state.setVisibility(View.VISIBLE);
                    stateText.setVisibility(View.GONE);
                } else {
                    state.setVisibility(View.GONE);
                    stateText.setVisibility(View.VISIBLE);
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(EditAddressActivity.this, configEntity);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.edit_firstName_EditText:
                    onFocus(firstName, firstNameText, firstNameText2, "First Name",null);
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, true);
                    if (firstName.getText().length()!=0)
                        ivClearEditFirst.setVisibility(View.VISIBLE);
                    else
                        ivClearEditFirst.setVisibility(View.GONE);
                    break;
                case R.id.edit_lastName_EditText:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, true);
                    onFocus(lastName, lastNameText, lastNameText2, "Last Name",null);
                    if (lastName.getText().length()!=0)
                        ivClearEditLast.setVisibility(View.VISIBLE);
                    else
                        ivClearEditLast.setVisibility(View.GONE);
                    break;
                case R.id.edit_country_EditText:
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
                    onFocus(country, countryText, countryText2, "Country",rl_edit_country);
                    break;
                case R.id.edit_address1_EditText:
                    onFocus(address1, address1Text, address1Text2, getResources().getString(R.string.address1),rl_edit_address1);
                    if (address1.getText().length()!=0)
                        ivClearEditAddress1.setVisibility(View.VISIBLE);
                    else
                        ivClearEditAddress1.setVisibility(View.GONE);
                    break;
                case R.id.edit_address2_EditText:
                    onFocus(address2, address2Text, address2Text2, getResources().getString(R.string.address2),rl_edit_address2);
                    if (address2.getText().length()!=0)
                        ivClearEditAddress2.setVisibility(View.VISIBLE);
                    else
                        ivClearEditAddress2.setVisibility(View.GONE);
                    break;
                case R.id.edit_postalcode_EditText:
                    onFocus(postalcode, postalcodeText, postalcodeText2, "Postal Code",rl_edit_postcode);
                    if (postalcode.getText().length()!=0)
                        ivClearEditCode.setVisibility(View.VISIBLE);
                    else
                        ivClearEditCode.setVisibility(View.GONE);
                    break;
                case R.id.edit_city_EditText:
                    onFocus(city, cityText, cityText2, "City",rl_edit_city);
                    if (city.getText().length()!=0)
                        ivClearEditCity.setVisibility(View.VISIBLE);
                    else
                        ivClearEditCity.setVisibility(View.GONE);
                    break;
                case R.id.edit_state_EditText:
                    clickState();
                    CharSequence tvcountry = country.getText();
                    if (tvcountry == null || JDataUtils.isEmpty(tvcountry.toString())) {
                        tvcountry = "Malaysia";
                    }
                    ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
                    for (CountrySubclass countrySub : list_countries) {
                        if (tvcountry.toString().equals(countrySub.getName())) {
                            ArrayList<CountryRegions> regionses = countrySub.getRegions();
                            for (CountryRegions region : regionses) {
                                WheelPickerEntity ww = new WheelPickerEntity();
                                ww.setDisplay(region.getName());
                                ww.setValue(region.getRegion_id());
                                list.add(ww);
                            }

                        } else {
                            continue;
                        }
                    }
                    break;
                case R.id.edit_eg:
                    CustomButtomLineRelativeLayout.setBottomLineActive(v_phone_line,true);
                    onFocus(eg, egText, egText2, getResources().getString(R.string.eg123),null);
                    if (eg.getText().length()!=0)
                        ivClearEditPhone.setVisibility(View.VISIBLE);
                    else
                        ivClearEditPhone.setVisibility(View.GONE);
            }
        }else{
            onblurAll(v.getId());
            ivClearEditFirst.setVisibility(View.GONE);
            ivClearEditLast.setVisibility(View.GONE);
            ivClearEditAddress1.setVisibility(View.GONE);
            ivClearEditAddress2.setVisibility(View.GONE);
            ivClearEditCode.setVisibility(View.GONE);
            ivClearEditCity.setVisibility(View.GONE);
            ivClearEditPhone.setVisibility(View.GONE);
        }
    }
    public boolean onblurAll(int id){
        switch (id){
            case R.id.edit_firstName_EditText:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, false);
                firstNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                firstNameText2.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(firstName.getText().toString().trim())){
                    firstName.setHint("First Name");
                    firstNameText.clearAnimation();
                    //验证字段
                    firstNameText2.setText(getResources().getString(R.string.required_field));
                    firstNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0,0);
                    return false;
                }else{
                    firstNameText.clearAnimation();
                }
                break;
            case R.id.edit_lastName_EditText:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, false);
                lastNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                lastNameText2.setVisibility(View.VISIBLE);
                if(lastName.getText().toString().trim().equals("")){
                    lastName.setHint("Last Name");
                    lastNameText.clearAnimation();
                    //验证字段
                    lastNameText2.setText(getResources().getString(R.string.required_field));
                    lastNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    lastNameText.clearAnimation();
                }
                break;
            case R.id.edit_country_EditText:
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                countryText2.setVisibility(View.VISIBLE);
                rl_edit_country.setBottomLineActive(false);
                if(country.getText().toString().trim().equals("")){
                    country.setHint("Country");
                    countryText.clearAnimation();
                    countryText2.setText(getResources().getString(R.string.required_field));
                    countryText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    countryText.clearAnimation();
                }
                break;
            case R.id.edit_address1_EditText:
                address1Text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                address1Text2.setVisibility(View.VISIBLE);
                rl_edit_address1.setBottomLineActive(false);
                if(address1.getText().toString().trim().equals("")){
                    address1.setHint(getResources().getString(R.string.address1));
                    address1Text.clearAnimation();
                    //验证字段
                    address1Text2.setText(getResources().getString(R.string.required_field));
                    address1Text2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    address1Text.clearAnimation();
                }
                break;
            case R.id.edit_address2_EditText:
                address2Text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                address2Text2.setVisibility(View.VISIBLE);
                rl_edit_address2.setBottomLineActive(false);
                if(address2.getText().toString().trim().equals("")){
                    address2.setHint(getResources().getString(R.string.address2));
                    address2Text.clearAnimation();
                    address2Text2.setVisibility(View.INVISIBLE);
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    address2Text.clearAnimation();
                }
                break;
            case R.id.edit_postalcode_EditText:
                postalcodeText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                postalcodeText2.setVisibility(View.VISIBLE);
                rl_edit_postcode.setBottomLineActive(false);
                if(postalcode!=null&&postalcode.getText().toString().trim().equals("")){
                    postalcode.setHint("Postal Code");
                    postalcodeText.clearAnimation();
                    //验证字段
                    postalcodeText2.setText(getResources().getString(R.string.This_is_a_required_field));
                    postalcodeText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else if(postalcode!=null&&postalcode.getText().toString().trim().length()<4){
                    postalcodeText.clearAnimation();
                    //验证字段
                    postalcodeText2.setText(getResources().getString(R.string.blur_postalcode));
                    postalcodeText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    postalcodeText.clearAnimation();
                    /**
                     * send request to get city and state by postcode.
                     * russell
                     */
                    sendRequestToGetCityAndStateByPostCode(postalcode.getText().toString().trim());
                }
                break;
            case R.id.edit_city_EditText:
                cityText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                cityText2.setVisibility(View.VISIBLE);
                rl_edit_city.setBottomLineActive(false);
                if(city.getText().toString().trim().equals("")){
                    city.setHint("City");
                    cityText.clearAnimation();
                    //验证字段
                    cityText2.setText(getResources().getString(R.string.required_field));
                    cityText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    cityText.clearAnimation();
                }
                break;
            case R.id.edit_state_EditText:
                stateText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                stateText2.setVisibility(View.VISIBLE);
                if(state.getText().toString().trim().equals("")){
                    state.setHint("State");
                    stateText.clearAnimation();
                    myScrollView.scrollTo(0, 0);
                    return false;
                }else{
                    stateText.clearAnimation();
                }
                break;
            case R.id.edit_eg:
                CustomButtomLineRelativeLayout.setBottomLineActive(v_phone_line, false);
                egText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                egText2.setVisibility(View.VISIBLE);
                if(eg.getText().toString().trim().equals("")){
                    eg.setHint(getResources().getString(R.string.eg123));
                    egText.clearAnimation();
                    //验证字段
                    egText2.setText(getResources().getString(R.string.required_field));
                    egText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    myScrollView.scrollTo(0,0);
                    return false;
                }else{
                    egText.clearAnimation();
                }
        }
        return true;
    }

    private void sendRequestToGetCityAndStateByPostCode(String postcode) {
        city.setEnabled(false);
        String session_key= GemfiveApplication.getAppConfiguration().getUserInfo(EditAddressActivity.this).getSessionKey();
        String country_id=country.getTag() == null ? "" : country.getTag().toString();
        dao.getCityAndStateByPostCodet(session_key, postcode, country_id);

    }

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
        edit.setHint("");
        if(edit.getText().toString().trim().equals("")) {
            JLogUtils.i(TAG, "--"+edit.getText());
            text2.setVisibility(View.INVISIBLE);
            text.startAnimation(set);
        }else{
            text2.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        }
        if(relativeLayout!=null) {
            relativeLayout.setBottomLineActive(true);
        }
    }

    /**
     * send request to webservice to get country and region datas
     */
    private void sendRequestToGetCountryAndRegions() {
        mDialog=JViewUtils.showProgressDialog(EditAddressActivity.this);
        dao.getCountryAndRegions(GemfiveApplication.getAppConfiguration().getUserInfo(EditAddressActivity.this).getSessionKey());

//        SVRParameters parameters = new SVRParameters();
//        parameters.put("session_key", GemfiveApplication.getAppConfiguration().getUserInfo(EditAddressActivity.this).getSessionKey());
//        SVRCountry countryHandler = new SVRCountry(EditAddressActivity.this, parameters);
//        countryHandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                final SVRAppServiceCustomerCountry countryEntityResult = (SVRAppServiceCustomerCountry) result;
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mDialog!=null){
//                            mDialog.cancel();
//                        }
//                        country.setOnClickListener(EditAddressActivity.this);
//                        stateText.setOnClickListener(EditAddressActivity.this);
////                        tvShippingCountry.setOnClickListener(this);
////                        tvShippingState.setOnClickListener(this);
//                        list_countries = countryEntityResult.getCountry();
//                        list_countries.add(0, new CountrySubclass("", getResources().getString(R.string.pleaseselect)));
////                        checkState();
//                        ArrayList<CountryRegions> regisons= getState(mBean.getCountryId(), list_countries);
//                        if(regisons==null||regisons.size()==0){
//                            stateText.setVisibility(View.GONE);
//                            state.setVisibility(View.VISIBLE);
//                        }else{
//                            stateText.setVisibility(View.VISIBLE);
//                            state.setVisibility(View.GONE);
//                        }
//                        mRegions.addAll(regisons);
//                        mRegions.add(0, new CountryRegions("", getResources().getString(R.string.pleaseselect)));
//                        SharedPreferences sharedPreferences = getSharedPreferences("countries", Activity.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("countries", new Gson().toJson(countryEntityResult));
//                        editor.commit();
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(int resultCode, final String errorMsg) {
//                if(resultCode==408||resultCode==700){
//                    Toast.makeText(EditAddressActivity.this,  getString(R.string.please_check), Toast.LENGTH_LONG).show();
//                }else if (!JDataUtils.errorMsgHandler(EditAddressActivity.this, errorMsg)) {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(EditAddressActivity.this
//                                    , errorMsg, Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        });
    }


    public final  ArrayList<CountryRegions> getState(String value,ArrayList<CountrySubclass>  countrys){
        ArrayList<CountryRegions>  regions=null;
        for(int i=0;i<countrys.size();i++){
            CountrySubclass sub=countrys.get(i);
            if(value.equals(sub.getCountry_id())){
                country.setTag(sub.getCountry_id()+"");
                regions=sub.getRegions();
            }
        }
        return  regions;
    }

//
//    public void wheelPicker(ArrayList<CountrySubclass> countries, final EditText edit){
//
//        list_countries = countries;
//
//        //Init Country UI first
//        ArrayList<WheelPickerEntity> list = new ArrayList<WheelPickerEntity>();
//        WheelPickerEntity oldEntity;
//
//        for (CountrySubclass country : countries) {
//            WheelPickerEntity ww = new WheelPickerEntity();
//            ww.setDisplay(country.getName());
//            ww.setValue(country.getCountry_id());
//            list.add(ww);
//        }
//        oldEntity = new WheelPickerEntity();
//        oldEntity.setDisplay("Malaysia");
//        oldEntity.setValue("MY");
//
//        /*ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
//        for (int index = 0; index < 10; ++index) {
//            WheelPickerEntity ww = new WheelPickerEntity();
//            ww.setDisplay("Size - M -" + index);
//            ww.setValue("M - " + index);
//            wheel.add(ww);
//        }
//        WheelPickerEntity ww = new WheelPickerEntity();
//        ww.setDisplay("Size - M - ");
//        ww.setValue("M - ");*/
//
//        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//        configEntity.setArrayList(list);
//        configEntity.setOldValue(oldEntity);
//        configEntity.setCallBack(new WheelPickerCallback() {
//            @Override
//            public void onCancel() {
//                JLogUtils.i("Zero", "onCancel()");
//                edit.setHint("");
//                edit.setText("");
//            }
//            @Override
//            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                JLogUtils.i("Zero", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
//            }
//            @Override
//            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                JLogUtils.i("Zero", "onDone() -- oldValue => " + oldValue + "  newValue => " + newValue);
//                if(newValue.getDisplay()==null){
//                    edit.setText(oldValue.getDisplay());
//                    edit.setHint(oldValue.getValue());
//                }else {
//                    edit.setText(newValue.getDisplay());
//                    edit.setHint(newValue.getValue());
//                }
//            }
//        });
//        JViewUtils.showWheelPickerOneDialog(EditAddressActivity.this, configEntity);
//    }

//    //PhoneNumber
//    ArrayList<PhoneNumberList> phoneNumberList;
//    public void  wheelPickerPhoneNumber(){
//        //服务器中取得数据
//        SVRParameters parameters = new SVRParameters();
//        SVRPhoneNumber svrhandler = new SVRPhoneNumber(EditAddressActivity.this, parameters);
//        svrhandler.loadDatasFromServer(new SVRCallback() {
//            @Override
//            public void onSuccess(int resultCode, SVRReturnEntity result) {
//                //成功后将数据放到Entity中
//                SVRAppServiceCustomerPhoneNumber phoneNumberEntity = (SVRAppServiceCustomerPhoneNumber) result;
//                JLogUtils.i("Allen", "  Success =" + phoneNumberEntity.getStatus());
//
//                ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
//                phoneNumberList = phoneNumberEntity.getTelephoneCodeList();
//                for (int index = 0; index < phoneNumberList.size(); ++index) {
//                    WheelPickerEntity ww = new WheelPickerEntity();
//                    ww.setDisplay(phoneNumberList.get(index).getLabel());
//                    ww.setValue(phoneNumberList.get(index).getValue());
//                    wheel.add(ww);
//                }
//
//                WheelPickerEntity ww = new WheelPickerEntity();
//                ww.setDisplay(phoneNumberList.get(0).getLabel());
//                ww.setValue(phoneNumberList.get(0).getValue());
//
//                WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
//                configEntity.setArrayList(wheel);
//                configEntity.setOldValue(ww);
//                configEntity.setCallBack(new WheelPickerCallback() {
//                    @Override
//                    public void onCancel() {
//                        JLogUtils.i("Martin", "onCancel()");
//                    }
//
//                    @Override
//                    public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                        JLogUtils.i("Martin", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
//                    }
//
//                    @Override
//                    public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
//                        JLogUtils.i("Martin", "onDone() -- oldValue => " + oldValue + "  newValue => " + newValue);
//                        if (newValue.getDisplay() == null) {
//                            phoneNumber.setText(oldValue.getDisplay());
//                        } else {
//                            phoneNumber.setText(newValue.getDisplay());
//                        }
//                    }
//                });
//                JViewUtils.showWheelPickerOneDialog(EditAddressActivity.this, configEntity);
//            }
//
//            @Override
//            public void onFailure(int resultCode, String errorMsg) {
//                if(resultCode==408||resultCode==700){
//                    Toast.makeText(EditAddressActivity.this,  getString(R.string.please_check), Toast.LENGTH_LONG).show();
//                }else {
//                    //失败后通过resultCode信息进行处理
//                    Toast.makeText(EditAddressActivity.this, "error " + resultCode + " " + errorMsg, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
}

package com.whitelabel.app.fragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.BaseActivity;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.MyAccountActivity;
import com.whitelabel.app.activity.MyAccountChangePasswordActivity;
import com.whitelabel.app.adapter.WheelPickerAdapter;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.WheelPickerCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.model.CountryRegions;
import com.whitelabel.app.model.CountrySubclass;
import com.whitelabel.app.model.CustomerList;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.MonthlyIncomList;
import com.whitelabel.app.model.PhoneNumberList;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRAppServiceCustomerMonthlyIncom;
import com.whitelabel.app.model.SVRAppServiceCustomerMyAccount;
import com.whitelabel.app.model.SVRAppServiceCustomerMyAccountUpdate;
import com.whitelabel.app.model.SVRGetCityANdStateByPostCodeEntity;
import com.whitelabel.app.model.WheelPickerConfigEntity;
import com.whitelabel.app.model.WheelPickerEntity;
import com.whitelabel.app.utils.AnimUtil;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by imaginato on 2015/6/26.
 */
public class MyAccountEditInfoFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private MyAccountActivity myAccountActivity;
    private SharedPreferences.Editor editor;
    private View contentView;
    private EditText firstName, lastName, email, country, birthday, gender, monthlyIncome, zip, city, stateProvince, eg;
    private TextView firstNameText;
    private TextView firstNameText2;
    private TextView lastNameText;
    private TextView lastNameText2;
    private TextView emailText;
    private TextView emailText2;
    private TextView countryText;
    private TextView countryText2;
    private TextView birthdayText;
    private TextView birthdayText2;
    private TextView genderText;
    private TextView genderText2;
    private TextView monthlyIncomeText;
    private TextView monthlyIncomeText2;
    private TextView zipText;
    private TextView zipText2;
    private TextView cityText;
    private TextView cityText2;
    private TextView stateProvinceText;
    private TextView stateProvinceText2;
    private TextView egText;
    private TextView egText2;
    public boolean saving = false;
    private ImageView iv_country_arrow, iv_birther_arrow, iv_gender_arrow, iv_monthly_arrow, iv_state_arrow;
    private ImageView clearFirstName;
    private ImageView clearLastName;
    private ImageView clearMail;
    private ImageView clearCode;
    private ImageView clearCity;
    private ImageView clearPhone;
    private CustomButtomLineRelativeLayout rl_editinfo_email, rl_editinfo_country, rl_editinfo_birthday, rl_editinfo_gender, rl_editinfo_monthly,
            rl_editinfo_postcode, rl_editinfo_city, rl_editinfo_state;
    private View view_firstname_line;
    private View view_lastname_line;
    private View v_editinfo_phone_line;
    private TextView phoneNumber;
    private SharedPreferences sharedIncome, sharedCountry;
    private CustomerList customerList;
    private ArrayList<CountrySubclass> countryList;
    private TextView save_error;
    private ScrollView myScrollView;
    private String brithdayMonth[] = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private Map<String, String> mothMap;
    private final String SESSION_EXPIRED = "session expired,login again please";
    private final int REQUESTCODE_LOGIN = 1000;
    private View relative14;
    private Dialog mDialog;
    private MyAccountDao mDao;
    private SharedPreferences.Editor editorIncome;
    private int[] location = new int[2];
    private int height;
    private int currentItem3, currentItem2, currentItem1;
    private boolean first = false;
    public MyAccountEditInfoFragment() {
    }
    public void initChangePassword() {
        if (!WhiteLabelApplication.getAppConfiguration().getUser().isEmailLogin()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relative14.getLayoutParams();
            params.height = 0;
            relative14.setLayoutParams(params);
        }
    }
    private static final class DataHandler extends Handler {
        private final WeakReference<MyAccountActivity> mActivity;
        private final WeakReference<MyAccountEditInfoFragment> mFragment;
        public DataHandler(MyAccountActivity activity, MyAccountEditInfoFragment fragment) {
            mActivity = new WeakReference<MyAccountActivity>(activity);
            mFragment = new WeakReference<MyAccountEditInfoFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null || mFragment.get() == null || !mFragment.get().isAdded()) {
                return;
            }
            // 点击save会调用 sendRequestToGetCityAndStateByPostCode，以至于 save（）的dialog被getPostCode（）关闭
            if (mFragment.get().mDialog != null && msg.what != MyAccountDao.REQUEST_SENDREQUESTTOGET) {
                mFragment.get().mDialog.dismiss();
            }
            switch (msg.what) {
                case MyAccountDao.REQUEST_SAVE:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        SVRAppServiceCustomerMyAccount myAccountEntity = (SVRAppServiceCustomerMyAccount) msg.obj;
                        //刷新本地缓存数据
                        GOUserEntity user = WhiteLabelApplication.getAppConfiguration().getUser();
                        String firstName = mFragment.get().firstName.getText().toString().trim();
                        String lastName = mFragment.get().lastName.getText().toString().trim();
                        user.setFirstName(JDataUtils.filterEmoji(firstName));
                        user.setLastName(JDataUtils.filterEmoji(lastName));
                        user.setEmail(mFragment.get().email.getText().toString().trim());
                        WhiteLabelApplication.getAppConfiguration().updateUserData(mActivity.get(), user);
                        mActivity.get().onBackPressed();
                    } else {
                        //isSaved  在onFoucsChangeListenter里作用，防止showWheelPickerOneDialog在点击save后自动弹出
                        mFragment.get().isSaved = true;
                        mFragment.get().saving = false;
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), msg.obj.toString())) {
                            //失败后通过resultCode信息进行处理
                            mFragment.get().save_error.setText(msg.obj.toString());
                            mFragment.get().myScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_SENDREQUESTTOGET:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        SVRGetCityANdStateByPostCodeEntity getCityANdStateByPostCodeEntity = (SVRGetCityANdStateByPostCodeEntity) msg.obj;
                        if (getCityANdStateByPostCodeEntity.getStatus() == 1) {
                            mFragment.get().city.setEnabled(true);
                            if (!JDataUtils.isEmpty(getCityANdStateByPostCodeEntity.getCity())) {
                                if (mFragment.get().cityText.getVisibility() == View.GONE && mFragment.get().cityText2.getVisibility() != View.VISIBLE) {
                                    mFragment.get().cityText2.setTextColor(mFragment.get().getResources().getColor(R.color.label_saved));//设置为灰色
                                    mFragment.get().cityText2.setVisibility(View.VISIBLE);
                                    mFragment.get().rl_editinfo_city.setBottomLineActive(false);
                                }
                                if (mFragment.get().stateProvinceText.getVisibility() != View.VISIBLE && mFragment.get().stateProvinceText2.getVisibility() != View.VISIBLE) {
                                    mFragment.get().stateProvinceText2.setVisibility(View.VISIBLE);
                                    mFragment.get().stateProvinceText2.setTextColor(mFragment.get().getResources().getColor(R.color.label_saved));//设置为灰色
                                }
                                mFragment.get().city.setText(getCityANdStateByPostCodeEntity.getCity());
                                if (mFragment.get().city.hasFocus()) {
                                    mFragment.get().city.clearFocus();
                                    InputMethodManager inputMethodManager = (InputMethodManager) mFragment.get().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(mFragment.get().city.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                mFragment.get().stateProvince.setText(getCityANdStateByPostCodeEntity.getRegionName());
                                mFragment.get().stateProvince.setTag(getCityANdStateByPostCodeEntity.getRegionId());
                                mFragment.get().stateProvince.setTextColor(mFragment.get().myAccountActivity.getResources().getColor(R.color.hint));
                                mFragment.get().stateProvince.setEnabled(false);
                                mFragment.get().stateProvinceText.setEnabled(false);

                            }
                        }
                    } else {
                        mFragment.get().city.setEnabled(true);
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), msg.obj.toString())) {
                            if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains(mFragment.get().SESSION_EXPIRED))) {

                                Intent intent = new Intent();
                                intent.setClass(mActivity.get(), LoginRegisterActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("expire", true);
                                intent.putExtras(bundle);
                                mActivity.get().startActivityForResult(intent, mFragment.get().REQUESTCODE_LOGIN);
                            } else {
                                mFragment.get().save_error.setText(msg.obj.toString());
                                mFragment.get().save_error.setVisibility(View.VISIBLE);
                                mFragment.get().myScrollView.scrollTo(0, 5000);
                            }
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_GETCOUNTRY_REGIONS:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        //成功后将数据放到Entity中
                        SVRAppServiceCustomerCountry countryEntity = (SVRAppServiceCustomerCountry) msg.obj;
                        mFragment.get().countryList = countryEntity.getCountry();
                        for (int index = 0; index < mFragment.get().countryList.size(); ++index) {
                            String label = mFragment.get().countryList.get(index).getName();
                            String value = mFragment.get().countryList.get(index).getCountry_id();
                            mFragment.get().editor.putString("" + index, label);
                            mFragment.get().editor.putString(value, label);
                            mFragment.get().editor.putString(label, value);
                            //stateProvince
                            ArrayList<CountryRegions> stateProvinceList = mFragment.get().countryList.get(index).getRegions();
                            for (int i = 0; i < stateProvinceList.size(); i++) {
                                String label2 = stateProvinceList.get(i).getName();
                                String value2 = stateProvinceList.get(i).getRegion_id();
                                mFragment.get().editor.putString("stateProvince" + i, label2);
                                mFragment.get().editor.putString(value2, label2);
                                mFragment.get().editor.putString(label2, value2);
                            }
                            mFragment.get().editor.putInt(label + "size", stateProvinceList.size());
                        }
                        mFragment.get().editor.putInt("size", mFragment.get().countryList.size());
                        mFragment.get().editor.commit();
                            if(countryEntity.getCountry()!=null&&countryEntity.getCountry().size()>0) {
                                mFragment.get().country.setText(countryEntity.getCountry().get(0).getName());
                            }
                        mFragment.get().stateProvince.setText(mFragment.get().sharedCountry.getString(mFragment.get().customerList.getRegion(), ""));
                        mFragment.get().initAllHint();
                    } else {
                        //失败后通过resultCode信息进行处理
                        Toast.makeText(mActivity.get(), "error" + msg.obj.toString(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case MyAccountDao.REQUEST_MONTHLYINCOM:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        //成功后将数据放到Entity中
                        SVRAppServiceCustomerMonthlyIncom monthlyIncomEntity = (SVRAppServiceCustomerMonthlyIncom) msg.obj;
                        mFragment.get().incomList = monthlyIncomEntity.getIncomeList();
                        for (int index = 0; index < mFragment.get().incomList.size(); ++index) {
                            String label = mFragment.get().incomList.get(index).getLabel();
                            String value = mFragment.get().incomList.get(index).getValue();
                            mFragment.get().editorIncome.putString("" + index, label);
                            mFragment.get().editorIncome.putString(value, label);
                            mFragment.get().editorIncome.putString(label, value);
                        }
                        mFragment.get().editorIncome.putInt("size", mFragment.get().incomList.size());
                        mFragment.get().editorIncome.commit();
                        mFragment.get().monthlyIncome.setText(mFragment.get().sharedIncome.getString(mFragment.get().customerList.getIncome(), ""));
                        mFragment.get().initAllHint();
                    } else {
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), msg.obj.toString())) {
                            //失败后通过resultCode信息进行处理
                            Toast.makeText(mActivity.get(), "error " + msg.obj.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_ACCOUNTUPDATEA:
                    if (msg.arg1 == MyAccountDao.RESPONSE_SUCCESS) {
                        //成功后将数据放到Entity中
                        SVRAppServiceCustomerMyAccountUpdate myAccountUpdateEntity = (SVRAppServiceCustomerMyAccountUpdate) msg.obj;
                        mFragment.get().customerList = myAccountUpdateEntity.getCustomer();

                        mFragment.get().myAccountActivity.initToolBar(setFirstLetterToUpperCase(mFragment.get().customerList.getFirstName()) + " " + setFirstLetterToUpperCase(mFragment.get().customerList.getLastName()));
                        mFragment.get().firstName.setText(mFragment.get().customerList.getFirstName());
                        mFragment.get().lastName.setText(mFragment.get().customerList.getLastName());
                        mFragment.get().email.setText(mFragment.get().customerList.getEmail());
                        if (!mFragment.get().customerList.getBirthday().equals("")) {
                            String birthdayStr[] = mFragment.get().customerList.getBirthday().split("-");
                            String MyBrithday = birthdayStr[2] + "-" + mFragment.get().mothMap.get(birthdayStr[1]) + "-" + birthdayStr[0];
                            mFragment.get().birthday.setText(MyBrithday);
                        }
                        if (mFragment.get().customerList.getGender().equals("1")) {
                            mFragment.get().gender.setText("Male");
                        } else if (mFragment.get().customerList.getGender().equals("2")) {
                            mFragment.get().gender.setText("Female");
                        } else {
                            mFragment.get().gender.setText("");
                        }
                        mFragment.get().zip.setText(mFragment.get().customerList.getPostcode());
                        mFragment.get().city.setText(mFragment.get().customerList.getCity());
                        mFragment.get().eg.setText(mFragment.get().customerList.getTelephone());
                        //mothleIncome数据存取
//                        mFragment.get().sharedIncome = mFragment.get().myAccountActivity.getSharedPreferences("mothleIncome", Activity.MODE_PRIVATE);
//                        if (!mFragment.get().sharedIncome.getBoolean("exits", false)) {
//                            mFragment.get().editorIncome = mFragment.get().sharedIncome.edit();
//                            mFragment.get().editorIncome.putBoolean("exits", true);
//
////                            mFragment.get().mDao.MonthlyIncom(WhiteLabelApplication.getAppConfiguration().getUserInfo(mActivity.get()).getSessionKey());
////
//                        } else {
//                            mFragment.get().monthlyIncome.setText(mFragment.get().sharedIncome.getString(mFragment.get().customerList.getIncome(), ""));
//                        }

                        //Country数据存取
                        mFragment.get().sharedCountry = mActivity.get().getSharedPreferences("country", Activity.MODE_PRIVATE);
                        mFragment.get().getCountryAndstateProvince();
                        if (!"".equals(mFragment.get().sharedCountry.getString(mFragment.get().customerList.getCountry_id(), ""))) {
                            mFragment.get().country.setText(mFragment.get().sharedCountry.getString(mFragment.get().customerList.getCountry_id(), ""));
                        }
                        mFragment.get().stateProvince.setText(mFragment.get().sharedCountry.getString(mFragment.get().customerList.getRegion(), ""));
                        if (!TextUtils.isEmpty(mFragment.get().stateProvince.getText().toString())) {
                            mFragment.get().stateProvince.setEnabled(false);
                            mFragment.get().stateProvince.setTextColor(mFragment.get().myAccountActivity.getResources().getColor(R.color.hint));
                        }
                        mFragment.get().initAllHint();
                    } else {
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), msg.obj.toString())) {
                            if (!mActivity.get().checkIsFinished() && !mActivity.get().checkIsInvisible()) {
                                if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains("session expired,login again please"))) {
                                    Intent intent = new Intent();
                                    intent.putExtra("expire", true);
                                    intent.setClass(mActivity.get(), LoginRegisterActivity.class);
                                    mActivity.get().startActivityForResult(intent, 1000);
                                    return;
                                }
                            }
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    mFragment.get().saving = false;
                    RequestErrorHelper requestErrorHelper = new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }
    }
//    && onblurAll(R.id.et_account_monthlyIncome) &&
//    onblurAll(R.id.et_account_zip) && onblurAll(R.id.et_account_city) && onblurAll(R.id.et_account_eg)
    public void save() {
        if (!saving && onblurAll(R.id.et_account_firstName) && onblurAll(R.id.et_account_lastName) && onblurAll(R.id.et_account_email) &&
                onblurAll(R.id.et_account_country) && onblurAll(R.id.et_account_birthday) ) {
            saving = true;
            if (mDialog != null) {
                mDialog.show();
            } else {
                mDialog = JViewUtils.showProgressDialog(myAccountActivity);
            }
            String session_key = WhiteLabelApplication.getAppConfiguration().getUserInfo(myAccountActivity).getSessionKey();
            String emailStr = email.getText().toString().trim();
            String firstNameStr = firstName.getText().toString().trim();
            String lastNameStr = lastName.getText().toString().trim();
            String genderStr = "";
            String birthdayStr = birthday.getText().toString().trim();
            String countryIdStr = sharedCountry.getString(country.getText().toString().trim(), "");
            String regionIdStr = sharedCountry.getString(stateProvince.getText().toString().trim(), "");
            String cityStr = city.getText().toString().trim();
//            String incomeIdStr = sharedIncome.getString(monthlyIncome.getText().toString().trim(), "");
            String postcodeStr = zip.getText().toString().trim();
            String telephoneCodeStr = phoneNumber.getText().toString().trim();
            String telephoneStr = eg.getText().toString().trim();

            //调用server
            // SVRParameters parameters = new SVRParameters();
            if (gender.getText().toString().trim().equals("Male")) {
                // parameters.put("gender", "1");
                genderStr = "1";
            }
            if (gender.getText().toString().trim().equals("Female")) {
                // parameters.put("gender", "2");
                genderStr = "2";

            }

            String birthdayNum = "";
            if (!TextUtils.isEmpty(birthdayStr)) {
                String bitthdayStrArr[] = birthdayStr.split("-");

                if (bitthdayStrArr != null && bitthdayStrArr.length == 3) {
                    String monthNum = mapValueGetKey(mothMap, bitthdayStrArr[1]);
                    birthdayNum = bitthdayStrArr[2] + "-" + monthNum + "-" + bitthdayStrArr[0];
                }
            }
            saving = true;
            mDao.save(session_key, emailStr, firstNameStr, lastNameStr, genderStr, birthdayNum, countryIdStr, regionIdStr, cityStr, "", postcodeStr, telephoneCodeStr, telephoneStr);
        }
    }

    private static String setFirstLetterToUpperCase(String title) {
        if (!TextUtils.isEmpty(title)) {
            title = title.toLowerCase();

            String[] split = title.trim().split(" ");
            StringBuilder newTitle = new StringBuilder();
            if (split.length != 0) {
                for (String cell : split) {
                    if (cell.trim().length() > 1) {
                        newTitle.append(cell.substring(0, 1).toUpperCase()).append(cell.substring(1)).append(" ");
                    } else {
                        newTitle.append(cell.toUpperCase()).append(" ");
                    }
                }
            }
            return newTitle.toString().trim();
        }
        return "";
    }
    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private Uri photoUri = null;
    private String picPath;// 图片路径
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myAccountActivity = (MyAccountActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_myaccount_edit_info, null);
        return contentView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataHandler handler = new DataHandler(myAccountActivity, this);
        String TAG = "MyAccountEditInfoFragment";
        mDao = new MyAccountDao(TAG, handler);

        mothMap = new HashMap<String, String>();//英文数字月份映射
        for (int i = 0; i < 12; i++) {
            if (i < 9) {
                mothMap.put("0" + (i + 1), brithdayMonth[i]);
            } else {
                mothMap.put("" + (i + 1), brithdayMonth[i]);
            }
        }
        mDialog = JViewUtils.showProgressDialog(myAccountActivity);
        view_firstname_line = contentView.findViewById(R.id.view_firstname_line);
        view_lastname_line = contentView.findViewById(R.id.view_lastname_line);
        v_editinfo_phone_line = contentView.findViewById(R.id.v_editinfo_phone_line);
        rl_editinfo_email = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_email);
        rl_editinfo_country = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_country);
        rl_editinfo_birthday = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_birthday);
        rl_editinfo_gender = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_gender);
        rl_editinfo_monthly = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_monthly);
        rl_editinfo_postcode = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_postcode);
        rl_editinfo_city = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_city);
        rl_editinfo_state = (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_editinfo_state);
//        relative1 = (RelativeLayout) contentView.findViewById(R.id.relative1);
        firstName = (EditText) contentView.findViewById(R.id.et_account_firstName);
        lastName = (EditText) contentView.findViewById(R.id.et_account_lastName);
        email = (EditText) contentView.findViewById(R.id.et_account_email);
        country = (EditText) contentView.findViewById(R.id.et_account_country);
        birthday = (EditText) contentView.findViewById(R.id.et_account_birthday);
        gender = (EditText) contentView.findViewById(R.id.et_account_gender);
        monthlyIncome = (EditText) contentView.findViewById(R.id.et_account_monthlyIncome);
        zip = (EditText) contentView.findViewById(R.id.et_account_zip);
        city = (EditText) contentView.findViewById(R.id.et_account_city);
        stateProvince = (EditText) contentView.findViewById(R.id.et_account_stateProvince);
        eg = (EditText) contentView.findViewById(R.id.et_account_eg);
        save_error = (TextView) contentView.findViewById(R.id.save_error);
        myScrollView = (ScrollView) contentView.findViewById(R.id.my_scroll_view);
        relative14 = contentView.findViewById(R.id.relative14);
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
//        email.setOnFocusChangeListener(this);
        email.setEnabled(false);
        email.clearFocus();
        country.setOnFocusChangeListener(this);
        birthday.setOnFocusChangeListener(this);
        gender.setOnFocusChangeListener(this);
        monthlyIncome.setOnFocusChangeListener(this);
        zip.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        stateProvince.setOnFocusChangeListener(this);

        clearFirstName = (ImageView) contentView.findViewById(R.id.clear_first_name);
        clearLastName = (ImageView) contentView.findViewById(R.id.clear_last_name);
        clearMail = (ImageView) contentView.findViewById(R.id.clear_mail_name);
        clearCode = (ImageView) contentView.findViewById(R.id.clear_code_name);
        clearCity = (ImageView) contentView.findViewById(R.id.clear_city_name);
        clearPhone = (ImageView) contentView.findViewById(R.id.clear_phone_name);

        iv_country_arrow = (ImageView) contentView.findViewById(R.id.iv_country_arrow);
        iv_birther_arrow = (ImageView) contentView.findViewById(R.id.iv_birther_arrow);
        iv_gender_arrow = (ImageView) contentView.findViewById(R.id.iv_gender_arrow);
        iv_monthly_arrow = (ImageView) contentView.findViewById(R.id.iv_monthly_arrow);
        iv_state_arrow = (ImageView) contentView.findViewById(R.id.iv_state_arrow);

        clearFirstName.setOnClickListener(this);
        clearLastName.setOnClickListener(this);
//        clearMail.setOnClickListener(this);
        clearCode.setOnClickListener(this);
        clearCity.setOnClickListener(this);
        clearPhone.setOnClickListener(this);

        clearFirstName.setVisibility(View.GONE);
        clearLastName.setVisibility(View.GONE);
        clearMail.setVisibility(View.GONE);
        clearCode.setVisibility(View.GONE);
        clearCity.setVisibility(View.GONE);
        clearPhone.setVisibility(View.GONE);


        eg.setOnFocusChangeListener(this);
        email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        firstNameText = (TextView) contentView.findViewById(R.id.ctv_account_firstName_label_ani);
        firstNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        firstNameText2 = (TextView) contentView.findViewById(R.id.ctv_account_firstName_label);
        lastNameText = (TextView) contentView.findViewById(R.id.ctv_account_lastName_label_ani);
        lastNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        lastNameText2 = (TextView) contentView.findViewById(R.id.ctv_account_lastName_label);

        emailText = (TextView) contentView.findViewById(R.id.ctv_account_email_label_ani);
        emailText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        emailText2 = (TextView) contentView.findViewById(R.id.ctv_account_email_label);
        countryText = (TextView) contentView.findViewById(R.id.ctv_account_country_label_ani);
        countryText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        countryText2 = (TextView) contentView.findViewById(R.id.ctv_account_country_label);
        birthdayText = (TextView) contentView.findViewById(R.id.ctv_account_birthday_label_ani);
        birthdayText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        birthdayText2 = (TextView) contentView.findViewById(R.id.ctv_account_birthday_label);
        genderText = (TextView) contentView.findViewById(R.id.ctv_account_gender_label_ani);
        genderText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        genderText2 = (TextView) contentView.findViewById(R.id.ctv_account_gender_label);
        monthlyIncomeText = (TextView) contentView.findViewById(R.id.ctv_account_monthlyIncome_label_ani);
        monthlyIncomeText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        monthlyIncomeText2 = (TextView) contentView.findViewById(R.id.ctv_account_monthlyIncome_label);
        //zip=postal code
        zipText = (TextView) contentView.findViewById(R.id.ctv_account_zip_label_ani);
        zipText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        zipText2 = (TextView) contentView.findViewById(R.id.ctv_account_zip_label);
        cityText = (TextView) contentView.findViewById(R.id.ctv_account_city_label_ani);
        cityText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        cityText2 = (TextView) contentView.findViewById(R.id.ctv_account_city_label);
        stateProvinceText = (TextView) contentView.findViewById(R.id.ctv_account_state_label_ani);
        stateProvinceText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        stateProvinceText2 = (TextView) contentView.findViewById(R.id.ctv_account_state_label);
        egText = (TextView) contentView.findViewById(R.id.ctv_account_eg_label_ani);
        egText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        egText2 = (TextView) contentView.findViewById(R.id.ctv_account_eg_label);
        TextView changePassword = (TextView) contentView.findViewById(R.id.changePassword);
        changePassword.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        changePassword.setOnClickListener(this);
        ImageView photo = (ImageView) contentView.findViewById(R.id.photo);
        photo.setOnClickListener(this);
        country.setOnClickListener(this);
        gender.setOnClickListener(this);
        birthday.setOnClickListener(this);
        monthlyIncome.setOnClickListener(this);
        stateProvince.setOnClickListener(this);
        phoneNumber = (TextView) contentView.findViewById(R.id.Phone_number);

        phoneNumber.setOnClickListener(this);
        //加载数据
        initChangePassword();
        if (WhiteLabelApplication.getAppConfiguration().isSignIn(myAccountActivity)) {
            initData();
        } else {
            Intent intent = new Intent();
            intent.putExtra("expire", true);
            intent.setClass(myAccountActivity, LoginRegisterActivity.class);
            startActivityForResult(intent, 1000);
        }

        eg.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        zip.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
    }

    public void initData() {
        mDao.CustomerInfo(WhiteLabelApplication.getAppConfiguration().getUserInfo(myAccountActivity).getSessionKey());
    }

    public void getCountryAndstateProvince() {
        editor = sharedCountry.edit();
        editor.putBoolean("exits", true);
        mDao.getCountryAndRegions(WhiteLabelApplication.getAppConfiguration().getUserInfo(myAccountActivity).getSessionKey());
    }
    //获取数据并在view中显示后,需要调此方法来处理label
    public void initAllHint() {
        if (!firstName.getText().toString().equals("")) {
            firstNameText2.setVisibility(View.VISIBLE);
        }
        if (!lastName.getText().toString().equals("")) {
            lastNameText2.setVisibility(View.VISIBLE);
        }
        if (!email.getText().toString().equals("")) {
            emailText2.setVisibility(View.VISIBLE);
        }
        if (!country.getText().toString().equals("")) {
            countryText2.setVisibility(View.VISIBLE);
        }
        if (!birthday.getText().toString().equals("")) {
            birthdayText2.setVisibility(View.VISIBLE);
        }
        if (!gender.getText().toString().equals("")) {
            genderText2.setVisibility(View.VISIBLE);
        }
        if (!monthlyIncome.getText().toString().equals("")) {
            monthlyIncomeText2.setVisibility(View.VISIBLE);
        }
        if (!zip.getText().toString().equals("")) {
            zipText2.setVisibility(View.VISIBLE);
        }
        if (!city.getText().toString().equals("")) {
            cityText2.setVisibility(View.VISIBLE);
        }
        if (!stateProvince.getText().toString().equals("")) {
            stateProvinceText2.setVisibility(View.VISIBLE);
        }
        if (!eg.getText().toString().equals("")) {
            egText2.setVisibility(View.VISIBLE);
        }
    }

    private Handler mHandler = new Handler();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo:
                //调用popupWindow 显示布局
                LayoutInflater inflater = (LayoutInflater) myAccountActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupWindowView = inflater.inflate(R.layout.fragment_myaccount_edit_info_photo, null);
                PopupWindow popupWindow = new PopupWindow(popupWindowView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //设置PopupWindow的弹出和消失效果
                popupWindow.setAnimationStyle(R.style.popupAnimation);
                Button album = (Button) popupWindowView.findViewById(R.id.album);
                album.setOnClickListener(this);
                Button photograph = (Button) popupWindowView.findViewById(R.id.photograph);
                photograph.setOnClickListener(this);
                Button cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
                cancleButton.setOnClickListener(this);
                popupWindow.showAtLocation(album, Gravity.CENTER, 0, 0);//显示并设置位置
                break;
            case R.id.album:
//                System.out.println("点击了相册按钮");
//                popupWindow.dismiss();
//                pickPhoto();
                break;
            case R.id.photograph:
//                System.out.println("点击了拍照按钮");
//                popupWindow.dismiss();
//                takePhoto();
                break;
            case R.id.cancleButton:
//                popupWindow.dismiss();
                break;
            case R.id.changePassword:
                Intent intent = new Intent(getActivity(), MyAccountChangePasswordActivity.class);
                startActivity(intent);
                ((BaseActivity)getActivity()).startActivityTransitionAnim();
                break;
            case R.id.et_account_country:
                wheelPickerCountry();
                break;
            case R.id.et_account_birthday:
                wheelPickerBirthday();
                break;
            case R.id.et_account_gender:
                wheelPickerGender();
                break;
            case R.id.et_account_monthlyIncome:
                wheelPickerMonthlyIncom();
                break;
            case R.id.et_account_stateProvince:
                wheelPickerStateProvince();
                break;
            case R.id.Phone_number:
                //  wheelPickerPhoneNumber();
                break;
            case R.id.clear_first_name:
                firstName.setText("");
                break;
            case R.id.clear_last_name:
                lastName.setText("");
                break;
            case R.id.clear_mail_name:
                email.setText("");
                break;
            case R.id.clear_code_name:
                zip.setText("");
                break;
            case R.id.clear_city_name:
                city.setText("");
                break;
            case R.id.clear_phone_name:
                eg.setText("");
                break;
        }
    }

    private String mapValueGetKey(Map<String, String> mothMap, String value) {
        if (mothMap == null || value == null) {
            return null;
        }
        Set<Map.Entry<String, String>> entriesSet = mothMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entriesSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;


    }

    //点击save后，如果焦点跑到 selecter上，则不执行获焦动作
    private boolean isSaved = false;

    private boolean cleanBoxFocus() {
        if (isSaved) {
            phoneNumber.requestFocus();
            phoneNumber.setFocusable(true);
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.et_account_firstName:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, true);
                    onFocus(firstName, firstNameText, firstNameText2, "First Name", null);
                    if (firstName.getText().length() != 0) {
                        clearFirstName.setVisibility(View.VISIBLE);
                    } else {
                        clearFirstName.setVisibility(View.GONE);
                    }
                    firstName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0) {
                                clearFirstName.setVisibility(View.VISIBLE);
                            } else {
                                clearFirstName.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.et_account_lastName:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, true);
                    onFocus(lastName, lastNameText, lastNameText2, "Last Name", null);
                    if (lastName.getText().length() != 0)
                        clearLastName.setVisibility(View.VISIBLE);
                    else
                        clearLastName.setVisibility(View.GONE);
                    lastName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0) {
                                clearLastName.setVisibility(View.VISIBLE);
                            } else {
                                clearLastName.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.et_account_email:
                    onFocus(email, emailText, emailText2, "Email", rl_editinfo_email);
                    if (email.getText().length() != 0) {
                        clearMail.setVisibility(View.VISIBLE);
                    } else {
                        clearMail.setVisibility(View.GONE);
                    }
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0)
                                clearMail.setVisibility(View.VISIBLE);
                            else
                                clearMail.setVisibility(View.GONE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.et_account_country:
                    if (cleanBoxFocus()) {
                        return;
                    }
                    onFocus(country, countryText, countryText2, "Country", rl_editinfo_country);
                    wheelPickerCountry();
                    break;
                case R.id.et_account_birthday:
                    if (cleanBoxFocus()) {
                        return;
                    }
                    onFocus(birthday, birthdayText, birthdayText2, "Birthday", rl_editinfo_birthday);
                    wheelPickerBirthday();
                    break;
                case R.id.et_account_gender:
                    if (cleanBoxFocus()) {
                        return;
                    }
                    onFocus(gender, genderText, genderText2, "Gender", rl_editinfo_gender);
                    wheelPickerGender();
                    break;
                case R.id.et_account_monthlyIncome:
                    if (cleanBoxFocus()) {
                        return;
                    }
                    onFocus(monthlyIncome, monthlyIncomeText, monthlyIncomeText2, "Monthly Income", rl_editinfo_monthly);
                    wheelPickerMonthlyIncom();
                    break;
                case R.id.et_account_zip:
                    onFocus(zip, zipText, zipText2, getActivity().getResources().getString(R.string.postal_code), rl_editinfo_postcode);
                    if (zip.getText().length() != 0) {
                        clearCode.setVisibility(View.VISIBLE);
                    } else {
                        clearCode.setVisibility(View.GONE);
                    }
                    zip.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            stateProvince.setTextColor(myAccountActivity.getResources().getColor(R.color.black000000));
                            stateProvince.setEnabled(true);
                            stateProvinceText.setEnabled(false);
                            if (s.length() != 0)
                                clearCode.setVisibility(View.VISIBLE);
                            else
                                clearCode.setVisibility(View.GONE);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
                case R.id.et_account_city:
                    onFocus(city, cityText, cityText2, "City", rl_editinfo_city);
                    if (city.getText().length() != 0) {
                        clearCity.setVisibility(View.VISIBLE);
                    } else {
                        clearCity.setVisibility(View.GONE);
                    }
                    city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //因为postCode需求，会自动填充city，所有需要判断一下是否获得焦点
                            if (s.length() != 0 && city.isFocused()) {
                                clearCity.setVisibility(View.VISIBLE);
                            } else {
                                clearCity.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.et_account_stateProvince:
                    if (cleanBoxFocus()) {
                        return;
                    }
                    // post code checkout 成功后，
                    if (stateProvince.isEnabled()) {
                        onFocus(stateProvince, stateProvinceText, stateProvinceText2, "State", rl_editinfo_state);
                        wheelPickerStateProvince();
                    }

                    break;
                case R.id.et_account_eg:
                    CustomButtomLineRelativeLayout.setBottomLineActive(v_editinfo_phone_line, true);
                    onFocus(eg, egText, egText2, getResources().getString(R.string.eg123), null);
                    if (eg.getText().length() != 0)
                        clearPhone.setVisibility(View.VISIBLE);
                    else
                        clearPhone.setVisibility(View.GONE);
                    eg.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0)
                                clearPhone.setVisibility(View.VISIBLE);
                            else
                                clearPhone.setVisibility(View.GONE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
            }
            isSaved = false;
        } else {
            onblurAll(v.getId());
            clearFirstName.setVisibility(View.GONE);
            clearLastName.setVisibility(View.GONE);
            clearMail.setVisibility(View.GONE);
            clearCode.setVisibility(View.GONE);
            clearCity.setVisibility(View.GONE);
            clearPhone.setVisibility(View.GONE);
        }
    }

    //生日
    private boolean run = false;
    private String myBrithday = "";
    private String year = "", month = "", day = "";
    private int num = 0;
    private boolean leapYear = false;
    private WheelPickerAdapter wpaLeft;
    private String value;

    public void wheelPickerBirthday() {
        AnimUtil.rotateArrow(iv_birther_arrow, true);
        rl_editinfo_birthday.setBottomLineActive(true);
        if (!birthday.getText().toString().trim().equals("")) {
            String brithdayStr[] = birthday.getText().toString().split("-");
            day = brithdayStr[0];
            month = brithdayStr[1];
            year = brithdayStr[2];
        } else {
            year = "2015";
            month = "January";
            day = "1";
        }
        value = month;
        //year
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity oldwheel = new WheelPickerEntity();
        if (year.equals("")) {
            oldwheel.setDisplay("1915");
            oldwheel.setValue("1915");
        } else {
            oldwheel.setDisplay(year);
            oldwheel.setValue(year);
        }

        for (int i = 0; i < 101; i++) {
            WheelPickerEntity w = new WheelPickerEntity();
            w.setDisplay("" + (1915 + i));
            w.setValue("" + (1915 + i));
            wheel.add(w);
            if (year.equals("" + (1915 + i))) {
                currentItem3 = i;
            }
        }

        final WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldwheel);

        ArrayList<WheelPickerEntity> wheel2 = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity oldwheel2 = new WheelPickerEntity();
        if (month.equals("")) {
            oldwheel2.setDisplay("January");
            oldwheel2.setValue("January");
        } else {
            oldwheel2.setDisplay(month);
            oldwheel2.setValue(month);
        }
        for (int i = 0; i < 12; i++) {

            WheelPickerEntity w = new WheelPickerEntity();
            w.setDisplay(brithdayMonth[i]);
            w.setValue(brithdayMonth[i]);
            wheel2.add(w);
            if (brithdayMonth[i].equals(month)) {
                currentItem2 = i;
            }
        }
        final WheelPickerConfigEntity configEntity2 = new WheelPickerConfigEntity();
        configEntity2.setArrayList(wheel2);
        configEntity2.setOldValue(oldwheel2);

        //day
        final ArrayList<WheelPickerEntity> wheel3 = new ArrayList<WheelPickerEntity>();
        final WheelPickerEntity oldwheel3 = new WheelPickerEntity();
        if (day.equals("")) {
            oldwheel3.setDisplay("1");
            oldwheel3.setValue("1");
        } else {
            oldwheel3.setDisplay(day);
            oldwheel3.setValue(day);
        }
        String str = "";
        int year1 = Integer.parseInt(year);
        if (year1 % 100 == 0) {
            leapYear = year1 % 4 == 0;
        } else {
            leapYear = year1 % 4 == 0;
        }
        //the number of days
        if (month.equals(brithdayMonth[0]) || month.equals(brithdayMonth[2]) || month.equals(brithdayMonth[4]) || month.equals(brithdayMonth[6]) || month.equals(brithdayMonth[7]) || month.equals(brithdayMonth[9]) || month.equals(brithdayMonth[11])) {
            num = 31;

        } else if (month.equals(brithdayMonth[3]) || month.equals(brithdayMonth[5]) || month.equals(brithdayMonth[8]) || month.equals(brithdayMonth[10])) {
            num = 30;

        } else if (month.equals(brithdayMonth[1]) && leapYear) {
            num = 29;

        } else {
            num = 28;
        }
        for (int i = 1; i <= num; i++) {
            WheelPickerEntity w = new WheelPickerEntity();
            String str2 = "";
            if (i < 10) {
                str2 = "0" + i;
            } else {
                str2 = "" + i;
            }

            w.setDisplay(str2);
            w.setValue(str2);
            wheel3.add(w);

            if (day.equals(str2)) {
                currentItem1 = i - 1;
            }
        }
        final WheelPickerConfigEntity configEntity3 = new WheelPickerConfigEntity();
        configEntity3.setArrayList(wheel3);
        configEntity3.setOldValue(oldwheel3);

        wpaLeft = new WheelPickerAdapter(getActivity(), configEntity3.getArrayList());

        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                birthday.clearFocus();
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                String year = newValue.getValue();
                String str2 = "";
                int yearInt = Integer.parseInt(year);
                if (yearInt % 100 == 0) {
                    leapYear = yearInt % 4 == 0;
                } else {
                    leapYear = yearInt % 4 == 0;
                }
                if (value.equals(brithdayMonth[1])) {
                    if (leapYear) {
                        num = 29;
                    } else {
                        num = 28;
                    }
                }
                wheel3.removeAll(wheel3);
                for (int i = 1; i <= num; i++) {
                    WheelPickerEntity w = new WheelPickerEntity();
                    if (i < 10) {
                        str2 = "0" + i;
                    } else {
                        str2 = "" + i;
                    }

                    w.setDisplay(str2);
                    w.setValue(str2);
                    wheel3.add(w);

                }
                currentItem1 = 0;
                configEntity3.setArrayList(wheel3);
                configEntity3.setOldValue(oldwheel3);

                // upadate days
                wpaLeft.notifyDataChangedEvent();
                wpaLeft.notifyDataInvalidatedEvent();

                JLogUtils.i("Martin1", leapYear + "");
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    myBrithday = myBrithday + "-" + year;

                } else {
                    myBrithday = myBrithday + "-" + newValue.getDisplay();
                    year = newValue.getDisplay();
                }
                //set date
                birthday.setText(day + "-" + month + "-" + year);
                birthday.clearFocus();
            }
        });

        configEntity2.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                value = newValue.getValue();
                month = value;
                JLogUtils.d("month_scroll", month);
                String str1 = "";
                if (value.equals(brithdayMonth[0]) || value.equals(brithdayMonth[2]) || value.equals(brithdayMonth[4]) || value.equals(brithdayMonth[6]) || value.equals(brithdayMonth[7]) || value.equals(brithdayMonth[9]) || value.equals(brithdayMonth[11])) {
                    num = 31;

                } else if (value.equals(brithdayMonth[3]) || value.equals(brithdayMonth[5]) || value.equals(brithdayMonth[8]) || value.equals(brithdayMonth[10])) {
                    num = 30;

                } else if (value.equals(brithdayMonth[1]) && leapYear) {
                    num = 29;

                } else {
                    num = 28;
                }
                wheel3.removeAll(wheel3);
                for (int i = 1; i <= num; i++) {
                    WheelPickerEntity w = new WheelPickerEntity();
                    if (i < 10) {
                        str1 = "0" + i;
                    } else {
                        str1 = "" + i;
                    }
                    w.setDisplay(str1);
                    w.setValue(str1);
                    wheel3.add(w);
                }
                currentItem1 = 0;
                configEntity3.setArrayList(wheel3);
                configEntity3.setOldValue(oldwheel3);

                wpaLeft.notifyDataChangedEvent();
                wpaLeft.notifyDataInvalidatedEvent();
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    myBrithday = myBrithday + "-" + month;
                } else {
                    myBrithday = myBrithday + "-" + newValue.getDisplay();
                    month = newValue.getDisplay();
                }
                birthday.setText(day + "-" + month + "-" + year);
            }
        });
        configEntity3.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                wpaLeft.notifyDataChangedEvent();
                wpaLeft.notifyDataInvalidatedEvent();

            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    myBrithday = day;
                } else {
                    myBrithday = newValue.getDisplay();
                    day = newValue.getDisplay();
                }
                birthday.setText(day + "-" + month + "-" + year);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JViewUtils.showWheelPickerThreeDialog2(myAccountActivity, wpaLeft, configEntity3, currentItem1, configEntity2, currentItem2, configEntity, currentItem3);
            }
        }, 260);
    }

    //性别
    public void wheelPickerGender() {
        rl_editinfo_gender.setBottomLineActive(true);
        AnimUtil.rotateArrow(iv_gender_arrow, true);
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity ww0 = new WheelPickerEntity();
        ww0.setDisplay("Please select");
        ww0.setValue("0");
        WheelPickerEntity ww1 = new WheelPickerEntity();
        ww1.setDisplay("Male");
        ww1.setValue("1");
        WheelPickerEntity ww2 = new WheelPickerEntity();
        ww2.setDisplay("Female");
        ww2.setValue("2");
        wheel.add(ww0);
        wheel.add(ww1);
        wheel.add(ww2);

        WheelPickerEntity oldWheel = new WheelPickerEntity();
        int genderNumber;
        if ("Please select".equals(gender.getText().toString().trim()) || "".equals(gender.getText().toString().trim())) {
            genderNumber = 0;
        } else {
            genderNumber = gender.getText().toString().trim().equals("Male") ? 1 : 2;
        }
        if ("Please select".equals(gender.getText().toString().trim()) || "".equals(gender.getText().toString().trim())) {
            oldWheel.setIndex(0);
            oldWheel.setDisplay("");
            oldWheel.setValue("0");

        } else {
            oldWheel.setIndex(genderNumber);
            oldWheel.setDisplay(gender.getText().toString().trim());
            oldWheel.setValue(String.valueOf(genderNumber));
        }

        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldWheel);
        configEntity.setIndex(oldWheel.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                gender.clearFocus();
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    gender.setText(oldValue.getDisplay());
                } else {
                    if (newValue.getValue().equals("0")) {
                        gender.setText("");
                    } else {
                        gender.setText(newValue.getDisplay());
                    }
                }

                gender.clearFocus();
            }
        });
        JViewUtils.showWheelPickerOneDialog(myAccountActivity, configEntity);
    }

    //Country
    ArrayList<CountrySubclass> countryValue = null;

    public void wheelPickerCountry() {
        AnimUtil.rotateArrow(iv_country_arrow, true);
        rl_editinfo_country.setBottomLineActive(true);
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        WheelPickerEntity oldwheel = new WheelPickerEntity();
        String oldlabel = country.getText().toString().trim();
//        oldlabel = oldlabel.equals("") ? "Malaysia" : oldlabel;
        if (sharedCountry == null) {
            sharedCountry = myAccountActivity.getSharedPreferences("country", Activity.MODE_PRIVATE);
        }
        String oldvalue = sharedCountry.getString(oldlabel, "");
        oldwheel.setDisplay(oldlabel);
        oldwheel.setValue(oldvalue);
        int countrySize = sharedCountry.getInt("size", 0);


        for (int i = 0; i < countrySize; i++) {
            WheelPickerEntity ww = new WheelPickerEntity();
            String label = sharedCountry.getString("" + i, "");
            String value = sharedCountry.getString(label, "");
            ww.setDisplay(label);
            ww.setValue(value);
            wheel.add(ww);
            if (country.getText().toString().trim().equals(label)) {
                oldwheel.setIndex(i);
            }
        }

        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldwheel);
        configEntity.setIndex(oldwheel.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                countryText2.setTextColor(JToolUtils.getColor(R.color.label_saved));//设置为灰色
                rl_editinfo_country.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_country_arrow, false);
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                countryText2.setTextColor(JToolUtils.getColor(R.color.label_saved));//设置为灰色
                rl_editinfo_country.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_country_arrow, false);
                String shengfen_str = "";
                if (newValue.getDisplay() == null) {
                    country.setText(oldValue.getDisplay());
                    shengfen_str = oldValue.getDisplay();
                } else {
                    country.setText(newValue.getDisplay());
                    shengfen_str = newValue.getDisplay();
                    stateProvince.setText("");
                }
                int shengfen = sharedCountry.getInt(shengfen_str + "size", 0);
                sharedCountry.edit().putInt("stateSize", shengfen).apply();
            }
        });
        JViewUtils.showWheelPickerOneDialog(myAccountActivity, configEntity);
    }

    //stateProvince
    public void wheelPickerStateProvince() {
        AnimUtil.rotateArrow(iv_state_arrow, true);
        rl_editinfo_state.setBottomLineActive(true);
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        String oldLabel = stateProvince.getText().toString().trim();
        if (sharedCountry == null) {
            //Country数据存取
            sharedCountry = myAccountActivity.getSharedPreferences("country", Activity.MODE_PRIVATE);
        }
        String oldValue = sharedCountry.getString(oldLabel, "");
        WheelPickerEntity oldwheel = new WheelPickerEntity();
        oldwheel.setDisplay(oldLabel);
        oldwheel.setValue(oldValue);

        int StateProvinceSize = sharedCountry.getInt(country.getText().toString().trim() + "size", 0);
        WheelPickerEntity fistWheel = new WheelPickerEntity();
        fistWheel.setDisplay(getString(R.string.pleaseselect));
        fistWheel.setValue(getString(R.string.pleaseselect));
        wheel.add(fistWheel);
        for (int i = 0; i < StateProvinceSize; i++) {
            WheelPickerEntity ww = new WheelPickerEntity();
            String label = sharedCountry.getString("stateProvince" + i, "");
            String value = sharedCountry.getString(label, "");
            ww.setDisplay(label);
            ww.setValue(value);
            wheel.add(ww);
            if (label.equals(stateProvince.getText().toString().trim())) {
                oldwheel.setIndex(i + 1);
            }

        }


        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldwheel);
        configEntity.setIndex(oldwheel.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                stateProvince.clearFocus();
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Martin", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {

                if (newValue.getDisplay() == null) {
                    stateProvince.setText(oldValue.getDisplay());
                } else {
                    if (newValue.getDisplay().equals(getString(R.string.pleaseselect))) {
                        stateProvince.setText("");
                        stateProvince.setHint("");
                    } else {
                        stateProvince.setText(newValue.getDisplay());
                    }
                }

                stateProvince.clearFocus();
            }
        });
        JViewUtils.showWheelPickerOneDialog(myAccountActivity, configEntity);
    }

    // Monthly income
    ArrayList<MonthlyIncomList> incomList;

    public void wheelPickerMonthlyIncom() {
        AnimUtil.rotateArrow(iv_monthly_arrow, true);
        rl_editinfo_monthly.setBottomLineActive(true);
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
//                String oldLabel=sharedIncome.getString("" + 0, "");
//                String oldValue=sharedIncome.getString(oldLabel, "");
        String oldLabel = monthlyIncome.getText().toString().trim();

        if (sharedIncome == null) {
            sharedIncome = myAccountActivity.getSharedPreferences("mothleIncome", Activity.MODE_PRIVATE);
        }
        String oldValue = sharedIncome.getString(oldLabel, "");
        WheelPickerEntity oldWheel = new WheelPickerEntity();
        oldWheel.setDisplay(oldLabel);
        oldWheel.setValue(oldValue);
        if ("".equals(oldLabel)) {
            oldWheel.setDisplay(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+"0-2000");
            oldWheel.setValue(WhiteLabelApplication.getAppConfiguration().getCurrency().getName()+" "+" 0-2000");
        }
        int incomeSize = sharedIncome.getInt("size", 0);

//        WheelPickerEntity fistWheel = new WheelPickerEntity();
//        fistWheel.setDisplay(getString(R.string.pleaseselect));
//        fistWheel.setValue(getString(R.string.pleaseselect));
//        wheel.add(fistWheel);
        for (int i = 0; i < incomeSize; i++) {
            WheelPickerEntity ww = new WheelPickerEntity();
            String label = sharedIncome.getString("" + i, "");
            String value = sharedIncome.getString(label, "");
            ww.setDisplay(label);
            ww.setValue(value);
            wheel.add(ww);
            if (label.equals(monthlyIncome.getText().toString().trim())) {
                oldWheel.setIndex(i);
            }
        }


        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(oldWheel);
        configEntity.setIndex(oldWheel.getIndex());
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                monthlyIncome.clearFocus();
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                if (newValue.getDisplay() == null) {
                    monthlyIncome.setText(oldValue.getDisplay());
                } else {
                    if (newValue.getDisplay().equals(getString(R.string.pleaseselect))) {
                        monthlyIncome.setText("");
                    } else {
                        monthlyIncome.setText(newValue.getDisplay());
                    }
                }
                monthlyIncome.clearFocus();
            }
        });
        JViewUtils.showWheelPickerOneDialog(myAccountActivity, configEntity);
    }

    //PhoneNumber
    ArrayList<PhoneNumberList> phoneNumberList;


    public void wheelPicker(final EditText edit) {
        ArrayList<WheelPickerEntity> wheel = new ArrayList<WheelPickerEntity>();
        for (int index = 0; index < 10; ++index) {
            WheelPickerEntity ww = new WheelPickerEntity();
            ww.setDisplay("Size - M - " + index);
            ww.setValue("M - " + index);
            wheel.add(ww);
        }

        WheelPickerEntity ww = new WheelPickerEntity();
        ww.setDisplay("Size - M - ");
        ww.setValue("M - ");

        WheelPickerConfigEntity configEntity = new WheelPickerConfigEntity();
        configEntity.setArrayList(wheel);
        configEntity.setOldValue(ww);
        configEntity.setCallBack(new WheelPickerCallback() {
            @Override
            public void onCancel() {
                JLogUtils.i("Martin", "onCancel()");
            }

            @Override
            public void onScrolling(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Martin", "onScrolling() -- oldValue => " + oldValue + "  newValue => " + newValue);
            }

            @Override
            public void onDone(WheelPickerEntity oldValue, WheelPickerEntity newValue) {
                JLogUtils.i("Martin", "onDone() -- oldValue => " + oldValue + "  newValue => " + newValue);
                if (newValue.getDisplay() == null) {
                    edit.setText(getResources().getString(R.string.size_m));
                } else {
                    edit.setText(newValue.getDisplay());
                }
            }
        });
        JViewUtils.showWheelPickerOneDialog(myAccountActivity, configEntity);
    }

    //According to the location of widget,scroll to top or bottom.
    public void srollto() {
        JLogUtils.d("firstNameText2", location[1] + " " + height);
        if (height > location[1]) {
            myScrollView.scrollTo(0, 0);
        } else {
            myScrollView.scrollTo(0, myScrollView.getHeight());
        }
    }

    public boolean onblurAll(int id) {
        switch (id) {
            case R.id.et_account_firstName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, false);
                firstNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                firstNameText2.setVisibility(View.VISIBLE);
                if (firstName.getText().toString().trim().equals("")) {
                    firstName.setHint(getResources().getString(R.string.first_name));
                    firstNameText2.getLocationOnScreen(location);
                    srollto();

                    firstNameText.clearAnimation();
                    //验证字段
                    firstNameText2.setText(getResources().getString(R.string.required_field));
                    firstNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    firstNameText.clearAnimation();
                }
                break;
            case R.id.et_account_lastName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, false);
                lastNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                lastNameText2.setVisibility(View.VISIBLE);
                if (lastName.getText().toString().trim().equals("")) {
                    lastName.setHint("Last Name");

                    lastNameText2.getLocationOnScreen(location);
                    srollto();

                    lastNameText.clearAnimation();
                    //验证字段
                    lastNameText2.setText(getResources().getString(R.string.required_field));
                    lastNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    lastNameText.clearAnimation();
                }
                break;
            case R.id.et_account_email:
                rl_editinfo_email.setBottomLineActive(false);
                emailText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                emailText2.setVisibility(View.VISIBLE);
                if (email.getText().toString().trim().equals("")) {
                    email.setHint("Email");

                    emailText2.getLocationOnScreen(location);
                    srollto();
                    emailText.clearAnimation();
                    //验证字段
                    emailText2.setText(getResources().getString(R.string.required_field));
                    emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    emailText.clearAnimation();
                    //验证邮箱格式
                    if (!JDataUtils.isEmail(email.getText().toString())) {
                        emailText2.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                        emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;
            case R.id.et_account_country:
                rl_editinfo_country.setBottomLineActive(false);
                countryText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                countryText2.setVisibility(View.VISIBLE);
                if (country.getText().toString().trim().equals("")) {
                    country.setHint("Country");

                    countryText2.getLocationOnScreen(location);
                    srollto();

                    countryText.clearAnimation();
                    //验证字段
                    countryText2.setText(getResources().getString(R.string.select_country));
                    countryText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    countryText.clearAnimation();
                }
                break;
            case R.id.et_account_birthday:
                rl_editinfo_birthday.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_birther_arrow, false);
                birthdayText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                birthdayText2.setVisibility(View.VISIBLE);
//                birthdayText.clearAnimation();
//                return true;
                if (birthday.getText().toString().trim().equals("")) {
                    birthday.setHint("Birthday");
                    birthdayText.clearAnimation();
                    //验证字段
                    birthdayText2.setVisibility(View.INVISIBLE);
                    genderText2.getLocationOnScreen(location);
                    srollto();
                    genderText.clearAnimation();
//                    birthdayText2.setText("This is a required field.");
                    //     birthdayText2.setTextColor(getResources().getColor(R.color.redC2060A));

                } else {
                    birthdayText.clearAnimation();
                }
                break;
            case R.id.et_account_gender:
                rl_editinfo_gender.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_gender_arrow, false);
                genderText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                genderText2.setVisibility(View.VISIBLE);
                if (gender.getText().toString().trim().equals("")) {
                    gender.setHint("Gender");

                    genderText2.setVisibility(View.INVISIBLE);
                    genderText2.getLocationOnScreen(location);
                    srollto();

                    genderText.clearAnimation();
                } else {
                    genderText.clearAnimation();
                }
                break;
            case R.id.et_account_monthlyIncome:
                rl_editinfo_monthly.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_monthly_arrow, false);
                monthlyIncomeText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                monthlyIncomeText2.setVisibility(View.VISIBLE);
                monthlyIncomeText.clearAnimation();
//                return true;
                if (monthlyIncome.getText().toString().trim().equals("")) {
                    monthlyIncome.setHint("Monthly Income");
                    monthlyIncomeText.clearAnimation();
                    //验证字段
                    monthlyIncomeText2.setVisibility(View.INVISIBLE);
//                    monthlyIncomeText2.setText("Please select an option.");
//                    monthlyIncomeText2.setTextColor(getResources().getColor(R.color.redC2060A));
                } else {
                    monthlyIncomeText.clearAnimation();
                }
                break;
            case R.id.et_account_zip:
                rl_editinfo_postcode.setBottomLineActive(false);
                zipText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                zipText2.setVisibility(View.VISIBLE);
                if (zip.getText().toString().trim().equals("")) {
                    zipText2.getLocationOnScreen(location);
                    srollto();
                    zip.setHint(getResources().getString(R.string.postal_code));
                    zipText.clearAnimation();
                    //验证字段
                    zipText2.setText(getResources().getString(R.string.zip_code));
                    zipText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    zipText.clearAnimation();
//                    sendRequestToGetCityAndStateByPostCode(zip.getText().toString().trim());
                }
                break;
            case R.id.et_account_city:
                rl_editinfo_city.setBottomLineActive(false);
                cityText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                cityText2.setVisibility(View.VISIBLE);
                if (city.getText().toString().trim().equals("")) {

                    cityText2.getLocationOnScreen(location);
                    srollto();
                    city.setHint("City");
                    cityText.clearAnimation();
                    //验证字段
                    cityText2.setText(getResources().getString(R.string.enter_city));
                    cityText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    cityText.clearAnimation();
                }
                break;
            case R.id.et_account_stateProvince:
                rl_editinfo_state.setBottomLineActive(false);
                AnimUtil.rotateArrow(iv_state_arrow, false);
                stateProvinceText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                stateProvinceText2.setVisibility(View.VISIBLE);
                if (stateProvince.getText().toString().trim().equals("")) {
                    stateProvinceText2.setVisibility(View.INVISIBLE);
                    stateProvince.setHint("State");

                    stateProvinceText2.getLocationOnScreen(location);
                    srollto();
                    stateProvinceText.clearAnimation();
                } else {
                    stateProvinceText.clearAnimation();
                }
                break;
            case R.id.et_account_eg:
                CustomButtomLineRelativeLayout.setBottomLineActive(v_editinfo_phone_line, false);
                egText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                egText2.setVisibility(View.VISIBLE);
                if (eg.getText().toString().trim().equals("")) {
                    eg.setHint(getResources().getString(R.string.eg123));


                    egText2.getLocationOnScreen(location);
                    srollto();

                    egText.clearAnimation();
                    //验证字段
                    egText2.setText(getResources().getString(R.string.required_field));
                    egText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    egText.clearAnimation();
                }
                break;
        }
        return true;
    }

//    private void sendRequestToGetCityAndStateByPostCode(String postcode) {
//        city.setEnabled(false);
//        mDao.sendRequestToGetCityAndStateByPostCode(WhiteLabelApplication.getAppConfiguration().getUserInfo(getActivity()).getSessionKey(), postcode, country.getTag() == null ? "MY" : country.getTag().toString());
//
//    }

    public void onFocus(EditText edit, TextView text, TextView text2, String hint, CustomButtomLineRelativeLayout relativeLayout) {
        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);
        //上移高度应该为自身的高度
        int textHeight = text.getHeight();
        Animation tran;
        if (textHeight > 0) {
            tran = new TranslateAnimation(0, 0, 0, 0 - textHeight);
        } else {
            tran = new TranslateAnimation(0, 0, 0, -50);
        }
        //平移
        tran.setDuration(300);
        //渐变
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        set.addAnimation(tran);
        set.addAnimation(alpha);
        text2.setText(hint);
        text2.setVisibility(View.VISIBLE);
        if (edit.getText().toString().trim().equals("")) {
            text2.setVisibility(View.INVISIBLE);
            edit.setHint("");
            text.startAnimation(set);
        } else {
            text2.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        }
        if (relativeLayout != null) {
            relativeLayout.setBottomLineActive(true);
        }
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {

    }

    @Override
    public void onStart() {
        super.onStart();
//        EasyTracker easyTracker = EasyTracker.getInstance(myAccountActivity);
//        easyTracker.send(MapBuilder.createEvent("Screen View", // Event category (required)
//                "Edit Profile Screen", // Event action (required)
//                null, // Event label
//                null) // Event value
//                .build());
        GaTrackHelper.getInstance().googleAnalytics("Edit Profile Screen", myAccountActivity);
        JLogUtils.i("googleGA_screen", "Edit Profile Screen");
    }
}

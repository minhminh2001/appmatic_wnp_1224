package com.whitelabel.app.ui.checkout;

import com.common.utils.DialogUtils;
import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.data.model.RegisterRequest;
import com.whitelabel.app.fragment.LoginRegisterEmailLoginFragment;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.ui.productdetail.ProductDetailActivity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by Ray on 2018/3/13.
 */

public class CheckoutRegisterFragment extends BaseFragment<CheckoutRegisterContract.Presenter>
    implements CheckoutRegisterContract.View, View.OnFocusChangeListener, View.OnClickListener {

    private final static String SUBSCRIBED = "0";

    private View contentView;

    private EditText firstName, lastName, email, et_phone_number, password, re_password;

    private TextView firstNameText, lastNameText, emailText, tv_phone_text, passwordText,
        re_passwordText;

    private TextView firstNameText2, lastNameText2, emailText2, tv_phone_text2, passwordText2,
        re_passwordText2;

    private ImageView img1, img2, img3, img4, img5, iv_phone_correct, clearFirst, clearLastName,
        clearMail, iv_clear_phone, clearPassword, clearRePassword;

    private CustomButtomLineRelativeLayout rl_register_email, rl_phone_number, rl_register_pwd,
        rl_register_repwd;

    private View view_firstname_line, view_lastname_line;

    private TextView tvLogin;

    private Dialog progressDialog;

    private final static int REQUEST_CODE=2000;

    private CheckoutRegisterCallBack checkoutRegisterCallBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_checkout_register, container, false);
        initView();
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        tvLogin = (TextView) contentView.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(this);
        tvLogin.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        tvLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        rl_register_email = (CustomButtomLineRelativeLayout) contentView
            .findViewById(R.id.rl_register_email);
        rl_phone_number = (CustomButtomLineRelativeLayout) contentView
            .findViewById(R.id.rl_phone_number);
        rl_register_pwd = (CustomButtomLineRelativeLayout) contentView
            .findViewById(R.id.rl_register_pwd);
        rl_register_repwd = (CustomButtomLineRelativeLayout) contentView
            .findViewById(R.id.rl_register_repwd);
        view_firstname_line = contentView.findViewById(R.id.view_firstname_line);
        view_lastname_line = contentView.findViewById(R.id.view_lastname_line);
        firstNameText = (TextView) contentView.findViewById(R.id.firstName_text);
        lastNameText = (TextView) contentView.findViewById(R.id.lastName_text);
        emailText = (TextView) contentView.findViewById(R.id.email_text);
        tv_phone_text = (TextView) contentView.findViewById(R.id.tv_phone_text);
        passwordText = (TextView) contentView.findViewById(R.id.password_text);
        re_passwordText = (TextView) contentView.findViewById(R.id.re_password_text);
        firstName = (EditText) contentView.findViewById(R.id.firstName);
        lastName = (EditText) contentView.findViewById(R.id.lastName);
        email = (EditText) contentView.findViewById(R.id.email);
        et_phone_number = (EditText) contentView.findViewById(R.id.et_phone_number);
        password = (EditText) contentView.findViewById(R.id.password);
        re_password = (EditText) contentView.findViewById(R.id.re_password);
        firstNameText2 = (TextView) contentView.findViewById(R.id.firstName_text2);
        lastNameText2 = (TextView) contentView.findViewById(R.id.lastName_text2);
        emailText2 = (TextView) contentView.findViewById(R.id.email_text2);
        tv_phone_text2 = (TextView) contentView.findViewById(R.id.tv_phone_text2);
        passwordText2 = (TextView) contentView.findViewById(R.id.password_text2);
        re_passwordText2 = (TextView) contentView.findViewById(R.id.re_password_text2);
        img1 = (ImageView) contentView.findViewById(R.id.img1);
        img2 = (ImageView) contentView.findViewById(R.id.img2);
        img3 = (ImageView) contentView.findViewById(R.id.img3);
        img4 = (ImageView) contentView.findViewById(R.id.img4);
        img5 = (ImageView) contentView.findViewById(R.id.img5);
        iv_phone_correct = (ImageView) contentView.findViewById(R.id.iv_phone_correct);
        clearFirst = (ImageView) contentView.findViewById(R.id.clearFirst);
        clearFirst.setOnClickListener(this);
        clearLastName = (ImageView) contentView.findViewById(R.id.clearLastName);
        clearLastName.setOnClickListener(this);
        clearMail = (ImageView) contentView.findViewById(R.id.clearMail);
        clearMail.setOnClickListener(this);
        iv_clear_phone = (ImageView) contentView.findViewById(R.id.iv_clear_phone);
        iv_clear_phone.setOnClickListener(this);
        clearPassword = (ImageView) contentView.findViewById(R.id.clearPassword);
        clearPassword.setOnClickListener(this);
        clearRePassword = (ImageView) contentView.findViewById(R.id.clearRePassword);
        clearRePassword.setOnClickListener(this);
        //焦点监听
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        et_phone_number.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        re_password.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {//输入动画
        if (hasFocus) {
            AnimationSet set = new AnimationSet(true);
            set.setFillAfter(true);
            int textHeight = firstNameText.getHeight();
            Animation tran;
            if (textHeight > 0) {
                tran = new TranslateAnimation(0, 0, 0, 0 - textHeight);
            } else {
                tran = new TranslateAnimation(0, 0, 0, -50);
            }
            tran.setDuration(300);
            Animation alpha = new AlphaAnimation(0, 1);
            alpha.setDuration(300);
            set.addAnimation(tran);
            set.addAnimation(alpha);
            switch (v.getId()) {
                case R.id.firstName:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, true);
                    firstNameText.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());
                    firstNameText2.setText(getResources().getString(R.string.first_name));
                    if (firstName.getText().length() != 0) {
                        clearFirst.setVisibility(View.VISIBLE);
                    } else {
                        clearFirst.setVisibility(View.GONE);
                    }
                    if (firstName.getText().toString().trim().equals("")) {
                        firstNameText2.setVisibility(View.INVISIBLE);
                        firstName.setHint("");
                        firstNameText.startAnimation(set);
                    } else {
                        firstNameText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img1.setVisibility(View.INVISIBLE);
                    firstName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && firstName.isFocused()) {
                                clearFirst.setVisibility(View.VISIBLE);
                            } else {
                                clearFirst.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
                case R.id.lastName:
                    lastNameText.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());

                    CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, true);
                    lastNameText2.setText(getResources().getString(R.string.last_name));
                    if (lastName.getText().length() != 0) {
                        clearLastName.setVisibility(View.VISIBLE);
                    } else {
                        clearLastName.setVisibility(View.GONE);
                    }
                    if (lastName.getText().toString().trim().equals("")) {
                        lastNameText2.setVisibility(View.INVISIBLE);
                        lastName.setHint("");
                        lastNameText.startAnimation(set);
                    } else {
                        lastNameText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img2.setVisibility(View.INVISIBLE);
//                    img2_error.setVisibility(View.INVISIBLE);
                    lastName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && lastName.isFocused()) {
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
                case R.id.email:
                    //down(xml top) customtextview
                    emailText.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());
                    //parent relativeLayout
                    rl_register_email.setBottomLineActive(true);
                    //top(xml down) customtextview
                    emailText2.setText(
                        getResources().getString(R.string.loginregister_emailbound_email_hint));
                    //customEdit
                    if (email.getText().length() != 0)
                        clearMail.setVisibility(View.VISIBLE);
                    else
                        clearMail.setVisibility(View.GONE);
                    if (email.getText().toString().trim().equals("")) {
                        emailText2.setVisibility(View.INVISIBLE);
                        email.setHint("");
                        emailText.startAnimation(set);
                    } else {
                        emailText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img3.setVisibility(View.INVISIBLE);
//                    img3_error.setVisibility(View.INVISIBLE);
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && email.isFocused()) {
                                clearMail.setVisibility(View.VISIBLE);
                            } else {
                                clearMail.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
                case R.id.et_phone_number:
                    tv_phone_text.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());
                    rl_phone_number.setBottomLineActive(true);
                    tv_phone_text.setText(getResources().getString(R.string.enter_phone_number));
                    tv_phone_text2.setText(getResources().getString(R.string.enter_phone_number));
                    if (et_phone_number.getText().length() != 0) {
                        iv_clear_phone.setVisibility(View.VISIBLE);
                    } else {
                        iv_clear_phone.setVisibility(View.GONE);
                    }
                    if (et_phone_number.getText().toString().trim().equals("")) {
                        tv_phone_text2.setVisibility(View.INVISIBLE);
                        et_phone_number.setHint("");
                        tv_phone_text.startAnimation(set);
                    } else {
                        tv_phone_text2.setTextColor(
                            ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    }
                    iv_phone_correct.setVisibility(View.INVISIBLE);
                    et_phone_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && et_phone_number.isFocused()) {
                                iv_clear_phone.setVisibility(View.VISIBLE);
                            } else {
                                iv_clear_phone.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case R.id.password:
                    passwordText.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());

                    rl_register_pwd.setBottomLineActive(true);
                    passwordText2.setText(getResources().getString(R.string.enter_password));
                    if (password.getText().length() != 0)
                        clearPassword.setVisibility(View.VISIBLE);
                    else
                        clearPassword.setVisibility(View.GONE);
                    if (password.getText().toString().trim().equals("")) {
                        passwordText2.setVisibility(View.INVISIBLE);
                        password.setHint("");
                        passwordText.startAnimation(set);
                    } else {
                        passwordText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img4.setVisibility(View.INVISIBLE);
//                    img4_error.setVisibility(View.INVISIBLE);
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && password.isFocused()) {
                                clearPassword.setVisibility(View.VISIBLE);
                            } else {
                                clearPassword.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.re_password:
                    re_passwordText.setTextColor(
                        WhiteLabelApplication.getAppConfiguration().getThemeConfig()
                            .getTheme_color());

                    rl_register_repwd.setBottomLineActive(true);
                    re_passwordText2.setText(getResources().getString(R.string.retype_password));
                    if (re_password.getText().length() != 0)
                        clearRePassword.setVisibility(View.VISIBLE);
                    else
                        clearRePassword.setVisibility(View.GONE);
                    if (re_password.getText().toString().trim().equals("")) {
                        re_passwordText2.setVisibility(View.INVISIBLE);
                        re_password.setHint("");
                        re_passwordText.startAnimation(set);
                    } else {
                        re_passwordText2.setTextColor(
                            ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    }
                    img5.setVisibility(View.INVISIBLE);
                    re_password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                            if (s.length() != 0 && re_password.isFocused()) {
                                clearRePassword.setVisibility(View.VISIBLE);
                            } else {
                                clearRePassword.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
            }
        } else {//所有的验证都放到了失去焦点事件上
            onblur(v.getId());
            clearFirst.setVisibility(View.GONE);
            clearLastName.setVisibility(View.GONE);
            clearMail.setVisibility(View.GONE);
            iv_clear_phone.setVisibility(View.GONE);
            clearPassword.setVisibility(View.GONE);
            clearRePassword.setVisibility(View.GONE);
        }
    }

    public boolean onblur(int id) {
        switch (id) {
            case R.id.firstName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line, false);
                firstNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                firstNameText2.setVisibility(View.VISIBLE);
                if (firstName.getText().toString().trim().equals("")) {
                    firstName.setHint("First Name");
                    firstNameText.clearAnimation();
                    //验证字段
                    img1.setVisibility(View.INVISIBLE);
                    firstNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    firstNameText2.setText(getResources().getString(R.string.required_field));
                    return false;
                } else {
                    firstNameText.clearAnimation();
                }
                break;
            case R.id.lastName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line, false);
                lastNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                lastNameText2.setVisibility(View.VISIBLE);
                if (lastName.getText().toString().trim().equals("")) {
                    lastName.setHint("Last Name");
                    lastNameText.clearAnimation();
                    img2.setVisibility(View.INVISIBLE);
                    lastNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    lastNameText2.setText(getResources().getString(R.string.required_field));
                    return false;
                } else {
                    lastNameText.clearAnimation();

                }
                break;
            case R.id.email:
                rl_register_email.setBottomLineActive(false);
                emailText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                emailText2.setVisibility(View.VISIBLE);
                if (email.getText().toString().trim().equals("")) {
                    email.setHint(getResources().getString(R.string.enter_email_address));
                    emailText.clearAnimation();
                    //验证字段
//                    img3_error.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.INVISIBLE);
                    emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    emailText2.setText(getResources().getString(R.string.required_field));
                    return false;
                } else {
                    emailText.clearAnimation();
                    //验证字段
                    if (!JDataUtils.isEmail(email.getText().toString())) {
                        img3.setVisibility(View.INVISIBLE);
                        //邮箱格式不正确
                        emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        emailText2.setText(getResources()
                            .getString(R.string.loginregister_emailbound_tips_error_email_format));
                        return false;
                    }
                }
                break;
            case R.id.et_phone_number:
                rl_phone_number.setBottomLineActive(false);
                tv_phone_text2
                    .setTextColor(ContextCompat.getColor(getActivity(), R.color.label_saved));
                tv_phone_text2.setVisibility(View.VISIBLE);
                if (et_phone_number.getText().toString().trim().equals("")) {
                    et_phone_number.setHint(getResources().getString(R.string.enter_phone_number));
                    tv_phone_text.clearAnimation();

//                    img3_error.setVisibility(View.VISIBLE);
                    iv_phone_correct.setVisibility(View.INVISIBLE);
                    tv_phone_text2
                        .setTextColor(ContextCompat.getColor(getActivity(), R.color.redC2060A));
                    tv_phone_text2.setText(getResources().getString(R.string.required_field));
                    return false;
                } else {
                    tv_phone_text.clearAnimation();

                    if (!JDataUtils.isPhoneNumber(et_phone_number.getText().toString())) {
//                        img3_error.setVisibility(View.VISIBLE);
                        iv_phone_correct.setVisibility(View.INVISIBLE);
                        tv_phone_text2
                            .setTextColor(ContextCompat.getColor(getActivity(), R.color.redC2060A));
                        tv_phone_text2
                            .setText(getResources().getString(R.string.address_phone_error_hint));
                        return false;
                    }
                }
                break;
            case R.id.password:
                rl_register_pwd.setBottomLineActive(false);
                passwordText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                passwordText2.setVisibility(View.VISIBLE);
                if (password.getText().toString().trim().equals("")) {
                    password.setHint(getResources().getString(R.string.enter_password));
                    passwordText.clearAnimation();
                    //验证字段
//                    img4_error.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.INVISIBLE);
                    passwordText2.setText(getResources().getString(R.string.required_field));
                    passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    passwordText.clearAnimation();
                    //验证字段
                    if (!JDataUtils.isPassword(password)) {
                        img4.setVisibility(View.INVISIBLE);
                        passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        passwordText2
                            .setText(getResources().getString(R.string.enter_characters_ignored));
                        return false;
                    }
                }
                break;
            case R.id.re_password:
                rl_register_repwd.setBottomLineActive(false);
                re_passwordText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                re_passwordText2.setVisibility(View.VISIBLE);
                if (re_password.getText().toString().trim().equals("")) {
                    re_password.setHint("Re-type Password");
                    re_passwordText.clearAnimation();
                    img5.setVisibility(View.INVISIBLE);
                    re_passwordText2.setText(getResources().getString(R.string.required_field));
                    re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    re_passwordText.clearAnimation();
                    if (!JDataUtils.isPassword(re_password)) {
                        img5.setVisibility(View.INVISIBLE);
                        re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        re_passwordText2
                            .setText(getResources().getString(R.string.enter_characters_ignored));
                        return false;
                    } else if (!re_password.getText().toString().trim()
                        .equals(password.getText().toString().trim())) {
                        img5.setVisibility(View.INVISIBLE);
                        re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        re_passwordText2.setText(getResources().getString(R.string.password_match));
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearFirst:
                firstName.setText("");
                break;
            case R.id.clearLastName:
                lastName.setText("");
                break;
            case R.id.clearMail:
                email.setText("");
                break;
            case R.id.iv_clear_phone:
                et_phone_number.setText("");
                break;
            case R.id.clearRePassword:
                re_password.setText("");
                break;
            case R.id.clearPassword:
                password.setText("");
                break;
            case R.id.tv_login:
                startLoginActivity();
                break;
        }
    }

    @Override
    public void addBatchShoppingSuccess() {

        if(progressDialog != null){
            progressDialog.cancel();
        }

        checkoutRegisterCallBack.switchNextFragment();
    }

    @Override
    public void showErrorMessage(String errorMsg) {

        if(progressDialog != null) {
            progressDialog.cancel();
        }

        DialogUtils.showDialog(getActivity(), "", errorMsg, getResources().getString(R.string.ok),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            },
            "", null);
    }

    @Override
    public void loginSuccess(
        SVRAppServiceCustomerLoginReturnEntity svrAppServiceCustomerLoginReturnEntity) {
        if (getActivity() == null) {
            return;
        }
        WhiteLabelApplication.getAppConfiguration()
            .signIn(getActivity(), svrAppServiceCustomerLoginReturnEntity);
    }

    @Override
    public void inject() {
        super.inject();
        DaggerPresenterComponent1.builder()
            .applicationComponent(WhiteLabelApplication.getApplicationComponent()).
            presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }

    public void requestResister() {

        progressDialog = DialogUtils.showProgressDialog(getActivity());

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName(firstName.getText().toString());
        registerRequest.setLastName(lastName.getText().toString());
        registerRequest.setEmail(email.getText().toString());
        registerRequest.setPassword(password.getText().toString());
        registerRequest.setPhone(et_phone_number.getText().toString());
        registerRequest.setSubscribed(SUBSCRIBED);
        registerRequest
            .setDeviceToken(WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
        mPresenter.registerEmail(registerRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CheckoutRegisterCallBack) {
            checkoutRegisterCallBack= (CheckoutRegisterCallBack) context;
        }
    }

    private void startLoginActivity() {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginRegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            getActivity().overridePendingTransition(R.anim.enter_bottom_top, R.anim.exit_bottom_top);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&& LoginRegisterEmailLoginFragment.RESULTCODE==resultCode){
            ((CheckoutActivity)getActivity()).openSelectFragment();
        }
    }

    interface CheckoutRegisterCallBack {
        void switchNextFragment();
    }
}

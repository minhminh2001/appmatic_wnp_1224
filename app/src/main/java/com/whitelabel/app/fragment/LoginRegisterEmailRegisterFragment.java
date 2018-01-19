package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.Toast;

import com.whitelabel.app.Const;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.activity.RegisterToHelpCenter;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomCheckBox;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by imaginato on 2015/6/10.
 */

@SuppressWarnings("ALL")
public class LoginRegisterEmailRegisterFragment extends Fragment implements View.OnFocusChangeListener,View.OnClickListener{
    private EditText firstName,lastName,email,et_phone_number,password,re_password;
    private TextView firstNameText,lastNameText,emailText,tv_phone_text,passwordText,re_passwordText;
    private TextView firstNameText2,lastNameText2,emailText2,tv_phone_text2,passwordText2,re_passwordText2;
    private ImageView img1,img2,img3,img4,img5,iv_phone_correct,clearFirst,clearLastName,clearMail,iv_clear_phone,clearPassword,clearRePassword;
    private boolean isClickRegister=true;
    private CustomButtomLineRelativeLayout rl_register_email,rl_phone_number,rl_register_pwd,rl_register_repwd;
    private View view_firstname_line,view_lastname_line;
    private TextView error;
    private CustomCheckBox checkBox;
    private TextView checkBoxText1;
    private View contentView;
    private boolean existVending=false;
    private LoginRegisterActivity loginRegisterActivity;
    private String TAG;
    private Dialog mDialog;
    private InputMethodManager inputMethodManager;
    private String updateDiaHintmsg;
    private  String updateDiaBtnMsg;
    private  DataHandler dataHandler;
    private MyAccountDao mAccountDao;
    private ToolBarFragmentCallback toolBarFragmentCallback;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginRegisterActivity = (LoginRegisterActivity) activity;
            toolBarFragmentCallback= (ToolBarFragmentCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_loginregister_emailregister,null);
        return contentView;
    }
    private void cleanEditText(){
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        et_phone_number.setText("");
        password.setText("");
        re_password.setText("");
    }
    @Override
    public void onResume() {
        super.onResume();
//        IsOldVersion();
    }

    private final static class DataHandler extends Handler{
        private final WeakReference<LoginRegisterActivity> mActivity;
        private final WeakReference<LoginRegisterEmailRegisterFragment> mFragment;
        public DataHandler(LoginRegisterActivity activity,LoginRegisterEmailRegisterFragment fragment){
            mActivity=new WeakReference<LoginRegisterActivity>(activity);
            mFragment=new WeakReference<LoginRegisterEmailRegisterFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            switch (msg.what){
                case ProductDao.REQUEST_CHECKVERSION:
//                    if(msg.arg1== ProductDao.RESPONSE_SUCCESS){
//
//                    }else{
//                        if(mActivity.get()!=null) {
//                            JViewUtils.showMaterialDialog(mActivity.get(),mFragment.get().updateDiaTitle, mFragment.get().updateDiaHintmsg, mFragment.get().updateDiaBtnMsg,mFragment.get().updateListener, false);
//                        }
//                    }
                    break;
                case MyAccountDao.REQUEST_REGISTERCODE:
                    if(mFragment.get().mDialog!=null){ mFragment.get().mDialog.cancel();}
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
//                        try{
//                            FirebaseEventUtils.getInstance().allAppSignUp(mActivity.get(),FirebaseEventUtils.lOGIN_EMAIL);
//                        }catch (Exception ex){
//                            ex.getStackTrace();
//                        }

                        mFragment.get().connectionLoginService();
                    }else{
                        String errorMsg= (String) msg.obj;
                        if(errorMsg.contains("app version")&&mActivity.get()!=null){
                            JViewUtils.showMaterialDialog(mActivity.get(), "", mFragment.get().updateDiaHintmsg, mFragment.get().updateDiaBtnMsg, mFragment.get().updateListener, false);
                        } else if (!JDataUtils.errorMsgHandler(mActivity.get(), errorMsg)) {
                            //失败后通过resultCode信息进行处理
                            mFragment.get().error.setVisibility(View.VISIBLE);
                            mFragment.get().error.setText(errorMsg);
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_EMAILLOGIN:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVRAppServiceCustomerLoginReturnEntity loginReturnEntity = (SVRAppServiceCustomerLoginReturnEntity) msg.obj;
                        loginReturnEntity.setEmailLogin(true);
                        WhiteLabelApplication.getAppConfiguration().signIn(mActivity.get(), loginReturnEntity);
                        //跳转界面
                        if (loginReturnEntity.getConfirmation() != 1) {
                            //不需要邮件确认
                            mActivity.get().setEmailConfirm(false);
                        }
                        mFragment.get().trackerRegister(loginReturnEntity.getId());
                        //跳转
                        mFragment.get().cleanEditText();
                        mActivity.get().redirectToAttachedFragment(LoginRegisterActivity.REGISTERSUCCESS_FLAG, 1);
                        mActivity.get().setMyEmail(mFragment.get().email.getText().toString().trim());
                    }else{
                        String errorMsg= (String) msg.obj;
                        Toast.makeText(mActivity.get(),errorMsg+"",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MyAccountDao.ERROR:
                    if(msg.arg1==ProductDao.REQUEST_CHECKVERSION){

                    }else {
                        if (mFragment.get().mDialog != null) {
                            mFragment.get().mDialog.cancel();
                        }
                        RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private View.OnClickListener updateListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
            for(int i=0;i<packages.size();i++) {
                PackageInfo packageInfo = packages.get(i);
                String packgeName="";
                packgeName=packageInfo.packageName;
                JLogUtils.i("Allen","packge="+packgeName);
                if(packgeName.contains("vending")){
                    //跳转进市场搜索的代码
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(GlobalData.jumpMarketUrl));
                    startActivity(intent);
                    existVending=true;
                }
            }
            if(!existVending){
                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.wnp.app");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                existVending=false;
            }
        }
    };

    //检出service版本号判断是否更新
    public void IsOldVersion(){
        new ProductDao(TAG,dataHandler).checkVersion("2");
    }
    public void onClickLeftMenu(View v) {
        inputMethodManager = (InputMethodManager)loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(loginRegisterActivity.getWindow().getDecorView().getWindowToken(), 0); //隐藏
        }
        loginRegisterActivity.onBackPressed();
        cleanEditText();
    }

    public void onClickRightMenu(View v) {
        if(loginRegisterActivity.fromStart) {
            Intent i = new Intent(loginRegisterActivity, HomeActivity.class);
            startActivity(i);
        }
        loginRegisterActivity.finish();
        loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cancel, menu);
        View view=menu.findItem(R.id.action_cancel).getActionView();
        ImageView ivCancel= (ImageView) view.findViewById(R.id.iv_img);
        JViewUtils.setNavBarIconColor(getActivity(),ivCancel,R.drawable.ic_action_close);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRightMenu(v);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_cancel:
                onClickRightMenu(item.getActionView());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private TextView tvRegisterHint;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.Sign_Up));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JViewUtils.getNavBarIconDrawable(getActivity(),R.drawable.ic_action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        rl_register_email= (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_register_email);
        rl_phone_number= (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_phone_number);
        rl_register_pwd= (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_register_pwd);
        rl_register_repwd= (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_register_repwd);
        view_firstname_line= (View) contentView.findViewById(R.id.view_firstname_line);
        view_lastname_line= (View) contentView.findViewById(R.id.view_lastname_line);
        tvRegisterHint= (TextView) contentView.findViewById(R.id.tv_register_hint);
        firstNameText= (TextView) contentView.findViewById(R.id.firstName_text);
        lastNameText= (TextView) contentView.findViewById(R.id.lastName_text);
        emailText= (TextView) contentView.findViewById(R.id.email_text);
        tv_phone_text= (TextView) contentView.findViewById(R.id.tv_phone_text);
        passwordText= (TextView) contentView.findViewById(R.id.password_text);
        re_passwordText= (TextView) contentView.findViewById(R.id.re_password_text);
        firstName= (EditText) contentView.findViewById(R.id.firstName);
        lastName= (EditText) contentView. findViewById(R.id.lastName);
        email = (EditText) contentView. findViewById(R.id.email);
        et_phone_number = (EditText) contentView. findViewById(R.id.et_phone_number);
        password= (EditText) contentView.findViewById(R.id.password);
        re_password= (EditText) contentView.findViewById(R.id.re_password);
        firstNameText2= (TextView) contentView.findViewById(R.id.firstName_text2);
        lastNameText2= (TextView) contentView.findViewById(R.id.lastName_text2);
        emailText2= (TextView) contentView.findViewById(R.id.email_text2);
        tv_phone_text2= (TextView) contentView.findViewById(R.id.tv_phone_text2);
        passwordText2= (TextView) contentView.findViewById(R.id.password_text2);
        re_passwordText2= (TextView) contentView.findViewById(R.id.re_password_text2);
        img1= (ImageView) contentView.findViewById(R.id.img1);
        img2= (ImageView) contentView.findViewById(R.id.img2);
        img3= (ImageView) contentView.findViewById(R.id.img3);
        img4= (ImageView) contentView.findViewById(R.id.img4);
        img5= (ImageView) contentView.findViewById(R.id.img5);
        iv_phone_correct= (ImageView) contentView.findViewById(R.id.iv_phone_correct);
        clearFirst=(ImageView)contentView.findViewById(R.id.clearFirst);
        clearFirst.setOnClickListener(this);
        clearLastName=(ImageView)contentView.findViewById(R.id.clearLastName);
        clearLastName.setOnClickListener(this);
        clearMail=(ImageView)contentView.findViewById(R.id.clearMail);
        clearMail.setOnClickListener(this);
        iv_clear_phone=(ImageView)contentView.findViewById(R.id.iv_clear_phone);
        iv_clear_phone.setOnClickListener(this);
        clearPassword=(ImageView)contentView.findViewById(R.id.clearPassword);
        clearPassword.setOnClickListener(this);
        clearRePassword=(ImageView)contentView.findViewById(R.id.clearRePassword);
        clearRePassword.setOnClickListener(this);
        //焦点监听
        firstName.setOnFocusChangeListener(this);
        lastName.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        et_phone_number.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        re_password.setOnFocusChangeListener(this);
        TextView sign_in = (TextView) contentView.findViewById(R.id.sign_in);
        sign_in.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder=stringBuilder.append(getActivity().getResources().getString(R.string.by_creating));
        stringBuilder=stringBuilder.append(" ");
        stringBuilder=stringBuilder.append(GlobalData.appName);
        tvRegisterHint.setText(stringBuilder.toString());
        sign_in.setOnClickListener(this);
        CustomTextView signUp = (CustomTextView) contentView.findViewById(R.id.sign_up);
        JViewUtils.setSoildButtonGlobalStyle(getActivity(),signUp);
        signUp.setOnClickListener(this);
        checkBox= (CustomCheckBox) contentView.findViewById(R.id.checkbox2);
        checkBox.setColorChecked(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        checkBox.setChecked(true);
        checkBoxText1= (TextView) contentView.findViewById(R.id.checkbox_text1);
        error= (TextView) contentView.findViewById(R.id.error);
        setMoreClickSpan();
        TextView t1 = (TextView) contentView.findViewById(R.id.t1);
        TextView t3 = (TextView) contentView.findViewById(R.id.t3);
        t1.setText(getResources().getString(R.string.TermsOfUserPrivacyPolicy1));
        t1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        t3.setText(getResources().getString(R.string.TermsOfUserPrivacyPolicy2));
        t3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        t1.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        t3.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
        t1.setOnClickListener(this);
        t3.setOnClickListener(this);
        TAG=this.getClass().getSimpleName();
        email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et_phone_number.setInputType( InputType.TYPE_CLASS_NUMBER);
        if(loginRegisterActivity.getIntent()!=null&&loginRegisterActivity.getIntent().getExtras()!=null){
            String activityAata = loginRegisterActivity.getIntent().getExtras().getString("Activity");//读出数据
            if ("start".equals(activityAata)) {
                boolean isStart = true;
            }
        }
        if(isClickRegister){
            //点了register按钮，再back就清空
            cleanEditText();
        }
        dataHandler=new DataHandler(loginRegisterActivity,this);
        mAccountDao=new MyAccountDao(TAG,dataHandler);
        String updateDiaTitle = getActivity().getResources().getString(R.string.versionCheckTitle);
        updateDiaHintmsg = getActivity().getResources().getString(R.string.versionCheckMsg);
        updateDiaBtnMsg = getActivity().getResources().getString(R.string.update);
    }
    private void setMoreClickSpan(){
//        try {

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder=stringBuilder.append(getResources().getString(R.string.checkBox1)).append(" "+GlobalData.appName);
        checkBoxText1.setText(stringBuilder.toString());
//            String  sendStr=getResources().getString(R.string.checkBox3);
//            int firstTextLength=firstStr.length();
//            int sendTextLength=sendStr.length();
//            if(firstTextLength==0||sendTextLength==0){
//                return;
//            }
//            SpannableStringBuilder builder = new SpannableStringBuilder(checkBoxText1.getText().toString());
//            checkBoxText1.setClickable(true);
//            checkBoxText1.setMovementMethod(LinkMovementMethod.getInstance());
//            NoUnderLineClickSpan greyNoLineClickableSpan=new NoUnderLineClickSpan(JToolUtils.getColor(R.color.grayText),false);
//            NoUnderLineClickSpan purpleNoLineClickableSpan=new NoUnderLineClickSpan(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color(),true);
//            greyNoLineClickableSpan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(checkBox.isChecked()){
//                        checkBox.setChecked(false,true);
//                    }else {
//                        checkBox.setChecked(true,true);
//                    }
//                }
//            });
//            purpleNoLineClickableSpan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle2=new Bundle();
//                    bundle2.putInt("helpCenter", 2);
//                    startActivitysForResult(bundle2, RegisterToHelpCenter.class, false);
//                }
//            });
//            builder.setSpan(purpleNoLineClickableSpan, firstTextLength, firstTextLength + sendTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            builder.setSpan(greyNoLineClickableSpan,  0,firstTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            checkBoxText1.setText(builder);
//        }catch(Exception e){
//        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {//输入动画
        if (hasFocus) {
            AnimationSet set = new AnimationSet(true);
            set.setFillAfter(true);
            //上移高度应该为自身的高度
            int textHeight=firstNameText.getHeight();
            Animation tran;
            if(textHeight>0){
                tran = new TranslateAnimation(0, 0, 0, 0-textHeight);
            }else {
                tran = new TranslateAnimation(0, 0, 0, -50);
            }
            tran.setDuration(300);
            //渐变
            Animation alpha = new AlphaAnimation(0, 1);
            alpha.setDuration(300);
            set.addAnimation(tran);
            set.addAnimation(alpha);
            switch (v.getId()) {
                case R.id.firstName:
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line,true);
                    firstNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                    firstNameText2.setText(getResources().getString(R.string.first_name));
                    if (firstName.getText().length()!=0) {
                        clearFirst.setVisibility(View.VISIBLE);
                    }else {
                        clearFirst.setVisibility(View.GONE);
                    }
                    if(firstName.getText().toString().trim().equals("")) {
                        firstNameText2.setVisibility(View.INVISIBLE);
                        firstName.setHint("");
                        firstNameText.startAnimation(set);
                    }else{
                        firstNameText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img1.setVisibility(View.INVISIBLE);
                    firstName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&firstName.isFocused()) {
                                clearFirst.setVisibility(View.VISIBLE);
                            }else {
                                clearFirst.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
                case R.id.lastName:
                    lastNameText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());

                    CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line,true);
                    lastNameText2.setText(getResources().getString(R.string.last_name));
                    if (lastName.getText().length()!=0) {
                        clearLastName.setVisibility(View.VISIBLE);
                    }else {
                        clearLastName.setVisibility(View.GONE);
                    }
                    if(lastName.getText().toString().trim().equals("")) {
                        lastNameText2.setVisibility(View.INVISIBLE);
                        lastName.setHint("");
                        lastNameText.startAnimation(set);
                    }else{
                        lastNameText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img2.setVisibility(View.INVISIBLE);
//                    img2_error.setVisibility(View.INVISIBLE);
                    lastName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&lastName.isFocused()) {
                                clearLastName.setVisibility(View.VISIBLE);
                            }else {
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
                    emailText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                    //parent relativeLayout
                    rl_register_email.setBottomLineActive(true);
                    //top(xml down) customtextview
                    emailText2.setText(getResources().getString(R.string.loginregister_emailbound_email_hint));
                    //customEdit
                    if (email.getText().length()!=0)
                        clearMail.setVisibility(View.VISIBLE);
                    else
                        clearMail.setVisibility(View.GONE);
                    if(email.getText().toString().trim().equals("")) {
                        emailText2.setVisibility(View.INVISIBLE);
                        email.setHint("");
                        emailText.startAnimation(set);
                    }else{
                        emailText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img3.setVisibility(View.INVISIBLE);
//                    img3_error.setVisibility(View.INVISIBLE);
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&email.isFocused()) {
                                clearMail.setVisibility(View.VISIBLE);
                            }else {
                                clearMail.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
                case R.id.et_phone_number:
                    tv_phone_text.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());
                    rl_phone_number.setBottomLineActive(true);
                    tv_phone_text.setText(getResources().getString(R.string.enter_phone_number));
                    tv_phone_text2.setText(getResources().getString(R.string.enter_phone_number));
                    if (et_phone_number.getText().length()!=0){
                        iv_clear_phone.setVisibility(View.VISIBLE);
                    }else {
                        iv_clear_phone.setVisibility(View.GONE);
                    }
                    if(et_phone_number.getText().toString().trim().equals("")) {
                        tv_phone_text2.setVisibility(View.INVISIBLE);
                        et_phone_number.setHint("");
                        tv_phone_text.startAnimation(set);
                    }else{
                        tv_phone_text2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                    }
                    iv_phone_correct.setVisibility(View.INVISIBLE);
                    et_phone_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&et_phone_number.isFocused()) {
                                iv_clear_phone.setVisibility(View.VISIBLE);
                            }else {
                                iv_clear_phone.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case R.id.password:
                    passwordText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());

                    rl_register_pwd.setBottomLineActive(true);
                    passwordText2.setText(getResources().getString(R.string.enter_password));
                    if (password.getText().length()!=0)
                        clearPassword.setVisibility(View.VISIBLE);
                    else
                        clearPassword.setVisibility(View.GONE);
                    if(password.getText().toString().trim().equals("")) {
                        passwordText2.setVisibility(View.INVISIBLE);
                        password.setHint("");
                        passwordText.startAnimation(set);
                    }else{
                        passwordText2.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    img4.setVisibility(View.INVISIBLE);
//                    img4_error.setVisibility(View.INVISIBLE);
                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&password.isFocused()) {
                                clearPassword.setVisibility(View.VISIBLE);
                            }else {
                                clearPassword.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    break;
                case R.id.re_password:
                    re_passwordText.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getTheme_color());

                    rl_register_repwd.setBottomLineActive(true);
                    re_passwordText2.setText(getResources().getString(R.string.retype_password));
                    if (re_password.getText().length()!=0)
                        clearRePassword.setVisibility(View.VISIBLE);
                    else
                        clearRePassword.setVisibility(View.GONE);
                    if(re_password.getText().toString().trim().equals("")) {
                        re_passwordText2.setVisibility(View.INVISIBLE);
                        re_password.setHint("");
                        re_passwordText.startAnimation(set);
                    }else{
                        re_passwordText2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
                    }
                    img5.setVisibility(View.INVISIBLE);
//                    img5_error.setVisibility(View.INVISIBLE);
                    re_password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0&&re_password.isFocused()) {
                                clearRePassword.setVisibility(View.VISIBLE);
                            }else {
                                clearRePassword.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    break;
            }
        }else{//所有的验证都放到了失去焦点事件上
            onblur(v.getId());
            clearFirst.setVisibility(View.GONE);
            clearLastName.setVisibility(View.GONE);
            clearMail.setVisibility(View.GONE);
            iv_clear_phone.setVisibility(View.GONE);
            clearPassword.setVisibility(View.GONE);
            clearRePassword.setVisibility(View.GONE);
        }
    }


    public boolean onblur(int id){
        switch (id) {
            case R.id.firstName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_firstname_line,false);
                firstNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                firstNameText2.setVisibility(View.VISIBLE);
                if(firstName.getText().toString().trim().equals("")){
                    firstName.setHint("First Name");
                    firstNameText.clearAnimation();
                    //验证字段
                    img1.setVisibility(View.INVISIBLE);
                    firstNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    firstNameText2.setText(getResources().getString(R.string.required_field));
                    return false;

                }else{
                    firstNameText.clearAnimation();
                    //验证字段
//                    img1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.lastName:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_lastname_line,false);
                lastNameText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                lastNameText2.setVisibility(View.VISIBLE);
                if(lastName.getText().toString().trim().equals("")){
                    lastName.setHint("Last Name");
                    lastNameText.clearAnimation();
                    //验证字段
//                    img2_error.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.INVISIBLE);
                    lastNameText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    lastNameText2.setText(getResources().getString(R.string.required_field));
                    return false;
                }else{
                    lastNameText.clearAnimation();
                    //验证字段
//                    img2_error.setVisibility(View.INVISIBLE);
//                    img2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.email:
                rl_register_email.setBottomLineActive(false);
                emailText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                emailText2.setVisibility(View.VISIBLE);
                if(email.getText().toString().trim().equals("")) {
                    email.setHint(getResources().getString(R.string.enter_email_address));
                    emailText.clearAnimation();
                    //验证字段
//                    img3_error.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.INVISIBLE);
                    emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    emailText2.setText(getResources().getString(R.string.required_field));
                    return false;
                }else{
                    emailText.clearAnimation();
                    //验证字段
                    if(JDataUtils.isEmail(email.getText().toString())){
//                        img3_error.setVisibility(View.INVISIBLE);
//                        img3.setVisibility(View.VISIBLE);
                    }else{
//                        img3_error.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.INVISIBLE);
                        //邮箱格式不正确
                        emailText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        emailText2.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                        return false;
                    }
                }
                break;
            case R.id.et_phone_number:
                rl_phone_number.setBottomLineActive(false);
                tv_phone_text2.setTextColor(ContextCompat.getColor(getActivity(),R.color.label_saved));
                tv_phone_text2.setVisibility(View.VISIBLE);
                if(et_phone_number.getText().toString().trim().equals("")) {
                    et_phone_number.setHint(getResources().getString(R.string.enter_phone_number));
                    tv_phone_text.clearAnimation();

//                    img3_error.setVisibility(View.VISIBLE);
                    iv_phone_correct.setVisibility(View.INVISIBLE);
                    tv_phone_text2.setTextColor(ContextCompat.getColor(getActivity(),R.color.redC2060A));
                    tv_phone_text2.setText(getResources().getString(R.string.required_field));
                    return false;
                }else{
                    tv_phone_text.clearAnimation();

                    if(!JDataUtils.isPhoneNumber(et_phone_number.getText().toString())){
//                        img3_error.setVisibility(View.VISIBLE);
                        iv_phone_correct.setVisibility(View.INVISIBLE);
                        tv_phone_text2.setTextColor(ContextCompat.getColor(getActivity(),R.color.redC2060A));
                        tv_phone_text2.setText(getResources().getString(R.string.address_phone_error_hint));
                        return false;
                    }
                }
                break;
            case R.id.password:
                rl_register_pwd.setBottomLineActive(false);
                passwordText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                passwordText2.setVisibility(View.VISIBLE);
                if(password.getText().toString().trim().equals("")){
                    password.setHint(getResources().getString(R.string.enter_password));
                    passwordText.clearAnimation();
                    //验证字段
//                    img4_error.setVisibility(View.VISIBLE);
                    img4.setVisibility(View.INVISIBLE);
                    passwordText2.setText(getResources().getString(R.string.required_field));
                    passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    passwordText.clearAnimation();
                    //验证字段
                    if(JDataUtils.isPassword(password)) {
//                        img4_error.setVisibility(View.INVISIBLE);
//                        img4.setVisibility(View.VISIBLE);
                    }else{
//                        img4_error.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.INVISIBLE);
                        passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        passwordText2.setText(getResources().getString(R.string.enter_characters_ignored));
                        return false;
                    }
                }
                break;
            case R.id.re_password:
                rl_register_repwd.setBottomLineActive(false);
                re_passwordText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                re_passwordText2.setVisibility(View.VISIBLE);
                if(re_password.getText().toString().trim().equals("")){
                    re_password.setHint("Re-type Password");
                    re_passwordText.clearAnimation();
                    //验证字段
//                    img5_error.setVisibility(View.VISIBLE);
                    img5.setVisibility(View.INVISIBLE);
                    re_passwordText2.setText(getResources().getString(R.string.required_field));
                    re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    re_passwordText.clearAnimation();
                    //验证字段
                    if(!JDataUtils.isPassword(re_password)) {
//                        img5_error.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.INVISIBLE);
                        re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        re_passwordText2.setText(getResources().getString(R.string.enter_characters_ignored));
                        return false;
                    }else if (!re_password.getText().toString().trim().equals(password.getText().toString().trim())) {
//                        img5_error.setVisibility(View.VISIBLE);
                        img5.setVisibility(View.INVISIBLE);
                        re_passwordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        re_passwordText2.setText(getResources().getString(R.string.password_match));
                        return false;
                    }else {
//                        img5_error.setVisibility(View.INVISIBLE);
//                        img5.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in:
                loginRegisterActivity.redirectToAttachedFragment(
                    LoginRegisterActivity.EMAILLOGIN_FLAG, 1);
                cleanEditText();
                break;
            case R.id.sign_up:
                JViewUtils.cleanCurrentViewFocus(getActivity());
                error.setText("");
                if(onblur(R.id.firstName) && onblur(R.id.lastName)&&onblur(R.id.email) &&onblur(R.id.et_phone_number)&&onblur(R.id.password)&&onblur(R.id.re_password)) {
                    mDialog= JViewUtils.showProgressDialog(loginRegisterActivity);
                    //隐藏软盘
                    inputMethodManager = (InputMethodManager)loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String checkBox_numbel=checkBox.isChecked()?"1":"0";
                    mAccountDao.registerUser(firstName.getText().toString().trim(),lastName.getText().toString().trim(),
                        email.getText().toString().trim(),et_phone_number.getText().toString().trim(),password.getText().toString().trim(),checkBox_numbel, WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());

                }
                break;
            case R.id.checkbox_text1:
                if(checkBox.isChecked()){
                    checkBox.setChecked(false,true);
                }else {
                    checkBox.setChecked(true,true);
                }
                break;
            case R.id.t1:
                Bundle bundle3=new Bundle();
                bundle3.putInt("helpCenter", 2);
                startActivitysForResult(bundle3, RegisterToHelpCenter.class, false);
                break;
            case R.id.t3:
                Bundle bundle4=new Bundle();
                bundle4.putInt("helpCenter", 1);
                startActivitysForResult(bundle4, RegisterToHelpCenter.class, false);
                break;
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

        }
    }

    public void startActivitysForResult(Bundle bundle, Class<?> pClass, boolean finishFlag) {
        Intent intent = new Intent(loginRegisterActivity, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, 110);
        loginRegisterActivity.startActivityTransitionAnim();
        if (finishFlag) {
            loginRegisterActivity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110&&resultCode==101){
            isClickRegister=false;
        }
    }

    public void trackerRegister(String id){
        try {
            GaTrackHelper.getInstance().googleAnalyticsEvent("Account Action", Const.GA.SIGN_UP_EVENT,
                id,
                Long.valueOf(id));
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mAccountDao!=null){
            mAccountDao.cancelHttpByTag(TAG);
        }

        if(dataHandler!=null){
            dataHandler.removeCallbacksAndMessages(null);
        }
    }

    public void connectionLoginService(){
        mAccountDao.emailLogin(email.getText().toString().trim(), password.getText().toString().trim(), WhiteLabelApplication.getPhoneConfiguration().getRegistrationToken());
    }


//    private void recoverEditTextContent() {
//        mFirstName=firstName.getText().toString();
//        mLastName=lastName.getText().toString();
//        mEmail=email.getText().toString();
//        mPassword=password.getText().toString();
//        mRePassword=re_password.getText().toString();
//    }

    @Override
    public void onStart() {
        super.onStart();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(loginRegisterActivity, true);
        GaTrackHelper.getInstance().googleAnalytics(Const.GA.SIGN_UP_SCREEN, loginRegisterActivity);
//        if(isstop){
//            firstName.setText(mFirstName);
//            lastName.setText(mLastName);
//            email.setText(mEmail);
//            password.setText(mPassword);
//            re_password.setText(mRePassword);
//        }
    }

    @Override
    public void onStop() {

        isClickRegister=true;
        super.onStop();
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(loginRegisterActivity, false);
    }
}

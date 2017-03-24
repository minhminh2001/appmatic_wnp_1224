package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.dao.ProductDao;
import com.whitelabel.app.listener.OnMessageDialogListener;
import com.whitelabel.app.model.FBGraphAPIUserEntity;
import com.whitelabel.app.model.SVRAppServiceCustomerLoginReturnEntity;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.utils.FirebaseEventUtils;
import com.whitelabel.app.utils.GaTrackHelper;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.utils.SendBoardUtil;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.NoUnderLineClickSpan;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by imaginato on 2015/6/10.
 */
public class LoginRegisterEmailLoginFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener,GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "LoginRegisterEmailLoginFragment";
    private View contentView;
    private EditText email, password;
    private String mCurrTag;
    private View ivFacebookLogin;
    private Button sign_in;
    private ImageView clearMail,clearPassword;
    private ToolBarFragmentCallback toolBarFragmentCallback;
    private LoginRegisterActivity loginRegisterActivity;
    private CustomButtomLineRelativeLayout rl_login_email,rl_login_pwd;
    private TextView forgotPassword, register;
    private TextView email_text, email_text2, password_text, password_text2, error, clickEmail2;
    //   private View bottomText;
    private View clickEmailInfo;
    private boolean sessionExpire;
    public final static  int RESULTCODE=1000;
    private Dialog mDialog;
    private boolean existVending=false;
    private boolean isStart=false;
    private boolean fromSignOut=false;
    private String updateDiaHintmsg,updateDiaTitle;
    private  String updateDiaBtnMsg;
    private  MyAccountDao mMyAccountDao;
    private DataHandler dataHandler;
    // Facebook
    private int resultTypeFinishLoadFacebookBasicInfo = -1;
    private int resultTypeFinishLoadFacebookPictureInfo = -1;
    private FBGraphAPIUserEntity fbGraphAPIUserEntity;
    private CallbackManager facebookCallbackManager;
    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile facebookProfile = Profile.getCurrentProfile();
            if (facebookProfile == null || JDataUtils.isEmpty(facebookProfile.getId())) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        if(getActivity()!=null) {
                            if (profile2 == null || JDataUtils.isEmpty(profile2.getId())) {
                                fbLoginError();
                                return;
                            }
                            mDialog=JViewUtils.showProgressDialog(loginRegisterActivity);
                            fbGetFacebookUserInfoFromFB(profile2.getId());
                            mProfileTracker.stopTracking();
                        }
                    }
                };
                mProfileTracker.startTracking();
            }else{
                mDialog=JViewUtils.showProgressDialog(loginRegisterActivity);
                fbGetFacebookUserInfoFromFB(facebookProfile.getId());
            }
        }

        @Override
        public void onCancel() {
            JLogUtils.i("Martin", "FacebookCallback=>onCancel2");
            fbLoginCancel();
        }

        @Override
        public void onError(FacebookException error) {
            JLogUtils.i("Martin", "FacebookCallback=>onError3-->" + error.getMessage());
            try {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }catch(Exception ex){
                ex.getStackTrace();
            }
            fbLoginError();
        }
    };

    public LoginRegisterEmailLoginFragment() {
    }

    private void fbGetFacebookUserInfoFromFB(String facebookUserId) {
        fbGraphAPIUserEntity = null;
        fbGetFacebookUserBasicInfoFromFB(facebookUserId);
        fbGetFacebookUserPictureInfoFromFB(facebookUserId);
    }

    private void fbGetFacebookUserBasicInfoFromFB(String facebookUserId) {
        resultTypeFinishLoadFacebookBasicInfo = -1;
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (response == null) {
                            resultTypeFinishLoadFacebookBasicInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        if (response.getError() != null) {
                            FacebookRequestError facebookRequestError = response.getError();
                            JLogUtils.i("Martin", "fbGetFacebookUserBasicInfoFromFB->error->" + facebookRequestError.getErrorMessage());
                            resultTypeFinishLoadFacebookBasicInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        JSONObject responseJsonObject = response.getJSONObject();
                        if (responseJsonObject == null) {
                            resultTypeFinishLoadFacebookBasicInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        String responseJsonStr = responseJsonObject.toString();
                        JLogUtils.i("Martin", "fbGetFacebookUserBasicInfoFromFB->sucess->getJSONObject.toString()=>" + response.getJSONObject().toString());
                        FBGraphAPIUserEntity fbInfoEntity = JJsonUtils.getFBGraphAPIUserEntityFromJson(responseJsonStr);
                        if (fbInfoEntity == null || JDataUtils.isEmpty(fbInfoEntity.getId())) {
                            resultTypeFinishLoadFacebookBasicInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        if (fbGraphAPIUserEntity == null) {
                            fbGraphAPIUserEntity = new FBGraphAPIUserEntity();
                        }
                        JLogUtils.i(TAG,"fbInfoEntity.getId():"+fbInfoEntity.getId());
                        JLogUtils.i(TAG,"fbInfoEntity.getEmail():"+fbInfoEntity.getEmail());
                        JLogUtils.i(TAG,"fbInfoEntity.getFirst_name():"+fbInfoEntity.getFirst_name());
                        JLogUtils.i(TAG,"fbInfoEntity.getGender():"+fbInfoEntity.getGender());
                        JLogUtils.i(TAG,"fbInfoEntity.getLast_name():"+fbInfoEntity.getLast_name());
                        JLogUtils.i(TAG,"fbInfoEntity.getLink():"+fbInfoEntity.getLink());
                        JLogUtils.i(TAG,"fbInfoEntity.getLocale():"+fbInfoEntity.getLocale());
                        JLogUtils.i(TAG,"fbInfoEntity.getName():"+fbInfoEntity.getName());
                        JLogUtils.i(TAG,"fbInfoEntity.getTimezone():"+fbInfoEntity.getTimezone());
                        JLogUtils.i(TAG,"fbInfoEntity.getUpdated_time():"+fbInfoEntity.getUpdated_time());
                        JLogUtils.i(TAG,"fbInfoEntity.isVerified():"+fbInfoEntity.isVerified());


                        fbGraphAPIUserEntity.setId(fbInfoEntity.getId());
                        fbGraphAPIUserEntity.setEmail(fbInfoEntity.getEmail());
                        fbGraphAPIUserEntity.setFirst_name(fbInfoEntity.getFirst_name());
                        fbGraphAPIUserEntity.setGender(fbInfoEntity.getGender());
                        fbGraphAPIUserEntity.setLast_name(fbInfoEntity.getLast_name());
                        fbGraphAPIUserEntity.setLink(fbInfoEntity.getLink());
                        fbGraphAPIUserEntity.setLocale(fbInfoEntity.getLocale());
                        fbGraphAPIUserEntity.setName(fbInfoEntity.getName());
                        fbGraphAPIUserEntity.setTimezone(fbInfoEntity.getTimezone());
                        fbGraphAPIUserEntity.setUpdated_time(fbInfoEntity.getUpdated_time());
                        fbGraphAPIUserEntity.setVerified(fbInfoEntity.isVerified());
                        resultTypeFinishLoadFacebookBasicInfo = 1;
                        fbLoginResultResponse();

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,first_name,last_name,gender,locale,timezone,updated_time,verified");
        request.setParameters(parameters);
        request.executeAsync();

    }
    private void fbGetFacebookUserPictureInfoFromFB(String facebookUserId) {
        resultTypeFinishLoadFacebookPictureInfo = -1;
        Bundle parameters = new Bundle();
        parameters.putBoolean("redirect", false);
        parameters.putString("height", "200");
        parameters.putString("type", "normal");
        parameters.putString("width", "200");
        GraphRequest graphRequest= new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + facebookUserId + "/picture",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response == null) {
                            resultTypeFinishLoadFacebookPictureInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        if (response.getError() != null) {
                            FacebookRequestError facebookRequestError = response.getError();
                            JLogUtils.i("Martin", "fbGetFacebookUserPictureInfoFromFB->error->" + facebookRequestError.getErrorMessage());
                            resultTypeFinishLoadFacebookPictureInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }

                        JSONObject responseJsonObject = response.getJSONObject();
                        if (responseJsonObject == null) {
                            resultTypeFinishLoadFacebookPictureInfo = 0;
                            fbLoginResultResponse();
                            return;
                        }
                        JLogUtils.i("Martin", "fbGetFacebookUserPictureInfoFromFB->success->getJSONObject.toString()=>" + responseJsonObject.toString());

                        String avatarUrl = "";
                        try {
                            JSONObject dataJsonObject = responseJsonObject.getJSONObject("data");
                            avatarUrl = dataJsonObject.getString("url");
                            avatarUrl = URLEncoder.encode(avatarUrl, "UTF-8");
                            JLogUtils.i(TAG,"avatarUrl:"+avatarUrl);
                        } catch (Exception e) {
                            JLogUtils.e(TAG, "fbGetFacebookUserPictureInfoFromFB", e);
                        }
                        if (fbGraphAPIUserEntity == null) {
                            fbGraphAPIUserEntity = new FBGraphAPIUserEntity();
                        }
                        fbGraphAPIUserEntity.setAvatarUrl(avatarUrl);

                        resultTypeFinishLoadFacebookPictureInfo = 1;
                        fbLoginResultResponse();
                    }
                }
        );
        JLogUtils.i("LoginRegisterEmailLoginFragment", "version:" + graphRequest.getVersion() + "");
        graphRequest.executeAsync();
    }
    private void fbLoginResultResponse() {
        if (-1 == resultTypeFinishLoadFacebookBasicInfo || -1 == resultTypeFinishLoadFacebookPictureInfo) {
            return;
        }
        if (1 == resultTypeFinishLoadFacebookBasicInfo) {
            fbUseInfoToLoginRemoteServer();
        } else {
            fbLoginError();
        }
    }

    private void fbUseInfoToLoginRemoteServer() {
        String fbHasEmail="";
        if(JDataUtils.isEmpty(fbGraphAPIUserEntity.getEmail())){
            fbHasEmail="0";
            loginRegisterActivity.setSubEmail("");
        }else{
            fbHasEmail="1";
            loginRegisterActivity.setSubEmail(fbGraphAPIUserEntity.getEmail());
        }
        mMyAccountDao.facebookLogin(fbGraphAPIUserEntity.getEmail(), fbHasEmail, fbGraphAPIUserEntity.getFirst_name(), fbGraphAPIUserEntity.getLast_name(), fbGraphAPIUserEntity.getId(), GemfiveApplication.getPhoneConfiguration().getRegistrationToken());
    }
    private void fbLoginCancel() {
    }
    private void fbLoginError() {
        if(getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            JViewUtils.showToast(loginRegisterActivity, null, getString(R.string.FB_Login_tips_error));
        }
    }

    private void loginSuccess(SVRAppserviceCustomerFbLoginReturnEntity fbLoginReturnEntity) {

        if(getActivity()!=null&&!getActivity().isFinishing()&&isAdded()) {
            GemfiveApplication.getAppConfiguration().signIn(loginRegisterActivity, fbLoginReturnEntity);
            if (false) {
                String testMessage = "sessionKey:" + fbLoginReturnEntity.getSessionKey() + "\n" + "user id:" + fbLoginReturnEntity.getId();
                JViewUtils.showMessageDialog(loginRegisterActivity, testMessage, new OnMessageDialogListener() {
                    @Override
                    public void onOKClickListener(Object object) {

                        if (isStart) {
                            Intent intent = new Intent(loginRegisterActivity, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loginRegisterActivity.setResult(RESULTCODE);
                        }

                        loginRegisterActivity.finish();
                        loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
                    }
                });
            } else {
                SendBoardUtil.sendNotificationBoard(loginRegisterActivity, SendBoardUtil.LOGINCODE, null);
                if (isStart) {
                    Intent intent = new Intent(loginRegisterActivity, HomeActivity.class);
                    startActivity(intent);
                } else {
//                                        loginRegisterActivity.onBackPressed();
                    Intent intent = new Intent();
                    if (loginRegisterActivity.addToWish) {
                        Bundle bundle = new Bundle();
                        bundle.putString("productId", loginRegisterActivity.productId);
                        intent.putExtras(bundle);
                    }
                    loginRegisterActivity.setResult(RESULTCODE, intent);
                }
                loginRegisterActivity.finish();
                loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
            }
        }

    }

    private LinearLayout llGoogleLogin;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginRegisterActivity = (LoginRegisterActivity) activity;
            toolBarFragmentCallback= (ToolBarFragmentCallback) activity;
            toolBarFragmentCallback.setToolBarLeftIconAndListenter(null,null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    private View.OnClickListener updateListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
//            for(int i=0;i<packages.size();i++) {
//                PackageInfo packageInfo = packages.get(i);
//                String packgeName="";
//                packgeName=packageInfo.packageName;
//                JLogUtils.i("Allen","packge="+packgeName);
//                if(packgeName.contains("vending")){
//                    //跳转进市场搜索的代码
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(GlobalData.jumpMarketUrl));
//                    startActivity(intent);
//                    existVending=true;
//                }
//            }
//            if(!existVending){
//                Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.whitelabel.app");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
//                existVending=false;
//            }
//        }
//    };

    //检出service版本号判断是否更新
    public void IsOldVersion(){
//        new ProductDao(TAG,dataHandler).checkVersion("2");
    }
    private void cleanEditText(){
        email.setText("");
        password.setText("");
        error.setText("");
        error.setVisibility(View.GONE);
    }
    @Override
    public void onResume() {
        super.onResume();
        cleanEditText();
//        IsOldVersion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Facebook
        FacebookSdk.sdkInitialize(loginRegisterActivity.getApplicationContext());
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_loginregister_login, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View scroll=contentView.findViewById(R.id.layout);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.sign_in));
//        toolBarFragmentCallback.setToolBarLeftIconAndListenter( JToolUtils.getDrawable(R.drawable.draw_trans), null);
        initGoogleApi();
        if(scroll!=null){
            ((ScrollView)scroll).setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mCurrTag=this.getClass().getSimpleName();
        rl_login_email=(CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_login_email);
        rl_login_pwd=(CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_login_pwd);

        llGoogleLogin = (LinearLayout) contentView.findViewById(R.id.ll_googleLogin);
        email = (EditText) contentView.findViewById(R.id.email);
        password = (EditText) contentView.findViewById(R.id.password);
        sign_in = (Button) contentView.findViewById(R.id.sign_in);
        sign_in.setBackgroundColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        ivFacebookLogin = contentView.findViewById(R.id.ivFacebookLogin);
        register = (TextView) contentView.findViewById(R.id.register);
        register.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        forgotPassword = (TextView) contentView.findViewById(R.id.forgot_password);
        forgotPassword.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
        //      bottomText=contentView.findViewById(R.id.bottomText);
        ivFacebookLogin.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        register.setOnClickListener(this);
        llGoogleLogin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        email.setOnFocusChangeListener(this);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && email.isFocused()) {
                    clearMail.setVisibility(View.VISIBLE);
                } else {
                    clearMail.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.setOnFocusChangeListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
        email_text = (TextView) contentView.findViewById(R.id.email_text);
        email_text2 = (TextView) contentView.findViewById(R.id.email_text2);
        password_text = (TextView) contentView.findViewById(R.id.password_text);
        password_text2 = (TextView) contentView.findViewById(R.id.password_text2);
        clearMail=(ImageView)contentView.findViewById(R.id.clear_mail);
        clearPassword=(ImageView)contentView.findViewById(R.id.clear_password);
        clearMail.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        error = (TextView) contentView.findViewById(R.id.error);
        clickEmailInfo = contentView.findViewById(R.id.clickEmailInfo);
        clickEmail2 = (TextView) contentView.findViewById(R.id.clickEmail2);
        setResendEmailClickSpan();
        email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        updateDiaTitle= getActivity().getResources().getString(R.string.versionCheckTitle);
        updateDiaHintmsg = getActivity().getResources().getString(R.string.versionCheckMsg);
        updateDiaBtnMsg = getActivity().getResources().getString(R.string.update);
        dataHandler=new DataHandler(loginRegisterActivity,this);
        mMyAccountDao=new MyAccountDao(TAG,dataHandler);

        if(loginRegisterActivity.getIntent().getExtras()!=null){
            Bundle bundle =loginRegisterActivity.getIntent().getExtras();
            String activityAata = bundle.getString("Activity");//读出数据
            fromSignOut=bundle.getBoolean("fromSignOut");
            if("start".equals(activityAata)){
                isStart=true;
            }
            //判断session是否过期
        }
        try {
            sessionExpire = loginRegisterActivity.getIntent().getBooleanExtra("expire", false);
            if(sessionExpire){ //假设session过期 删除本地缓存数据
                GemfiveApplication.getAppConfiguration().signOut(loginRegisterActivity);
            }
        }catch (Exception ex){
            ex.getStackTrace();
        }
        /**
         * facebook 登出
         */
        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cancel, menu);
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

    public void onClickLeftMenu(View v) {
    }

    public void onClickRightMenu(View v) {
        closeCurrActivity();
    }

    private GoogleApiClient mGoogleApiClient;
    private void initGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
         .build();
         mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setResendEmailClickSpan(){
        try {
            String clickEmailStr=getResources().getString(R.string.ClickEmail);
            String resendStr=getResources().getString(R.string.to_resend);
            int clickEmailStrLength=clickEmailStr.length();
            int resendStrLength=resendStr.length();
            if(clickEmailStrLength==0||resendStrLength==0){
                return;
            }
            clickEmail2.setText(clickEmailStr+ resendStr);
            SpannableStringBuilder builder = new SpannableStringBuilder(clickEmail2.getText().toString());
            clickEmail2.setClickable(true);
            clickEmail2.setMovementMethod(LinkMovementMethod.getInstance());
            NoUnderLineClickSpan clickEmailNoLineClickableSpan=new NoUnderLineClickSpan(JToolUtils.getColor(R.color.redC2060A),true);
            NoUnderLineClickSpan resendNoLineClickableSpan=new NoUnderLineClickSpan(JToolUtils.getColor(R.color.redC2060A),false);
            clickEmailNoLineClickableSpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginRegisterActivity.redirectToAttachedFragment(LoginRegisterActivity.SENDEMAIL_FLAG, 1);
                }
            });
            builder.setSpan(clickEmailNoLineClickableSpan, 0, clickEmailStrLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(resendNoLineClickableSpan, clickEmailStrLength, clickEmailStrLength + resendStrLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            clickEmail2.setText(builder);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFacebookLogin: {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email", "user_friends"));
                break;
            }
            case R.id.sign_in:

                //clear focus
                email.clearFocus();
                password.clearFocus();
                //hide 'clear' icon
                clearPassword.setVisibility(View.GONE);
                clearMail.setVisibility(View.INVISIBLE);

                error.setVisibility(View.GONE);
                clickEmailInfo.setVisibility(View.INVISIBLE);
                //隐藏软盘
                InputMethodManager inputMethodManager = (InputMethodManager) loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (onblur(R.id.email) && onblur(R.id.password)) {
                    mDialog= JViewUtils.showProgressDialog(loginRegisterActivity);
                    mMyAccountDao.emailLogin(email.getText().toString().trim(), password.getText().toString().trim(), GemfiveApplication.getPhoneConfiguration().getRegistrationToken());
                }
                break;
            case R.id.register:
                loginRegisterActivity.redirectToAttachedFragment(
                        LoginRegisterActivity.EMAILREGISTER_FLAG, 1);
                break;
            case R.id.forgot_password:
                loginRegisterActivity.setMyEmail(email.getText().toString().trim());
                loginRegisterActivity.redirectToAttachedFragment(LoginRegisterActivity.FORGOTPASSWORD_FLAG, 1);

                break;
            case R.id.clear_mail:
                email.setText("");
                break;
            case R.id.clear_password:
                password.setText("");
                break;
            case R.id.ll_googleLogin:
                if(mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    getActivity().startActivityForResult(signInIntent, RC_SIGN_IN);
                }
                break;
        }
    }

    public final int RC_SIGN_IN=10010;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        JLogUtils.i("ray","connectionResult:"+connectionResult.getErrorMessage());
    }



    private void closeCurrActivity(){
        if(getActivity()!=null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        }
        if(isStart||sessionExpire) {

            Intent i = new Intent(loginRegisterActivity, HomeActivity.class);
            loginRegisterActivity.startActivity(i);
            loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
            loginRegisterActivity.finish();
        }else {
            loginRegisterActivity.finish();
            loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
        }
    }





    private static final class DataHandler extends Handler{
        private final WeakReference<LoginRegisterActivity> mActivity;
        private final WeakReference<LoginRegisterEmailLoginFragment> mFragment;
        private final String EMAIL_NEED = "Email is a required field";
        private final String EMAIL_CONFIRMATION = "This account is not confirmed";
        public DataHandler(LoginRegisterActivity activity,LoginRegisterEmailLoginFragment fragment){
            mActivity=new WeakReference<LoginRegisterActivity>(activity);
            mFragment=new WeakReference<LoginRegisterEmailLoginFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
            switch (msg.what){
                case ProductDao.REQUEST_CHECKVERSION:
//                    if(msg.arg1!= ProductDao.RESPONSE_SUCCESS){
//                        if(mActivity.get()!=null) {
//                            JViewUtils.showMaterialDialog(mActivity.get(),mFragment.get().updateDiaTitle, mFragment.get().updateDiaHintmsg, mFragment.get().updateDiaBtnMsg, mFragment.get().updateListener, false);
//                        }
//                    }
                    break;
                case MyAccountDao.REQUEST_EMAILLOGIN:
                    if(mFragment.get().mDialog!=null){mFragment.get(). mDialog.cancel();}
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SharedPreferences shared = mActivity.get().getSharedPreferences("oldEmail", Activity.MODE_PRIVATE);

                        //成功后将数据放到Entity中
                        SVRAppServiceCustomerLoginReturnEntity loginReturnEntity = (SVRAppServiceCustomerLoginReturnEntity) msg.obj;
                        loginReturnEntity.setEmailLogin(true);
                        // GemfiveApplication.getAppConfiguration().signIn(loginReturnEntity);
                        loginReturnEntity.setLoginType(FirebaseEventUtils.lOGIN_EMAIL);
                        GemfiveApplication.getAppConfiguration().signIn(mActivity.get(), loginReturnEntity);

                        //跳转界面
                        if (loginReturnEntity.getConfirmation() == 1) {

                            mFragment.get().loginRegisterActivity.setSubEmail(mFragment.get().email.getText().toString().trim());
                            mFragment.get().clickEmailInfo.setVisibility(View.VISIBLE);

                        } else {
                            if (false) {
                                String testMessage = "sessionKey:" + loginReturnEntity.getSessionKey() + "\n" + "user id:" + loginReturnEntity.getId();
                                JViewUtils.showMessageDialog(mActivity.get(), testMessage, new OnMessageDialogListener() {
                                    private SVRAppServiceCustomerLoginReturnEntity loginReturnEntity;

                                    public OnMessageDialogListener init(SVRAppServiceCustomerLoginReturnEntity a) {
                                        this.loginReturnEntity = a;
                                        return this;
                                    }
                                    @Override
                                    public void onOKClickListener(Object object) {
                                        Bundle mBundle = new Bundle();
                                        mBundle.putString("sessionKey", loginReturnEntity.getSessionKey());
                                        mActivity.get().startNextActivity(mBundle, HomeActivity.class, false);
                                        mActivity.get().finish();
                                    }
                                }.init(loginReturnEntity));
                            } else {
//                                SendBoardUtil.sendNotificationBoard(mActivity.get(),SendBoardUtil.LOGINCODE,null);
                                Bundle mBundle = new Bundle();
                                mBundle.putString("sessionKey", loginReturnEntity.getSessionKey());
                                boolean oldAccount=mFragment.get().email.getText().toString().equals(shared.getString("email",""));
                                //  if(isStart||(!oldAccount&&fromSignOut)){
                                if(mFragment.get().isStart){
                                    mActivity.get().startNextActivity(mBundle, HomeActivity.class, false);
                                }else {
                                    Intent intent=new Intent();
                                    if(mActivity.get().addToWish){
                                        Bundle bundle=new Bundle();
                                        bundle.putString("productId", mActivity.get().productId);
                                        intent.putExtras(bundle);
                                    }
                                    mActivity.get().setResult(RESULTCODE,intent);
                                }
                                mActivity.get().finish();
                            }
                        }
                        SharedPreferences.Editor editor2 = shared.edit();
                        editor2.putString("email", loginReturnEntity.getEmail());
                        editor2.commit();
                        try {
                            GaTrackHelper.getInstance().googleAnalyticsEvent("Account Action",
                                    "Sign In",
                                    "Email",
                                    Long.valueOf(loginReturnEntity.getId()));
                            FirebaseEventUtils.getInstance().customizedSignIn(mActivity.get(), FirebaseEventUtils.lOGIN_EMAIL);
                        }catch (Exception ex){
                            ex.getStackTrace();
                        }

                    }else{
                        String errorMsg= (String) msg.obj;
                        if(errorMsg.contains("app version")){
//                            JViewUtils.showMaterialDialog(mActivity.get(), "", mFragment.get().updateDiaHintmsg, mFragment.get().updateDiaBtnMsg, mFragment.get().updateListener, false);

                        }else{
                            mFragment.get().error.setText(errorMsg);
                            mFragment.get().error.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case MyAccountDao.REQUEST_FACEBOOKLOGIN:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.dismiss();
                    }
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVRAppserviceCustomerFbLoginReturnEntity result= (SVRAppserviceCustomerFbLoginReturnEntity) msg.obj;
                        if ((result != null) && (result instanceof SVRAppserviceCustomerFbLoginReturnEntity)) {
                            result.setLoginType(FirebaseEventUtils.LOGIN_FACEBOOK);
                            mFragment.get().loginSuccess(result);
                            try {
                                GaTrackHelper.getInstance().googleAnalyticsEvent("Account Action",
                                        "Sign In",
                                        "Facebook",
                                        Long.valueOf(result.getId()));
                                FirebaseEventUtils.getInstance().customizedSignIn(mActivity.get(), FirebaseEventUtils.LOGIN_FACEBOOK);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mFragment.get().fbLoginError();
                        }
                    }else{
                        String errorMsg= (String) msg.obj;
                        if ( !JDataUtils.isEmpty(errorMsg) && errorMsg.contains(EMAIL_NEED)) {
                            mActivity.get().fbGraphAPIUserEntity = mFragment.get().fbGraphAPIUserEntity;
                            mActivity.get().redirectToAttachedFragment(LoginRegisterActivity.EMAIL_BOUND, 1);
                        } else if(!JDataUtils.isEmpty(errorMsg) && errorMsg.contains(EMAIL_CONFIRMATION)){
                            mFragment.get().clickEmailInfo.setVisibility(View.VISIBLE);
                        }else {

                            if(!TextUtils.isEmpty(errorMsg)){
                                JViewUtils.showErrorToast(mActivity.get(),errorMsg);
                            }
                        }
                    }
                    break;


                case MyAccountDao.REQUEST_GOOGLELOGIN:
                    if (mFragment.get().mDialog != null) {
                        mFragment.get().mDialog.dismiss();
                    }
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        SVRAppserviceCustomerFbLoginReturnEntity result= (SVRAppserviceCustomerFbLoginReturnEntity) msg.obj;
                        if ((result != null) && (result instanceof SVRAppserviceCustomerFbLoginReturnEntity)) {
                            result.setLoginType(FirebaseEventUtils.LOGIN_GOOGLE);
                            mFragment.get().loginSuccess(result);
                            try {
                                GaTrackHelper.getInstance().googleAnalyticsEvent("Account Action",
                                        "Sign In",
                                        "Google",
                                        Long.valueOf(result.getId()));
                                FirebaseEventUtils.getInstance().customizedSignIn(mActivity.get(),FirebaseEventUtils.LOGIN_GOOGLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mFragment.get().fbLoginError();
                        }
                    }else{
                        String errorMsg= (String) msg.obj;
                        if ( !JDataUtils.isEmpty(errorMsg) && errorMsg.contains(EMAIL_NEED)) {
                            mActivity.get().fbGraphAPIUserEntity = mFragment.get().fbGraphAPIUserEntity;
                            mActivity.get().redirectToAttachedFragment(LoginRegisterActivity.EMAIL_BOUND, 1);
                        } else if(!JDataUtils.isEmpty(errorMsg) && errorMsg.contains(EMAIL_CONFIRMATION)){
                            mFragment.get().clickEmailInfo.setVisibility(View.VISIBLE);
                        }else {
                            if(!TextUtils.isEmpty(errorMsg)){
                                JViewUtils.showErrorToast(mActivity.get(),errorMsg);
                            }
//
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                    if(msg.arg1==ProductDao.REQUEST_CHECKVERSION){

                    }else {
                        if (mFragment.get().mDialog != null) {
                            mFragment.get().mDialog.dismiss();
                        }
                        RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                        requestErrorHelper.showNetWorkErrorToast(msg);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mMyAccountDao!=null){
            mMyAccountDao.cancelHttpByTag(TAG);
        }
        if(dataHandler!=null){
            dataHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            AnimationSet set = new AnimationSet(true);
            set.setFillAfter(true);
            //上移高度应该为自身的高度
            int textHeight=email_text.getHeight();
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
                case R.id.email:
                    email_text2.setText(getResources().getString(R.string.loginregister_emailbound_email_hint));
                    email_text.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
                    rl_login_email.setBottomLineActive(true);
                    if (email.getText().length()!=0)
                        clearMail.setVisibility(View.VISIBLE);
                    else
                        clearMail.setVisibility(View.INVISIBLE);
                    if (email.getText().toString().trim().equals("")) {
                        email_text2.setVisibility(View.INVISIBLE);
                        email.setHint("");
                        email_text.startAnimation(set);
                   } else {
                        email_text2.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
                    }

                    break;
                case R.id.password:
                    rl_login_pwd.setBottomLineActive(true);
                    password_text.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
                    password_text2.setText(getResources().getString(R.string.enter_password));
                    if (password.getText().length()!=0)
                        clearPassword.setVisibility(View.VISIBLE);
                    else
                        clearPassword.setVisibility(View.GONE);
                    if (password.getText().toString().trim().equals("")) {
                        password_text2.setVisibility(View.INVISIBLE);
                        password.setHint("");
                        password_text.startAnimation(set);
                    } else {
                        password_text2.setTextColor(GemfiveApplication.getAppConfiguration().getThemeConfig().getPrimaryColor());
                    }

                    break;
            }
        } else {
            onblur(v.getId());
            clearMail.setVisibility(View.INVISIBLE);
            clearPassword.setVisibility(View.GONE);
        }
    }

    public boolean onblur(int id) {
        switch (id) {
            case R.id.email:
                rl_login_email.setBottomLineActive(false);
                email_text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                email_text2.setVisibility(View.VISIBLE);
                if (email.getText().toString().trim().equals("")) {
                    email.setHint(getResources().getString(R.string.loginregister_emailbound_email_hint));
                    email_text.clearAnimation();
                    //验证字段
                    email_text2.setText(getResources().getString(R.string.required_field));
                    email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    email_text.clearAnimation();
                    //验证邮箱格式
                    if (!JDataUtils.isEmail(email.getText().toString())) {
                        email_text2.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                        email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;
            case R.id.password:
                rl_login_pwd.setBottomLineActive(false);
                password_text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                password_text2.setVisibility(View.VISIBLE);
                if (password.getText().toString().trim().equals("")) {
                    password.setHint(getResources().getString(R.string.enter_password));
                    password_text.clearAnimation();
                    //验证字段
                    password_text2.setText(getResources().getString(R.string.required_field));
                    password_text2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                } else {
                    password_text.clearAnimation();
                    //验证密码格式
                    if (!JDataUtils.isPassword(password.getText().toString().trim())) {
                        password_text2.setText(getResources().getString(R.string.enter_characters_ignored));
                        password_text2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;
        }
        return true;
    }

//    public void updateFavoriteData(){
//        final SharedPreferences shared=loginRegisterActivity.getSharedPreferences("likes", Activity.MODE_PRIVATE);
//        Set<String> set=new HashSet<String>();
//        set=shared.getStringSet("likes",null);
//
//        if(set!=null) {
//            SVRParameters parameters = new SVRParameters();
//            parameters.put("session_key", GemfiveApplication.getAppConfiguration().getUserInfo(loginRegisterActivity).getSessionKey());
//
//            for (String str : set) {
//                String category_ids = "category_ids[" + str + "]";
//                parameters.put(category_ids, "1");
//            }
//
//            SVRFavoriteUpdate svrhandler = new SVRFavoriteUpdate(loginRegisterActivity, parameters);
//            svrhandler.loadDatasFromServer(new SVRCallback() {
//                @Override
//                public void onSuccess(int resultCode, SVRReturnEntity result) {
//                    //成功后将数据放到Entity中
//                    UpdateFavoriteEntity entity = (UpdateFavoriteEntity) result;
//
//                    //成功后将Favorite数据清空
//                    SharedPreferences.Editor edit = shared.edit();
//                    edit.putStringSet("likes", null);
//                    edit.commit();
//                }
//
//                @Override
//                public void onFailure(int resultCode, String errorMsg) {
//                    if (!JDataUtils.errorMsgHandler(loginRegisterActivity, errorMsg)) {
//                        Toast.makeText(loginRegisterActivity, "error " + resultCode + " " + errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }

//    //存储app版本号到本地
//    public void saveVersion(){
//        SharedPreferences sharedVersion= loginRegisterActivity.getSharedPreferences("Version",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedVersion.edit();
//        editor.putString("Version",getVersion());
//        editor.commit();
//    }

//    //获取app版本
//    public String getVersion() {
//        try {
//            PackageManager manager = loginRegisterActivity.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(loginRegisterActivity.getPackageName(), 0);
//            String version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "0";
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        JLogUtils.i(mCurrTag,"result:"+resultCode);
        if(getActivity()==null)return;
        if (requestCode == RC_SIGN_IN&&resultCode==getActivity().RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String mEmail = acct.getEmail();
                ggUseInfoToLoginRemoteServer(mEmail, acct.getGivenName(), acct.getFamilyName(), acct.getId());
            }else{
                JViewUtils.showToast(loginRegisterActivity, null, getString(R.string.Global_Error_Internet));
            }
        }
    }


    private void ggUseInfoToLoginRemoteServer(String email,String firstName,String lastName,String id) {
        mDialog=JViewUtils.showProgressDialog(getActivity());
        mMyAccountDao.googleLogin(email, firstName, lastName, id, GemfiveApplication.getPhoneConfiguration().getRegistrationToken());
        loginRegisterActivity.setSubEmail(email);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(loginRegisterActivity, true);
        GaTrackHelper.getInstance().googleAnalytics("Sign In screen", loginRegisterActivity);
        JLogUtils.i("googleGA_screen", "Sign In screen");
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
        GaTrackHelper.getInstance().googleAnalyticsReportActivity(loginRegisterActivity, false);
    }
}

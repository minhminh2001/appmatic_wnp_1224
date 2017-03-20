package com.whitelabel.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.LoginRegisterDao;
import com.whitelabel.app.model.SVRAppserviceCustomerFbLoginReturnEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;
import com.whitelabel.app.widget.CustomButton;
import com.whitelabel.app.widget.CustomEditText;
import com.whitelabel.app.widget.CustomTextView;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/6/25.
 */
public class LoginRegisterEmailBoundFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {
    private LoginRegisterActivity loginRegisterActivity;
    private View contentView;

    private ImageView  clearSubmit;
    private CustomButtomLineRelativeLayout rl_emailbound_email;
    private CustomEditText cetEmail;
    private CustomTextView emptyAndfileEmail;
    private CustomTextView hasEmail;
    private CustomButton cbtSubmit;
    private final String TAG = "LoginRegisterEmailBoundFragment";
    private LoginRegisterDao mDao;
    private DataHandler mHandler;
    private InputMethodManager inputMethodManager;
    private ToolBarFragmentCallback toolBarFragmentCallback;

    private static final class DataHandler extends android.os.Handler {
        private final WeakReference<LoginRegisterActivity> mActivity;
        private final WeakReference<LoginRegisterEmailBoundFragment> mFragment;

        public DataHandler(LoginRegisterActivity activity,LoginRegisterEmailBoundFragment fragment) {
            mActivity = new WeakReference<LoginRegisterActivity>(activity);
            mFragment=new WeakReference<LoginRegisterEmailBoundFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null||mFragment.get()==null){
                return;
            }
//            if (mFragment.get().mDialog != null) {
//                mFragment.get().mDialog.cancel();
//            }
            //   JViewUtils.dismissProgressBar(mActivity.get());
            switch (msg.what){
                case LoginRegisterDao.REQUEST_FBUSERINFO:
                    if(msg.arg1==LoginRegisterDao.RESPONSE_SUCCESS) {
                        GemfiveApplication.getAppConfiguration().signIn(mActivity.get(), (SVRAppserviceCustomerFbLoginReturnEntity) msg.obj);
                    }else{

                    }
                    break;
                case LoginRegisterDao.REQUEST_BOUNDUSERINFO:
                    if(msg.arg1==LoginRegisterDao.RESPONSE_SUCCESS) {
                        if (mActivity.get() != null && !mActivity.get().checkIsFinished() && mFragment.get().isAdded()) {
                            mActivity.get().setEmailConfirm(false);
                            mFragment.get().fbUseInfoToLoginRemoteServer(mFragment.get().cetEmail.getText().toString().trim());
                            mActivity.get().switchFragment(LoginRegisterActivity.EMAIL_BOUND, LoginRegisterActivity.REGISTERSUCCESS_FLAG);
                        }
                    }else{
                        String EMAIL_NO_CONFIRMED = "This account is not confirmed";
                        if ( mActivity.get() != null && ! mActivity.get().checkIsFinished() && mFragment.get().isAdded()) {
                            mActivity.get().setEmailConfirm(true);
                            JLogUtils.i("Martin", "emailBoundUseInfoToLoginRemoteServer -> onFailure -> "+ "errorMsg=>" + msg.obj.toString());
                            if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains(EMAIL_NO_CONFIRMED))) {
                                mActivity.get().switchFragment(LoginRegisterActivity.EMAIL_BOUND, LoginRegisterActivity.REGISTERSUCCESS_FLAG);
                            } else {
                                mFragment.get().emailBoundLoginError(msg.obj.toString());
                            }
                        }
                    }
                    break;
                case LoginRegisterDao.REQUEST_ERROR:
                    Toast.makeText(mActivity.get(), mActivity.get().getString(R.string.Global_Error_Internet), Toast.LENGTH_LONG).show();
                    mFragment.get().hasEmail.setVisibility(View.INVISIBLE);
//                    mFragment.get().hasEmail.setText(mActivity.get().getString(R.string.please_check));
                    break;
            }
            super.handleMessage(msg);
        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        toolBarFragmentCallback= (ToolBarFragmentCallback) activity;
        loginRegisterActivity = (LoginRegisterActivity) activity;
    }
    @Override
    public void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void onClickLeftMenu(View v) {
        inputMethodManager = (InputMethodManager)loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(loginRegisterActivity.getWindow().getDecorView().getWindowToken(), 0); //隐藏
        }
        loginRegisterActivity.onBackPressed();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_loginregister_emailbound, null);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.loginregister_emailbound_header_title));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JToolUtils.getDrawable(R.drawable.action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        mHandler=new DataHandler(loginRegisterActivity,this);
        mDao=new LoginRegisterDao(TAG,mHandler);
        cetEmail = (CustomEditText) contentView.findViewById(R.id.cetEmail);
        rl_emailbound_email= (CustomButtomLineRelativeLayout) contentView.findViewById(R.id.rl_emailbound_email);
        hasEmail= (CustomTextView) contentView.findViewById(R.id.hasEmail);
        emptyAndfileEmail= (CustomTextView) contentView.findViewById(R.id.emptyAndfileEmail);
        cbtSubmit = (CustomButton) contentView.findViewById(R.id.cbtSubmit);

        clearSubmit=(ImageView)contentView.findViewById(R.id.clear_submit);

        cbtSubmit.setOnClickListener(this);
        cetEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        cetEmail.setOnClickListener(this);
        clearSubmit.setOnClickListener(this);
        cetEmail.setOnFocusChangeListener(this);
        cetEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0&&cetEmail.isFocused()) {
                    clearSubmit.setVisibility(View.VISIBLE);
                }else {
                    clearSubmit.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.cetEmail:
                    rl_emailbound_email.setBottomLineActive(true);
                    emptyAndfileEmail.setText(getResources().getString(R.string.loginregister_emailbound_email_hint));
                    emptyAndfileEmail.setTextColor(getResources().getColor(R.color.blue5097DA));
                    if (cetEmail.getText().length()!=0) {
                        clearSubmit.setVisibility(View.VISIBLE);
                    }else {
                        clearSubmit.setVisibility(View.GONE);
                        AnimationSet set = new AnimationSet(true);
                        set.setFillAfter(true);
                        //上移高度应该为自身的高度
                        int textHeight=cetEmail.getHeight();
                        Animation tran;
                        if(textHeight>0){
                            tran = new TranslateAnimation(0, 0, textHeight,0);
                        }else {
                            tran = new TranslateAnimation(0, 0, 50, 0);
                        }
                        tran.setDuration(300);
                        //渐变
                        Animation alpha = new AlphaAnimation(0, 1);
                        alpha.setDuration(300);
                        set.addAnimation(tran);
                        set.addAnimation(alpha);
                        emptyAndfileEmail.startAnimation(set);
                    }
                    break;
            }
        } else {
            rl_emailbound_email.setBottomLineActive(false);
            clearSubmit.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cetEmail:
                if(hasEmail.getVisibility()==View.VISIBLE){
                    hasEmail.setVisibility(View.GONE);
                }
                break;
            case R.id.cbtSubmit: {
                JViewUtils.cleanCurrentViewFocus(getActivity());
                JToolUtils.closeKeyBoard(getActivity(), cetEmail);
                if (JDataUtils.isEmpty(cetEmail)) {
                    emptyAndfileEmail.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_empty));
                    emptyAndfileEmail.setTextColor(getResources().getColor(R.color.redC2060A));
                    hasEmail.setVisibility(View.INVISIBLE);
                    return;
                }
                if (!JDataUtils.isEmail(cetEmail)) {
//                    JViewUtils.showToast(loginRegisterActivity, null, getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                    emptyAndfileEmail.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                    emptyAndfileEmail.setTextColor(getResources().getColor(R.color.redC2060A));
                    return;
                }
                emailBoundUseInfoToLoginRemoteServer(cetEmail.getText().toString().trim());
                break;
            }
            case R.id.clear_submit:
                cetEmail.setText("");
                break;
        }
    }

    private void emailBoundUseInfoToLoginRemoteServer(String email) {
        loginRegisterActivity.setMyEmail(email);

        String firstnameStr=loginRegisterActivity.fbGraphAPIUserEntity.getFirst_name();
        String lastnameStr=loginRegisterActivity.fbGraphAPIUserEntity.getLast_name();
        String fb_idStr= loginRegisterActivity.fbGraphAPIUserEntity.getId();
        String device_token=GemfiveApplication.getPhoneConfiguration().getRegistrationToken();

        mDao.emailBoundUseInfoToLoginRemoteServer(email,firstnameStr,lastnameStr,fb_idStr,"0","1",device_token);

    }

    private void emailBoundLoginError(String msg) {
        hasEmail.setVisibility(View.VISIBLE);
        JLogUtils.d("jay","msg"+msg);
        JLogUtils.d("msg",msg);
        hasEmail.setText(msg);
       //JViewUtils.showToast(loginRegisterActivity, null, msg);
    }

    private void emailBoundLoginSuccess(SVRAppserviceCustomerFbLoginReturnEntity fbLoginReturnEntity) {
        GemfiveApplication.getAppConfiguration().signIn(loginRegisterActivity, fbLoginReturnEntity);

        Intent intent = new Intent(loginRegisterActivity, HomeActivity.class);
        startActivity(intent);
        loginRegisterActivity.finish();
        loginRegisterActivity.overridePendingTransition(R.anim.enter_top_bottom, R.anim.exit_top_bottom);
    }


    public void fbUseInfoToLoginRemoteServer(String eamil) {
        String firstnameStr=loginRegisterActivity.fbGraphAPIUserEntity.getFirst_name();
        String lastnameStr=loginRegisterActivity.fbGraphAPIUserEntity.getLast_name();
        String fb_id=loginRegisterActivity.fbGraphAPIUserEntity.getId();
        String device_token=GemfiveApplication.getPhoneConfiguration().getRegistrationToken();
        mDao.fbUseInfoToLoginRemoteServer(eamil,"1",firstnameStr,lastnameStr,fb_id,"1",device_token);

    }
}

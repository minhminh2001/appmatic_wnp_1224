package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.LoginRegisterDao;
import com.whitelabel.app.model.SVRAppServiceCustomerResetpass;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/6/15.
 * 修改密码
 */
public class LoginRegisterResetPassFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener{
    private LoginRegisterActivity loginRegisterActivity;
    private View contentView,view_reset_email_line;
    private Button reset_password;
    private EditText email;
    private TextView email_text, email_text2;
    private final String TAG = "LoginRegisterResetPassFragment";
    private DataHandler mHandler;
    private LoginRegisterDao mDao;
    private Dialog mDialog;
    private ImageView clear;
    private ToolBarFragmentCallback toolBarFragmentCallback;

    private static final class DataHandler extends android.os.Handler {
    private final WeakReference<LoginRegisterActivity> mActivity;
    private final WeakReference<LoginRegisterResetPassFragment> mFragment;

    public DataHandler(LoginRegisterActivity activity,LoginRegisterResetPassFragment fragment) {
        mActivity = new WeakReference<LoginRegisterActivity>(activity);
        mFragment=new WeakReference<LoginRegisterResetPassFragment>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if(mActivity.get()==null||mFragment.get()==null){
            return;
        }
        if (mFragment.get().mDialog != null) {
            mFragment.get().mDialog.cancel();
        }
     //   JViewUtils.dismissProgressBar(mActivity.get());
        switch (msg.what){
            case LoginRegisterDao.REQUEST_GETDATA:
                if(msg.arg1==LoginRegisterDao.RESPONSE_SUCCESS){
                    SVRAppServiceCustomerResetpass resetpassReturnEntity = (SVRAppServiceCustomerResetpass) msg.obj;
                    //切换fragment
                    mActivity.get().redirectToAttachedFragment(LoginRegisterActivity.SENDESUCCESS_FLAG, 1);
                    mActivity.get().setMyEmail(mFragment.get().email.getText().toString().trim());
                }else{
                    if (!JDataUtils.errorMsgHandler( mActivity.get(), msg.obj.toString())) {
                        mFragment.get().email_text2.setText(msg.obj.toString());
                        mFragment.get().email_text2.setTextColor( mFragment.get().getResources().getColor(R.color.red));
                    }
                }
                break;
            case LoginRegisterDao.REQUEST_ERROR:
                RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                requestErrorHelper.showNetWorkErrorToast(msg);
                break;
        }
        super.handleMessage(msg);
    }

}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        super.onAttach(activity);
        try {
            loginRegisterActivity = (LoginRegisterActivity) activity;
            toolBarFragmentCallback= (ToolBarFragmentCallback) activity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public void onClickLeftMenu(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        JViewUtils.cleanCurrentViewFocus(getActivity());
        inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().onBackPressed();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView= inflater.inflate(R.layout.fragment_loginregister_resetpassword, null);

        return contentView;
    }
    @Override
    public void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.retrieve_your_password));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JToolUtils.getDrawable(R.drawable.action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        mHandler=new DataHandler(loginRegisterActivity,this);
        mDao=new LoginRegisterDao(TAG,mHandler);
        email= (EditText) contentView.findViewById(R.id.email);
        view_reset_email_line= contentView.findViewById(R.id.view_reset_email_line);
        email_text= (TextView) contentView.findViewById(R.id.email_text);
        email_text2= (TextView) contentView.findViewById(R.id.email_text2);
        email.setOnFocusChangeListener(this);
        email.setOnClickListener(this);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0&&email.hasFocus()) {
                    clear.setVisibility(View.VISIBLE);
                }else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reset_password= (Button) contentView.findViewById(R.id.reset_password);
        reset_password.setOnClickListener(this);
        email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        clear=(ImageView)contentView.findViewById(R.id.clear);
        clear.setOnClickListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
        if(JDataUtils.isEmail(loginRegisterActivity.getMyEmail())) {
            email.setText(loginRegisterActivity.getMyEmail());
        }
        if(email.getText().toString().length()>0) {
            if (email.hasFocus()) {
                onFocus();
            } else {
                onb1ur();
            }
        }
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
         switch(v.getId()){
             case R.id.email:
                 onFocus();
                 break;
             case R.id.reset_password:
                if(onb1ur()) {
                    //隐藏软盘
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_reset_email_line,false);
                    JViewUtils.cleanCurrentViewFocus(getActivity());
                    inputMethodManager.hideSoftInputFromWindow(email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mDao.loadDataFromServer(email.getText().toString().trim());
                    mDialog = JViewUtils.showProgressDialog(loginRegisterActivity);
                    mDialog.show();
                }
                 break;
             case R.id.clear:
                 email.setText("");
                 break;
         }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId()== R.id.email&&hasFocus){
           onFocus();
            if (email.getText().toString().length()>0) {
                clear.setVisibility(View.VISIBLE);
            }else {
                clear.setVisibility(View.GONE);
            }
        }else{
            clear.setVisibility(View.GONE);
        }
    }
    public void onFocus(){
        AnimationSet set = new AnimationSet(true);
        set.setFillAfter(true);
        //平移
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
        email_text2.setText(getResources().getString(R.string.loginregister_emailbound_email_hint));
        CustomButtomLineRelativeLayout.setBottomLineActive(view_reset_email_line,true);
        if(email.getText().toString().trim().equals("")) {
            email_text2.setVisibility(View.INVISIBLE);
            email.setHint("");
            email_text.startAnimation(set);
        }else {
            email_text2.setTextColor(getResources().getColor(R.color.blue5097DA));
            email_text2.setVisibility(View.VISIBLE);
        }
    }
    public boolean onb1ur(){
        email_text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
        email_text2.setVisibility(View.VISIBLE);
        if(email.getText().toString().trim().equals("")){
            email.setHint(getResources().getString(R.string.loginregister_emailbound_email_hint));
            email_text.clearAnimation();
            //验证字段
            email_text2.setText(getResources().getString(R.string.required_field));
            email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
            return false;
        }else{
            email_text.clearAnimation();
            //验证邮箱格式
            if(!JDataUtils.isEmail(email.getText().toString())){
                email_text2.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
                return false;
            }
        }
        return true;
    }


}

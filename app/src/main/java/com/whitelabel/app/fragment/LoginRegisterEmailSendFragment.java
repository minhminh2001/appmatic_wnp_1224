package com.whitelabel.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.HomeActivity;
import com.whitelabel.app.activity.LoginRegisterActivity;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.callback.ToolBarFragmentCallback;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JToolUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by imaginato on 2015/6/19.
 */
public class LoginRegisterEmailSendFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener{
    private LoginRegisterActivity loginRegisterActivity;
    private View contentView;
    private RelativeLayout rl_send_email;

    private View view_send_email_line;
    private ImageView clearSubmit;
    private Button s_submit;
    private EditText et_email;
    private TextView email_text,email_text2;
    private DataHandler  dataHandler;
    private String TAG;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_loginregister_login_send_email,null);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolBarFragmentCallback.setToolBarTitle(getResources().getString(R.string.send_confirmation_email));
        toolBarFragmentCallback.setToolBarLeftIconAndListenter(JToolUtils.getDrawable(R.drawable.action_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeftMenu(v);
            }
        });
        rl_send_email= (RelativeLayout) contentView.findViewById(R.id.rl_send_email);
        view_send_email_line= contentView.findViewById(R.id.view_send_email_line);
        s_submit= (Button) contentView.findViewById(R.id.s_submit);
        s_submit.setBackgroundColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());

        s_submit.setOnClickListener(this);
        et_email= (EditText) contentView.findViewById(R.id.et_email);
        email_text= (TextView) contentView.findViewById(R.id.email_text);
        email_text.setTextColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        email_text2= (TextView) contentView.findViewById(R.id.email_text2);
        clearSubmit=(ImageView)contentView.findViewById(R.id.clear_submit);
        clearSubmit.setOnClickListener(this);
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && et_email.isFocused()) {
                    clearSubmit.setVisibility(View.VISIBLE);
                } else {
                    clearSubmit.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        email.setOnClickListener(this);
        et_email.setOnFocusChangeListener(this);
        et_email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et_email.setText(loginRegisterActivity.getSubEmail());
        et_email.setFocusable(true);
        et_email.requestFocus();
        TAG=this.getClass().getSimpleName();
        dataHandler=new DataHandler(this,loginRegisterActivity);
        mAccountDao=new MyAccountDao(TAG,dataHandler);

    }
    public void onClickLeftMenu(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        getActivity().onBackPressed();
    }

    public void onClickRightMenu(View v) {
        Intent i = new Intent(loginRegisterActivity, HomeActivity.class);
        startActivity(i);
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

    private static final class DataHandler extends Handler{
         private final WeakReference<LoginRegisterEmailSendFragment> mFragment;
         private final WeakReference<LoginRegisterActivity> mActivity;
         public DataHandler(LoginRegisterEmailSendFragment fragment,LoginRegisterActivity activity){
             mFragment=new WeakReference<LoginRegisterEmailSendFragment>(fragment);
             mActivity=new WeakReference<LoginRegisterActivity>(activity);
         }

         @Override
         public void handleMessage(Message msg) {
             if(mActivity.get()==null&&mFragment.get()==null){
                 return;
             }
             if(mFragment.get().mDialog!=null){
                 mFragment.get().mDialog.dismiss();
             }
             switch (msg.what){
                 case MyAccountDao.REQUEST_CHECKEMAIL:
                      if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                          mActivity.get().redirectToAttachedFragment(LoginRegisterActivity.EMAILLOGIN_FLAG, 1);
                      }else{
                          String errorMsg= (String) msg.obj;
                          mFragment.get().email_text2.setText(errorMsg);
                          mFragment.get().email_text2.setTextColor(mActivity.get().getResources().getColor(R.color.redC2060A));
                      }
                     break;
                 case MyAccountDao.ERROR:
                     RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                     requestErrorHelper.showNetWorkErrorToast(msg);
                     break;
             }
             super.handleMessage(msg);
         }
     }


    private Dialog mDialog;
    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) loginRegisterActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()){
            case R.id.clear_submit:
                et_email.setText("");
                break;
            case R.id.s_submit:
              if(onblur()) {
                  //清除紫线
                  CustomButtomLineRelativeLayout.setBottomLineActive(view_send_email_line,false);
                  rl_send_email.setFocusable(true);
                  rl_send_email.requestFocus();
                  //隐藏软盘
                  inputMethodManager.hideSoftInputFromWindow(et_email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                  mDialog= JViewUtils.showProgressDialog(loginRegisterActivity);
                  mAccountDao.checkEmail(et_email.getText().toString().trim());
              }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            onFocus();
            clearSubmit.setVisibility(View.VISIBLE);
        }else{
            clearSubmit.setVisibility(View.GONE);
            onblur();
        }
    }
    //获得焦点
    public void onFocus(){
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
        //平移
        tran.setDuration(300);
        //渐变
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        set.addAnimation(tran);
        set.addAnimation(alpha);
        email_text2.setVisibility(View.VISIBLE);
        email_text2.setText(getResources().getString(R.string.loginregister_emailbound_email_hint));
        if(et_email.getText().toString().trim().equals("")) {
            email_text2.setVisibility(View.INVISIBLE);
            et_email.setHint("");
            email_text.startAnimation(set);
        }else {
          email_text2.setVisibility(View.VISIBLE);
//            email_text.setAnimation(set);
            email_text2.setTextColor(getResources().getColor(R.color.blue5097DA));
        }
        CustomButtomLineRelativeLayout.setBottomLineActive(view_send_email_line,true);
    }
    public boolean onblur(){
        email_text2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
        email_text2.setVisibility(View.VISIBLE);
        if(et_email.getText().toString().trim().equals("")){
            et_email.setHint(getResources().getString(R.string.loginregister_emailbound_email_hint));
            email_text.clearAnimation();
            //验证字段
            email_text2.setText(getResources().getString(R.string.required_field));
            email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
            return false;
        }else{
            email_text.clearAnimation();
            //验证邮箱格式
            if(!JDataUtils.isEmail(et_email.getText().toString())){
                email_text2.setText(getResources().getString(R.string.loginregister_emailbound_tips_error_email_format));
                email_text2.setTextColor(getResources().getColor(R.color.redC2060A));
               return false;
            }
        }
          return true;
    }


}

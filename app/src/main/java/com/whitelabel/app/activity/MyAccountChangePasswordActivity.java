package com.whitelabel.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whitelabel.app.R;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.dao.MyAccountDao;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.utils.RequestErrorHelper;
import com.whitelabel.app.widget.CustomButtomLineRelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyAccountChangePasswordActivity extends com.whitelabel.app.BaseActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private View contentView;
    private View view_changepwd_oldpwd_line,view_changepwd_newpwd_line,view_changepwd_confpwd_line;
    private EditText old,newPassword,confirm;
    private TextView oldText,oldText2,newPasswordText,newPasswordText2,confirmText,confirmText2;
    private ImageView clearOldPassword,clearNewPassword,clearConfirmPassword;
    private MyAccountDao mDao;
    private final String TAG = "MyAccountChangePasswordFragment";
    private DataHandler mHandler;
    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_myaccount_changepassword);
        initToolbar();
        initView();
        old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    clearOldPassword.setVisibility(View.VISIBLE);
                else
                    clearOldPassword.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0)
                    clearNewPassword.setVisibility(View.VISIBLE);
                else
                    clearNewPassword.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0)
                    clearConfirmPassword.setVisibility(View.VISIBLE);
                else
                    clearConfirmPassword.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }
    private void initToolbar() {
        setTitle(getResources().getString(R.string.change_password));
        setLeftMenuIcon(R.drawable.action_back);
        setLeftMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                        clickSaveOption();
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                clickSaveOption();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void clickSaveOption() {
        if(onblurAll(R.id.old)&&onblurAll(R.id.newpassword)&&onblurAll(R.id.confirm)) {
            mDialog = JViewUtils.showProgressDialog(this);//test
            String session_key = GemfiveApplication.getAppConfiguration().getUserInfo(this).getSessionKey();
            mDao.changePass(session_key, old.getText().toString().trim(), newPassword.getText().toString().trim(), confirm.getText().toString().trim());
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.clear_old_password:
                old.setText("");
                break;
            case R.id.clear_password:
                newPassword.setText("");
                break;
            case R.id.clear_confirm_password:
                confirm.setText("");
                break;
        }
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.old:
                    onFocus(old, oldText, oldText2, "Old Password");
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_oldpwd_line,true);
                    if (old.getText().length() != 0)
                        clearOldPassword.setVisibility(View.VISIBLE);
                    else
                        clearOldPassword.setVisibility(View.GONE);
                    break;
                case R.id.newpassword:
                    onFocus(newPassword, newPasswordText, newPasswordText2, "New Password");

                    CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_newpwd_line, true);
                    if (newPassword.getText().length()!=0)
                        clearNewPassword.setVisibility(View.VISIBLE);
                    else
                        clearNewPassword.setVisibility(View.GONE);
                    break;
                case R.id.confirm:
                    onFocus(confirm, confirmText, confirmText2, "Confirm New Password");
                    CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_confpwd_line, true);
                    if (confirm.getText().length()!=0)
                        clearConfirmPassword.setVisibility(View.VISIBLE);
                    else
                        clearConfirmPassword.setVisibility(View.GONE);
                    break;
            }
        }else{
            onblurAll(v.getId());
            clearOldPassword.setVisibility(View.GONE);
            clearNewPassword.setVisibility(View.GONE);
            clearConfirmPassword.setVisibility(View.GONE);
        }
    }
    public boolean onblurAll(int id){
        switch (id){
            case R.id.old:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_oldpwd_line,false);
                oldText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                oldText2.setVisibility(View.VISIBLE);
                if(old.getText().toString().trim().equals("")){
                    old.setHint("Old Password");
                    oldText.clearAnimation();
                    //验证字段
                    oldText2.setText(getResources().getString(R.string.current_password));
                    oldText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    oldText.clearAnimation();
                    //验证密码格式正确性
                    if(!JDataUtils.isPassword(old)){
                        oldText2.setText(getResources().getString(R.string.current_password));
                        oldText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;
            case R.id.newpassword:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_newpwd_line,false);
                newPasswordText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                newPasswordText2.setVisibility(View.VISIBLE);
                if(newPassword.getText().toString().trim().equals("")){
                    newPassword.setHint("New Password");
                    newPasswordText.clearAnimation();
                    //验证字段
                    newPasswordText2.setText(getResources().getString(R.string.password_empty));
                    newPasswordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    newPasswordText.clearAnimation();
                    //验证密码格式正确性
                    if(!JDataUtils.isPassword(newPassword)){
                        newPasswordText2.setText(getResources().getString(R.string.enter_characters_ignored));
                        newPasswordText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;
            case R.id.confirm:
                CustomButtomLineRelativeLayout.setBottomLineActive(view_changepwd_confpwd_line,false);
                confirmText2.setTextColor(getResources().getColor(R.color.label_saved));//设置为灰色
                confirmText2.setVisibility(View.VISIBLE);
                if(confirm.getText().toString().trim().equals("")){
                    confirm.setHint("Confirm New Password");
                    confirmText.clearAnimation();
                    //验证字段
                    if(newPassword.getText().toString().trim().equals("")) {
                        confirmText2.setText(getResources().getString(R.string.password_empty));
                    }else{
                        confirmText2.setText(getResources().getString(R.string.confirmation_password));
                    }
                    confirmText2.setTextColor(getResources().getColor(R.color.redC2060A));
                    return false;
                }else{
                    confirmText.clearAnimation();
                    if(!confirm.getText().toString().trim().equals(newPassword.getText().toString().trim())){
                        confirmText2.setText(getResources().getString(R.string.confirmation_password));
                        confirmText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }else if(!JDataUtils.isPassword(confirm)){
                        confirmText2.setText(getResources().getString(R.string.enter_characters_ignored));
                        confirmText2.setTextColor(getResources().getColor(R.color.redC2060A));
                        return false;
                    }
                }
                break;

        }
        return true;
    }

    public void onFocus(EditText edit,TextView text,TextView text2,String hint ){
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
        text2.setText(hint);
        if(edit.getText().toString().trim().equals("")) {
            text2.setVisibility(View.INVISIBLE);
            edit.setHint("");
            text.startAnimation(set);
        }else{
            text2.setTextColor(getResources().getColor(R.color.blue5097DA));
        }
    }


    @Override
    public void onDestroy() {
        mDao.cancelHttpByTag(TAG);
        super.onDestroy();
    }

    private static final class DataHandler extends android.os.Handler {
        private final WeakReference<MyAccountChangePasswordActivity> mActivity;


        public DataHandler(MyAccountChangePasswordActivity activity) {
            mActivity = new WeakReference<MyAccountChangePasswordActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(mActivity.get()==null){
                return;
            }
            if(mActivity.get().mDialog!=null){ mActivity.get().mDialog.cancel();}
            switch (msg.what){
                case MyAccountDao.REQUEST_CHANGEPASS:
                    if(msg.arg1==MyAccountDao.RESPONSE_SUCCESS){
                        //成功后将数据放到Entity中
                        mActivity.get().onBackPressed();
                    }else{
                        if (!JDataUtils.errorMsgHandler(mActivity.get(), msg.obj.toString())) {
                            //失败后通过resultCode信息进行处理
                            mActivity.get().oldText2.setText(msg.obj.toString());
                            mActivity.get().oldText2.setTextColor(mActivity.get().getResources().getColor(R.color.redC2060A));
                        }
                    }
                    break;
                case MyAccountDao.ERROR:
                        if ((!JDataUtils.isEmpty(msg.obj.toString())) && (msg.obj.toString().contains("session expired,login again please"))) {
                            Intent intent = new Intent();
                            intent.putExtra("expire", true);
                            intent.setClass(mActivity.get(), LoginRegisterActivity.class);
                            mActivity.get().startActivityForResult(intent, 1000);
                            return;
                        }
                    RequestErrorHelper requestErrorHelper=new RequestErrorHelper(mActivity.get());
                    requestErrorHelper.showNetWorkErrorToast(msg);
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void initView() {
        mHandler=new DataHandler(this);
        mDao=new MyAccountDao(TAG,mHandler);
        old= (EditText) findViewById(R.id.old);
        oldText= (TextView) findViewById(R.id.old_text);
        oldText2=(TextView)findViewById(R.id.old_text2);
        newPassword= (EditText) findViewById(R.id.newpassword);
        newPasswordText= (TextView) findViewById(R.id.newpassword_text);
        newPasswordText2= (TextView) findViewById(R.id.newpassword_text2);
        confirm= (EditText) findViewById(R.id.confirm);
        confirmText= (TextView) findViewById(R.id.confirm_text);
        confirmText2= (TextView) findViewById(R.id.confirm_text2);
        confirm.setOnFocusChangeListener(this);
        old.setOnFocusChangeListener(this);
        newPassword.setOnFocusChangeListener(this);
        clearOldPassword=(ImageView)findViewById(R.id.clear_old_password);
        clearNewPassword=(ImageView)findViewById(R.id.clear_password);
        clearConfirmPassword=(ImageView)findViewById(R.id.clear_confirm_password);

        view_changepwd_oldpwd_line=findViewById(R.id.view_changepwd_oldpwd_line);
        view_changepwd_newpwd_line=findViewById(R.id.view_changepwd_newpwd_line);
        view_changepwd_confpwd_line=findViewById(R.id.view_changepwd_confpwd_line);

        clearOldPassword.setOnClickListener(this);
        clearNewPassword.setOnClickListener(this);
        clearConfirmPassword.setOnClickListener(this);
    }
}

package com.whitelabel.app.ui.notifyme;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.JViewUtil;
import com.whitelabel.app.BaseDialogFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JViewUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import injection.components.DaggerPresenterComponent1;
import injection.modules.PresenterModule;

/**
 * Created by Aaron on 2018/3/26.
 */

public class NotifyMeDialogFragment extends BaseDialogFragment<NotifyMeConstract.Presenter> implements NotifyMeConstract.View{

    enum InvalidationCode {
        OK,
        NAME_IS_EMPTY,
        EMAIL_IS_EMPTY,
        EMAIL_INVALID
    }

    enum InvalidationType{
        USER_NAME,
        USER_EMAIL
    }

    private static final String TAG = "NotifyMeDialogFragment";

    public static final String FRAGMENT_ARG_PRODUCT_ID = "productid";
    public static final String FRAGMENT_ARG_STORE_ID = "storeid";
    public static final String FRAGMENT_ARG_NAME = "name";
    public static final String FRAGMENT_ARG_EMAIL = "email";
    public static final String FRAGMENT_ARG_SESSION_KEY = "sessionkey";

    @BindView(R.id.cb_notifyme)
    public CheckBox cbNotifyMeWithEmail;
    @BindView(R.id.et_name)
    public EditText etName;
    @BindView(R.id.et_email)
    public EditText etEmail;
    @BindView(R.id.btn_notify_me)
    public Button btnNotifyMe;
    @BindView(R.id.btn_cancel)
    public Button btnCancel;
    @BindView(R.id.til_name)
    public TextInputLayout tilName;
    @BindView(R.id.til_email)
    public TextInputLayout tilEmail;
    @BindView(R.id.tv_errmssage)
    public TextView tvErrMessage;

    private Unbinder unbinder = null;
    private String productId;
    private String storeId;
    private String name;
    private String email;
    private String sessionKey;
    private NotifyMeListener notifyMeListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view  = inflater.inflate(R.layout.dialog_notify_me, container);

        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();


        return view;
    }

    public void show(FragmentManager fragmentManager){
        if(fragmentManager == null) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if(fragment != null) {
            transaction.remove(fragment);
        }

        show(transaction, TAG);
    }

    public void hide(){
        dismissAllowingStateLoss();
    }

    private void initView(){

        cbNotifyMeWithEmail.setChecked(true);
        etName.setText(name);
        etEmail.setText(email);

        JViewUtils.setSoildButtonGlobalStyle(getActivity(), btnNotifyMe);
        JViewUtils.setStrokeButtonGlobalStyle(getActivity(), btnCancel);

    }

    private void initData(){
        Bundle bundle = getArguments();
        if(bundle == null) {
            return;
        }

        productId = bundle.getString(FRAGMENT_ARG_PRODUCT_ID);
        storeId = bundle.getString(FRAGMENT_ARG_STORE_ID);
        name = bundle.getString(FRAGMENT_ARG_NAME);
        email = bundle.getString(FRAGMENT_ARG_EMAIL);
        sessionKey = bundle.getString(FRAGMENT_ARG_SESSION_KEY);
    }

    @Override
    public void inject(){
        DaggerPresenterComponent1.builder()
                .applicationComponent(WhiteLabelApplication.getApplicationComponent()).
                presenterModule(new PresenterModule(getActivity())).build().inject(this);
    }

    public void setNotifyMeListener(NotifyMeListener listener){
        if(listener == null) {
            return;
        }

        notifyMeListener = listener;
    }

    @OnCheckedChanged(R.id.cb_notifyme)
    public void onNotifyMeCheckChanged(boolean bCheck){

        etName.setEnabled(bCheck);
        etEmail.setEnabled(bCheck);
    }

    @OnClick(value = {R.id.btn_notify_me, R.id.btn_cancel})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_notify_me:
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();

                InvalidationCode errCodeByName = invalidationData(name, InvalidationType.USER_NAME);
                if(errCodeByName != InvalidationCode.OK){
                    showErrMessage(errCodeByName);
                } else {
                    hideErrMessage(InvalidationType.USER_NAME);
                }

                InvalidationCode errCodeByEmail = invalidationData(email, InvalidationType.USER_EMAIL);
                if(errCodeByEmail != InvalidationCode.OK){
                    showErrMessage(errCodeByEmail);
                } else {
                    hideErrMessage(InvalidationType.USER_EMAIL);
                }

                if(errCodeByName == InvalidationCode.OK
                        && errCodeByEmail == InvalidationCode.OK){
                    mPresenter.registerNotifyForProduct(productId, storeId, name, email, sessionKey);
                }
                break;
            case R.id.btn_cancel:
                hide();
                break;
        }
    }

    private InvalidationCode invalidationData(String value, InvalidationType type){

        InvalidationCode result = InvalidationCode.OK;
        switch(type){
            case USER_NAME:
                if(TextUtils.isEmpty(value)){
                    result = InvalidationCode.NAME_IS_EMPTY;
                }
                break;
            case USER_EMAIL:
                if(TextUtils.isEmpty(value)){
                    result = InvalidationCode.EMAIL_IS_EMPTY;
                } else if(!JDataUtils.isEmail(value)){
                    result = InvalidationCode.EMAIL_INVALID;
                }
                break;
        }

        return result;
    }

    private void showErrMessage(InvalidationCode errCode){
        switch(errCode){
            case NAME_IS_EMPTY:
                tilName.setErrorEnabled(true);
                tilName.setError(getString(R.string.apply_hint_red));
                break;
            case EMAIL_IS_EMPTY:
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(getString(R.string.apply_hint_red));
                break;
            case EMAIL_INVALID:
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(getString(R.string.loginregister_emailbound_tips_error_email_format));
                break;
        }
    }

    private void hideErrMessage(InvalidationType type){
        if(type == InvalidationType.USER_NAME){
            tilName.setError("");
            tilName.setErrorEnabled(false);
        } else if(type == InvalidationType.USER_EMAIL){
            tilEmail.setError("");;
            tilEmail.setErrorEnabled(false);
        }
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Log.i(errorMsg, TAG);
        tvErrMessage.setVisibility(View.VISIBLE);
        tvErrMessage.setText(errorMsg);
    }

    @Override
    public void onSuccess() {

        if(notifyMeListener != null){
            notifyMeListener.onCloseNotifyMeDialog();
        }

        hide();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //TODO(Aaron):Release butterknife
        unbinder.unbind();
    }

    public interface NotifyMeListener{
        public void onCloseNotifyMeDialog();
    }
}

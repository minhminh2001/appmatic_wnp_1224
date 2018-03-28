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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.whitelabel.app.BaseDialogFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

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
        if(fragmentManager == null)
            return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if(fragment != null)
            transaction.remove(fragment);

        show(transaction, TAG);
    }

    public void hide(){
        dismissAllowingStateLoss();
    }

    private void initView(){

        cbNotifyMeWithEmail.setChecked(true);
        etName.setText(name);
        etEmail.setText(email);
    }

    private void initData(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;

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
                if(TextUtils.isEmpty(name)){
                    tilName.setError(getString(R.string.apply_hint_red));
                }
                if(TextUtils.isEmpty(email)){
                    tilEmail.setError(getString(R.string.apply_hint_red));
                }else if(!JDataUtils.isEmail(email)){
                    tilEmail.setError(getString(R.string.loginregister_emailbound_tips_error_email_format));
                }
                JLogUtils.v(TAG, "productid:" + productId);
                mPresenter.registerNotifyForProduct(productId, storeId, name, email, sessionKey);
                break;
            case R.id.btn_cancel:
                hide();
                break;
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
        hide();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //TODO(Aaron):Release butterknife
        unbinder.unbind();
    }
}

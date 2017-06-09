package com.whitelabel.app.ui.checkout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.whitelabel.app.BaseFragment;
import com.whitelabel.app.R;
import com.whitelabel.app.ui.checkout.model.CheckoutDefaultAddressResponse;
import com.whitelabel.app.utils.JViewUtils;
import com.whitelabel.app.widget.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutDefaultAdressFragment extends BaseFragment<CheckoutDefaultAddressContract.Presenter>
        implements CheckoutDefaultAddressContract.View {

    // TODO: Rename parameter arguments, choose names that match
    private Dialog mProgressDialog;

    public CheckoutDefaultAdressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckoutDefaultAdressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutDefaultAdressFragment newInstance() {
        CheckoutDefaultAdressFragment fragment = new CheckoutDefaultAdressFragment();
        return fragment;
    }

    @Override
    public CheckoutDefaultAddressContract.Presenter getPresenter() {
        return new CheckoutDefaultAddressPresenter();
    }
    @Override
    public void showErrorMsg(String errorMsg) {
        JViewUtils.showErrorToast(getActivity(), errorMsg);
    }

    @Override
    public void showData(CheckoutDefaultAddressResponse checkoutDefaultAddressResponse) {


    }

    @Override
    public void openProgressDialog() {
        mProgressDialog = JViewUtils.showProgressDialog(getActivity());
    }

    @Override
    public void dissmissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout_default_adress, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openProgressDialog();
        mPresenter.getDefaultAddress();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

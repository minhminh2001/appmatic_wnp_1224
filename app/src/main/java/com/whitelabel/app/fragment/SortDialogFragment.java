package com.whitelabel.app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.whitelabel.app.R;
import com.whitelabel.app.listener.OnFilterSortFragmentListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortDialogFragment extends DialogFragment {

    @BindView(R.id.fl_Container)
    FrameLayout flContainer;

    public int showX;
    public int showY;

    private OnFilterSortFragmentListener fragmentListener;

    public SortDialogFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup containter, Bundle saveInstanceState){

        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //params.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        params.gravity = Gravity.TOP;
        params.y = showY;
        window.setAttributes(params);

        View view = layoutInflater.inflate(R.layout.fragment_sort_dialog, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ProductListSortFragment fragment = new ProductListSortFragment();
        fragment.setFragmentListener(fragmentListener);
        fragment.setArguments(getArguments());

        FragmentManager fragmentManager = this.getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_Container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void setFragmentListener(OnFilterSortFragmentListener listener) {
        this.fragmentListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}

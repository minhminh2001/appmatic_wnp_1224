package com.whitelabel.app.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by ray on 2017/3/20.
 */

public abstract  class BaseFragment<T extends BasePresenter> extends Fragment  implements BaseView {
    protected  T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=getPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }

    protected  abstract T getPresenter();
}

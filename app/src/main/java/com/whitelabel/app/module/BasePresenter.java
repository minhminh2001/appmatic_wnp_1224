package com.whitelabel.app.module;

import android.view.View;

/**
 * Created by ray on 2017/3/20.
 */

public  interface BasePresenter<T extends BaseView>  {
    void attachView(T mvpView);
    void detachView();
}

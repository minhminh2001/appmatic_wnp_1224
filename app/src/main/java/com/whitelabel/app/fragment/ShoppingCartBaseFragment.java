package com.whitelabel.app.fragment;

import com.whitelabel.app.ui.BasePresenter;

public class ShoppingCartBaseFragment<T extends BasePresenter> extends HomeBaseFragment<T> {

    public final static int FROM_HOME=2;
    public final static int FROM_OTHER=0;

    public void refresh(){}
}

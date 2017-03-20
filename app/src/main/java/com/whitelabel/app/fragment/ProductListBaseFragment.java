package com.whitelabel.app.fragment;

/**
 * Created by imaginato on 2015/8/7.
 */
public abstract class ProductListBaseFragment extends com.whitelabel.app.BaseFragment {
    public abstract void onBackPressed();
    public abstract void onFilterWidgetClick(boolean show);
    public abstract void onSortWidgetClick(boolean show);
    public abstract void onSlideToTop();
    public boolean isDoubleCol=true;
}

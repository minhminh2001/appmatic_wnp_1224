package com.whitelabel.app.ui.common;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface BasePresenter<V extends BaseView> {

    void attachView(V mvpView);
    void detachView();
}

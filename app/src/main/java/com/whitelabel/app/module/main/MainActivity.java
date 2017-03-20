package com.whitelabel.app.module.main;

import android.os.Bundle;
import com.whitelabel.app.R;
import com.whitelabel.app.module.BaseActivity;
public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
    @Override
    protected MainContract.Presenter getPresenter() {
        return new MainPresenterImpl();
    }
}

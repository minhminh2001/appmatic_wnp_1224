package com.whitelabel.app.module.main;

import com.whitelabel.app.module.BasePresenter;
import com.whitelabel.app.module.BaseView;

/**
 * Created by ray on 2017/3/20.
 */

public class MainContract {

    public interface  View extends BaseView{

    }

    public interface  Presenter extends BasePresenter<View>{

    }
}

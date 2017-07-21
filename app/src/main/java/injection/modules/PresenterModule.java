package injection.modules;

import android.app.Activity;

import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.ui.home.HomeCategoryDetailContract;
import com.whitelabel.app.ui.home.HomeContract;
import com.whitelabel.app.ui.home.HomeHomeContract;
import com.whitelabel.app.ui.home.presenter.HomeCategoryDetailPresenterImpl;
import com.whitelabel.app.ui.home.presenter.HomeHomePresenterImpl;
import com.whitelabel.app.ui.home.presenter.HomePresenterImpl;
import com.whitelabel.app.ui.start.StartContract;
import com.whitelabel.app.ui.start.StartPresenterImpl;

import dagger.Module;
import dagger.Provides;
import injection.ActivityScope;

/**
 * Created by ray on 2017/5/5.
 */

@Module
public class PresenterModule {
    private Activity mActivity;
    public PresenterModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    public StartContract.Presenter  provideStartPresenter(IBaseManager configService){
        return new StartPresenterImpl(configService);
    }
    @Provides
    @ActivityScope
    public HomeContract.Presenter  provideHomeFragmentV2Presenter(ICommodityManager iCommodityManager,IBaseManager configService){
        return new HomePresenterImpl(iCommodityManager,configService);
    }

    @Provides
    @ActivityScope
    public  HomeHomeContract.Presenter provideHomeHomeFragmentV1Presenter(ICommodityManager iCommodityManager){
        return new HomeHomePresenterImpl(iCommodityManager);
    }

    @Provides
    @ActivityScope
    public  HomeCategoryDetailContract.Presenter provideHomeHomeV3Presenter(ICommodityManager iCommodityManager,IBaseManager iBaseManager){
        return new HomeCategoryDetailPresenterImpl(iCommodityManager,iBaseManager);
    }


}

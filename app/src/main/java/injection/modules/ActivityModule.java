package injection.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/5/26.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return mActivity;
    }
}

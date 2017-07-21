package injection.modules;

import android.app.Application;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ray on 2017/5/5.
 */
@Module
public class ApplicationModule {
    private Application mApplication;
    public ApplicationModule(Application application) {
        this.mApplication = application;
    }
    @Provides
    @Singleton
    public Application providesApplication() {
        return this.mApplication;
    }
}


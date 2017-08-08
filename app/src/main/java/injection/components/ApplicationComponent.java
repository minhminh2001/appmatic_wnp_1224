package injection.components;

import android.app.Application;

import com.whitelabel.app.GlobalData;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.BaseApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.data.retrofit.ShoppingCartApi;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICheckoutManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.data.service.IShoppingCartManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import injection.modules.ApplicationModule;
import injection.modules.NetModule;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ray on 2017/5/5.
 */
@Component(modules = {ApplicationModule.class,NetModule.class})
@Singleton
public interface ApplicationComponent {
    @Named("DefaultOkHttpClient")
    OkHttpClient okHttpClient();
    @Named("DefaultRetrofit")
    Retrofit defaultRetrofit();
    Application appliation();
    IBaseManager getIBaseManager();
    ICommodityManager getICommodityManager();
    IAccountManager getIAccountManager();
    IShoppingCartManager getIShoppingCartManager();
    ICheckoutManager getICheckoutManager();
    void inject(WhiteLabelApplication app);
    final class Initializer {
        private Initializer() {
        }
        public static ApplicationComponent init(WhiteLabelApplication app) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(app)).netModule(new NetModule(GlobalData.serviceRequestUrl,GlobalData.mockUrl,GlobalData.
                            apiVersion,GlobalData.apiKey,GlobalData.appKey,GlobalData.appVersion,GlobalData.serviceVersion,GlobalData.oneAll_Subdomain))
                    .build();
        }
    }
}

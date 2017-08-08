package injection.modules;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.BaseApi;
import com.whitelabel.app.data.retrofit.CheckoutApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.data.retrofit.MyAccoutApi;
import com.whitelabel.app.data.retrofit.OneAllApi;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.data.retrofit.ShoppingCartApi;
import com.whitelabel.app.data.service.AccountManager;
import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.data.service.CheckoutManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.data.service.ICheckoutManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.data.service.IShoppingCartManager;
import com.whitelabel.app.data.service.ShoppingCartManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ray on 2017/5/8.
 */
@Module
public class NetModule {
    private String mRequestUri;
    private String mGooleTrackId;
    private String mMockUrl;
    private String apiVersion;
    private String apiKey;
    private String appKey;
    private String versionNumber;
    private String serviceVersion;
    private String oneAllUrl;
    public NetModule(String requestUrl, String mockUrl,String apiVersion,String apiKey,String appKey,String versionNumber,String serviceVersion,String subdoMain){
        this.mRequestUri=requestUrl;
        this.mMockUrl=mockUrl;
        this.apiVersion=apiVersion;
        this.apiKey=apiKey;
        this.appKey=appKey;
        this.versionNumber=versionNumber;
        this.serviceVersion=serviceVersion;
        oneAllUrl= String.format("https://%s.api.oneall.com", subdoMain);;
    }
    @Provides
    @Named("DefaultOkHttpClient")
    @Singleton
    public OkHttpClient provideDefaultOkHttpClient(){
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl url=original.url().newBuilder()
                        .addEncodedQueryParameter("platformId","2")
                        .addEncodedQueryParameter("versionNumber",versionNumber)
                        .addEncodedQueryParameter("serviceVersion",serviceVersion).build();
                Request.Builder  builder1=original.newBuilder()
                        .header("API-VERSION", apiVersion)
                        .header("API-KEY", apiKey)
                        .header("APP-KEY",appKey);
                Request request1=builder1.method(original.method(), original.body()).url(url).build();
                return chain.proceed(request1);
            }
        });
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient mOkHttpClient=builder.build();
        return mOkHttpClient;
    }
    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }
    @Provides
    @Named("DefaultRetrofit")
    @Singleton
    public Retrofit provideDefaultRetrofit(Gson gson, @Named("DefaultOkHttpClient") OkHttpClient client){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(mRequestUri)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }


    @Provides
    @Named("MockRetrofit")
    @Singleton
    public Retrofit provideMockRetrofit(Gson gson, @Named("DefaultOkHttpClient") OkHttpClient client){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(mMockUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    @Provides
    @Named("OneAllRetroift")
    public Retrofit provideOneAllRetrofit(Gson gson, @Named("DefaultOkHttpClient") OkHttpClient client){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(oneAllUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }


    @Provides
    public CheckoutApi providesCheckoutApi(@Named("DefaultRetrofit") Retrofit retrofit){
        return retrofit.create(CheckoutApi.class);
    }


    @Provides
    public ProductApi providesIProductApi(@Named("DefaultRetrofit") Retrofit retrofit){
        return retrofit.create(ProductApi.class);
    }


    @Provides
    public MockApi providesMockApi(@Named("MockRetrofit") Retrofit retrofit){
        return retrofit.create(MockApi.class);
    }

    @Provides
    public ShoppingCartApi providesShoppingCartApi(@Named("DefaultRetrofit") Retrofit retrofit){
        return retrofit.create(ShoppingCartApi.class);
    }
    @Provides
    public MyAccoutApi providesMyAccountApi(@Named("DefaultRetrofit") Retrofit retrofit){
        return retrofit.create(MyAccoutApi.class);
    }
    @Provides
    public BaseApi providesBaseApi(@Named("DefaultRetrofit") Retrofit retrofit){
        return retrofit.create(BaseApi.class);
    }

    @Provides
    public OneAllApi providesOneAllApi(@Named("OneAllRetroift") Retrofit retrofit){
        return retrofit.create(OneAllApi.class);

    }
    @Provides
    public ICacheApi  providesCacheApi(){
        return new PreferHelper();
    }

    @Provides
    public IBaseManager  providesIBaesManager(MockApi mockApi,BaseApi baseApi,ICacheApi iCacheApi){
        return new BaseManager(mockApi,baseApi,iCacheApi);
    }
    @Provides
    public IShoppingCartManager providesIShoppingCartManager(ShoppingCartApi shoppingCartApi,ICacheApi iCacheApi){
          return new ShoppingCartManager(shoppingCartApi,iCacheApi);
    }
    @Provides
    public IAccountManager  providesIAccountManager(MyAccoutApi myAccoutApi , ICacheApi iCacheApi, OneAllApi oneAllApi){
        return new AccountManager(myAccoutApi,iCacheApi,oneAllApi);
    }

    @Provides
    public ICommodityManager providesICommodityManager(ProductApi productApi,ICacheApi iCacheApi){
        return new CommodityManager(productApi,iCacheApi);
    }


    @Provides
    public ICheckoutManager providesICheckoutManager(CheckoutApi checkoutApi){
            return new CheckoutManager(checkoutApi);
    }


   @Provides
   @Singleton
   public GoogleAnalytics providesGooleAnalytics(){
       return GoogleAnalytics.getInstance(WhiteLabelApplication.getInstance());
   }
    @Provides
    @Singleton
    public Tracker providesGoogleTracker(GoogleAnalytics googleAnalytics){
        Tracker mTracker = googleAnalytics.newTracker(mGooleTrackId);
        //Tracker mTracker = analytics.newTracker(R.xml.global_tracker);
        googleAnalytics.getLogger()
                .setLogLevel(Logger.LogLevel.VERBOSE);
        mTracker.enableExceptionReporting(true);
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.enableAutoActivityTracking(false);
        return mTracker;
    }
}

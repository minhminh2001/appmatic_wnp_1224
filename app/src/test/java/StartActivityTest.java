import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.ui.start.StartActivityV2;
import com.whitelabel.app.ui.start.StartPresenterImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/4.
 */

import static org.mockito.Mockito.verify;
public class StartActivityTest {
    @Mock
    private BaseManager configService;
    private StartPresenterImpl  mStartPresenter;


    @Mock StartActivityV2 activityV2;
    @Before
    public void setUp(){
//        MockitoAnnotations.initMocks(this);
////        configService=new ConfigService();
//        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
//            @Override
//            public Scheduler getMainThreadScheduler() {
//                return Schedulers.immediate();
//            }
//        });
//        BaseManager configService=new BaseManager(DataManager.getInstance().getMockApi(),DataManager.getInstance().getAppApi(),DataManager.getInstance().getPreferHelper());
//        mStartPresenter=new StartPresenterImpl(configService);
//        mStartPresenter.attachView(activityV2);
    }
    @Test
    public void  testGetConfigInfo(){

//        CompositeSubscription   mCompositeSubscription=new CompositeSubscription();
//       Subscription subscription= configService.getConfigInfo(numberId,new ConfigService.ConfigInfoCallback() {
//            @Override
//            public void onSuccess(RemoteConfigResonseModel remoteConfigResonseModel) {
//            }
//            @Override
//            public void onError(String errorMsg) {
//            }
//        });
//        mCompositeSubscription.add(subscription);

//        verify(activityV2).delayStart();
    }
    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

}

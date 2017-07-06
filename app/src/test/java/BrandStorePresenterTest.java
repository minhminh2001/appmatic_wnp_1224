import com.whitelabel.app.GlobalData;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.model.BrandStoreModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import rx.observers.TestSubscriber;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BrandStorePresenterTest {
    ProductApi productApi;
    @Before
    public void  setUp(){
        productApi=DataManager.getInstance().getProductApi();
        GlobalData.serviceRequestUrl="https://dev2.wnp.com.hk/";
    }
    @Test
    public void  testGetBrandProductList(){
//        TestSubscriber<BrandStoreModel> subscriber=TestSubscriber.create();
//        productApi.getProductListByBrandId("10001","0","10","","","","","").
//                subscribe(subscriber);
//        subscriber.assertNoErrors();
//        subscriber.assertCompleted();
    }

}

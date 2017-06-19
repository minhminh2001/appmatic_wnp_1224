import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.ui.productdetail.BindProductPresenterImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
@RunWith(PowerMockRunner.class)
public class BindProductTest {
    private BindProductPresenterImpl presenter;
    @Before
    public void setUp(){
        presenter=new BindProductPresenterImpl();
    }
    @Test
    public  void   bindProductPresenter_checkProductIsSelected(){
        List<SVRAppserviceProductDetailResultPropertyReturnEntity> beans=null;
        Assert.assertFalse(presenter.checkProductIsSelected(beans));
        beans=new ArrayList<>();
        Assert.assertFalse(presenter.checkProductIsSelected(beans));
        SVRAppserviceProductDetailResultPropertyReturnEntity bean0=new SVRAppserviceProductDetailResultPropertyReturnEntity();
        SVRAppserviceProductDetailResultPropertyReturnEntity bean=new SVRAppserviceProductDetailResultPropertyReturnEntity();
        bean.setSelected(true);
        beans.add(bean0);
        beans.add(bean);
        Assert.assertTrue(presenter.checkProductIsSelected(beans));
    }
    @Test
    public void bindProductPresenter_computeSumPrice(){
        List<SVRAppserviceProductDetailResultPropertyReturnEntity> beans=new ArrayList<>();
        SVRAppserviceProductDetailResultPropertyReturnEntity bean=new SVRAppserviceProductDetailResultPropertyReturnEntity();
        bean.setFinalPrice("13.3");
        SVRAppserviceProductDetailResultPropertyReturnEntity bean1=new SVRAppserviceProductDetailResultPropertyReturnEntity();
        bean1.setFinalPrice("22.3");
        beans.add(bean);
        beans.add(bean1);
        Assert.assertEquals(35.6, presenter.computeSumPrice(beans),0.001);
    }
}

import com.whitelabel.app.model.ProductPropertyModel;
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
        List<ProductPropertyModel> beans=null;
        Assert.assertFalse(presenter.checkProductIsSelected(beans));
        beans=new ArrayList<>();
        Assert.assertFalse(presenter.checkProductIsSelected(beans));
        ProductPropertyModel bean0=new ProductPropertyModel();
        ProductPropertyModel bean=new ProductPropertyModel();
        bean.setSelected(true);
        beans.add(bean0);
        beans.add(bean);
        Assert.assertTrue(presenter.checkProductIsSelected(beans));
    }
    @Test
    public void bindProductPresenter_computeSumPrice(){
        List<ProductPropertyModel> beans=new ArrayList<>();
        ProductPropertyModel bean=new ProductPropertyModel();
        bean.setFinalPrice("13.3");
        ProductPropertyModel bean1=new ProductPropertyModel();
        bean1.setFinalPrice("22.3");
        beans.add(bean);
        beans.add(bean1);
        Assert.assertEquals(35.6, presenter.computeSumPrice(beans),0.001);
    }
}

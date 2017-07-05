package com.whitelabel.app.data.service;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/5.
 */
public class CommodityManagerTest {
    private CommodityManager mCommodityManager;
    @Before
    public void setUp(){
        mCommodityManager=new CommodityManager(DataManager.getInstance().getProductApi(),DataManager.getInstance().getPreferHelper());
    }
    @Test
    public void getAllCategoryManager() throws Exception {
        TestSubscriber<SVRAppserviceCatalogSearchReturnEntity> testSubscriber=new TestSubscriber<>();
        mCommodityManager.getAllCategoryManager()
                .subscribe(testSubscriber);
        SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity=testSubscriber.getOnNextEvents().get(0);
        Assert.assertNotNull(svrAppserviceCatalogSearchReturnEntity);
        Assert.assertTrue(svrAppserviceCatalogSearchReturnEntity.getCategory().size()>0);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

}
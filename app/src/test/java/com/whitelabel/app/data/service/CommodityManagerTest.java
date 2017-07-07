package com.whitelabel.app.data.service;

import android.util.Log;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import  static  org.mockito.Mockito.verify;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/5.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
public class CommodityManagerTest {

    private CommodityManager mCommodityManager;

    @Mock
    PreferHelper preferHelper;


    @Before
    public void setUp(){
        PowerMockito.mockStatic(Log.class);
        MockitoAnnotations.initMocks(this);
        mCommodityManager=new CommodityManager(DataManager.getInstance().getProductApi(),preferHelper);

    }
    @Test
    public void getAllCategoryManager() throws Exception {

        TestSubscriber<SVRAppserviceCatalogSearchReturnEntity> testSubscriber=new TestSubscriber<>();
        mCommodityManager.getAllCategoryManager()
                .subscribe(testSubscriber);
        SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity=testSubscriber.getOnNextEvents().get(0);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        Assert.assertNotNull(svrAppserviceCatalogSearchReturnEntity);
        Assert.assertTrue(svrAppserviceCatalogSearchReturnEntity.getCategory().size()>0);
    }
    @Test
    public void getCategoryDetail()  throws Exception{
        mCommodityManager.getCategoryDetail(true,"1","1");
        verify(preferHelper).getCategoryDetail("1");

        TestSubscriber<CategoryDetailModel>  testSubscriber=TestSubscriber.create();
        mCommodityManager.getCategoryDetail(false,"4","").
                subscribe(testSubscriber);

       testSubscriber.assertNoErrors();
       testSubscriber.assertCompleted();
        CategoryDetailModel categoryDetailModel= testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(categoryDetailModel.getCategory_id(),"4");
    }
    @Test
    public void getLocalShoppingProductCount() throws Exception {
        List<TMPLocalCartRepositoryProductEntity> tmps=new ArrayList<>();
         tmps.add(new TMPLocalCartRepositoryProductEntity(3));
         tmps.add(new TMPLocalCartRepositoryProductEntity(5));
         tmps.add(new TMPLocalCartRepositoryProductEntity(10));
         Mockito.when(preferHelper.getShoppingCartProduct()).thenReturn(Observable.just(tmps));
         TestSubscriber testSubscriber=TestSubscriber.create();
        mCommodityManager.getLocalShoppingProductCount()
                 .subscribe(testSubscriber);
          Integer integer= (Integer) testSubscriber.getOnNextEvents().get(0);
          Assert.assertTrue(integer==18);

    }

}
package com.whitelabel.app.ui.home.presenter;

import android.util.Log;

import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.data.service.BaseManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.ui.home.HomeContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static  org.mockito.Mockito.verify;
import java.util.Observable;


/**
 * Created by Administrator on 2017/7/6.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
@PowerMockIgnore("javax.net.ssl.*")
public class HomePresenterImplTest {
    private HomePresenterImpl homePresenter;
    @Mock
    BaseManager baseManager;
    @Mock
    CommodityManager  commodityManager;
    @Mock
    HomeContract.View mView;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);
        RxUnitTestTools.openRxTools();
        homePresenter=new HomePresenterImpl(mView,commodityManager,baseManager);
    }
    @Test
    public void getBaseCategory() throws Exception {
        SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity=new SVRAppserviceCatalogSearchReturnEntity();
        Mockito.when(commodityManager.getAllCategoryManager()).thenReturn(rx.Observable.just(svrAppserviceCatalogSearchReturnEntity));
        homePresenter.getBaseCategory();
        verify(mView).dissmissProgressDialog();
        verify(mView).loadData(svrAppserviceCatalogSearchReturnEntity);
        verify(mView).hideOnlineErrorLayout();
    }
    @Test
    public void getShoppingCount() throws Exception {
        GOUserEntity entity=new GOUserEntity();
        entity.setCartItemCount(5);
        Mockito.when(commodityManager.getLocalShoppingProductCount()).thenReturn(rx.Observable.just(10));
        Mockito.when(baseManager.getUser()).thenReturn(entity);
        Mockito.when(baseManager.isSign()).thenReturn(true);
        homePresenter.getShoppingCount();
        verify(mView).setShoppingCartCount(15);
    }

    @Test
    public void setShoppingCartCount() throws Exception {
        GOUserEntity entity=new GOUserEntity();
        entity.setCartItemCount(5);
        Mockito.when(baseManager.isSign()).thenReturn(true);
        Mockito.when(baseManager.getUser()).thenReturn(entity);
        homePresenter.setShoppingCartCount(10);
        ArgumentCaptor<Integer>  argumentCaptor=ArgumentCaptor.forClass(Integer.class);
        verify(mView).setShoppingCartCount(argumentCaptor.capture());
        long   num=argumentCaptor.getValue();
        Assert.assertEquals(num,15);

    }
    @Test
    public void setShoppingCartCount2(){
        Mockito.when(baseManager.isSign()).thenReturn(false);
        homePresenter.setShoppingCartCount(10);
        ArgumentCaptor<Integer>  argumentCaptor1=ArgumentCaptor.forClass(Integer.class);
        verify(mView).setShoppingCartCount(argumentCaptor1.capture());
        long   notSignCount=argumentCaptor1.getValue();
        Assert.assertEquals(notSignCount,10);

    }
}
package com.whitelabel.app.data.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.AppApi;
import com.whitelabel.app.data.retrofit.MockApi;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.utils.JLogUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/7/5.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JLogUtils.class})
@PowerMockIgnore("javax.net.ssl.*")
public class BaseManagerTest {

    @Mock
    PreferHelper preferHelper;
    BaseManager configService;
    @Before
    public void setUp(){
        PowerMockito.mockStatic(JLogUtils.class);
        MockitoAnnotations.initMocks(this);
        configService=new BaseManager(DataManager.getInstance().getMockApi(),DataManager.getInstance().getAppApi(),preferHelper);
    }
    @Test
    public void getConfigInfo() throws Exception {
        when(preferHelper.getVersionNumber()).thenReturn("123");
        TestSubscriber<RemoteConfigResonseModel> subscriber=TestSubscriber.create();
        configService.getConfigInfo().subscribe(subscriber);
        RemoteConfigResonseModel user = subscriber.getOnNextEvents()
                .get(0);
        Assert.assertNotNull(user.getData().getUiStyle());
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
    }
    @Test
    public void getCurrencyUnit() throws Exception {
        TestSubscriber<GOCurrencyEntity> subscriber=TestSubscriber.create();
        configService.getCurrencyUnit("","").subscribe(subscriber);
        GOCurrencyEntity goCurrencyEntity=subscriber.getOnNextEvents().get(0);
        Assert.assertNotNull(goCurrencyEntity.getName());
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
    }
    @Test
    public void parseCurrentUnitJson() throws Exception {
        Gson gson=new Gson();
        JsonObject jsonObject= gson.fromJson("{'status':'1','data':{'unit':$HK}}",JsonObject.class);
        GOCurrencyEntity goCurrencyEntity= configService.parseCurrentUnitJson(jsonObject);
        Assert.assertNotNull(goCurrencyEntity);
        Assert.assertEquals(goCurrencyEntity.getName(),"$HK");
    }
}
package com.whitelabel.app.ui.start;

import android.util.Log;

import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.data.service.IBaseManager;
import com.whitelabel.app.model.GOCurrencyEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import  static org.mockito.Mockito.verify;

import java.util.Observable;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/5.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class StartPresenterImplTest {
    @Mock
    private IBaseManager iBaseManager;
    @Mock
    private StartContract.View mView;
    private StartPresenterImpl startPresenter;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);
        RxUnitTestTools.openRxTools();
        startPresenter=new StartPresenterImpl(iBaseManager);
        startPresenter.attachView(mView);
    }
    @Test
    public void getConfigInfo() throws Exception {
        GOCurrencyEntity goCurrencyEntity=new GOCurrencyEntity();
        goCurrencyEntity.setName("$HK");
        RemoteConfigResonseModel  remoteConfigResonseModel=new RemoteConfigResonseModel();
        remoteConfigResonseModel.setCode(1000);
        remoteConfigResonseModel.setData(new RemoteConfigResonseModel.RetomeConfig());
        Mockito.when(iBaseManager.getCurrencyUnit("","")).thenReturn(rx.Observable.just(goCurrencyEntity));
        Mockito.when(iBaseManager.getConfigInfo()).thenReturn(rx.Observable.just(remoteConfigResonseModel));
        startPresenter.getConfigInfo("","");
        verify(iBaseManager).getCurrencyUnit("","");
        verify(iBaseManager).getConfigInfo();
    }

    @Test
    public void timeOutJudgment() throws Exception {
        startPresenter.setStartTime();
        startPresenter.timeOutJudgment();
        ArgumentCaptor<Long> argumentCaptor=ArgumentCaptor.forClass(Long.class);
        verify(mView).postDelayed(argumentCaptor.capture());
        Assert.assertTrue((argumentCaptor.getValue()<2000));

        startPresenter.setStartTime();
        Thread.sleep(2300);
        startPresenter.timeOutJudgment();
        verify(mView).startNextActivity();
    }
}
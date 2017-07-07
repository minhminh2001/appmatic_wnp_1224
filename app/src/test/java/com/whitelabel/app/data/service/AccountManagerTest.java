package com.whitelabel.app.data.service;

import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ResponseModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import rx.observers.TestSubscriber;

/**
 * Created by kevin on 2017/7/7.
 */

public class AccountManagerTest {
    AccountManager accountManager;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        RxUnitTestTools.openRxTools();
        accountManager=new AccountManager(DataManager.getInstance().getMyAccountApi());
    }

    @Test
    public void testDeleteAddressById() throws Exception {
        TestSubscriber<ResponseModel> subscriber=TestSubscriber.create();
        accountManager.deleteAddressById("1","1").subscribe(subscriber);
        ResponseModel responseModel = subscriber.getOnNextEvents().get(0);
        Assert.assertNotNull(responseModel);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
    }

    @Test
    public void testGetAddressList() throws Exception {
        TestSubscriber<AddresslistReslut> subscriber=TestSubscriber.create();
        accountManager.getAddressList("1").subscribe(subscriber);
        AddresslistReslut addresslistReslut = subscriber.getOnNextEvents().get(0);
        Assert.assertNotNull(addresslistReslut);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
    }

}

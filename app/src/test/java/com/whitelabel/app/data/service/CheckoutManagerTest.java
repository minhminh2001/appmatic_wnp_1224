package com.whitelabel.app.data.service;

import com.google.gson.JsonObject;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.ui.checkout.model.PaypalPlaceOrderReponse;
import com.whitelabel.app.ui.checkout.model.RequestOrderNumberResponse;
import com.whitelabel.app.utils.JDataUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Response;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/18.
 */
public class CheckoutManagerTest {

    private CheckoutManager checkoutManager;
    String sessionKey="29e718b891b7470bcddde33ac2dc5073";
    @Mock
    ICacheApi iCacheApi;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        checkoutManager=new CheckoutManager(DataManager.getInstance().getCheckoutApi());
    }
    @Test
    public void paypalPlaceOrder() throws Exception {
        TestSubscriber<PaypalPlaceOrderReponse> testSubscriber=TestSubscriber.create();
        checkoutManager.paypalPlaceOrder(sessionKey).
                subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        PaypalPlaceOrderReponse responseModel= testSubscriber.getOnNextEvents().get(0);
        Assert.assertFalse(JDataUtils.isEmpty(responseModel.getOrderNumber()));
    }

    @Test
    public void requestOrderNumber() throws Exception {
        TestSubscriber<RequestOrderNumberResponse> testSubscriber=TestSubscriber.create();
        checkoutManager.requestOrderNumber(sessionKey).
                subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        RequestOrderNumberResponse responseModel= testSubscriber.getOnNextEvents().get(0);
//        Assert.assertTrue(JDataUtils.isEmail(responseModel.get));
        System.out.println("status:"+responseModel.getStatus());
    }

}
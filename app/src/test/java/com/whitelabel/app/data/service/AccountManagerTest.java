package com.whitelabel.app.data.service;

import com.whitelabel.app.ProductProvider;
import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.SessionKeyProvider;
import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.WishDelEntityResult;

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
    String sessionKey="";
    ProductDetailModel productDetailModel;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        RxUnitTestTools.openRxTools();
        accountManager=new AccountManager(DataManager.getInstance().getMyAccountApi(),DataManager.getInstance().getPreferHelper());
        sessionKey=new SessionKeyProvider().getSession();
        productDetailModel=new ProductProvider().getProduct(sessionKey);
    }
    @Test
    public void addWishlist() throws Exception {
        TestSubscriber<AddToWishlistEntity> testSubscriber=TestSubscriber.create();
         accountManager.addWishlist(sessionKey,productDetailModel.getId())
                 .subscribe(testSubscriber);
        AddToWishlistEntity responseModel=testSubscriber.getOnNextEvents().get(0);
        Assert.assertTrue(responseModel.getStatus()!=-1);
    }
    @Test
    public void deleteWishlist() throws Exception {
        productDetailModel=new ProductProvider().getProduct(sessionKey);
        TestSubscriber<WishDelEntityResult> testSubscriber=TestSubscriber.create();
        accountManager.deleteWishlist(sessionKey,productDetailModel.getItemId())
                .subscribe(testSubscriber);
        WishDelEntityResult responseModel=testSubscriber.getOnNextEvents().get(0);
        Assert.assertTrue(responseModel.getStatus()!=-1);
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

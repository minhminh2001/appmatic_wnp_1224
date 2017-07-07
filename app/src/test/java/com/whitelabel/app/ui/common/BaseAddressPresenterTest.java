package com.whitelabel.app.ui.common;

import com.whitelabel.app.RxUnitTestTools;
import com.whitelabel.app.data.service.IAccountManager;
import com.whitelabel.app.data.service.ICommodityManager;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.AddresslistReslut;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.utils.JLogUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/**
 * Created by kevin on 2017/7/6.
 */

public class BaseAddressPresenterTest {
    @Mock
    private ICommodityManager iCommodityManager;
    @Mock
    private IAccountManager iAccountManager;
    @Mock
    private BaseAddressContract.View mView;
    @Mock
    private JLogUtils jLogUtils;

    private BaseAddressPresenter baseAddressPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        RxUnitTestTools.openRxTools();

        baseAddressPresenter=new BaseAddressPresenter(true,
                iCommodityManager,
                iAccountManager,
                mView);
        baseAddressPresenter.setJLogUtils(jLogUtils);
    }

    @Test
    public void testDeleteAddressById() throws Exception {
        ResponseModel responseModel=new ResponseModel();
        responseModel.setStatus(1);
        Mockito.when(iAccountManager.deleteAddressById("qwe","1")).thenReturn(rx.Observable.just(responseModel));
        ///
        AddresslistReslut addresslistReslut=new AddresslistReslut();
        addresslistReslut.setStatus(1);
        addresslistReslut.setAddress(new ArrayList<AddressBook>() );
        Mockito.when(iAccountManager.getAddressList("qwe")).thenReturn(rx.Observable.just(addresslistReslut));
        ///
        baseAddressPresenter.deleteAddressById("qwe","1","1");
        ///

        Mockito.verify(mView,Mockito.atLeast(2)).closeProgressDialog();
        Mockito.verify(mView).openSwipeLayout();
        ///
        if(responseModel.getStatus()==1) {
            Mockito.verify(mView).closeSwipeLayout();
        }
        if(responseModel.getStatus()==1&&addresslistReslut.getStatus()==1) {
            Mockito.verify(mView).loadData(addresslistReslut.getAddress());
        }
    }

}

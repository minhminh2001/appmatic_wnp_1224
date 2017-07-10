package com.whitelabel.app;

import com.whitelabel.app.data.DataManager;
import com.whitelabel.app.data.service.CommodityManager;
import com.whitelabel.app.model.ProductDetailModel;

import rx.observers.TestSubscriber;

/**
 * Created by Administrator on 2017/7/7.
 */

public class ProductProvider {
    public ProductDetailModel  getProduct(String sessionKey){
       CommodityManager mCommodityManager=new CommodityManager(DataManager.getInstance().getProductApi(),DataManager.getInstance().getPreferHelper());
        TestSubscriber<ProductDetailModel> testSubscriber=new TestSubscriber<>();
        mCommodityManager.getProductDetail(sessionKey,"428").
                subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        ProductDetailModel productDetailModel=testSubscriber.getOnNextEvents().get(0);
        return productDetailModel;
    }
}

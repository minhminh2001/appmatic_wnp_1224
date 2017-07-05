package com.whitelabel.app.data.service;

import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceSaveBillingEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/5.
 */
public class CommodityManager  implements ICommodityManager{
    private ProductApi  productApi;
    private PreferHelper  cacheHelper;
    private SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity;
    public CommodityManager(ProductApi productApi, PreferHelper preferHelper){
        this.productApi=productApi;
        cacheHelper=preferHelper;
    }
    @Override
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getAllCategoryManager() {
        if(svrAppserviceCatalogSearchReturnEntity==null) {
            return productApi.getBaseCategory().doOnNext(new Action1<SVRAppserviceCatalogSearchReturnEntity>() {
                @Override
                public void call(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                    CommodityManager.this.svrAppserviceCatalogSearchReturnEntity = svrAppserviceCatalogSearchReturnEntity;
                }
            });
        }else{
            return Observable.just(svrAppserviceCatalogSearchReturnEntity);
        }
    }
    @Override
    public Observable<Integer> getLocalShoppingProductCount() {
        return cacheHelper.getShoppingCartProduct().flatMap(new Func1<List<TMPLocalCartRepositoryProductEntity>, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(List<TMPLocalCartRepositoryProductEntity> tmpLocalCartRepositoryProductEntities) {
                if(tmpLocalCartRepositoryProductEntities!=null){
                    return Observable.just(sumProductCount(tmpLocalCartRepositoryProductEntities));
                }
                return Observable.just(0);
            }
        });
    }
    public int sumProductCount(List<TMPLocalCartRepositoryProductEntity>  tmps){
        int count=0;
        for(TMPLocalCartRepositoryProductEntity  tmp: tmps){
                count+=tmp.getSelectedQty();
         }
         return count;
    }


}

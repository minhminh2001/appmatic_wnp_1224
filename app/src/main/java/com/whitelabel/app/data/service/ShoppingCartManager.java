package com.whitelabel.app.data.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.retrofit.BaseApi;
import com.whitelabel.app.data.retrofit.ShoppingCartApi;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/12.
 */
public class ShoppingCartManager implements IShoppingCartManager {
    private ShoppingCartApi shoppingCartApi;
    private ICacheApi  iCacheApi;
    @Inject
    public ShoppingCartManager(ShoppingCartApi shoppingCartApi, ICacheApi iCacheApi) {
        this.shoppingCartApi = shoppingCartApi;
        this.iCacheApi = iCacheApi;
    }
    @Override
    public Observable<ShoppingCartListEntityCart> getShoppingCartInfo(String sessionKey) {
        return shoppingCartApi.getShoppingCartInfo(sessionKey).
                map(new Func1<JsonObject, ShoppingCartListEntityCart>() {
            @Override
            public ShoppingCartListEntityCart call(JsonObject jsonObject) {
                int status=jsonObject.get("status").getAsInt();
                if(status==1) {
                    JsonObject jsonObject1 = jsonObject.getAsJsonObject("cart");
                    ShoppingCartListEntityCart shoppingCartListEntityCart =
                            new Gson().fromJson(jsonObject1.toString(),
                                    ShoppingCartListEntityCart.class);
                      //when
                    if(shoppingCartListEntityCart!=null) {
                        for (ShoppingCartListEntityCell cell : shoppingCartListEntityCart.getItems()) {
                            if (cell.getQty() != null) {
                                cell.setCurrStockQty(Integer.parseInt(cell.getQty()) + cell.getStockQty() + "");
                            }
                        }
                    }
                    return  shoppingCartListEntityCart;
                }else{
                    String errorMessage=jsonObject.get("errorMessage").getAsString();
                    Observable.error(new ApiException(errorMessage));
                    return null;
                }
            }
        }).doOnNext(new Action1<ShoppingCartListEntityCart>() {
                    @Override
                    public void call(ShoppingCartListEntityCart shoppingCartListEntityCart) {
                       GOUserEntity goUserEntity= iCacheApi.getUser();
                       goUserEntity.setCartItemCount(shoppingCartListEntityCart.getSummaryQty());
                       iCacheApi.saveUser(goUserEntity);
                    }
                });
    }
    @Override
    public Observable<ResponseModel> addProductToShoppingCart(String sessionKey, String productId, Map<String,String> idQtys){
        Map<String ,String> params=new HashMap<>();
        int  index=0;
        for(String id :idQtys.keySet()){
            params.put("simpleId["+index+"]",id);
            params.put("qty["+index+"]",idQtys.get(id));
            index++;
        }
        return  shoppingCartApi.addProductToShoppingCart(sessionKey,productId,params)
                .doOnNext(new Action1<ResponseModel>() {
                    @Override
                    public void call(ResponseModel responseModel) {
                        if (responseModel.getStatus()==-1){
                            Observable.error(new ApiException(responseModel.getErrorMessage()));
                        }
                    }
                });
    }


    @Override
    public Observable<ShoppingCartVoucherApplyEntity> applyOrCancelVercherCode(String sessionKey, String vercherCode, String state) {
        return shoppingCartApi.applyOrCancelVoucherCode(sessionKey,vercherCode,state);
    }
    @Override
    public Observable<ResponseModel> checkoutOfStock(String sessionKey) {
        return shoppingCartApi.checkoutOutOfStock(sessionKey);
    }
    @Override
    public Observable<ShoppingCartDeleteCellEntity> deleteProductFromShoppingCart(String sessionKey, String id) {
        return shoppingCartApi.deleteProductFromShoppingCart(sessionKey,id);
    }
    @Override
    public Observable<ShoppingCartDeleteCellEntity> setProductCountFromShoppingCart(String sessionKey, String productId, int count) {
        Map<String,String> params=new HashMap<>();
        params.put("cart["+productId+"][qty]",count+"");
        return shoppingCartApi.setProductCountFromShoppingCart(sessionKey,params);
    }
}

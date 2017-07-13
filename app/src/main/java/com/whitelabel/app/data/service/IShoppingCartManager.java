package com.whitelabel.app.data.service;

import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface IShoppingCartManager {
    public Observable<ShoppingCartListEntityCart> getShoppingCartInfo(String sessionKey);
    public Observable<ShoppingCartVoucherApplyEntity> applyOrCancelVercherCode(String sessionKey,String vercherCode,String state);
    public Observable<ResponseModel> checkoutOfStock(String sessionKey);
    public  Observable<ShoppingCartDeleteCellEntity>  deleteProductFromShoppingCart(String sessionKey,  String id);
    public Observable<ShoppingCartDeleteCellEntity> setProductCountFromShoppingCart(String sessionKey,String productId,int count);
    public Observable<ResponseModel> addProductToShoppingCart(String sessionKey, String productId, Map<String,String> idQtys);
}

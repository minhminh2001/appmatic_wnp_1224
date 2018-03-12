package com.whitelabel.app.data.service;

import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface IShoppingCartManager {

    Observable<ShoppingCartListEntityCart> getShoppingCartInfo(String sessionKey);

    Observable<ShoppingCartVoucherApplyEntity> applyOrCancelVercherCode(String sessionKey,
        String vercherCode, String state);

    Observable<ResponseModel> checkoutOfStock(String sessionKey);

    Observable<ShoppingCartDeleteCellEntity> deleteProductFromShoppingCart(String sessionKey,
        String id);

    Observable<ShoppingCartDeleteCellEntity> setProductCountFromShoppingCart(
        String sessionKey, String productId, int count);

    Observable<ResponseModel> addProductToShoppingCart(String sessionKey, String productId,
        Map<String, String> idQtys);

    Observable<Boolean> saveProductToLocal(List<ShoppingItemLocalModel> shoppingItemLocalModels);

    Observable<List<ShoppingItemLocalModel>> getProductListFromLocal();

    Observable<ShoppingCartListEntityCart> getGuestList(
        List<ShoppingItemLocalModel> shoppingItemLocalModels);
}

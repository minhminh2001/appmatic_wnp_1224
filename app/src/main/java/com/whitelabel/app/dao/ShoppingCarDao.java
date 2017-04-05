package com.whitelabel.app.dao;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.whitelabel.app.application.GemfiveApplication;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.KeyValueBean;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultPropertyReturnEntity;
import com.whitelabel.app.model.ShoppingCartDeleteCellEntity;
import com.whitelabel.app.model.ShoppingCartErrorMsgBean;
import com.whitelabel.app.model.ShoppingCartListEntityCart;
import com.whitelabel.app.model.ShoppingCartListEntityCell;
import com.whitelabel.app.model.ShoppingCartVoucherApplyEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductOptionEntity;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ray on 2015/10/29.
 */
public class ShoppingCarDao extends BaseHttp {
    public static final int REQUEST_SHOPPINGINFO = 3;
    public static final int REQUEST_REFERSHERRORSHOPPINGINFO = 25;
    public static final int REQUEST_SAVEBANK = 4;
    public static final int REQUEST_CHECKSTOCK = 5;
    public static final int REQUEST_ERROR = 10;
    public static final int REQUEST_LOCALSHOPPINGINFO = 6;
    public static final int REQUEST_VOUCHERCODE = 7;
    public static final int REQUEST_MULTIPLECODE = 8;
    public static final int REQUEST_ADDPRODUCT = 9;
    public static final int REQUEST_ADDCAMPAGINTOCART = 11;
    public static final int REQUEST_RECOVERORDER = 222;
    public static final int REQUEST_CHANGESHOPPINGCARCOUNT = 14;
    public static final int REQUEST_DELETEFROMSHOPPINGCART = 23;
    public static final int REQUEST_STORECREDIT = 24;
    public ShoppingCarDao(String TAG, Handler handler) {
        super(TAG, handler);
    }
    @Override
    public void requestHttp(HTTP_METHOD method, String url, TreeMap map, int requestCode) {
        super.requestHttp(method, url, map, requestCode);
    }
    public void getShoppingCarInfo(String sessionKey) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/list", params, REQUEST_SHOPPINGINFO);
    }
    public void refershErrorShoppingCarInfo(String sessionKey) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/list", params, REQUEST_REFERSHERRORSHOPPINGINFO);
    }
    //1  remove  0 add
    public void redeemStoreCredit(String sessionKey, String remove) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("remove", remove);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/storeCredit", params, REQUEST_STORECREDIT);
    }

    public void deleteProductFromShoppingCart(String session, String itemId, String position) {
        params = new TreeMap<>();
        params.put("session_key", session);
        params.put("id", itemId + "");
        requestHttp(HTTP_METHOD.POST, "appservice/cart/delete", params, REQUEST_DELETEFROMSHOPPINGCART, position);
    }

    public void saveShoppingCartCount(Context context, int num) {
        GOUserEntity userEntity = GemfiveApplication.getAppConfiguration().getUserInfo(context);
        GemfiveApplication.getAppConfiguration().updateDate(context, userEntity);
    }

    public void addProductToShoppingCart(String sessionKey, String productId, String qty, List<SVRAppserviceProductDetailResultPropertyReturnEntity> propertyReturnEntities) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("product_id", productId);
        params.put("qty", qty);
        if (propertyReturnEntities != null) {
            for (int i = 0; i < propertyReturnEntities.size(); i++) {
                params.put("super_attribute[" + propertyReturnEntities.get(i).getSuperAttribute() + "]", propertyReturnEntities.get(i).getId());
            }
        }
//        if(!JDataUtils.isEmpty(colorAttribute)&&!JDataUtils.isEmpty(colorId)){
//            params.put("super_attribute["+colorAttribute+"]",colorId);
//        }
//        if(!JDataUtils.isEmpty(sizeAttribute)&&!JDataUtils.isEmpty(sizeId)){
//            params.put("super_attribute["+sizeAttribute+"]",sizeId);
//        }
        requestHttp(HTTP_METHOD.POST, "appservice/cart/add", params, REQUEST_ADDPRODUCT);
    }

    public void addBatch(String sessionkey, ShoppingCartListEntityCell[] shoppingCart) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        for (int i = 0; i < shoppingCart.length; i++) {
            ShoppingCartListEntityCell cell = shoppingCart[shoppingCart.length - 1 - i];
            if (!TextUtils.isEmpty(cell.getProductId())) {
                params.put("products[" + i + "][product_id]", cell.getProductId());
            }
            if (!TextUtils.isEmpty(cell.getQty())) {
                params.put("products[" + i + "][qty]", cell.getQty());
            }

            if (cell.getLocalOptions() != null) {
                for (int z = 0; z < cell.getLocalOptions().size(); z++) {
                    HashMap<String, String> attribute = cell.getLocalOptions().get(z);
                    if (attribute != null && !TextUtils.isEmpty(attribute.get("attributeId"))) {
                        params.put("products[" + i + "][super_attribute][" + attribute.get("attributeKey") +
                                "]", attribute.get("attributeId"));
                    }
                }
            }
//            HashMap<String,String> hash= cell.getOptions().get(0);
//            HashMap<String,String> hash1=cell.getOptions().get(1);
//            if(!TextUtils.isEmpty(hash.get("ColorId"))) {
//                params.put("products[" + i + "][super_attribute][" + hash.get("ColorKey") +
//                        "]", hash.get("ColorId"));
//            }
//            if(!TextUtils.isEmpty(hash1.get("SizeId"))) {
//                params.put("products[" + i + "][super_attribute][" + hash1.get("SizeKey") +
//                        "]", hash1.get("SizeId"));
//            }
        }
        requestHttp(HTTP_METHOD.POST, "appservice/cart/addBatch", params,
                REQUEST_MULTIPLECODE);
    }

//    public void addProductToShoppingCart(String sessionKey, String productId, String qty, String colorAttribute, String colorId, String sizeAttribute, String sizeId) {
//        params = new TreeMap<>();
//        params.put("session_key", sessionKey);
//        params.put("product_id", productId);
//        params.put("qty", qty);
//        if (!JDataUtils.isEmpty(colorAttribute) && !JDataUtils.isEmpty(colorId)) {
//            params.put("super_attribute[" + colorAttribute + "]", colorId);
//        }
//        if (!JDataUtils.isEmpty(sizeAttribute) && !JDataUtils.isEmpty(sizeId)) {
//            params.put("super_attribute[" + sizeAttribute + "]", sizeId);
//        }
//        requestHttp(HTTP_METHOD.POST, "/appservice/cart/add", params, REQUEST_ADDPRODUCT);
//    }



    public void sendRecoverOrder(String sessionKey, String orderId, String storeId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("order_id", orderId);
        params.put("store_id", storeId);
        requestHttp(HTTP_METHOD.POST, "appservice/order/reorder", params, REQUEST_RECOVERORDER);
    }

    public void addCampaignProductToCart(String sessionKey, String productId) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("product_id", productId);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/addPromoProduct", params, REQUEST_ADDCAMPAGINTOCART);
    }

    public void requestChangeCount(String sessionKey, String cellId, String qty, KeyValueBean keyValueBean) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("cart[" + cellId + "][qty]", qty);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/updateShoppingCart", params, REQUEST_CHANGESHOPPINGCARCOUNT, keyValueBean);
    }


    public void applyOrCancelVoucherCode(String sessionKey, String voucherCode, String flag) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        params.put("coupon_code", voucherCode);
        params.put("remove", flag);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/couponPost", params, REQUEST_VOUCHERCODE);
    }

    public void getShoppingCartLocalInfo(final Context context) {
        new Thread() {
            @Override
            public void run() {
                LinkedList shoppingcart = new LinkedList<>();
                ArrayList<TMPLocalCartRepositoryProductEntity> products = JStorageUtils.getProductListFromLocalCartRepository(context);//加载本地数据

                ShoppingCartListEntityCart car = new ShoppingCartListEntityCart();

                ShoppingCartListEntityCell[] items = new ShoppingCartListEntityCell[products.size()];
                double subTotal = 0;
                int num = 0;
                for (int i = 0; i < products.size(); i++) {
                    TMPLocalCartRepositoryProductEntity entity = products.get(i);
                    ShoppingCartListEntityCell product = new ShoppingCartListEntityCell();
                    product.setQty(entity.getSelectedQty() + ""); //数量
                    product.setBrand(entity.getBrand());
                    product.setCategory(entity.getCategory());
                    product.setFinalPrice(entity.getFinalPrice());
                    product.setId(entity.getProductId());
                    product.setBrandId(entity.getBrandId());
                    product.setInStock(entity.getInStock() + "");
                    product.setProductId(entity.getProductId());
                    product.setImage(entity.getImage());
                    product.setName(entity.getName());
                    product.setMaxQty(entity.getQty() + "");
                    product.setMaxSaleQty(entity.getMaxSaleQty());
                    product.setPrice(entity.getPrice());
                    product.setVendorDisplayName(entity.getVendorDisplayName());
                    product.setVendor_id(entity.getVendor_id());
                    product.setCanViewPdp(entity.getCanViewPdp());
                    product.setVisibility(entity.getVisibility());
                    if (TextUtils.isEmpty(entity.getAvailability())) {
                        //版本兼容，没有Availability属性的默认设为  1
                        product.setAvailability("1");
                    } else {
                        product.setAvailability(entity.getAvailability());
                    }
                    ArrayList<TMPLocalCartRepositoryProductOptionEntity> options = entity.getOptions();

                    ArrayList<HashMap<String, String>> localList = new ArrayList<HashMap<String, String>>();
                    ArrayList<HashMap<String, String>> displayList = new ArrayList<HashMap<String, String>>();

                    for (int z = 0; z < options.size(); z++) {
                        TMPLocalCartRepositoryProductOptionEntity entity1 = options.get(z);
                        HashMap<String, String> attribute = new HashMap<String, String>();
                        HashMap<String, String> displayAttribute = new HashMap<String, String>();
                        attribute.put("attributeKey", entity1.getSuperAttribute());
                        attribute.put("attributeId", entity1.getId());
                        attribute.put("attributeLabel", entity1.getLabel());
                        localList.add(attribute);
                        displayAttribute.put(entity1.getSuperAttribute(), entity1.getLabel());
                        displayList.add(displayAttribute);
                    }
                    product.setOptions(displayList);
                    product.setLocalOptions(localList);
                    items[i] = product;
                    num += entity.getSelectedQty();
                    subTotal += Double.parseDouble(entity.getFinalPrice()) * entity.getSelectedQty();
                    shoppingcart.add(product);
                }
                car.setItems(items);
                car.setGrandTotal(subTotal + "");
                car.setSubTotal(subTotal + "");
                car.setSummaryQty(num);
                postHandler(REQUEST_LOCALSHOPPINGINFO, car);
            }
        }.start();
    }


    public void checkOutofStock(String sessionkey) {
        params = new TreeMap<>();
        params.put("session_key", sessionkey);
        requestHttp(HTTP_METHOD.POST, "appservice/cart/checkCartStock", params, REQUEST_CHECKSTOCK);
    }


    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", requestCode, response);
        switch (requestCode) {
            case REQUEST_REFERSHERRORSHOPPINGINFO:
            case REQUEST_SHOPPINGINFO:
                if (isOk(response)) {
                    ShoppingCartListEntityCart carBean = null;
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject carlist = obj.getJSONObject("cart");
                        carBean = JJsonUtils.parseJsonObj(carlist.toString(), ShoppingCartListEntityCart.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postHandler(requestCode, carBean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHECKSTOCK:
                if (isOk(response)) {
                    postHandler(REQUEST_CHECKSTOCK, null, RESPONSE_SUCCESS);
                } else {
                    ShoppingCartErrorMsgBean shoppingCartErrorMsgBean = getShoppingCartErrorMsgBean(response);
                    postHandler(REQUEST_CHECKSTOCK, shoppingCartErrorMsgBean, RESPONSE_FAILED);
                }
                break;
            case REQUEST_VOUCHERCODE:

                if (isOk(response)) {
                    ShoppingCartVoucherApplyEntity bean = JJsonUtils.parseJsonObj(response, ShoppingCartVoucherApplyEntity.class);
                    postHandler(requestCode, bean, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean, RESPONSE_FAILED);
                }

                break;
            case REQUEST_MULTIPLECODE:
                if (isOk(response)) {
                    postHandler(requestCode, null, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean, RESPONSE_FAILED);
                }
                break;
            case REQUEST_ADDPRODUCT:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_ADDCAMPAGINTOCART:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_RECOVERORDER:
                if (isOk(response)) {
                    postHandler(requestCode, "", RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHANGESHOPPINGCARCOUNT:
                if (isOk(response)) {
                    ShoppingCartDeleteCellEntity cellEntity = JJsonUtils.parseJsonObj(response, ShoppingCartDeleteCellEntity.class);
                    if (cellEntity != null) {
                        cellEntity.setParams((KeyValueBean) object);
                    }
                    postHandler(requestCode, cellEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    errorMsgBean.setParams(object);
                    postHandler(requestCode, errorMsgBean, RESPONSE_FAILED);
                }
                break;
            case REQUEST_DELETEFROMSHOPPINGCART:
                if (isOk(response)) {
                    ShoppingCartDeleteCellEntity shoppingCartDeleteCell = JJsonUtils.parseJsonObj(response, ShoppingCartDeleteCellEntity.class);
                    shoppingCartDeleteCell.setParam(object);
                    postHandler(requestCode, shoppingCartDeleteCell, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    errorMsgBean.setParams(object);
                    postHandler(requestCode, errorMsgBean, RESPONSE_FAILED);
                }

                break;
            case REQUEST_STORECREDIT:
                if (isOk(response)) {
                    ShoppingCartDeleteCellEntity shoppingCartDeleteCell = JJsonUtils.parseJsonObj(response, ShoppingCartDeleteCellEntity.class);
                    postHandler(requestCode, shoppingCartDeleteCell, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean errorMsgBean = getErrorMsgBean(response);
                    postHandler(requestCode, errorMsgBean.getErrorMessage(), RESPONSE_FAILED);
                }

                break;
        }
    }

    public ShoppingCartErrorMsgBean getShoppingCartErrorMsgBean(String response) {
        ShoppingCartErrorMsgBean bean = JJsonUtils.parseJsonObj(response, ShoppingCartErrorMsgBean.class);
        if (bean == null) {
            bean = new ShoppingCartErrorMsgBean();
            bean.setErrorMessage("error");
        }
        return bean;
    }

    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        if (requestCode == REQUEST_CHANGESHOPPINGCARCOUNT) {
            postErrorHandler(REQUEST_ERROR, requestCode, object,errorType);
        } else {
            postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(),errorType);
        }
    }
}

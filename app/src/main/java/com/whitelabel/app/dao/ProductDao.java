package com.whitelabel.app.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.whitelabel.app.model.AddToWishlistEntity;
import com.whitelabel.app.model.ErrorMsgBean;
import com.whitelabel.app.model.GetServiceEntity;
import com.whitelabel.app.model.MarketingLayersEntity;
import com.whitelabel.app.model.SVRAppServiceCustomerVersionCheck;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListLandingPageItemReturnEntity;
import com.whitelabel.app.model.SVRAppserviceLandingPagesListReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductMerchantReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.SearchSuggestionBean;
import com.whitelabel.app.model.ServerTime;
import com.whitelabel.app.model.ShoppingCartCampaignListEntityReturn;
import com.whitelabel.app.network.BaseHttp;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;
import com.whitelabel.app.utils.JStorageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductDao extends BaseHttp {
    public static final int REQUEST_MERCHANTDETAILS = 1001;
    public static final int REQUEST_SUGGESTIONS = 1002;
    public static final int REQUEST_GETSERVICE = 1;
    public static final int REQUEST_ERROR = 10;
    public static final int REQUEST_CATALOGSEARCH = 2;
    public static final int REQUEST_CHECKVERSION = 3;
    public static final int REQUEST_CURATION = 4;
    public static final int REQUEST_CURATIONDETAIL = 5;
    public static final int REQUEST_PRODUCTSEARCH = 6;
    public static final int REQUEST_PRODUCTRECOMMEND = 11;
    public static final int REQUEST_MARKETING = 7;
    public static final int REQUEST_PRODUCTDETAIL = 8;
    public static final int REQUEST_ADDPRODUCTTOWISH = 15;
    public static final int REQUEST_ADDPRODUCTLISTTOWISH = 14;
    public static final int REQUEST_CAMPAGINPRODUCT = 16;
    public static final int LOCAL_SAVEMAINCATEOGRY = 17;
    public static final int LOCAL_GETMAINCATEGORY = 18;
    private ExecutorService fixedThreadPool;
    public static final int REQUEST_SERVERTIMER = 27;
    public ProductDao(String TAG, Handler handler) {
        super(TAG, handler);
        fixedThreadPool = Executors.newFixedThreadPool(1);
    }

    public void getServiceList(String userName, String Password) {
        params = new TreeMap<>();
        params.put("username", userName);
        params.put("password", Password);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/appsconnection/list", params, REQUEST_GETSERVICE);
    }


    class MainCategoryLocalData extends Thread {
        public static final int TYPE_SAVE = 1;
        public static final int TYPE_GET = 2;
        public WeakReference<Handler> mHandler;
        public WeakReference<Context> context;
        public int currType;
        private String categoryId;
        private List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans;
        private int requestCode;

        public MainCategoryLocalData(int type, int requestCode, Handler handler, Context context, String categoryId, List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans) {
            currType = type;
            mHandler = new WeakReference<Handler>(handler);
            this.context = new WeakReference<Context>(context);
            this.categoryId = categoryId;
            this.beans = beans;
            this.requestCode = requestCode;
        }

        @Override
        public void run() {
            if (mHandler.get() == null || context.get() == null) {
                return;
            }
            if (currType == TYPE_SAVE) {
                JStorageUtils.saveMainCategoryData(context.get(), categoryId, beans);
            } else if (currType == TYPE_GET) {
                List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans = JStorageUtils.getMainCategoryData(context.get(), categoryId);
                Message msg = new Message();
                msg.obj = beans;
                msg.what = requestCode;
                msg.arg1 = RESPONSE_SUCCESS;
                mHandler.get().sendMessage(msg);
            }
            super.run();
        }
    }


    public void saveMainCategoryLocal(Context context, String categoryId, List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> beans) {
        new MainCategoryLocalData(MainCategoryLocalData.TYPE_SAVE, LOCAL_SAVEMAINCATEOGRY, mHandler, context, categoryId, beans).start();
    }


    public void getMainCategoryLocal(Context context, String categoryId) {
        fixedThreadPool.execute(new MainCategoryLocalData(MainCategoryLocalData.TYPE_GET, LOCAL_GETMAINCATEGORY, mHandler, context, categoryId, null));
//        new MainCategoryLocalData(MainCategoryLocalData.TYPE_GET,LOCAL_GETMAINCATEGORY,mHandler,context,categoryId,null).start();
    }


    public void getServerTime() {
        params = new TreeMap<>();

        requestHttp(HTTP_METHOD.GET, "appservice/timezone/now", params, REQUEST_SERVERTIMER);


    }

    public void getProductDetail(String productId, String sessionKey) {
        params = new TreeMap<>();
        params.put("product_id", productId);
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/product/detail", params, REQUEST_PRODUCTDETAIL);

    }

    public void getBaseCategory() {
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/catalogSearch", null, REQUEST_CATALOGSEARCH);
    }

    public void getMerchantDetail(String order, String dir, String brand, String modelType, String price, String pageID, String offset, String limit, String sessionkey) {
        params = new TreeMap<>();
        params.put("vendor_id", pageID);
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("session_key", sessionkey);
        if (!TextUtils.isEmpty(price)) {
            params.put("price", price);
        }
        if (!TextUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!TextUtils.isEmpty(dir)) {
            params.put("dir", dir);
        }

        if (!TextUtils.isEmpty(brand)) {
            params.put("brand", brand);
        }
        if (!TextUtils.isEmpty(modelType)) {
            params.put("model_type", modelType);
        }
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/merchant/index", params, REQUEST_MERCHANTDETAILS);
    }
    public void getCurationDetail(String order, String dir, String brand, String modelType, String price, String pageID,String offset,String limit,String sessionkey){
        params = new TreeMap<>();
        params.put("page_id", pageID);
        params.put("offset", offset);
        params.put("limit", limit);
        if (!TextUtils.isEmpty(sessionkey)) {
            params.put("session_key", sessionkey);
        }
        if (!TextUtils.isEmpty(price)) {
            params.put("price", price);
        }
        if (!TextUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!TextUtils.isEmpty(dir)) {
            params.put("dir", dir);
        }
        if (!TextUtils.isEmpty(brand)) {
            params.put("brand", brand);
        }
        if (!TextUtils.isEmpty(modelType)) {
            params.put("model_type", modelType);
        }
        requestHttp(BaseHttp.HTTP_METHOD.GET,"appservice/landingPages/detail",params,REQUEST_CURATIONDETAIL);
    }

    public void getCurationList(String categoryId) {
        params = new TreeMap<>();
        params.put("category_id", categoryId);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/landingPages/list", params, REQUEST_CURATION);
    }

    public void getMarketingLayers(String categoryId, int identification) {
        params = new TreeMap<>();
        params.put("category_id", categoryId);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/appmarketing/index", params, REQUEST_MARKETING, identification);

    }
    public void addProductToWish(String productId, String sessionkey) {
        params = new TreeMap<>();
        params.put("product_id", productId);
        params.put("session_key", sessionkey);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/wishlist/addToWishList", params, REQUEST_ADDPRODUCTTOWISH);
    }

    public void addProductListToWish(String productId, String sessionkey, Object object) {
        params = new TreeMap<>();
        params.put("product_id", productId);
        params.put("session_key", sessionkey);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/wishlist/addToWishList", params, REQUEST_ADDPRODUCTLISTTOWISH, object);
    }

    public void checkVersion(String sessionKey) {
        params = new TreeMap<>();
        params.put("session_key", sessionKey);
        requestHttp(BaseHttp.HTTP_METHOD.POST, "appservice/version/check", params, REQUEST_CHECKVERSION);
    }

    @Override
    public void requestHttp(BaseHttp.HTTP_METHOD method, String url, TreeMap map, int requestCode) {
        super.requestHttp(method, url, map, requestCode);
    }

    public void  productSearch(String storeId, String p, String limit, String order, String dir,
                              String brand, String categoryId, String modelType, String q,
                              String keywords, String price, String sessionKey, String fromPage,
                              String categoryOption, List<String> brandOptions,
                              List<String> flavorOptions, List<String> liftStageOptions) {
        params = new TreeMap<>();
        params.put("store_id", storeId);
        params.put("p", p);
        params.put("limit", limit);
        if (!TextUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!TextUtils.isEmpty(dir)) {
            params.put("dir", dir);
        }

        if (!TextUtils.isEmpty(brand)) {
            params.put("brand", brand);
        }
        if (!TextUtils.isEmpty(categoryId)) {
            params.put("category_id", categoryId);
        }
        if (!TextUtils.isEmpty(modelType)) {
            params.put("model_type", modelType);
        }
        if (!TextUtils.isEmpty(q)) {
            params.put("q", q);
        }

        if (!TextUtils.isEmpty(keywords)) {
            params.put("keywords", keywords);
        }
        if (!TextUtils.isEmpty(price)) {
            params.put("price", price);
        }
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        if(!TextUtils.isEmpty(fromPage)){
            params.put("pageType",fromPage);
        }
        if(brandOptions != null) { // use for filter search
            if(brandOptions.size() > 1) {// multi-brand use for filter
                for (int index = 0; index < brandOptions.size(); index++) {
                    params.put("vesbrand[" + index + "]", brandOptions.get(index));
                }
            } else if(brandOptions.get(0) != null) {

                params.put("vesbrand", brandOptions.get(0));
            }
        }
        if(flavorOptions != null) { // use for filter search
            if(flavorOptions.size() > 1) {// multi-flavor use for filter
                for (int index = 0; index < flavorOptions.size(); index++) {
                    params.put("flavor[" + index + "]", flavorOptions.get(index));
                }
            } else {
                params.put("flavor", flavorOptions.get(0));
            }
        }
        if (liftStageOptions != null) { // use for filter search
            if(liftStageOptions.size() > 1) {// multi-lift stage use for filter
                for (int index = 0; index < liftStageOptions.size(); index++) {
                    params.put("stage[" + index + "]", liftStageOptions.get(index));
                }
            } else {
                params.put("stage", liftStageOptions.get(0));
            }
        }
        if (!TextUtils.isEmpty(categoryOption)) { // use for filter search
            params.put("cat", categoryOption);
        }

        JLogUtils.d("response","params="+params.toString());
        requestHttp(HTTP_METHOD.POST, "appservice/product/search", params, REQUEST_PRODUCTSEARCH);
    }

    public void brandSearch(String storeId, String p,String brandId, String limit, String order, String dir,
                              String keywords, String sessionKey) {
        params = new TreeMap<>();
        params.put("store_id", storeId);
        params.put("p", p);
        params.put("limit", limit);
        params.put("brand_id", brandId);
        if (!TextUtils.isEmpty(order)) {
            params.put("order", order);
        }
        if (!TextUtils.isEmpty(dir)) {
            params.put("dir", dir);
        }
        /*if (!TextUtils.isEmpty(keywords)) {
            params.put("keywords", keywords);
        }*/
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        JLogUtils.d("response","params="+params.toString());
        requestHttp(HTTP_METHOD.POST, "appservice/category/brandDetail", params, REQUEST_PRODUCTSEARCH);
    }


    public void getProductRecommendList(String storeId, String limit, String productId, String sessionKey) {
        params = new TreeMap<>();
        params.put("store_id", storeId);
        params.put("limit", limit);
        params.put("product_id", productId);
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        requestHttp(HTTP_METHOD.GET, "appservice/product/recommendedlist", params, REQUEST_PRODUCTRECOMMEND);
    }
    public void getSuggestions(String q, String sessionKey) {
        params = new TreeMap<>();
        params.put("q", q);
        if (!TextUtils.isEmpty(sessionKey)) {
            params.put("session_key", sessionKey);
        }
        requestHttp(HTTP_METHOD.GET, "appservice/catalogSearch/getSugges", params, REQUEST_SUGGESTIONS);
    }

    public void getCampaignProduct(String session) {
        params = new TreeMap<>();
        params.put("session_key", session);
        requestHttp(BaseHttp.HTTP_METHOD.GET, "appservice/cart/promoProductList", params, REQUEST_CAMPAGINPRODUCT);
    }


    @Override
    public void onSuccess(int requestCode, String response, Object object) {
        JLogUtils.json("response", requestCode, response);
        switch (requestCode) {
            case REQUEST_SERVERTIMER:
                if (isOk(response)) {
                    ServerTime entity = JJsonUtils.parseJsonObj(response, ServerTime.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_SUGGESTIONS:
                if (isOk(response)) {
                    SearchSuggestionBean entity = JJsonUtils.parseJsonObj(response, SearchSuggestionBean.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_GETSERVICE:
                if (isOk(response)) {
//                        JSONObject jsonObject= new JSONObject(response);
//                        JSONArray arrayList=jsonObject.getJSONArray("result");
//                        ArrayList<GetServiceListEntity> serviceListEntities= (ArrayList<GetServiceListEntity>) JJsonUtils.parseJsonList(arrayList.toString(), GetServiceListEntity.class);
                    GetServiceEntity entity = JJsonUtils.parseJsonObj(response, GetServiceEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);

                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CATALOGSEARCH:
                if (isOk(response)) {
                    SVRAppserviceCatalogSearchReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceCatalogSearchReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CHECKVERSION:
                if (isOk(response)) {
                    SVRAppServiceCustomerVersionCheck entity = JJsonUtils.parseJsonObj(response, SVRAppServiceCustomerVersionCheck.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    if (bean != null && bean.getStatus() == -1) {
                        postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                    }
                }
                break;
            case REQUEST_CURATION:
                if (isOk(response)) {
                    SVRAppserviceLandingPagesListReturnEntity entity = JJsonUtils.parseJsonObj(response, SVRAppserviceLandingPagesListReturnEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CURATIONDETAIL:

                if (isOk(response)) {
                    SVRAppserviceLandingPagesDetailReturnEntity curationReturnEntity = JJsonUtils.parseJsonObj(response, SVRAppserviceLandingPagesDetailReturnEntity.class);
                    postHandler(requestCode, curationReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }

                break;
            case REQUEST_MERCHANTDETAILS:
                if (isOk(response)) {
                    JLogUtils.d("response", "response=========================" + response);
                    SVRAppserviceProductMerchantReturnEntity merchantReturnEntity = JJsonUtils.parseJsonObj(response, SVRAppserviceProductMerchantReturnEntity.class);
                    postHandler(requestCode, merchantReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }

                break;
            case REQUEST_PRODUCTRECOMMEND:
                if (isOk(response)) {
                    SVRAppserviceProductRecommendedReturnEntity productRecommendedReturnEntity = JJsonUtils.parseJsonObj(response, SVRAppserviceProductRecommendedReturnEntity.class);
                    postHandler(requestCode, productRecommendedReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_PRODUCTSEARCH:
                if (isOk(response)) {
                    SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity = JJsonUtils.parseJsonObj(response, SVRAppserviceProductSearchReturnEntity.class);
                    JLogUtils.i("ray","sss"+svrAppserviceProductSearchReturnEntity);

                    postHandler(requestCode, svrAppserviceProductSearchReturnEntity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_MARKETING:
                if (isOk(response)) {
                    MarketingLayersEntity entity = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String marketStr = jsonObject.getJSONObject("market").toString();
                        entity = JJsonUtils.parseJsonObj(marketStr, MarketingLayersEntity.class);
                        entity.setIndex((Integer) object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    postHandler(requestCode, "", RESPONSE_FAILED);
                }
                break;
            case REQUEST_PRODUCTDETAIL:
                if (isOk(response)) {
                    SVRAppserviceProductDetailReturnEntity productentity = JJsonUtils.parseJsonObj(response, SVRAppserviceProductDetailReturnEntity.class);
                    postHandler(requestCode, productentity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;

            case REQUEST_ADDPRODUCTLISTTOWISH:
                if (isOk(response)) {
                    AddToWishlistEntity entity = JJsonUtils.parseJsonObj(response, AddToWishlistEntity.class);
                    entity.setParams(object);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    bean.setParams(object);
                    postHandler(requestCode, bean, RESPONSE_FAILED);
                }

                break;
            case REQUEST_ADDPRODUCTTOWISH:
                if (isOk(response)) {
                    AddToWishlistEntity entity = JJsonUtils.parseJsonObj(response, AddToWishlistEntity.class);
                    postHandler(requestCode, entity, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
            case REQUEST_CAMPAGINPRODUCT:
                if (isOk(response)) {
                    ShoppingCartCampaignListEntityReturn cartCampaignListEntityReturn = JJsonUtils.parseJsonObj(response, ShoppingCartCampaignListEntityReturn.class);
                    postHandler(requestCode, cartCampaignListEntityReturn, RESPONSE_SUCCESS);
                } else {
                    ErrorMsgBean bean = getErrorMsgBean(response);
                    postHandler(requestCode, bean.getErrorMessage(), RESPONSE_FAILED);
                }
                break;
        }
    }

    @Override
    public void onFalied(int requestCode, VolleyError volleyError, Object object, int errorType) {
        postErrorHandler(REQUEST_ERROR, requestCode, volleyError.getMessage(), errorType);
    }

}

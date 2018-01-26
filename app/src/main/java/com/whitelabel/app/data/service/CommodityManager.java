package com.whitelabel.app.data.service;

import android.text.TextUtils;

import com.google.gson.internal.LinkedTreeMap;
import com.whitelabel.app.data.preference.ICacheApi;
import com.whitelabel.app.data.preference.PreferHelper;
import com.whitelabel.app.data.retrofit.ProductApi;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.ApiException;
import com.whitelabel.app.model.AutoHintSearchRequest;
import com.whitelabel.app.model.BindProductResponseModel;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.CategoryDetailModel;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ProductDetailModel;
import com.whitelabel.app.model.ResponseModel;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailResultDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductDetailReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductRecommendedReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchReturnEntity;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JLogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/7/5.
 */
public class CommodityManager  implements ICommodityManager{
    private ProductApi  productApi;
    private ICacheApi cacheHelper;

    @Inject
    public CommodityManager(ProductApi productApi, ICacheApi preferHelper){
        this.productApi=productApi;
        cacheHelper=preferHelper;
    }
    @Override
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getAllCategoryManager() {
            return productApi.getBaseCategory()
                    .doOnNext(new Action1<SVRAppserviceCatalogSearchReturnEntity>() {
                @Override
                public void call(SVRAppserviceCatalogSearchReturnEntity svrAppserviceCatalogSearchReturnEntity) {
                    JLogUtils.i("ray","Thread:"+Thread.currentThread().getName());
                      cacheHelper.saveBaseCategory(svrAppserviceCatalogSearchReturnEntity);
                }
            });
    }

    @Override
    public Observable<CategoryBaseBean> getAllCategoryManagerV2() {
        return productApi.getBaseCategoryV2()
                .doOnNext(new Action1<CategoryBaseBean>() {
                    @Override
                    public void call(CategoryBaseBean categoryBaseBean) {
                        cacheHelper.saveBaseCategoryV2(categoryBaseBean);
                    }
                });
    }

    @Override
    public Observable<Integer> getLocalShoppingProductCount() {
        return cacheHelper.getShoppingCartProduct()
        .flatMap(new Func1<List<TMPLocalCartRepositoryProductEntity>, Observable<Integer>>() {
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
    @Override
    public Observable<List<AddressBook>> getAddressListCache(String userId) {
        return cacheHelper.getAddressListCache(userId);
    }
    @Override
    public Observable<CategoryDetailNewModel> getCategoryDetail(boolean isCache, final String categoryId, String sessionKey) {
        if(isCache){
            return cacheHelper.getCategoryDetail(categoryId);
        }else {
            return productApi.getCategoryDetail(categoryId, sessionKey)
                    .map(new Func1<ResponseModel<CategoryDetailNewModel>, CategoryDetailNewModel>() {
                        @Override
                        public CategoryDetailNewModel call(ResponseModel<CategoryDetailNewModel> categoryDetailModelResponseModel) {
                            return categoryDetailModelResponseModel.getData();
                        }
                    }).doOnNext(new Action1<CategoryDetailNewModel>() {
                        @Override
                        public void call(CategoryDetailNewModel categoryDetailModel) {
                            cacheHelper.saveCategoryDetail(categoryId,categoryDetailModel);
                  }
             });
        }
    }

    @Override
    public Observable<ShopBrandResponse> getShopBrandDetail(boolean isCache,String menuId, String sessionKey) {
        if (isCache){
            return cacheHelper.getShopBrandDetail();
        }else {
            return productApi.getShopBrandDetail(menuId,sessionKey)
                    .map(new Func1<ResponseModel<ShopBrandResponse>, ShopBrandResponse>() {
                        @Override
                        public ShopBrandResponse call(ResponseModel<ShopBrandResponse> shopBrandResponseResponseModel) {
                            return shopBrandResponseResponseModel.getData();
                        }
                    }).doOnNext(new Action1<ShopBrandResponse>() {
                        @Override
                        public void call(ShopBrandResponse shopBrandResponse) {
                            cacheHelper.saveShopBrandDetail(shopBrandResponse);
                        }
                    });
        }

    }

    @Override
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getLocalCategoryManager() {
        return cacheHelper.getBaseCategory();
    }

    @Override
    public Observable<ProductDetailModel> getProductDetail(String sessionKey, String productId) {
         return productApi.getProductDetail(sessionKey,productId)
                 .flatMap(new Func1<SVRAppserviceProductDetailReturnEntity, Observable<ProductDetailModel>>() {
             @Override
             public Observable<ProductDetailModel> call(SVRAppserviceProductDetailReturnEntity svrAppserviceProductDetailReturnEntity) {

                 if(svrAppserviceProductDetailReturnEntity.getStatus()==-1){
                    return  Observable.error(new ApiException(svrAppserviceProductDetailReturnEntity.getErrorMessage()));
                 }else{
                     ProductDetailModel productDetailModel=svrAppserviceProductDetailReturnEntity.getResult();
                     productDetailModel.setUiDetailHtmlText(getProductDetailHtml(productDetailModel));
                     return Observable.just(productDetailModel);
                 }
             }
         });
    }


    @Override
    public Observable<BindProductResponseModel> getRelateProducts(String productId) {
        return productApi.getRelateProducts(productId).flatMap(new Func1<ResponseModel<BindProductResponseModel>, Observable<BindProductResponseModel>>() {
            @Override
            public Observable<BindProductResponseModel> call(ResponseModel<BindProductResponseModel> bindProductResponseModelResponseModel) {
                if (bindProductResponseModelResponseModel.getStatus()==-1){
                    return  Observable.error(new ApiException(bindProductResponseModelResponseModel.getErrorMessage()));
                }else {
                    return Observable.just(bindProductResponseModelResponseModel.getData());
                }
            }
        });
    }

    @Override
    public Observable<ResponseModel> addBoughtTogether(String productId,String sessionKey){
        return productApi.addBoughtTogether(productId,sessionKey).map(new Func1<ResponseModel, ResponseModel>() {
            @Override
            public ResponseModel call(ResponseModel responseModel) {
              return responseModel;
            }
        });
    }

    @Override
    public Observable<SVRAppserviceProductSearchReturnEntity> autoHintSearch(
        Map<String,String> params) {
        return productApi.autoHintSearch(params).map(
            new Func1<SVRAppserviceProductSearchReturnEntity, SVRAppserviceProductSearchReturnEntity>() {
                @Override
                public SVRAppserviceProductSearchReturnEntity call(
                    SVRAppserviceProductSearchReturnEntity svrAppserviceProductSearchReturnEntity) {
                    return svrAppserviceProductSearchReturnEntity;
                }
            });
    }

    public String  getProductDetailHtml(ProductDetailModel productDetailModel) {
        StringBuilder stringBuilder1 = new StringBuilder("");
//        if (productDetailModel.getShippingInfo() != null) {
//            stringBuilder1.append("<h3 class=\"text1\" ><B>SHIPPING INFO</B></h3>");
//            if (!JDataUtils.isEmpty(productDetailModel.getShippingInfo().getWestDeliversDays())) {
//                stringBuilder1.append(productDetailModel.getShippingInfo().getWestDeliversDays()).append("<br>");
//            }
//            if (!JDataUtils.isEmpty(productDetailModel.getShippingInfo().getEastDeliversDays())) {
//                stringBuilder1.append(productDetailModel.getShippingInfo().getEastDeliversDays()).append("<br>");
//            }
//            if (!JDataUtils.isEmpty(productDetailModel.getShippingInfo().getLocationNotDelivered())) {
//                stringBuilder1.append(productDetailModel.getShippingInfo().getLocationNotDelivered()).append("<br>");
//            }
//            String detailDelivery1 = productDetailModel.getShippingInfo().getDetailDelivery1();
//            if (!JDataUtils.isEmpty(detailDelivery1)) {
//                stringBuilder1.append(detailDelivery1).append("<br>");
//            }
//            String detailDelivery2 = productDetailModel.getShippingInfo().getDetailDelivery2();
//            if (!JDataUtils.isEmpty(detailDelivery2)) {
//                detailDelivery2 = detailDelivery2.replace("<li>", "");
//                detailDelivery2 = detailDelivery2.replace("</li>", "");
//                stringBuilder1.append(detailDelivery2).append("<br>");
//            }
//        }
        ArrayList<SVRAppserviceProductDetailResultDetailReturnEntity> arrayList = productDetailModel.getDetail();
        if (arrayList != null && arrayList.size() > 0) {
            stringBuilder1.append("<h3 class=\"text1\" ><B>Description</B></h3>");
            for (SVRAppserviceProductDetailResultDetailReturnEntity productdetailitem : arrayList) {
                if ("productDimension".equals(productdetailitem.getCode())) {
                    stringBuilder1.append(getProductDimenSionV2Html(productdetailitem.getValueArray()));
                    continue;
                }
                String label=productdetailitem.getLabel();
                if(!JDataUtils.isEmpty(label)){
                    label="<B class=\"text1\" >"+label+"</B><br> ";
                }
                stringBuilder1.append(label).append(productdetailitem.getValue());
            }
        }
        if(!JDataUtils.isEmpty(productDetailModel.getIngredients())){
            stringBuilder1.append("<h3 class=\"text1\" ><B>Ingredients</B></h3>");
            stringBuilder1.append(productDetailModel.getIngredients());
        }

        if(!JDataUtils.isEmpty(productDetailModel.getFeatures())){
            stringBuilder1.append("<h3 class=\"text1\" ><B>Features</B></h3>");
            stringBuilder1.append(productDetailModel.getFeatures());
        }
        stringBuilder1.append("<br><br>");
        String htmlText=stringBuilder1.toString();
        htmlText = htmlText.replaceAll("\u009D", "");
        htmlText = htmlText.replace("<br />\r\n<br />\r\n", "<br>\r\n");
        htmlText = htmlText.replace("<br />\n<br />\n", "<br>\r\n");
        htmlText = htmlText.replace("\r\n", "");
        htmlText = htmlText.replace("\u2028", " ");
        return htmlText;
    }
    public String getProductDimenSionV2Html(ArrayList<?> arrayList) {
        try {
            StringBuilder stringBuild = new StringBuilder("");
            if (arrayList != null && arrayList.size() > 0) {
                stringBuild.append(" <table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">   ");
                for (int i = 1; i <= arrayList.size(); i++) {
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) arrayList.get(i - 1);
                    String dimenTitle = (String) linkedTreeMap.get("title");
                    String dimenValue = (String) linkedTreeMap.get("value");
                    linkedTreeMap.get("title");
                    if (i % 2 == 1) {
                        stringBuild.append("   <tr>");
                    }
                    stringBuild.append("  <td> <strong>$title$</strong>: $value$&nbsp;&nbsp;&nbsp;</td>");
                    if (i % 2 == 0) {
                        stringBuild.append(" </tr>");
                    } else if (i == arrayList.size()) {
                        stringBuild.append(" </tr>");
                    }
                    String a = stringBuild.toString().replace("$title$", dimenTitle);
                    String b = a.replace("$value$", dimenValue);
                    stringBuild = new StringBuilder(b);
                }
                stringBuild.append(" </table> <br>  ");
            }
            return stringBuild.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public Observable<SVRAppserviceProductRecommendedReturnEntity> getProductRecommendList(String storeId, String limit, String productId, String sessionKey) {
        return productApi.getRecommendedList(storeId,limit,productId,sessionKey);
    }
}

package com.whitelabel.app.data.preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.whitelabel.app.WhiteLabelApplication;
import com.whitelabel.app.data.preference.model.ShoppingItemLocalModel;
import com.whitelabel.app.model.AddressBook;
import com.whitelabel.app.model.CategoryBaseBean;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.GOUserEntity;
import com.whitelabel.app.model.RemoteConfigResonseModel;
import com.whitelabel.app.model.SVRAppServiceCustomerCountry;
import com.whitelabel.app.model.SVRAppserviceCatalogSearchReturnEntity;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.model.SkipToAppStoreMarket;
import com.whitelabel.app.model.TMPLocalCartRepositoryProductEntity;
import com.whitelabel.app.utils.JDataUtils;
import com.whitelabel.app.utils.JJsonUtils;
import com.whitelabel.app.utils.JLogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PreferHelper implements ICacheApi {

    public static final String IS_FINISH_ORDER_TO_SHOW_APPSTORE_DIALOG =
        "IS_FINISH_ORDER_TO_SHOW_APPSTORE_DIALOG";

    public static final String SHOP_BRAND_DETAIL = "SHOP_BRAND_DETAIL";

    public static final String IS_GUIDE = "IS_GUIDE";

    public static final String IS_FIRST_CHECKOUT_ADDADDRESS = "IS_FIRST_CHECKOUT_ADDADDRESS";

    public static final String GET_COUNTRY_AND_REGIONS = "countries";

    public static final String ALL_CATEGORYS_V2 = "allCategorysv2";

    private static final String FILE_NAME = "whtelabel";

    private static final String TABLE_CONFIG = "config";

    private static final String TABLE_CURRENCY = "currency";

    private static final String TABLE_LOCAL_SHOPPING_CAR = "shopping_car";

    private static final String FILED_SHOPPING_CAR_LIST = "shopping_car_list";

    public String getVersionNumber() {
        RemoteConfigResonseModel.RetomeConfig config = getLocalConfigModel();
        String currentVersion = "";
        if (config != null) {
            currentVersion = config.getVersion();
        }
        return currentVersion;
    }

    public void saveConfigInfo(RemoteConfigResonseModel remoteConfigModel) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        RemoteConfigResonseModel.RetomeConfig config = remoteConfigModel.getData();
        String configStr = new Gson().toJson(config);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CONFIG, configStr);
        editor.commit();
    }

    public void saveCurrency(String currency) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TABLE_CURRENCY, currency);
        editor.apply();
    }

    public String getCurrency() {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TABLE_CURRENCY, "HK$");
    }

    public RemoteConfigResonseModel.RetomeConfig getLocalConfigModel() {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(TABLE_CONFIG, "");
        Gson gson = new Gson();
        RemoteConfigResonseModel.RetomeConfig retomeConfig = null;
        if (!TextUtils.isEmpty(str)) {
            retomeConfig = gson.fromJson(str, RemoteConfigResonseModel.RetomeConfig.class);
        }
        return retomeConfig;
    }

    public void saveAddressList(String userId, List<AddressBook> beans) {
        try {
            SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
                .getSharedPreferences("myAccount", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String beanStr = gson.toJson(beans);
            editor.putString("address_" + userId, beanStr);
            editor.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public rx.Observable<List<TMPLocalCartRepositoryProductEntity>> getShoppingCartProduct() {
        return rx.Observable
            .fromCallable(new Callable<List<TMPLocalCartRepositoryProductEntity>>() {
                @Override
                public List<TMPLocalCartRepositoryProductEntity> call() throws Exception {
                    ArrayList<TMPLocalCartRepositoryProductEntity> result = new
                        ArrayList<TMPLocalCartRepositoryProductEntity>();
                    SharedPreferences cartProductSP = WhiteLabelApplication.getInstance()
                        .getSharedPreferences("localStorageShoppingCartProductList",
                            Context.MODE_PRIVATE);
                    if (cartProductSP == null) {
                        return result;
                    }
                    String productListString = cartProductSP.getString("productList", null);
                    JLogUtils.d("response", "getLocalCart>>" + productListString);
                    if (!JDataUtils.isEmpty(productListString)) {
                        ArrayList<TMPLocalCartRepositoryProductEntity> oldProductEntityArrayList
                            = null;
                        try {
                            Gson gson = new Gson();
                            TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>> typeToken =
                                new TypeToken<ArrayList<TMPLocalCartRepositoryProductEntity>>() {
                                };
                            if (typeToken != null && gson != null) {
                                Type type = typeToken.getType();
                                oldProductEntityArrayList = gson.fromJson(productListString, type);
                            }
                        } catch (Exception ex) {
                            JLogUtils
                                .e("PreferHelper", "getProductListFromLocalCartRepository", ex);
                        }
                        if (oldProductEntityArrayList != null && oldProductEntityArrayList
                            .size() > 0) {
                            result.addAll(oldProductEntityArrayList);
                        }
                    }
                    return result;
                }
            });
    }

    public rx.Observable<List<AddressBook>> getAddressListCache(final String userId) {
        return rx.Observable.fromCallable(new Callable<List<AddressBook>>() {
            @Override
            public List<AddressBook> call() throws Exception {
                if (TextUtils.isEmpty(userId)) {
                    return null;
                }
                List<AddressBook> beans = null;
                try {
                    SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
                        .getSharedPreferences("myAccount", Context.MODE_PRIVATE);
                    String beanStr = sharedPreferences.getString("address_" + userId, "");
                    if (!TextUtils.isEmpty(beanStr)) {
                        JLogUtils.i("JStorageUtils", "localStr;" + beanStr);
                        beans = JJsonUtils.parseAddressList(beanStr);
                    }
                } catch (Exception ex) {
                    ex.getStackTrace();
                }
                return beans;
            }
        });
    }

    public GOUserEntity getUser() {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("user_info", "");
        if (!userInfo.equals("")) {
            GOUserEntity user = JJsonUtils.getUserEntityFromJson(userInfo);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void saveUser(GOUserEntity goUserEntity) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", new Gson().toJson(goUserEntity));
        editor.commit();
    }

    @Override
    public void saveCategoryDetail(String menuId, CategoryDetailNewModel categoryDetailModel) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String categoryStr = gson.toJson(categoryDetailModel);
        sharedPreferences.edit().putString("category" + menuId, categoryStr).commit();
    }

    public rx.Observable<CategoryDetailNewModel> getCategoryDetail(final String categoryId) {
        return rx.Observable.fromCallable(new Callable<CategoryDetailNewModel>() {
            @Override
            public CategoryDetailNewModel call() throws Exception {
                SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
                    .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
                String categoryStr = sharedPreferences.getString("category" + categoryId, "");
                Gson gson = new Gson();
                CategoryDetailNewModel categoryDetailModel = null;
                if (categoryStr != null && !"".equals(categoryStr)) {
                    categoryDetailModel = gson.fromJson(categoryStr, CategoryDetailNewModel.class);
                }
                return categoryDetailModel;
            }
        });
    }

    @Override
    public void saveBaseCategory(SVRAppserviceCatalogSearchReturnEntity allCategorys) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String allCategoryStr = gson.toJson(allCategorys);
        sharedPreferences.edit().
            putString("allCategorys", allCategoryStr).commit();
    }

    @Override
    public void saveBaseCategoryV2(CategoryBaseBean categoryBaseBean) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String allCategoryStr = gson.toJson(categoryBaseBean);
        sharedPreferences.edit().
            putString(ALL_CATEGORYS_V2, allCategoryStr).commit();
    }

    @Override
    public Observable<SVRAppserviceCatalogSearchReturnEntity> getBaseCategory() {
        return Observable.fromCallable(new Callable<SVRAppserviceCatalogSearchReturnEntity>() {
            @Override
            public SVRAppserviceCatalogSearchReturnEntity call() throws Exception {
                SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
                    .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
                String allCategoryStr = sharedPreferences.getString("allCategorys", "");
                SVRAppserviceCatalogSearchReturnEntity entity = null;
                if (!JDataUtils.isEmpty(allCategoryStr)) {
                    entity = new Gson()
                        .fromJson(allCategoryStr, SVRAppserviceCatalogSearchReturnEntity.class);
                }
                return entity;
            }
        });
    }

    @Override
    public Observable<CategoryBaseBean> getBaseCategoryV2() {
        return Observable.fromCallable(new Callable<CategoryBaseBean>() {
            @Override
            public CategoryBaseBean call() throws Exception {
                SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
                    .getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
                String allCategoryStr = sharedPreferences.getString(ALL_CATEGORYS_V2, "");
                CategoryBaseBean entity = null;
                if (!JDataUtils.isEmpty(allCategoryStr)) {
                    entity = new Gson().fromJson(allCategoryStr, CategoryBaseBean.class);
                }
                return entity;
            }
        });
    }

    @Override
    public void saveFinishOrderAndMarkTime(long currentTime) {
        Gson gson = new Gson();
        SkipToAppStoreMarket market = new SkipToAppStoreMarket();
        market.setAfterFirstOrder(true);
        market.setTime(currentTime);
        String marketString = gson.toJson(market);
        AppPrefs.putString(IS_FINISH_ORDER_TO_SHOW_APPSTORE_DIALOG, marketString);
    }

    @Override
    public SkipToAppStoreMarket getFirstOrderAndMarkTime() {
        String beanStr = AppPrefs.getString(IS_FINISH_ORDER_TO_SHOW_APPSTORE_DIALOG, "");
        SkipToAppStoreMarket market = new SkipToAppStoreMarket();
        if (TextUtils.isEmpty(beanStr)) {
            market.setTime(0);
            market.setAfterFirstOrder(false);
        } else {
            market = new Gson().fromJson(beanStr, SkipToAppStoreMarket.class);
        }
        return market;
    }

    @Override
    public Observable<ShopBrandResponse> getShopBrandDetail() {
        return rx.Observable.fromCallable(new Callable<ShopBrandResponse>() {
            @Override
            public ShopBrandResponse call() throws Exception {
                String shopStr = AppPrefs.getString(SHOP_BRAND_DETAIL, "");
                Gson gson = new Gson();
                ShopBrandResponse shopBrandResponse = null;
                if (!TextUtils.isEmpty(shopStr)) {
                    shopBrandResponse = gson.fromJson(shopStr, ShopBrandResponse.class);
                }
                return shopBrandResponse;
            }
        });
    }

    @Override
    public void saveShopBrandDetail(ShopBrandResponse shopBrandResponse) {
        Gson gson = new Gson();
        ShopBrandResponse response = new ShopBrandResponse();
        String marketString = gson.toJson(response);
        AppPrefs.putString(SHOP_BRAND_DETAIL, marketString);
    }

    @Override
    public void saveGuideFlag(Boolean isFirst) {
        AppPrefs.putBoolean(IS_GUIDE, isFirst);
    }

    @Override
    public boolean isGuide() {
        return AppPrefs.getBoolean(IS_GUIDE, false);
    }

    @Override
    public void saveCountryAndRegions(SVRAppServiceCustomerCountry countryEntityResult) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(GET_COUNTRY_AND_REGIONS, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String countryStr = gson.toJson(countryEntityResult);
        sharedPreferences.edit().putString(GET_COUNTRY_AND_REGIONS, countryStr).commit();
    }

    @Override
    public SVRAppServiceCustomerCountry getCountryAndRegions() {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(GET_COUNTRY_AND_REGIONS, Activity.MODE_PRIVATE);
        String countryStr = sharedPreferences.getString(GET_COUNTRY_AND_REGIONS, "");
        Gson gson = new Gson();
        SVRAppServiceCustomerCountry svrAppServiceCustomerCountry = null;
        if (!TextUtils.isEmpty(countryStr)) {
            svrAppServiceCustomerCountry = gson
                .fromJson(countryStr, SVRAppServiceCustomerCountry.class);
        }
        return svrAppServiceCustomerCountry;
    }

    @Override
    public Observable<Boolean> addProductDetailToLocal(
        List<ShoppingItemLocalModel> shoppingItemLocalModels) {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(TABLE_LOCAL_SHOPPING_CAR, Activity.MODE_PRIVATE);
        List<ShoppingItemLocalModel> productDetailModels = getShoppingList();
        if (productDetailModels == null) {
            productDetailModels = new ArrayList<>();
        }
        List<String> indexs = new ArrayList<>();
        for (int i = 0; i < productDetailModels.size(); i++) {
            for (int j = 0; j < shoppingItemLocalModels.size(); j++) {
                if (productDetailModels.get(i).getGroupId()
                    .equals(shoppingItemLocalModels.get(j).getGroupId()) && productDetailModels
                    .get(i).getSimpleId().equals(shoppingItemLocalModels.get(j).getSimpleId())) {
                    productDetailModels.get(i)
                        .setNumber(sum(shoppingItemLocalModels.get(j).getNumber(),
                            productDetailModels.get(i).getNumber()) + "");
                    indexs.add(j + "");
                }
            }
        }
        for (int i = 0; i < shoppingItemLocalModels.size(); i++) {
            if (!indexs.contains(i + "")) {
                productDetailModels.add(shoppingItemLocalModels.get(i));
            }
        }
        Gson gson = new Gson();
        sharedPreferences.edit()
            .putString(FILED_SHOPPING_CAR_LIST,
                gson.toJson(productDetailModels, new TypeToken<List<ShoppingItemLocalModel>>() {
                }.getType())).commit();
        return Observable.just(true);
    }

    public int sum(String numberA, String numberB) {
        return Integer.parseInt(numberA) + Integer.parseInt(numberB);
    }

    public List<ShoppingItemLocalModel> getShoppingList() {
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(TABLE_LOCAL_SHOPPING_CAR, Activity.MODE_PRIVATE);
        String content = sharedPreferences.getString(FILED_SHOPPING_CAR_LIST, "");
        List<ShoppingItemLocalModel> productDetailModels = null;
        if (!TextUtils.isEmpty(content)) {
            productDetailModels = JJsonUtils.fromJsonList(content, ShoppingItemLocalModel.class);
        }
        return productDetailModels;
    }

    @Override
    public Observable<Boolean> updateNumberByiD(String simpleId, String number) {
        List<ShoppingItemLocalModel> shoppingItemLocalModels = getShoppingList();
        for (ShoppingItemLocalModel shoppingItemLocalModel : shoppingItemLocalModels) {
            if (simpleId.equals(shoppingItemLocalModel.getSimpleId())) {
                shoppingItemLocalModel.setNumber(number);
            }
        }
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(TABLE_LOCAL_SHOPPING_CAR, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit()
            .putString(FILED_SHOPPING_CAR_LIST,
                gson.toJson(shoppingItemLocalModels, new TypeToken<List<ShoppingItemLocalModel>>() {
                }.getType())).commit();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> deleteShoppingItem(String simpleId) {
        List<ShoppingItemLocalModel> shoppingItemLocalModels = getShoppingList();
        for (ShoppingItemLocalModel shoppingItemLocalModel : shoppingItemLocalModels) {
            if (simpleId.equals(shoppingItemLocalModel.getSimpleId())) {
                shoppingItemLocalModels.remove(shoppingItemLocalModel);
                break;
            }
        }
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(TABLE_LOCAL_SHOPPING_CAR, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit()
            .putString(FILED_SHOPPING_CAR_LIST,
                gson.toJson(shoppingItemLocalModels, new TypeToken<List<ShoppingItemLocalModel>>() {
                }.getType())).commit();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> updateLocalShoppingItemNumber(String simpleId, String s) {
        List<ShoppingItemLocalModel> shoppingItemLocalModels = getShoppingList();
        for (ShoppingItemLocalModel shoppingItemLocalModel : shoppingItemLocalModels) {
            if (simpleId.equals(shoppingItemLocalModel.getSimpleId())) {
                shoppingItemLocalModel.setNumber(s);
            }
        }
        SharedPreferences sharedPreferences = WhiteLabelApplication.getInstance()
            .getSharedPreferences(TABLE_LOCAL_SHOPPING_CAR, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit()
            .putString(FILED_SHOPPING_CAR_LIST,
                gson.toJson(shoppingItemLocalModels, new TypeToken<List<ShoppingItemLocalModel>>() {
                }.getType())).commit();
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> deleteItemById(String simpleId) {
        return null;
    }

    @Override
    public Observable<List<ShoppingItemLocalModel>> getShoppingListFromLocal() {
        return Observable.just(getShoppingList());
    }
}
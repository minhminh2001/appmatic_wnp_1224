package com.whitelabel.app.utils;

import android.content.Context;
import android.content.Intent;

import com.whitelabel.app.activity.ProductListActivity;
import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;
import com.whitelabel.app.model.CategoryDetailNewModel;
import com.whitelabel.app.model.ShopBrandResponse;
import com.whitelabel.app.ui.home.activity.ShopBrandActivity;
import com.whitelabel.app.ui.home.fragment.HomeHomeFragmentV4;

import java.io.Serializable;

/**
 * Created by img on 2017/11/22.
 */

public class PageIntentUtils {
    private static final String SEARCH_KEY_BRAND="brand";
    private static final String SEARCH_KEY_CATEGORY="category";
    private static final String SEARCH_KEY_SEARCH="search";
    private static final String SEARCH_KEY_BG="bg";
   public static void skipToSerachPage(Context context,Serializable serializable){
       //home top header banner
        if (serializable instanceof CategoryDetailNewModel.BannersBean){
            String type = ((CategoryDetailNewModel.BannersBean) serializable).getType();
            switch (type){
                case SEARCH_KEY_BRAND:
                    String BrandKey = ((CategoryDetailNewModel.BannersBean) serializable).getKey();
                    Intent intentBrand = new Intent();
                    intentBrand.setClass(context, ProductListActivity.class);
                    intentBrand.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                    intentBrand.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                    intentBrand.putExtra(ProductListKeywordsSearchFragment.FROM_OTHER_PAGE_KEYWORD, BrandKey);
                    context.startActivity(intentBrand);
                    break;
                case SEARCH_KEY_CATEGORY:
                    String categoryKey = ((CategoryDetailNewModel.BannersBean) serializable).getKey();
                    Intent intentCategory = new Intent(context, ProductListActivity.class);
                    intentCategory.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                    intentCategory.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                    intentCategory.putExtra(ProductListActivity.INTENT_CATEGORY_ID, categoryKey);
                    context.startActivity(intentCategory);
                    break;
                case SEARCH_KEY_SEARCH:
                    String searchKey = ((CategoryDetailNewModel.BannersBean) serializable).getKey();
                    Intent intentSearch = new Intent();
                    intentSearch.setClass(context, ProductListActivity.class);
                    intentSearch.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
                    intentSearch.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
                    intentSearch.putExtra(ProductListKeywordsSearchFragment.FROM_OTHER_PAGE_KEYWORD, searchKey);
                    context.startActivity(intentSearch);
                    break;
                case SEARCH_KEY_BG:
                    break;
            }
            //shop brand page
        }else if (serializable instanceof ShopBrandResponse.BrandsBean.ItemsBean){
            Intent intent = new Intent();
            intent.setClass(context, ProductListActivity.class);
            intent.putExtra(ProductListActivity.INTENT_DATA_PREVTYPE, ProductListActivity.INTENT_DATA_PREVTYPE_VALUE_HOME);
            intent.putExtra(ProductListActivity.INTENT_DATA_FRAGMENTTYPE, ProductListActivity.FRAGMENT_TYPE_PRODUCTLIST_KEYWORDS);
            intent.putExtra(ProductListActivity.SHOP_BRAND_ID, ((ShopBrandResponse.BrandsBean.ItemsBean)serializable).getId());
            intent.putExtra(ProductListKeywordsSearchFragment.FROM_OTHER_PAGE_KEYWORD,((ShopBrandResponse.BrandsBean.ItemsBean)serializable).getIdentifier());
            context.startActivity(intent);
        }
   }

   public static void skipToBrandListPage(Context context,String menuId,String menuTitle){
       Intent intent=new Intent(context, ShopBrandActivity.class);
       intent.putExtra(HomeHomeFragmentV4.ARG_CATEGORY_ID,menuId);
       intent.putExtra(HomeHomeFragmentV4.ARG_CATEGORY_NAME,menuTitle);
       context.startActivity(intent);
   }

}

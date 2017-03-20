package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductSearchFacetsReturnEntity implements Serializable {
    private ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> category_filter;
    private SVRAppserviceProductSearchFacetsPriceReturnEntity price_filter;
    private ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> brand_filter;
    private ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> model_type_filter;
    private ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> sort_filter;

    public ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> getCategory_filter() {
        return category_filter;
    }

    public void setCategory_filter(ArrayList<SVRAppserviceProductSearchFacetsCategoryItemReturnEntity> category_filter) {
        this.category_filter = category_filter;
    }

    public SVRAppserviceProductSearchFacetsPriceReturnEntity getPrice_filter() {
        return price_filter;
    }

    public void setPrice_filter(SVRAppserviceProductSearchFacetsPriceReturnEntity price_filter) {
        this.price_filter = price_filter;
    }

    public ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> getBrand_filter() {
        return brand_filter;
    }

    public void setBrand_filter(ArrayList<SVRAppserviceProductSearchFacetsBrandItemReturnEntity> brand_filter) {
        this.brand_filter = brand_filter;
    }

    public ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> getModel_type_filter() {
        return model_type_filter;
    }

    public void setModel_type_filter(ArrayList<SVRAppserviceProductSearchFacetsModelReturnEntity> model_type_filter) {
        this.model_type_filter = model_type_filter;
    }

    public ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> getSort_filter() {
        return sort_filter;
    }

    public void setSort_filter(ArrayList<SVRAppserviceProductSearchFacetsSortItemReturnEntity> sort_filter) {
        this.sort_filter = sort_filter;
    }
}

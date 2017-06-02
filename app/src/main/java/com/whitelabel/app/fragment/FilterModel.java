package com.whitelabel.app.fragment;

import com.whitelabel.app.model.FilterItemModel;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsPriceReturnEntity;
import com.whitelabel.app.model.SVRAppserviceProductSearchFacetsSortItemReturnEntity;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */
public class FilterModel implements Serializable {
    private List<FilterItemModel> filterList;
    private List<SVRAppserviceProductSearchFacetsSortItemReturnEntity> sort_filter;
    private SVRAppserviceProductSearchFacetsPriceReturnEntity  price_filter;
    public List<FilterItemModel> getFilterList() {
        return filterList;
    }
    public void setFilterList(List<FilterItemModel> filterList) {
        this.filterList = filterList;
    }
    public List<SVRAppserviceProductSearchFacetsSortItemReturnEntity> getSort_filter() {
        return sort_filter;
    }
    public void setSort_filter(List<SVRAppserviceProductSearchFacetsSortItemReturnEntity> sort_filter) {
        this.sort_filter = sort_filter;
    }
    public SVRAppserviceProductSearchFacetsPriceReturnEntity getPrice_filter() {
        return price_filter;
    }
    public void setPrice_filter(SVRAppserviceProductSearchFacetsPriceReturnEntity price_filter) {
        this.price_filter = price_filter;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/8/5.
 */
public class TMPProductListFilterSortPageEntity implements Serializable {
    private int previousFragmentType;
    private int categoryFragmentPosition;
    private SVRAppserviceProductSearchFacetsReturnEntity facets;

    public int getPreviousFragmentType() {
        return previousFragmentType;
    }

    public void setPreviousFragmentType(int previousFragmentType) {
        this.previousFragmentType = previousFragmentType;
    }

    public int getCategoryFragmentPosition() {
        return categoryFragmentPosition;
    }

    public void setCategoryFragmentPosition(int categoryFragmentPosition) {
        this.categoryFragmentPosition = categoryFragmentPosition;
    }

    public SVRAppserviceProductSearchFacetsReturnEntity getFacets() {
        return facets;
    }

    public void setFacets(SVRAppserviceProductSearchFacetsReturnEntity facets) {
        this.facets = facets;
    }
}

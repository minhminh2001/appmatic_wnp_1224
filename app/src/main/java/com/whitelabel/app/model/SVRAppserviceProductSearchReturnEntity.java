package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductSearchReturnEntity extends SVRReturnEntity {
    private long status;
    private long total;
    private ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> results;
    private SVRAppserviceProductSearchFacetsReturnEntity facets;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> getResults() {
        return results;
    }

    public void setResults(ArrayList<SVRAppserviceProductSearchResultsItemReturnEntity> results) {
        this.results = results;
    }

    public SVRAppserviceProductSearchFacetsReturnEntity getFacets() {
        return facets;
    }

    public void setFacets(SVRAppserviceProductSearchFacetsReturnEntity facets) {
        this.facets = facets;
    }
}

package com.whitelabel.app.model;

import java.util.ArrayList;

public class SVRAppserviceProductMerchantReturnEntity extends SVRReturnEntity {
    /**
     * {
     "vendor_name": "GTRONICS",
     "banner": "",
     "description": "",
     "total": 219,
     "results": [
     "facets": {
     "status": 1
     }
     */
    private String vendor_name;
    private String banner;
    private String description;
    private long total;
    private ArrayList<SVRAppserviceProductItemReturnEntity> results;
    private SVRAppserviceProductSearchFacetsReturnEntity facets;
    private long status;

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ArrayList<SVRAppserviceProductItemReturnEntity> getResults() {
        return results;
    }

    public void setResults(ArrayList<SVRAppserviceProductItemReturnEntity> results) {
        this.results = results;
    }

    public SVRAppserviceProductSearchFacetsReturnEntity getFacets() {
        return facets;
    }

    public void setFacets(SVRAppserviceProductSearchFacetsReturnEntity facets) {
        this.facets = facets;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

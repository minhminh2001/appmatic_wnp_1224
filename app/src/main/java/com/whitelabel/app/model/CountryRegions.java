package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/8.
 */
public class CountryRegions extends SVRReturnEntity {
    private String region_id;
    private String name;

    public CountryRegions() {

    }

    public CountryRegions(String id, String name) {
        this.region_id = id;
        this.name = name;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/9.
 */
public class SVRGetCityANdStateByPostCodeEntity extends SVRReturnEntity {
    private int status;
    private String city;
    private String regionId;
    private String regionName;

    @Override
    public String toString() {
        return "status:"+status+"  city:"+city+"  regionName:"+regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

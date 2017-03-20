package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/21.
 */
public class SVRAppserviceLandingPagesDetailReturnEntity extends SVRReturnEntity {
    private String wtfShowClock;
    private String wtfEndtime;
    private String wtfStaticApp;
    private String title;
    private String image;
    private ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productList;
    private SVRAppserviceProductSearchFacetsReturnEntity facets;
    private long status;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private String heading;
    private String badgeValue;

    public SVRAppserviceProductSearchFacetsReturnEntity getFacets() {
        return facets;
    }

    public void setFacets(SVRAppserviceProductSearchFacetsReturnEntity facets) {
        this.facets = facets;
    }

    private String badgeType;
    private String description;

    public String getWtfShowClock() {
        return wtfShowClock;
    }

    public void setWtfShowClock(String wtfShowClock) {
        this.wtfShowClock = wtfShowClock;
    }

    public String getWtfEndtime() {
        return wtfEndtime;
    }

    public void setWtfEndtime(String wtfEndtime) {
        this.wtfEndtime = wtfEndtime;
    }

    public String getWtfStaticApp() {
        return wtfStaticApp;
    }

    public void setWtfStaticApp(String wtfStaticApp) {
        this.wtfStaticApp = wtfStaticApp;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBadgeValue() {
        return badgeValue;
    }

    public void setBadgeValue(String badgeValue) {
        this.badgeValue = badgeValue;
    }

    public String getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(String badgeType) {
        this.badgeType = badgeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<SVRAppserviceLandingPagesDetailProductListItemReturnEntity> productList) {
        this.productList = productList;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

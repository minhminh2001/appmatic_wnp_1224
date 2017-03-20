package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2015/11/10.
 */
public class NotificationCategory implements Serializable {
    private String firstCategory;
    private String secondCategory;
    private String  thirdCategory;
    private SVRAppserviceCatalogSearchCategoryItemReturnEntity androidTree;




    public SVRAppserviceCatalogSearchCategoryItemReturnEntity getAndroidTree() {
        return androidTree;
    }

    public void setAndroidTree(SVRAppserviceCatalogSearchCategoryItemReturnEntity androidTree) {
        this.androidTree = androidTree;
    }

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public String getThirdCategory() {
        return thirdCategory;
    }

    public void setThirdCategory(String thirdCategory) {
        this.thirdCategory = thirdCategory;
    }
}

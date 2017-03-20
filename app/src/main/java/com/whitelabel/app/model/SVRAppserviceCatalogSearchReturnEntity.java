package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceCatalogSearchReturnEntity extends SVRReturnEntity {
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> category;
    private long status;

    public ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> category) {
        this.category = category;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

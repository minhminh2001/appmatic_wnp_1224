package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductRecommendedReturnEntity extends SVRReturnEntity {
    private long status;
    private ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> results;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }


    public ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> getResults() {
        return results;
    }

    public void setResults(ArrayList<SVRAppserviceProductRecommendedResultsItemReturnEntity> results) {
        this.results = results;
    }

}

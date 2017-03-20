package com.whitelabel.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/22.
 */
public class SVRAppserviceLandingPagesListReturnEntity extends SVRReturnEntity {
    private List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> landingList;
    private long status;

    public List<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> getLandingList() {
        return landingList;
    }

    public void setLandingList(ArrayList<SVRAppserviceLandingPagesListLandingPageItemReturnEntity> landingList) {
        this.landingList = landingList;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

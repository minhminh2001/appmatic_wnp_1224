package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/10.
 */
public class SVRAppserviceCustomerUploadHeadImageAndroidReturnEntity extends SVRReturnEntity {
    private String headImage;
    private long status;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

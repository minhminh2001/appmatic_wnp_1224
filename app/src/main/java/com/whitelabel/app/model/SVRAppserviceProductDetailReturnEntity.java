package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductDetailReturnEntity extends SVRReturnEntity {
    private int status;
    private SVRAppserviceProductDetailResultReturnEntity result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SVRAppserviceProductDetailResultReturnEntity getResult() {
        return result;
    }

    public void setResult(SVRAppserviceProductDetailResultReturnEntity result) {
        this.result = result;
    }


}

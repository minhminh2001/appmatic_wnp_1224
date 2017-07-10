package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductDetailReturnEntity extends SVRReturnEntity {
    private int status;
    private ProductDetailModel result;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProductDetailModel getResult() {
        return result;
    }

    public void setResult(ProductDetailModel result) {
        this.result = result;
    }


}

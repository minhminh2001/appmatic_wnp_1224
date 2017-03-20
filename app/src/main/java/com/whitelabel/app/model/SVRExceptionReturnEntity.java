package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/6/11.
 */
public class SVRExceptionReturnEntity extends SVRReturnEntity {
    private String errorMessage;
    private long status;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }
}

package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/9.
 */
public class AddToCartEntity extends SVRReturnEntity {
    private int summaryQty;
    private int status;
    private int Success;

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
    }

    public int getSummaryQty() {
        return summaryQty;
    }

    public void setSummaryQty(int summaryQty) {
        this.summaryQty = summaryQty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

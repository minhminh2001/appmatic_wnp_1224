package com.whitelabel.app.data.model;

import com.whitelabel.app.model.ResponseModel;

/**
 * Created by Ray on 2018/3/14.
 */

public class MergeBatchResponse extends ResponseModel {

    private String summaryQty;

    private String subTotal;

    private String grandTotal;

    public String getSummaryQty() {
        return summaryQty;
    }

    public void setSummaryQty(String summaryQty) {
        this.summaryQty = summaryQty;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }
}

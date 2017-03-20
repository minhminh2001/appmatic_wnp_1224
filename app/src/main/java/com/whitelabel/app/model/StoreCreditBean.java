package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class StoreCreditBean implements Serializable {
    private String amount;
    private String amountToRM;
    private String CMScontent;
    private String status;
    private String CMScontentTitle;
    private List<StoreCreditItemBean> history;
    private String isEnabled;
    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<StoreCreditItemBean> getHistory() {
        return history;
    }

    public void setHistory(List<StoreCreditItemBean> history) {
        this.history = history;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountToRM() {
        return amountToRM;
    }

    public void setAmountToRM(String amountToRM) {
        this.amountToRM = amountToRM;
    }

    public String getCMScontent() {
        return CMScontent;
    }

    public void setCMScontent(String CMScontent) {
        this.CMScontent = CMScontent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCMScontentTitle() {
        return CMScontentTitle;
    }

    public void setCMScontentTitle(String CMScontentTitle) {
        this.CMScontentTitle = CMScontentTitle;
    }
}

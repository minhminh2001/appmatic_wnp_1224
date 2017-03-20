package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class SVRAppserviceSaveOrderReturnEntity extends SVRReturnEntity {

    private int status;

    private String lastrealorderid;
    private String cashondeliveryContent;
    private String vcode;
    private String amount;
    private String paymentUrl;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getCashondeliveryContent() {
        return cashondeliveryContent;
    }

    public void setCashondeliveryContent(String cashondeliveryContent) {
        this.cashondeliveryContent = cashondeliveryContent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastrealorderid() {
        return lastrealorderid;
    }

    public void setLastrealorderid(String lastrealorderid) {
        this.lastrealorderid = lastrealorderid;
    }

}

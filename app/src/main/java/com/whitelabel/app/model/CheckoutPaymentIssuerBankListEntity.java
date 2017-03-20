package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/14.
 */
public class CheckoutPaymentIssuerBankListEntity extends SVRReturnEntity {

    private int status;

    private String [] banks;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getBanks() {
        return banks;
    }

    public void setBanks(String[] bank) {
        this.banks = bank;
    }
}

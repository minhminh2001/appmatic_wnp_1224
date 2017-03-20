package com.whitelabel.app.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/24.
 */
public class CheckoutGetPaymentListEntity extends SVRReturnEntity {

    private int status;
    private String paymentlist_image;
    private HashMap<String, String> cctype;
    private String[] banks;
    private String[] methods;
    private HashMap<String, String> onlinebanks;
    private BanktransferBean banktransfer;
    private String braintreetoken;
    private CashondeLiveryBean cashondelivery;


    public String getPaymentlist_image() {
        return paymentlist_image;
    }

    public void setPaymentlist_image(String paymentlist_image) {
        this.paymentlist_image = paymentlist_image;
    }


    public String getBraintreetoken() {
        return braintreetoken;
    }

    public void setBraintreetoken(String braintreetoken) {
        this.braintreetoken = braintreetoken;
    }

    public CashondeLiveryBean getCashondelivery() {
        return cashondelivery;
    }

    public void setCashondelivery(CashondeLiveryBean cashondelivery) {
        this.cashondelivery = cashondelivery;
    }

    public BanktransferBean getBanktransfer() {
        return banktransfer;
    }

    public void setBanktransfer(BanktransferBean banktransfer) {
        this.banktransfer = banktransfer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getCctype() {
        return cctype;
    }

    public void setCctype(HashMap<String, String> cctype) {
        this.cctype = cctype;
    }

    public String[] getBanks() {
        return banks;
    }

    public void setBanks(String[] banks) {
        this.banks = banks;
    }

    public String[] getMethods() {
        return methods;
    }

    public void setMethods(String[] methods) {
        this.methods = methods;
    }

    public HashMap<String, String> getOnlinebanks() {
        return onlinebanks;
    }

    public void setOnlinebanks(HashMap<String, String> onlinebanks) {
        this.onlinebanks = onlinebanks;
    }
}

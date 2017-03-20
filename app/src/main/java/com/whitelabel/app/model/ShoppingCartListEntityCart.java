package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ShoppingCartListEntityCart implements Serializable {

    private int summaryQty;
    private int canUseCampaign;//1 campaign ; 0 no campaign
    private String subTotal;
    private String grandTotal;
    private String popupText;
    public ShoppingCartListEntityCell [] items;
    private ShoppingDiscountBean discount;
    private ShoppingDiscountBean shipping;
    private String androidCampBanner;
    private ShoppingCarStoreCreditBean storeCreditMessage;
    private ShoppingCarStoreCreditBean storeCredit;
    private String gst;

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public ShoppingCarStoreCreditBean getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(ShoppingCarStoreCreditBean storeCredit) {
        this.storeCredit = storeCredit;
    }

    public ShoppingCarStoreCreditBean getStoreCreditMessage() {
        return storeCreditMessage;
    }

    public void setStoreCreditMessage(ShoppingCarStoreCreditBean storeCreditMessage) {
        this.storeCreditMessage = storeCreditMessage;
    }

    public String getPopupText() {
        return popupText;
    }

    public void setPopupText(String popupText) {
        this.popupText = popupText;
    }

    public int getCanUseCampaign() {
        return canUseCampaign;
    }

    public void setCanUseCampaign(int canUseCampaign) {
        this.canUseCampaign = canUseCampaign;
    }

    public ShoppingDiscountBean getShipping() {
        return shipping;
    }

    public void setShipping(ShoppingDiscountBean shipping) {
        this.shipping = shipping;
    }

    public ShoppingDiscountBean getDiscount() {
        return discount;
    }

    public void setDiscount(ShoppingDiscountBean discount) {
        this.discount = discount;
    }

    public int getSummaryQty() {
        return summaryQty;
    }

    public void setSummaryQty(int summaryQty) {
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

    public ShoppingCartListEntityCell[] getItems() {
        return items;
    }

    public void setItems(ShoppingCartListEntityCell[] items) {
        this.items = items;
    }

    public String getAndroidCampBanner() {
        return androidCampBanner;
    }

    public void setAndroidCampBanner(String androidCampBanner) {
        this.androidCampBanner = androidCampBanner;
    }
}

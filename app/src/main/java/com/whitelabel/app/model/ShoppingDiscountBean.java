package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by pc on 2015/7/23.
 */
public class ShoppingDiscountBean implements Serializable{
    private String title;
    private String value;
    private String couponCode;
    private String stopRulesProcessing;
    private int isShare;  //1 share  0  not share
    private String shareImage;
    private String  shareLink;
    private String shareTitle;
    private String caption;
    private String shareDescription;

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public String getStopRulesProcessing() {
        return stopRulesProcessing;
    }

    public void setStopRulesProcessing(String stopRulesProcessing) {
        this.stopRulesProcessing = stopRulesProcessing;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

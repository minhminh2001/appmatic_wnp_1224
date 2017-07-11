package com.whitelabel.app.model;


/**
 * Created by imaginato on 2015/7/7.
 */
public class WishDelEntityResult extends SVRReturnEntity {
    private Object params;
    private int status;
    private int wishListItemCount=0;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWishListItemCount() {
        return wishListItemCount;
    }

    public void setWishListItemCount(int wishListItemCount) {
        this.wishListItemCount = wishListItemCount;
    }
}

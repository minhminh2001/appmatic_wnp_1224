package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/14.

 */
public class AddToWishlistEntity extends SVRReturnEntity {
    private int Success;
    private int status;
    private int wishListItemCount;
    private String itemId;
    private Object params;
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

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}

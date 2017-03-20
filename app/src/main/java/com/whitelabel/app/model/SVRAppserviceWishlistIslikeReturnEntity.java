package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/31.
 */
public class SVRAppserviceWishlistIslikeReturnEntity extends SVRReturnEntity {
    private int isLike;
    private int status;

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

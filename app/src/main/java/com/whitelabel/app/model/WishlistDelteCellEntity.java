package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/7/9.
 */
public class WishlistDelteCellEntity extends SVRReturnEntity{
    private int wishListItemCount;
    private int status;
    public int getWishListItemCount() {
        return wishListItemCount;
    }

    public void setWishListItemCount(int wishListItemCount) {
        this.wishListItemCount = wishListItemCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}

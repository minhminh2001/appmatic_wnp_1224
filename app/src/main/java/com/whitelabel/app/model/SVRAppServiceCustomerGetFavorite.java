package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by imaginato on 2015/9/11.
 */
public class SVRAppServiceCustomerGetFavorite extends SVRReturnEntity {
    private int status;
    private int hasFavorite;
    private ArrayList<FavoriteDate> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(int hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public ArrayList<FavoriteDate> getList() {
        return list;
    }

    public void setList(ArrayList<FavoriteDate> list) {
        this.list = list;
    }
}

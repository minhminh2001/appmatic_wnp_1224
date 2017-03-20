package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by imaginato on 2015/8/19.
 */
public class FavoriteEntity extends SVRReturnEntity {
    private int status;
    private List<FavoriteSonEntity> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FavoriteSonEntity> getList() {
        return list;
    }

    public void setList(List<FavoriteSonEntity> list) {
        this.list = list;
    }
}

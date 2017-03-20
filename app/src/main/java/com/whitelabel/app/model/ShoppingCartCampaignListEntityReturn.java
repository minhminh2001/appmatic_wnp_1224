package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ShoppingCartCampaignListEntityReturn extends SVRReturnEntity {

    private int status;
    private ArrayList<ShoppingCartCampaignListEntity> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<ShoppingCartCampaignListEntity> getList() {
        return list;
    }

    public void setList(ArrayList<ShoppingCartCampaignListEntity> list) {
        this.list = list;
    }
}

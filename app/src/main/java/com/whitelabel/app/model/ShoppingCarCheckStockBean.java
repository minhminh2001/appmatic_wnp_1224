package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by ray on 2015/7/27.
 */
public class ShoppingCarCheckStockBean extends  SVRReturnEntity{
    private int status;
    private List<String> ids;
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public int getStatus() {return status;}

        public void setStatus(int status) {this.status = status;}

}

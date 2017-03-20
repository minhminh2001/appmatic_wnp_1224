package com.whitelabel.app.model;

import java.util.ArrayList;

/**
 * Created by jay on 2017/2/6.
 */

public class SearchSuggestionBean {
    /**
     * {"suggests":
     [{
     "title":"suggest title",
     "row_type":"row type(brand,category,product,)",
     "id": suggest id
     }]
     }
     */
    private int status;
    private ArrayList<SuggestsEntity> suggests;

    public ArrayList<SuggestsEntity> getList() {
        return suggests;
    }

    public void setList(ArrayList<SuggestsEntity> list) {
        this.suggests = list;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}

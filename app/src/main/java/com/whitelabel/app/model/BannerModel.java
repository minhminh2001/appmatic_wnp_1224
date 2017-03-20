package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/1.
 */
//"name":null,"url":"imaginato\/brand\/5\/8\/58b677b13f11b.jpg","is_link":"0","sort_order":"0"
public class BannerModel  implements Serializable{
    private String name;
    private String url;
    private String is_link;
    private int sort_order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_link() {
        return is_link;
    }

    public void setIs_link(String is_link) {
        this.is_link = is_link;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }
}

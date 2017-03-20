package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductSearchFacetsPriceReturnEntity implements Serializable {
    private long min_price;
    private long max_price;
    private long from;
    private long to;

    public long getMin_price() {
        return min_price;
    }

    public void setMin_price(long min_price) {
        this.min_price = min_price;
    }

    public long getMax_price() {
        return max_price;
    }

    public void setMax_price(long max_price) {
        this.max_price = max_price;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }
}

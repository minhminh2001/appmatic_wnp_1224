package com.whitelabel.app.model;

import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 */
public class MyAccountOrderListEntityResult extends SVRReturnEntity {

    private int status;

    private int total;

    private List<MyAccountOrderOuter> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MyAccountOrderOuter> getResults() {
        return results;
    }

    public void setResults(List<MyAccountOrderOuter> results) {
        this.results = results;
    }
}

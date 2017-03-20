package com.whitelabel.app.model;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/7.
 */
public class WishlistEntityResult extends SVRReturnEntity {
    private int success;
    private int status;
    private String total;
    private List<Wishlist> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Wishlist> getResults() {
        return results;
    }

    public void setResults(List<Wishlist> results) {
        this.results = results;
    }

    public void setResults(LinkedList<Wishlist> results) {
        this.results = results;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }



    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }



}

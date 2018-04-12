package com.whitelabel.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2018/4/8.
 */

public class RecentSearchKeywordResponse {

    private List<String> keywords = new ArrayList<String>();
    private int status;
    private String result;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

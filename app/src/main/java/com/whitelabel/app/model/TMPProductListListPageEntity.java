package com.whitelabel.app.model;

import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/8/7.
 */
public class TMPProductListListPageEntity implements Serializable {
    private int searchType;

    public TMPProductListListPageEntity() {
        searchType = ProductListKeywordsSearchFragment.SEARCH_TYPE_KEYWORDS;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
}

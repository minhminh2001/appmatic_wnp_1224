package com.whitelabel.app.model;

import com.whitelabel.app.fragment.ProductListKeywordsSearchFragment;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/8/7.
 */
public class TMPProductListListPageEntity implements Serializable {
    private int searchType;
    private String categoryId;
    private String keyWord;
    private String brandId;

    public TMPProductListListPageEntity() {
        searchType = ProductListKeywordsSearchFragment.SEARCH_TYPE_KEYWORDS;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}

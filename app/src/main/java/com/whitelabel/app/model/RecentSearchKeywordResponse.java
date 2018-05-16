package com.whitelabel.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2018/4/8.
 */

public class RecentSearchKeywordResponse {

    private List<Keyword> keywords = new ArrayList<Keyword>();
    private int status;
    private String result;

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
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

    public class Keyword{
        @SerializedName("word")
        private String keyword;
        @SerializedName("time")
        private String timeStamp;
        @SerializedName("type")
        private int type;
        @SerializedName("category_id")
        private String categroyId;
        @SerializedName("brand_id")
        private String brandId;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCategroyId() {
            return categroyId;
        }

        public void setCategroyId(String categroyId) {
            this.categroyId = categroyId;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }
    }
}

package com.whitelabel.app.model;

import com.google.gson.annotations.SerializedName;

public class StoreInfo {
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("code")
    private String code;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("is_active")
    private String isActive;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}

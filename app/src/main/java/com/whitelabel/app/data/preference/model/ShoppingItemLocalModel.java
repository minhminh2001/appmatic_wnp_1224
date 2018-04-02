package com.whitelabel.app.data.preference.model;

/**
 * Created by Ray on 2018/3/12.
 */

public class ShoppingItemLocalModel {

    private String groupId;

    private String simpleId;

    private String number;

    public ShoppingItemLocalModel(String groupId, String simpleId, String number) {
        this.groupId = groupId;
        this.simpleId = simpleId;
        this.number = number;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSimpleId() {
        return simpleId;
    }

    public void setSimpleId(String simpleId) {
        this.simpleId = simpleId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/27.
 */
public class TMPHelpCenterListToDetailEntity implements Serializable {
    private int helpCenterType;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHelpCenterType() {
        return helpCenterType;
    }

    public void setHelpCenterType(int helpCenterType) {
        this.helpCenterType = helpCenterType;
    }
}

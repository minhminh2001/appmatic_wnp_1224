package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by ray on 2015/11/26.
 */
public class DialogProductBean implements Serializable{
    private String imageUrl;
    private String name;
    public DialogProductBean(){

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }
}

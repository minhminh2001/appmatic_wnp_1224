package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/9/11.
 */
public class FavoriteDate implements Serializable{
    private String categoryId;
    private String  categoryName;
    private String  image;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

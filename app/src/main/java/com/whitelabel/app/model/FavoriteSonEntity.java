package com.whitelabel.app.model;

/**
 * Created by imaginato on 2015/8/20.
 */
public class FavoriteSonEntity extends SVRReturnEntity  {
    private String categoryId;
    private String categoryName;
    private String image;
    private String iconImage;
    private boolean selected;

    public boolean isSelected() {return selected;}

    public void setSelected(boolean selected) {this.selected = selected;}

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIconImage() {return iconImage;}

    public void setIconImage(String iconImage) {this.iconImage = iconImage;}
}

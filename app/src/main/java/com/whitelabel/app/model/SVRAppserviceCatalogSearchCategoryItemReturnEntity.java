package com.whitelabel.app.model;

import com.google.gson.annotations.SerializedName;
import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceCatalogSearchCategoryItemReturnEntity extends ExpandableRecyclerAdapter.ListItem implements Serializable {
    private String id;
    private String brandId;
    @SerializedName("menu_id")
    private String menuId;
    @SerializedName("menu_type")
    private String menuType;
    @SerializedName("menu_title")
    private String menuTitle;
    private int level;
    //all name replace menu_title
    private String name;
    private String url;
    private String image;
    private String brandName;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> children=new ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity>();
    private int inStock;
    private int image_width;
    private int image_height;

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return menuTitle;
    }

    public void setName(String name) {
        this.menuTitle = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> children) {
        this.children = children;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
}

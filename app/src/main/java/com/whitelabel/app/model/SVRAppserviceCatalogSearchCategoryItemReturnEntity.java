package com.whitelabel.app.model;

import com.whitelabel.app.widget.ExpandableRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceCatalogSearchCategoryItemReturnEntity extends ExpandableRecyclerAdapter.ListItem implements Serializable {
    private String id;
    private String brandId;
    private String menu_id;
    private String menu_type;
    private String menu_title;
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

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getMenu_title() {
        return menu_title;
    }

    public void setMenu_title(String menu_title) {
        this.menu_title = menu_title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return menu_title;
    }

    public void setName(String name) {
        this.menu_title = name;
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

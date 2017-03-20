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
    private int level;
    private String name;
    private String url;
    private String image;
    private String brandName;
    private ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity> children=new ArrayList<SVRAppserviceCatalogSearchCategoryItemReturnEntity>();
    private int inStock;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

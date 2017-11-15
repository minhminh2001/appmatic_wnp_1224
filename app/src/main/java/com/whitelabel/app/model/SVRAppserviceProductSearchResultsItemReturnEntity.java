package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductSearchResultsItemReturnEntity implements Serializable {
    private String productId;
    private String name;
    private String category;
    private String brand;
    private int position;
    //header title item
    private int type;
    private String brandId;
    private String price;
    private String final_price;
    private String smallImage;
    private String inStock;
    private String vendorDisplayName;
    private int isLike;
    private String item_id;
    private String maxSaleQty;
    private boolean syncnServering=false;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public boolean isSyncnServering() {
        return syncnServering;
    }

    public String getItem_id() {
        return item_id;
    }
    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
    public boolean getSyncnServering() {
        return syncnServering;
    }

    public void setSyncnServering(boolean syncnServering) {
        this.syncnServering = syncnServering;
    }
    private String vendor_id;

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendorDisplayName() {
        return vendorDisplayName;
    }

    public void setVendorDisplayName(String vendorDisplayName) {
        this.vendorDisplayName = vendorDisplayName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(String maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    @Override
    public String toString() {
        return "SVRAppserviceProductSearchResultsItemReturnEntity{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", position=" + position +
                ", brandId='" + brandId + '\'' +
                ", price='" + price + '\'' +
                ", final_price='" + final_price + '\'' +
                ", smallImage='" + smallImage + '\'' +
                ", inStock='" + inStock + '\'' +
                ", vendorDisplayName='" + vendorDisplayName + '\'' +
                ", isLike=" + isLike +
                ", item_id='" + item_id + '\'' +
                ", maxSaleQty='" + maxSaleQty + '\'' +
                ", syncnServering=" + syncnServering +
                ", itemId='" + itemId + '\'' +
                ", vendor_id='" + vendor_id + '\'' +
                '}';
    }
}

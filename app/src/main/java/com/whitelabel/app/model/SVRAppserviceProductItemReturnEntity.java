package com.whitelabel.app.model;

import java.io.Serializable;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductItemReturnEntity implements Serializable {
    /**
     *  {
     "productId": "171980",
     "name": "Huawei Honor 5C 16GB - Silver",
     "brand": "Huawei",
     "inStock": 1,
     "type": "simple",
     "price": "799.00",
     "finalPrice": "779.00",
     "smallImage": "catalog/product/569/h/o/honor5c-silver-(1).jpg",
     "isLike": 0,
     "itemId": 0,
     "availability": 0,
     "visibility": 1
     }
     */
    private String productId;
    private String name;
    private String brand;
    private int inStock;
    private String type;
    private String price;
    private String finalPrice;
    private String smallImage;
    private int isLike;
    private String itemId;
    private int availability;
    private int visibility;
    private String brandId;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}

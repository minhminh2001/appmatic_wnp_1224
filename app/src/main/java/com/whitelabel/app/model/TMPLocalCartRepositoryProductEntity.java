package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/27.
 */
public class TMPLocalCartRepositoryProductEntity implements Serializable {
    private String productId;
    private String image;
    private String name;
    private String brandId;
    private String brand;
    private String category;
    private int inStock; // 1 -> in stock; 0 -> out of stock
    private long qty;
    private String type; // "configurable" or "simple"
    private String price;
    private String finalPrice;
    private ArrayList<TMPLocalCartRepositoryProductOptionEntity> options; // 0 -> Color, 1 -> Size
    private long selectedQty;
    private String canViewPdp;
    private String availability;
    private String maxSaleQty;
    private String vendorDisplayName;
    private String visibility;
    private String vendor_id;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(String maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCanViewPdp() {
        return canViewPdp;
    }

    public void setCanViewPdp(String canViewPdp) {
        this.canViewPdp = canViewPdp;
    }


    public String getVendorDisplayName() {
        return vendorDisplayName;
    }

    public void setVendorDisplayName(String vendorDisplayName) {
        this.vendorDisplayName = vendorDisplayName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
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

    public ArrayList<TMPLocalCartRepositoryProductOptionEntity> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<TMPLocalCartRepositoryProductOptionEntity> options) {
        this.options = options;
    }

    public long getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(long selectedQty) {
        this.selectedQty = selectedQty;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * MyAccountOrder: Inner datas
 */
public class MyAccountOrderInner  implements Serializable {

    private String productId;
    private String itemId;
    private String category;
    private String brand;
    private String name;
    private String qty;
    private String price;
    private String image;
    private String canViewPdp;
    private String availability;
    private String visibility;
    private String vendorDisplayName;
    private String vendor_id;
    private String brandId;

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

    //a map of color and size,format with Json:[{"label":"Color","value":"Pink"},{"label":"Size","value":"S"}]


    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCanViewPdp() {
        return canViewPdp;
    }

    public void setCanViewPdp(String canViewPdp) {
        this.canViewPdp = canViewPdp;
    }

    private ArrayList<HashMap<String,String>> options;

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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<HashMap<String, String>> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<HashMap<String, String>> options) {
        this.options = options;
    }
}

package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ShoppingCartListEntityCell extends ShoppingCartListBase implements Serializable {

    private ArrayList<HashMap<String, String>> localOptions;

    private String id;

    private String productId;

    private String name;

    private String category;

    private String firstCategory;

    private String qty;

    private String oldQty;

    private String price;

    private String image;

    private String brand;

    private String brandId;

    private int stockQty;

    private String currStockQty;

    private String maxSaleQty;

    private String maxQty;

    private String finalPrice;

    private String inStock;

    private int isCampaignProduct;//1 means promotion campaign product , otherwise no.

    private String canViewPdp;

    private String availability;

    private String visibility;

    private String vendorDisplayName;

    private String vendor_id;

    private String type;

    private String has_error;

    private String error_message;

    private String simpleId;

    private ArrayList<HashMap<String, String>> options;//Color and Size,adapter to

    public String getSimpleId() {
        return simpleId;
    }

    public void setSimpleId(String simpleId) {
        this.simpleId = simpleId;
    }

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

    public ArrayList<HashMap<String, String>> getLocalOptions() {
        return localOptions;
    }

    public void setLocalOptions(ArrayList<HashMap<String, String>> localOptions) {
        this.localOptions = localOptions;
    }

    public String getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(String maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    public String getCurrStockQty() {
        return currStockQty;
    }

    public void setCurrStockQty(String currStockQty) {
        this.currStockQty = currStockQty;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

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

    public String getVendorDisplayName() {
        return vendorDisplayName;
    }

    public void setVendorDisplayName(String vendorDisplayName) {
        this.vendorDisplayName = vendorDisplayName;
    }

    public String getFirstCategory() {
        return firstCategory;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public String getCanViewPdp() {
        return canViewPdp;
    }

    public void setCanViewPdp(String canViewPdp) {
        this.canViewPdp = canViewPdp;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getBrand() {
        return brand;
    }

//    public boolean isStock() {
//        return isStock;
//    }
//
//    public void setIsStock(boolean isStock) {
//        this.isStock = isStock;
//    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    // ShoppingCartListEntityCellColorAndSize

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        setOldQty(this.qty);
        this.qty = qty;
    }

    public String getOldQty() {
        return this.oldQty;
    }

    public void setOldQty(String oldQty) {
        this.oldQty = oldQty;
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

    public int getIsCampaignProduct() {
        return isCampaignProduct;
    }

    public void setIsCampaignProduct(int isCampaignProduct) {
        this.isCampaignProduct = isCampaignProduct;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHasError() {
        return has_error;
    }

    public void setHasError(String has_error) {
        this.has_error = has_error;
    }

    public String getErrorMessage() {
        return error_message;
    }

    public void setErrorMessage(String error_message) {
        this.error_message = error_message;
    }
}

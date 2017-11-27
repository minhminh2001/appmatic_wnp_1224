package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imaginato on 2015/7/8.
 */
public class ProductDetailModel implements Serializable {
    public static final String TYPE_CONFIGURABLE = "configurable";
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_GROUP="grouped";
    private String id;
    private String name;
    private String brand;
    private String brandId;
    public static String getTypeSimple() {
        return TYPE_SIMPLE;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public static String getTypeConfigurable() {
        return TYPE_CONFIGURABLE;
    }
    private String category;
    private long stockQty;
    private long maxSaleQty;
    private int eligibleForCod;
    private ShippingInfoBean shippingInfo;
    private String visibility;
    private int inStock; // 1 -> in stock; 0 -> out of stock
    private String type; // "configurable" or "simple"
    private String price;
    private String finalPrice;
    private ArrayList<String> images;
    private String description;
    private ArrayList<SVRAppserviceProductDetailResultDetailReturnEntity> detail;
    private int isLike;
    private String itemId;
    private ArrayList<ProductPropertyModel> property;
    private String url;
    private String canViewPdp;
    private String availability;
    private String saveRm;
    private String itemsLeft;
    private String vendorDisplayName;
    private String vendor_id;
    private String uiDetailHtmlText;
    private  String ingredients;
    private String features;
    private int image_width;
    private int image_height;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getUiDetailHtmlText() {
        return uiDetailHtmlText;
    }
    public void setUiDetailHtmlText(String uiDetailHtmlText) {
        this.uiDetailHtmlText = uiDetailHtmlText;
    }
    public static String getTypeGroup() {
        return TYPE_GROUP;
    }

    private ArrayList<SVRAppserviceProductDetailResultProductDimensionReturnEntity> productDimension;
    private List<ProductPropertyModel> relatedProducts;
    public List<ProductPropertyModel> getRelatedProducts() {
        return relatedProducts;
    }
    public void setRelatedProducts(List<ProductPropertyModel> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
    public ArrayList<SVRAppserviceProductDetailResultProductDimensionReturnEntity> getProductDimension() {
        return productDimension;
    }
    public void setProductDimension(ArrayList<SVRAppserviceProductDetailResultProductDimensionReturnEntity> productDimension) {
        this.productDimension = productDimension;
    }
    public String getSaveRm() {
        return saveRm;
    }

    public void setSaveRm(String saveRm) {
        this.saveRm = saveRm;
    }

    public String getItemsLeft() {
        return itemsLeft;
    }

    public void setItemsLeft(String itemsLeft) {
        this.itemsLeft = itemsLeft;
    }

    public long getMaxSaleQty() {
        return maxSaleQty;
    }

    public void setMaxSaleQty(long maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }

    public String getCanViewPdp() {
        return canViewPdp;
    }

    public void setCanViewPdp(String canViewPdp) {
        this.canViewPdp = canViewPdp;
    }


    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
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


    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public ShippingInfoBean getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoBean shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public int getEligibleForCod() {
        return eligibleForCod;
    }

    public void setEligibleForCod(int eligibleForCod) {
        this.eligibleForCod = eligibleForCod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getStockQty() {
        return stockQty;
    }

    public void setStockQty(long stockQty) {
        this.stockQty = stockQty;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<SVRAppserviceProductDetailResultDetailReturnEntity> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<SVRAppserviceProductDetailResultDetailReturnEntity> detail) {
        this.detail = detail;
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

    public ArrayList<ProductPropertyModel> getProperty() {
        return property;
    }

    public void setProperty(ArrayList<ProductPropertyModel> property) {
        this.property = property;
    }

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
}

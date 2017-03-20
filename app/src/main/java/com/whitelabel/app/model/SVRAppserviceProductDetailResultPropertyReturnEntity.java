package com.whitelabel.app.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imaginato on 2015/7/8.
 */
public class SVRAppserviceProductDetailResultPropertyReturnEntity implements Serializable {
    private String id;
    private String label;
    private String price;
    private String superAttribute;
    private String finalPrice;
    private int eligibleForCod;
    private String superLabel;
    private long stockQty;
    private int level;
    private long maxSaleQty;
    private int inStock;
    private ArrayList<String> images;
    private ArrayList<SVRAppserviceProductDetailResultPropertyReturnEntity> child;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getMaxSaleQty() {
        return maxSaleQty;
    }
    private String saveRm;
    private String itemsLeft;

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

    public void setMaxSaleQty(long maxSaleQty) {
        this.maxSaleQty = maxSaleQty;
    }
    public int getEligibleForCod() {
        return eligibleForCod;
    }

    public void setEligibleForCod(int eligibleForCod) {
        this.eligibleForCod = eligibleForCod;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSuperAttribute() {
        return superAttribute;
    }

    public void setSuperAttribute(String superAttribute) {
        this.superAttribute = superAttribute;
    }

    public String getSuperLabel() {
        return superLabel;
    }

    public void setSuperLabel(String superLabel) {
        this.superLabel = superLabel;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<SVRAppserviceProductDetailResultPropertyReturnEntity> getChild() {
        return child;
    }

    public void setChild(ArrayList<SVRAppserviceProductDetailResultPropertyReturnEntity> child) {
        this.child = child;
    }
}

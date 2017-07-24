package com.whitelabel.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ray on 2017/3/31.
 */

public class CategoryDetailModel  implements Serializable {



        /**
         * category_id : 1111
         * category_name : category1
         * category_img : ["image.jpg","icon.jpg"]
         * newArrivalProducts : [{"productId":"243008","name":"Jh 12045 Banquet Table White (70844151) (70844151)","brand":"Tesco","brandId":"10677","inStock":"1","maxSaleQty":10,"price":"84.90","final_price":"55.90","smallImage":"catalog/product/1076/7/0/70844151-(1).jpg","is_like":0,"item_id":0,"vendorDisplayName":"Tesco","vendor_id":"1076"}]
         * bestSellerProducts : [{"productId":"243008","name":"Jh 12045 Banquet Table White (70844151) (70844151)","brand":"Tesco","brandId":"10677","inStock":"1","maxSaleQty":10,"price":"84.90","final_price":"55.90","smallImage":"catalog/product/1076/7/0/70844151-(1).jpg","is_like":0,"item_id":0,"vendorDisplayName":"Tesco","vendor_id":"1076"}]
         */

        private String category_id;
        private String category_name;
        private String category_img;
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

    private List<SVRAppserviceProductSearchResultsItemReturnEntity> newArrivalProducts;
        private List<SVRAppserviceProductSearchResultsItemReturnEntity> bestSellerProducts;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

    public String getCategory_img() {
        return category_img;
    }

    public void setCategory_img(String category_img) {
        this.category_img = category_img;
    }

    public List<SVRAppserviceProductSearchResultsItemReturnEntity> getNewArrivalProducts() {
            return newArrivalProducts;
        }

        public void setNewArrivalProducts(List<SVRAppserviceProductSearchResultsItemReturnEntity> newArrivalProducts) {
            this.newArrivalProducts = newArrivalProducts;
        }

        public List<SVRAppserviceProductSearchResultsItemReturnEntity> getBestSellerProducts() {
            return bestSellerProducts;
        }

        public void setBestSellerProducts(List<SVRAppserviceProductSearchResultsItemReturnEntity> bestSellerProducts) {
            this.bestSellerProducts = bestSellerProducts;
        }
    }

